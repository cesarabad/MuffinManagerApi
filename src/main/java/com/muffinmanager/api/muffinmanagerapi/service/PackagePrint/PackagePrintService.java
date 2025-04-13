package com.muffinmanager.api.muffinmanagerapi.service.PackagePrint;

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
import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.database.PackagePrintEntity;
import com.muffinmanager.api.muffinmanagerapi.model.PackagePrint.dto.PackagePrintDto;
import com.muffinmanager.api.muffinmanagerapi.repository.IPackagePrintRepository;
import com.muffinmanager.api.muffinmanagerapi.repository.IUserRepository;

@Service
public class PackagePrintService implements IPackagePrintService{

    @Autowired
    private IPackagePrintRepository packagePrintRepository;
    @Autowired 
    private IUserRepository userRepository;
    @Autowired
    JwtAutenticationFilter jwtFilter;
    @Autowired
    IJwtService jwtService;

    @Override
    public List<PackagePrintDto> getAll() {
        return StreamSupport.stream(packagePrintRepository.findAll().spliterator(), false)
            .map(PackagePrintEntity::toDto)
            .sorted(Comparator
                .comparing(PackagePrintDto::getReference))
            .collect(Collectors.toList());
    }

    @Override
    public PackagePrintDto getById(int id) {
        PackagePrintEntity packagePrintEntity = packagePrintRepository.findById(id).orElse(null);
        return packagePrintEntity != null ? packagePrintEntity.toDto() : null;
    }

    @Override
    public PackagePrintDto insert(PackagePrintDto entityDto) {
        return packagePrintRepository.save(getEntityByDto(entityDto)).toDto();
    }

    @Override
    public PackagePrintDto update(PackagePrintDto entityDto) {
        PackagePrintEntity packagePrintEntity = packagePrintRepository.findById(entityDto.getId()).orElse(null);
        if (packagePrintEntity != null) {
            packagePrintEntity.setDescription(entityDto.getDescription());
            packagePrintEntity.setPackagePrintReference(entityDto.getReference());
            packagePrintEntity.setLastModifyDate(Timestamp.valueOf(LocalDateTime.now()));
            packagePrintEntity.setLastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null));
            return packagePrintRepository.save(packagePrintEntity).toDto();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        packagePrintRepository.deleteById(id);
    }

    @Override
    public PackagePrintEntity getEntityByDto(PackagePrintDto dto) {
        return PackagePrintEntity.builder()
            .id(dto.getId())
            .packagePrintReference(dto.getReference())
            .description(dto.getDescription())
            .lastModifyDate(Timestamp.valueOf(LocalDateTime.now()))
            .lastModifyUser(userRepository.findByDni(jwtService.getDniFromToken(jwtFilter.getToken())).orElse(null))
            .build();
    }
    
}
