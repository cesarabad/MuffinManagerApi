package com.muffinmanager.api.muffinmanagerapi.service.Stock.ProductStock;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.database.ProductEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductLightDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.database.BrandEntity;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database.MuffinShapeEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementStatus;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementType;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database.ProductStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;
import com.muffinmanager.api.muffinmanagerapi.repository.ICheckStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IMovementStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IPackagePrintRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.CheckStock.ICheckStockService;

@Service
public class ProductStockService implements IProductStockService{

    @Autowired
    private IProductStockRepository productStockRepository;
    @Autowired
    private IPackagePrintRepository packagePrintRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICheckStockService checkStockService;
    @Autowired
    private ICheckStockRepository checkStockRepository;
    @Autowired
    private IMovementStockRepository movementStockRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;
    
    
    public ProductStockEntity getEntityByDto(ProductStockRequestDto dto) {
        return ProductStockEntity.builder()
            .id(dto.getId())
            .product(productRepository.findById(dto.getProductId()).orElse(null))
            .packagePrint(packagePrintRepository.findById(dto.getPackagePrintId()).orElse(null))
            .batch(dto.getBatch())
            .stock(dto.getStock())
            .isDeleted(false)
            .observations(dto.getObservations())
            .lastCheckDate(dto.getLastCheckDate() != null ? Timestamp.valueOf(dto.getLastCheckDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .build();

    }



    @Override
    public Object getAllGroupedByBrandThenMuffinShapeThenProductId() {
        return StreamSupport.stream(productStockRepository.findAll().spliterator(), false)
            .collect(Collectors.groupingBy(
                (ProductStockEntity entity) -> entity.getProduct().getProductItem().getBrand(),
                () -> new TreeMap<BrandEntity, Map<MuffinShapeEntity, Map<ProductEntity, List<ProductStockEntity>>>>(
                    Comparator.comparing(BrandEntity::getBrandReference)
                ),
                Collectors.groupingBy(
                    (ProductStockEntity entity) -> entity.getProduct().getProductItem().getBaseProductItem().getMuffinShape(),
                    () -> new TreeMap<MuffinShapeEntity, Map<ProductEntity, List<ProductStockEntity>>>(
                        Comparator.comparing(MuffinShapeEntity::getShapeReference)
                    ),
                    Collectors.groupingBy(
                        ProductStockEntity::getProduct,
                        () -> new TreeMap<ProductEntity, List<ProductStockEntity>>(
                            Comparator.comparing(ProductEntity::getProductReference)
                        ),
                        Collectors.toList()
                    )
                )
            ))
            .entrySet().stream()
            .map(brandEntry -> {
                BrandEntity brand = brandEntry.getKey();
                Map<MuffinShapeEntity, Map<ProductEntity, List<ProductStockEntity>>> shapeMap = brandEntry.getValue();

                return new Object() {
                    @SuppressWarnings("unused")
                    public final boolean hasToCheck = checkStockRepository.findActiveCheckStock().isPresent();
                    @SuppressWarnings("unused")
                    public final String brandName = brand.getName();
                    @SuppressWarnings("unused")
                    public final String brandAliasVersion = brand.getAliasVersion();
                    @SuppressWarnings("unused")
                    public final String brandLogoBase64 = brand.getLogo() != null
                            ? Base64.getEncoder().encodeToString(brand.getLogo())
                            : null;

                    @SuppressWarnings("unused")
                    public final List<Object> muffinShapes = shapeMap.entrySet().stream()
                        .map(shapeEntry -> {
                            MuffinShapeEntity shape = shapeEntry.getKey();
                            Map<ProductEntity, List<ProductStockEntity>> productsMap = shapeEntry.getValue();

                            return new Object() {
                                public final String muffinShape = shape.getDescription();
                                public final List<Object> products = productsMap.entrySet().stream()
                                    .map(productEntry -> {
                                        ProductEntity productEntity = productEntry.getKey();
                                        List<ProductStockEntity> stockList = productEntry.getValue();

                                        return new Object() {
                                            public final ProductLightDto product = productEntity.toLightDto();
                                            public final List<Object> stockDetails = stockList.stream()
                                                .sorted(Comparator.comparing(ProductStockEntity::getBatch))
                                                .filter(productStock -> !productStock.isDeleted())
                                                .map(ProductStockEntity::toResponseDto)
                                                .peek(productStock -> {
                                                    productStock.setHasToCheck(checkStockService.hasToCheckStock(productStock.getLastCheckDate()));
                                                })
                                                .collect(Collectors.toList());
                                        };
                                    })
                                    .filter(productEntry -> productEntry.stockDetails.size() != 0)
                                    .collect(Collectors.toList());
                            };
                        })
                        .filter(muffinShapeEntry -> muffinShapeEntry.products.size() != 0)
                        .collect(Collectors.toList());
                };
            })
            .filter(brandEntry -> brandEntry.muffinShapes.size() != 0)
            .collect(Collectors.toList());
    }

    






    @Override
    public List<ProductStockResponseDto> getAllByProductId(int productId) {
        return productStockRepository.findByProductId(productId)
            .map(entities -> StreamSupport.stream(((Iterable<ProductStockEntity>) entities).spliterator(), false)
                .map(ProductStockEntity::toResponseDto)
                .peek(productStock -> {
                    productStock.setHasToCheck(checkStockService.hasToCheckStock(productStock.getLastCheckDate()));
                })
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
            entity.setLastCheckDate(entity.getStock() != productStockDto.getStock() ? Timestamp.valueOf(LocalDateTime.now()) : entity.getLastCheckDate());
            if (entity.getStock() != productStockDto.getStock()) {
                movementStockRepository.save(MovementStockEntity.builder()
                    .productStock(entity)
                    .type(MovementType.Adjustment)
                    .responsible(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
                    .stockDifference(productStockDto.getStock() - entity.getStock())
                    .units(productStockDto.getStock())
                    .destination(null)
                    .creationDate(Timestamp.valueOf(LocalDateTime.now()))
                    .endDate(Timestamp.valueOf(LocalDateTime.now()))
                    .observations(null)
                    .status(MovementStatus.Completed)
                    .build());
            }
            entity.setStock(productStockDto.getStock());
            entity.setDeleted(entity.getStock() == 0 && entity.getReserves().size() == 0);
            ProductStockEntity savedEntity = productStockRepository.save(entity);
            checkStockService.verify();
            return savedEntity.toResponseDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        productStockRepository.deleteById(id);
        checkStockService.verify();
    }



    @Override
    public void updateLastCheckDate(int id) {
        ProductStockEntity entity = productStockRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setLastCheckDate(Timestamp.valueOf(LocalDateTime.now()));
            productStockRepository.save(entity);
            checkStockService.verify();
        }
    }

}
