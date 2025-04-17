package com.muffinmanager.api.muffinmanagerapi.controller.ProductData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericVersionController;
import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.Product.dto.ProductDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.ProductData.Product.IProductService;

@RestController
@RequestMapping(ProductController.BASE_URL)
public class ProductController implements GenericVersionController<ProductDto>{
    
    public static final String BASE_URL = "/manage/product-data/product";

    @Autowired
    private IProductService productService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @Override
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @Override
    public ResponseEntity<ProductDto> getById(int id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        productService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.deletedVersion").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductDto> insert(ProductDto entityDto) {
        ProductDto createdEntity = productService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.created").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, createdEntity);
        return ResponseEntity.ok(createdEntity);
    }

    @Override
    public ResponseEntity<ProductDto> update(ProductDto entityDto) {
        ProductDto updatedEntity = productService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.updatedVersion").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, updatedEntity);
        return ResponseEntity.ok(updatedEntity);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getObsoletes() {
        return ResponseEntity.ok(productService.getObsoletes());
    }

    @Override
    public ResponseEntity<Void> deleteByReference(String reference) {
        productService.deleteByReference(reference);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.deleted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteByReference(String reference, boolean obsolete) {
        productService.setObsoleteByReference(reference, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.updated").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteById(int id, boolean obsolete) {
        productService.setObsoleteById(id, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.product.updatedVersion").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id + "->" + obsolete);
        return ResponseEntity.ok().build();
    }
}
