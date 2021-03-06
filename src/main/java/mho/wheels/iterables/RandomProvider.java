package mho.wheels.iterables;

import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.random.IsaacPRNG;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.assertEquals;

/**
 * <p>A {@code RandomProvider} produces {@code Iterable}s that randomly generate some set of values with a specified
 * distribution. A {@code RandomProvider} is deterministic, but not immutable: its state changes every time a random
 * value is generated. It may be reverted to its original state with {@link RandomProvider#reset}.</p>
 *
 * <p>{@code RandomProvider} uses the cryptographically-secure ISAAC pseudorandom number generator, implemented in
 * {@link mho.wheels.random.IsaacPRNG}. The source of its randomness is a {@code int[]} seed. It contains three scale
 * parameters which some of the distributions depend on; the exact relationship between the parameters and the
 * distributions is specified in the distribution's documentation.</p>
 *
 * <p>To create an instance which shares a generator with {@code this}, use {@link RandomProvider#copy()}. To create
 * an instance which copies the generator, use {@link RandomProvider#deepCopy()}.</p>
 *
 * <p>Note that sometimes the documentation will say things like "returns an {@code Iterable} containing all
 * {@code String}s". This cannot strictly be true, since {@code IsaacPRNG} has a finite period, and will therefore
 * produce only a finite number of {@code String}s. So in general, the documentation often pretends that the source of
 * randomness is perfect (but still deterministic).</p>
 */
public final strictfp class RandomProvider extends IterableProvider {
    /**
     * A list of all {@code Ordering}s.
     */
    private static final @NotNull List<Ordering> ORDERINGS = toList(ExhaustiveProvider.INSTANCE.orderings());

    /**
     * A list of all {@code RoundingMode}s.
     */
    private static final @NotNull List<RoundingMode> ROUNDING_MODES =
            toList(ExhaustiveProvider.INSTANCE.roundingModes());

    /**
     * The default value of {@code scale}.
     */
    private static final int DEFAULT_SCALE = 32;

    /**
     * The default value of {@code secondaryScale}.
     */
    private static final int DEFAULT_SECONDARY_SCALE = 8;

    /**
     * The default value of {@code tertiaryScale}.
     */
    private static final int DEFAULT_TERTIARY_SCALE = 2;

    /**
     * A list of numbers which determines exactly which values will be deterministically output over the life of
     * {@code this}. It must have length {@link mho.wheels.random.IsaacPRNG#SIZE}.
     */
    private final @NotNull List<Integer> seed;

    /**
     * A pseudorandom number generator. It changes state every time a random number is generated.
     */
    private @NotNull IsaacPRNG prng;

    /**
     * A parameter that determines the size of some of the generated objects.
     */
    private int scale = DEFAULT_SCALE;

    /**
     * Another parameter that determines the size of some of the generated objects.
     */
    private int secondaryScale = DEFAULT_SECONDARY_SCALE;

    /**
     * A third parameter that determines the size of some of the generated objects.
     */
    private int tertiaryScale = DEFAULT_TERTIARY_SCALE;

    /**
     * Constructs a {@code RandomProvider} with a seed generated from the current system time.
     *
     * <ul>
     *  <li>(conjecture) Any {@code RandomProvider} with default {@code scale}, {@code secondaryScale}, and
     *  {@code tertiaryScale} may be constructed with this constructor.</li>
     * </ul>
     */
    public RandomProvider() {
        prng = new IsaacPRNG();
        seed = new ArrayList<>();
        for (int i = 0; i < IsaacPRNG.SIZE; i++) {
            seed.add(prng.nextInt());
        }
        prng = new IsaacPRNG(seed);
    }

    /**
     * Constructs a {@code RandomProvider} with a given seed.
     *
     * <ul>
     *  <li>{@code seed} must have length {@link mho.wheels.random.IsaacPRNG#SIZE}.</li>
     *  <li>Any {@code RandomProvider} with default {@code scale}, {@code secondaryScale}, and {@code tertiaryScale}
     *  may be constructed with this constructor.</li>
     * </ul>
     *
     * @param seed the source of randomness
     */
    public RandomProvider(@NotNull List<Integer> seed) {
        if (seed.size() != IsaacPRNG.SIZE) {
            throw new IllegalArgumentException("seed must have length mho.wheels.random.IsaacPRNG#SIZE, which is " +
                    IsaacPRNG.SIZE + ". Length of invalid seed: " + seed.size());
        }
        this.seed = seed;
        prng = new IsaacPRNG(seed);
    }

    /**
     * A {@code RandomProvider} used for testing. This allows for deterministic testing without manually setting up a
     * lengthy seed each time.
     */
    public static @NotNull RandomProvider example() {
        IsaacPRNG prng = IsaacPRNG.example();
        List<Integer> seed = new ArrayList<>();
        for (int i = 0; i < IsaacPRNG.SIZE; i++) {
            seed.add(prng.nextInt());
        }
        return new RandomProvider(seed);
    }

    /**
     * Returns {@code this}'s first scale parameter.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the first scale parameter of {@code this}
     */
    public int getScale() {
        return scale;
    }

    /**
     * Returns {@code this}'s second scale parameter.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the second scale parameter of {@code this}
     */
    public int getSecondaryScale() {
        return secondaryScale;
    }

    /**
     * Returns {@code this}'s third scale parameter.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the third scale parameter of {@code this}
     */
    public int getTertiaryScale() {
        return tertiaryScale;
    }

    /**
     * Returns {@code this}'s seed. Makes a defensive copy.
     *
     * <ul>
     *  <li>The result is an array of {@link mho.wheels.random.IsaacPRNG#SIZE} {@code int}s.</li>
     * </ul>
     *
     * @return the seed of {@code this}
     */
    public @NotNull List<Integer> getSeed() {
        return toList(seed);
    }

    /**
     * A {@code RandomProvider} with the same fields as {@code this}. The copy shares {@code prng} with the original.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull RandomProvider copy() {
        RandomProvider copy = new RandomProvider(seed);
        copy.scale = scale;
        copy.secondaryScale = secondaryScale;
        copy.tertiaryScale = tertiaryScale;
        copy.prng = prng;
        return copy;
    }

    /**
     * A {@code RandomProvider} with the same fields as {@code this}. The copy receives a new copy of {@code prng},
     * so generating values from the copy will not affect the state of the original's {@code prng}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull RandomProvider deepCopy() {
        RandomProvider copy = new RandomProvider(seed);
        copy.scale = scale;
        copy.secondaryScale = secondaryScale;
        copy.tertiaryScale = tertiaryScale;
        copy.prng = prng.copy();
        return copy;
    }

    /**
     * A {@code RandomProvider} with the same fields as {@code this} except for a new scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the new scale
     * @return A copy of {@code this} with a new scale
     */
    @Override
    public @NotNull RandomProvider withScale(int scale) {
        RandomProvider scaled = copy();
        scaled.scale = scale;
        return scaled;
    }

    /**
     * A {@code RandomProvider} with the same fields as {@code this} except for a new secondary scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code secondaryScale} mat be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param secondaryScale the new secondary scale
     * @return A copy of {@code this} with a new secondary scale
     */
    @Override
    public @NotNull RandomProvider withSecondaryScale(int secondaryScale) {
        RandomProvider scaled = copy();
        scaled.secondaryScale = secondaryScale;
        return scaled;
    }

    /**
     * A {@code RandomProvider} with the same fields as {@code this} except for a new tertiary scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code tertiaryScale} mat be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param tertiaryScale the new tertiary scale
     * @return A copy of {@code this} with a new tertiary scale
     */
    @Override
    public @NotNull RandomProvider withTertiaryScale(int tertiaryScale) {
        RandomProvider scaled = copy();
        scaled.tertiaryScale = tertiaryScale;
        return scaled;
    }

    /**
     * Put the {@code prng} back in its original state. Creating a {@code RandomProvider}, generating some values,
     * resetting, and generating the same types of values again will result in the same values. Any
     * {@code RandomProvider}s created from {@code this} using {@link RandomProvider#copy},
     * {@link RandomProvider#withScale(int)}, {@link RandomProvider#withSecondaryScale(int)}, or
     * {@link RandomProvider#withTertiaryScale(int)} (but not {@link RandomProvider#deepCopy()}) is also reset.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     */
    @Override
    public void reset() {
        prng.setSeed(seed);
    }

    /**
     * Returns an id which has a good chance of being different in two instances with unequal {@code prng}s. It's used
     * in {@link RandomProvider#toString()} to distinguish between different {@code RandomProvider} instances.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @return the id
     */
    public long getId() {
        return prng.getId();
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> integers() {
        return () -> new NoRemoveIterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Integer next() {
                return prng.nextInt();
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code Long}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> longs() {
        return map(c -> (long) c.get(0) << 32 | c.get(1) & 0xffffffffL, chunkInfinite(2, integers()));
    }

    /**
     * An {@code Iterator} that generates both {@code Boolean}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Boolean> booleans() {
        return map(i -> (i & 1) != 0, integers());
    }

    /**
     * An {@code Iterable} that generates {@code Integer}s taken from a uniform distribution between 0 and
     * 2<sup>{@code bits}</sup>–1, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code bits} must be greater than 0 and less than 32.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param bits the maximum number of bits of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code Integer}s with up to {@code bits} bits
     */
    private @NotNull Iterable<Integer> integersPow2(int bits) {
        int mask = (1 << bits) - 1;
        return map(i -> i & mask, integers());
    }

    /**
     * An {@code Iterable} that generates {@code Long}s taken from a uniform distribution between 0 and
     * 2<sup>{@code bits}</sup>–1, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code bits} must be greater than 0 and less than 64.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code Long}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param bits the maximum number of bits of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code Long}s with up to {@code bits} bits
     */
    private @NotNull Iterable<Long> longsPow2(int bits) {
        long mask = (1L << bits) - 1;
        return map(l -> l & mask, longs());
    }

    /**
     * Returns a randomly-generated {@code BigInteger} taken from a uniform distribution between 0 and
     * 2<sup>{@code bits}</sup>–1, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code bits} must be positive.</li>
     *  <li>The result cannot be negative.</li>
     * </ul>
     *
     * @param bits the maximum bitlength of the generated {@code BigInteger}
     * @return a {@code BigInteger} with up to {@code bits} bits
     */
    private @NotNull BigInteger nextBigIntegerPow2(int bits) {
        byte[] bytes = new byte[bits / 8 + 1];
        int x = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (i % 4 == 0) {
                x = prng.nextInt();
            }
            bytes[i] = (byte) x;
            x >>= 8;
        }
        bytes[0] &= (1 << (bits % 8)) - 1;
        return new BigInteger(bytes);
    }

    /**
     * An {@code Iterable} that generates {@code BigInteger}s taken from a uniform distribution between 0 and
     * 2<sup>{@code bits}</sup>–1, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code bits} must be positive.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param bits the maximum number of bits of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code BigInteger}s with up to {@code bits} bits
     */
    private @NotNull Iterable<BigInteger> bigIntegersPow2(int bits) {
        return () -> new NoRemoveIterator<BigInteger>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                return nextBigIntegerPow2(bits);
            }
        };
    }

    /**
     * An {@code Iterable} that generates {@code Integer}s taken from a uniform distribution between 0 and {@code n}–1,
     * inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code n} must be positive.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param n one more than the maximum value of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code Integer}s less than {@code n}
     */
    private @NotNull Iterable<Integer> integersBounded(int n) {
        if (n == 1) {
            return repeat(0);
        } else {
            if (IntegerUtils.isPowerOfTwo(n)) {
                return integersPow2(IntegerUtils.ceilingLog2(n));
            } else {
                return filterInfinite(i -> i < n, integersPow2(IntegerUtils.ceilingLog2(n)));
            }
        }
    }

    /**
     * An {@code Iterable} that generates {@code Long}s taken from a uniform distribution between 0 and {@code n}–1,
     * inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code n} must be positive.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code Long}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param n one more than the maximum value of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code Long}s less than {@code n}
     */
    private @NotNull Iterable<Long> longsBounded(long n) {
        if (n == 1) {
            return repeat(0L);
        } else {
            if (IntegerUtils.isPowerOfTwo(n)) {
                return longsPow2(IntegerUtils.ceilingLog2(n));
            } else {
                return filterInfinite(i -> i < n, longsPow2(IntegerUtils.ceilingLog2(n)));
            }
        }
    }

    /**
     * An {@code Iterable} that generates {@code BigInteger}s taken from a uniform distribution between 0 and
     * {@code n}–1, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code n} must be positive.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing non-negative {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param n one more than the maximum value of any element in the output {@code Iterable}
     * @return uniformly-distributed positive {@code BigInteger}s less than {@code n}
     */
    private @NotNull Iterable<BigInteger> bigIntegersBounded(@NotNull BigInteger n) {
        if (n.equals(BigInteger.ONE)) {
            return repeat(BigInteger.ZERO);
        } else {
            if (IntegerUtils.isPowerOfTwo(n)) {
                return bigIntegersPow2(IntegerUtils.ceilingLog2(n));
            } else {
                return filterInfinite(i -> lt(i, n), bigIntegersPow2(IntegerUtils.ceilingLog2(n)));
            }
        }
    }

    /**
     * An {@code Iterable} that uniformly generates elements from a (possibly null-containing) list.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} cannot be empty.</li>
     *  <li>The result is non-removable and infinite.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs the source list
     * @param <T> the type of {@code xs}'s elements
     * @return uniformly-distributed elements of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        if (xs.isEmpty()) {
            throw new IllegalArgumentException("xs cannot be empty");
        }
        return map(xs::get, integersBounded(xs.size()));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Character}s from a {@code String}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code s} cannot be empty.</li>
     *  <li>The result is an empty or infinite non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param s the source {@code String}
     * @return uniformly-distributed {@code Character}s from {@code s}
     */
    @Override
    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("s cannot be empty");
        }
        return map(s::charAt, integersBounded(s.length()));
    }

    /**
     * An {@code Iterator} that generates all {@code Ordering}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Ordering> orderings() {
        return uniformSample(ORDERINGS);
    }

    /**
     * An {@code Iterable} that generates all {@code RoundingMode}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return uniformSample(ROUNDING_MODES);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Byte}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return filterInfinite(b -> b > 0, naturalBytes());
    }

    /**
     * An {@code Iterable} that generates all positive {@code Short}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return filterInfinite(s -> s > 0, naturalShorts());
    }

    /**
     * An {@code Iterable} that generates all positive {@code Integer}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> positiveIntegers() {
        return filterInfinite(i -> i > 0, naturalIntegers());
    }

    /**
     * An {@code Iterable} that generates all positive {@code Long}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> positiveLongs() {
        return filterInfinite(l -> l > 0, naturalLongs());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Byte}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> negativeBytes() {
        return map(b -> (byte) ~b, naturalBytes());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Short}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> negativeShorts() {
        return map(s -> (short) ~s, naturalShorts());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Integer}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> negativeIntegers() {
        return map(i -> ~i, naturalIntegers());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Long}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> negativeLongs() {
        return map(l -> ~l, naturalLongs());
    }

    /**
     * An {@code Iterable} that generates all natural {@code Byte}s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> naturalBytes() {
        return map(i -> (byte) i.intValue(), integersPow2(7));
    }

    /**
     * An {@code Iterable} that generates all natural {@code Short}s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> naturalShorts() {
        return map(i -> (short) i.intValue(), integersPow2(15));
    }

    /**
     * An {@code Iterable} that generates all natural {@code Integer}s (including 0) from a uniform distribution.
     * Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> naturalIntegers() {
        return integersPow2(31);
    }

    /**
     * An {@code Iterable} that generates all natural {@code Long}s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> naturalLongs() {
        return longsPow2(63);
    }

    /**
     * An {@code Iterable} that generates all {@code Byte}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> bytes() {
        return map(i -> (byte) i.intValue(), integers());
    }

    /**
     * An {@code Iterable} that generates all {@code Short}s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> shorts() {
        return map(i -> (short) i.intValue(), integers());
    }

    /**
     * An {@code Iterable} that generates all ASCII {@code Character}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Character> asciiCharacters() {
        return map(i -> (char) i.intValue(), integersPow2(7));
    }

    /**
     * An {@code Iterable} that generates all {@code Character}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Character> characters() {
        return map(i -> (char) i.intValue(), integers());
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Byte}s greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return uniformly-distributed {@code Byte}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Byte> rangeUp(byte a) {
        return map(i -> (byte) (i + a), integersBounded((1 << 7) - a));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Short}s greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return uniformly-distributed {@code Short}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Short> rangeUp(short a) {
        return map(i -> (short) (i + a), integersBounded((1 << 15) - a));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Integer}s greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return uniformly-distributed {@code Integer}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Integer> rangeUp(int a) {
        return map(l -> (int) (l + a), longsBounded((1L << 31) - a));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Long}s greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return uniformly-distributed {@code Long}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Long> rangeUp(long a) {
        return map(
                i -> i.add(BigInteger.valueOf(a)).longValueExact(),
                bigIntegersBounded(BigInteger.ONE.shiftLeft(63).subtract(BigInteger.valueOf(a)))
        );
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Characters}s greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return uniformly-distributed {@code Character}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Character> rangeUp(char a) {
        return map(i -> (char) (i + a), integersBounded((1 << 16) - a));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Byte}s less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Byte}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Byte> rangeDown(byte a) {
        int offset = 1 << 7;
        return map(i -> (byte) (i - offset), integersBounded(a + offset + 1));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Short}s less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Short}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Short> rangeDown(short a) {
        int offset = 1 << 15;
        return map(i -> (short) (i - offset), integersBounded(a + offset + 1));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Integer}s less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Integer}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Integer> rangeDown(int a) {
        long offset = 1L << 31;
        return map(l -> (int) (l - offset), longsBounded(a + offset + 1));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Long}s less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Long}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Long> rangeDown(long a) {
        BigInteger offset = BigInteger.ONE.shiftLeft(63);
        return map(
                i -> i.subtract(offset).longValueExact(),
                bigIntegersBounded(BigInteger.valueOf(a).add(BigInteger.ONE).add(offset))
        );
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Character}s less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Character}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Character> rangeDown(char a) {
        return map(i -> (char) i.intValue(), integersBounded(a + 1));
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Byte}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>{@code b} may be any {@code byte}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result an infinite, non-removable {@code Iterable} containing {@code Byte}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Byte}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Byte> range(byte a, byte b) {
        if (a > b) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(a);
        } else {
            return map(i -> (byte) (i + a), integersBounded((int) b - a + 1));
        }
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Short}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>{@code b} may be any {@code short}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Short}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Short}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Short> range(short a, short b) {
        if (a > b) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(a);
        } else {
            return map(i -> (short) (i + a), integersBounded((int) b - a + 1));
        }
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Integer}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>{@code b} may be any {@code int}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Integer}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Integer> range(int a, int b) {
        if (a > b) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(a);
        } else {
            return map(i -> (int) (i + a), longsBounded((long) b - a + 1));
        }
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Long}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>{@code b} may be any {@code long}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Long}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Long}s between {@code a} and {@code b}, inclusive
     */
    public @NotNull Iterable<Long> range(long a, long b) {
        if (a > b) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(a);
        } else {
            return map(
                    i -> i.add(BigInteger.valueOf(a)).longValueExact(),
                    bigIntegersBounded(BigInteger.valueOf(b).subtract(BigInteger.valueOf(a)).add(BigInteger.ONE))
            );
        }
    }

    /**
     * An {@code Iterable} that uniformly generates {@code BigInteger}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code BigInteger}.</li>
     *  <li>{@code b} may be any {@code BigInteger}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code BigInteger}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a.equals(b)) {
            return repeat(a);
        } else {
            return map(i -> i.add(a), bigIntegersBounded(b.subtract(a).add(BigInteger.ONE)));
        }
    }

    /**
     * An {@code Iterable} that uniformly generates {@code Character}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>{@code b} may be any {@code char}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Character}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return uniformly-distributed {@code Character}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Character> range(char a, char b) {
        if (a > b) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(a);
        } else {
            return map(i -> (char) (i + a), integersBounded(b - a + 1));
        }
    }

    /**
     * An {@code Iterable} that generates all positive {@code Integer}s chosen from a geometric distribution with mean
     * {@code scale}. The distribution is truncated at {@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> positiveIntegersGeometric() {
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        Iterable<Integer> integersBounded = integersBounded(scale);
        return () -> new NoRemoveIterator<Integer>() {
            private final @NotNull Iterator<Integer> is = integersBounded.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Integer next() {
                int i;
                int j = 0;
                do {
                    i = is.next();
                    j++;
                } while (i != 0);
                return j;
            }
        };
    }

    /**
     * An {@code Iterable} that generates all negative {@code Integer}s chosen from a geometric distribution with mean
     * –{@code scale}. The distribution is truncated at –{@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> negativeIntegersGeometric() {
        return map(i -> -i, positiveIntegersGeometric());
    }

    /**
     * An {@code Iterable} that generates all natural {@code Integer}s (including 0) chosen from a geometric
     * distribution with mean {@code scale}. The distribution is truncated at {@code Integer.MAX_VALUE}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing natural {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> naturalIntegersGeometric() {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        return map(i -> i - 1, withScale(scale + 1).positiveIntegersGeometric());
    }

    /**
     * An {@code Iterable} that generates all natural {@code Integer}s (including 0) chosen from a geometric
     * distribution with mean {@code numerator}/{@code denominator}. The distribution is truncated at
     * {@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code numerator} must be positive.</li>
     *  <li>{@code denominator} must be positive.</li>
     *  <li>The sum of {@code numerator} and {@code denominator} must be less than 2<sup>31</sup>.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing natural {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    private @NotNull Iterable<Integer> naturalIntegersGeometric(int numerator, int denominator) {
        if (numerator < 1) {
            throw new IllegalArgumentException("numerator must be positive. numerator: " + numerator);
        }
        if (denominator < 1) {
            throw new IllegalArgumentException("denominator must be positive. denominator: " + denominator);
        }
        long sum = (long) numerator + denominator;
        if (sum > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The sum of numerator and denominator must be less than 2^31." +
                    " numerator is " + numerator + " and denominator is " + denominator + ".");
        }
        Iterable<Integer> integersBounded = integersBounded((int) sum);
        return () -> new NoRemoveIterator<Integer>() {
            private final @NotNull Iterator<Integer> is = integersBounded.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Integer next() {
                int i;
                int j = 0;
                do {
                    i = is.next();
                    j++;
                } while (i >= denominator);
                return j - 1;
            }
        };
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Integer}s whose absolute value is chosen from a geometric
     * distribution with mean {@code scale}, and whose sign is chosen uniformly. The distribution is truncated at
     * ±{@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing natural {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> nonzeroIntegersGeometric() {
        return zipWith((i, b) -> b ? i : -i, positiveIntegersGeometric(), booleans());
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s whose absolute value is chosen from a geometric
     * distribution with mean {@code scale}, and whose sign is chosen uniformly. The distribution is truncated at
     * ±{@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> integersGeometric() {
        return zipWith((i, b) -> b ? i : -i, naturalIntegersGeometric(), booleans());
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s whose absolute value is chosen from a geometric
     * distribution with mean {@code numerator}/{@code denominator}, and whose sign is chosen uniformly. The
     * distribution is truncated at ±{@code Integer.MAX_VALUE}. Does not support removal.
     *
     * <ul>
     *  <li>{@code numerator} must be positive.</li>
     *  <li>{@code denominator} must be positive.</li>
     *  <li>The sum of {@code numerator} and {@code denominator} must be less than 2<sup>31</sup>.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    private @NotNull Iterable<Integer> integersGeometric(int numerator, int denominator) {
        return zipWith((i, b) -> b ? i : -i, naturalIntegersGeometric(numerator, denominator), booleans());
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s greater than or equal to {@code a}, chosen from a
     * geometric distribution with mean {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale greater than {@code a} and less than {@code Integer.MAX_VALUE}+a.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> rangeUpGeometric(int a) {
        if (scale <= a) {
            throw new IllegalStateException("this must have a scale greater than a, which is " + a +
                    ". Invalid scale: " + scale);
        }
        if (a < 1 && scale >= Integer.MAX_VALUE + a) {
            throw new IllegalStateException("this must have a scale less than Integer.MAX_VALUE + a, which is " +
                    (Integer.MAX_VALUE + a) + ". Invalid scale: " + scale);
        }
        return filterInfinite(j -> j >= a, map(i -> i + a - 1, withScale(scale - a + 1).positiveIntegersGeometric()));
    }

    /**
     * An {@code Iterable} that generates all {@code Integer}s less than or equal to {@code a}, chosen from a geometric
     * distribution with mean {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale less than {@code a} and greater than
     *  {@code a}–{@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Integer}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> rangeDownGeometric(int a) {
        if (scale >= a) {
            throw new IllegalStateException("this must have a scale less than a, which is " + a + ". Invalid scale: " +
                    scale);
        }
        if (a >= 0 && scale <= a - Integer.MAX_VALUE) {
            throw new IllegalStateException("this must have a scale greater than a - Integer.MAX_VALUE, which is " +
                    (a - Integer.MAX_VALUE) + ". Invalid scale: " + scale);
        }
        return filterInfinite(j -> j <= a, map(i -> a - i + 1, withScale(a - scale + 1).positiveIntegersGeometric()));
    }

    /**
     * An {@code Iterable} that generates all positive {@code BigInteger}s. The bit size is chosen from a geometric
     * distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s with that bit size. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return map(i -> nextBigIntegerPow2(i).setBit(i - 1), positiveIntegersGeometric());
    }

    /**
     * An {@code Iterable} that generates all negative {@code BigInteger}s. The bit size is chosen from a geometric
     * distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s with that bit size. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return map(BigInteger::negate, positiveBigIntegers());
    }

    /**
     * An {@code Iterable} that generates all natural {@code BigInteger}s (including 0). The bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s with that bit size. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        Iterable<Integer> naturalIntegers = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<BigInteger>() {
            private final @NotNull Iterator<Integer> is = naturalIntegers.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigInteger next() {
                int size = is.next();
                if (size == 0) return BigInteger.ZERO;
                return nextBigIntegerPow2(size).setBit(size - 1);
            }
        };
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code BigInteger}s. The bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s with that bit size; the sign is also chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> nonzeroBigIntegers() {
        return zipWith((i, b) -> b ? i : i.negate(), positiveBigIntegers(), booleans());
    }

    /**
     * An {@code Iterable} that generates all natural {@code BigInteger}s. The bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s with that bit size; the sign is also chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return zipWith((i, b) -> b ? i : i.negate(), naturalBigIntegers(), booleans());
    }

    /**
     * An {@code Iterable} that generates all {@code BigInteger}s greater than or equal to {@code a}. The bit size is
     * chosen from a geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen
     * uniformly from all {@code BigInteger}s greater than or equal to {@code a} with that bit size. Does not support
     * removal.
     *
     * <ul>
     *  <li>Let {@code minBitLength} be 0 if {@code a} is negative, and ⌊log<sub>2</sub>({@code a})⌋ otherwise.
     *  {@code this} must have a scale greater than {@code minBitLength}. If {@code minBitLength} is 0, {@code scale}
     *  cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        int minBitLength = a.signum() == -1 ? 0 : a.bitLength();
        if (scale <= minBitLength) {
            throw new IllegalStateException("this must have a scale greater than minBitLength, which is " +
                    minBitLength + ". Invalid scale: " + scale);
        }
        if (minBitLength == 0 && scale == Integer.MAX_VALUE) {
            throw new IllegalStateException("If minBitLength is 0, scale cannot be Integer.MAX_VALUE.");
        }
        int absBitLength = a.abs().bitLength();
        Iterable<Integer> rangeUpGeometric = rangeUpGeometric(minBitLength);
        Iterable<BigInteger> bigIntegersBounded = bigIntegersBounded(
                a.signum() == -1 ? BigInteger.ONE.subtract(a) : BigInteger.ONE.shiftLeft(absBitLength).subtract(a)
        );
        return () -> new NoRemoveIterator<BigInteger>() {
            private final @NotNull Iterator<Integer> bitLengths = rangeUpGeometric.iterator();
            private final @NotNull Iterator<BigInteger> offsets = bigIntegersBounded.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigInteger next() {
                int bitLength = bitLengths.next();
                BigInteger i;
                if (bitLength != absBitLength) {
                    if (bitLength == 0) {
                        i = BigInteger.ZERO;
                    } else {
                        i = nextBigIntegerPow2(bitLength);
                        boolean mostSignificantBit = i.testBit(bitLength - 1);
                        if (!mostSignificantBit) {
                            i = i.setBit(bitLength - 1);
                            if (bitLength < absBitLength && a.signum() == -1) {
                                i = i.negate();
                            }
                        }
                    }
                } else {
                    if (a.signum() != -1) {
                        i = offsets.next().add(a);
                    } else {
                        BigInteger b = BigInteger.ONE.shiftLeft(absBitLength - 1);
                        BigInteger x = offsets.next();
                        i = lt(x, b) ? x.add(b) : b.negate().subtract(x.subtract(b));
                    }
                }
                return i;
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code BigInteger}s less than or equal to {@code a}. The bit size is
     * chosen from a geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen
     * uniformly from all {@code BigInteger}s greater than or equal to {@code a} with that bit size. Does not support
     * removal.
     *
     * <ul>
     *  <li>Let {@code minBitLength} be 0 if {@code a} is positive, and ⌊log<sub>2</sub>(–{@code a})⌋ otherwise.
     *  {@code this} must have a scale greater than {@code minBitLength}. If {@code minBitLength} is 0, {@code scale}
     *  cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigInteger}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        return map(BigInteger::negate, rangeUp(a.negate()));
    }

    /**
     * An {@code Iterable} that generates all positive {@code BinaryFraction}s. The mantissa bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd positive
     * {@code BigInteger}s with that bit size. The absolute value of the exponent is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> positiveBinaryFractions() {
        return zipWith(
                (m, s) -> BinaryFraction.of(m.setBit(0), s),
                positiveBigIntegers(),
                withScale(secondaryScale).integersGeometric()
        );
    }

    /**
     * An {@code Iterable} that generates all negative {@code BinaryFraction}s. The mantissa bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd negative
     * {@code BigInteger}s with that bit size. The absolute value of the exponent is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a secondary scale of at least 1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> negativeBinaryFractions() {
        return map(BinaryFraction::negate, positiveBinaryFractions());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code BinaryFraction}s. The mantissa bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd
     * {@code BigInteger}s with that bit size. The absolute value of the exponent is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly. Finally, the sign of the
     * {@code BinaryFraction} itself is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> nonzeroBinaryFractions() {
        return zipWith((bf, b) -> b ? bf : bf.negate(), positiveBinaryFractions(), booleans());
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s. The mantissa bit size is chosen from a geometric
     * distribution with mean {@code scale}. If the bit size is zero, the {@code BinaryFraction} is zero; otherwise,
     * the mantissa is chosen uniformly from all odd {@code BigInteger}s with that bit size, thhe absolute value of the
     * exponent is chosen from a geometric distribution with mean {@code secondaryScale}, the exponent's sign is chosen
     * uniformly, and, finally, the sign of the {@code BinaryFraction} itself is chosen uniformly. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> binaryFractions() {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        Iterable<BigInteger> bigIntegers = bigIntegers();
        Iterable<Integer> integersGeometric = integersGeometric(secondaryScale * (scale + 1), scale);
        return () -> new NoRemoveIterator<BinaryFraction>() {
            private final @NotNull Iterator<BigInteger> ms = bigIntegers.iterator();
            private final @NotNull Iterator<Integer> ss = integersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BinaryFraction next() {
                BigInteger mantissa = ms.next();
                if (mantissa.equals(BigInteger.ZERO)) {
                    return BinaryFraction.ZERO;
                } else {
                    int exponent = ss.next();
                    return BinaryFraction.of(mantissa.setBit(0), exponent);
                }
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s greater than or equal to {@code a}. A higher
     * {@code scale} corresponds to a higher mantissa bit size and a higher {@code secondaryScale} corresponds to a
     * higher exponent bit size, but the exact relationship is not simple to describe. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> rangeUp(@NotNull BinaryFraction a) {
        return map(
                bf -> bf.abs().add(BinaryFraction.of(a.getMantissa())).shiftLeft(a.getExponent()),
                binaryFractions()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code BinaryFraction}s less than or equal to {@code a}. A higher
     * {@code scale} corresponds to a higher mantissa bit size and a higher {@code secondaryScale} corresponds to a
     * higher exponent bit size, but the exact relationship is not simple to describe. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BinaryFraction> rangeDown(@NotNull BinaryFraction a) {
        return map(BinaryFraction::negate, rangeUp(a.negate()));
    }

    /**
     * <p>An {@code Iterable} that generates {@code BinaryFraction}s between {@code a} and {@code b}, inclusive. Does
     * not support removal.</p>
     *
     * <p>Every interval with {@code BinaryFraction} bounds may be broken up into equal blocks whose length is a power
     * of 2. Consider the subdivision with the largest possible block size. We can call points that lie on the
     * boundaries between blocks, along with the lower and upper bounds of the interval, <i>division-0</i> points.
     * Let points within the interval that are halfway between division-0 points be called <i>division-1</i> points;
     * points halfway between division-0 or division-1 points <i>division-2</i> points; and so on. The
     * {@code BinaryFraction}s returned by this method have divisions chosen from a geometric distribution with mean
     * {@code scale}. The distribution of {@code BinaryFraction}s is approximately uniform.</p>
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code BinaryFraction}.</li>
     *  <li>{@code b} may be any {@code BinaryFraction}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return approximately uniformly-distributed {@code BinaryFraction}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BinaryFraction> range(@NotNull BinaryFraction a, @NotNull BinaryFraction b) {
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        }
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (scale == Integer.MAX_VALUE) {
            throw new IllegalStateException("scale cannot be Integer.MAX_VALUE.");
        }
        if (a.equals(b)) {
            return repeat(a);
        }
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        BinaryFraction difference = b.subtract(a);
        Iterable<BigInteger> range1 = range(BigInteger.ZERO, difference.getMantissa());
        Iterable<BigInteger> range2 = range(BigInteger.ZERO, difference.getMantissa().subtract(BigInteger.ONE));
        return () -> new NoRemoveIterator<BinaryFraction>() {
            private @NotNull final Iterator<Integer> is = naturalIntegersGeometric.iterator();
            private @NotNull final Iterator<BigInteger> is2 = range1.iterator();
            private @NotNull final Iterator<BigInteger> is3 = range2.iterator();
            private @NotNull final Map<Integer, Iterator<BigInteger>> isMap = new HashMap<>();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BinaryFraction next() {
                int i = is.next();
                if (i == 0) {
                    return BinaryFraction.of(is2.next(), difference.getExponent()).add(a);
                } else {
                    Iterator<BigInteger> is4 = isMap.computeIfAbsent(
                            i,
                            k -> range(BigInteger.ZERO, BigInteger.ONE.shiftLeft(i - 1).subtract(BigInteger.ONE))
                                    .iterator()
                    );
                    BinaryFraction fraction = BinaryFraction.of(is4.next().shiftLeft(1).add(BigInteger.ONE), -i);
                    return fraction.add(BinaryFraction.of(is3.next())).shiftLeft(difference.getExponent()).add(a);
                }
            }
        };
    }

    /**
     * An {@code Iterable} that generates all positive {@code Float}s from a uniform distribution among {@code Float}s,
     * including {@code Infinity} but not positive zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> positiveFloats() {
        return filterInfinite(f -> f > 0, floats());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Float}s from a uniform distribution among {@code Float}s,
     * including {@code -Infinity} but not negative zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> negativeFloats() {
        return filterInfinite(f -> f < 0, floats());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Float}s from a uniform distribution among {@code Float}s,
     * including {@code Infinity} and {@code -Infinity}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> nonzeroFloats() {
        return filterInfinite(f -> f != 0, floats());
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s from a uniform distribution among {@code Float}s,
     * {@code NaN}, positive and negative zeros, {@code Infinity}, and {@code -Infinity}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> floats() {
        return map(
                p -> p.b,
                filterInfinite(
                        p -> !Float.isNaN(p.b) || p.a == 0x7fc00000,
                        map(i -> new Pair<>(i, Float.intBitsToFloat(i)), integers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all positive {@code Double}s from a uniform distribution among
     * {@code Double}s, including {@code Infinity} but not positive zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> positiveDoubles() {
        return filterInfinite(d -> d > 0, doubles());
    }

    /**
     * An {@code Iterable} that generates all negative {@code Double}s from a uniform distribution among
     * {@code Double}s, including {@code -Infinity} but not negative zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> negativeDoubles() {
        return filterInfinite(d -> d < 0, doubles());
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Double}s from a uniform distribution among
     * {@code Double}s, including {@code Infinity} and {@code -Infinity}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> nonzeroDoubles() {
        return filterInfinite(d -> d != 0, doubles());
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s from a uniform distribution among {@code Double}s,
     * {@code NaN}, positive and negative zeros, {@code Infinity}, and {@code -Infinity}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> doubles() {
        return map(
                p -> p.b,
                filterInfinite(
                        p -> !Double.isNaN(p.b) || p.a == 0x7ff8000000000000L,
                        map(l -> new Pair<>(l, Double.longBitsToDouble(l)), longs())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all positive finite {@code Float}s, as if reals were sampled from a uniform
     * distribution between {@code Float.MIN_VALUE} and {@code Float.MAX_VALUE} and then rounded to the nearest
     * {@code float}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> positiveFloatsUniform() {
        return zipWith(
                (i, b) -> {
                    Pair<Float, Float> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_FLOAT_EXPONENT)
                            .floatRange();
                    return b ? range.a : range.b;
                },
                range(BigInteger.ONE, FloatingPointUtils.SCALED_UP_MAX_FLOAT),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all negative finite {@code Float}s, as if reals were sampled from a uniform
     * distribution between {@code -Float.MAX_VALUE} and {@code -Float.MIN_VALUE} and then rounded to the nearest
     * {@code Float}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> negativeFloatsUniform() {
        return map(f -> -f, positiveFloatsUniform());
    }

    /**
     * An {@code Iterable} that generates all nonzero finite {@code Float}s, as if reals were sampled from a uniform
     * distribution between {@code -Float.MAX_VALUE} and {@code -Float.MIN_VALUE}, rounded to the nearest
     * {@code Float}s, and resampled if zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> nonzeroFloatsUniform() {
        return zipWith((f, b) -> b ? f : -f, positiveFloatsUniform(), booleans());
    }

    /**
     * An {@code Iterable} that generates all finite {@code Float}s except for negative zero, as if reals were sampled
     * from a uniform distribution between {@code -Float.MAX_VALUE} and {@code Float.MAX_VALUE} and rounded to the
     * nearest {@code Float}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> floatsUniform() {
        return zipWith(
                (i, b) -> {
                    Pair<Float, Float> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_FLOAT_EXPONENT)
                            .floatRange();
                    return b ? range.a : range.b;
                },
                range(FloatingPointUtils.SCALED_UP_MAX_FLOAT.negate(), FloatingPointUtils.SCALED_UP_MAX_FLOAT),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all positive finite {@code Double}s, as if reals were sampled from a uniform
     * distribution between {@code Double.MIN_VALUE} and {@code Double.MAX_VALUE} and then rounded to the nearest
     * {@code Double}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> positiveDoublesUniform() {
        return zipWith(
                (i, b) -> {
                    Pair<Double, Double> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_DOUBLE_EXPONENT)
                            .doubleRange();
                    return b ? range.a : range.b;
                },
                range(BigInteger.ONE, FloatingPointUtils.SCALED_UP_MAX_DOUBLE),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all negative finite {@code Double}s, as if reals were sampled from a uniform
     * distribution between {@code -Double.MAX_VALUE} and {@code -Double.MIN_VALUE} and then rounded to the nearest
     * {@code Double}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> negativeDoublesUniform() {
        return map(d -> -d, positiveDoublesUniform());
    }

    /**
     * An {@code Iterable} that generates all nonzero finite {@code Double}s, as if reals were sampled from a uniform
     * distribution between {@code -Double.MAX_VALUE} and {@code -Double.MIN_VALUE}, rounded to the nearest
     * {@code Double}s, and resampled if zero. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> nonzeroDoublesUniform() {
        return zipWith((d, b) -> b ? d : -d, positiveDoublesUniform(), booleans());
    }

    /**
     * An {@code Iterable} that generates all finite {@code Double}s except for negative zero, as if reals were sampled
     * from a uniform distribution between {@code -Double.MAX_VALUE} and {@code Double.MAX_VALUE} and rounded to the
     * nearest {@code Double}s. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> doublesUniform() {
        return zipWith(
                (i, b) -> {
                    Pair<Double, Double> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_DOUBLE_EXPONENT)
                            .doubleRange();
                    return b ? range.a : range.b;
                },
                range(FloatingPointUtils.SCALED_UP_MAX_DOUBLE.negate(), FloatingPointUtils.SCALED_UP_MAX_DOUBLE),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s greater than or equal to {@code a} from a uniform
     * distribution among {@code Float}s. If a≤0, both positive and negative zeros may be generated. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Float}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Float}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Float> rangeUp(float a) {
        int oa = FloatingPointUtils.toOrderedRepresentation(a);
        if (oa <= 0) {
            return map(
                    n -> n == oa - 1 ? -0.0f : FloatingPointUtils.floatFromOrderedRepresentation(n),
                    range(oa - 1, FloatingPointUtils.POSITIVE_FINITE_FLOAT_COUNT + 1)
            );
        } else {
            return map(
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    range(oa, FloatingPointUtils.POSITIVE_FINITE_FLOAT_COUNT + 1)
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s less than or equal to {@code a} from a uniform
     * distribution among {@code Float}s. If a≥0, both positive and negative zeros may be generated. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Float}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Float}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Float> rangeDown(float a) {
        return map(f -> -f, rangeUp(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s between {@code a} and {@code b}, inclusive, from a uniform
     * distribution among {@code Float}s. If a≤0≤b, either positive or negative zero may be returned. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>{@code b} cannot be {@code NaN}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Float}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Float}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Float> range(float a, float b) {
        if ((!FloatingPointUtils.isPositiveZero(a) || !FloatingPointUtils.isNegativeZero(b)) && gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return a == 0.0f ? uniformSample(Arrays.asList(0.0f, -0.0f)) : repeat(a);
        }
        int oa = FloatingPointUtils.toOrderedRepresentation(a);
        int ob = FloatingPointUtils.toOrderedRepresentation(b);
        if (oa <= 0 && 0 <= ob) {
            return map(
                    n -> n == oa - 1 ? -0.0f : FloatingPointUtils.floatFromOrderedRepresentation(n),
                    range(oa - 1, ob)
            );
        } else {
            return map(FloatingPointUtils::floatFromOrderedRepresentation, range(oa, ob));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s greater than or equal to {@code a} from a uniform
     * distribution among {@code Double}s. If a≤0, both positive and negative zeros may be generated. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Double}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @return {@code Double}s greater than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Double> rangeUp(double a) {
        long oa = FloatingPointUtils.toOrderedRepresentation(a);
        if (oa <= 0L) {
            return map(
                    n -> n == oa - 1 ? -0.0 : FloatingPointUtils.doubleFromOrderedRepresentation(n),
                    range(oa - 1, FloatingPointUtils.POSITIVE_FINITE_DOUBLE_COUNT + 1)
            );
        } else {
            return map(
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    range(oa, FloatingPointUtils.POSITIVE_FINITE_DOUBLE_COUNT + 1)
            );
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s less than or equal to {@code a} from a uniform
     * distribution among {@code Double}s. If a≥0, both positive and negative zeros may be generated. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Double}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive upper bound of the generated elements
     * @return {@code Double}s less than or equal to {@code a}
     */
    @Override
    public @NotNull Iterable<Double> rangeDown(double a) {
        return map(f -> -f, rangeUp(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s between {@code a} and {@code b}, inclusive, from a
     * uniform distribution among {@code Double}s. If a≤0≤b, either positive or negative zero may be returned. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} cannot be {@code NaN}.</li>
     *  <li>{@code b} cannot be {@code NaN}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Double}s that are not
     *  {@code NaN}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Double}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Double> range(double a, double b) {
        if ((!FloatingPointUtils.isPositiveZero(a) || !FloatingPointUtils.isNegativeZero(b)) && gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return a == 0.0 ? uniformSample(Arrays.asList(0.0, -0.0)) : repeat(a);
        }
        long oa = FloatingPointUtils.toOrderedRepresentation(a);
        long ob = FloatingPointUtils.toOrderedRepresentation(b);
        if (oa <= 0L && 0L <= ob) {
            return map(
                    n -> n == oa - 1 ? -0.0f : FloatingPointUtils.doubleFromOrderedRepresentation(n),
                    range(oa - 1, ob)
            );
        } else {
            return map(FloatingPointUtils::doubleFromOrderedRepresentation, range(oa, ob));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s greater than or equal to {@code a}, as if reals were
     * sampled from a uniform distribution between {@code a} and {@code Float.MAX_VALUE} and then rounded to the
     * nearest {@code Float}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Float}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Float> rangeUpUniform(float a) {
        if (!Float.isFinite(a)) {
            throw new ArithmeticException("a must be finite. Invalid a: " + a);
        }
        return zipWith(
                (i, b) -> {
                    Pair<Float, Float> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_FLOAT_EXPONENT)
                            .floatRange();
                    return b ? range.a : range.b;
                },
                range(FloatingPointUtils.scaleUp(a).get(), FloatingPointUtils.SCALED_UP_MAX_FLOAT),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s less than or equal to {@code a}, as if reals were sampled
     * from a uniform distribution between {@code -Float.MAX_VALUE} and {@code a} and then rounded to the nearest
     * {@code Float}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Float}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Float> rangeDownUniform(float a) {
        return map(f -> -f, rangeUpUniform(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Float}s between {@code a} and {@code b}, inclusive, as if reals
     * were sampled from a uniform distribution between {@code a} and {@code b} and then rounded to the nearest
     * {@code Float}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>{@code b} must be finite.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Float}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Float}s between {@code a} and {@code b}, inclusive
     */
    public @NotNull Iterable<Float> rangeUniform(float a, float b) {
        if (!Float.isFinite(a)) {
            throw new ArithmeticException("a must be finite.");
        }
        if (!Float.isFinite(b)) {
            throw new ArithmeticException("b must be finite.");
        }
        if ((!FloatingPointUtils.isPositiveZero(a) || !FloatingPointUtils.isNegativeZero(b)) && gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(FloatingPointUtils.absNegativeZeros(a));
        }
        return zipWith(
                (i, s) -> {
                    Pair<Float, Float> range = BinaryFraction.of(
                            i,
                            FloatingPointUtils.MIN_SUBNORMAL_FLOAT_EXPONENT
                    ).floatRange();
                    return s ? range.a : range.b;
                },
                range(FloatingPointUtils.scaleUp(a).get(), FloatingPointUtils.scaleUp(b).get()),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s greater than or equal to {@code a}, as if reals were
     * sampled from a uniform distribution between {@code a} and {@code Double.MAX_VALUE} and then rounded to the
     * nearest {@code Double}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Double}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Double> rangeUpUniform(double a) {
        if (!Double.isFinite(a)) {
            throw new ArithmeticException("a must be finite. Invalid a: " + a);
        }
        return zipWith(
                (i, b) -> {
                    Pair<Double, Double> range = BinaryFraction.of(i, FloatingPointUtils.MIN_SUBNORMAL_DOUBLE_EXPONENT)
                            .doubleRange();
                    return b ? range.a : range.b;
                },
                range(FloatingPointUtils.scaleUp(a).get(), FloatingPointUtils.SCALED_UP_MAX_DOUBLE),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s less than or equal to {@code a}, as if reals were sampled
     * from a uniform distribution between {@code -Double.MAX_VALUE} and {@code a} and then rounded to the nearest
     * {@code Double}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Double}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Double> rangeDownUniform(double a) {
        return map(d -> -d, rangeUpUniform(-a));
    }

    /**
     * An {@code Iterable} that generates all {@code Double}s between {@code a} and {@code b}, inclusive, as if reals
     * were sampled from a uniform distribution between {@code a} and {@code b} and then rounded to the nearest
     * {@code Double}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} must be finite.</li>
     *  <li>{@code b} must be finite.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finite {@code Double}s that are not
     *  negative zero.</li>
     * </ul>
     *
     * Length is infinite if a≤b, 0 otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Double}s between {@code a} and {@code b}, inclusive
     */
    public @NotNull Iterable<Double> rangeUniform(double a, double b) {
        if (!Double.isFinite(a)) {
            throw new ArithmeticException("a must be finite.");
        }
        if (!Double.isFinite(b)) {
            throw new ArithmeticException("b must be finite.");
        }
        if ((!FloatingPointUtils.isPositiveZero(a) || !FloatingPointUtils.isNegativeZero(b)) && gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (a == b) {
            return repeat(FloatingPointUtils.absNegativeZeros(a));
        }
        return zipWith(
                (i, s) -> {
                    Pair<Double, Double> range = BinaryFraction.of(
                            i,
                            FloatingPointUtils.MIN_SUBNORMAL_DOUBLE_EXPONENT
                    ).doubleRange();
                    return s ? range.a : range.b;
                },
                range(FloatingPointUtils.scaleUp(a).get(), FloatingPointUtils.scaleUp(b).get()),
                booleans()
        );
    }

    /**
     * An {@code Iterable} that generates all positive {@code BigDecimal}s. The unscaled-value bit size is chosen from
     * a geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all positive
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return map(
                p -> new BigDecimal(p.a, p.b), pairs(positiveBigIntegers(),
                        withScale(secondaryScale).integersGeometric())
        );
    }

    /**
     * An {@code Iterable} that generates all negative {@code BigDecimal}s. The unscaled-value bit size is chosen from
     * a geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all negative
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return map(
                p -> new BigDecimal(p.a, p.b),
                pairs(negativeBigIntegers(), withScale(secondaryScale).integersGeometric())
        );
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code BigDecimal}s. The unscaled-value bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the unscaled value is chosen uniformly from all
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Finally, the sign of the {@code BigDecimal}
     * itself is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> nonzeroBigDecimals() {
        return map(
                p -> new BigDecimal(p.a, p.b),
                pairs(nonzeroBigIntegers(), withScale(secondaryScale).integersGeometric())
        );
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s. The unscaled-value bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the unscaled value is chosen uniformly from all
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Finally, the sign of the {@code BigDecimal}
     * itself is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return map(p -> new BigDecimal(p.a, p.b), pairs(bigIntegers(), withScale(secondaryScale).integersGeometric()));
    }

    /**
     * An {@code Iterable} that generates all positive canonical {@code BigDecimal}s. The unscaled-value bit size is
     * chosen from a geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all
     * positive {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive canonical
     *  {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> positiveCanonicalBigDecimals() {
        Iterable<BigInteger> positiveBigIntegers = positiveBigIntegers();
        Iterable<Integer> positiveIntegersGeometric = positiveIntegersGeometric();
        Iterable<Integer> naturalIntegersGeometric = withScale(secondaryScale).naturalIntegersGeometric();
        return () -> new NoRemoveIterator<BigDecimal>() {
            private final @NotNull Iterator<BigInteger> is1 = positiveBigIntegers.iterator();
            private final @NotNull Iterator<Integer> is2 = positiveIntegersGeometric.iterator();
            private final @NotNull Iterator<Integer> is3 = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigDecimal next() {
                int bdScale = is3.next();
                BigInteger unscaled;
                if (bdScale == 0) {
                    unscaled = is1.next();
                } else {
                    int unscaledBitLength = is2.next();
                    do {
                        unscaled = nextBigIntegerPow2(unscaledBitLength);
                        unscaled = unscaled.setBit(unscaledBitLength - 1);
                    } while (unscaled.mod(BigInteger.TEN).equals(BigInteger.ZERO));
                }
                return new BigDecimal(unscaled, bdScale);
            }
        };
    }

    /**
     * An {@code Iterable} that generates all negative canonical {@code BigDecimal}s. The unscaled-value bit size is
     * chosen from a geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all
     * negative {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative canonical
     *  {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> negativeCanonicalBigDecimals() {
        return map(BigDecimal::negate, positiveCanonicalBigDecimals());
    }

    /**
     * An {@code Iterable} that generates all nonzero canonical {@code BigDecimal}s. The unscaled-value bit size is
     * chosen from a geometric distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero canonical
     *  {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> nonzeroCanonicalBigDecimals() {
        return zipWith((bd, b) -> b ? bd : bd.negate(), positiveCanonicalBigDecimals(), booleans());
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s. The unscaled-value bit size is chosen from
     * a geometric distribution with mean {@code scale}, and then the unscaled value is chosen uniformly from all
     * {@code BigInteger}s with that bit size. The absolute value of the scale is chosen from a geometric distribution
     * with mean {@code secondaryScale}, and its sign is chosen uniformly. Finally, the sign of the {@code BigDecimal}
     * itself is chosen uniformly. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing canonical {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> canonicalBigDecimals() {
        Iterable<Boolean> booleans = booleans();
        Iterable<BigInteger> naturalBigIntegers = naturalBigIntegers();
        Iterable<Integer> positiveIntegersGeometric = positiveIntegersGeometric();
        Iterable<Integer> naturalIntegersGeometric = withScale(secondaryScale).naturalIntegersGeometric();
        return () -> new NoRemoveIterator<BigDecimal>() {
            private final @NotNull Iterator<Boolean> bs = booleans.iterator();
            private final @NotNull Iterator<BigInteger> is1 = naturalBigIntegers.iterator();
            private final @NotNull Iterator<Integer> is2 = positiveIntegersGeometric.iterator();
            private final @NotNull Iterator<Integer> is3 = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigDecimal next() {
                int bdScale = is3.next();
                BigInteger unscaled;
                if (bdScale == 0) {
                    unscaled = is1.next();
                } else {
                    int unscaledBitLength = is2.next();
                    do {
                        unscaled = nextBigIntegerPow2(unscaledBitLength);
                        unscaled = unscaled.setBit(unscaledBitLength - 1);
                    } while (unscaled.mod(BigInteger.TEN).equals(BigInteger.ZERO));
                }
                return new BigDecimal(bs.next() ? unscaled : unscaled.negate(), bdScale);
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s greater than or equal to zero and less than or equal
     * to a specified power of ten. A higher {@code scale} corresponds to a larger unscaled-value bit size, but the
     * exact relationship is not simple to describe. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale.</li>
     *  <li>{@code pow} may be any {@code int}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing canonical {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param pow an {@code int}
     * @return canonical {@code BigDecimal}s between 0 and 10<sup>{@code pow}</sup>, inclusive
     */
    private @NotNull Iterable<BigDecimal> zeroToPowerOfTenCanonicalBigDecimals(int pow) {
        Iterable<Boolean> booleans = booleans();
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<BigDecimal>() {
            private final @NotNull Iterator<Boolean> bs = booleans.iterator();
            private final @NotNull Iterator<Integer> is = naturalIntegersGeometric.iterator();
            private final @NotNull Map<Integer, Iterator<BigInteger>> isMap = new HashMap<>();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigDecimal next() {
                int normalizedScale = is.next();
                if (normalizedScale == 0) {
                    return bs.next() ? BigDecimal.ONE.movePointRight(pow) : BigDecimal.ZERO;
                } else {
                    Iterator<BigInteger> ds = isMap.computeIfAbsent(
                            normalizedScale,
                            k -> range(BigInteger.ONE, BigInteger.TEN.pow(normalizedScale).subtract(BigInteger.ONE))
                                    .iterator()
                    );
                    BigInteger digits;
                    do {
                        digits = ds.next();
                    } while (digits.mod(BigInteger.TEN).equals(BigInteger.ZERO));
                    return new BigDecimal(digits, normalizedScale - pow);
                }
            }
        };
    }

    /**
     * Given an infinte {@code Iterable xs} of unique, canonical {@code BigDecimal}s, returns an {@code Iterable} whose
     * elements are {x|{@code BigDecimalUtils.canonicalize(x)}∈{@code xs}}. A higher {@code scale} corresponds to a
     * higher precision, but the exact relationship is not simple to describe.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale.</li>
     *  <li>{@code bds} must be infinite and consist only of canonical elements.</li>
     *  <li>The result is infinite, and for every {@code BigDecimal} x that it contains, if
     *  {@code BigDecimalUtils.canonicalize(}x{@code )} is y, then the result contains every {@code BigDecimal} which,
     *  when canonicalized, yields y.</li>
     * </ul>
     *
     * @param bds an infinite {@code Iterable} of canonical {@code BigDecimal}s
     * @return all {@code BigDecimal}s which, once canonicalized, belong to {@code xs}
     */
    private @NotNull Iterable<BigDecimal> uncanonicalize(@NotNull Iterable<BigDecimal> bds) {
        Iterable<Integer> integersGeometric = integersGeometric();
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<BigDecimal>() {
            private final @NotNull Iterator<BigDecimal> bdi = bds.iterator();
            private final @NotNull Iterator<Integer> is1 = integersGeometric.iterator();
            private final @NotNull Iterator<Integer> is2 = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigDecimal next() {
                BigDecimal bd = bdi.next();
                if (bd.equals(BigDecimal.ZERO)) {
                    return new BigDecimal(BigInteger.ZERO, is1.next());
                } else {
                    bd = bd.stripTrailingZeros();
                    return BigDecimalUtils.setPrecision(bd, is2.next() + bd.precision());
                }
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s greater than or equal to {@code a}. A higher
     * {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale} corresponds to
     * a higher unscaled-value bit size and a higher scale, but the exact relationship is not simple to describe. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeUp(@NotNull BigDecimal a) {
        return withScale(secondaryScale).uncanonicalize(rangeUpCanonical(a));
    }

    /**
     * An {@code Iterable} that generates all {@code BigDecimal}s less than or equal to {@code a}. A higher
     * {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale} corresponds to
     * a higher unscaled-value bit size and a higher scale, but the exact relationship is not simple to describe. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeDown(@NotNull BigDecimal a) {
        return map(BigDecimal::negate, rangeUp(a.negate()));
    }

    /**
     * <p>An {@code Iterable} that generates {@code BigDecimal}s between {@code a} and {@code b}, inclusive. A higher
     * {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale} corresponds to
     * a higher unscaled-value bit size and a higher scale, but the exact relationship is not simple to describe.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} may be any {@code BigDecimal}.</li>
     *  <li>{@code b} may be any {@code BigDecimal}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code BigDecimal}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigDecimal> range(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else {
            return withScale(secondaryScale).uncanonicalize(rangeCanonical(a, b));
        }
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s greater than or equal to {@code a}. A
     * higher {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale}
     * corresponds to a higher scale, but the exact relationship is not simple to describe. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeUpCanonical(@NotNull BigDecimal a) {
        return map(
                c -> BigDecimalUtils.canonicalize(a.add(c)),
                filterInfinite(bd -> bd.signum() != -1, canonicalBigDecimals())
        );
    }

    /**
     * An {@code Iterable} that generates all canonical {@code BigDecimal}s less than or equal to {@code a}. A higher
     * {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale} corresponds to
     * a higher scale, but the exact relationship is not simple to describe. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeDownCanonical(@NotNull BigDecimal a) {
        return map(BigDecimal::negate, rangeUpCanonical(a.negate()));
    }

    /**
     * <p>An {@code Iterable} that generates canonical {@code BigDecimal}s between {@code a} and {@code b}, inclusive.
     * A higher {@code scale} corresponds to a higher unscaled-value bit size and a higher {@code secondaryScale}
     * corresponds to a higher unscaled-value bit size and a higher scale, but the exact relationship is not simple to
     * describe.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} may be any {@code BigDecimal}.</li>
     *  <li>{@code b} may be any {@code BigDecimal}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code BigDecimal}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return canonical {@code BigDecimal}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<BigDecimal> rangeCanonical(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        } else if (eq(a, b)) {
            return repeat(BigDecimalUtils.canonicalize(a));
        }
        BigDecimal difference = BigDecimalUtils.canonicalize(b.subtract(a));
        return map(
                c -> BigDecimalUtils.canonicalize(a.add(c)),
                filterInfinite(
                        bd -> le(bd, difference),
                        withScale(secondaryScale)
                                .zeroToPowerOfTenCanonicalBigDecimals(BigDecimalUtils.ceilingLog10(difference))
                )
        );
    }

    /**
     * Randomly inserts an element into a given infinite {@code Iterable}. The frequency of the added element is
     * 1/{@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>{@code x} may be any value of type {@code T}, or null.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite and non-removable.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param x an extra element
     * @param xs an {@code Iterable}
     * @param <T> the type of elements that {@code xs} contains
     * @return {@code xs} with copies of {@code x} randomly inserted
     */
    @Override
    public @NotNull <T> Iterable<T> withElement(@Nullable T x, @NotNull Iterable<T> xs) {
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        Iterable<Integer> range = range(1, scale);
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<Integer> is = range.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (is.next() == 1) {
                    return x;
                } else if (!xsi.hasNext()) {
                    throw new IllegalArgumentException("xs must be infinite.");
                } else {
                    return xsi.next();
                }
            }
        };
    }

    /**
     * See {@link RandomProvider#dependentPairsInfinite(Iterable, Function)}
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot, with no available hash code
     * @param <B> the type of values in the second slot
     * @return all possible pairs of values specified by {@code xs} and {@code f}
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsIdentityHash(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return dependentPairsInfiniteIdentityHash(xs, f);
    }

    /**
     * Generates all pairs of values, given a list of possible first values of the pairs, and a function mapping each
     * possible first value to a list of possible second values.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code f} must terminate and not return null when applied to any element of {@code xs}. All results must be
     *  infinite.</li>
     *  <li>The result is infinite, non-removable and does not contain nulls.</li>
     * </ul>
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
            private final @NotNull Iterator<A> xsi = xs.iterator();
            private final @NotNull Map<A, Iterator<B>> aToBs = new HashMap<>();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Pair<A, B> next() {
                A a = xsi.next();
                Iterator<B> bs = aToBs.computeIfAbsent(a, k -> f.apply(a).iterator());
                return new Pair<>(a, bs.next());
            }
        };
    }

    /**
     * Generates all pairs of values, given a list of possible first values of the pairs, and a function mapping each
     * possible first value to a list of possible second values. This method differs from
     * {@link RandomProvider#dependentPairsInfinite(Iterable, Function)} in that A doesn't need to have a hash code.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code f} must terminate and not return null when applied to any element of {@code xs}. All results must be
     *  infinite.</li>
     *  <li>The result is infinite, non-removable and does not contain nulls.</li>
     * </ul>
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot, with no available hash code
     * @param <B> the type of values in the second slot
     * @return all possible pairs of values specified by {@code xs} and {@code f}
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfiniteIdentityHash(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull Iterator<A> xsi = xs.iterator();
            private final @NotNull Map<A, Iterator<B>> aToBs = new IdentityHashMap<>();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Pair<A, B> next() {
                A a = xsi.next();
                Iterator<B> bs = aToBs.computeIfAbsent(a, k -> f.apply(a).iterator());
                return new Pair<>(a, bs.next());
            }
        };
    }

    /**
     * Shuffles a {@code List} in place using the Fisher-Yates algorithm. Every permutation is an equally likely
     * outcome.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param xs the {@code List} to be shuffled
     * @param <T> the type of the {@code List}'s elements
     */
    public <T> void shuffle(@NotNull List<T> xs) {
        int limit = xs.size() - 1;
        for (int i = 0; i < limit; i++) {
            int j = head(range(i, limit));
            Collections.swap(xs, i, j);
        }
    }

    /**
     * An {@code Iterable} that generates permutations of a {@code List}. Every permutation is an equally likely
     * outcome. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is infinite, non-removable, and consists of permutations of a {@code List}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs a list of elements
     * @param <T> the type of the given {@code List}'s elements
     * @return permutations of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> permutationsFinite(@NotNull List<T> xs) {
        return () -> new NoRemoveIterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull List<T> next() {
                List<T> copy = toList(xs);
                shuffle(copy);
                return copy;
            }
        };
    }

    /**
     * An {@code Iterable} that generates permutations of an {@code Iterable}. If the {@code Iterable} is finite, all
     * permutations can be generated; if it is infinite, then only permutations that are equal to the identity except
     * in a finite prefix can be generated. Unlike {@link RandomProvider#permutationsFinite(List)}, this method will
     * generally not return an {@code Iterable} where every permutation occurs with equal probability.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale.</li>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is infinite, non-removable, and consists of permutations of some {@code Iterable} such that each
     *  permutation differs from {@code xs} in a finite prefix.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return permutations of {@code xs} which differ from {@code xs} in some finite prefix
     */
    @Override
    public @NotNull <T> Iterable<Iterable<T>> prefixPermutations(@NotNull Iterable<T> xs) {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (!lengthAtLeast(2, xs)) return repeat(new NoRemoveIterable<>(xs));
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<Iterable<T>>() {
            private final @NotNull CachedIterator<T> cxs = new CachedIterator<>(xs);
            private final @NotNull Iterator<Integer> prefixSizes = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Iterable<T> next() {
                outer:
                while (true) {
                    int prefixSize = prefixSizes.next();
                    if (cxs.knownSize().isPresent() && cxs.knownSize().get() > prefixSize) {
                        continue;
                    }
                    List<T> prefix = new ArrayList<>();
                    for (int i = 0; i < prefixSize; i++) {
                        NullableOptional<T> x = cxs.get(i);
                        if (!x.isPresent()) {
                            continue outer;
                        }
                        prefix.add(x.get());
                    }
                    shuffle(prefix);
                    return concat(prefix, drop(prefixSize, xs));
                }
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code List}s of a given length with elements from a given
     * {@code Iterable}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements have the same length.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param size the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists of a given length created from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return chunkInfinite(size, xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Pair}s of elements from two {@code Iterable}s. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return ordered {@code Pair}s of elements from {@code as} and {@code bs}
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return zipInfinite(as, bs);
    }

    /**
     * An {@code Iterable} that generates all {@code Pair}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Pair}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return chunkPairsInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Triple}s of elements from three {@code Iterable}s. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>{@code cs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @return ordered {@code Triple}s of elements from {@code as}, {@code bs}, and {@code cs}
     */
    @Override
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return zip3Infinite(as, bs, cs);
    }

    /**
     * An {@code Iterable} that generates all {@code Triple}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Triple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return chunkTriplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Quadruple}s of elements from four {@code Iterable}s. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>{@code cs} must be infinite.</li>
     *  <li>{@code ds} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @return ordered {@code Quadruple}s of elements from {@code as}, {@code bs}, {@code cs}, and {@code ds}
     */
    @Override
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return zip4Infinite(as, bs, cs, ds);
    }

    /**
     * An {@code Iterable} that generates all {@code Quadruple}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quadruple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return chunkQuadruplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Quintuple}s of elements from five {@code Iterable}s. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>{@code cs} must be infinite.</li>
     *  <li>{@code ds} must be infinite.</li>
     *  <li>{@code es} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
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
     * @return ordered {@code Quintuple}s of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, and
     * {@code es}
     */
    @Override
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return zip5Infinite(as, bs, cs, ds, es);
    }

    /**
     * An {@code Iterable} that generates all {@code Quintuple}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quintuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return chunkQuintuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Sextuple}s of elements from six {@code Iterable}s. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>{@code cs} must be infinite.</li>
     *  <li>{@code ds} must be infinite.</li>
     *  <li>{@code es} must be infinite.</li>
     *  <li>{@code fs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
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
     * @return ordered {@code Sextuple}s of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es},
     * and {@code fs}
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
        return zip6Infinite(as, bs, cs, ds, es, fs);
    }

    /**
     * An {@code Iterable} that generates all {@code Sextuple}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Sextuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return chunkSextuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Septuple}s of elements from seven {@code Iterable}s. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>{@code cs} must be infinite.</li>
     *  <li>{@code ds} must be infinite.</li>
     *  <li>{@code es} must be infinite.</li>
     *  <li>{@code fs} must be infinite.</li>
     *  <li>{@code gs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
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
     * @return ordered {@code Septuple}s of elements from {@code as}, {@code bs}, {@code cs}, {@code ds}, {@code es},
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
        return zip7Infinite(as, bs, cs, ds, es, fs, gs);
    }

    /**
     * An {@code Iterable} that generates all {@code Septuple}s of elements from an {@code Iterable}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Septuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return chunkSeptuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code String}s of a given length with characters from a given
     * {@code String}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>If {@code s} is empty, {@code size} must be 0.</li>
     *  <li>The result is infinite, non-removable, and all of its elements have the same length.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param size the length of the result {@code String}s
     * @param s the {@code String} from which characters are selected
     * @return {@code String}s of a given length created from {@code s}
     */
    @Override
    public @NotNull Iterable<String> strings(int size, @NotNull String s) {
        if (s.isEmpty()) {
            if (size == 0) {
                return repeat("");
            } else {
                throw new IllegalArgumentException("If s is empty, size must be 0. Invalid size: " + size);
            }
        }
        return super.strings(size, s);
    }

    /**
     * An {@code Iterable} that generates all {@code List}s with elements from a given {@code Iterable}. The mean
     * length of the {@code List}s is {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and none of its elements are null.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists created from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<Integer> sizes = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull List<T> next() {
                int size = sizes.next();
                List<T> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(xsi.next());
                }
                return list;
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code List}s with a minimum length and with elements from a given
     * {@code Iterable}. The mean length of the {@code List}s is {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code minSize} cannot be negative.</li>
     *  <li>{@code this} must have a {@code scale} at least 1 greater than {@code minSize}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and none of its elements are null.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minSize the minimum length of the result {@code List}s
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists with length at least {@code minSize} created from {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        if (minSize < 0) {
            throw new IllegalArgumentException("minSize cannot be negative. Invalid minSize: " + minSize);
        }
        Iterable<Integer> rangeUpGeometric = rangeUpGeometric(minSize);
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<Integer> sizes = rangeUpGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull List<T> next() {
                int size = sizes.next();
                List<T> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(xsi.next());
                }
                return list;
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code List}s of a given length with elements from a given
     * {@code Iterable} with no repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code xs} must contain at least {@code size} distinct elements.</li>
     *  <li>The result is infinite, non-removable, and all of its elements have the same length and no
     *  repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param size the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists of a given length created from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<List<T>> distinctLists(int size, @NotNull Iterable<T> xs) {
        return distinctChunkInfinite(size, xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Pair}s of elements from an {@code Iterable} with no repetitions.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 2 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Pair}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return distinctChunkPairsInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Triple}s of elements from an {@code Iterable} with no repetitions.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 3 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Triple}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return distinctChunkTriplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Quadruple}s of elements from an {@code Iterable} with no
     * repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 4 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quadruple}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return distinctChunkQuadruplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Quintuple}s of elements from an {@code Iterable} with no
     * repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 5 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quintuple}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return distinctChunkQuintuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Sextuple}s of elements from an {@code Iterable} with no
     * repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 6 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Sextuple}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return distinctChunkSextuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code Septuple}s of elements from an {@code Iterable} with no
     * repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite and must contain at least 7 distinct elements.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements contain no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Septuple}s of elements from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuples(@NotNull Iterable<T> xs) {
        return distinctChunkSeptuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all {@code List}s with elements from a given {@code Iterable} with no
     * repetitions. The mean length of the {@code List}s increases as {@code scale} increases. The more distinct
     * elements {@code xs} contains, the closer the mean length will be to {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements have no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists of a given length created from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<List<T>> distinctLists(@NotNull Iterable<T> xs) {
        Iterable<Integer> naturalIntegersGeometric = naturalIntegersGeometric();
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<Integer> sizes = naturalIntegersGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public List<T> next() {
                int size = sizes.next();
                Set<T> set = new LinkedHashSet<>();
                for (int i = 0; i < size; i++) {
                    set.add(xsi.next());
                }
                return toList(set);
            }
        };
    }

    /**
     * An {@code Iterable} that generates all {@code List}s with a minimum length and with elements from a given
     * {@code Iterable} with no repetitions. The mean length of the {@code List}s increases as {@code scale} increases.
     * The more distinct elements {@code xs} contains, the closer the mean length will be to {@code scale}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code minSize} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code this} must have a {@code scale} at least 1 greater than {@code minSize}.</li>
     *  <li>{@code xs} must contain at least {@code minSize} distinct elements.</li>
     *  <li>The result is infinite, non-removable, and none of its elements are null.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minSize the minimum length of the result {@code List}s
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return lists with length at least {@code minSize} created from {@code xs} with no repetitions
     */
    @Override
    public @NotNull <T> Iterable<List<T>> distinctListsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        if (minSize < 0) {
            throw new IllegalArgumentException("minSize cannot be negative. Invalid minSize: " + minSize);
        }
        Iterable<Integer> rangeUpGeometric = rangeUpGeometric(minSize);
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<Integer> sizes = rangeUpGeometric.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public List<T> next() {
                int size = sizes.next();
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < minSize) {
                    set.add(xsi.next());
                }
                for (int i = 0; i < size - minSize; i++) {
                    set.add(xsi.next());
                }
                return toList(set);
            }
        };
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s of a given length with elements from a given
     * {@code Iterable}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted and have the same length.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param size the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return sorted lists of a given length created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bags(int size, @NotNull Iterable<T> xs) {
        return sortedChunkInfinite(size, xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Pair}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Pair}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> bagPairs(@NotNull Iterable<T> xs) {
        return sortedChunkPairsInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Triple}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Triple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> bagTriples(@NotNull Iterable<T> xs) {
        return sortedChunkTriplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Quadruple}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Quadruple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> bagQuadruples(@NotNull Iterable<T> xs) {
        return sortedChunkQuadruplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Quintuple}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Quintuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> bagQuintuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedChunkQuintuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Sextuple}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Sextuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> bagSextuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedChunkSextuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code Septuple}s of elements from an {@code Iterable}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return unordered {@code Septuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> bagSeptuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedChunkSeptuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s with elements from a given {@code Iterable}. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return sorted lists created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bags(@NotNull Iterable<T> xs) {
        return map(
                ys -> {
                    if (ys.size() == 1) {
                        T first = ys.get(0);
                        first.compareTo(first);
                    }
                    return sort(ys);
                },
                lists(xs)
        );
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s with a minimum length and with elements from a given
     * {@code Iterable}. The mean length of the {@code List}s is {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code minSize} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code this} must have a {@code scale} at least 1 greater than {@code minSize}.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minSize the minimum length of the result {@code List}s
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return sorted lists with length at least {@code minSize} created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bagsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return map(
                ys -> {
                    if (ys.size() == 1) {
                        T first = ys.get(0);
                        first.compareTo(first);
                    }
                    return sort(ys);
                },
                listsAtLeast(minSize, xs)
        );
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s of a given length with elements from a given
     * {@code Iterable} with no repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted, have the same length, and have
     *  no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param size the length of the result lists
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return distinct sorted lists of a given length created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsets(int size, @NotNull Iterable<T> xs) {
        return sortedDistinctChunkInfinite(size, xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Pair}s of elements from an {@code Iterable}. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Pair}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> subsetPairs(@NotNull Iterable<T> xs) {
        return sortedDistinctChunkPairsInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Triple}s of elements from an {@code Iterable}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Triple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> subsetTriples(@NotNull Iterable<T> xs) {
        return sortedDistinctChunkTriplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Quadruple}s of elements from an {@code Iterable}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quadruple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> subsetQuadruples(
            @NotNull Iterable<T> xs
    ) {
        return sortedDistinctChunkQuadruplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Quintuple}s of elements from an {@code Iterable}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Quintuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> subsetQuintuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedDistinctChunkQuintuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Sextuple}s of elements from an {@code Iterable}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Sextuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> subsetSextuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedDistinctChunkSextuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all distinct sorted {@code Septuple}s of elements from an {@code Iterable}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and contains no nulls. Its elements are sorted and distinct.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     * @return ordered {@code Septuple}s of elements from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> subsetSeptuples(
            @NotNull Iterable<T> xs
    ) {
        return sortedDistinctChunkSeptuplesInfinite(xs);
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s with elements from a given {@code Iterable} with no
     * repetitions. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted and have no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return sorted, distinct lists created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsets(@NotNull Iterable<T> xs) {
        return map(
                ys -> {
                    if (ys.size() == 1) {
                        T first = ys.get(0);
                        first.compareTo(first);
                    }
                    return sort(ys);
                },
                distinctLists(xs)
        );
    }

    /**
     * An {@code Iterable} that generates all sorted {@code List}s with a minimum length and with elements from a given
     * {@code Iterable} with no repetitions. The mean length of the {@code List}s is {@code scale}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code minSize} cannot be negative.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code this} must have a {@code scale} at least 1 greater than {@code minSize}.</li>
     *  <li>The result is infinite, non-removable, and all of its elements are sorted and have no repetitions.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minSize the minimum length of the result {@code List}s
     * @param xs the {@code Iterable} from which elements are selected
     * @param <T> the type of the given {@code Iterable}'s elements
     * @return sorted, distinct lists with length at least {@code minSize} created from {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return map(
                ys -> {
                    if (ys.size() == 1) {
                        T first = ys.get(0);
                        first.compareTo(first);
                    }
                    return sort(ys);
                },
                distinctListsAtLeast(minSize, xs)
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Either}s generated from two {@code Iterable}s. The ratio of
     * elements from {@code as} to elements from {@code bs} is {@code scale}:1. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code as} must be infinite.</li>
     *  <li>{@code bs} must be infinite.</li>
     *  <li>The result contains {@code Either}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all {@code Either}s created from {@code as} and {@code bs}
     */
    @Override
    public @NotNull <A, B> Iterable<Either<A, B>> eithers(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        Iterable<Integer> range = range(0, scale);
        return () -> new NoRemoveIterator<Either<A, B>>() {
            private final @NotNull Iterator<Integer> indices = range.iterator();
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Either<A, B> next() {
                if (indices.next() == 0) {
                    return Either.ofB(bsi.next());
                } else {
                    return Either.ofA(asi.next());
                }
            }
        };
    }

    /**
     * An {@code Iterable} that generates all elements from a list of {@code Iterable}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code xss} cannot be empty. Every element of {@code xss} must be infinite.</li>
     *  <li>The result is infinite.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xss the list of {@code Iterable}s
     * @param <T> the type of the {@code Iterable}s' elements
     * @return all elements of {@code xss}
     */
    public @NotNull <T> Iterable<T> choose(@NotNull List<Iterable<T>> xss) {
        return map(Iterator::next, uniformSample(toList(map(Iterable::iterator, xss))));
    }

    /**
     * An {@code Iterable} that uniformly generates elements from the Cartesian product of a {@code List} of
     * {@code List}s. Does not support removal.
     *
     * <ul>
     *  <li>{@code xss} cannot be empty and no element of {@code xss} may be null or empty.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param xss a {@code List} of {@code List}s
     * @param <T> the type of the {@code List}s' elements
     * @return {@code List}s whose ith element is selected uniformly from the ith list in {@code xss}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> cartesianProduct(@NotNull List<List<T>> xss) {
        if (xss.isEmpty()) {
            throw new IllegalArgumentException("xss cannot be empty.");
        }
        return transpose(map(this::uniformSample, xss));
    }

    /**
     * An {@code Iterable} that generates {@code List}s of elements from a given {@code Iterable} that contain a given
     * element. The mean length of the {@code List}s is {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 3.</li>
     *  <li>{@code x} may be any value of type {@code T}, or null.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code xs} cannot only contain copies of {@code x}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param x an element that the output {@code List}s must contain
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     * @return {@code List}s containing {@code x} and possibly members of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> listsWithElement(@Nullable T x, @NotNull Iterable<T> xs) {
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int leftScale = (scale - 1) / 2;
        int rightScale = (scale & 1) == 1 ? leftScale : leftScale + 1;
        return map(
                p -> toList(concat(p.a, cons(x, p.b))),
                pairs(
                        withScale(leftScale).lists(filterInfinite(y -> !Objects.equals(y, x), xs)),
                        withScale(rightScale).lists(xs)
                )
        );
    }

    /**
     * An {@code Iterable} that generates distinct sorted {@code List}s of elements from a given {@code Iterable} that
     * contain a given element. The mean length of the {@code List}s is approximately {@code scale}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>{@code x} may be any value of type {@code T}, or null.</li>
     *  <li>{@code xs} must be infinite.</li>
     *  <li>{@code xs} cannot only contain copies of {@code x}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param x an element that the output {@code List}s must contain
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     * @return sorted, distinct {@code List}s containing {@code x} and possibly members of {@code xs}
     */
    @Override
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsWithElement(
            T x,
            @NotNull Iterable<T> xs
    ) {
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        return map(ys -> sort(cons(x, ys)), withScale(scale - 1).subsets(filter(y -> !Objects.equals(y, x), xs)));
    }

    /**
     * An {@code Iterable} that generates {@code List}s of elements from a given {@code Iterable} that contain at least
     * one of a given {@code Iterable} of sublists. The average length of the result {@code List}s is {@code scale}
     * plus the average length of the {@code List}s in {@code sublists}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2.</li>
     *  <li>{@code sublists} must be infinite and cannot contain nulls.</li>
     *  <li>{@code xs} must be infinite and cannot contain nulls.</li>
     *  <li>The result is infinite and does not contain nulls.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param sublists {@code List}s, at least one of which must be contained in each result {@code List}
     * @param xs a {@code List}
     * @param <T> the type of elements in {@code xs}
     * @return all lists containing at least one of {@code sublists} and possibly members of {@code xs}
     */
    @Override
    public @NotNull <T> Iterable<List<T>> listsWithSublists(
            @NotNull Iterable<List<T>> sublists,
            @NotNull Iterable<T> xs
    ) {
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                t -> toList(concat(Arrays.asList(t.a, t.b, t.c))),
                triples(withScale(leftScale).lists(xs), sublists, withScale(rightScale).lists(xs))
        );
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        RandomProvider rp = (RandomProvider) that;
        return scale == rp.scale && secondaryScale == rp.secondaryScale && tertiaryScale == rp.tertiaryScale &&
                seed.equals(rp.seed) && prng.equals(rp.prng);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        int result = seed.hashCode();
        result = 31 * result + prng.hashCode();
        result = 31 * result + scale;
        result = 31 * result + secondaryScale;
        result = 31 * result + tertiaryScale;
        return result;
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public String toString() {
        return "RandomProvider[@" + getId() + ", " + scale + ", " + secondaryScale + ", " + tertiaryScale + "]";
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code RandomProvider} used outside this class.
     */
    public void validate() {
        prng.validate();
        assertEquals(this, seed.size(), IsaacPRNG.SIZE);
    }
}
