package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

@Data
@Entity
public class Piece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Enumerated(EnumType.STRING)
    private PieceSubtype type;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "start_date_entity_id")
    private DateEntity startDate;

    @ManyToOne
    @JoinColumn(name = "end_date_entity_id")
    private DateEntity endDate;

    @OneToMany(mappedBy = "piece")
    private Collection<ComponentEntity> components;

    public String getFullDesc() {
        return description + "\n\n" + "Type: " + type + "\n\n" +
                (startDate != null ? READABLE_DATE_FORMAT.format(startDate.getValue()) : "*") +
                "-" +
                (endDate != null ? READABLE_DATE_FORMAT.format(endDate.getValue()) : "*");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return id.equals(piece.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
