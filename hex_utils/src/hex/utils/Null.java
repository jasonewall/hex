package hex.utils;

/**
 * Created by jason on 14-12-03.
 */
public class Null {
    public static class Consumer<T> implements java.util.function.Consumer<T> {
        @Override
        public void accept(T t) {

        }

        @Override
        public java.util.function.Consumer<T> andThen(java.util.function.Consumer<? super T> after) {
            return after::accept;
        }
    }

    public static class Predicate<T> implements java.util.function.Predicate<T> {
        @Override
        public boolean test(T t) {
            return true;
        }

        @Override
        public java.util.function.Predicate<T> and(java.util.function.Predicate<? super T> other) {
            return other::test;
        }

        @Override
        public java.util.function.Predicate<T> negate() {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.function.Predicate<T> or(java.util.function.Predicate<? super T> other) {
            return this;
        }
    }
}
