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
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.MuffinShape.IMuffinShapeService;

@RestController
@RequestMapping(MuffinShapeController.BASE_URL)
public class MuffinShapeController implements GenericController<MuffinShapeDto> {

    public static final String BASE_URL = "/manage/muffin-shape";

    @Autowired
    private IMuffinShapeService muffinShapeService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;
    @Autowired
    IUserRepository userRepository;

    @Override
    public ResponseEntity<List<MuffinShapeDto>> getAll() {
        return ResponseEntity.ok(muffinShapeService.getAll());
    }

    @Override
    public ResponseEntity<MuffinShapeDto> getById(int id) {
        return ResponseEntity.ok(muffinShapeService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        muffinShapeService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una forma ha sido eliminada por " + user.getName() + " " + user.getSecondName());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<MuffinShapeDto> insert(MuffinShapeDto entityDto) {
        MuffinShapeDto createdEntity = muffinShapeService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una forma ha sido creada por " + user.getName() + " " + user.getSecondName());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, createdEntity);
        return ResponseEntity.ok(createdEntity);
    }

    @Override
    public ResponseEntity<MuffinShapeDto> update(MuffinShapeDto entityDto) {
        MuffinShapeDto updatedEntity = muffinShapeService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una forma ha sido actualizada por " + user.getName() + " " + user.getSecondName());  
        messagingTemplate.convertAndSend("/topic" + BASE_URL, updatedEntity);
        return ResponseEntity.ok(updatedEntity);
    }
    
}
