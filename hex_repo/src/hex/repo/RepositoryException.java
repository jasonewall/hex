package hex.repo;

/**
 * Catch all wrapper exception for anything that goes wrong during
 * query execution. Inspect root cause to understand what went wrong. You
 * should be catching this exception in a generic location in your application
 * to provide developer information in an appropriate location.
 */
public class RepositoryException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public RepositoryException(Throwable cause) {
        super(cause);
    }
}
