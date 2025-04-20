package com.muffinmanager.api.muffinmanagerapi.service.Stock.ProductStock;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database.ProductStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IPackagePrintRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductStockRepository;

@Service
public class ProductStockService implements IProductStockService{

    @Autowired
    private IProductStockRepository productStockRepository;
    @Autowired
    private IPackagePrintRepository packagePrintRepository;
    @Autowired
    private IProductRepository productRepository;

    
    
    public ProductStockEntity getEntityByDto(ProductStockRequestDto dto) {
        return ProductStockEntity.builder()
            .id(dto.getId())
            .product(productRepository.findById(dto.getProductId()).orElse(null))
            .packagePrint(packagePrintRepository.findById(dto.getPackagePrintId()).orElse(null))
            .batch(dto.getBatch())
            .stock(dto.getStock())
            .observations(dto.getObservations())
            .lastCheckDate(dto.getLastCheckDate() != null ? Timestamp.valueOf(dto.getLastCheckDate()) : Timestamp.valueOf(LocalDateTime.now()))
            
            .build();

    }



    @Override
    public Object getAllGroupedByBrandThenMuffinShapeThenProductId() {
        // brand -> muffin shape -> product id
        
        return StreamSupport.stream(productStockRepository.findAll().spliterator(), false)
            .collect(Collectors.groupingBy(entity -> entity.getProduct().getProductItem().getBrand(), // Group by brand
            Collectors.groupingBy(entity -> entity.getProduct().getProductItem().getBaseProductItem().getMuffinShape(), // Group by muffin shape
                Collectors.groupingBy(entity -> entity.getProduct().getId(), // Group by product ID
                Collectors.toList())))) // Collect as list
            .entrySet().stream()
            .map(brandEntry -> {
                return new Object() {
                    @SuppressWarnings("unused")
                    public final Object brand = brandEntry.getValue().entrySet().stream()
                    .map(shapeEntry -> {
                        
                        return new Object() {
                            public final Object muffinShape = shapeEntry.getValue().entrySet().stream()
                            .map(productEntry -> {
                                return new Object() {
                                    public final Object productId = productEntry.getValue().stream()
                                    .filter(entity -> entity.getStock() > 0)
                                    .map(ProductStockEntity::toResponseDto)
                                    .collect(Collectors.toList());
                                };
                            })
                            .collect(Collectors.toList());
                        };
                    })
                    .collect(Collectors.toList());
                };
            })
            .collect(Collectors.toList());
    }



    @Override
    public List<ProductStockResponseDto> getAllByProductId(int productId) {
        return productStockRepository.findByProductId(productId)
            .map(entities -> StreamSupport.stream(((Iterable<ProductStockEntity>) entities).spliterator(), false)
                .map(ProductStockEntity::toResponseDto)
                .sorted(Comparator.comparing(ProductStockResponseDto::getBatch))
                .collect(Collectors.toList()))
            .orElseGet(List::of);
    }



    @Override
    public ProductStockResponseDto insert(ProductStockRequestDto productStockDto) {
        return productStockRepository.save(getEntityByDto(productStockDto)).toResponseDto();
    }



    @Override
    public ProductStockResponseDto update(ProductStockRequestDto productStockDto) {
        ProductStockEntity entity = productStockRepository.findById(productStockDto.getId()).orElse(null);
        if (entity != null) {
            entity.setProduct(productRepository.findById(productStockDto.getProductId()).orElse(null));
            entity.setPackagePrint(packagePrintRepository.findById(productStockDto.getPackagePrintId()).orElse(null));
            entity.setBatch(productStockDto.getBatch());
            entity.setObservations(productStockDto.getObservations());
            entity.setStock(productStockDto.getStock());
            entity.setLastCheckDate(productStockDto.getLastCheckDate() != null ? Timestamp.valueOf(productStockDto.getLastCheckDate()) : entity.getLastCheckDate());
            return productStockRepository.save(entity).toResponseDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        productStockRepository.deleteById(id);
    }

}
