package hex.utils;

import java.util.function.Consumer;

/**
 * Created by jason on 14-12-03.
 */
@SuppressWarnings("unchecked")
public class Null {
    public static final Consumer Consumer = (t) -> {};

    public static <T> Consumer<T> Consumer() {
        return (Consumer<T>)Consumer;
    }
}
