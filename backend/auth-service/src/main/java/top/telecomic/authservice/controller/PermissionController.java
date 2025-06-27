package top.telecomic.authservice.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.telecomic.authservice.dto.response.CustomApiResponse;
import top.telecomic.authservice.dto.response.permission.PermissionResponse;

import java.util.List;

@RestController
@RequestMapping("/auth/permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    @GetMapping("/all")
    public CustomApiResponse<List<PermissionResponse>> getAllPermissions() {
        // This method should call a service to retrieve all permissions.
        // For now, we return an empty list as a placeholder.
        List<PermissionResponse> permissions = List.of(); // Replace with actual service call
        return CustomApiResponse.<List<PermissionResponse>>builder()
                .message("Permissions retrieved successfully")
                .data(permissions)
                .build();
    }

}
