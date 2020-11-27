package com.katyshevtseva.kikiorg.core.sections.finance.entity;


import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @Override
    public String toString() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    private Owner owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return getId() == item.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
