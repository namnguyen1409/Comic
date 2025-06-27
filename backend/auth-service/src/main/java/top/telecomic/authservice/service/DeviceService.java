package top.telecomic.authservice.service;

import top.telecomic.authservice.dto.request.device.DeviceRegisterRequest;

public interface DeviceService {
    void registerDevice(DeviceRegisterRequest request);
}
