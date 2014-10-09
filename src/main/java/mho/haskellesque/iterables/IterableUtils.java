package mho.haskellesque.iterables;

import mho.haskellesque.tuples.Pair;
import mho.haskellesque.tuples.Quadruple;
import mho.haskellesque.tuples.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.haskellesque.ordering.Ordering.*;

public class IterableUtils {
    /**
     * Adds an <tt>Iterable</tt>'s elements to a <tt>Collection</tt>, in the order that the elements appear in the
     * <tt>Iterable</tt>. Only works for finite <tt>Iterable</tt>s.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li><tt>collection</tt> must be non-null.</li>
     *  <li><tt>collection</tt> must be able to hold every element of <tt>xs</tt>.</li>
     * </ul>
     *
     * @param xs the iterable
     * @param collection the collection to which the <tt>Iterable</tt>'s elements are added
     * @param <T> the <tt>Iterable</tt>'s element type
     */
    public static <T> void addTo(@NotNull Iterable<T> xs, @NotNull Collection<T> collection) {
        for (T x : xs) {
            collection.add(x);
        }
    }

    /**
     * Adds a <tt>String</tt>'s characters to a <tt>Collection</tt>, in the order that the characters appear in the
     * <tt>String</tt>.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-null.</li>
     *  <li><tt>collection</tt> must be non-null.</li>
     *  <li><tt>collection</tt> must be able to hold every character of <tt>s</tt>.</li>
     * </ul>
     *
     * @param s the string
     * @param collection the collection to which the <tt>String</tt>'s characters are added
     */
    public static void addTo(@NotNull String s, @NotNull Collection<Character> collection) {
        for (int i = 0; i < s.length(); i++) {
            collection.add(s.charAt(i));
        }
    }

    /**
     * Converts an <tt>Iterable</tt> to a <tt>List</tt>. Only works for finite <tt>Iterable</tt>s. The resulting list
     * may be modified, but the modifications will not affect the original <tt>Iterable</tt>.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs the <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return a <tt>List</tt> containing the elements of the <tt>Iterable</tt> in their original order
     */
    public static @NotNull <T> List<T> toList(@NotNull Iterable<T> xs) {
        List<T> list = new ArrayList<>();
        addTo(xs, list);
        return list;
    }

    /**
     * Converts an <tt>Iterable</tt> to a <tt>List</tt>. Only works for finite <tt>Iterable</tt>s.
     *
     * <ul>
     *  <li><tt>s</tt> may be any <tt>String</tt>.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param s the <tt>String</tt>
     * @return a <tt>List</tt> containing the characters of <tt>s</tt> in their original order
     */
    public static @NotNull List<Character> toList(@NotNull String s) {
        List<Character> list = new ArrayList<>();
        addTo(s, list);
        return list;
    }

    /**
     * Creates a <tt>String</tt> representation of <tt>xs</tt>. Each element is converted to a <tt>String</tt> and
     * those strings are placed in a comma-separated list surrounded by square brackets. Only works for finite
     * <tt>Iterable</tt>s.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li>The result begins with '[' and ends with ']'.</li>
     * </ul>
     *
     * @param xs the <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return a <tt>String</tt> representation of <tt>xs</tt>
     */
    public static @NotNull <T> String toString(@NotNull Iterable<T> xs) {
        return toList(xs).toString();
    }

    /**
     * Converts a <tt>String</tt> to an <tt>Iterable</tt> of characters. The order of the characters is preserved. Uses
     * O(1) additional memory. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-null.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * @param s the <tt>String</tt>
     * @return an <tt>Iterable</tt> containing all the <tt>String</tt>'s characters in their original order
     */
    public static @NotNull Iterable<Character> fromString(@NotNull String s) {
        return () -> new Iterator<Character>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < s.length();
            }

            @Override
            public Character next() {
                return s.charAt(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Creates a <tt>String</tt> from an <tt>Iterable</tt> of characters. The order of the characters is preserved.
     * Only works for finite <tt>Iterable</tt>s.
     *
     * <ul>
     *  <li><tt>cs</tt> must be finite and cannot contain nulls.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param cs the <tt>Iterable</tt> of characters
     * @return the <tt>String</tt> containing all of <tt>chars</tt>'s characters in their original order
     */
    public static @NotNull String charsToString(@NotNull Iterable<Character> cs) {
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Equivalent of Haskell's <tt>(:)</tt> list constructor. Creates an <tt>Iterable</tt> whose first element is
     * <tt>x</tt> and whose remaining elements are given by <tt>xs</tt>. <tt>xs</tt> may be infinite, in which case the
     * result is also infinite. Uses O(1) additional memory. The <tt>Iterable</tt> produced does not support removing
     * elements.
     *
     * <ul>
     *  <li><tt>x</tt> can be anything.</li>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result is a non-empty <tt>Iterable</tt>.</li>
     * </ul>
     *
     * @param x the first element of the <tt>Iterable</tt> to be created
     * @param xs the second-through-last elements of the <tt>Iterable</tt> to be created
     * @param <T> the element type of the <tt>Iterable</tt> to be created
     * @return the <tt>Iterable</tt> to be created
     */
    public static @NotNull <T> Iterable<T> cons(@Nullable T x, @NotNull Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private boolean readHead = false;
            private final Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return !readHead || xsi.hasNext();
            }

            @Override
            public T next() {
                if (readHead) {
                    return xsi.next();
                } else {
                    readHead = true;
                    return x;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>(:)</tt> list constructor. Creates a <tt>String</tt> whose first character is
     * <tt>c</tt> and whose remaining characters are given by <tt>cs</tt>. Uses O(n) additional memory, where n is the
     * length of cs.
     *
     * <ul>
     *  <li><tt>c</tt> can be anything.</li>
     *  <li><tt>cs</tt> must be non-null.</li>
     *  <li>The result is a non-empty <tt>String</tt>.</li>
     * </ul>
     *
     * @param c the first character of the <tt>String</tt> to be created
     * @param cs the second-through-last characters of the <tt>String</tt> to be created
     * @return the <tt>String</tt> to be created
     */
    public static @NotNull String cons(char c, @NotNull String cs) {
        return Character.toString(c) + cs;
    }

    /**
     * Equivalent of Haskell's <tt>(++)</tt> operator. Creates an <tt>Iterable</tt> consisting of <tt>xs</tt>'s
     * elements followed by <tt>ys</tt>'s elements. <tt>xs</tt> may be infinite, in which case the result will be equal
     * to <tt>xs</tt>. <tt>ys</tt> may be infinite, in which case the result will also be infinite. Uses O(1)
     * additional memory. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li><tt>ys</tt> must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param ys another <tt>Iterable</tt>
     * @param <T> the element type of the <tt>Iterable</tt> to be created
     * @return <tt>xs</tt> concatenated with <tt>ys</tt>
     */
    public static @NotNull <T> Iterable<T> concat(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            private final Iterator<T> ysi = ys.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext() || ysi.hasNext();
            }

            @Override
            public T next() {
                return (xsi.hasNext() ? xsi : ysi).next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>(++)</tt> operator. Creates a <tt>String</tt> consisting of <tt>s</tt>'s characters
     * followed by <tt>t</tt>'s characters. Uses O(n+m) additional memory, where n is the length of <tt>s</tt> and m is
     * the length of <tt>t</tt>.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-null.</li>
     *  <li><tt>t</tt> must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @param t a <tt>String</tt>
     * @return <tt>s</tt> concatenated with <tt>t</tt>
     */
    public static @NotNull String concat(@NotNull String s, @NotNull String t) {
        return s + t;
    }

    /**
     * Equivalent of Haskell's <tt>head</tt> function. Returns the first element of an <tt>Iterable</tt>. Works on
     * infinite <tt>Iterable</tt>s. Uses O(1) additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return the <tt>Iterable</tt>'s first element
     */
    public static @Nullable <T> T head(@NotNull Iterable<T> xs) {
        return xs.iterator().next();
    }

    /**
     * Equivalent of Haskell's <tt>head</tt> function. Returns the first element of a <tt>List</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a <tt>List</tt>
     * @param <T> the <tt>List</tt>'s element type
     * @return the <tt>List</tt>'s first element
     */
    public static @Nullable <T> T head(@NotNull List<T> xs) {
        return xs.get(0);
    }

    /**
     * Equivalent of Haskell's <tt>head</tt> function. Returns the first element of a <tt>SortedSet</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a <tt>SortedSet</tt>
     * @param <T> the <tt>SortedSet</tt>'s element type
     * @return the <tt>SortedSet</tt>'s first element
     */
    public static @Nullable <T> T head(@NotNull SortedSet<T> xs) {
        return xs.first();
    }

    /**
     * Equivalent of Haskell's <tt>head</tt> function. Returns the first character of a <tt>String</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-empty.</li>
     *  <li>The result may be any <tt>char</tt>.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return the <tt>String</tt>'s first character
     */
    public static char head(@NotNull String s) {
        return s.charAt(0);
    }

    /**
     * Equivalent of Haskell's <tt>last</tt> function. Returns the last element of an <tt>Iterable</tt>. Only works on
     * finite <tt>Iterable</tt>s. Uses O(1) additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty and finite.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return the <tt>Iterable</tt>'s last element
     */
    public static @Nullable <T> T last(@NotNull Iterable<T> xs) {
        T previous = null;
        boolean empty = true;
        for (T x : xs) {
            empty = false;
            previous = x;
        }
        if (empty)
            throw new NoSuchElementException();
        return previous;
    }

    /**
     * Equivalent of Haskell's <tt>last</tt> function. Returns the last element of a <tt>List</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a <tt>List</tt>
     * @param <T> the <tt>List</tt>'s element type
     * @return the <tt>List</tt>'s last element
     */
    public static @Nullable <T> T last(@NotNull List<T> xs) {
        return xs.get(xs.size() - 1);
    }

    /**
     * Equivalent of Haskell's <tt>last</tt> function. Returns the last element of a <tt>SortedSet</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a <tt>SortedSet</tt>
     * @param <T> the <tt>SortedSet</tt>'s element type
     * @return the <tt>SortedSet</tt>'s last element
     */
    public static @Nullable <T> T last(@NotNull SortedSet<T> xs) {
        return xs.last();
    }

    /**
     * Equivalent of Haskell's <tt>last</tt> function. Returns the last character of a <tt>String</tt>. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-empty.</li>
     *  <li>The result may be any <tt>char</tt>.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return the <tt>String</tt>'s last character
     */
    public static char last(@NotNull String s) {
        return s.charAt(s.length() - 1);
    }

    /**
     * Equivalent of Haskell's <tt>tail</tt> function. Returns all elements of an <tt>Iterable</tt> but the first.
     * <tt>xs</tt> may be infinite, in which the result will also be infinite. Uses O(1) additional memory. The
     * <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return an <tt>Iterable</tt> containing all elements of <tt>xs</tt> but the first
     */
    public static @NotNull <T> Iterable<T> tail(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            throw new NoSuchElementException();
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            {
                xsi.next();
            }

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public T next() {
                return xsi.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>tail</tt> function. Given a <tt>String</tt>, returns a <tt>String</tt> containing
     * all of its characters but the first. Uses O(n) additional memory, where n is the length of <tt>s</tt>.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-empty.</li>
     *  <li>The result may be any <tt>char</tt>.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return a <tt>String</tt> containing all characters of <tt>s</tt> but the first
     */
    public static @NotNull String tail(@NotNull String s) {
        return s.substring(1);
    }

    /**
     * Equivalent of Haskell's <tt>init</tt> function. Returns all elements of an <tt>Iterable</tt> but the last.
     * <tt>xs</tt> may be infinite, in which the result will be <tt>xs</tt>. Uses O(1) additional memory. The
     * <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-empty.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return an <tt>Iterable</tt> containing all elements of <tt>xs</tt> but the last
     */
    public static @NotNull <T> Iterable<T> init(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            throw new NoSuchElementException();
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private final Iterator<T> xsi = xs.iterator();
                    private T next = xsi.next();

                    @Override
                    public boolean hasNext() {
                        return xsi.hasNext();
                    }

                    @Override
                    public T next() {
                        T oldNext = next;
                        next = xsi.next();
                        return oldNext;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("cannot remove from this iterator");
                    }
                };
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>tail</tt> function. Given a <tt>String</tt>, returns a <tt>String</tt> containing
     * all of its characters but the last. Uses O(n) additional memory, where n is the length of <tt>s</tt>.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-empty.</li>
     *  <li>The result may be any <tt>char</tt>.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return a <tt>String</tt> containing all characters of <tt>s</tt> but the last
     */
    public static @NotNull String init(@NotNull String s) {
        return s.substring(0, s.length() - 1);
    }

    /**
     * Equivalent of Haskell's <tt>null</tt> function. Tests whether an <tt>Iterable</tt> contains no elements.
     * <tt>xs</tt> may be infinite. Uses O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return whether <tt>xs</tt> is empty
     */
    public static <T> boolean isEmpty(@NotNull Iterable<T> xs) {
        return !xs.iterator().hasNext();
    }

    /**
     * Equivalent of Haskell's <tt>null</tt> function. Tests whether a <tt>Collection</tt> contains no elements. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param xs a <tt>Collection</tt>
     * @param <T> the <tt>Collection</tt>'s element type
     * @return whether <tt>xs</tt> is empty
     */
    public static <T> boolean isEmpty(@NotNull Collection<T> xs) {
        return xs.isEmpty();
    }

    /**
     * Equivalent of Haskell's <tt>null</tt> function. Tests whether a <tt>String</tt> contains no characters. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return whether <tt>s</tt> is empty
     */
    public static boolean isEmpty(@NotNull String s) {
        return s.isEmpty();
    }

    /**
     * Equivalent of Haskell's <tt>length</tt> function. Returns the number of elements in an <tt>Iterable</tt>. Only
     * works on finite <tt>Iterable</tt>s. Uses O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return the <tt>Iterable</tt>'s length
     */
    public static <T> int length(@NotNull Iterable<T> xs) {
        int i = 0;
        for (T x : xs) {
            i++;
        }
        return i;
    }

    /**
     * Equivalent of Haskell's <tt>length</tt> function. Returns the number of elements in an <tt>Iterable</tt>. Only
     * works on finite <tt>Iterable</tt>s. Uses O(log(n)) additional space, where n is <tt>xs</tt>'s length; but it's
     * effectively constant space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return the <tt>Iterable</tt>'s length
     */
    public static @NotNull <T> BigInteger bigIntegerLength(@NotNull Iterable<T> xs) {
        BigInteger bi = BigInteger.ZERO;
        for (T x : xs) {
            bi = bi.add(BigInteger.ONE);
        }
        return bi;
    }

    /**
     * Equivalent of Haskell's <tt>length</tt> function. Returns the number of elements in a <tt>Collection</tt>. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs a <tt>Collection</tt>
     * @param <T> the <tt>Collection</tt>'s element type
     * @return the <tt>Collection</tt>'s length
     */
    public static <T> int length(@NotNull Collection<T> xs) {
        return xs.size();
    }

    /**
     * Equivalent of Haskell's <tt>length</tt> function. Returns the number of characters in a <tt>String</tt>. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return the <tt>String</tt>'s length
     */
    public static int length(@NotNull String s) {
        return s.length();
    }

    /**
     * Equivalent of Haskell's <tt>map</tt> function. Transforms one <tt>Iterable</tt> into another by applying a
     * function to each element. <tt>xs</tt> may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>f</tt> must be non-null.</li>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li><tt>xs</tt> must only contain elements that are valid inputs for <tt>f</tt>.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param f the function that transforms each element in the <tt>Iterable</tt>
     * @param xs the <tt>Iterable</tt>
     * @param <A> the type of the original <tt>Iterable</tt>'s elements
     * @param <B> the type of the output <tt>Iterable</tt>'s elements
     * @return an <tt>Iterable</tt> containing the elements of <tt>xs</tt> transformed by <tt>f</tt>
     */
    public static @NotNull <A, B> Iterable<B> map(@NotNull Function<A, B> f, @NotNull Iterable<A> xs) {
        return () -> new Iterator<B>() {
            private final Iterator<A> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public B next() {
                return f.apply(xsi.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>map</tt> function. Transforms one <tt>String</tt> into another by applying a
     * function to each character. Uses O(n) additional memory, where n is the length of the input string.
     * additional memory.
     *
     * <ul>
     *  <li><tt>f</tt> must be non-null.</li>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li><tt>xs</tt> must only contain characters that are valid inputs for <tt>f</tt>.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param f the function that transforms each character in the <tt>String</tt>
     * @param s the <tt>String</tt>
     * @return a <tt>String</tt> containing the characters of <tt>s</tt> transformed by <tt>f</tt>
     */
    public static @NotNull String map(@NotNull Function<Character, Character> f, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(f.apply(s.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * Equivalent of Haskell's <tt>reverse</tt> function. Reverses an <tt>Iterable</tt>. <tt>xs</tt> must be finite.
     * Uses O(n) additional memory, where n is the length of <tt>xs</tt>. The resulting list may be modified, but the
     * modifications will not affect the original <tt>Iterable</tt>.
     *
     * <ul>
     *  <li><tt>xs</tt> must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return a <tt>List</tt> containing <tt>xs</tt>'s elements in reverse order
     */
    public static @NotNull <T> List<T> reverse(@NotNull Iterable<T> xs) {
        List<T> list = toList(xs);
        Collections.reverse(list);
        return list;
    }

    /**
     * Equivalent of Haskell's <tt>reverse</tt> function. Reverses a <tt>String</tt>. Uses O(n) additional memory,
     * where n is the length of <tt>s</tt>.
     *
     * <ul>
     *  <li><tt>s</tt> must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param s a <tt>String</tt>
     * @return a <tt>String</tt> containing <tt>s</tt>'s characters in reverse order
     */
    public static @NotNull String reverse(@NotNull String s) {
        char[] reversed = new char[s.length()];
        for (int i = 0; i < s.length() / 2; i++) {
            int j = s.length() - i - 1;
            reversed[i] = s.charAt(j);
            reversed[j] = s.charAt(i);
        }
        if ((s.length() & 1) == 1) {
            int i = s.length() / 2;
            reversed[i] = s.charAt(i);
        }
        return new String(reversed);
    }

    /**
     * Equivalent of Haskell's <tt>intersperse</tt> function. Given an <tt>Iterable</tt> <tt>xs</tt> and a seperator
     * <tt>sep</tt>, returns an <tt>Iterable</tt> consisting of the elements of <tt>xs</tt> with <tt>sep</tt> between
     * every adjacent pair. <tt>xs</tt> may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>sep</tt> may be anything.</li>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li>The result is an <tt>Iterable</tt> whose odd-indexed (using 0-based indexing) elements are identical.</li>
     * </ul>
     *
     * @param sep a separator
     * @param xs an <tt>Iterable</tt>
     * @param <T> the <tt>Iterable</tt>'s element type
     * @return an <tt>Iterable</tt> consisting of the elements of <tt>xs</tt> interspersed with <tt>sep</tt>
     */
    public static @NotNull <T> Iterable<T> intersperse(@Nullable T sep, @NotNull Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            private boolean separating = false;

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public T next() {
                if (separating) {
                    separating = false;
                    return sep;
                } else {
                    separating = true;
                    return xsi.next();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>intersperse</tt> function. Given a <tt>String</tt> <tt>s</tt> and a seperator
     * <tt>sep</tt>, returns a <tt>String</tt> consisting of the characters of <tt>s</tt> with <tt>sep</tt> between
     * every adjacent pair. Uses O(n) additional memory, where n is the length of <tt>s</tt>.
     *
     * <ul>
     *  <li><tt>sep</tt> may be any <tt>char</tt>.</li>
     *  <li><tt>s</tt> must be non-null.</li>
     *  <li>The result is a <tt>String</tt> whose odd-indexed (using 0-based indexing) characters are identical.</li>
     * </ul>
     *
     * @param sep a separator
     * @param s a <tt>String</tt>
     * @return a <tt>String</tt> consisting of the characters of <tt>s</tt> interspersed with <tt>sep</tt>
     */
    public static @NotNull String intersperse(char sep, @NotNull String s) {
        if (s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            sb.append(sep);
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Equivalent of Haskell's <tt>intercalate</tt> function. Inserts an <tt>Iterable</tt> between every two adjacent
     * <tt>Iterable</tt>s in an <tt>Iterable</tt> of <tt>Iterable</tt>s, flattening the result. <tt>xss</tt>, any
     * element of <tt>xss</tt>, or <tt>xs</tt> may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>xs</tt> must be non-null.</li>
     *  <li><tt>xss</tt> must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs the separating <tt>Iterable</tt>
     * @param xss the separated <tt>Iterable</tt>
     * @param <T> the resulting <tt>Iterable</tt>'s element type
     * @return <tt>xss</tt> intercalated by <tt>xs</tt>
     */
    public static @NotNull <T> Iterable<T> intercalate(@NotNull Iterable<T> xs, @NotNull Iterable<Iterable<T>> xss) {
        return concat(intersperse(xs, xss));
    }

    /**
     * Equivalent of Haskell's <tt>intercalate</tt> function. Inserts a <tt>String</tt> between every two adjacent
     * <tt>String</tt>s in an <tt>Iterable</tt> of <tt>String</tt>s, flattening the result. Uses O(abc) additional
     * memory, where a is the length of <tt>strings</tt>, b is the maximum length of any string in <tt>strings</tt>,
     * and c is the length of <tt>sep</tt>.
     * The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>sep</tt> must be non-null.</li>
     *  <li><tt>strings</tt> must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param sep the separating <tt>String</tt>
     * @param strings the separated <tt>String</tt>s
     * @return <tt>strings</tt> intercalated by <tt>sep</tt>
     */
    public static @NotNull String intercalate(@NotNull String sep, @NotNull Iterable<String> strings) {
        return concatStrings(intersperse(sep, strings));
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>Iterables</tt>. If the rows have different lengths, then the "overhanging" elements still end up in the
     * result. See test cases for examples. Any element of <tt>xss</tt> may be infinite, in which case the result will
     * be infinite. Uses O(nm) additional memory, where n is then length of <tt>xss</tt> and m is the largest amount of
     * memory used by any <tt>Iterable</tt> in <tt>xss</tt>. The <tt>Iterable</tt> produced does not support removing
     * elements.
     *
     * <ul>
     *  <li><tt>xss</tt> must be finite.</li>
     *  <li>The lengths of the result's elements are non-increasing and never 0.</li>
     * </ul>
     *
     * @param xss an <tt>Iterable</tt> of <tt>Iterable</tt>s
     * @param <T> the <tt>Iterable</tt>'s elements' element type
     * @return <tt>xss</tt>, transposed
     */
    public static @NotNull <T> Iterable<Iterable<T>> transpose(@NotNull Iterable<Iterable<T>> xss) {
        return () -> new Iterator<Iterable<T>>() {
            private List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

            @Override
            public boolean hasNext() {
                return any(Iterator::hasNext, iterators);
            }

            @Override
            public Iterable<T> next() {
                List<T> nextList = new ArrayList<>();
                for (Iterator<T> iterator : iterators) {
                    if (iterator.hasNext()) {
                        nextList.add(iterator.next());
                    }
                }
                return nextList;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>String</tt>s. If the rows have different lengths, then the "overhanging" characters still end up in the
     * result. See test cases for examples. Uses O(nm) additional memory, where n is then length of <tt>xss</tt> and m
     * is the length of the longest <tt>String</tt> in <tt>xss</tt>. The <tt>Iterable</tt> produced does not support
     * removing elements.
     *
     * <ul>
     *  <li><tt>strings</tt> must be non-null.</li>
     *  <li>The lengths of the result's elements are non-increasing and never 0.</li>
     * </ul>
     *
     * @param strings an <tt>Iterable</tt> of <tt>String</tt>s
     * @return <tt>strings</tt>, transposed
     */
    public static @NotNull Iterable<String> transposeStrings(@NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transpose(map(s -> fromString(s), strings))
        );
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>Iterables</tt>. If the rows have different lengths, then the "overhanging" elements will be truncated; the
     * result's rows will all have equal lengths. See test cases for examples. Any element of <tt>xss</tt> may be
     * infinite, in which case the result will be infinite. Uses O(nm) additional memory, where n is then length of
     * <tt>xss</tt> and m is the largest amount of memory used by any <tt>Iterable</tt> in <tt>xss</tt>. The
     * <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>xss</tt> must be finite.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * @param xss an <tt>Iterable</tt> of <tt>Iterable</tt>s
     * @param <T> the <tt>Iterable</tt>'s elements' element type
     * @return <tt>xss</tt>, transposed
     */
    public static @NotNull <T> Iterable<Iterable<T>> transposeTruncating(@NotNull Iterable<Iterable<T>> xss) {
        return () -> new Iterator<Iterable<T>>() {
            private List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

            @Override
            public boolean hasNext() {
                return !iterators.isEmpty() && all(Iterator::hasNext, iterators);
            }

            @Override
            public Iterable<T> next() {
                List<T> nextList = new ArrayList<>();
                for (Iterator<T> iterator : iterators) {
                    nextList.add(iterator.next());
                }
                return nextList;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>String</tt>s. If the rows have different lengths, then the "overhanging" characters will be truncated. See
     * test cases for examples. Uses O(nm) additional memory, where n is then length of <tt>xss</tt> and m is the
     * length of the longest <tt>String</tt> in <tt>xss</tt>. The <tt>Iterable</tt> produced does not support removing
     * elements.
     *
     * <ul>
     *  <li><tt>strings</tt> must be non-null.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * @param strings an <tt>Iterable</tt> of <tt>String</tt>s
     * @return <tt>strings</tt>, transposed
     */
    public static @NotNull Iterable<String> transposeStringsTruncating(@NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transposeTruncating(map(s -> fromString(s), strings))
        );
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>Iterables</tt>. If the rows have different lengths, then the gaps will be padded; the result's rows will all
     * have equal lengths. See test cases for examples. Any element of <tt>xss</tt> may be infinite, in which case the
     * result will be infinite. Uses O(nm) additional memory, where n is then length of <tt>xss</tt> and m is the
     * largest amount of memory used by any <tt>Iterable</tt> in <tt>xss</tt>. The <tt>Iterable</tt> produced does not
     * support removing elements.
     *
     * <ul>
     *  <li><tt>xss</tt> must be finite.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * @param xss an <tt>Iterable</tt> of <tt>Iterable</tt>s
     * @param pad the padding
     * @param <T> the <tt>Iterable</tt>'s elements' element type
     * @return <tt>xss</tt>, transposed
     */
    public static @NotNull <T> Iterable<Iterable<T>>
    transposePadded(@Nullable T pad, @NotNull Iterable<Iterable<T>> xss) {
        return () -> new Iterator<Iterable<T>>() {
            private List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

            @Override
            public boolean hasNext() {
                return any(Iterator::hasNext, iterators);
            }

            @Override
            public Iterable<T> next() {
                List<T> nextList = new ArrayList<>();
                for (Iterator<T> iterator : iterators) {
                    nextList.add(iterator.hasNext() ? iterator.next() : pad);
                }
                return nextList;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Equivalent of Haskell's <tt>transpose</tt> function. Swaps rows and columns of an <tt>Iterable</tt> of
     * <tt>String</tt>s. If the rows have different lengths, then the gaps will be padded; the result's rows will all
     * have equal lengths. Uses O(nm) additional memory, where n is then length of <tt>xss</tt> and m is the length of
     * the longest <tt>String</tt> in <tt>xss</tt>. The <tt>Iterable</tt> produced does not support removing elements.
     *
     * <ul>
     *  <li><tt>strings</tt> must be non-null.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * @param strings an <tt>Iterable</tt> of <tt>String</tt>s
     * @return <tt>strings</tt>, transposed
     */
    public static @NotNull Iterable<String> transposeStringsPadded(char pad, @NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transposePadded(pad, map(s -> fromString(s), strings))
        );
    }

    public static <T> Iterable<T> concat(Iterable<Iterable<T>> xss) {
        return () -> new Iterator<T>() {
            final Iterator<Iterable<T>> xssi = xss.iterator();
            Iterator<T> xsi = xssi.hasNext() ? xssi.next().iterator() : null;

            @Override
            public boolean hasNext() {
                if (xsi == null) return false;
                while (!xsi.hasNext()) {
                    if (!xssi.hasNext()) return false;
                    xsi = xssi.next().iterator();
                }
                return true;
            }

            @Override
            public T next() {
                hasNext();
                return xsi.next();
            }
        };
    }

    public static String concatStrings(Iterable<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static <A, B> Iterable<B> concatMap(Function<A, Iterable<B>> f, Iterable<A> xs) {
        return concat(map(f, xs));
    }

    public static boolean and(Iterable<Boolean> xs) {
        for (boolean x : xs) {
            if (!x) return false;
        }
        return true;
    }

    public static boolean or(Iterable<Boolean> xs) {
        for (boolean x : xs) {
            if (x) return true;
        }
        return false;
    }

    public static <T> boolean any(Predicate<T> predicate, Iterable<T> xs) {
        for (T x : xs) {
            if (predicate.test(x)) return true;
        }
        return false;
    }

    public static <T> boolean all(Predicate<T> predicate, Iterable<T> xs) {
        for (T x : xs) {
            if (!predicate.test(x)) return false;
        }
        return true;
    }

    public static byte sumByte(Iterable<Byte> xs) {
        byte sum = 0;
        for (byte x : xs) {
            sum += x;
        }
        return sum;
    }

    public static short sumShort(Iterable<Short> xs) {
        short sum = 0;
        for (short x : xs) {
            sum += x;
        }
        return sum;
    }

    public static int sumInteger(Iterable<Integer> xs) {
        int sum = 0;
        for (int x : xs) {
            sum += x;
        }
        return sum;
    }

    public static long sumLong(Iterable<Long> xs) {
        long sum = 0;
        for (long x : xs) {
            sum += x;
        }
        return sum;
    }

    public static float sumFloat(Iterable<Float> xs) {
        float sum = 0;
        for (float x : xs) {
            sum += x;
        }
        return sum;
    }

    public static double sumDouble(Iterable<Double> xs) {
        double sum = 0;
        for (double x : xs) {
            sum += x;
        }
        return sum;
    }

    public static BigInteger sumBigInteger(Iterable<BigInteger> xs) {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger x : xs) {
            sum = sum.add(x);
        }
        return sum;
    }

    public static BigDecimal sumBigDecimal(Iterable<BigDecimal> xs) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal x : xs) {
            sum = sum.add(x);
        }
        return sum;
    }

    public static byte productByte(Iterable<Byte> xs) {
        byte product = 1;
        for (byte x : xs) {
            product *= x;
        }
        return product;
    }

    public static short productShort(Iterable<Short> xs) {
        short product = 1;
        for (short x : xs) {
            product *= x;
        }
        return product;
    }

    public static int productInteger(Iterable<Integer> xs) {
        int product = 1;
        for (int x : xs) {
            product *= x;
        }
        return product;
    }

    public static long productLong(Iterable<Long> xs) {
        long product = 1;
        for (long x : xs) {
            product *= x;
        }
        return product;
    }

    public static float productFloat(Iterable<Float> xs) {
        float product = 1;
        for (float x : xs) {
            product *= x;
        }
        return product;
    }

    public static double productDouble(Iterable<Double> xs) {
        double product = 1;
        for (double x : xs) {
            product *= x;
        }
        return product;
    }

    public static BigInteger productBigInteger(Iterable<BigInteger> xs) {
        BigInteger product = BigInteger.ONE;
        for (BigInteger x : xs) {
            product = product.multiply(x);
        }
        return product;
    }

    public static BigDecimal productBigDecimal(Iterable<BigDecimal> xs) {
        BigDecimal product = BigDecimal.ONE;
        for (BigDecimal x : xs) {
            product = product.multiply(x);
        }
        return product;
    }

    public static <T extends Comparable<T>> T maximum(Iterable<T> xs) {
        T max = null;
        for (T x : xs) {
            if (gt(x, max)) {
                max = x;
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> T minimum(Iterable<T> xs) {
        T min = null;
        for (T x : xs) {
            if (lt(x, min)) {
                min = x;
            }
        }
        return min;
    }

    public static <T> Iterable<T> iterate(Function<T, T> f, T x) {
        return () -> new Iterator<T>() {
            private T current = x;
            private boolean firstTime = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (firstTime) {
                    firstTime = false;
                } else {
                    current = f.apply(current);
                }
                return current;
            }
        };
    }

    public static Iterable<Byte> range(byte a) {
        return range(a, Byte.MAX_VALUE);
    }

    public static Iterable<Short> range(short a) {
        return range(a, Short.MAX_VALUE);
    }

    public static Iterable<Integer> range(int a) {
        return range(a, Integer.MAX_VALUE);
    }

    public static Iterable<Long> range(long a) {
        return range(a, Long.MAX_VALUE);
    }

    public static Iterable<BigInteger> range(BigInteger a) {
        return iterate(bi -> bi.add(BigInteger.ONE), a);
    }

    public static Iterable<Byte> range(byte a, byte b) {
        return () -> new Iterator<Byte>() {
            private byte x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Byte next() {
                reachedEnd = x == b;
                return x++;
            }
        };
    }

    public static Iterable<Short> range(short a, short b) {
        return () -> new Iterator<Short>() {
            private short x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Short next() {
                reachedEnd = x == b;
                return x++;
            }
        };
    }

    public static Iterable<Integer> range(int a, int b) {
        return () -> new Iterator<Integer>() {
            private int x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Integer next() {
                reachedEnd = x == b;
                return x++;
            }
        };
    }

    public static Iterable<Long> range(long a, long b) {
        return () -> new Iterator<Long>() {
            private long x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Long next() {
                reachedEnd = x == b;
                return x++;
            }
        };
    }

    public static Iterable<BigInteger> range(BigInteger a, BigInteger b) {
        return () -> new Iterator<BigInteger>() {
            private BigInteger x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public BigInteger next() {
                reachedEnd = x.equals(b);
                BigInteger oldX = x;
                x = x.add(BigInteger.ONE);
                return oldX;
            }
        };
    }

    public static Iterable<Byte> rangeBy(byte a, byte i) {
        return () -> new Iterator<Byte>() {
            private byte x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Byte next() {
                byte oldX = x;
                x += i;
                reachedEnd = i > 0 ? x < a : x > a;
                return oldX;
            }
        };
    }

    public static Iterable<Short> rangeBy(short a, short i) {
        return () -> new Iterator<Short>() {
            private short x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Short next() {
                short oldX = x;
                x += i;
                reachedEnd = i > 0 ? x < a : x > a;
                return oldX;
            }
        };
    }

    public static Iterable<Integer> rangeBy(int a, int i) {
        return () -> new Iterator<Integer>() {
            private int x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Integer next() {
                int oldX = x;
                x += i;
                reachedEnd = i > 0 ? x < a : x > a;
                return oldX;
            }
        };
    }

    public static Iterable<Long> rangeBy(long a, long i) {
        return () -> new Iterator<Long>() {
            private long x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Long next() {
                long oldX = x;
                x += i;
                reachedEnd = i > 0 ? x < a : x > a;
                return oldX;
            }
        };
    }

    public static Iterable<BigInteger> rangeBy(BigInteger a, BigInteger i) {
        return () -> new Iterator<BigInteger>() {
            private BigInteger x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public BigInteger next() {
                BigInteger oldX = x;
                x = x.add(i);
                reachedEnd = i.signum() == 1 ? lt(x, a) : gt(x, a);
                return oldX;
            }
        };
    }

    public static Iterable<Byte> rangeBy(byte a, byte i, byte b) {
        return () -> new Iterator<Byte>() {
            private byte x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Byte next() {
                byte oldX = x;
                x += i;
                reachedEnd = i > 0 ? (x > b || x < a) : (x < b || x > a);
                return oldX;
            }
        };
    }

    public static Iterable<Short> rangeBy(short a, short i, short b) {
        return () -> new Iterator<Short>() {
            private short x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Short next() {
                short oldX = x;
                x += i;
                reachedEnd = i > 0 ? (x > b || x < a) : (x < b || x > a);
                return oldX;
            }
        };
    }

    public static Iterable<Integer> rangeBy(int a, int i, int b) {
        return () -> new Iterator<Integer>() {
            private int x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Integer next() {
                int oldX = x;
                x += i;
                reachedEnd = i > 0 ? (x > b || x < a) : (x < b || x > a);
                return oldX;
            }
        };
    }

    public static Iterable<Long> rangeBy(long a, long i, long b) {
        return () -> new Iterator<Long>() {
            private long x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Long next() {
                long oldX = x;
                x += i;
                reachedEnd = i > 0 ? (x > b || x < a) : (x < b || x > a);
                return oldX;
            }
        };
    }

    public static Iterable<BigInteger> rangeBy(BigInteger a, BigInteger i, BigInteger b) {
        return () -> new Iterator<BigInteger>() {
            private BigInteger x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public BigInteger next() {
                BigInteger oldX = x;
                x = x.add(i);
                reachedEnd = i.signum() == 1 ? gt(x, b) : lt(x, b);
                return oldX;
            }
        };
    }

    public static <T> Iterable<T> repeat(T x) {
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return x;
            }
        };
    }

    public static <T> Iterable<T> replicate(int n, T x) {
        return () -> new Iterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < n;
            }

            @Override
            public T next() {
                i++;
                return x;
            }
        };
    }

    public static <T> Iterable<T> replicate(BigInteger n, T x) {
        return () -> new Iterator<T>() {
            private BigInteger i = BigInteger.ZERO;

            @Override
            public boolean hasNext() {
                return lt(i, n);
            }

            @Override
            public T next() {
                i = i.add(BigInteger.ONE);
                return x;
            }
        };
    }

    public static <T> Iterable<T> cycle(Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (!xsi.hasNext()) xsi = xs.iterator();
                return xsi.next();
            }
        };
    }

    public static <T> Iterable<T> take(int n, Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private int i = 0;
            private final Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return i < n && xsi.hasNext();
            }

            @Override
            public T next() {
                i++;
                return xsi.next();
            }
        };
    }

    public static <T> Iterable<T> take(BigInteger n, Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private BigInteger i = BigInteger.ZERO;
            private final Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return lt(i, n) && xsi.hasNext();
            }

            @Override
            public T next() {
                i = i.add(BigInteger.ONE);
                return xsi.next();
            }
        };
    }

    public static <T> Iterable<T> drop(int n, Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            {
                int i = n;
                while (xsi.hasNext()) {
                    if (i <= 0) break;
                    xsi.next();
                    i--;
                }
            }

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public T next() {
                return xsi.next();
            }
        };
    }

    public static <T> Iterable<T> drop(BigInteger n, Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            {
                BigInteger i = n;
                while (xsi.hasNext()) {
                    if (le(i, BigInteger.ZERO)) break;
                    xsi.next();
                    i = i.subtract(BigInteger.ONE);
                }
            }

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public T next() {
                return xsi.next();
            }
        };
    }

    public static <T> Pair<Iterable<T>, Iterable<T>> splitAt(int n, Iterable<T> xs) {
        return new Pair<>(take(n, xs), drop(n, xs));
    }

    public static <T> Pair<Iterable<T>, Iterable<T>> splitAt(BigInteger n, Iterable<T> xs) {
        return new Pair<>(take(n, xs), drop(n, xs));
    }

    public static <T> Iterable<T> takeWhile(Predicate<T> p, Iterable<T> xs) {
        return () -> new Iterator<T>() {
            private final Iterator<T> xsi = xs.iterator();
            private T next;
            private boolean hasNext;
            {
                advanceNext();
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                T current = next;
                advanceNext();
                return current;
            }

            private void advanceNext() {
                if (xsi.hasNext()) {
                    next = xsi.next();
                    hasNext = p.test(next);
                } else {
                    hasNext = false;
                }
            }
        };
    }

    public static <T> Iterable<Iterable<T>> chunk(int size, Iterable<T> xs) {
        return () -> new Iterator<Iterable<T>>() {
            private Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> chunk = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    if (!xsi.hasNext()) break;
                    chunk.add(xsi.next());
                }
                return chunk;
            }
        };
    }

    public static <T> Iterable<String> chunk(int size, String s) {
        return () -> new Iterator<String>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i != s.length();
            }

            @Override
            public String next() {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    if (i == s.length()) break;
                    sb.append(s.charAt(i++));
                }
                return sb.toString();
            }
        };
    }

    public static <T> Iterable<Iterable<T>> chunkPadded(T pad, int size, Iterable<T> xs) {
        return () -> new Iterator<Iterable<T>>() {
            private Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> chunk = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    chunk.add(xsi.hasNext() ? xsi.next() : pad);
                }
                return chunk;
            }
        };
    }

    public static <T> Iterable<T> mux(List<Iterable<T>> xss) {
        return concat(transpose(xss));
    }

    public static <T> List<Iterable<T>> demux(int lines, Iterable<T> xs) {
        List<Iterable<T>> demuxed = new ArrayList<>();
        for (int i = 0; i < lines; i++) {
            Iterable<Boolean> mask = concat(replicate(i, false), cycle(cons(true, replicate(lines - 1, false))));
            demuxed.add(select(mask, xs));
        }
        return demuxed;
    }

//    public static <T> List<Iterable<T>> demuxPadded(T pad, int lines, Iterable<T> xs) {
//        return transpose(chunkPadded(pad, lines, xs));
//    }

    public static <T> Iterable<T> filter(Predicate<T> p, Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private final Iterator<T> xsi = xs.iterator();
                    private T next;
                    private boolean hasNext;

                    {
                        advanceNext();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T current = next;
                        advanceNext();
                        return current;
                    }

                    private void advanceNext() {
                        while (xsi.hasNext()) {
                            next = xsi.next();
                            if (p.test(next)) {
                                hasNext = true;
                                return;
                            }
                        }
                        hasNext = false;
                    }
                };
            }
        };
    }

    public static <T> T get(Iterable<T> xs, int i) {
        if (i < 0)
            throw new IndexOutOfBoundsException();
        Iterator<T> xsi = xs.iterator();
        T element = null;
        for (int j = 0; j <= i; j++) {
            if (!xsi.hasNext())
                throw new IndexOutOfBoundsException();
            element = xsi.next();
        }
        return element;
    }

    public static <T> T get(Iterable<T> xs, BigInteger i) {
        if (lt(i, BigInteger.ZERO))
            throw new IndexOutOfBoundsException();
        Iterator<T> xsi = xs.iterator();
        T element = null;
        for (BigInteger j = BigInteger.ZERO; le(j, i); j = j.add(BigInteger.ONE)) {
            if (!xsi.hasNext())
                throw new IndexOutOfBoundsException();
            element = xsi.next();
        }
        return element;
    }

    public static <T> T get(List<T> xs, int i) {
        return xs.get(i);
    }

    public static char get(String s, int i) {
        return s.charAt(i);
    }

    public static <T> Iterable<T> select(Iterable<Boolean> bs, Iterable<T> xs) {
        return map(p -> p.b, filter(p -> p.a, (Iterable<Pair<Boolean, T>>) zip(bs, xs)));
    }

    public static <A, B> Iterable<Pair<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        return () -> new Iterator<Pair<A, B>>() {
            private final Iterator<A> asi = as.iterator();
            private final Iterator<B> bsi = bs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() && bsi.hasNext();
            }

            @Override
            public Pair<A, B> next() {
                return new Pair<>(asi.next(), bsi.next());
            }
        };
    }

    public static <A, B, C> Iterable<Triple<A, B, C>> zip3(Iterable<A> as, Iterable<B> bs, Iterable<C> cs) {
        return () -> new Iterator<Triple<A, B, C>>() {
            private final Iterator<A> asi = as.iterator();
            private final Iterator<B> bsi = bs.iterator();
            private final Iterator<C> csi = cs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() && bsi.hasNext() && csi.hasNext();
            }

            @Override
            public Triple<A, B, C> next() {
                return new Triple<>(asi.next(), bsi.next(), csi.next());
            }
        };
    }

    public static <A, B, C, D> Iterable<Quadruple<A, B, C, D>> zip4(Iterable<A> as, Iterable<B> bs, Iterable<C> cs, Iterable<D> ds) {
        return () -> new Iterator<Quadruple<A, B, C, D>>() {
            private Iterator<A> asi = as.iterator();
            private Iterator<B> bsi = bs.iterator();
            private Iterator<C> csi = cs.iterator();
            private Iterator<D> dsi = ds.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() && bsi.hasNext() && csi.hasNext() && dsi.hasNext();
            }

            @Override
            public Quadruple<A, B, C, D> next() {
                return new Quadruple<>(asi.next(), bsi.next(), csi.next(), dsi.next());
            }
        };
    }

    public static <A, B> Iterable<Pair<A, B>> zipPadded(A aPad, B bPad, Iterable<A> as, Iterable<B> bs) {
        return () -> new Iterator<Pair<A, B>>() {
            private final Iterator<A> asi = as.iterator();
            private final Iterator<B> bsi = bs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() || bsi.hasNext();
            }

            @Override
            public Pair<A, B> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                return new Pair<>(a, b);
            }
        };
    }

    public static <A, B, C> Iterable<Triple<A, B, C>> zip3Padded(A aPad, B bPad, C cPad, Iterable<A> as, Iterable<B> bs, Iterable<C> cs) {
        return () -> new Iterator<Triple<A, B, C>>() {
            private final Iterator<A> asi = as.iterator();
            private final Iterator<B> bsi = bs.iterator();
            private final Iterator<C> csi = cs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() || bsi.hasNext() || csi.hasNext();
            }

            @Override
            public Triple<A, B, C> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                C c = csi.hasNext() ? csi.next() : cPad;
                return new Triple<>(a, b, c);
            }
        };
    }

    public static <A, B, C, D> Iterable<Quadruple<A, B, C, D>> zip4Padded(A aPad, B bPad, C cPad, D dPad, Iterable<A> as, Iterable<B> bs, Iterable<C> cs, Iterable<D> ds) {
        return () -> new Iterator<Quadruple<A, B, C, D>>() {
            private Iterator<A> asi = as.iterator();
            private Iterator<B> bsi = bs.iterator();
            private Iterator<C> csi = cs.iterator();
            private Iterator<D> dsi = ds.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() || bsi.hasNext() || csi.hasNext() || dsi.hasNext();
            }

            @Override
            public Quadruple<A, B, C, D> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                C c = csi.hasNext() ? csi.next() : cPad;
                D d = dsi.hasNext() ? dsi.next() : dPad;
                return new Quadruple<>(a, b, c, d);
            }
        };
    }

    public static <A, B, O> Iterable<O> zipWith(
            Function<Pair<A, B>, O> f,
            Iterable<A> as,
            Iterable<B> bs
    ) {
        return map(f, zip(as, bs));
    }

    public static <A, B, C, O> Iterable<O> zipWith3(
            Function<Triple<A, B, C>, O> f,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs
    ) {
        return map(f, zip3(as, bs, cs));
    }

    public static <A, B, C, D, O> Iterable<O> zipWith4(
            Function<Quadruple<A, B, C, D>, O> f,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds
    ) {
        return map(f, zip4(as, bs, cs, ds));
    }

    public static <A, B, O> Iterable<O> zipWithPadded(
            Function<Pair<A, B>, O> f,
            A aPad,
            B bPad,
            Iterable<A> as,
            Iterable<B> bs
    ) {
        return map(f, zipPadded(aPad, bPad, as, bs));
    }

    public static <A, B, C, O> Iterable<O> zipWith3Padded(
            Function<Triple<A, B, C>, O> f,
            A aPad,
            B bPad,
            C cPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs
    ) {
        return map(f, zip3Padded(aPad, bPad, cPad, as, bs, cs));
    }

    public static <A, B, C, D, O> Iterable<O> zipWith4Padded(
            Function<Quadruple<A, B, C, D>, O> f,
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds
    ) {
        return map(f, zip4Padded(aPad, bPad, cPad, dPad, as, bs, cs, ds));
    }

    public static <A, B> Pair<Iterable<A>, Iterable<B>> unzip(Iterable<Pair<A, B>> ps) {
        return new Pair<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps)
        );
    }

    public static <A, B, C> Triple<Iterable<A>, Iterable<B>, Iterable<C>> unzip3(Iterable<Triple<A, B, C>> ps) {
        return new Triple<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps),
                map(p -> p.c, ps)
        );
    }

    public static <A, B, C, D> Quadruple<
            Iterable<A>,
            Iterable<B>,
            Iterable<C>,
            Iterable<D>
            > unzip4(Iterable<Quadruple<A, B, C, D>> ps) {
        return new Quadruple<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps),
                map(p -> p.c, ps),
                map(p -> p.d, ps)
        );
    }
}
