package org.example.filter;

// DataFilter.java - Main filtering logic
import java.util.*;
import java.util.stream.Collectors;

public class DataFilter {
    private List<LabelValue> data;

    public DataFilter() {
        this.data = new ArrayList<>();
    }

    public DataFilter(List<LabelValue> data) {
        this.data = new ArrayList<>(data);
    }

    public void setData(List<LabelValue> data) {
        this.data = new ArrayList<>(data);
    }

    public void addData(LabelValue item) {
        this.data.add(item);
    }

    public void addData(String id, int value) {
        this.data.add(new LabelValue(id, value));
    }

    public List<LabelValue> getData() {
        return new ArrayList<>(data);
    }

    // Filter by ID and return sum
    public int filterByIDAndSum(String targetId) {
        ID target = new ID(targetId);
        return data.stream()
                .filter(item -> item.getId().equals(target))
                .mapToInt(LabelValue::getValue)
                .sum();
    }

    // Filter by ID and return sum (using ID object)
    public int filterByIDAndSum(ID targetId) {
        return data.stream()
                .filter(item -> item.getId().equals(targetId))
                .mapToInt(LabelValue::getValue)
                .sum();
    }

    // Filter using FilterValue object and return ResultValue
    public ResultValue filterBy(FilterValue filter) {
        if ("ID".equals(filter.getName())) {
            int sum = filterByIDAndSum(filter.getValue());
            return new ResultValue(sum);
        }
        return new ResultValue(0);
    }

    // Get filtered data (returns the actual items, not just sum)
    public List<LabelValue> getFilteredData(String targetId) {
        ID target = new ID(targetId);
        return data.stream()
                .filter(item -> item.getId().equals(target))
                .collect(Collectors.toList());
    }

    public List<LabelValue> getFilteredData(ID targetId) {
        return data.stream()
                .filter(item -> item.getId().equals(targetId))
                .collect(Collectors.toList());
    }
}
