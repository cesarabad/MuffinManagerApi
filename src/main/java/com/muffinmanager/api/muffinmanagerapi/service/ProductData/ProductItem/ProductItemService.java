package com.muffinmanager.api.muffinmanagerapi.service.ProductData.ProductItem;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.database.ProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.dto.ProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IBaseProductItemRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IBrandRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductItemRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class ProductItemService implements IProductItemService {

    @Autowired
    private IProductItemRepository productItemRepository;
    @Autowired
    private IBaseProductItemRepository baseProductItemRepository;
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;

    @Override
    public List<ProductItemDto> getObsoletes() {
        return StreamSupport.stream(productItemRepository.findAll().spliterator(), false)
            .map(ProductItemEntity::toDto)
            .filter(productItemDto -> productItemDto.isObsolete())
            .sorted(Comparator
                .comparing(ProductItemDto::getReference)
                .thenComparing(ProductItemDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByReference(String reference) {
        productItemRepository.findByItemReference(reference).ifPresent(items -> {
            List<ProductItemEntity> itemsToDelete = items.stream()
                .collect(Collectors.toList());

            if (!itemsToDelete.isEmpty()) {
                productItemRepository.deleteAll(itemsToDelete);
            }
        });
    }

    @Override
    public void setObsoleteByReference(String reference, boolean obsolete) {
        productItemRepository.findByItemReference(reference).ifPresent(items -> {
            List<ProductItemEntity> itemsToUpdate = items.stream()
                .filter(item -> item.getIsObsolete() != obsolete)
                .peek(item -> {
                    item.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
                    item.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
                    item.setIsObsolete(obsolete);
                    item.setEndDate(obsolete ? (item.getEndDate() != null ? item.getEndDate() : Timestamp.valueOf(LocalDateTime.now())) : null);
                })
                .collect(Collectors.toList());

            if (!itemsToUpdate.isEmpty()) {
                productItemRepository.saveAll(itemsToUpdate);
            }
        });
    }

    @Override
    public void setObsoleteById(int id, boolean obsolete) {
        ProductItemEntity item = productItemRepository.findById(id).orElse(null);
        if (item != null && item.getIsObsolete() != obsolete) {
            item.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            item.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            item.setIsObsolete(obsolete);
            item.setEndDate(obsolete ? (item.getEndDate() != null ? item.getEndDate() : Timestamp.valueOf(LocalDateTime.now())) : null);
            productItemRepository.save(item);
        }
    }

    @Override
    public List<ProductItemDto> getAll() {
        return StreamSupport.stream(productItemRepository.findAll().spliterator(), false)
            .map(ProductItemEntity::toDto)
            .filter(productItemDto -> !productItemDto.isObsolete())
            .sorted(Comparator
                .comparing(ProductItemDto::getReference)
                .thenComparing(ProductItemDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public ProductItemDto getById(int id) {
        return productItemRepository.findById(id)
            .map(ProductItemEntity::toDto)
            .orElse(null);
    }

    @Override
    public ProductItemDto insert(ProductItemDto entityDto) {
        ProductItemEntity productItemEntity = getEntityByDto(entityDto);
        productItemEntity.setVersion(productItemRepository.findHighestVersionByReference(entityDto.getReference()).orElse(0) + 1);
        productItemEntity.setIsObsolete(false);
        productItemEntity.setEndDate(null);
        return productItemRepository.save(productItemEntity).toDto();
    }

    @Override
    public ProductItemDto update(ProductItemDto entityDto) {
        ProductItemEntity productItemEntity = productItemRepository.findById(entityDto.getId()).orElse(null);
        if (productItemEntity != null) {
            productItemEntity.setItemReference(entityDto.getReference());
            productItemEntity.setBaseProductItem(baseProductItemRepository.findById(entityDto.getBaseProductItemId()).orElse(null));
            productItemEntity.setBrand(brandRepository.findById(entityDto.getBrandId()).orElse(null));
            productItemEntity.setMainDescription(entityDto.getMainDescription());
            productItemEntity.setEan13(entityDto.getEan13());
            productItemEntity.setAliasVersion(entityDto.getAliasVersion());
            productItemEntity.setCreationDate(productItemEntity.getCreationDate() != null ? productItemEntity.getCreationDate() : Timestamp.valueOf(LocalDateTime.now()));
            productItemEntity.setEndDate(entityDto.isObsolete() 
                ? (entityDto.getEndDate() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(entityDto.getEndDate())) 
                : null);
            productItemEntity.setIsObsolete(entityDto.isObsolete());
            productItemEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            productItemEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return productItemRepository.save(productItemEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        productItemRepository.deleteById(id);
    }

    @Override
    public ProductItemEntity getEntityByDto(ProductItemDto dto) {
        return ProductItemEntity.builder()
            .id(dto.getId())
            .itemReference(dto.getReference())
            .baseProductItem(baseProductItemRepository.findById(dto.getBaseProductItemId()).orElse(null))
            .brand(brandRepository.findById(dto.getBrandId()).orElse(null))
            .mainDescription(dto.getMainDescription())
            .ean13(dto.getEan13())
            .version(dto.getVersion())
            .aliasVersion(dto.getAliasVersion())
            .creationDate(dto.getCreationDate() != null ? Timestamp.valueOf(dto.getCreationDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .endDate(dto.getEndDate() != null && dto.isObsolete() ? Timestamp.valueOf(dto.getEndDate()) : null)
            .isObsolete(dto.isObsolete())
            .lastModifyDate(dto.getLastModifyDate() != null ? Timestamp.valueOf(dto.getLastModifyDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    }
    
}
