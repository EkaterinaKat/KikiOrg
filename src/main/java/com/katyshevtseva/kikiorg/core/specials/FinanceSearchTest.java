package com.katyshevtseva.kikiorg.core.specials;

import com.katyshevtseva.kikiorg.core.repo.TestRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.*;

@Service
@RequiredArgsConstructor
public class FinanceSearchTest {
    private final TestRepo testRepo;
    private final FinanceSearchService searchService;
    private final FinanceService financeService;

//    @PostConstruct
    private void test() {
        System.out.println(searchService.search(getRequest1()).size() + " - " + testRepo.count1(getIds(getSomeAccounts())));
        System.out.println(searchService.search(getRequest2()).size() + " - " + testRepo.count2(getIds(getSomeAccounts())));
        System.out.println(searchService.search(getRequest3()).size() + " - " + testRepo.count3(getIds(getSomeAccounts())));
        System.out.println(searchService.search(getRequest4()).size() + " - " + testRepo.count4(getIds(getSomeSources())));
        System.out.println(searchService.search(getRequest5()).size() + " - " + testRepo.count5(getIds(getSomeAccounts()), getIds(getSomeItems())));
        System.out.println(searchService.search(getRequest6()).size() + " - " + testRepo.count6());
    }

    private SearchRequest getRequest1() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(REPLENISHMENT);

        request.setMinAmount("600");
        request.setMaxAmount("");

        request.setStart(LocalDate.of(2020, 11, 5));
        request.setEnd(LocalDate.of(2021, 3, 25));

        request.setFrom(new ArrayList<>(), getAllSources());
        request.setTo(getSomeAccounts(), getAllAccounts());

        return request;
    }

    private SearchRequest getRequest2() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(EXPENSE);

        request.setMinAmount("600");
        request.setMaxAmount("1200");

        request.setStart(null);
        request.setEnd(LocalDate.of(2021, 1, 25));

        request.setFrom(getSomeAccounts(), getAllAccounts());
        request.setTo(new ArrayList<>(), getAllItems());

        return request;
    }

    private SearchRequest getRequest3() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(TRANSFER);

        request.setMinAmount(null);
        request.setMaxAmount("1500");

        request.setStart(LocalDate.of(2020, 11, 6));

        request.setFrom(new ArrayList<>(), getAllAccounts());
        request.setTo(getSomeAccounts(), getAllAccounts());

        return request;
    }

    private SearchRequest getRequest4() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(REPLENISHMENT);

        request.setMinAmount("10000");

        request.setEnd(LocalDate.of(2021, 2, 15));

        request.setFrom(getSomeSources(), getAllSources());
        request.setTo(new ArrayList<>(), getAllAccounts());

        return request;
    }

    private SearchRequest getRequest5() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(EXPENSE);

        request.setMinAmount("1000");
        request.setMaxAmount("15000");

        request.setStart(LocalDate.of(2020, 10, 11));
        request.setEnd(LocalDate.of(2021, 1, 25));

        request.setFrom(getSomeAccounts(), getAllAccounts());
        request.setTo(getSomeItems(), getAllItems());

        return request;
    }

    private SearchRequest getRequest6() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(TRANSFER);

        request.setEnd(null);

        request.setFrom(new ArrayList<>(), getAllAccounts());
        request.setTo(new ArrayList<>(), getAllAccounts());

        return request;
    }

    private List<OperationEnd> getAllAccounts() {
        return financeService.getAllAccounts().stream().map(account -> (OperationEnd) account).collect(Collectors.toList());
    }

    private List<OperationEnd> getSomeAccounts() {
        return financeService.getAllAccounts().stream().filter(account -> account.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<OperationEnd> getAllSources() {
        return financeService.getAllSources().stream().map(source -> (OperationEnd) source).collect(Collectors.toList());
    }

    private List<OperationEnd> getSomeSources() {
        return financeService.getAllSources().stream().filter(source -> source.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<OperationEnd> getAllItems() {
        return financeService.getAllItems().stream().map(item -> (OperationEnd) item).collect(Collectors.toList());
    }

    private List<OperationEnd> getSomeItems() {
        return financeService.getAllItems().stream().filter(item -> item.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<Long> getIds(List<OperationEnd> endList) {
        return endList.stream().map(OperationEnd::getId).collect(Collectors.toList());
    }
}
