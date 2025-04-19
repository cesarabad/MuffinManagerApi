package com.muffinmanager.api.muffinmanagerapi.service.Stock.MovementStock;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;

import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;
//import com.muffinmanager.api.muffinmanagerapi.repository.IMovementStockRepository;

public class MovementStockService implements IMovementStockService {

    //@Autowired
    //private IMovementStockRepository movementStockRepository;

    @Override
    public MovementStockDto insert(MovementStockDto movementStockDto) {
        
        

        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<MovementStockDto> getHistoric() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHistoric'");
    }

    @Override
    public List<MovementStockDto> getHistoricByProductStockId(int productStockId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHistoricByProductStockId'");
    }

    @Override
    public MovementStockDto undoMovement(int movementStockId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'undoMovement'");
    }

    @Override
    public MovementStockDto endReserve(int movementStockId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endReserve'");
    }
    
}
