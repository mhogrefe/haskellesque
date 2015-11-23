package mho.wheels.math;

import mho.wheels.numberUtils.IntegerUtils;
import org.junit.Test;

import java.math.BigInteger;

import static mho.wheels.math.MathUtils.*;
import static mho.wheels.testing.Testing.aeq;
import static org.junit.Assert.fail;

public class MathUtilsTest {
    @Test
    public void testGcd_int_int() {
        aeq(gcd(12, 15), 3);
        aeq(gcd(35, 210), 35);
        aeq(gcd(17, 20), 1);
        aeq(gcd(1, 5), 1);
        aeq(gcd(-12, 15), 3);
        aeq(gcd(12, -15), 3);
        aeq(gcd(-12, -15), 3);
        aeq(gcd(6, 0), 6);
        aeq(gcd(-6, 0), 6);
        aeq(gcd(0, 6), 6);
        aeq(gcd(0, -6), 6);
        try {
            gcd(0, 0);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testGcd_long_long() {
        aeq(gcd(12L, 15L), 3);
        aeq(gcd(35L, 210L), 35);
        aeq(gcd(17L, 20L), 1);
        aeq(gcd(1L, 5L), 1);
        aeq(gcd(-12L, 15L), 3);
        aeq(gcd(12L, -15L), 3);
        aeq(gcd(-12L, -15L), 3);
        aeq(gcd(6L, 0L), 6);
        aeq(gcd(-6L, 0L), 6);
        aeq(gcd(0L, 6L), 6);
        aeq(gcd(0L, -6L), 6);
        try {
            gcd(0L, 0L);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLcm() {
        aeq(lcm(BigInteger.valueOf(12), BigInteger.valueOf(15)), 60);
        aeq(lcm(BigInteger.valueOf(35), BigInteger.valueOf(210)), 210);
        aeq(lcm(BigInteger.valueOf(17), BigInteger.valueOf(20)), 340);
        aeq(lcm(BigInteger.ONE, BigInteger.valueOf(5)), 5);
        try {
            lcm(BigInteger.valueOf(-12), BigInteger.valueOf(15));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            lcm(BigInteger.valueOf(12), BigInteger.valueOf(-15));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            lcm(BigInteger.valueOf(-12), BigInteger.valueOf(-15));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            lcm(BigInteger.valueOf(6), BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            lcm(BigInteger.ZERO, BigInteger.valueOf(6));
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            lcm(BigInteger.ZERO, BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFactorial_int() {
        aeq(factorial(0), 1);
        aeq(factorial(1), 1);
        aeq(factorial(2), 2);
        aeq(factorial(3), 6);
        aeq(factorial(4), 24);
        aeq(factorial(5), 120);
        aeq(factorial(6), 720);
        aeq(factorial(10), 3628800);
        aeq(factorial(100), "9332621544394415268169923885626670049071596826438162146859296389521759999322991" +
                            "5608941463976156518286253697920827223758251185210916864000000000000000000000000");
        try {
            factorial(-1);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFactorial_BigInteger() {
        aeq(factorial(BigInteger.ZERO), 1);
        aeq(factorial(BigInteger.ONE), 1);
        aeq(factorial(IntegerUtils.TWO), 2);
        aeq(factorial(BigInteger.valueOf(3)), 6);
        aeq(factorial(BigInteger.valueOf(4)), 24);
        aeq(factorial(BigInteger.valueOf(5)), 120);
        aeq(factorial(BigInteger.valueOf(6)), 720);
        aeq(factorial(BigInteger.TEN), 3628800);
        aeq(factorial(BigInteger.valueOf(100)),
                "9332621544394415268169923885626670049071596826438162146859296389521759999322991" +
                "5608941463976156518286253697920827223758251185210916864000000000000000000000000");
        try {
            factorial(IntegerUtils.NEGATIVE_ONE);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubfactorial_int() {
        aeq(subfactorial(0), 1);
        aeq(subfactorial(1), 0);
        aeq(subfactorial(2), 1);
        aeq(subfactorial(3), 2);
        aeq(subfactorial(4), 9);
        aeq(subfactorial(5), 44);
        aeq(subfactorial(6), 265);
        aeq(subfactorial(10), 1334961);
        aeq(subfactorial(100), "3433279598416380476519597752677614203236578380537578498354340028268518079332763" +
                               "2432791396429850988990237345920155783984828001486412574060553756854137069878601"); ///
        try {
            subfactorial(-1);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubfactorial_BigInteger() {
        aeq(subfactorial(BigInteger.ZERO), 1);
        aeq(subfactorial(BigInteger.ONE), 0);
        aeq(subfactorial(IntegerUtils.TWO), 1);
        aeq(subfactorial(BigInteger.valueOf(3)), 2);
        aeq(subfactorial(BigInteger.valueOf(4)), 9);
        aeq(subfactorial(BigInteger.valueOf(5)), 44);
        aeq(subfactorial(BigInteger.valueOf(6)), 265);
        aeq(subfactorial(BigInteger.TEN), 1334961);
        aeq(subfactorial(BigInteger.valueOf(100)),
                "3433279598416380476519597752677614203236578380537578498354340028268518079332763" +
                "2432791396429850988990237345920155783984828001486412574060553756854137069878601");
        try {
            subfactorial(IntegerUtils.NEGATIVE_ONE);
            fail();
        } catch (ArithmeticException ignored) {}
    }
}
