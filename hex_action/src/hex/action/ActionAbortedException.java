package hex.action;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by jason on 14-11-16.
 */
class ActionAbortedException extends RuntimeException {
    private ServletException servletException;

    private IOException ioException;

    public ActionAbortedException(ServletException cause) {
        super(cause);
        servletException = cause;
    }

    public ActionAbortedException(IOException cause) {
        super(cause);
        ioException = cause;
    }

    public Optional<ServletException> getServletExceptionCause() {
        return Optional.ofNullable(servletException);
    }

    public Optional<IOException> getIOExceptionCause() {
        return Optional.ofNullable(ioException);
    }
}
