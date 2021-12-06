package io.app.adfly.domain.dto;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.List;

public class PaginatedRequest {
    private int StartAt = 0;

    public int getStartAt() {
        return StartAt;
    }

    public void setStartAt(int startAt) {
        StartAt = startAt;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public List<String> getSortFields() {
        return SortFields;
    }

    public void setSortFields(List<String> sortFields) {
        SortFields = sortFields;
    }

    private int Count = 10;
    private List<String> SortFields;

}
