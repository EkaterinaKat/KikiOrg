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

    private Integer x;

    private Integer y;

    private Double relativeWidth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "component_pieces",
            joinColumns = @JoinColumn(name = "component_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id"))
    private List<Piece> pieces;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collage_id", nullable = false)
    private CollageEntity collageEntity;

}
