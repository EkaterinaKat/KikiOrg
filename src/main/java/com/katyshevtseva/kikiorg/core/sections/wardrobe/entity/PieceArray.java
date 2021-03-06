package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ArrayType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class PieceArray {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArrayType arrayType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "arrays_pieces",
            joinColumns = @JoinColumn(name = "array_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id"))
    private List<Piece> pieces;
}
