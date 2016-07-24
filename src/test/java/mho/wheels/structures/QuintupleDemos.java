package mho.wheels.structures;

import mho.wheels.io.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.testing.Demos;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static mho.wheels.testing.Testing.EP;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class QuintupleDemos extends Demos {
    private static final @NotNull String NULLABLE_INTEGER_QUINTUPLE_CHARS = " (),-0123456789lnu";

    public QuintupleDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoConstructor() {
        for (Quintuple<Integer, Integer, Integer, Integer, Integer> q :
                take(LIMIT, P.quintuples(P.withNull(P.integers())))) {
            System.out.println("new Quintuple<Integer, Integer, Integer, Integer, Integer>(" + q.a + ", " + q.b +
                    ", " + q.c + ", " + q.d + ", " + q.e + ") = " + new Quintuple<>(q.a, q.b, q.c, q.d, q.e));
        }
    }

    private void demoToList() {
        for (Quintuple<Integer, Integer, Integer, Integer, Integer> q :
                take(LIMIT, P.quintuples(P.withNull(P.integers())))) {
            System.out.println("toList" + q + " = " + Quintuple.toList(q));
        }
    }

    private void demoFromList() {
        for (List<Integer> xs : take(LIMIT, P.lists(5, P.withNull(P.integers())))) {
            System.out.println("fromList(" + middle(xs.toString()) + ") = " + Quintuple.fromList(xs));
        }
    }

    private void demoCompare() {
        Iterable<
                Pair<
                        Quintuple<Integer, Integer, Integer, Integer, Integer>,
                        Quintuple<Integer, Integer, Integer, Integer, Integer>
                >
        > ps = P.pairs(P.quintuples(P.integers()));
        for (Pair<
                Quintuple<Integer, Integer, Integer, Integer, Integer>,
                Quintuple<Integer, Integer, Integer, Integer, Integer>
        > p : take(LIMIT, ps)) {
            System.out.println("compare(" + p.a + ", " + p.b + ") = " + Quintuple.compare(p.a, p.b));
        }
    }

    private void demoEquals_Quintuple() {
        Iterable<
                Pair<
                        Quintuple<Integer, Integer, Integer, Integer, Integer>,
                        Quintuple<Integer, Integer, Integer, Integer, Integer>
                >
        > ps = P.pairs(P.quintuples(P.withNull(P.integers())));
        for (Pair<
                Quintuple<Integer, Integer, Integer, Integer, Integer>,
                Quintuple<Integer, Integer, Integer, Integer, Integer>
        > p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Quintuple<Integer, Integer, Integer, Integer, Integer> q :
                take(LIMIT, P.quintuples(P.withNull(P.integers())))) {
            //noinspection ObjectEqualsNull
            System.out.println(q + (q.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Quintuple<Integer, Integer, Integer, Integer, Integer> q :
                take(LIMIT, P.quintuples(P.withNull(P.integers())))) {
            System.out.println("hashCode" + q + " = " + q.hashCode());
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println(
                    "readStrict(" + nicePrint(s) + ") = " +
                    Quintuple.readStrict(
                            s,
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict)
                    )
            );
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(NULLABLE_INTEGER_QUINTUPLE_CHARS))) {
            System.out.println(
                    "readStrict(" + nicePrint(s) + ") = " +
                    Quintuple.readStrict(
                            s,
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict),
                            Readers.readWithNullsStrict(Readers::readIntegerStrict)
                    )
            );
        }
    }

    private void demoQuintupleComparator_compare() {
        Iterable<
                Pair<
                        Pair<
                                Quintuple<Integer, Integer, Integer, Integer, Integer>,
                                Quintuple<Integer, Integer, Integer, Integer, Integer>
                        >,
                        Quintuple<List<Integer>, List<Integer>, List<Integer>, List<Integer>, List<Integer>>
                >
        > ps = P.dependentPairs(
                P.pairs(P.quintuples(P.withNull(P.integersGeometric()))),
                p -> P.quintuples(
                        EP.permutationsFinite(toList(nub(concat(Quintuple.toList(p.a), Quintuple.toList(p.b)))))
                )
        );
        for (Pair<
                Pair<
                        Quintuple<Integer, Integer, Integer, Integer, Integer>,
                        Quintuple<Integer, Integer, Integer, Integer, Integer>
                >,
                Quintuple<List<Integer>, List<Integer>, List<Integer>, List<Integer>, List<Integer>>
        > p : take(MEDIUM_LIMIT, ps)) {
            Comparator<Integer> aComparator = (x, y) -> {
                int xIndex = p.b.a.indexOf(x);
                if (xIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                int yIndex = p.b.a.indexOf(y);
                if (yIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                return Integer.compare(xIndex, yIndex);
            };
            Comparator<Integer> bComparator = (x, y) -> {
                int xIndex = p.b.b.indexOf(x);
                if (xIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                int yIndex = p.b.b.indexOf(y);
                if (yIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                return Integer.compare(xIndex, yIndex);
            };
            Comparator<Integer> cComparator = (x, y) -> {
                int xIndex = p.b.c.indexOf(x);
                if (xIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                int yIndex = p.b.c.indexOf(y);
                if (yIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                return Integer.compare(xIndex, yIndex);
            };
            Comparator<Integer> dComparator = (x, y) -> {
                int xIndex = p.b.d.indexOf(x);
                if (xIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                int yIndex = p.b.d.indexOf(y);
                if (yIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                return Integer.compare(xIndex, yIndex);
            };
            Comparator<Integer> eComparator = (x, y) -> {
                int xIndex = p.b.e.indexOf(x);
                if (xIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                int yIndex = p.b.e.indexOf(y);
                if (yIndex == -1) {
                    throw new IllegalArgumentException("undefined");
                }
                return Integer.compare(xIndex, yIndex);
            };
            System.out.println("new QuintupleComparator(" +
                    intercalate(" < ", map(Objects::toString, p.b.a)) + ", " +
                    intercalate(" < ", map(Objects::toString, p.b.b)) + ", " +
                    intercalate(" < ", map(Objects::toString, p.b.c)) + ", " +
                    intercalate(" < ", map(Objects::toString, p.b.d)) + ", " +
                    intercalate(" < ", map(Objects::toString, p.b.e)) +
                    "): " + p.a.a + " " +
                    Ordering.fromInt(
                            new Quintuple.QuintupleComparator<>(
                                    aComparator,
                                    bComparator,
                                    cComparator,
                                    dComparator,
                                    eComparator
                            ).compare(p.a.a, p.a.b)
                    ) + " " + p.a.b);
        }
    }
}
