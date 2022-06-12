package com.katyshevtseva.kikiorg.core.sections.finance.search;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@RequiredArgsConstructor
public class ReplenishmentSpec implements Specification<Replenishment> {
    private final SearchRequest request;

    @Override
    public Predicate toPredicate(Root<Replenishment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();

        if (request.getStart() != null) {
            Join<Replenishment, DateEntity> dateEntityJoin = root.join("dateEntity");
            objCriteria.add(cb.greaterThanOrEqualTo(dateEntityJoin.get("value"), request.getStart()));
        }

        if (request.getEnd() != null) {
            Join<Replenishment, DateEntity> dateEntityJoin = root.join("dateEntity");
            objCriteria.add(cb.lessThanOrEqualTo(dateEntityJoin.get("value"), request.getEnd()));
        }

        if (request.getMinAmount() != null) {
            objCriteria.add(cb.greaterThanOrEqualTo(root.get("amount"), request.getMinAmount()));
        }

        if (request.getMaxAmount() != null) {
            objCriteria.add(cb.lessThanOrEqualTo(root.get("amount"), request.getMaxAmount()));
        }

        if (!isEmpty(request.getFrom())) {
            objCriteria.add(root.get("source").in(request.getFrom()));
        }

        if (!isEmpty(request.getTo())) {
            objCriteria.add(root.get("account").in(request.getTo()));
        }

        return cb.and(objCriteria.toArray(new Predicate[]{}));
    }
}
