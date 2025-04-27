package com.muffinmanager.api.muffinmanagerapi.controller.Stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.CheckStock.database.CheckStockEntity;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.CheckStock.ICheckStockService;

@RestController
@RequestMapping(CheckStockController.BASE_URL)
public class CheckStockController {
    public static final String BASE_URL = "/stock/check-stock";
    
    @Autowired
    private ICheckStockService checkStockService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @PostMapping("create")
    public ResponseEntity<CheckStockEntity> create() {
        CheckStockEntity checkStock = checkStockService.create();
        if (checkStock != null) {
            UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
            messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.checkStock.initialized").user(user).build());
            messagingTemplate.convertAndSend("/topic" + BASE_URL, "Check stock initialized");
        }
        return ResponseEntity.ok(checkStock);
    }

    @PostMapping("forceCreate")
    public ResponseEntity<CheckStockEntity> forceCreate() {
        CheckStockEntity checkStock = checkStockService.forceCreate();
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.checkStock.restarted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, "Check stock restarted");
        return ResponseEntity.ok(checkStock);
    }

    @PostMapping("cancel")
    public ResponseEntity<Void> cancel() {
        checkStockService.cancel();
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.checkStock.canceled").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, "Check stock canceled");
        return ResponseEntity.ok().build();
    }

}
