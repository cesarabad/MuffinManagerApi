package com.muffinmanager.api.muffinmanagerapi.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffinmanager.api.muffinmanagerapi.controller.GenericController.GenericController;
import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto.PackagePrintDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.model.WSMessage.WebSocketMessage;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.PackagePrint.IPackagePrintService;

@RestController
@RequestMapping(PackagePrintController.BASE_URL)
public class PackagePrintController implements GenericController<PackagePrintDto> {
    
    public static final String BASE_URL = "/manage/package-print";

    @Autowired
    private IPackagePrintService packagePrintService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;
    @Autowired
    IUserRepository userRepository;

    @Override
    public ResponseEntity<List<PackagePrintDto>> getAll() {
        return ResponseEntity.ok(packagePrintService.getAll());
    }

    @Override
    public ResponseEntity<PackagePrintDto> getById(int id) {
        return ResponseEntity.ok(packagePrintService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        packagePrintService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.packagePrint.deleted").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PackagePrintDto> insert(PackagePrintDto entityDto) {
        PackagePrintDto createdEntity = packagePrintService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.packagePrint.created").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(createdEntity);
    }

    @Override
    public ResponseEntity<PackagePrintDto> update(PackagePrintDto entityDto) {
        PackagePrintDto updatedEntity = packagePrintService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", WebSocketMessage.builder().dictionaryKey("ws.packagePrint.updated").user(user).build());
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(updatedEntity);
    }
    
}
