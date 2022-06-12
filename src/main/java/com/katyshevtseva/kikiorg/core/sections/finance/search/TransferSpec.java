package com.katyshevtseva.kikiorg.core.sections.finance.search;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@RequiredArgsConstructor
public class TransferSpec implements Specification<Transfer> {
    private final SearchRequest request;

    @Override
    public Predicate toPredicate(Root<Transfer> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();

        if (request.getStart() != null) {
            Join<Transfer, DateEntity> dateEntityJoin = root.join("dateEntity");
            objCriteria.add(cb.greaterThanOrEqualTo(dateEntityJoin.get("value"), request.getStart()));
        }

        if (request.getEnd() != null) {
            Join<Transfer, DateEntity> dateEntityJoin = root.join("dateEntity");
            objCriteria.add(cb.lessThanOrEqualTo(dateEntityJoin.get("value"), request.getEnd()));
        }

        if (request.getMinAmount() != null) {
            objCriteria.add(cb.greaterThanOrEqualTo(root.get("goneAmount"), request.getMinAmount()));
            objCriteria.add(cb.greaterThanOrEqualTo(root.get("cameAmount"), request.getMinAmount()));
        }

        if (request.getMaxAmount() != null) {
            objCriteria.add(cb.lessThanOrEqualTo(root.get("goneAmount"), request.getMaxAmount()));
            objCriteria.add(cb.lessThanOrEqualTo(root.get("cameAmount"), request.getMaxAmount()));
        }

        if (!isEmpty(request.getFrom())) {
            objCriteria.add(root.get("from").in(request.getFrom()));
        }

        if (!isEmpty(request.getTo())) {
            objCriteria.add(root.get("to").in(request.getTo()));
        }

        return cb.and(objCriteria.toArray(new Predicate[]{}));
    }
}