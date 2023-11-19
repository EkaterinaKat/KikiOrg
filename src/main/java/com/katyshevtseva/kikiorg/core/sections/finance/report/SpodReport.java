package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.general.ReportCell;

import java.util.ArrayList;
import java.util.List;

// Single period one direction (income or outgo) report
public class SpodReport {
    private final String title;
    private final List<Line> lines = new ArrayList<>();
    private Long total;
    private List<List<ReportCell>> table;
    private PieChartData pieChartData;

    public SpodReport(String title) {
        this.title = title;
    }

    public void addLine(String title, Long value) {
        lines.add(new Line(title, value));
        resetCalculatedData();
    }

    public void addLines(List<Line> lines1) {
        lines.addAll(lines1);
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

        List<ReportCell> headline = new ArrayList<>();

        headline.add(ReportCell.builder().text("Total").width(180).build());
        headline.add(ReportCell.builder().text(getTotal() + "").width(100).build());
        headline.add(ReportCell.builder().text("100%").width(100).build());
        table.add(headline);

        for (PieChartData.Segment segment : pieChartData.getGetSegmentList()) {
            List<ReportCell> line = new ArrayList<>();

            line.add(ReportCell.builder().text(segment.getTitle()).width(180).build());
            line.add(ReportCell.builder().text(segment.getAmount() + "").width(100).build());
            line.add(ReportCell.builder().text(segment.getPercent() + "%").width(100).build());
            table.add(line);
        }
    }

    private void initializePieChartData() {
        pieChartData = new PieChartData(title);
        for (Line line : lines) {
            pieChartData.addSegment(new PieChartData.Segment(line.value.intValue(), line.title, null)); //todo
        }
    }

    static class Line {
        private final String title;
        private final Long value;

        public Line(String title, Long value) {
            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public Long getValue() {
            return value;
        }
    }
}
