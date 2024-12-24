package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "pieces_categories", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    Set<Category> categories;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @OneToMany(mappedBy = "piece")
    private Collection<ComponentEntity> components;

    public String getFullDesc() {
        return description + "\n\n" + "Type: " + type + "\n" + categories + "\n\n" +
                (startDate != null ? READABLE_DATE_FORMAT.format(startDate) : "*") +
                "-" +
                (endDate != null ? READABLE_DATE_FORMAT.format(endDate) : "*");
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
