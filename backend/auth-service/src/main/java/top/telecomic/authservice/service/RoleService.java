package top.telecomic.authservice.service;

import org.springframework.data.domain.Page;
import top.telecomic.authservice.dto.filter.role.RoleFilter;
import top.telecomic.authservice.dto.request.role.RoleCreateRequest;
import top.telecomic.authservice.dto.request.role.RoleUpdateRequest;
import top.telecomic.authservice.dto.response.role.RoleResponse;
import top.telecomic.authservice.dto.response.role.RoleResponseDetail;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();

    void clearRoleCache();

    Page<RoleResponseDetail> getRolesByPage(RoleFilter filter);

    RoleResponseDetail getRoleByCode(String code);

    RoleResponseDetail createRole(RoleCreateRequest roleCreateRequest);

    RoleResponseDetail updateRole(String code, RoleUpdateRequest roleUpdateRequest);

    void deleteRole(String code);
}
