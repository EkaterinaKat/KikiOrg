package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerAdapterService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                report.addSegment(new ExpensesSegment(this, node, amount));
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
}
