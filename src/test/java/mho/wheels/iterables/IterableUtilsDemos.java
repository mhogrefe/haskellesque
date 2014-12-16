package mho.wheels.iterables;

import mho.wheels.structures.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mho.wheels.iterables.IterableUtils.*;

public class IterableUtilsDemos {
    private static final boolean USE_RANDOM = false;
    private static int LIMIT;

    private static IterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new RandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = ExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    public static void demoAddTo_Collection_Iterable() {
        initialize();
        for (Pair<List<Integer>, List<Integer>> p : take(LIMIT, P.pairs(P.lists(P.withNull(P.integers()))))) {
            assert p.a != null;
            assert p.b != null;
            List<Integer> list = new ArrayList<>();
            list.addAll(p.b);
            addTo(p.a, list);
            System.out.println("addTo(" + p.a + ", " + p.b + ") => " + list);
        }
    }

    public static void demoAddTo_Collection_String() {
        initialize();
        Iterable<Pair<String, List<Character>>> ps = P.pairs(P.strings(), P.lists(P.withNull(P.characters())));
        for (Pair<String, List<Character>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            List<Character> list = new ArrayList<>();
            list.addAll(p.b);
            addTo(p.a, list);
            System.out.println("addTo(" + p.a + ", " + p.b + ") => " + list);
        }
    }

    public static void demoToList_Iterable() {
        initialize();
        for (List<Integer> is : take(LIMIT, P.lists(P.withNull(P.integers())))) {
            String listString = tail(init(is.toString()));
            System.out.println("toList(" + listString + ") = " + toList(is));
        }
    }

    public static void demoToList_String() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("toList(" + s + ") = " + toList(s));
        }
    }

    public static void demoToString_Iterable() {
        initialize();
        for (List<Integer> is : take(LIMIT, P.lists(P.withNull(P.integers())))) {
            String listString = tail(init(is.toString()));
            System.out.println("toString(" + listString + ") = " + IterableUtils.toString(is));
        }
    }

    public static void demoToString_int_finite_Iterable() {
        initialize();
        Iterable<Pair<Integer, List<Integer>>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = map(
                    p -> new Pair<Integer, List<Integer>>(p.b, p.a),
                    ((ExhaustiveProvider) P).pairsSquareRootOrder(
                            P.lists(P.withNull(P.integers())),
                            P.naturalIntegers()
                    )
            );
        } else {
            ps = P.pairs(((RandomProvider) P).naturalIntegersGeometric(20), P.lists(P.integers()));
        }
        for (Pair<Integer, List<Integer>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("toString(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(p.a, p.b));
        }
    }

    public static void demoToString_int_infinite_Iterable() {
        initialize();
        Iterable<Pair<Integer, Iterable<Integer>>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = map(
                    p -> {
                        assert p.a != null;
                        return new Pair<>(p.b, cycle((List<Integer>) p.a));
                    },
                    ((ExhaustiveProvider) P).pairsSquareRootOrder(
                            P.lists(P.withNull(P.integers())),
                            P.naturalIntegers()
                    )
            );
        } else {
            ps = P.pairs(
                    ((RandomProvider) P).naturalIntegersGeometric(20),
                    map(IterableUtils::cycle, P.lists(P.integers()))
            );
        }
        for (Pair<Integer, Iterable<Integer>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("toString(" + p.a + ", " + IterableUtils.toString(10, p.b) + ") = " +
                    IterableUtils.toString(p.a, p.b));
        }
    }

    public static void demoFromString() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("fromString(" + s + ") = " + toList(fromString(s)));
        }
    }

    public static void demoCharsToString() {
        initialize();
        for (List<Character> cs : take(LIMIT, P.lists(P.characters()))) {
            String listString = tail(init(cs.toString()));
            System.out.println("charsToString(" + listString + ") = " + charsToString(cs));
        }
    }

    public static void demoRange_byte() {
        initialize();
        for (byte b : take(LIMIT, P.bytes())) {
            System.out.println("range(" + b + ") = " + toList(range(b)));
        }
    }

    public static void demoRange_short() {
        initialize();
        for (short s : take(LIMIT, P.shorts())) {
            System.out.println("range(" + s + ") = " + IterableUtils.toString(20, range(s)));
        }
    }

    public static void demoRange_int() {
        initialize();
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("range(" + i + ") = " + IterableUtils.toString(20, range(i)));
        }
    }

    public static void demoRange_long() {
        initialize();
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("range(" + l + ") = " + IterableUtils.toString(20, range(l)));
        }
    }

    public static void demoRange_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("range(" + i + ") = " + IterableUtils.toString(20, range(i)));
        }
    }

    public static void demoRange_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("range(" + bd + ") = " + IterableUtils.toString(20, range(bd)));
        }
    }

    public static void demoRange_char() {
        initialize();
        for (char c : take(LIMIT, P.characters())) {
            System.out.println("range(" + c + ") = " + IterableUtils.toString(20, range(c)));
        }
    }

    public static void demoRange_float() {
        initialize();
        for (float f : take(LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("range(" + f + ") = " + IterableUtils.toString(20, range(f)));
        }
    }

    public static void demoRange_double() {
        initialize();
        for (double d : take(LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("range(" + d + ") = " + IterableUtils.toString(20, range(d)));
        }
    }

    public static void demoRange_byte_byte() {
        initialize();
        for (Pair<Byte, Byte> p : take(LIMIT, P.pairs(P.bytes()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_short_short() {
        initialize();
        for (Pair<Short, Short> p : take(LIMIT, P.pairs(P.shorts()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_int_int() {
        initialize();
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_long_long() {
        initialize();
        for (Pair<Long, Long> p : take(LIMIT, P.pairs(P.longs()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_BigInteger_BigInteger() {
        initialize();
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_BigDecimal_BigDecimal() {
        initialize();
        for (Pair<BigDecimal, BigDecimal> p : take(LIMIT, P.pairs(P.bigDecimals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_char_char() {
        initialize();
        for (Pair<Character, Character> p : take(LIMIT, P.pairs(P.characters()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_float_float() {
        initialize();
        Iterable<Pair<Float, Float>> ps = P.pairs(filter(f -> !Float.isNaN(f), P.floats()));
        for (Pair<Float, Float> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoRange_double_double() {
        initialize();
        Iterable<Pair<Double, Double>> ps = P.pairs(filter(d -> !Double.isNaN(d), P.doubles()));
        for (Pair<Double, Double> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("range(" + p.a + ", " + p.b + ") = " + IterableUtils.toString(20, range(p.a, p.b)));
        }
    }

    public static void demoSumByte() {
        initialize();
        for (List<Byte> bs : take(LIMIT, P.lists(P.bytes()))) {
            String listString = tail(init(bs.toString()));
            System.out.println("Σ(" + listString + ") = " + sumByte(bs));
        }
    }

    public static void demoSumShort() {
        initialize();
        for (List<Short> ss : take(LIMIT, P.lists(P.shorts()))) {
            String listString = tail(init(ss.toString()));
            System.out.println("Σ(" + listString + ") = " + sumShort(ss));
        }
    }

    public static void demoSumInteger() {
        initialize();
        for (List<Integer> is : take(LIMIT, P.lists(P.integers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("Σ(" + listString + ") = " + sumInteger(is));
        }
    }

    public static void demoSumLong() {
        initialize();
        for (List<Long> ls : take(LIMIT, P.lists(P.longs()))) {
            String listString = tail(init(ls.toString()));
            System.out.println("Σ(" + listString + ") = " + sumLong(ls));
        }
    }

    public static void demoSumFloat() {
        initialize();
        for (List<Float> fs : take(LIMIT, P.lists(P.floats()))) {
            String listString = tail(init(fs.toString()));
            System.out.println("Σ(" + listString + ") = " + sumFloat(fs));
        }
    }

    public static void demoSumDouble() {
        initialize();
        for (List<Double> ds : take(LIMIT, P.lists(P.doubles()))) {
            String listString = tail(init(ds.toString()));
            System.out.println("Σ(" + listString + ") = " + sumDouble(ds));
        }
    }

    public static void demoSumBigInteger() {
        initialize();
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("Σ(" + listString + ") = " + sumBigInteger(is));
        }
    }

    public static void demoSumBigDecimal() {
        initialize();
        for (List<BigDecimal> bds : take(LIMIT, P.lists(P.bigDecimals()))) {
            String listString = tail(init(bds.toString()));
            System.out.println("Σ(" + listString + ") = " + sumBigDecimal(bds));
        }
    }

    public static void demoProductByte() {
        initialize();
        for (List<Byte> bs : take(LIMIT, P.lists(P.bytes()))) {
            String listString = tail(init(bs.toString()));
            System.out.println("Π(" + listString + ") = " + productByte(bs));
        }
    }

    public static void demoProductShort() {
        initialize();
        for (List<Short> ss : take(LIMIT, P.lists(P.shorts()))) {
            String listString = tail(init(ss.toString()));
            System.out.println("Π(" + listString + ") = " + productShort(ss));
        }
    }

    public static void demoProductInteger() {
        initialize();
        for (List<Integer> is : take(LIMIT, P.lists(P.integers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("Π(" + listString + ") = " + productInteger(is));
        }
    }

    public static void demoProductLong() {
        initialize();
        for (List<Long> ls : take(LIMIT, P.lists(P.longs()))) {
            String listString = tail(init(ls.toString()));
            System.out.println("Π(" + listString + ") = " + productLong(ls));
        }
    }

    public static void demoProductFloat() {
        initialize();
        for (List<Float> fs : take(LIMIT, P.lists(P.floats()))) {
            String listString = tail(init(fs.toString()));
            System.out.println("Π(" + listString + ") = " + productFloat(fs));
        }
    }

    public static void demoProductDouble() {
        initialize();
        for (List<Double> ds : take(LIMIT, P.lists(P.doubles()))) {
            String listString = tail(init(ds.toString()));
            System.out.println("Π(" + listString + ") = " + productDouble(ds));
        }
    }

    public static void demoProductBigInteger() {
        initialize();
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("Π(" + listString + ") = " + productBigInteger(is));
        }
    }

    public static void demoProductBigDecimal() {
        initialize();
        for (List<BigDecimal> bds : take(LIMIT, P.lists(P.bigDecimals()))) {
            String listString = tail(init(bds.toString()));
            System.out.println("Π(" + listString + ") = " + productBigDecimal(bds));
        }
    }

    public static void demoDeltaByte() {
        initialize();
        for (List<Byte> bs : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.bytes())))) {
            String listString = tail(init(bs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaByte(bs)));
        }
    }

    public static void demoDeltaShort() {
        initialize();
        for (List<Short> ss : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.shorts())))) {
            String listString = tail(init(ss.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaShort(ss)));
        }
    }

    public static void demoDeltaInteger() {
        initialize();
        for (List<Integer> is : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.integers())))) {
            String listString = tail(init(is.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaInteger(is)));
        }
    }

    public static void demoDeltaLong() {
        initialize();
        for (List<Long> ls : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.longs())))) {
            String listString = tail(init(ls.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaLong(ls)));
        }
    }

    public static void demoDeltaBigInteger() {
        initialize();
        for (List<BigInteger> is : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.bigIntegers())))) {
            String listString = tail(init(is.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaBigInteger(is)));
        }
    }

    public static void demoDeltaBigDecimal() {
        initialize();
        for (List<BigDecimal> bds : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.bigDecimals())))) {
            String listString = tail(init(bds.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaBigDecimal(bds)));
        }
    }

    public static void demoDeltaFloat() {
        initialize();
        for (List<Float> fs : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.floats())))) {
            String listString = tail(init(fs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaFloat(fs)));
        }
    }

    public static void demoDeltaDouble() {
        initialize();
        for (List<Double> ds : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.doubles())))) {
            String listString = tail(init(ds.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaDouble(ds)));
        }
    }

    public static void demoDeltaCharacter() {
        initialize();
        for (List<Character> cs : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.characters())))) {
            String listString = tail(init(cs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, deltaCharacter(cs)));
        }
    }
}