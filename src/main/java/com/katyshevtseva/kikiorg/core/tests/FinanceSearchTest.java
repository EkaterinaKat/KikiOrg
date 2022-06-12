package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.kikiorg.core.repo.TestRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.search.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.search.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.general.GeneralUtils.getFailedBanner;
import static com.katyshevtseva.general.GeneralUtils.getSuccessBanner;
import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.*;

@Service
@RequiredArgsConstructor
public class FinanceSearchTest {
    private final TestRepo testRepo;
    private final FinanceSearchService searchService;
    private final FinanceService financeService;

    public void test() {
        int result11 = searchService.search(getRequest1()).size();
        long result12 = testRepo.count1(getIds(getSomeAccounts()));
        System.out.println(result11 + " - " + result12);

        int result21 = searchService.search(getRequest2()).size();
        long result22 = testRepo.count2(getIds(getSomeAccounts()));
        System.out.println(result21 + " - " + result22);

        int result31 = searchService.search(getRequest3()).size();
        long result32 = testRepo.count3(getIds(getSomeAccounts()));
        System.out.println(result31 + " - " + result32);

        int result41 = searchService.search(getRequest4()).size();
        long result42 = testRepo.count4(getIds(getSomeSources()));
        System.out.println(result41 + " - " + result42);

        int result51 = searchService.search(getRequest5()).size();
        long result52 = testRepo.count5(getIds(getSomeAccounts()), getIds(getSomeItems()));
        System.out.println(result51 + " - " + result52);

        int result61 = searchService.search(getRequest6()).size();
        long result62 = testRepo.count6();
        System.out.println(result61 + " - " + result62);

        if (result11 == result12
                && result21 == result22
                && result31 == result32
                && result41 == result42
                && result51 == result52
                && result61 == result62) {
            System.out.println(getSuccessBanner("FINANCE SEARCH"));
        } else {
            System.out.println(getFailedBanner("FINANCE SEARCH"));
        }
    }

    private SearchRequest getRequest1() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(REPLENISHMENT);

        request.setMinAmount("600");

        request.setStart(LocalDate.of(2020, 11, 5));
        request.setEnd(LocalDate.of(2021, 3, 25));

        request.setTo(getSomeAccounts());

        return request;
    }

    private SearchRequest getRequest2() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(EXPENSE);

        request.setMinAmount("600");
        request.setMaxAmount("1200");

        request.setEnd(LocalDate.of(2021, 1, 25));

        request.setFrom(getSomeAccounts());

        return request;
    }

    private SearchRequest getRequest3() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(TRANSFER);

        request.setMaxAmount("1500");

        request.setStart(LocalDate.of(2020, 11, 6));

        request.setTo(getSomeAccounts());

        return request;
    }

    private SearchRequest getRequest4() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(REPLENISHMENT);

        request.setMinAmount("10000");

        request.setEnd(LocalDate.of(2021, 2, 15));

        request.setFrom(getSomeSources());

        return request;
    }

    private SearchRequest getRequest5() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(EXPENSE);

        request.setMinAmount("1000");
        request.setMaxAmount("15000");

        request.setStart(LocalDate.of(2020, 10, 11));
        request.setEnd(LocalDate.of(2021, 1, 25));

        request.setFrom(getSomeAccounts());
        request.setTo(getSomeItems());

        return request;
    }

    private SearchRequest getRequest6() {
        SearchRequest request = new SearchRequest();
        request.setOperationType(TRANSFER);

        return request;
    }

    private List<OperationEnd> getSomeAccounts() {
        return financeService.getAllAccounts().stream().filter(account -> account.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<OperationEnd> getSomeSources() {
        return financeService.getAllSources().stream().filter(source -> source.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<OperationEnd> getSomeItems() {
        return financeService.getAllItems().stream().filter(item -> item.getTitle().length() % 2 == 0).collect(Collectors.toList());
    }

    private List<Long> getIds(List<OperationEnd> endList) {
        return endList.stream().map(OperationEnd::getId).collect(Collectors.toList());
    }
}
