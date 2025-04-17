package com.muffinmanager.api.muffinmanagerapi.service.Brand;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.controller.ProductData.ProductItemController;
import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.database.BrandEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.dto.BrandDto;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database.ProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.repository.IBrandRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductItemRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.ProductData.ProductItem.IProductItemService;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private IBrandRepository brandRepository;
    @Autowired 
    private IProductItemRepository productItemRepository;
    @Autowired 
    private IProductItemService productItemService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    IJwtService jwtService;

    @Override
    public List<BrandDto> getAll() {
        return StreamSupport.stream(brandRepository.findAll().spliterator(), false)
            .map(BrandEntity::toDto)
            .filter(brandDto -> !brandDto.isObsolete())
            .sorted(Comparator
            .comparing(BrandDto::getReference)
            .thenComparing(BrandDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public BrandDto getById(int id) {
        return brandRepository.findById(id)
            .map(BrandEntity::toDto)
            .orElse(null);
    }

    @Override
    public BrandDto insert(BrandDto entityDto) {
        BrandEntity brandEntity = getEntityByDto(entityDto);
        brandEntity.setVersion(brandRepository.findHighestVersionByReference(entityDto.getReference()).orElse(0) + 1);
        brandEntity.setObsolete(false);
        brandEntity.setEndDate(null);
        BrandEntity savedBrandEntity = brandRepository.save(brandEntity);
        productItemRepository.findByBrandReference(savedBrandEntity.getBrandReference()).ifPresent(items -> {
            
            items.stream()
            .filter(item -> !item.getIsObsolete())
            .forEach(item -> {
            ProductItemEntity itemCloned = item.clone();
            itemCloned.setId(0);
            itemCloned.setBrand(savedBrandEntity);
            itemCloned.setAliasVersion(brandEntity.getAliasVersion());
            productItemService.insert(itemCloned.toDto());
            });

            if (!items.isEmpty()) {
                messagingTemplate.convertAndSend("/topic" + ProductItemController.BASE_URL, "");
            }
        });
        return savedBrandEntity.toDto();
    }

    @Override
    public BrandDto update(BrandDto entityDto) {
        BrandEntity brandEntity = brandRepository.findById(entityDto.getId()).orElse(null);
        if (brandEntity != null) {
            brandEntity.setBrandReference(entityDto.getReference());
            brandEntity.setName(entityDto.getName());
            brandEntity.setLogo(entityDto.getLogoBase64() != null ? Base64.getDecoder().decode(entityDto.getLogoBase64()) : null);
            brandEntity.setAliasVersion(entityDto.getAliasVersion());
            brandEntity.setCreationDate(brandEntity.getCreationDate() != null ? brandEntity.getCreationDate() : Timestamp.valueOf(LocalDateTime.now()));
            brandEntity.setEndDate(entityDto.isObsolete() 
                ? (entityDto.getEndDate() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(entityDto.getEndDate())) 
                : null);
            brandEntity.setObsolete(entityDto.isObsolete());
            brandEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            brandEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return brandRepository.save(brandEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        brandRepository.deleteById(id);
    }

    @Override
    public BrandEntity getEntityByDto(BrandDto dto) {
        return BrandEntity.builder()
            .id(dto.getId())
            .brandReference(dto.getReference())
            .name(dto.getName())
            .logo(dto.getLogoBase64() != null ? Base64.getDecoder().decode(dto.getLogoBase64()) : null)
            .version(dto.getVersion())
            .aliasVersion(dto.getAliasVersion())
            .creationDate(dto.getCreationDate() != null ? Timestamp.valueOf(dto.getCreationDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .endDate(dto.getEndDate() != null ? Timestamp.valueOf(dto.getEndDate()) : null)
            .isObsolete(dto.isObsolete())
            .lastModifyDate(Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    }

    @Override
    public List<BrandDto> getObsoletes() {
        return StreamSupport.stream(brandRepository.findAll().spliterator(), false)
            .map(BrandEntity::toDto)
            .filter(brandDto -> brandDto.isObsolete())
            .sorted(Comparator
            .comparing(BrandDto::getReference)
            .thenComparing(BrandDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByReference(String reference) {
        List<BrandEntity> brandsToDelete = brandRepository.findByBrandReference(reference).orElse(new ArrayList<>()).stream()
            .collect(Collectors.toList());
        brandRepository.deleteAll(brandsToDelete);
    }

    /*@Override
    public List<BrandDto> changeReference(String oldReference, String newReference) {
        List<BrandEntity> brandsToChange = brandRepository.findByBrandReference(oldReference).orElse(new ArrayList<>());
        if (!brandsToChange.isEmpty()) {
            brandsToChange.forEach(brand -> brand.setBrandReference(newReference));
            brandRepository.saveAll(brandsToChange);
        }
        return brandsToChange.stream().map(BrandEntity::toDto).collect(Collectors.toList());
    }*/

    @Override
    public void setObsoleteByReference(String reference, boolean obsolete) {
        brandRepository.findByBrandReference(reference).ifPresent(brands -> {
            List<BrandEntity> brandsToUpdate = brands.stream()
                .filter(brand -> brand.isObsolete() != obsolete)
                .peek(brand -> {
                    brand.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
                    brand.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
                    brand.setObsolete(obsolete);
                    brand.setEndDate(obsolete ? (brand.getEndDate() != null ? brand.getEndDate() : Timestamp.valueOf(LocalDateTime.now())) : null);
                })
                .collect(Collectors.toList());
            
            if (!brandsToUpdate.isEmpty()) {
                brandRepository.saveAll(brandsToUpdate);
            }
        });
    }

    @Override
    public void setObsoleteById(int id, boolean obsolete) {
        BrandEntity brand = brandRepository.findById(id).orElse(null);
        if (brand != null && brand.isObsolete() != obsolete) {
            brand.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            brand.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            brand.setObsolete(obsolete);
            brand.setEndDate(obsolete ? (brand.getEndDate() != null ? brand.getEndDate() : Timestamp.valueOf(LocalDateTime.now())) : null);
            brandRepository.save(brand);
        }
    }
    
}
