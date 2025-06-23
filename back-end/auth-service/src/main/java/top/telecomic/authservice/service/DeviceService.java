package top.telecomic.authservice.service;

import top.telecomic.authservice.dto.request.DeviceRegisterRequest;

public interface DeviceService {
    void registerDevice(DeviceRegisterRequest request);
}
