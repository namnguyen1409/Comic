package top.telecomic.authservice.criteria.impl;

import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import top.telecomic.authservice.criteria.CriteriaHelper;
import top.telecomic.authservice.dto.filter.BaseFilter;

import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class CriteriaHelperImpl<E, F extends BaseFilter> implements CriteriaHelper<E, F> {

    final ThreadLocal<List<Predicate>> predicateHolder = ThreadLocal.withInitial(ArrayList::new);

    record CriteriaContext<E>(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<E> root) {}

    final ThreadLocal<CriteriaContext<E>> contextHolder = new ThreadLocal<>();

    protected Set<String> getAllowedSortFields() {
        throw new UnsupportedOperationException("Subclass must override getAllowedSortFields()");
    }

    protected void initialize(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        contextHolder.set(new CriteriaContext<>(criteriaBuilder, criteriaQuery, root));
        predicateHolder.get().clear();
    }

    protected CriteriaBuilder cb() {
        return contextHolder.get().criteriaBuilder();
    }

    protected CriteriaQuery<?> query() {
        return contextHolder.get().criteriaQuery();
    }

    protected Root<E> root() {
        return contextHolder.get().root();
    }

    protected List<Predicate> getPredicates() {
        return predicateHolder.get();
    }

    @Override
    public void addEqualIfNotEmpty(Path<String> path, String value) {
        if (StringUtils.hasText(value)) {
            getPredicates().add(cb().equal(path, value));
        }
    }

    @Override
    public <T> void addEqualIfNotNull(Path<T> path, T value) {
        if (value != null) {
            getPredicates().add(cb().equal(path, value));
        }
    }

    public void addBooleanEqual(Path<Boolean> path, Boolean value) {
        if (value != null) {
            getPredicates().add(cb().equal(path, value));
        }
    }

    @Override
    public void addLikeIfNotEmpty(Path<String> path, String value) {
        if (StringUtils.hasText(value)) {
            String escaped = value.toLowerCase()
                    .replace("_", "\\_")
                    .replace("%", "\\%");
            getPredicates().add(
                    cb().like(
                            cb().lower(path),
                            "%" + escaped + "%",
                            '\\'
                    )
            );
        }
    }

    @Override
    public <T extends Comparable<? super T>> void addRange(Path<T> path, T from, T to) {
        if (from != null && to != null) {
            getPredicates().add(cb().between(path, from, to));
        } else if (from != null) {
            getPredicates().add(cb().greaterThanOrEqualTo(path, from));
        } else if (to != null) {
            getPredicates().add(cb().lessThanOrEqualTo(path, to));
        }
    }

    @Override
    public <X> void addInIfNotEmpty(Path<X> path, Collection<X> values) {
        if (values != null && !values.isEmpty()) {
            getPredicates().add(path.in(values));
        }
    }

    @Override
    public <T> void addNotEqualIfNotNull(Path<T> path, T value) {
        if (value != null) {
            getPredicates().add(cb().notEqual(path, value));
        }
    }

    @Override
    public <T> void addNotInIfNotEmpty(Path<T> path, Collection<T> values) {
        if (values != null && !values.isEmpty()) {
            getPredicates().add(cb().not(path.in(values)));
        }
    }

    @Override
    public Predicate andAll() {
        List<Predicate> list = getPredicates();
        return list.isEmpty()
                ? cb().conjunction()
                : cb().and(list.toArray(new Predicate[0]));
    }

    @Override
    public Predicate orAll() {
        List<Predicate> list = getPredicates();
        return list.isEmpty()
                ? cb().disjunction()
                : cb().or(list.toArray(new Predicate[0]));
    }

    @Override
    public void addOrGroup(Predicate... orPredicates) {
        if (orPredicates != null && orPredicates.length > 0) {
            getPredicates().add(cb().or(orPredicates));
        }
    }

    @Override
    public void addSort(F filter) {
        String sortBy = filter.getSortBy();
        String direction = filter.getSortDirection();

        if (!StringUtils.hasText(sortBy) || !getAllowedSortFields().contains(sortBy)) {
            log.warn("Invalid or missing sort field: '{}'. Ignoring sort.", sortBy);
            return;
        }

        if (!StringUtils.hasText(direction)) {
            direction = Sort.Direction.ASC.name();
        }

        try {
            Path<Object> sortPath = root().get(sortBy);
            Order order = direction.equalsIgnoreCase("desc")
                    ? cb().desc(sortPath)
                    : cb().asc(sortPath);
            query().orderBy(order);
        } catch (IllegalArgumentException e) {
            log.warn("Sort field '{}' is invalid for entity '{}'", sortBy, root().getJavaType().getSimpleName());
        }
    }

    @Override
    public void clearPredicates() {
        predicateHolder.get().clear();
    }
}
