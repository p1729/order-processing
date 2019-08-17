package com.pankaj.core.rules;

@FunctionalInterface
public interface Rule<T, O> {
    boolean apply(T t, O o);

    default Rule<T, O> and(Rule<T, O> other) {
        return (t, o) -> this.apply(t, o) && other.apply(t, o);
    }

    default Rule<T, O> or(Rule<T, O> other) {
        return (t, o) -> this.apply(t, o) || other.apply(t, o);
    }

    default Rule<T, O> negate() {
        return (t, o) -> !this.apply(t, o);
    }
}