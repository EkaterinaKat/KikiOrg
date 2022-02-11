package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.hierarchy.Group;
import com.katyshevtseva.kikiorg.core.sections.finance.OldItemHierarchyService.ItemHierarchyNode;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class ItemGroup implements ItemHierarchyNode, Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    @Getter
    private ItemGroup parentGroup;

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void setParentGroup(ItemGroup group) {
        this.parentGroup = group;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void setParentGroup(Group group) {
        this.parentGroup = (ItemGroup) group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroup itemGroup = (ItemGroup) o;
        return getId() == itemGroup.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return title;
    }
}
