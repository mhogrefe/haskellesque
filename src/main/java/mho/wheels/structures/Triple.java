package mho.wheels.structures;

import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.EQ;

/**
 * An ordered triple of values. Any combination of values may be null. The {@code Triple} is immutable iff all of its
 * values are.
 *
 * @param <A> the type of the first value
 * @param <B> the type of the second value
 * @param <C> the type of the third value
 */
public final class Triple<A, B, C> {
    /**
     * The first component of the {@code Triple}
     */
    public final A a;

    /**
     * The second component of the {@code Triple}
     */
    public final B b;

    /**
     * The third component of the {@code Triple}
     */
    public final C c;

    /**
     * Constructs a {@code Triple} from three values.
     *
     * <ul>
     *  <li>{@code a} may be anything.</li>
     *  <li>{@code b} may be anything.</li>
     *  <li>{@code c} may be anything.</li>
     *  <li>Any {@code Triple} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param a the first value
     * @param b the second value
     * @param c the third value
     */
    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Converts a {@code Triple} of three values of the same type to a {@code List}.
     *
     * <ul>
     *  <li>{@code p} cannot be null.</li>
     *  <li>The result has length 3.</li>
     * </ul>
     *
     * @param t a {@code Triple}
     * @param <T> the type of all values of {@code t}
     * @return a {@code List} containing the values of {@code t}
     */
    public static <T> List<T> toList(Triple<T, T, T> t) {
        return Arrays.asList(t.a, t.b, t.c);
    }

    /**
     * Converts a {@code List} of three values to a {@code Triple}.
     *
     * <ul>
     *  <li>{@code xs} must have length 3.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     * @return a {@code Triple} containing the values of {@code xs}
     */
    public static <T> Triple<T, T, T> fromList(@NotNull List<T> xs) {
        if (xs.size() != 3) {
            throw new IllegalArgumentException("xs must have length 3. Invalid xs: " + xs);
        }
        return new Triple<>(xs.get(0), xs.get(1), xs.get(2));
    }

    /**
     * Compares two {@code Triple}s, provided that {@code A}, {@code B}, and {@code C} all implement
     * {@code Comparable}.
     *
     * <ul>
     *  <li>{@code p} must be non-null.</li>
     *  <li>{@code q} must be non-null.</li>
     *  <li>{@code p.a} and {@code q.a} must be comparable by their type's {@code compareTo} method.</li>
     *  <li>{@code p.b} and {@code q.b} must be comparable by their type's {@code compareTo} method.</li>
     *  <li>{@code p.c} and {@code q.c} must be comparable by their type's {@code compareTo} method.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param p the first {@code Triple}
     * @param q the second {@code Triple}
     * @param <A> the type of the first component of {@code p} and {@code q}
     * @param <B> the type of the second component of {@code p} and {@code q}
     * @param <C> the type of the third component of {@code p} and {@code q}
     * @return how {@code p} and {@code q} are ordered
     */
    public static @NotNull <
            A extends Comparable<A>,
            B extends Comparable<B>,
            C extends Comparable<C>
            > Ordering compare(@NotNull Triple<A, B, C> p, @NotNull Triple<A, B, C> q) {
        Ordering aOrdering = Ordering.compare(p.a, q.a);
        if (aOrdering != EQ) return aOrdering;
        Ordering bOrdering = Ordering.compare(p.b, q.b);
        if (bOrdering != EQ) return bOrdering;
        return Ordering.compare(p.c, q.c);
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Triple}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Triple} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Triple triple = (Triple) that;
        return (a == null ? triple.a == null : a.equals(triple.a)) &&
               (b == null ? triple.b == null : b.equals(triple.b)) && ///
               (c == null ? triple.c == null : c.equals(triple.c)); ///
    }

    /**
     * Calculates the hash code of {@code this}. The hash code is deterministic iff all values' hash codes are.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Triple}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code
     */
    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }

    /**
     * Creates a {@code Triple} from a {@code String}. Valid strings are of the form
     * {@code "(" + a + ", " + b + ", " + c + ")"}, where {@code a}, {@code b}, and {@code c} are valid {@code String}s
     * for their types. {@code a} and {@code b} must not contain the {@code String} {@code ", "}, because this will
     * confuse the parser.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code Triple}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Triple}
     * @param readA a function which reads a {@code String} which represents null or a value of type {@code A}
     * @param readB a function which reads a {@code String} which represents null or a value of type {@code B}
     * @param readC a function which reads a {@code String} which represents null or a value of type {@code C}
     * @param <A> the type of the {@code Triple}'s first value
     * @param <B> the type of the {@code Triple}'s second value
     * @param <C> the type of the {@code Triple}'s third value
     * @return the {@code Triple} represented by {@code s}, or an empty {@code Optional} if {@code s} is invalid
     */
    public static @NotNull <A, B, C> Optional<Triple<A, B, C>> readStrict(
            @NotNull String s,
            @NotNull Function<String, NullableOptional<A>> readA,
            @NotNull Function<String, NullableOptional<B>> readB,
            @NotNull Function<String, NullableOptional<C>> readC
    ) {
        if (s.length() < 2 || head(s) != '(' || last(s) != ')') return Optional.empty();
        s = middle(s);
        A a = null;
        B b = null;
        C c = null;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String token : s.split(", ")) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(token);
            switch (i) {
                case 0:
                    NullableOptional<A> oa = readA.apply(sb.toString());
                    if (oa.isPresent()) {
                        a = oa.get();
                        i++;
                        sb = new StringBuilder();
                    }
                    break;
                case 1:
                    NullableOptional<B> ob = readB.apply(sb.toString());
                    if (ob.isPresent()) {
                        b = ob.get();
                        i++;
                        sb = new StringBuilder();
                    }
                    break;
                case 2:
                    NullableOptional<C> oc = readC.apply(sb.toString());
                    if (oc.isPresent()) {
                        c = oc.get();
                        i++;
                        sb = new StringBuilder();
                    }
                    break;
                default:
                    return Optional.empty();
            }
        }

        if (i != 3) return Optional.empty();
        return Optional.of(new Triple<>(a, b, c));
    }

    /**
     * Creates a string representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Triple}.</li>
     *  <li>The result begins with a left parenthesis, ends with a right parenthesis, and contains the string
     *  {@code ", "} at least twice.</li>
     * </ul>
     *
     * @return a string representation of {@code this}
     */
    public @NotNull String toString() {
        return "(" + a + ", " + b + ", " + c + ")";
    }

    /**
     * A comparator which compares two {@code Triple}s via {@code Comparators} provided for each component.
     *
     * @param <A> the type of the {@code Triple}s' first components
     * @param <B> the type of the {@code Triple}s' second components
     * @param <C> the type of the {@code Triple}s' third components
     */
    public static class TripleComparator<A, B, C> implements Comparator<Triple<A, B, C>> {
        /**
         * The first component's {@code Comparator}
         */
        private final @NotNull Comparator<A> aComparator;

        /**
         * The second component's {@code Comparator}
         */
        private final @NotNull Comparator<B> bComparator;

        /**
         * The third component's {@code Comparator}
         */
        private final @NotNull Comparator<C> cComparator;

        /**
         * Constructs a {@code TripleComparator} from three {@code Comparator}s.
         *
         * <ul>
         *  <li>{@code aComparator} must be non-null.</li>
         *  <li>{@code bComparator} must be non-null.</li>
         *  <li>{@code cComparator} must be non-null.</li>
         *  <li>Any {@code TripleComparator} may be constructed with this constructor.</li>
         * </ul>
         *
         * @param aComparator the first component's {@code Comparator}
         * @param bComparator the second component's {@code Comparator}
         * @param cComparator the third component's {@code Comparator}
         */
        public TripleComparator(
                @NotNull Comparator<A> aComparator,
                @NotNull Comparator<B> bComparator,
                @NotNull Comparator<C> cComparator
        ) {
            this.aComparator = aComparator;
            this.bComparator = bComparator;
            this.cComparator = cComparator;
        }

        /**
         * Compares two {@code Triple}s lexicographically, returning 1, –1, or 0 if the answer is "greater than", "less
         * than", or "equal to", respectively.
         *
         * <ul>
         *  <li>{@code p} must be non-null.</li>
         *  <li>{@code q} must be non-null.</li>
         *  <li>{@code p.a} and {@code q.a} must be comparable by {@code aComparator}.</li>
         *  <li>{@code p.b} and {@code q.b} must be comparable by {@code bComparator}.</li>
         *  <li>{@code p.c} and {@code q.c} must be comparable by {@code cComparator}.</li>
         *  <li>The result is –1, 0, or 1.</li>
         * </ul>
         *
         * @param p the first {@code Triple}
         * @param q the second {@code Triple}
         * @return {@code this} compared to {@code that}
         */
        @Override
        public int compare(@NotNull Triple<A, B, C> p, @NotNull Triple<A, B, C> q) {
            Ordering aOrdering = Ordering.compare(aComparator, p.a, q.a);
            if (aOrdering != EQ) return aOrdering.toInt();
            Ordering bOrdering = Ordering.compare(bComparator, p.b, q.b);
            if (bOrdering != EQ) return bOrdering.toInt();
            return Ordering.compare(cComparator, p.c, q.c).toInt();
        }
    }
}
