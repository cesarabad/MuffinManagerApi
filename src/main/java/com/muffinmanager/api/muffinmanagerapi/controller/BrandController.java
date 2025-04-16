package com.muffinmanager.api.muffinmanagerapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericVersionController;
import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Brand.dto.BrandDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Brand.IBrandService;

@RestController
@RequestMapping(BrandController.BASE_URL)
public class BrandController implements GenericVersionController<BrandDto> {

    public static final String BASE_URL = "/manage/brand";

    @Autowired
    private IBrandService brandService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;
    
    @Override
    public ResponseEntity<List<BrandDto>> getAll() {
        return ResponseEntity.ok(brandService.getAll());
    }

    @Override
    public ResponseEntity<BrandDto> getById(int id) {
        return ResponseEntity.ok(brandService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        brandService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.deletedVersion").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BrandDto> insert(BrandDto entityDto) {
        return ResponseEntity.ok(brandService.insert(entityDto));
    }

    @Override
    public ResponseEntity<BrandDto> update(BrandDto entityDto) {
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.updatedVersion").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto.getId());
        return ResponseEntity.ok(brandService.update(entityDto));
    }

    @Override
    public ResponseEntity<List<BrandDto>> getObsoletes() {
        return ResponseEntity.ok(brandService.getObsoletes());
    }

    @Override
    public ResponseEntity<Void> deleteByReference(String reference) {
        brandService.deleteByReference(reference);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.deleted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference);
        return ResponseEntity.ok().build();
    }

    /*@Override
    public ResponseEntity<List<BrandDto>> changeReference(String oldReference, String newReference) {
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.updated").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, oldReference + "->" + newReference);
        return ResponseEntity.ok(brandService.changeReference(oldReference, newReference));
    }*/

    @Override
    public ResponseEntity<Void> setObsoleteByReference(String reference, boolean obsolete) {
        brandService.setObsoleteByReference(reference, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.updated").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, reference);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setObsoleteById(int id, boolean obsolete) {
        brandService.setObsoleteById(id, obsolete);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.brand.updatedVersion").user(user).build());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }
}
