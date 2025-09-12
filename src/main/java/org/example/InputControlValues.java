package org.example;

public class InputControlValues {
    private int frame;
    private int roll;
    private int remaining;

    public InputControlValues() {
        this.frame = 1;
        this.roll = 1;
        this.remaining = 10;
    }

    // Getters and setters
    public int getFrame() { return frame; }
    public void setFrame(int frame) { this.frame = frame; }
    public int getRoll() { return roll; }
    public void setRoll(int roll) { this.roll = roll; }
    public int getRemaining() { return remaining; }
    public void setRemaining(int remaining) { this.remaining = remaining; }
}
