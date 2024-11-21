package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.Review;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MenuSpecification {
    public static Specification<Menu> nameContains(String query) {
        return (root, queryCriteria, criteriaBuilder) ->
                query != null ? criteriaBuilder.like(root.get("name"), "%" + query + "%") : null;
    }

    public static Specification<Menu> attributeInRange(String attribute, Double min, Double max) {
        return (root, queryCriteria, criteriaBuilder) -> {
            if (attribute == null || min == null || max == null) return null;

            Join<Menu, Review> reviews = root.join("reviews", JoinType.LEFT);
            Expression<Double> attributePath = reviews.get(attribute);

            return criteriaBuilder.between(attributePath, min, max);
        };
    }
}
