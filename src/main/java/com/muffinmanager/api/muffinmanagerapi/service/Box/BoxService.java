package com.muffinmanager.api.muffinmanagerapi.service.Box;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muffinmanager.api.muffinmanagerapi.jwt.IJwtService;
import com.muffinmanager.api.muffinmanagerapi.jwt.JwtAutenticationFilter;
import com.muffinmanager.api.muffinmanagerapi.model.Box.BoxDto;
import com.muffinmanager.api.muffinmanagerapi.model.Box.Database.BoxEntity;
import com.muffinmanager.api.muffinmanagerapi.repository.IBoxRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class BoxService implements IBoxService {

    @Autowired
    private IBoxRepository boxRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;

    @Override
    public List<BoxDto> getAll() {
        return StreamSupport.stream(boxRepository.findAll().spliterator(), false)
            .map(BoxEntity::toDto)
            .sorted(Comparator
                .comparing(BoxDto::getReference))  
            .collect(Collectors.toList());
    }

    @Override
    public BoxDto getById(int id) {
        return boxRepository.findById(id)
            .map(BoxEntity::toDto)
            .orElse(null);
    }

    @Override
    public BoxDto insert(BoxDto entityDto) {
        return boxRepository.save(getEntityByDto(entityDto)).toDto();
    }

    @Override
    public BoxDto update(BoxDto entityDto) {
        
        BoxEntity boxEntity = boxRepository.findById(entityDto.getId()).orElse(null);
        if (boxEntity != null) {
            boxEntity.setDescription(entityDto.getDescription());
            boxEntity.setBoxReference(entityDto.getReference());
            boxEntity.setEuropeanBase(entityDto.getEuropeanBase());
            boxEntity.setAmericanBase(entityDto.getAmericanBase());
            boxEntity.setDefaultHeight(entityDto.getDefaultHeight());
            boxEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            boxEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return boxRepository.save(boxEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        boxRepository.deleteById(id);
    }

    @Override
    public BoxEntity getEntityByDto(BoxDto dto) {
        return BoxEntity.builder()
            .id(dto.getId())
            .description(dto.getDescription())
            .boxReference(dto.getReference())
            .europeanBase(dto.getEuropeanBase())
            .americanBase(dto.getAmericanBase())
            .defaultHeight(dto.getDefaultHeight())
            .lastModifyDate(dto.getLastModifyDate() != null ? Timestamp.valueOf(dto.getLastModifyDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    }

    
    
}
