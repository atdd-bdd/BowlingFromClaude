package org.example.bowling;

public class FrameDisplay {
    private String frame;
    private String mark1;
    private String mark2;
    private String mark3; // Only shown on 10th frame
    private String totalScore;

    public FrameDisplay() {
        this.frame = "";
        this.mark1 = "";
        this.mark2 = "";
        this.mark3 = "";
        this.totalScore = "";
    }

    // Getters and setters
    public String getFrame() { return frame; }
    public void setFrame(String frame) { this.frame = frame; }
    public String getMark1() { return mark1; }
    public void setMark1(String mark1) { this.mark1 = mark1; }
    public String getMark2() { return mark2; }
    public void setMark2(String mark2) { this.mark2 = mark2; }
    public String getMark3() { return mark3; }
    public void setMark3(String mark3) { this.mark3 = mark3; }
    public String getTotalScore() { return totalScore; }
    public void setTotalScore(String totalScore) { this.totalScore = totalScore; }
}