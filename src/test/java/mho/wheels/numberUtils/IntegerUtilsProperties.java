package mho.wheels.numberUtils;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import mho.wheels.testing.TestProperties;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.math.MathUtils.ceilingLog;
import static mho.wheels.numberUtils.IntegerUtils.*;
import static mho.wheels.numberUtils.IntegerUtils.demux;
import static mho.wheels.numberUtils.IntegerUtils.mux;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class IntegerUtilsProperties extends TestProperties {
    public IntegerUtilsProperties() {
        super("IntegerUtils");
    }

    @Override
    protected void testBothModes() {
        propertiesLowestOneBit_int();
        propertiesLowestOneBit_long();
        propertiesHighestOneBit_int();
        propertiesHighestOneBit_long();
        propertiesIsPowerOfTwo_int();
        compareImplementationsIsPowerOfTwo_int();
        propertiesIsPowerOfTwo_long();
        compareImplementationsIsPowerOfTwo_long();
        propertiesIsPowerOfTwo_BigInteger();
        compareImplementationsIsPowerOfTwo_BigInteger();
        propertiesCeilingLog2_int();
        compareImplementationsCeilingLog2_int();
        propertiesCeilingLog2_long();
        compareImplementationsCeilingLog2_long();
        propertiesCeilingLog2_BigInteger();
        propertiesBits_int();
        compareImplementationsBits_int();
        propertiesBits_BigInteger();
        compareImplementationsBits_BigInteger();
        propertiesBitsPadded_int_int();
        compareImplementationsBitsPadded_int_int();
        propertiesBitsPadded_int_BigInteger();
        propertiesBigEndianBits_int();
        compareImplementationsBigEndianBits_int();
        propertiesBigEndianBits_BigInteger();
        compareImplementationsBigEndianBits_BigInteger();
        propertiesBigEndianBitsPadded_int_int();
        compareImplementationsBigEndianBitsPadded_int_int();
        propertiesBigEndianBitsPadded_int_BigInteger();
        compareImplementationsBigEndianBitsPadded_int_BigInteger();
        propertiesFromBits();
        compareImplementationsFromBits();
        propertiesFromBigEndianBits();
        compareImplementationsFromBigEndianBits();
        propertiesDigits_int_int();
        compareImplementationsDigits_int_int();
        propertiesDigits_BigInteger_BigInteger();
        compareImplementationsDigits_BigInteger_BigInteger();
        propertiesDigitsPadded_int_int_int();
        compareImplementationsDigitsPadded_int_int_int();
        propertiesDigitsPadded_int_BigInteger_BigInteger();
        compareImplementationsDigitsPadded_int_BigInteger_BigInteger();
        propertiesBigEndianDigits_int_int();
        compareImplementationsBigEndianDigits_int_int();
        propertiesBigEndianDigits_BigInteger_BigInteger();
        propertiesBigEndianDigitsPadded_int_int_int();
        compareImplementationsBigEndianDigitsPadded_int_int_int();
        propertiesBigEndianDigitsPadded_int_BigInteger_BigInteger();
        compareImplementationsBigEndianDigitsPadded_int_BigInteger_BigInteger();
        propertiesFromDigits_int_Iterable_Integer();
        compareImplementationsFromDigits_int_Iterable_Integer();
        propertiesFromDigits_BigInteger_Iterable_BigInteger();
        propertiesFromBigEndianDigits_int_Iterable_Integer();
        compareImplementationsFromBigEndianDigits_int_Iterable_Integer();
        propertiesFromBigEndianDigits_int_Iterable_BigInteger();
        propertiesToDigit();
        propertiesFromDigit();
        propertiesToStringBase_int_int();
        compareImplementationsToStringBase_int_int();
        propertiesToStringBase_BigInteger_BigInteger();
        propertiesFromStringBase_int_String();
        compareImplementationsFromStringBase_int_String();
        propertiesFromStringBase_BigInteger_String();
        propertiesLogarithmicMux();
        propertiesLogarithmicDemux();
        propertiesSquareRootMux();
        compareImplementationsSquareRootMux();
        propertiesSquareRootDemux();
        compareImplementationsSquareRootDemux();
        propertiesSquareRootDemux();
        propertiesMux();
        compareImplementationsMux();
        propertiesDemux();
        compareImplementationsDemux();
    }

    private void propertiesLowestOneBit_int() {
        initialize("lowestOneBit(int)");
        for (int i : take(LIMIT, P.integers())) {
            //noinspection ResultOfMethodCallIgnored
            Integer.lowestOneBit(i);
        }

        for (int i : take(LIMIT, filterInfinite(j -> j != Integer.MIN_VALUE, P.integers()))) {
            int b = Integer.lowestOneBit(i);
            assertTrue(i, b >= 0);
            assertTrue(i, b <= 1 << 30);
            assertEquals(i, Integer.lowestOneBit(-i), b);
        }

        for (int i : take(LIMIT, filterInfinite(j -> j != Integer.MIN_VALUE, P.nonzeroIntegers()))) {
            int b = Integer.lowestOneBit(i);
            assertTrue(i, isPowerOfTwo(b));
            assertNotEquals(i, i & b, 0);
        }
    }

    private void propertiesLowestOneBit_long() {
        initialize("lowestOneBit(long)");
        for (long l : take(LIMIT, P.longs())) {
            //noinspection ResultOfMethodCallIgnored
            Long.lowestOneBit(l);
        }

        for (long l : take(LIMIT, filterInfinite(m -> m != Long.MIN_VALUE, P.longs()))) {
            long b = Long.lowestOneBit(l);
            assertTrue(l, b >= 0);
            assertTrue(l, b <= 1L << 62);
            assertEquals(l, Long.lowestOneBit(-l), b);
        }

        for (long l : take(LIMIT, filterInfinite(m -> m != Long.MIN_VALUE, P.nonzeroLongs()))) {
            long b = Long.lowestOneBit(l);
            assertTrue(l, isPowerOfTwo(b));
            assertNotEquals(l, l & b, 0);
        }
    }

    private void propertiesHighestOneBit_int() {
        initialize("highestOneBit(int)");
        for (int i : take(LIMIT, P.integers())) {
            //noinspection ResultOfMethodCallIgnored
            Integer.highestOneBit(i);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            int b = Integer.highestOneBit(i);
            assertTrue(i, b >= 0);
            assertTrue(i, b <= 1 << 30);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            int b = Integer.highestOneBit(i);
            assertEquals(i, b, Integer.MIN_VALUE);
        }

        for (int i : take(LIMIT, P.positiveIntegers())) {
            int b = Integer.highestOneBit(i);
            assertTrue(i, isPowerOfTwo(b));
            assertNotEquals(i, i & b, 0);
        }
    }

    private void propertiesHighestOneBit_long() {
        initialize("highestOneBit(long)");
        for (long l : take(LIMIT, P.longs())) {
            //noinspection ResultOfMethodCallIgnored
            Long.highestOneBit(l);
        }

        for (long l : take(LIMIT, P.naturalLongs())) {
            long b = Long.highestOneBit(l);
            assertTrue(l, b >= 0);
            assertTrue(l, b <= 1L << 62);
        }

        for (long l : take(LIMIT, P.negativeLongs())) {
            long b = Long.highestOneBit(l);
            assertEquals(l, b, Long.MIN_VALUE);
        }

        for (long l : take(LIMIT, P.positiveLongs())) {
            long b = Long.highestOneBit(l);
            assertTrue(l, isPowerOfTwo(b));
            assertNotEquals(l, l & b, 0);
        }
    }

    private static boolean isPowerOfTwo_int_simplest(int n) {
        return isPowerOfTwo(BigInteger.valueOf(n));
    }

    private static boolean isPowerOfTwo_int_alt(int n) {
        if (n < 1) {
            throw new ArithmeticException("n must be positive. Invalid n: " + n);
        }
        return Integer.lowestOneBit(n) == Integer.highestOneBit(n);
    }

    private void propertiesIsPowerOfTwo_int() {
        initialize("isPowerOfTwo(int)");
        for (int i : take(LIMIT, P.positiveIntegers())) {
            boolean isPowerOfTwo = isPowerOfTwo(i);
            assertEquals(i, isPowerOfTwo, isPowerOfTwo_int_alt(i));
            assertEquals(i, isPowerOfTwo, 1 << ceilingLog2(i) == i);
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                isPowerOfTwo(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsPowerOfTwo_int() {
        Map<String, Function<Integer, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::isPowerOfTwo_int_simplest);
        functions.put("alt", IntegerUtilsProperties::isPowerOfTwo_int_alt);
        functions.put("standard", IntegerUtils::isPowerOfTwo);
        compareImplementations("isPowerOfTwo(int)", take(100000, P.positiveIntegers()), functions, v -> P.reset());
    }

    private static boolean isPowerOfTwo_long_simplest(long n) {
        return isPowerOfTwo(BigInteger.valueOf(n));
    }

    private static boolean isPowerOfTwo_long_alt(long n) {
        if (n < 1L) {
            throw new ArithmeticException("n must be positive. Invalid n: " + n);
        }
        return Long.lowestOneBit(n) == Long.highestOneBit(n);
    }

    private void propertiesIsPowerOfTwo_long() {
        initialize("isPowerOfTwo(long)");
        for (long l : take(LIMIT, P.positiveLongs())) {
            boolean isPowerOfTwo = isPowerOfTwo(l);
            assertEquals(l, isPowerOfTwo, isPowerOfTwo_long_alt(l));
            assertEquals(l, isPowerOfTwo, 1L << ceilingLog2(l) == l);
        }

        for (long l : take(LIMIT, P.rangeDown(0L))) {
            try {
                isPowerOfTwo(l);
                fail(l);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsPowerOfTwo_long() {
        Map<String, Function<Long, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::isPowerOfTwo_long_simplest);
        functions.put("alt", IntegerUtilsProperties::isPowerOfTwo_long_alt);
        functions.put("standard", IntegerUtils::isPowerOfTwo);
        compareImplementations("isPowerOfTwo(long)", take(LIMIT, P.positiveLongs()), functions, v -> P.reset());
    }

    private static boolean isPowerOfTwo_BigInteger_alt(@NotNull BigInteger n) {
        if (n.signum() != 1) {
            throw new ArithmeticException("n must be positive. Invalid n: " + n);
        }
        return n.and(n.subtract(BigInteger.ONE)).equals(BigInteger.ZERO);
    }

    private void propertiesIsPowerOfTwo_BigInteger() {
        initialize("isPowerOfTwo(BigInteger)");
        for (BigInteger i : take(LIMIT, P.positiveBigIntegers())) {
            boolean isPowerOfTwo = isPowerOfTwo(i);
            assertEquals(i, isPowerOfTwo, isPowerOfTwo_BigInteger_alt(i));
            assertEquals(i, isPowerOfTwo, BigInteger.ONE.shiftLeft(ceilingLog2(i)).equals(i));
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.ZERO))) {
            try {
                isPowerOfTwo(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsPowerOfTwo_BigInteger() {
        Map<String, Function<BigInteger, Boolean>> functions = new LinkedHashMap<>();
        functions.put("alt", IntegerUtilsProperties::isPowerOfTwo_BigInteger_alt);
        functions.put("standard", IntegerUtils::isPowerOfTwo);
        compareImplementations(
                "isPowerOfTwo(BigInteger)",
                take(LIMIT, P.positiveBigIntegers()),
                functions,
                v -> P.reset()
        );
    }

    private static int ceilingLog2_int_simplest(int n) {
        return ceilingLog2(BigInteger.valueOf(n));
    }

    private void propertiesCeilingLog2_int() {
        initialize("ceilingLog2(int)");
        for (int i : take(LIMIT, P.positiveIntegers())) {
            int ceilingLog2 = ceilingLog2(i);
            assertTrue(i, ceilingLog2 >= 0);
            assertTrue(i, ceilingLog2 < 32);
            assertTrue(i, ceilingLog2 == 31 || 1 << ceilingLog2 >= i);
            assertTrue(i, 1 << (ceilingLog2 - 1) < i);
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                ceilingLog2(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsCeilingLog2_int() {
        Map<String, Function<Integer, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::ceilingLog2_int_simplest);
        functions.put("standard", IntegerUtils::ceilingLog2);
        compareImplementations("ceilingLog2(int)", take(LIMIT, P.positiveIntegers()), functions, v -> P.reset());
    }

    private static int ceilingLog2_long_simplest(long n) {
        return ceilingLog2(BigInteger.valueOf(n));
    }

    private void propertiesCeilingLog2_long() {
        initialize("ceilingLog2(long)");
        for (long l : take(LIMIT, P.positiveLongs())) {
            int ceilingLog2 = ceilingLog2(l);
            assertTrue(l, ceilingLog2 >= 0);
            assertTrue(l, ceilingLog2 < 64);
            assertTrue(l, ceilingLog2 == 63 || 1L << ceilingLog2 >= l);
            assertTrue(l, 1L << (ceilingLog2 - 1) < l);
        }

        for (long l : take(LIMIT, P.rangeDown(0L))) {
            try {
                ceilingLog2(l);
                fail(l);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsCeilingLog2_long() {
        Map<String, Function<Long, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::ceilingLog2_long_simplest);
        functions.put("standard", IntegerUtils::ceilingLog2);
        compareImplementations("ceilingLog2(long)", take(LIMIT, P.positiveLongs()), functions, v -> P.reset());
    }

    private void propertiesCeilingLog2_BigInteger() {
        initialize("ceilingLog2(BigInteger)");
        for (BigInteger i : take(LIMIT, P.positiveBigIntegers())) {
            int ceilingLog2 = ceilingLog2(i);
            assertTrue(i, ceilingLog2 >= 0);
            assertTrue(i, ge(BigInteger.ONE.shiftLeft(ceilingLog2), i));
            assertTrue(i, lt(BigInteger.ONE.shiftLeft(ceilingLog2 - 1), i));
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.ZERO))) {
            try {
                ceilingLog2(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<Boolean> bits_int_simplest(int n) {
        return bits(BigInteger.valueOf(n));
    }

    private void propertiesBits_int() {
        initialize("bits(int)");
        for (int i : take(LIMIT, P.naturalIntegers())) {
            List<Boolean> bits = bits(i);
            assertEquals(i, bits, bits_int_simplest(i));
            assertEquals(i, bits, reverse(bigEndianBits(i)));
            assertTrue(i, all(Objects::nonNull, bits));
            inverse(IntegerUtils::bits, (List<Boolean> bs) -> fromBits(bs).intValueExact(), i);
            assertEquals(i, bits.size(), BigInteger.valueOf(i).bitLength());
        }

        for (int i : take(LIMIT, P.positiveIntegers())) {
            List<Boolean> bits = bits(i);
            assertFalse(i, bits.isEmpty());
            assertEquals(i, last(bits), true);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                bits(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBits_int() {
        Map<String, Function<Integer, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::bits_int_simplest);
        functions.put("standard", IntegerUtils::bits);
        compareImplementations("bits(int)", take(LIMIT, P.naturalIntegers()), functions, v -> P.reset());
    }

    private static @NotNull List<Boolean> bits_BigInteger_alt(@NotNull BigInteger n) {
        if (n.signum() == -1) {
            throw new ArithmeticException("n cannot be negative. Invalid n: " + n);
        }
        List<Boolean> bits = new ArrayList<>();
        for (BigInteger remaining = n; !remaining.equals(BigInteger.ZERO); remaining = remaining.shiftRight(1)) {
            bits.add(remaining.testBit(0));
        }
        return bits;
    }

    private void propertiesBits_BigInteger() {
        initialize("bits(BigInteger)");
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            List<Boolean> bits = bits(i);
            assertEquals(i, bits, bits_BigInteger_alt(i));
            assertEquals(i, bits, reverse(bigEndianBits(i)));
            assertTrue(i, all(Objects::nonNull, bits));
            inverse(IntegerUtils::bits, IntegerUtils::fromBits, i);
            assertEquals(i, bits.size(), i.bitLength());
        }

        for (BigInteger i : take(LIMIT, P.positiveBigIntegers())) {
            List<Boolean> bits = bits(i);
            assertFalse(i, bits.isEmpty());
            assertEquals(i, last(bits), true);
        }

        for (BigInteger i : take(LIMIT, P.negativeBigIntegers())) {
            try {
                bits(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBits_BigInteger() {
        Map<String, Function<BigInteger, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("alt", IntegerUtilsProperties::bits_BigInteger_alt);
        functions.put("standard", IntegerUtils::bits);
        compareImplementations("bits(BigInteger)", take(LIMIT, P.naturalBigIntegers()), functions, v -> P.reset());
    }

    private static @NotNull List<Boolean> bitsPadded_int_int_simplest(int length, int n) {
        return bitsPadded(length, BigInteger.valueOf(n));
    }

    private void propertiesBitsPadded_int_int() {
        initialize("bitsPadded(int, int)");
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bitsPadded(p.b, p.a);
            assertEquals(p, bits, bitsPadded_int_int_simplest(p.b, p.a));
            assertEquals(p, bits, reverse(bigEndianBitsPadded(p.b, p.a)));
            assertFalse(p, any(Objects::isNull, bits));
            assertEquals(p, bits.size(), p.b);
            for (int i = 32; i < bits.size(); i++) {
                assertFalse(p, bits.get(i));
            }
        }

        ps = map(
                p -> new Pair<>(p.a, BigInteger.valueOf(p.a).bitLength() + p.b),
                P.pairsSquareRootOrder(P.naturalIntegers(), P.naturalIntegersGeometric())
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            inverse(i -> bitsPadded(p.b, i), (List<Boolean> bs) -> fromBits(bs).intValueExact(), p.a);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.naturalIntegers(), P.negativeIntegers()))) {
            try {
                bitsPadded(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {
            }
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.naturalIntegers()))) {
            try {
                bitsPadded(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBitsPadded_int_int() {
        Map<String, Function<Pair<Integer, Integer>, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> bitsPadded_int_int_simplest(p.b, p.a));
        functions.put("standard", p -> bitsPadded(p.b, p.a));
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        compareImplementations("bitsPadded(int, int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesBitsPadded_int_BigInteger() {
        initialize("bitsPadded(int, BigInteger)");
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bitsPadded(p.b, p.a);
            assertEquals(p, bits, reverse(bigEndianBitsPadded(p.b, p.a)));
            assertFalse(p, any(Objects::isNull, bits));
            assertEquals(p, bits.size(), p.b);
        }

        ps = map(
                p -> new Pair<>(p.a, p.a.bitLength() + p.b),
                P.pairsSquareRootOrder(P.naturalBigIntegers(), P.naturalIntegersGeometric())
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            inverse(i -> bitsPadded(p.b, i), IntegerUtils::fromBits, p.a);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.naturalBigIntegers(), P.negativeIntegers()))) {
            try {
                bitsPadded(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {
            }
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.negativeBigIntegers(), P.naturalIntegers()))) {
            try {
                bitsPadded(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<Boolean> bigEndianBits_int_simplest(int n) {
        return bigEndianBits(BigInteger.valueOf(n));
    }

    private static @NotNull List<Boolean> bigEndianBits_int_alt(int n) {
        return reverse(bits(n));
    }

    private void propertiesBigEndianBits_int() {
        initialize("bigEndianBits(int)");
        for (int i : take(LIMIT, P.naturalIntegers())) {
            List<Boolean> bits = bigEndianBits(i);
            assertEquals(i, bits, bigEndianBits_int_simplest(i));
            assertEquals(i, bits, bigEndianBits_int_alt(i));
            assertEquals(i, bits, reverse(bits(i)));
            assertTrue(i, all(Objects::nonNull, bits));
            assertEquals(i, fromBigEndianBits(bits).intValueExact(), i);
            assertEquals(i, bits.size(), BigInteger.valueOf(i).bitLength());
        }

        for (int i : take(LIMIT, P.positiveIntegers())) {
            List<Boolean> bits = bigEndianBits(i);
            assertFalse(i, bits.isEmpty());
            assertTrue(i, head(bits));
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                bigEndianBits(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBigEndianBits_int() {
        Map<String, Function<Integer, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::bigEndianBits_int_simplest);
        functions.put("alt", IntegerUtilsProperties::bigEndianBits_int_alt);
        functions.put("standard", IntegerUtils::bigEndianBits);
        compareImplementations("bigEndianBits(int)", take(LIMIT, P.naturalIntegers()), functions, v -> P.reset());
    }

    private static @NotNull List<Boolean> bigEndianBits_BigInteger_simplest(@NotNull BigInteger n) {
        return reverse(bits(n));
    }

    private void propertiesBigEndianBits_BigInteger() {
        initialize("bigEndianBits(BigInteger)");
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            List<Boolean> bits = bigEndianBits(i);
            assertEquals(i, bits, bigEndianBits_BigInteger_simplest(i));
            assertEquals(i, bits, reverse(bits(i)));
            assertTrue(i, all(Objects::nonNull, bits));
            inverse(IntegerUtils::bigEndianBits, IntegerUtils::fromBigEndianBits, i);
            assertEquals(i, bits.size(), i.bitLength());
        }

        for (BigInteger i : take(LIMIT, P.positiveBigIntegers())) {
            List<Boolean> bits = bigEndianBits(i);
            assertFalse(i, bits.isEmpty());
            assertTrue(i, head(bits));
        }

        for (BigInteger i : take(LIMIT, P.negativeBigIntegers())) {
            try {
                bigEndianBits(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBigEndianBits_BigInteger() {
        Map<String, Function<BigInteger, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::bigEndianBits_BigInteger_simplest);
        functions.put("standard", IntegerUtils::bigEndianBits);
        compareImplementations(
                "bigEndianBits(BigInteger)",
                take(LIMIT, P.naturalBigIntegers()),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull List<Boolean> bigEndianBitsPadded_int_int_simplest(int length, int n) {
        return bigEndianBitsPadded(length, BigInteger.valueOf(n));
    }

    private static @NotNull List<Boolean> bigEndianBitsPadded_int_int_alt(int length, int n) {
        return reverse(bitsPadded(length, n));
    }

    private void propertiesBigEndianBitsPadded_int_int() {
        initialize("bigEndianBitsPadded(int, int)");
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bigEndianBitsPadded(p.b, p.a);
            assertEquals(p, bits, bigEndianBitsPadded_int_int_simplest(p.b, p.a));
            assertEquals(p, bits, bigEndianBitsPadded_int_int_alt(p.b, p.a));
            assertEquals(p, bits, reverse(bitsPadded(p.b, p.a)));
            assertTrue(p, all(Objects::nonNull, bits));
            assertEquals(p, bits.size(), p.b);
            for (int i = 32; i < bits.size(); i++) {
                assertFalse(p, bits.get(bits.size() - i - 1));
            }
        }

        ps = map(
                p -> new Pair<>(p.a, BigInteger.valueOf(p.a).bitLength() + p.b),
                P.pairs(P.naturalIntegers(), P.naturalIntegersGeometric())
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bigEndianBitsPadded(p.b, p.a);
            assertEquals(p, fromBigEndianBits(bits).intValueExact(), p.a);
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.naturalIntegers(), P.negativeIntegers()))) {
            try {
                bigEndianBitsPadded(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.naturalIntegers()))) {
            try {
                bigEndianBitsPadded(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBigEndianBitsPadded_int_int() {
        Map<String, Function<Pair<Integer, Integer>, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> bigEndianBitsPadded_int_int_simplest(p.b, p.a));
        functions.put("alt", p -> bigEndianBitsPadded_int_int_alt(p.b, p.a));
        functions.put("standard", p -> bigEndianBitsPadded(p.b, p.a));
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        compareImplementations("bigEndianBitsPadded(int, int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull List<Boolean> bigEndianBitsPadded_int_BigInteger_alt(int length, @NotNull BigInteger n) {
        return reverse(bitsPadded(length, n));
    }

    private void propertiesBigEndianBitsPadded_int_BigInteger() {
        initialize("bigEndianBitsPadded(int, BigInteger)");
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bigEndianBitsPadded(p.b, p.a);
            assertEquals(p, bits, bigEndianBitsPadded_int_BigInteger_alt(p.b, p.a));
            assertEquals(p, bits, reverse(bitsPadded(p.b, p.a)));
            assertTrue(p, all(Objects::nonNull, bits));
            assertEquals(p, bits.size(), p.b);
        }

        ps = map(
                p -> new Pair<>(p.a, p.a.bitLength() + p.b),
                P.pairs(P.naturalBigIntegers(), P.naturalIntegersGeometric())
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<Boolean> bits = bigEndianBitsPadded(p.b, p.a);
            assertEquals(p, fromBigEndianBits(bits), p.a);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.naturalBigIntegers(), P.negativeIntegers()))) {
            try {
                bigEndianBitsPadded(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.negativeBigIntegers(), P.naturalIntegers()))) {
            try {
                bigEndianBitsPadded(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBigEndianBitsPadded_int_BigInteger() {
        Map<String, Function<Pair<BigInteger, Integer>, List<Boolean>>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> bigEndianBitsPadded_int_BigInteger_alt(p.b, p.a));
        functions.put("standard", p -> bigEndianBitsPadded(p.b, p.a));
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                P.naturalIntegersGeometric()
        );
        compareImplementations("bigEndianBitsPadded(int, BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull BigInteger fromBits_alt(@NotNull List<Boolean> xs) {
        BigInteger n = BigInteger.ZERO;
        for (int i : select(xs, ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0))) {
            n = n.setBit(i);
        }
        return n;
    }

    private void propertiesFromBits() {
        initialize("fromBits(List<Boolean>)");
        for (List<Boolean> bs : take(LIMIT, P.lists(P.booleans()))) {
            BigInteger i = fromBits(bs);
            assertEquals(bs, fromBits_alt(bs), i);
            assertTrue(bs, i.signum() != -1);
            assertEquals(bs, i, fromBigEndianBits(reverse(bs)));
        }

        Iterable<List<Boolean>> bss = map(
                xs -> toList(concat(xs, Collections.singletonList(true))),
                P.lists(P.booleans())
        );
        for (List<Boolean> bs : take(LIMIT, bss)) {
            BigInteger i = fromBits(bs);
            aeqit(bs, bs, bits(i));
        }

        for (List<Boolean> bs : take(LIMIT, P.listsWithElement(null, P.booleans()))) {
            try {
                fromBits(bs);
                fail(bs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsFromBits() {
        Map<String, Function<List<Boolean>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("alt", IntegerUtilsProperties::fromBits_alt);
        functions.put("standard", IntegerUtils::fromBits);
        compareImplementations(
                "fromBits(List<Boolean>)",
                take(LIMIT, P.lists(P.booleans())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull BigInteger fromBigEndianBits_simplest(@NotNull Iterable<Boolean> xs) {
        return fromBits(reverse(xs));
    }

    private void propertiesFromBigEndianBits() {
        initialize("fromBigEndianBits(List<Boolean>)");
        for (List<Boolean> bs : take(LIMIT, P.lists(P.booleans()))) {
            BigInteger i = fromBigEndianBits(bs);
            assertEquals(bs, fromBigEndianBits_simplest(bs), i);
            assertTrue(bs, i.signum() != -1);
            assertEquals(bs, i, fromBits(reverse(bs)));
        }

        for (List<Boolean> bs : take(LIMIT, map(xs -> toList(cons(true, xs)), P.lists(P.booleans())))) {
            BigInteger i = fromBigEndianBits(bs);
            aeqit(bs, bs, bigEndianBits(i));
        }

        for (List<Boolean> bs : take(LIMIT, P.listsWithElement(null, P.booleans()))) {
            try {
                fromBigEndianBits(bs);
                fail(bs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsFromBigEndianBits() {
        Map<String, Function<List<Boolean>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("simplest", IntegerUtilsProperties::fromBigEndianBits_simplest);
        functions.put("standard", IntegerUtils::fromBigEndianBits);
        compareImplementations(
                "fromBigEndianBits(List<Boolean>)",
                take(LIMIT, P.lists(P.booleans())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull List<Integer> digits_int_int_simplest(int base, int n) {
        return toList(map(BigInteger::intValue, digits(BigInteger.valueOf(base), BigInteger.valueOf(n))));
    }

    private static @NotNull List<Integer> digits_int_int_alt(int base, int n) {
        if (base < 2) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        if (n < 0) {
            throw new ArithmeticException("n cannot be negative. Invalid n: " + n);
        }
        List<Integer> digits = new ArrayList<>();
        int remaining = n;
        while (remaining != 0) {
            digits.add(remaining % base);
            remaining /= base;
        }
        return digits;
    }

    private void propertiesDigits_int_int() {
        initialize("digits(int, int)");
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = digits(p.b, p.a);
            assertEquals(p, digits, digits_int_int_simplest(p.b, p.a));
            assertEquals(p, digits, digits_int_int_alt(p.b, p.a));
            assertEquals(p, digits, reverse(bigEndianDigits(p.b, p.a)));
            assertTrue(p, all(i -> i >= 0 && i < p.b, digits));
            inverse(i -> digits(p.b, i), (List<Integer> is) -> fromDigits(p.b, is).intValueExact(), p.a);
        }

        ps = P.pairsSquareRootOrder(P.positiveIntegers(), P.rangeUpGeometric(2));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = toList(digits(p.b, p.a));
            assertFalse(p, digits.isEmpty());
            assertNotEquals(p, last(digits), 0);
            int targetDigitCount = ceilingLog(BigInteger.valueOf(p.b), BigInteger.valueOf(p.a));
            if (BigInteger.valueOf(p.b).pow(targetDigitCount).equals(BigInteger.valueOf(p.a))) {
                targetDigitCount++;
            }
            assertEquals(p, digits.size(), targetDigitCount);
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };
        for (int i : take(LIMIT, P.naturalIntegers())) {
            List<Integer> digits = toList(digits(2, i));
            aeqit(i, map(digitsToBits, digits), bits(i));
        }

        for (int i : take(LIMIT, P.rangeUp(2))) {
            assertTrue(i, isEmpty(digits(i, 0)));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.naturalIntegers(), P.rangeDown(1)))) {
            try {
                digits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.rangeUp(2)))) {
            try {
                digits(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDigits_int_int() {
        Map<String, Function<Pair<Integer, Integer>, List<Integer>>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> digits_int_int_simplest(p.b, p.a));
        functions.put("alt", p -> digits_int_int_alt(p.b, p.a));
        functions.put("standard", p -> digits(p.b, p.a));
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        compareImplementations("digits(int, int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull List<BigInteger> digits_BigInteger_BigInteger_alt(
            @NotNull BigInteger base,
            @NotNull BigInteger n
    ) {
        if (lt(base, TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        if (n.signum() == -1) {
            throw new ArithmeticException("n cannot be negative. Invalid n: " + n);
        }
        List<BigInteger> digits = new ArrayList<>();
        BigInteger remaining = n;
        while (!remaining.equals(BigInteger.ZERO)) {
            digits.add(remaining.mod(base));
            remaining = remaining.divide(base);
        }
        return digits;
    }

    private static @NotNull List<BigInteger> digits_BigInteger_BigInteger_alt2(
            @NotNull BigInteger base,
            @NotNull BigInteger n
    ) {
        if (lt(base, TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        if (n.signum() == -1) {
            throw new ArithmeticException("n cannot be negative. Invalid n: " + n);
        }
        List<BigInteger> digits = new ArrayList<>();
        if (isPowerOfTwo(base)) {
            int log = ceilingLog2(base);
            BigInteger mask = base.subtract(BigInteger.ONE);
            int shift = 0;
            int bitLength = n.bitLength();
            while (shift < bitLength) {
                digits.add(n.and(mask).shiftRight(shift));
                mask = mask.shiftLeft(log);
                shift += log;
            }
        } else {
            BigInteger remaining = n;
            while (!remaining.equals(BigInteger.ZERO)) {
                digits.add(remaining.mod(base));
                remaining = remaining.divide(base);
            }
        }
        return digits;
    }

    private void propertiesDigits_BigInteger_BigInteger() {
        initialize("digits(BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> digits = digits(p.b, p.a);
            assertEquals(p, digits, digits_BigInteger_BigInteger_alt(p.b, p.a));
            assertEquals(p, digits, digits_BigInteger_BigInteger_alt2(p.b, p.a));
            assertEquals(p, digits, reverse(bigEndianDigits(p.b, p.a)));
            assertTrue(p, all(i -> i.signum() != -1 && lt(i, p.b), digits));
            inverse(i -> digits(p.b, i), (List<BigInteger> is) -> fromDigits(p.b, is), p.a);
        }

        //noinspection Convert2MethodRef
        ps = P.pairsSquareRootOrder(P.positiveBigIntegers(), map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> digits = toList(digits(p.b, p.a));
            assertFalse(p, digits.isEmpty());
            assertNotEquals(p, last(digits), BigInteger.ZERO);
            int targetDigitCount = ceilingLog(p.b, p.a);
            if (p.b.pow(targetDigitCount).equals(p.a)) {
                targetDigitCount++;
            }
            assertEquals(p, digits.size(), targetDigitCount);
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) return false;
            if (i.equals(BigInteger.ONE)) return true;
            throw new IllegalArgumentException();
        };
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            List<BigInteger> digits = toList(digits(TWO, i));
            aeqit(i, map(digitsToBits, digits), bits(i));
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(TWO))) {
            assertTrue(i, isEmpty(digits(i, BigInteger.ZERO)));
        }

        Iterable<Pair<BigInteger, BigInteger>> psFail = P.pairs(P.naturalBigIntegers(), P.rangeDown(NEGATIVE_ONE));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, psFail)) {
            try {
                digits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.pairs(P.negativeBigIntegers(), P.rangeUp(TWO));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, psFail)) {
            try {
                digits(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDigits_BigInteger_BigInteger() {
        Map<String, Function<Pair<BigInteger, BigInteger>, List<BigInteger>>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> digits_BigInteger_BigInteger_alt(p.b, p.a));
        functions.put("alt2", p -> digits_BigInteger_BigInteger_alt2(p.b, p.a));
        functions.put("standard", p -> digits(p.b, p.a));
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        compareImplementations("digits(BigInteger, BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull List<Integer> digitsPadded_int_int_int_simplest(int length, int base, int n) {
        return toList(
                map(BigInteger::intValue, digitsPadded(length, BigInteger.valueOf(base), BigInteger.valueOf(n)))
        );
    }

    private static @NotNull List<Integer> digitsPadded_int_int_int_alt(int length, int base, int n) {
        return toList(pad(0, length, digits(base, n)));
    }

    private void propertiesDigitsPadded_int_int_int() {
        initialize("digitsPadded(int, int, int)");
        Iterable<Triple<Integer, Integer, Integer>> ts = P.triples(
                P.naturalIntegersGeometric(),
                P.rangeUpGeometric(2),
                P.naturalIntegers()
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            List<Integer> digits = digitsPadded(t.a, t.b, t.c);
            assertEquals(t, digits, digitsPadded_int_int_int_simplest(t.a, t.b, t.c));
            assertEquals(t, digits, digitsPadded_int_int_int_alt(t.a, t.b, t.c));
            assertEquals(t, digits, reverse(bigEndianDigitsPadded(t.a, t.b, t.c)));
            assertTrue(t, all(i -> i >= 0 && i < t.b, digits));
            assertEquals(t, digits.size(), t.a);
        }

        ts = map(
                q -> new Triple<>(q.b, q.a.b, q.a.a),
                P.dependentPairs(
                        P.pairsLogarithmicOrder(P.naturalIntegers(), P.rangeUpGeometric(2)),
                        p -> {
                            int targetDigitCount = 0;
                            if (p.a > 0) {
                                targetDigitCount = ceilingLog(BigInteger.valueOf(p.b), BigInteger.valueOf(p.a));
                                BigInteger x = BigInteger.valueOf(p.b).pow(targetDigitCount);
                                BigInteger y = BigInteger.valueOf(p.a);
                                //noinspection SuspiciousNameCombination
                                if (x.equals(y)) {
                                    targetDigitCount++;
                                }
                            }
                            return P.rangeUpGeometric(targetDigitCount);
                        }
                )
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            List<Integer> digits = digitsPadded(t.a, t.b, t.c);
            assertEquals(t, fromDigits(t.b, digits).intValueExact(), t.c);
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };

        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = digitsPadded(p.b, 2, p.a);
            aeqit(p, map(digitsToBits, digits), bitsPadded(p.b, p.a));
        }

        Iterable<Triple<Integer, Integer, Integer>> tsFail = P.triples(
                P.naturalIntegers(),
                P.rangeUp(2),
                P.negativeIntegers()
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(P.negativeIntegers(), P.rangeUp(2), P.naturalIntegers());
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(P.naturalIntegers(), P.negativeIntegers(), P.negativeIntegers());
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsDigitsPadded_int_int_int() {
        Map<String, Function<Triple<Integer, Integer, Integer>, List<Integer>>> functions = new LinkedHashMap<>();
        functions.put("simplest", t -> digitsPadded_int_int_int_simplest(t.a, t.b, t.c));
        functions.put("alt", t -> digitsPadded_int_int_int_alt(t.a, t.b, t.c));
        functions.put("standard", t -> digitsPadded(t.a, t.b, t.c));
        Iterable<Triple<Integer, Integer, Integer>> ts = P.triples(
                P.naturalIntegersGeometric(),
                P.rangeUpGeometric(2),
                P.naturalIntegers()
        );
        compareImplementations("digitsPadded(int, int, int)", take(LIMIT, ts), functions, v -> P.reset());
    }

    private static @NotNull List<BigInteger> digitsPadded_int_BigInteger_BigInteger_alt(
            int length,
            @NotNull BigInteger base,
            @NotNull BigInteger n
    ) {
        return toList(pad(BigInteger.ZERO, length, digits(base, n)));
    }

    private void propertiesDigitsPadded_int_BigInteger_BigInteger() {
        initialize("digitsPadded(int, BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Triple<Integer, BigInteger, BigInteger>> ts = P.triples(
                P.naturalIntegersGeometric(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.naturalBigIntegers()
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            List<BigInteger> digits = digitsPadded(t.a, t.b, t.c);
            assertEquals(t, digits, digitsPadded_int_BigInteger_BigInteger_alt(t.a, t.b, t.c));
            assertEquals(t, digits, reverse(bigEndianDigitsPadded(t.a, t.b, t.c)));
            assertTrue(t, all(i -> i.signum() != -1 && lt(i, t.b), digits));
            assertEquals(t, digits.size(), t.a);
        }

        //noinspection Convert2MethodRef
        ts = map(
                q -> new Triple<>(q.b, q.a.b, q.a.a),
                P.dependentPairs(
                        P.pairsLogarithmicOrder(
                                P.naturalBigIntegers(),
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                        ),
                        p -> {
                            int targetDigitCount = 0;
                            if (p.a.signum() == 1) {
                                targetDigitCount = ceilingLog(p.b, p.a);
                                if (p.b.pow(targetDigitCount).equals(p.a)) {
                                    targetDigitCount++;
                                }
                            }
                            return P.withScale(targetDigitCount + 1).rangeUpGeometric(targetDigitCount);
                        }
                )
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            List<BigInteger> digits = digitsPadded(t.a, t.b, t.c);
            assertEquals(t, fromDigits(t.b, digits), t.c);
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) {
                return false;
            } else if (i.equals(BigInteger.ONE)) {
                return true;
            } else {
                throw new IllegalArgumentException();
            }
        };

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<BigInteger> digits = digitsPadded(p.b, IntegerUtils.TWO, p.a);
            aeqit(p, map(digitsToBits, digits), bitsPadded(p.b, p.a));
        }

        //noinspection Convert2MethodRef
        Iterable<Triple<Integer, BigInteger, BigInteger>> tsFail = P.triples(
                P.naturalIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.negativeBigIntegers()
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = P.triples(
                P.negativeIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.naturalBigIntegers()
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(P.naturalIntegers(), P.negativeBigIntegers(), P.negativeBigIntegers());
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                digitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsDigitsPadded_int_BigInteger_BigInteger() {
        Map<String, Function<Triple<Integer, BigInteger, BigInteger>, List<BigInteger>>> functions =
                new LinkedHashMap<>();
        functions.put("alt", t -> digitsPadded_int_BigInteger_BigInteger_alt(t.a, t.b, t.c));
        functions.put("standard", t -> digitsPadded(t.a, t.b, t.c));
        //noinspection Convert2MethodRef
        Iterable<Triple<Integer, BigInteger, BigInteger>> ts = P.triples(
                P.naturalIntegersGeometric(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.naturalBigIntegers()
        );
        compareImplementations(
                "digitsPadded(int, BigInteger, BigInteger)",
                take(LIMIT, ts),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull List<Integer> bigEndianDigits_int_int_simplest(int base, int n) {
        return toList(map(BigInteger::intValue, bigEndianDigits(BigInteger.valueOf(base), BigInteger.valueOf(n))));
    }

    private void propertiesBigEndianDigits_int_int() {
        initialize("bigEndianDigits(int, int)");
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = bigEndianDigits(p.b, p.a);
            assertEquals(p, digits, bigEndianDigits_int_int_simplest(p.b, p.a));
            assertEquals(p, digits, reverse(digits(p.b, p.a)));
            assertTrue(p, all(i -> i >= 0 && i < p.b, digits));
            inverse(
                    i -> bigEndianDigits(p.b, i),
                    (List<Integer> is) -> fromBigEndianDigits(p.b, is).intValueExact(),
                    p.a
            );
        }

        ps = P.pairsSquareRootOrder(P.positiveIntegers(), P.rangeUpGeometric(2));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = bigEndianDigits(p.b, p.a);
            assertFalse(p, digits.isEmpty());
            assertNotEquals(p, head(digits), 0);
            int targetDigitCount = ceilingLog(BigInteger.valueOf(p.b), BigInteger.valueOf(p.a));
            if (BigInteger.valueOf(p.b).pow(targetDigitCount).equals(BigInteger.valueOf(p.a))) {
                targetDigitCount++;
            }
            assertEquals(p, digits.size(), targetDigitCount);
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };
        for (int i : take(LIMIT, P.naturalIntegers())) {
            List<Integer> digits = bigEndianDigits(2, i);
            aeqit(i, map(digitsToBits, digits), bigEndianBits(i));
        }

        for (int i : take(LIMIT, P.rangeUp(2))) {
            assertTrue(i, isEmpty(bigEndianDigits(i, 0)));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.naturalIntegers(), P.rangeDown(1)))) {
            try {
                bigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.negativeIntegers(), P.rangeUp(2)))) {
            try {
                bigEndianDigits(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsBigEndianDigits_int_int() {
        Map<String, Function<Pair<Integer, Integer>, List<Integer>>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> bigEndianDigits_int_int_simplest(p.b, p.a));
        functions.put("standard", p -> bigEndianDigits(p.b, p.a));
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        compareImplementations("bigEndianDigits(int, int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesBigEndianDigits_BigInteger_BigInteger() {
        initialize("bigEndianDigits(BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> digits = bigEndianDigits(p.b, p.a);
            assertEquals(p, digits, reverse(digits(p.b, p.a)));
            assertTrue(p, all(i -> i.signum() != -1 && lt(i, p.b), digits));
            inverse(i -> bigEndianDigits(p.b, i), (List<BigInteger> is) -> fromBigEndianDigits(p.b, is), p.a);
        }

        //noinspection Convert2MethodRef
        ps = P.pairsSquareRootOrder(P.positiveBigIntegers(), map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            List<BigInteger> digits = bigEndianDigits(p.b, p.a);
            assertFalse(p, digits.isEmpty());
            assertNotEquals(p, head(digits), BigInteger.ZERO);
            int targetDigitCount = ceilingLog(p.b, p.a);
            if (p.b.pow(targetDigitCount).equals(p.a)) {
                targetDigitCount++;
            }
            assertEquals(p, digits.size(), targetDigitCount);
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) return false;
            if (i.equals(BigInteger.ONE)) return true;
            throw new IllegalArgumentException();
        };
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            List<BigInteger> digits = bigEndianDigits(TWO, i);
            aeqit(i, map(digitsToBits, digits), bigEndianBits(i));
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(TWO))) {
            assertTrue(i, isEmpty(bigEndianDigits(i, BigInteger.ZERO)));
        }

        Iterable<Pair<BigInteger, BigInteger>> psFail = P.pairs(P.naturalBigIntegers(), P.rangeDown(BigInteger.ONE));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, psFail)) {
            try {
                bigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.pairs(P.negativeBigIntegers(), P.rangeUp(TWO));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, psFail)) {
            try {
                bigEndianDigits(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<Integer> bigEndianDigitsPadded_int_int_int_simplest(int length, int base, int n) {
        return toList(
                map(
                        BigInteger::intValue,
                        bigEndianDigitsPadded(length, BigInteger.valueOf(base), BigInteger.valueOf(n))
                )
        );
    }

    private static @NotNull List<Integer> bigEndianDigitsPadded_int_int_int_alt(int length, int base, int n) {
        return reverse(digitsPadded(length, base, n));
    }

    private void propertiesBigEndianDigitsPadded_int_int_int() {
        initialize("bigEndianDigitsPadded(int, int, int)");
        Iterable<Triple<Integer, Integer, Integer>> ts = P.triples(
                P.naturalIntegersGeometric(),
                P.rangeUpGeometric(2),
                P.naturalIntegers()
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            List<Integer> digits = bigEndianDigitsPadded(t.a, t.b, t.c);
            assertEquals(t, digits, bigEndianDigitsPadded_int_int_int_simplest(t.a, t.b, t.c));
            assertEquals(t, digits, bigEndianDigitsPadded_int_int_int_alt(t.a, t.b, t.c));
            assertEquals(t, digits, reverse(digitsPadded(t.a, t.b, t.c)));
            assertTrue(t, all(i -> i >= 0 && i < t.b, digits));
            assertEquals(t, digits.size(), t.a);
        }

        ts = map(
                q -> new Triple<>(q.b, q.a.b, q.a.a),
                P.dependentPairs(
                        P.pairsLogarithmicOrder(P.naturalIntegers(), P.rangeUpGeometric(2)),
                        p -> {
                            int targetDigitCount = 0;
                            if (p.a > 0) {
                                targetDigitCount = ceilingLog(BigInteger.valueOf(p.b), BigInteger.valueOf(p.a));
                                BigInteger x = BigInteger.valueOf(p.b).pow(targetDigitCount);
                                BigInteger y = BigInteger.valueOf(p.a);
                                //noinspection SuspiciousNameCombination
                                if (x.equals(y)) {
                                    targetDigitCount++;
                                }
                            }
                            return P.rangeUpGeometric(targetDigitCount);
                        }
                )
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, ts)) {
            List<Integer> digits = bigEndianDigitsPadded(t.a, t.b, t.c);
            assertEquals(t, fromBigEndianDigits(t.b, digits).intValueExact(), t.c);
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };

        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            List<Integer> digits = bigEndianDigitsPadded(p.b, 2, p.a);
            aeqit(p, map(digitsToBits, digits), bigEndianBitsPadded(p.b, p.a));
        }

        Iterable<Triple<Integer, Integer, Integer>> tsFail = P.triples(
                P.naturalIntegers(),
                P.rangeUp(2),
                P.negativeIntegers()
        );
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(P.negativeIntegers(), P.rangeUp(2), P.naturalIntegers());
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(P.naturalIntegers(), P.rangeDown(1), P.naturalIntegers());
        for (Triple<Integer, Integer, Integer> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsBigEndianDigitsPadded_int_int_int() {
        Map<String, Function<Triple<Integer, Integer, Integer>, List<Integer>>> functions = new LinkedHashMap<>();
        functions.put("simplest", t -> bigEndianDigitsPadded_int_int_int_simplest(t.a, t.b, t.c));
        functions.put("alt", t -> bigEndianDigitsPadded_int_int_int_alt(t.a, t.b, t.c));
        functions.put("standard", t -> bigEndianDigitsPadded(t.a, t.b, t.c));
        Iterable<Triple<Integer, Integer, Integer>> ts = P.triples(
                P.naturalIntegersGeometric(),
                P.rangeUpGeometric(2),
                P.naturalIntegers()
        );
        compareImplementations("bigEndianDigitsPadded(int, int, int)", take(LIMIT, ts), functions, v -> P.reset());
    }

    private static @NotNull List<BigInteger> bigEndianDigitsPadded_int_BigInteger_BigInteger_alt(
            int length,
            @NotNull BigInteger base,
            @NotNull BigInteger n
    ) {
        return reverse(digitsPadded(length, base, n));
    }

    private void propertiesBigEndianDigitsPadded_int_BigInteger_BigInteger() {
        initialize("bigEndianDigitsPadded(int, BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Triple<Integer, BigInteger, BigInteger>> ts = P.triples(
                P.naturalIntegersGeometric(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.naturalBigIntegers()
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            List<BigInteger> digits = bigEndianDigitsPadded(t.a, t.b, t.c);
            assertEquals(t, digits, bigEndianDigitsPadded_int_BigInteger_BigInteger_alt(t.a, t.b, t.c));
            assertEquals(t, digits, reverse(digitsPadded(t.a, t.b, t.c)));
            assertTrue(t, all(i -> i != null && i.signum() != -1 && lt(i, t.b), digits));
            assertEquals(t, digits.size(), t.a);
        }

        //noinspection Convert2MethodRef
        ts = map(
                q -> new Triple<>(q.b, q.a.b, q.a.a),
                P.dependentPairs(
                        P.pairsLogarithmicOrder(
                                P.naturalBigIntegers(),
                                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                        ),
                        p -> {
                            int targetDigitCount = 0;
                            if (p.a.signum() == 1) {
                                targetDigitCount = ceilingLog(p.b, p.a);
                                if (p.b.pow(targetDigitCount).equals(p.a)) {
                                    targetDigitCount++;
                                }
                            }
                            return P.withScale(targetDigitCount + 1).rangeUpGeometric(targetDigitCount);
                        }
                )
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            List<BigInteger> digits = bigEndianDigitsPadded(t.a, t.b, t.c);
            assertEquals(t, fromBigEndianDigits(t.b, digits), t.c);
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) return false;
            if (i.equals(BigInteger.ONE)) return true;
            throw new IllegalArgumentException();
        };

        Iterable<Pair<BigInteger, Integer>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<BigInteger> digits = bigEndianDigitsPadded(p.b, TWO, p.a);
            aeqit(p, map(digitsToBits, digits), bigEndianBitsPadded(p.b, p.a));
        }

        Iterable<Triple<Integer, BigInteger, BigInteger>> tsFail = P.triples(
                P.naturalIntegers(),
                P.rangeUp(TWO),
                P.negativeBigIntegers()
        );
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        tsFail = P.triples(P.negativeIntegers(), P.rangeUp(TWO), P.naturalBigIntegers());
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = P.triples(P.naturalIntegers(), P.rangeDown(BigInteger.ONE), P.naturalBigIntegers());
        for (Triple<Integer, BigInteger, BigInteger> t : take(LIMIT, tsFail)) {
            try {
                bigEndianDigitsPadded(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsBigEndianDigitsPadded_int_BigInteger_BigInteger() {
        Map<String, Function<Triple<Integer, BigInteger, BigInteger>, List<BigInteger>>> functions =
                new LinkedHashMap<>();
        functions.put("alt", t -> bigEndianDigitsPadded_int_BigInteger_BigInteger_alt(t.a, t.b, t.c));
        functions.put("standard", t -> bigEndianDigitsPadded(t.a, t.b, t.c));
        //noinspection Convert2MethodRef
        Iterable<Triple<Integer, BigInteger, BigInteger>> ts = P.triples(
                P.naturalIntegersGeometric(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.naturalBigIntegers()
        );
        compareImplementations(
                "bigEndianDigitsPadded(int, BigInteger, BigInteger)",
                take(LIMIT, ts),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull BigInteger fromDigits_int_Iterable_Integer_simplest(
            int base,
            @NotNull Iterable<Integer> digits
    ) {
        //noinspection Convert2MethodRef
        return fromDigits(BigInteger.valueOf(base), map(i -> BigInteger.valueOf(i), digits));
    }

    private void propertiesFromDigits_int_Iterable_Integer() {
        initialize("fromDigits(int, Iterable<Integer>)");
        Iterable<Pair<List<Integer>, Integer>> ps = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, ps)) {
            BigInteger n = fromDigits(p.b, p.a);
            assertEquals(p, n, fromDigits_int_Iterable_Integer_simplest(p.b, p.a));
            assertEquals(p, n, fromBigEndianDigits(p.b, reverse(p.a)));
            assertNotEquals(p, n.signum(), -1);
        }

        ps = filterInfinite(
                p -> (p.a.isEmpty() || last(p.a) != 0) && all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, ps)) {
            BigInteger n = fromDigits(p.b, p.a);
            aeqit(p, p.a, map(BigInteger::intValueExact, digits(BigInteger.valueOf(p.b), n)));
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };
        for (List<Integer> is : take(LIMIT, P.lists(P.range(0, 1)))) {
            assertEquals(is, fromDigits(2, is), fromBits(toList(map(digitsToBits, is))));
        }

        Iterable<Pair<List<Integer>, Integer>> psFail = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairs(P.lists(P.integers()), P.rangeDown(1))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> i < 0, p.a), P.pairs(P.lists(P.integers()), P.rangeDown(1)));
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> i >= p.b, p.a), P.pairs(P.lists(P.integers()), P.rangeDown(1)));
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsFromDigits_int_Iterable_Integer() {
        Map<String, Function<Pair<List<Integer>, Integer>, BigInteger>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> fromDigits_int_Iterable_Integer_simplest(p.b, p.a));
        functions.put("standard", p -> fromDigits(p.b, p.a));
        Iterable<Pair<List<Integer>, Integer>> ps = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        compareImplementations("fromDigits(int, Iterable<Integer>)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesFromDigits_BigInteger_Iterable_BigInteger() {
        initialize("fromDigits(BigInteger, Iterable<BigInteger>)");
        //noinspection Convert2MethodRef
        Iterable<Pair<List<BigInteger>, BigInteger>> ps = filterInfinite(
                p -> all(i -> lt(i, p.b), p.a),
                P.pairsLogarithmicOrder(
                        P.lists(map(i -> BigInteger.valueOf(i), P.naturalIntegersGeometric())),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, ps)) {
            BigInteger n = fromDigits(p.b, p.a);
            assertEquals(p, n, fromBigEndianDigits(p.b, reverse(p.a)));
            assertNotEquals(p, n.signum(), -1);
        }

        ps = filterInfinite(p -> p.a.isEmpty() || !last(p.a).equals(BigInteger.ZERO), ps);
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, ps)) {
            BigInteger n = fromDigits(p.b, p.a);
            assertEquals(p, p.a, digits(p.b, n));
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) return false;
            if (i.equals(BigInteger.ONE)) return true;
            throw new IllegalArgumentException();
        };
        for (List<BigInteger> is : take(LIMIT, P.lists(P.range(BigInteger.ZERO, BigInteger.ONE)))) {
            assertEquals(is, fromDigits(TWO, is), fromBits(toList(map(digitsToBits, is))));
        }

        Iterable<Pair<List<BigInteger>, BigInteger>> psFail = filterInfinite(
                p -> all(i -> lt(i, p.b), p.a),
                P.pairs(
                        P.lists(P.bigIntegers()),
                        P.rangeDown(BigInteger.ONE)
                )
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(
                p -> any(i -> i.signum() == -1, p.a),
                P.pairs(P.lists(P.bigIntegers()), P.rangeUp(TWO))
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> ge(i, p.b), p.a), P.pairs(P.lists(P.bigIntegers()), P.rangeUp(TWO)));
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull BigInteger fromBigEndianDigits_int_Iterable_Integer_simplest(
            int base,
            @NotNull Iterable<Integer> digits
    ) {
        //noinspection Convert2MethodRef
        return fromBigEndianDigits(BigInteger.valueOf(base), map(i -> BigInteger.valueOf(i), digits));
    }

    private void propertiesFromBigEndianDigits_int_Iterable_Integer() {
        initialize("fromBigEndianDigits(int, Iterable<Integer>)");
        Iterable<Pair<List<Integer>, Integer>> ps = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, ps)) {
            BigInteger n = fromBigEndianDigits(p.b, p.a);
            assertEquals(p, n, fromBigEndianDigits_int_Iterable_Integer_simplest(p.b, p.a));
            assertEquals(p, n, fromDigits(p.b, reverse(p.a)));
            assertNotEquals(p, n.signum(), -1);
        }

        ps = filterInfinite(
                p -> (p.a.isEmpty() || head(p.a) != 0) && all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, ps)) {
            BigInteger n = fromBigEndianDigits(p.b, p.a);
            aeqit(p, p.a, map(BigInteger::intValueExact, bigEndianDigits(BigInteger.valueOf(p.b), n)));
        }

        Function<Integer, Boolean> digitsToBits = i -> {
            switch (i) {
                case 0: return false;
                case 1: return true;
                default: throw new IllegalArgumentException();
            }
        };
        for (List<Integer> is : take(LIMIT, P.lists(P.range(0, 1)))) {
            assertEquals(is, fromBigEndianDigits(2, is), fromBigEndianBits(toList(map(digitsToBits, is))));
        }

        Iterable<Pair<List<Integer>, Integer>> psFail = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairs(P.lists(P.integers()), P.rangeDown(1))
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> i < 0, p.a), P.pairs(P.lists(P.integers()), P.rangeDown(1)));
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> i >= p.b, p.a), P.pairs(P.lists(P.integers()), P.rangeDown(1)));
        for (Pair<List<Integer>, Integer> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsFromBigEndianDigits_int_Iterable_Integer() {
        Map<String, Function<Pair<List<Integer>, Integer>, BigInteger>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> fromBigEndianDigits_int_Iterable_Integer_simplest(p.b, p.a));
        functions.put("standard", p -> fromBigEndianDigits(p.b, p.a));
        Iterable<Pair<List<Integer>, Integer>> ps = filterInfinite(
                p -> all(i -> i < p.b, p.a),
                P.pairsLogarithmicOrder(P.lists(P.naturalIntegersGeometric()), P.rangeUpGeometric(2))
        );
        compareImplementations(
                "fromBigEndianDigits(int, Iterable<Integer>)",
                take(LIMIT, ps),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesFromBigEndianDigits_int_Iterable_BigInteger() {
        initialize("fromBigEndianDigits(BigInteger, Iterable<BigInteger>)");
        //noinspection Convert2MethodRef
        Iterable<Pair<List<BigInteger>, BigInteger>> ps = filterInfinite(
                p -> all(i -> lt(i, p.b), p.a),
                P.pairsLogarithmicOrder(
                        P.lists(map(i -> BigInteger.valueOf(i), P.naturalIntegersGeometric())),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, ps)) {
            BigInteger n = fromBigEndianDigits(p.b, p.a);
            assertEquals(p, n, fromDigits(p.b, reverse(p.a)));
            assertNotEquals(p, n.signum(), -1);
        }

        ps = filterInfinite(p -> p.a.isEmpty() || !head(p.a).equals(BigInteger.ZERO), ps);
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, ps)) {
            BigInteger n = fromBigEndianDigits(p.b, p.a);
            assertEquals(p, p.a, bigEndianDigits(p.b, n));
        }

        Function<BigInteger, Boolean> digitsToBits = i -> {
            if (i.equals(BigInteger.ZERO)) return false;
            if (i.equals(BigInteger.ONE)) return true;
            throw new IllegalArgumentException();
        };
        for (List<BigInteger> is : take(LIMIT, P.lists(P.range(BigInteger.ZERO, BigInteger.ONE)))) {
            assertEquals(is, fromBigEndianDigits(TWO, is), fromBigEndianBits(toList(map(digitsToBits, is))));
        }

        Iterable<Pair<List<BigInteger>, BigInteger>> psFail = filterInfinite(
                p -> all(i -> lt(i, p.b), p.a),
                P.pairs(
                        P.lists(P.bigIntegers()),
                        P.rangeDown(BigInteger.ONE)
                )
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(
                p -> any(i -> i.signum() == -1, p.a),
                P.pairs(P.lists(P.bigIntegers()), P.rangeUp(TWO))
        );
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = filterInfinite(p -> any(i -> ge(i, p.b), p.a), P.pairs(P.lists(P.bigIntegers()), P.rangeUp(TWO)));
        for (Pair<List<BigInteger>, BigInteger> p : take(LIMIT, psFail)) {
            try {
                fromBigEndianDigits(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesToDigit() {
        initialize("toDigit(int)");
        Set<Character> numbers = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'));
        Set<Character> letters = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z'));
        for (int i : take(LIMIT, P.range(0, 35))) {
            char digit = toDigit(i);
            assertTrue(i, numbers.contains(digit) || letters.contains(digit));
            inverse(IntegerUtils::toDigit, IntegerUtils::fromDigit, i);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                toDigit(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }

        for (int i : take(LIMIT, P.rangeUp(36))) {
            try {
                toDigit(i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromDigit() {
        initialize("fromDigit(char)");
        for (char c : take(LIMIT, P.withScale(1).choose(P.range('0', '9'), P.range('A', 'Z')))) {
            int i = fromDigit(c);
            assertTrue(c, i >= 0 && i < 36);
            inverse(IntegerUtils::fromDigit, IntegerUtils::toDigit, c);
        }

        Set<Character> numbers = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'));
        Set<Character> letters = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z'));
        for (char c : take(LIMIT, filter(d -> !numbers.contains(d) && !letters.contains(d), P.characters()))) {
            try {
                fromDigit(c);
                fail(c);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull String toStringBase_int_int_simplest(int base, int n) {
        return toStringBase(BigInteger.valueOf(base), BigInteger.valueOf(n));
    }

    private void propertiesToStringBase_int_int() {
        initialize("toStringBase(int, int)");
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            String s = toStringBase(p.b, p.a);
            assertEquals(p, toStringBase_int_int_simplest(p.b, p.a), s);
            assertEquals(p, fromStringBase(p.b, s), BigInteger.valueOf(p.a));
        }

        Set<Character> numbers = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'));
        Set<Character> letters = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z'));
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.integers(), P.range(2, 36)))) {
            String s = toStringBase(p.b, p.a);
            assertTrue(p, all(c -> c == '-' || numbers.contains(c) || letters.contains(c), s));
        }

        ps = P.pairsSquareRootOrder(P.integers(), P.withScale(38).rangeUpGeometric(37));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            String s = toStringBase(p.b, p.a);
            if (head(s) == '-') s = tail(s);
            assertEquals(p, head(s), '(');
            assertEquals(p, last(s), ')');
            s = middle(s);
            Iterable<Integer> digits = map(Integer::parseInt, Arrays.asList(s.split("\\)\\(")));
            assertFalse(p, isEmpty(digits));
            assertTrue(p, p.a == 0 || head(digits) != 0);
            assertTrue(p, all(d -> d >= 0 && d < p.b, digits));
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers(), P.rangeDown(1)))) {
            try {
                toStringBase(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsToStringBase_int_int() {
        Map<String, Function<Pair<Integer, Integer>, String>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> toStringBase_int_int_simplest(p.b, p.a));
        functions.put("standard", p -> toStringBase(p.b, p.a));
        Iterable<Pair<Integer, Integer>> ps = P.pairsSquareRootOrder(P.naturalIntegers(), P.rangeUpGeometric(2));
        compareImplementations(
                "fromBigEndianDigits(int, Iterable<Integer>)",
                take(LIMIT, ps),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesToStringBase_BigInteger_BigInteger() {
        initialize("toStringBase(BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairsSquareRootOrder(
                P.naturalBigIntegers(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            String s = toStringBase(p.b, p.a);
            assertEquals(p, fromStringBase(p.b, s), p.a);
        }

        Set<Character> numbers = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'));
        Set<Character> letters = toHashSet(ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z'));
        //noinspection Convert2MethodRef
        ps = P.pairsSquareRootOrder(P.bigIntegers(), map(i -> BigInteger.valueOf(i), P.range(2, 36)));
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            String s = toStringBase(p.b, p.a);
            assertTrue(p, all(c -> c == '-' || numbers.contains(c) || letters.contains(c), s));
        }

        //noinspection Convert2MethodRef
        ps = P.pairsSquareRootOrder(
                P.bigIntegers(),
                map(i -> BigInteger.valueOf(i), P.withScale(38).rangeUpGeometric(37))
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            String s = toStringBase(p.b, p.a);
            if (head(s) == '-') s = tail(s);
            assertEquals(p, head(s), '(');
            assertEquals(p, last(s), ')');
            s = middle(s);
            Iterable<BigInteger> digits = map(BigInteger::new, Arrays.asList(s.split("\\)\\(")));
            assertFalse(p, isEmpty(digits));
            assertTrue(p, p.a.equals(BigInteger.ZERO) || !head(digits).equals(BigInteger.ZERO));
            assertTrue(p, all(d -> d.signum() != -1 && lt(d, p.b), digits));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rangeDown(BigInteger.ONE)))) {
            try {
                toStringBase(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull BigInteger fromStringBase_int_String_simplest(int base, @NotNull String s) {
        return fromStringBase(BigInteger.valueOf(base), s);
    }

    private void propertiesFromStringBase_int_String() {
        initialize("fromString(int, String)");
        Iterable<Pair<Integer, String>> ps = map(
                p -> new Pair<>(p.a, toStringBase(BigInteger.valueOf(p.a), p.b)),
                P.pairs(P.rangeUpGeometric(2), P.bigIntegers())
        );
        for (Pair<Integer, String> p : take(LIMIT, ps)) {
            BigInteger i = fromStringBase(p.a, p.b);
            assertEquals(p, fromStringBase_int_String_simplest(p.a, p.b), i);
            assertEquals(p, toStringBase(BigInteger.valueOf(p.a), i), p.b);
        }

        for (int i : take(LIMIT, P.rangeUp(2))) {
            assertEquals(i, fromStringBase(i, ""), BigInteger.ZERO);
        }

        for (Pair<Integer, String> p : take(LIMIT, P.pairs(P.rangeDown(1), P.strings()))) {
            try {
                fromStringBase(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsFromStringBase_int_String() {
        Map<String, Function<Pair<Integer, String>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> fromStringBase_int_String_simplest(p.a, p.b));
        functions.put("standard", p -> fromStringBase(p.a, p.b));
        Iterable<Pair<Integer, String>> ps = map(
                p -> new Pair<>(p.a, toStringBase(BigInteger.valueOf(p.a), p.b)),
                P.pairs(P.rangeUpGeometric(2), P.bigIntegers())
        );
        compareImplementations("fromString(int, String)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesFromStringBase_BigInteger_String() {
        initialize("fromStringBase(BigInteger, String)");
        Iterable<Pair<BigInteger, String>> ps = map(
                p -> new Pair<>(p.a, toStringBase(p.a, p.b)),
                P.pairs(P.rangeUp(TWO), P.bigIntegers())
        );
        for (Pair<BigInteger, String> p : take(LIMIT, ps)) {
            BigInteger i = fromStringBase(p.a, p.b);
            assertEquals(p, toStringBase(p.a, i), p.b);
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(TWO))) {
            assertEquals(i, fromStringBase(i, ""), BigInteger.ZERO);
        }

        for (Pair<BigInteger, String> p : take(LIMIT, P.pairs(P.rangeDown(BigInteger.ONE), P.strings()))) {
            try {
                fromStringBase(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesLogarithmicMux() {
        initialize("logarithmicMux(BigInteger, BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairs(
                P.naturalBigIntegers(),
                map(i -> BigInteger.valueOf(i), P.naturalIntegersGeometric())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            BigInteger i = logarithmicMux(p.a, p.b);
            assertNotEquals(p, i.signum(), -1);
            assertEquals(p, logarithmicDemux(i), p);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.naturalBigIntegers(), P.negativeBigIntegers()))) {
            try {
                logarithmicMux(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.negativeBigIntegers(), P.naturalBigIntegers()))) {
            try {
                logarithmicMux(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLogarithmicDemux() {
        initialize("logarithmicDemux(BigInteger)");
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            Pair<BigInteger, BigInteger> p = logarithmicDemux(i);
            assertNotEquals(p, p.a.signum(), -1);
            assertNotEquals(p, p.b.signum(), -1);
            assertEquals(p, logarithmicMux(p.a, p.b), i);
            assertTrue(p, lt(i, logarithmicMux(p.a.add(BigInteger.ONE), p.b)));
            assertTrue(p, lt(i, logarithmicMux(p.a, p.b.add(BigInteger.ONE))));
        }

        for (BigInteger i : take(LIMIT, P.negativeBigIntegers())) {
            try {
                logarithmicDemux(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull BigInteger squareRootMux_alt(@NotNull BigInteger x, @NotNull BigInteger y) {
        List<Boolean> xBits = bits(x);
        List<Boolean> yBits = bits(y);
        int outputSize = max(xBits.size(), yBits.size()) * 3;
        Iterable<Iterable<Boolean>> xChunks = map(w -> w, chunk(2, concat(xBits, repeat(false))));
        Iterable<Iterable<Boolean>> yChunks = map(Arrays::asList, concat(yBits, repeat(false)));
        return fromBits(toList(take(outputSize, concat(ExhaustiveProvider.INSTANCE.choose(yChunks, xChunks)))));
    }

    private @NotNull BigInteger squareRootMux_rec(@NotNull BigInteger x, @NotNull BigInteger y) {
        return squareRootMux_rec_helper(0, x, y);
    }

    private @NotNull BigInteger squareRootMux_rec_helper(int c, @NotNull BigInteger x, @NotNull BigInteger y) {
        if (x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }
        BigInteger result;
        if (c == 0) {
            result = squareRootMux_rec_helper(2, x, y.shiftRight(1)).shiftLeft(1);
            if (y.testBit(0)) {
                result = result.setBit(0);
            }
        } else if (c == 1) {
            result = squareRootMux_rec_helper(0, x.shiftRight(1), y).shiftLeft(1);
            if (x.testBit(0)) {
                result = result.setBit(0);
            }
        } else { //c == 2
            result = squareRootMux_rec_helper(1, x.shiftRight(1), y).shiftLeft(1);
            if (x.testBit(0)) {
                result = result.setBit(0);
            }
        }
        return result;
    }

    private void propertiesSquareRootMux() {
        initialize("squareRootMux(BigInteger, BigInteger)");
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.naturalBigIntegers()))) {
            BigInteger i = squareRootMux(p.a, p.b);
            assertEquals(p, i, squareRootMux_alt(p.a, p.b));
            assertEquals(p, i, squareRootMux_rec(p.a, p.b));
            assertNotEquals(p, i.signum(), -1);
            assertEquals(p, squareRootDemux(i), p);
            assertTrue(p, lt(i, squareRootMux(p.a.add(BigInteger.ONE), p.b)));
            assertTrue(p, lt(i, squareRootMux(p.a, p.b.add(BigInteger.ONE))));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.naturalBigIntegers(), P.negativeBigIntegers()))) {
            try {
                squareRootMux(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.negativeBigIntegers(), P.naturalBigIntegers()))) {
            try {
                squareRootMux(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSquareRootMux() {
        Map<String, Function<Pair<BigInteger, BigInteger>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> squareRootMux_alt(p.a, p.b));
        functions.put("recursive", p -> squareRootMux_rec(p.a, p.b));
        functions.put("standard", p -> squareRootMux(p.a, p.b));
        Iterable<Pair<BigInteger, BigInteger>> ps = P.pairs(P.naturalBigIntegers());
        compareImplementations("squareRootMux(BigInteger, BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull Pair<BigInteger, BigInteger> squareRootDemux_alt(@NotNull BigInteger n) {
        List<Boolean> bits = bits(n);
        Iterable<Boolean> xMask = cycle(Arrays.asList(false, true, true));
        Iterable<Boolean> yMask = cycle(Arrays.asList(true, false, false));
        return new Pair<>(fromBits(toList(select(xMask, bits))), fromBits(toList(select(yMask, bits))));
    }

    private void propertiesSquareRootDemux() {
        initialize("squareRootDemux(BigInteger)");
        for (BigInteger i : take(LIMIT, P.naturalBigIntegers())) {
            Pair<BigInteger, BigInteger> p = squareRootDemux(i);
            assertEquals(i, p, squareRootDemux_alt(i));
            assertNotEquals(p, p.a.signum(), -1);
            assertNotEquals(p, p.b.signum(), -1);
            assertEquals(p, squareRootMux(p.a, p.b), i);
        }

        for (BigInteger i : take(LIMIT, P.negativeBigIntegers())) {
            try {
                squareRootDemux(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSquareRootDemux() {
        Map<String, Function<BigInteger, Pair<BigInteger, BigInteger>>> functions = new LinkedHashMap<>();
        functions.put("alt", IntegerUtilsProperties::squareRootDemux_alt);
        functions.put("standard", IntegerUtils::squareRootDemux);
        compareImplementations(
                "squareRootDemux(BigInteger)",
                take(LIMIT, P.naturalBigIntegers()),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull BigInteger mux_alt(@NotNull List<BigInteger> xs) {
        if (xs.isEmpty()) return BigInteger.ZERO;
        Iterable<Boolean> muxedBits = IterableUtils.mux(toList(map(x -> concat(bits(x), repeat(false)), reverse(xs))));
        int outputSize = maximum(map(BigInteger::bitLength, xs)) * xs.size();
        return fromBits(toList(take(outputSize, muxedBits)));
    }

    private void propertiesMux() {
        initialize("mux(List<BigInteger>)");
        for (List<BigInteger> is : take(LIMIT, P.lists(P.naturalBigIntegers()))) {
            BigInteger i = IntegerUtils.mux(is);
            assertEquals(is, mux_alt(is), i);
            assertNotEquals(is, i.signum(), -1);
            assertEquals(is, demux(is.size(), i), is);
            for (int j = 0; j < is.size(); j++) {
                assertTrue(is, lt(i, mux(toList(set(is, j, is.get(j).add(BigInteger.ONE))))));
            }
        }

        for (List<BigInteger> is : take(LIMIT, P.listsWithElement(null, P.naturalBigIntegers()))) {
            try {
                IntegerUtils.mux(is);
                fail(is);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        Iterable<List<BigInteger>> isFail = filterInfinite(
                is -> any(i -> i.signum() == -1, is),
                P.lists(P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, isFail)) {
            try {
                IntegerUtils.mux(is);
                fail(is);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsMux() {
        Map<String, Function<List<BigInteger>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("alt", IntegerUtilsProperties::mux_alt);
        functions.put("standard", IntegerUtils::mux);
        compareImplementations(
                "mux(List<BigInteger>)",
                take(LIMIT, P.lists(P.naturalBigIntegers())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull List<BigInteger> demux_alt(int size, @NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return toList(replicate(size, BigInteger.ZERO));
        }
        return reverse(IterableUtils.map(xs -> IntegerUtils.fromBits(toList(xs)), IterableUtils.demux(size, bits(n))));
    }

    private static @NotNull List<BigInteger> demux_alt2(int size, @NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return toList(replicate(size, BigInteger.ZERO));
        }
        int length = n.bitLength();
        int resultLength = (length / 8 + 1) / size + 2;
        byte[][] demuxedBytes = new byte[size][resultLength];
        int ri = resultLength - 1;
        int rj = 1;
        int rk = size - 1;
        for (int i = 0; i < length; i++) {
            if (n.testBit(i)) {
                demuxedBytes[rk][ri] |= rj;
            }
            if (rk == 0) {
                rk = size - 1;
                rj <<= 1;
                if (rj == 256) {
                    rj = 1;
                    ri--;
                }
            } else {
                rk--;
            }
        }
        List<BigInteger> demuxed = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            demuxed.add(new BigInteger(demuxedBytes[i]));
        }
        return demuxed;
    }

    private void propertiesDemux() {
        initialize("demux(int size, BigInteger n)");
        Iterable<Pair<BigInteger, Integer>> ps = P.withElement(
                new Pair<>(BigInteger.ZERO, 0),
                P.pairsLogarithmicOrder(P.naturalBigIntegers(), P.positiveIntegersGeometric())
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            List<BigInteger> xs = demux(p.b, p.a);
            assertEquals(p, xs, demux_alt(p.b, p.a));
            assertEquals(p, xs, demux_alt2(p.b, p.a));
            assertTrue(p, all(x -> x.signum() != -1, xs));
            assertEquals(p, IntegerUtils.mux(xs), p.a);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.naturalBigIntegers(), P.negativeIntegers()))) {
            try {
                demux(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.negativeBigIntegers(), P.positiveIntegers()))) {
            try {
                demux(p.b, p.a);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.positiveBigIntegers())) {
            try {
                demux(0, i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDemux() {
        Map<String, Function<Pair<BigInteger, Integer>, List<BigInteger>>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> demux_alt(p.b, p.a));
        functions.put("alt2", p -> demux_alt2(p.b, p.a));
        functions.put("standard", p -> demux(p.b, p.a));
        Iterable<Pair<BigInteger, Integer>> ps = P.withElement(
                new Pair<>(BigInteger.ZERO, 0),
                P.pairsLogarithmicOrder(P.naturalBigIntegers(), P.positiveIntegersGeometric())
        );
        compareImplementations("demux(int, BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }
}
