package org.example;

// FrameValues.java
public class FrameValues {
    private int frame;
    private Roll roll1;
    private Roll roll2;
    private Roll roll3;
    private int score;
    private int totalScore;

    public static final int TBR = -1; // To Be Rolled
    public static final int TBS = -1; // To Be Scored

    public FrameValues() {
        this.frame = 1;
        this.roll1 = new Roll(Roll.TBR);
        this.roll2 = new Roll(Roll.TBR);
        this.roll3 = new Roll(Roll.TBR);
        this.score = TBS;
        this.totalScore = TBS;
    }

    public FrameValues(int frame) {
        this();
        this.frame = frame;
    }

    // Getters and setters
    public int getFrame() { return frame; }
    public void setFrame(int frame) { this.frame = frame; }
    public Roll getRoll1() { return roll1; }
    public void setRoll1(Roll roll1) { this.roll1 = roll1; }
    public Roll getRoll2() { return roll2; }
    public void setRoll2(Roll roll2) { this.roll2 = roll2; }
    public Roll getRoll3() { return roll3; }
    public void setRoll3(Roll roll3) { this.roll3 = roll3; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
}
