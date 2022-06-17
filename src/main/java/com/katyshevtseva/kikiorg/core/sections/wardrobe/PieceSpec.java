package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService.PieceFilter;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
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
    private final ClothesType clothesType;
    private final PieceFilter pieceFilter;

    @Override
    public Predicate toPredicate(Root<Piece> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> objCriteria = new ArrayList<>();

        if (clothesType != null) {
            objCriteria.add(root.get("type").in(clothesType.getSubtypes()));
        }

        if (pieceFilter != null) {
            switch (pieceFilter) {
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