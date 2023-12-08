package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PieceSpec implements Specification<Piece> {
    private final PieceType type;
    private final PieceState state;
    private final Category category;

    @Override
    public Predicate toPredicate(Root<Piece> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();

        if (type != null) {
            objCriteria.add(root.get("type").in(type.getSubtypes()));
        }

        if (category != null) {
            objCriteria.add(root.join("categories").in(category));
        }

        if (state != null) {
            switch (state) {
                case ACTIVE:
                    objCriteria.add(cb.isNull(root.get("endDate")));
                    break;
                case ARCHIVE:
                    objCriteria.add(cb.isNotNull(root.get("endDate")));
                    break;
                case UNUSED:
                    objCriteria.add(cb.and(
                            cb.isEmpty(root.get("components")),
                            cb.isNull(root.get("endDate"))));
            }
        }

        return cb.and(objCriteria.toArray(new Predicate[]{}));
    }
}