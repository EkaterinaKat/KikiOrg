package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public List<ItemHierarchyNode> getNodesByParent(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode instanceof Item)
            return nodes;
        nodes.addAll(itemRepo.findByParentGroup((ItemGroup) parentNode));
        nodes.addAll(itemGroupRepo.findByParentGroup((ItemGroup) parentNode));
        return nodes;
    }

    public long getAmountByNodeAndPeriod(ItemHierarchyNode node, Date startDate, Date endDate) {
        if (node instanceof Item)
            return getAmountByItemAndPeriod((Item) node, startDate, endDate);

        long amount = 0;
        for (ItemHierarchyNode childNode : getNodesByParent(node))
            amount += getAmountByNodeAndPeriod(childNode, startDate, endDate);
        return amount;
    }

    private long getAmountByItemAndPeriod(Item item, Date startDate, Date endDate) {
        long amount = 0;
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(startDate, endDate);
        for (DateEntity dateEntity : dateEntities) {
            List<Expense> expenses = expenseRepo.findByItemAndDateEntity(item, dateEntity);
            for (Expense expense : expenses) {
                amount += expense.getAmount();
            }
        }
        return amount;
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
}
