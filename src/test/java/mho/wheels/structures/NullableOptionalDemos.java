package mho.wheels.structures;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;

import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.structures.NullableOptional.*;

@SuppressWarnings("UnusedDeclaration")
public class NullableOptionalDemos {
    private static final boolean USE_RANDOM = false;
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

    private static void demoOf_Integer() {
        initialize();
        for (Integer i : take(LIMIT, P.withNull(P.integers()))) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private static void demoIsPresent_Integer() {
        initialize();
        for (NullableOptional<Integer> noi : take(LIMIT, P.nullableOptionals(P.withNull(P.integers())))) {
            System.out.println(noi + " is" + (noi.isPresent() ? "" : " not") + " present");
        }
    }

    private static void demoGet_Integer() {
        initialize();
        for (NullableOptional<Integer> noi : take(LIMIT, P.nonEmptyNullableOptionals(P.withNull(P.integers())))) {
            System.out.println("get(" + noi + ") = " + noi.get());
        }
    }

    private static void demoFromOptional_Integer() {
        initialize();
        for (Optional<Integer> oi : take(LIMIT, P.optionals(P.integers()))) {
            System.out.println("fromOptional(" + oi + ") = " + fromOptional(oi));
        }
    }

    private static void demoEquals_NullableOptional_Integer() {
        initialize();
        Iterable<Pair<NullableOptional<Integer>, NullableOptional<Integer>>> nois = P.pairs(
                P.nullableOptionals(P.withNull(P.integers()))
        );
        for (Pair<NullableOptional<Integer>, NullableOptional<Integer>> p : take(LIMIT, nois)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private static void demoEquals_null() {
        initialize();
        for (NullableOptional<Integer> noi : take(LIMIT, P.nullableOptionals(P.withNull(P.integers())))) {
            //noinspection ObjectEqualsNull
            System.out.println(noi + (noi.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private static void demoHashCode_Integer() {
        initialize();
        for (NullableOptional<Integer> noi : take(LIMIT, P.nullableOptionals(P.withNull(P.integers())))) {
            System.out.println("hashCode(" + noi + ") = " + noi.hashCode());
        }
    }

    private static void demoToString_Integer() {
        initialize();
        for (NullableOptional<Integer> noi : take(LIMIT, P.nullableOptionals(P.withNull(P.integers())))) {
            System.out.println(noi);
        }
    }
}