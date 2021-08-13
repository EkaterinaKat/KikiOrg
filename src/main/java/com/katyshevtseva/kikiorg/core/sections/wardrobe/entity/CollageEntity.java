package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class CollageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer width;

    private Integer height;

    private String color;

    @OneToMany(mappedBy = "collageEntity", fetch = FetchType.EAGER)
    private Collection<ComponentEntity> components;

}
