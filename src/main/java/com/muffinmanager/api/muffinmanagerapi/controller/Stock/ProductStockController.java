package com.muffinmanager.api.muffinmanagerapi.controller.Stock;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockRequestDto;
import com.muffinmanager.api.muffinmanagerapi.model.Stock.ProductStock.dto.ProductStockResponseDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Stock.ProductStock.IProductStockService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping(ProductStockController.BASE_URL)
public class ProductStockController {
    
    public static final String BASE_URL = "/stock/product-stock";

    @Autowired
    private IProductStockService productStockService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @PostMapping("insert")
    public ResponseEntity<ProductStockResponseDto> insert(@RequestBody ProductStockRequestDto productStockDto) {
        ProductStockResponseDto createdEntity = productStockService.insert(productStockDto);
        messagingTemplate.convertAndSend("/topic" + BASE_URL, productStockDto);
        return ResponseEntity.ok(createdEntity);
    }

    @GetMapping("getGroupedBy")
    public ResponseEntity<Object> getAllGroupedByBrandThenMuffinShapeThenProductId() {
        return ResponseEntity.ok(productStockService.getAllGroupedByBrandThenMuffinShapeThenProductId());
    }
    
    @GetMapping("getByProductId")
    public ResponseEntity<List<ProductStockResponseDto>> getAllByProductId(@PathParam("productId") int productId) {
        return ResponseEntity.ok(productStockService.getAllByProductId(productId));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        productStockService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.productStock.deleted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("update")
    public ResponseEntity<ProductStockResponseDto> update(@RequestBody ProductStockRequestDto productStockDto) {
        ProductStockResponseDto updatedEntity = productStockService.update(productStockDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.stock.productStock.updated").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, productStockDto);
        return ResponseEntity.ok(updatedEntity);
    }

    @PostMapping("updateLastCheckDate/{id}")
    public ResponseEntity<Void> updateLastCheckDate(@PathVariable int id) {
        productStockService.updateLastCheckDate(id);
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }
    
}
