package top.telecomic.authservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.authservice.dto.request.DeviceRegisterRequest;
import top.telecomic.authservice.dto.response.CustomApiResponse;
import top.telecomic.authservice.service.DeviceService;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    public CustomApiResponse<Void> register(
            @RequestBody DeviceRegisterRequest request
    ) {
        deviceService.registerDevice(request);
        return CustomApiResponse.<Void>builder()
                .build();
    }


}
