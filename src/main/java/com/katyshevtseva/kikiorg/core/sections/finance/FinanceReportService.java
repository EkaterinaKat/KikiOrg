package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
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
    @Autowired
    private OwnerAdapterService adapter;

    public Report getHeadReport(Date startDate, Date endDate) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getTopLevelNodesForCurrentUser();
        return getReportByNodes(nodes, startDate, endDate, "All Expenses");
    }

    public Report getReportByRoot(ItemHierarchyNode root, Date startDate, Date endDate) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getNodesByParentForCurrentUser(root);
        return getReportByNodes(nodes, startDate, endDate, root.getTitle());
    }

    private Report getReportByNodes(List<ItemHierarchyNode> nodes, Date startDate, Date endDate, String title) {
        Report report = new Report(title);
        for (ItemHierarchyNode node : nodes) {
            long amount = getAmountByNodeAndPeriodForCurrentUser(node, startDate, endDate);
            if (amount != 0)
                report.addSegment(new ExpensesSegment(node, amount));
        }
        return report;
    }

    private long getAmountByNodeAndPeriodForCurrentUser(ItemHierarchyNode node, Date startDate, Date endDate) {
        if (node.isLeaf())
            return getAmountByItemAndPeriodForCurrentUser(((ItemHierarchyLeaf) node).getItem(), startDate, endDate);

        long amount = 0;
        for (ItemHierarchyNode childNode : itemHierarchyService.getNodesByParentForCurrentUser(node))
            amount += getAmountByNodeAndPeriodForCurrentUser(childNode, startDate, endDate);
        return amount;
    }

    private long getAmountByItemAndPeriodForCurrentUser(Item item, Date startDate, Date endDate) {
        long amount = 0;
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(startDate, endDate);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : adapter.getAccountsForCurrentUser()) {
                List<Expense> expenses = expenseRepo.findByItemAndDateEntityAndAccount(item, dateEntity, account);
                for (Expense expense : expenses) {
                    amount += expense.getAmount();
                }
            }
        }
        return amount;
    }

    public class Report {
        private List<ExpensesSegment> segments = new ArrayList<>();
        private long total = 0;
        private String title;

        public Report(String title) {
            this.title = title;
        }

        void addSegment(ExpensesSegment segment) {
            segments.add(segment);
            total += segment.getAmount();
            recalculatePercents();
        }

        private void recalculatePercents() {
            for (ExpensesSegment segment : segments) {
                if (total == 0)
                    segment.setPercent(0);
                else
                    segment.setPercent((int) ((segment.getAmount() * 100) / total));
            }
        }

        public List<ExpensesSegment> getSegments() {
            return segments;
        }

        public long getTotal() {
            return total;
        }

        public String getTitle() {
            return title;
        }
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

        public String getTitle() {
            return node.getTitle();
        }
    }
}
