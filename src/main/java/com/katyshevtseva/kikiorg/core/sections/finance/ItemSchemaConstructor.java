package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ItemSchemaConstructor {
    @Autowired
    private ItemHierarchyService service;

    public List<SchemaLine> getShema() {
        List<SchemaLine> schema = new ArrayList<>();
        for (ItemHierarchyNode topLevelNode : service.getTopLevelNodes()) {
            schema.addAll(getSchemaByRoot(topLevelNode, 0));
            schema.add(new EmptyLine());
        }
        return schema;
    }

    private List<SchemaLine> getSchemaByRoot(ItemHierarchyNode node, int level) {
        if (node.isLeaf())
            return Collections.singletonList(
                    new Entry(getIndentByLevel(level) + node.getTitle(), getColorByLevel(level), node));

        List<SchemaLine> schema = new ArrayList<>();
        for (ItemHierarchyNode childNode : service.getNodesByParent(node)) {
            schema.addAll(getSchemaByRoot(childNode, level + 1));
        }
        schema.add(new AddButton(getIndentByLevel(level), (ItemGroup) node));
        return schema;
    }

    private String getIndentByLevel(int level) {
        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "   ";
        }
        return indent;
    }

    private String getColorByLevel(int level) {
        if (level == 0)
            return "#ff0000";
        if (level == 1)
            return "#00ff00";
        if (level == 3)
            return "#0000ff";
        return "#000000";
    }

    public class Entry implements SchemaLine {
        private final String text;
        private final String color;
        private final ItemHierarchyNode node;

        public Entry(String text, String color, ItemHierarchyNode node) {
            this.text = text;
            this.color = color;
            this.node = node;
        }

        public void deleteFromShema() {
            service.destroyTreeWithRootNode(node);
        }

        public String getText() {
            return text;
        }

        public String getColor() {
            return color;
        }
    }

    public class AddButton implements SchemaLine {
        private final String indent;
        private final ItemGroup groupToAddTo;

        public AddButton(String indent, ItemGroup groupToAddTo) {
            this.indent = indent;
            this.groupToAddTo = groupToAddTo;
        }

        public void add(ItemHierarchyNode childNodeToAdd) throws SchemaException {
            if (childNodeToAdd.getParentGroup() != null)
                throw new SchemaException(childNodeToAdd.getTitle() + " уже имеет родителя");

            if (service.treeWithRootContainsNode(childNodeToAdd, groupToAddTo))
                throw new SchemaException(groupToAddTo.getTitle() + " является подкатегорией категории " + childNodeToAdd.getTitle());

            childNodeToAdd.setParentGroup(groupToAddTo);
            service.saveNode(childNodeToAdd);
        }

        public String getIndent() {
            return indent;
        }
    }

    public static class EmptyLine implements SchemaLine {

    }

    public interface SchemaLine {

    }
}
