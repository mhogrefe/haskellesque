package mho.wheels.iterables;

import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

/**
 * An {@code ExhaustiveProvider} produces {@code Iterable}s that generate some set of values in a specified order.
 * There is only a single instance of this class.
 */
public final strictfp class ExhaustiveProvider extends IterableProvider {
    /**
     * The single instance of this class.
     */
    public static final @NotNull ExhaustiveProvider INSTANCE = new ExhaustiveProvider();

    /**
     * Disallow instantiation
     */
    private ExhaustiveProvider() {}

    /**
     * An {@link Iterable} that generates both {@link boolean}s. Does not support removal.
     *
     * Length is 2
     */
    @Override
    public @NotNull Iterable<Boolean> booleans() {
        return new NoRemoveIterable<>(Arrays.asList(false, true));
    }

    /**
     * An {@code Iterable} that generates all {@link Ordering}s in increasing order. Does not support removal.
     *
     * Length is 3
     */
    @Override
    public @NotNull Iterable<Ordering> orderingsIncreasing() {
        return new NoRemoveIterable<>(Arrays.asList(LT, EQ, GT));
    }

    /**
     * An {@code Iterable} that generates all {@code Ordering}s. Does not support removal.
     *
     * Length is 3
     */
    @Override
    public @NotNull Iterable<Ordering> orderings() {
        return new NoRemoveIterable<>(Arrays.asList(EQ, LT, GT));
    }

    /**
     * An {@code Iterable} that generates all {@link RoundingMode}s. Does not support removal.
     *
     * Length is 8
     */
    @Override
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return new NoRemoveIterable<>(Arrays.asList(
                RoundingMode.UNNECESSARY,
                RoundingMode.UP,
                RoundingMode.DOWN,
                RoundingMode.CEILING,
                RoundingMode.FLOOR,
                RoundingMode.HALF_UP,
                RoundingMode.HALF_DOWN,
                RoundingMode.HALF_EVEN
        ));
    }

    /**
     * Returns an unmodifiable version of a list. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is finite.</li>
     * </ul>
     *
     * Length is |{@code xs}|
     *
     * @param xs a {@code List}
     * @param <T> the type of {@code xs}'s elements
     * @return an unmodifiable version of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        return new NoRemoveIterable<>(xs);
    }

    /**
     * Turns a {@code String} into an {@code Iterable} of {@code Character}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result is finite.</li>
     * </ul>
     *
     * Length is |{@code s}|
     *
     * @param s a {@code String}
     * @return the {@code Character}s in {@code s}
     */
    @Override
    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        return fromString(s);
    }

    /**
     * An {@code Iterable} that generates all {@link Byte}s in increasing order. Does not support removal.
     *
     * Length is 2<sup>8</sup> = 256
     */
    @Override
    public @NotNull Iterable<Byte> bytesIncreasing() {
        return IterableUtils.rangeUp(Byte.MIN_VALUE);
    }

    /**
     * An {@code Iterable} that generates all {@link Short}s in increasing order. Does not support removal.
     *
     * Length is 2<sup>16</sup> = 65,536
     */
    @Override
    public @NotNull Iterable<Short> shortsIncreasing() {
        return IterableUtils.rangeUp(Short.MIN_VALUE);
    }

    /**
     * An {@code Iterable} that generates all {@link Integer}s in increasing order. Does not support removal.
     *
     * Length is 2<sup>32</sup> = 4,294,967,296
     */
    @Override
    public @NotNull Iterable<Integer> integersIncreasing() {
        return IterableUtils.rangeUp(Integer.MIN_VALUE);
    }

    /**
     * An {@code Iterable} that generates all {@link Long}s in increasing order. Does not support removal.
     *
     * Length is 2<sup>64</sup> = 18,446,744,073,709,551,616
     */
    @Override
    public @NotNull Iterable<Long> longsIncreasing() {
        return IterableUtils.rangeUp(Long.MIN_VALUE);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Byte}s. Does not support removal.
     *
     * Length is 2<sup>7</sup>–1 = 127
     */
    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return IterableUtils.rangeUp((byte) 1);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Short}s. Does not support removal.
     *
     * Length is 2<sup>15</sup>–1 = 32,767
     */
    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return IterableUtils.rangeUp((short) 1);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Integer}s. Does not support removal.
     *
     * Length is 2<sup>31</sup>–1 = 2,147,483,647
     */
    @Override
    public @NotNull Iterable<Integer> positiveIntegers() {
        return IterableUtils.rangeUp(1);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Long}s. Does not support removal.
     *
     * Length is 2<sup>63</sup>–1 = 9,223,372,036,854,775,807
     */
    @Override
    public @NotNull Iterable<Long> positiveLongs() {
        return IterableUtils.rangeUp(1L);
    }

    /**
     * An {@code Iterable} that generates all positive {@link BigInteger}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return IterableUtils.rangeUp(BigInteger.ONE);
    }

    /**
     * An {@code Iterable} that generates all negative {@code Byte}s. Does not support removal.
     *
     * Length is 2<sup>7</sup> = 128
     */
    @Override
    public @NotNull Iterable<Byte> negativeBytes() {
        return IterableUtils.rangeBy((byte) -1, (byte) -1);
    }

    /**
     * An {@code Iterable} that generates all negative {@code Short}s. Does not support removal.
     *
     * Length is 2<sup>15</sup> = 32,768
     */
    @Override
    public @NotNull Iterable<Short> negativeShorts() {
        return IterableUtils.rangeBy((short) -1, (short) -1);
    }

    /**
     * An {@code Iterable} that generates all negative {@code Integer}s. Does not support removal.
     *
     * Length is 2<sup>31</sup> = 2,147,483,648
     */
    @Override
    public @NotNull Iterable<Integer> negativeIntegers() {
        return IterableUtils.rangeBy(-1, -1);
    }

    /**
     * An {@code Iterable} that generates all negative {@code Long}s. Does not support removal.
     *
     * Length is 2<sup>63</sup> = 9,223,372,036,854,775,808
     */
    @Override
    public @NotNull Iterable<Long> negativeLongs() {
        return IterableUtils.rangeBy(-1L, -1L);
    }

    /**
     * An {@code Iterable} that generates all negative {@code BigInteger}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return IterableUtils.rangeBy(IntegerUtils.NEGATIVE_ONE, IntegerUtils.NEGATIVE_ONE);
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Byte}s. Does not support removal.
     *
     * Length is 2<sup>8</sup>–1 = 127
     */
    @Override
    public @NotNull Iterable<Byte> nonzeroBytes() {
        return mux(Arrays.asList(positiveBytes(), negativeBytes()));
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Short}s. Does not support removal.
     *
     * Length is 2<sup>16</sup>–1 = 65,535
     */
    @Override
    public @NotNull Iterable<Short> nonzeroShorts() {
        return mux(Arrays.asList(positiveShorts(), negativeShorts()));
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Integer}s. Does not support removal.
     *
     * Length is 2<sup>32</sup>–1 = 4,294,967,295
     */
    @Override
    public @NotNull Iterable<Integer> nonzeroIntegers() {
        return mux(Arrays.asList(positiveIntegers(), negativeIntegers()));
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Long}s. Does not support removal.
     *
     * Length is 2<sup>64</sup>–1 = 18,446,744,073,709,551,615
     */
    @Override
    public @NotNull Iterable<Long> nonzeroLongs() {
        return mux(Arrays.asList(positiveLongs(), negativeLongs()));
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code BigInteger}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> nonzeroBigIntegers() {
        return mux(Arrays.asList(positiveBigIntegers(), negativeBigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all natural {@code Byte}s. Does not support removal.
     *
     * Length is 2<sup>7</sup> = 128
     */
    @Override
    public @NotNull Iterable<Byte> naturalBytes() {
        return IterableUtils.rangeUp((byte) 0);
    }

    /**
     * An {@code Iterable} that generates all natural {@code Short}s (including 0). Does not support removal.
     *
     * Length is 2<sup>15</sup> = 32,768
     */
    @Override
    public @NotNull Iterable<Short> naturalShorts() {
        return IterableUtils.rangeUp((short) 0);
    }

    /**
     * An {@code Iterable} that generates all natural {@code Integer}s (including 0). Does not support removal.
     *
     * Length is 2<sup>31</sup> = 2,147,483,648
     */
    @Override
    public @NotNull Iterable<Integer> naturalIntegers() {
        return IterableUtils.rangeUp(0);
    }

    /**
     * An {@code Iterable} that generates all natural {@code Long}s (including 0). Does not support removal.
     *
     * Length is 2<sup>63</sup> = 9,223,372,036,854,775,808
     */
    @Override
    public @NotNull Iterable<Long> naturalLongs() {
        return IterableUtils.rangeUp(0L);
    }

    /**
     * An {@code Iterable} that generates all natural {@code BigInteger}s (including 0). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return rangeUp(BigInteger.ZERO);
    }

    /**
     * An {@code Iterable} that generates all {@code Byte}s. Does not support removal.
     *
     * Length is 2<sup>8</sup> = 128
     */
    @Override
    public @NotNull Iterable<Byte> bytes() {
        return cons((byte) 0, nonzeroBytes());
    }

    /**
     * An {@code Iterable} that generates all {@code Short}s. Does not support removal.
     *
     * Length is 2<sup>16</sup> = 65,536
     */
    @Override
    public @NotNull Iterable<Short> shorts() {
        return cons((short) 0, nonzeroShorts());
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s. Does not support removal.
     *
     * Length is 2<sup>32</sup> = 4,294,967,296
     */
    @Override
    public @NotNull Iterable<Integer> integers() {
        return cons(0, nonzeroIntegers());
    }

    /**
     * An {@code Iterable} that generates all {@code Long}s. Does not support removal.
     *
     * Length is 2<sup>64</sup> = 18,446,744,073,709,551,616
     */
    @Override
    public @NotNull Iterable<Long> longs() {
        return cons(0L, nonzeroLongs());
    }

    /**
     * An {@code Iterable} that generates all {@code BigInteger}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return cons(BigInteger.ZERO, nonzeroBigIntegers());
    }

    /**
     * An {@code Iterable} that generates all ASCII {@link Character}s in increasing order. Does not support
     * removal.
     *
     * Length is 2<sup>7</sup> = 128
     */
    @Override
    public @NotNull Iterable<Character> asciiCharactersIncreasing() {
        return range((char) 0, (char) 127);
    }

    /**
     * An {@code Iterable} that generates all ASCII {@code Character}s in an order which places "friendly" characters
     * first. Does not support removal.
     *
     * Length is 2<sup>7</sup> = 128
     */
    @Override
    public @NotNull Iterable<Character> asciiCharacters() {
        return concat(Arrays.asList(
                range('a', 'z'),
                range('A', 'Z'),
                range('0', '9'),
                range('!', '/'),                  // printable non-alphanumeric ASCII...
                range(':', '@'),                  // ...
                range('[', '`'),                  // ...
                range('{', '~'),                  // ...
                Collections.singleton(' '),       // ' '
                range((char) 0, (char) 31),       // non-printable and whitespace ASCII
                Collections.singleton((char) 127) // DEL
        ));
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s in increasing order. Does not support removal.
     *
     * Length is 2<sup>16</sup> = 65,536
     */
    @Override
    public @NotNull Iterable<Character> charactersIncreasing() {
        return range(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s in an order which places "friendly" characters
     * first. Does not support removal.
     *
     * Length is 2<sup>16</sup> = 65,536
     */
    @Override
    public @NotNull Iterable<Character> characters() {
        return concat(Arrays.asList(
                range('a', 'z'),
                range('A', 'Z'),
                range('0', '9'),
                range('!', '/'),            // printable non-alphanumeric ASCII...
                range(':', '@'),            // ...
                range('[', '`'),            // ...
                range('{', '~'),            // ...
                Collections.singleton(' '), // ' '
                range((char) 0, (char) 31), // non-printable and whitespace ASCII
                rangeUp((char) 127)         // DEL and non-ASCII
        ));
    }

    /**
     * An {@code Iterable} that generates all {@code Byte}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is 2<sup>7</sup>–{@code a}
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Byte}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Byte> rangeUp(byte a) {
        if (a >= 0) {
            return IterableUtils.rangeUp(a);
        } else {
            return cons(
                    (byte) 0,
                    mux(Arrays.asList(IterableUtils.rangeUp((byte) 1), IterableUtils.rangeBy((byte) -1, (byte) -1, a)))
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Short}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is 2<sup>15</sup>–{@code a}
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Short}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Short> rangeUp(short a) {
        if (a >= 0) {
            return IterableUtils.rangeUp(a);
        } else {
            return cons(
                    (short) 0,
                    mux(
                            Arrays.asList(
                                    IterableUtils.rangeUp((short) 1),
                                    IterableUtils.rangeBy((short) -1, (short) -1, a)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Integers}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is 2<sup>31</sup>–{@code a}
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Integer}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Integer> rangeUp(int a) {
        if (a >= 0) {
            return IterableUtils.rangeUp(a);
        } else {
            return cons(0, mux(Arrays.asList(IterableUtils.rangeUp(1), IterableUtils.rangeBy(-1, -1, a))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Long}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is 2<sup>63</sup>–{@code a}
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Long}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Long> rangeUp(long a) {
        if (a >= 0) {
            return IterableUtils.rangeUp(a);
        } else {
            return cons(0L, mux(Arrays.asList(IterableUtils.rangeUp(1L), IterableUtils.rangeBy(-1L, -1L, a))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code BigIntegers}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code BigInteger}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        if (a.signum() != -1) {
            return IterableUtils.rangeUp(a);
        } else {
            return cons(
                    BigInteger.ZERO,
                    mux(
                            Arrays.asList(
                                    IterableUtils.rangeUp(BigInteger.ONE),
                                    IterableUtils.rangeBy(IntegerUtils.NEGATIVE_ONE, IntegerUtils.NEGATIVE_ONE, a)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is 2<sup>16</sup>–{@code a}
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Character}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Character> rangeUp(char a) {
        return IterableUtils.rangeUp(a);
    }

    /**
     * An {@code Iterable} that generates all {@code Byte}s less than or equal to {@code a}. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is {@code a}+2<sup>7</sup>+1
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Byte}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Byte> rangeDown(byte a) {
        if (a <= 0) {
            return IterableUtils.rangeBy(a, (byte) -1);
        } else {
            return cons(
                    (byte) 0,
                    mux(Arrays.asList(IterableUtils.range((byte) 1, a), IterableUtils.rangeBy((byte) -1, (byte) -1)))
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Short}s less than or equal to {@code a}. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is {@code a}+2<sup>15</sup>+1
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Short}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Short> rangeDown(short a) {
        if (a <= 0) {
            return IterableUtils.rangeBy(a, (short) -1);
        } else {
            return cons(
                    (short) 0,
                    mux(
                            Arrays.asList(
                                    IterableUtils.range((short) 1, a),
                                    IterableUtils.rangeBy((short) -1, (short) -1)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s less than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is {@code a}+2<sup>31</sup>+1
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Integer}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Integer> rangeDown(int a) {
        if (a <= 0) {
            return IterableUtils.rangeBy(a, -1);
        } else {
            return cons(0, mux(Arrays.asList(IterableUtils.range(1, a), IterableUtils.rangeBy(-1, -1))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Long}s less than or equal to {@code a}. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is {@code a}+2<sup>63</sup>+1
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Long}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Long> rangeDown(long a) {
        if (a <= 0) {
            return IterableUtils.rangeBy(a, -1L);
        } else {
            return cons(0L, mux(Arrays.asList(IterableUtils.range(1L, a), IterableUtils.rangeBy(-1L, -1L))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code BigInteger}s less than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code BigInteger}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        if (a.signum() != 1) {
            return IterableUtils.rangeBy(a, IntegerUtils.NEGATIVE_ONE);
        } else {
            return cons(
                    BigInteger.ZERO,
                    mux(
                            Arrays.asList(
                                    IterableUtils.range(BigInteger.ONE, a),
                                    IterableUtils.rangeBy(IntegerUtils.NEGATIVE_ONE, IntegerUtils.NEGATIVE_ONE)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s less than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is {@code a}+1
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Character}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Character> rangeDown(char a) {
        return IterableUtils.range('\0', a);
    }

    /**
     * An {@code Iterable} that generates all {@code Byte}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>{@code b} may be any {@code byte}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Byte}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Byte> range(byte a, byte b) {
        if (a > b) return Collections.emptyList();
        if (a >= 0 && b >= 0) {
            return IterableUtils.range(a, b);
        } else if (a < 0 && b < 0) {
            return IterableUtils.rangeBy(b, (byte) -1, a);
        } else {
            return cons(
                    (byte) 0,
                    mux(
                            Arrays.asList(
                                    IterableUtils.range((byte) 1, b),
                                    IterableUtils.rangeBy((byte) -1, (byte) -1, a)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Short}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>{@code b} may be any {@code short}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Short}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Short> range(short a, short b) {
        if (a > b) return Collections.emptyList();
        if (a >= 0 && b >= 0) {
            return IterableUtils.range(a, b);
        } else if (a < 0 && b < 0) {
            return IterableUtils.rangeBy(b, (short) -1, a);
        } else {
            return cons(
                    (short) 0,
                    mux(
                            Arrays.asList(
                                    IterableUtils.range((short) 1, b),
                                    IterableUtils.rangeBy((short) -1, (short) -1, a)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>{@code b} may be any {@code int}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Integer}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Integer> range(int a, int b) {
        if (a > b) return Collections.emptyList();
        if (a >= 0 && b >= 0) {
            return IterableUtils.range(a, b);
        } else if (a < 0 && b < 0) {
            return IterableUtils.rangeBy(b, -1, a);
        } else {
            return cons(0, mux(Arrays.asList(IterableUtils.range(1, b), IterableUtils.rangeBy(-1, -1, a))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Long}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>{@code b} may be any {@code long}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Long}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Long> range(long a, long b) {
        if (a > b) return Collections.emptyList();
        if (a >= 0 && b >= 0) {
            return IterableUtils.range(a, b);
        } else if (a < 0 && b < 0) {
            return IterableUtils.rangeBy(b, (byte) -1, a);
        } else {
            return cons(0L, mux(Arrays.asList(IterableUtils.range(1L, b), IterableUtils.rangeBy(-1L, -1L, a))));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code BigInteger}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code BigInteger}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        if (gt(a, b)) return Collections.emptyList();
        if (a.signum() != -1 && b.signum() != -1) {
            return IterableUtils.range(a, b);
        } else if (a.signum() == -1 && b.signum() == -1) {
            return IterableUtils.rangeBy(b, IntegerUtils.NEGATIVE_ONE, a);
        } else {
            return cons(
                    BigInteger.ZERO,
                    mux(
                            Arrays.asList(
                                    IterableUtils.range(BigInteger.ONE, b),
                                    IterableUtils.rangeBy(IntegerUtils.NEGATIVE_ONE, IntegerUtils.NEGATIVE_ONE, a)
                            )
                    )
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>{@code b} may be any {@code char}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is max(0, {@code b}–{@code a}+1)
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Character}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Character> range(char a, char b) {
        return IterableUtils.range(a, b);
    }

    /**
     * An {@code Iterable} that generates all positive {@link BinaryFraction}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> positiveBinaryFractions() {
        return map(
                p -> BinaryFraction.of(p.a.shiftLeft(1).add(BigInteger.ONE), p.b),
                pairsLogarithmicOrder(naturalBigIntegers(), integers())
        );
    }

    /**
     * An {@code Iterable} that generates all negative {@link BinaryFraction}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> negativeBinaryFractions() {
        return map(BinaryFraction::negate, positiveBinaryFractions());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@link BinaryFraction}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> nonzeroBinaryFractions() {
        return mux(Arrays.asList(positiveBinaryFractions(), negativeBinaryFractions()));
    }

    /**
     * An {@code Iterable} that generates all {@link BinaryFraction}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> binaryFractions() {
        return cons(BinaryFraction.ZERO, nonzeroBinaryFractions());
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s greater than or equal to {@code a}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code BinaryFraction}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BinaryFraction> rangeUp(@NotNull BinaryFraction a) {
        return cons(a, map(bf -> bf.shiftLeft(a.getExponent()).add(a), positiveBinaryFractions()));
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s less than or equal to {@code a}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code BinaryFraction}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BinaryFraction> rangeDown(@NotNull BinaryFraction a) {
        return cons(a, map(bf -> a.subtract(bf.shiftLeft(a.getExponent())), positiveBinaryFractions()));
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s between 0 and 1, exclusive. Does not support
     * removal.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BinaryFraction}s greater than 0 and less
     *  than 1.</li>
     * </ul>
     *
     * Length is infinite
     */
    private static @NotNull Iterable<BinaryFraction> positiveBinaryFractionsLessThanOne() {
        return concatMap(
                e -> map(
                        i -> BinaryFraction.of(i.shiftLeft(1).add(BigInteger.ONE), -e),
                        IterableUtils.range(BigInteger.ZERO, BigInteger.ONE.shiftLeft(e - 1).subtract(BigInteger.ONE))
                ),
                IterableUtils.rangeUp(1)
        );
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is 0 if a>b, 1 if a=b, and infinite otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code BinaryFraction}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BinaryFraction> range(@NotNull BinaryFraction a, @NotNull BinaryFraction b) {
        switch (compare(a, b)) {
            case GT: return Collections.emptyList();
            case EQ: return Collections.singletonList(a);
            case LT:
                BinaryFraction difference = b.subtract(a);
                int blockExponent = difference.getExponent();
                BigInteger blockCount = difference.getMantissa();
                return concat(
                        map(
                                i -> BinaryFraction.ONE.shiftLeft(blockExponent).multiply(BinaryFraction.of(i)).add(a),
                                IterableUtils.range(BigInteger.ZERO, blockCount)
                        ),
                        concatMap(
                                bf -> map(
                                        j -> bf.add(BinaryFraction.of(j)).shiftLeft(blockExponent).add(a),
                                        IterableUtils.range(BigInteger.ZERO, blockCount.subtract(BigInteger.ONE))
                                ),
                                positiveBinaryFractionsLessThanOne()
                        )
                );
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    /**
     * An {@code Iterable} that generates all possible positive {@code float} mantissas. A {@code float}'s mantissa is
     * the unique odd integer that, when multiplied by a power of 2, equals the {@code float}. Does not support
     * removal.
     *
     * Length is 2<sup>23</sup> = 8,388,608
     */
    private static final @NotNull Iterable<Integer> FLOAT_MANTISSAS = IterableUtils.rangeBy(
            1,
            2,
            1 << (FloatingPointUtils.FLOAT_FRACTION_WIDTH + 1)
    );

    /**
     * An {@code Iterable} that generates all possible float exponents. A positive float's exponent is the base-2
     * logarithm of the float divided by its mantissa. Does not support removal.
     *
     * Length is 2<sup>8</sup>+23–2 = 277
     */
    private static final @NotNull Iterable<Integer> FLOAT_EXPONENTS = cons(
            0,
            mux(
                    Arrays.asList(INSTANCE.range(1, Float.MAX_EXPONENT),
                    IterableUtils.rangeBy(-1, -1, FloatingPointUtils.MIN_SUBNORMAL_FLOAT_EXPONENT))
            )
    );

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN} nor infinite) positive {@code Float}s. Does
     * not support removal.
     *
     * Length is 2<sup>31</sup>–2<sup>23</sup>–1 = 2,139,095,039
     */
    private static @NotNull Iterable<Float> positiveOrdinaryFloats() {
        return optionalMap(
                p -> FloatingPointUtils.floatFromMantissaAndExponent(p.a, p.b),
                INSTANCE.pairs(FLOAT_MANTISSAS, FLOAT_EXPONENTS)
        );
    }

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN}, negative zero, nor infinite) negative
     * {@code Float}s. Does not support removal.
     *
     * Length is 2<sup>31</sup>–2<sup>23</sup>–1 = 2,139,095,039
     */
    private static @NotNull Iterable<Float> negativeOrdinaryFloats() {
        return map(f -> -f, positiveOrdinaryFloats());
    }

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN} nor infinite) nonzero {@code Float}s. Does
     * not support removal.
     *
     * Length is 2<sup>32</sup>–2<sup>24</sup>–2 = 4,278,190,078
     */
    private static @NotNull Iterable<Float> nonzeroOrdinaryFloats() {
        return mux(Arrays.asList(positiveOrdinaryFloats(), negativeOrdinaryFloats()));
    }

    /**
     * An {@code Iterable} that generates all positive {@code Float}s, including {@code Infinity} but not positive
     * zero. Does not support removal.
     *
     * Length is 2<sup>31</sup>–2<sup>23</sup> = 2,139,095,040
     */
    @Override
    public @NotNull Iterable<Float> positiveFloats() {
        return cons(Float.POSITIVE_INFINITY, positiveOrdinaryFloats());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Float}s, including {@code -Infinity} but not negative
     * zero. Does not support removal.
     *
     * Length is 2<sup>31</sup>–2<sup>23</sup> = 2,139,095,040
     */
    @Override
    public @NotNull Iterable<Float> negativeFloats() {
        return cons(Float.NEGATIVE_INFINITY, negativeOrdinaryFloats());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Float}s, including {@code NaN}, {@code Infinity}, and
     * {@code -Infinity}. Does not support removal.
     *
     * Length is 2<sup>32</sup>–2<sup>24</sup>+1 = 4,278,190,081
     */
    @Override
    public @NotNull Iterable<Float> nonzeroFloats() {
        return concat(
                Arrays.asList(Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY),
                nonzeroOrdinaryFloats()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s, including {@code NaN}, positive and negative zeros,
     * {@code Infinity}, and {@code -Infinity}. Does not support removal.
     *
     * Length is 2<sup>32</sup>–2<sup>24</sup>+3 = 4,278,190,083
     */
    @Override
    public @NotNull Iterable<Float> floats() {
        return concat(
                Arrays.asList(Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 0.0f, -0.0f),
                nonzeroOrdinaryFloats()
        );
    }

    /**
     * An {@code Iterable} that generates all possible positive {@code double} mantissas. A {@code double}'s mantissa
     * is the unique odd integer that, when multiplied by a power of 2, equals the {@code double}. Does not support
     * removal.
     *
     * Length is 2<sup>52</sup> = 4,503,599,627,370,496
     */
    private static final @NotNull Iterable<Long> DOUBLE_MANTISSAS = IterableUtils.rangeBy(
            1L,
            2,
            1L << (FloatingPointUtils.DOUBLE_FRACTION_WIDTH + 1)
    );

    /**
     * An {@code Iterable} that generates all possible {@code double} exponents. A positive {@code double}'s exponent
     * is the base-2 logarithm of the {@code double} divided by its mantissa. Does not support removal.
     *
     * Length is 2<sup>11</sup>+52–2 = 2,098
     */
    private static final @NotNull Iterable<Integer> DOUBLE_EXPONENTS = cons(
            0,
            mux(
                    Arrays.asList(INSTANCE.range(1, Double.MAX_EXPONENT),
                    IterableUtils.rangeBy(-1, -1, FloatingPointUtils.MIN_SUBNORMAL_DOUBLE_EXPONENT))
            )
    );

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN} nor infinite) positive {@code Double}s.
     * Does not support removal.
     *
     * Length is 2<sup>63</sup>–2<sup>52</sup>–1 = 9,218,868,437,227,405,311
     */
    private static @NotNull Iterable<Double> positiveOrdinaryDoubles() {
        return optionalMap(
                p -> FloatingPointUtils.doubleFromMantissaAndExponent(p.a, p.b),
                INSTANCE.pairs(DOUBLE_MANTISSAS, DOUBLE_EXPONENTS)
        );
    }

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN}, negative zero, nor infinite) negative
     * {@code Double}s. Does not support removal.
     *
     * Length is 2<sup>63</sup>–2<sup>52</sup>–1 = 9,218,868,437,227,405,311
     */
    private static @NotNull Iterable<Double> negativeOrdinaryDoubles() {
        return map(d -> -d, positiveOrdinaryDoubles());
    }

    /**
     * An {@code Iterable} that generates all ordinary (neither {@code NaN} nor infinite) nonzero {@code Double}s. Does
     * not support removal.
     *
     * Length is 2<sup>64</sup>–2<sup>53</sup>–2 = 18,437,736,874,454,810,622
     */
    private static @NotNull Iterable<Double> nonzeroOrdinaryDoubles() {
        return mux(Arrays.asList(positiveOrdinaryDoubles(), negativeOrdinaryDoubles()));
    }

    /**
     * An {@code Iterable} that generates all positive {@code Double}s, including {@code Infinity} but not positive
     * zero. Does not support removal.
     *
     * Length is 2<sup>63</sup>–2<sup>52</sup> = 9,218,868,437,227,405,312
     */
    @Override
    public @NotNull Iterable<Double> positiveDoubles() {
        return cons(Double.POSITIVE_INFINITY, positiveOrdinaryDoubles());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Double}s, including {@code -Infinity} but not negative
     * zero. Does not support removal.
     *
     * Length is 2<sup>63</sup>–2<sup>52</sup> = 9,218,868,437,227,405,312
     */
    @Override
    public @NotNull Iterable<Double> negativeDoubles() {
        return cons(Double.NEGATIVE_INFINITY, negativeOrdinaryDoubles());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Double}s, including {@code NaN}, {@code Infinity}, and
     * {@code -Infinity}. Does not support removal.
     *
     * Length is 2<sup>64</sup>–2<sup>53</sup>+1 = 18,437,736,874,454,810,625
     */
    @Override
    public @NotNull Iterable<Double> nonzeroDoubles() {
        return concat(
                Arrays.asList(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY),
                nonzeroOrdinaryDoubles()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s, including {@code NaN}, positive and negative zeros,
     * {@code Infinity}, and {@code -Infinity}. Does not support removal.
     *
     * Length is 2<sup>64</sup>–2<sup>53</sup>+3 = 18,437,736,874,454,810,627
     */
    @Override
    public @NotNull Iterable<Double> doubles() {
        return concat(
                Arrays.asList(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, -0.0),
                nonzeroOrdinaryDoubles()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s greater than or equal to {@code a}. Does not include
     * {@code NaN}; may include infinities. Positive and negative zeros are both present or both absent. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Float}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code float}→{@code int} be {@link FloatingPointUtils#toOrderedRepresentation(float)}. If {@code a}≤0,
     * length is 2<sup>31</sup>–2<sup>23</sup>+2–rep({@code a}); otherwise it's
     * 2<sup>31</sup>–2<sup>23</sup>+1–rep({@code a})
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Float}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Float> rangeUp(float a) {
        if (a == Float.POSITIVE_INFINITY) return Collections.singletonList(Float.POSITIVE_INFINITY);
        if (a == Float.NEGATIVE_INFINITY) return filter(f -> !Float.isNaN(f), floats());
        Iterable<Float> noNegativeZeros = cons(
                Float.POSITIVE_INFINITY,
                map(
                        q -> q.a,
                        filter(
                                BigInteger.valueOf((long) FloatingPointUtils.POSITIVE_FINITE_FLOAT_COUNT -
                                        FloatingPointUtils.toOrderedRepresentation(a) + 1),
                                p -> p.a.equals(p.b), map(BinaryFraction::floatRange,
                                rangeUp(BinaryFraction.of(a).get()))
                        )
                )
        );
        return concatMap(f -> f == 0.0f ? Arrays.asList(0.0f, -0.0f) : Collections.singletonList(f), noNegativeZeros);
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s less than or equal to {@code a}. Does not include
     * {@code NaN}; may include infinities. Positive and negative zeros are both present or both absent. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Float}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code float}→{@code int} be {@link FloatingPointUtils#toOrderedRepresentation(float)}. If {@code a}≥0,
     * length is 2<sup>31</sup>–2<sup>23</sup>+2+rep({@code a}); otherwise it's
     * 2<sup>31</sup>–2<sup>23</sup>+1+rep({@code a})
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Float}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Float> rangeDown(float a) {
        return map(f -> -f, rangeUp(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not include {@code NaN}; may include
     * infinities. Positive and negative zeros are both present or both absent. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>{@code b} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Float}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code float}→{@code int} be {@link FloatingPointUtils#toOrderedRepresentation(float)}. If
     * {@code a}≤0≤{@code b}, length is rep({@code b})–rep({@code a})+2; otherwise it's rep({@code b})–rep({@code a})+2
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Float}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Float> range(float a, float b) {
        if (Float.isNaN(a)) {
            throw new ArithmeticException("a cannot be NaN.");
        }
        if (Float.isNaN(b)) {
            throw new ArithmeticException("b cannot be NaN.");
        }
        if (a > b) return Collections.emptyList();
        if (a == Float.NEGATIVE_INFINITY) return rangeDown(b);
        if (b == Float.POSITIVE_INFINITY) return rangeUp(a);
        if (a == Float.POSITIVE_INFINITY || b == Float.NEGATIVE_INFINITY) return Collections.emptyList();
        BinaryFraction bfa = BinaryFraction.of(a).get();
        BinaryFraction bfb = BinaryFraction.of(b).get();
        if (bfa.getExponent() > bfb.getExponent()) {
            return map(f -> -f, range(-b, -a));
        }
        Iterable<Float> noNegativeZeros = map(
                q -> q.a,
                filter(
                        BigInteger.valueOf((long) FloatingPointUtils.toOrderedRepresentation(b) -
                                FloatingPointUtils.toOrderedRepresentation(a) + 1),
                        p -> p.a.equals(p.b),
                        map(BinaryFraction::floatRange, range(bfa, bfb))
                )
        );
        return concatMap(f -> f == 0.0f ? Arrays.asList(0.0f, -0.0f) : Collections.singletonList(f), noNegativeZeros);
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s greater than or equal to {@code a}. Does not include
     * {@code NaN}; may include infinities. Positive and negative zeros are both present or both absent. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Double}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code double}→{@code long} be {@link FloatingPointUtils#toOrderedRepresentation(double)}. If
     * {@code a}≤0, length is 2<sup>63</sup>–2<sup>52</sup>+2–rep({@code a}); otherwise it's
     * 2<sup>63</sup>–2<sup>52</sup>+1–rep({@code a})
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Double}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Double> rangeUp(double a) {
        if (a == Double.POSITIVE_INFINITY) return Collections.singletonList(Double.POSITIVE_INFINITY);
        if (a == Double.NEGATIVE_INFINITY) return filter(d -> !Double.isNaN(d), doubles());
        Iterable<Double> noNegativeZeros = cons(
                Double.POSITIVE_INFINITY,
                map(
                        q -> q.a,
                        filter(
                                BigInteger.valueOf(FloatingPointUtils.POSITIVE_FINITE_DOUBLE_COUNT)
                                        .subtract(BigInteger.valueOf(FloatingPointUtils.toOrderedRepresentation(a)))
                                        .add(BigInteger.ONE),
                                p -> p.a.equals(p.b), map(BinaryFraction::doubleRange,
                                        rangeUp(BinaryFraction.of(a).get()))
                        )
                )
        );
        return concatMap(d -> d == 0.0 ? Arrays.asList(0.0, -0.0) : Collections.singletonList(d), noNegativeZeros);
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s less than or equal to {@code a}. Does not include
     * {@code NaN}; may include infinities. Positive and negative zeros are both present or both absent. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Double}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code double}→{@code long} be {@link FloatingPointUtils#toOrderedRepresentation(double)}. If
     * {@code a}≤0, length is 2<sup>63</sup>–2<sup>52</sup>+2+rep({@code a}); otherwise it's
     * 2<sup>63</sup>–2<sup>52</sup>+1+rep({@code a})
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Double}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Double> rangeDown(double a) {
        return map(d -> -d, rangeUp(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not include {@code NaN}; may include
     * infinities. Positive and negative zeros are both present or both absent. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>{@code b} cannot be {@code NaN}.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Double}s which aren't {@code NaN}.</li>
     * </ul>
     *
     * Let rep:{@code double}→{@code long} be {@link FloatingPointUtils#toOrderedRepresentation(double)}. If
     * {@code a}≤0≤{@code b}, length is rep({@code b})–rep({@code a})+2; otherwise it's rep({@code b})–rep({@code a})+2
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Double}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Double> range(double a, double b) {
        if (Double.isNaN(a)) {
            throw new ArithmeticException("a cannot be NaN.");
        }
        if (Double.isNaN(b)) {
            throw new ArithmeticException("b cannot be NaN.");
        }
        if (a > b) return Collections.emptyList();
        if (a == Double.NEGATIVE_INFINITY) return rangeDown(b);
        if (b == Double.POSITIVE_INFINITY) return rangeUp(a);
        if (a == Double.POSITIVE_INFINITY || b == Double.NEGATIVE_INFINITY) return Collections.emptyList();
        BinaryFraction bfa = BinaryFraction.of(a).get();
        BinaryFraction bfb = BinaryFraction.of(b).get();
        if (bfa.getExponent() > bfb.getExponent()) {
            return map(f -> -f, range(-b, -a));
        }
        Iterable<Double> noNegativeZeros = map(
                q -> q.a,
                filter(
                        BigInteger.valueOf(FloatingPointUtils.toOrderedRepresentation(b))
                                .subtract(BigInteger.valueOf(FloatingPointUtils.toOrderedRepresentation(a)))
                                .add(BigInteger.ONE),
                        p -> p.a.equals(p.b),
                        map(BinaryFraction::doubleRange, range(bfa, bfb))
                )
        );
        return concatMap(d -> d == 0.0 ? Arrays.asList(0.0, -0.0) : Collections.singletonList(d), noNegativeZeros);
    }

    /**
     * An {@code Iterable} that generates all positive {@link BigDecimal}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return map(p -> new BigDecimal(p.a, p.b), pairsLogarithmicOrder(positiveBigIntegers(), integers()));
    }

    /**
     * An {@code Iterable} that generates all negative {@code BigDecimal}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return map(p -> new BigDecimal(p.a, p.b), pairsLogarithmicOrder(negativeBigIntegers(), integers()));
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code BigDecimal}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> nonzeroBigDecimals() {
        return map(p -> new BigDecimal(p.a, p.b), pairsLogarithmicOrder(nonzeroBigIntegers(), integers()));
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return map(p -> new BigDecimal(p.a, p.b), pairsLogarithmicOrder(bigIntegers(), integers()));
    }

    /**
     * Generates positive {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> positiveCanonicalBigDecimals() {
        return filterInfinite(BigDecimalUtils::isCanonical, positiveBigDecimals());
    }

    /**
     * Generates negative {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> negativeCanonicalBigDecimals() {
        return filterInfinite(BigDecimalUtils::isCanonical, negativeBigDecimals());
    }

    /**
     * Generates nonzero {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> nonzeroCanonicalBigDecimals() {
        return filterInfinite(BigDecimalUtils::isCanonical, nonzeroBigDecimals());
    }

    /**
     * Generates {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> canonicalBigDecimals() {
        return filterInfinite(BigDecimalUtils::isCanonical, bigDecimals());
    }

    /**
     * Generates canonical {@code BigDecimal}s greater than or equal to zero and less than or equal to a specified
     * power of ten.
     *
     * <ul>
     *  <li>{@code pow} may be any {@code int}.</li>
     *  <li>The result is a non-removable, infinite {@code Iterable} containing canonical {@code BigDecimal}s between
     *  zero and a power of ten, inclusive.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param pow an {@code int}
     * @return all canonical {@code BigDecimal}s between 0 and 10<sup>{@code pow}</sup>, inclusive
     */
    private static @NotNull Iterable<BigDecimal> zeroToPowerOfTenCanonicalBigDecimals(int pow) {
        return cons(
                BigDecimal.ZERO,
                nub(
                        map(
                                p -> BigDecimalUtils.canonicalize(
                                        new BigDecimal(
                                                p.a,
                                                p.b + MathUtils.ceilingLog(BigInteger.TEN, p.a).intValueExact() - pow
                                        )
                                ),
                                INSTANCE.pairsLogarithmicOrder(
                                        INSTANCE.positiveBigIntegers(),
                                        INSTANCE.naturalIntegers()
                                )
                        )
                )
        );
    }

    /**
     * Given an {@code Iterable xs} of unique, canonical {@code BigDecimal}s, returns an {@code Iterable} whose
     * elements are {x|{@code BigDecimalUtils.canonicalize(x)}∈{@code xs}}.
     *
     * <ul>
     *  <li>{@code xs} must only contain canonical {@code BigDecimal}s and cannot contain any duplicates.</li>
     *  <li>The result does not contain any nulls, is either empty or infinite, and for every {@code BigDecimal} x that
     *  it contains, if {@code BigDecimalUtils.canonicalize(}x{@code )} is y, then the result contains every
     *  {@code BigDecimal} which, when canonicalized, yields y. The result contains no duplicates.</li>
     * </ul>
     *
     * Length is empty if {@code xs} is empty, infinite otherwise
     *
     * @param xs an {@code Iterable} of unique, canonical {@code BigDecimal}s
     * @return all {@code BigDecimal}s which, once canonicalized, belong to {@code xs}
     */
    private static @NotNull Iterable<BigDecimal> uncanonicalize(@NotNull Iterable<BigDecimal> xs) {
        CachedIterator<Integer> integers = new CachedIterator<>(INSTANCE.integers());
        return map(
                p -> p.a.equals(BigDecimal.ZERO) ?
                        new BigDecimal(BigInteger.ZERO, integers.get(p.b).get()) :
                        BigDecimalUtils.setPrecision(
                                p.a.stripTrailingZeros(),
                                p.b + p.a.stripTrailingZeros().precision()
                        ),
                INSTANCE.pairsLogarithmicOrder(xs, INSTANCE.naturalIntegers())
        );
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s greater than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code BigDecimal}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeUp(@NotNull BigDecimal a) {
        return uncanonicalize(rangeUpCanonical(a));
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s less than or equal to {@code a}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code BigDecimal}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeDown(@NotNull BigDecimal a) {
        return uncanonicalize(rangeDownCanonical(a));
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is 0 if a>b, 1 if a=b, and infinite otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code BigDecimal}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigDecimal> range(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        if (eq(a, b)) {
            if (a.signum() == 0) {
                return map(i -> new BigDecimal(BigInteger.ZERO, i), integers());
            } else {
                return map(
                        x -> BigDecimalUtils.setPrecision(a, x),
                        IterableUtils.rangeUp(a.stripTrailingZeros().precision())
                );
            }
        }
        return uncanonicalize(rangeCanonical(a, b));
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s greater than or equal to {@code a}. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing canonical {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return canonical {@code BigDecimal}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeUpCanonical(@NotNull BigDecimal a) {
        BigDecimal ca = BigDecimalUtils.canonicalize(a);
        return map(
                bd -> BigDecimalUtils.canonicalize(bd.add(ca)),
                cons(BigDecimal.ZERO, positiveCanonicalBigDecimals())
        );
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s less than or equal to {@code a}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing canonical {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return canonical {@code BigDecimal}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeDownCanonical(@NotNull BigDecimal a) {
        return map(BigDecimal::negate, rangeUpCanonical(a.negate()));
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s between {@code a} and {@code b}, inclusive.
     * If {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing canonical {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is 0 if a>b, 1 if a=b, and infinite otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return canonical {@code BigDecimal}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeCanonical(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        if (gt(a, b)) return Collections.emptyList();
        if (eq(a, b)) return Collections.singletonList(BigDecimalUtils.canonicalize(a));
        BigDecimal difference = BigDecimalUtils.canonicalize(b.subtract(a));
        return map(
                c -> BigDecimalUtils.canonicalize(c.add(a)),
                filter(
                        bd -> le(bd, difference),
                        zeroToPowerOfTenCanonicalBigDecimals(BigDecimalUtils.ceilingLog10(difference))
                )
        );
    }

    /**
     * See {@link IterableUtils#cons}.
     */
    @Override
    public @NotNull <T> Iterable<T> withElement(@Nullable T x, @NotNull Iterable<T> xs) {
        return cons(x, xs);
    }

    /**
     * Generates all pairs of values, given an {@code Iterable} of possible first values of the pairs, and a function
     * mapping each possible first value to an {@code Iterable} of possible second values. For each first value, the
     * second values are listed consecutively. If all the input lists are unique, the output pairs are unique as well.
     * This method is similar to {@link ExhaustiveProvider#dependentPairsInfinite(Iterable, Function)}, but with
     * different conditions on the arguments.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>{@code f} must terminate and not return null when applied to any element of {@code xs}. All results, except
     *  possibly the result when applied to the last element of {@code xs} (if it exists) must be finite.</li>
     *  <li>The result is non-removable and does not contain nulls.</li>
     * </ul>
     *
     * Length is finite iff {@code f.apply(last(xs))} is finite
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     * @return all possible pairs of values specified by {@code xs} and {@code f}
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return concatMap(x -> map(y -> new Pair<>(x, y), f.apply(x)), xs);
    }

    /**
     * Generates all pairs of values, given an infinite {@code Iterable} of possible first values of the pairs, and a
     * function mapping each possible first value to an infinite {@code Iterable} of possible second values. The pairs
     * are traversed along a Z-curve. If all the input lists are unique, the output pairs are unique as well. This
     * method is similar to {@link ExhaustiveProvider#dependentPairs(Iterable, Function)}, but with different
     * conditions on the arguments.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>{@code f} must terminate and not return null when applied to any element of {@code xs}. All results must be
     *  infinite.</li>
     *  <li>The result is non-removable and does not contain nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     * @return all possible pairs of values specified by {@code xs} and {@code f}
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfinite(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull
            CachedIterator<A> as = new CachedIterator<>(xs);
            private final @NotNull Map<A, CachedIterator<B>> aToBs = new HashMap<>();
            private final @NotNull Iterator<Pair<Integer, Integer>> indices = pairs(naturalIntegers()).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Pair<A, B> next() {
                Pair<Integer, Integer> index = indices.next();
                A a = as.get(index.a).get();
                CachedIterator<B> bs = aToBs.get(a);
                if (bs == null) {
                    bs = new CachedIterator<>(f.apply(a));
                    aToBs.put(a, bs);
                }
                return new Pair<>(a, bs.get(index.b).get());
            }
        };
    }

    /**
     * Generates all possible pairs of values where the first element is selected from one {@code Iterable} and the
     * second element from another. There are many possible orderings of pairs; to make the ordering unique, you
     * can specify an unpairing function–a bijective function from natural {@code BigInteger}s to pairs of natural
     * {@code BigInteger}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code unpairingFunction} must bijectively map natural {@code BigInteger}s to pairs of natural
     *  {@code BigInteger}s.</li>
     *  <li>{@code as} cannot be null.</li>
     *  <li>{@code bs} cannot be null.</li>
     *  <li>The result is non-removable and is the cartesian product of two sets.</li>
     * </ul>
     *
     * Length is |{@code as}||{@code bs}|
     *
     * @param unpairingFunction a bijection ℕ→ℕ×ℕ
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in an order determined by {@code unpairingFunction}
     */
    private @NotNull <A, B> Iterable<Pair<A, B>> pairsByFunction(
            @NotNull Function<BigInteger, Pair<BigInteger, BigInteger>> unpairingFunction,
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        if (isEmpty(as) || isEmpty(bs)) return Collections.emptyList();
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull CachedIterator<A> cas = new CachedIterator<>(as);
            private final @NotNull CachedIterator<B> cbs = new CachedIterator<>(bs);
            private final @NotNull Iterator<BigInteger> is = naturalBigIntegers().iterator();
            private @NotNull Optional<BigInteger> outputSize = Optional.empty();
            private @NotNull BigInteger index = BigInteger.ZERO;
            private boolean reachedEnd = false;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public @NotNull Pair<A, B> next() {
                if (reachedEnd) {
                    throw new NoSuchElementException();
                }
                while (true) {
                    Pair<BigInteger, BigInteger> indices = unpairingFunction.apply(is.next());
                    NullableOptional<A> oa = cas.get(indices.a.intValueExact());
                    if (!oa.isPresent()) continue;
                    NullableOptional<B> ob = cbs.get(indices.b.intValueExact());
                    if (!ob.isPresent()) continue;
                    if (!outputSize.isPresent() && cas.knownSize().isPresent() && cbs.knownSize().isPresent()) {
                        outputSize = Optional.of(
                                BigInteger.valueOf(cas.knownSize().get())
                                        .multiply(BigInteger.valueOf(cbs.knownSize().get()))
                        );
                    }
                    index = index.add(BigInteger.ONE);
                    if (outputSize.isPresent() && index.equals(outputSize.get())) {
                        reachedEnd = true;
                    }
                    return new Pair<>(oa.get(), ob.get());
                }
            }
        };
    }

    /**
     * Generates all possible pairs of values where the first element and second elements are selected from the same
     * {@code Iterable}. There are many possible orderings of pairs; to make the ordering unique, you can specify an
     * unpairing function–a bijective function from natural {@code BigInteger}s to pairs of natural
     * {@code BigInteger}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code unpairingFunction} must bijectively map natural {@code BigInteger}s to pairs of natural
     *  {@code BigInteger}s.</li>
     *  <li>{@code as} cannot be null.</li>
     *  <li>{@code bs} cannot be null.</li>
     *  <li>The result is non-removable and is the cartesian product of two sets.</li>
     * </ul>
     *
     * Length is |{@code as}||{@code bs}|
     *
     * @param unpairingFunction a bijection ℕ→ℕ×ℕ
     * @param xs the {@code Iterable} from which the components of the pairs are selected
     * @param <T> the type of the {@code Iterable}'s elements
     * @return all pairs of elements from {@code xs} in an order determined by {@code unpairingFunction}
     */
    private @NotNull <T> Iterable<Pair<T, T>> pairsByFunction(
            @NotNull Function<BigInteger, Pair<BigInteger, BigInteger>> unpairingFunction,
            @NotNull Iterable<T> xs
    ) {
        if (isEmpty(xs)) return Collections.emptyList();
        return () -> new NoRemoveIterator<Pair<T, T>>() {
            private final @NotNull CachedIterator<T> cxs = new CachedIterator<>(xs);
            private final @NotNull Iterator<BigInteger> is = naturalBigIntegers().iterator();
            private @NotNull Optional<BigInteger> outputSize = Optional.empty();
            private @NotNull BigInteger index = BigInteger.ZERO;
            private boolean reachedEnd = false;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public @NotNull Pair<T, T> next() {
                if (reachedEnd) {
                    throw new NoSuchElementException();
                }
                while (true) {
                    Pair<BigInteger, BigInteger> indices = unpairingFunction.apply(is.next());
                    NullableOptional<T> oa = cxs.get(indices.a.intValueExact());
                    if (!oa.isPresent()) continue;
                    NullableOptional<T> ob = cxs.get(indices.b.intValueExact());
                    if (!ob.isPresent()) continue;
                    if (!outputSize.isPresent() && cxs.knownSize().isPresent()) {
                        outputSize = Optional.of(BigInteger.valueOf(cxs.knownSize().get()).pow(2));
                    }
                    index = index.add(BigInteger.ONE);
                    if (outputSize.isPresent() && index.equals(outputSize.get())) {
                        reachedEnd = true;
                    }
                    return new Pair<>(oa.get(), ob.get());
                }
            }
        };
    }

    /**
     * Returns all pairs of elements taken from two {@code Iterable}s in such a way that the first component grows
     * linearly but the second grows logarithmically. Does not support removal.
     *
     * <ul>
     *  <li>{@code as} cannot be null.</li>
     *  <li>{@code bs} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing all pairs of elements taken from two
     *  {@code Iterable}s. The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@link IntegerUtils#logarithmicDemux(BigInteger)} and interpreting the resulting pairs as indices into the
     *  original {@code Iterable}s.</li>
     * </ul>
     *
     * Length is |{@code as}||{@code bs}|
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in logarithmic order
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return pairsByFunction(IntegerUtils::logarithmicDemux, as, bs);
    }

    /**
     * Returns all pairs of elements taken from one {@code Iterable}s in such a way that the first component grows
     * linearly but the second grows logarithmically (hence the name). Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing all pairs of elements taken from some
     *  {@code Iterable}. The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@link IntegerUtils#logarithmicDemux(BigInteger)} and interpreting the resulting pairs as indices into the
     *  original {@code Iterable}.</li>
     * </ul>
     *
     * Length is |{@code xs}|<sup>2</sup>
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all pairs of elements from {@code xs} in logarithmic order
     */
    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        return pairsByFunction(IntegerUtils::logarithmicDemux, xs);
    }

    /**
     * Returns all pairs of elements taken from two {@code Iterable}s in such a way that the first component grows
     * as O(n<sup>2/3</sup>) but the second grows as O(n<sup>1/3</sup>). Does not support removal.
     *
     * <ul>
     *  <li>{@code as} cannot be null.</li>
     *  <li>{@code bs} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing all pairs of elements taken from two
     *  {@code Iterable}s. The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@link IntegerUtils#squareRootDemux(BigInteger)} and interpreting the resulting pairs as indices into the
     *  original {@code Iterable}s.</li>
     * </ul>
     *
     * Length is |{@code as}||{@code bs}|
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in square-root order
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return pairsByFunction(IntegerUtils::squareRootDemux, as, bs);
    }

    /**
     * Returns all pairs of elements taken from one {@code Iterable}s in such a way that the first component grows
     * as O(n<sup>2/3</sup>) but the second grows as O(n<sup>1/3</sup>). Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing all pairs of elements taken from some
     *  {@code Iterable}. The ordering of these elements is determined by mapping the sequence 0, 1, 2, ... by
     *  {@link IntegerUtils#squareRootDemux(BigInteger)} and interpreting the resulting pairs as indices into the
     *  original {@code Iterable}.</li>
     * </ul>
     *
     * Length is |{@code xs}|<sup>2</sup>
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return all pairs of elements from {@code xs} in square-root order
     */
    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        return pairsByFunction(IntegerUtils::squareRootDemux, xs);
    }

    /**
     * Given a list of non-negative integers in weakly increasing order, returns all distinct permutations of these
     * integers in lexicographic order.
     *
     * <ul>
     *  <li>{@code start} must be weakly increasing and can only contain non-negative integers.</li>
     *  <li>The result is in lexicographic order, contains only non-negative integers, contains no repetitions, and is
     *  the complete set of permutations of some list.</li>
     * </ul>
     *
     * Length is the number of distinct permutations of {@code start}
     *
     * @param start a lexicographically smallest permutation
     * @return all permutations of {@code start}
     */
    private static @NotNull Iterable<List<Integer>> finitePermutationIndices(@NotNull List<Integer> start) {
        return () -> new NoRemoveIterator<List<Integer>>() {
            private final @NotNull BigInteger outputSize = MathUtils.permutationCount(start);
            private @NotNull List<Integer> list = toList(start);
            private @NotNull BigInteger index = BigInteger.ZERO;

            @Override
            public boolean hasNext() {
                return lt(index, outputSize);
            }

            @Override
            public @NotNull List<Integer> next() {
                if (index.equals(BigInteger.ZERO)) {
                    index = BigInteger.ONE;
                    return list;
                }
                list = toList(list);
                int k = list.size() - 2;
                while (list.get(k) >= list.get(k + 1)) k--;
                int m = list.size() - 1;
                while (m > k && list.get(k) >= list.get(m)) m--;
                Collections.swap(list, k, m);
                int i = k + 1;
                int j = list.size() - 1;
                while (i < j) {
                    Collections.swap(list, i, j);
                    i++;
                    j--;
                }
                index = index.add(BigInteger.ONE);
                return list;
            }
        };
    }

    /**
     * Returns an {@code Iterable} containing every distinct permutation of a list. The result is ordered
     * lexicographically, preserving the order in the initial list.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is lexicographically increasing with respect to some order on {@code T}, contains no
     *  repetitions, and is the complete set of permutations of some list.</li>
     * </ul>
     *
     * Length is the number of distinct permutations of {@code start}
     *
     * @param xs a list of elements
     * @param <T> the type of the given {@code List}'s elements
     * @return all distinct permutations of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> permutationsFinite(@NotNull List<T> xs) {
        List<T> nub = toList(nub(xs));
        Map<T, Integer> indexMap = toMap(zip(nub, IterableUtils.rangeUp(0)));
        List<Integer> startingIndices = sort(map(indexMap::get, xs));
        return map(is -> toList(map(nub::get, is)), finitePermutationIndices(startingIndices));
    }

    /**
     * Returns an {@code Iterable} containing every permutation of an {@code Iterable}. If the {@code Iterable} is
     * finite, all permutations are generated; if it is infinite, then only permutations that are equal to the identity
     * except in a finite prefix are generated. Unlike {@link ExhaustiveProvider#permutationsFinite(List)}, this method
     * may return an {@code Iterable} with duplicate elements.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is the set of permutations of some {@code Iterable} such that each permutation differs from
     *  {@code xs} in a finite prefix.</li>
     * </ul>
     *
     * Length is |{@code xs}|!
     *
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of the given {@code Iterable}'s elements
     */
    @Override
    public @NotNull <T> Iterable<Iterable<T>> prefixPermutations(@NotNull Iterable<T> xs) {
        if (!lengthAtLeast(2, xs)) return Collections.singletonList(new NoRemoveIterable<>(xs));
        return () -> new NoRemoveIterator<Iterable<T>>() {
            private final @NotNull CachedIterator<T> cxs = new CachedIterator<>(xs);
            private @NotNull Optional<BigInteger> outputSize = Optional.empty();
            private Iterator<List<Integer>> prefixIndices;
            private int prefixLength = 0;
            private @NotNull BigInteger index = BigInteger.ZERO;
            private boolean reachedEnd = false;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public @NotNull Iterable<T> next() {
                if (reachedEnd) {
                    throw new NoSuchElementException();
                }
                if (prefixIndices == null || !prefixIndices.hasNext()) {
                    updatePrefixIndices();
                }
                Iterable<T> permutation = concat(cxs.get(prefixIndices.next()).get(), drop(prefixLength, xs));
                if (!outputSize.isPresent() && cxs.knownSize().isPresent()) {
                    outputSize = Optional.of(MathUtils.factorial(cxs.knownSize().get()));
                }
                index = index.add(BigInteger.ONE);
                if (outputSize.isPresent() && index.equals(outputSize.get())) {
                    reachedEnd = true;
                }
                return permutation;
            }

            private void updatePrefixIndices() {
                if (prefixIndices == null) {
                    prefixLength = 0;
                } else if (prefixLength == 0) {
                    prefixLength = 2;
                } else {
                    prefixLength++;
                }
                prefixIndices = filter(
                        is -> is.isEmpty() || last(is) != prefixLength - 1,
                        finitePermutationIndices(toList(IterableUtils.range(0, prefixLength - 1)))
                ).iterator();
            }
        };
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
    public @NotNull <T> Iterable<List<T>> listsLex(int length, @NotNull Iterable<T> xs) {
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
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLex(
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
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triplesLex(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                pairsLex(as, (Iterable<Pair<B, C>>) pairsLex(bs, cs))
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
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruplesLex(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return map(
                p -> new Quadruple<>(p.a.a, p.a.b, p.b.a, p.b.b),
                pairsLex(
                        (Iterable<Pair<A, B>>) pairsLex(as, bs),
                        (Iterable<Pair<C, D>>) pairsLex(cs, ds)
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
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuplesLex(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return map(
                p -> new Quintuple<>(p.a.a, p.a.b, p.b.a, p.b.b, p.b.c),
                pairsLex(
                        (Iterable<Pair<A, B>>) pairsLex(as, bs),
                        (Iterable<Triple<C, D, E>>) triplesLex(cs, ds, es)
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
    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuplesLex(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        return map(
                p -> new Sextuple<>(p.a.a, p.a.b, p.a.c, p.b.a, p.b.b, p.b.c),
                pairsLex(
                        (Iterable<Triple<A, B, C>>) triplesLex(as, bs, cs),
                        (Iterable<Triple<D, E, F>>) triplesLex(ds, es, fs)
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
    public @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuplesLex(
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
                pairsLex(
                        (Iterable<Triple<A, B, C>>) triplesLex(as, bs, cs),
                        (Iterable<Quadruple<D, E, F, G>>) quadruplesLex(ds, es, fs, gs)
                )
        );
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
    public @NotNull Iterable<String> stringsLex(int length, @NotNull String s) {
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
    public @NotNull <T> Iterable<List<T>> listsShortlex(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return Collections.singletonList(new ArrayList<>());
        return concatMap(i -> listsLex(i.intValueExact(), xs), naturalBigIntegers());
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
    public @NotNull Iterable<String> stringsShortlex(@NotNull String s) {
        if (isEmpty(s)) return Collections.singletonList("");
        return concatMap(i -> stringsLex(i.intValueExact(), s), naturalBigIntegers());
    }

    public @NotNull <T> Iterable<List<T>> listsShortlexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return minSize == 0 ? Collections.singletonList(new ArrayList<>()) : new ArrayList<>();
        return concatMap(i -> listsLex(i, xs), rangeUp(minSize));
    }

    public @NotNull Iterable<String> stringsShortlexAtLeast(int minSize, @NotNull String s) {
        if (isEmpty(s)) return minSize == 0 ? Collections.singletonList("") : new ArrayList<>();
        return concatMap(i -> stringsLex(i.intValueExact(), s), naturalBigIntegers());
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        if (size == 0) {
            return Collections.singletonList(new ArrayList<T>());
        }
        CachedIterator<T> ii = new CachedIterator<>(xs);
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
                filter(Optional::isPresent, stopAt(lastList, map(f, naturalBigIntegers())))
        );
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
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return pairsByFunction(
                bi -> {
                    List<BigInteger> list = IntegerUtils.demux(2, bi);
                    return new Pair<>(list.get(0), list.get(1));
                },
                as,
                bs
        );
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Pair<>(list.get(0), list.get(1)), lists(2, xs));
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
    @Override
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs)) return new ArrayList<>();
        CachedIterator<A> aii = new CachedIterator<>(as);
        CachedIterator<B> bii = new CachedIterator<>(bs);
        CachedIterator<C> cii = new CachedIterator<>(cs);
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
                                        IterableUtils.map(bi -> f.apply(bi), naturalBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Triple<>(list.get(0), list.get(1), list.get(2)), lists(3, xs));
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
    @Override
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs) || isEmpty(ds)) return new ArrayList<>();
        CachedIterator<A> aii = new CachedIterator<>(as);
        CachedIterator<B> bii = new CachedIterator<>(bs);
        CachedIterator<C> cii = new CachedIterator<>(cs);
        CachedIterator<D> dii = new CachedIterator<>(ds);
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
                                        IterableUtils.map(bi -> f.apply(bi), naturalBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(list -> new Quadruple<>(list.get(0), list.get(1), list.get(2), list.get(3)), lists(4, xs));
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
    @Override
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        if (isEmpty(as) || isEmpty(bs) || isEmpty(cs) || isEmpty(ds) || isEmpty(es)) return new ArrayList<>();
        CachedIterator<A> aii = new CachedIterator<>(as);
        CachedIterator<B> bii = new CachedIterator<>(bs);
        CachedIterator<C> cii = new CachedIterator<>(cs);
        CachedIterator<D> dii = new CachedIterator<>(ds);
        CachedIterator<E> eii = new CachedIterator<>(es);
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
                                        IterableUtils.map(bi -> f.apply(bi), naturalBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(
                list -> new Quintuple<>(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)),
                lists(5, xs)
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
    @Override
    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuples(
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
        CachedIterator<A> aii = new CachedIterator<>(as);
        CachedIterator<B> bii = new CachedIterator<>(bs);
        CachedIterator<C> cii = new CachedIterator<>(cs);
        CachedIterator<D> dii = new CachedIterator<>(ds);
        CachedIterator<E> eii = new CachedIterator<>(es);
        CachedIterator<F> fii = new CachedIterator<>(fs);
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
                                        IterableUtils.map(bi -> f.apply(bi), naturalBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return new ArrayList<>();
        return map(
                list -> new Sextuple<>(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5)),
                lists(6, xs)
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
    @Override
    public @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuples(
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
        CachedIterator<A> aii = new CachedIterator<>(as);
        CachedIterator<B> bii = new CachedIterator<>(bs);
        CachedIterator<C> cii = new CachedIterator<>(cs);
        CachedIterator<D> dii = new CachedIterator<>(ds);
        CachedIterator<E> eii = new CachedIterator<>(es);
        CachedIterator<F> fii = new CachedIterator<>(fs);
        CachedIterator<G> gii = new CachedIterator<>(gs);
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
                                        IterableUtils.map(bi -> f.apply(bi), naturalBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
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

    @Override
    public @NotNull Iterable<String> strings(int size, @NotNull String s) {
        return map(IterableUtils::charsToString, lists(size, fromString(s)));
    }

    @Override
    public @NotNull Iterable<String> strings(int size) {
        return map(IterableUtils::charsToString, lists(size, characters()));
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        CachedIterator<T> ii = new CachedIterator<>(xs);
        Function<BigInteger, Optional<List<T>>> f = bi -> {
            if (bi.equals(BigInteger.ZERO)) {
                return Optional.of(new ArrayList<T>());
            }
            bi = bi.subtract(BigInteger.ONE);
            Pair<BigInteger, BigInteger> sizeIndex = IntegerUtils.logarithmicDemux(bi);
            int size = sizeIndex.b.intValueExact() + 1;
            return ii.get(map(BigInteger::intValueExact, IntegerUtils.demux(size, sizeIndex.a)));
        };
        return optionalMap(f, naturalBigIntegers());
    }

    @Override
    public @NotNull Iterable<String> strings(@NotNull String s) {
        return map(IterableUtils::charsToString, lists(fromString(s)));
    }

    @Override
    public @NotNull Iterable<String> strings() {
        return map(IterableUtils::charsToString, lists(characters()));
    }

    @Override
    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        if (minSize == 0) return lists(xs);
        CachedIterator<T> ii = new CachedIterator<>(xs);
        Function<BigInteger, Optional<List<T>>> f = bi -> {
            if (bi.equals(BigInteger.ZERO)) {
                return Optional.<List<T>>empty();
            }
            bi = bi.subtract(BigInteger.ONE);
            Pair<BigInteger, BigInteger> sizeIndex = IntegerUtils.logarithmicDemux(bi);
            int size = sizeIndex.b.intValueExact() + minSize;
            return ii.get(map(BigInteger::intValueExact, IntegerUtils.demux(size, sizeIndex.a)));
        };
        return optionalMap(f, naturalBigIntegers());
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull String s) {
        return map(IterableUtils::charsToString, listsAtLeast(minSize, fromString(s)));
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int minSize) {
        return map(IterableUtils::charsToString, listsAtLeast(minSize, characters()));
    }

    public @NotNull <T> Iterable<List<T>> distinctListsLex(int length, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Pair<T, T>> distinctPairsLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriplesLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruplesLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuplesLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuplesLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuplesLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> distinctStringsLex(int length, @NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> distinctListsLex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> distinctStringsLex(@NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> distinctListsLexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> distinctStringsLexAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> distinctListsShortlex(@NotNull Iterable<T> xs) {
        int n = length(xs);
        return filter(IterableUtils::unique, takeWhile(ys -> ys.size() <= n, listsShortlex(xs)));
    }

    public @NotNull Iterable<String> distinctStringsShortlex(@NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> distinctListsShortlexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> distinctStringsShortlexAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> distinctLists(int size, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return filter(p -> !p.a.equals(p.b), pairs(xs));
    }

    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return filter(t -> !t.a.equals(t.b) && !t.a.equals(t.c) && !t.b.equals(t.c), triples(xs));
    }

    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return filter(q -> and(map(p -> !p.a.equals(p.b), pairs(Quadruple.toList(q)))), quadruples(xs));
    }

    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return filter(q -> and(map(p -> !p.a.equals(p.b), pairs(Quintuple.toList(q)))), quintuples(xs));
    }

    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return filter(s -> and(map(p -> !p.a.equals(p.b), pairs(Sextuple.toList(s)))), sextuples(xs));
    }

    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuples(@NotNull Iterable<T> xs) {
        return filter(s -> and(map(p -> !p.a.equals(p.b), pairs(Septuple.toList(s)))), septuples(xs));
    }

    @Override
    public @NotNull Iterable<String> distinctStrings(int size, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> distinctStrings(int size) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> distinctLists(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return Collections.singletonList(new ArrayList<>());
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull CachedIterator<T> cxs = new CachedIterator<>(xs);
            private final @NotNull Iterator<List<Integer>> xsi = lists(naturalIntegers()).iterator();
            private @NotNull Optional<BigInteger> outputSize = Optional.empty();
            private @NotNull BigInteger index = BigInteger.ZERO;
            private boolean reachedEnd = false;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public List<T> next() {
                if (reachedEnd) {
                    throw new NoSuchElementException();
                }
                outer:
                while (true) {
                    List<T> list = new ArrayList<>();
                    Set<T> seen = new HashSet<>();
                    List<Integer> indices = xsi.next();
                    for (int index : indices) {
                        int j = 0;
                        T x;
                        do {
                            NullableOptional<T> ox = cxs.get(j);
                            if (!ox.isPresent()) continue outer;
                            x = ox.get();
                            j++;
                        } while (seen.contains(x));
                        for (int i = 0; i < index; i++) {
                            do {
                                NullableOptional<T> ox = cxs.get(j);
                                if (!ox.isPresent()) continue outer;
                                x = ox.get();
                                j++;
                            } while (seen.contains(x));
                        }
                        list.add(x);
                        seen.add(x);
                    }
                    if (!outputSize.isPresent() && cxs.knownSize().isPresent()) {
                        outputSize = Optional.of(MathUtils.numberOfArrangementsOfASet(cxs.knownSize().get()));
                    }
                    index = index.add(BigInteger.ONE);
                    if (outputSize.isPresent() && index.equals(outputSize.get())) {
                        reachedEnd = true;
                    }
                    return list;
                }
            }
        };
    }

    @Override
    public @NotNull Iterable<String> distinctStrings(@NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> distinctStrings() {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> distinctListsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> bagsLex(int size, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringBagsLex(int size, @NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> bagsShortlex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringBagsShortlex(@NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> bagsShortlexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringBagsShortlexAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> bags(int size, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBags(int size, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBags(int size) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> bags(@NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBags(@NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBags() {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> bagsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBagsAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringBagsAtLeast(int minSize) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> subsetsLex(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            return Collections.singletonList(new ArrayList<T>());
        return () -> new NoRemoveIterator<List<T>>() {
            private CachedIterator<T> cxs = new CachedIterator<T>(xs);
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
        };
    }

    public @NotNull Iterable<String> stringSubsetsLex(@NotNull String s) {
        return map(IterableUtils::charsToString, subsetsLex(fromString(s)));
    }

    public @NotNull <T> Iterable<List<T>> subsetsLexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringSubsetsLexAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> subsetsShortlex(@NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringSubsetsShortlex(@NotNull String s) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> subsetsShortlexAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    public @NotNull Iterable<String> stringSubsetsShortlexAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> subsets(int size, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringSubsets(int size, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringSubsets(int size) {
        return null;
    }

    @Override
    public @NotNull <T> Iterable<List<T>> subsets(@NotNull Iterable<T> xs) {
        CachedIterator<T> cxs = new CachedIterator<>(xs);
        return map(
                Optional::get,
                takeWhile(
                        Optional::isPresent, map(i -> cxs.select(IntegerUtils.bits(i)),
                                IterableUtils.rangeUp(BigInteger.ZERO))
                )
        );
    }

    @Override
    public @NotNull <T> Iterable<List<T>> subsetsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize, @NotNull String s) {
        return null;
    }

    @Override
    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize) {
        return null;
    }

    public @NotNull <T> Iterable<List<T>> controlledListsLex(@NotNull List<Iterable<T>> xss) {
        if (xss.size() == 0) return Collections.singletonList(new ArrayList<T>());
        if (xss.size() == 1) return map(Collections::singletonList, xss.get(0));
        if (xss.size() == 2) return map(p -> Arrays.<T>asList(p.a, p.b), pairsLex(xss.get(0), xss.get(1)));
        List<Iterable<T>> leftList = new ArrayList<>();
        List<Iterable<T>> rightList = new ArrayList<>();
        for (int i = 0; i < xss.size() / 2; i++) {
            leftList.add(xss.get(i));
        }
        for (int i = xss.size() / 2; i < xss.size(); i++) {
            rightList.add(xss.get(i));
        }
        Iterable<List<T>> leftLists = controlledListsLex(leftList);
        Iterable<List<T>> rightLists = controlledListsLex(rightList);
        return map(p -> toList(concat(p.a, p.b)), pairsLex(leftLists, rightLists));
    }

    @Override
    public @NotNull Iterable<String> substrings(@NotNull String s) {
        return nub(super.substrings(s));
    }

    /**
     * Determines whether {@code this} is equal to {@code that}. This implementation is the same as in
     * {@link java.lang.Object#equals}, but repeated here for clarity.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExhaustiveProvider}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code ExhaustiveProvider} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that;
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExhaustiveProvider}.</li>
     *  <li>The result is 0.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code ExhaustiveProvider}.</li>
     *  <li>The result is {@code "ExhaustiveProvider"}.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    @Override
    public String toString() {
        return "ExhaustiveProvider";
    }
}
