package top.telecomic.authservice.criteria;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import top.telecomic.authservice.dto.filter.BaseFilter;

import java.util.Collection;

public interface CriteriaHelper<E, F extends BaseFilter> {

    void addEqualIfNotEmpty(
            Path<String> path,
            String value
    );

    <T> void addEqualIfNotNull(
            Path<T> path,
            T value
    );

    void addLikeIfNotEmpty(
            Path<String> path,
            String value
    );

    <T extends Comparable<? super T>> void addRange(
            Path<T> path,
            T from,
            T to
    );

    <X> void addInIfNotEmpty(
            Path<X> path,
            Collection<X> values
    );

    <T> void addNotEqualIfNotNull(
            Path<T> path,
            T value
    );

    <T> void addNotInIfNotEmpty(
            Path<T> path,
            Collection<T> values
    );


    Specification<E> buildSpecification(
            F filterRequest
    );


    Predicate andAll();

    Predicate orAll();

    void addOrGroup(Predicate... orPredicates);

    void addSort(F filter);

    void clearPredicates();
}
