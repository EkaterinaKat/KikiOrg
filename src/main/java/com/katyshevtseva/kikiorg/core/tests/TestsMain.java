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

    @PostConstruct
    public void test() {
        test(financeTest, "financeTest");
        test(financeSearchTest, "financeSearchTest");
        test(financeMulticurrencyTest, "financeMulticurrencyTest");
        test(habitTest, "habitTest");
    }

    private void test(TestClass testClass, String title) {
        System.out.println(GeneralUtils.getHeader(title));
        boolean success = testClass.test();
        if (success) {
            System.out.println("\n" + getSuccessBanner(title));
        } else {
            System.out.println("\n" + getFailedBanner(title));
        }
    }
}
