package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountDeleteService {
    private final AccountGroupService accountGroupService;
    private final CheckLineRepo checkLineRepo;
    private final HuddleRepo huddleRepo;
    private final ExpenseRepo expenseRepo;
    private final ReplenishmentRepo replenishmentRepo;
    private final TransferRepo transferRepo;
    private final FinanceService financeService;
    private final OperationDeletionService operationDeletionService;
    private final AccountRepo accountRepo;


    public List<Account> getSubstituteAccounts(Account account) {
        if (account == null)
            return new ArrayList<>();

        return accountRepo.findAllByCurrency(account.getCurrency()).stream()
                .filter(account1 -> !account1.equals(account))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Account accountToDelete, Account substituteAccount) throws Exception {
        if (accountToDelete.equals(substituteAccount)) {
            throw new Exception("accountToDelete.equals(substituteAccount)");
        }
        if (accountToDelete.getCurrency() != substituteAccount.getCurrency()) {
            throw new Exception("accountToDelete.getCurrency()!=substituteAccount.getCurrency()");
        }

        deleteFromEverywhere(accountToDelete);

        //пополнения
        for (Replenishment replenishment : replenishmentRepo.findByAccount(accountToDelete)) {
            operationDeletionService.deleteOperation(replenishment);
            financeService.addReplenishment(substituteAccount, replenishment.getAmount(),
                    replenishment.getSource(), replenishment.getDate());
        }

        //расходы
        for (Expense expense : expenseRepo.findByAccount(accountToDelete)) {
            operationDeletionService.deleteOperation(expense);
            financeService.addExpense(substituteAccount, expense.getAmount(), expense.getItem(), expense.getDate(),
                    expense.getNecessity(), expense.getComment(), expense.getMoveToNextMonth());
        }

        //переводы на счет
        for (Transfer transfer : transferRepo.findAllByTo(accountToDelete)) {
            operationDeletionService.deleteOperation(transfer);
            financeService.addTransfer(transfer.getFrom(), substituteAccount,
                    transfer.getGoneAmount(), transfer.getCameAmount(), transfer.getDate());
        }

        //переводы со счета
        for (Transfer transfer : transferRepo.findAllByFrom(accountToDelete)) {
            operationDeletionService.deleteOperation(transfer);
            financeService.addTransfer(substituteAccount, transfer.getTo(),
                    transfer.getGoneAmount(), transfer.getCameAmount(), transfer.getDate());
        }

        //удаление возвратных переводов
        for (Transfer transfer : transferRepo.findAllByFrom(substituteAccount)) {
            if (transfer.getTo().equals(substituteAccount)) {
                if (transfer.getCameAmount() != transfer.getGoneAmount()) {
                    throw new Exception("Полундра!");
                }
                transferRepo.delete(transfer);
            }
        }

        accountRepo.delete(accountToDelete);
    }

    private void deleteFromEverywhere(Account account) {
        for (AccountGroup accountGroup : accountGroupService.getAll()) {
            if (accountGroup.getAccounts().contains(account)) {
                accountGroupService.delete(accountGroup);
            }
        }

        checkLineRepo.deleteAll(checkLineRepo.findAllByAccount(account));

        for (Huddle huddle : huddleRepo.findAll()) {
            if (huddle.getAccounts().contains(account)) {
                huddleRepo.delete(huddle);
            }
        }
    }
}
