package dreamteam.tp2_grupo5.states;

public class ValidationState {
    boolean status;
    String message;

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
