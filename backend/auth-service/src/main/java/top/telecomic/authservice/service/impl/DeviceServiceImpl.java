package top.telecomic.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.telecomic.authservice.dto.request.device.DeviceRegisterRequest;
import top.telecomic.authservice.entity.Device;
import top.telecomic.authservice.exception.ErrorCode;
import top.telecomic.authservice.exception.GlobalException;
import top.telecomic.authservice.repository.DeviceRepository;
import top.telecomic.authservice.service.DeviceService;
import top.telecomic.authservice.service.KeyService;
import top.telecomic.authservice.utils.CookiesUtils;
import top.telecomic.authservice.utils.SecurityUtils;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    static final String ROBOT = "Robot";

    DeviceRepository deviceRepository;
    HttpServletRequest httpServletRequest;
    UserAgentAnalyzer uua = UserAgentAnalyzer.newBuilder()
            .hideMatcherLoadStats()
            .withCache(10_000)
            .build();
    CookiesUtils cookiesUtils;
    SecurityUtils securityUtils;
    KeyService keyService;

    @NonFinal
    @Value("${setup.device.fingerprint.name}")
    String deviceFingerprintName;

    @Override
    @Transactional
    public void registerDevice(DeviceRegisterRequest request) {
        var device = deviceRepository.findByDeviceFingerprint(request.getDeviceFingerprint())
                .orElseGet(() -> {
                    var newDevice = new Device();
                    newDevice.setDeviceFingerprint(request.getDeviceFingerprint());
                    return newDevice;
                });

        updateDeviceInfo(device);
        String deviceId = saveDevice(device).getId().toString();
        var claims = Map.of(
                deviceFingerprintName, securityUtils.hash(request.getDeviceFingerprint(), "SHA-256")
        );
        var jwtDeviceId = securityUtils.createJwt(
                deviceId,
                "X-Device-Id",
                claims,
                Duration.ofDays(365),
                keyService.getLatestKey()
        );
        cookiesUtils.setCookie("X-Device-Id", jwtDeviceId, Integer.MAX_VALUE);
    }

    private void updateDeviceInfo(Device device) {
        var uaString = httpServletRequest.getHeader("User-Agent");
        if (uaString == null || uaString.isEmpty()) {
            throw new GlobalException(ErrorCode.DEVICE_USER_AGENT_NOT_FOUND);
        }
        UserAgent ua = uua.parse(uaString);
        validateDeviceAnomaly(device, ua);
        device.setDeviceName(checkRobot(ua.getValue(UserAgent.DEVICE_NAME)));
        device.setOperatingSystemClass(checkRobot(ua.getValue(UserAgent.OPERATING_SYSTEM_CLASS)));
        device.setOperatingSystemName(checkRobot(ua.getValue(UserAgent.OPERATING_SYSTEM_NAME)));
        device.setOperatingSystemVersion(checkRobot(ua.getValue(UserAgent.OPERATING_SYSTEM_VERSION)));
        device.setAgentClass(checkRobot(ua.getValue(UserAgent.AGENT_CLASS)));
        device.setAgentNameVersion(checkRobot(ua.getValue(UserAgent.AGENT_NAME_VERSION)));
    }


    private Device saveDevice(Device device) {
        try {
            return deviceRepository.save(device);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.DEVICE_SAVE_FAILED, "Failed to save device: " + e.getMessage());
        }
    }

    private String checkRobot(String value) {
        if (value == null || value.isEmpty()) {
            throw new GlobalException(ErrorCode.DEVICE_USER_AGENT_NOT_FOUND, "User-agent is not found");
        }
        if (value.contains(ROBOT)) {
            throw new GlobalException(ErrorCode.DEVICE_USER_AGENT_NOT_FOUND, "Robot user-agent is not allowed");
        }
        return value;
    }


    private void validateDeviceAnomaly(Device oldDevice, UserAgent newUa) {
        if (oldDevice.getId() == null) {
            return;
        }
        String newDeviceName = checkRobot(newUa.getValue(UserAgent.DEVICE_NAME));
        String newOsClass = checkRobot(newUa.getValue(UserAgent.OPERATING_SYSTEM_CLASS));
        String newOsName = checkRobot(newUa.getValue(UserAgent.OPERATING_SYSTEM_NAME));
        String newOsVersion = checkRobot(newUa.getValue(UserAgent.OPERATING_SYSTEM_VERSION));
        String newAgentClass = checkRobot(newUa.getValue(UserAgent.AGENT_CLASS));
        if (compareVersions(newOsVersion, oldDevice.getOperatingSystemVersion()) < 0) {
            throw new GlobalException(ErrorCode.DEVICE_USER_AGENT_NOT_FOUND, "OS version downgrade not allowed");
        }

        if (!newDeviceName.equals(oldDevice.getDeviceName())) {
            log.warn("Device name changed from {} to {}", oldDevice.getDeviceName(), newDeviceName);
        }

        if (!newOsClass.equals(oldDevice.getOperatingSystemClass())) {
            log.warn("OS class changed from {} to {}", oldDevice.getOperatingSystemClass(), newOsClass);
        }

        if (!newOsName.equals(oldDevice.getOperatingSystemName())) {
            log.warn("OS name changed from {} to {}", oldDevice.getOperatingSystemName(), newOsName);
        }

        if (!newAgentClass.equals(oldDevice.getAgentClass())) {
            log.warn("Agent class changed from {} to {}", oldDevice.getAgentClass(), newAgentClass);
        }
    }


    private int compareVersions(String version1, String version2) {
        var v1 = new ComparableVersion(version1);
        var v2 = new ComparableVersion(version2);
        return v1.compareTo(v2);
    }

}
