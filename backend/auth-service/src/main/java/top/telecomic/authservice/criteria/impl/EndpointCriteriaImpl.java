package top.telecomic.authservice.criteria.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import top.telecomic.authservice.criteria.EndpointCriteria;
import top.telecomic.authservice.dto.filter.endpoint.EndpointFilter;
import top.telecomic.authservice.entity.Endpoint;

import java.util.Set;

@Component
public class EndpointCriteriaImpl
        extends CriteriaHelperImpl<Endpoint, EndpointFilter>
        implements EndpointCriteria {

    @Override
    public Set<String> getAllowedSortFields() {
        return Set.of(
                Endpoint.Fields.path,
                Endpoint.Fields.method,
                Endpoint.Fields.description,
                Endpoint.Fields.isPublic
        );
    }

    @Override
    public Specification<Endpoint> buildSpecification(EndpointFilter filterRequest) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            initialize(root, criteriaQuery, criteriaBuilder);

            addLikeIfNotEmpty(
                    root.get(Endpoint.Fields.path),
                    filterRequest.getPath()
            );

            addLikeIfNotEmpty(
                    root.get(Endpoint.Fields.method),
                    filterRequest.getMethod()
            );

            addLikeIfNotEmpty(
                    root.get(Endpoint.Fields.description),
                    filterRequest.getDescription()
            );

            addBooleanEqual(
                    root.get(Endpoint.Fields.isPublic),
                    filterRequest.getIsPublic()
            );
            addSort(filterRequest);
            return andAll();
        };
    }
}
