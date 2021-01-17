package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.Season;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "outfits_pieces",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_id")
    )
    private List<Piece> pieces;


    @ElementCollection
    @JoinTable(name = "outfits_seasons", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    List<Season> seasons;

    @ElementCollection
    @JoinTable(name = "outfits_purposes", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    List<Purpose> purposes;
}
