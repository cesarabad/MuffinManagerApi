package com.muffinmanager.api.muffinmanagerapi.service.MuffinShape;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public List<MuffinShapeDto> getNotObsoletesDetailed() {
        return StreamSupport.stream(muffinShapeRepository.findAll().spliterator(), false)
            .filter(MuffinShapeEntity::isActive)
            .map(MuffinShapeEntity::toDetaliedDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MuffinShapeDto> getNotObsoletes() {
        return StreamSupport.stream(muffinShapeRepository.findAll().spliterator(), false)
            .filter(MuffinShapeEntity::isActive)
            .map(MuffinShapeEntity::toListDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MuffinShapeDto> getObsoletes() {
        return StreamSupport.stream(muffinShapeRepository.findAll().spliterator(), false)
            .filter(shape ->  shape.isActive() && shape.isObsolete())
            .map(MuffinShapeEntity::toListDto)
            .collect(Collectors.toList());
    }

    @Override
    public void setObsoleteByReference(String reference, boolean obsolete) {
        List<MuffinShapeEntity> muffinShapes = muffinShapeRepository.findByShapeReference(reference).orElseThrow();

        muffinShapes.forEach(muffinShape -> {
            muffinShape.setObsolete(obsolete);

            if (muffinShape.getEndDate() == null && obsolete) {
                muffinShape.setEndDate(Timestamp.valueOf(LocalDateTime.now()));
            } else if (!obsolete) {
                muffinShape.setEndDate(null);
            }
        });
        muffinShapeRepository.saveAll(muffinShapes);
    }

    @Override
    public MuffinShapeDto save(MuffinShapeDto entityDto) {
        
        MuffinShapeEntity muffinShape = muffinShapeRepository.findByShapeReferenceAndVersion(entityDto.getShapeReference(), entityDto.getVersion()).orElse(new MuffinShapeEntity()).clone();
        muffinShape.setShapeReference(entityDto.getShapeReference());
        muffinShape.setDescription(entityDto.getDescription());
        muffinShape.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        muffinShape.setEndDate(null);
        muffinShape.setActive(true);
        muffinShape.setObsolete(false);
        muffinShape.setVersion(muffinShapeRepository.findMaxVersionByShapeReference(entityDto.getShapeReference()).orElse(0) + 1);
        muffinShape.setLastModifyUser(
            userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElseThrow()
        );
        return muffinShapeRepository.save(muffinShape).toDetaliedDto();
    }

    @Override
    public List<MuffinShapeDto> changeReference(String oldReference, String newReference) {
        List<MuffinShapeEntity> muffinShapes = muffinShapeRepository.findByShapeReference(oldReference).orElseThrow();
        muffinShapes.forEach(muffinShape -> {
            muffinShape.setShapeReference(newReference);
        });
        muffinShapeRepository.saveAll(muffinShapes);
        return muffinShapes.stream().map(MuffinShapeEntity::toDetaliedDto).collect(Collectors.toList());
    }

    @Override
    public List<MuffinShapeDto> getAllVersionsByReference(String reference) {
        return StreamSupport.stream(muffinShapeRepository.findAll().spliterator(), false)
            .filter(shape ->  shape.getShapeReference().equals(reference) && shape.isActive())
            .map(MuffinShapeEntity::toDetaliedDto)
            .collect(Collectors.toList());
    }

    @Override
    public void setActiveByReference(String reference, boolean active) {
        List<MuffinShapeEntity> shapes = muffinShapeRepository.findByShapeReference(reference).orElseThrow();
        shapes.forEach(muffinShape -> {
            muffinShape.setActive(active);
        });
        muffinShapeRepository.saveAll(shapes);
    }

    @Override
    public void setActiveByReferenceAndVersion(String reference, int version, boolean active) {
        MuffinShapeEntity muffinShape = muffinShapeRepository.findByShapeReferenceAndVersion(reference, version).orElseThrow();
        muffinShape.setActive(active);
        muffinShapeRepository.save(muffinShape);
    }

    @Override
    public void setObsoleteByReferenceAndVersion(String reference, int version, boolean obsolete) {
        MuffinShapeEntity muffinShape = muffinShapeRepository.findByShapeReferenceAndVersion(reference, version).orElseThrow();
        muffinShape.setObsolete(obsolete);
        muffinShape.setEndDate(muffinShape.isObsolete() ? Timestamp.valueOf(LocalDateTime.now()) : null);
        muffinShapeRepository.save(muffinShape);
    }

    

}
