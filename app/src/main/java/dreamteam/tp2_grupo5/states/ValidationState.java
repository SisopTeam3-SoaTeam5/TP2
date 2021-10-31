package dreamteam.tp2_grupo5.states;

public class ValidationState {
    final boolean status;
    final String message;

    public ValidationState(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
