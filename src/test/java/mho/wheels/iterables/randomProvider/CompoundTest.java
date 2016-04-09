package mho.wheels.iterables.randomProvider;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.structures.Either;
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
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeq(meanOfIntegers(toList(map(x -> Objects.equals(x, element) ? 1 : 0, sample))), elementFrequency);
        P.reset();
    }

    @Test
    public void testWithElement() {
        withElement_helper(2, "[1]", null, "RandomProvider_withElement_i", 0.4992549999935604);
        withElement_helper(8, "[1]", null, "RandomProvider_withElement_ii", 0.12480700000010415);
        withElement_helper(32, "[1]", null, "RandomProvider_withElement_iii", 0.031218000000010567);
        withElement_helper(2, "[null, 2, 3]", 10, "RandomProvider_withElement_iv", 0.4992549999935604);
        withElement_helper(8, "[null, 2, 3]", 10, "RandomProvider_withElement_v", 0.12480700000010415);
        withElement_helper(32, "[null, 2, 3]", 10, "RandomProvider_withElement_vi", 0.031218000000010567);

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
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeq(meanOfIntegers(toList(map(x -> x == null ? 1 : 0, sample))), nullFrequency);
        P.reset();
    }

    @Test
    public void testWithNull() {
        withNull_helper(2, "[1]", "RandomProvider_withNull_i", 0.4992549999935604);
        withNull_helper(8, "[1]", "RandomProvider_withNull_ii", 0.12480700000010415);
        withNull_helper(32, "[1]", "RandomProvider_withNull_iii", 0.031218000000010567);
        withNull_helper(2, "[1, 2, 3]", "RandomProvider_withNull_iv", 0.4992549999935604);
        withNull_helper(8, "[1, 2, 3]", "RandomProvider_withNull_v", 0.12480700000010415);
        withNull_helper(32, "[1, 2, 3]", "RandomProvider_withNull_vi", 0.031218000000010567);

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
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeq(meanOfIntegers(toList(map(x -> x.isPresent() ? 0 : 1, sample))), emptyFrequency);
        P.reset();
    }

    @Test
    public void testOptionals() {
        optionalsHelper(2, "[1]", "RandomProvider_optionals_i", 0.4992549999935604);
        optionalsHelper(8, "[1]", "RandomProvider_optionals_ii", 0.12480700000010415);
        optionalsHelper(32, "[1]", "RandomProvider_optionals_iii", 0.031218000000010567);
        optionalsHelper(2, "[1, 2, 3]", "RandomProvider_optionals_iv", 0.4992549999935604);
        optionalsHelper(8, "[1, 2, 3]", "RandomProvider_optionals_v", 0.12480700000010415);
        optionalsHelper(32, "[1, 2, 3]", "RandomProvider_optionals_vi", 0.031218000000010567);

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
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeq(meanOfIntegers(toList(map(x -> x.isPresent() ? 0 : 1, sample))), emptyFrequency);
        P.reset();
    }

    @Test
    public void testNullableOptionals() {
        nullableOptionals_helper(2, "[1]", "RandomProvider_nullableOptionals_i", 0.4992549999935604);
        nullableOptionals_helper(8, "[1]", "RandomProvider_nullableOptionals_ii", 0.12480700000010415);
        nullableOptionals_helper(32, "[1]", "RandomProvider_nullableOptionals_iii", 0.031218000000010567);
        nullableOptionals_helper(2, "[null, 2, 3]", "RandomProvider_nullableOptionals_iv", 0.4992549999935604);
        nullableOptionals_helper(8, "[null, 2, 3]", "RandomProvider_nullableOptionals_v", 0.12480700000010415);
        nullableOptionals_helper(32, "[null, 2, 3]", "RandomProvider_nullableOptionals_vi", 0.031218000000010567);

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
        aeqitLimitLog(
                TINY_LIMIT,
                P.dependentPairsInfinite(P.range(1, 5), i -> P.strings(i, charsToString(range('a', 'z')))),
                "RandomProvider_dependentPairsInfinite"
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

    private static void permutationsFinite_helper(@NotNull String input, @NotNull String output) {
        permutationsFinite_helper(readIntegerListWithNulls(input), output);
    }

    private static void permutationsFinite_helper(@NotNull List<Integer> input, @NotNull String output) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.permutationsFinite(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    @Test
    public void testPermutationsFinite() {
        permutationsFinite_helper("[]", "RandomProvider_permutationsFinite_i");
        permutationsFinite_helper("[5]", "RandomProvider_permutationsFinite_ii");
        permutationsFinite_helper("[1, 2]", "RandomProvider_permutationsFinite_iii");
        permutationsFinite_helper("[1, 2, 3]", "RandomProvider_permutationsFinite_iv");
        permutationsFinite_helper("[1, 2, 3, 4]", "RandomProvider_permutationsFinite_v");
        permutationsFinite_helper("[1, 2, 2, 4]", "RandomProvider_permutationsFinite_vi");
        permutationsFinite_helper("[2, 2, 2, 2]", "RandomProvider_permutationsFinite_vii");
        permutationsFinite_helper("[3, 1, 4, 1]", "RandomProvider_permutationsFinite_viii");
        permutationsFinite_helper("[3, 1, null, 1]", "RandomProvider_permutationsFinite_ix");
        permutationsFinite_helper(toList(IterableUtils.range(1, 10)), "RandomProvider_permutationsFinite_x");
    }

    private static void stringPermutations_helper(@NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringPermutations(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    @Test
    public void testStringPermutations() {
        stringPermutations_helper("", "RandomProvider_stringPermutations_i");
        stringPermutations_helper("a", "RandomProvider_stringPermutations_ii");
        stringPermutations_helper("abc", "RandomProvider_stringPermutations_iii");
        stringPermutations_helper("foo", "RandomProvider_stringPermutations_iv");
        stringPermutations_helper("hello", "RandomProvider_stringPermutations_v");
        stringPermutations_helper("Mississippi", "RandomProvider_stringPermutations_vi");
    }

    private static void prefixPermutations_helper(int scale, @NotNull String input, @NotNull String output) {
        prefixPermutations_helper(scale, readIntegerListWithNulls(input), output);
    }

    private static void prefixPermutations_helper(int scale, @NotNull List<Integer> input, @NotNull String output) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, map(IterableUtils::toList, P.withScale(scale).prefixPermutations(input)))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private static void prefixPermutations_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, map(Testing::its, P.withScale(scale).prefixPermutations(input)))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        prefixPermutations_helper(1, "[]", "RandomProvider_prefixPermutations_i");
        prefixPermutations_helper(2, "[]", "RandomProvider_prefixPermutations_ii");
        prefixPermutations_helper(4, "[]", "RandomProvider_prefixPermutations_iii");
        prefixPermutations_helper(1, "[5]", "RandomProvider_prefixPermutations_iv");
        prefixPermutations_helper(2, "[5]", "RandomProvider_prefixPermutations_v");
        prefixPermutations_helper(4, "[5]", "RandomProvider_prefixPermutations_vi");
        prefixPermutations_helper(1, "[1, 2]", "RandomProvider_prefixPermutations_vii");
        prefixPermutations_helper(2, "[1, 2]", "RandomProvider_prefixPermutations_viii");
        prefixPermutations_helper(4, "[1, 2]", "RandomProvider_prefixPermutations_ix");
        prefixPermutations_helper(1, "[1, 2, 3]", "RandomProvider_prefixPermutations_x");
        prefixPermutations_helper(2, "[1, 2, 3]", "RandomProvider_prefixPermutations_xi");
        prefixPermutations_helper(4, "[1, 2, 3]", "RandomProvider_prefixPermutations_xii");
        prefixPermutations_helper(1, "[1, 2, 3, 4]", "RandomProvider_prefixPermutations_xiii");
        prefixPermutations_helper(2, "[1, 2, 3, 4]", "RandomProvider_prefixPermutations_xiv");
        prefixPermutations_helper(4, "[1, 2, 3, 4]", "RandomProvider_prefixPermutations_xv");
        prefixPermutations_helper(1, "[1, 2, 2, 4]", "RandomProvider_prefixPermutations_xvi");
        prefixPermutations_helper(2, "[1, 2, 2, 4]", "RandomProvider_prefixPermutations_xvii");
        prefixPermutations_helper(4, "[1, 2, 2, 4]", "RandomProvider_prefixPermutations_xviii");
        prefixPermutations_helper(1, "[2, 2, 2, 2]", "RandomProvider_prefixPermutations_xix");
        prefixPermutations_helper(2, "[2, 2, 2, 2]", "RandomProvider_prefixPermutations_xx");
        prefixPermutations_helper(4, "[2, 2, 2, 2]", "RandomProvider_prefixPermutations_xxi");
        prefixPermutations_helper(1, "[3, 1, 4, 1]", "RandomProvider_prefixPermutations_xxii");
        prefixPermutations_helper(2, "[3, 1, 4, 1]", "RandomProvider_prefixPermutations_xxiii");
        prefixPermutations_helper(4, "[3, 1, 4, 1]", "RandomProvider_prefixPermutations_xxiv");
        prefixPermutations_helper(1, "[3, 1, null, 1]", "RandomProvider_prefixPermutations_xxv");
        prefixPermutations_helper(2, "[3, 1, null, 1]", "RandomProvider_prefixPermutations_xxvi");
        prefixPermutations_helper(4, "[3, 1, null, 1]", "RandomProvider_prefixPermutations_xxvii");
        prefixPermutations_helper(1, "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", "RandomProvider_prefixPermutations_xxviii");
        prefixPermutations_helper(2, "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", "RandomProvider_prefixPermutations_xxix");
        prefixPermutations_helper(4, "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", "RandomProvider_prefixPermutations_xxx");
        prefixPermutations_helper(
                1,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "RandomProvider_prefixPermutations_xxxi"
        );
        prefixPermutations_helper(
                2,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "RandomProvider_prefixPermutations_xxxii"
        );
        prefixPermutations_helper(
                4,
                ExhaustiveProvider.INSTANCE.positiveIntegers(),
                "RandomProvider_prefixPermutations_xxxiii"
        );
        prefixPermutations_helper(1, repeat(1), "RandomProvider_prefixPermutations_xxxiv");
        prefixPermutations_helper(2, repeat(1), "RandomProvider_prefixPermutations_xxxv");
        prefixPermutations_helper(4, repeat(1), "RandomProvider_prefixPermutations_xxxvi");
        prefixPermutations_fail_helper(0, "[1, 2, 3]");
        prefixPermutations_fail_helper(-1, "[1, 2, 3]");
    }

    private static void strings_int_Iterable_helper(int size, @NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.strings(size, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        strings_int_Iterable_helper(0, "", "RandomProvider_strings_int_String_i");
        strings_int_Iterable_helper(0, "a", "RandomProvider_strings_int_String_ii");
        strings_int_Iterable_helper(1, "a", "RandomProvider_strings_int_String_iii");
        strings_int_Iterable_helper(2, "a", "RandomProvider_strings_int_String_iv");
        strings_int_Iterable_helper(3, "a", "RandomProvider_strings_int_String_v");
        strings_int_Iterable_helper(0, "abc", "RandomProvider_strings_int_String_vi");
        strings_int_Iterable_helper(1, "abc", "RandomProvider_strings_int_String_vii");
        strings_int_Iterable_helper(2, "abc", "RandomProvider_strings_int_String_viii");
        strings_int_Iterable_helper(3, "abc", "RandomProvider_strings_int_String_ix");
        strings_int_Iterable_helper(0, "abbc", "RandomProvider_strings_int_String_x");
        strings_int_Iterable_helper(1, "abbc", "RandomProvider_strings_int_String_xi");
        strings_int_Iterable_helper(2, "abbc", "RandomProvider_strings_int_String_xii");
        strings_int_Iterable_helper(3, "abbc", "RandomProvider_strings_int_String_xiii");
        strings_int_Iterable_helper(0, "Mississippi", "RandomProvider_strings_int_String_xiv");
        strings_int_Iterable_helper(1, "Mississippi", "RandomProvider_strings_int_String_xv");
        strings_int_Iterable_helper(2, "Mississippi", "RandomProvider_strings_int_String_xvi");
        strings_int_Iterable_helper(3, "Mississippi", "RandomProvider_strings_int_String_xvii");

        strings_int_Iterable_fail_helper(1, "");
        strings_int_Iterable_fail_helper(-1, "abc");
    }

    private static void strings_int_helper(int size, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.strings(size)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        strings_int_helper(0, "RandomProvider_strings_int_i");
        strings_int_helper(1, "RandomProvider_strings_int_ii");
        strings_int_helper(2, "RandomProvider_strings_int_iii");
        strings_int_helper(3, "RandomProvider_strings_int_iv");

        strings_int_fail_helper(-1);
    }

    private static void lists_Iterable_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).lists(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void lists_Iterable_helper_uniform(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        lists_Iterable_helper(scale, P.uniformSample(readIntegerListWithNulls(input)), output, meanSize);
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
        lists_Iterable_helper_uniform(1, "[5]", "RandomProvider_lists_Iterable_i", 1.0008359999977228);
        lists_Iterable_helper_uniform(2, "[5]", "RandomProvider_lists_Iterable_ii", 2.0020969999891216);
        lists_Iterable_helper_uniform(4, "[5]", "RandomProvider_lists_Iterable_iii", 4.004359999991779);
        lists_Iterable_helper_uniform(1, "[1, 2, 3]", "RandomProvider_lists_Iterable_iv", 1.00085799999768);
        lists_Iterable_helper_uniform(2, "[1, 2, 3]", "RandomProvider_lists_Iterable_v", 2.0023509999891522);
        lists_Iterable_helper_uniform(4, "[1, 2, 3]", "RandomProvider_lists_Iterable_vi", 4.00516399999172);
        lists_Iterable_helper_uniform(1, "[1, null, 3]", "RandomProvider_lists_Iterable_vii", 1.00085799999768);
        lists_Iterable_helper_uniform(2, "[1, null, 3]", "RandomProvider_lists_Iterable_viii", 2.0023509999891522);
        lists_Iterable_helper_uniform(4, "[1, null, 3]", "RandomProvider_lists_Iterable_ix", 4.00516399999172);
        lists_Iterable_helper_uniform(1, "[1, 2, 3, 4]", "RandomProvider_lists_Iterable_x", 1.0006389999976706);
        lists_Iterable_helper_uniform(2, "[1, 2, 3, 4]", "RandomProvider_lists_Iterable_xi", 2.0037019999891394);
        lists_Iterable_helper_uniform(4, "[1, 2, 3, 4]", "RandomProvider_lists_Iterable_xii", 4.00571499999147);
        lists_Iterable_helper_uniform(1, "[1, 2, 2, 4]", "RandomProvider_lists_Iterable_xiii", 1.0006389999976706);
        lists_Iterable_helper_uniform(2, "[1, 2, 2, 4]", "RandomProvider_lists_Iterable_xiv", 2.0037019999891394);
        lists_Iterable_helper_uniform(4, "[1, 2, 2, 4]", "RandomProvider_lists_Iterable_xv", 4.00571499999147);
        lists_Iterable_helper_uniform(1, "[2, 2, 2, 2]", "RandomProvider_lists_Iterable_xvi", 1.0006389999976706);
        lists_Iterable_helper_uniform(2, "[2, 2, 2, 2]", "RandomProvider_lists_Iterable_xvii", 2.0037019999891394);
        lists_Iterable_helper_uniform(4, "[2, 2, 2, 2]", "RandomProvider_lists_Iterable_xviii", 4.00571499999147);
        lists_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_lists_Iterable_xix",
                0.998919999997707
        );
        lists_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_lists_Iterable_xx",
                2.003595999989077
        );
        lists_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_lists_Iterable_xxi",
                4.002965999991581
        );
        lists_Iterable_helper(
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_lists_Iterable_xxii",
                1.0012699999976906
        );
        lists_Iterable_helper(
                2,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_lists_Iterable_xxiii",
                2.001994999989098
        );
        lists_Iterable_helper(
                4,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_lists_Iterable_xxiv",
                4.0083209999916205
        );
        lists_Iterable_helper(1, repeat(1), "RandomProvider_lists_Iterable_xxv", 1.0008359999977228);
        lists_Iterable_helper(2, repeat(1), "RandomProvider_lists_Iterable_xxvi", 2.0020969999891216);
        lists_Iterable_helper(4, repeat(1), "RandomProvider_lists_Iterable_xxvii", 4.004359999991779);
        lists_Iterable_fail_helper(1, Collections.emptyList());
        lists_Iterable_fail_helper(1, Arrays.asList(1, 2, 3));
        lists_Iterable_fail_helper(0, P.integers());
        lists_Iterable_fail_helper(-1, P.integers());
    }

    private static void strings_String_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).strings(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        strings_String_helper(1, "a", "RandomProvider_strings_String_i", 1.0008359999977228);
        strings_String_helper(2, "a", "RandomProvider_strings_String_ii", 2.0020969999891216);
        strings_String_helper(4, "a", "RandomProvider_strings_String_iii", 4.004359999991779);
        strings_String_helper(1, "abc", "RandomProvider_strings_String_iv", 1.00085799999768);
        strings_String_helper(2, "abc", "RandomProvider_strings_String_v", 2.0023509999891522);
        strings_String_helper(4, "abc", "RandomProvider_strings_String_vi", 4.00516399999172);
        strings_String_helper(1, "abbc", "RandomProvider_strings_String_vii", 1.0006389999976706);
        strings_String_helper(2, "abbc", "RandomProvider_strings_String_viii", 2.0037019999891394);
        strings_String_helper(4, "abbc", "RandomProvider_strings_String_ix", 4.00571499999147);
        strings_String_helper(1, "Mississippi", "RandomProvider_strings_String_x", 0.9996679999977037);
        strings_String_helper(2, "Mississippi", "RandomProvider_strings_String_xi", 2.0026269999890762);
        strings_String_helper(4, "Mississippi", "RandomProvider_strings_String_xii", 4.0051349999917525);
        strings_String_fail_helper(1, "");
        strings_String_fail_helper(0, "abc");
        strings_String_fail_helper(-1, "abc");
    }

    private static void strings_helper(int scale, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).strings()));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        strings_helper(1, "RandomProvider_strings_i", 1.0006389999976706);
        strings_helper(2, "RandomProvider_strings_ii", 2.0037019999891394);
        strings_helper(4, "RandomProvider_strings_iii", 4.00571499999147);
        strings_fail_helper(0);
        strings_fail_helper(-1);
    }

    private static void listsAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).listsAtLeast(minSize, input))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void listsAtLeast_helper_uniform(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        listsAtLeast_helper(
                scale,
                minSize,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
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
        listsAtLeast_helper_uniform(2, 1, "[5]", "RandomProvider_listsAtLeast_i", 2.0008359999800347);
        listsAtLeast_helper_uniform(5, 3, "[5]", "RandomProvider_listsAtLeast_ii", 5.002096999996331);
        listsAtLeast_helper_uniform(32, 8, "[5]", "RandomProvider_listsAtLeast_iii", 32.00360900002322);
        listsAtLeast_helper_uniform(2, 1, "[1, 2, 3]", "RandomProvider_listsAtLeast_iv", 1.9993039999798474);
        listsAtLeast_helper_uniform(5, 3, "[1, 2, 3]", "RandomProvider_listsAtLeast_v", 5.003739999996368);
        listsAtLeast_helper_uniform(32, 8, "[1, 2, 3]", "RandomProvider_listsAtLeast_vi", 32.010685000021894);
        listsAtLeast_helper_uniform(2, 1, "[1, null, 3]", "RandomProvider_listsAtLeast_vii", 1.9993039999798474);
        listsAtLeast_helper_uniform(5, 3, "[1, null, 3]", "RandomProvider_listsAtLeast_viii", 5.003739999996368);
        listsAtLeast_helper_uniform(32, 8, "[1, null, 3]", "RandomProvider_listsAtLeast_ix", 32.010685000021894);
        listsAtLeast_helper_uniform(2, 1, "[1, 2, 3, 4]", "RandomProvider_listsAtLeast_x", 1.999585999979838);
        listsAtLeast_helper_uniform(5, 3, "[1, 2, 3, 4]", "RandomProvider_listsAtLeast_xi", 5.00315899999616);
        listsAtLeast_helper_uniform(32, 8, "[1, 2, 3, 4]", "RandomProvider_listsAtLeast_xii", 32.008717000021356);
        listsAtLeast_helper_uniform(2, 1, "[1, 2, 2, 4]", "RandomProvider_listsAtLeast_xiii", 1.999585999979838);
        listsAtLeast_helper_uniform(5, 3, "[1, 2, 2, 4]", "RandomProvider_listsAtLeast_xiv", 5.00315899999616);
        listsAtLeast_helper_uniform(32, 8, "[1, 2, 2, 4]", "RandomProvider_listsAtLeast_xv", 32.008717000021356);
        listsAtLeast_helper_uniform(2, 1, "[2, 2, 2, 2]", "RandomProvider_listsAtLeast_xvi", 1.999585999979838);
        listsAtLeast_helper_uniform(5, 3, "[2, 2, 2, 2]", "RandomProvider_listsAtLeast_xvii", 5.00315899999616);
        listsAtLeast_helper_uniform(32, 8, "[2, 2, 2, 2]", "RandomProvider_listsAtLeast_xviii", 32.008717000021356);
        listsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_listsAtLeast_xix",
                1.9987289999797695
        );
        listsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_listsAtLeast_xx",
                5.002305999996172
        );
        listsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_listsAtLeast_xxi",
                31.997066000022638
        );
        listsAtLeast_helper(
                2,
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsAtLeast_xxiii",
                2.001126999979881
        );
        listsAtLeast_helper(
                5,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsAtLeast_xxiv",
                5.001189999995907
        );
        listsAtLeast_helper(
                32,
                8,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsAtLeast_xxv",
                32.00730000002313
        );
        listsAtLeast_helper(2, 1, repeat(1), "RandomProvider_listsAtLeast_xxvi", 2.0008359999800347);
        listsAtLeast_helper(5, 3, repeat(1), "RandomProvider_listsAtLeast_xxvii", 5.002096999996331);
        listsAtLeast_helper(32, 8, repeat(1), "RandomProvider_listsAtLeast_xxviii", 32.00360900002322);

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
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsAtLeast(minSize, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        stringsAtLeast_int_String_helper(2, 1, "a", "RandomProvider_stringsAtLeast_int_String_i", 2.0008359999800347);
        stringsAtLeast_int_String_helper(5, 3, "a", "RandomProvider_stringsAtLeast_int_String_ii", 5.002096999996331);
        stringsAtLeast_int_String_helper(
                32,
                8,
                "a",
                "RandomProvider_stringsAtLeast_int_String_iii",
                32.00360900002322
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "abc",
                "RandomProvider_stringsAtLeast_int_String_iv",
                1.9993039999798474
        );
        stringsAtLeast_int_String_helper(5, 3, "abc", "RandomProvider_stringsAtLeast_int_String_v", 5.003739999996368);
        stringsAtLeast_int_String_helper(
                32,
                8,
                "abc",
                "RandomProvider_stringsAtLeast_int_String_vi",
                32.010685000021894
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "abbc",
                "RandomProvider_stringsAtLeast_int_String_vii",
                1.999585999979838
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "abbc",
                "RandomProvider_stringsAtLeast_int_String_viii",
                5.00315899999616
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "abbc",
                "RandomProvider_stringsAtLeast_int_String_ix",
                32.008717000021356
        );
        stringsAtLeast_int_String_helper(
                2,
                1,
                "Mississippi",
                "RandomProvider_stringsAtLeast_int_String_x",
                1.9990949999798069
        );
        stringsAtLeast_int_String_helper(
                5,
                3,
                "Mississippi",
                "RandomProvider_stringsAtLeast_int_String_xi",
                5.003636999996235
        );
        stringsAtLeast_int_String_helper(
                32,
                8,
                "Mississippi",
                "RandomProvider_stringsAtLeast_int_String_xii",
                32.00263800002314
        );

        stringsAtLeast_int_String_fail_helper(5, 3, "");
        stringsAtLeast_int_String_fail_helper(5, 5, "abc");
        stringsAtLeast_int_String_fail_helper(4, 5, "abc");
    }

    private static void stringsAtLeast_int_helper(int scale, int minSize, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsAtLeast(minSize)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        stringsAtLeast_int_helper(2, 1, "RandomProvider_stringsAtLeast_int_i", 1.999585999979838);
        stringsAtLeast_int_helper(5, 3, "RandomProvider_stringsAtLeast_int_ii", 5.00315899999616);
        stringsAtLeast_int_helper(32, 8, "RandomProvider_stringsAtLeast_int_iii", 32.008717000021356);
        stringsAtLeast_int_fail_helper(5, 5);
        stringsAtLeast_int_fail_helper(4, 5);
    }

    private static void distinctStrings_int_String_helper(int size, @NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.distinctStrings(size, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void distinctStrings_int_String_fail_helper(int size, @NotNull String input) {
        try {
            P.distinctStrings(size, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testDistinctStrings_int_String() {
        distinctStrings_int_String_helper(0, "a", "RandomProvider_distinctStrings_int_String_i");
        distinctStrings_int_String_helper(1, "a", "RandomProvider_distinctStrings_int_String_ii");
        distinctStrings_int_String_helper(0, "abc", "RandomProvider_distinctStrings_int_String_iii");
        distinctStrings_int_String_helper(1, "abc", "RandomProvider_distinctStrings_int_String_iv");
        distinctStrings_int_String_helper(2, "abc", "RandomProvider_distinctStrings_int_String_v");
        distinctStrings_int_String_helper(3, "abc", "RandomProvider_distinctStrings_int_String_vi");
        distinctStrings_int_String_helper(0, "abbc", "RandomProvider_distinctStrings_int_String_vii");
        distinctStrings_int_String_helper(1, "abbc", "RandomProvider_distinctStrings_int_String_viii");
        distinctStrings_int_String_helper(2, "abbc", "RandomProvider_distinctStrings_int_String_ix");
        distinctStrings_int_String_helper(3, "abbc", "RandomProvider_distinctStrings_int_String_x");
        distinctStrings_int_String_helper(0, "Mississippi", "RandomProvider_distinctStrings_int_String_xi");
        distinctStrings_int_String_helper(1, "Mississippi", "RandomProvider_distinctStrings_int_String_xii");
        distinctStrings_int_String_helper(2, "Mississippi", "RandomProvider_distinctStrings_int_String_xiii");
        distinctStrings_int_String_helper(3, "Mississippi", "RandomProvider_distinctStrings_int_String_xiv");

        distinctStrings_int_String_fail_helper(1, "");
        distinctStrings_int_String_fail_helper(-1, "abc");
    }

    private static void distinctStrings_int_helper(int size, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.distinctStrings(size)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void distinctStrings_int_fail_helper(int size) {
        try {
            P.distinctStrings(size);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testDistinctStrings_int() {
        distinctStrings_int_helper(0, "RandomProvider_distinctStrings_int_i");
        distinctStrings_int_helper(1, "RandomProvider_distinctStrings_int_ii");
        distinctStrings_int_helper(2, "RandomProvider_distinctStrings_int_iii");
        distinctStrings_int_helper(3, "RandomProvider_distinctStrings_int_iv");

        distinctStrings_int_fail_helper(-1);
    }

    private static void distinctLists_Iterable_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctLists(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void distinctLists_Iterable_helper_uniform(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        distinctLists_Iterable_helper(
                scale,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                meanSize
        );
    }

    private static void distinctLists_Iterable_fail_helper(int scale, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).distinctLists(input));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctLists_Iterable() {
        distinctLists_Iterable_helper_uniform(1, "[5]", "RandomProvider_distinctLists_Iterable_i", 0.5008749999935656);
        distinctLists_Iterable_helper_uniform(
                2,
                "[5]",
                "RandomProvider_distinctLists_Iterable_ii",
                0.6661869999983192
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[5]",
                "RandomProvider_distinctLists_Iterable_iii",
                0.7998060000021615
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[1, 2, 3]",
                "RandomProvider_distinctLists_Iterable_iv",
                0.7507059999970308
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[1, 2, 3]",
                "RandomProvider_distinctLists_Iterable_v",
                1.2008789999923022
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[1, 2, 3]",
                "RandomProvider_distinctLists_Iterable_vi",
                1.7145229999887661
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[1, null, 3]",
                "RandomProvider_distinctLists_Iterable_vii",
                0.7507059999970308
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[1, null, 3]",
                "RandomProvider_distinctLists_Iterable_viii",
                1.2008789999923022
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[1, null, 3]",
                "RandomProvider_distinctLists_Iterable_ix",
                1.7145229999887661
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4]",
                "RandomProvider_distinctLists_Iterable_x",
                0.8006769999971934
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4]",
                "RandomProvider_distinctLists_Iterable_xi",
                1.334835999990812
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4]",
                "RandomProvider_distinctLists_Iterable_xii",
                2.001787999981212
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[1, 2, 2, 4]",
                "RandomProvider_distinctLists_Iterable_xiii",
                0.7339709999971153
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[1, 2, 2, 4]",
                "RandomProvider_distinctLists_Iterable_xiv",
                1.1676389999927037
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[1, 2, 2, 4]",
                "RandomProvider_distinctLists_Iterable_xv",
                1.667697999989275
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[2, 2, 2, 2]",
                "RandomProvider_distinctLists_Iterable_xvi",
                0.5004429999935531
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[2, 2, 2, 2]",
                "RandomProvider_distinctLists_Iterable_xvii",
                0.6669589999983414
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[2, 2, 2, 2]",
                "RandomProvider_distinctLists_Iterable_xviii",
                0.7999900000021668
        );
        distinctLists_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctLists_Iterable_xix",
                0.9078379999975383
        );
        distinctLists_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctLists_Iterable_xx",
                1.6697689999898184
        );
        distinctLists_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctLists_Iterable_xxi",
                2.8588639999882393
        );
        distinctLists_Iterable_helper(
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctLists_Iterable_xxii",
                0.8811449999975006
        );
        distinctLists_Iterable_helper(
                2,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctLists_Iterable_xxiii",
                1.583489999990105
        );
        distinctLists_Iterable_helper(
                4,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctLists_Iterable_xxiv",
                2.668782999988186
        );
        distinctLists_Iterable_helper(1, repeat(1), "RandomProvider_distinctLists_Iterable_xxv", 0.5008749999935656);
        distinctLists_Iterable_helper(2, repeat(1), "RandomProvider_distinctLists_Iterable_xxvi", 0.6661869999983192);
        distinctLists_Iterable_helper(4, repeat(1), "RandomProvider_distinctLists_Iterable_xxvii", 0.7998060000021615);

        distinctLists_Iterable_fail_helper(1, Collections.emptyList());
        distinctLists_Iterable_fail_helper(1, Arrays.asList(1, 2, 3));
        distinctLists_Iterable_fail_helper(0, P.integers());
        distinctLists_Iterable_fail_helper(-1, P.integers());
    }

    private static void distinctStrings_String_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctStrings(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void distinctStrings_String_fail_helper(int scale, @NotNull String input) {
        try {
            toList(P.withScale(scale).distinctStrings(input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctStrings_String() {
        distinctStrings_String_helper(1, "a", "RandomProvider_distinctStrings_String_i", 0.5008749999935656);
        distinctStrings_String_helper(2, "a", "RandomProvider_distinctStrings_String_ii", 0.6661869999983192);
        distinctStrings_String_helper(4, "a", "RandomProvider_distinctStrings_String_iii", 0.7998060000021615);
        distinctStrings_String_helper(1, "abc", "RandomProvider_distinctStrings_String_iv", 0.7507059999970308);
        distinctStrings_String_helper(2, "abc", "RandomProvider_distinctStrings_String_v", 1.2008789999923022);
        distinctStrings_String_helper(4, "abc", "RandomProvider_distinctStrings_String_vi", 1.7145229999887661);
        distinctStrings_String_helper(1, "abbc", "RandomProvider_distinctStrings_String_vii", 0.7339709999971153);
        distinctStrings_String_helper(2, "abbc", "RandomProvider_distinctStrings_String_viii", 1.1676389999927037);
        distinctStrings_String_helper(4, "abbc", "RandomProvider_distinctStrings_String_ix", 1.667697999989275);
        distinctStrings_String_helper(1, "Mississippi", "RandomProvider_distinctStrings_String_x", 0.7700039999971866);
        distinctStrings_String_helper(
                2,
                "Mississippi",
                "RandomProvider_distinctStrings_String_xi",
                1.2632049999918284
        );
        distinctStrings_String_helper(
                4,
                "Mississippi",
                "RandomProvider_distinctStrings_String_xii",
                1.8740139999846195
        );

        distinctStrings_String_fail_helper(1, "");
        distinctStrings_String_fail_helper(0, "abc");
        distinctStrings_String_fail_helper(-1, "abc");
    }

    private static void distinctStrings_helper(int scale, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctStrings()));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void distinctStrings_fail_helper(int scale) {
        try {
            toList(P.withScale(scale).distinctStrings());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctStrings() {
        distinctStrings_helper(1, "RandomProvider_distinctStrings_i", 1.0006239999976707);
        distinctStrings_helper(2, "RandomProvider_distinctStrings_ii", 2.0036399999891383);
        distinctStrings_helper(4, "RandomProvider_distinctStrings_iii", 4.005472999991468);

        distinctStrings_fail_helper(0);
        distinctStrings_fail_helper(-1);
    }

    private static void distinctListsAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctListsAtLeast(minSize, input))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void distinctListsAtLeast_helper_uniform(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        distinctListsAtLeast_helper(
                scale,
                minSize,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                meanSize
        );
    }

    private static void distinctListsAtLeast_fail_helper(int scale, int minSize, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).distinctListsAtLeast(minSize, input));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctListsAtLeast() {
        distinctListsAtLeast_helper_uniform(2, 1, "[5]", "RandomProvider_distinctListsAtLeast_i", 1.000000000007918);
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3]",
                "RandomProvider_distinctListsAtLeast_ii",
                1.50008299998526
        );
        distinctListsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3]",
                "RandomProvider_distinctListsAtLeast_iii",
                2.9999999999775233
        );
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[1, null, 3]",
                "RandomProvider_distinctListsAtLeast_iv",
                1.50008299998526
        );
        distinctListsAtLeast_helper_uniform(
                5,
                3,
                "[1, null, 3]",
                "RandomProvider_distinctListsAtLeast_v",
                2.9999999999775233
        );
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4]",
                "RandomProvider_distinctListsAtLeast_vi",
                1.5996069999831977
        );
        distinctListsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4]",
                "RandomProvider_distinctListsAtLeast_vii",
                3.3338519999899345
        );
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 2, 4]",
                "RandomProvider_distinctListsAtLeast_viii",
                1.466173999985577
        );
        distinctListsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 2, 4]",
                "RandomProvider_distinctListsAtLeast_ix",
                2.9999999999775233
        );
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[2, 2, 2, 2]",
                "RandomProvider_distinctListsAtLeast_x",
                1.000000000007918
        );
        distinctListsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctListsAtLeast_xi",
                1.8170889999810345
        );
        distinctListsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctListsAtLeast_xii",
                4.168420999985633
        );
        distinctListsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_distinctListsAtLeast_xiii",
                9.41189799992237
        );
        distinctListsAtLeast_helper(
                2,
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctListsAtLeast_xiv",
                1.7601419999815262
        );
        distinctListsAtLeast_helper(
                5,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctListsAtLeast_xv",
                3.989958999983499
        );
        distinctListsAtLeast_helper(
                32,
                8,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_distinctListsAtLeast_xvi",
                10.378239999978224
        );
        distinctListsAtLeast_helper(2, 1, repeat(1), "RandomProvider_distinctListsAtLeast_xvii", 1.000000000007918);

        distinctListsAtLeast_fail_helper(5, 3, Collections.emptyList());
        distinctListsAtLeast_fail_helper(5, 3, Arrays.asList(1, 2, 3));
        distinctListsAtLeast_fail_helper(5, 5, P.integers());
        distinctListsAtLeast_fail_helper(4, 5, P.integers());
    }

    private static void distinctStringsAtLeast_int_String_helper(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctStringsAtLeast(minSize, input))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void distinctStringsAtLeast_int_String_fail_helper(int scale, int minSize, @NotNull String input) {
        try {
            toList(P.withScale(scale).distinctStringsAtLeast(minSize, input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctStringsAtLeast_int_String() {
        distinctStringsAtLeast_int_String_helper(
                2,
                1,
                "a",
                "RandomProvider_distinctStringsAtLeast_int_String_i",
                1.000000000007918
        );
        distinctStringsAtLeast_int_String_helper(
                2,
                1,
                "abc",
                "RandomProvider_distinctStringsAtLeast_int_String_ii",
                1.50008299998526
        );
        distinctStringsAtLeast_int_String_helper(
                5,
                3,
                "abc",
                "RandomProvider_distinctStringsAtLeast_int_String_iii",
                2.9999999999775233
        );
        distinctStringsAtLeast_int_String_helper(
                2,
                1,
                "abbc",
                "RandomProvider_distinctStringsAtLeast_int_String_iv",
                1.466173999985577
        );
        distinctStringsAtLeast_int_String_helper(
                5,
                3,
                "abbc",
                "RandomProvider_distinctStringsAtLeast_int_String_v",
                2.9999999999775233
        );
        distinctStringsAtLeast_int_String_helper(
                2,
                1,
                "Mississippi",
                "RandomProvider_distinctStringsAtLeast_int_String_vi",
                1.5401079999842737
        );
        distinctStringsAtLeast_int_String_helper(
                5,
                3,
                "Mississippi",
                "RandomProvider_distinctStringsAtLeast_int_String_vii",
                3.2285219999851744
        );

        distinctStringsAtLeast_int_String_fail_helper(5, 3, "");
        distinctStringsAtLeast_int_String_fail_helper(5, 5, "abc");
        distinctStringsAtLeast_int_String_fail_helper(4, 5, "abc");
    }

    private static void distinctStringsAtLeast_int_helper(
            int scale,
            int minSize,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).distinctStringsAtLeast(minSize)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void distinctStringsAtLeast_int_fail_helper(int scale, int minSize) {
        try {
            toList(P.withScale(scale).distinctStringsAtLeast(minSize));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testDistinctStringsAtLeast_int() {
        distinctStringsAtLeast_int_helper(2, 1, "RandomProvider_distinctStringsAtLeast_int_i", 1.9995569999798375);
        distinctStringsAtLeast_int_helper(5, 3, "RandomProvider_distinctStringsAtLeast_int_ii", 5.00299199999616);
        distinctStringsAtLeast_int_helper(32, 8, "RandomProvider_distinctStringsAtLeast_int_iii", 31.99690200002153);

        distinctStringsAtLeast_int_fail_helper(5, 5);
        distinctStringsAtLeast_int_fail_helper(4, 5);
    }

    private static void stringBags_int_String_helper(int size, @NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringBags(size, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void stringBags_int_String_fail_helper(int size, @NotNull String input) {
        try {
            P.stringBags(size, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStringBags_int_String() {
        stringBags_int_String_helper(0, "a", "RandomProvider_stringBags_int_String_i");
        stringBags_int_String_helper(1, "a", "RandomProvider_stringBags_int_String_ii");
        stringBags_int_String_helper(2, "a", "RandomProvider_stringBags_int_String_iii");
        stringBags_int_String_helper(3, "a", "RandomProvider_stringBags_int_String_iv");
        stringBags_int_String_helper(0, "abc", "RandomProvider_stringBags_int_String_v");
        stringBags_int_String_helper(1, "abc", "RandomProvider_stringBags_int_String_vi");
        stringBags_int_String_helper(2, "abc", "RandomProvider_stringBags_int_String_vii");
        stringBags_int_String_helper(3, "abc", "RandomProvider_stringBags_int_String_viii");
        stringBags_int_String_helper(0, "abbc", "RandomProvider_stringBags_int_String_ix");
        stringBags_int_String_helper(1, "abbc", "RandomProvider_stringBags_int_String_x");
        stringBags_int_String_helper(2, "abbc", "RandomProvider_stringBags_int_String_xi");
        stringBags_int_String_helper(3, "abbc", "RandomProvider_stringBags_int_String_xii");
        stringBags_int_String_helper(0, "Mississippi", "RandomProvider_stringBags_int_String_xiii");
        stringBags_int_String_helper(1, "Mississippi", "RandomProvider_stringBags_int_String_xiv");
        stringBags_int_String_helper(2, "Mississippi", "RandomProvider_stringBags_int_String_xv");
        stringBags_int_String_helper(3, "Mississippi", "RandomProvider_stringBags_int_String_xvi");

        stringBags_int_String_fail_helper(1, "");
        stringBags_int_String_fail_helper(-1, "abc");
    }

    private static void stringBags_int_helper(int size, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringBags(size)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void stringBags_int_fail_helper(int size) {
        try {
            P.stringBags(size);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStringBags_int() {
        stringBags_int_helper(0, "RandomProvider_stringBags_int_i");
        stringBags_int_helper(1, "RandomProvider_stringBags_int_ii");
        stringBags_int_helper(2, "RandomProvider_stringBags_int_iii");
        stringBags_int_helper(3, "RandomProvider_stringBags_int_iv");

        stringBags_int_fail_helper(-1);
    }

    private static void bags_Iterable_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).bags(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void bags_Iterable_helper_uniform(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        bags_Iterable_helper(scale, P.uniformSample(readIntegerListWithNulls(input)), output, meanSize);
    }

    private static void bags_Iterable_fail_helper(int scale, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).bags(input));
            fail();
        } catch (NoSuchElementException | IllegalStateException | NullPointerException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testBags_Iterable() {
        bags_Iterable_helper_uniform(1, "[5]", "RandomProvider_bags_Iterable_i", 1.0008359999977228);
        bags_Iterable_helper_uniform(2, "[5]", "RandomProvider_bags_Iterable_ii", 2.0020969999891216);
        bags_Iterable_helper_uniform(4, "[5]", "RandomProvider_bags_Iterable_iii", 4.004359999991779);
        bags_Iterable_helper_uniform(1, "[1, 2, 3]", "RandomProvider_bags_Iterable_iv", 1.00085799999768);
        bags_Iterable_helper_uniform(2, "[1, 2, 3]", "RandomProvider_bags_Iterable_v", 2.0023509999891522);
        bags_Iterable_helper_uniform(4, "[1, 2, 3]", "RandomProvider_bags_Iterable_vi", 4.00516399999172);
        bags_Iterable_helper_uniform(1, "[1, 2, 3, 4]", "RandomProvider_bags_Iterable_vii", 1.0006389999976706);
        bags_Iterable_helper_uniform(2, "[1, 2, 3, 4]", "RandomProvider_bags_Iterable_viii", 2.0037019999891394);
        bags_Iterable_helper_uniform(4, "[1, 2, 3, 4]", "RandomProvider_bags_Iterable_ix", 4.00571499999147);
        bags_Iterable_helper_uniform(1, "[1, 2, 2, 4]", "RandomProvider_bags_Iterable_x", 1.0006389999976706);
        bags_Iterable_helper_uniform(2, "[1, 2, 2, 4]", "RandomProvider_bags_Iterable_xi", 2.0037019999891394);
        bags_Iterable_helper_uniform(4, "[1, 2, 2, 4]", "RandomProvider_bags_Iterable_xii", 4.00571499999147);
        bags_Iterable_helper_uniform(1, "[2, 2, 2, 2]", "RandomProvider_bags_Iterable_xiii", 1.0006389999976706);
        bags_Iterable_helper_uniform(2, "[2, 2, 2, 2]", "RandomProvider_bags_Iterable_xiv", 2.0037019999891394);
        bags_Iterable_helper_uniform(4, "[2, 2, 2, 2]", "RandomProvider_bags_Iterable_xv", 4.00571499999147);
        bags_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bags_Iterable_xvi",
                0.998919999997707
        );
        bags_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bags_Iterable_xvii",
                2.003595999989077
        );
        bags_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bags_Iterable_xviii",
                4.002965999991581
        );
        bags_Iterable_helper(
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bags_Iterable_xix",
                1.0012699999976906
        );
        bags_Iterable_helper(
                2,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bags_Iterable_xx",
                2.001994999989098
        );
        bags_Iterable_helper(
                4,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bags_Iterable_xxi",
                4.0083209999916205
        );
        bags_Iterable_helper(1, repeat(1), "RandomProvider_bags_Iterable_xxii", 1.0008359999977228);
        bags_Iterable_helper(2, repeat(1), "RandomProvider_bags_Iterable_xxiii", 2.0020969999891216);
        bags_Iterable_helper(4, repeat(1), "RandomProvider_bags_Iterable_xxiv", 4.004359999991779);

        bags_Iterable_fail_helper(1, Collections.emptyList());
        bags_Iterable_fail_helper(1, Arrays.asList(1, 2, 3));
        bags_Iterable_fail_helper(0, P.integers());
        bags_Iterable_fail_helper(-1, P.integers());
        bags_Iterable_fail_helper(1, Arrays.asList(1, null, 3));
        bags_Iterable_fail_helper(1, Collections.singleton(null));
    }

    private static void stringBags_String_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringBags(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringBags_String_fail_helper(int scale, @NotNull String input) {
        try {
            toList(P.withScale(scale).stringBags(input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringBags_String() {
        stringBags_String_helper(1, "a", "RandomProvider_stringBags_String_i", 1.0008359999977228);
        stringBags_String_helper(2, "a", "RandomProvider_stringBags_String_ii", 2.0020969999891216);
        stringBags_String_helper(4, "a", "RandomProvider_stringBags_String_iii", 4.004359999991779);
        stringBags_String_helper(1, "abc", "RandomProvider_stringBags_String_iv", 1.00085799999768);
        stringBags_String_helper(2, "abc", "RandomProvider_stringBags_String_v", 2.0023509999891522);
        stringBags_String_helper(4, "abc", "RandomProvider_stringBags_String_vi", 4.00516399999172);
        stringBags_String_helper(1, "abbc", "RandomProvider_stringBags_String_vii", 1.0006389999976706);
        stringBags_String_helper(2, "abbc", "RandomProvider_stringBags_String_viii", 2.0037019999891394);
        stringBags_String_helper(4, "abbc", "RandomProvider_stringBags_String_ix", 4.00571499999147);
        stringBags_String_helper(1, "Mississippi", "RandomProvider_stringBags_String_x", 0.9996679999977037);
        stringBags_String_helper(2, "Mississippi", "RandomProvider_stringBags_String_xi", 2.0026269999890762);
        stringBags_String_helper(4, "Mississippi", "RandomProvider_stringBags_String_xii", 4.0051349999917525);

        stringBags_String_fail_helper(1, "");
        stringBags_String_fail_helper(0, "abc");
        stringBags_String_fail_helper(-1, "abc");
    }

    private static void stringBags_helper(int scale, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringBags()));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringBags_fail_helper(int scale) {
        try {
            toList(P.withScale(scale).stringBags());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringBags() {
        stringBags_helper(1, "RandomProvider_stringBags_i", 1.0006389999976706);
        stringBags_helper(2, "RandomProvider_stringBags_ii", 2.0037019999891394);
        stringBags_helper(4, "RandomProvider_stringBags_iii", 4.00571499999147);

        stringBags_fail_helper(0);
        stringBags_fail_helper(-1);
    }

    private static void bagsAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).bagsAtLeast(minSize, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void bagsAtLeast_helper_uniform(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        bagsAtLeast_helper(
                scale,
                minSize,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                meanSize
        );
    }

    private static void bagsAtLeast_fail_helper(int scale, int minSize, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).bagsAtLeast(minSize, input));
            fail();
        } catch (NoSuchElementException | IllegalStateException | NullPointerException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testBagsAtLeast() {
        bagsAtLeast_helper_uniform(2, 1, "[5]", "RandomProvider_bagsAtLeast_i", 2.0008359999800347);
        bagsAtLeast_helper_uniform(5, 3, "[5]", "RandomProvider_bagsAtLeast_ii", 5.002096999996331);
        bagsAtLeast_helper_uniform(32, 8, "[5]", "RandomProvider_bagsAtLeast_iii", 32.00360900002322);
        bagsAtLeast_helper_uniform(2, 1, "[1, 2, 3]", "RandomProvider_bagsAtLeast_iv", 1.9993039999798474);
        bagsAtLeast_helper_uniform(5, 3, "[1, 2, 3]", "RandomProvider_bagsAtLeast_v", 5.003739999996368);
        bagsAtLeast_helper_uniform(32, 8, "[1, 2, 3]", "RandomProvider_bagsAtLeast_vi", 32.010685000021894);
        bagsAtLeast_helper_uniform(2, 1, "[1, 2, 3, 4]", "RandomProvider_bagsAtLeast_vii", 1.999585999979838);
        bagsAtLeast_helper_uniform(5, 3, "[1, 2, 3, 4]", "RandomProvider_bagsAtLeast_viii", 5.00315899999616);
        bagsAtLeast_helper_uniform(32, 8, "[1, 2, 3, 4]", "RandomProvider_bagsAtLeast_ix", 32.008717000021356);
        bagsAtLeast_helper_uniform(2, 1, "[1, 2, 2, 4]", "RandomProvider_bagsAtLeast_x", 1.999585999979838);
        bagsAtLeast_helper_uniform(5, 3, "[1, 2, 2, 4]", "RandomProvider_bagsAtLeast_xi", 5.00315899999616);
        bagsAtLeast_helper_uniform(32, 8, "[1, 2, 2, 4]", "RandomProvider_bagsAtLeast_xii", 32.008717000021356);
        bagsAtLeast_helper_uniform(2, 1, "[2, 2, 2, 2]", "RandomProvider_bagsAtLeast_xiii", 1.999585999979838);
        bagsAtLeast_helper_uniform(5, 3, "[2, 2, 2, 2]", "RandomProvider_bagsAtLeast_xiv", 5.00315899999616);
        bagsAtLeast_helper_uniform(32, 8, "[2, 2, 2, 2]", "RandomProvider_bagsAtLeast_xv", 32.008717000021356);
        bagsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bagsAtLeast_xvi",
                1.9987289999797695
        );
        bagsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bagsAtLeast_xvii",
                5.002305999996172
        );
        bagsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_bagsAtLeast_xviii",
                31.997066000022638
        );
        bagsAtLeast_helper(
                2,
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bagsAtLeast_xix",
                2.001126999979881
        );
        bagsAtLeast_helper(
                5,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bagsAtLeast_xx",
                5.001189999995907
        );
        bagsAtLeast_helper(
                32,
                8,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_bagsAtLeast_xxi",
                32.00730000002313
        );
        bagsAtLeast_helper(2, 1, repeat(1), "RandomProvider_bagsAtLeast_xxii", 2.0008359999800347);
        bagsAtLeast_helper(5, 3, repeat(1), "RandomProvider_bagsAtLeast_xxiii", 5.002096999996331);
        bagsAtLeast_helper(32, 8, repeat(1), "RandomProvider_bagsAtLeast_xxiv", 32.00360900002322);

        bagsAtLeast_fail_helper(5, 3, Collections.emptyList());
        bagsAtLeast_fail_helper(5, 3, Arrays.asList(1, 2, 3));
        bagsAtLeast_fail_helper(5, 5, P.integers());
        bagsAtLeast_fail_helper(4, 5, P.integers());
        bagsAtLeast_fail_helper(2, 1, Collections.singletonList(null));
        bagsAtLeast_fail_helper(2, 1, Arrays.asList(1, null, 3));
    }

    private static void stringBagsAtLeast_int_String_helper(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringBagsAtLeast(minSize, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringBagsAtLeast_int_String_fail_helper(int scale, int minSize, @NotNull String input) {
        try {
            toList(P.withScale(scale).stringBagsAtLeast(minSize, input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringBagsAtLeast_int_String() {
        stringBagsAtLeast_int_String_helper(
                2,
                1,
                "a",
                "RandomProvider_stringBagsAtLeast_int_String_i",
                2.0008359999800347
        );
        stringBagsAtLeast_int_String_helper(
                5,
                3,
                "a",
                "RandomProvider_stringBagsAtLeast_int_String_ii",
                5.002096999996331
        );
        stringBagsAtLeast_int_String_helper(
                32,
                8,
                "a",
                "RandomProvider_stringBagsAtLeast_int_String_iii",
                32.00360900002322
        );
        stringBagsAtLeast_int_String_helper(
                2,
                1,
                "abc",
                "RandomProvider_stringBagsAtLeast_int_String_iv",
                1.9993039999798474
        );
        stringBagsAtLeast_int_String_helper(
                5,
                3,
                "abc",
                "RandomProvider_stringBagsAtLeast_int_String_v",
                5.003739999996368
        );
        stringBagsAtLeast_int_String_helper(
                32,
                8,
                "abc",
                "RandomProvider_stringBagsAtLeast_int_String_vi",
                32.010685000021894
        );
        stringBagsAtLeast_int_String_helper(
                2,
                1,
                "abbc",
                "RandomProvider_stringBagsAtLeast_int_String_vii",
                1.999585999979838
        );
        stringBagsAtLeast_int_String_helper(
                5,
                3,
                "abbc",
                "RandomProvider_stringBagsAtLeast_int_String_viii",
                5.00315899999616
        );
        stringBagsAtLeast_int_String_helper(
                32,
                8,
                "abbc",
                "RandomProvider_stringBagsAtLeast_int_String_ix",
                32.008717000021356
        );
        stringBagsAtLeast_int_String_helper(
                2,
                1,
                "Mississippi",
                "RandomProvider_stringBagsAtLeast_int_String_x",
                1.9990949999798069
        );
        stringBagsAtLeast_int_String_helper(
                5,
                3,
                "Mississippi",
                "RandomProvider_stringBagsAtLeast_int_String_xi",
                5.003636999996235
        );
        stringBagsAtLeast_int_String_helper(
                32,
                8,
                "Mississippi",
                "RandomProvider_stringBagsAtLeast_int_String_xii",
                32.00263800002314
        );

        stringBagsAtLeast_int_String_fail_helper(5, 3, "");
        stringBagsAtLeast_int_String_fail_helper(5, 5, "abc");
        stringBagsAtLeast_int_String_fail_helper(4, 5, "abc");
    }

    private static void stringBagsAtLeast_int_helper(int scale, int minSize, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringBagsAtLeast(minSize)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringBagsAtLeast_int_fail_helper(int scale, int minSize) {
        try {
            toList(P.withScale(scale).stringBagsAtLeast(minSize));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringBagsAtLeast_int() {
        stringBagsAtLeast_int_helper(2, 1, "RandomProvider_stringBagsAtLeast_int_i", 1.999585999979838);
        stringBagsAtLeast_int_helper(5, 3, "RandomProvider_stringBagsAtLeast_int_ii", 5.00315899999616);
        stringBagsAtLeast_int_helper(32, 8, "RandomProvider_stringBagsAtLeast_int_iii", 32.008717000021356);
        stringBagsAtLeast_int_fail_helper(5, 5);
        stringBagsAtLeast_int_fail_helper(4, 5);
    }

    private static void stringSubsets_int_String_helper(int size, @NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringSubsets(size, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void stringSubsets_int_String_fail_helper(int size, @NotNull String input) {
        try {
            P.stringSubsets(size, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStringSubsets_int_String() {
        stringSubsets_int_String_helper(0, "a", "RandomProvider_stringSubsets_int_String_i");
        stringSubsets_int_String_helper(1, "a", "RandomProvider_stringSubsets_int_String_ii");
        stringSubsets_int_String_helper(0, "abc", "RandomProvider_stringSubsets_int_String_iii");
        stringSubsets_int_String_helper(1, "abc", "RandomProvider_stringSubsets_int_String_iv");
        stringSubsets_int_String_helper(2, "abc", "RandomProvider_stringSubsets_int_String_v");
        stringSubsets_int_String_helper(3, "abc", "RandomProvider_stringSubsets_int_String_vi");
        stringSubsets_int_String_helper(0, "abbc", "RandomProvider_stringSubsets_int_String_vii");
        stringSubsets_int_String_helper(1, "abbc", "RandomProvider_stringSubsets_int_String_viii");
        stringSubsets_int_String_helper(2, "abbc", "RandomProvider_stringSubsets_int_String_ix");
        stringSubsets_int_String_helper(3, "abbc", "RandomProvider_stringSubsets_int_String_x");
        stringSubsets_int_String_helper(0, "Mississippi", "RandomProvider_stringSubsets_int_String_xi");
        stringSubsets_int_String_helper(1, "Mississippi", "RandomProvider_stringSubsets_int_String_xii");
        stringSubsets_int_String_helper(2, "Mississippi", "RandomProvider_stringSubsets_int_String_xiii");
        stringSubsets_int_String_helper(3, "Mississippi", "RandomProvider_stringSubsets_int_String_xiv");

        stringSubsets_int_String_fail_helper(1, "");
        stringSubsets_int_String_fail_helper(-1, "abc");
    }

    private static void stringSubsets_int_helper(int size, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.stringSubsets(size)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private void stringSubsets_int_fail_helper(int size) {
        try {
            P.stringBags(size);
            fail();
        } catch (IllegalArgumentException ignored) {}
        finally{
            P.reset();
        }
    }

    @Test
    public void testStringSubsets_int() {
        stringSubsets_int_helper(0, "RandomProvider_stringSubsets_int_i");
        stringSubsets_int_helper(1, "RandomProvider_stringSubsets_int_ii");
        stringSubsets_int_helper(2, "RandomProvider_stringSubsets_int_iii");
        stringSubsets_int_helper(3, "RandomProvider_stringSubsets_int_iv");

        stringSubsets_int_fail_helper(-1);
    }

    private static void subsets_Iterable_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).subsets(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void subsets_Iterable_helper_uniform(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        subsets_Iterable_helper(
                scale,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                meanSize
        );
    }

    private static void subsets_Iterable_fail_helper(int scale, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).subsets(input));
            fail();
        } catch (NoSuchElementException | IllegalStateException | NullPointerException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSubsets_Iterable() {
        subsets_Iterable_helper_uniform(1, "[5]", "RandomProvider_subsets_Iterable_i", 0.5008749999935656);
        subsets_Iterable_helper_uniform(2, "[5]", "RandomProvider_subsets_Iterable_ii", 0.6661869999983192);
        subsets_Iterable_helper_uniform(4, "[5]", "RandomProvider_subsets_Iterable_iii", 0.7998060000021615);
        subsets_Iterable_helper_uniform(1, "[1, 2, 3]", "RandomProvider_subsets_Iterable_iv", 0.7507059999970308);
        subsets_Iterable_helper_uniform(2, "[1, 2, 3]", "RandomProvider_subsets_Iterable_v", 1.2008789999923022);
        subsets_Iterable_helper_uniform(4, "[1, 2, 3]", "RandomProvider_subsets_Iterable_vi", 1.7145229999887661);
        subsets_Iterable_helper_uniform(1, "[1, 2, 3, 4]", "RandomProvider_subsets_Iterable_vii", 0.8006769999971934);
        subsets_Iterable_helper_uniform(2, "[1, 2, 3, 4]", "RandomProvider_subsets_Iterable_viii", 1.334835999990812);
        subsets_Iterable_helper_uniform(4, "[1, 2, 3, 4]", "RandomProvider_subsets_Iterable_ix", 2.001787999981212);
        subsets_Iterable_helper_uniform(1, "[1, 2, 2, 4]", "RandomProvider_subsets_Iterable_x", 0.7339709999971153);
        subsets_Iterable_helper_uniform(2, "[1, 2, 2, 4]", "RandomProvider_subsets_Iterable_xi", 1.1676389999927037);
        subsets_Iterable_helper_uniform(4, "[1, 2, 2, 4]", "RandomProvider_subsets_Iterable_xii", 1.667697999989275);
        subsets_Iterable_helper_uniform(1, "[2, 2, 2, 2]", "RandomProvider_subsets_Iterable_xiii", 0.5004429999935531);
        subsets_Iterable_helper_uniform(2, "[2, 2, 2, 2]", "RandomProvider_subsets_Iterable_xiv", 0.6669589999983414);
        subsets_Iterable_helper_uniform(4, "[2, 2, 2, 2]", "RandomProvider_subsets_Iterable_xv", 0.7999900000021668);
        subsets_Iterable_helper_uniform(
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsets_Iterable_xvi",
                0.9078379999975383
        );
        subsets_Iterable_helper_uniform(
                2,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsets_Iterable_xvii",
                1.6697689999898184
        );
        subsets_Iterable_helper_uniform(
                4,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsets_Iterable_xviii",
                2.8588639999882393
        );
        subsets_Iterable_helper(
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsets_Iterable_xix",
                0.8811449999975006
        );
        subsets_Iterable_helper(
                2,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsets_Iterable_xx",
                1.583489999990105
        );
        subsets_Iterable_helper(
                4,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsets_Iterable_xxi",
                2.668782999988186
        );
        subsets_Iterable_helper(1, repeat(1), "RandomProvider_subsets_Iterable_xxii", 0.5008749999935656);
        subsets_Iterable_helper(2, repeat(1), "RandomProvider_subsets_Iterable_xxiii", 0.6661869999983192);
        subsets_Iterable_helper(4, repeat(1), "RandomProvider_subsets_Iterable_xxiv", 0.7998060000021615);

        subsets_Iterable_fail_helper(1, Collections.emptyList());
        subsets_Iterable_fail_helper(1, Arrays.asList(1, 2, 3));
        subsets_Iterable_fail_helper(0, P.integers());
        subsets_Iterable_fail_helper(-1, P.integers());
        subsets_Iterable_fail_helper(1, Arrays.asList(1, null, 3));
        subsets_Iterable_fail_helper(1, Collections.singleton(null));
    }

    private static void stringSubsets_String_helper(
            int scale,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsets(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsets_String_fail_helper(int scale, @NotNull String input) {
        try {
            toList(P.withScale(scale).stringSubsets(input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsets_String() {
        stringSubsets_String_helper(1, "a", "RandomProvider_stringSubsets_String_i", 0.5008749999935656);
        stringSubsets_String_helper(2, "a", "RandomProvider_stringSubsets_String_ii", 0.6661869999983192);
        stringSubsets_String_helper(4, "a", "RandomProvider_stringSubsets_String_iii", 0.7998060000021615);
        stringSubsets_String_helper(1, "abc", "RandomProvider_stringSubsets_String_iv", 0.7507059999970308);
        stringSubsets_String_helper(2, "abc", "RandomProvider_stringSubsets_String_v", 1.2008789999923022);
        stringSubsets_String_helper(4, "abc", "RandomProvider_stringSubsets_String_vi", 1.7145229999887661);
        stringSubsets_String_helper(1, "abbc", "RandomProvider_stringSubsets_String_vii", 0.7339709999971153);
        stringSubsets_String_helper(2, "abbc", "RandomProvider_stringSubsets_String_viii", 1.1676389999927037);
        stringSubsets_String_helper(4, "abbc", "RandomProvider_stringSubsets_String_ix", 1.667697999989275);
        stringSubsets_String_helper(1, "Mississippi", "RandomProvider_stringSubsets_String_x", 0.7700039999971866);
        stringSubsets_String_helper(2, "Mississippi", "RandomProvider_stringSubsets_String_xi", 1.2632049999918284);
        stringSubsets_String_helper(4, "Mississippi", "RandomProvider_stringSubsets_String_xii", 1.8740139999846195);

        stringSubsets_String_fail_helper(1, "");
        stringSubsets_String_fail_helper(0, "abc");
        stringSubsets_String_fail_helper(-1, "abc");
    }

    private static void stringSubsets_helper(int scale, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsets()));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsets_fail_helper(int scale) {
        try {
            toList(P.withScale(scale).stringSubsets());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsets() {
        stringSubsets_helper(1, "RandomProvider_stringSubsets_i", 1.0006239999976707);
        stringSubsets_helper(2, "RandomProvider_stringSubsets_ii", 2.0036399999891383);
        stringSubsets_helper(4, "RandomProvider_stringSubsets_iii", 4.005472999991468);

        stringSubsets_fail_helper(0);
        stringSubsets_fail_helper(-1);
    }

    private static void subsetsAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).subsetsAtLeast(minSize, input))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void subsetsAtLeast_helper_uniform(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        subsetsAtLeast_helper(scale, minSize, P.uniformSample(readIntegerListWithNulls(input)), output, meanSize);
    }

    private static void subsetsAtLeast_fail_helper(int scale, int minSize, @NotNull Iterable<Integer> input) {
        try {
            toList(P.withScale(scale).subsetsAtLeast(minSize, input));
            fail();
        } catch (NoSuchElementException | IllegalStateException | NullPointerException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSubsetsAtLeast() {
        subsetsAtLeast_helper_uniform(2, 1, "[5]", "RandomProvider_subsetsAtLeast_i", 1.000000000007918);
        subsetsAtLeast_helper_uniform(2, 1, "[1, 2, 3]", "RandomProvider_subsetsAtLeast_ii", 1.50008299998526);
        subsetsAtLeast_helper_uniform(5, 3, "[1, 2, 3]", "RandomProvider_subsetsAtLeast_iii", 2.9999999999775233);
        subsetsAtLeast_helper_uniform(2, 1, "[1, 2, 3, 4]", "RandomProvider_subsetsAtLeast_iv", 1.5996069999831977);
        subsetsAtLeast_helper_uniform(5, 3, "[1, 2, 3, 4]", "RandomProvider_subsetsAtLeast_v", 3.3338519999899345);
        subsetsAtLeast_helper_uniform(2, 1, "[1, 2, 2, 4]", "RandomProvider_subsetsAtLeast_vi", 1.466173999985577);
        subsetsAtLeast_helper_uniform(5, 3, "[1, 2, 2, 4]", "RandomProvider_subsetsAtLeast_vii", 2.9999999999775233);
        subsetsAtLeast_helper_uniform(2, 1, "[2, 2, 2, 2]", "RandomProvider_subsetsAtLeast_viii", 1.000000000007918);
        subsetsAtLeast_helper_uniform(
                2,
                1,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsetsAtLeast_ix",
                1.8170889999810345
        );
        subsetsAtLeast_helper_uniform(
                5,
                3,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsetsAtLeast_x",
                4.168420999985633
        );
        subsetsAtLeast_helper_uniform(
                32,
                8,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                "RandomProvider_subsetsAtLeast_xi",
                9.41189799992237
        );
        subsetsAtLeast_helper(
                2,
                1,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsetsAtLeast_xii",
                1.7601419999815262
        );
        subsetsAtLeast_helper(
                5,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsetsAtLeast_xiii",
                3.989958999983499
        );
        subsetsAtLeast_helper(
                32,
                8,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_subsetsAtLeast_xiv",
                10.378239999978224
        );
        subsetsAtLeast_helper(2, 1, repeat(1), "RandomProvider_subsetsAtLeast_xv", 1.000000000007918);

        subsetsAtLeast_fail_helper(5, 3, Collections.emptyList());
        subsetsAtLeast_fail_helper(5, 3, Arrays.asList(1, 2, 3));
        subsetsAtLeast_fail_helper(5, 5, P.integers());
        subsetsAtLeast_fail_helper(4, 5, P.integers());
        subsetsAtLeast_fail_helper(2, 1, Collections.singletonList(null));
        subsetsAtLeast_fail_helper(2, 1, Arrays.asList(1, null, 3));
    }

    private static void stringSubsetsAtLeast_int_String_helper(
            int scale,
            int minSize,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsetsAtLeast(minSize, input))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsetsAtLeast_int_String_fail_helper(int scale, int minSize, @NotNull String input) {
        try {
            toList(P.withScale(scale).stringSubsetsAtLeast(minSize, input));
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsetsAtLeast_int_String() {
        stringSubsetsAtLeast_int_String_helper(
                2,
                1,
                "a",
                "RandomProvider_stringSubsetsAtLeast_int_String_i",
                1.000000000007918
        );
        stringSubsetsAtLeast_int_String_helper(
                2,
                1,
                "abc",
                "RandomProvider_stringSubsetsAtLeast_int_String_ii",
                1.50008299998526
        );
        stringSubsetsAtLeast_int_String_helper(
                5,
                3,
                "abc",
                "RandomProvider_stringSubsetsAtLeast_int_String_iii",
                2.9999999999775233
        );
        stringSubsetsAtLeast_int_String_helper(
                2,
                1,
                "abbc",
                "RandomProvider_stringSubsetsAtLeast_int_String_iv",
                1.466173999985577
        );
        stringSubsetsAtLeast_int_String_helper(
                5,
                3,
                "abbc",
                "RandomProvider_stringSubsetsAtLeast_int_String_v",
                2.9999999999775233
        );
        stringSubsetsAtLeast_int_String_helper(
                2,
                1,
                "Mississippi",
                "RandomProvider_stringSubsetsAtLeast_int_String_vi",
                1.5401079999842737
        );
        stringSubsetsAtLeast_int_String_helper(
                5,
                3,
                "Mississippi",
                "RandomProvider_stringSubsetsAtLeast_int_String_vii",
                3.2285219999851744
        );

        stringSubsetsAtLeast_int_String_fail_helper(5, 3, "");
        stringSubsetsAtLeast_int_String_fail_helper(5, 5, "abc");
        stringSubsetsAtLeast_int_String_fail_helper(4, 5, "abc");
    }

    private static void stringSubsetsAtLeast_int_helper(
            int scale,
            int minSize,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsetsAtLeast(minSize)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsetsAtLeast_int_fail_helper(int scale, int minSize) {
        try {
            toList(P.withScale(scale).stringSubsetsAtLeast(minSize));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsetsAtLeast_int() {
        stringSubsetsAtLeast_int_helper(2, 1, "RandomProvider_stringSubsetsAtLeast_int_i", 1.9995569999798375);
        stringSubsetsAtLeast_int_helper(5, 3, "RandomProvider_stringSubsetsAtLeast_int_ii", 5.00299199999616);
        stringSubsetsAtLeast_int_helper(32, 8, "RandomProvider_stringSubsetsAtLeast_int_iii", 31.99690200002153);

        stringSubsetsAtLeast_int_fail_helper(5, 5);
        stringSubsetsAtLeast_int_fail_helper(4, 5);
    }

    private static void eithers_helper(int scale, @NotNull String as, @NotNull String bs, @NotNull String output) {
        List<Either<Integer, Integer>> sample = toList(
                take(
                        DEFAULT_SAMPLE_SIZE,
                        P.withScale(scale).eithers(
                                P.uniformSample(readIntegerListWithNulls(as)),
                                P.uniformSample(readIntegerListWithNulls(bs))
                        )
                )
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private static void eithers_fail_helper(int scale, @NotNull Iterable<Integer> as, @NotNull Iterable<Integer> bs) {
        try {
            toList(P.withScale(scale).eithers(as, bs));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testEithers() {
        eithers_helper(1, "[1]", "[2]", "RandomProvider_eithers_i");
        eithers_helper(2, "[1]", "[2]", "RandomProvider_eithers_ii");
        eithers_helper(10, "[1]", "[2]", "RandomProvider_eithers_iii");
        eithers_helper(1, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_eithers_iv");
        eithers_helper(2, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_eithers_v");
        eithers_helper(10, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_eithers_vi");

        eithers_fail_helper(1, Arrays.asList(-1, -2, -3), P.naturalIntegers());
        eithers_fail_helper(1, P.naturalIntegers(), Arrays.asList(-1, -2, -3));
        eithers_fail_helper(1, Arrays.asList(1, 2, 3), Arrays.asList(-1, -2, -3));
        eithers_fail_helper(0, P.naturalIntegers(), P.negativeIntegers());
        eithers_fail_helper(-1, P.naturalIntegers(), P.negativeIntegers());
    }

    private static void choose_helper(int scale, @NotNull String as, @NotNull String bs, @NotNull String output) {
        List<Integer> sample = toList(
                take(
                        DEFAULT_SAMPLE_SIZE,
                        P.withScale(scale).choose(
                                P.uniformSample(readIntegerListWithNulls(as)),
                                P.uniformSample(readIntegerListWithNulls(bs))
                        )
                )
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private static void choose_fail_helper(int scale, @NotNull Iterable<Integer> as, @NotNull Iterable<Integer> bs) {
        try {
            toList(P.withScale(scale).choose(as, bs));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testChoose() {
        choose_helper(1, "[1]", "[2]", "RandomProvider_choose_i");
        choose_helper(2, "[1]", "[2]", "RandomProvider_choose_ii");
        choose_helper(10, "[1]", "[2]", "RandomProvider_choose_iii");
        choose_helper(1, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_choose_iv");
        choose_helper(2, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_choose_v");
        choose_helper(10, "[1, 2, 3]", "[null, -2, -3]", "RandomProvider_choose_vi");

        choose_fail_helper(1, Arrays.asList(-1, -2, -3), P.naturalIntegers());
        choose_fail_helper(1, P.naturalIntegers(), Arrays.asList(-1, -2, -3));
        choose_fail_helper(1, Arrays.asList(1, 2, 3), Arrays.asList(-1, -2, -3));
        choose_fail_helper(0, P.naturalIntegers(), P.negativeIntegers());
        choose_fail_helper(-1, P.naturalIntegers(), P.negativeIntegers());
    }

    private static void cartesianProduct_helper(@NotNull String xss, @NotNull String output) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.cartesianProduct(readIntegerListWithNullsListsWithNulls(xss)))
        );
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    private static void cartesianProduct_fail_helper(@NotNull String xss) {
        try {
            toList(P.cartesianProduct(readIntegerListWithNullsListsWithNulls(xss)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testCartesianProduct() {
        cartesianProduct_helper("[[0]]", "RandomProvider_cartesianProduct_i");
        cartesianProduct_helper("[[null]]", "RandomProvider_cartesianProduct_ii");
        cartesianProduct_helper("[[0, 1]]", "RandomProvider_cartesianProduct_iii");
        cartesianProduct_helper("[[0, 1], [2, 3]]", "RandomProvider_cartesianProduct_iv");
        cartesianProduct_helper("[[1], [1], [1]]", "RandomProvider_cartesianProduct_v");
        cartesianProduct_helper("[[null, null, null]]", "RandomProvider_cartesianProduct_vi");
        cartesianProduct_helper("[[0, 1, 2], [-3, -4], [null, 10]]", "RandomProvider_cartesianProduct_vii");
        cartesianProduct_helper("[[0, 1], [0, 1], [0, 1]]", "RandomProvider_cartesianProduct_viii");

        cartesianProduct_fail_helper("[]");
        cartesianProduct_fail_helper("[[], [1, 2]]");
        cartesianProduct_fail_helper("[[1, 2], null]");
    }

    private static void repeatingIterables_helper(
            int scale,
            @NotNull Iterable<Integer> input,
            @NotNull String output
    ) {
        aeqitLimitLog(TINY_LIMIT, map(Testing::its, P.withScale(scale).repeatingIterables(input)), output);
        P.reset();
    }

    @Test
    public void testRepeatingIterables() {
        repeatingIterables_helper(2, P.positiveIntegersGeometric(), "RandomProvider_repeatingIterables_i");
        repeatingIterables_helper(4, P.positiveIntegersGeometric(), "RandomProvider_repeatingIterables_ii");
        repeatingIterables_helper(8, P.positiveIntegersGeometric(), "RandomProvider_repeatingIterables_iii");
    }

    private static void repeatingIterablesDistinctAtLeast_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input,
            @NotNull String output
    ) {
        aeqitLimitLog(
                TINY_LIMIT,
                map(Testing::its, P.withScale(scale).repeatingIterablesDistinctAtLeast(minSize, input)),
                output
        );
        P.reset();
    }

    private static void repeatingIterablesDistinctAtLeast_fail_helper(
            int scale,
            int minSize,
            @NotNull Iterable<Integer> input
    ) {
        try {
            P.withScale(scale).repeatingIterablesDistinctAtLeast(minSize, input);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRepeatingIterablesDistinctAtLeast() {
        repeatingIterablesDistinctAtLeast_helper(
                3,
                2,
                P.positiveIntegersGeometric(),
                "RandomProvider_repeatingIterablesDistinctAtLeast_i"
        );
        repeatingIterablesDistinctAtLeast_helper(
                5,
                3,
                P.positiveIntegersGeometric(),
                "RandomProvider_repeatingIterablesDistinctAtLeast_ii"
        );
        repeatingIterablesDistinctAtLeast_helper(
                32,
                5,
                P.positiveIntegersGeometric(),
                "RandomProvider_repeatingIterablesDistinctAtLeast_iii"
        );
        repeatingIterablesDistinctAtLeast_fail_helper(1, 1, P.positiveIntegers());
        repeatingIterablesDistinctAtLeast_fail_helper(1, -1, P.positiveIntegers());
    }

    private static void sublists_helper(@NotNull String input, @NotNull String output) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.sublists(readIntegerListWithNulls(input))));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    @Test
    public void testSublists() {
        sublists_helper("[]", "RandomProvider_sublists_i");
        sublists_helper("[1, 2, 3, 4]", "RandomProvider_sublists_ii");
        sublists_helper("[1, null, 3, 4]", "RandomProvider_sublists_iii");
        sublists_helper("[3, 1, 4, 1]", "RandomProvider_sublists_iv");
        sublists_helper("[1, 1, 1, 1]", "RandomProvider_sublists_v");
    }

    private static void substrings_helper(@NotNull String input, @NotNull String output) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.substrings(input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        P.reset();
    }

    @Test
    public void testSubstrings() {
        substrings_helper("", "RandomProvider_substrings_i");
        substrings_helper("abcd", "RandomProvider_substrings_ii");
        substrings_helper("aaaa", "RandomProvider_substrings_iii");
        substrings_helper("Mississippi", "RandomProvider_substrings_iv");
    }

    private static void listsWithElement_helper(
            int scale,
            @Nullable Integer x,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).listsWithElement(x, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void listsWithElement_helper_uniform(
            int scale,
            @Nullable Integer x,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        listsWithElement_helper(
                scale,
                x,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                meanSize
        );
    }

    private static void listsWithElement_fail_helper(
            int scale,
            @Nullable Integer x,
            @NotNull Iterable<Integer> input
    ) {
        try {
            toList(P.withScale(scale).listsWithElement(x, input));
            fail();
        } catch (IllegalStateException | NoSuchElementException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testListsWithElement() {
        listsWithElement_helper_uniform(3, 0, "[1, 2, 3]", "RandomProvider_listsWithElement_i", 3.001751999985313);
        listsWithElement_helper_uniform(5, -5, "[1, 2, 3]", "RandomProvider_listsWithElement_ii", 5.007290999999437);
        listsWithElement_helper_uniform(
                32,
                null,
                "[1, 2, 3]",
                "RandomProvider_listsWithElement_iii",
                31.993837000022335
        );
        listsWithElement_helper_uniform(3, 0, "[1, null, 3]", "RandomProvider_listsWithElement_iv", 3.001751999985313);
        listsWithElement_helper_uniform(5, -5, "[1, null, 3]", "RandomProvider_listsWithElement_v", 5.007290999999437);
        listsWithElement_helper_uniform(
                32,
                null,
                "[1, null, 3]",
                "RandomProvider_listsWithElement_vi",
                32.00126100002188
        );
        listsWithElement_helper_uniform(
                3,
                0,
                "[1, 2, 2, 4]",
                "RandomProvider_listsWithElement_vii",
                3.0022749999853073
        );
        listsWithElement_helper_uniform(
                5,
                -5,
                "[1, 2, 2, 4]",
                "RandomProvider_listsWithElement_viii",
                5.005771999999611
        );
        listsWithElement_helper_uniform(
                32,
                null,
                "[1, 2, 2, 4]",
                "RandomProvider_listsWithElement_ix",
                32.023569000021205
        );
        listsWithElement_helper(
                3,
                0,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsWithElement_x",
                2.999244999985283
        );
        listsWithElement_helper(
                5,
                -5,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsWithElement_xi",
                5.003905999999416
        );
        listsWithElement_helper(
                32,
                null,
                P.withScale(4).positiveIntegersGeometric(),
                "RandomProvider_listsWithElement_xii",
                32.001327000020574
        );
        listsWithElement_helper(3, 0, repeat(1), "RandomProvider_listsWithElement_xiii", 2.999981999985526);
        listsWithElement_helper(5, -5, repeat(1), "RandomProvider_listsWithElement_xiv", 5.0037189999994975);
        listsWithElement_helper(32, null, repeat(1), "RandomProvider_listsWithElement_xv", 31.985562000021005);

        listsWithElement_fail_helper(5, null, Collections.emptyList());
        listsWithElement_fail_helper(5, null, Arrays.asList(1, 2, 3));
        listsWithElement_fail_helper(2, null, P.integers());
    }

    private static void stringsWithChar_char_String_helper(
            int scale,
            char c,
            @NotNull String input,
            @NotNull String output,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsWithChar(c, input)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsWithChar_char_String_fail_helper(int scale, char c, @NotNull String input) {
        try {
            P.withScale(scale).stringsWithChar(c, input);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringsWithChar_char_String() {
        stringsWithChar_char_String_helper(
                3,
                'b',
                "abcd",
                "RandomProvider_stringsWithChar_char_String_i",
                3.0011149999852065
        );
        stringsWithChar_char_String_helper(
                5,
                '#',
                "abcd",
                "RandomProvider_stringsWithChar_char_String_ii",
                5.005771999999611
        );
        stringsWithChar_char_String_helper(
                32,
                ' ',
                "abcd",
                "RandomProvider_stringsWithChar_char_String_iii",
                32.023569000021205
        );
        stringsWithChar_char_String_helper(
                3,
                'b',
                "aaaa",
                "RandomProvider_stringsWithChar_char_String_iv",
                3.0022749999853073
        );
        stringsWithChar_char_String_helper(
                5,
                '#',
                "aaaa",
                "RandomProvider_stringsWithChar_char_String_v",
                5.005771999999611
        );
        stringsWithChar_char_String_helper(
                32,
                ' ',
                "aaaa",
                "RandomProvider_stringsWithChar_char_String_vi",
                32.023569000021205
        );
        stringsWithChar_char_String_helper(
                3,
                'b',
                "Mississippi",
                "RandomProvider_stringsWithChar_char_String_vii",
                2.9995019999853154
        );
        stringsWithChar_char_String_helper(
                5,
                '#',
                "Mississippi",
                "RandomProvider_stringsWithChar_char_String_viii",
                5.0064659999996515
        );
        stringsWithChar_char_String_helper(
                32,
                ' ',
                "Mississippi",
                "RandomProvider_stringsWithChar_char_String_ix",
                31.994617000022732
        );

        stringsWithChar_char_String_fail_helper(2, ' ', "abc");
    }

    private static void stringsWithChar_char_helper(int scale, char c, @NotNull String output, double meanSize) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsWithChar(c)));
        aeqitLimitLog(TINY_LIMIT, sample, output);
        aeqMapLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsWithChar_char_fail_helper(int scale, char c) {
        try {
            P.withScale(scale).stringsWithChar(c);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringsWithChar_char() {
        stringsWithChar_char_helper(3, 'b', "RandomProvider_stringsWithChar_char_i", 3.0022749999853757);
        stringsWithChar_char_helper(5, '#', "RandomProvider_stringsWithChar_char_ii", 5.005778999999451);
        stringsWithChar_char_helper(32, ' ', "RandomProvider_stringsWithChar_char_iii", 32.02361800002122);

        stringsWithChar_char_fail_helper(2, ' ');
    }

    private static void subsetsWithElement_helper(
            int scale,
            @Nullable Integer x,
            @NotNull Iterable<Integer> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).subsetsWithElement(x, input))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void subsetsWithElement_helper_uniform(
            int scale,
            @Nullable Integer x,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        subsetsWithElement_helper(
                scale,
                x,
                P.uniformSample(readIntegerListWithNulls(input)),
                output,
                topSampleCount,
                meanSize
        );
    }

    private static void subsetsWithElement_fail_helper(
            int scale,
            @Nullable Integer x,
            @NotNull Iterable<Integer> input
    ) {
        try {
            toList(P.withScale(scale).subsetsWithElement(x, input));
            fail();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSubsetsWithElement() {
        subsetsWithElement_helper_uniform(
                2,
                0,
                "[1, 2, 3]",
                "[[0, 2], [0, 1, 2, 3], [0], [0], [0], [0, 2, 3], [0], [0], [0, 1, 2], [0, 3], [0], [0], [0, 2]," +
                " [0, 1, 3], [0, 3], [0, 3], [0], [0, 3], [0], [0], ...]",
                "{[0]=499507, [0, 3]=100200, [0, 1]=100121, [0, 2]=99865, [0, 2, 3]=50269, [0, 1, 2, 3]=50235," +
                " [0, 1, 2]=50160, [0, 1, 3]=49643}",
                1.7510349999822503
        );
        subsetsWithElement_helper_uniform(
                5,
                -5,
                "[1, 2, 3]",
                "[[-5, 2], [-5, 2], [-5, 1, 2, 3], [-5, 1], [-5, 1], [-5, 1, 2, 3], [-5, 2, 3], [-5, 1, 2, 3], [-5]," +
                " [-5, 1, 2, 3], [-5, 1, 3], [-5, 1, 3], [-5, 1, 2, 3], [-5], [-5], [-5, 1, 3], [-5, 1], [-5, 1, 3]," +
                " [-5], [-5, 1, 3], ...]",
                "{[-5, 1, 2, 3]=332308, [-5]=199912, [-5, 2, 3]=83429, [-5, 1, 3]=82869, [-5, 1, 2]=82865," +
                " [-5, 3]=72904, [-5, 1]=72875, [-5, 2]=72838}",
                2.7138669999868528
        );
        subsetsWithElement_helper_uniform(
                32,
                3,
                "[1, 2, 3]",
                "[[1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [3], [1, 2, 3]," +
                " [1, 2, 3], [2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3], [1, 2, 3]," +
                " [1, 2, 3], [1, 2, 3], ...]",
                "{[1, 2, 3]=909921, [3]=31389, [1, 3]=29542, [2, 3]=29148}",
                2.878531999977901
        );
        subsetsWithElement_helper_uniform(
                2,
                0,
                "[1, 2, 2, 4]",
                "[[0, 2], [0], [0, 1, 2, 4], [0, 1, 2], [0, 4], [0, 4], [0, 4], [0, 1, 2], [0, 2], [0], [0], [0, 2]," +
                " [0, 4], [0, 2], [0], [0, 1], [0, 2], [0], [0, 2], [0], ...]",
                "{[0]=499557, [0, 2]=167319, [0, 4]=71464, [0, 1]=71430, [0, 1, 2]=61773, [0, 2, 4]=61661," +
                " [0, 1, 2, 4]=42991, [0, 1, 4]=23805}",
                1.733663999982879
        );
        subsetsWithElement_helper_uniform(
                5,
                -5,
                "[1, 2, 2, 4]",
                "[[-5, 2], [-5, 2, 4], [-5, 1, 2, 4], [-5, 1, 2], [-5], [-5], [-5, 2], [-5, 2, 4], [-5, 1, 2]," +
                " [-5, 1, 2, 4], [-5, 2, 4], [-5, 1, 2, 4], [-5, 2], [-5], [-5, 1, 2, 4], [-5, 1, 2], [-5, 1, 2, 4]," +
                " [-5, 4], [-5], [-5, 2, 4], ...]",
                "{[-5, 1, 2, 4]=300718, [-5]=200010, [-5, 2]=133219, [-5, 2, 4]=116740, [-5, 1, 2]=116152," +
                " [-5, 1]=49925, [-5, 4]=49800, [-5, 1, 4]=33436}",
                2.6677539999856954
        );
        subsetsWithElement_helper_uniform(
                32,
                3,
                "[1, 2, 2, 4]",
                "[[1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3], [1, 2, 3, 4], [3], [1, 2, 3, 4], [3]," +
                " [1, 2, 3, 4], [1, 2, 3, 4], [2, 3], [2, 3], [1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3, 4]," +
                " [1, 2, 3, 4], [1, 2, 3, 4], [1, 2, 3, 4], [2, 3], [1, 2, 3, 4], ...]",
                "{[1, 2, 3, 4]=823110, [1, 2, 3]=43609, [2, 3, 4]=43361, [3]=31317, [2, 3]=29307, [1, 3]=10032," +
                " [3, 4]=9935, [1, 3, 4]=9329}",
                3.711202000016531
        );
        subsetsWithElement_helper(
                2,
                0,
                P.withScale(4).positiveIntegersGeometric(),
                "[[0, 1, 2, 3, 4, 7, 10], [0], [0, 7], [0, 5], [0], [0], [0], [0], [0], [0], [0], [0], [0, 3]," +
                " [0, 5, 6, 9, 15], [0, 12], [0], [0], [0], [0], [0], ...]",
                "{[0]=499603, [0, 1]=71342, [0, 2]=51658, [0, 3]=37660, [0, 4]=27749, [0, 5]=20802, [0, 1, 2]=16906," +
                " [0, 6]=15214, [0, 1, 3]=12259, [0, 7]=11373}",
                1.8810509999810079
        );
        subsetsWithElement_helper(
                5,
                -5,
                P.withScale(4).positiveIntegersGeometric(),
                "[[-5, 7], [-5, 1, 2, 3, 4, 7, 8], [-5, 1, 6, 8, 12, 15], [-5, 2], [-5, 1, 2, 3, 5, 6, 10, 13]," +
                " [-5], [-5, 1, 2, 3, 4, 6, 12], [-5], [-5], [-5], [-5, 1, 2], [-5, 3, 5], [-5, 4]," +
                " [-5, 1, 4, 6, 7, 9], [-5], [-5, 1, 2, 3, 5], [-5, 3, 7, 13], [-5, 4, 10]," +
                " [-5, 1, 2, 3, 4, 17, 19, 22], [-5], ...]",
                "{[-5]=199867, [-5, 1]=49980, [-5, 2]=35180, [-5, 3]=25191, [-5, 1, 2]=22543, [-5, 4]=18409," +
                " [-5, 1, 3]=15351, [-5, 5]=13445, [-5, 1, 2, 3]=13147, [-5, 1, 4]=11025}",
                3.669920999984385
        );
        subsetsWithElement_helper(
                32,
                3,
                P.withScale(4).positiveIntegersGeometric(),
                "[[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15], [1, 2, 3, 4, 5, 6, 7, 9], [1, 2, 3, 4, 5, 7, 10, 13]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 10, 13, 14, 17, 19, 20], [1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 18]," +
                " [1, 2, 3, 4, 5, 8, 10, 13], [1, 2, 3, 4, 7, 9, 10, 15]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17], [1, 2, 3, 4, 6, 7, 11], [1, 2, 3, 8]," +
                " [1, 2, 3, 6, 7, 10, 11], [1, 2, 3, 7, 8], [1, 2, 3, 4, 5, 6, 8, 10, 13]," +
                " [1, 2, 3, 5, 6, 7, 8, 9, 12, 16], [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15]," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 10, 16, 17], [1, 2, 3, 4, 5, 8, 11]," +
                " [1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12], [1, 2, 3, 4, 16], [1, 2, 3, 4, 5, 6], ...]",
                "{[3]=31419, [1, 3]=12349, [1, 2, 3]=9638, [2, 3]=8232, [1, 2, 3, 4, 5, 6, 7, 8, 9]=7495," +
                " [1, 2, 3, 4]=7409, [1, 2, 3, 4, 5, 6, 7, 8]=7163, [1, 2, 3, 4, 5, 6, 7]=6981," +
                " [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]=6925, [1, 2, 3, 4, 5]=6673}",
                8.632959000000918
        );
        subsetsWithElement_helper(
                2,
                0,
                repeat(1),
                "[[0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0], [0, 1], [0, 1], [0], [0], [0, 1], [0, 1], [0, 1]," +
                " [0], [0, 1], [0], [0], [0, 1], [0, 1], [0], ...]",
                "{[0, 1]=500875, [0]=499125}",
                1.5008749999899669
        );
        subsetsWithElement_helper(
                5,
                -5,
                repeat(1),
                "[[-5, 1], [-5, 1], [-5, 1], [-5, 1], [-5, 1], [-5], [-5, 1], [-5], [-5, 1], [-5, 1], [-5, 1]," +
                " [-5, 1], [-5, 1], [-5, 1], [-5, 1], [-5, 1], [-5], [-5, 1], [-5, 1], [-5, 1], ...]",
                "{[-5, 1]=799806, [-5]=200194}",
                1.799806000002514
        );
        subsetsWithElement_helper(
                32,
                3,
                repeat(1),
                "[[1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3]," +
                " [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], [1, 3], ...]",
                "{[1, 3]=968801, [3]=31199}",
                1.9688010000135663
        );
        subsetsWithElement_fail_helper(5, 0, Collections.emptyList());
        subsetsWithElement_fail_helper(5, 0, Arrays.asList(1, 2, 3));
        subsetsWithElement_fail_helper(1, 0, P.integers());
    }

    private static void stringSubsetsWithChar_char_String_helper(
            int scale,
            char c,
            @NotNull String input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsetsWithChar(c, input)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsetsWithChar_char_String_fail_helper(int scale, char c, @NotNull String input) {
        try {
            P.withScale(scale).stringSubsetsWithChar(c, input);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsetsWithChar_char_String() {
        stringSubsetsWithChar_char_String_helper(
                2,
                'b',
                "abcd",
                "[b, bd, abcd, b, abcd, bd, bd, bd, abc, bc, b, bc, bd, b, b, b, bd, bc, b, bc, ...]",
                "{b=499234, bc=100943, ab=100118, bd=99936, abcd=50039, abc=49991, abd=49945, bcd=49794}",
                1.7505739999822374
        );
        stringSubsetsWithChar_char_String_helper(
                5,
                '#',
                "abcd",
                "[#b, #bd, #abcd, #ab, #, #, #c, #bcd, #abc, #abcd, #bcd, #abcd, #c, #, #acd, #ac, #abcd, #d, #," +
                " #cd, ...]",
                "{#abcd=200356, #=200010, #acd=50436, #bcd=50299, #c=50105, #abd=49926, #a=49925, #b=49902," +
                " #abc=49828, #d=49800}",
                3.0014489999821183
        );
        stringSubsetsWithChar_char_String_helper(
                32,
                ' ',
                "abcd",
                "[ abcd,  abcd,  abcd,  abc,  abcd,  ,  abcd,  ,  abcd,  acd,  c,  c,  abcd,  abcd,  abcd,  abcd," +
                "  abcd,  abcd,  bc,  abcd, ...]",
                "{ abcd=773484,  =31317,  abc=25051,  abd=24894,  bcd=24763,  acd=24732,  a=10032,  b=9969,  d=9935," +
                "  c=9893}",
                4.543944999926795
        );
        stringSubsetsWithChar_char_String_helper(
                2,
                'b',
                "aaaa",
                "[ab, b, ab, ab, ab, ab, ab, ab, ab, b, b, ab, ab, ab, b, ab, ab, b, ab, b, ...]",
                "{ab=500443, b=499557}",
                1.500442999989859
        );
        stringSubsetsWithChar_char_String_helper(
                5,
                '#',
                "aaaa",
                "[#a, #a, #a, #a, #, #, #a, #a, #a, #a, #a, #a, #a, #, #a, #a, #a, #a, #, #a, ...]",
                "{#a=799990, #=200010}",
                1.7999900000025462
        );
        stringSubsetsWithChar_char_String_helper(
                32,
                ' ',
                "aaaa",
                "[ a,  a,  a,  a,  a,  ,  a,  ,  a,  a,  a,  a,  a,  a,  a,  a,  a,  a,  a,  a, ...]",
                "{ a=968683,  =31317}",
                1.9686830000135644
        );
        stringSubsetsWithChar_char_String_helper(
                2,
                'b',
                "Mississippi",
                "[bps, bips, b, bps, bis, bs, b, b, bs, bs, bs, bs, bi, b, bi, b, b, b, b, b, ...]",
                "{b=499907, bs=111412, bi=111205, bis=63811, bp=49643, bips=28080, bps=26542, bip=26154, Mb=24053," +
                " Mbis=12372}",
                1.7701949999823314
        );
        stringSubsetsWithChar_char_String_helper(
                5,
                '#',
                "Mississippi",
                "[#ps, #i, #Mips, #p, #ps, #s, #Mips, #s, #Mis, #s, #Mis, #ips, #, #Mips, #s, #Mips, #, #, #Mis," +
                " #Ms, ...]",
                "{#=199852, #ips=143859, #Mips=132111, #is=113744, #s=82185, #i=81806, #Mis=51927, #ps=38829," +
                " #ip=38579, #p=33990}",
                2.874729999983963
        );
        stringSubsetsWithChar_char_String_helper(
                32,
                ' ',
                "Mississippi",
                "[ Mips,  ips,  Mis,  ips,  ,  s,  Mips,  Mips,  ips,  i,  Mips,  is,  Mis,  Mips,  Mips,  Mips," +
                "  ips,  is,  s,  ips, ...]",
                "{ Mips=679742,  ips=126854,  is=40560,  Mis=32736,  =31294,  s=16993,  i=16988,  ip=11342," +
                "  ps=11221,  p=6667}",
                4.425015999937323
        );
        stringSubsetsWithChar_char_String_fail_helper(1, ' ', "abc");
    }

    private static void stringSubsetsWithChar_char_helper(
            int scale,
            char c,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringSubsetsWithChar(c)));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringSubsetsWithChar_char_fail_helper(int scale, char c) {
        try {
            P.withScale(scale).stringSubsetsWithChar(c);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testStringSubsetsWithChar_char() {
        stringSubsetsWithChar_char_helper(
                2,
                'b',
                "[bε嘩, b, bϡ\u19dc䊿䯏刿罖몱\udd15ﲦ, bᬜKㄾ, b㵏, b㩷, b纫, b䝲坤, b琖, b, b, b\uea45, b\u2b63, b鸅, b," +
                " b\uee1c, bᅺ, b, b䇺, b, ...]",
                "{b=499556, b\uf59a=15, bཛ=14, b釁=14, b嚷=14, b匔=14, b\u20b5=13, b僵=13, b縖=13, b㓬=13}",
                2.0006209999799105
        );
        stringSubsetsWithChar_char_helper(
                5,
                '#',
                "[#\u31e5嘩髽, #肣\udd15, #ᅺᤘ\u2b63\u33b2䇺煖误輮酓鸂鸅됽몱캆\uee1c\uf637, #全\ue9fd, #, #, #覚," +
                " #ሮ尩ꪻ굿\uecf5, #瀵疜컦, #\u061a\u2e94㽖䥔刓嗮壙穨糦鼧픫핀\udd82\uf329ﶼﻧ, #\u0e77慚ꯃ총\udd42," +
                " #\u2293䴻庺槔横靯駆ꎤ퉐\ued0d\uf36b, #ढ, #, #ᑒ䃼拷만, #ͺ\u124e\u2506囀\ue68e, #䔾唯嶂湑猂蹙ꪪ췴턞\uead1ﮍ," +
                " #甧, #, #ᖒ滞\uab6e\ue89b, ...]",
                "{#=200002, #棦=12, #\u2043=11, #农=11, #\ue373=10, #\u32a0=10, #돧=10, #돹=10, #㽚=10, #켓=10}",
                5.005519000008595
        );
        stringSubsetsWithChar_char_helper(
                32,
                ' ',
                "[ ܓᅺሮᤘ\u28de\u2aec\u2b63\u2e24\u33b2㖊㭠㱉䇺䟆䱸全嘩尩泦瀵煖疜覚误輮鄒酓鸂鸅\ua7b1ꪻ\uaaf0굿돘됽띯빅빮짎캆컦" +
                "\ue2da\ue5cb\ue78f\ue8b2\ue9fd\uecf5\uee1c\uf637\uf878聆\uff03," +
                "  B\u0964\u0d5b\u0e77\u1366ᵠ\u2a57\u337d㽿䍾䞂刓嗮徜慚睸糦緜萋詪詵錹鎿鐳颒鰫ꅪꯃ묆쀝쪡총\ud8ca\udcc6\udd42" +
                "\udec6\uf21b\uf329\uf36bﻧ," +
                "  ᯖ\u202c㝏䃼䦣儖匀駆낛띆\ue9c6\uea55ﱓ,  Ȟͺұډ\u0a49ල\u124eᗶ㲜䠬葒蛕ꪪ\ue28c\ue852," +
                "  ᖒ\u2e64㥑㿘䁅䝀䮼偘滞燔趵ꎿ\uab6e년믱젯\ue89b,  ,  ᅒ\u2320\u2606\u32b0㕯婜狙阏陜頵顓ꎚꑼ궥뚗쇻쓎쟒쳮,  ," +
                "  \u0d47\u0f1bᓑ\u2eea㚏䢗帇晸濙犋瞁诨鈖ꤥ갇댲죴쿕퀯퓰\uebc7\uede9\uf800,  а\u2684\u2b83梏葆ꒂ팗풬\ue40c,  궺," +
                "  羾뗢," +
                "  \u073d\u0aa9ஆ\u0b8c\u1069ቾ\u1739គᥔ\u19b1\u1a60\u1aba\u1b56ᳯᶝ\u2026\u22a3\u2a33\u2af8\u2f0d" +
                "\u3243\u3291㥐㭍㻶㼰㾸䅁䈏䐄䝨䦼䨺\u4df4乃侒偰剹厖叵呼団姕嬵悲挑摡枧柿桶泈狚睗窀箴糘茞蕕閦顆飰館鳙鳸\ua6f4Ꜣ" +
                "\ua7afꯈ굲냥놂닁듂뼄뾁쀁쁳쑹운쥇즓챲춮칗탰헑\ud845\ude93\ue10d\ue480\ue531\ue649\ue672\uecda\ued1a\uf27f\uf28d" +
                "\uf351\uf3a8ﱜﱷ,  ʵۮܒ\u07aa\u0875\u17cbᰁ\u1fce\u25a0\u291e\u2e50\u2fbeㆲ㗨㙿㢍䒁䒉嘟廏民滎瀉爑甍皑秳稔臙" +
                "艸芦藟詂謳雩頔\ua837놿뇐멖뮣벧솄톋퍆\udde1\ue22c\uecda\uee3a\uee84\uf6cd," +
                "  K\u0529Ꮃᶑ㓆䌚䜐卩嘂壵操显禎细鏌阘鳰Ꜯ\ua9f4갭넀쉙쬋쵕춢후\uda6c\ude3a\ue61e\ue9d5\uf04f\uf6a5\ufe3d," +
                "  7Ϯ\u0c00\u0f24ნᏲᚅᬱ\u1deb\u2153ぜど㖃㥥㴯㺸䌕䎌䜜則勪懜戎担敉杁欎溯潵焣穣笨绡缂耒葑頒鱼鲦齞걓겧뎀렔몔씅익쯹츪클" +
                "퇢횋\udd06\uddd7\ude35\ue266\ue47a\ue498\ue59b\uf7cd串聾ﶯＵ,  伺ꜰ쾭\ud847\uef98," +
                "  \u0361\u0cecᒚ\u17cd\u21ba\u2818\u2b5dⲄⴋⴓ㺱㻞巶忢憊椼檌檮滙玌璎疟祍蠛裤贲ꚸ뤀먛\ud85c\udbf4\uddcf\uec15," +
                "  溑좲햽,  Ⴃ\u2572\u2fb7䳰嬐涫跁뼴솔턕퐗\ue103\ueea7ﰀ, ...]",
                "{ =31331,  ᴅ=6,  \u29fb=5,  뙺=5,  དྷ=5,  ᝰ=5,  \ue3dd=4,  筞=4,  \ufff9=4,  勡=4}",
                32.01744900002343
        );
        stringSubsetsWithChar_char_fail_helper(1, ' ');
    }

    private static void listsWithSublists_helper(
            int scale,
            @NotNull Iterable<List<Integer>> sublists,
            @NotNull Iterable<Integer> xs,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<List<Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).listsWithSublists(sublists, xs))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(List::size, sample))), meanSize);
        P.reset();
    }

    private static void listsWithSublists_fail_helper(
            int scale,
            @NotNull Iterable<List<Integer>> sublists,
            @NotNull Iterable<Integer> input
    ) {
        try {
            toList(P.withScale(scale).listsWithSublists(sublists, input));
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testListsWithSublists() {
        listsWithSublists_helper(
                2,
                P.uniformSample(Collections.singletonList(Collections.emptyList())),
                P.uniformSample(Arrays.asList(4, 5, 6)),
                "[[5, 4, 5, 5, 5, 6, 5, 6, 4, 6, 5], [4, 6, 6, 5], [], [4, 6, 5], [], [6, 4, 6], [6, 6], [6], []," +
                " [], [], [], [], [5], [6, 6, 5, 6], [4, 5], [5, 6, 5, 4, 5, 4, 6, 5, 6], [5, 6, 6], [], [], ...]",
                "{[]=249571, [5]=83609, [6]=83425, [4]=82914, [5, 5]=21160, [4, 5]=20988, [4, 4]=20910," +
                " [6, 4]=20897, [5, 4]=20876, [6, 5]=20855}",
                2.0017519999875453
        );
        listsWithSublists_helper(
                16,
                P.uniformSample(Collections.singletonList(Collections.emptyList())),
                P.uniformSample(Arrays.asList(4, 5, 6)),
                "[[6, 5, 6, 4, 6, 6, 6, 5, 6, 6, 4, 5, 4, 6, 6, 4, 4, 6, 4, 4, 6, 4, 4, 6, 6, 5, 4, 5, 5, 4, 6, 5," +
                " 6, 5], [6, 6, 5, 6, 6, 6, 6, 5, 4, 6, 5, 6, 4, 6, 6, 6, 4, 5, 5, 6, 6, 4, 5, 6], [4, 4, 6, 6, 5]," +
                " [6, 6, 6, 5, 4, 4, 6, 5, 5, 4, 6, 5, 6, 6, 4, 5, 4, 4, 5, 5, 4, 6, 6], [4, 5, 5, 4, 4, 4, 6, 5]," +
                " [4, 6, 6], [5, 6, 6, 6, 4], [4, 4, 6, 5, 5, 6, 4, 5, 4, 5], [4, 4, 6, 4, 4, 6]," +
                " [6, 6, 5, 6, 6, 5, 4]," +
                " [5, 5, 5, 6, 5, 5, 5, 6, 6, 6, 5, 6, 6, 4, 5, 4, 4, 5, 4, 4, 6, 6, 4, 6, 5, 4, 6, 5, 4, 6, 4, 6," +
                " 6, 5, 5, 4, 4, 4, 5, 5, 4, 6, 5]," +
                " [4, 4, 6, 5, 4, 4, 4, 6, 5, 5, 4, 5, 5, 4, 6, 6, 6, 6, 5, 6, 5]," +
                " [6, 5, 5, 6, 5, 4, 5, 5, 5, 6, 5, 4, 6, 6, 5, 4, 6, 4, 5]," +
                " [6, 6, 5, 4, 6, 4, 5, 6, 4, 4, 5, 6, 5, 6, 6, 5, 6, 4, 6, 5, 4, 4, 6, 5, 6, 6]," +
                " [6, 5, 5, 6, 5, 5]," +
                " [5, 4, 5, 4, 4, 6, 4, 5, 5, 4, 4, 4, 4, 5, 6, 5, 6, 4, 5, 6, 5, 5, 6, 6, 5, 6, 5, 4, 6, 5, 4, 5," +
                " 4, 6, 6, 4, 5, 4, 5, 4, 6], [4, 4, 4, 6, 4, 6, 6, 5, 6, 5, 4, 6], [5, 4, 6, 5, 6, 6, 5, 6, 4]," +
                " [4, 6, 4, 4, 5, 5, 4, 5, 6, 5, 5, 4, 4], [4, 5, 5, 6, 5, 6, 5, 4, 4, 4, 4, 6, 4], ...]",
                "{[]=12333, [6]=7348, [5]=7330, [4]=7320, [5, 6]=3371, [4, 6]=3309, [6, 4]=3282, [5, 4]=3267," +
                " [5, 5]=3247, [4, 4]=3245}",
                15.983893999996807
        );
        listsWithSublists_helper(
                2,
                P.uniformSample(Collections.singletonList(Arrays.asList(1, 2, 3))),
                P.uniformSample(Arrays.asList(4, 5, 6)),
                "[[5, 4, 5, 1, 2, 3, 5, 5, 6, 5, 6, 4, 6, 5], [4, 6, 1, 2, 3, 6, 5], [1, 2, 3], [4, 6, 1, 2, 3, 5]," +
                " [1, 2, 3], [6, 1, 2, 3, 4, 6], [6, 1, 2, 3, 6], [1, 2, 3, 6], [1, 2, 3], [1, 2, 3], [1, 2, 3]," +
                " [1, 2, 3], [1, 2, 3], [5, 1, 2, 3], [6, 6, 5, 6, 1, 2, 3], [4, 5, 1, 2, 3]," +
                " [5, 6, 5, 4, 5, 4, 1, 2, 3, 6, 5, 6], [5, 6, 6, 1, 2, 3], [1, 2, 3], [1, 2, 3], ...]",
                "{[1, 2, 3]=249571, [1, 2, 3, 5]=41857, [6, 1, 2, 3]=41798, [5, 1, 2, 3]=41752, [1, 2, 3, 6]=41627," +
                " [1, 2, 3, 4]=41496, [4, 1, 2, 3]=41418, [5, 5, 1, 2, 3]=7145, [4, 5, 1, 2, 3]=7142," +
                " [5, 1, 2, 3, 5]=7079}",
                5.001751999985147
        );
        listsWithSublists_helper(
                16,
                P.uniformSample(Collections.singletonList(Arrays.asList(1, 2, 3))),
                P.uniformSample(Arrays.asList(4, 5, 6)),
                "[[6, 5, 6, 4, 6, 6, 6, 5, 6, 6, 4, 5, 4, 6, 6, 4, 4, 6, 4, 4, 6, 4, 4, 6, 6, 5, 4, 5, 5, 4, 6, 1," +
                " 2, 3, 5, 6, 5], [6, 6, 5, 6, 6, 6, 6, 5, 4, 6, 5, 6, 4, 6, 6, 6, 4, 5, 5, 6, 6, 4, 5, 1, 2, 3, 6]," +
                " [1, 2, 3, 4, 4, 6, 6, 5]," +
                " [6, 6, 6, 5, 4, 4, 6, 5, 5, 4, 6, 5, 6, 1, 2, 3, 6, 4, 5, 4, 4, 5, 5, 4, 6, 6]," +
                " [4, 5, 5, 1, 2, 3, 4, 4, 4, 6, 5], [4, 6, 1, 2, 3, 6], [5, 6, 1, 2, 3, 6, 6, 4]," +
                " [4, 4, 6, 5, 5, 6, 4, 5, 1, 2, 3, 4, 5], [4, 4, 6, 4, 4, 1, 2, 3, 6]," +
                " [1, 2, 3, 6, 6, 5, 6, 6, 5, 4]," +
                " [5, 5, 5, 6, 5, 5, 5, 6, 6, 6, 5, 6, 6, 4, 5, 4, 4, 5, 4, 4, 6, 6, 4, 6, 5, 4, 6, 5, 4, 6, 4, 6," +
                " 6, 5, 5, 4, 4, 4, 5, 5, 4, 6, 5, 1, 2, 3]," +
                " [4, 4, 6, 5, 1, 2, 3, 4, 4, 4, 6, 5, 5, 4, 5, 5, 4, 6, 6, 6, 6, 5, 6, 5]," +
                " [6, 5, 5, 6, 1, 2, 3, 5, 4, 5, 5, 5, 6, 5, 4, 6, 6, 5, 4, 6, 4, 5]," +
                " [6, 6, 5, 4, 6, 4, 5, 6, 4, 4, 5, 6, 5, 6, 1, 2, 3, 6, 5, 6, 4, 6, 5, 4, 4, 6, 5, 6, 6]," +
                " [6, 5, 5, 6, 5, 1, 2, 3, 5]," +
                " [5, 4, 5, 4, 4, 6, 4, 5, 5, 4, 4, 4, 4, 5, 6, 5, 6, 4, 5, 6, 5, 5, 6, 6, 5, 6, 5, 4, 6, 1, 2, 3," +
                " 5, 4, 5, 4, 6, 6, 4, 5, 4, 5, 4, 6], [4, 1, 2, 3, 4, 4, 6, 4, 6, 6, 5, 6, 5, 4, 6]," +
                " [5, 4, 6, 5, 6, 1, 2, 3, 6, 5, 6, 4], [4, 6, 4, 4, 5, 5, 4, 1, 2, 3, 5, 6, 5, 5, 4, 4]," +
                " [4, 5, 5, 6, 5, 6, 5, 4, 4, 4, 4, 6, 4, 1, 2, 3], ...]",
                "{[1, 2, 3]=12333, [1, 2, 3, 6]=3709, [4, 1, 2, 3]=3694, [5, 1, 2, 3]=3682, [1, 2, 3, 5]=3648," +
                " [6, 1, 2, 3]=3639, [1, 2, 3, 4]=3626, [5, 1, 2, 3, 6]=1151, [4, 1, 2, 3, 5]=1147," +
                " [4, 1, 2, 3, 6]=1140}",
                18.983894000012278
        );
        listsWithSublists_helper(
                2,
                map(i -> Arrays.asList(i, i), P.naturalIntegersGeometric()),
                P.naturalIntegersGeometric(),
                "[[17, 47, 25, 0, 0, 17], [21, 21, 8], [13, 13], [11, 11], [17, 10, 10, 5], [77, 77]," +
                " [19, 70, 17, 17, 52, 6], [22, 16, 6, 6], [8, 0, 41, 41, 6, 15], [20, 20], [23, 23, 22, 9]," +
                " [83, 32, 32, 27], [2, 41, 41], [1, 12, 12, 20], [45, 45, 33]," +
                " [25, 14, 12, 2, 24, 24, 37, 37, 2, 15, 15], [118, 49, 46, 54, 54], [15, 15, 0, 90, 31, 31]," +
                " [18, 18], [28, 19, 36, 36], ...]",
                "{[0, 0]=7655, [1, 1]=7389, [2, 2]=6970, [3, 3]=6785, [4, 4]=6667, [5, 5]=6585, [6, 6]=6124," +
                " [7, 7]=6065, [8, 8]=5847, [9, 9]=5823}",
                4.001453999968074
        );
        listsWithSublists_helper(
                16,
                map(i -> Arrays.asList(i, i), P.naturalIntegersGeometric()),
                P.naturalIntegersGeometric(),
                "[[47, 25, 0, 19, 21, 9, 13, 12, 20, 10, 7, 77, 19, 70, 17, 53, 6, 24, 16, 6, 9, 0, 41, 9, 15, 20," +
                " 23, 24, 9, 84, 32, 28, 28, 2], [32, 3, 12, 21, 45, 34, 28, 14, 12, 2, 24, 24, 37, 37, 2, 15]," +
                " [3, 118, 49, 46, 54, 19, 15, 0, 90, 31, 19, 31, 19, 36, 44, 43, 168, 18, 7, 3, 3, 22, 13, 33, 49," +
                " 61, 50, 14, 0, 8, 121, 23, 122, 19, 75, 14, 6, 16, 51, 10, 7, 31, 9, 18, 12, 17, 1, 73, 9]," +
                " [10, 21, 3, 55, 16, 35, 10, 64, 12, 48, 24, 170, 60, 21, 32, 22, 34, 0, 29, 91, 34, 21, 23, 100," +
                " 100, 0, 126, 0, 11, 16, 0, 29, 7], [13, 22, 120, 2, 30, 36, 11, 11, 29, 10]," +
                " [2, 33, 33, 10, 14, 18], [34, 33, 33, 52, 29, 43, 4]," +
                " [37, 25, 43, 89, 20, 30, 41, 41, 24, 15, 7, 18]," +
                " [14, 21, 106, 0, 62, 94, 16, 8, 1, 5, 5, 40, 4, 17, 3, 25]," +
                " [26, 25, 27, 80, 53, 53, 21, 25, 40, 44], [5, 11, 11, 4, 47, 28, 33], [46, 7, 68, 68, 59, 35]," +
                " [19, 19, 67, 21, 47, 7, 7, 45, 4, 37, 20, 17, 12, 19], [1, 1, 38], [43, 43]," +
                " [1, 33, 24, 33, 17, 6, 5, 9, 11, 31, 40, 42, 35, 0, 14, 27, 4, 6, 6, 27, 28, 34, 2, 50]," +
                " [9, 4, 32, 11, 37, 18, 31, 28, 84, 44, 8, 30, 57, 19, 27, 25, 23, 12, 4, 7, 60, 76, 3, 39, 23, 3," +
                " 11, 26, 33, 15, 20, 2, 7, 102, 8, 41, 89, 45, 42, 42, 31, 64], [43, 11, 0, 0, 12, 2, 28, 12]," +
                " [23, 117, 4, 96, 96, 22, 5, 6, 18, 1, 74, 6, 136, 55, 31, 17, 14, 18, 30, 38, 19, 4]," +
                " [2, 30, 32, 32, 46, 112, 44, 24, 7, 19, 50], ...]",
                "{[0, 0]=377, [1, 1]=367, [2, 2]=350, [4, 4]=331, [5, 5]=319, [9, 9]=300, [7, 7]=297, [6, 6]=297," +
                " [3, 3]=296, [11, 11]=286}",
                17.99236900000833
        );
        listsWithSublists_fail_helper(
                1,
                P.uniformSample(Collections.singletonList(Arrays.asList(1, 2, 3))),
                P.uniformSample(Arrays.asList(4, 5, 6))
        );
    }

    private static void stringsWithSubstrings_Iterable_String_String_helper(
            int scale,
            @NotNull Iterable<String> substrings,
            @NotNull String s,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsWithSubstrings(substrings, s))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsWithSubstrings_Iterable_String_String_fail_helper(
            int scale,
            @NotNull Iterable<String> substrings,
            @NotNull String s
    ) {
        try {
            P.withScale(scale).stringsWithSubstrings(substrings, s);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void stringsWithSubstrings_Iterable_String_String() {
        stringsWithSubstrings_Iterable_String_String_helper(
                2,
                P.uniformSample(Collections.singletonList("")),
                charsToString(range('a', 'z')),
                "[vyfvrgpbwvv, , xl, sew, f, d, fs, w, k, , , , , , djvs, g, av, ftlszpdrxg, f, zclu, ...]",
                "{=249881, q=9816, f=9774, u=9750, t=9732, j=9712, d=9696, w=9692, i=9691, p=9668}",
                2.0001649999875575
        );
        stringsWithSubstrings_Iterable_String_String_helper(
                16,
                P.uniformSample(Collections.singletonList("")),
                charsToString(range('a', 'z')),
                "[dxcfpostgwymkoqiyyeyotsdplrqjvsvgt, gojhcctlgszydxlcxgkgelvkbkg, agtpw, okjmsjrmsvozsioareerp," +
                " uzjcwsyx, kaop, ovg, typuhphphyl, rzxi, pemdgqck, gwvszug," +
                " rpbjcvjncsdbshwqbianyugwexqwzuwiorxpqaurzud, iysydesxrntijrexskkgb, zngnchfandrrtveggjh, ," +
                " onmlyndxhytyrwjceojqygvmgnm, ojllrhj, vyvigafvheyhdutbcjkypxvsfdjkfpxvgsatnqvucva," +
                " meklvozmwrumtby, sxopuashqudb, ...]",
                "{=12409, j=907, b=904, o=885, q=884, x=877, e=874, z=867, d=866, g=864}",
                15.980978999996648
        );
        stringsWithSubstrings_Iterable_String_String_helper(
                2,
                P.uniformSample(Collections.singletonList("cat")),
                charsToString(range('a', 'z')),
                "[vyfcatvrgpbwvv, cat, catxl, scatew, fcat, catd, fcats, wcat, catk, cat, cat, cat, cat, cat," +
                " dcatjvs, catg, avcat, ftlszpcatdrxg, fcat, zclcatu, ...]",
                "{cat=249881, tcat=4961, catw=4949, fcat=4919, qcat=4918, catb=4900, catr=4899, catq=4898," +
                " icat=4897, caty=4895}",
                5.000164999985253
        );
        stringsWithSubstrings_Iterable_String_String_helper(
                16,
                P.uniformSample(Collections.singletonList("cat")),
                charsToString(range('a', 'z')),
                "[dxcfpostgwymkoqiyyeyotsdplrqjvscatvgt, gojhcctlgszydxlcxgkgelvkbkcatg, catagtpw," +
                " okjmsjrmsvozsicatoareerp, uzjcwscatyx, kcataop, ocatvg, typcatuhphphyl, rzxcati, pemdgqcatck," +
                " catgwvszug, rpbjcvjncsdbshwqbianyugwexqwzuwiorxpqaurzucatd, iyscatydesxrntijrexskkgb," +
                " zngnchcatfandrrtveggjh, cat, onmlyndxhytyrwjcatceojqygvmgnm, ojllcatrhj," +
                " vyvigafvheyhdutbcjkypxvsfdjkcatfpxvgsatnqvucva, meklvozmwrumtcatby, sxopucatashqudb, ...]",
                "{cat=12409, xcat=469, jcat=463, cato=459, qcat=455, bcat=453, catz=452, dcat=452, catb=451," +
                " ecat=445}",
                18.980979000012418
        );
        stringsWithSubstrings_Iterable_String_String_helper(
                2,
                map(f -> Float.toString(f), P.floats()),
                charsToString(range('0', '9')),
                "[585-8.745077E295161655, -3.8562423E-20, 72-5.418378E2546, 5-3.31379009E12, 31.36775321E17," +
                " 23-2.763534E-21, 8-57.728504, -3.87529152E8, 1.2088307E-37, 5.0185977E28, 3.8192072E-283," +
                " 9528.22823E-356, 056.5493657E-7532913, 725.2670996E7, 9273.655872E36, -9.5261086E-35," +
                " -2.3683897E32, 15.7468652, 298-2.1571468E-19, 76652.009171606, ...]",
                "{-1.1123897E-17=2, 3.703E-5=2, -1.99528348E18=2, -1.5117249E-35=2, -4080635.8=2, 1.6368128E27=2," +
                " 4.695226=2, 4.523417E27=2, -6.4474385E-16=2, 2.6752413E20=2}",
                14.31768299994003
        );
        stringsWithSubstrings_Iterable_String_String_helper(
                16,
                map(f -> Float.toString(f), P.floats()),
                charsToString(range('0', '9')),
                "[37252366808884832310952569605631.0719636E1669722362983727664516060344," +
                " 06-2.56170422E14929125928241219, 01-4.4911137E-3049926282, 4.0917575E-1153, 07.3894236E-319," +
                " 564.5286633E30385, 4777865-4.2354097E-23, 8793.1971366E-284360, 20-3.07368407E176652946," +
                " 119259223127601808466470694681700419410343-9.547804E-48, 83427138914726172535-5.7034625E-149," +
                " 50311354669700114.3459432E21837783816994, 249086563492.68999117E99," +
                " 179-8.955546E37585860557487343129875253925, 57562030542507.82684E-21459614384018," +
                " 27422-1.5677651E3802704, 4769-1.1446778E-4153, 7196164088990692-3.8460768E28738," +
                " 467508-4.4292383E-31260559736943308616284817, 9752366342.789954737433859214120, ...]",
                "{37252366808884832310952569605631.0719636E1669722362983727664516060344=1," +
                " 06-2.56170422E14929125928241219=1, 01-4.4911137E-3049926282=1, 4.0917575E-1153=1," +
                " 07.3894236E-319=1, 564.5286633E30385=1, 4777865-4.2354097E-23=1, 8793.1971366E-284360=1," +
                " 20-3.07368407E176652946=1, 119259223127601808466470694681700419410343-9.547804E-48=1}",
                28.306307000011344
        );
        stringsWithSubstrings_Iterable_String_String_fail_helper(
                1,
                P.uniformSample(Collections.singletonList("cat")),
                charsToString(range('a', 'z'))
        );
    }

    private static void stringsWithSubstrings_Iterable_String_helper(
            int scale,
            @NotNull Iterable<String> substrings,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanSize
    ) {
        List<String> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.withScale(scale).stringsWithSubstrings(substrings))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(String::length, sample))), meanSize);
        P.reset();
    }

    private static void stringsWithSubstrings_Iterable_String_fail_helper(
            int scale,
            @NotNull Iterable<String> substrings
    ) {
        try {
            P.withScale(scale).stringsWithSubstrings(substrings);
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void stringsWithSubstrings_Iterable_String() {
        stringsWithSubstrings_Iterable_String_helper(
                2,
                P.uniformSample(Collections.singletonList("")),
                "[ε䊿\u2538\udd15몱ﲦ䯏ϡ罖\u19dc刿ㄾ, ᬜK㵏㩷, 纫䝲, 坤琖\uea45, , \u2b63鸅, \uee1c, ᅺ䇺, 㖊, , , , , ," +
                " \uff03尩굿\uecf5, \ue8b2, 䟆㭠瀵, 쪅右䦋\u2832ﭙ빜, 홃祝몷ࢦ\ufdd7, \u0c55壙\udd82, ...]",
                "{=249337, ᓈ=14, 瘍=14, ᾥ=14, 箚=13, 餦=13, 㐏=13, 縞=13, 攻=13, 䬋=13}",
                2.002274999987558
        );
        stringsWithSubstrings_Iterable_String_helper(
                16,
                P.uniformSample(Collections.singletonList("")),
                "[\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒\uff03" +
                "尩, 瀵컦刓嗏\u3353糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ\udcc6," +
                " 駆퉐庺\u2293䃼匀낛띆, ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხ\uab6e䝀㥑\u2e64년믱젯䁅偘," +
                " 㙴ᶙ䁩聂\uf518\ue2d7劏嘨, 훀쳮陜, 쓎頵\u2606, 旞\u2613\u19de죴\u0d47㚏帇퀯\uebc7晸犋鈖, ᓑ濙诨갇," +
                " 梏\u2684\ue40c\u2b83葆, 뗢撻," +
                " 濆엶䩵ᣞ\ud992\u2f79滔헑䈏닁\ue649គ姕\u1069\u2f0d듂狚\ue672団䅁悲枧\u1b56偰摡泈\u1a60㭍\u2af8운\u2026桶뼄" +
                "ቾᶝ睗㥐厖剹ᥔ㻶\uf3a8춮茞\ue531칗ᳯ\u073d飰\ue480, \u3243\u4df4\u2a33䨺館," +
                " 糘ﱜ\u22a3䐄굲ﱷ\u3291\uf28d즓\uf27f䝨雩\uecb1ᡄ\ude17ᒲ蕪逊兆묡訾ꉹ\uaa4d藆\ue34d\uf5a2," +
                " \ue2c5哠甍뮣民皑\u291e秳ʵ솄퍆芦瀉벧, \uee3aۮ\uf6cd\ue22c\u2fbe톋艸操샣墺貗\u1c47\uf2ffㆸⱳ," +
                " 䌚\ufe3d춢후Ꜯ卩鳰阘细\ue9d5\ude3a显鏌㓆갭禎\u0529K쬋," +
                " ᬱ뭇昺픕\u23b8ᆵ䨨\ueb1c\u0966儠씅潵겧\u0f24㺸則穣클䜜걓绡缂敉勪\ue498溯7익Ᏺ㥥㖃," +
                " \ue59b聾ﶯ\uddd7ぜＵნ\ue266耒뎀\u2153\uf7cdꜰ耕詴\ud847\uef98," +
                " 檌裤㻞椼憊ⴋ\u21ba\uec15檮滙\u0cec\u0361ꚸ璎祍忢\u17cd, ...]",
                "{=12385, \uef01=5, \u24cb=5, 濸=5, 黢=4, \u197d=4, 쁃=4, 졆=4, 竬=4, 熔=4}",
                15.98473899999724
        );
        stringsWithSubstrings_Iterable_String_helper(
                2,
                P.uniformSample(Collections.singletonList("cat")),
                "[ε䊿\u2538cat\udd15몱ﲦ䯏ϡ罖\u19dc刿ㄾ, ᬜK㵏cat㩷, 纫cat䝲, 坤琖cat\uea45, cat, \u2b63cat鸅, \uee1ccat," +
                " ᅺcat䇺, cat㖊, cat, cat, cat, cat, cat, \uff03cat尩굿\uecf5, \ue8b2cat, 䟆cat㭠瀵," +
                " cat쪅右䦋\u2832ﭙ빜, 홃祝몷ࢦ\ufdd7cat, \u0c55cat壙\udd82, ...]",
                "{cat=249337, \u2863cat=10, cat휵=10, cat鯖=10, \uf84acat=10, 傄cat=10, cat놼=9, ᓈcat=9, cat餦=9," +
                " 㐏cat=9}",
                5.002274999985167
        );
        stringsWithSubstrings_Iterable_String_helper(
                16,
                P.uniformSample(Collections.singletonList("cat")),
                "[\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒" +
                "\uff03cat尩, 瀵컦刓嗏\u3353cat糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ\udcc6," +
                " 駆퉐庺\u2293cat䃼匀낛띆, ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხcat\uab6e䝀㥑\u2e64년믱젯䁅偘," +
                " 㙴ᶙ䁩聂cat\uf518\ue2d7劏嘨, cat훀쳮陜, 쓎cat頵\u2606, 旞\u2613\u19decat죴\u0d47㚏帇퀯\uebc7晸犋鈖," +
                " ᓑ濙cat诨갇, 梏\u2684\ue40c\u2b83葆cat, 뗢撻cat," +
                " 濆엶䩵ᣞ\ud992\u2f79滔cat헑䈏닁\ue649គ姕\u1069\u2f0d듂狚\ue672団䅁悲枧\u1b56偰摡泈\u1a60㭍\u2af8운\u2026桶" +
                "뼄ቾᶝ睗㥐厖剹ᥔ㻶\uf3a8춮茞\ue531칗ᳯ\u073d飰\ue480," +
                " \u3243\u4df4\u2a33䨺cat館," +
                " 糘ﱜ\u22a3䐄굲ﱷ\u3291\uf28d즓\uf27f䝨雩\uecb1ᡄ\ude17ᒲ蕪逊兆묡cat訾ꉹ\uaa4d藆\ue34d\uf5a2," +
                " \ue2c5哠甍뮣民皑\u291e秳ʵ솄퍆芦瀉벧cat, cat\uee3aۮ\uf6cd\ue22c\u2fbe톋艸操샣墺貗\u1c47\uf2ffㆸⱳ," +
                " 䌚\ufe3d춢후Ꜯ卩鳰阘细\ue9d5\ude3a显鏌㓆갭cat禎\u0529K쬋," +
                " ᬱ뭇昺cat픕\u23b8ᆵ䨨\ueb1c\u0966儠씅潵겧\u0f24㺸則穣클䜜걓绡缂敉勪\ue498溯7익Ᏺ㥥㖃," +
                " \ue59b聾ﶯ\uddd7ぜＵნ\ue266耒뎀\u2153\uf7cdꜰ耕詴cat\ud847\uef98," +
                " 檌裤㻞椼憊ⴋ\u21ba\uec15檮滙\u0ceccat\u0361ꚸ璎祍忢\u17cd, ...]",
                "{cat=12385, cat竬=4, 헣cat=4, \u24cbcat=4, औcat=4, \u4dffcat=3, catᏑ=3, 黢cat=3, 縁cat=3, 㒓cat=3}",
                18.984739000011526
        );
        stringsWithSubstrings_Iterable_String_helper(
                2,
                map(f -> Float.toString(f), P.floats()),
                "[ε䊿\u2538-4.730412E-34\udd15몱ﲦ䯏ϡ罖\u19dc刿, -3.041954E25ᬜK, 㩷ⴿ2.0586195E-32䝲\uf207," +
                " 坤1.841686E31\uea45, 1.5387214E38\u2b63, 鸅1.1917704, 0.016748283ᅺ, 䇺2.681261E36㖊, 7.741398E-26," +
                " -1.467987E37, -2.81001E34, 7.645123E-18, 尩굿\uecf5ꪻ-1.7399366E-8, 143097.08, 1.6778092E-13㭠," +
                " 刓1.0719636E16쪅右䦋\u2832, 䫯-1.7012438E-6홃祝몷, -6.0346965E28, \u0c554.3328808E-37壙," +
                " 63701.168\u2e94, ...]",
                "{3.3475204E22=2, -4.16071123E11=2, 4.523417E27=2, -170.97168=2, 3.37845E-11=2, -10.269147=2," +
                " -5.751692E-14=2, ε䊿\u2538-4.730412E-34\udd15몱ﲦ䯏ϡ罖\u19dc刿=1, -3.041954E25ᬜK=1," +
                " 㩷ⴿ2.0586195E-32䝲\uf207=1}",
                14.31644599993982
        );
        stringsWithSubstrings_Iterable_String_helper(
                16,
                map(f -> Float.toString(f), P.floats()),
                "[\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒" +
                "\uff03-2.5324939E30尩," +
                " 瀵컦刓嗏\u33532.8102187E-19糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ," +
                " 駆퉐庺\u2293\ued0d6.2105025E-19䃼匀낛띆, ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხ-7.5414853\uab6e䝀" +
                "㥑\u2e64년믱젯䁅偘, 㙴ᶙ䁩聂3.5566916\uf518\ue2d7劏, \ue54a-0.009772067훀쳮, 쓎-3.06893264E15頵\u2606," +
                " 旞\u2613\u19de1.5354367E-16죴\u0d47㚏帇퀯\uebc7晸犋, ᓑ濙䢗-1.8859948E34," +
                " 梏\u2684\ue40c\u2b83葆а팗4.1738653E-4뗢, -3.07368407E17濆엶䩵ᣞ\ud992\u2f79滔," +
                " 헑䈏닁\ue649គ姕\u1069\u2f0d듂狚\ue672団䅁悲枧\u1b56偰摡泈\u1a60㭍\u2af8운\u2026桶뼄ቾᶝ睗㥐厖剹ᥔ㻶\uf3a8춮茞" +
                "\ue531칗ᳯ\u073d飰\ue4804.01656349E14\u3243\u4df4\u2a33," +
                " 館1.0064398E30糘ﱜ\u22a3䐄굲ﱷ\u3291\uf28d즓\uf27f䝨雩\uecb1ᡄ\ude17ᒲ蕪逊兆," +
                " 訾ꉹ\uaa4d藆\ue34d\uf5a2됇3733098.5\ue2c5哠甍뮣民皑\u291e秳ʵ솄퍆芦瀉," +
                " 滎-6.043743E10\uee3aۮ\uf6cd\ue22c\u2fbe톋艸操샣墺貗\u1c47\uf2ffㆸⱳ," +
                " 䌚\ufe3d춢후Ꜯ卩鳰阘细\ue9d5\ude3a显鏌㓆갭-6.3691853E8禎\u0529K쬋," +
                " ᬱ뭇昺1.7043144E24픕\u23b8ᆵ䨨\ueb1c\u0966儠씅潵겧\u0f24㺸則穣클䜜걓绡缂敉勪\ue498溯7익Ᏺ㥥㖃," +
                " \ue59b聾ﶯ\uddd7ぜＵნ\ue266耒뎀\u2153\uf7cdꜰ耕詴6.117129E-31\ud847\uef98," +
                " 檌裤㻞椼憊ⴋ\u21ba\uec15檮滙\u0cec-1.7406041E25\u0361ꚸ璎祍忢," +
                " 좲햽퐗僮-3.7339023E-30ﰀ\u2572\ueea7䳰뼴Ⴃ跁涫, ...]",
                "{\u2b63\uf637鸂鸅误輮\uee1c\u33b2酓캆ᅺ됽煖䇺ᤘ\ue9fd\u2aec㖊짎\uaaf0全覚돘䱸\u28de\u2e24\uf878ሮܓ鄒\uff03" +
                "-2.5324939E30尩=1," +
                " 瀵컦刓嗏\u33532.8102187E-19糦嗮\uf329ﻧ\udd42䞂鎿鐳鰫묆颒錹睸ꯃ慚총\u0e77\uf36bB㽿\u2a57緜\udec6ꅪ=1," +
                " 駆퉐庺\u2293\ued0d6.2105025E-19䃼匀낛띆=1," +
                " ͺ\u124eꪪ\u0a49䠬㲜\ue852ډұ\ue28c葒ලȞ蛕䮼ხ-7.5414853\uab6e䝀㥑\u2e64년믱젯䁅偘=1," +
                " 㙴ᶙ䁩聂3.5566916\uf518\ue2d7劏=1, \ue54a-0.009772067훀쳮=1, 쓎-3.06893264E15頵\u2606=1," +
                " 旞\u2613\u19de1.5354367E-16죴\u0d47㚏帇퀯\uebc7晸犋=1, ᓑ濙䢗-1.8859948E34=1," +
                " 梏\u2684\ue40c\u2b83葆а팗4.1738653E-4뗢=1}",
                28.30002600001155
        );
        stringsWithSubstrings_Iterable_String_fail_helper(1, P.uniformSample(Collections.singletonList("cat")));
    }

    private static void maps_helper(@NotNull String keys, @NotNull Iterable<Integer> values, @NotNull String output) {
        List<Map<Integer, Integer>> sample = toList(
                take(DEFAULT_SAMPLE_SIZE, P.maps(readIntegerListWithNulls(keys), values))
        );
        aeqitLimit(TINY_LIMIT, sample, output);
        P.reset();
    }

    @Test
    public void testMaps() {
        maps_helper("[5]", P.naturalIntegersGeometric(),
                "[{5=19}, {5=47}, {5=25}, {5=0}, {5=19}, {5=21}, {5=9}, {5=13}, {5=12}, {5=20}, {5=10}, {5=7}," +
                " {5=77}, {5=19}, {5=70}, {5=17}, {5=53}, {5=6}, {5=24}, {5=16}, ...]"
        );
        maps_helper("[1, 2, 3]", P.naturalIntegersGeometric(),
                "[{1=19, 2=47, 3=25}, {1=0, 2=19, 3=21}, {1=9, 2=13, 3=12}, {1=20, 2=10, 3=7}, {1=77, 2=19, 3=70}," +
                " {1=17, 2=53, 3=6}, {1=24, 2=16, 3=6}, {1=9, 2=0, 3=41}, {1=9, 2=15, 3=20}, {1=23, 2=24, 3=9}," +
                " {1=84, 2=32, 3=28}, {1=2, 2=41, 3=3}, {1=12, 2=21, 3=45}, {1=34, 2=28, 3=14}, {1=12, 2=2, 3=24}," +
                " {1=40, 2=37, 3=2}, {1=15, 2=15, 3=118}, {1=49, 2=46, 3=54}, {1=19, 2=15, 3=0}, {1=90, 2=31, 3=19}," +
                " ...]"
        );
        maps_helper(
                "[1, null, 3]",
                P.naturalIntegersGeometric(),
                "[{null=47, 1=19, 3=25}, {null=19, 1=0, 3=21}, {null=13, 1=9, 3=12}, {null=10, 1=20, 3=7}," +
                " {null=19, 1=77, 3=70}, {null=53, 1=17, 3=6}, {null=16, 1=24, 3=6}, {null=0, 1=9, 3=41}," +
                " {null=15, 1=9, 3=20}, {null=24, 1=23, 3=9}, {null=32, 1=84, 3=28}, {null=41, 1=2, 3=3}," +
                " {null=21, 1=12, 3=45}, {null=28, 1=34, 3=14}, {null=2, 1=12, 3=24}, {null=37, 1=40, 3=2}," +
                " {null=15, 1=15, 3=118}, {null=46, 1=49, 3=54}, {null=15, 1=19, 3=0}, {null=31, 1=90, 3=19}, ...]"
        );
    }

    private static double meanOfIntegers(@NotNull List<Integer> xs) {
        return sumDouble(map(i -> (double) i / DEFAULT_SAMPLE_SIZE, xs));
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<List<Integer>> readIntegerListWithNullsListsWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers.readListWithNulls(Readers::readInteger)).apply(s).get();
    }
}
// @formatter:on
