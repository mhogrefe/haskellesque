package mho.wheels.numberUtils;

import mho.wheels.io.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.wheels.numberUtils.IntegerUtils.*;
import static mho.wheels.testing.Testing.aeq;
import static org.junit.Assert.fail;

public class IntegerUtilsTest {
    @Test
    public void testConstants() {
        aeq(NEGATIVE_ONE, "-1");
        aeq(TWO, "2");
    }

    private static void lowestOneBit_int_helper(int input, int output) {
        aeq(Integer.lowestOneBit(input), output);
    }

    @Test
    public void testLowestOneBit_int() {
        lowestOneBit_int_helper(0, 0);
        lowestOneBit_int_helper(1, 1);
        lowestOneBit_int_helper(2, 2);
        lowestOneBit_int_helper(3, 1);
        lowestOneBit_int_helper(4, 4);
        lowestOneBit_int_helper(5, 1);
        lowestOneBit_int_helper(6, 2);
        lowestOneBit_int_helper(7, 1);
        lowestOneBit_int_helper(8, 8);
        lowestOneBit_int_helper(9, 1);
        lowestOneBit_int_helper(10, 2);
        lowestOneBit_int_helper(111, 1);
        lowestOneBit_int_helper(1000, 8);
        lowestOneBit_int_helper(-1, 1);
        lowestOneBit_int_helper(-2, 2);
        lowestOneBit_int_helper(-3, 1);
        lowestOneBit_int_helper(-4, 4);
        lowestOneBit_int_helper(-5, 1);
        lowestOneBit_int_helper(-6, 2);
        lowestOneBit_int_helper(-7, 1);
        lowestOneBit_int_helper(-8, 8);
        lowestOneBit_int_helper(-9, 1);
        lowestOneBit_int_helper(-10, 2);
        lowestOneBit_int_helper(-111, 1);
        lowestOneBit_int_helper(-1000, 8);
        lowestOneBit_int_helper(Integer.MAX_VALUE, 1);
        lowestOneBit_int_helper(Integer.MIN_VALUE, -2147483648);
    }

    private static void lowestOneBit_long_helper(long input, long output) {
        aeq(Long.lowestOneBit(input), output);
    }

    @Test
    public void testLowestOneBit_long() {
        lowestOneBit_long_helper(0L, 0L);
        lowestOneBit_long_helper(1L, 1L);
        lowestOneBit_long_helper(2L, 2L);
        lowestOneBit_long_helper(3L, 1L);
        lowestOneBit_long_helper(4L, 4L);
        lowestOneBit_long_helper(5L, 1L);
        lowestOneBit_long_helper(6L, 2L);
        lowestOneBit_long_helper(7L, 1L);
        lowestOneBit_long_helper(8L, 8L);
        lowestOneBit_long_helper(9L, 1L);
        lowestOneBit_long_helper(10L, 2L);
        lowestOneBit_long_helper(111L, 1L);
        lowestOneBit_long_helper(1000L, 8L);
        lowestOneBit_long_helper(-1L, 1L);
        lowestOneBit_long_helper(-2L, 2L);
        lowestOneBit_long_helper(-3L, 1L);
        lowestOneBit_long_helper(-4L, 4L);
        lowestOneBit_long_helper(-5L, 1L);
        lowestOneBit_long_helper(-6L, 2L);
        lowestOneBit_long_helper(-7L, 1L);
        lowestOneBit_long_helper(-8L, 8L);
        lowestOneBit_long_helper(-9L, 1L);
        lowestOneBit_long_helper(-10L, 2L);
        lowestOneBit_long_helper(-111L, 1L);
        lowestOneBit_long_helper(-1000L, 8L);
        lowestOneBit_long_helper(Long.MAX_VALUE, 1L);
        lowestOneBit_long_helper(Long.MIN_VALUE, -9223372036854775808L);
    }

    private static void highestOneBit_int_helper(int input, int output) {
        aeq(Integer.highestOneBit(input), output);
    }

    @Test
    public void testHighestOneBit_int() {
        highestOneBit_int_helper(0, 0);
        highestOneBit_int_helper(1, 1);
        highestOneBit_int_helper(2, 2);
        highestOneBit_int_helper(3, 2);
        highestOneBit_int_helper(4, 4);
        highestOneBit_int_helper(5, 4);
        highestOneBit_int_helper(6, 4);
        highestOneBit_int_helper(7, 4);
        highestOneBit_int_helper(8, 8);
        highestOneBit_int_helper(9, 8);
        highestOneBit_int_helper(10, 8);
        highestOneBit_int_helper(111, 64);
        highestOneBit_int_helper(1000, 512);
        highestOneBit_int_helper(-1, -2147483648);
        highestOneBit_int_helper(-2, -2147483648);
        highestOneBit_int_helper(-3, -2147483648);
        highestOneBit_int_helper(-4, -2147483648);
        highestOneBit_int_helper(-5, -2147483648);
        highestOneBit_int_helper(-6, -2147483648);
        highestOneBit_int_helper(-7, -2147483648);
        highestOneBit_int_helper(-8, -2147483648);
        highestOneBit_int_helper(-9, -2147483648);
        highestOneBit_int_helper(-10, -2147483648);
        highestOneBit_int_helper(-111, -2147483648);
        highestOneBit_int_helper(-1000, -2147483648);
        highestOneBit_int_helper(Integer.MAX_VALUE, 1073741824);
        highestOneBit_int_helper(Integer.MIN_VALUE, -2147483648);
    }

    private static void highestOneBit_long_helper(long input, long output) {
        aeq(Long.highestOneBit(input), output);
    }

    @Test
    public void testHighestOneBit_long() {
        highestOneBit_long_helper(0L, 0L);
        highestOneBit_long_helper(1L, 1L);
        highestOneBit_long_helper(2L, 2L);
        highestOneBit_long_helper(3L, 2L);
        highestOneBit_long_helper(4L, 4L);
        highestOneBit_long_helper(5L, 4L);
        highestOneBit_long_helper(6L, 4L);
        highestOneBit_long_helper(7L, 4L);
        highestOneBit_long_helper(8L, 8L);
        highestOneBit_long_helper(9L, 8L);
        highestOneBit_long_helper(10L, 8L);
        highestOneBit_long_helper(111L, 64L);
        highestOneBit_long_helper(1000L, 512L);
        highestOneBit_long_helper(-1L, -9223372036854775808L);
        highestOneBit_long_helper(-2L, -9223372036854775808L);
        highestOneBit_long_helper(-3L, -9223372036854775808L);
        highestOneBit_long_helper(-4L, -9223372036854775808L);
        highestOneBit_long_helper(-5L, -9223372036854775808L);
        highestOneBit_long_helper(-6L, -9223372036854775808L);
        highestOneBit_long_helper(-7L, -9223372036854775808L);
        highestOneBit_long_helper(-8L, -9223372036854775808L);
        highestOneBit_long_helper(-9L, -9223372036854775808L);
        highestOneBit_long_helper(-10L, -9223372036854775808L);
        highestOneBit_long_helper(-111L, -9223372036854775808L);
        highestOneBit_long_helper(-1000L, -9223372036854775808L);
        highestOneBit_long_helper(Long.MAX_VALUE, 4611686018427387904L);
        highestOneBit_long_helper(Long.MIN_VALUE, -9223372036854775808L);
    }

    private static void isPowerOfTwo_int_helper(int input, boolean output) {
        aeq(isPowerOfTwo(input), output);
    }

    private static void isPowerOfTwo_int_fail_helper(int input) {
        try {
            isPowerOfTwo(input);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsPowerOfTwo_int() {
        isPowerOfTwo_int_helper(1, true);
        isPowerOfTwo_int_helper(2, true);
        isPowerOfTwo_int_helper(4, true);
        isPowerOfTwo_int_helper(8, true);
        isPowerOfTwo_int_helper(16, true);
        isPowerOfTwo_int_helper(1 << 30, true);
        isPowerOfTwo_int_helper(3, false);
        isPowerOfTwo_int_helper(13, false);
        isPowerOfTwo_int_helper(100, false);
        isPowerOfTwo_int_helper(Integer.MAX_VALUE, false);

        isPowerOfTwo_int_fail_helper(0);
        isPowerOfTwo_int_fail_helper(-5);
    }

    private static void isPowerOfTwo_long_helper(long input, boolean output) {
        aeq(isPowerOfTwo(input), output);
    }

    private static void isPowerOfTwo_long_fail_helper(long input) {
        try {
            isPowerOfTwo(input);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsPowerOfTwo_long() {
        isPowerOfTwo_long_helper(1L, true);
        isPowerOfTwo_long_helper(2L, true);
        isPowerOfTwo_long_helper(4L, true);
        isPowerOfTwo_long_helper(8L, true);
        isPowerOfTwo_long_helper(16L, true);
        isPowerOfTwo_long_helper(1L << 62, true);
        isPowerOfTwo_long_helper(3L, false);
        isPowerOfTwo_long_helper(13L, false);
        isPowerOfTwo_long_helper(100L, false);
        isPowerOfTwo_long_helper(Long.MAX_VALUE, false);

        isPowerOfTwo_long_fail_helper(0);
        isPowerOfTwo_long_fail_helper(-5);
    }

    private static void isPowerOfTwo_BigInteger_helper(@NotNull String input, boolean output) {
        aeq(isPowerOfTwo(Readers.readBigIntegerStrict(input).get()), output);
    }

    private static void isPowerOfTwo_BigInteger_fail_helper(@NotNull String input) {
        try {
            isPowerOfTwo(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIsPowerOfTwo_BigInteger() {
        isPowerOfTwo_BigInteger_helper("1", true);
        isPowerOfTwo_BigInteger_helper("2", true);
        isPowerOfTwo_BigInteger_helper("4", true);
        isPowerOfTwo_BigInteger_helper("8", true);
        isPowerOfTwo_BigInteger_helper("16", true);
        isPowerOfTwo_BigInteger_helper("3", false);
        isPowerOfTwo_BigInteger_helper("13", false);
        isPowerOfTwo_BigInteger_helper("100", false);

        isPowerOfTwo_BigInteger_fail_helper("0");
        isPowerOfTwo_BigInteger_fail_helper("-5");
    }

    private static void ceilingLog2_int_helper(int input, int output) {
        aeq(ceilingLog2(input), output);
    }

    private static void ceilingLog2_int_fail_helper(int n) {
        try {
            ceilingLog2(n);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testCeilingLog2_int() {
        ceilingLog2_int_helper(1, 0);
        ceilingLog2_int_helper(2, 1);
        ceilingLog2_int_helper(3, 2);
        ceilingLog2_int_helper(4, 2);
        ceilingLog2_int_helper(5, 3);
        ceilingLog2_int_helper(6, 3);
        ceilingLog2_int_helper(7, 3);
        ceilingLog2_int_helper(8, 3);
        ceilingLog2_int_helper(9, 4);
        ceilingLog2_int_helper(1000, 10);
        ceilingLog2_int_helper(Integer.MAX_VALUE, 31);

        ceilingLog2_int_fail_helper(0);
        ceilingLog2_int_fail_helper(-5);
    }

    private static void ceilingLog2_long_helper(long input, int output) {
        aeq(ceilingLog2(input), output);
    }

    private static void ceilingLog2_long_fail_helper(long n) {
        try {
            ceilingLog2(n);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testCeilingLog2_long() {
        ceilingLog2_long_helper(1L, 0);
        ceilingLog2_long_helper(2L, 1);
        ceilingLog2_long_helper(3L, 2);
        ceilingLog2_long_helper(4L, 2);
        ceilingLog2_long_helper(5L, 3);
        ceilingLog2_long_helper(6L, 3);
        ceilingLog2_long_helper(7L, 3);
        ceilingLog2_long_helper(8L, 3);
        ceilingLog2_long_helper(9L, 4);
        ceilingLog2_long_helper(1000L, 10);
        ceilingLog2_long_helper(Long.MAX_VALUE, 63);

        ceilingLog2_long_fail_helper(0L);
        ceilingLog2_long_fail_helper(-5L);
    }

    private static void ceilingLog2_BigInteger_helper(@NotNull String input, int output) {
        aeq(ceilingLog2(Readers.readBigIntegerStrict(input).get()), output);
    }

    private static void ceilingLog2_BigInteger_fail_helper(@NotNull String input) {
        try {
            ceilingLog2(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testCeilingLog2_BigInteger() {
        ceilingLog2_BigInteger_helper("1", 0);
        ceilingLog2_BigInteger_helper("2", 1);
        ceilingLog2_BigInteger_helper("3", 2);
        ceilingLog2_BigInteger_helper("4", 2);
        ceilingLog2_BigInteger_helper("5", 3);
        ceilingLog2_BigInteger_helper("6", 3);
        ceilingLog2_BigInteger_helper("7", 3);
        ceilingLog2_BigInteger_helper("8", 3);
        ceilingLog2_BigInteger_helper("9", 4);
        ceilingLog2_BigInteger_helper("1000", 10);

        ceilingLog2_BigInteger_fail_helper("0");
        ceilingLog2_BigInteger_fail_helper("-5");
    }

    private static void bits_int_helper(int input, @NotNull String output) {
        aeq(bits(input), output);
    }

    private static void bits_int_fail_helper(int input) {
        try {
            bits(input);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBits_int() {
        bits_int_helper(0, "[]");
        bits_int_helper(1, "[true]");
        bits_int_helper(6, "[false, true, true]");
        bits_int_helper(105, "[true, false, false, true, false, true, true]");
        bits_int_helper(Integer.MAX_VALUE,
                "[true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true," +
                " true, true, true, true, true, true, true, true, true, true, true, true, true, true, true]");

        bits_int_fail_helper(-1);
    }

    private static void bits_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(bits(Readers.readBigIntegerStrict(input).get()), output);
    }

    private static void bits_BigInteger_fail_helper(@NotNull String input) {
        try {
            bits(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBits_BigInteger() {
        bits_BigInteger_helper("0", "[]");
        bits_BigInteger_helper("1", "[true]");
        bits_BigInteger_helper("6", "[false, true, true]");
        bits_BigInteger_helper("105", "[true, false, false, true, false, true, true]");

        bits_BigInteger_fail_helper("-1");
    }

    private static void bitsPadded_int_int_helper(int length, int n, @NotNull String output) {
        aeq(bitsPadded(length, n), output);
    }

    private static void bitsPadded_int_int_fail_helper(int length, int n) {
        try {
            bitsPadded(length, n);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBitsPadded_int_int() {
        bitsPadded_int_int_helper(8, 0, "[false, false, false, false, false, false, false, false]");
        bitsPadded_int_int_helper(8, 1, "[true, false, false, false, false, false, false, false]");
        bitsPadded_int_int_helper(8, 6, "[false, true, true, false, false, false, false, false]");
        bitsPadded_int_int_helper(8, 105, "[true, false, false, true, false, true, true, false]");
        bitsPadded_int_int_helper(8, 1000, "[false, false, false, true, false, true, true, true]");
        bitsPadded_int_int_helper(2, 104, "[false, false]");
        bitsPadded_int_int_helper(2, 105, "[true, false]");
        bitsPadded_int_int_helper(1, 104, "[false]");
        bitsPadded_int_int_helper(1, 105, "[true]");
        bitsPadded_int_int_helper(0, 104, "[]");
        bitsPadded_int_int_helper(100, 105,
                "[true, false, false, true, false, true, true, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false]");
        bitsPadded_int_int_helper(31, Integer.MAX_VALUE,
                "[true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true," +
                " true, true, true, true, true, true, true, true, true, true, true, true, true, true, true]");

        bitsPadded_int_int_fail_helper(8, -1);
        bitsPadded_int_int_fail_helper(-1, 8);
    }

    private static void bitsPadded_int_BigInteger_helper(int length, @NotNull String n, @NotNull String output) {
        aeq(bitsPadded(length, Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void bitsPadded_int_BigInteger_fail_helper(int length, @NotNull String n) {
        try {
            bitsPadded(length, Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBitsPadded_int_BigInteger() {
        bitsPadded_int_BigInteger_helper(8, "0", "[false, false, false, false, false, false, false, false]");
        bitsPadded_int_BigInteger_helper(8, "1", "[true, false, false, false, false, false, false, false]");
        bitsPadded_int_BigInteger_helper(8, "6", "[false, true, true, false, false, false, false, false]");
        bitsPadded_int_BigInteger_helper(8, "105", "[true, false, false, true, false, true, true, false]");
        bitsPadded_int_BigInteger_helper(8, "1000", "[false, false, false, true, false, true, true, true]");
        bitsPadded_int_BigInteger_helper(2, "104", "[false, false]");
        bitsPadded_int_BigInteger_helper(2, "105", "[true, false]");
        bitsPadded_int_BigInteger_helper(1, "104", "[false]");
        bitsPadded_int_BigInteger_helper(1, "105", "[true]");
        bitsPadded_int_BigInteger_helper(0, "104", "[]");
        bitsPadded_int_BigInteger_helper(100, "105",
                "[true, false, false, true, false, true, true, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false]");

        bitsPadded_int_BigInteger_fail_helper(8, "-1");
        bitsPadded_int_BigInteger_fail_helper(-1, "8");
    }

    private static void bigEndianBits_int_helper(int input, @NotNull String output) {
        aeq(bigEndianBits(input), output);
    }

    private static void bigEndianBits_int_fail_helper(int input) {
        try {
            bigEndianBits(input);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianBits_int() {
        bigEndianBits_int_helper(0, "[]");
        bigEndianBits_int_helper(1, "[true]");
        bigEndianBits_int_helper(6, "[true, true, false]");
        bigEndianBits_int_helper(105, "[true, true, false, true, false, false, true]");
        bigEndianBits_int_helper(Integer.MAX_VALUE,
                "[true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true," +
                " true, true, true, true, true, true, true, true, true, true, true, true, true, true, true]");

        bigEndianBits_int_fail_helper(-1);
    }

    private static void bigEndianBits_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(bigEndianBits(Readers.readBigIntegerStrict(input).get()), output);
    }

    private static void bigEndianBits_BigInteger_fail_helper(@NotNull String input) {
        try {
            bigEndianBits(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianBits_BigInteger() {
        bigEndianBits_BigInteger_helper("0", "[]");
        bigEndianBits_BigInteger_helper("1", "[true]");
        bigEndianBits_BigInteger_helper("6", "[true, true, false]");
        bigEndianBits_BigInteger_helper("105", "[true, true, false, true, false, false, true]");

        bigEndianBits_BigInteger_fail_helper("-1");
    }

    private static void bigEndianBitsPadded_int_int_helper(int length, int n, @NotNull String output) {
        aeq(bigEndianBitsPadded(length, n), output);
    }

    private static void bigEndianBitsPadded_int_int_fail_helper(int length, int n) {
        try {
            bigEndianBitsPadded(length, n);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigEndianBitsPadded_int_int() {
        bigEndianBitsPadded_int_int_helper(8, 0, "[false, false, false, false, false, false, false, false]");
        bigEndianBitsPadded_int_int_helper(8, 1, "[false, false, false, false, false, false, false, true]");
        bigEndianBitsPadded_int_int_helper(8, 6, "[false, false, false, false, false, true, true, false]");
        bigEndianBitsPadded_int_int_helper(8, 105, "[false, true, true, false, true, false, false, true]");
        bigEndianBitsPadded_int_int_helper(8, 1000, "[true, true, true, false, true, false, false, false]");
        bigEndianBitsPadded_int_int_helper(2, 104, "[false, false]");
        bigEndianBitsPadded_int_int_helper(2, 105, "[false, true]");
        bigEndianBitsPadded_int_int_helper(1, 104, "[false]");
        bigEndianBitsPadded_int_int_helper(1, 105, "[true]");
        bigEndianBitsPadded_int_int_helper(0, 104, "[]");
        bigEndianBitsPadded_int_int_helper(100, 105,
                "[false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, true, true, false, true, false," +
                " false, true]");
        bigEndianBitsPadded_int_int_helper(31, Integer.MAX_VALUE,
                "[true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true," +
                " true, true, true, true, true, true, true, true, true, true, true, true, true, true, true]");

        bigEndianBitsPadded_int_int_fail_helper(8, -1);
        bigEndianBitsPadded_int_int_fail_helper(-1, 8);
    }

    private static void bigEndianBitsPadded_int_BigInteger_helper(
            int length,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(bigEndianBitsPadded(length, Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void bigEndianBitsPadded_int_BigInteger_fail_helper(int length, @NotNull String n) {
        try {
            bigEndianBitsPadded(length, Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigEndianBitsPadded_BigInteger_BigInteger() {
        bigEndianBitsPadded_int_BigInteger_helper(8, "0", "[false, false, false, false, false, false, false, false]");
        bigEndianBitsPadded_int_BigInteger_helper(8, "1", "[false, false, false, false, false, false, false, true]");
        bigEndianBitsPadded_int_BigInteger_helper(8, "6", "[false, false, false, false, false, true, true, false]");
        bigEndianBitsPadded_int_BigInteger_helper(8, "105", "[false, true, true, false, true, false, false, true]");
        bigEndianBitsPadded_int_BigInteger_helper(8, "1000", "[true, true, true, false, true, false, false, false]");
        bigEndianBitsPadded_int_BigInteger_helper(2, "104", "[false, false]");
        bigEndianBitsPadded_int_BigInteger_helper(2, "105", "[false, true]");
        bigEndianBitsPadded_int_BigInteger_helper(1, "104", "[false]");
        bigEndianBitsPadded_int_BigInteger_helper(1, "105", "[true]");
        bigEndianBitsPadded_int_BigInteger_helper(0, "104", "[]");
        bigEndianBitsPadded_int_BigInteger_helper(100, "105",
                "[false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, false, false, false, false, false," +
                " false, false, false, false, false, false, false, false, false, true, true, false, true, false," +
                " false, true]");

        bigEndianBitsPadded_int_BigInteger_fail_helper(8, "-1");
        bigEndianBitsPadded_int_BigInteger_fail_helper(-1, "8");
    }

    private static void fromBits_helper(@NotNull String input, @NotNull String output) {
        aeq(fromBits(readBooleanList(input)), output);
    }

    private static void fromBits_fail_helper(@NotNull String input) {
        try {
            fromBits(readBooleanListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testFromBits() {
        fromBits_helper("[]", "0");
        fromBits_helper("[false, false]", "0");
        fromBits_helper("[true, false]", "1");
        fromBits_helper("[false, true, true, false, false, false, false, false]", "6");
        fromBits_helper("[true, false, false, true, false, true, true]", "105");

        fromBits_fail_helper("[true, null, true]");
    }

    private static void fromBigEndianBits_helper(@NotNull String input, @NotNull String output) {
        aeq(fromBigEndianBits(readBooleanList(input)), output);
    }

    private static void fromBigEndianBits_fail_helper(@NotNull String input) {
        try {
            fromBigEndianBits(readBooleanListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testFromBigEndianBits() {
        fromBigEndianBits_helper("[]", "0");
        fromBigEndianBits_helper("[false, false]", "0");
        fromBigEndianBits_helper("[false, true]", "1");
        fromBigEndianBits_helper("[false, false, false, false, false, true, true, false]", "6");
        fromBigEndianBits_helper("[true, true, false, true, false, false, true]", "105");

        fromBigEndianBits_fail_helper("[true, null, true]");
    }

    private static void digits_int_int_helper(int base, int n, @NotNull String output) {
        aeq(digits(base, n), output);
    }

    private static void digits_int_int_fail_helper(int base, int n) {
        try {
            digits(base, n);
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testDigits_int_int() {
        digits_int_int_helper(2, 0, "[]");
        digits_int_int_helper(3, 0, "[]");
        digits_int_int_helper(8, 0, "[]");
        digits_int_int_helper(10, 0, "[]");
        digits_int_int_helper(12, 0, "[]");
        digits_int_int_helper(57, 0, "[]");
        digits_int_int_helper(2, 1, "[1]");
        digits_int_int_helper(3, 1, "[1]");
        digits_int_int_helper(8, 1, "[1]");
        digits_int_int_helper(10, 1, "[1]");
        digits_int_int_helper(12, 1, "[1]");
        digits_int_int_helper(57, 1, "[1]");
        digits_int_int_helper(2, 10, "[0, 1, 0, 1]");
        digits_int_int_helper(3, 10, "[1, 0, 1]");
        digits_int_int_helper(8, 10, "[2, 1]");
        digits_int_int_helper(10, 10, "[0, 1]");
        digits_int_int_helper(12, 10, "[10]");
        digits_int_int_helper(57, 10, "[10]");
        digits_int_int_helper(2, 187, "[1, 1, 0, 1, 1, 1, 0, 1]");
        digits_int_int_helper(3, 187, "[1, 2, 2, 0, 2]");
        digits_int_int_helper(8, 187, "[3, 7, 2]");
        digits_int_int_helper(10, 187, "[7, 8, 1]");
        digits_int_int_helper(12, 187, "[7, 3, 1]");
        digits_int_int_helper(57, 187, "[16, 3]");
        digits_int_int_helper(Integer.MAX_VALUE, 0, "[]");
        digits_int_int_helper(Integer.MAX_VALUE, 187, "[187]");
        digits_int_int_helper(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, "[2147483646]");
        digits_int_int_helper(Integer.MAX_VALUE, Integer.MAX_VALUE, "[0, 1]");

        digits_int_int_fail_helper(1, 10);
        digits_int_int_fail_helper(0, 10);
        digits_int_int_fail_helper(2, -1);
        digits_int_int_fail_helper(0, -1);
    }

    private static void digits_BigInteger_BigInteger_helper(
            @NotNull String base,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(digits(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void digits_BigInteger_BigInteger_fail_helper(@NotNull String base, @NotNull String n) {
        try {
            digits(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testDigits_BigInteger_BigInteger() {
        digits_BigInteger_BigInteger_helper("2", "0", "[]");
        digits_BigInteger_BigInteger_helper("3", "0", "[]");
        digits_BigInteger_BigInteger_helper("8", "0", "[]");
        digits_BigInteger_BigInteger_helper("10", "0", "[]");
        digits_BigInteger_BigInteger_helper("12", "0", "[]");
        digits_BigInteger_BigInteger_helper("57", "0", "[]");
        digits_BigInteger_BigInteger_helper("2", "1", "[1]");
        digits_BigInteger_BigInteger_helper("3", "1", "[1]");
        digits_BigInteger_BigInteger_helper("8", "1", "[1]");
        digits_BigInteger_BigInteger_helper("10", "1", "[1]");
        digits_BigInteger_BigInteger_helper("12", "1", "[1]");
        digits_BigInteger_BigInteger_helper("57", "1", "[1]");
        digits_BigInteger_BigInteger_helper("2", "10", "[0, 1, 0, 1]");
        digits_BigInteger_BigInteger_helper("3", "10", "[1, 0, 1]");
        digits_BigInteger_BigInteger_helper("8", "10", "[2, 1]");
        digits_BigInteger_BigInteger_helper("10", "10", "[0, 1]");
        digits_BigInteger_BigInteger_helper("12", "10", "[10]");
        digits_BigInteger_BigInteger_helper("57", "10", "[10]");
        digits_BigInteger_BigInteger_helper("2", "187", "[1, 1, 0, 1, 1, 1, 0, 1]");
        digits_BigInteger_BigInteger_helper("3", "187", "[1, 2, 2, 0, 2]");
        digits_BigInteger_BigInteger_helper("8", "187", "[3, 7, 2]");
        digits_BigInteger_BigInteger_helper("10", "187", "[7, 8, 1]");
        digits_BigInteger_BigInteger_helper("12", "187", "[7, 3, 1]");
        digits_BigInteger_BigInteger_helper("57", "187", "[16, 3]");

        digits_BigInteger_BigInteger_fail_helper("1", "10");
        digits_BigInteger_BigInteger_fail_helper("0", "10");
        digits_BigInteger_BigInteger_fail_helper("2", "-1");
        digits_BigInteger_BigInteger_fail_helper("0", "-1");
    }

    private static void digitsPadded_int_int_int_helper(int length, int base, int n, @NotNull String output) {
        aeq(digitsPadded(length, base, n), output);
    }

    private static void digitsPadded_int_int_int_fail_helper(int length, int base, int n) {
        try {
            digitsPadded(length, base, n);
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testDigitsPadded_int_int_int() {
        digitsPadded_int_int_int_helper(0, 2, 0, "[]");
        digitsPadded_int_int_int_helper(0, 3, 0, "[]");
        digitsPadded_int_int_int_helper(0, 57, 0, "[]");
        digitsPadded_int_int_int_helper(0, 2, 1, "[]");
        digitsPadded_int_int_int_helper(0, 3, 1, "[]");
        digitsPadded_int_int_int_helper(0, 57, 1, "[]");
        digitsPadded_int_int_int_helper(0, 2, 10, "[]");
        digitsPadded_int_int_int_helper(0, 3, 10, "[]");
        digitsPadded_int_int_int_helper(0, 57, 10, "[]");
        digitsPadded_int_int_int_helper(0, 2, 187, "[]");
        digitsPadded_int_int_int_helper(0, 3, 187, "[]");
        digitsPadded_int_int_int_helper(0, 57, 187, "[]");
        digitsPadded_int_int_int_helper(1, 2, 0, "[0]");
        digitsPadded_int_int_int_helper(1, 3, 0, "[0]");
        digitsPadded_int_int_int_helper(1, 57, 0, "[0]");
        digitsPadded_int_int_int_helper(1, 2, 1, "[1]");
        digitsPadded_int_int_int_helper(1, 3, 1, "[1]");
        digitsPadded_int_int_int_helper(1, 57, 1, "[1]");
        digitsPadded_int_int_int_helper(1, 2, 10, "[0]");
        digitsPadded_int_int_int_helper(1, 3, 10, "[1]");
        digitsPadded_int_int_int_helper(1, 57, 10, "[10]");
        digitsPadded_int_int_int_helper(1, 2, 187, "[1]");
        digitsPadded_int_int_int_helper(1, 3, 187, "[1]");
        digitsPadded_int_int_int_helper(1, 57, 187, "[16]");
        digitsPadded_int_int_int_helper(2, 2, 0, "[0, 0]");
        digitsPadded_int_int_int_helper(2, 3, 0, "[0, 0]");
        digitsPadded_int_int_int_helper(2, 57, 0, "[0, 0]");
        digitsPadded_int_int_int_helper(2, 2, 1, "[1, 0]");
        digitsPadded_int_int_int_helper(2, 3, 1, "[1, 0]");
        digitsPadded_int_int_int_helper(2, 57, 1, "[1, 0]");
        digitsPadded_int_int_int_helper(2, 2, 10, "[0, 1]");
        digitsPadded_int_int_int_helper(2, 3, 10, "[1, 0]");
        digitsPadded_int_int_int_helper(2, 57, 10, "[10, 0]");
        digitsPadded_int_int_int_helper(2, 2, 187, "[1, 1]");
        digitsPadded_int_int_int_helper(2, 3, 187, "[1, 2]");
        digitsPadded_int_int_int_helper(2, 57, 187, "[16, 3]");
        digitsPadded_int_int_int_helper(8, 2, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 3, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 57, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 2, 1, "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 3, 1, "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 57, 1, "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 2, 10, "[0, 1, 0, 1, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 3, 10, "[1, 0, 1, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 57, 10, "[10, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 2, 187, "[1, 1, 0, 1, 1, 1, 0, 1]");
        digitsPadded_int_int_int_helper(8, 3, 187, "[1, 2, 2, 0, 2, 0, 0, 0]");
        digitsPadded_int_int_int_helper(8, 57, 187, "[16, 3, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, 0, "[0]");
        digitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, 187, "[187]");
        digitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, Integer.MAX_VALUE - 1, "[2147483646]");
        digitsPadded_int_int_int_helper(2, Integer.MAX_VALUE, Integer.MAX_VALUE, "[0, 1]");

        digitsPadded_int_int_int_fail_helper(3, 1, 10);
        digitsPadded_int_int_int_fail_helper(3, 0, 10);
        digitsPadded_int_int_int_fail_helper(3, 2, -1);
        digitsPadded_int_int_int_fail_helper(3, 0, -1);
        digitsPadded_int_int_int_fail_helper(-1, 2, 3);
        digitsPadded_int_int_int_fail_helper(-1, 0, -1);
    }

    private static void digitsPadded_int_BigInteger_BigInteger_helper(
            int length,
            @NotNull String base,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(
                digitsPadded(length, Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get()),
                output
        );
    }

    private static void digitsPadded_int_BigInteger_BigInteger_fail_helper(
            int length,
            @NotNull String base,
            @NotNull String n
    ) {
        try {
            digitsPadded(length, Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testDigitsPadded_int_BigInteger_BigInteger() {
        digitsPadded_int_BigInteger_BigInteger_helper(0, "2", "0", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "3", "0", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "57", "0", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "2", "1", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "3", "1", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "57", "1", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "2", "10", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "3", "10", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "57", "10", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "2", "187", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "3", "187", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(0, "57", "187", "[]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "2", "0", "[0]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "3", "0", "[0]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "57", "0", "[0]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "2", "1", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "3", "1", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "57", "1", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "2", "10", "[0]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "3", "10", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "57", "10", "[10]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "2", "187", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "3", "187", "[1]");
        digitsPadded_int_BigInteger_BigInteger_helper(1, "57", "187", "[16]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "2", "0", "[0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "3", "0", "[0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "57", "0", "[0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "2", "1", "[1, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "3", "1", "[1, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "57", "1", "[1, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "2", "10", "[0, 1]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "3", "10", "[1, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "57", "10", "[10, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "2", "187", "[1, 1]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "3", "187", "[1, 2]");
        digitsPadded_int_BigInteger_BigInteger_helper(2, "57", "187", "[16, 3]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "2", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "3", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "57", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "2", "1", "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "3", "1", "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "57", "1", "[1, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "2", "10", "[0, 1, 0, 1, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "3", "10", "[1, 0, 1, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "57", "10", "[10, 0, 0, 0, 0, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "2", "187", "[1, 1, 0, 1, 1, 1, 0, 1]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "3", "187", "[1, 2, 2, 0, 2, 0, 0, 0]");
        digitsPadded_int_BigInteger_BigInteger_helper(8, "57", "187", "[16, 3, 0, 0, 0, 0, 0, 0]");

        digitsPadded_int_BigInteger_BigInteger_fail_helper(3, "1", "10");
        digitsPadded_int_BigInteger_BigInteger_fail_helper(3, "0", "10");
        digitsPadded_int_BigInteger_BigInteger_fail_helper(3, "2", "-1");
        digitsPadded_int_BigInteger_BigInteger_fail_helper(3, "0", "-1");
        digitsPadded_int_BigInteger_BigInteger_fail_helper(-1, "2", "3");
        digitsPadded_int_BigInteger_BigInteger_fail_helper(-1, "0", "-1");
    }

    private static void bigEndianDigits_int_int_helper(int base, int n, @NotNull String output) {
        aeq(bigEndianDigits(base, n), output);
    }

    private static void bigEndianDigits_int_int_fail_helper(int base, int n) {
        try {
            bigEndianDigits(base, n);
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianDigits_int_int() {
        bigEndianDigits_int_int_helper(2, 0, "[]");
        bigEndianDigits_int_int_helper(3, 0, "[]");
        bigEndianDigits_int_int_helper(8, 0, "[]");
        bigEndianDigits_int_int_helper(10, 0, "[]");
        bigEndianDigits_int_int_helper(12, 0, "[]");
        bigEndianDigits_int_int_helper(57, 0, "[]");
        bigEndianDigits_int_int_helper(2, 1, "[1]");
        bigEndianDigits_int_int_helper(3, 1, "[1]");
        bigEndianDigits_int_int_helper(8, 1, "[1]");
        bigEndianDigits_int_int_helper(10, 1, "[1]");
        bigEndianDigits_int_int_helper(12, 1, "[1]");
        bigEndianDigits_int_int_helper(57, 1, "[1]");
        bigEndianDigits_int_int_helper(2, 10, "[1, 0, 1, 0]");
        bigEndianDigits_int_int_helper(3, 10, "[1, 0, 1]");
        bigEndianDigits_int_int_helper(8, 10, "[1, 2]");
        bigEndianDigits_int_int_helper(10, 10, "[1, 0]");
        bigEndianDigits_int_int_helper(12, 10, "[10]");
        bigEndianDigits_int_int_helper(57, 10, "[10]");
        bigEndianDigits_int_int_helper(2, 187, "[1, 0, 1, 1, 1, 0, 1, 1]");
        bigEndianDigits_int_int_helper(3, 187, "[2, 0, 2, 2, 1]");
        bigEndianDigits_int_int_helper(8, 187, "[2, 7, 3]");
        bigEndianDigits_int_int_helper(10, 187, "[1, 8, 7]");
        bigEndianDigits_int_int_helper(12, 187, "[1, 3, 7]");
        bigEndianDigits_int_int_helper(57, 187, "[3, 16]");
        bigEndianDigits_int_int_helper(Integer.MAX_VALUE, 0, "[]");
        bigEndianDigits_int_int_helper(Integer.MAX_VALUE, 187, "[187]");
        bigEndianDigits_int_int_helper(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, "[2147483646]");
        bigEndianDigits_int_int_helper(Integer.MAX_VALUE, Integer.MAX_VALUE, "[1, 0]");

        bigEndianDigits_int_int_fail_helper(1, 10);
        bigEndianDigits_int_int_fail_helper(0, 10);
        bigEndianDigits_int_int_fail_helper(2, -1);
        bigEndianDigits_int_int_fail_helper(0, -1);
    }

    private static void bigEndianDigits_BigInteger_BigInteger_helper(
            @NotNull String base,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(bigEndianDigits(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void bigEndianDigits_BigInteger_BigInteger_fail_helper(@NotNull String base, @NotNull String n) {
        try {
            bigEndianDigits(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianDigits_BigInteger_BigInteger() {
        bigEndianDigits_BigInteger_BigInteger_helper("2", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("3", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("8", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("10", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("12", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("57", "0", "[]");
        bigEndianDigits_BigInteger_BigInteger_helper("2", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("3", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("8", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("10", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("12", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("57", "1", "[1]");
        bigEndianDigits_BigInteger_BigInteger_helper("2", "10", "[1, 0, 1, 0]");
        bigEndianDigits_BigInteger_BigInteger_helper("3", "10", "[1, 0, 1]");
        bigEndianDigits_BigInteger_BigInteger_helper("8", "10", "[1, 2]");
        bigEndianDigits_BigInteger_BigInteger_helper("10", "10", "[1, 0]");
        bigEndianDigits_BigInteger_BigInteger_helper("12", "10", "[10]");
        bigEndianDigits_BigInteger_BigInteger_helper("57", "10", "[10]");
        bigEndianDigits_BigInteger_BigInteger_helper("2", "187", "[1, 0, 1, 1, 1, 0, 1, 1]");
        bigEndianDigits_BigInteger_BigInteger_helper("3", "187", "[2, 0, 2, 2, 1]");
        bigEndianDigits_BigInteger_BigInteger_helper("8", "187", "[2, 7, 3]");
        bigEndianDigits_BigInteger_BigInteger_helper("10", "187", "[1, 8, 7]");
        bigEndianDigits_BigInteger_BigInteger_helper("12", "187", "[1, 3, 7]");
        bigEndianDigits_BigInteger_BigInteger_helper("57", "187", "[3, 16]");

        bigEndianDigits_BigInteger_BigInteger_fail_helper("1", "10");
        bigEndianDigits_BigInteger_BigInteger_fail_helper("0", "10");
        bigEndianDigits_BigInteger_BigInteger_fail_helper("2", "-1");
        bigEndianDigits_BigInteger_BigInteger_fail_helper("0", "-1");
    }

    private static void bigEndianDigitsPadded_int_int_int_helper(int length, int base, int n, @NotNull String output) {
        aeq(bigEndianDigitsPadded(length, base, n), output);
    }

    private static void bigEndianDigitsPadded_int_int_int_fail_helper(int length, int base, int n) {
        try {
            bigEndianDigitsPadded(length, base, n);
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianDigitsPadded_int_int_int() {
        bigEndianDigitsPadded_int_int_int_helper(0, 2, 0, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 3, 0, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 57, 0, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 2, 1, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 3, 1, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 57, 1, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 2, 10, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 3, 10, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 57, 10, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 2, 187, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 3, 187, "[]");
        bigEndianDigitsPadded_int_int_int_helper(0, 57, 187, "[]");
        bigEndianDigitsPadded_int_int_int_helper(1, 2, 0, "[0]");
        bigEndianDigitsPadded_int_int_int_helper(1, 3, 0, "[0]");
        bigEndianDigitsPadded_int_int_int_helper(1, 57, 0, "[0]");
        bigEndianDigitsPadded_int_int_int_helper(1, 2, 1, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 3, 1, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 57, 1, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 2, 10, "[0]");
        bigEndianDigitsPadded_int_int_int_helper(1, 3, 10, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 57, 10, "[10]");
        bigEndianDigitsPadded_int_int_int_helper(1, 2, 187, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 3, 187, "[1]");
        bigEndianDigitsPadded_int_int_int_helper(1, 57, 187, "[16]");
        bigEndianDigitsPadded_int_int_int_helper(2, 2, 0, "[0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(2, 3, 0, "[0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(2, 57, 0, "[0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(2, 2, 1, "[0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 3, 1, "[0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 57, 1, "[0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 2, 10, "[1, 0]");
        bigEndianDigitsPadded_int_int_int_helper(2, 3, 10, "[0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 57, 10, "[0, 10]");
        bigEndianDigitsPadded_int_int_int_helper(2, 2, 187, "[1, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 3, 187, "[2, 1]");
        bigEndianDigitsPadded_int_int_int_helper(2, 57, 187, "[3, 16]");
        bigEndianDigitsPadded_int_int_int_helper(8, 2, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(8, 3, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(8, 57, 0, "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_int_int_helper(8, 2, 1, "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 3, 1, "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 57, 1, "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 2, 10, "[0, 0, 0, 0, 1, 0, 1, 0]");
        bigEndianDigitsPadded_int_int_int_helper(8, 3, 10, "[0, 0, 0, 0, 0, 1, 0, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 57, 10, "[0, 0, 0, 0, 0, 0, 0, 10]");
        bigEndianDigitsPadded_int_int_int_helper(8, 2, 187, "[1, 0, 1, 1, 1, 0, 1, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 3, 187, "[0, 0, 0, 2, 0, 2, 2, 1]");
        bigEndianDigitsPadded_int_int_int_helper(8, 57, 187, "[0, 0, 0, 0, 0, 0, 3, 16]");
        bigEndianDigitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, 0, "[0]");
        bigEndianDigitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, 187, "[187]");
        bigEndianDigitsPadded_int_int_int_helper(1, Integer.MAX_VALUE, Integer.MAX_VALUE - 1, "[2147483646]");
        bigEndianDigitsPadded_int_int_int_helper(2, Integer.MAX_VALUE, Integer.MAX_VALUE, "[1, 0]");

        bigEndianDigitsPadded_int_int_int_fail_helper(3, 1, 10);
        bigEndianDigitsPadded_int_int_int_fail_helper(3, 0, 10);
        bigEndianDigitsPadded_int_int_int_fail_helper(3, 2, -1);
        bigEndianDigitsPadded_int_int_int_fail_helper(3, 0, -1);
        bigEndianDigitsPadded_int_int_int_fail_helper(-1, 2, 3);
        bigEndianDigitsPadded_int_int_int_fail_helper(-1, 0, -1);
    }

    private static void bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(
            int length,
            @NotNull String base,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(
                bigEndianDigitsPadded(
                        length,
                        Readers.readBigIntegerStrict(base).get(),
                        Readers.readBigIntegerStrict(n).get()
                ),
                output
        );
    }

    private static void bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(
            int length,
            @NotNull String base,
            @NotNull String n
    ) {
        try {
            bigEndianDigitsPadded(
                    length,
                    Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get()
            );
            fail();
        } catch (IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testBigEndianDigitsPadded_BigInteger_BigInteger_BigInteger() {
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "2", "0", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "3", "0", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "57", "0", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "2", "1", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "3", "1", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "57", "1", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "2", "10", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "3", "10", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "57", "10", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "2", "187", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "3", "187", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(0, "57", "187", "[]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "2", "0", "[0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "3", "0", "[0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "57", "0", "[0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "2", "1", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "3", "1", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "57", "1", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "2", "10", "[0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "3", "10", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "57", "10", "[10]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "2", "187", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "3", "187", "[1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(1, "57", "187", "[16]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "2", "0", "[0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "3", "0", "[0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "57", "0", "[0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "2", "1", "[0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "3", "1", "[0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "57", "1", "[0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "2", "10", "[1, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "3", "10", "[0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "57", "10", "[0, 10]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "2", "187", "[1, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "3", "187", "[2, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(2, "57", "187", "[3, 16]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "2", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "3", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "57", "0", "[0, 0, 0, 0, 0, 0, 0, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "2", "1", "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "3", "1", "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "57", "1", "[0, 0, 0, 0, 0, 0, 0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "2", "10", "[0, 0, 0, 0, 1, 0, 1, 0]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "3", "10", "[0, 0, 0, 0, 0, 1, 0, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "57", "10", "[0, 0, 0, 0, 0, 0, 0, 10]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "2", "187", "[1, 0, 1, 1, 1, 0, 1, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "3", "187", "[0, 0, 0, 2, 0, 2, 2, 1]");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_helper(8, "57", "187", "[0, 0, 0, 0, 0, 0, 3, 16]");

        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(3, "1", "10");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(3, "0", "10");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(3, "2", "-1");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(3, "0", "-1");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(-1, "2", "3");
        bigEndianDigitsPadded_int_BigInteger_BigInteger_fail_helper(-1, "0", "-1");
    }

    private static void fromDigits_int_Iterable_Integer_helper(
            int base,
            @NotNull String digits,
            @NotNull String output
    ) {
        aeq(fromDigits(base, readIntegerList(digits)), output);
    }

    private static void fromDigits_int_Iterable_Integer_fail_helper(int base, @NotNull String digits) {
        try {
            fromDigits(base, readIntegerListWithNulls(digits));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromDigits_int_Iterable_Integer() {
        fromDigits_int_Iterable_Integer_helper(2, "[0, 0]", "0");
        fromDigits_int_Iterable_Integer_helper(2, "[1, 0]", "1");
        fromDigits_int_Iterable_Integer_helper(2, "[1, 0, 1, 1, 1, 0]", "29");
        fromDigits_int_Iterable_Integer_helper(10, "[9, 5, 1, 4, 1, 3]", "314159");
        fromDigits_int_Iterable_Integer_helper(70, "[8, 0, 20, 5, 43]", "1034243008");
        fromDigits_int_Iterable_Integer_helper(70, "[]", "0");
        fromDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[]", "0");
        fromDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[187]", "187");
        fromDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[2147483646]", "2147483646");
        fromDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[0, 1]", "2147483647");

        fromDigits_int_Iterable_Integer_fail_helper(2, "[0, null]");
        fromDigits_int_Iterable_Integer_fail_helper(1, "[1, 2, 3]");
        fromDigits_int_Iterable_Integer_fail_helper(0, "[1, 2, 3]");
        fromDigits_int_Iterable_Integer_fail_helper(-1, "[1, 2, 3]");
        fromDigits_int_Iterable_Integer_fail_helper(10, "[-1, 2, 3]");
        fromDigits_int_Iterable_Integer_fail_helper(10, "[1, 2, 10]");
    }

    private static void fromDigits_BigInteger_Iterable_BigInteger_helper(
            @NotNull String base,
            @NotNull String digits,
            @NotNull String output
    ) {
        aeq(fromDigits(Readers.readBigIntegerStrict(base).get(), readBigIntegerList(digits)), output);
    }

    private static void fromDigits_BigInteger_Iterable_BigInteger_fail_helper(
            @NotNull String base,
            @NotNull String digits
    ) {
        try {
            fromDigits(Readers.readBigIntegerStrict(base).get(), readBigIntegerListWithNulls(digits));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromDigits_BigInteger_Iterable_BigInteger() {
        fromDigits_BigInteger_Iterable_BigInteger_helper("2", "[0, 0]", "0");
        fromDigits_BigInteger_Iterable_BigInteger_helper("2", "[1, 0]", "1");
        fromDigits_BigInteger_Iterable_BigInteger_helper("2", "[1, 0, 1, 1, 1, 0]", "29");
        fromDigits_BigInteger_Iterable_BigInteger_helper("10", "[9, 5, 1, 4, 1, 3]", "314159");
        fromDigits_BigInteger_Iterable_BigInteger_helper("70", "[8, 0, 20, 5, 43]", "1034243008");
        fromDigits_BigInteger_Iterable_BigInteger_helper("70", "[]", "0");

        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("2", "[0, null]");
        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("1", "[1, 2, 3]");
        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("0", "[1, 2, 3]");
        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("-1", "[1, 2, 3]");
        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("10", "[-1, 2, 3]");
        fromDigits_BigInteger_Iterable_BigInteger_fail_helper("10", "[1, 2, 10]");
    }

    private static void fromBigEndianDigits_int_Iterable_Integer_helper(
            int base,
            @NotNull String digits,
            @NotNull String output
    ) {
        aeq(fromBigEndianDigits(base, readIntegerList(digits)), output);
    }

    private static void fromBigEndianDigits_int_Iterable_Integer_fail_helper(int base, @NotNull String digits) {
        try {
            fromBigEndianDigits(base, readIntegerListWithNulls(digits));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromBigEndianDigits_int_Iterable_Integer() {
        fromBigEndianDigits_int_Iterable_Integer_helper(2, "[0, 0]", "0");
        fromBigEndianDigits_int_Iterable_Integer_helper(2, "[0, 1]", "1");
        fromBigEndianDigits_int_Iterable_Integer_helper(2, "[0, 1, 1, 1, 0, 1]", "29");
        fromBigEndianDigits_int_Iterable_Integer_helper(10, "[3, 1, 4, 1, 5, 9]", "314159");
        fromBigEndianDigits_int_Iterable_Integer_helper(70, "[43, 5, 20, 0, 8]", "1034243008");
        fromBigEndianDigits_int_Iterable_Integer_helper(70, "[]", "0");
        fromBigEndianDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[]", "0");
        fromBigEndianDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[187]", "187");
        fromBigEndianDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[2147483646]", "2147483646");
        fromBigEndianDigits_int_Iterable_Integer_helper(Integer.MAX_VALUE, "[1, 0]", "2147483647");

        fromBigEndianDigits_int_Iterable_Integer_fail_helper(1, "[1, 2, 3]");
        fromBigEndianDigits_int_Iterable_Integer_fail_helper(0, "[1, 2, 3]");
        fromBigEndianDigits_int_Iterable_Integer_fail_helper(-1, "[1, 2, 3]");
        fromBigEndianDigits_int_Iterable_Integer_fail_helper(10, "[-1, 2, 3]");
        fromBigEndianDigits_int_Iterable_Integer_fail_helper(10, "[1, 2, 10]");
    }

    private static void fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper(
            @NotNull String base,
            @NotNull String digits,
            @NotNull String output
    ) {
        aeq(fromBigEndianDigits(Readers.readBigIntegerStrict(base).get(), readBigIntegerList(digits)), output);
    }

    private static void fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper(
            @NotNull String base,
            @NotNull String digits
    ) {
        try {
            fromBigEndianDigits(Readers.readBigIntegerStrict(base).get(), readBigIntegerListWithNulls(digits));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromBigEndianDigits_BigInteger_Iterable_BigInteger() {
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("2", "[0, 0]", "0");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("2", "[0, 1]", "1");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("2", "[0, 1, 1, 1, 0, 1]", "29");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("10", "[3, 1, 4, 1, 5, 9]", "314159");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("70", "[43, 5, 20, 0, 8]", "1034243008");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_helper("70", "[]", "0");

        fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper("1", "[1, 2, 3]");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper("0", "[1, 2, 3]");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper("-1", "[1, 2, 3]");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper("10", "[-1, 2, 3]");
        fromBigEndianDigits_BigInteger_Iterable_BigInteger_fail_helper("10", "[1, 2, 10]");
    }

    private static void toDigit_helper(int i, char c) {
        aeq(toDigit(i), c);
    }

    private static void toDigit_fail_helper(int i) {
        try {
            toDigit(i);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToDigit() {
        toDigit_helper(0, '0');
        toDigit_helper(6, '6');
        toDigit_helper(10, 'A');
        toDigit_helper(20, 'K');
        toDigit_helper(35, 'Z');

        toDigit_fail_helper(-1);
        toDigit_fail_helper(36);
    }

    private static void fromDigit_helper(char c, int i) {
        aeq(fromDigit(c), i);
    }

    private static void fromDigit_fail_helper(char c) {
        try {
            fromDigit(c);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromDigit() {
        fromDigit_helper('0', 0);
        fromDigit_helper('6', 6);
        fromDigit_helper('A', 10);
        fromDigit_helper('K', 20);
        fromDigit_helper('Z', 35);

        fromDigit_fail_helper(' ');
        fromDigit_fail_helper('a');
    }

    private static void toStringBase_int_int_helper(int base, int n, @NotNull String output) {
        aeq(toStringBase(base, n), output);
    }

    private static void toStringBase_int_int_fail_helper(int base, int n) {
        try {
            toStringBase(base, n);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToStringBase_int_int() {
        toStringBase_int_int_helper(2, 0, "0");
        toStringBase_int_int_helper(3, 0, "0");
        toStringBase_int_int_helper(4, 0, "0");
        toStringBase_int_int_helper(10, 0, "0");
        toStringBase_int_int_helper(12, 0, "0");
        toStringBase_int_int_helper(16, 0, "0");
        toStringBase_int_int_helper(36, 0, "0");
        toStringBase_int_int_helper(88, 0, "(0)");
        toStringBase_int_int_helper(100, 0, "(0)");
        toStringBase_int_int_helper(Integer.MAX_VALUE, 0, "(0)");
        toStringBase_int_int_helper(2, 524393454, "11111010000011001101111101110");
        toStringBase_int_int_helper(3, 524393454, "1100112201221120210");
        toStringBase_int_int_helper(4, 524393454, "133100121233232");
        toStringBase_int_int_helper(10, 524393454, "524393454");
        toStringBase_int_int_helper(12, 524393454, "127750526");
        toStringBase_int_int_helper(16, 524393454, "1F419BEE");
        toStringBase_int_int_helper(36, 524393454, "8O7KKU");
        toStringBase_int_int_helper(88, 524393454, "(8)(65)(44)(8)(46)");
        toStringBase_int_int_helper(100, 524393454, "(5)(24)(39)(34)(54)");
        toStringBase_int_int_helper(Integer.MAX_VALUE, 524393454, "(524393454)");
        toStringBase_int_int_helper(2, -524393454, "-11111010000011001101111101110");
        toStringBase_int_int_helper(3, -524393454, "-1100112201221120210");
        toStringBase_int_int_helper(4, -524393454, "-133100121233232");
        toStringBase_int_int_helper(10, -524393454, "-524393454");
        toStringBase_int_int_helper(12, -524393454, "-127750526");
        toStringBase_int_int_helper(16, -524393454, "-1F419BEE");
        toStringBase_int_int_helper(36, -524393454, "-8O7KKU");
        toStringBase_int_int_helper(88, -524393454, "-(8)(65)(44)(8)(46)");
        toStringBase_int_int_helper(100, -524393454, "-(5)(24)(39)(34)(54)");
        toStringBase_int_int_helper(Integer.MAX_VALUE, -524393454, "-(524393454)");
        toStringBase_int_int_helper(2, Integer.MAX_VALUE, "1111111111111111111111111111111");
        toStringBase_int_int_helper(3, Integer.MAX_VALUE, "12112122212110202101");
        toStringBase_int_int_helper(4, Integer.MAX_VALUE, "1333333333333333");
        toStringBase_int_int_helper(10, Integer.MAX_VALUE, "2147483647");
        toStringBase_int_int_helper(12, Integer.MAX_VALUE, "4BB2308A7");
        toStringBase_int_int_helper(16, Integer.MAX_VALUE, "7FFFFFFF");
        toStringBase_int_int_helper(36, Integer.MAX_VALUE, "ZIK0ZJ");
        toStringBase_int_int_helper(88, Integer.MAX_VALUE, "(35)(71)(21)(31)(23)");
        toStringBase_int_int_helper(100, Integer.MAX_VALUE, "(21)(47)(48)(36)(47)");
        toStringBase_int_int_helper(Integer.MAX_VALUE, Integer.MAX_VALUE, "(1)(0)");

        toStringBase_int_int_fail_helper(1, 524393454);
        toStringBase_int_int_fail_helper(0, 524393454);
        toStringBase_int_int_fail_helper(1, 0);
        toStringBase_int_int_fail_helper(0, 0);
    }

    private static void toStringBase_BigInteger_BigInteger_helper(
            @NotNull String base,
            @NotNull String n,
            @NotNull String output
    ) {
        aeq(toStringBase(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void toStringBase_BigInteger_BigInteger_fail_helper(@NotNull String base, @NotNull String n) {
        try {
            toStringBase(Readers.readBigIntegerStrict(base).get(), Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToStringBase_BigInteger_BigInteger() {
        toStringBase_BigInteger_BigInteger_helper("2", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("3", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("4", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("10", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("12", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("16", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("36", "0", "0");
        toStringBase_BigInteger_BigInteger_helper("88", "0", "(0)");
        toStringBase_BigInteger_BigInteger_helper("100", "0", "(0)");
        toStringBase_BigInteger_BigInteger_helper("2", "524393454", "11111010000011001101111101110");
        toStringBase_BigInteger_BigInteger_helper("3", "524393454", "1100112201221120210");
        toStringBase_BigInteger_BigInteger_helper("4", "524393454", "133100121233232");
        toStringBase_BigInteger_BigInteger_helper("10", "524393454", "524393454");
        toStringBase_BigInteger_BigInteger_helper("12", "524393454", "127750526");
        toStringBase_BigInteger_BigInteger_helper("16", "524393454", "1F419BEE");
        toStringBase_BigInteger_BigInteger_helper("36", "524393454", "8O7KKU");
        toStringBase_BigInteger_BigInteger_helper("88", "524393454", "(8)(65)(44)(8)(46)");
        toStringBase_BigInteger_BigInteger_helper("100", "524393454", "(5)(24)(39)(34)(54)");
        toStringBase_BigInteger_BigInteger_helper("2", "-524393454", "-11111010000011001101111101110");
        toStringBase_BigInteger_BigInteger_helper("3", "-524393454", "-1100112201221120210");
        toStringBase_BigInteger_BigInteger_helper("4", "-524393454", "-133100121233232");
        toStringBase_BigInteger_BigInteger_helper("10", "-524393454", "-524393454");
        toStringBase_BigInteger_BigInteger_helper("12", "-524393454", "-127750526");
        toStringBase_BigInteger_BigInteger_helper("16", "-524393454", "-1F419BEE");
        toStringBase_BigInteger_BigInteger_helper("36", "-524393454", "-8O7KKU");
        toStringBase_BigInteger_BigInteger_helper("88", "-524393454", "-(8)(65)(44)(8)(46)");
        toStringBase_BigInteger_BigInteger_helper("100", "-524393454", "-(5)(24)(39)(34)(54)");

        toStringBase_BigInteger_BigInteger_fail_helper("1", "524393454");
        toStringBase_BigInteger_BigInteger_fail_helper("0", "524393454");
        toStringBase_BigInteger_BigInteger_fail_helper("1", "0");
        toStringBase_BigInteger_BigInteger_fail_helper("0", "0");
    }

    private static void fromStringBase_int_String_helper(int base, @NotNull String s, @NotNull String output) {
        aeq(fromStringBase(base, s), output);
    }

    private static void fromStringBase_int_String_fail_helper(int base, @NotNull String s) {
        try {
            fromStringBase(base, s);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromStringBase_int_String() {
        fromStringBase_int_String_helper(2, "", "0");
        fromStringBase_int_String_helper(3, "", "0");
        fromStringBase_int_String_helper(4, "", "0");
        fromStringBase_int_String_helper(10, "", "0");
        fromStringBase_int_String_helper(12, "", "0");
        fromStringBase_int_String_helper(16, "", "0");
        fromStringBase_int_String_helper(36, "", "0");
        fromStringBase_int_String_helper(88, "", "0");
        fromStringBase_int_String_helper(100, "", "0");

        fromStringBase_int_String_helper(2, "0", "0");
        fromStringBase_int_String_helper(3, "0", "0");
        fromStringBase_int_String_helper(4, "0", "0");
        fromStringBase_int_String_helper(10, "0", "0");
        fromStringBase_int_String_helper(12, "0", "0");
        fromStringBase_int_String_helper(16, "0", "0");
        fromStringBase_int_String_helper(36, "0", "0");
        fromStringBase_int_String_helper(88, "(0)", "0");
        fromStringBase_int_String_helper(100, "(0)", "0");

        fromStringBase_int_String_helper(2, "00", "0");
        fromStringBase_int_String_helper(3, "00", "0");
        fromStringBase_int_String_helper(4, "00", "0");
        fromStringBase_int_String_helper(10, "00", "0");
        fromStringBase_int_String_helper(12, "00", "0");
        fromStringBase_int_String_helper(16, "00", "0");
        fromStringBase_int_String_helper(36, "00", "0");
        fromStringBase_int_String_helper(88, "(0)(0)", "0");
        fromStringBase_int_String_helper(100, "(0)(0)", "0");

        fromStringBase_int_String_helper(2, "-0", "0");
        fromStringBase_int_String_helper(3, "-0", "0");
        fromStringBase_int_String_helper(4, "-0", "0");
        fromStringBase_int_String_helper(10, "-0", "0");
        fromStringBase_int_String_helper(12, "-0", "0");
        fromStringBase_int_String_helper(16, "-0", "0");
        fromStringBase_int_String_helper(36, "-0", "0");
        fromStringBase_int_String_helper(88, "-(0)", "0");
        fromStringBase_int_String_helper(100, "-(0)", "0");

        fromStringBase_int_String_helper(2, "-00", "0");
        fromStringBase_int_String_helper(3, "-00", "0");
        fromStringBase_int_String_helper(4, "-00", "0");
        fromStringBase_int_String_helper(10, "-00", "0");
        fromStringBase_int_String_helper(12, "-00", "0");
        fromStringBase_int_String_helper(16, "-00", "0");
        fromStringBase_int_String_helper(36, "-00", "0");
        fromStringBase_int_String_helper(88, "-(0)(0)", "0");
        fromStringBase_int_String_helper(100, "-(0)(0)", "0");

        fromStringBase_int_String_helper(2, "11111010000011001101111101110", "524393454");
        fromStringBase_int_String_helper(3, "1100112201221120210", "524393454");
        fromStringBase_int_String_helper(4, "133100121233232", "524393454");
        fromStringBase_int_String_helper(10, "524393454", "524393454");
        fromStringBase_int_String_helper(12, "127750526", "524393454");
        fromStringBase_int_String_helper(16, "1F419BEE", "524393454");
        fromStringBase_int_String_helper(36, "8O7KKU", "524393454");
        fromStringBase_int_String_helper(88, "(8)(65)(44)(8)(46)", "524393454");
        fromStringBase_int_String_helper(100, "(5)(24)(39)(34)(54)", "524393454");

        fromStringBase_int_String_helper(2, "00011111010000011001101111101110", "524393454");
        fromStringBase_int_String_helper(3, "0001100112201221120210", "524393454");
        fromStringBase_int_String_helper(4, "000133100121233232", "524393454");
        fromStringBase_int_String_helper(10, "000524393454", "524393454");
        fromStringBase_int_String_helper(12, "000127750526", "524393454");
        fromStringBase_int_String_helper(16, "0001F419BEE", "524393454");
        fromStringBase_int_String_helper(36, "0008O7KKU", "524393454");
        fromStringBase_int_String_helper(88, "(0)(0)(0)(8)(65)(44)(8)(46)", "524393454");
        fromStringBase_int_String_helper(100, "(0)(0)(0)(5)(24)(39)(34)(54)", "524393454");

        fromStringBase_int_String_helper(2, "-11111010000011001101111101110", "-524393454");
        fromStringBase_int_String_helper(3, "-1100112201221120210", "-524393454");
        fromStringBase_int_String_helper(4, "-133100121233232", "-524393454");
        fromStringBase_int_String_helper(10, "-524393454", "-524393454");
        fromStringBase_int_String_helper(12, "-127750526", "-524393454");
        fromStringBase_int_String_helper(16, "-1F419BEE", "-524393454");
        fromStringBase_int_String_helper(36, "-8O7KKU", "-524393454");
        fromStringBase_int_String_helper(88, "-(8)(65)(44)(8)(46)", "-524393454");
        fromStringBase_int_String_helper(100, "-(5)(24)(39)(34)(54)", "-524393454");

        fromStringBase_int_String_helper(2, "-00011111010000011001101111101110", "-524393454");
        fromStringBase_int_String_helper(3, "-0001100112201221120210", "-524393454");
        fromStringBase_int_String_helper(4, "-000133100121233232", "-524393454");
        fromStringBase_int_String_helper(10, "-000524393454", "-524393454");
        fromStringBase_int_String_helper(12, "-000127750526", "-524393454");
        fromStringBase_int_String_helper(16, "-0001F419BEE", "-524393454");
        fromStringBase_int_String_helper(36, "-0008O7KKU", "-524393454");
        fromStringBase_int_String_helper(88, "-(0)(0)(0)(8)(65)(44)(8)(46)", "-524393454");
        fromStringBase_int_String_helper(100, "-(0)(0)(0)(5)(24)(39)(34)(54)", "-524393454");

        fromStringBase_int_String_fail_helper(1, "");
        fromStringBase_int_String_fail_helper(0, "");
        fromStringBase_int_String_fail_helper(2, "-");
        fromStringBase_int_String_fail_helper(2, "3");
        fromStringBase_int_String_fail_helper(2, "*");
        fromStringBase_int_String_fail_helper(100, "12");
        fromStringBase_int_String_fail_helper(100, "(-12)");
        fromStringBase_int_String_fail_helper(100, "(3F)");
        fromStringBase_int_String_fail_helper(100, "-");
        fromStringBase_int_String_fail_helper(100, "()");
        fromStringBase_int_String_fail_helper(100, "()()");
        fromStringBase_int_String_fail_helper(100, "(00)");
        fromStringBase_int_String_fail_helper(100, "(02)");
        fromStringBase_int_String_fail_helper(100, "(2)()");
    }

    private static void fromStringBase_BigInteger_String_helper(
            @NotNull String base,
            @NotNull String s,
            @NotNull String output
    ) {
        aeq(fromStringBase(Readers.readBigIntegerStrict(base).get(), s), output);
    }

    private static void fromStringBase_BigInteger_String_fail_helper(@NotNull String base, @NotNull String s) {
        try {
            fromStringBase(Readers.readBigIntegerStrict(base).get(), s);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromStringBase_BigInteger_String() {
        fromStringBase_BigInteger_String_helper("2", "", "0");
        fromStringBase_BigInteger_String_helper("3", "", "0");
        fromStringBase_BigInteger_String_helper("4", "", "0");
        fromStringBase_BigInteger_String_helper("10", "", "0");
        fromStringBase_BigInteger_String_helper("12", "", "0");
        fromStringBase_BigInteger_String_helper("16", "", "0");
        fromStringBase_BigInteger_String_helper("36", "", "0");
        fromStringBase_BigInteger_String_helper("88", "", "0");
        fromStringBase_BigInteger_String_helper("100", "", "0");

        fromStringBase_BigInteger_String_helper("2", "0", "0");
        fromStringBase_BigInteger_String_helper("3", "0", "0");
        fromStringBase_BigInteger_String_helper("4", "0", "0");
        fromStringBase_BigInteger_String_helper("10", "0", "0");
        fromStringBase_BigInteger_String_helper("12", "0", "0");
        fromStringBase_BigInteger_String_helper("16", "0", "0");
        fromStringBase_BigInteger_String_helper("36", "0", "0");
        fromStringBase_BigInteger_String_helper("88", "(0)", "0");
        fromStringBase_BigInteger_String_helper("100", "(0)", "0");

        fromStringBase_BigInteger_String_helper("2", "00", "0");
        fromStringBase_BigInteger_String_helper("3", "00", "0");
        fromStringBase_BigInteger_String_helper("4", "00", "0");
        fromStringBase_BigInteger_String_helper("10", "00", "0");
        fromStringBase_BigInteger_String_helper("12", "00", "0");
        fromStringBase_BigInteger_String_helper("16", "00", "0");
        fromStringBase_BigInteger_String_helper("36", "00", "0");
        fromStringBase_BigInteger_String_helper("88", "(0)(0)", "0");
        fromStringBase_BigInteger_String_helper("100", "(0)(0)", "0");

        fromStringBase_BigInteger_String_helper("2", "-0", "0");
        fromStringBase_BigInteger_String_helper("3", "-0", "0");
        fromStringBase_BigInteger_String_helper("4", "-0", "0");
        fromStringBase_BigInteger_String_helper("10", "-0", "0");
        fromStringBase_BigInteger_String_helper("12", "-0", "0");
        fromStringBase_BigInteger_String_helper("16", "-0", "0");
        fromStringBase_BigInteger_String_helper("36", "-0", "0");
        fromStringBase_BigInteger_String_helper("88", "-(0)", "0");
        fromStringBase_BigInteger_String_helper("100", "-(0)", "0");

        fromStringBase_BigInteger_String_helper("2", "-00", "0");
        fromStringBase_BigInteger_String_helper("3", "-00", "0");
        fromStringBase_BigInteger_String_helper("4", "-00", "0");
        fromStringBase_BigInteger_String_helper("10", "-00", "0");
        fromStringBase_BigInteger_String_helper("12", "-00", "0");
        fromStringBase_BigInteger_String_helper("16", "-00", "0");
        fromStringBase_BigInteger_String_helper("36", "-00", "0");
        fromStringBase_BigInteger_String_helper("88", "-(0)(0)", "0");
        fromStringBase_BigInteger_String_helper("100", "-(0)(0)", "0");

        fromStringBase_BigInteger_String_helper("2", "11111010000011001101111101110", "524393454");
        fromStringBase_BigInteger_String_helper("3", "1100112201221120210", "524393454");
        fromStringBase_BigInteger_String_helper("4", "133100121233232", "524393454");
        fromStringBase_BigInteger_String_helper("10", "524393454", "524393454");
        fromStringBase_BigInteger_String_helper("12", "127750526", "524393454");
        fromStringBase_BigInteger_String_helper("16", "1F419BEE", "524393454");
        fromStringBase_BigInteger_String_helper("36", "8O7KKU", "524393454");
        fromStringBase_BigInteger_String_helper("88", "(8)(65)(44)(8)(46)", "524393454");
        fromStringBase_BigInteger_String_helper("100", "(5)(24)(39)(34)(54)", "524393454");

        fromStringBase_BigInteger_String_helper("2", "00011111010000011001101111101110", "524393454");
        fromStringBase_BigInteger_String_helper("3", "0001100112201221120210", "524393454");
        fromStringBase_BigInteger_String_helper("4", "000133100121233232", "524393454");
        fromStringBase_BigInteger_String_helper("10", "000524393454", "524393454");
        fromStringBase_BigInteger_String_helper("12", "000127750526", "524393454");
        fromStringBase_BigInteger_String_helper("16", "0001F419BEE", "524393454");
        fromStringBase_BigInteger_String_helper("36", "0008O7KKU", "524393454");
        fromStringBase_BigInteger_String_helper("88", "(0)(0)(0)(8)(65)(44)(8)(46)", "524393454");
        fromStringBase_BigInteger_String_helper("100", "(0)(0)(0)(5)(24)(39)(34)(54)", "524393454");

        fromStringBase_BigInteger_String_helper("2", "-11111010000011001101111101110", "-524393454");
        fromStringBase_BigInteger_String_helper("3", "-1100112201221120210", "-524393454");
        fromStringBase_BigInteger_String_helper("4", "-133100121233232", "-524393454");
        fromStringBase_BigInteger_String_helper("10", "-524393454", "-524393454");
        fromStringBase_BigInteger_String_helper("12", "-127750526", "-524393454");
        fromStringBase_BigInteger_String_helper("16", "-1F419BEE", "-524393454");
        fromStringBase_BigInteger_String_helper("36", "-8O7KKU", "-524393454");
        fromStringBase_BigInteger_String_helper("88", "-(8)(65)(44)(8)(46)", "-524393454");
        fromStringBase_BigInteger_String_helper("100", "-(5)(24)(39)(34)(54)", "-524393454");

        fromStringBase_BigInteger_String_helper("2", "-00011111010000011001101111101110", "-524393454");
        fromStringBase_BigInteger_String_helper("3", "-0001100112201221120210", "-524393454");
        fromStringBase_BigInteger_String_helper("4", "-000133100121233232", "-524393454");
        fromStringBase_BigInteger_String_helper("10", "-000524393454", "-524393454");
        fromStringBase_BigInteger_String_helper("12", "-000127750526", "-524393454");
        fromStringBase_BigInteger_String_helper("16", "-0001F419BEE", "-524393454");
        fromStringBase_BigInteger_String_helper("36", "-0008O7KKU", "-524393454");
        fromStringBase_BigInteger_String_helper("88", "-(0)(0)(0)(8)(65)(44)(8)(46)", "-524393454");
        fromStringBase_BigInteger_String_helper("100", "-(0)(0)(0)(5)(24)(39)(34)(54)", "-524393454");

        fromStringBase_BigInteger_String_fail_helper("1", "");
        fromStringBase_BigInteger_String_fail_helper("0", "");
        fromStringBase_BigInteger_String_fail_helper("2", "-");
        fromStringBase_BigInteger_String_fail_helper("2", "3");
        fromStringBase_BigInteger_String_fail_helper("2", "*");
        fromStringBase_BigInteger_String_fail_helper("100", "12");
        fromStringBase_BigInteger_String_fail_helper("100", "(-12)");
        fromStringBase_BigInteger_String_fail_helper("100", "(3F)");
        fromStringBase_BigInteger_String_fail_helper("100", "-");
        fromStringBase_BigInteger_String_fail_helper("100", "()");
        fromStringBase_BigInteger_String_fail_helper("100", "()()");
        fromStringBase_BigInteger_String_fail_helper("100", "(00)");
        fromStringBase_BigInteger_String_fail_helper("100", "(02)");
        fromStringBase_BigInteger_String_fail_helper("100", "(2)()");
    }

    private static void logarithmicMux_helper(@NotNull String x, @NotNull String y, @NotNull String output) {
        aeq(logarithmicMux(Readers.readBigIntegerStrict(x).get(), Readers.readBigIntegerStrict(y).get()), output);
    }

    private static void logarithmicMux_fail_helper(@NotNull String x, @NotNull String y) {
        try {
            logarithmicMux(Readers.readBigIntegerStrict(x).get(), Readers.readBigIntegerStrict(y).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLogarithmicMux() {
        logarithmicMux_helper("0", "0", "0");
        logarithmicMux_helper("0", "1", "1");
        logarithmicMux_helper("1", "0", "2");
        logarithmicMux_helper("5", "10", "11263");
        logarithmicMux_helper("10", "5", "671");
        logarithmicMux_helper("500000", "0", "1000000");
        logarithmicMux_helper("100000000000000000000", "5", "6400000000000000000031");

        logarithmicMux_fail_helper("-5", "5");
        logarithmicMux_fail_helper("5", "-5");
        logarithmicMux_fail_helper("-5", "-5");
        logarithmicMux_fail_helper("5", "100000000000000000000");
    }

    private static void logarithmicDemux_helper(@NotNull String n, @NotNull String x, @NotNull String y) {
        Pair<BigInteger, BigInteger> p = logarithmicDemux(Readers.readBigIntegerStrict(n).get());
        aeq(p.a, x);
        aeq(p.b, y);
    }

    private static void logarithmicDemux_fail_helper(@NotNull String n) {
        try {
            logarithmicDemux(Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLogarithmicDemux() {
        logarithmicDemux_helper("0", "0", "0");
        logarithmicDemux_helper("1", "0", "1");
        logarithmicDemux_helper("2", "1", "0");
        logarithmicDemux_helper("11263", "5", "10");
        logarithmicDemux_helper("671", "10", "5");
        logarithmicDemux_helper("1000000", "500000", "0");
        logarithmicDemux_helper("6400000000000000000031", "100000000000000000000", "5");

        logarithmicDemux_fail_helper("-5");
    }

    private static void squareRootMux_helper(@NotNull String x, @NotNull String y, @NotNull String output) {
        aeq(squareRootMux(Readers.readBigIntegerStrict(x).get(), Readers.readBigIntegerStrict(y).get()), output);
    }

    private static void squareRootMux_fail_helper(@NotNull String x, @NotNull String y) {
        try {
            squareRootMux(Readers.readBigIntegerStrict(x).get(), Readers.readBigIntegerStrict(y).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSquareRootMux() {
        squareRootMux_helper("0", "0", "0");
        squareRootMux_helper("0", "1", "1");
        squareRootMux_helper("1", "0", "2");
        squareRootMux_helper("5", "10", "538");
        squareRootMux_helper("10", "5", "101");
        squareRootMux_helper("7680", "76", "1000000");
        squareRootMux_helper("5", "100000000000000000000",
                "408122147460934661546617663009122238394934174203728183164946");
        squareRootMux_helper("100000000000000000000", "5", "1451615365330893086667197907009");

        squareRootMux_fail_helper("-5", "5");
        squareRootMux_fail_helper("5", "-5");
        squareRootMux_fail_helper("-5", "-5");
    }

    private static void squareRootDemux_helper(@NotNull String n, @NotNull String x, @NotNull String y) {
        Pair<BigInteger, BigInteger> p = squareRootDemux(Readers.readBigIntegerStrict(n).get());
        aeq(p.a, x);
        aeq(p.b, y);
    }

    private static void squareRootDemux_fail_helper(@NotNull String n) {
        try {
            squareRootDemux(Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSquareRootDemux() {
        squareRootDemux_helper("0", "0", "0");
        squareRootDemux_helper("1", "0", "1");
        squareRootDemux_helper("2", "1", "0");
        squareRootDemux_helper("538", "5", "10");
        squareRootDemux_helper("101", "10", "5");
        squareRootDemux_helper("1000000", "7680", "76");
        squareRootDemux_helper("408122147460934661546617663009122238394934174203728183164946", "5",
                "100000000000000000000");
        squareRootDemux_helper("1451615365330893086667197907009", "100000000000000000000", "5");

        squareRootDemux_fail_helper("-5");
    }

    private static void mux_helper(@NotNull String input, @NotNull String output) {
        aeq(mux(readBigIntegerList(input)), output);
    }

    private static void mux_fail_helper(@NotNull String input) {
        try {
            mux(readBigIntegerListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException | ArithmeticException ignored) {}
    }

    @Test
    public void testMux() {
        mux_helper("[]", "0");
        mux_helper("[0]", "0");
        mux_helper("[1]", "1");
        mux_helper("[2]", "2");
        mux_helper("[0, 0]", "0");
        mux_helper("[0, 1]", "1");
        mux_helper("[1, 0]", "2");
        mux_helper("[5, 10]", "102");
        mux_helper("[10, 5]", "153");
        mux_helper("[784, 904]", "1000000");
        mux_helper("[0, 0, 0]", "0");
        mux_helper("[10, 10, 10]", "3640");
        mux_helper("[48, 96, 76]", "1000000");
        mux_helper("[1, 2, 3, 4]", "362");
        mux_helper("[3, 2, 2, 3, 0, 2, 0, 0, 0, 0]", "1000000");

        mux_fail_helper("[1, 2, -3]");
        mux_fail_helper("[1, null, 2]");
    }

    private static void demux_helper(int size, @NotNull String n, @NotNull String output) {
        aeq(demux(size, Readers.readBigIntegerStrict(n).get()), output);
    }

    private static void demux_fail_helper(int size, @NotNull String n) {
        try {
            demux(size, Readers.readBigIntegerStrict(n).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDemux() {
        demux_helper(0, "0", "[]");
        demux_helper(1, "0", "[0]");
        demux_helper(1, "1", "[1]");
        demux_helper(1, "2", "[2]");
        demux_helper(2, "0", "[0, 0]");
        demux_helper(2, "1", "[0, 1]");
        demux_helper(2, "2", "[1, 0]");
        demux_helper(2, "1000000", "[784, 904]");
        demux_helper(3, "0", "[0, 0, 0]");
        demux_helper(3, "3640", "[10, 10, 10]");
        demux_helper(3, "1000000", "[48, 96, 76]");
        demux_helper(4, "362", "[1, 2, 3, 4]");
        demux_helper(10, "1000000", "[3, 2, 2, 3, 0, 2, 0, 0, 0, 0]");

        demux_fail_helper(0, "1");
        demux_fail_helper(-2, "0");
        demux_fail_helper(-2, "1");
        demux_fail_helper(2, "-5");
    }

    private static @NotNull List<Boolean> readBooleanList(@NotNull String s) {
        return Readers.readListStrict(Readers::readBooleanStrict).apply(s).get();
    }

    private static @NotNull List<Boolean> readBooleanListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readBooleanStrict).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readBigIntegerStrict).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readBigIntegerStrict).apply(s).get();
    }
}
