package com.muffinmanager.api.muffinmanagerapi.service.MuffinShape;

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
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.database.MuffinShapeEntity;
import com.muffinmanager.api.muffinmanagerapi.model.MuffinShape.dto.MuffinShapeDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IMuffinShapeRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class MuffinShapeService implements IMuffinShapeService{

    @Autowired
    private IMuffinShapeRepository muffinShapeRepository;
    @Autowired 
    private IUserRepository userRepository;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;
    @Override
    public List<MuffinShapeDto> getAll() {
        return StreamSupport.stream(muffinShapeRepository.findAll().spliterator(), false)
            .map(MuffinShapeEntity::toDto)
            .sorted(Comparator
                .comparing(MuffinShapeDto::getReference))
            .collect(Collectors.toList());
    }

    @Override
    public MuffinShapeDto getById(int id) {
        MuffinShapeEntity muffinShapeEntity = muffinShapeRepository.findById(id).orElse(null);
        return muffinShapeEntity != null ? muffinShapeEntity.toDto() : null;
    }

    @Override
    public MuffinShapeDto insert(MuffinShapeDto entityDto) {
        return muffinShapeRepository.save(getEntityByDto(entityDto)).toDto();
    }

    @Override
    public MuffinShapeDto update(MuffinShapeDto entityDto) {
        MuffinShapeEntity muffinShapeEntity = muffinShapeRepository.findById(entityDto.getId()).orElse(null);
        if (muffinShapeEntity != null) {
            muffinShapeEntity.setDescription(entityDto.getDescription());
            muffinShapeEntity.setShapeReference(entityDto.getReference());
            muffinShapeEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            muffinShapeEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return muffinShapeRepository.save(muffinShapeEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        muffinShapeRepository.deleteById(id);
    }

    @Override
    public MuffinShapeEntity getEntityByDto(MuffinShapeDto dto) {
        return MuffinShapeEntity.builder()
            .id(dto.getId())
            .shapeReference(dto.getReference())
            .description(dto.getDescription())
            .lastModifyDate(Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    }

    
}
