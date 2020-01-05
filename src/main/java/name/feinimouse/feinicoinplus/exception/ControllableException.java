package name.feinimouse.feinicoinplus.exception;

import lombok.Getter;

public class ControllableException extends Exception {
    @Getter
    private int type;
    private String message;
    
    public ControllableException(int type) {
        this.type = type;
    }

    public ControllableException(int type, String message) {
        super(message);
        this.type = type;
        this.message = message;
    }

    public ControllableException() {
    }

    public ControllableException(String message) {
        super(message);
        this.message = message;
    }
}
