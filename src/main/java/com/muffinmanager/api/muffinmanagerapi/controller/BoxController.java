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
import com.muffinmanager.api.muffinmanagerapi.model.Box.BoxDto;
import com.muffinmanager.api.muffinmanagerapi.model.User.dto.UserSafeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;
import com.muffinmanager.api.muffinmanagerapi.service.Box.IBoxService;

@RestController
@RequestMapping(BoxController.BASE_URL)
public class BoxController implements GenericController<BoxDto> {
    
    public static final String BASE_URL = "/manage/box";

    @Autowired
    private IBoxService boxService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;
    @Autowired
    IUserRepository userRepository;

    @Override
    public ResponseEntity<List<BoxDto>> getAll() {
        return ResponseEntity.ok(boxService.getAll());
    }

    @Override
    public ResponseEntity<BoxDto> getById(int id) {
        return ResponseEntity.ok(boxService.getById(id));
    }

    @Override
    public ResponseEntity<Void> deleteById(int id) {
        boxService.deleteById(id);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una caja ha sido eliminada por " + user.getName() + " " + user.getSecondName());   
        messagingTemplate.convertAndSend("/topic" + BASE_URL, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BoxDto> insert(BoxDto entityDto) {
        BoxDto insertedBoxDto = boxService.insert(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una nueva caja ha sido añadida por " + user.getName() + " " + user.getSecondName() + ".");
        messagingTemplate.convertAndSend("/topic" + BASE_URL, entityDto);
        return ResponseEntity.ok(insertedBoxDto);
        }

        @Override
        public ResponseEntity<BoxDto> update(BoxDto entityDto) {
        BoxDto updatedBoxDto = boxService.update(entityDto);
        UserSafeDto user = userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow().toSafeDto();
        messagingTemplate.convertAndSend("/topic/global", "Una caja ha sido actualizada por " + user.getName() + " " + user.getSecondName() + ".");
        messagingTemplate.convertAndSend("/topic" + BASE_URL, updatedBoxDto);
        return ResponseEntity.ok(updatedBoxDto);
        }
}
