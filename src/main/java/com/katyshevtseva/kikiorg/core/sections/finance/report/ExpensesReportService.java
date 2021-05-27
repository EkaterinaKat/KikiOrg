package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType.FROM_USER_ACCOUNTS;

@Service
public class ExpensesReportService {
    @Autowired
    private ItemHierarchyService itemHierarchyService;
    @Autowired
    private DateService dateService;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private TransfersReportService transfersReportService;

    public Report getHeadReport(Period period) {
        Report report = getReportByNodes(itemHierarchyService.getTopLevelNodes(), period, "All Expenses");
        TransferSegment transferSegment = transfersReportService.getRootTransferSegment(period, FROM_USER_ACCOUNTS);
        if (transferSegment.getAmount() > 0)
            report.addSegment(transferSegment);
        return report;
    }

    Report getReportByRoot(ItemHierarchyNode root, Period period) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getNodesByParent(root);
        return getReportByNodes(nodes, period, root.getTitle());
    }

    private Report getReportByNodes(List<ItemHierarchyNode> nodes, Period period, String title) {
        Report report = new Report(title);
        for (ItemHierarchyNode node : nodes) {
            long amount = getAmountByNodeAndPeriod(node, period);
            if (amount != 0)
                report.addSegment(new ExpensesSegment(this, node, amount));
        }
        return report;
    }

    private long getAmountByNodeAndPeriod(ItemHierarchyNode node, Period period) {
        if (node.isLeaf())
            return getAmountByItemAndPeriod(((ItemHierarchyLeaf) node).getItem(), period);

        long amount = 0;
        for (ItemHierarchyNode childNode : itemHierarchyService.getNodesByParent(node))
            amount += getAmountByNodeAndPeriod(childNode, period);
        return amount;
    }

    private long getAmountByItemAndPeriod(Item item, Period period) {
        long amount = 0;
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : financeService.getAllAccounts()) {
                List<Expense> expenses = expenseRepo.findByItemAndDateEntityAndAccount(item, dateEntity, account);
                for (Expense expense : expenses) {
                    amount += expense.getAmount();
                }
            }
        }
        return amount;
    }
}
