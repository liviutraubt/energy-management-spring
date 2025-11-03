package org.example.deviceservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.deviceservice.dto.DeviceDTO;
import org.example.deviceservice.dto.UserDTO;
import org.example.deviceservice.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device")
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> findAll() {
        List<DeviceDTO> devices = deviceService.findAll();
        return ResponseEntity.ok(devices);
    }

    @PostMapping
    public ResponseEntity<?> saveDevice(@RequestBody DeviceDTO deviceDTO) {
        try{
            return ResponseEntity.ok(deviceService.insertDevice(deviceDTO));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> insertUser(@RequestBody UserDTO userDTO) {
        try{
            return ResponseEntity.ok(deviceService.insertUser(userDTO));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try{
            deviceService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long id) {
        try{
            deviceService.deleteDevice(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Long id, @RequestBody DeviceDTO deviceDTO) {
        try{
            return ResponseEntity.ok(deviceService.updateDevice(deviceDTO, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DeviceDTO>> findDeviceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.findDevicesByUserId(id));
    }
}
