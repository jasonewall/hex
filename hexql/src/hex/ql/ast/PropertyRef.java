package hex.ql.ast;

import java.util.function.Function;

/**
 * Created by jason on 14-10-26.
 */
public class PropertyRef<T,R> implements Node {
    private Function<T,R> property;

    public PropertyRef(Function<T,R> property) {
        this.property = property;
    }

    public Function<T, R> getProperty() {
        return property;
    }
}
