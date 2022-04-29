package com.katyshevtseva.kikiorg.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class TestsMain {
    private final FinanceTest financeTest;
    private final FinanceSearchTest financeSearchTest;
    private final HabitDescTest habitDescTest;

    @PostConstruct
    public void test() {
        financeTest.test();
//        financeSearchTest.test(); //не работает
        habitDescTest.test();
    }
}
