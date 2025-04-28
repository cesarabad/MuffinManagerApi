package com.muffinmanager.api.muffinmanagerapi.controller.Stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.MovementStock.dto.MovementStockDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.MovementStock.IMovementStockService;

@RestController
@RequestMapping(MovementStockController.BASE_URL)
public class MovementStockController {

    public static final String BASE_URL = "/stock/movement-stock";

    @Autowired
    private IMovementStockService movementStockService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @PostMapping("insert")
    public ResponseEntity<MovementStockDto> insert(@RequestBody MovementStockDto movementStockDto) {
        MovementStockDto createdEntity = movementStockService.insert(movementStockDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        switch (createdEntity.getType()) {
            case Entry -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.created.entry").user(user).build());
            }
            case Assigned -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.created.assigned").user(user).build());
            }
            case Adjustment -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.created.adjustment").user(user).build());
            }
            case Reserve -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.created.reserve").user(user).build());
            }
            case Delete -> {}
        }
        messagingTemplate.convertAndSend("/topic" + BASE_URL, movementStockDto);
        return ResponseEntity.ok(createdEntity);
    }

    @PostMapping("undoMovement/{movementStockId}")
    public MovementStockDto undoMovement(@PathVariable int movementStockId) {
        MovementStockDto movementStockDto = movementStockService.undoMovement(movementStockId);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        switch (movementStockDto.getType()) {
            case Entry -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.undoMovement.entry").user(user).build());
            }
            case Assigned -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.undoMovement.assigned").user(user).build());
            }
            case Adjustment -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.undoMovement.adjustment").user(user).build());
            }
            case Reserve -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.undoMovement.reserve").user(user).build());
            }
            case Delete -> {
                messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.undoMovement.delete").user(user).build());
            }
        }
        messagingTemplate.convertAndSend("/topic" + BASE_URL, movementStockDto);
        return movementStockDto;
    }

    @PostMapping("endReserve/{movementStockId}")
    public MovementStockDto endReserve(@PathVariable int movementStockId) {
        MovementStockDto movementStockDto = movementStockService.endReserve(movementStockId);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.endReserve").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, movementStockDto);
        return movementStockDto;
    }

    @PostMapping("update")
    public ResponseEntity<MovementStockDto> update(@RequestBody MovementStockDto movementStockDto) {
        MovementStockDto updatedEntity = movementStockService.update(movementStockDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        switch (movementStockDto.getType()) {
            case Entry -> {
            messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.updated.entry").user(user).build());
            }
            case Assigned -> {
            messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.updated.assigned").user(user).build());
            }
            case Adjustment -> {
            messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.updated.adjustment").user(user).build());
            }
            case Reserve -> {
            messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.movementStock.updated.reserve").user(user).build());
            }
            case Delete -> {}
        }
        messagingTemplate.convertAndSend("/topic" + BASE_URL, movementStockDto);
        return ResponseEntity.ok(updatedEntity);
    }

    @GetMapping("getHistoric")
    public List<MovementStockDto> getHistoric() {
        return movementStockService.getHistoric();
    }

    @GetMapping("getHistoricByProductStockId/{productStockId}")
    public List<MovementStockDto> getHistoricByProductStockId(@PathVariable int productStockId) {
        return movementStockService.getHistoricByProductStockId(productStockId);
    }

    @GetMapping("getHistoricByProductId/{productId}")
    public List<MovementStockDto> getHistoricByProductId(@PathVariable int productId) {
        return movementStockService.getHistoricByProductId(productId);
    }

}
