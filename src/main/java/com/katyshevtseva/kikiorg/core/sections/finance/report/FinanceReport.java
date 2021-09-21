package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class FinanceReport {
    private final String title;
    private final List<Line> lines = new ArrayList<>();

    public FinanceReport(String title) {
        this.title = title;
    }

    public void addLine(String title, Long value) {
        lines.add(new Line(title, value, null));
    }

    public void addLine(String title, Long value, ItemGroup itemGroup) {
        lines.add(new Line(title, value, itemGroup));
    }

    public List<List<ReportCell>> getTable() {
        return null;
    }

    public PieChartData getPieChartData() {
        return null;
    }

    public Long getTotal() {
        return lines.stream().map(line -> line.value).reduce(Long::sum).orElse(0L);
    }

    static class Line {
        private final String title;
        private final Long value;
        private final ItemGroup itemGroup;

        public Line(String title, Long value, ItemGroup itemGroup) {
            this.title = title;
            this.value = value;
            this.itemGroup = itemGroup;
        }
    }
}
