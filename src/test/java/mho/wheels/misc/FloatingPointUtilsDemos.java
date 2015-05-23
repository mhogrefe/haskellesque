package mho.wheels.misc;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;

import static mho.wheels.iterables.IterableUtils.filter;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.misc.FloatingPointUtils.predecessor;
import static mho.wheels.misc.FloatingPointUtils.successor;

@SuppressWarnings("UnusedDeclaration")
public strictfp class FloatingPointUtilsDemos {
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

    private static void demoIsNegativeZero_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println(f + " is " + (FloatingPointUtils.isNegativeZero(f) ? "" : "not ") + "negative zero");
        }
    }

    private static void demoIsNegativeZero_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println(d + " is " + (FloatingPointUtils.isNegativeZero(d) ? "" : "not ") + "negative zero");
        }
    }

    private static void demoSuccessor_float() {
        initialize();
        Iterable<Float> fs = filter(f -> !Float.isNaN(f) && (!Float.isInfinite(f) || f < 0), P.floats());
        for (float f : take(LIMIT, fs)) {
            System.out.println("successor(" + f + ") = " + successor(f));
        }
    }

    private static void demoPredecessor_float() {
        initialize();
        Iterable<Float> fs = filter(f -> !Float.isNaN(f) && (!Float.isInfinite(f) || f > 0), P.floats());
        for (float f : take(LIMIT, fs)) {
            System.out.println("predecessor(" + f + ") = " + predecessor(f));
        }
    }

    private static void demoSuccessor_double() {
        initialize();
        Iterable<Double> ds = filter(d -> !Double.isNaN(d) && (!Double.isInfinite(d) || d < 0), P.doubles());
        for (double d : take(LIMIT, ds)) {
            System.out.println("successor(" + d + ") = " + successor(d));
        }
    }

    private static void demoPredecessor_double() {
        initialize();
        Iterable<Double> ds = filter(d -> !Double.isNaN(d) && (!Double.isInfinite(d) || d > 0), P.doubles());
        for (double d : take(LIMIT, ds)) {
            System.out.println("predecessor(" + d + ") = " + predecessor(d));
        }
    }
}
