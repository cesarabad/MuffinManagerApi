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
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.ProductItem.dto.ProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.ProductData.ProductItem.IProductItemService;

@RestController
@RequestMapping(ProductItemController.BASE_URL)
public class ProductItemController implements GenericVersionController<ProductItemDto> {
    
    public static final String BASE_URL = "/manage/product-data/product-item";
    
    @Autowired
    private IProductItemService productItemService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;

    @Override
    public ResponseEntity<List<ProductItemDto>> getAll() {
        return ResponseEntity.ok(productItemService.getAll());
    }

    @Override
    public ResponseEntity<ProductItemDto> getById(int id) {
        return ResponseEntity.ok(productItemService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        productItemService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.deletedVersion").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductItemDto> insert(ProductItemDto entityDto) {
        ProductItemDto createdEntity = productItemService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.created").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(createdEntity);
    }

    @Override
    public ResponseEntity<ProductItemDto> update(ProductItemDto entityDto) {
        ProductItemDto updatedEntity = productItemService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.updatedVersion").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(updatedEntity);
    }

    @Override
    public ResponseEntity<List<ProductItemDto>> getObsoletes() {
        return ResponseEntity.ok(productItemService.getObsoletes());
    }

    @Override
    public ResponseEntity<Void> deleteByReference(String reference) {
        productItemService.deleteByReference(reference);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.deleted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteByReference(String reference, boolean obsolete) {
        productItemService.setObsoleteByReference(reference, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.updated").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference + "->" + obsolete);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteById(int id, boolean obsolete) {
        productItemService.setObsoleteById(id, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.productItem.updatedVersion").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id + "->" + obsolete);
        return ResponseEntity.ok().build();
    }
}
