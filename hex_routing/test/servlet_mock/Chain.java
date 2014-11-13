package servlet_mock;

/**
 * Created by jason on 14-11-13.
 */
@FunctionalInterface
public interface Chain<T> {
    default public Chain<T> andThen(T thing) {
        apply(thing);
        return this;
    }

    public void apply(T thing);
}
