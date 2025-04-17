package com.muffinmanager.api.muffinmanagerapi.service.ProductData.Product;

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
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IBoxRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductItemRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IBoxRepository boxRepository;
    @Autowired
    private IProductItemRepository productItemRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @Override
    public List<ProductDto> getObsoletes() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
            .map(ProductEntity::toDto)
            .filter(productDto -> productDto.isObsolete())
            .sorted(Comparator
                .comparing(ProductDto::getReference)
                .thenComparing(ProductDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteByReference(String reference) {
        productRepository.findByProductReference(reference).ifPresent(items -> {
            List<ProductEntity> itemsToDelete = items.stream()
                .collect(Collectors.toList());

            if (!itemsToDelete.isEmpty()) {
                productRepository.deleteAll(itemsToDelete);
            }
        });
    }

    @Override
    public void setObsoleteByReference(String reference, boolean obsolete) {
        productRepository.findByProductReference(reference).ifPresent(items -> {
            List<ProductEntity> itemsToUpdate = items.stream()
                .filter(item -> item.isObsolete() != obsolete)
                .peek(item -> {
                    item.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
                    item.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
                    item.setObsolete(obsolete);
                    item.setEndDate(obsolete ? Timestamp.valueOf(LocalDateTime.now()) : null);
                })
                .collect(Collectors.toList());

            if (!itemsToUpdate.isEmpty()) {
                productRepository.saveAll(itemsToUpdate);
            }
        });
    }

    @Override
    public void setObsoleteById(int id, boolean obsolete) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product != null && product.isObsolete() != obsolete) {
            product.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            product.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            product.setObsolete(obsolete);
            product.setEndDate(obsolete ? (product.getEndDate() != null ? product.getEndDate() : Timestamp.valueOf(LocalDateTime.now())) : null);
            productRepository.save(product);
        }
    }

    @Override
    public List<ProductDto> getAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
            .map(ProductEntity::toDto)
            .sorted(Comparator
                .comparing(ProductDto::getReference)
                .thenComparing(ProductDto::getVersion))
            .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(int id) {
        return productRepository.findById(id).orElse(null).toDto();
    }

    @Override
    public ProductDto insert(ProductDto entityDto) {
        ProductEntity product = getEntityByDto(entityDto);
        product.setVersion(productRepository.findHighestVersionByReference(entityDto.getReference()).orElse(0) + 1);
        product.setObsolete(false);
        product.setEndDate(null);
        return productRepository.save(product).toDto();
    }

    @Override
    public ProductDto update(ProductDto entityDto) {
        ProductEntity product = productRepository.findById(entityDto.getId()).orElse(null);
        if (product != null) {
            product.setProductReference(entityDto.getReference());
            product.setBox(boxRepository.findById(entityDto.getBoxId()).orElse(null));
            product.setProductItem(productItemRepository.findById(entityDto.getProductItemId()).orElse(null));
            product.setItemsPerProduct(entityDto.getItemsPerProduct());
            product.setEan14(entityDto.getEan14());
            product.setAliasVersion(entityDto.getAliasVersion());
            product.setCreationDate(product.getCreationDate() != null ? product.getCreationDate() : Timestamp.valueOf(LocalDateTime.now()));
            product.setEndDate(entityDto.isObsolete() 
            ? (entityDto.getEndDate() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(entityDto.getEndDate())) 
            : null);
            product.setObsolete(entityDto.isObsolete());
            product.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            product.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return productRepository.save(product).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductEntity getEntityByDto(ProductDto dto) {
        return ProductEntity.builder()
            .id(dto.getId())
            .productReference(dto.getReference())
            .box(boxRepository.findById(dto.getBoxId()).orElse(null))
            .productItem(productItemRepository.findById(dto.getProductItemId()).orElse(null))
            .itemsPerProduct(dto.getItemsPerProduct())
            .ean14(dto.getEan14())
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
