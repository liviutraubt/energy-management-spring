package org.example.deviceservice.repository;

import org.example.deviceservice.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    public List<DeviceEntity> findByUserId(Long userId);
}
