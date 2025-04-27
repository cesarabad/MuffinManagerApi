package com.muffinmanager.api.muffinmanagerapi.service.Stock.CheckStock;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database.CheckStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.enums.CheckStockStatus;
import com.muffinmanager.api.muffinmanagerapi.repository.ICheckStockRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IProductStockRepository;
@Service
public class CheckStockService implements ICheckStockService {

    @Autowired
    private ICheckStockRepository checkStockRepository;
    @Autowired
    private IProductStockRepository productStockRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void verify() {
        CheckStockEntity checkStock = checkStockRepository.findActiveCheckStock().orElse(null);
        
        if (checkStock != null && 
            StreamSupport.stream(productStockRepository.findAll().spliterator(), false)
            .filter(productStock -> (productStock.getStock() != 0 || productStock.getReserves().size() != 0) && 
                    productStock.getLastCheckDate() != null && productStock.getLastCheckDate().before(checkStock.getStartDate()))
                    .toList().size() == 0) {
            
            messagingTemplate.convertAndSend("/topic/checkStockCompleted", "Check stock completed");
            checkStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
            checkStock.setStatus(CheckStockStatus.Completed);
            checkStockRepository.save(checkStock);
        }
    }

    @Override
    public CheckStockEntity create() {
        
        if (checkStockRepository.findActiveCheckStock().isPresent()) {
            messagingTemplate.convertAndSend("/topic/createCheckStockFailed", "Check stock already in progress");
            return null;
        }
        
        return checkStockRepository.save(CheckStockEntity.builder()
            .status(CheckStockStatus.InProgress)
            .startDate(Timestamp.valueOf(LocalDateTime.now()))
            .build());
    }

    @Override
    public CheckStockEntity forceCreate() {
        CheckStockEntity checkStock = checkStockRepository.findActiveCheckStock().orElse(null);
        if (checkStock != null) {
            checkStock.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
            checkStock.setStatus(CheckStockStatus.InProgress);
        } else  {
            checkStock = CheckStockEntity.builder()
                .status(CheckStockStatus.InProgress)
                .startDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        }
        
        return checkStockRepository.save(checkStock);
    }

    @Override
    public CheckStockEntity cancel() {
        CheckStockEntity checkStock = checkStockRepository.findActiveCheckStock().orElse(null);
        if (checkStock != null) {
            checkStock.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
            checkStock.setStatus(CheckStockStatus.PartiallyCompleted);
            checkStockRepository.save(checkStock);
        }
        return checkStock;
    }

    @Override
    public boolean hasToCheckStock(LocalDateTime lastCheckDate) {
        CheckStockEntity checkStock = checkStockRepository.findActiveCheckStock().orElse(null);
        return checkStock != null && checkStock.getStartDate().after(Timestamp.valueOf(lastCheckDate));
    }
    
}
