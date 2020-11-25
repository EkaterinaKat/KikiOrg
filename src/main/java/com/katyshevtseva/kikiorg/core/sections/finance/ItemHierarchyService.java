package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemHierarchyService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemGroupRepo itemGroupRepo;
    @Autowired
    private DateService dateService;
    @Autowired
    private ExpenseRepo expenseRepo;

    public List<ItemHierarchyNode> getTopLevelNodes() {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        nodes.addAll(itemRepo.findByParentGroupIsNull());
        nodes.addAll(itemGroupRepo.findByParentGroupIsNull());
        return nodes;
    }

    List<ItemHierarchyNode> getNodesByParent(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode instanceof Item)
            return nodes;
        nodes.addAll(itemRepo.findByParentGroup((ItemGroup) parentNode));
        nodes.addAll(itemGroupRepo.findByParentGroup((ItemGroup) parentNode));
        return nodes;
    }

    void destroyTreeWithRootNode(ItemHierarchyNode node) {
        node.setParentGroup(null);
        saveNode(node);

        if (node.isLeaf())
            return;

        for (ItemHierarchyNode childNode : getNodesByParent(node))
            destroyTreeWithRootNode(childNode);
    }

    void saveNode(ItemHierarchyNode node) {
        if (node.isLeaf())
            itemRepo.save((Item) node);
        else
            itemGroupRepo.save((ItemGroup) node);
    }

    boolean treeWithRootContainsNode(ItemHierarchyNode root, ItemHierarchyNode nodeToSearch) {
        if (root.equals(nodeToSearch))
            return true;

        if (root.isLeaf())
            return false;

        for (ItemHierarchyNode childNode : getNodesByParent(root))
            if (treeWithRootContainsNode(childNode, nodeToSearch))
                return true;

        return false;
    }

    public void saveGroup(String name) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setTitle(name);
        itemGroupRepo.save(itemGroup);
    }

    public interface ItemHierarchyNode {
        long getId();

        boolean isLeaf();

        String getTitle();

        ItemHierarchyNode getParentGroup();

        void setParentGroup(ItemGroup group);
    }
}
