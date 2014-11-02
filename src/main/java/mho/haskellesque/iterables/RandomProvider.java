package mho.haskellesque.iterables;

import mho.haskellesque.math.MathUtils;
import mho.haskellesque.ordering.Ordering;
import mho.haskellesque.structures.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.haskellesque.iterables.IterableUtils.isEmpty;

/**
 * <tt>Iterable</tt>s that randomly generate all (or some important subset) of a type's values.
 */
public class RandomProvider implements IterableProvider {
    protected static final int BIG_INTEGER_MEAN_BIT_SIZE = 64;
    protected static final int BIG_DECIMAL_MEAN_SCALE = (int) Math.round(Math.log10(2) * BIG_INTEGER_MEAN_BIT_SIZE);
    protected static final int MEAN_LIST_SIZE = 10;

    protected final @NotNull Random generator;

    public RandomProvider() {
        generator = new Random();
    }

    public RandomProvider(@NotNull Random generator) {
        this.generator = generator;
    }

    /**
     * An <tt>Iterator</tt> that generates both <tt>Boolean</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Boolean> booleans() {
        return () -> new Iterator<Boolean>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Boolean next() {
                return generator.nextBoolean();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterator</tt> that generates all <tt>Ordering</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Ordering> orderings() {
        return () -> new Iterator<Ordering>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Ordering next() {
                return Ordering.fromInt(generator.nextInt(3) - 1);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>RoundingMode</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return () -> new Iterator<RoundingMode>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public RoundingMode next() {
                int index = generator.nextInt(8);
                switch (index) {
                    case 0:  return RoundingMode.UNNECESSARY;
                    case 1:  return RoundingMode.UP;
                    case 2:  return RoundingMode.DOWN;
                    case 3:  return RoundingMode.CEILING;
                    case 4:  return RoundingMode.FLOOR;
                    case 5:  return RoundingMode.HALF_UP;
                    case 6:  return RoundingMode.HALF_DOWN;
                    default: return RoundingMode.HALF_EVEN;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all positive <tt>Byte</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return () -> new Iterator<Byte>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Byte next() {
                return (byte) (generator.nextInt(Byte.MAX_VALUE) + 1);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all positive <tt>Short</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return () -> new Iterator<Short>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Short next() {
                return (short) (generator.nextInt(Short.MAX_VALUE) + 1);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all positive <tt>Integer</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> positiveIntegers() {
        return () -> new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return generator.nextInt(Integer.MAX_VALUE) + 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all positive <tt>Long</tt>s from a uniform distribution from a uniform
     * distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> positiveLongs() {
        return () -> new Iterator<Long>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long next() {
                long next;
                do {
                    next = Math.abs(generator.nextLong());
                } while (next < 0);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return An <tt>Iterable</tt> that generates all positive <tt>BigInteger</tt>s. The bit size is chosen from a
     * geometric distribution with mean approximately <tt>meanBitSize</tt> (The ratio between the actual mean and
     * <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The is an infinite pseudorandom sequence of all <tt>BigIntegers</tt></li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>BigInteger</tt>s generated
     */
    public @NotNull Iterable<BigInteger> positiveBigIntegers(int meanBitSize) {
        if (meanBitSize <= 2)
            throw new IllegalArgumentException("meanBitSize must be greater than 2.");
        return () -> new Iterator<BigInteger>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                List<Boolean> bits = new ArrayList<>();
                bits.add(true);
                while (true) {
                    if (generator.nextDouble() < 1.0 / (meanBitSize - 1)) {
                        break;
                    } else {
                        bits.add(generator.nextBoolean());
                    }
                }
                return MathUtils.fromBigEndianBits(bits);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all positive <tt>BigInteger</tt>s. The bit size is chosen from a geometric
     * distribution with mean approximately 64. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return positiveBigIntegers(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * An <tt>Iterable</tt> that generates all negative <tt>Byte</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> negativeBytes() {
        return () -> new Iterator<Byte>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Byte next() {
                return (byte) (-1 - generator.nextInt(Byte.MAX_VALUE + 1));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all negative <tt>Short</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> negativeShorts() {
        return () -> new Iterator<Short>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Short next() {
                return (short) (-1 - generator.nextInt(Short.MAX_VALUE + 1));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all negative <tt>Integer</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> negativeIntegers() {
        return filter(i -> i < 0, integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all negative <tt>Long</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> negativeLongs() {
        return filter(l -> l < 0, longs());
    }

    /**
     * @return An <tt>Iterable</tt> that generates all negative <tt>BigInteger</tt>s. The bit size is chosen from a
     * geometric distribution with mean approximately <tt>meanBitSize</tt> (The ratio between the actual mean and
     * <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>BigIntegers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>BigInteger</tt>s generated
     */
    public @NotNull Iterable<BigInteger> negativeBigIntegers(int meanBitSize) {
        return map(BigInteger::negate, positiveBigIntegers(meanBitSize));
    }

    /**
     * An <tt>Iterable</tt> that generates all negative <tt>BigInteger</tt>s. The bit size is chosen from a geometric
     * distribution with mean approximately 64. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return map(BigInteger::negate, positiveBigIntegers());
    }

    /**
     * An <tt>Iterable</tt> that generates all natural <tt>Byte</tt>s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> naturalBytes() {
        return map(i -> (byte) (i & 0x7f), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all natural <tt>Short</tt>s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> naturalShorts() {
        return map(i -> (short) (i & 0x7fff), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all natural <tt>Integer</tt>s (including 0) from a uniform distribution.
     * Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> naturalIntegers() {
        return map(i -> i & 0x7fffffff, integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all natural <tt>Long</tt>s (including 0) from a uniform distribution. Does
     * not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> naturalLongs() {
        return map(l -> l & 0x7fffffffffffffffL, longs());
    }

    /**
     * @return An <tt>Iterable</tt> that generates all natural <tt>BigInteger</tt>s (including 0). The bit size is
     * chosen from a geometric distribution with mean approximately <tt>meanBitSize</tt> (The ratio between the actual
     * mean and <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>BigIntegers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>BigInteger</tt>s generated
     */
    public @NotNull Iterable<BigInteger> naturalBigIntegers(int meanBitSize) {
        if (meanBitSize <= 2)
            throw new IllegalArgumentException("meanBitSize must be greater than 2.");
        return () -> new Iterator<BigInteger>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                List<Boolean> bits = new ArrayList<>();
                while (true) {
                    if (generator.nextDouble() < 1.0 / (meanBitSize - 1)) {
                        break;
                    } else {
                        bits.add(generator.nextBoolean());
                    }
                }
                return MathUtils.fromBigEndianBits(bits);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all natural <tt>BigInteger</tt>s (including 0). The bit size is chosen from
     * a geometric distribution with mean approximately 64. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return naturalBigIntegers(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Byte</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Byte> bytes() {
        return map(i -> (byte) (i & 0xff), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Short</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Short> shorts() {
        return map(i -> (short) (i & 0xffff), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Integer</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Integer> integers() {
        return () -> new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return generator.nextInt();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Long</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Long> longs() {
        return () -> new Iterator<Long>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Long next() {
                return generator.nextLong();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return An <tt>Iterable</tt> that generates all <tt>BigInteger</tt>s. The bit size is chosen from a geometric
     * distribution with mean approximately <tt>meanBitSize</tt> (The ratio between the actual mean and
     * <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>BigIntegers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>BigInteger</tt>s generated
     */
    public @NotNull Iterable<BigInteger> bigIntegers(int meanBitSize) {
        if (meanBitSize <= 2)
            throw new IllegalArgumentException("meanBitSize must be greater than 2.");
        return () -> new Iterator<BigInteger>() {
            private final Iterator<BigInteger> it = naturalBigIntegers(meanBitSize).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                BigInteger nbi = it.next();
                if (generator.nextBoolean()) {
                    nbi = nbi.negate().subtract(BigInteger.ONE);
                }
                return nbi;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>BigInteger</tt>s. The bit size is chosen from a geometric
     * distribution with mean approximately 64. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return bigIntegers(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * @return An <tt>Iterable</tt> that generates all natural <tt>Integer</tt>s chosen from a geometric distribution
     * with mean approximately <tt>meanSize</tt> (The ratio between the actual mean and <tt>meanSize</tt> decreases as
     * <tt>meanSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanSize</tt> must be greater than 1.</li>
     *  <li>The result is an infinite pseudorandom sequence of all natural <tt>Integers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanSize the approximate mean bit size of the <tt>Integer</tt>s generated
     */
    public @NotNull Iterable<Integer> naturalIntegersGeometric(int meanSize) {
        if (meanSize <= 1)
            throw new IllegalArgumentException("meanSize must be greater than 1.");
        return () -> new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                int i = 0;
                while (generator.nextDouble() >= 1.0 / meanSize) {
                    i++;
                }
                return i;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return An <tt>Iterable</tt> that generates all positive <tt>Integer</tt>s chosen from a geometric distribution
     * with mean approximately <tt>meanSize</tt> (The ratio between the actual mean and <tt>meanSize</tt> decreases as
     * <tt>meanSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all positive <tt>Integers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanSize the approximate mean size of the <tt>Integer</tt>s generated
     */
    public @NotNull Iterable<Integer> positiveIntegersGeometric(int meanSize) {
        if (meanSize <= 2)
            throw new IllegalArgumentException("meanSize must be greater than 2.");
        return () -> new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                int i = 1;
                while (generator.nextDouble() >= 1.0 / (meanSize - 1)) {
                    i++;
                }
                return i;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return An <tt>Iterable</tt> that generates all negative <tt>Integer</tt>s chosen from a geometric distribution
     * with absolute mean approximately <tt>meanSize</tt> (The ratio between the actual absolute mean and
     * <tt>meanSize</tt> decreases as <tt>meanSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all negative <tt>Integers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanSize the approximate absolute mean size of the <tt>Integer</tt>s generated
     */
    public @NotNull Iterable<Integer> negativeIntegersGeometric(int meanSize) {
        return map(i -> -i, positiveIntegersGeometric(meanSize));
    }

    /**
     * @return An <tt>Iterable</tt> that generates all <tt>Integer</tt>s chosen from a geometric distribution with
     * absolute mean approximately <tt>meanSize</tt> (The ratio between the actual absolute mean and <tt>meanSize</tt>
     * decreases as <tt>meanSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>meanSize</tt> must be greater than 1.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>Integers</tt>.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanSize the approximate mean bit size of the <tt>Integer</tt>s generated
     */
    public @NotNull Iterable<Integer> integersGeometric(int meanSize) {
        if (meanSize <= 1)
            throw new IllegalArgumentException("meanSize must be greater than 1.");
        return () -> new Iterator<Integer>() {
            private final Iterator<Integer> it = naturalIntegersGeometric(meanSize).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                Integer ni = it.next();
                if (generator.nextBoolean()) {
                    ni = -ni - 1;
                }
                return ni;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all ASCII <tt>Character</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Character> asciiCharacters() {
        return map(i -> (char) (i & 0x7f), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Character</tt>s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Character> characters() {
        return map(i -> (char) (i & 0xffff), integers());
    }

    /**
     * An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) positive floats from a uniform
     * distribution. Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Float> positiveOrdinaryFloats() {
        return map(Math::abs, filter(
                f -> Float.isFinite(f) && !Float.isNaN(f) && !f.equals(-0.0f) && !f.equals(0.0f),
                floats()
        ));
    }

    /**
     * @return An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) negative floats from a
     * uniform distribution. Negative zero is not included. Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Float> negativeOrdinaryFloats() {
        return map(f -> -f, positiveOrdinaryFloats());
    }

    /**
     * An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) floats from a uniform distribution.
     * Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Float> ordinaryFloats() {
        return filter(f -> Float.isFinite(f) && !Float.isNaN(f) && !f.equals(-0.0f), floats());
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Float</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Float> floats() {
        return () -> new Iterator<Float>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Float next() {
                float next;
                boolean problematicNaN;
                do {
                    int floatBits = generator.nextInt();
                    next = Float.intBitsToFloat(floatBits);
                    problematicNaN = Float.isNaN(next) && floatBits != 0x7fc00000;
                } while (problematicNaN);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) positive floats from a uniform
     * distribution. Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Double> positiveOrdinaryDoubles() {
        return map(Math::abs, filter(
                d -> Double.isFinite(d) && !Double.isNaN(d) && !d.equals(-0.0f) && !d.equals(0.0f),
                doubles()
        ));
    }

    /**
     * @return An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) negative floats from a
     * uniform distribution. Negative zero is not included. Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Double> negativeOrdinaryDoubles() {
        return map(d -> -d, positiveOrdinaryDoubles());
    }

    /**
     * An <tt>Iterable</tt> that generates all ordinary (neither NaN nor infinite) floats from a uniform distribution.
     * Does not support removal.
     *
     * Length is infinite.
     */
    @Override
    public @NotNull Iterable<Double> ordinaryDoubles() {
        return filter(d -> Double.isFinite(d) && !Double.isNaN(d) && !d.equals(-0.0), doubles());
    }

    /**
     * An <tt>Iterable</tt> that generates all <tt>Double</tt>s from a uniform distribution. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Double> doubles() {
        return () -> new Iterator<Double>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Double next() {
                double next;
                boolean problematicNaN;
                do {
                    long doubleBits = generator.nextLong();
                    next = Double.longBitsToDouble(doubleBits);
                    problematicNaN = Double.isNaN(next) && doubleBits != 0x7ff8000000000000L;
                } while (problematicNaN);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    public @NotNull Iterable<BigDecimal> positiveBigDecimals(int meanScale) {
        return map(
                p -> new BigDecimal(p.a, p.b),
                pairs(negativeBigIntegers(), integersGeometric(meanScale))
        );
    }

    @Override
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return positiveBigDecimals(BIG_DECIMAL_MEAN_SCALE);
    }

    public @NotNull Iterable<BigDecimal> negativeBigDecimals(int meanScale) {
        return map(
                p -> new BigDecimal(p.a, p.b),
                pairs(negativeBigIntegers(), integersGeometric(meanScale))
        );
    }

    @Override
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return negativeBigDecimals(BIG_DECIMAL_MEAN_SCALE);
    }

    public @NotNull Iterable<BigDecimal> bigDecimals(int meanScale) {
        return map(p -> new BigDecimal(p.a, p.b), pairs(bigIntegers(), integersGeometric(meanScale)));
    }

    @Override
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return bigDecimals(BIG_DECIMAL_MEAN_SCALE);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return zip(as, bs);
    }

    @Override
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return zip3(as, bs, cs);
    }

    @Override
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return zip4(as, bs, cs, ds);
    }

    @Override
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return zip5(as, bs, cs, ds, es);
    }

    @Override
    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        return zip6(as, bs, cs, ds, es, fs);
    }

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
        return zip7(as, bs, cs, ds, es, fs, gs);
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(2, xs);
        return zip(xss.get(0), xss.get(1));
    }

    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(3, xs);
        return zip3(xss.get(0), xss.get(1), xss.get(2));
    }

    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(4, xs);
        return zip4(xss.get(0), xss.get(1), xss.get(2), xss.get(3));
    }

    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(5, xs);
        return zip5(xss.get(0), xss.get(1), xss.get(2), xss.get(3), xss.get(4));
    }

    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(6, xs);
        return zip6(xss.get(0), xss.get(1), xss.get(2), xss.get(3), xss.get(4), xss.get(5));
    }

    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        List<Iterable<T>> xss = demux(7, xs);
        return zip7(xss.get(0), xss.get(1), xss.get(2), xss.get(3), xss.get(4), xss.get(5), xss.get(6));
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return transpose(demux(size, xs));
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return Arrays.asList(new ArrayList<T>());
        return () -> new Iterator<List<T>>() {
            private Iterator<T> xsi = xs.iterator();
            private Iterator<Integer> sizes = naturalIntegersGeometric(MEAN_LIST_SIZE).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public List<T> next() {
                int size = sizes.next();
                List<T> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(xsi.next());
                }
                return list;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    @Override
    public @NotNull Iterable<String> strings(int size, @NotNull Iterable<Character> cs) {
        return map(IterableUtils::charsToString, transpose(demux(size, cs)));
    }

    @Override
    public @NotNull Iterable<String> strings(int size) {
        return strings(size, characters());
    }

    //todo
    @Override
    public @NotNull Iterable<String> strings(@NotNull Iterable<Character> cs) {
        if (isEmpty(cs)) return Arrays.asList("");
        return () -> new Iterator<String>() {
            private Iterator<Character> csi = cs.iterator();
            private Iterator<Integer> sizes = naturalIntegersGeometric(MEAN_LIST_SIZE).iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                int size = sizes.next();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    sb.append(csi.next());
                }
                return sb.toString();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    @Override
    public @NotNull Iterable<String> strings() {
        return strings(characters());
    }
}
