package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class FinanceReport {
    private final String title;
    private final List<Line> lines = new ArrayList<>();
    private Long total;
    private List<List<ReportCell>> table;
    private PieChartData pieChartData;

    public FinanceReport(String title) {
        this.title = title;
    }

    public void addLine(String title, Long value) {
        lines.add(new Line(title, value, null));
        resetCalculatedData();
    }

    public void addLine(String title, Long value, ItemGroup itemGroup) {
        lines.add(new Line(title, value, itemGroup));
        resetCalculatedData();
    }

    private void resetCalculatedData() {
        total = null;
        table = null;
        pieChartData = null;
    }

    public List<List<ReportCell>> getTable() {
        if (table == null)
            initializeTable();
        return table;
    }

    public PieChartData getPieChartData() {
        if (pieChartData == null)
            initializePieChartData();
        return pieChartData;
    }

    public Long getTotal() {
        if (total == null)
            total = lines.stream().map(line -> line.value).reduce(Long::sum).orElse(0L);
        return total;
    }

    public String getTitle() {
        return title;
    }

    private void initializeTable() {
        if (pieChartData == null)
            initializePieChartData();

        table = new ArrayList<>();
        for (PieChartData.Segment segment : pieChartData.getGetSegmentList()) {
            List<ReportCell> line = new ArrayList<>();
            line.add(ReportCell.filled(segment.getTitle(), ReportCell.Color.WHITE, 200));
            line.add(ReportCell.filled(segment.getAmount() + "", ReportCell.Color.WHITE, 100));
            line.add(ReportCell.filled(segment.getPercent() + "", ReportCell.Color.WHITE, 100));
            table.add(line);
        }
    }

    private void initializePieChartData() {
        PieChartData pieChartData = new PieChartData(title);
        for (Line line : lines) {
            pieChartData.addSegment(new PieChartData.Segment(line.value.intValue(), line.title, null)); //todo
        }
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
