package mho.wheels.iterables;

import mho.wheels.math.BinaryFraction;
import mho.wheels.structures.*;
import mho.wheels.testing.Testing;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class ExhaustiveProviderDemos {
    private static final boolean USE_RANDOM = false;
    private static final @NotNull ExhaustiveProvider EP = ExhaustiveProvider.INSTANCE;
    private static final int SMALL_LIMIT = 1000;
    private static final int TINY_LIMIT = 100;
    private static int LIMIT;
    private static IterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = RandomProvider.example();
            LIMIT = 1000;
        } else {
            P = ExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    private static void demoUniformSample_Iterable() {
        initialize();
        for (List<Integer> is : take(LIMIT, P.lists(P.withNull(P.integersGeometric())))) {
            String listString = tail(init(is.toString()));
            System.out.println("uniformSample(" + listString + ") = " + its(EP.uniformSample(is)));
        }
    }

    private static void demoUniformSample_String() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("uniformSample(" + nicePrint(s) + ") = " + cits(EP.uniformSample(s)));
        }
    }

    private static void demoRangeUp_byte() {
        initialize();
        for (byte b : take(SMALL_LIMIT, P.bytes())) {
            System.out.println("rangeUp(" + b + ") = " + its(EP.rangeUp(b)));
        }
    }

    private static void demoRangeUp_short() {
        initialize();
        for (short s : take(SMALL_LIMIT, P.shorts())) {
            System.out.println("rangeUp(" + s + ") = " + its(EP.rangeUp(s)));
        }
    }

    private static void demoRangeUp_int() {
        initialize();
        for (int i : take(SMALL_LIMIT, P.integers())) {
            System.out.println("rangeUp(" + i + ") = " + its(EP.rangeUp(i)));
        }
    }

    private static void demoRangeUp_long() {
        initialize();
        for (long l : take(SMALL_LIMIT, P.longs())) {
            System.out.println("rangeUp(" + l + ") = " + its(EP.rangeUp(l)));
        }
    }

    private static void demoRangeUp_BigInteger() {
        initialize();
        for (BigInteger i : take(SMALL_LIMIT, P.bigIntegers())) {
            System.out.println("rangeUp(" + i + ") = " + its(EP.rangeUp(i)));
        }
    }

    private static void demoRangeUp_char() {
        initialize();
        for (char c : take(SMALL_LIMIT, P.characters())) {
            System.out.println("rangeUp(" + nicePrint(c) + ") = " + cits(EP.rangeUp(c)));
        }
    }

    private static void demoRangeDown_byte() {
        initialize();
        for (byte b : take(SMALL_LIMIT, P.bytes())) {
            System.out.println("rangeDown(" + b + ") = " + its(EP.rangeDown(b)));
        }
    }

    private static void demoRangeDown_short() {
        initialize();
        for (short s : take(SMALL_LIMIT, P.shorts())) {
            System.out.println("rangeDown(" + s + ") = " + its(EP.rangeDown(s)));
        }
    }

    private static void demoRangeDown_int() {
        initialize();
        for (int i : take(SMALL_LIMIT, P.integers())) {
            System.out.println("rangeDown(" + i + ") = " + its(EP.rangeDown(i)));
        }
    }

    private static void demoRangeDown_long() {
        initialize();
        for (long l : take(SMALL_LIMIT, P.longs())) {
            System.out.println("rangeDown(" + l + ") = " + its(EP.rangeDown(l)));
        }
    }

    private static void demoRangeDown_BigInteger() {
        initialize();
        for (BigInteger i : take(SMALL_LIMIT, P.bigIntegers())) {
            System.out.println("rangeDown(" + i + ") = " + its(EP.rangeDown(i)));
        }
    }

    private static void demoRangeDown_char() {
        initialize();
        for (char c : take(SMALL_LIMIT, P.characters())) {
            System.out.println("rangeDown(" + nicePrint(c) + ") = " + cits(EP.rangeDown(c)));
        }
    }

    private static void demoRange_byte_byte() {
        initialize();
        for (Pair<Byte, Byte> p : take(SMALL_LIMIT, P.pairs(P.bytes()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRange_short_short() {
        initialize();
        for (Pair<Short, Short> p : take(SMALL_LIMIT, P.pairs(P.shorts()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRange_int_int() {
        initialize();
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.integers()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRange_long_long() {
        initialize();
        for (Pair<Long, Long> p : take(SMALL_LIMIT, P.pairs(P.longs()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRange_BigInteger_BigInteger() {
        initialize();
        for (Pair<BigInteger, BigInteger> p : take(SMALL_LIMIT, P.pairs(P.bigIntegers()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRange_char_char() {
        initialize();
        for (Pair<Character, Character> p : take(SMALL_LIMIT, P.pairs(P.characters()))) {
            System.out.println("range(" + nicePrint(p.a) + ", " + nicePrint(p.b) + ") = " + cits(EP.range(p.a, p.b)));
        }
    }

    private static void demoRangeUp_BinaryFraction() {
        initialize();
        for (BinaryFraction bf : take(SMALL_LIMIT, P.binaryFractions())) {
            System.out.println("rangeUp(" + bf + ") = " + its(EP.rangeUp(bf)));
        }
    }

    private static void demoRangeDown_BinaryFraction() {
        initialize();
        for (BinaryFraction bf : take(SMALL_LIMIT, P.binaryFractions())) {
            System.out.println("rangeDown(" + bf + ") = " + its(EP.rangeDown(bf)));
        }
    }

    private static void demoRange_BinaryFraction_BinaryFraction() {
        initialize();
        for (Pair<BinaryFraction, BinaryFraction> p : take(SMALL_LIMIT, P.pairs(P.binaryFractions()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRangeUp_float() {
        initialize();
        for (float f : take(SMALL_LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("rangeUp(" + f + ") = " + its(EP.rangeUp(f)));
        }
    }

    private static void demoRangeDown_float() {
        initialize();
        for (float f : take(SMALL_LIMIT, filter(g -> !Float.isNaN(g), P.floats()))) {
            System.out.println("rangeDown(" + f + ") = " + its(EP.rangeDown(f)));
        }
    }

    private static void demoRange_float_float() {
        initialize();
        for (Pair<Float, Float> p : take(SMALL_LIMIT, P.pairs(filter(f -> !Float.isNaN(f), P.floats())))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRangeUp_double() {
        initialize();
        for (double d : take(SMALL_LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("rangeUp(" + d + ") = " + its(EP.rangeUp(d)));
        }
    }

    private static void demoRangeDown_double() {
        initialize();
        for (double d : take(SMALL_LIMIT, filter(e -> !Double.isNaN(e), P.doubles()))) {
            System.out.println("rangeDown(" + d + ") = " + its(EP.rangeDown(d)));
        }
    }

    private static void demoRange_double_double() {
        initialize();
        for (Pair<Double, Double> p : take(SMALL_LIMIT, P.pairs(filter(d -> !Double.isNaN(d), P.doubles())))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRangeUp_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(SMALL_LIMIT, P.bigDecimals())) {
            System.out.println("rangeUp(" + bd + ") = " + its(EP.rangeUp(bd)));
        }
    }

    private static void demoRangeDown_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(SMALL_LIMIT, P.bigDecimals())) {
            System.out.println("rangeDown(" + bd + ") = " + its(EP.rangeDown(bd)));
        }
    }

    private static void demoRange_BigDecimal_BigDecimal() {
        initialize();
        for (Pair<BigDecimal, BigDecimal> p : take(SMALL_LIMIT, P.pairs(P.bigDecimals()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(EP.range(p.a, p.b)));
        }
    }

    private static void demoRangeUpCanonical_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(SMALL_LIMIT, P.bigDecimals())) {
            System.out.println("rangeUpCanonical(" + bd + ") = " + its(EP.rangeUpCanonical(bd)));
        }
    }

    private static void demoRangeDownCanonical_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(SMALL_LIMIT, P.bigDecimals())) {
            System.out.println("rangeDownCanonical(" + bd + ") = " + its(EP.rangeDownCanonical(bd)));
        }
    }

    private static void demoRangeCanonical_BigDecimal_BigDecimal() {
        initialize();
        for (Pair<BigDecimal, BigDecimal> p : take(SMALL_LIMIT, P.pairs(P.bigDecimals()))) {
            System.out.println("rangeCanonical(" + p.a + ", " + p.b + ") = " + its(EP.rangeCanonical(p.a, p.b)));
        }
    }

    private static void demoWithNull_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.integersGeometric()))) {
            System.out.println("withNull(" + xs + ") = " + its(EP.withNull(xs)));
        }
    }

    private static void demoWithNull_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.naturalIntegers()))) {
            String xsString = tail(init(its(xs)));
            System.out.println("withNull(" + xsString + ") = " + its(EP.withNull(xs)));
        }
    }

    private static void demoNonEmptyOptionals_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.integersGeometric()))) {
            System.out.println("nonEmptyOptionals(" + xs + ") = " + its(EP.nonEmptyOptionals(xs)));
        }
    }

    private static void demoNonEmptyOptionals_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.naturalIntegers()))) {
            String xsString = tail(init(its(xs)));
            System.out.println("nonEmptyOptionals(" + xsString + ") = " + its(EP.nonEmptyOptionals(xs)));
        }
    }

    private static void demoOptionals_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.integersGeometric()))) {
            System.out.println("optionals(" + xs + ") = " + its(EP.optionals(xs)));
        }
    }

    private static void demoOptionals_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.naturalIntegers()))) {
            String xsString = tail(init(its(xs)));
            System.out.println("optionals(" + xsString + ") = " + its(EP.optionals(xs)));
        }
    }

    private static void demoNonEmptyNullableOptionals_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            System.out.println("nonEmptyNullableOptionals(" + xs + ") = " + its(EP.nonEmptyNullableOptionals(xs)));
        }
    }

    private static void demoNonEmptyNullableOptionals_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.withNull(EP.naturalIntegers())))) {
            String xsString = tail(init(its(xs)));
            System.out.println("nonEmptyNullableOptionals(" + xsString + ") = " +
                    its(EP.nonEmptyNullableOptionals(xs)));
        }
    }

    private static void demoNullableOptionals_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            System.out.println("nullableOptionals(" + xs + ") = " + its(EP.nullableOptionals(xs)));
        }
    }

    private static void demoNullableOptionals_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.withNull(EP.naturalIntegers())))) {
            String xsString = tail(init(its(xs)));
            System.out.println("nullableOptionals(" + xsString + ") = " + its(EP.nullableOptionals(xs)));
        }
    }

    private static void demoDependentPairs_finite() {
        initialize();
        IterableProvider PS = P.withScale(4);
        Iterable<Pair<List<Integer>, Function<Integer, Iterable<Integer>>>> ps = P.dependentPairsInfinite(
                PS.lists(P.integersGeometric()),
                xs -> xs.isEmpty() ?
                        repeat(new FiniteDomainFunction<>(new HashMap<>())) :
                        map(
                                FiniteDomainFunction::new,
                                PS.maps(xs, map(is -> (Iterable<Integer>) is, PS.lists(P.integersGeometric())))
                        )
        );
        if (P instanceof ExhaustiveProvider) {
            ps = nub(ps);
        }
        for (Pair<List<Integer>, Function<Integer, Iterable<Integer>>> p : take(LIMIT, ps)) {
            System.out.println("dependentPairs(" + p.a + ", " + p.b + ") = " + its(EP.dependentPairs(p.a, p.b)));
        }
    }

    private static void demoDependentPairs_cyclic() {
        initialize();
        IterableProvider PS = P.withScale(4);
        Function<List<Integer>, Iterable<Map<Integer, List<Integer>>>> f = xs -> {
            if (xs.isEmpty()) {
                return repeat(new HashMap<>());
            } else {
                return filter(m -> !all(p -> isEmpty(p.b), fromMap(m)), PS.maps(xs, PS.lists(P.integersGeometric())));
            }
        };
        Function<
                Pair<List<Integer>, Map<Integer, List<Integer>>>, Pair<Iterable<Integer>,
                Function<Integer, Iterable<Integer>>>
        > g = p -> {
            Iterable<Pair<Integer, List<Integer>>> values = fromMap(p.b);
            Map<Integer, Iterable<Integer>> transformedValues = toMap(
                    map(e -> new Pair<>(e.a, (Iterable<Integer>) e.b), values)
            );
            return new Pair<>(cycle(p.a), new FiniteDomainFunction<>(transformedValues));
        };
        Iterable<Pair<Iterable<Integer>, Function<Integer, Iterable<Integer>>>> ps = map(
                g,
                nub(
                        P.dependentPairsInfinite(
                                nub(map(IterableUtils::unrepeat, PS.listsAtLeast(1, P.positiveIntegersGeometric()))),
                                f
                        )
                )
        );
        for (Pair<Iterable<Integer>, Function<Integer, Iterable<Integer>>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("dependentPairs(" + its(p.a) + ", " + p.b + ") = " + its(EP.dependentPairs(p.a, p.b)));
        }
    }

    private static void demoDependentPairsInfinite() {
        initialize();
        IterableProvider PS = P.withScale(4);
        Function<List<Integer>, Iterable<Map<Integer, List<Integer>>>> f = xs -> filterInfinite(
                m -> !all(p -> isEmpty(p.b), fromMap(m)),
                PS.maps(xs, map(IterableUtils::unrepeat, PS.listsAtLeast(1, P.integersGeometric())))
        );
        Function<
                Pair<List<Integer>, Map<Integer, List<Integer>>>,
                Pair<Iterable<Integer>, FiniteDomainFunction<Integer, Iterable<Integer>>>
        > g = p -> {
            Iterable<Pair<Integer, List<Integer>>> values = fromMap(p.b);
            Map<Integer, Iterable<Integer>> transformedValues = toMap(
                    map(e -> new Pair<>(e.a, cycle(e.b)), values)
            );
            return new Pair<>(cycle(p.a), new FiniteDomainFunction<>(transformedValues));
        };
        Iterable<Pair<Iterable<Integer>, FiniteDomainFunction<Integer, Iterable<Integer>>>> ps = map(
                g,
                nub(
                        P.dependentPairsInfinite(
                                nub(map(IterableUtils::unrepeat, PS.listsAtLeast(1, P.integersGeometric()))),
                                f
                        )
                )
        );
        for (Pair<Iterable<Integer>, FiniteDomainFunction<Integer, Iterable<Integer>>> p : take(SMALL_LIMIT, ps)) {
            String niceFunction = toMap(map(q -> new Pair<>(q.a, its(q.b)), fromMap(p.b.asMap()))).toString();
            System.out.println("dependentPairsInfinite(" + its(p.a) + ", " + niceFunction + ") = " +
                    its(EP.dependentPairsInfinite(p.a, p.b)));
        }
    }

    private static void demoPairsLogarithmicOrder_Iterable_Iterable_finite() {
        initialize();
        Iterable<Pair<List<Integer>, List<Integer>>> ps = P.pairs(
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Pair<List<Integer>, List<Integer>> p : take(LIMIT, ps)) {
            System.out.println("pairsLogarithmicOrder(" + p.a + ", " + p.b + ") = " +
                    its(EP.pairsLogarithmicOrder(p.a, p.b)));
        }
    }

    private static void demoPairsLogarithmicOrder_Iterable_Iterable_infinite() {
        initialize();
        Iterable<Pair<Iterable<Integer>, Iterable<Integer>>> ps = P.pairs(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers()))
        );
        for (Pair<Iterable<Integer>, Iterable<Integer>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("pairsLogarithmicOrder(" + its(p.a) + ", " + its(p.b) + ") = " +
                    its(EP.pairsLogarithmicOrder(p.a, p.b)));
        }
    }

    private static void demoPairsLogarithmicOrder_Iterable_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            String listString = tail(init(xs.toString()));
            System.out.println("pairsLogarithmicOrder(" + listString + ") = " + its(EP.pairsLogarithmicOrder(xs)));
        }
    }

    private static void demoPairsLogarithmicOrder_Iterable_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.withNull(EP.naturalIntegers())))) {
            String listString = tail(init(its(xs)));
            System.out.println("pairsLogarithmicOrder(" + listString + ") = " + its(EP.pairsLogarithmicOrder(xs)));
        }
    }

    private static void demoPairsSquareRootOrder_Iterable_Iterable_finite() {
        initialize();
        Iterable<Pair<List<Integer>, List<Integer>>> ps = P.pairs(
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Pair<List<Integer>, List<Integer>> p : take(LIMIT, ps)) {
            System.out.println("pairsSquareRootOrder(" + p.a + ", " + p.b + ") = " +
                    its(EP.pairsSquareRootOrder(p.a, p.b)));
        }
    }

    private static void demoPairsSquareRootOrder_Iterable_Iterable_infinite() {
        initialize();
        Iterable<Pair<Iterable<Integer>, Iterable<Integer>>> ps = P.pairs(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers()))
        );
        for (Pair<Iterable<Integer>, Iterable<Integer>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("pairsSquareRootOrder(" + its(p.a) + ", " + its(p.b) + ") = " +
                    its(EP.pairsSquareRootOrder(p.a, p.b)));
        }
    }

    private static void demoPairsSquareRootOrder_Iterable_finite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            String listString = tail(init(xs.toString()));
            System.out.println("pairsSquareRootOrder(" + listString + ") = " + its(EP.pairsSquareRootOrder(xs)));
        }
    }

    private static void demoPairsSquareRootOrder_Iterable_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(SMALL_LIMIT, P.prefixPermutations(EP.withNull(EP.naturalIntegers())))) {
            String listString = tail(init(its(xs)));
            System.out.println("pairsSquareRootOrder(" + listString + ") = " + its(EP.pairsSquareRootOrder(xs)));
        }
    }

    private static void demoPermutationsFinite() {
        initialize();
        for (List<Integer> xs : take(LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            String listString = tail(init(xs.toString()));
            System.out.println("permutationsFinite(" + listString + ") = " + its(EP.permutationsFinite(xs)));
        }
    }

    private static void demoStringPermutations() {
        initialize();
        for (String s : take(LIMIT, P.withScale(4).strings())) {
            System.out.println("stringPermutations(" + nicePrint(s) + ") = " +
                    its(map(Testing::nicePrint, EP.stringPermutations(s))));
        }
    }

    private static void demoPrefixPermutations_finite() {
        initialize();
        for (List<Integer> xs : take(SMALL_LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            String listString = tail(init(xs.toString()));
            System.out.println("prefixPermutations(" + listString + ") = " +
                    its(map(Testing::its, EP.prefixPermutations(xs))));
        }
    }

    private static void demoPrefixPermutations_infinite() {
        initialize();
        for (Iterable<Integer> xs : take(TINY_LIMIT, P.prefixPermutations(EP.withNull(EP.naturalIntegers())))) {
            String listString = tail(init(its(xs)));
            System.out.println("prefixPermutations(" + listString + ") = " +
                    its(map(Testing::its, EP.prefixPermutations(xs))));
        }
    }

    private static void demoListsLex_int_Iterable() {
        initialize();
        Iterable<Pair<List<Integer>, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).lists(P.withNull(P.integersGeometric())),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<List<Integer>, Integer> p : take(LIMIT, ps)) {
            System.out.println("listsLex(" + p.b + ", " + p.a + ") = " + its(EP.listsLex(p.b, p.a)));
        }
    }

    private static void demoPairsLex_finite() {
        initialize();
        Iterable<Pair<List<Integer>, List<Integer>>> ps = P.pairs(
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Pair<List<Integer>, List<Integer>> p : take(LIMIT, ps)) {
            System.out.println("pairsLex(" + p.a + ", " + p.b + ") = " + its(EP.pairsLex(p.a, p.b)));
        }
    }

    private static void demoPairsLex_infinite() {
        initialize();
        Iterable<Pair<Iterable<Integer>, List<Integer>>> ps = P.pairs(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Pair<Iterable<Integer>, List<Integer>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("pairsLex(" + its(p.a) + ", " + p.b + ") = " + its(EP.pairsLex(p.a, p.b)));
        }
    }

    private static void demoTriplesLex_finite() {
        initialize();
        Iterable<Triple<List<Integer>, List<Integer>, List<Integer>>> ts = P.triples(
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Triple<List<Integer>, List<Integer>, List<Integer>> t : take(SMALL_LIMIT, ts)) {
            System.out.println("triplesLex(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    its(EP.triplesLex(t.a, t.b, t.c)));
        }
    }

    private static void demoTriplesLex_infinite() {
        initialize();
        Iterable<List<Integer>> finiteArgs = P.withScale(4).lists(P.withNull(P.integersGeometric()));
        Iterable<Triple<Iterable<Integer>, List<Integer>, List<Integer>>> ts = P.triples(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                finiteArgs,
                finiteArgs
        );
        for (Triple<Iterable<Integer>, List<Integer>, List<Integer>> t : take(SMALL_LIMIT, ts)) {
            System.out.println("triplesLex(" + its(t.a) + ", " + t.b + ", " + t.c + ") = " +
                    its(EP.triplesLex(t.a, t.b, t.c)));
        }
    }

    private static void demoQuadruplesLex_finite() {
        initialize();
        Iterable<Quadruple<List<Integer>, List<Integer>, List<Integer>, List<Integer>>> qs = P.quadruples(
                P.withScale(4).lists(P.withNull(P.integersGeometric()))
        );
        for (Quadruple<List<Integer>, List<Integer>, List<Integer>, List<Integer>> q : take(SMALL_LIMIT, qs)) {
            System.out.println("quadruplesLex(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    its(EP.quadruplesLex(q.a, q.b, q.c, q.d)));
        }
    }

    private static void demoQuadruplesLex_infinite() {
        initialize();
        Iterable<List<Integer>> finiteArgs = P.withScale(4).lists(P.withNull(P.integersGeometric()));
        Iterable<Quadruple<Iterable<Integer>, List<Integer>, List<Integer>, List<Integer>>> qs = P.quadruples(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                finiteArgs,
                finiteArgs,
                finiteArgs
        );
        for (Quadruple<Iterable<Integer>, List<Integer>, List<Integer>, List<Integer>> q : take(SMALL_LIMIT, qs)) {
            System.out.println("quadruplesLex(" + its(q.a) + ", " + q.b + ", " + q.c + ", " + q.d + ") = " +
                    its(EP.quadruplesLex(q.a, q.b, q.c, q.d)));
        }
    }

    private static void demoQuintuplesLex_finite() {
        initialize();
        Iterable<Quintuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> qs = P.quintuples(P.withScale(4).lists(P.withNull(P.integersGeometric())));
        for (Quintuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > q : take(SMALL_LIMIT, qs)) {
            System.out.println("quintuplesLex(" + q.a + ", " + q.b + ", " + q.c + ", " + q.d + ", " + q.e + ") = " +
                    its(EP.quintuplesLex(q.a, q.b, q.c, q.d, q.e)));
        }
    }

    private static void demoQuintuplesLex_infinite() {
        initialize();
        Iterable<List<Integer>> finiteArgs = P.withScale(4).lists(P.withNull(P.integersGeometric()));
        Iterable<Quintuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> qs = P.quintuples(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs
        );
        for (Quintuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > q : take(SMALL_LIMIT, qs)) {
            System.out.println(
                    "quintuplesLex(" + its(q.a) + ", " + q.b + ", " + q.c + ", " + q.d + ", " + q.e + ") = " +
                    its(EP.quintuplesLex(q.a, q.b, q.c, q.d, q.e))
            );
        }
    }

    private static void demoSextuplesLex_finite() {
        initialize();
        Iterable<Sextuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> ss = P.sextuples(P.withScale(4).lists(P.withNull(P.integersGeometric())));
        for (Sextuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > s : take(SMALL_LIMIT, ss)) {
            System.out.println(
                    "sextuplesLex(" + s.a + ", " + s.b + ", " + s.c + ", " + s.d + ", " + s.e + ", " + s.f + ") = " +
                    its(EP.sextuplesLex(s.a, s.b, s.c, s.d, s.e, s.f))
            );
        }
    }

    private static void demoSextuplesLex_infinite() {
        initialize();
        Iterable<List<Integer>> finiteArgs = P.withScale(4).lists(P.withNull(P.integersGeometric()));
        Iterable<Sextuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> ss = P.sextuples(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs
        );
        for (Sextuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > s : take(SMALL_LIMIT, ss)) {
            System.out.println(
                    "sextuplesLex(" +
                    its(s.a) + ", " + s.b + ", " + s.c + ", " + s.d + ", " + s.e + ", " + s.f + ") = " +
                    its(EP.sextuplesLex(s.a, s.b, s.c, s.d, s.e, s.f))
            );
        }
    }

    private static void demoSeptuplesLex_finite() {
        initialize();
        Iterable<Septuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> ss = P.septuples(P.withScale(4).lists(P.withNull(P.integersGeometric())));
        for (Septuple<
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > s : take(SMALL_LIMIT, ss)) {
            System.out.println(
                    "septuplesLex(" +
                    s.a + ", " + s.b + ", " + s.c + ", " + s.d + ", " + s.e + ", " + s.f + ", " + s.g + ") = " +
                    its(EP.septuplesLex(s.a, s.b, s.c, s.d, s.e, s.f, s.g))
            );
        }
    }

    private static void demoSeptuplesLex_infinite() {
        initialize();
        Iterable<List<Integer>> finiteArgs = P.withScale(4).lists(P.withNull(P.integersGeometric()));
        Iterable<Septuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        >> ss = P.septuples(
                P.prefixPermutations(EP.withNull(EP.naturalIntegers())),
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs,
                finiteArgs
        );
        for (Septuple<
                Iterable<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>,
                List<Integer>
        > s : take(SMALL_LIMIT, ss)) {
            System.out.println(
                    "septuplesLex(" +
                    its(s.a) + ", " + s.b + ", " + s.c + ", " + s.d + ", " + s.e + ", " + s.f + ", " + s.g + ") = " +
                    its(EP.septuplesLex(s.a, s.b, s.c, s.d, s.e, s.f, s.g))
            );
        }
    }

    private static void demoStringsLex_int_String() {
        initialize();
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).strings(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("stringsLex(" + p.b + ", " + nicePrint(p.a) + ") = " +
                    its(map(Testing::nicePrint, EP.stringsLex(p.b, p.a))));
        }
    }

    private static void demoListsShortlex() {
        initialize();
        for (List<Integer> xs : take(SMALL_LIMIT, P.withScale(4).lists(P.withNull(P.integersGeometric())))) {
            System.out.println("listsShortlex(" + xs + ") = " + its(EP.listsShortlex(xs)));
        }
    }

    private static void demoStringsShortlex() {
        initialize();
        for (String s : take(SMALL_LIMIT, P.withScale(4).strings())) {
            System.out.println("stringsShortlex(" + nicePrint(s) + ") = " +
                    its(map(Testing::nicePrint, EP.stringsShortlex(s))));
        }
    }

    private static void demoListsShortlexAtLeast() {
        initialize();
        Iterable<Pair<List<Integer>, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).lists(P.withNull(P.integersGeometric())),
                P.naturalIntegersGeometric()
        );
        for (Pair<List<Integer>, Integer> p : take(TINY_LIMIT, ps)) {
            System.out.println("listsShortlexAtLeast(" + p.b + ", " + p.a + ") = " +
                    its(EP.listsShortlexAtLeast(p.b, p.a)));
        }
    }

    private static void demoStringsShortlexAtLeast() {
        initialize();
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).strings(),
                P.naturalIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(TINY_LIMIT, ps)) {
            System.out.println("stringsShortlexAtLeast(" + p.b + ", " + nicePrint(p.a) + ") = " +
                    its(map(Testing::nicePrint, EP.stringsShortlexAtLeast(p.b, p.a))));
        }
    }
}
