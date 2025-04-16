package com.muffinmanager.api.muffinmanagerapi.controller.ProductData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericController;
import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.dto.BaseProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.ProductData.BaseProductItem.IBaseProductItemService;

@RestController
@RequestMapping(BaseProductItemController.BASE_URL)
public class BaseProductItemController implements GenericController<BaseProductItemDto>{

    public static final String BASE_URL = "/manage/product-data/base-product-item";

    @Autowired
    private IBaseProductItemService baseProductItemService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;
    @Autowired
    IUserRepository userRepository;

    @Override
    public ResponseEntity<List<BaseProductItemDto>> getAll() {
        return ResponseEntity.ok(baseProductItemService.getAll());
    }

    @Override
    public ResponseEntity<BaseProductItemDto> getById(int id) {
        return ResponseEntity.ok(baseProductItemService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        baseProductItemService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.baseProductItem.deleted").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BaseProductItemDto> insert(BaseProductItemDto entityDto) {
        BaseProductItemDto  newBaseProductItem = baseProductItemService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.baseProductItem.created").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(newBaseProductItem);
    }

    @Override
    public ResponseEntity<BaseProductItemDto> update(BaseProductItemDto entityDto) {
        BaseProductItemDto updatedEntity = baseProductItemService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.productData.baseProductItem.updated").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(updatedEntity);
    }
    
}
