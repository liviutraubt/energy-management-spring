package org.example.deviceservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDTO {

    private Long id;
    private String device_type;
    private Double consumption;
    private Boolean active;
    private UserDTO user;
}
