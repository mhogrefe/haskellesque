package mho.wheels.iterables.randomProvider;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.random.IsaacPRNG;
import mho.wheels.structures.NullableOptional;
import mho.wheels.testing.Testing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;

// @formatter:off
public strictfp class CompoundTest {
    private static RandomProvider P;
    private static final int DEFAULT_SAMPLE_SIZE = 1000000;
    private static final int DEFAULT_TOP_COUNT = 10;
    private static final int TINY_LIMIT = 20;

    @Before
    public void initialize() {
        P = RandomProvider.example();
    }

    private static void withElement_helper(
            int scale,
            @NotNull String input,
            @Nullable Integer element,
            @NotNull String output,
            double elementFrequency
    ) {
        Iterable<Integer> xs = P.withScale(scale).withElement(element, cycle(readIntegerListWithNulls(input)));
        List<Integer> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqit(take(TINY_LIMIT, sample), output);
        aeq(meanOfIntegers(toList(map(x -> Objects.equals(x, element) ? 1 : 0, sample))), elementFrequency);
        P.reset();
    }

    @Test
    public void testWithElement() {
        withElement_helper(
                2,
                "[1]",
                null,
                "[1, null, 1, 1, 1, 1, 1, 1, 1, null, 1, null, null, 1, null, null, 1, 1, null, 1]",
                0.4992549999935604
        );
        withElement_helper(
                8,
                "[1]",
                null,
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]",
                0.12480700000010415
        );
        withElement_helper(
                32,
                "[1]",
                null,
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]",
                0.031218000000010567
        );
        withElement_helper(
                2,
                "[null, 2, 3]",
                10,
                "[null, 10, 2, 3, null, 2, 3, null, 2, 10, 3, 10, 10, null, 10, 10, 2, 3, 10, null]",
                0.4992549999935604
        );
        withElement_helper(
                8,
                "[null, 2, 3]",
                10,
                "[null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2]",
                0.12480700000010415
        );
        withElement_helper(
                32,
                "[null, 2, 3]",
                10,
                "[null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2, 3, null, 2]",
                0.031218000000010567
        );
        try {
            toList(P.withElement(null, readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withScale(1).withElement(null, cycle(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalStateException ignored) {}
    }

    private static void withNull_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double nullFrequency
    ) {
        Iterable<Integer> xs = P.withScale(scale).withNull(cycle(readIntegerListWithNulls(input)));
        List<Integer> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqit(take(TINY_LIMIT, sample), output);
        aeq(meanOfIntegers(toList(map(x -> x == null ? 1 : 0, sample))), nullFrequency);
        P.reset();
    }

    @Test
    public void testWithNull() {
        withNull_helper(
                2,
                "[1]",
                "[1, null, 1, 1, 1, 1, 1, 1, 1, null, 1, null, null, 1, null, null, 1, 1, null, 1]",
                0.4992549999935604
        );
        withNull_helper(8, "[1]", "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]", 0.12480700000010415);
        withNull_helper(
                32,
                "[1]",
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]",
                0.031218000000010567
        );
        withNull_helper(
                2,
                "[1, 2, 3]",
                "[1, null, 2, 3, 1, 2, 3, 1, 2, null, 3, null, null, 1, null, null, 2, 3, null, 1]",
                0.4992549999935604
        );
        withNull_helper(
                8,
                "[1, 2, 3]",
                "[1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2]",
                0.12480700000010415
        );
        withNull_helper(
                32,
                "[1, 2, 3]",
                "[1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2]",
                0.031218000000010567
        );
        try {
            toList(P.withNull(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withScale(1).withNull(cycle(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalStateException ignored) {}
    }

    private static void optionalsHelper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double emptyFrequency
    ) {
        Iterable<Optional<Integer>> xs = P.withScale(scale).optionals(cycle(readIntegerListWithNulls(input)));
        List<Optional<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqit(take(TINY_LIMIT, sample), output);
        aeq(meanOfIntegers(toList(map(x -> x.isPresent() ? 0 : 1, sample))), emptyFrequency);
        P.reset();
    }

    @Test
    public void testOptionals() {
        optionalsHelper(
                2,
                "[1]",
                "[Optional[1], Optional.empty, Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]," +
                " Optional[1], Optional[1], Optional.empty, Optional[1], Optional.empty, Optional.empty," +
                " Optional[1], Optional.empty, Optional.empty, Optional[1], Optional[1], Optional.empty, Optional[1]]",
                0.4992549999935604
        );
        optionalsHelper(
                8,
                "[1]",
                "[Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]," +
                " Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]," +
                " Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]]",
                0.12480700000010415
        );
        optionalsHelper(
                32,
                "[1]",
                "[Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]," +
                " Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]," +
                " Optional[1], Optional[1], Optional[1], Optional[1], Optional[1], Optional[1]]",
                0.031218000000010567
        );
        optionalsHelper(
                2,
                "[1, 2, 3]",
                "[Optional[1], Optional.empty, Optional[2], Optional[3], Optional[1], Optional[2], Optional[3]," +
                " Optional[1], Optional[2], Optional.empty, Optional[3], Optional.empty, Optional.empty," +
                " Optional[1], Optional.empty, Optional.empty, Optional[2], Optional[3], Optional.empty, Optional[1]]",
                0.4992549999935604
        );
        optionalsHelper(
                8,
                "[1, 2, 3]",
                "[Optional[1], Optional[2], Optional[3], Optional[1], Optional[2], Optional[3], Optional[1]," +
                " Optional[2], Optional[3], Optional[1], Optional[2], Optional[3], Optional[1], Optional[2]," +
                " Optional[3], Optional[1], Optional[2], Optional[3], Optional[1], Optional[2]]",
                0.12480700000010415
        );
        optionalsHelper(
                32,
                "[1, 2, 3]",
                "[Optional[1], Optional[2], Optional[3], Optional[1], Optional[2], Optional[3], Optional[1]," +
                " Optional[2], Optional[3], Optional[1], Optional[2], Optional[3], Optional[1], Optional[2]," +
                " Optional[3], Optional[1], Optional[2], Optional[3], Optional[1], Optional[2]]",
                0.031218000000010567
        );
        try {
            toList(P.optionals(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withScale(1).optionals(cycle(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalStateException ignored) {}
    }

    private static void nullableOptionals_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double emptyFrequency
    ) {
        Iterable<NullableOptional<Integer>> xs = P.withScale(scale)
                .nullableOptionals(cycle(readIntegerListWithNulls(input)));
        List<NullableOptional<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqit(take(TINY_LIMIT, sample), output);
        aeq(meanOfIntegers(toList(map(x -> x.isPresent() ? 0 : 1, sample))), emptyFrequency);
        P.reset();
    }

    @Test
    public void testNullableOptionals() {
        nullableOptionals_helper(
                2,
                "[1]",
                "[NullableOptional[1], NullableOptional.empty, NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional.empty, NullableOptional[1], NullableOptional.empty," +
                " NullableOptional.empty, NullableOptional[1], NullableOptional.empty, NullableOptional.empty," +
                " NullableOptional[1], NullableOptional[1], NullableOptional.empty, NullableOptional[1]]",
                0.4992549999935604
        );
        nullableOptionals_helper(
                8,
                "[1]",
                "[NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]]",
                0.12480700000010415
        );
        nullableOptionals_helper(
                32,
                "[1]",
                "[NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]," +
                " NullableOptional[1], NullableOptional[1], NullableOptional[1], NullableOptional[1]]",
                0.031218000000010567
        );
        nullableOptionals_helper(
                2,
                "[null, 2, 3]",
                "[NullableOptional[null], NullableOptional.empty, NullableOptional[2], NullableOptional[3]," +
                " NullableOptional[null], NullableOptional[2], NullableOptional[3], NullableOptional[null]," +
                " NullableOptional[2], NullableOptional.empty, NullableOptional[3], NullableOptional.empty," +
                " NullableOptional.empty, NullableOptional[null], NullableOptional.empty, NullableOptional.empty," +
                " NullableOptional[2], NullableOptional[3], NullableOptional.empty, NullableOptional[null]]",
                0.4992549999935604
        );
        nullableOptionals_helper(
                8,
                "[null, 2, 3]",
                "[NullableOptional[null], NullableOptional[2], NullableOptional[3], NullableOptional[null]," +
                " NullableOptional[2], NullableOptional[3], NullableOptional[null], NullableOptional[2]," +
                " NullableOptional[3], NullableOptional[null], NullableOptional[2], NullableOptional[3]," +
                " NullableOptional[null], NullableOptional[2], NullableOptional[3], NullableOptional[null]," +
                " NullableOptional[2], NullableOptional[3], NullableOptional[null], NullableOptional[2]]",
                0.12480700000010415
        );
        nullableOptionals_helper(
                32,
                "[null, 2, 3]",
                "[NullableOptional[null], NullableOptional[2], NullableOptional[3], NullableOptional[null]," +
                " NullableOptional[2], NullableOptional[3], NullableOptional[null], NullableOptional[2]," +
                " NullableOptional[3], NullableOptional[null], NullableOptional[2], NullableOptional[3]," +
                " NullableOptional[null], NullableOptional[2], NullableOptional[3], NullableOptional[null]," +
                " NullableOptional[2], NullableOptional[3], NullableOptional[null], NullableOptional[2]]",
                0.031218000000010567
        );
        try {
            toList(P.nullableOptionals(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withScale(1).nullableOptionals(cycle(readIntegerListWithNulls("[1, 2, 3]")));
            fail();
        } catch (IllegalStateException ignored) {}
    }

    @Test
    public void dependentPairsInfiniteTest() {
        aeqitLimit(
                TINY_LIMIT,
                P.dependentPairsInfinite(P.range(1, 5), i -> P.strings(i, charsToString(range('a', 'z')))),
                "[(2, ds), (4, rhvt), (1, v), (2, wv), (5, kpzex), (3, hje), (4, mfse), (1, d), (3, fpo), (3, tgw)," +
                " (1, m), (1, y), (1, o), (3, dpl), (1, j), (5, sofgp), (4, pttf), (4, lszp), (2, dr), (3, fvx), ...]"
        );
        P.reset();

        try {
            toList(P.dependentPairsInfinite(P.range(1, 5), i -> null));
            fail();
        } catch (NullPointerException ignored) {}

        try {
            toList(
                    P.dependentPairsInfinite(
                            ExhaustiveProvider.INSTANCE.range(1, 5),
                            i -> P.strings(i, charsToString(range('a', 'z')))
                    )
            );
            fail();
        } catch (NoSuchElementException ignored) {}

        try {
            toList(
                    P.dependentPairsInfinite(
                            P.range(1, 5),
                            i -> ExhaustiveProvider.INSTANCE.range('a', 'z')
                    )
            );
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    private static void shuffle_helper(@NotNull String input, @NotNull String output) {
        shuffle_helper(readIntegerListWithNulls(input), output);
    }

    private static void shuffle_helper(@NotNull List<Integer> input, @NotNull String output) {
        List<Integer> xs = toList(input);
        P.shuffle(xs);
        aeqit(xs, output);
        P.reset();
    }

    @Test
    public void testShuffle() {
        shuffle_helper("[]", "[]");
        shuffle_helper("[5]", "[5]");
        shuffle_helper("[1, 2]", "[2, 1]");
        shuffle_helper("[1, 2, 3]", "[2, 1, 3]");
        shuffle_helper("[1, 2, 3, 4]", "[2, 4, 1, 3]");
        shuffle_helper("[1, 2, 2, 4]", "[2, 4, 1, 2]");
        shuffle_helper("[2, 2, 2, 2]", "[2, 2, 2, 2]");
        shuffle_helper("[3, 1, 4, 1]", "[1, 1, 3, 4]");
        shuffle_helper("[3, 1, null, 1]", "[1, 1, 3, null]");
        shuffle_helper(toList(IterableUtils.range(1, 10)), "[10, 4, 1, 9, 8, 7, 5, 2, 3, 6]");
    }

    private static void permutationsFinite_helper(
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        permutationsFinite_helper(readIntegerListWithNulls(input), output, topSampleCount);
    }

    private static void permutationsFinite_helper(
            @NotNull List<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.permutationsFinite(input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    @Test
    public void testPermutationsFinite() {
        permutationsFinite_helper(
                "[]",
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}");
        permutationsFinite_helper(
                "[5]",
                "[[5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5]," +
                " [5], ...]",
                "{[5]=1000000}");
        permutationsFinite_helper(
                "[1, 2]",
                "[[2, 1], [1, 2], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [1, 2], [2, 1], [1, 2]," +
                " [1, 2], [2, 1], [1, 2], [1, 2], [2, 1], [2, 1], [1, 2], [2, 1], ...]",
                "{[2, 1]=500745, [1, 2]=499255}");
        permutationsFinite_helper(
                "[1, 2, 3]",
                "[[2, 1, 3], [2, 3, 1], [2, 3, 1], [2, 3, 1], [3, 1, 2], [1, 2, 3], [3, 2, 1], [2, 3, 1], [3, 1, 2]," +
                " [3, 1, 2], [1, 3, 2], [2, 1, 3], [1, 3, 2], [2, 1, 3], [3, 2, 1], [2, 1, 3], [2, 1, 3], [1, 2, 3]," +
                " [1, 2, 3], [3, 2, 1], ...]",
                "{[2, 3, 1]=167387, [3, 2, 1]=167243, [1, 3, 2]=166538, [1, 2, 3]=166496, [3, 1, 2]=166232," +
                " [2, 1, 3]=166104}");
        permutationsFinite_helper(
                "[1, 2, 3, 4]",
                "[[2, 4, 1, 3], [2, 3, 4, 1], [2, 3, 1, 4], [2, 1, 3, 4], [4, 1, 3, 2], [2, 4, 1, 3], [3, 1, 2, 4]," +
                " [4, 3, 2, 1], [1, 3, 2, 4], [3, 4, 2, 1], [3, 1, 2, 4], [1, 4, 3, 2], [1, 4, 3, 2], [4, 3, 1, 2]," +
                " [2, 1, 3, 4], [3, 4, 2, 1], [4, 3, 2, 1], [4, 1, 3, 2], [4, 2, 1, 3], [3, 4, 1, 2], ...]",
                "{[4, 2, 3, 1]=42026, [2, 3, 1, 4]=42012, [4, 1, 3, 2]=41883, [1, 4, 3, 2]=41846," +
                " [3, 2, 4, 1]=41820, [4, 3, 1, 2]=41782, [3, 1, 4, 2]=41776, [3, 4, 1, 2]=41771," +
                " [2, 1, 3, 4]=41764, [4, 3, 2, 1]=41745}");
        permutationsFinite_helper(
                "[1, 2, 2, 4]",
                "[[2, 4, 1, 2], [2, 2, 4, 1], [2, 2, 1, 4], [2, 1, 2, 4], [4, 1, 2, 2], [2, 4, 1, 2], [2, 1, 2, 4]," +
                " [4, 2, 2, 1], [1, 2, 2, 4], [2, 4, 2, 1], [2, 1, 2, 4], [1, 4, 2, 2], [1, 4, 2, 2], [4, 2, 1, 2]," +
                " [2, 1, 2, 4], [2, 4, 2, 1], [4, 2, 2, 1], [4, 1, 2, 2], [4, 2, 1, 2], [2, 4, 1, 2], ...]",
                "{[4, 2, 2, 1]=83771, [2, 2, 1, 4]=83554, [2, 2, 4, 1]=83502, [2, 4, 1, 2]=83498," +
                " [4, 1, 2, 2]=83476, [1, 4, 2, 2]=83417, [2, 1, 4, 2]=83341, [4, 2, 1, 2]=83271," +
                " [2, 4, 2, 1]=83193, [2, 1, 2, 4]=83115}");
        permutationsFinite_helper(
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], ...]",
                "{[2, 2, 2, 2]=1000000}");
        permutationsFinite_helper(
                "[3, 1, 4, 1]",
                "[[1, 1, 3, 4], [1, 4, 1, 3], [1, 4, 3, 1], [1, 3, 4, 1], [1, 3, 4, 1], [1, 1, 3, 4], [4, 3, 1, 1]," +
                " [1, 4, 1, 3], [3, 4, 1, 1], [4, 1, 1, 3], [4, 3, 1, 1], [3, 1, 4, 1], [3, 1, 4, 1], [1, 4, 3, 1]," +
                " [1, 3, 4, 1], [4, 1, 1, 3], [1, 4, 1, 3], [1, 3, 4, 1], [1, 1, 3, 4], [4, 1, 3, 1], ...]",
                "{[1, 4, 3, 1]=83794, [1, 1, 4, 3]=83659, [1, 3, 4, 1]=83647, [1, 4, 1, 3]=83427," +
                " [4, 1, 1, 3]=83380, [3, 1, 4, 1]=83325, [4, 1, 3, 1]=83313, [1, 1, 3, 4]=83216," +
                " [1, 3, 1, 4]=83158, [4, 3, 1, 1]=83127}");
        permutationsFinite_helper(
                "[3, 1, null, 1]",
                "[[1, 1, 3, null], [1, null, 1, 3], [1, null, 3, 1], [1, 3, null, 1], [1, 3, null, 1]," +
                " [1, 1, 3, null], [null, 3, 1, 1], [1, null, 1, 3], [3, null, 1, 1], [null, 1, 1, 3]," +
                " [null, 3, 1, 1], [3, 1, null, 1], [3, 1, null, 1], [1, null, 3, 1], [1, 3, null, 1]," +
                " [null, 1, 1, 3], [1, null, 1, 3], [1, 3, null, 1], [1, 1, 3, null], [null, 1, 3, 1], ...]",
                "{[1, null, 3, 1]=83794, [1, 1, null, 3]=83659, [1, 3, null, 1]=83647, [1, null, 1, 3]=83427," +
                " [null, 1, 1, 3]=83380, [3, 1, null, 1]=83325, [null, 1, 3, 1]=83313, [1, 1, 3, null]=83216," +
                " [1, 3, 1, null]=83158, [null, 3, 1, 1]=83127}");
        permutationsFinite_helper(
                toList(IterableUtils.range(1, 10)),
                "[[10, 4, 1, 9, 8, 7, 5, 2, 3, 6], [7, 3, 1, 10, 2, 5, 4, 6, 8, 9], [3, 6, 2, 9, 4, 1, 10, 5, 8, 7]," +
                " [3, 8, 2, 6, 10, 1, 7, 5, 9, 4], [5, 4, 10, 1, 6, 3, 9, 2, 8, 7], [7, 1, 6, 2, 10, 9, 3, 8, 5, 4]," +
                " [2, 8, 5, 10, 3, 1, 4, 6, 9, 7], [5, 8, 4, 6, 2, 1, 7, 10, 3, 9], [3, 9, 2, 10, 4, 1, 6, 8, 7, 5]," +
                " [7, 2, 3, 1, 8, 10, 6, 5, 9, 4], [4, 8, 9, 7, 5, 2, 3, 6, 1, 10], [9, 2, 1, 5, 3, 7, 6, 4, 10, 8]," +
                " [3, 4, 9, 5, 10, 7, 6, 8, 2, 1], [9, 6, 4, 10, 5, 2, 3, 8, 1, 7], [4, 2, 9, 1, 6, 5, 3, 7, 10, 8]," +
                " [3, 1, 7, 5, 8, 9, 4, 6, 2, 10], [9, 8, 2, 6, 4, 5, 10, 7, 3, 1], [9, 2, 7, 3, 5, 10, 1, 6, 4, 8]," +
                " [10, 3, 2, 1, 6, 7, 8, 4, 9, 5], [3, 6, 2, 1, 10, 8, 9, 5, 7, 4], ...]",
                "{[7, 4, 2, 6, 9, 3, 1, 5, 10, 8]=6, [10, 9, 5, 3, 8, 1, 7, 2, 6, 4]=5," +
                " [5, 8, 1, 10, 6, 3, 9, 4, 7, 2]=5, [3, 9, 6, 4, 1, 10, 5, 7, 8, 2]=5," +
                " [4, 1, 6, 5, 8, 10, 3, 7, 2, 9]=5, [8, 5, 6, 2, 7, 9, 4, 3, 1, 10]=5," +
                " [1, 3, 4, 6, 2, 5, 9, 10, 8, 7]=5, [4, 5, 6, 7, 1, 10, 3, 8, 2, 9]=5," +
                " [5, 3, 6, 2, 8, 10, 1, 9, 7, 4]=5, [3, 10, 4, 7, 8, 9, 1, 5, 6, 2]=5}");
    }

    private static void stringPermutations_helper(
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringPermutations(input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    @Test
    public void testStringPermutations() {
        stringPermutations_helper("", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        stringPermutations_helper(
                "a",
                "[a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, ...]",
                "{a=1000000}");
        stringPermutations_helper(
                "abc",
                "[bac, bca, bca, bca, cab, abc, cba, bca, cab, cab, acb, bac, acb, bac, cba, bac, bac, abc, abc," +
                " cba, ...]",
                "{bca=167387, cba=167243, acb=166538, abc=166496, cab=166232, bac=166104}");
        stringPermutations_helper(
                "foo",
                "[ofo, oof, oof, oof, ofo, foo, oof, oof, ofo, ofo, foo, ofo, foo, ofo, oof, ofo, ofo, foo, foo," +
                " oof, ...]",
                "{oof=334630, foo=333034, ofo=332336}");
        stringPermutations_helper(
                "hello",
                "[elhol, elloh, eholl, lhoel, lheol, oellh, lleho, leolh, olhle, ellho, loehl, lohle, lhloe, lehlo," +
                " llheo, olleh, elohl, loehl, lhleo, helol, ...]",
                "{elolh=16971, lehlo=16937, lhloe=16931, llhoe=16917, leohl=16876, lleoh=16866, ollhe=16835," +
                " olhel=16828, lleho=16806, leolh=16802}");
        stringPermutations_helper(
                "Mississippi",
                "[psissisiipM, iMpssissipi, Mpipsiiisss, ipsisiipssM, iiissMpspsi, iiipsMispss, psiisiMspsi," +
                " sisMipiissp, siisspipiMs, piiMsssisip, ssMiipisspi, piisiiMssps, Mispspsiisi, iisssMisppi," +
                " sspsspMiiii, sipssiMspii, sipipissiMs, iissipisMps, isipiMsssip, siMipiipsss, ...]",
                "{iipssMiissp=54, iisMpissips=52, iisMsspiips=52, ssispiiMpis=51, spMsisiipsi=51, iMspiipssis=51," +
                " ssisMppiiis=50, sMsipssiipi=50, spisiiiMpss=50, sipMspsiisi=50}");
    }

    private static void prefixPermutations_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        prefixPermutations_helper(scale, readIntegerListWithNulls(input), output, topSampleCount);
    }

    private static void prefixPermutations_helper(
            int scale,
            @NotNull List<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, map(IterableUtils::toList, P.withScale(scale).prefixPermutations(input)))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    private static void prefixPermutations_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, map(Testing::its, P.withScale(scale).prefixPermutations(input)))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    private static void prefixPermutations_fail_helper(int scale, @NotNull String input) {
        try {
            toList(P.withScale(scale).prefixPermutations(readIntegerListWithNulls(input)));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPrefixPermutations() {
        prefixPermutations_helper(
                1,
                "[]",
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}"
        );
        prefixPermutations_helper(
                2,
                "[]",
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}"
        );
        prefixPermutations_helper(
                4,
                "[]",
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}"
        );
        prefixPermutations_helper(
                1,
                "[5]",
                "[[5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5]," +
                " [5], ...]",
                "{[5]=1000000}"
        );
        prefixPermutations_helper(
                2,
                "[5]",
                "[[5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5]," +
                " [5], ...]",
                "{[5]=1000000}"
        );
        prefixPermutations_helper(
                4,
                "[5]",
                "[[5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5], [5]," +
                " [5], ...]",
                "{[5]=1000000}"
        );
        prefixPermutations_helper(
                1,
                "[1, 2]",
                "[[2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [1, 2], [2, 1], [2, 1], [1, 2], [1, 2], [1, 2]," +
                " [2, 1], [1, 2], [1, 2], [1, 2], [1, 2], [2, 1], [1, 2], [2, 1], ...]",
                "{[1, 2]=500128, [2, 1]=499872}"
        );
        prefixPermutations_helper(
                2,
                "[1, 2]",
                "[[2, 1], [1, 2], [1, 2], [1, 2], [1, 2], [1, 2], [1, 2], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1]," +
                " [1, 2], [2, 1], [1, 2], [1, 2], [1, 2], [2, 1], [1, 2], [2, 1], ...]",
                "{[2, 1]=500320, [1, 2]=499680}"
        );
        prefixPermutations_helper(
                4,
                "[1, 2]",
                "[[1, 2], [1, 2], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [2, 1], [1, 2], [2, 1], [1, 2]," +
                " [1, 2], [1, 2], [1, 2], [2, 1], [1, 2], [1, 2], [1, 2], [2, 1], ...]",
                "{[1, 2]=500657, [2, 1]=499343}"
        );
        prefixPermutations_helper(
                1,
                "[1, 2, 3]",
                "[[2, 3, 1], [3, 1, 2], [1, 3, 2], [2, 1, 3], [3, 1, 2], [2, 3, 1], [3, 2, 1], [3, 2, 1], [1, 2, 3]," +
                " [1, 3, 2], [2, 3, 1], [1, 3, 2], [1, 2, 3], [2, 3, 1], [3, 1, 2], [3, 1, 2], [2, 1, 3], [1, 3, 2]," +
                " [3, 1, 2], [1, 2, 3], ...]",
                "{[1, 3, 2]=166994, [3, 1, 2]=166913, [2, 3, 1]=166781, [3, 2, 1]=166581, [1, 2, 3]=166397" +
                ", [2, 1, 3]=166334}"
        );
        prefixPermutations_helper(
                2,
                "[1, 2, 3]",
                "[[3, 2, 1], [3, 1, 2], [3, 2, 1], [3, 2, 1], [1, 3, 2], [2, 3, 1], [1, 2, 3], [3, 1, 2], [3, 1, 2]," +
                " [2, 3, 1], [3, 1, 2], [1, 3, 2], [2, 1, 3], [2, 3, 1], [1, 2, 3], [2, 1, 3], [2, 1, 3], [2, 3, 1]," +
                " [1, 2, 3], [2, 1, 3], ...]",
                "{[1, 2, 3]=167294, [2, 1, 3]=166661, [3, 2, 1]=166629, [2, 3, 1]=166619, [3, 1, 2]=166593" +
                ", [1, 3, 2]=166204}"
        );
        prefixPermutations_helper(
                4,
                "[1, 2, 3]",
                "[[2, 3, 1], [3, 1, 2], [2, 3, 1], [1, 2, 3], [3, 1, 2], [2, 3, 1], [2, 3, 1], [1, 2, 3], [1, 3, 2]," +
                " [3, 2, 1], [2, 1, 3], [1, 3, 2], [1, 3, 2], [1, 2, 3], [2, 1, 3], [3, 2, 1], [1, 2, 3], [2, 3, 1]," +
                " [2, 3, 1], [2, 3, 1], ...]",
                "{[3, 1, 2]=167085, [1, 3, 2]=167081, [2, 3, 1]=166636, [1, 2, 3]=166544, [2, 1, 3]=166445" +
                ", [3, 2, 1]=166209}"
        );
        prefixPermutations_helper(
                1,
                "[1, 2, 3, 4]",
                "[[2, 3, 1, 4], [4, 1, 2, 3], [1, 4, 3, 2], [4, 1, 2, 3], [2, 4, 3, 1], [2, 4, 3, 1], [1, 3, 2, 4]," +
                " [3, 1, 2, 4], [1, 3, 4, 2], [3, 1, 4, 2], [2, 1, 4, 3], [4, 3, 1, 2], [1, 4, 2, 3], [1, 3, 4, 2]," +
                " [1, 2, 4, 3], [2, 1, 4, 3], [3, 4, 1, 2], [1, 3, 4, 2], [2, 1, 3, 4], [1, 3, 2, 4], ...]",
                "{[3, 1, 2, 4]=42143, [3, 4, 1, 2]=41973, [2, 1, 4, 3]=41966, [1, 3, 4, 2]=41863," +
                " [4, 3, 2, 1]=41814, [2, 3, 1, 4]=41774, [2, 1, 3, 4]=41765, [3, 1, 4, 2]=41743," +
                " [4, 3, 1, 2]=41704, [4, 1, 2, 3]=41663}"
        );
        prefixPermutations_helper(
                2,
                "[1, 2, 3, 4]",
                "[[2, 1, 4, 3], [4, 2, 1, 3], [3, 4, 1, 2], [3, 1, 2, 4], [3, 2, 4, 1], [2, 3, 1, 4], [3, 4, 1, 2]," +
                " [1, 3, 2, 4], [3, 1, 4, 2], [1, 2, 4, 3], [2, 3, 4, 1], [3, 2, 4, 1], [4, 1, 2, 3], [4, 1, 3, 2]," +
                " [1, 3, 2, 4], [4, 3, 2, 1], [2, 1, 4, 3], [1, 2, 3, 4], [3, 2, 1, 4], [3, 4, 1, 2], ...]",
                "{[3, 2, 1, 4]=42069, [4, 2, 1, 3]=42028, [3, 4, 2, 1]=42017, [4, 1, 2, 3]=41899," +
                " [2, 1, 4, 3]=41847, [1, 3, 4, 2]=41824, [2, 4, 1, 3]=41808, [2, 4, 3, 1]=41726," +
                " [1, 2, 3, 4]=41711, [3, 4, 1, 2]=41707}"
        );
        prefixPermutations_helper(
                4,
                "[1, 2, 3, 4]",
                "[[2, 1, 4, 3], [3, 4, 1, 2], [4, 2, 1, 3], [1, 3, 2, 4], [1, 3, 4, 2], [3, 4, 1, 2], [4, 3, 1, 2]," +
                " [1, 4, 2, 3], [4, 2, 3, 1], [2, 3, 4, 1], [2, 4, 3, 1], [2, 3, 4, 1], [4, 2, 1, 3], [2, 3, 4, 1]," +
                " [3, 2, 1, 4], [2, 3, 1, 4], [1, 2, 4, 3], [4, 1, 2, 3], [3, 2, 4, 1], [4, 3, 1, 2], ...]",
                "{[2, 1, 4, 3]=41909, [1, 2, 3, 4]=41878, [1, 3, 2, 4]=41859, [3, 1, 4, 2]=41836," +
                " [2, 4, 1, 3]=41825, [1, 2, 4, 3]=41813, [3, 2, 1, 4]=41781, [2, 3, 4, 1]=41776," +
                " [4, 1, 2, 3]=41766, [4, 3, 1, 2]=41710}"
        );
        prefixPermutations_helper(
                1,
                "[1, 2, 2, 4]",
                "[[2, 2, 1, 4], [4, 1, 2, 2], [1, 4, 2, 2], [4, 1, 2, 2], [2, 4, 2, 1], [2, 4, 2, 1], [1, 2, 2, 4]," +
                " [2, 1, 2, 4], [1, 2, 4, 2], [2, 1, 4, 2], [2, 1, 4, 2], [4, 2, 1, 2], [1, 4, 2, 2], [1, 2, 4, 2]," +
                " [1, 2, 4, 2], [2, 1, 4, 2], [2, 4, 1, 2], [1, 2, 4, 2], [2, 1, 2, 4], [1, 2, 2, 4], ...]",
                "{[2, 1, 2, 4]=83908, [2, 1, 4, 2]=83709, [2, 4, 1, 2]=83591, [1, 2, 4, 2]=83469," +
                " [4, 2, 2, 1]=83413, [2, 2, 1, 4]=83293, [4, 2, 1, 2]=83185, [1, 2, 2, 4]=83168," +
                " [2, 2, 4, 1]=83139, [2, 4, 2, 1]=83052}"
        );
        prefixPermutations_helper(
                2,
                "[1, 2, 2, 4]",
                "[[2, 1, 4, 2], [4, 2, 1, 2], [2, 4, 1, 2], [2, 1, 2, 4], [2, 2, 4, 1], [2, 2, 1, 4], [2, 4, 1, 2]," +
                " [1, 2, 2, 4], [2, 1, 4, 2], [1, 2, 4, 2], [2, 2, 4, 1], [2, 2, 4, 1], [4, 1, 2, 2], [4, 1, 2, 2]," +
                " [1, 2, 2, 4], [4, 2, 2, 1], [2, 1, 4, 2], [1, 2, 2, 4], [2, 2, 1, 4], [2, 4, 1, 2], ...]",
                "{[2, 4, 2, 1]=83743, [2, 2, 1, 4]=83582, [4, 2, 1, 2]=83535, [2, 4, 1, 2]=83515," +
                " [4, 1, 2, 2]=83348, [2, 1, 4, 2]=83342, [2, 1, 2, 4]=83257, [1, 2, 2, 4]=83237," +
                " [1, 2, 4, 2]=83215, [1, 4, 2, 2]=83101}"
        );
        prefixPermutations_helper(
                4,
                "[1, 2, 2, 4]",
                "[[2, 1, 4, 2], [2, 4, 1, 2], [4, 2, 1, 2], [1, 2, 2, 4], [1, 2, 4, 2], [2, 4, 1, 2], [4, 2, 1, 2]," +
                " [1, 4, 2, 2], [4, 2, 2, 1], [2, 2, 4, 1], [2, 4, 2, 1], [2, 2, 4, 1], [4, 2, 1, 2], [2, 2, 4, 1]," +
                " [2, 2, 1, 4], [2, 2, 1, 4], [1, 2, 4, 2], [4, 1, 2, 2], [2, 2, 4, 1], [4, 2, 1, 2], ...]",
                "{[2, 1, 4, 2]=83745, [1, 2, 2, 4]=83737, [1, 2, 4, 2]=83458, [2, 2, 4, 1]=83446," +
                " [2, 2, 1, 4]=83442, [4, 2, 2, 1]=83360, [2, 4, 1, 2]=83245, [4, 2, 1, 2]=83230," +
                " [2, 4, 2, 1]=83181, [1, 4, 2, 2]=83133}"
        );
        prefixPermutations_helper(
                1,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], ...]",
                "{[2, 2, 2, 2]=1000000}"
        );
        prefixPermutations_helper(
                2,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], ...]",
                "{[2, 2, 2, 2]=1000000}"
        );
        prefixPermutations_helper(
                4,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2]," +
                " [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], ...]",
                "{[2, 2, 2, 2]=1000000}"
        );
        prefixPermutations_helper(
                1,
                "[3, 1, 4, 1]",
                "[[1, 4, 3, 1], [1, 3, 1, 4], [3, 1, 4, 1], [1, 3, 1, 4], [1, 1, 4, 3], [1, 1, 4, 3], [3, 4, 1, 1]," +
                " [4, 3, 1, 1], [3, 4, 1, 1], [4, 3, 1, 1], [1, 3, 1, 4], [1, 4, 3, 1], [3, 1, 1, 4], [3, 4, 1, 1]," +
                " [3, 1, 1, 4], [1, 3, 1, 4], [4, 1, 3, 1], [3, 4, 1, 1], [1, 3, 4, 1], [3, 4, 1, 1], ...]",
                "{[4, 3, 1, 1]=83886, [1, 3, 1, 4]=83629, [3, 4, 1, 1]=83508, [4, 1, 3, 1]=83492," +
                " [1, 4, 3, 1]=83478, [1, 4, 1, 3]=83387, [1, 3, 4, 1]=83135, [4, 1, 1, 3]=83122," +
                " [3, 1, 1, 4]=83104, [1, 1, 3, 4]=83099}"
        );
        prefixPermutations_helper(
                2,
                "[3, 1, 4, 1]",
                "[[1, 3, 1, 4], [1, 1, 3, 4], [4, 1, 3, 1], [4, 3, 1, 1], [4, 1, 1, 3], [1, 4, 3, 1], [4, 1, 3, 1]," +
                " [3, 4, 1, 1], [4, 3, 1, 1], [3, 1, 1, 4], [1, 4, 1, 3], [4, 1, 1, 3], [1, 3, 1, 4], [1, 3, 4, 1]," +
                " [3, 4, 1, 1], [1, 4, 1, 3], [1, 3, 1, 4], [3, 1, 4, 1], [4, 1, 3, 1], [4, 1, 3, 1], ...]",
                "{[1, 1, 3, 4]=83836, [4, 1, 3, 1]=83776, [1, 3, 1, 4]=83746, [4, 1, 1, 3]=83601," +
                " [3, 4, 1, 1]=83350, [3, 1, 4, 1]=83286, [1, 1, 4, 3]=83153, [4, 3, 1, 1]=83129," +
                " [1, 4, 1, 3]=83114, [1, 3, 4, 1]=83072}"
        );
        prefixPermutations_helper(
                4,
                "[3, 1, 4, 1]",
                "[[1, 3, 1, 4], [4, 1, 3, 1], [1, 1, 3, 4], [3, 4, 1, 1], [3, 4, 1, 1], [4, 1, 3, 1], [1, 4, 3, 1]," +
                " [3, 1, 1, 4], [1, 1, 4, 3], [1, 4, 1, 3], [1, 1, 4, 3], [1, 4, 1, 3], [1, 1, 3, 4], [1, 4, 1, 3]," +
                " [4, 1, 3, 1], [1, 4, 3, 1], [3, 1, 1, 4], [1, 3, 1, 4], [4, 1, 1, 3], [1, 4, 3, 1], ...]",
                "{[1, 3, 1, 4]=83675, [3, 4, 1, 1]=83504, [1, 4, 1, 3]=83477, [3, 1, 1, 4]=83475," +
                " [4, 3, 1, 1]=83395, [1, 4, 3, 1]=83371, [3, 1, 4, 1]=83349, [1, 1, 3, 4]=83345," +
                " [4, 1, 1, 3]=83297, [1, 1, 4, 3]=83213}"
        );
        prefixPermutations_helper(
                1,
                "[3, 1, null, 1]",
                "[[1, null, 3, 1], [1, 3, 1, null], [3, 1, null, 1], [1, 3, 1, null], [1, 1, null, 3]," +
                " [1, 1, null, 3], [3, null, 1, 1], [null, 3, 1, 1], [3, null, 1, 1], [null, 3, 1, 1]," +
                " [1, 3, 1, null], [1, null, 3, 1], [3, 1, 1, null], [3, null, 1, 1], [3, 1, 1, null]," +
                " [1, 3, 1, null], [null, 1, 3, 1], [3, null, 1, 1], [1, 3, null, 1], [3, null, 1, 1], ...]",
                "{[null, 3, 1, 1]=83886, [1, 3, 1, null]=83629, [3, null, 1, 1]=83508, [null, 1, 3, 1]=83492," +
                " [1, null, 3, 1]=83478, [1, null, 1, 3]=83387, [1, 3, null, 1]=83135, [null, 1, 1, 3]=83122," +
                " [3, 1, 1, null]=83104, [1, 1, 3, null]=83099}"
        );
        prefixPermutations_helper(
                2,
                "[3, 1, null, 1]",
                "[[1, 3, 1, null], [1, 1, 3, null], [null, 1, 3, 1], [null, 3, 1, 1], [null, 1, 1, 3]," +
                " [1, null, 3, 1], [null, 1, 3, 1], [3, null, 1, 1], [null, 3, 1, 1], [3, 1, 1, null]," +
                " [1, null, 1, 3], [null, 1, 1, 3], [1, 3, 1, null], [1, 3, null, 1], [3, null, 1, 1]," +
                " [1, null, 1, 3], [1, 3, 1, null], [3, 1, null, 1], [null, 1, 3, 1], [null, 1, 3, 1], ...]",
                "{[1, 1, 3, null]=83836, [null, 1, 3, 1]=83776, [1, 3, 1, null]=83746, [null, 1, 1, 3]=83601," +
                " [3, null, 1, 1]=83350, [3, 1, null, 1]=83286, [1, 1, null, 3]=83153, [null, 3, 1, 1]=83129," +
                " [1, null, 1, 3]=83114, [1, 3, null, 1]=83072}"
        );
        prefixPermutations_helper(
                4,
                "[3, 1, null, 1]",
                "[[1, 3, 1, null], [null, 1, 3, 1], [1, 1, 3, null], [3, null, 1, 1], [3, null, 1, 1]," +
                " [null, 1, 3, 1], [1, null, 3, 1], [3, 1, 1, null], [1, 1, null, 3], [1, null, 1, 3]," +
                " [1, 1, null, 3], [1, null, 1, 3], [1, 1, 3, null], [1, null, 1, 3], [null, 1, 3, 1]," +
                " [1, null, 3, 1], [3, 1, 1, null], [1, 3, 1, null], [null, 1, 1, 3], [1, null, 3, 1], ...]",
                "{[1, 3, 1, null]=83675, [3, null, 1, 1]=83504, [1, null, 1, 3]=83477, [3, 1, 1, null]=83475," +
                " [null, 3, 1, 1]=83395, [1, null, 3, 1]=83371, [3, 1, null, 1]=83349, [1, 1, 3, null]=83345," +
                " [null, 1, 1, 3]=83297, [1, 1, null, 3]=83213}"
        );
        prefixPermutations_helper(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[2, 3, 1, 4, 5, 6, 7, 8, 9, 10], [2, 6, 1, 4, 3, 5, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [2, 1, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], ...]",
                "{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]=3553, [2, 1, 3, 4, 5, 6, 7, 8, 9, 10]=298," +
                " [1, 3, 2, 4, 5, 6, 7, 8, 9, 10]=65, [2, 3, 1, 4, 5, 6, 7, 8, 9, 10]=62," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10]=47, [3, 2, 1, 4, 5, 6, 7, 8, 9, 10]=41," +
                " [1, 4, 2, 3, 5, 6, 7, 8, 9, 10]=9, [3, 4, 1, 2, 5, 6, 7, 8, 9, 10]=9," +
                " [4, 3, 2, 1, 5, 6, 7, 8, 9, 10]=9, [4, 1, 3, 2, 5, 6, 7, 8, 9, 10]=9}"
        );
        prefixPermutations_helper(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[2, 1, 4, 3, 5, 6, 7, 8, 9, 10], [3, 1, 2, 4, 5, 6, 7, 8, 9, 10], [2, 1, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [5, 4, 2, 1, 3, 6, 7, 8, 9, 10], [3, 2, 1, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [3, 1, 2, 4, 5, 6, 7, 8, 9, 10], [3, 1, 4, 2, 5, 6, 7, 8, 9, 10]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], [3, 6, 7, 2, 4, 5, 1, 8, 9, 10]," +
                " [3, 4, 2, 1, 5, 6, 7, 8, 9, 10], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], ...]",
                "{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]=28, [2, 1, 3, 4, 5, 6, 7, 8, 9, 10]=6," +
                " [7, 5, 2, 10, 9, 8, 6, 3, 4, 1]=6, [1, 10, 4, 2, 6, 8, 7, 9, 3, 5]=6," +
                " [2, 5, 3, 6, 4, 7, 1, 9, 10, 8]=6, [10, 3, 1, 2, 6, 9, 7, 8, 4, 5]=6," +
                " [4, 5, 3, 10, 9, 8, 7, 6, 2, 1]=6, [8, 1, 7, 2, 10, 6, 9, 3, 4, 5]=6," +
                " [9, 7, 2, 10, 4, 6, 5, 8, 1, 3]=5, [9, 7, 6, 5, 2, 3, 10, 1, 8, 4]=5}"
        );
        prefixPermutations_helper(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[2, 1, 4, 3, 5, 6, 7, 8, 9, 10], [7, 8, 5, 4, 10, 3, 1, 6, 9, 2], [10, 9, 1, 2, 8, 7, 6, 5, 4, 3]," +
                " [7, 4, 2, 10, 1, 6, 3, 5, 8, 9], [10, 8, 2, 7, 9, 5, 4, 1, 6, 3], [5, 9, 4, 8, 7, 6, 2, 1, 10, 3]," +
                " [5, 10, 9, 1, 6, 7, 8, 3, 2, 4], [2, 10, 7, 8, 6, 3, 5, 4, 1, 9], [1, 2, 9, 5, 4, 7, 10, 6, 3, 8]," +
                " [3, 6, 10, 8, 2, 9, 7, 5, 1, 4], [9, 4, 3, 2, 6, 7, 10, 1, 8, 5], [1, 2, 9, 3, 4, 5, 6, 8, 7, 10]," +
                " [8, 4, 1, 10, 3, 9, 7, 2, 5, 6], [5, 9, 1, 3, 4, 2, 6, 8, 10, 7], [6, 1, 5, 9, 8, 3, 4, 2, 10, 7]," +
                " [9, 2, 1, 10, 6, 8, 7, 5, 4, 3], [4, 3, 8, 2, 10, 9, 6, 1, 5, 7], [10, 2, 6, 9, 8, 3, 4, 1, 7, 5]," +
                " [3, 8, 10, 1, 5, 6, 4, 7, 9, 2], [10, 7, 2, 3, 4, 8, 5, 6, 1, 9], ...]",
                "{[2, 4, 5, 7, 8, 3, 10, 9, 6, 1]=6, [4, 10, 2, 9, 7, 3, 1, 5, 6, 8]=5," +
                " [10, 8, 3, 2, 4, 6, 9, 7, 5, 1]=5, [9, 4, 8, 10, 5, 1, 6, 7, 3, 2]=5," +
                " [9, 7, 5, 2, 10, 8, 1, 6, 4, 3]=5, [2, 3, 10, 1, 9, 6, 8, 7, 4, 5]=5," +
                " [9, 2, 7, 10, 4, 8, 5, 6, 1, 3]=5, [8, 9, 2, 7, 5, 10, 1, 4, 6, 3]=5," +
                " [2, 4, 7, 1, 8, 6, 10, 9, 5, 3]=5, [3, 5, 2, 9, 7, 8, 1, 6, 4, 10]=5}"
        );
        prefixPermutations_helper(
                1,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "[[2, 3, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 6, 1, 4, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...], ...]",
                "{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=824351," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=74171," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=11916," +
                " [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=11875," +
                " [3, 2, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=11874," +
                " [2, 3, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=11862," +
                " [3, 4, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=1485," +
                " [4, 3, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=1481," +
                " [1, 4, 3, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=1474" +
                ", [3, 2, 4, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=1469}"
        );
        prefixPermutations_helper(
                2,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "[[2, 1, 4, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [5, 4, 2, 1, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 2, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 1, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 6, 7, 2, 4, 5, 1, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 4, 2, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...], ...]",
                "{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=648981," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=93409," +
                " [2, 3, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=19809," +
                " [3, 2, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=19717," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=19627," +
                " [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=19442," +
                " [3, 1, 4, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=3322," +
                " [4, 2, 1, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=3273," +
                " [3, 4, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=3264" +
                ", [2, 1, 4, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=3260}"
        );
        prefixPermutations_helper(
                4,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "[[2, 1, 4, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [8, 7, 5, 10, 15, 16, 2, 6, 13, 1, 9, 11, 14, 3, 12, 4, 17, 18, 19, 20, ...]," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [3, 9, 7, 10, 2, 12, 16, 4, 1, 13, 8, 6, 15, 5, 14, 11, 17, 18, 19, 20, ...]," +
                " [1, 5, 6, 8, 9, 10, 3, 7, 2, 4, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 4, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [5, 8, 1, 3, 6, 7, 4, 2, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [4, 2, 1, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 5, 3, 4, 2, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [4, 2, 1, 5, 3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 9, 6, 2, 8, 3, 5, 4, 7, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]," +
                " [1, 6, 2, 4, 5, 3, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...], ...]",
                "{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=445737," +
                " [2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=83952," +
                " [2, 3, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=21276," +
                " [1, 3, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=21147," +
                " [3, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=21128," +
                " [3, 2, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=21069," +
                " [2, 1, 4, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=4172," +
                " [4, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=4121," +
                " [3, 2, 4, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=4098" +
                ", [3, 4, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...]=4060}"
        );
        prefixPermutations_helper(
                1,
                repeat(1),
                "[[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...], ...]",
                "{[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]=1000000}"
        );
        prefixPermutations_helper(
                2,
                repeat(1),
                "[[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...], ...]",
                "{[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]=1000000}"
        );
        prefixPermutations_helper(
                4,
                repeat(1),
                "[[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...], ...]",
                "{[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]=1000000}"
        );
        prefixPermutations_fail_helper(0, "[1, 2, 3]");
        prefixPermutations_fail_helper(-1, "[1, 2, 3]");
    }

    private static void strings_int_Iterable_helper(
            int size,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.strings(size, input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    private void strings_int_Iterable_fail_helper(int size, @NotNull String input) {
        try {
            P.strings(size, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStrings_int_String() {
        strings_int_Iterable_helper(0, "", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_Iterable_helper(0, "a", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_Iterable_helper(
                1,
                "a",
                "[a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, ...]",
                "{a=1000000}"
        );
        strings_int_Iterable_helper(
                2,
                "a",
                "[aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, aa, ...]",
                "{aa=1000000}"
        );
        strings_int_Iterable_helper(
                3,
                "a",
                "[aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa, aaa," +
                " aaa, ...]",
                "{aaa=1000000}"
        );
        strings_int_Iterable_helper(0, "abc", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_Iterable_helper(
                1,
                "abc",
                "[b, b, c, b, a, b, b, b, b, b, a, b, b, c, b, c, a, c, b, b, ...]",
                "{c=333615, b=333313, a=333072}"
        );
        strings_int_Iterable_helper(
                2,
                "abc",
                "[bb, cb, ab, bb, bb, ab, bc, bc, ac, bb, ca, cb, ac, bc, cb, aa, ca, bc, ab, ac, ...]",
                "{cb=111390, bc=111322, ca=111219, aa=111121, ba=111088, cc=111028, ab=111012, bb=110943, ac=110877}"
        );
        strings_int_Iterable_helper(
                3,
                "abc",
                "[bbc, bab, bbb, bab, bcb, cac, bbc, acb, acb, ccb, aac, abc, aba, cbc, acc, cbc, cab, acc, aac," +
                " aac, ...]",
                "{aaa=37441, bbb=37355, cac=37327, bcb=37306, cbc=37273, cba=37236, cca=37231, ccc=37214, bca=37158," +
                " aab=37136}"
        );
        strings_int_Iterable_helper(0, "abbc", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_Iterable_helper(
                1,
                "abbc",
                "[b, b, c, b, b, c, a, b, b, c, c, b, c, b, c, b, a, b, b, b, ...]",
                "{b=499640, c=250298, a=250062}"
        );
        strings_int_Iterable_helper(
                2,
                "abbc",
                "[bb, cb, bc, ab, bc, cb, cb, cb, ab, bb, cb, ba, cb, bc, bb, ab, cb, ac, cb, cb, ...]",
                "{bb=250376, bc=124904, cb=124818, ab=124686, ba=124685, ac=62739, cc=62694, aa=62656, ca=62442}"
        );
        strings_int_Iterable_helper(
                3,
                "abbc",
                "[bbc, bbc, abb, ccb, cbc, bab, bbc, bba, cbb, cbb, abc, bac, cbc, bbb, cba, abc, abb, aba, ccb," +
                " bcb, ...]",
                "{bbb=125202, cbb=62727, bba=62625, bbc=62606, abb=62481, bab=62386, bcb=62173, acb=31470," +
                " bcc=31464, cbc=31441}"
        );
        strings_int_Iterable_helper(0, "Mississippi", "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_Iterable_helper(
                1,
                "Mississippi",
                "[p, p, s, s, s, p, s, s, i, i, s, s, s, p, s, i, s, i, s, s, ...]",
                "{s=363979, i=363703, p=181581, M=90737}"
        );
        strings_int_Iterable_helper(
                2,
                "Mississippi",
                "[pp, ss, sp, ss, ii, ss, sp, si, si, ss, si, pi, is, si, pi, ss, ss, is, Ms, is, ...]",
                "{ss=132606, si=132473, is=132392, ii=131960, ps=66316, sp=66221, ip=66050, pi=65612, Mi=33235," +
                " pp=33071}"
        );
        strings_int_Iterable_helper(
                3,
                "Mississippi",
                "[pps, ssp, ssi, iss, sps, isi, sss, ipi, iss, ipi, sss, sis, Msi, sss, ssi, sip, iMp, ipp, ips," +
                " ssi, ...]",
                "{sss=48687, sis=48297, ssi=48283, iis=48220, sii=48107, iii=48048, iss=47940, isi=47797, psi=24274," +
                " pss=24260}"
        );
        strings_int_Iterable_fail_helper(1, "");
        strings_int_Iterable_fail_helper(-1, "abc");
    }

    private static void strings_int_helper(int size, @NotNull String output, @NotNull String topSampleCount) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, map(Testing::nicePrint, P.strings(size))));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    private void strings_int_fail_helper(int size) {
        try {
            P.strings(size);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStrings_int() {
        strings_int_helper(0, "[, , , , , , , , , , , , , , , , , , , , ...]", "{=1000000}");
        strings_int_helper(
                1,
                "[嘩, 퇉, 馃, \\u2df2, ε, 䊿, \\u2538, \\u31e5, 髽, 肣, \\uf6ff, ﳑ, 赧, \\ue215, \\u17f3, \\udd75, 껸," +
                " \\udd15, 몱, ﲦ, ...]",
                "{\\uf1b2=36, 撢=35, આ=34, 퉃=34, \\27=33, 韖=32, 㖒=32, 膗=31, 㗞=31, 䕦=31}"
        );
        strings_int_helper(
                2,
                "[嘩퇉, 馃\\u2df2, ε䊿, \\u2538\\u31e5, 髽肣, \\uf6ffﳑ, 赧\\ue215, \\u17f3\\udd75, 껸\\udd15, 몱ﲦ, 䯏ϡ," +
                " 罖\\u19dc, 刿ㄾ, 䲵箿, 偵恾, \u1B1CK, 㵏ꏹ, 缄㩷, \u2D3F읾, 纫\\ufe2d, ...]",
                "{\\uf310틺=2, 緑\\ue709=2, 㑰\\uf5be=2, \\ue429菧=2, \\uf480\\u23c0=2, \u2CC8고=2, 㜛땹=2," +
                " \\ue283捿=2, \\ua8ed\u2C04=2, 楮譂=2}"
        );
        strings_int_helper(
                3,
                "[嘩퇉馃, \\u2df2ε䊿, \\u2538\\u31e5髽, 肣\\uf6ffﳑ, 赧\\ue215\\u17f3, \\udd75껸\\udd15, 몱ﲦ䯏," +
                " ϡ罖\\u19dc, 刿ㄾ䲵, 箿偵恾, \u1B1CK㵏, ꏹ缄㩷, \u2D3F읾纫, \\ufe2d㗂䝲, \\uf207갩힜, 坤琖\\u2a43," +
                " 퉌\\uea45\\ue352, 蕤餥䉀, \\u2b63\\uf637鸂, 鸅误輮, ...]",
                "{嘩퇉馃=1, \\u2df2ε䊿=1, \\u2538\\u31e5髽=1, 肣\\uf6ffﳑ=1, 赧\\ue215\\u17f3=1, \\udd75껸\\udd15=1," +
                " 몱ﲦ䯏=1, ϡ罖\\u19dc=1, 刿ㄾ䲵=1, 箿偵恾=1}"
        );
        strings_int_fail_helper(-1);
    }

    private static void lists_Iterable_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).lists(input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void lists_Iterable_helper_uniform(
            int scale,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        lists_Iterable_helper(
                scale,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                topSampleCount,
                meanSize
        );
    }

    private static void lists_Iterable_fail_helper(int scale, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).lists(input));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testLists_Iterable() {
        lists_Iterable_helper_uniform(
                1,
                "[5]",
                "[[5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5], [5], [5], [5], [5, 5], [5], [], [], [5], [5]," +
                " [5], [], [5], [5], [], [5], [], [], ...]",
                "{[]=499557, [5]=250432, [5, 5]=124756, [5, 5, 5]=62825, [5, 5, 5, 5]=31144, [5, 5, 5, 5, 5]=15656," +
                " [5, 5, 5, 5, 5, 5]=7784, [5, 5, 5, 5, 5, 5, 5]=3987, [5, 5, 5, 5, 5, 5, 5, 5]=1945" +
                ", [5, 5, 5, 5, 5, 5, 5, 5, 5]=945}",
                1.0006389999976706
        );
        lists_Iterable_helper_uniform(
                2,
                "[5]",
                "[[5, 5, 5, 5], [5, 5, 5], [5, 5], [5, 5, 5], [], [5, 5, 5, 5, 5], [5], [], [5, 5, 5], [5, 5, 5, 5]," +
                " [], [], [5], [5], [5, 5, 5], [], [5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5], [5], [5, 5, 5, 5, 5, 5, 5]" +
                ", ...]",
                "{[]=333041, [5]=222027, [5, 5]=148088, [5, 5, 5]=98825, [5, 5, 5, 5]=65746, [5, 5, 5, 5, 5]=44116," +
                " [5, 5, 5, 5, 5, 5]=29303, [5, 5, 5, 5, 5, 5, 5]=19671, [5, 5, 5, 5, 5, 5, 5, 5]=13059" +
                ", [5, 5, 5, 5, 5, 5, 5, 5, 5]=8625}",
                2.0037019999891394
        );
        lists_Iterable_helper_uniform(
                4,
                "[5]",
                "[[5, 5, 5, 5], [5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5], [], [], [5]," +
                " [5, 5, 5, 5, 5], [5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5], [], [5, 5, 5, 5], [5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5], [], [5, 5, 5, 5], ...]",
                "{[]=200010, [5]=159823, [5, 5]=128026, [5, 5, 5]=102068, [5, 5, 5, 5]=82001, [5, 5, 5, 5, 5]=65507," +
                " [5, 5, 5, 5, 5, 5]=52528, [5, 5, 5, 5, 5, 5, 5]=41779, [5, 5, 5, 5, 5, 5, 5, 5]=33653" +
                ", [5, 5, 5, 5, 5, 5, 5, 5, 5]=26990}",
                4.00571499999147
        );
        lists_Iterable_helper_uniform(
                1,
                "[1, 2, 3]",
                "[[2, 1, 2], [2, 2, 3, 2, 3, 1, 3, 2], [1, 3], [3, 2], [], [], [1, 3], [2], [], [], [3], [1, 3]," +
                " [3], [3], [], [3], [], [], [], [], ...]",
                "{[]=499504, [2]=83540, [3]=83439, [1]=83275, [2, 3]=13999, [2, 2]=13964, [1, 1]=13961," +
                " [3, 2]=13913, [2, 1]=13882, [1, 2]=13879}",
                1.00085799999768
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, 2, 3]",
                "[[2, 2, 2, 2], [2], [3, 2, 2, 3], [], [3, 2], [1, 3, 1], [2, 1], [3, 3, 3], [2, 1, 3], [1], [1]," +
                " [1], [2, 2, 1], [2, 3, 2, 3, 2, 3, 2], [], [2], [2, 3, 3, 1, 1, 3], [], [], [], ...]",
                "{[]=333247, [2]=74291, [3]=74037, [1]=73733, [3, 1]=16580, [1, 2]=16560, [3, 3]=16556," +
                " [2, 1]=16422, [1, 3]=16411, [3, 2]=16379}",
                2.0023509999891522
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, 2, 3]",
                "[[2, 2, 2, 2], [2], [3, 2, 3, 1, 3, 3, 3, 2, 3, 3, 1, 2, 1, 3, 3, 1, 1], [1], [3], [2, 2, 1, 3, 3]," +
                " [2, 3], [2, 3, 3, 1, 1, 3, 1, 1, 1, 3, 3, 2, 3, 3, 3, 3, 2], []," +
                " [3, 1, 3, 2, 1, 1, 3, 3, 2, 1, 1, 1, 1, 3], [1, 3, 3], [3, 1, 3, 3, 3]," +
                " [3, 1, 3, 3, 2, 3, 2, 2, 2, 2, 3], [], [], [3, 3, 1, 1], [1], [3, 1, 1, 1], [], [1, 3, 1], ...]",
                "{[]=199912, [2]=53484, [1]=53481, [3]=53282, [2, 3]=14347, [1, 1]=14328, [1, 2]=14321," +
                " [3, 1]=14304, [2, 2]=14257, [3, 3]=14250}",
                4.00516399999172
        );
        lists_Iterable_helper_uniform(
                1,
                "[1, null, 3]",
                "[[null, 1, null], [null, null, 3, null, 3, 1, 3, null], [1, 3], [3, null], [], [], [1, 3], [null]," +
                " [], [], [3], [1, 3], [3], [3], [], [3], [], [], [], [], ...]",
                "{[]=499504, [null]=83540, [3]=83439, [1]=83275, [null, 3]=13999, [null, null]=13964, [1, 1]=13961," +
                " [3, null]=13913, [null, 1]=13882, [1, null]=13879}",
                1.00085799999768
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, null, 3]",
                "[[null, null, null, null], [null], [3, null, null, 3], [], [3, null], [1, 3, 1], [null, 1]," +
                " [3, 3, 3], [null, 1, 3], [1], [1], [1], [null, null, 1], [null, 3, null, 3, null, 3, null], []," +
                " [null], [null, 3, 3, 1, 1, 3], [], [], [], ...]",
                "{[]=333247, [null]=74291, [3]=74037, [1]=73733, [3, 1]=16580, [1, null]=16560, [3, 3]=16556," +
                " [null, 1]=16422, [1, 3]=16411, [3, null]=16379}",
                2.0023509999891522
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, null, 3]",
                "[[null, null, null, null], [null], [3, null, 3, 1, 3, 3, 3, null, 3, 3, 1, null, 1, 3, 3, 1, 1]," +
                " [1], [3], [null, null, 1, 3, 3], [null, 3]," +
                " [null, 3, 3, 1, 1, 3, 1, 1, 1, 3, 3, null, 3, 3, 3, 3, null], []," +
                " [3, 1, 3, null, 1, 1, 3, 3, null, 1, 1, 1, 1, 3], [1, 3, 3], [3, 1, 3, 3, 3]," +
                " [3, 1, 3, 3, null, 3, null, null, null, null, 3], [], [], [3, 3, 1, 1], [1], [3, 1, 1, 1], []," +
                " [1, 3, 1], ...]",
                "{[]=199912, [null]=53484, [1]=53481, [3]=53282, [null, 3]=14347, [1, 1]=14328, [1, null]=14321," +
                " [3, 1]=14304, [null, null]=14257, [3, 3]=14250}",
                4.00516399999172
        );
        lists_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4]",
                "[[2, 4, 1], [2, 2, 3, 4, 2, 3, 1, 4, 3], [1, 3, 4], [4], [4], [3], [1, 3], [2], [], [], [4], [2]," +
                " [1], [], [3], [3], [], [3], [], [], ...]",
                "{[]=499557, [3]=62855, [1]=62711, [2]=62536, [4]=62330, [2, 2]=8015, [4, 3]=7980, [1, 4]=7963," +
                " [2, 3]=7893, [4, 2]=7892}",
                1.0006389999976706
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4]",
                "[[2, 2, 4, 4], [2, 2, 3], [4, 3], [3, 4, 2], [], [1, 3, 4, 1, 2], [2], [], [3, 4, 3], [2, 1, 3, 3]," +
                " [], [], [1], [1], [2, 4, 2], [], [2, 3, 4, 4, 4, 4, 2], [4, 4, 4, 4], [4], [2, 3, 4, 4, 3, 1, 1]" +
                ", ...]",
                "{[]=333041, [1]=55655, [2]=55628, [4]=55498, [3]=55246, [3, 3]=9433, [2, 2]=9373, [2, 4]=9320," +
                " [3, 4]=9312, [2, 1]=9295}",
                2.0037019999891394
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4]",
                "[[2, 2, 4, 4], [2, 2], [4, 4, 3, 2, 4, 3, 1, 3, 4, 3, 3, 2, 3, 3, 1, 2], [1, 3], [], [], [3]," +
                " [2, 4, 2, 4, 1], [2, 3, 4], [2, 3, 4, 4, 3, 1, 1, 3, 1, 1, 1, 3, 3, 2, 4, 3], [4, 3, 2, 4, 4]," +
                " [3, 1, 3, 4, 2, 4, 1, 1, 3, 4, 3], [1], [], [1, 3, 4, 3], [3, 1, 3, 3, 3]," +
                " [3, 1, 3, 3, 2, 3, 4, 2, 2, 2, 4], [3], [], [3, 4, 3, 1], ...]",
                "{[]=200010, [2]=40047, [4]=39960, [3]=39955, [1]=39861, [4, 1]=8182, [3, 2]=8116, [4, 3]=8107," +
                " [1, 1]=8029, [4, 2]=8025}",
                4.00571499999147
        );
        lists_Iterable_helper_uniform(
                1,
                "[1, 2, 2, 4]",
                "[[2, 4, 1], [2, 2, 2, 4, 2, 2, 1, 4, 2], [1, 2, 4], [4], [4], [2], [1, 2], [2], [], [], [4], [2]," +
                " [1], [], [2], [2], [], [2], [], [], ...]",
                "{[]=499557, [2]=125391, [1]=62711, [4]=62330, [2, 2]=31328, [4, 2]=15872, [2, 1]=15491," +
                " [1, 2]=15489, [2, 4]=15329, [1, 4]=7963}",
                1.0006389999976706
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, 2, 2, 4]",
                "[[2, 2, 4, 4], [2, 2, 2], [4, 2], [2, 4, 2], [], [1, 2, 4, 1, 2], [2], [], [2, 4, 2], [2, 1, 2, 2]," +
                " [], [], [1], [1], [2, 4, 2], [], [2, 2, 4, 4, 4, 4, 2], [4, 4, 4, 4], [4], [2, 2, 4, 4, 2, 1, 1]" +
                ", ...]",
                "{[]=333041, [2]=110874, [1]=55655, [4]=55498, [2, 2]=37235, [2, 4]=18632, [1, 2]=18512," +
                " [2, 1]=18480, [4, 2]=18414, [2, 2, 2]=12455}",
                2.0037019999891394
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, 2, 2, 4]",
                "[[2, 2, 4, 4], [2, 2], [4, 4, 2, 2, 4, 2, 1, 2, 4, 2, 2, 2, 2, 2, 1, 2], [1, 2], [], [], [2]," +
                " [2, 4, 2, 4, 1], [2, 2, 4], [2, 2, 4, 4, 2, 1, 1, 2, 1, 1, 1, 2, 2, 2, 4, 2], [4, 2, 2, 4, 4]," +
                " [2, 1, 2, 4, 2, 4, 1, 1, 2, 4, 2], [1], [], [1, 2, 4, 2], [2, 1, 2, 2, 2]," +
                " [2, 1, 2, 2, 2, 2, 4, 2, 2, 2, 4], [2], [], [2, 4, 2, 1], ...]",
                "{[]=200010, [2]=80002, [4]=39960, [1]=39861, [2, 2]=32002, [4, 2]=16132, [2, 4]=16010," +
                " [2, 1]=15945, [1, 2]=15898, [2, 2, 2]=12985}",
                4.00571499999147
        );
        lists_Iterable_helper_uniform(
                1,
                "[2, 2, 2, 2]",
                "[[2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2], [2], [2], [2], [2, 2], [2], [], [], [2], [2]," +
                " [2], [], [2], [2], [], [2], [], [], ...]",
                "{[]=499557, [2]=250432, [2, 2]=124756, [2, 2, 2]=62825, [2, 2, 2, 2]=31144, [2, 2, 2, 2, 2]=15656," +
                " [2, 2, 2, 2, 2, 2]=7784, [2, 2, 2, 2, 2, 2, 2]=3987, [2, 2, 2, 2, 2, 2, 2, 2]=1945" +
                ", [2, 2, 2, 2, 2, 2, 2, 2, 2]=945}",
                1.0006389999976706
        );
        lists_Iterable_helper_uniform(
                2,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2], [2, 2], [2, 2, 2], [], [2, 2, 2, 2, 2], [2], [], [2, 2, 2], [2, 2, 2, 2]," +
                " [], [], [2], [2], [2, 2, 2], [], [2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2], [2], [2, 2, 2, 2, 2, 2, 2]" +
                ", ...]",
                "{[]=333041, [2]=222027, [2, 2]=148088, [2, 2, 2]=98825, [2, 2, 2, 2]=65746, [2, 2, 2, 2, 2]=44116," +
                " [2, 2, 2, 2, 2, 2]=29303, [2, 2, 2, 2, 2, 2, 2]=19671, [2, 2, 2, 2, 2, 2, 2, 2]=13059" +
                ", [2, 2, 2, 2, 2, 2, 2, 2, 2]=8625}",
                2.0037019999891394
        );
        lists_Iterable_helper_uniform(
                4,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2], [], [], [2]," +
                " [2, 2, 2, 2, 2], [2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2], [], [2, 2, 2, 2], [2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2], [], [2, 2, 2, 2], ...]",
                "{[]=200010, [2]=159823, [2, 2]=128026, [2, 2, 2]=102068, [2, 2, 2, 2]=82001, [2, 2, 2, 2, 2]=65507," +
                " [2, 2, 2, 2, 2, 2]=52528, [2, 2, 2, 2, 2, 2, 2]=41779, [2, 2, 2, 2, 2, 2, 2, 2]=33653" +
                ", [2, 2, 2, 2, 2, 2, 2, 2, 2]=26990}",
                4.00571499999147
        );
        lists_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[6, 9, 6], [6, 2, 7, 2, 7, 6, 6, 10], [], [3, 3], [5, 7], [6], [], [], [4], [6], [3], [7], [], []," +
                " [1], [], [], [], [], [], ...]",
                "{[]=500030, [10]=25344, [1]=25129, [4]=25128, [7]=25106, [2]=25093, [6]=25064, [8]=24996," +
                " [3]=24945, [5]=24916}",
                0.998919999997707
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[6, 4, 2, 8], [6, 2], [6, 6, 10], [], [5, 7, 4, 6, 3], [], [4], [3, 4, 7], [1, 9, 9, 9], [9]," +
                " [10, 6, 3], [6, 7, 4, 4, 4, 6], [2, 4, 2, 8], [10, 3, 8, 5, 9, 7], [], [], []," +
                " [4, 8, 3, 8, 7, 7, 5, 6], [2, 7], [], ...]",
                "{[]=333018, [7]=22348, [10]=22330, [6]=22322, [2]=22312, [4]=22212, [1]=22163, [3]=22146," +
                " [5]=22122, [8]=22019}",
                2.003595999989077
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[6, 4, 2, 8], [6], [4, 8, 3, 6, 3, 4, 7, 7, 9, 1, 9, 9, 9, 5, 9, 4, 3], [10, 6, 3], [6, 7]," +
                " [10, 3, 8, 5, 9, 7, 5, 1, 7, 10, 8, 3, 3, 4, 7, 3, 10], []," +
                " [7, 1, 4, 5, 5, 3, 8, 1, 1, 7, 4, 7, 6, 7], [3, 8], [7, 1, 10, 3, 10], [3, 5, 2, 3, 2, 10], [1]," +
                " [3, 9, 5, 6], [5], [3, 1, 9, 8], [], [], [1, 4, 1], [6, 7], [4, 9, 6, 4, 2], ...]",
                "{[]=200177, [8]=16184, [10]=16147, [6]=16096, [1]=16095, [9]=16049, [5]=16043, [7]=15982," +
                " [2]=15958, [3]=15950}",
                4.002965999991581
        );
        lists_Iterable_helper(
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "[[3, 10, 7], [5], [], [10, 1], [], [3], [7], [], [5], [3], [], [], [], [], [], [], [], [], [5]," +
                " [9, 15, 6, 12], ...]",
                "{[]=499603, [1]=62583, [2]=46821, [3]=35045, [4]=26163, [5]=20032, [6]=14805, [7]=11067, [8]=8386" +
                ", [1, 1]=7678}",
                1.0012699999976906
        );
        lists_Iterable_helper(
                2,
                P.withScale(4).positiveIntegersGeometric(),
                "[[10, 7, 7, 4], [1, 3, 3, 2, 7], [2, 3, 1, 2, 1, 2], [], [5, 9, 15], [12], [1, 2, 1], []," +
                " [9, 4, 7, 6, 2, 5, 1, 6], [], [], [], [3, 1, 5, 1, 1], [2], [1, 4, 5, 4], [12], [2], [], [4, 1]," +
                " [1], ...]",
                "{[]=333149, [1]=55129, [2]=41612, [3]=31155, [4]=23246, [5]=17762, [6]=13192, [7]=9863," +
                " [1, 1]=9292, [8]=7549}",
                2.001994999989098
        );
        lists_Iterable_helper(
                4,
                P.withScale(4).positiveIntegersGeometric(),
                "[[10, 7, 7, 4], [7, 8, 2, 3, 1, 2, 1, 2, 1, 8], [15, 6, 12, 6, 1, 2], [13]," +
                " [2, 5, 1, 6, 1, 1, 1, 10, 3, 1, 5, 1, 1, 4], [], [2, 12, 2, 2, 1, 4, 4, 1, 3, 1, 1, 6, 2], [], []," +
                " [], [1, 2, 3], [5, 4], [9], [4, 1, 6, 7, 5], [], [1, 1, 2, 1, 3, 2, 3], [13, 7, 10], [4, 19]," +
                " [2, 4, 1, 22, 17, 2, 1, 2, 1, 3, 4, 3], [], ...]",
                "{[]=199867, [1]=39597, [2]=30067, [3]=22409, [4]=16944, [5]=12477, [6]=9434, [1, 1]=8183, [7]=7055" +
                ", [2, 1]=6035}",
                4.0083209999916205
        );
        lists_Iterable_helper(
                1,
                repeat(1),
                "[[1, 1, 1], [1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1], [1, 1], [], [1], [1, 1, 1], [], []," +
                " [1, 1], [1, 1], [1, 1], [], [1, 1], [], [], [1], [1], [], ...]",
                "{[]=499125, [1]=250897, [1, 1]=124849, [1, 1, 1]=62518, [1, 1, 1, 1]=31407, [1, 1, 1, 1, 1]=15634," +
                " [1, 1, 1, 1, 1, 1]=7825, [1, 1, 1, 1, 1, 1, 1]=3926, [1, 1, 1, 1, 1, 1, 1, 1]=1896" +
                ", [1, 1, 1, 1, 1, 1, 1, 1, 1]=956}",
                1.0008359999977228
        );
        lists_Iterable_helper(
                2,
                repeat(1),
                "[[1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1], [1, 1], [1, 1, 1, 1, 1], [], [1]," +
                " [1, 1], [1], [1, 1, 1], [1, 1, 1, 1, 1, 1], [1], [1, 1], [], [1], [], [1], [], [1, 1, 1], ...]",
                "{[]=333813, [1]=221150, [1, 1]=148025, [1, 1, 1]=98992, [1, 1, 1, 1]=66270, [1, 1, 1, 1, 1]=43747," +
                " [1, 1, 1, 1, 1, 1]=29389, [1, 1, 1, 1, 1, 1, 1]=19567, [1, 1, 1, 1, 1, 1, 1, 1]=12958" +
                ", [1, 1, 1, 1, 1, 1, 1, 1, 1]=8571}",
                2.0020969999891216
        );
        lists_Iterable_helper(
                4,
                repeat(1),
                "[[1, 1, 1, 1], [1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1], [1, 1], [], [1], [], [1], [1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [], [1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1], [1, 1, 1, 1], ...]",
                "{[]=200194, [1]=160489, [1, 1]=127708, [1, 1, 1]=101606, [1, 1, 1, 1]=82008, [1, 1, 1, 1, 1]=65900," +
                " [1, 1, 1, 1, 1, 1]=52157, [1, 1, 1, 1, 1, 1, 1]=41827, [1, 1, 1, 1, 1, 1, 1, 1]=33413" +
                ", [1, 1, 1, 1, 1, 1, 1, 1, 1]=26877}",
                4.004359999991779
        );
        lists_Iterable_fail_helper(1, Collections.emptyList());
        lists_Iterable_fail_helper(1, Arrays.asList(1, 2, 3));
        lists_Iterable_fail_helper(0, P.integers());
        lists_Iterable_fail_helper(-1, P.integers());
    }

    private static void strings_String_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).strings(input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void strings_String_fail_helper(int scale, @NotNull String input) {
        try {
            toList(P.withScale(scale).strings(input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStrings_String() {
        strings_String_helper(
                1,
                "a",
                "[aaa, aaaaaaaaa, aaa, a, a, a, aa, a, , , a, a, a, , a, a, , a, , , ...]",
                "{=499557, a=250432, aa=124756, aaa=62825, aaaa=31144, aaaaa=15656, aaaaaa=7784, aaaaaaa=3987," +
                " aaaaaaaa=1945, aaaaaaaaa=945}",
                1.0006389999976706
        );
        strings_String_helper(
                2,
                "a",
                "[aaaa, aaa, aa, aaa, , aaaaa, a, , aaa, aaaa, , , a, a, aaa, , aaaaaaa, aaaa, a, aaaaaaa, ...]",
                "{=333041, a=222027, aa=148088, aaa=98825, aaaa=65746, aaaaa=44116, aaaaaa=29303, aaaaaaa=19671," +
                " aaaaaaaa=13059, aaaaaaaaa=8625}",
                2.0037019999891394
        );
        strings_String_helper(
                4,
                "a",
                "[aaaa, aa, aaaaaaaaaaaaaaaa, aa, , , a, aaaaa, aaa, aaaaaaaaaaaaaaaa, aaaaa, aaaaaaaaaaa, a, ," +
                " aaaa, aaaaa, aaaaaaaaaaa, a, , aaaa, ...]",
                "{=200010, a=159823, aa=128026, aaa=102068, aaaa=82001, aaaaa=65507, aaaaaa=52528, aaaaaaa=41779," +
                " aaaaaaaa=33653, aaaaaaaaa=26990}",
                4.00571499999147
        );
        strings_String_helper(
                1,
                "abc",
                "[bab, bbcbcacb, ac, cb, , , ac, b, , , c, ac, c, c, , c, , , , , ...]",
                "{=499504, b=83540, c=83439, a=83275, bc=13999, bb=13964, aa=13961, cb=13913, ba=13882, ab=13879}",
                1.00085799999768
        );
        strings_String_helper(
                2,
                "abc",
                "[bbbb, b, cbbc, , cb, aca, ba, ccc, bac, a, a, a, bba, bcbcbcb, , b, bccaac, , , , ...]",
                "{=333247, b=74291, c=74037, a=73733, ca=16580, ab=16560, cc=16556, ba=16422, ac=16411, cb=16379}",
                2.0023509999891522
        );
        strings_String_helper(
                4,
                "abc",
                "[bbbb, b, cbcacccbccabaccaa, a, c, bbacc, bc, bccaacaaaccbccccb, , cacbaaccbaaaac, acc, caccc," +
                " caccbcbbbbc, , , ccaa, a, caaa, , aca, ...]",
                "{=199912, b=53484, a=53481, c=53282, bc=14347, aa=14328, ab=14321, ca=14304, bb=14257, cc=14250}",
                4.00516399999172
        );
        strings_String_helper(
                1,
                "abbc",
                "[bca, bbbcbbacb, abc, c, c, b, ab, b, , , c, b, a, , b, b, , b, , , ...]",
                "{=499557, b=125391, a=62711, c=62330, bb=31328, cb=15872, ba=15491, ab=15489, bc=15329, ac=7963}",
                1.0006389999976706
        );
        strings_String_helper(
                2,
                "abbc",
                "[bbcc, bbb, cb, bcb, , abcab, b, , bcb, babb, , , a, a, bcb, , bbccccb, cccc, c, bbccbaa, ...]",
                "{=333041, b=110874, a=55655, c=55498, bb=37235, bc=18632, ab=18512, ba=18480, cb=18414, bbb=12455}",
                2.0037019999891394
        );
        strings_String_helper(
                4,
                "abbc",
                "[bbcc, bb, ccbbcbabcbbbbbab, ab, , , b, bcbca, bbc, bbccbaabaaabbbcb, cbbcc, babcbcaabcb, a, ," +
                " abcb, babbb, babbbbcbbbc, b, , bcba, ...]",
                "{=200010, b=80002, c=39960, a=39861, bb=32002, cb=16132, bc=16010, ba=15945, ab=15898, bbb=12985}",
                4.00571499999147
        );
        strings_String_helper(
                1,
                "Mississippi",
                "[sps, sisisssi, is, , is, s, , , s, s, s, i, i, , i, , , , , , ...]",
                "{=499907, s=91141, i=91078, p=45481, M=22586, si=16547, is=16526, ss=16500, ii=16475, ip=8402}",
                0.9996679999977037
        );
        strings_String_helper(
                2,
                "Mississippi",
                "[ssii, si, ssi, i, issss, , s, sss, iMpi, , , p, pss, ssssss, isii, psiiip, i, , siisisis, , ...]",
                "{=333528, s=80737, i=80484, p=40277, M=20228, ss=19746, is=19600, si=19560, ii=19556, ip=9890}",
                2.0026269999890762
        );
        strings_String_helper(
                4,
                "Mississippi",
                "[ssii, s, sisssssisipiMpipp, s, psss, ss, psiiipsiMspisssss, s, sMisiiisiMMss, si, sMiip," +
                " siisipipsM, , spis, i, sMpi, , , MsM, ss, ...]",
                "{=199852, s=58261, i=58255, p=29278, si=17043, is=16951, ss=16925, ii=16678, M=14538, ip=8503}",
                4.0051349999917525
        );
        strings_String_fail_helper(1, "");
        strings_String_fail_helper(0, "abc");
        strings_String_fail_helper(-1, "abc");
    }

    private static void strings_helper(
            int scale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).strings()));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void strings_fail_helper(int scale) {
        try {
            toList(P.withScale(scale).strings());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStrings() {
        strings_helper(
                1,
                "[ε䊿\u2538, \udd15몱ﲦ䯏ϡ罖\u19dc刿ㄾ, ᬜK㵏, 㩷, 纫, 䝲, 坤琖, \uea45, , , \u2b63, 鸅, \uee1c, , ᅺ, 䇺," +
                " , 㖊, , , ...]",
                "{=499557, \uf59a=15, 僵=14, \u12c7=14, 瘍=14, \ue0de=14, \ua838=13, 䃢=13, 喽=13, 瓫=13}",
                1.0006389999976706
        );
        strings_helper(
                2,
                "[\u31e5髽肣\uf6ff, \udd15몱ﲦ, 刿ㄾ, K㵏ꏹ, , 坤琖\u2a43퉌\uea45, 餥, , \u33b2酓캆, \ue9fd\u2aec㖊짎, , ," +
                " 䱸, \uf878, 尩굿\uecf5, , 瀵컦刓嗏\u3353\ue2d3\ud805, 䫯噋\uf36fꌻ, 홃, 壙\udd82픫鼧\u061a\u2e94穨, ...]",
                "{=333041, 趤=15, 挗=13, \u2fae=13, 阤=12, \u0978=12, \ue2fe=12, \uab10=12, 䖸=12, \ue973=12}",
                2.0037019999891394
        );
        strings_helper(
                4,
                "[\u31e5髽肣\uf6ff, \udd15몱, \u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd, 全覚, , , ሮ," +
                " 尩굿\uecf5ꪻ疜, 瀵컦刓, 壙\udd82픫鼧\u061a\u2e94穨㽖ﶼ䥔핀糦嗮\uf329ﻧ\udd42, ꯃ慚총\u0e77\uf36b," +
                " 駆퉐庺\u2293\ued0d䴻ꎤ槔横靯ढ, 䃼, , 만ᑒ拷\ue68e, \u2506囀ͺ\u124eꪪ, 嶂췴䔾턞\uead1猂唯湑ﮍ蹙甧, \uab6e, ," +
                " 滞\ue89bᖒ㿘, ...]",
                "{=200010, \ued08=11, 듏=11, \ua495=11, 幱=10, 㚼=10, Ꙛ=10, 홣=10, ﺆ=10, \ua494=10}",
                4.00571499999147
        );
        strings_fail_helper(0);
        strings_fail_helper(-1);
    }

    private static void listsAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).listsAtLeast(minSize, input))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void listsAtLeast_helper_uniform(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        listsAtLeast_helper(
                scale,
                minSize,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                topSampleCount,
                meanSize
        );
    }

    private static void listsAtLeast_fail_helper(int scale, int minSize, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).listsAtLeast(minSize, input));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testListsAtLeast() {
        listsAtLeast_helper_uniform(
                2,
                1,
                "[5]",
                "[[5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5], [5], [5, 5], [5], [5, 5, 5], [5], [5]," +
                " [5, 5], [5], [5, 5], [5, 5], [5], [5], [5], [5], [5], [5], [5], ...]",
                "{[5]=500269, [5, 5]=249809, [5, 5, 5]=124830, [5, 5, 5, 5]=62682, [5, 5, 5, 5, 5]=31195," +
                " [5, 5, 5, 5, 5, 5]=15549, [5, 5, 5, 5, 5, 5, 5]=7831, [5, 5, 5, 5, 5, 5, 5, 5]=3938," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5]=2013, [5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=951}",
                1.999585999979838
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[5]",
                "[[5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5], [5, 5, 5, 5, 5], [5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5], [5, 5, 5, 5, 5], [5, 5, 5, 5, 5], [5, 5, 5], [5, 5, 5, 5], [5, 5, 5, 5], [5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5], [5, 5, 5], [5, 5, 5, 5, 5, 5], ...]",
                "{[5, 5, 5]=332475, [5, 5, 5, 5]=222950, [5, 5, 5, 5, 5]=148435, [5, 5, 5, 5, 5, 5]=98386," +
                " [5, 5, 5, 5, 5, 5, 5]=65648, [5, 5, 5, 5, 5, 5, 5, 5]=43847, [5, 5, 5, 5, 5, 5, 5, 5, 5]=29430," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=19567, [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=13014" +
                ", [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=8771}",
                5.00315899999616
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[5]",
                "[[5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], [5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5," +
                " 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5], ...]",
                "{[5, 5, 5, 5, 5, 5, 5, 5]=40181, [5, 5, 5, 5, 5, 5, 5, 5, 5]=38543," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=37070, [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=35343," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=33943, [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=32305," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=31206," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=29856," +
                " [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=28774" +
                ", [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5]=27718}",
                32.008717000021356
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3]",
                "[[2, 1, 2, 2], [2, 2, 3, 2, 3, 1, 3, 2], [1, 3, 2], [3], [3, 2, 1], [3], [2, 3], [2], [3]," +
                " [1, 3, 3], [2], [3], [2], [3], [1], [3], [1], [1], [3], [2, 1], ...]",
                "{[2]=167156, [3]=166869, [1]=166251, [2, 3]=28319, [1, 3]=27831, [2, 2]=27782, [3, 1]=27773," +
                " [1, 1]=27761, [1, 2]=27704, [3, 2]=27683}",
                1.9993039999798474
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3]",
                "[[2, 2, 2, 2, 2, 1, 2], [3, 2, 2, 3, 1, 3, 2], [3, 2, 3], [1, 3, 1, 2, 3], [2, 1, 3]," +
                " [3, 3, 3, 2, 3], [2, 1, 3, 3], [1, 3, 1], [3, 1, 1], [2, 2, 1, 3, 3, 2], [2, 3, 2, 3, 2, 3, 2]," +
                " [2, 1, 2], [2, 3, 3, 1, 1, 3, 1, 1, 1], [3, 2, 3, 1, 3, 3, 3, 1, 2, 2, 3], [2, 3, 2, 3]," +
                " [3, 1, 3], [1, 3, 3, 2], [1, 1, 1], [3, 3, 1, 1, 3, 3, 1, 1], [2, 1, 3], ...]",
                "{[2, 3, 3]=12598, [2, 3, 1]=12517, [3, 3, 1]=12472, [2, 1, 1]=12457, [3, 2, 3]=12442," +
                " [1, 3, 3]=12437, [3, 3, 2]=12420, [1, 1, 1]=12412, [3, 1, 2]=12392, [3, 1, 3]=12372}",
                5.003739999996368
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3]",
                "[[3, 2, 3, 1, 3, 3, 3, 2, 3, 3, 1, 2, 1, 3, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 3, 2, 1, 2, 2, 1, 3, 3," +
                " 2, 3, 3, 2, 3, 1, 2, 3, 2, 3, 2, 3, 2, 1, 2, 1]," +
                " [3, 3, 2, 3, 3, 3, 3, 2, 1, 3, 2, 3, 1, 3, 3, 3, 1, 2, 2, 3, 3, 1, 2, 3, 2, 3]," +
                " [3, 1, 3, 2, 1, 1, 3, 3], [1, 1, 3, 3, 2, 3, 3, 1, 3, 3]," +
                " [3, 3, 3, 2, 1, 1, 3, 2, 2, 1, 3, 2, 3, 2, 1, 3, 2, 3, 1, 3, 1]," +
                " [3, 1, 2, 1, 1, 2, 2, 1, 3, 3, 1, 1, 2, 1, 1, 1]," +
                " [3, 1, 1, 3, 2, 3, 1, 2, 3, 3, 2, 1, 3, 3, 1, 3, 3, 1, 2, 2, 2, 2, 2, 1, 2]," +
                " [1, 1, 3, 2, 2, 3, 1, 2, 2, 2, 1, 1, 2], [1, 1, 3, 1, 1, 3, 3, 1, 3, 3, 1, 3, 3, 3, 2, 3]," +
                " [2, 2, 2, 3, 2, 2, 2, 3, 3, 3, 2, 3, 3, 1, 2, 1, 1, 2, 1, 1, 3, 3, 1, 3, 2, 1, 3, 2, 1, 3, 1, 3," +
                " 3, 2, 2, 1, 1, 1, 2, 2, 1, 3, 2, 1, 1, 3, 2, 1, 1, 1, 3, 2, 2, 1, 2, 2, 1, 3, 1, 1, 1, 2, 3, 2, 2," +
                " 3, 3, 2, 2, 3, 3, 3, 2, 2, 1, 1, 1, 1, 3, 2, 2, 1, 2, 2, 1, 3, 3, 3, 3]," +
                " [2, 2, 2, 3, 2, 1, 3, 3, 2, 1, 3, 1, 2, 3, 3, 2, 1, 2, 3, 1, 2, 2, 1, 3, 3, 2, 2, 2, 3, 3, 1, 1," +
                " 3, 3, 2, 1, 3, 1, 2, 3]," +
                " [3, 2, 3, 1, 3, 2, 1, 1, 3, 2, 3, 3, 1, 3, 2, 1, 1, 2, 3, 2, 1, 3, 2, 2, 3, 2, 2, 1, 2, 3, 2, 3," +
                " 2]," +
                " [2, 2, 1, 1, 1, 1, 2, 3, 2, 3, 1, 2, 3, 2, 2, 3, 3, 2, 3, 2, 1, 3, 2, 1, 2, 1, 2, 1, 3, 2, 3, 3," +
                " 3, 1, 2, 3, 1, 3, 2, 1, 2, 1, 2, 1, 3, 3, 1, 2, 1, 2, 1, 3, 2, 1, 1]," +
                " [1, 1, 3, 1, 3, 3, 2, 3, 2, 1, 3, 2, 1, 1, 3, 1, 2, 1, 1, 1, 2, 1, 3, 2, 3, 2, 2, 1, 2, 1, 1]," +
                " [1, 3, 1, 1, 2, 2, 1, 1, 3, 1, 2, 3, 2, 1, 3, 3, 2, 2, 3, 1, 2, 3]," +
                " [1, 1, 3, 1, 2, 1, 2, 2, 1, 3, 3, 2, 3, 2, 3, 3, 2, 2, 1, 2, 2, 1, 3, 3, 3, 3, 2, 1, 2, 3, 1, 1," +
                " 3, 3, 2, 2, 3, 1, 1, 1, 2, 2, 2, 3]," +
                " [3, 3, 1, 2, 2, 3, 3, 2, 1, 3, 3, 2, 1, 2, 1, 1, 1, 3, 2, 3, 2, 3, 1, 1, 1, 2, 2, 3, 1, 2, 3, 3," +
                " 1, 1, 3, 3], [2, 3, 2, 3, 3, 3, 2, 2, 2]," +
                " [1, 1, 3, 3, 1, 3, 3, 2, 2, 3, 2, 1, 2, 3, 1, 2, 1, 1, 2, 2, 2, 3, 3, 2, 1, 2, 2, 1, 1, 2, 1]," +
                " [2, 2, 2, 1, 3, 1, 3, 1, 2], ...]",
                "{[3, 3, 1, 2, 2, 3, 1, 1]=16, [1, 3, 3, 3, 3, 1, 1, 1]=15, [1, 1, 1, 3, 2, 3, 1, 1]=15," +
                " [3, 2, 3, 3, 3, 2, 2, 3]=15, [1, 3, 1, 2, 2, 1, 2, 2]=15, [2, 1, 3, 1, 1, 1, 2, 3]=15," +
                " [1, 2, 2, 2, 2, 3, 2, 2]=14, [1, 1, 2, 2, 1, 2, 1, 2]=14, [1, 2, 3, 2, 3, 1, 2, 1]=14" +
                ", [3, 2, 1, 3, 1, 3, 3, 2]=14}",
                32.010685000021894
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, null, 3]",
                "[[null, 1, null, null], [null, null, 3, null, 3, 1, 3, null], [1, 3, null], [3], [3, null, 1], [3]," +
                " [null, 3], [null], [3], [1, 3, 3], [null], [3], [null], [3], [1], [3], [1], [1], [3], [null, 1]" +
                ", ...]",
                "{[null]=167156, [3]=166869, [1]=166251, [null, 3]=28319, [1, 3]=27831, [null, null]=27782," +
                " [3, 1]=27773, [1, 1]=27761, [1, null]=27704, [3, null]=27683}",
                1.9993039999798474
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, null, 3]",
                "[[null, null, null, null, null, 1, null], [3, null, null, 3, 1, 3, null], [3, null, 3]," +
                " [1, 3, 1, null, 3], [null, 1, 3], [3, 3, 3, null, 3], [null, 1, 3, 3], [1, 3, 1], [3, 1, 1]," +
                " [null, null, 1, 3, 3, null], [null, 3, null, 3, null, 3, null], [null, 1, null]," +
                " [null, 3, 3, 1, 1, 3, 1, 1, 1], [3, null, 3, 1, 3, 3, 3, 1, null, null, 3], [null, 3, null, 3]," +
                " [3, 1, 3], [1, 3, 3, null], [1, 1, 1], [3, 3, 1, 1, 3, 3, 1, 1], [null, 1, 3], ...]",
                "{[null, 3, 3]=12598, [null, 3, 1]=12517, [3, 3, 1]=12472, [null, 1, 1]=12457, [3, null, 3]=12442," +
                " [1, 3, 3]=12437, [3, 3, null]=12420, [1, 1, 1]=12412, [3, 1, null]=12392, [3, 1, 3]=12372}",
                5.003739999996368
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, null, 3]",
                "[[3, null, 3, 1, 3, 3, 3, null, 3, 3, 1, null, 1, 3, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 3, null, 1," +
                " null, null, 1, 3, 3, null, 3, 3, null, 3, 1, null, 3, null, 3, null, 3, null, 1, null, 1]," +
                " [3, 3, null, 3, 3, 3, 3, null, 1, 3, null, 3, 1, 3, 3, 3, 1, null, null, 3, 3, 1, null, 3, null," +
                " 3], [3, 1, 3, null, 1, 1, 3, 3], [1, 1, 3, 3, null, 3, 3, 1, 3, 3]," +
                " [3, 3, 3, null, 1, 1, 3, null, null, 1, 3, null, 3, null, 1, 3, null, 3, 1, 3, 1]," +
                " [3, 1, null, 1, 1, null, null, 1, 3, 3, 1, 1, null, 1, 1, 1]," +
                " [3, 1, 1, 3, null, 3, 1, null, 3, 3, null, 1, 3, 3, 1, 3, 3, 1, null, null, null, null, null, 1," +
                " null], [1, 1, 3, null, null, 3, 1, null, null, null, 1, 1, null]," +
                " [1, 1, 3, 1, 1, 3, 3, 1, 3, 3, 1, 3, 3, 3, null, 3]," +
                " [null, null, null, 3, null, null, null, 3, 3, 3, null, 3, 3, 1, null, 1, 1, null, 1, 1, 3, 3, 1," +
                " 3, null, 1, 3, null, 1, 3, 1, 3, 3, null, null, 1, 1, 1, null, null, 1, 3, null, 1, 1, 3, null, 1," +
                " 1, 1, 3, null, null, 1, null, null, 1, 3, 1, 1, 1, null, 3, null, null, 3, 3, null, null, 3, 3, 3," +
                " null, null, 1, 1, 1, 1, 3, null, null, 1, null, null, 1, 3, 3, 3, 3]," +
                " [null, null, null, 3, null, 1, 3, 3, null, 1, 3, 1, null, 3, 3, null, 1, null, 3, 1, null, null," +
                " 1, 3, 3, null, null, null, 3, 3, 1, 1, 3, 3, null, 1, 3, 1, null, 3]," +
                " [3, null, 3, 1, 3, null, 1, 1, 3, null, 3, 3, 1, 3, null, 1, 1, null, 3, null, 1, 3, null, null," +
                " 3, null, null, 1, null, 3, null, 3, null]," +
                " [null, null, 1, 1, 1, 1, null, 3, null, 3, 1, null, 3, null, null, 3, 3, null, 3, null, 1, 3," +
                " null, 1, null, 1, null, 1, 3, null, 3, 3, 3, 1, null, 3, 1, 3, null, 1, null, 1, null, 1, 3, 3," +
                " 1, null, 1, null, 1, 3, null, 1, 1]," +
                " [1, 1, 3, 1, 3, 3, null, 3, null, 1, 3, null, 1, 1, 3, 1, null, 1, 1, 1, null, 1, 3, null, 3," +
                " null, null, 1, null, 1, 1]," +
                " [1, 3, 1, 1, null, null, 1, 1, 3, 1, null, 3, null, 1, 3, 3, null, null, 3, 1, null, 3]," +
                " [1, 1, 3, 1, null, 1, null, null, 1, 3, 3, null, 3, null, 3, 3, null, null, 1, null, null, 1, 3," +
                " 3, 3, 3, null, 1, null, 3, 1, 1, 3, 3, null, null, 3, 1, 1, 1, null, null, null, 3]," +
                " [3, 3, 1, null, null, 3, 3, null, 1, 3, 3, null, 1, null, 1, 1, 1, 3, null, 3, null, 3, 1, 1, 1," +
                " null, null, 3, 1, null, 3, 3, 1, 1, 3, 3], [null, 3, null, 3, 3, 3, null, null, null]," +
                " [1, 1, 3, 3, 1, 3, 3, null, null, 3, null, 1, null, 3, 1, null, 1, 1, null, null, null, 3, 3," +
                " null, 1, null, null, 1, 1, null, 1], [null, null, null, 1, 3, 1, 3, 1, null], ...]",
                "{[3, 3, 1, null, null, 3, 1, 1]=16, [1, 3, 3, 3, 3, 1, 1, 1]=15, [1, 1, 1, 3, null, 3, 1, 1]=15," +
                " [3, null, 3, 3, 3, null, null, 3]=15, [1, 3, 1, null, null, 1, null, null]=15," +
                " [null, 1, 3, 1, 1, 1, null, 3]=15, [1, null, null, null, null, 3, null, null]=14," +
                " [1, 1, null, null, 1, null, 1, null]=14, [1, null, 3, null, 3, 1, null, 1]=14," +
                " [3, null, 1, 3, 1, 3, 3, null]=14}",
                32.010685000021894
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4]",
                "[[2, 4, 1, 2], [2, 2, 3, 4, 2, 3, 1, 4, 3], [1, 3, 4, 2], [4], [4, 2], [3], [1, 3, 4], [2], [1]," +
                " [4, 4], [2], [1, 3], [3, 2], [3], [2], [3], [1], [3], [1], [1], ...]",
                "{[4]=125444, [2]=125303, [1]=125036, [3]=124486, [2, 3]=15820, [3, 3]=15793, [1, 1]=15774," +
                " [2, 2]=15720, [2, 4]=15688, [3, 4]=15683}",
                1.999585999979838
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4]",
                "[[2, 2, 4, 4, 2, 4, 2], [2, 2, 3, 4], [4, 3, 2, 4, 2], [3, 4, 2, 1], [1, 3, 4, 1, 2, 3, 1, 2]," +
                " [4, 4, 3], [3, 4, 3, 3, 2], [2, 1, 3, 3, 1], [3, 1, 1], [1, 3, 4, 3], [2, 4, 2, 4], [3, 3, 2]," +
                " [2, 3, 4, 4, 4, 4, 2], [4, 4, 4, 4, 2, 1, 4], [2, 3, 4, 4, 3, 1, 1, 3, 1, 1], [3, 3, 2]," +
                " [4, 3, 2, 4, 4, 3, 4, 4], [3, 3, 3], [2, 4, 4], [2, 3, 2, 3, 4, 1], ...]",
                "{[1, 2, 4]=5450, [1, 2, 1]=5359, [1, 4, 4]=5355, [3, 4, 4]=5307, [3, 3, 4]=5307, [2, 2, 4]=5280," +
                " [1, 3, 3]=5279, [4, 1, 4]=5273, [4, 3, 3]=5271, [2, 3, 2]=5269}",
                5.00315899999616
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4]",
                "[[4, 4, 3, 2, 4, 3, 1, 3, 4, 3, 3, 2, 3, 3, 1, 2, 1, 3, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 4, 3, 4, 4," +
                " 4, 2, 1, 2, 4, 2, 4, 1, 3, 3, 2, 3, 3, 4, 2, 3], [2, 3, 4, 4, 4, 4, 2, 4]," +
                " [3, 3, 2, 4, 3, 3, 4, 4, 4, 3, 3, 2, 1, 4, 3, 2, 4, 4, 3, 4, 4, 1, 3, 3, 3, 1, 2, 4, 4, 2, 3, 3," +
                " 1, 2, 3], [3, 1, 3, 4, 2, 4, 1, 1, 3], [1, 1, 4, 3, 4, 4, 3, 2, 3, 3, 4, 1]," +
                " [3, 3, 3, 2, 1, 1, 3, 2, 2, 1, 3, 2, 3, 2, 1, 3, 2, 3, 1, 3, 1, 3, 3]," +
                " [3, 1, 2, 1, 1, 2, 4, 2, 1, 3, 4, 3, 1, 1, 4, 2], [1, 2, 2, 3, 3, 3, 1, 3]," +
                " [3, 1, 4, 4, 1, 3, 2, 3, 4, 4, 1, 2, 3, 3, 2, 1, 4, 3, 3], [3, 4, 3, 1, 4, 2, 2, 2]," +
                " [1, 4, 4, 4, 4, 4, 1, 4, 3, 2, 2, 4, 3, 1, 2, 2, 4, 2, 1]," +
                " [4, 1, 1, 4, 3, 1, 4, 1, 3, 3, 1, 3, 4, 3, 1, 3, 3, 3, 2]," +
                " [2, 4, 2, 2, 3, 2, 2, 2, 3, 3, 3, 4, 2, 3, 4, 3, 1, 2, 1, 1, 2, 1, 1, 3, 3, 1, 3, 2, 4, 1, 3, 2," +
                " 1, 3, 1, 3, 3, 2, 4, 4, 2, 1, 1, 1, 2, 2, 1, 3, 2, 4, 1, 4, 1, 4, 3, 2, 1, 1, 1, 3, 2, 2, 1, 2, 2," +
                " 1, 3, 1, 1, 1, 2, 4, 4, 3, 4, 4, 4, 2, 2, 3, 3, 2, 2, 3, 3, 3, 2, 2, 1, 1]," +
                " [2, 4, 2, 2, 3, 4, 2, 1, 3, 3, 2, 4, 1, 3, 1, 2, 3, 4, 4, 3, 2, 1, 2, 3, 1, 2, 2, 4, 1, 3, 3, 4," +
                " 2, 4, 2, 4, 2, 3, 3, 4, 4, 1, 1, 3, 3, 2, 1, 3, 4, 1, 2, 4, 3, 4, 4, 4]," +
                " [3, 2, 3, 1, 3, 2, 1, 1, 3, 2, 3, 3, 1, 3, 2, 1, 4, 1, 2, 3, 2, 1, 3, 2, 4, 4, 2, 3, 2, 2, 4]," +
                " [2, 4, 3, 2, 3, 4, 2, 1]," +
                " [2, 2, 4, 1, 1, 4, 4, 1, 1, 4, 2, 3, 2, 3, 1, 4, 4, 2, 3, 2, 4, 2, 3, 3, 4, 2, 4, 3, 2, 1, 3, 2," +
                " 1, 4, 2, 1, 2, 1, 3, 2, 3, 3, 3, 1, 4, 2, 3, 1, 3, 2, 1, 4, 2, 4]," +
                " [4, 2, 1, 2, 1, 4, 3, 2, 1, 4, 1, 2]," +
                " [1, 1, 3, 1, 3, 4, 3, 2, 3, 2, 1, 3, 4, 2, 1, 4, 1, 4, 3, 4, 1, 2, 1, 1, 1, 2, 1, 3, 2, 3]," +
                " [3, 2, 4, 3, 4, 1, 3, 3, 3, 3, 2, 4, 4, 4], ...]",
                "{[3, 3, 1, 4, 4, 4, 3, 4]=6, [4, 1, 3, 1, 1, 3, 3, 3]=6, [1, 4, 2, 4, 3, 1, 3, 3]=6," +
                " [1, 4, 3, 1, 4, 2, 3, 4]=6, [2, 1, 2, 3, 1, 3, 1, 3]=5, [4, 4, 2, 3, 3, 2, 3, 2]=5," +
                " [2, 3, 4, 4, 4, 3, 1, 4]=5, [3, 1, 1, 2, 1, 4, 3, 3]=5, [2, 4, 3, 4, 2, 3, 1, 4]=5," +
                " [4, 2, 3, 1, 4, 4, 3, 1]=5}",
                32.008717000021356
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 2, 4]",
                "[[2, 4, 1, 2], [2, 2, 2, 4, 2, 2, 1, 4, 2], [1, 2, 4, 2], [4], [4, 2], [2], [1, 2, 4], [2], [1]," +
                " [4, 4], [2], [1, 2], [2, 2], [2], [2], [2], [1], [2], [1], [1], ...]",
                "{[2]=249789, [4]=125444, [1]=125036, [2, 2]=62887, [2, 4]=31371, [4, 2]=31153, [1, 2]=31119," +
                " [2, 1]=30818, [1, 1]=15774, [1, 4]=15648}",
                1.999585999979838
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 2, 4]",
                "[[2, 2, 4, 4, 2, 4, 2], [2, 2, 2, 4], [4, 2, 2, 4, 2], [2, 4, 2, 1], [1, 2, 4, 1, 2, 2, 1, 2]," +
                " [4, 4, 2], [2, 4, 2, 2, 2], [2, 1, 2, 2, 1], [2, 1, 1], [1, 2, 4, 2], [2, 4, 2, 4], [2, 2, 2]," +
                " [2, 2, 4, 4, 4, 4, 2], [4, 4, 4, 4, 2, 1, 4], [2, 2, 4, 4, 2, 1, 1, 2, 1, 1], [2, 2, 2]," +
                " [4, 2, 2, 4, 4, 2, 4, 4], [2, 2, 2], [2, 4, 4], [2, 2, 2, 2, 4, 1], ...]",
                "{[2, 2, 2]=41560, [2, 2, 4]=20918, [1, 2, 2]=20798, [2, 4, 2]=20791, [2, 1, 2]=20753," +
                " [4, 2, 2]=20552, [2, 2, 1]=20519, [2, 2, 2, 2]=14169, [1, 2, 4]=10717, [1, 2, 1]=10620}",
                5.00315899999616
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 2, 4]",
                "[[4, 4, 2, 2, 4, 2, 1, 2, 4, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 4, 2, 4, 4," +
                " 4, 2, 1, 2, 4, 2, 4, 1, 2, 2, 2, 2, 2, 4, 2, 2], [2, 2, 4, 4, 4, 4, 2, 4]," +
                " [2, 2, 2, 4, 2, 2, 4, 4, 4, 2, 2, 2, 1, 4, 2, 2, 4, 4, 2, 4, 4, 1, 2, 2, 2, 1, 2, 4, 4, 2, 2, 2," +
                " 1, 2, 2], [2, 1, 2, 4, 2, 4, 1, 1, 2], [1, 1, 4, 2, 4, 4, 2, 2, 2, 2, 4, 1]," +
                " [2, 2, 2, 2, 1, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1, 2, 2]," +
                " [2, 1, 2, 1, 1, 2, 4, 2, 1, 2, 4, 2, 1, 1, 4, 2], [1, 2, 2, 2, 2, 2, 1, 2]," +
                " [2, 1, 4, 4, 1, 2, 2, 2, 4, 4, 1, 2, 2, 2, 2, 1, 4, 2, 2], [2, 4, 2, 1, 4, 2, 2, 2]," +
                " [1, 4, 4, 4, 4, 4, 1, 4, 2, 2, 2, 4, 2, 1, 2, 2, 4, 2, 1]," +
                " [4, 1, 1, 4, 2, 1, 4, 1, 2, 2, 1, 2, 4, 2, 1, 2, 2, 2, 2]," +
                " [2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 2, 2, 4, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 4, 1, 2, 2," +
                " 1, 2, 1, 2, 2, 2, 4, 4, 2, 1, 1, 1, 2, 2, 1, 2, 2, 4, 1, 4, 1, 4, 2, 2, 1, 1, 1, 2, 2, 2, 1, 2, 2," +
                " 1, 2, 1, 1, 1, 2, 4, 4, 2, 4, 4, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1]," +
                " [2, 4, 2, 2, 2, 4, 2, 1, 2, 2, 2, 4, 1, 2, 1, 2, 2, 4, 4, 2, 2, 1, 2, 2, 1, 2, 2, 4, 1, 2, 2, 4," +
                " 2, 4, 2, 4, 2, 2, 2, 4, 4, 1, 1, 2, 2, 2, 1, 2, 4, 1, 2, 4, 2, 4, 4, 4]," +
                " [2, 2, 2, 1, 2, 2, 1, 1, 2, 2, 2, 2, 1, 2, 2, 1, 4, 1, 2, 2, 2, 1, 2, 2, 4, 4, 2, 2, 2, 2, 4]," +
                " [2, 4, 2, 2, 2, 4, 2, 1]," +
                " [2, 2, 4, 1, 1, 4, 4, 1, 1, 4, 2, 2, 2, 2, 1, 4, 4, 2, 2, 2, 4, 2, 2, 2, 4, 2, 4, 2, 2, 1, 2, 2," +
                " 1, 4, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 4, 2, 2, 1, 2, 2, 1, 4, 2, 4]," +
                " [4, 2, 1, 2, 1, 4, 2, 2, 1, 4, 1, 2]," +
                " [1, 1, 2, 1, 2, 4, 2, 2, 2, 2, 1, 2, 4, 2, 1, 4, 1, 4, 2, 4, 1, 2, 1, 1, 1, 2, 1, 2, 2, 2]," +
                " [2, 2, 4, 2, 4, 1, 2, 2, 2, 2, 2, 4, 4, 4], ...]",
                "{[2, 2, 2, 2, 2, 2, 2, 2]=155, [2, 2, 2, 2, 2, 2, 2, 1]=97, [2, 2, 2, 2, 2, 4, 2, 2]=93," +
                " [2, 1, 2, 2, 2, 2, 2, 2]=87, [2, 2, 2, 2, 2, 2, 2, 2, 2]=83, [2, 2, 4, 2, 2, 2, 2, 2]=80," +
                " [2, 2, 2, 4, 2, 2, 2, 2]=79, [2, 4, 2, 2, 2, 2, 2, 2]=78, [2, 2, 1, 2, 2, 2, 2, 2]=77" +
                ", [2, 2, 2, 2, 2, 2, 4, 2]=73}",
                32.008717000021356
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2], [2], [2, 2], [2], [2, 2, 2], [2], [2]," +
                " [2, 2], [2], [2, 2], [2, 2], [2], [2], [2], [2], [2], [2], [2], ...]",
                "{[2]=500269, [2, 2]=249809, [2, 2, 2]=124830, [2, 2, 2, 2]=62682, [2, 2, 2, 2, 2]=31195," +
                " [2, 2, 2, 2, 2, 2]=15549, [2, 2, 2, 2, 2, 2, 2]=7831, [2, 2, 2, 2, 2, 2, 2, 2]=3938," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2]=2013, [2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=951}",
                1.999585999979838
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2], [2, 2, 2, 2, 2], [2, 2, 2, 2, 2], [2, 2, 2], [2, 2, 2, 2], [2, 2, 2, 2], [2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2], [2, 2, 2], [2, 2, 2, 2, 2, 2], ...]",
                "{[2, 2, 2]=332475, [2, 2, 2, 2]=222950, [2, 2, 2, 2, 2]=148435, [2, 2, 2, 2, 2, 2]=98386," +
                " [2, 2, 2, 2, 2, 2, 2]=65648, [2, 2, 2, 2, 2, 2, 2, 2]=43847, [2, 2, 2, 2, 2, 2, 2, 2, 2]=29430," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=19567, [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=13014" +
                ", [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=8771}",
                5.00315899999616
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[2, 2, 2, 2]",
                "[[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], [2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2," +
                " 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2], ...]",
                "{[2, 2, 2, 2, 2, 2, 2, 2]=40181, [2, 2, 2, 2, 2, 2, 2, 2, 2]=38543," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=37070, [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=35343," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=33943, [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=32305," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=31206," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=29856," +
                " [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=28774" +
                ", [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]=27718}",
                32.008717000021356
        );
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[6, 9, 6, 4], [6, 2, 7, 2, 7, 6, 6], [10], [8], [3, 3], [5, 7, 4], [6], [5], [4, 8], [6], [3, 4]," +
                " [7], [9], [1, 9], [9], [5], [4], [4], [10, 6, 3, 6], [7], ...]",
                "{[6]=50479, [5]=50284, [2]=50162, [4]=50062, [3]=50049, [10]=50015, [9]=49998, [1]=49982," +
                " [8]=49890, [7]=49879}",
                1.9987289999797695
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[6, 4, 2, 8, 6, 4, 6], [6, 2, 7], [6, 6, 10, 5, 8], [5, 7, 4, 6, 3, 5, 6, 1], [3, 4, 7, 7, 9, 1]," +
                " [9, 9, 5], [4, 3, 4], [10, 6, 3, 6], [6, 7, 4, 4, 4, 6, 4], [2, 4, 2, 8, 7, 8, 3]," +
                " [10, 3, 8, 5, 9, 7, 5], [7, 10, 8], [4, 8, 3, 8, 7, 7, 5, 6], [2, 7, 1, 7, 1], [5, 3, 8, 1, 1]," +
                " [7, 7, 9, 3, 8, 4, 5, 4], [7, 1, 10, 3], [3, 6, 10, 3, 9], [2, 3, 2, 10], [1, 2, 5, 5, 2], ...]",
                "{[7, 3, 8]=397, [2, 8, 9]=390, [10, 1, 2]=389, [6, 9, 3]=383, [5, 2, 3]=383, [7, 9, 8]=382," +
                " [4, 7, 3]=378, [9, 10, 7]=377, [4, 10, 3]=376, [6, 6, 1]=376}",
                5.002305999996172
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "[[4, 8, 3, 6, 3, 4, 7, 7, 9, 1, 9, 9, 9, 5, 9, 4, 3, 4, 2, 1, 10, 6, 3, 6, 7, 10, 7, 1, 6, 7, 4, 4," +
                " 4, 6, 4, 7, 6, 4, 3, 10, 2, 4, 2, 8, 7, 8, 3, 6]," +
                " [7, 10, 8, 3, 3, 4, 7, 3, 10, 9, 4, 8, 3, 8, 7, 7, 5, 6, 2], [7, 1, 4, 5, 5, 3, 8, 1, 1, 7]," +
                " [10, 3, 10, 2, 3, 6, 10, 3, 9, 3, 5, 2, 3, 2, 10, 8, 10, 7, 1, 1, 2, 5, 5, 2, 6, 9, 3, 9, 5, 6," +
                " 1], [4, 1, 10, 3, 8, 1, 6, 7, 6, 3, 3, 1, 4, 9, 6, 4, 2, 4, 10, 4, 9, 2, 6, 1, 5, 8]," +
                " [2, 10, 8, 2, 1, 9, 8, 10, 3, 8, 2, 5, 9, 1, 5, 4, 7, 1], [3, 1, 3, 6, 7, 10, 6, 8, 2, 4, 1]," +
                " [2, 2, 10, 3, 6, 10, 3, 3, 4, 2, 3, 8, 7, 1, 2, 9, 1, 9, 5, 7, 7, 5, 8, 1, 7, 10, 5, 7, 9, 2, 8," +
                " 1, 1, 5, 2, 10, 5, 2, 1, 4, 5, 4, 6, 1, 9, 9, 3, 6, 2, 9, 2, 6, 9, 10, 8, 7, 4, 4, 4, 10, 6, 3, 7," +
                " 6, 3, 7, 2, 10, 1, 9, 4, 5, 3, 8, 2, 4, 9, 10, 2, 5, 8, 3, 7]," +
                " [4, 2, 2, 4, 6, 5, 7, 7, 10, 8, 1, 1, 2, 2, 5, 10, 3, 5, 10, 9, 3, 3, 2, 4, 2, 6, 7, 8, 5, 1, 9," +
                " 4, 8, 8, 9, 4, 9, 2, 7, 10]," +
                " [3, 5, 10, 1, 9, 7, 6, 7, 4, 5, 10, 3, 6, 1, 10, 6, 6, 2, 1, 2, 8, 10, 3, 4, 10]," +
                " [6, 6, 8, 5, 9, 8, 4, 5, 4, 2, 3, 10, 9, 8, 6, 3, 6, 4, 10, 3, 6, 2, 6, 5, 2, 10, 9, 7, 6, 7, 3," +
                " 4, 6, 5, 6, 1, 6, 8, 6, 7, 3, 1, 4, 1, 6, 5, 3, 6, 1, 8, 9, 3, 7, 10, 8]," +
                " [5, 6, 10, 7, 2, 5, 4, 9, 5, 1, 2, 9, 3, 2, 5, 6, 5, 1, 3, 8, 5, 3, 3, 4]," +
                " [1, 3, 8, 1, 5, 4, 2, 6, 4, 1, 5, 8, 7]," +
                " [9, 9, 10, 10, 1, 7, 10, 3, 2, 3, 2, 6, 1, 8, 4, 9, 3, 3, 8, 10, 4, 9, 2, 1, 5, 7, 8, 6, 1, 9, 2," +
                " 2, 2, 4, 4, 6, 5, 6, 3, 5, 3, 3, 10, 10, 5, 4, 5, 5, 9, 6, 7, 6, 7, 2]," +
                " [3, 7, 1, 6, 6, 10, 8, 4, 7, 10, 5, 4]," +
                " [10, 8, 6, 3, 4, 7, 7, 10, 10, 5, 6, 9, 2, 10, 8, 8, 5, 5, 6, 8, 8, 7, 4, 7, 5, 10, 5, 1, 5, 8]," +
                " [2, 9, 8, 2, 10, 7, 2, 8, 9, 10, 9, 9, 4, 2, 8, 1, 2, 2, 8, 4, 9, 5, 1, 2, 9]," +
                " [8, 3, 10, 1, 8, 2, 3, 7, 2, 7, 1, 8, 1, 6, 8, 2, 3, 10, 9, 6, 4, 9, 5, 7, 2, 5, 3, 9, 9, 9, 2, 1," +
                " 1, 4, 6, 5, 2, 8, 8, 7, 2, 6, 9, 1, 6, 1, 7, 7, 10, 2, 9, 7, 9, 9, 9, 3, 6, 7, 2, 4, 7, 9, 2, 10," +
                " 6, 5, 10, 5, 3, 2, 3, 8, 4, 9, 7, 5, 2, 3, 3, 10, 7, 4, 3, 7, 7, 2, 1, 7, 3, 6, 4, 1, 2, 1, 6, 3," +
                " 1, 2, 4, 7], [1, 1, 4, 5, 8, 9, 9, 9, 8, 5, 10, 8, 8, 5, 3, 4, 1, 4]," +
                " [5, 6, 1, 7, 5, 8, 4, 2, 6, 9, 2, 10, 8, 6, 5, 7, 1, 9, 1, 4, 9, 1, 9, 3, 2, 9, 3, 2], ...]",
                "{[6, 9, 1, 7, 2, 5, 10, 2]=2, [1, 4, 4, 8, 9, 2, 1, 10]=2, [6, 1, 3, 1, 8, 8, 3, 7]=2," +
                " [7, 9, 1, 3, 4, 4, 4, 7]=2, [3, 4, 5, 9, 3, 3, 3, 8]=2, [3, 5, 2, 10, 9, 7, 6, 10, 6]=2," +
                " [4, 8, 3, 6, 3, 4, 7, 7, 9, 1, 9, 9, 9, 5, 9, 4, 3, 4, 2, 1, 10, 6, 3, 6, 7, 10, 7, 1, 6, 7, 4, 4," +
                " 4, 6, 4, 7, 6, 4, 3, 10, 2, 4, 2, 8, 7, 8, 3, 6]=1," +
                " [7, 10, 8, 3, 3, 4, 7, 3, 10, 9, 4, 8, 3, 8, 7, 7, 5, 6, 2]=1, [7, 1, 4, 5, 5, 3, 8, 1, 1, 7]=1," +
                " [10, 3, 10, 2, 3, 6, 10, 3, 9, 3, 5, 2, 3, 2, 10, 8, 10, 7, 1, 1, 2, 5, 5, 2, 6, 9, 3, 9, 5, 6," +
                " 1]=1}",
                31.997066000022638
        );
        listsAtLeast_helper(
                2,
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "[[3, 10, 7, 7], [3], [7, 1, 3], [1, 2], [4, 8, 2], [2], [2], [2], [8], [9, 15, 6, 12, 6], [2], [1]," +
                " [12], [7, 4], [2, 6, 2, 5, 1], [5], [1], [10], [2], [5], ...]",
                "{[1]=124902, [2]=94480, [3]=69971, [4]=52478, [5]=39569, [6]=29407, [7]=22369, [8]=16718," +
                " [1, 1]=15589, [9]=12621}",
                2.001126999979881
        );
        listsAtLeast_helper(
                5,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "[[10, 7, 7, 4, 10, 1, 3], [2, 7, 8, 2, 3], [2, 1, 2], [8, 5, 9], [6, 12, 6, 1, 2, 1, 1, 13, 9, 4]," +
                " [6, 2, 5, 1, 6, 1, 1], [10, 3, 1], [1, 1, 4, 2, 5], [4, 5, 4], [12, 2, 2, 1], [4, 1, 3, 1, 1]," +
                " [2, 1, 3, 1, 5, 1, 2, 3], [5, 4, 4, 9, 7, 4], [6, 7, 5], [10, 1, 1], [2, 1, 3], [3, 4, 13]," +
                " [10, 3, 1, 4, 19, 5, 7, 6, 2], [1, 22, 17], [1, 2, 1, 3], ...]",
                "{[1, 1, 1]=5214, [1, 1, 2]=3940, [2, 1, 1]=3856, [1, 2, 1]=3855, [1, 1, 3]=2974, [3, 1, 1]=2942," +
                " [1, 2, 2]=2859, [2, 1, 2]=2823, [2, 2, 1]=2821, [1, 3, 1]=2789}",
                5.001189999995907
        );
        listsAtLeast_helper(
                32,
                8,
                P.withScale(4).positiveIntegersGeometric(),
                "[[7, 8, 2, 3, 1, 2, 1, 2, 1, 8, 5, 9, 15, 6, 12, 6, 1, 2, 1, 1, 13, 9, 4, 7, 6, 2, 5, 1, 6, 1, 1," +
                " 1, 10, 3, 1, 5, 1, 1, 4, 2, 5, 1, 4, 5, 4, 2, 12, 2]," +
                " [1, 6, 2, 1, 3, 1, 5, 1, 2, 3, 6, 5, 4, 4, 9, 7, 4, 1, 6]," +
                " [5, 1, 10, 1, 1, 1, 2, 1, 3, 2, 3, 4, 13]," +
                " [17, 2, 1, 2, 1, 3, 4, 3, 2, 7, 1, 1, 3, 4, 2, 4, 1, 1, 4, 3, 2, 1, 1, 19, 1, 1, 2, 7, 3, 14, 2," +
                " 20, 4, 1, 2, 5, 2, 8, 5, 2, 7, 3, 4, 13, 1, 4, 3, 7, 2, 7, 1, 1, 6, 5, 2, 4, 4, 3, 1, 5, 3, 2, 4," +
                " 10, 8, 1, 2, 6, 2, 5, 2, 5, 1, 10, 8, 3, 2, 2, 2, 2, 2, 1, 2]," +
                " [5, 2, 3, 3, 2, 4, 2, 6, 11, 2, 18, 1, 1, 2, 7, 4, 2, 4, 2, 1, 1, 2, 7, 2, 1, 6, 9, 3, 1, 3, 1, 6," +
                " 1, 4, 5, 6, 6, 1, 1, 3, 2, 10, 2, 7, 1, 6, 1, 2, 7, 1, 3, 1]," +
                " [13, 6, 8, 4, 1, 8, 1, 1, 10, 4, 1, 5, 5, 1]," +
                " [3, 8, 4, 3, 3, 1, 6, 2, 1, 5, 5, 2, 6, 15, 2, 4, 2, 3, 2, 10]," +
                " [1, 4, 7, 7, 3, 2, 1, 9, 3, 1, 4, 2]," +
                " [3, 4, 9, 3, 3, 8, 6, 1, 4, 4, 2, 2, 1, 4, 1, 5, 17, 1, 2, 9, 2, 1, 2, 1, 2, 9, 5, 3, 8, 3, 15, 8," +
                " 2, 5, 12, 4, 2, 3, 1, 2, 1, 2, 3, 1, 3, 1, 2, 5, 6, 2, 8, 2, 4, 1, 4, 8, 2, 3, 2, 3, 5, 3, 4, 2," +
                " 3, 3, 2, 1, 1, 4, 3, 10, 1, 14, 4, 3, 10, 1, 4, 1, 5, 8, 2, 5, 7, 2, 2, 2, 10, 13, 6, 5, 3, 3, 4," +
                " 1, 2, 2, 3, 2, 5, 4, 5, 15, 3, 2, 2, 1, 6, 1, 2, 1], [2, 2, 8, 1, 8, 1, 1, 7, 4, 2, 1]," +
                " [1, 7, 11, 6, 4, 3, 1, 2, 4, 3, 1, 8, 7, 3, 3, 1, 6, 2, 10]," +
                " [2, 2, 8, 7, 4, 1, 3, 4, 5, 1, 2, 1, 4, 7, 2, 4, 2, 1, 3, 1, 3, 5]," +
                " [2, 5, 5, 3, 2, 1, 2, 1, 1, 6, 2, 5, 8, 1, 1, 3, 6, 4, 13, 3, 8]," +
                " [6, 5, 9, 1, 8, 1, 5, 7, 3, 12, 6, 2, 2, 2, 2, 3, 16, 8, 1, 6, 6, 9, 6, 2, 10, 3, 2, 4, 3, 9, 2," +
                " 2, 3]," +
                " [2, 4, 5, 3, 1, 6, 1, 3, 14, 4, 1, 1, 6, 1, 9, 1, 3, 1, 3, 15, 6, 1, 1, 1, 5, 4, 10, 5, 8, 17, 5," +
                " 1, 11, 1, 2, 4, 3, 1, 5, 7, 1, 5, 4, 2, 6, 6, 6, 2, 2, 4, 4, 2, 1, 4, 1, 6, 4, 1, 5, 3, 1, 10, 3," +
                " 1, 1, 1, 1, 2, 1, 6, 8, 3, 1, 1, 5, 3, 3, 4, 2, 1]," +
                " [4, 7, 4, 5, 4, 2, 5, 1, 2, 8, 2, 2, 8, 11, 4, 8, 4, 9, 6]," +
                " [5, 12, 4, 2, 1, 2, 9, 6, 8, 2, 2, 3, 8, 1, 9, 4, 10, 2, 3], [6, 5, 6, 1, 2, 3, 11, 6, 4]," +
                " [16, 2, 1, 1, 5, 5, 1, 1, 1, 4, 6, 2, 1]," +
                " [17, 1, 7, 2, 3, 7, 1, 3, 1, 6, 1, 3, 1, 3, 1, 3, 3, 1, 11, 1, 2, 5, 5, 1, 3, 3, 2, 2, 8, 1, 3, 5," +
                " 15, 4, 1, 2, 4, 1, 10, 2, 1, 1, 2, 1, 7, 3, 2, 3], ...]",
                "{[1, 2, 3, 1, 1, 4, 4, 1]=3, [1, 1, 1, 1, 1, 2, 4, 6]=3, [4, 6, 1, 2, 1, 1, 3, 1]=3," +
                " [1, 7, 3, 2, 6, 1, 3, 1]=2, [3, 1, 6, 2, 9, 3, 1, 5, 2]=2, [3, 1, 1, 5, 3, 3, 2, 1]=2," +
                " [4, 2, 1, 6, 1, 1, 2, 2]=2, [3, 2, 3, 1, 1, 2, 2, 2, 2]=2, [1, 1, 1, 1, 3, 1, 3, 7, 12]=2," +
                " [3, 1, 6, 3, 1, 3, 3, 1]=2}",
                32.00730000002313
        );
        listsAtLeast_helper(
                2,
                1,
                repeat(1),
                "[[1, 1, 1, 1], [1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1], [1, 1, 1], [1], [1, 1]," +
                " [1, 1, 1, 1], [1], [1], [1, 1, 1], [1, 1, 1], [1, 1, 1], [1], [1, 1, 1], [1], [1], [1, 1], [1, 1]," +
                " [1], ...]",
                "{[1]=499125, [1, 1]=250897, [1, 1, 1]=124849, [1, 1, 1, 1]=62518, [1, 1, 1, 1, 1]=31407," +
                " [1, 1, 1, 1, 1, 1]=15634, [1, 1, 1, 1, 1, 1, 1]=7825, [1, 1, 1, 1, 1, 1, 1, 1]=3926," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1]=1896, [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=956}",
                2.0008359999800347
        );
        listsAtLeast_helper(
                5,
                3,
                repeat(1),
                "[[1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1], [1, 1, 1, 1, 1], [1, 1, 1]," +
                " [1, 1, 1, 1], [1, 1, 1], [1, 1, 1, 1], [1, 1, 1], [1, 1, 1, 1, 1, 1], ...]",
                "{[1, 1, 1]=333813, [1, 1, 1, 1]=221150, [1, 1, 1, 1, 1]=148025, [1, 1, 1, 1, 1, 1]=98992," +
                " [1, 1, 1, 1, 1, 1, 1]=66270, [1, 1, 1, 1, 1, 1, 1, 1]=43747, [1, 1, 1, 1, 1, 1, 1, 1, 1]=29389," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=19567, [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=12958" +
                ", [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=8571}",
                5.002096999996331
        );
        listsAtLeast_helper(
                32,
                8,
                repeat(1),
                "[[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1," +
                " 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], ...]",
                "{[1, 1, 1, 1, 1, 1, 1, 1]=39940, [1, 1, 1, 1, 1, 1, 1, 1, 1]=38196," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=36988, [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=35334," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=33831, [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=32551," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=31521," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=30149," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=28763," +
                " [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]=27543}",
                32.00360900002322
        );
        listsAtLeast_fail_helper(5, 3, Collections.emptyList());
        listsAtLeast_fail_helper(5, 3, Arrays.asList(1, 2, 3));
        listsAtLeast_fail_helper(5, 5, P.integers());
        listsAtLeast_fail_helper(4, 5, P.integers());
    }

    private static void stringsAtLeast_int_String_helper(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsAtLeast(minSize, input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsAtLeast_int_String_fail_helper(int scale, int minSize, @NotNull String input) {
        try {
            toList(P.withScale(scale).stringsAtLeast(minSize, input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringsAtLeast_int_String() {
        stringsAtLeast_int_String_helper(
                2,
                1,
                "a",
                "[aaaa, aaaaaaaaa, aaaa, a, aa, a, aaa, a, a, aa, a, aa, aa, a, a, a, a, a, a, a, ...]",
                "{a=500269, aa=249809, aaa=124830, aaaa=62682, aaaaa=31195, aaaaaa=15549, aaaaaaa=7831," +
                " aaaaaaaa=3938, aaaaaaaaa=2013, aaaaaaaaaa=951}",
                1.999585999979838
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "a",
                "[aaaaaaa, aaaa, aaaaa, aaaa, aaaaaaaa, aaa, aaaaa, aaaaa, aaa, aaaa, aaaa, aaa, aaaaaaa, aaaaaaa," +
                " aaaaaaaaaa, aaa, aaaaaaaa, aaa, aaa, aaaaaa, ...]",
                "{aaa=332475, aaaa=222950, aaaaa=148435, aaaaaa=98386, aaaaaaa=65648, aaaaaaaa=43847," +
                " aaaaaaaaa=29430, aaaaaaaaaa=19567, aaaaaaaaaaa=13014, aaaaaaaaaaaa=8771}",
                5.00315899999616
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "a",
                "[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa," +
                " aaaaaaaaa, aaaaaaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaaaaaaaaaa, aaaaaaaa, aaaaaaaaaaaaaaaaaaa," +
                " aaaaaaaa, aaaaaaaaaaaaaaaaaaa, aaaaaaaaaaaaaaaaaaa," +
                " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa," +
                " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa," +
                " aaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaaaaaa," +
                " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaaaaaaaa, ...]",
                "{aaaaaaaa=40181, aaaaaaaaa=38543, aaaaaaaaaa=37070, aaaaaaaaaaa=35343, aaaaaaaaaaaa=33943," +
                " aaaaaaaaaaaaa=32305, aaaaaaaaaaaaaa=31206, aaaaaaaaaaaaaaa=29856, aaaaaaaaaaaaaaaa=28774" +
                ", aaaaaaaaaaaaaaaaa=27718}",
                32.008717000021356
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "abc",
                "[babb, bbcbcacb, acb, c, cba, c, bc, b, c, acc, b, c, b, c, a, c, a, a, c, ba, ...]",
                "{b=167156, c=166869, a=166251, bc=28319, ac=27831, bb=27782, ca=27773, aa=27761, ab=27704, cb=27683}",
                1.9993039999798474
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "abc",
                "[bbbbbab, cbbcacb, cbc, acabc, bac, cccbc, bacc, aca, caa, bbaccb, bcbcbcb, bab, bccaacaaa," +
                " cbcacccabbc, bcbc, cac, accb, aaa, ccaaccaa, bac, ...]",
                "{bcc=12598, bca=12517, cca=12472, baa=12457, cbc=12442, acc=12437, ccb=12420, aaa=12412, cab=12392" +
                ", cac=12372}",
                5.003739999996368
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "abc",
                "[cbcacccbccabaccaacaacaaccbabbaccbccbcabcbcbcbaba, ccbccccbacbcacccabbccabcbc, cacbaacc," +
                " aaccbccacc, cccbaacbbacbcbacbcaca, cabaabbaccaabaaa, caacbcabccbaccaccabbbbbab, aacbbcabbbaab," +
                " aacaaccaccacccbc," +
                " bbbcbbbcccbccabaabaaccacbacbacaccbbaaabbacbaacbaaacbbabbacaaabcbbccbbcccbbaaaacbbabbacccc," +
                " bbbcbaccbacabccbabcabbaccbbbccaaccbacabc, cbcacbaacbccacbaabcbacbbcbbabcbcb," +
                " bbaaaabcbcabcbbccbcbacbababacbcccabcacbababaccababacbaa, aacaccbcbacbaacabaaabacbcbbabaa," +
                " acaabbaacabcbaccbbcabc, aacababbaccbcbccbbabbaccccbabcaaccbbcaaabbbc," +
                " ccabbccbaccbabaaacbcbcaaabbcabccaacc, bcbcccbbb, aaccaccbbcbabcabaabbbccbabbaaba, bbbacacab, ...]",
                "{ccabbcaa=16, accccaaa=15, aaacbcaa=15, cbcccbbc=15, acabbabb=15, bacaaabc=15, abbbbcbb=14," +
                " aabbabab=14, abcbcaba=14, cbacaccb=14}",
                32.010685000021894
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "abbc",
                "[bcab, bbbcbbacb, abcb, c, cb, b, abc, b, a, cc, b, ab, bb, b, b, b, a, b, a, a, ...]",
                "{b=249789, c=125444, a=125036, bb=62887, bc=31371, cb=31153, ab=31119, ba=30818, aa=15774, ac=15648}",
                1.999585999979838
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "abbc",
                "[bbccbcb, bbbc, cbbcb, bcba, abcabbab, ccb, bcbbb, babba, baa, abcb, bcbc, bbb, bbccccb, ccccbac," +
                " bbccbaabaa, bbb, cbbccbcc, bbb, bcc, bbbbca, ...]",
                "{bbb=41560, bbc=20918, abb=20798, bcb=20791, bab=20753, cbb=20552, bba=20519, bbbb=14169," +
                " abc=10717, aba=10620}",
                5.00315899999616
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "abbc",
                "[ccbbcbabcbbbbbababbaabaabaabcbcccbabcbcabbbbbcbb, bbccccbc, bbbcbbcccbbbacbbccbccabbbabccbbbabb," +
                " babcbcaab, aacbccbbbbca, bbbbaabbbabbbbabbbababb, babaabcbabcbaacb, abbbbbab, baccabbbccabbbbacbb," +
                " bcbacbbb, acccccacbbbcbabbcba, caacbacabbabcbabbbb," +
                " bcbbbbbbbbbcbbcbabaabaabbabbcabbababbbccbaaabbabbcacacbbaaabbbabbabaaabccbcccbbbbbbbbbbbaa," +
                " bcbbbcbabbbcababbccbbabbabbcabbcbcbcbbbccaabbbabcabcbccc, bbbabbaabbbbabbacabbbabbccbbbbc," +
                " bcbbbcba, bbcaaccaacbbbbaccbbbcbbbcbcbbabbacbababbbbbacbbabbacbc, cbabacbbacab," +
                " aababcbbbbabcbacacbcabaaababbb, bbcbcabbbbbccc, ...]",
                "{bbbbbbbb=155, bbbbbbba=97, bbbbbcbb=93, babbbbbb=87, bbbbbbbbb=83, bbcbbbbb=80, bbbcbbbb=79," +
                " bcbbbbbb=78, bbabbbbb=77, bbbbbbcb=73}",
                32.008717000021356
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "Mississippi",
                "[spss, sisisss, i, iss, iss, s, i, si, s, ss, i, ip, iM, i, p, i, s, s, psss, s, ...]",
                "{i=182168, s=181773, p=91352, M=45380, si=33041, ii=32991, is=32976, ss=32971, sp=16706, pi=16449}",
                1.9990949999798069
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "Mississippi",
                "[ssiisss, sis, ssipi, issssisM, sssisi, iMp, pips, psssi, ssssss, isiisis, psiiips, iMs," +
                " siisisisis, iisMs, isi, isi, MMss, sspsisi, sMii, spis, ...]",
                "{sss=16261, sis=16159, iii=16155, iss=16050, ssi=16043, iis=16002, isi=15947, sii=15643, sip=8121" +
                ", spi=8107}",
                5.003636999996235
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "Mississippi",
                "[sisssssisipiMpippipsssiMpsssispsMssssssssssspisi, spisssssppsiisisisisiiis, sMisiiis, Msssssssss," +
                " iipspisspspsiisipipsM, iiiispspi, ippssspsM, sMpsiMssisssMspssi, iiiipsssiMipiiMp," +
                " issMiisMsiMssispsi," +
                " iipsspsissisisMipMpissiiMspispiiMMiipiiiMsisisMppssipispipisssspssssissipMpsisiisppiii," +
                " siississpiMMiiiipsippssisissiiiMipsiiipspisp, isipMpssissipssMpssiMiiip," +
                " ssiipisisispipisssspissisiippssssssiisMsisssMsMsissMipiss, iiispsiiispiMipsiisiMsiisiis," +
                " MsiMisissMiisp, pipppMspsisiisMispssiipspiMisisMpiiisissississppisii, ssMsspisispissMpsiss," +
                " piisssssppispipiiiiiis, iisiisspispsiiisMip, ...]",
                "{isssssss=23, siiiisss=22, sssiisss=22, sisisiii=21, ssssisii=21, iisssiss=21, ssissisi=20," +
                " isiisisi=20, sissiisi=20, iisiissi=20}",
                32.00263800002314
        );
        stringsAtLeast_int_String_fail_helper(5, 3, "");
        stringsAtLeast_int_String_fail_helper(5, 5, "abc");
        stringsAtLeast_int_String_fail_helper(4, 5, "abc");
    }

    private static void stringsAtLeast_int_helper(
            int scale,
            int minSize,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsAtLeast(minSize)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsAtLeast_int_fail_helper(int scale, int minSize) {
        try {
            toList(P.withScale(scale).stringsAtLeast(minSize));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringsAtLeast_int() {
        stringsAtLeast_int_helper(
                2,
                1,
                "[ε䊿\u2538\u31e5, \udd15몱ﲦ䯏ϡ罖\u19dc刿ㄾ, ᬜK㵏ꏹ, 㩷, 纫\ufe2d, 䝲, 坤琖\u2a43, \uea45, 蕤," +
                " \u2b63\uf637, 鸅, \uee1c\u33b2, ᅺ됽, 䇺, \ue9fd, 㖊, \uaaf0, 覚, 䱸, \u2e24, ...]",
                "{瓫=24, 簐=22, 듑=22, 瀯=21, \uf3e9=21, 䝞=21, 抷=20, 䒢=20, Ẑ=20, 텁=20}",
                1.999585999979838
        );
        stringsAtLeast_int_helper(
                5,
                3,
                "[\u31e5髽肣\uf6ffﳑ赧\ue215, \udd15몱ﲦ䯏, 刿ㄾ䲵箿偵, K㵏ꏹ缄, 坤琖\u2a43퉌\uea45\ue352蕤餥," +
                " \u2b63\uf637鸂, \u33b2酓캆ᅺ됽, \ue9fd\u2aec㖊짎\uaaf0, 覚돘䱸, \uf878ሮܓ鄒, 尩굿\uecf5ꪻ, \ue8b2빮빅," +
                " 瀵컦刓嗏\u3353\ue2d3\ud805, 䫯噋\uf36fꌻ躁\ue87c홃, 壙\udd82픫鼧\u061a\u2e94穨㽖ﶼ䥔, 糦嗮\uf329," +
                " ꯃ慚총\u0e77\uf36bB㽿\u2a57, \udec6ꅪ\udcc6, \u337d萋\u0d5b, 쪡詪쀝\u1366\uf21bᵠ, ...]",
                "{\u31e5髽肣\uf6ffﳑ赧\ue215=1, \udd15몱ﲦ䯏=1, 刿ㄾ䲵箿偵=1, K㵏ꏹ缄=1, 坤琖\u2a43퉌\uea45\ue352蕤餥=1," +
                " \u2b63\uf637鸂=1, \u33b2酓캆ᅺ됽=1, \ue9fd\u2aec㖊짎\uaaf0=1, 覚돘䱸=1, \uf878ሮܓ鄒=1}",
                5.00315899999616
        );
        stringsAtLeast_int_helper(
                32,
                8,
                "[\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒\uff03" +
                "띯\ue5cb\ua7b1聆尩굿\uecf5ꪻ疜\ue8b2빮빅\ue2da䟆\ue78f㱉泦, 瀵컦刓嗏\u3353\ue2d3\ud805ឃ," +
                " 糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ\udcc6\u0964\u337d萋\u0d5b詵\ud8ca" +
                "䍾徜쪡詪, 駆퉐庺\u2293\ued0d䴻ꎤ槔横, 䃼匀낛띆ﱓ㝏ᯖ\uea55\ue9c6儖䦣\u202c," +
                " ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხ叙繲\uefc8嶂췴䔾턞, \uab6e䝀㥑\u2e64년믱젯䁅偘滞\ue89bᖒ㿘燔ꎿ趵," +
                " 㙴ᶙ䁩聂ꃖꐲ\udc58\u26f2, 쳮陜阏顓\u2320쓎狙쟒㕯뚗\u32b0頵\u2606ꎚ궥婜쇻ᅒ댲, 旞\u2613\u19de\u0618戯韕똽\u25ad," +
                " 죴\u0d47㚏帇퀯\uebc7晸犋鈖ꤥ쿕\u0f1b\u2eea\uf800ᓑ濙䢗瞁퓰," +
                " 梏\u2684\ue40c\u2b83葆а팗풬궺쥶ꎠ뗢撻뵪\u21c0羾놂쒞沅," +
                " 헑䈏닁\ue649គ姕\u1069\u2f0d듂狚\ue672団䅁悲枧\u1b56偰摡泈\u1a60㭍\u2af8운\u2026桶뼄ቾᶝ睗㥐厖剹ᥔ㻶\uf3a8춮茞" +
                "\ue531칗ᳯ\u073d飰\ue480\ua6f4\u19b1\u1739箴\ued1a쀁\ua7af탰\u3243\u4df4\u2a33䨺\ud845㼰館㾸侒叵\uf351鳸" +
                "뾁냥ꯈ\u1aba呼\u0b8c䦼鳙柿쥇顆乃\ude93쁳\u0aa9蕕챲閦\ue10d嬵\uecdaꜢஆ挑쑹窀糘," +
                " 甍뮣民皑\u291e秳ʵ솄퍆芦瀉벧\u2e50滎\u25a0爑\u1fce廏놿\u07aa\udde1頔臙ㆲ\uee84㢍䒉藟㗨詂ܒ嘟ᰁ謳䒁\u17cb" +
                "\u0875멖\uecda㙿\ua837稔뇐\uee3aۮ\uf6cd\ue22c\u2fbe톋艸操샣墺貗\u1c47\uf2ff," +
                " 䌚\ufe3d춢후Ꜯ卩鳰阘细\ue9d5\ude3a显鏌㓆갭\uda6cᎳ\ua9f4쉙嘂\uf6a5䜐禎\u0529K쬋壵\ue61e쵕ᶑ\uf04f," +
                " ᬱ뭇昺玉뾂炣虹㨘," +
                " 씅潵겧\u0f24㺸則穣클䜜걓绡缂敉勪\ue498溯7익Ᏺ㥥㖃ど츪퇢㴯ᚅ\u1deb齞杁鱼欎䌕렔횋葑䎌쯹笨\udd06\ude35鲦頒Ϯ懜焣担戎" +
                "몔\ue47a串\u0c00\ue59b聾ﶯ," +
                " \u2153\uf7cdꜰ耕詴茟\u0482쵕ꏠ\ud847\uef98쾭," +
                " 檌裤㻞椼憊ⴋ\u21ba\uec15檮滙\u0cec巶먛㺱\udbf4蠛玌疟ᒚⴓ\u2818\u2b5d\ud85cⲄ뤀\u0361ꚸ璎祍忢," +
                " 좲햽퐗僮眏겴\uf5e2霺ⱊȢ\ue41d\u2f43뜓軷, ...]",
                "{\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒\uff03" +
                "띯\ue5cb\ua7b1聆尩굿\uecf5ꪻ疜\ue8b2빮빅\ue2da䟆\ue78f㱉泦=1, 瀵컦刓嗏\u3353\ue2d3\ud805ឃ=1," +
                " 糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ\udcc6\u0964\u337d萋\u0d5b詵\ud8ca" +
                "䍾徜쪡詪=1," +
                " 駆퉐庺\u2293\ued0d䴻ꎤ槔横=1, 䃼匀낛띆ﱓ㝏ᯖ\uea55\ue9c6儖䦣\u202c=1," +
                " ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხ叙繲\uefc8嶂췴䔾턞=1," +
                " \uab6e䝀㥑\u2e64년믱젯䁅偘滞\ue89bᖒ㿘燔ꎿ趵=1, 㙴ᶙ䁩聂ꃖꐲ\udc58\u26f2=1," +
                " 쳮陜阏顓\u2320쓎狙쟒㕯뚗\u32b0頵\u2606ꎚ궥婜쇻ᅒ댲=1, 旞\u2613\u19de\u0618戯韕똽\u25ad=1}",
                32.008717000021356
        );
        stringsAtLeast_int_fail_helper(5, 5);
        stringsAtLeast_int_fail_helper(4, 5);
    }

    private static double meanOfIntegers(@NotNull List<Integer> xs) {
        return sumDouble(map(i -> (double) i / DEFAULT_SAMPLE_SIZE, xs));
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readInteger).apply(s).get();
    }
}
// @formatter:on
