package top.telecomic.authservice.criteria.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import top.telecomic.authservice.criteria.RoleCriteria;
import top.telecomic.authservice.dto.filter.role.RoleFilter;
import top.telecomic.authservice.entity.Role;

import java.util.Set;

@Component
public class RoleCriteriaImpl
        extends CriteriaHelperImpl<Role, RoleFilter>
        implements RoleCriteria {

    @Override
    public Set<String> getAllowedSortFields() {
        return Set.of(
                Role.Fields.code,
                Role.Fields.name,
                Role.Fields.description
        );
    }


    @Override
    public Specification<Role> buildSpecification(RoleFilter filterRequest) {
        return (root, query, criteriaBuilder) -> {
            clearPredicates();

            addLikeIfNotEmpty(
                    root.get(Role.Fields.code),
                    filterRequest.getCode()
            );

            addLikeIfNotEmpty(
                    root.get(Role.Fields.name),
                    filterRequest.getName()
            );

            addLikeIfNotEmpty(
                    root.get(Role.Fields.description),
                    filterRequest.getDescription()
            );

            addSort(filterRequest);
            return andAll();
        };
    }
}
