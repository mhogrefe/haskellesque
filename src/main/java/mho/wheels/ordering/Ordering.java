package mho.wheels.ordering;

import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * An enumeration consisting of the elements {@code LT} (less than), {@code EQ} (equal to), and {@code GT} (greater
 * than). Ordering-related utilities are also provided.
 */
public enum Ordering {
    LT, // <
    EQ, // ==
    GT; // >

    /**
     * Converts an integer to an ordering, based on comparing the integer to zero. This is useful because C-style
     * ordering-related methods often use negative integers to mean "less than", zero to mean "equal to", and positive
     * integers to mean "greater than".
     *
     * <ul>
     *  <li>{@code i} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param i an {@code int}
     * @return whether {@code i} is greater than, less than, or equal to zero
     */
    public static @NotNull Ordering fromInt(int i) {
        if (i > 0) return GT;
        if (i < 0) return LT;
        return EQ;
    }

    /**
     * Converts {@code this} to an {@code int}. {@code EQ} becomes 0, {@code LT} becomes –1, and {@code GT} becomes 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Ordering}.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @return an {@code int} equivalent to {@code this}
     */
    public int toInt() {
        switch (this) {
            case LT: return -1;
            case EQ: return 0;
            case GT: return 1;
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Returns the opposite ordering: {@code EQ} stays {@code EQ}, and {@code LT} and {@code GT} switch places.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Ordering}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the opposite or inverse ordering
     */
    public @NotNull Ordering invert() {
        switch (this) {
            case LT: return GT;
            case EQ: return EQ;
            case GT: return LT;
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Compares two values using their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return whether {@code a} is less than, equal to, or greater than {@code b}
     */
    public static @NotNull <T extends Comparable<T>> Ordering compare(@NotNull T a, @NotNull T b) {
        return fromInt(a.compareTo(b));
    }

    /**
     * Compares two values using a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param comparator the {@code Comparator} used to compare {@code a} and {@code b}
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return whether {@code a} is less than, equal to, or greater than {@code b}
     */
    public static @NotNull <T> Ordering compare(@NotNull Comparator<T> comparator, @Nullable T a, @Nullable T b) {
        return fromInt(comparator.compare(a, b));
    }

    /**
     * Whether {@code a} and {@code b} are equal with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a=b
     */
    public static <T extends Comparable<T>> boolean eq(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) == 0;
    }

    /**
     * Whether {@code a} and {@code b} are unequal with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≠b
     */
    public static <T extends Comparable<T>> boolean ne(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) != 0;
    }

    /**
     * Whether {@code a} is less than b {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a{@literal <}b
     */
    public static <T extends Comparable<T>> boolean lt(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) < 0;
    }

    /**
     * Whether {@code a} is greater than b {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a{@literal >}b
     */
    public static <T extends Comparable<T>> boolean gt(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * Whether {@code a} is less than or equal to b {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≤b
     */
    public static <T extends Comparable<T>> boolean le(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * Whether {@code a} is greater than or equal to b {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≥b
     */
    public static <T extends Comparable<T>> boolean ge(@NotNull T a, @NotNull T b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * Whether {@code a} and {@code b} are equal with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a=<sub>{@code comparator}</sub>b
     */
    public static <T> boolean eq(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) == 0;
    }

    /**
     * Whether {@code a} and {@code b} are unequal with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≠<sub>{@code comparator}</sub>b
     */
    public static <T> boolean ne(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) != 0;
    }

    /**
     * Whether {@code a} is less than {@code b} with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a{@literal <}<sub>{@code comparator}</sub>b
     */
    public static <T> boolean lt(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) < 0;
    }

    /**
     * Whether {@code a} is greater than {@code b} with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a{@literal >}<sub>{@code comparator}</sub>b
     */
    public static <T> boolean gt(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) > 0;
    }

    /**
     * Whether {@code a} is less than or equal to {@code b} with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≤<sub>{@code comparator}</sub>b
     */
    public static <T> boolean le(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) <= 0;
    }

    /**
     * Whether {@code a} is less than or equal to {@code b} with respect to a provided {@code Comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return a≥<sub>{@code comparator}</sub>b
     */
    public static <T> boolean ge(@NotNull Comparator<T> comparator, T a, T b) {
        return comparator.compare(a, b) >= 0;
    }

    /**
     * Returns the smaller of {@code a} and {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return min({@code a}, {@code b})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull <T extends Comparable<T>> T min(@NotNull T a, @NotNull T b) {
        return lt(a, b) ? a : b;
    }

    /**
     * Returns the larger of {@code a} and {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return max({@code a}, {@code b})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull <T extends Comparable<T>> T max(@NotNull T a, @NotNull T b) {
        return gt(a, b) ? a : b;
    }

    /**
     * Returns the smaller and the larger of {@code a} and {@code b} with respect to their default {@code Comparator}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} and {@code b} must be comparable by their default comparator.</li>
     *  <li>The result is not null, and neither of its elements is null.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return (min({@code a}, {@code b}), max({@code a}, {@code b}))
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull <T extends Comparable<T>> Pair<T, T> minMax(@NotNull T a, @NotNull T b) {
        return lt(a, b) ? new Pair<>(a, b) : new Pair<>(b, a);
    }

    /**
     * Returns the smaller of {@code a} and {@code b} with respect to {@code comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} may be any {@code T}.</li>
     *  <li>{@code b} may be any {@code T}.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be any {@code T}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return min<sub>{@code comparator}</sub>({@code a}, {@code b})
     */
    @SuppressWarnings("JavaDoc")
    public static <T> T min(@NotNull Comparator<T> comparator, T a, T b) {
        return lt(comparator, a, b) ? a : b;
    }

    /**
     * Returns the larger of {@code a} and {@code b} with respect to {@code comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} may be any {@code T}.</li>
     *  <li>{@code b} may be any {@code T}.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be any {@code T}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return max<sub>{@code comparator}</sub>({@code a}, {@code b})
     */
    @SuppressWarnings("JavaDoc")
    public static <T> T max(@NotNull Comparator<T> comparator, T a, T b) {
        return gt(comparator, a, b) ? a : b;
    }

    /**
     * Returns the smaller and the larger of {@code a} and {@code b} with respect to {@code comparator}.
     *
     * <ul>
     *  <li>{@code comparator} cannot be null.</li>
     *  <li>{@code a} may be any {@code T}.</li>
     *  <li>{@code b} may be any {@code T}.</li>
     *  <li>{@code a} and {@code b} must be comparable by {@code comparator}.</li>
     *  <li>The result may be any {@code T}.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param <T> the type of {@code a} and {@code b}
     * @return (min<sub>{@code comparator}</sub>({@code a}, {@code b}),
     * max<sub>{@code comparator}</sub>({@code a}, {@code b}))
     */
    @SuppressWarnings("JavaDoc")
    public static <T> Pair<T, T> minMax(@NotNull Comparator<T> comparator, T a, T b) {
        return lt(comparator, a, b) ? new Pair<>(a, b) : new Pair<>(b, a);
    }

    /**
     * Converts {@code this} to a single-character {@code String}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Ordering}.</li>
     *  <li>The result may be "<", "=", or ">".</li>
     * </ul>
     *
     * @return the {@code String} representing {@code this}
     */
    public @NotNull String toString() {
        switch (this) {
            case LT: return "<";
            case EQ: return "=";
            case GT: return ">";
        }
        throw new IllegalStateException("unreachable");
    }
}
