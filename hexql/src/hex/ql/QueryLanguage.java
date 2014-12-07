package hex.ql;

import hex.ql.ast.predicates.Condition;
import hex.ql.ast.predicates.EqualityPredicate;
import hex.ql.ast.predicates.NotEqualsPredicate;
import hex.ql.queries.StreamQuery;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jason on 14-10-25.
 */
public class QueryLanguage {
    public static <T> Query<T> from(List<T> source) {
        return new StreamQuery<>(source.stream());
    }

    public static <T> Query<T> from(Queryable<T> source) {
        return source.stream();
    }

    public static <T> List<T> select(List<T> source, Predicate<T> predicate) {
        return from(source).filter(predicate).collect(Collectors.toList());
    }

    public static <T, U> Condition<T,U> where(Function<T, U> extractor, Predicate<U> predicate) {
        return new Condition<>(extractor, predicate);
    }

    public static <U> Predicate<U> is(U value) {
        return new EqualityPredicate<>(value);
    }

    public static <U> Predicate<U> isNot(U value) {
        return new NotEqualsPredicate<>(value);
    }
}
