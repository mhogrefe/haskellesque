package mho.wheels.ordering.comparators;

import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.testing.Demos;

import static mho.wheels.iterables.IterableUtils.nub;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class StringShortlexComparatorDemos extends Demos {
    public StringShortlexComparatorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoConstructor() {
        StringShortlexComparator comparator = new StringShortlexComparator();
        for (Pair<String, String> p : take(LIMIT, P.pairs(P.strings()))) {
            System.out.println("new StringShortlexComparator(): " + nicePrint(p.a) + " " +
                    Ordering.fromInt(comparator.compare(p.a, p.b)) + " " + nicePrint(p.b));
        }
    }

    private void demoConstructor_Comparator() {
        Iterable<Pair<String, Pair<String, String>>> ps = P.withElement(
                new Pair<>("", new Pair<>("", "")),
                P.dependentPairsInfinite(
                        P.withScale(4).stringsAtLeast(1),
                        s -> P.pairs(P.withScale(4).strings(nub(s)))
                )
        );
        for (Pair<String, Pair<String, String>> p : take(LIMIT, ps)) {
            StringShortlexComparator comparator = new StringShortlexComparator(new StringBasedComparator(p.a));
            System.out.println("new StringShortlexComparator(new StringBasedComparator(" + nicePrint(p.a) + ")): " +
                    nicePrint(p.b.a) + " " + Ordering.fromInt(comparator.compare(p.b.a, p.b.b)) + " " +
                    nicePrint(p.b.b));
        }
    }
}
