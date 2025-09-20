package org.example.bowling;
import java.util.*;

public class BowlingGame {
    private List<Roll> rolls;
    private List<FrameValues> frameValues;
    private List<FrameDisplay> frameDisplays;
    private InputControlValues inputControl;
    private boolean gameComplete;

    public BowlingGame() {
        this.rolls = new ArrayList<>();
        this.frameValues = new ArrayList<>();
        this.frameDisplays = new ArrayList<>();
        this.inputControl = new InputControlValues();
        this.gameComplete = false;

        // Initialize 10 frames
        for (int i = 1; i <= 10; i++) {
            frameValues.add(new FrameValues(i));
            frameDisplays.add(new FrameDisplay());
        }
    }

    public void addRoll(Roll roll) {
        if (!roll.isValid()) {
            return; // Don't add invalid rolls
        }

        // Check if this roll would make the frame total > 10 (except for 10th frame)
        if (inputControl.getFrame() < 10 && inputControl.getRoll() == 2) {
            int previousRoll = rolls.get(rolls.size() - 1).getIntValue();
            if (previousRoll + roll.getIntValue() > 10) {
                return; // Invalid combination, don't add
            }
        }

        rolls.add(roll);
        updateInputControl();
    }

    private void updateInputControl() {
        if (rolls.isEmpty()) {
            inputControl.setFrame(1);
            inputControl.setRoll(1);
            inputControl.setRemaining(10);
            return;
        }

        // Parse through rolls to determine current position
        int rollIndex = 0;
        int currentFrame = 1;
        int currentRoll = 1;

        while (rollIndex < rolls.size() && currentFrame <= 10) {
            if (currentFrame < 10) {
                // Frames 1-9
                if (currentRoll == 1) {
                    if (rolls.get(rollIndex).getIntValue() == 10) {
                        // Strike - move to next frame
                        currentFrame++;
                        currentRoll = 1;
                    } else {
                        currentRoll = 2;
                    }
                } else {
                    // Second roll of frame
                    currentFrame++;
                    currentRoll = 1;
                }
            } else {
                // 10th frame
                if (currentRoll == 1) {
                    currentRoll = 2;
                } else if (currentRoll == 2) {
                    // Check if we need a third roll
                    int roll1 = getRollForFrame(10, 0);
                    int roll2 = getRollForFrame(10, 1);
                    if (roll1 == 10 || roll1 + roll2 == 10) {
                        currentRoll = 3;
                    } else {
                        currentFrame = 11; // Game complete
                    }
                } else {
                    currentFrame = 11; // Game complete
                }
            }
            rollIndex++;
        }

        inputControl.setFrame(Math.min(currentFrame, 10));
        inputControl.setRoll(Math.min(currentRoll, 3));

        // Calculate remaining pins
        int remaining = 10;
        if (currentFrame <= 10 && currentRoll == 2 && currentFrame < 10) {
            int firstRoll = getRollForFrame(currentFrame, 0);
            if (firstRoll != -1) {
                remaining = 10 - firstRoll;
            }
        }
        inputControl.setRemaining(remaining);

        // Check if game is complete
        gameComplete = currentFrame > 10;
    }

    private int getRollForFrame(int frame, int rollNum) {
        if (frame > 10 || rollNum < 0) return -1;

        int rollIndex = 0;
        int currentFrame = 1;

        // Navigate to the start of the target frame
        while (currentFrame < frame && rollIndex < rolls.size()) {
            if (currentFrame < 10) {
                if (rolls.get(rollIndex).getIntValue() == 10) {
                    rollIndex++; // Strike
                } else {
                    rollIndex += 2; // Two rolls
                }
            } else {
                // 10th frame handling would be here but we're looking for frame < 10
                break;
            }
            currentFrame++;
        }

        if (currentFrame == frame && rollIndex + rollNum < rolls.size()) {
            return rolls.get(rollIndex + rollNum).getIntValue();
        }

        return -1;
    }

    public void score() {
        updateFrameValues();
        updateFrameDisplays();
        updateInputControl();
    }

    private void updateFrameValues() {
        int rollIndex = 0;

        for (int frame = 0; frame < 10 && rollIndex < rolls.size(); frame++) {
            FrameValues fv = frameValues.get(frame);
            fv.setFrame(frame + 1);

            if (frame < 9) { // Frames 1-9
                // Set roll values
                if (rollIndex < rolls.size()) {
                    fv.setRoll1(rolls.get(rollIndex));
                    int roll1Value = rolls.get(rollIndex).getIntValue();

                    if (roll1Value == 10) {
                        // Strike
                        fv.setRoll2(new Roll("0"));
                        rollIndex++;

                        // Calculate score with next two rolls
                        int score = 10;
                        if (rollIndex < rolls.size()) {
                            score += rolls.get(rollIndex).getIntValue();
                            if (rollIndex + 1 < rolls.size()) {
                                int nextRoll = rolls.get(rollIndex + 1).getIntValue();
                                if (rolls.get(rollIndex).getIntValue() == 10 && frame < 8) {
                                    // Double strike
                                    score += nextRoll;
                                } else {
                                    score += nextRoll;
                                }
                            }
                        }
                        fv.setScore(score);
                    } else {
                        // Regular frame
                        rollIndex++;
                        if (rollIndex < rolls.size()) {
                            fv.setRoll2(rolls.get(rollIndex));
                            int roll2Value = rolls.get(rollIndex).getIntValue();
                            rollIndex++;

                            if (roll1Value + roll2Value == 10) {
                                // Spare
                                int score = 10;
                                if (rollIndex < rolls.size()) {
                                    score += rolls.get(rollIndex).getIntValue();
                                }
                                fv.setScore(score);
                            } else {
                                // Open frame
                                fv.setScore(roll1Value + roll2Value);
                            }
                        }
                    }
                }
            } else { // 10th frame
                if (rollIndex < rolls.size()) {
                    fv.setRoll1(rolls.get(rollIndex));
                    rollIndex++;
                }
                if (rollIndex < rolls.size()) {
                    fv.setRoll2(rolls.get(rollIndex));
                    rollIndex++;
                }
                if (rollIndex < rolls.size()) {
                    fv.setRoll3(rolls.get(rollIndex));
                } else {
                    fv.setRoll3(new Roll(Roll.TBR));
                }

                // Calculate 10th frame score
                int roll1 = fv.getRoll1().getIntValue();
                int roll2 = fv.getRoll2().getIntValue() != -1 ? fv.getRoll2().getIntValue() : 0;
                int roll3 = fv.getRoll3().getIntValue() != -1 ? fv.getRoll3().getIntValue() : 0;

                if (gameComplete) {
                    fv.setScore(roll1 + roll2 + roll3);
                } else {
                    fv.setScore(FrameValues.TBS);
                }
            }

            // Calculate total score
            if (frame == 0) {
                fv.setTotalScore(fv.getScore());
            } else {
                FrameValues prevFrame = frameValues.get(frame - 1);
                if (fv.getScore() != FrameValues.TBS && prevFrame.getTotalScore() != FrameValues.TBS) {
                    fv.setTotalScore(prevFrame.getTotalScore() + fv.getScore());
                } else {
                    fv.setTotalScore(FrameValues.TBS);
                }
            }
        }
    }

    private void updateFrameDisplays() {
        for (int frame = 0; frame < 10; frame++) {
            FrameValues fv = frameValues.get(frame);
            FrameDisplay fd = frameDisplays.get(frame);

            if (frame < 9) { // Frames 1-9
                int roll1 = fv.getRoll1().getIntValue();
                int roll2 = fv.getRoll2().getIntValue();

                if (roll1 == 10) {
                    // Strike
                    fd.setMark1("X");
                    fd.setMark2("");
                } else if (roll1 == 0) {
                    fd.setMark1("-");
                    if (roll2 == 10) {
                        fd.setMark2("/");
                    } else if (roll1 + roll2 == 10) {
                        fd.setMark2("/");
                    } else if (roll2 == 0) {
                        fd.setMark2("-");
                    } else if (roll2 != -1) {
                        fd.setMark2(String.valueOf(roll2));
                    }
                } else if (roll1 != -1) {
                    fd.setMark1(String.valueOf(roll1));
                    if (roll1 + roll2 == 10) {
                        fd.setMark2("/");
                    } else if (roll2 == 0) {
                        fd.setMark2("-");
                    } else if (roll2 != -1) {
                        fd.setMark2(String.valueOf(roll2));
                    }
                }

                if (fv.getTotalScore() != FrameValues.TBS) {
                    fd.setTotalScore(String.valueOf(fv.getTotalScore()));
                }
            } else { // 10th frame
                int roll1 = fv.getRoll1().getIntValue();
                int roll2 = fv.getRoll2().getIntValue();
                int roll3 = fv.getRoll3().getIntValue();

                // First roll
                if (roll1 == 10) {
                    fd.setMark1("X");
                } else if (roll1 == 0) {
                    fd.setMark1("-");
                } else if (roll1 != -1) {
                    fd.setMark1(String.valueOf(roll1));
                }

                // Second roll
                if (roll1 == 10) {
                    if (roll2 == 10) {
                        fd.setMark2("X");
                    } else if (roll2 == 0) {
                        fd.setMark2("-");
                    } else if (roll2 != -1) {
                        fd.setMark2(String.valueOf(roll2));
                    }
                } else {
                    if (roll1 + roll2 == 10) {
                        fd.setMark2("/");
                    } else if (roll2 == 0) {
                        fd.setMark2("-");
                    } else if (roll2 != -1) {
                        fd.setMark2(String.valueOf(roll2));
                    }
                }

                // Third roll (only if needed)
                if (roll3 != -1) {
                    if (roll3 == 10) {
                        fd.setMark3("X");
                    } else if (roll3 == 0) {
                        fd.setMark3("-");
                    } else {
                        fd.setMark3(String.valueOf(roll3));
                    }
                }

                if (fv.getTotalScore() != FrameValues.TBS) {
                    fd.setTotalScore(String.valueOf(fv.getTotalScore()));
                }
            }
        }
    }

    public String getDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        for (int i = 0; i < 10; i++) {
            FrameDisplay fd = frameDisplays.get(i);
            if (i == 9) { // 10th frame
                String mark1 = fd.getMark1().isEmpty() ? " " : fd.getMark1();
                String mark2 = fd.getMark2().isEmpty() ? " " : fd.getMark2();
                String mark3 = fd.getMark3().isEmpty() ? " " : fd.getMark3();
                sb.append(mark1).append(mark2).append(mark3).append("|");
            } else {
                String mark1 = fd.getMark1().isEmpty() ? " " : fd.getMark1();
                String mark2 = fd.getMark2().isEmpty() ? " " : fd.getMark2();
                sb.append(mark1).append(mark2).append(" |");
            }
        }

        sb.append("\n|");
        for (int i = 0; i < 10; i++) {
            FrameDisplay fd = frameDisplays.get(i);
            String total = fd.getTotalScore().isEmpty() ? "   " : String.format("%3s", fd.getTotalScore());
            sb.append(total).append("|");
        }

        return sb.toString();
    }

    // Getters
    public List<Roll> getRolls() { return rolls; }
    public List<FrameValues> getFrameValues() { return frameValues; }
    public List<FrameDisplay> getFrameDisplays() { return frameDisplays; }
    public InputControlValues getInputControl() { return inputControl; }
    public boolean isGameComplete() { return gameComplete; }
}