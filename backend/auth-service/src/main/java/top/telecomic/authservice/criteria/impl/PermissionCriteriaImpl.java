package top.telecomic.authservice.criteria.impl;

import org.springframework.data.jpa.domain.Specification;
import top.telecomic.authservice.criteria.PermissionCriteria;
import top.telecomic.authservice.dto.filter.permission.PermissionFilter;
import top.telecomic.authservice.entity.Permission;

import java.util.Set;

public class PermissionCriteriaImpl
        extends CriteriaHelperImpl<Permission, PermissionFilter>
        implements PermissionCriteria {

    @Override
    public Set<String> getAllowedSortFields() {
        return Set.of(
                Permission.Fields.code,
                Permission.Fields.name,
                Permission.Fields.description
        );
    }

    @Override
    public Specification<Permission> buildSpecification(PermissionFilter filterRequest) {
        return (root, query, criteriaBuilder) -> {
            initialize(root, query, criteriaBuilder);
            addLikeIfNotEmpty(
                    root.get(Permission.Fields.code),
                    filterRequest.getCode()
            );
            addLikeIfNotEmpty(
                    root.get(Permission.Fields.name),
                    filterRequest.getName()
            );
            addLikeIfNotEmpty(
                    root.get(Permission.Fields.description),
                    filterRequest.getDescription()
            );
            addSort(filterRequest);
            return andAll();
        };
    }
}
