package org.example.dollar;

public class MoneyTransactionResult {
    private final MoneyState state;
    private final String message;

    public MoneyTransactionResult(MoneyState state, String message) {
        this.state = state;
        this.message = message;
    }

    public MoneyState getState() { return state; }
    public String getMessage() { return message; }
    public boolean hasError() { return !message.isEmpty(); }

    @Override
    public String toString() {
        return String.format("State: %s, Message: %s", state, message);
    }
}

