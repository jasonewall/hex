package hex.utils;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Created by jason on 14-12-10.
 */
public class Memo<T> {
    private final T t;

    public static <T> Memo<T> of(T t) {
        return new Memo<>(t);
    }

    private Memo(T t) {
        this.t = t;
    }

    public Memo<T> andThen(UnaryOperator<T> op) {
        T t = op.apply(this.t);
        if(t == null) return this;
        return new Memo<>(t);
    }

    public Memo<T> tap(Consumer<T> op) {
        op.accept(this.t);
        return this;
    }

    public T finish() {
        return t;
    }
}
