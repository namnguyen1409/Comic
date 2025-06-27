package top.telecomic.authservice.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public interface CriteriaSpecificationHelper {

    void addEqualIfNotEmpty(
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            Path<String> path,
            String value
    );

    <T> void addEqualIfNotNull(
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            Path<T> path,
            T value
    );

    void addLikeIfNotEmpty(
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            Path<String> path,
            String value
    );

    <T extends Comparable<? super T>> void addRange(
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            Path<T> path,
            T from,
            T to
    );

}
