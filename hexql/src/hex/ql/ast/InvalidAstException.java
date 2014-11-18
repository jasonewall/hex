package hex.ql.ast;

/**
 * Created by jason on 14-10-26.
 */
public class InvalidAstException extends Exception {
    public InvalidAstException() {
        super();
    }

    public InvalidAstException(String message) {
        super(message);
    }

    public InvalidAstException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAstException(Throwable cause) {
        super(cause);
    }

    protected InvalidAstException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
