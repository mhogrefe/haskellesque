package mho.wheels.misc;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.filter;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.misc.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class FloatingPointUtilsProperties {
    private static int LIMIT;
    private static IterableProvider P;

    private static void initialize(String name) {
        P.reset();
        System.out.println("\t\ttesting " + name + " properties...");
    }

    @Test
    public void testAllProperties() {
        List<Triple<IterableProvider, Integer, String>> configs = new ArrayList<>();
        configs.add(new Triple<>(ExhaustiveProvider.INSTANCE, 10000, "exhaustively"));
        configs.add(new Triple<>(RandomProvider.example(), 1000, "randomly"));
        System.out.println("FloatingPointUtils properties");
        for (Triple<IterableProvider, Integer, String> config : configs) {
            P = config.a;
            LIMIT = config.b;
            System.out.println("\ttesting " + config.c);
            propertiesIsNegativeZero_float();
            propertiesIsNegativeZero_double();
            propertiesIsPositiveZero_float();
            propertiesIsPositiveZero_double();
            propertiesSuccessor_float();
            propertiesPredecessor_float();
            propertiesSuccessor_double();
            propertiesPredecessor_double();
            propertiesToOrderedRepresentation_float();
            propertiesFloatFromOrderedRepresentation();
            propertiesToOrderedRepresentation_double();
            propertiesDoubleFromOrderedRepresentation();
            propertiesFloatFromMantissaAndExponent();
            propertiesDoubleFromMantissaAndExponent();
            propertiesToMantissaAndExponent_float();
            propertiesToMantissaAndExponent_double();
            propertiesAbsNegativeZeros_float();
            propertiesAbsNegativeZeros_double();
            propertiesScaleUp_float();
            propertiesScaleUp_double();
        }
        System.out.println("Done");
    }

    private static void propertiesIsNegativeZero_float() {
        initialize("isNegativeZero(float)");
        for (float f : take(LIMIT, P.floats())) {
            isNegativeZero(f);
        }

        for (float f : take(LIMIT, P.nonzeroFloats())) {
            assertFalse(f, isNegativeZero(f));
        }
    }

    private static void propertiesIsNegativeZero_double() {
        initialize("isNegativeZero(double)");
        for (double d : take(LIMIT, P.doubles())) {
            isNegativeZero(d);
        }

        for (double d : take(LIMIT, P.nonzeroDoubles())) {
            assertFalse(d, isNegativeZero(d));
        }
    }

    private static void propertiesIsPositiveZero_float() {
        initialize("isPositiveZero(float)");
        for (float f : take(LIMIT, P.floats())) {
            isPositiveZero(f);
        }

        for (float f : take(LIMIT, P.nonzeroFloats())) {
            assertFalse(f, isPositiveZero(f));
        }
    }

    private static void propertiesIsPositiveZero_double() {
        initialize("isPositiveZero(double)");
        for (double d : take(LIMIT, P.doubles())) {
            isPositiveZero(d);
        }

        for (double d : take(LIMIT, P.nonzeroDoubles())) {
            assertFalse(d, isPositiveZero(d));
        }
    }

    private static void propertiesSuccessor_float() {
        initialize("successor(float)");
        Iterable<Float> fs = filter(
                f -> !Float.isNaN(f) && f != Float.POSITIVE_INFINITY && !isNegativeZero(f),
                P.floats()
        );
        for (float f : take(LIMIT, fs)) {
            //noinspection RedundantCast
            inverses((Function<Float, Float>) FloatingPointUtils::successor, FloatingPointUtils::predecessor, f);
        }
    }

    private static void propertiesPredecessor_float() {
        initialize("predecessor(float)");
        Iterable<Float> fs = filter(
                f -> !Float.isNaN(f) && f != Float.NEGATIVE_INFINITY && !isPositiveZero(f),
                P.floats()
        );
        for (float f : take(LIMIT, fs)) {
            //noinspection RedundantCast
            inverses((Function<Float, Float>) FloatingPointUtils::predecessor, FloatingPointUtils::successor, f);
        }
    }

    private static void propertiesSuccessor_double() {
        initialize("successor(double)");
        Iterable<Double> ds = filter(
                d -> !Double.isNaN(d) && d != Double.POSITIVE_INFINITY && !isNegativeZero(d),
                P.doubles()
        );
        for (double d : take(LIMIT, ds)) {
            //noinspection RedundantCast
            inverses((Function<Double, Double>) FloatingPointUtils::successor, FloatingPointUtils::predecessor, d);
        }
    }

    private static void propertiesPredecessor_double() {
        initialize("predecessor(double)");
        Iterable<Double> ds = filter(
                d -> !Double.isNaN(d) && d != Double.NEGATIVE_INFINITY && !isPositiveZero(d),
                P.doubles()
        );
        for (double d : take(LIMIT, ds)) {
            //noinspection RedundantCast
            inverses((Function<Double, Double>) FloatingPointUtils::predecessor, FloatingPointUtils::successor, d);
        }
    }

    private static void propertiesToOrderedRepresentation_float() {
        initialize("toOrderedRepresentation(float)");
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            int n = toOrderedRepresentation(f);
            assertTrue(f, le(Math.abs(n), POSITIVE_FINITE_FLOAT_COUNT + 1));
            homomorphic(
                    g -> -g,
                    i -> -i,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    f
            );
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && !isNegativeZero(g), P.floats()))) {
            inverses(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    f
            );
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && g != Float.POSITIVE_INFINITY, P.floats()))) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::successor,
                    i -> i + 1,
                    f
            );
        }

        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g) && g != Float.NEGATIVE_INFINITY, P.floats()))) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::predecessor,
                    i -> i - 1,
                    f
            );
        }

        Iterable<Pair<Float, Float>> ps = P.pairs(filter(f -> !Float.isNaN(f) && !isNegativeZero(f), P.floats()));
        for (Pair<Float, Float> p : take(LIMIT, ps)) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    Function.identity(),
                    Float::compare,
                    Integer::compare,
                    p
            );
        }
    }

    private static void propertiesFloatFromOrderedRepresentation() {
        initialize("floatFromOrderedRepresentation(int)");
        int maxAbs = FloatingPointUtils.POSITIVE_FINITE_FLOAT_COUNT + 1;

        for (int i : take(LIMIT, P.range(-maxAbs, maxAbs))) {
            float f = floatFromOrderedRepresentation(i);
            assertFalse(f, Float.isNaN(f));
            inverses(
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    i
            );
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.range(-maxAbs, maxAbs)))) {
            homomorphic(
                    j -> -j,
                    g -> -g,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    i
            );
        }

        for (int i : take(LIMIT, filter(j -> j != -1, P.range(-maxAbs, maxAbs - 1)))) {
            homomorphic(
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    j -> j + 1,
                    FloatingPointUtils::successor,
                    i
            );
        }

        for (int i : take(LIMIT, P.range(-maxAbs + 1, maxAbs))) {
            homomorphic(
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    j -> j - 1,
                    FloatingPointUtils::predecessor,
                    i
            );
        }

        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.range(-maxAbs, maxAbs)))) {
            homomorphic(
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    FloatingPointUtils::floatFromOrderedRepresentation,
                    Function.identity(),
                    Integer::compare,
                    Float::compare,
                    p
            );
        }

        for (int i : take(LIMIT, P.rangeUp(maxAbs + 1))) {
            try {
                floatFromOrderedRepresentation(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (int i : take(LIMIT, P.rangeDown(-maxAbs - 1))) {
            try {
                floatFromOrderedRepresentation(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesToOrderedRepresentation_double() {
        initialize("toOrderedRepresentation(double)");
        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            long n = toOrderedRepresentation(d);
            assertTrue(d, le(Math.abs(n), POSITIVE_FINITE_DOUBLE_COUNT + 1));
            homomorphic(
                    g -> -g,
                    i -> -i,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    d
            );
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && !isNegativeZero(e), P.doubles()))) {
            inverses(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    d
            );
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && e != Double.POSITIVE_INFINITY, P.doubles()))) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::successor,
                    l -> l + 1,
                    d
            );
        }

        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e) && e != Double.NEGATIVE_INFINITY, P.doubles()))) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::predecessor,
                    l -> l - 1,
                    d
            );
        }

        Iterable<Pair<Double, Double>> ps = P.pairs(filter(d -> !Double.isNaN(d) && !isNegativeZero(d), P.doubles()));
        for (Pair<Double, Double> p : take(LIMIT, ps)) {
            homomorphic(
                    FloatingPointUtils::toOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    Function.identity(),
                    Double::compare,
                    Long::compare,
                    p
            );
        }
    }

    private static void propertiesDoubleFromOrderedRepresentation() {
        initialize("doubleFromOrderedRepresentation(long)");
        long maxAbs = FloatingPointUtils.POSITIVE_FINITE_DOUBLE_COUNT + 1;

        for (long l : take(LIMIT, P.range(-maxAbs, maxAbs))) {
            double d = doubleFromOrderedRepresentation(l);
            assertFalse(d, Double.isNaN(d));
            inverses(
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    FloatingPointUtils::toOrderedRepresentation,
                    l
            );
        }

        for (long l : take(LIMIT, filter(m -> m != 0L, P.range(-maxAbs, maxAbs)))) {
            homomorphic(
                    m -> -m,
                    e -> -e,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    l
            );
        }

        for (long l : take(LIMIT, filter(m -> m != -1L, P.range(-maxAbs, maxAbs - 1)))) {
            homomorphic(
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    m -> m + 1,
                    FloatingPointUtils::successor,
                    l
            );
        }

        for (long l : take(LIMIT, P.range(-maxAbs + 1, maxAbs))) {
            homomorphic(
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    m -> m - 1,
                    FloatingPointUtils::predecessor,
                    l
            );
        }

        for (Pair<Long, Long> p : take(LIMIT, P.pairs(P.range(-maxAbs, maxAbs)))) {
            homomorphic(
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    FloatingPointUtils::doubleFromOrderedRepresentation,
                    Function.identity(),
                    Long::compare,
                    Double::compare,
                    p
            );
        }

        for (long l : take(LIMIT, P.rangeUp(maxAbs + 1))) {
            try {
                doubleFromOrderedRepresentation(l);
                fail(l);
            } catch (ArithmeticException ignored) {}
        }

        for (long l : take(LIMIT, P.rangeDown(-maxAbs - 1))) {
            try {
                doubleFromOrderedRepresentation(l);
                fail(l);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesFloatFromMantissaAndExponent() {
        initialize("floatFromMantissaAndExponent()");
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers()))) {
            floatFromMantissaAndExponent(p.a, p.b);
        }

        Iterable<Pair<Integer, Integer>> ps = filter(
                q -> floatFromMantissaAndExponent(q.a, q.b).isPresent(),
                P.pairs(P.range(-1 << 24, 1 << 24), P.range(MIN_SUBNORMAL_FLOAT_EXPONENT, 128))
        );
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            float f = floatFromMantissaAndExponent(p.a, p.b).get();
            assertFalse(p, Float.isNaN(f));
            assertFalse(p, Float.isInfinite(f));
            assertFalse(p, isNegativeZero(f));
            assertTrue(p, p.a == 0 || (p.a & 1) == 1);
            inverses(
                    q -> floatFromMantissaAndExponent(q.a, q.b).get(),
                    (Float g) -> toMantissaAndExponent(g).get(),
                    p
            );
        }
    }

    private static void propertiesDoubleFromMantissaAndExponent() {
        initialize("doubleFromMantissaAndExponent()");
        for (Pair<Long, Integer> p : take(LIMIT, P.pairs(P.longs(), P.integers()))) {
            doubleFromMantissaAndExponent(p.a, p.b);
        }

        Iterable<Pair<Long, Integer>> ps = filter(
                q -> doubleFromMantissaAndExponent(q.a, q.b).isPresent(),
                P.pairs(P.range(-1L << 53, 1L << 53), P.range(MIN_SUBNORMAL_DOUBLE_EXPONENT, 1024))
        );
        for (Pair<Long, Integer> p : take(LIMIT, ps)) {
            double d = doubleFromMantissaAndExponent(p.a, p.b).get();
            assertFalse(p, Double.isNaN(d));
            assertFalse(p, Double.isInfinite(d));
            assertFalse(p, isNegativeZero(d));
            assertTrue(p, p.a == 0L || (p.a & 1) == 1);
            inverses(
                    q -> doubleFromMantissaAndExponent(q.a, q.b).get(),
                    (Double e) -> toMantissaAndExponent(e).get(),
                    p
            );
        }
    }

    private static void propertiesToMantissaAndExponent_float() {
        initialize("toMantissaAndExponent(float)");
        for (float f : take(LIMIT, P.floats())) {
            toMantissaAndExponent(f);
        }

        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Pair<Integer, Integer> p = toMantissaAndExponent(f).get();
            assertNotNull(f, p.a);
            assertNotNull(f, p.b);
            assertTrue(f, p.a == 0 || (p.a & 1) == 1);
            if (!isNegativeZero(f)) {
                inverses(
                        (Float g) -> toMantissaAndExponent(g).get(),
                        q -> floatFromMantissaAndExponent(q.a, q.b).get(),
                        f
                );
            }
        }
    }

    private static void propertiesToMantissaAndExponent_double() {
        initialize("toMantissaAndExponent(double)");
        for (double d : take(LIMIT, P.doubles())) {
            toMantissaAndExponent(d);
        }

        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Pair<Long, Integer> p = toMantissaAndExponent(d).get();
            assertNotNull(d, p.a);
            assertNotNull(d, p.b);
            assertTrue(d, p.a == 0L || (p.a & 1) == 1);
            if (!isNegativeZero(d)) {
                inverses(
                        (Double e) -> toMantissaAndExponent(e).get(),
                        q -> doubleFromMantissaAndExponent(q.a, q.b).get(),
                        d
                );
            }
        }
    }

    private static void propertiesAbsNegativeZeros_float() {
        initialize("absNegativeZeros(float)");
        for (float f : take(LIMIT, P.floats())) {
            assertFalse(f, isNegativeZero(absNegativeZeros(f)));
        }

        for (float f : take(LIMIT, filter(g -> !isNegativeZero(g), P.floats()))) {
            assertEquals(f, f, absNegativeZeros(f));
        }
    }

    private static void propertiesAbsNegativeZeros_double() {
        initialize("absNegativeZeros(double)");
        for (double d : take(LIMIT, P.doubles())) {
            assertFalse(d, isNegativeZero(absNegativeZeros(d)));
        }

        for (double d : take(LIMIT, filter(e -> !isNegativeZero(e), P.doubles()))) {
            assertEquals(d, d, absNegativeZeros(d));
        }
    }

    private static void propertiesScaleUp_float() {
        initialize("scaleUp(float)");
        for (float f : take(LIMIT, P.floats())) {
            scaleUp(f);
        }

        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            BigInteger scaled = scaleUp(f).get();
            assertTrue(f, le(scaled.abs(), SCALED_UP_MAX_FLOAT));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            inverses(
                    g -> scaleUp(g).get(),
                    (BigInteger i) -> BinaryFraction.of(i, MIN_SUBNORMAL_FLOAT_EXPONENT).floatRange().a,
                    f
            );
        }
    }

    private static void propertiesScaleUp_double() {
        initialize("scaleUp(double)");
        for (double d : take(LIMIT, P.doubles())) {
            scaleUp(d);
        }

        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            BigInteger scaled = scaleUp(d).get();
            assertTrue(d, le(scaled.abs(), SCALED_UP_MAX_DOUBLE));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverses(
                    e -> scaleUp(e).get(),
                    (BigInteger i) -> BinaryFraction.of(i, MIN_SUBNORMAL_DOUBLE_EXPONENT).doubleRange().a,
                    d
            );
        }
    }
}
