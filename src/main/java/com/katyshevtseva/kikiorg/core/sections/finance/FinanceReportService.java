package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
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

    private long getAmountByNodeAndPeriod(ItemHierarchyService.ItemHierarchyNode node, Date startDate, Date endDate) {
        if (node instanceof Item)
            return getAmountByItemAndPeriod((Item) node, startDate, endDate);

        long amount = 0;
        for (ItemHierarchyService.ItemHierarchyNode childNode : itemHierarchyService.getNodesByParentForCurrentUser(node))
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
}
