package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinanceReportService {
    @Autowired
    private ItemHierarchyService itemHierarchyService;
    @Autowired
    private DateService dateService;
    @Autowired
    private ExpenseRepo expenseRepo;

    public List<ExpensesSegment> getHeadReport(Date startDate, Date endDate) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getTopLevelNodesForCurrentUser();
        return getReportByNodes(nodes, startDate, endDate);
    }

    public List<ExpensesSegment> getReportByRoot(ItemHierarchyNode root, Date startDate, Date endDate) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getNodesByParentForCurrentUser(root);
        return getReportByNodes(nodes, startDate, endDate);
    }

    private List<ExpensesSegment> getReportByNodes(List<ItemHierarchyNode> nodes, Date startDate, Date endDate) {
        List<ExpensesSegment> segments = new ArrayList<>();
        for (ItemHierarchyNode node : nodes) {
            segments.add(new ExpensesSegment(node, getAmountByNodeAndPeriod(node, startDate, endDate)));
        }
        setSegmentsPercents(segments);
        return segments;
    }

    private void setSegmentsPercents(List<ExpensesSegment> segments) {
        long total = 0;
        for (ExpensesSegment segment : segments)
            total += segment.getAmount();
        for (ExpensesSegment segment : segments) {
            segment.setPercent((int) ((segment.getAmount() * 100) / total));
        }
    }

    private long getAmountByNodeAndPeriod(ItemHierarchyNode node, Date startDate, Date endDate) {
        if (node instanceof Item)
            return getAmountByItemAndPeriod((Item) node, startDate, endDate);

        long amount = 0;
        for (ItemHierarchyNode childNode : itemHierarchyService.getNodesByParentForCurrentUser(node))
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

    public class ExpensesSegment {
        private ItemHierarchyNode node;
        private int percent;
        private long amount;

        ExpensesSegment(ItemHierarchyNode node, long amount) {
            this.node = node;
            this.amount = amount;
        }

        public ItemHierarchyNode getNode() {
            return node;
        }

        public int getPercent() {
            return percent;
        }

        public long getAmount() {
            return amount;
        }

        void setPercent(int percent) {
            this.percent = percent;
        }
    }
}
