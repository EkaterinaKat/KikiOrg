package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemMergeService {
    private final ExpenseRepo expenseRepo;
    private final ItemRepo itemRepo;

    @Transactional
    public void merge(Item itemToMerge, Item destItem) throws Exception {
        if (itemToMerge.equals(destItem)) {
            throw new Exception("itemToMerge.equals(destItem)");
        }

        for (Expense expense : expenseRepo.findByItem(itemToMerge)) {
            expense.setItem(destItem);
            expenseRepo.save(expense);
        }

        itemRepo.delete(itemToMerge);
    }
}
