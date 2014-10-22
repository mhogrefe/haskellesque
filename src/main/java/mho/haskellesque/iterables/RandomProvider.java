package mho.haskellesque.iterables;

import mho.haskellesque.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Random;

public final class RandomProvider implements IterableProvider {
    private final @NotNull Random generator;

    public RandomProvider() {
        generator = new Random();
    }

    public RandomProvider(@NotNull Random generator) {
        this.generator = generator;
    }

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
        };
    }

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
        };
    }

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
        };
    }

    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return () -> new Iterator<Byte>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Byte next() {
                return (byte) (generator.nextInt(127) + 1);
            }
        };
    }

    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return () -> new Iterator<Short>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Short next() {
                return (short) (generator.nextInt(65535) + 1);
            }
        };
    }

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
        };
    }

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
        };
    }

    @NotNull
    @Override
    public Iterable<BigInteger> positiveBigIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Byte> negativeBytes() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Short> negativeShorts() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Integer> negativeIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Long> negativeLongs() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<BigInteger> negativeBigIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Byte> naturalBytes() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Short> naturalShorts() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Integer> naturalIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Long> naturalLongs() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<BigInteger> naturalBigIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Byte> bytes() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Short> shorts() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Integer> integers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Long> longs() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<BigInteger> bigIntegers() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Character> asciiCharacters() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Character> characters() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Float> positiveOrdinaryFloats() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Float> negativeOrdinaryFloats() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Float> ordinaryFloats() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Float> floats() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Double> positiveOrdinaryDoubles() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Double> negativeOrdinaryDoubles() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Double> ordinaryDoubles() {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Double> doubles() {
        return null;
    }
}