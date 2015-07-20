package mho.wheels.math;

import mho.wheels.iterables.CachedIterable;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.eq;
import static mho.wheels.ordering.Ordering.lt;

/**
 * Various combinatorial functions and {@code Iterable}s.
 */
public final class Combinatorics {
    /**
     * A provider of {@code Iterable}s containing every value of some type.
     */
    private static final @NotNull ExhaustiveProvider P = ExhaustiveProvider.INSTANCE;

    /**
     * Disallow instantiation
     */
    private Combinatorics() {}

    /**
     * The factorial function {@code n}!
     *
     * <ul>
     *  <li>{@code n} cannot be negative.</li>
     *  <li>The result is a factorial.</li>
     * </ul>
     *
     * @param n the argument
     * @return {@code n}!
     */
    public static @NotNull BigInteger factorial(int n) {
        if (n < 0)
            throw new ArithmeticException("cannot take factorial of " + n);
        return productBigInteger(range(BigInteger.ONE, BigInteger.valueOf(n)));
    }

    /**
     * The factorial function {@code n}!
     *
     * <ul>
     *  <li>{@code n} cannot be negative.</li>
     *  <li>The result is a factorial.</li>
     * </ul>
     *
     * @param n the argument
     * @return {@code n}!
     */
    public static @NotNull BigInteger factorial(@NotNull BigInteger n) {
        if (n.signum() == -1)
            throw new ArithmeticException("cannot take factorial of " + n);
        return productBigInteger(range(BigInteger.ONE, n));
    }

    /**
     * The subfactorial function !{@code n}
     *
     * <ul>
     *  <li>{@code n} cannot be negative.</li>
     *  <li>The result is a subfactorial (rencontres number).</li>
     * </ul>
     *
     * @param n the argument
     * @return !{@code n}
     */
    public static @NotNull BigInteger subfactorial(int n) {
        if (n < 0)
            throw new ArithmeticException("cannot take subfactorial of " + n);
        BigInteger sf = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            sf = sf.multiply(BigInteger.valueOf(i));
            if ((i & 1) == 0) {
                sf = sf.add(BigInteger.ONE);
            } else {
                sf = sf.subtract(BigInteger.ONE);
            }
        }
        return sf;
    }

    /**
     * The subfactorial function !{@code n}
     *
     * <ul>
     *  <li>{@code n} cannot be negative.</li>
     *  <li>The result is a subfactorial (rencontres number).</li>
     * </ul>
     *
     * @param n the argument
     * @return !{@code n}
     */
    public static @NotNull BigInteger subfactorial(@NotNull BigInteger n) {
        if (n.signum() == -1)
            throw new ArithmeticException("cannot take subfactorial of " + n);
        BigInteger sf = BigInteger.ONE;
        for (BigInteger i : range(BigInteger.ONE, n)) {
            sf = sf.multiply(i);
            if (i.getLowestSetBit() != 0) {
                sf = sf.add(BigInteger.ONE);
            } else {
                sf = sf.subtract(BigInteger.ONE);
            }
        }
        return sf;
    }

    /**
     * Given two {@code Iterable}s, returns all ordered pairs of elements from these {@code Iterable}s in increasing
     * order. The second {@code Iterable} must be finite; using a long second {@code Iterable} is possible but
     * discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>The result is the Cartesian product of two finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}|
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all ordered pairs of elements from {@code as} and {@code bs}
     */
    public static @NotNull <A, B> Iterable<Pair<A, B>> pairsIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return concatMap(p -> zip(repeat(p.a), p.b), zip(as, repeat(bs)));
    }

    /**
     * Given three {@code Iterable}s, returns all ordered triples of elements from these {@code Iterable}s in
     * increasing order. All {@code Iterable}s but the first must be finite; using long {@code Iterable}s in any
     * position but the first is possible but discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>{@code cs} must be finite.</li>
     *  <li>The result is the Cartesian product of three finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}|
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @return all ordered triples of elements from {@code as}, {@code bs}, and {@code cs}
     */
    public static @NotNull <A, B, C> Iterable<Triple<A, B, C>> triplesIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                pairsIncreasing(as, (Iterable<Pair<B, C>>) pairsIncreasing(bs, cs))
        );
    }

    /**
     * Given four {@code Iterable}s, returns all ordered quadruples of elements from these {@code Iterable}s in
     * increasing order. All {@code Iterable}s but the first must be finite; using long {@code Iterable}s in any
     * position but the first is possible but discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>{@code cs} must be finite.</li>
     *  <li>{@code ds} must be finite.</li>
     *  <li>The result is the Cartesian product of four finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}|
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @return all ordered quadruples of elements from {@code as}, {@code bs}, {@code cs}, and {@code ds}
     */
    public static @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruplesIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return map(
                p -> new Quadruple<>(p.a.a, p.a.b, p.b.a, p.b.b),
                pairsIncreasing(
                        (Iterable<Pair<A, B>>) pairsIncreasing(as, bs),
                        (Iterable<Pair<C, D>>) pairsIncreasing(cs, ds)
                )
        );
    }

    /**
     * Given five {@code Iterable}s, returns all ordered quintuples of elements from these {@code Iterable}s in
     * increasing order. All {@code Iterable}s but the first must be finite; using long {@code Iterable}s in any
     * position but the first is possible but discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>{@code cs} must be finite.</li>
     *  <li>{@code ds} must be finite.</li>
     *  <li>{@code es} must be finite.</li>
     *  <li>The result is the Cartesian product of five finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}|
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @return all ordered quintuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, and
     * {@code es}
     */
    public static @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuplesIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return map(
                p -> new Quintuple<>(p.a.a, p.a.b, p.b.a, p.b.b, p.b.c),
                pairsIncreasing(
                        (Iterable<Pair<A, B>>) pairsIncreasing(as, bs),
                        (Iterable<Triple<C, D, E>>) triplesIncreasing(cs, ds, es)
                )
        );
    }

    /**
     * Given six {@code Iterable}s, returns all ordered sextuples of elements from these {@code Iterable}s in
     * increasing order. All {@code Iterable}s but the first must be finite; using long {@code Iterable}s in any
     * position but the first is possible but discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>{@code cs} must be finite.</li>
     *  <li>{@code ds} must be finite.</li>
     *  <li>{@code es} must be finite.</li>
     *  <li>{@code fs} must be finite.</li>
     *  <li>The result is the Cartesian product of six finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}||{@code fs}|
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @return all ordered sextuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es},
     * and {@code fs}
     */
    public static @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuplesIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        return map(
                p -> new Sextuple<>(p.a.a, p.a.b, p.a.c, p.b.a, p.b.b, p.b.c),
                pairsIncreasing(
                        (Iterable<Triple<A, B, C>>) triplesIncreasing(as, bs, cs),
                        (Iterable<Triple<D, E, F>>) triplesIncreasing(ds, es, fs)
                )
        );
    }

    /**
     * Given seven {@code Iterable}s, returns all ordered septuples of elements from these {@code Iterable}s in
     * increasing order. All {@code Iterable}s but the first must be finite; using long {@code Iterable}s in any
     * position but the first is possible but discouraged.
     *
     * <ul>
     *  <li>{@code as} must be non-null.</li>
     *  <li>{@code bs} must be finite.</li>
     *  <li>{@code cs} must be finite.</li>
     *  <li>{@code ds} must be finite.</li>
     *  <li>{@code es} must be finite.</li>
     *  <li>{@code fs} must be finite.</li>
     *  <li>{@code gs} must be finite.</li>
     *  <li>The result is the Cartesian product of seven finite {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}||{@code fs}||{@code gs}
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param gs the seventh {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @param <G> the type of the seventh {@code Iterable}'s elements
     * @return all ordered septuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es},
     * {@code fs}, and {@code gs}
     */
    public static @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuplesIncreasing(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs,
            @NotNull Iterable<G> gs
    ) {
        return map(
                p -> new Septuple<>(p.a.a, p.a.b, p.a.c, p.b.a, p.b.b, p.b.c, p.b.d),
                pairsIncreasing(
                        (Iterable<Triple<A, B, C>>) triplesIncreasing(as, bs, cs),
                        (Iterable<Quadruple<D, E, F, G>>) quadruplesIncreasing(ds, es, fs, gs)
                )
        );
    }

    /**
     * Returns an {@code Iterable} containing all lists of a given length with elements from a given
     * {@code Iterable}. The lists are ordered lexicographically, matching the order given by the original
     * {@code Iterable}. The {@code Iterable} must be finite; using a long {@code Iterable} is possible but
     * discouraged.
     *
     * <ul>
     *  <li>{@code length} cannot be negative.</li>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is finite. All of its elements have the same length. None are empty, unless the result consists
     *  entirely of one empty element.</li>
     * </ul>
     *
     * Result length is |{@code xs}|<sup>{@code length}</sup>
     *
     * @param length the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all lists of a given length created from {@code xs}
     */
    public static @NotNull <T> Iterable<List<T>> listsIncreasing(int length, @NotNull Iterable<T> xs) {
        if (length < 0)
            throw new IllegalArgumentException("lists must have a non-negative length");
        if (length == 0) {
            return Collections.singletonList(new ArrayList<>());
        }
        Function<T, List<T>> makeSingleton = x -> {
            List<T> list = new ArrayList<>();
            list.add(x);
            return list;
        };
        List<Iterable<List<T>>> intermediates = new ArrayList<>();
        intermediates.add(map(makeSingleton, xs));
        for (int i = 1; i < length; i++) {
            Iterable<List<T>> lists = last(intermediates);
            intermediates.add(concatMap(x -> map(list -> toList(cons(x, list)), lists), xs));
        }
        return last(intermediates);
    }

    /**
     * Returns an {@code Iterable} containing all lists of a given length with elements from a given
     * {@code Iterable}. The lists are ordered lexicographically, matching the order given by the original
     * {@code Iterable}. The {@code Iterable} must be finite; using a long {@code Iterable} is possible but
     * discouraged.
     *
     * <ul>
     *  <li>{@code length} cannot be negative.</li>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is finite. All of its elements have the same length. None are empty, unless the result consists
     *  entirely of one empty element.</li>
     * </ul>
     *
     * Result length is |{@code xs}|<sup>{@code length}</sup>
     *
     * @param length the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all lists of a given length created from {@code xs}
     */
    public static @NotNull <T> Iterable<List<T>> listsIncreasing(@NotNull BigInteger length, @NotNull Iterable<T> xs) {
        if (lt(length, BigInteger.ZERO))
            throw new IllegalArgumentException("lists must have a non-negative length");
        if (eq(length, BigInteger.ZERO)) {
            return Collections.singletonList(new ArrayList<>());
        }
        Function<T, List<T>> makeSingleton = x -> {
            List<T> list = new ArrayList<>();
            list.add(x);
            return list;
        };
        List<Iterable<List<T>>> intermediates = new ArrayList<>();
        intermediates.add(map(makeSingleton, xs));
        for (BigInteger i : range(BigInteger.ONE, length.subtract(BigInteger.ONE))) {
            Iterable<List<T>> lists = last(intermediates);
            intermediates.add(concatMap(x -> map(list -> toList((Iterable<T>) cons(x, list)), lists), xs));
        }
        return last(intermediates);
    }

    /**
     * Returns an {@code Iterable} containing all {@code String}s of a given length with characters from a given
     * {@code Iterable}. The {@code String}s are ordered lexicographically, matching the order given by the original
     * {@code Iterable}. Using long {@code String} is possible but discouraged.
     *
     * <ul>
     *  <li>{@code length} cannot be negative.</li>
     *  <li>{@code s} is non-null.</li>
     *  <li>The result is finite. All of its {@code String}s have the same length. None are empty, unless the result
     *  consists entirely of one empty {@code String}.</li>
     * </ul>
     *
     * Result length is |{@code s}|<sup>{@code length}</sup>
     *
     * @param length the length of the result {@code String}
     * @param s the {@code String} from which characters are selected
     * @return all Strings of a given length created from {@code s}
     */
    public static @NotNull Iterable<String> stringsIncreasing(int length, @NotNull String s) {
        if (length < 0)
            throw new IllegalArgumentException("strings must have a non-negative length");
        if (s.isEmpty()) {
            return length == 0 ? Collections.singletonList("") : new ArrayList<>();
        }
        if (s.length() == 1) {
            return Collections.singletonList(replicate(length, s.charAt(0)));
        }
        BigInteger totalLength = BigInteger.valueOf(s.length()).pow(length);
        Function<BigInteger, String> f = bi -> charsToString(
                map(
                        i -> s.charAt(i.intValueExact()),
                        IntegerUtils.bigEndianDigitsPadded(length, BigInteger.valueOf(s.length()), bi)
                )
        );
        return map(f, range(BigInteger.ZERO, totalLength.subtract(BigInteger.ONE)));
    }

    /**
     * Returns an {@code Iterable} containing all {@code String}s of a given length with characters from a given
     * {@code Iterable}. The {@code String}s are ordered lexicographically, matching the order given by the original
     * {@code Iterable}. Using long {@code String} is possible but discouraged.
     *
     * <ul>
     *  <li>{@code length} cannot be negative.</li>
     *  <li>{@code s} is non-null.</li>
     *  <li>The result is finite. All of its {@code String}s have the same length. None are empty, unless the result
     *  consists entirely of one empty {@code String}.</li>
     * </ul>
     *
     * Result length is |{@code s}|<sup>{@code length}</sup>
     *
     * @param length the length of the result {@code String}
     * @param s the {@code String} from which characters are selected
     * @return all Strings of a given length created from {@code s}
     */
    public static @NotNull Iterable<String> stringsIncreasing(@NotNull BigInteger length, @NotNull String s) {
        if (lt(length, BigInteger.ZERO))
            throw new IllegalArgumentException("strings must have a non-negative length");
        if (s.isEmpty()) {
            return length.equals(BigInteger.ZERO) ? Collections.singletonList("") : new ArrayList<>();
        }
        if (s.length() == 1) {
            return Collections.singletonList(replicate(length, s.charAt(0)));
        }
        BigInteger totalLength = BigInteger.valueOf(s.length()).pow(length.intValueExact());
        Function<BigInteger, String> f = bi -> charsToString(
                map(
                        i -> s.charAt(i.intValueExact()),
                        IntegerUtils.bigEndianDigitsPadded(length.intValueExact(), BigInteger.valueOf(s.length()), bi)
                )
        );
        return map(f, range(BigInteger.ZERO, totalLength.subtract(BigInteger.ONE)));
    }

    /**
     * Returns an {@code Iterable} containing all lists with elements from a given {@code Iterable}. The lists are in
     * shortlex order; that is, shorter lists precede longer lists, and lists of the same length are ordered
     * lexicographically, matching the order given by the original {@code Iterable}. The {@code Iterable} must be
     * finite; using a long {@code Iterable} is possible but discouraged.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result either consists of a single empty list, or is infinite. It is in shortlex order (according to
     *  some ordering of its elements) and contains every list of elements drawn from some sequence.</li>
     * </ul>
     *
     * Result length is 1 if {@code xs} is empty, infinite otherwise
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all lists created from {@code xs}
     */
    public static @NotNull <T> Iterable<List<T>> listsShortlex(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return Collections.singletonList(new ArrayList<>());
        return concatMap(i -> listsIncreasing(i, xs), P.naturalBigIntegers());
    }

    /**
     * Returns an {@code Iterable} containing all {@code String}s with characters from a given {@code String}. The
     * {@code String}s are in shortlex order; that is, shorter {@code String}s precede longer {@code String}s, and
     * {@code String}s of the same length are ordered lexicographically, matching the order given by the original
     * {@code String}. Using a long {@code String} is possible but discouraged.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result either consists of a single empty {@code String}, or is infinite. It is in shortlex order
     *  (according to some ordering of its characters) and contains every {@code String} with characters drawn from
     *  some sequence.</li>
     * </ul>
     *
     * Result length is 1 if {@code s} is empty, infinite otherwise
     *
     * @param s the {@code String} from which characters are selected
     * @return all {@code String}s created from {@code s}
     */
    public static @NotNull Iterable<String> stringsShortlex(@NotNull String s) {
        if (isEmpty(s)) return Collections.singletonList("");
        return concatMap(i -> stringsIncreasing(i, s), P.naturalBigIntegers());
    }

    //todo docs
    public static @NotNull <T> Iterable<List<T>> listsShortlexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return minSize == 0 ? Collections.singletonList(new ArrayList<>()) : new ArrayList<>();
        return concatMap(i -> listsIncreasing(i, xs), P.rangeUp(minSize));
    }

    public static @NotNull Iterable<String> stringsShortlexAtLeast(int minSize, @NotNull String s) {
        if (isEmpty(s)) return minSize == 0 ? Collections.singletonList("") : new ArrayList<>();
        return concatMap(i -> stringsIncreasing(i, s), P.naturalBigIntegers());
    }

    private static @NotNull <A, B> Iterable<Pair<A, B>> pairsByFunction(
            @NotNull Function<BigInteger, Pair<BigInteger, BigInteger>> unpairingFunction,
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        if (isEmpty(as) || isEmpty(bs)) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        Function<BigInteger, Optional<Pair<A, B>>> f = bi -> {
            Pair<BigInteger, BigInteger> p = unpairingFunction.apply(bi);
            NullableOptional<A> optA = aii.get(p.a.intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.b.intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            return Optional.of(new Pair<>(optA.get(), optB.get()));
        };
        Predicate<Optional<Pair<A, B>>> lastPair = o -> {
            if (!o.isPresent()) return false;
            Pair<A, B> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            return lastA.isPresent() && lastB.isPresent() && lastA.get() && lastB.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Pair<A, B>>::isPresent,
                        stopAt(
                                lastPair,
                                (Iterable<Optional<Pair<A, B>>>)
                                        map(f::apply, P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all pairs of elements taken from one {@code Iterable}s in such a way that the first component grows
     * linearly but the second grows logarithmically (hence the name).
     *
     * <ul>
     *  <li>{@code xs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all pairs of elements taken from some {@code Iterable}.
     *  The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@code BasicMath.logarithmicDemux} and interpreting the resulting pairs as indices into the original
     *  {@code Iterable}.</li>
     * </ul>
     *
     * Result length is |{@code xs}|<sup>2</sup>
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all pairs of elements from {@code xs} in logarithmic order
     */
    public static @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        CachedIterable<T> ii = new CachedIterable<>(xs);
        Function<BigInteger, Optional<Pair<T, T>>> f = bi -> {
            Pair<BigInteger, BigInteger> p = IntegerUtils.logarithmicDemux(bi);
            NullableOptional<T> optA = ii.get(p.a.intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<T> optB = ii.get(p.b.intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            return Optional.of(new Pair<>(optA.get(), optB.get()));
        };
        Predicate<Optional<Pair<T, T>>> lastPair = o -> {
            if (!o.isPresent()) return false;
            Pair<T, T> p = o.get();
            Optional<Boolean> lastA = ii.isLast(p.a);
            Optional<Boolean> lastB = ii.isLast(p.b);
            return lastA.isPresent() && lastB.isPresent() && lastA.get() && lastB.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Pair<T, T>>::isPresent,
                        stopAt(
                                lastPair,
                                (Iterable<Optional<Pair<T, T>>>)
                                        map(f::apply, P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all pairs of elements taken from two {@code Iterable}s in such a way that the first component grows
     * linearly but the second grows logarithmically.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all pairs of elements taken from two {@code Iterable}s.
     *  The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@code BasicMath.logarithmicDemux} and interpreting the resulting pairs as indices into the original
     *  {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}|
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in logarithmic order
     */
    public static @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return pairsByFunction(IntegerUtils::logarithmicDemux, as, bs);
    }

    /**
     * Returns all pairs of elements taken from one {@code Iterable}s in such a way that the first component grows
     * as O(n<sup>2/3</sup>) but the second grows as O(n<sup>1/3</sup>).
     *
     * <ul>
     *  <li>{@code xs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all pairs of elements taken from some {@code Iterable}.
     *  The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@code BasicMath.squareRootDemux} and interpreting the resulting pairs as indices into the original
     *  {@code Iterable}.</li>
     * </ul>
     *
     * Result length is |{@code xs}|<sup>2</sup>
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all pairs of elements from {@code xs} in square-root order
     */
    public static @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        CachedIterable<T> ii = new CachedIterable<>(xs);
        Function<BigInteger, Optional<Pair<T, T>>> f = bi -> {
            Pair<BigInteger, BigInteger> p = IntegerUtils.squareRootDemux(bi);
            NullableOptional<T> optA = ii.get(p.a.intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<T> optB = ii.get(p.b.intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            return Optional.of(new Pair<>(optA.get(), optB.get()));
        };
        Predicate<Optional<Pair<T, T>>> lastPair = o -> {
            if (!o.isPresent()) return false;
            Pair<T, T> p = o.get();
            Optional<Boolean> lastA = ii.isLast(p.a);
            Optional<Boolean> lastB = ii.isLast(p.b);
            return lastA.isPresent() && lastB.isPresent() && lastA.get() && lastB.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Pair<T, T>>::isPresent,
                        stopAt(
                                lastPair,
                                (Iterable<Optional<Pair<T, T>>>)
                                        map(f::apply, P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all pairs of elements taken from two {@code Iterable}s in such a way that the first component grows
     * as O(n<sup>2/3</sup>) but the second grows as O(n<sup>1/3</sup>).
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all pairs of elements taken from two {@code Iterable}s.
     *  The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@code BasicMath.squareRootDemux} and interpreting the resulting pairs as indices into the original
     *  {@code Iterable}s.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}|
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in square-root order
     */
    public static @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return pairsByFunction(IntegerUtils::squareRootDemux, as, bs);
    }

    /**
     * Returns all pairs of elements taken from two {@code Iterable}s in such a way that both components grow as the
     * square root of the number of pairs iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all pairs of elements taken from two {@code Iterable}s.
     *  The elements are ordered by following a Z-curve through the pair space. The curve is computed by
     *  un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}|
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs}
     */
    public static @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return pairsByFunction(
                bi -> {
                    List<BigInteger> list = IntegerUtils.demux(2, bi);
                    return new Pair<>(list.get(0), list.get(1));
                },
                as,
                bs
        );
    }

    /**
     * Returns all triples of elements taken from three {@code Iterable}s in such a way that all components grow as
     * the cube root of the number of triples iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>{@code cs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all triples of elements taken from three {@code Iterable}s.
     *  The elements are ordered by following a Z-curve through the triple space. The curve is computed by
     *  un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}|
     *
     * @param as the {@code Iterable} from which the first components of the triples are selected
     * @param bs the {@code Iterable} from which the second components of the triples are selected
     * @param cs the {@code Iterable} from which the third components of the triples are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @return all triples of elements from {@code as}, {@code bs}, and {@code cs}
     */
    public static @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs)) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        CachedIterable<C> cii = new CachedIterable<>(cs);
        Function<BigInteger, Optional<Triple<A, B, C>>> f = bi -> {
            List<BigInteger> p = IntegerUtils.demux(3, bi);
            NullableOptional<A> optA = aii.get(p.get(0).intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.get(1).intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            NullableOptional<C> optC = cii.get(p.get(2).intValueExact());
            if (!optC.isPresent()) return Optional.empty();
            return Optional.of(new Triple<>(optA.get(), optB.get(), optC.get()));
        };
        Predicate<Optional<Triple<A, B, C>>> lastTriple = o -> {
            if (!o.isPresent()) return false;
            Triple<A, B, C> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            Optional<Boolean> lastC = cii.isLast(p.c);
            return lastA.isPresent() &&
                    lastB.isPresent() &&
                    lastC.isPresent() &&
                    lastA.get() &&
                    lastB.get() &&
                    lastC.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Triple<A, B, C>>::isPresent,
                        stopAt(
                                lastTriple,
                                (Iterable<Optional<Triple<A, B, C>>>)
                                        map(bi -> f.apply(bi), P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all quadruples of elements taken from four {@code Iterable}s in such a way that all components grow as
     * the fourth root of the number of quadruples iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>{@code cs} is non-null.</li>
     *  <li>{@code ds} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all quadruples of elements taken from four
     *  {@code Iterable}s. The elements are ordered by following a Z-curve through the quadruple space. The curve is
     *  computed by un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}|
     *
     * @param as the {@code Iterable} from which the first components of the quadruples are selected
     * @param bs the {@code Iterable} from which the second components of the quadruples are selected
     * @param cs the {@code Iterable} from which the third components of the quadruples are selected
     * @param ds the {@code Iterable} from which the fourth components of the quadruples are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @return all quadruples of elements from {@code as}, {@code bs}, {@code cs}, and {@code ds}
     */
    public static @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs) || isEmpty(ds)) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        CachedIterable<C> cii = new CachedIterable<>(cs);
        CachedIterable<D> dii = new CachedIterable<>(ds);
        Function<BigInteger, Optional<Quadruple<A, B, C, D>>> f = bi -> {
            List<BigInteger> p = IntegerUtils.demux(4, bi);
            NullableOptional<A> optA = aii.get(p.get(0).intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.get(1).intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            NullableOptional<C> optC = cii.get(p.get(2).intValueExact());
            if (!optC.isPresent()) return Optional.empty();
            NullableOptional<D> optD = dii.get(p.get(3).intValueExact());
            if (!optD.isPresent()) return Optional.empty();
            return Optional.of(new Quadruple<A, B, C, D>(optA.get(), optB.get(), optC.get(), optD.get()));
        };
        Predicate<Optional<Quadruple<A, B, C, D>>> lastQuadruple = o -> {
            if (!o.isPresent()) return false;
            Quadruple<A, B, C, D> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            Optional<Boolean> lastC = cii.isLast(p.c);
            Optional<Boolean> lastD = dii.isLast(p.d);
            return lastA.isPresent() &&
                    lastB.isPresent() &&
                    lastC.isPresent() &&
                    lastD.isPresent() &&
                    lastA.get() &&
                    lastB.get() &&
                    lastC.get() &&
                    lastD.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Quadruple<A, B, C, D>>::isPresent,
                        stopAt(
                                lastQuadruple,
                                (Iterable<Optional<Quadruple<A, B, C, D>>>)
                                        map(bi -> f.apply(bi), P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all quintuples of elements taken from five {@code Iterable}s in such a way that all components grow as
     * the fifth root of the number of quintuples iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>{@code cs} is non-null.</li>
     *  <li>{@code ds} is non-null.</li>
     *  <li>{@code es} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all quintuples of elements taken from five
     *  {@code Iterable}s. The elements are ordered by following a Z-curve through the quintuple space. The curve is
     *  computed by un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}|
     *
     * @param as the {@code Iterable} from which the first components of the quintuples are selected
     * @param bs the {@code Iterable} from which the second components of the quintuples are selected
     * @param cs the {@code Iterable} from which the third components of the quintuples are selected
     * @param ds the {@code Iterable} from which the fourth components of the quintuples are selected
     * @param es the {@code Iterable} from which the fifth components of the quintuples are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @return all quintuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, and {@code es}
     */
    public static @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs) || isEmpty(ds) || isEmpty(es)) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        CachedIterable<C> cii = new CachedIterable<>(cs);
        CachedIterable<D> dii = new CachedIterable<>(ds);
        CachedIterable<E> eii = new CachedIterable<>(es);
        Function<BigInteger, Optional<Quintuple<A, B, C, D, E>>> f = bi -> {
            List<BigInteger> p = IntegerUtils.demux(5, bi);
            NullableOptional<A> optA = aii.get(p.get(0).intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.get(1).intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            NullableOptional<C> optC = cii.get(p.get(2).intValueExact());
            if (!optC.isPresent()) return Optional.empty();
            NullableOptional<D> optD = dii.get(p.get(3).intValueExact());
            if (!optD.isPresent()) return Optional.empty();
            NullableOptional<E> optE = eii.get(p.get(4).intValueExact());
            if (!optE.isPresent()) return Optional.empty();
            return Optional.of(new Quintuple<A, B, C, D, E>(
                    optA.get(),
                    optB.get(),
                    optC.get(),
                    optD.get(),
                    optE.get()
            ));
        };
        Predicate<Optional<Quintuple<A, B, C, D, E>>> lastQuintuple = o -> {
            if (!o.isPresent()) return false;
            Quintuple<A, B, C, D, E> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            Optional<Boolean> lastC = cii.isLast(p.c);
            Optional<Boolean> lastD = dii.isLast(p.d);
            Optional<Boolean> lastE = eii.isLast(p.e);
            return lastA.isPresent() &&
                    lastB.isPresent() &&
                    lastC.isPresent() &&
                    lastD.isPresent() &&
                    lastE.isPresent() &&
                    lastA.get() &&
                    lastB.get() &&
                    lastC.get() &&
                    lastD.get() &&
                    lastE.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Quintuple<A, B, C, D, E>>::isPresent,
                        stopAt(
                                lastQuintuple,
                                (Iterable<Optional<Quintuple<A, B, C, D, E>>>)
                                        map(bi -> f.apply(bi), P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all sextuples of elements taken from six {@code Iterable}s in such a way that all components grow as
     * the sixth root of the number of sextuples iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>{@code cs} is non-null.</li>
     *  <li>{@code ds} is non-null.</li>
     *  <li>{@code es} is non-null.</li>
     *  <li>{@code fs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all sextuples of elements taken from six {@code Iterable}s.
     *  The elements are ordered by following a Z-curve through the sextuple space. The curve is computed by
     *  un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}||{@code fs}|
     *
     * @param as the {@code Iterable} from which the first components of the sextuples are selected
     * @param bs the {@code Iterable} from which the second components of the sextuples are selected
     * @param cs the {@code Iterable} from which the third components of the sextuples are selected
     * @param ds the {@code Iterable} from which the fourth components of the sextuples are selected
     * @param es the {@code Iterable} from which the fifth components of the sextuples are selected
     * @param fs the {@code Iterable} from which the sixth components of the sextuples are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @return all sextuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es}, and
     * {@code fs}
     */
    public static @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        if (
                isEmpty(as) ||
                        isEmpty(bs) ||
                        isEmpty(cs) ||
                        isEmpty(ds) ||
                        isEmpty(es) ||
                        isEmpty(fs)
                ) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        CachedIterable<C> cii = new CachedIterable<>(cs);
        CachedIterable<D> dii = new CachedIterable<>(ds);
        CachedIterable<E> eii = new CachedIterable<>(es);
        CachedIterable<F> fii = new CachedIterable<>(fs);
        Function<BigInteger, Optional<Sextuple<A, B, C, D, E, F>>> f = bi -> {
            List<BigInteger> p = IntegerUtils.demux(6, bi);
            NullableOptional<A> optA = aii.get(p.get(0).intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.get(1).intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            NullableOptional<C> optC = cii.get(p.get(2).intValueExact());
            if (!optC.isPresent()) return Optional.empty();
            NullableOptional<D> optD = dii.get(p.get(3).intValueExact());
            if (!optD.isPresent()) return Optional.empty();
            NullableOptional<E> optE = eii.get(p.get(4).intValueExact());
            if (!optE.isPresent()) return Optional.empty();
            NullableOptional<F> optF = fii.get(p.get(5).intValueExact());
            if (!optF.isPresent()) return Optional.empty();
            return Optional.of(new Sextuple<A, B, C, D, E, F>(
                    optA.get(),
                    optB.get(),
                    optC.get(),
                    optD.get(),
                    optE.get(),
                    optF.get()
            ));
        };
        Predicate<Optional<Sextuple<A, B, C, D, E, F>>> lastSextuple = o -> {
            if (!o.isPresent()) return false;
            Sextuple<A, B, C, D, E, F> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            Optional<Boolean> lastC = cii.isLast(p.c);
            Optional<Boolean> lastD = dii.isLast(p.d);
            Optional<Boolean> lastE = eii.isLast(p.e);
            Optional<Boolean> lastF = fii.isLast(p.f);
            return lastA.isPresent() &&
                    lastB.isPresent() &&
                    lastC.isPresent() &&
                    lastD.isPresent() &&
                    lastE.isPresent() &&
                    lastF.isPresent() &&
                    lastA.get() &&
                    lastB.get() &&
                    lastC.get() &&
                    lastD.get() &&
                    lastE.get() &&
                    lastF.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Sextuple<A, B, C, D, E, F>>::isPresent,
                        stopAt(
                                lastSextuple,
                                (Iterable<Optional<Sextuple<A, B, C, D, E, F>>>)
                                        map(bi -> f.apply(bi), P.naturalBigIntegers())
                        )
                )
        );
    }

    /**
     * Returns all septuples of elements taken from seven {@code Iterable}s in such a way that all components grow as
     * the seventh root of the number of septuples iterated.
     *
     * <ul>
     *  <li>{@code as} is non-null.</li>
     *  <li>{@code bs} is non-null.</li>
     *  <li>{@code cs} is non-null.</li>
     *  <li>{@code ds} is non-null.</li>
     *  <li>{@code es} is non-null.</li>
     *  <li>{@code fs} is non-null.</li>
     *  <li>{@code gs} is non-null.</li>
     *  <li>The result is an {@code Iterable} containing all septuples of elements taken from seven
     *  {@code Iterable}s. The elements are ordered by following a Z-curve through the septuple space. The curve is
     *  computed by un-interleaving bits of successive integers.</li>
     * </ul>
     *
     * Result length is |{@code as}||{@code bs}||{@code cs}||{@code ds}||{@code es}||{@code fs}||{@code gs}|
     *
     * @param as the {@code Iterable} from which the first components of the septuples are selected
     * @param bs the {@code Iterable} from which the second components of the septuples are selected
     * @param cs the {@code Iterable} from which the third components of the septuples are selected
     * @param ds the {@code Iterable} from which the fourth components of the septuples are selected
     * @param es the {@code Iterable} from which the fifth components of the septuples are selected
     * @param fs the {@code Iterable} from which the sixth components of the septuples are selected
     * @param gs the {@code Iterable} from which the seventh components of the septuples are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @param <G> the type of the seventh {@code Iterable}'s elements
     * @return all septuples of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es},
     * {@code fs}, and {@code gs}
     */
    public static @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs,
            @NotNull Iterable<G> gs
    ) {
        if (
                isEmpty(as) ||
                        isEmpty(bs) ||
                        isEmpty(cs) ||
                        isEmpty(ds) ||
                        isEmpty(es) ||
                        isEmpty(fs) ||
                        isEmpty(gs)
                ) return new ArrayList<>();
        CachedIterable<A> aii = new CachedIterable<>(as);
        CachedIterable<B> bii = new CachedIterable<>(bs);
        CachedIterable<C> cii = new CachedIterable<>(cs);
        CachedIterable<D> dii = new CachedIterable<>(ds);
        CachedIterable<E> eii = new CachedIterable<>(es);
        CachedIterable<F> fii = new CachedIterable<>(fs);
        CachedIterable<G> gii = new CachedIterable<>(gs);
        Function<BigInteger, Optional<Septuple<A, B, C, D, E, F, G>>> f = bi -> {
            List<BigInteger> p = IntegerUtils.demux(7, bi);
            NullableOptional<A> optA = aii.get(p.get(0).intValueExact());
            if (!optA.isPresent()) return Optional.empty();
            NullableOptional<B> optB = bii.get(p.get(1).intValueExact());
            if (!optB.isPresent()) return Optional.empty();
            NullableOptional<C> optC = cii.get(p.get(2).intValueExact());
            if (!optC.isPresent()) return Optional.empty();
            NullableOptional<D> optD = dii.get(p.get(3).intValueExact());
            if (!optD.isPresent()) return Optional.empty();
            NullableOptional<E> optE = eii.get(p.get(4).intValueExact());
            if (!optE.isPresent()) return Optional.empty();
            NullableOptional<F> optF = fii.get(p.get(5).intValueExact());
            if (!optF.isPresent()) return Optional.empty();
            NullableOptional<G> optG = gii.get(p.get(6).intValueExact());
            if (!optG.isPresent()) return Optional.empty();
            return Optional.of(new Septuple<A, B, C, D, E, F, G>(
                    optA.get(),
                    optB.get(),
                    optC.get(),
                    optD.get(),
                    optE.get(),
                    optF.get(),
                    optG.get()
            ));
        };
        Predicate<Optional<Septuple<A, B, C, D, E, F, G>>> lastSeptuple = o -> {
            if (!o.isPresent()) return false;
            Septuple<A, B, C, D, E, F, G> p = o.get();
            Optional<Boolean> lastA = aii.isLast(p.a);
            Optional<Boolean> lastB = bii.isLast(p.b);
            Optional<Boolean> lastC = cii.isLast(p.c);
            Optional<Boolean> lastD = dii.isLast(p.d);
            Optional<Boolean> lastE = eii.isLast(p.e);
            Optional<Boolean> lastF = fii.isLast(p.f);
            Optional<Boolean> lastG = gii.isLast(p.g);
            return lastA.isPresent() &&
                    lastB.isPresent() &&
                    lastC.isPresent() &&
                    lastD.isPresent() &&
                    lastE.isPresent() &&
                    lastF.isPresent() &&
                    lastG.isPresent() &&
                    lastA.get() &&
                    lastB.get() &&
                    lastC.get() &&
                    lastD.get() &&
                    lastE.get() &&
                    lastF.get() &&
                    lastG.get();
        };
        return map(
                Optional::get,
                filter(
                        Optional<Septuple<A, B, C, D, E, F, G>>::isPresent,
                        stopAt(
                                lastSeptuple,
                                (Iterable<Optional<Septuple<A, B, C, D, E, F, G>>>)
                                        map(bi -> f.apply(bi), P.naturalBigIntegers())
                        )
                )
        );
    }

    public static <T> Iterable<List<T>> lists(int size, Iterable<T> xs) {
        if (size == 0) {
            return Collections.singletonList(new ArrayList<T>());
        }
        CachedIterable<T> ii = new CachedIterable<>(xs);
        Function<BigInteger, Optional<List<T>>> f = bi ->
                ii.get(map(BigInteger::intValueExact, IntegerUtils.demux(size, bi)));
        Predicate<Optional<List<T>>> lastList = o -> {
            if (!o.isPresent()) return false;
            List<T> list = o.get();
            for (T x : list) {
                Optional<Boolean> oLastX = ii.isLast(x);
                if (!oLastX.isPresent() || !oLastX.get()) return false;
            }
            return true;
        };
        return map(
                Optional::get,
                filter(Optional::isPresent, stopAt(lastList, map(bi -> f.apply(bi), P.naturalBigIntegers())))
        );
    }

    public static @NotNull <T> Iterable<List<T>> controlledListsIncreasing(@NotNull List<Iterable<T>> xss) {
        if (xss.size() == 0) return Collections.singletonList(new ArrayList<T>());
        if (xss.size() == 1) return map(Collections::singletonList, xss.get(0));
        if (xss.size() == 2) return map(p -> Arrays.<T>asList(p.a, p.b), pairsIncreasing(xss.get(0), xss.get(1)));
        List<Iterable<T>> leftList = new ArrayList<>();
        List<Iterable<T>> rightList = new ArrayList<>();
        for (int i = 0; i < xss.size() / 2; i++) {
            leftList.add(xss.get(i));
        }
        for (int i = xss.size() / 2; i < xss.size(); i++) {
            rightList.add(xss.get(i));
        }
        Iterable<List<T>> leftLists = controlledListsIncreasing(leftList);
        Iterable<List<T>> rightLists = controlledListsIncreasing(rightList);
        return map(p -> toList(concat(p.a, p.b)), pairsIncreasing(leftLists, rightLists));
    }

    public static Iterable<String> strings(int size, Iterable<Character> cs) {
        return map(IterableUtils::charsToString, lists(size, cs));
    }

    public static Iterable<String> strings(int size, String s) {
        return map(IterableUtils::charsToString, lists(size, fromString(s)));
    }

    public Iterable<String> strings(int size) {
        return strings(size, P.characters());
    }

    public static @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Pair<>(list.get(0), list.get(1)), lists(2, xs));
    }

    public static @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return filter(p -> !p.a.equals(p.b), pairs(xs));
    }

    public static @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Triple<>(list.get(0), list.get(1), list.get(2)), lists(3, xs));
    }

    public static @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return filter(t -> !t.a.equals(t.b) && !t.a.equals(t.c) && !t.b.equals(t.c), triples(xs));
    }

    public static @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Quadruple<>(list.get(0), list.get(1), list.get(2), list.get(3)), lists(4, xs));
    }

    public static @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return filter(q -> and(map(p -> !p.a.equals(p.b), pairs(Quadruple.toList(q)))), quadruples(xs));
    }

    public static @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(
                list -> new Quintuple<>(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)),
                lists(5, xs)
        );
    }

    public static @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return filter(q -> and(map(p -> !p.a.equals(p.b), pairs(Quintuple.toList(q)))), quintuples(xs));
    }

    public static @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(
                list -> new Sextuple<>(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5)),
                lists(6, xs)
        );
    }

    public static @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return filter(s -> and(map(p -> !p.a.equals(p.b), pairs(Sextuple.toList(s)))), sextuples(xs));
    }

    public static @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(
                list -> new Septuple<>(
                        list.get(0),
                        list.get(1),
                        list.get(2),
                        list.get(3),
                        list.get(4),
                        list.get(5),
                        list.get(6)
                ),
                lists(7, xs)
        );
    }

    public static @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuples(@NotNull Iterable<T> xs) {
        return filter(s -> and(map(p -> !p.a.equals(p.b), pairs(Septuple.toList(s)))), septuples(xs));
    }

    public static @NotNull <T> Iterable<List<T>> lists(Iterable<T> xs) {
        CachedIterable<T> ii = new CachedIterable<>(xs);
        Function<BigInteger, Optional<List<T>>> f = bi -> {
            if (bi.equals(BigInteger.ZERO)) {
                return Optional.of(new ArrayList<T>());
            }
            bi = bi.subtract(BigInteger.ONE);
            Pair<BigInteger, BigInteger> sizeIndex = IntegerUtils.logarithmicDemux(bi);
            int size = sizeIndex.b.intValueExact() + 1;
            return ii.get(map(BigInteger::intValueExact, IntegerUtils.demux(size, sizeIndex.a)));
        };
        return optionalMap(f::apply, P.naturalBigIntegers());
    }

    public static @NotNull Iterable<String> strings(@NotNull Iterable<Character> cs) {
        return map(IterableUtils::charsToString, lists(cs));
    }

    public static @NotNull Iterable<String> strings(@NotNull String s) {
        return map(IterableUtils::charsToString, lists(fromString(s)));
    }

    public static @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, Iterable<T> xs) {
        if (minSize == 0) return lists(xs);
        CachedIterable<T> ii = new CachedIterable<>(xs);
        Function<BigInteger, Optional<List<T>>> f = bi -> {
            if (bi.equals(BigInteger.ZERO)) {
                return Optional.<List<T>>empty();
            }
            bi = bi.subtract(BigInteger.ONE);
            Pair<BigInteger, BigInteger> sizeIndex = IntegerUtils.logarithmicDemux(bi);
            int size = sizeIndex.b.intValueExact() + minSize;
            return ii.get(map(BigInteger::intValueExact, IntegerUtils.demux(size, sizeIndex.a)));
        };
        return optionalMap(f::apply, P.naturalBigIntegers());
    }

    public static @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull Iterable<Character> cs) {
        return map(IterableUtils::charsToString, listsAtLeast(minSize, cs));
    }

    public static @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull String s) {
        return map(IterableUtils::charsToString, listsAtLeast(minSize, fromString(s)));
    }

    public static @NotNull <T> Iterable<List<T>> orderedSubsequences(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            return Collections.singletonList(new ArrayList<T>());
        return () -> new Iterator<List<T>>() {
            private CachedIterable<T> cxs = new CachedIterable<T>(xs);
            private List<Integer> indices = new ArrayList<>();

            @Override
            public boolean hasNext() {
                return indices != null;
            }

            @Override
            public List<T> next() {
                List<T> subsequence = cxs.get(indices).get();
                if (indices.isEmpty()) {
                    indices.add(0);
                } else {
                    int lastIndex = last(indices);
                    if (lastIndex < cxs.size() - 1) {
                        indices.add(lastIndex + 1);
                    } else if (indices.size() == 1) {
                        indices = null;
                    } else {
                        indices.remove(indices.size() - 1);
                        indices.set(indices.size() - 1, last(indices) + 1);
                    }
                }
                return subsequence;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    public static @NotNull Iterable<String> orderedSubstrings(@NotNull String s) {
        return map(IterableUtils::charsToString, orderedSubsequences(fromString(s)));
    }

    public static @NotNull <T> Iterable<List<T>> subsequences(@NotNull Iterable<T> xs) {
        CachedIterable<T> cxs = new CachedIterable<>(xs);
        return map(
                Optional::get,
                takeWhile(Optional::isPresent, map(i -> cxs.select(IntegerUtils.bits(i)), rangeUp(BigInteger.ZERO)))
        );
    }

    public static @NotNull Iterable<String> substrings(@NotNull String s) {
        return map(IterableUtils::charsToString, subsequences(fromString(s)));
    }
}
