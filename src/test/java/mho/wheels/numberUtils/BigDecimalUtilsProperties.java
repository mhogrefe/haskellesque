package mho.wheels.numberUtils;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.numberUtils.BigDecimalUtils.*;
import static mho.wheels.ordering.Ordering.eq;
import static mho.wheels.ordering.Ordering.ne;
import static org.junit.Assert.*;

public class BigDecimalUtilsProperties {
    private static int LIMIT;
    private static IterableProvider P;

    @Test
    public void testAllProperties() {
        List<Triple<IterableProvider, Integer, String>> configs = new ArrayList<>();
        configs.add(new Triple<>(ExhaustiveProvider.INSTANCE, 10000, "exhaustively"));
        configs.add(new Triple<>(RandomProvider.example(), 1000, "randomly"));
        System.out.println("BigDecimalUtils properties");
        for (Triple<IterableProvider, Integer, String> config : configs) {
            P = config.a;
            LIMIT = config.b;
            System.out.println("\ttesting " + config.c);
            propertiesSetPrecision();
            propertiesSuccessor();
            propertiesPredecessor();
        }
        System.out.println("Done");
    }

    private static void propertiesSetPrecision() {
        System.out.println("\t\ttesting setPrecision(BigDecimal, int) properties...");

        Iterable<Pair<BigDecimal, Integer>> ps = filterInfinite(
                q -> ne(q.a, BigDecimal.ZERO),
                P.pairsSquareRootOrder(P.bigDecimals(), P.withScale(20).positiveIntegersGeometric())
        );
        for (Pair<BigDecimal, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = setPrecision(p.a, p.b);
            assertEquals(p.toString(), bd.precision(), (int) p.b);
            assertTrue(p.toString(), ne(bd, BigDecimal.ZERO));
        }

        Iterable<Pair<BigDecimal, Integer>> zeroPs;
        if (P instanceof ExhaustiveProvider) {
            Iterable<BigDecimal> zeroes = map(i -> new BigDecimal(BigInteger.ZERO, i), P.integers());
            zeroPs = ((ExhaustiveProvider) P).pairsSquareRootOrder(zeroes, P.positiveIntegers());
        } else {
            Iterable<BigDecimal> zeroes = map(
                    i -> new BigDecimal(BigInteger.ZERO, i),
                    P.withScale(20).integersGeometric()
            );
            zeroPs = P.pairs(zeroes, P.withScale(20).positiveIntegersGeometric());
        }
        for (Pair<BigDecimal, Integer> p : take(LIMIT, zeroPs)) {
            BigDecimal bd = setPrecision(p.a, p.b);
            assertTrue(p.toString(), bd.scale() > -1);
            assertTrue(p.toString(), eq(bd, BigDecimal.ZERO));
        }

        for (Pair<BigDecimal, Integer> p : take(LIMIT, filterInfinite(q -> q.b > 1, zeroPs))) {
            BigDecimal bd = setPrecision(p.a, p.b);
            assertTrue(
                    p.toString(),
                    bd.toString().equals("0." + replicate(p.b - 1, '0')) || bd.toString().equals("0E-" + (p.b - 1))
            );
        }

        for (Pair<BigDecimal, Integer> p : take(LIMIT, P.pairs(P.bigDecimals(), P.rangeDown(0)))) {
            try {
                setPrecision(p.a, p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesSuccessor() {
        System.out.println("\t\ttesting successor(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            BigDecimal successor = successor(bd);
            assertEquals(bd.toString(), bd.scale(), successor.scale());
            assertEquals(bd.toString(), predecessor(successor), bd);
            assertEquals(bd.toString(), successor(bd.negate()), predecessor(bd).negate());
        }
    }

    private static void propertiesPredecessor() {
        System.out.println("\t\ttesting predecessor(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            BigDecimal predecessor = predecessor(bd);
            assertEquals(bd.toString(), bd.scale(), predecessor.scale());
            assertEquals(bd.toString(), successor(predecessor), bd);
            assertEquals(bd.toString(), predecessor(bd.negate()), successor(bd).negate());
        }
    }
}