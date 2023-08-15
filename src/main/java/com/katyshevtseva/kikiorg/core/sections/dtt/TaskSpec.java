package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TaskSpec implements Specification<DatelessTask> {
    private final Sphere sphere;
    private final TaskStatus status;
    private final String searchString;

    @Override
    public Predicate toPredicate(Root<DatelessTask> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();


        if (sphere != null) {
            objCriteria.add(cb.equal(root.get("sphere"), sphere));
        }

        if (status != null) {
            objCriteria.add(cb.equal(root.get("status"), status));
        }

        if (!GeneralUtils.isEmpty(searchString)) {
            String likeString = "%" + searchString.toUpperCase() + "%";
            objCriteria.add(cb.or(cb.like(cb.upper(root.get("title")), likeString),
                    cb.like(cb.upper(root.get("description")), likeString)));
        }

        return cb.and(objCriteria.toArray(new Predicate[]{}));
    }
}