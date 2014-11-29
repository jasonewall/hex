package hex.action.initialization;

/**
 * Created by jason on 14-11-28.
 */
public interface Initializer {
    public static final String PACKAGE = "initializers.package";

    public static final String LIST = "initializers";
    void start() throws InitializationException;
}
