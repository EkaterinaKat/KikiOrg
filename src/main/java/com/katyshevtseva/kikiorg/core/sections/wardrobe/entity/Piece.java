package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.Data;

import javax.persistence.*;
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
    private ClothesType type;

    @ManyToOne
    @JoinColumn(name = "start_date_entity_id")
    private DateEntity startDate;

    @ManyToOne
    @JoinColumn(name = "end_date_entity_id")
    private DateEntity endDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "pieces_seasons", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    Set<Season> seasons;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "pieces_purposes", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    Set<Purpose> purposes;

    public String getFullDesc() {
        StringBuilder fullDesc = new StringBuilder(description + "\n\n" + "Type: " + type + "\n\n" + "Seasons: ");
        for (Season season : seasons)
            fullDesc.append(season).append(", ");
        fullDesc.delete(fullDesc.length() - 2, fullDesc.length());
        fullDesc.append(".");
        fullDesc.append("\n\nPurposes: ");
        for (Purpose purpose : purposes)
            fullDesc.append(purpose).append(", ");
        fullDesc.delete(fullDesc.length() - 2, fullDesc.length());
        fullDesc.append(".\n\n")
                .append(startDate != null ? READABLE_DATE_FORMAT.format(startDate.getValue()) : "*")
                .append("-")
                .append(endDate != null ? READABLE_DATE_FORMAT.format(endDate.getValue()) : "*");
        return fullDesc.toString();
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
