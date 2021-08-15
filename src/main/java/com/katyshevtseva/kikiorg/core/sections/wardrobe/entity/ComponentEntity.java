package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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
    private Set<Piece> pieces;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "front_piece_id", nullable = false)
    private Piece frontPiece;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "collage_id", nullable = false)
    private CollageEntity collageEntity;

    @Override
    public String toString() {
        return "ComponentEntity{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentEntity that = (ComponentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
