package com.muffinmanager.api.muffinmanagerapi.service.ProductData.BaseProductItem;

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
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.database.BaseProductItemEntity;
import com.muffinmanager.api.muffinmanagerapi.model.ProductData.BaseProductItem.dto.BaseProductItemDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IBaseProductItemRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IMuffinShapeRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class BaseProductItemService implements IBaseProductItemService {

    @Autowired
    private IBaseProductItemRepository baseProductItemRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JwtAutenticationFilter jwtFilter;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private IMuffinShapeRepository muffinShapeRepository;

    @Override
    public List<BaseProductItemDto> getAll() {
        return StreamSupport.stream(baseProductItemRepository.findAll().spliterator(), false)
            .map(BaseProductItemEntity::toDto)
            .sorted(Comparator.comparing(BaseProductItemDto::getReference))
            .collect(Collectors.toList());
    }

    @Override
    public BaseProductItemDto getById(int id) {
        return baseProductItemRepository.findById(id)
            .map(BaseProductItemEntity::toDto)
            .orElse(null);
    }

    @Override
    public BaseProductItemDto insert(BaseProductItemDto entityDto) {
        return baseProductItemRepository.save(getEntityByDto(entityDto)).toDto();
    }

    @Override
    public BaseProductItemDto update(BaseProductItemDto entityDto) {
        BaseProductItemEntity baseProductItemEntity = baseProductItemRepository.findById(entityDto.getId()).orElse(null);
        if (baseProductItemEntity != null) {
            baseProductItemEntity.setBaseItemReference(entityDto.getReference());
            baseProductItemEntity.setMainDescription(entityDto.getMainDescription());
            baseProductItemEntity.setMuffinShape(muffinShapeRepository.findById(entityDto.getMuffinShape()).orElse(null));
            baseProductItemEntity.setWeight(entityDto.getWeight() != null ? entityDto.getWeight() : 0f);
            baseProductItemEntity.setUnitsPerItem(entityDto.getUnitsPerItem());
            baseProductItemEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            baseProductItemEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return baseProductItemRepository.save(baseProductItemEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        baseProductItemRepository.deleteById(id);
    }

    @Override
    public BaseProductItemEntity getEntityByDto(BaseProductItemDto dto) {
        return BaseProductItemEntity.builder()
            .id(dto.getId())
            .baseItemReference(dto.getReference())
            .mainDescription(dto.getMainDescription())
            .muffinShape(muffinShapeRepository.findById(dto.getMuffinShape()).orElse(null))
            .weight(dto.getWeight())
            .unitsPerItem(dto.getUnitsPerItem())
            .lastModifyDate(dto.getLastModifyDate() != null ? Timestamp.valueOf(dto.getLastModifyDate()) : Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    
    }
    
}
