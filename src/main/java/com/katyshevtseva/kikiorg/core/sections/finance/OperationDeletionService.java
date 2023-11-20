package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationDeletionService {
    private final ExpenseRepo expenseRepo;
    private final ReplenishmentRepo replenishmentRepo;
    private final TransferRepo transferRepo;
    private final CalculationService calculationService;
    private final AccountRepo accountRepo;

    public void deleteOperation(FinanceOperationService.Operation operation) {
        switch (operation.getType()) {
            case EXPENSE:
                expenseRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Expense) operation).getAccount());
                break;
            case REPLENISHMENT:
                replenishmentRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Replenishment) operation).getAccount());
                break;
            case TRANSFER:
                transferRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Transfer) operation).getTo());
                recalculateAndRewriteAccountAmount(((Transfer) operation).getFrom());
        }
    }

    private void recalculateAndRewriteAccountAmount(Account account) {
        rewriteAccountAmount(account, calculationService.calculateAccountAmountByOperations(account));
    }

    private void rewriteAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(amount);
        accountRepo.save(actualAccount);
    }
}
