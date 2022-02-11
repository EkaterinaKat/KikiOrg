package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.OldItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemSchemaService {
    @Autowired
    private OldItemHierarchyService service;

    public List<SchemaLine> getSchema() {
        List<SchemaLine> schema = new ArrayList<>();
        for (ItemHierarchyNode topLevelNode : service.getTopLevelNodes()) {
            schema.addAll(getSchemaByRoot(topLevelNode, 0));
            schema.add(new EmptyLine());
        }
        return schema;
    }

    private List<SchemaLine> getSchemaByRoot(ItemHierarchyNode node, int level) {
        List<SchemaLine> schema = new ArrayList<>();
        schema.add(new Entry(node.getTitle(), level, getColorByLevel(level), node, getTooltip(node)));

        if (node.isLeaf())
            return schema;

        for (ItemHierarchyNode childNode : service.getNodesByParent(node)) {
            schema.addAll(getSchemaByRoot(childNode, level + 1));
        }

        schema.add(new AddButton(level + 1, (ItemGroup) node));
        return schema;
    }

    private String getTooltip(ItemHierarchyNode node) {
        if (node instanceof Item) {
            return ((Item) node).getDescription();
        }
        return "";
    }

    private String getColorByLevel(int level) {
        if (level == 0)
            return "#ff0000";
        if (level == 1)
            return "#0000ff";
        if (level == 2)
            return "#118f06";
        return "#000000";
    }

    public class Entry implements SchemaLine {
        private final String text;
        private final int level;
        private final String color;
        private final ItemHierarchyNode node;
        private final String tooltip;

        public Entry(String text, int level, String color, ItemHierarchyNode node, String tooltip) {
            this.text = text;
            this.level = level;
            this.color = color;
            this.node = node;
            this.tooltip = tooltip;
        }

        public void deleteFromSchema() {
            service.destroyTreeWithRootNode(node);
        }

        public String getText() {
            return text;
        }

        public String getColor() {
            return color;
        }

        public int getLevel() {
            return level;
        }

        public boolean isLeaf() {
            return node.isLeaf();
        }

        public boolean isTopLevel() {
            return node.getParentGroup() == null;
        }

        public Item getItem() {
            if (!node.isLeaf())
                throw new RuntimeException("Попытка получить Item из узла, который является ItemGroup");
            return (Item) node;
        }

        public String getTooltip() {
            return tooltip;
        }
    }

    public class AddButton implements SchemaLine {
        private final int level;
        private final ItemGroup groupToAddTo;

        AddButton(int level, ItemGroup groupToAddTo) {
            this.level = level;
            this.groupToAddTo = groupToAddTo;
        }

        public void add(ItemHierarchyNode childNodeToAdd) throws SchemaException {
            if (childNodeToAdd.getParentGroup() != null)
                throw new SchemaException(childNodeToAdd.getTitle() + " уже имеет родителя");

            if (service.treeWithRootContainsNode(childNodeToAdd, groupToAddTo))
                throw new SchemaException(groupToAddTo.getTitle() + " является подкатегорией категории " + childNodeToAdd.getTitle());

            childNodeToAdd.setParentGroup(groupToAddTo);
            service.saveModifiedNode(childNodeToAdd);
        }

        public int getLevel() {
            return level;
        }
    }

    public static class EmptyLine implements SchemaLine {

    }

    public interface SchemaLine {

    }
}
