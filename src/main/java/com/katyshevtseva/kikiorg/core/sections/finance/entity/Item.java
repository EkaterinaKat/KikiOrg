package com.katyshevtseva.kikiorg.core.sections.finance.entity;


import com.katyshevtseva.hierarchy.Group;
import com.katyshevtseva.hierarchy.Leaf;
import com.katyshevtseva.kikiorg.core.sections.finance.OldItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Item implements OperationEnd, ItemHierarchyNode, Leaf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private ItemGroup parentGroup;

    @Override
    public String toString() {
        return title;
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

    @Override
    public OperationEndType getType() {
        return OperationEndType.ITEM;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public void setParentGroup(ItemGroup group) {
        this.parentGroup = group;
    }

    @Override
    public void setParentGroup(Group group) {
        this.parentGroup = (ItemGroup) group;
    }
}
