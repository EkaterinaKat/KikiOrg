package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OutfitSpec implements Specification<Outfit> {
    private final Season season;
    private final Purpose purpose;

    @Override
    public Predicate toPredicate(Root<Outfit> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();

        if (season != null) {
            objCriteria.add(cb.equal(root.get("season"), season));
        }

        if (purpose != null) {
            objCriteria.add(cb.equal(root.get("purpose"), purpose));
        }

        return cb.and(objCriteria.toArray(new Predicate[]{}));
    }
}
