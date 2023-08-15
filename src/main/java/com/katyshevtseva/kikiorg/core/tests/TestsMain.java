package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.general.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.katyshevtseva.general.GeneralUtils.getFailedBanner;
import static com.katyshevtseva.general.GeneralUtils.getSuccessBanner;

@Service
@RequiredArgsConstructor
public class TestsMain {
    private final FinanceTest financeTest;
    private final FinanceSearchTest financeSearchTest;
    private final HabitTest habitTest;
    private final FinanceMulticurrencyTest financeMulticurrencyTest;
    private final TrackerTest trackerTest;

//    @PostConstruct
    public void test() {
        test(financeTest);
        test(financeSearchTest);
        test(financeMulticurrencyTest);
        test(habitTest);
        test(trackerTest);
    }

    private void test(TestClass testClass) {
        System.out.println(GeneralUtils.getHeader(testClass.getTitle()));
        boolean success = testClass.test();
        if (success) {
            System.out.println("\n" + getSuccessBanner(testClass.getTitle()));
        } else {
            System.out.println("\n" + getFailedBanner(testClass.getTitle()));
        }
    }
}
