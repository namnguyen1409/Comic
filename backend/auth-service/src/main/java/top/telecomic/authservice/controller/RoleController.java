package top.telecomic.authservice.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.telecomic.authservice.dto.filter.role.RoleFilter;
import top.telecomic.authservice.dto.request.role.RoleCreateRequest;
import top.telecomic.authservice.dto.request.role.RoleUpdateRequest;
import top.telecomic.authservice.dto.response.CustomApiResponse;
import top.telecomic.authservice.dto.response.role.RoleResponse;
import top.telecomic.authservice.dto.response.role.RoleResponseDetail;
import top.telecomic.authservice.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/auth/role")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {

    RoleService roleService;

    @GetMapping("/all")
    public CustomApiResponse<List<RoleResponse>> getAllRoles() {
        var response = roleService.getAllRoles();
        return CustomApiResponse.<List<RoleResponse>>builder()
                .message("Roles retrieved successfully")
                .data(response)
                .build();
    }

    @GetMapping
    public CustomApiResponse<Page<RoleResponseDetail>> view() {
        var response = roleService.getRolesByPage(new RoleFilter());
        return CustomApiResponse.<Page<RoleResponseDetail>>builder()
                .message("Roles retrieved successfully")
                .data(response)
                .build();
    }

    @PostMapping
    public CustomApiResponse<Page<RoleResponseDetail>> view(RoleFilter filter) {
        var response = roleService.getRolesByPage(filter);
        return CustomApiResponse.<Page<RoleResponseDetail>>builder()
                .message("Roles retrieved successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{code}")
    public CustomApiResponse<RoleResponseDetail> getRoleByCode(@PathVariable String code) {
        var response = roleService.getRoleByCode(code);
        return CustomApiResponse.<RoleResponseDetail>builder()
                .message("Role retrieved successfully")
                .data(response)
                .build();
    }

    @PostMapping("/create")
    public CustomApiResponse<RoleResponseDetail> createRole(
            @RequestBody @Validated RoleCreateRequest roleCreateRequest
            ) {
        var response = roleService.createRole(roleCreateRequest);
        return CustomApiResponse.<RoleResponseDetail>builder()
                .message("Role created successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/{code}")
    public CustomApiResponse<RoleResponseDetail> updateRole(
            @PathVariable String code,
            @RequestBody @Validated RoleUpdateRequest roleUpdateRequest
            ) {
        var response = roleService.updateRole(code, roleUpdateRequest);
        return CustomApiResponse.<RoleResponseDetail>builder()
                .message("Role updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{code}")
    public CustomApiResponse<Void> deleteRole(@PathVariable String code) {
        roleService.deleteRole(code);
        return CustomApiResponse.<Void>builder()
                .message("Role deleted successfully")
                .build();
    }


}
