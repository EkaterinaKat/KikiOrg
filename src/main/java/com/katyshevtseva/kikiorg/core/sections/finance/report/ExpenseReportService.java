package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerAdapterService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType.FROM_USER_ACCOUNTS;

@Service
public class ExpenseReportService {
    @Autowired
    private ItemHierarchyService itemHierarchyService;
    @Autowired
    private DateService dateService;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private OwnerAdapterService adapter;
    @Autowired
    private FinanceService financeService;

    public Report getHeadReport(Period period) {
        Report report = getReportByNodes(itemHierarchyService.getTopLevelNodesForCurrentUser(), period, "All Expenses");
        addTransferSegmentToRootReportIfNeeded(report, period);
        return report;
    }

    Report getReportByRoot(ItemHierarchyNode root, Period period) {
        List<ItemHierarchyNode> nodes = itemHierarchyService.getNodesByParentForCurrentUser(root);
        return getReportByNodes(nodes, period, root.getTitle());
    }

    private Report getReportByNodes(List<ItemHierarchyNode> nodes, Period period, String title) {
        Report report = new Report(title);
        for (ItemHierarchyNode node : nodes) {
            long amount = getAmountByNodeAndPeriodForCurrentUser(node, period);
            if (amount != 0)
                report.addSegment(new ExpensesSegment(this, node, amount));
        }
        return report;
    }

    private long getAmountByNodeAndPeriodForCurrentUser(ItemHierarchyNode node, Period period) {
        if (node.isLeaf())
            return getAmountByItemAndPeriodForCurrentUser(((ItemHierarchyLeaf) node).getItem(), period);

        long amount = 0;
        for (ItemHierarchyNode childNode : itemHierarchyService.getNodesByParentForCurrentUser(node))
            amount += getAmountByNodeAndPeriodForCurrentUser(childNode, period);
        return amount;
    }

    private long getAmountByItemAndPeriodForCurrentUser(Item item, Period period) {
        long amount = 0;
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
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

    private void addTransferSegmentToRootReportIfNeeded(Report report, Period period) {
        List<Transfer> transfers = financeService.getTransfersForCuByPeriod(period, FROM_USER_ACCOUNTS);
        long amount = 0;
        for (Transfer transfer : transfers) {
            if (transfer.isOuter())
                amount += transfer.getAmount();
        }
        if (amount > 0)
            report.addSegment(new TransferSegment(amount, "Transfers", true, this));
    }

    Report getTransfersReport(Period period) {
        List<Transfer> transfers = financeService.getTransfersForCuByPeriod(period, FROM_USER_ACCOUNTS);
        Map<Account, Long> accountAmountMap = new HashMap<>();
        for (Transfer transfer : transfers) {
            if (transfer.isOuter()) {
                long initialAmount = accountAmountMap.getOrDefault(transfer.getTo(), 0L);
                long increasedAmount = initialAmount + transfer.getAmount();
                accountAmountMap.put(transfer.getTo(), increasedAmount);
            }
        }
        Report report = new Report("Transfers");
        for (Map.Entry<Account, Long> entry : accountAmountMap.entrySet()) {
            report.addSegment(new TransferSegment(entry.getValue(), entry.getKey().getTitle(), false, this));
        }
        return report;
    }
}
