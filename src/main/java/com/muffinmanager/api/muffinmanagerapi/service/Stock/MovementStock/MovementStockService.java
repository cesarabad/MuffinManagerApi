package com.muffinmanager.api.muffinmanagerapi.service.Stock.MovementStock;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.database.MovementStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementStatus;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.enums.MovementType;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.database.ProductStockEntity;
import com.muffinmanager.api.muffinmanagerapi.repository.IMovementStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class MovementStockService implements IMovementStockService {

    @Autowired
    private IMovementStockRepository movementStockRepository;
    @Autowired
    private IProductStockRepository productStockRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @Override
    public MovementStockDto insert(MovementStockDto movementStockDto) {
        switch (movementStockDto.getType()) {
            case Entry -> {
                return insertEntry(movementStockDto);
            }
            case Assigned -> {
                return insertAssigned(movementStockDto);
            }
            case Adjustment -> {
                return insertAdjustment(movementStockDto);
            }
            case Reserve -> {
                return insertReserve(movementStockDto);
            }
        }
        throw new IllegalArgumentException("Invalid movement type: " + movementStockDto.getType());
    }

    private MovementStockDto insertEntry(MovementStockDto movementStockDto) {
        ProductStockEntity productStock = productStockRepository.findById(movementStockDto.getProductStockId()).orElse(null);
        if (productStock != null) {
            MovementStockEntity movementStock = getEntityByDto(movementStockDto, productStock);
            productStock.setStock(productStock.getStock() + movementStockDto.getUnits());
            if (productStockRepository.save(productStock) != null) {
                movementStock.setStockDifference(movementStock.getUnits());
                movementStock.setStatus(MovementStatus.Completed);
                movementStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                return movementStockRepository.save(movementStock).toDto();
            }
        }
        
        throw new IllegalArgumentException("Product stock not found for ID: " + movementStockDto.getProductStockId());
    }

    private MovementStockDto insertAssigned(MovementStockDto movementStockDto) {

        ProductStockEntity productStock = productStockRepository.findById(movementStockDto.getProductStockId()).orElse(null);
        if (productStock != null) {
            MovementStockEntity movementStock = getEntityByDto(movementStockDto, productStock);
            productStock.setStock(productStock.getStock() - movementStockDto.getUnits());
            if (productStockRepository.save(productStock) != null) {
                movementStock.setStockDifference(-movementStock.getUnits());
                movementStock.setStatus(MovementStatus.Completed);
                movementStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                return movementStockRepository.save(movementStock).toDto();
            }
        }
        throw new IllegalArgumentException("Product stock not found for ID: " + movementStockDto.getProductStockId());
    }

    private MovementStockDto insertAdjustment(MovementStockDto movementStockDto) {
        ProductStockEntity productStock = productStockRepository.findById(movementStockDto.getProductStockId()).orElse(null);
        
        if (productStock != null) {
            MovementStockEntity movementStock = getEntityByDto(movementStockDto, productStock);
            movementStock.setStockDifference(movementStockDto.getUnits() - productStock.getStock());
            productStock.setStock(movementStockDto.getUnits());
            productStock.setLastCheckDate(Timestamp.valueOf(LocalDateTime.now()));
            if (productStockRepository.save(productStock) != null) {
                movementStock.setStatus(MovementStatus.Completed);
                movementStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                return movementStockRepository.save(movementStock).toDto();
            }
        }
        throw new IllegalArgumentException("Product stock not found for ID: " + movementStockDto.getProductStockId());
    }

    private MovementStockDto insertReserve(MovementStockDto movementStockDto) {
        ProductStockEntity productStock = productStockRepository.findById(movementStockDto.getProductStockId()).orElse(null);
        if (productStock != null) {
            MovementStockEntity movementStock = getEntityByDto(movementStockDto, productStock);
            productStock.setStock(productStock.getStock() - movementStockDto.getUnits());
            if (productStockRepository.save(productStock) != null) {
                movementStock.setStockDifference(-movementStock.getUnits());
                return movementStockRepository.save(movementStock).toDto();
            }
        }
        throw new IllegalArgumentException("Product stock not found for ID: " + movementStockDto.getProductStockId());
    }

    private MovementStockEntity getEntityByDto(MovementStockDto dto, ProductStockEntity productStock) {
        return MovementStockEntity.builder()
            .id(dto.getId())
            .productStock(productStock)
            .type(dto.getType())
            .responsible(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .units(dto.getUnits())
            .destination(dto.getDestination())
            .creationDate(dto.getCreationDate() != null ? Timestamp.valueOf(dto.getCreationDate()) : Timestamp.valueOf(LocalDateTime.now())) // Default to current timestamp if null
            .endDate(dto.getEndDate() != null ? Timestamp.valueOf(dto.getEndDate()) : (dto.getStatus() == MovementStatus.Completed ? Timestamp.valueOf(LocalDateTime.now()) : null))
            .observations(dto.getObservations())
            .status(dto.getStatus() != null ? dto.getStatus() : MovementStatus.InProgress)
            .build();
    }

    @Override
    public List<MovementStockDto> getHistoric() {
        return StreamSupport.stream(movementStockRepository.findAll().spliterator(), false)
            .map(MovementStockEntity::toDto)
            .sorted((a, b) -> b.getCreationDate().compareTo(a.getCreationDate()))
            .toList();
    }

    @Override
    public List<MovementStockDto> getHistoricByProductStockId(int productStockId) {
        return StreamSupport.stream(movementStockRepository.findByProductStockId(productStockId).orElseThrow().spliterator(), false)
            .map(MovementStockEntity::toDto)
            .sorted((a, b) -> b.getCreationDate().compareTo(a.getCreationDate()))
            .toList();
    }

    @Override
    public MovementStockDto undoMovement(int movementStockId) {
        MovementStockEntity movementStock = movementStockRepository.findById(movementStockId).orElse(null);
        if (movementStock != null && movementStock.getStatus() != MovementStatus.Canceled) {

            ProductStockEntity productStock = movementStock.getProductStock();
            if (productStock != null) {
                productStock.setStock(productStock.getStock() - movementStock.getStockDifference());
                productStockRepository.save(productStock);
                movementStock.setStatus(MovementStatus.Canceled);
                movementStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                return movementStockRepository.save(movementStock).toDto();
            }
        }
        throw new IllegalArgumentException("Movement stock not found for ID: " + movementStockId);
    }

    @Override
    public MovementStockDto endReserve(int movementStockId) {
        MovementStockEntity movementStock = movementStockRepository.findById(movementStockId).orElse(null);
        if (movementStock != null && 
            movementStock.getStatus() == MovementStatus.InProgress &&
            movementStock.getType() == MovementType.Reserve) {

                movementStock.setStatus(MovementStatus.Completed);
                movementStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
                return movementStockRepository.save(movementStock).toDto();
            
        }
        throw new IllegalArgumentException("Movement stock not found for ID: " + movementStockId);
    }

    @Override
    public List<MovementStockDto> getHistoricByProductId(int productId) {
        return StreamSupport.stream(movementStockRepository.findAll().spliterator(), false)
            .filter(movementStock -> movementStock.getProductStock() != null 
                && movementStock.getProductStock().getProduct() != null 
                && movementStock.getProductStock().getProduct().getId() == productId)
            .map(MovementStockEntity::toDto)
            
            .sorted((a, b) -> b.getCreationDate().compareTo(a.getCreationDate()))
            .toList();
    }
    
}
