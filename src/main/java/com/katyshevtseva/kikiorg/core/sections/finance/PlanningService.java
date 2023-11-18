package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.AccountGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.PotentialExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.report.BaseReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService;
import com.katyshevtseva.kikiorg.core.setting.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.getCurrentMonthPeriod;
import static com.katyshevtseva.kikiorg.core.setting.SettingService.FIN_PLAN_ACC_GR_ID;
import static com.katyshevtseva.kikiorg.core.setting.SettingService.FIN_PLAN_UPPER_LIMIT;

@Service
@RequiredArgsConstructor
public class PlanningService {
    private final PotentialExpenseRepo potentialExpenseRepo;
    private final SettingService settingService;
    private final AccountGroupRepo accountGroupRepo;
    private final BaseReportService baseReportService;

    public void addPotentialExpense(long amount, Item item, Necessity necessity, String comment) {
        PotentialExpense expense = new PotentialExpense();
        expense.setAmount(amount);
        expense.setItem(item);
        expense.setComment(comment);
        expense.setNecessity(necessity);
        potentialExpenseRepo.saveAndFlush(expense);
    }

    public void delete(PotentialExpense potentialExpense) {
        potentialExpenseRepo.delete(potentialExpense);
    }

    public void setSelectedAccountGroup(AccountGroup group) {
        settingService.save(FIN_PLAN_ACC_GR_ID, "" + group.getId());
    }

    public AccountGroup getSelectedAccountGroupOrNull() {
        Optional<AccountGroup> optionalAccountGroup = settingService.getValue(FIN_PLAN_ACC_GR_ID)
                .map(Long::parseLong).map(accountGroupRepo::findById).orElse(null);
        return optionalAccountGroup == null ? null : optionalAccountGroup.orElse(null);
    }

    public void setUpperLimit(int limit) {
        settingService.save(FIN_PLAN_UPPER_LIMIT, "" + limit);
    }

    public Integer getUpperLimit() {
        return settingService.getValue(FIN_PLAN_UPPER_LIMIT).map(Integer::parseInt).orElse(0);
    }

    public String getPlanningInfo() {
        Integer upperLimit = getUpperLimit();
        StringBuilder result = new StringBuilder("Planned upper limit: ").append(upperLimit).append("\n");

        List<Operation> operations = getCurrentMonthOperationsWithoutGrouping();

        int realOutgo = 0;
        int realAndPotentialOutgo = 0;

        for (Operation operation : operations) {
            realAndPotentialOutgo += operation.getGoneAmount();
            if (!(operation instanceof PotentialExpense)) {
                realOutgo += operation.getGoneAmount();
            }
        }

        result.append("Current outgo: ").append(realOutgo).append(" ")
                .append(realOutgo > upperLimit ? "EXCEEDS THE LIMIT" : "☑")
                .append("\n");

        result.append("Potential outgo: ").append(realAndPotentialOutgo).append(" ")
                .append(realAndPotentialOutgo > upperLimit ? "EXCEEDS THE LIMIT" : "☑");

        return result.toString();
    }

    public List<Operation> getCurrentMonthOperationsWithoutGrouping() {
        AccountGroup group = getSelectedAccountGroupOrNull();
        if (group == null) {
            return new ArrayList<>();
        }

        ReportPeriodService.ReportPeriod period = getCurrentMonthPeriod();
        List<Operation> operations = new ArrayList<>();
        operations.addAll(potentialExpenseRepo.findAll());
        operations.addAll(baseReportService.getExpenses(period.getPeriod(), group.getAccounts(), null));
        operations.addAll(baseReportService.getTransfersFromAccounts(period.getPeriod(), group.getAccounts()));
        return operations;
    }

    public Map<String, Integer> getCurrentMonthItemAmountMap() {
        List<Operation> operations = getCurrentMonthOperationsWithoutGrouping();

        Map<String, Integer> itemAmountMap = new HashMap<>();
        for (Operation operation : operations) {
            if (operation instanceof Expense || operation instanceof PotentialExpense) {
                Item item = operation instanceof Expense ? ((Expense) operation).getItem()
                        : ((PotentialExpense) operation).getItem();
                HierarchyNode parentNode = getFirstParent(item);
                int existingSum = itemAmountMap.getOrDefault(parentNode.getTitle(), 0);
                itemAmountMap.put(parentNode.getTitle(), (int) (existingSum + operation.getGoneAmount()));
            } else if (operation instanceof Transfer) {
                int existingSum = itemAmountMap.getOrDefault(operation.getToTitle(), 0);
                itemAmountMap.put(operation.getToTitle(), (int) (existingSum + operation.getGoneAmount()));
            } else {
                throw new RuntimeException();
            }
        }

        return itemAmountMap;
    }

    public Map<String, Integer> getCurrentMonthNecessityAmountMap() {
        List<Operation> operations = getCurrentMonthOperationsWithoutGrouping();

        Map<String, Integer> necessityAmountMap = new HashMap<>();
        for (Operation operation : operations) {
            if (operation instanceof Expense || operation instanceof PotentialExpense) {
                Necessity necessity = operation instanceof Expense ? ((Expense) operation).getNecessity()
                        : ((PotentialExpense) operation).getNecessity();
                int existingSum = necessityAmountMap.getOrDefault(necessity.toString(), 0);
                necessityAmountMap.put(necessity.toString(), (int) (existingSum + operation.getGoneAmount()));
            } else if (operation instanceof Transfer) {
                int existingSum = necessityAmountMap.getOrDefault("transfer", 0);
                necessityAmountMap.put("transfer", (int) (existingSum + operation.getGoneAmount()));
            } else {
                throw new RuntimeException();
            }
        }

        return necessityAmountMap;
    }

    private HierarchyNode getFirstParent(Item item) {
        HierarchyNode result = item;
        while (result.getParentGroup() != null) {
            result = result.getParentGroup();
        }
        return result;
    }
}
