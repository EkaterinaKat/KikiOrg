package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemSchemaConstructor {
    @Autowired
    private ItemHierarchyService itemHierarchyService;

    public List<SchemaLine> getShema() {
        return null;
    }

    private String getIndentByLevel(int level) {
        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "   ";
        }
        return indent;
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
            itemHierarchyService.destroyTreeWithRootNode(node);
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

            if (itemHierarchyService.treeWithRootContainsNode(childNodeToAdd, groupToAddTo))
                throw new SchemaException(groupToAddTo.getTitle() + " является подкатегорией категории " + childNodeToAdd.getTitle());

            childNodeToAdd.setParentGroup(groupToAddTo);
            itemHierarchyService.saveNode(childNodeToAdd);
        }

        public String getIndent() {
            return indent;
        }
    }

    public class EmptyLine implements SchemaLine {

    }

    public interface SchemaLine {

    }
}
