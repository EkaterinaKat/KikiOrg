package com.katyshevtseva.kikiorg.core.sections.finance.entity;


import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Item implements ItemHierarchyNode {
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

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private ItemGroup parentGroup;

    @Override
    public boolean isLeaf() {
        return true;
    }

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
