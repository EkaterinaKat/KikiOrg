package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ComponentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double relativeX;

    private Double relativeY;

    private Integer z;

    private Double relativeWidth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "component_pieces",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id"))
    private List<Piece> pieces;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "front_piece_id", nullable = false)
    private Piece frontPiece;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collage_id", nullable = false)
    private CollageEntity collageEntity;

}
