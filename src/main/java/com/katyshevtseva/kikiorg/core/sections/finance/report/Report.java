package com.katyshevtseva.kikiorg.core.sections.finance.report;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<ReportSegment> segments = new ArrayList<>();
    private long total = 0;
    private String title;

    public Report(String title) {
        this.title = title;
    }

    void addSegment(ReportSegment segment) {
        segments.add(segment);
        total += segment.getAmount();
        recalculatePercents();
    }

    private void recalculatePercents() {
        for (ReportSegment segment : segments) {
            if (total == 0)
                segment.setPercent(0);
            else
                segment.setPercent((int) ((segment.getAmount() * 100) / total));
        }
    }

    public List<ReportSegment> getSegments() {
        return segments;
    }

    public long getTotal() {
        return total;
    }

    public String getTitle() {
        return title;
    }

}
