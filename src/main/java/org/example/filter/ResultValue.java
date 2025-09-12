package org.example.filter;

public class ResultValue {
    private int sum;

    public ResultValue() {
        this.sum = 0;
    }

    public ResultValue(int sum) {
        this.sum = sum;
    }

    // Getters and setters
    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }

    @Override
    public String toString() {
        return String.format("ResultValue(sum=%d)", sum);
    }
}
