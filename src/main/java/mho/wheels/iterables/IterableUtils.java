package mho.wheels.iterables;

import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static mho.wheels.ordering.Ordering.*;

/**
 * Methods for generating and manipulating {@link Iterable}s. The equivalents of every function in Haskell's
 * {@code Data.List} module may be found here (except for {@code permutations} and {@code subsequences}, which are in
 * {@link IterableProvider}).
 */
public final strictfp class IterableUtils {
    /**
     * Disallow instantiation
     */
    private IterableUtils() {}

    //todo docs
    public static @NotNull <T> Iterable<T> fromSupplier(@NotNull Supplier<T> supplier) {
        return () -> new NoRemoveIterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return supplier.get();
            }
        };
    }

    public static @NotNull <T> Supplier<T> toSupplier(@NotNull Iterable<T> xs) {
        return new Supplier<T>() {
            private final @NotNull Iterator<T> it = xs.iterator();

            @Override
            public T get() {
                return it.next();
            }
        };
    }

    /**
     * Adds an {@code Iterable}'s elements to a {@link Collection}, in the order that the elements appear in the
     * {@code Iterable}. Only works for finite {@code Iterable}s.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>{@code collection} must be non-null.</li>
     *  <li>{@code collection} must be able to hold every element of {@code xs}.</li>
     * </ul>
     *
     * @param xs the {@code Iterable}
     * @param collection the {@code Collection} to which the {@code Iterable}'s elements are added
     * @param <T> the {@code Iterable}'s element type
     */
    public static <T> void addTo(@NotNull Iterable<T> xs, @NotNull Collection<T> collection) {
        for (T x : xs) {
            collection.add(x);
        }
    }

    /**
     * Adds a {@code String}'s characters to a {@code Collection}, in the order that the characters appear in the
     * {@code String}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>{@code collection} must be non-null.</li>
     *  <li>{@code collection} must be able to hold every character of {@code s}.</li>
     * </ul>
     *
     * @param s the string
     * @param collection the collection to which the {@code String}'s characters are added
     */
    public static void addTo(@NotNull String s, @NotNull Collection<Character> collection) {
        for (int i = 0; i < s.length(); i++) {
            collection.add(s.charAt(i));
        }
    }

    /**
     * Converts an {@code Iterable} to a {@link List}. Only works for finite {@code Iterable}s. The resulting list may
     * be modified, but the modifications will not affect the original {@code Iterable}.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs the {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return a {@code List} containing the elements of the {@code Iterable} in their original order
     */
    public static @NotNull <T> List<T> toList(@NotNull Iterable<T> xs) {
        List<T> list = new ArrayList<>();
        addTo(xs, list);
        return list;
    }

    public static @NotNull <T> List<T> toList(@NotNull List<T> xs) {
        return new ArrayList<>(xs);
    }

    /**
     * Converts an {@code Iterable} to a {@code List}. Only works for finite {@code Iterable}s.
     *
     * <ul>
     *  <li>{@code s} may be any {@code String}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param s the {@code String}
     * @return a {@code List} containing the characters of {@code s} in their original order
     */
    public static @NotNull List<Character> toList(@NotNull String s) {
        List<Character> list = new ArrayList<>();
        addTo(s, list);
        return list;
    }

    public static @NotNull <T> HashSet<T> toHashSet(@NotNull Iterable<T> xs) {
        HashSet<T> set = new HashSet<>();
        addTo(xs, set);
        return set;
    }

    public static @NotNull <T extends Comparable<T>> TreeSet<T> toTreeSet(@NotNull Iterable<T> xs) {
        TreeSet<T> set = new TreeSet<>();
        addTo(xs, set);
        return set;
    }

    /**
     * Creates a {@code String} representation of {@code xs}. Each element is converted to a {@code String} and
     * those {@code String}s are placed in a comma-separated list surrounded by square brackets. Only works for finite
     * {@code Iterable}s.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result begins with {@code '['} and ends with {@code ']'}.</li>
     * </ul>
     *
     * @param xs the {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return a {@code String} representation of {@code xs}
     */
    public static @NotNull <T> String toString(@NotNull Iterable<T> xs) {
        return toList(xs).toString();
    }

    /**
     * Creates a {@code String} representation of {@code xs}, displaying at most {@code size} elements. The first
     * {@code size} elements are converted to a {@code String} and those {@code String}s are placed in a
     * comma-separated list surrounded by square brackets. If the {@code Iterable} contains more than {@code size}
     * elements, an ellipsis ({@code ...}) is added at the end of the list.
     *
     * <ul>
     *  <li>{@code size} cannot be negative.</li>
     *  <li>{@code xs} may be any {@code Iterable}.</li>
     *  <li>The result begins with {@code '['} and ends with {@code ']'}.</li>
     * </ul>
     *
     * @param size the maximum number of elements displayed
     * @param xs the {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return a {@code String} representation of {@code xs}
     */
    public static @NotNull <T> String toString(int size, @NotNull Iterable<T> xs) {
        if (size < 0)
            throw new IllegalArgumentException("size cannot be negative");
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        String prefix = "";
        Iterator<T> it = xs.iterator();
        int i = 0;
        while (it.hasNext() && i < size) {
            sb.append(prefix);
            sb.append(it.next());
            prefix = ", ";
            i++;
        }
        if (it.hasNext()) {
            sb.append(prefix);
            sb.append("...");
        }
        sb.append(']');
        return sb.toString();
    }

    public static @NotNull <T> List<String> itsList(int size, @NotNull Iterable<T> xs) {
        if (size < 0)
            throw new IllegalArgumentException("size cannot be negative");
        List<String> out = new ArrayList<>();
        Iterator<T> it = xs.iterator();
        int i = 0;
        while (it.hasNext() && i < size) {
            out.add(Objects.toString(it.next()));
            i++;
        }
        if (it.hasNext()) {
            out.add("...");
        }
        return out;
    }

    public static @NotNull <A, B> Map<String, String> itsMap(@NotNull Map<A, B> map) {
        Map<String, String> out = new HashMap<>();
        for (Map.Entry<A, B> entry : map.entrySet()) {
            out.put(Objects.toString(entry.getKey()), Objects.toString(entry.getValue()));
        }
        return out;
    }

    /**
     * Converts a {@code String} to an {@code Iterable} of {@code Character}s. The order of the characters is
     * preserved. Uses O(1) additional memory. Does not support removal.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * @param s the {@code String}
     * @return an {@code Iterable} containing all the {@code String}'s characters in their original order
     */
    public static @NotNull Iterable<Character> fromString(@NotNull String s) {
        return () -> new NoRemoveIterator<Character>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < s.length();
            }

            @Override
            public Character next() {
                return s.charAt(i++);
            }
        };
    }

    /**
     * Creates a {@code String} from an {@code Iterable} of {@code Character}s. The order of the characters is
     * preserved. Only works for finite {@code Iterable}s.
     *
     * <ul>
     *  <li>{@code cs} must be finite and cannot contain nulls.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param cs the {@code Iterable} of {@code Character}s
     * @return the {@code String} containing all of {@code chars}'s characters in their original order
     */
    public static @NotNull String charsToString(@NotNull Iterable<Character> cs) {
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            sb.append(c);
        }
        return sb.toString();
    }

    //todo docs
    public static @NotNull <A, B> Map<A, B> toMap(@NotNull Iterable<Pair<A, B>> ps) {
        Map<A, B> map = new HashMap<>();
        for (Pair<A, B> p : ps) {
            map.put(p.a, p.b);
        }
        return map;
    }

    public static @NotNull <A extends Comparable<A>, B> SortedMap<A, B> toSortedMap(@NotNull Iterable<Pair<A, B>> ps) {
        SortedMap<A, B> map = new TreeMap<>();
        for (Pair<A, B> p : ps) {
            map.put(p.a, p.b);
        }
        return map;
    }

    public static @NotNull <A, B> Map<A, B> toMapPreservingOrder(@NotNull Iterable<Pair<A, B>> ps) {
        Map<A, B> map = new LinkedHashMap<>();
        for (Pair<A, B> p : ps) {
            map.put(p.a, p.b);
        }
        return map;
    }

    public static @NotNull <A, B> IdentityHashMap<A, B> toIdentityMap(@NotNull Iterable<Pair<A, B>> ps) {
        IdentityHashMap<A, B> map = new IdentityHashMap<>();
        for (Pair<A, B> p : ps) {
            map.put(p.a, p.b);
        }
        return map;
    }

    public static @NotNull <A, B> Iterable<Pair<A, B>> fromMap(@NotNull Map<A, B> map) {
        return map(entry -> new Pair<A, B>(entry.getKey(), entry.getValue()), map.entrySet());
    }

    /**
     * Equivalent of Haskell's {@code (:)} list constructor. Creates an {@code Iterable} whose first element is
     * {@code x} and whose remaining elements are given by {@code xs}. {@code xs} may be infinite, in which case the
     * result is also infinite. Uses O(1) additional memory. Does not support removal.
     *
     * <ul>
     *  <li>{@code x} can be anything.</li>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result is a non-empty {@code Iterable}.</li>
     * </ul>
     *
     * Result length is |{@code xs}|+1
     *
     * @param x the first element of the {@code Iterable} to be created
     * @param xs the second-through-last elements of the {@code Iterable} to be created
     * @param <T> the element type of the {@code Iterable} to be created
     * @return the {@code Iterable} to be created
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull <T> Iterable<T> cons(@Nullable T x, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private boolean readHead = false;
            private final @NotNull Iterator<T> xsi = xs.iterator();

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
        };
    }

    /**
     * Equivalent of Haskell's {@code (:)} list constructor. Creates a {@code String} whose first character is
     * {@code c} and whose remaining characters are given by {@code cs}. Uses O(n) additional memory, where n is the
     * length of cs.
     *
     * <ul>
     *  <li>{@code c} can be anything.</li>
     *  <li>{@code cs} must be non-null.</li>
     *  <li>The result is a non-empty {@code String}.</li>
     * </ul>
     *
     * Result length is |{@code cs}|+1
     *
     * @param c the first character of the {@code String} to be created
     * @param cs the second-through-last characters of the {@code String} to be created
     * @return the {@code String} to be created
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull String cons(char c, @NotNull String cs) {
        return Character.toString(c) + cs;
    }

    /**
     * Equivalent of Haskell's {@code (++)} operator. Creates an {@code Iterable} consisting of {@code xs}'s
     * elements followed by {@code ys}'s elements. {@code xs} may be infinite, in which case the result will be equal
     * to {@code xs}. {@code ys} may be infinite, in which case the result will also be infinite. Uses O(1)
     * additional memory. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>{@code ys} must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code xs}|+|{@code ys}|
     *
     * @param xs an {@code Iterable}
     * @param ys another {@code Iterable}
     * @param <T> the element type of the {@code Iterable} to be created
     * @return {@code xs} concatenated with {@code ys}
     */
    public static @NotNull <T> Iterable<T> concat(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<T> ysi = ys.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext() || ysi.hasNext();
            }

            @Override
            public T next() {
                return (xsi.hasNext() ? xsi : ysi).next();
            }
        };
    }

    /**
     * Equivalent of Haskell's {@code (++)} operator. Creates a {@code String} consisting of {@code s}'s characters
     * followed by {@code t}'s characters. Uses O(n+m) additional memory, where n is the length of {@code s} and m is
     * the length of {@code t}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>{@code t} must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code s}|+|{@code t}|
     *
     * @param s a {@code String}
     * @param t a {@code String}
     * @return {@code s} concatenated with {@code t}
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull String concat(@NotNull String s, @NotNull String t) {
        return s + t;
    }

    /**
     * Equivalent of Haskell's {@code head} function. Returns the first element of an {@code Iterable}. Works on
     * infinite {@code Iterable}s. Uses O(1) additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return the {@code Iterable}'s first element
     */
    public static <T> T head(@NotNull Iterable<T> xs) {
        return xs.iterator().next();
    }

    /**
     * Equivalent of Haskell's {@code head} function. Returns the first element of a {@code List}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a {@code List}
     * @param <T> the {@code List}'s element type
     * @return the {@code List}'s first element
     */
    public static <T> T head(@NotNull List<T> xs) {
        return xs.get(0);
    }

    /**
     * Equivalent of Haskell's {@code head} function. Returns the first element of a {@code SortedSet}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a {@code SortedSet}
     * @param <T> the {@code SortedSet}'s element type
     * @return the {@code SortedSet}'s first element
     */
    public static <T> T head(@NotNull SortedSet<T> xs) {
        return xs.first();
    }

    /**
     * Equivalent of Haskell's {@code head} function. Returns the first character of a {@code String}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code s} must be non-empty.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param s a {@code String}
     * @return the {@code String}'s first character
     */
    public static char head(@NotNull String s) {
        return s.charAt(0);
    }

    /**
     * Equivalent of Haskell's {@code last} function. Returns the last element of an {@code Iterable}. Only works on
     * finite {@code Iterable}s. Uses O(1) additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty and finite.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return the {@code Iterable}'s last element
     */
    public static <T> T last(@NotNull Iterable<T> xs) {
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
     * Equivalent of Haskell's {@code last} function. Returns the last element of a {@code List}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a {@code List}
     * @param <T> the {@code List}'s element type
     * @return the {@code List}'s last element
     */
    public static <T> T last(@NotNull List<T> xs) {
        return xs.get(xs.size() - 1);
    }

    /**
     * Equivalent of Haskell's {@code last} function. Returns the last element of a {@code SortedSet}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result may be anything.</li>
     * </ul>
     *
     * @param xs a {@code SortedSet}
     * @param <T> the {@code SortedSet}'s element type
     * @return the {@code SortedSet}'s last element
     */
    public static <T> T last(@NotNull SortedSet<T> xs) {
        return xs.last();
    }

    /**
     * Equivalent of Haskell's {@code last} function. Returns the last character of a {@code String}. Uses O(1)
     * additional memory.
     *
     * <ul>
     *  <li>{@code s} must be non-empty.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param s a {@code String}
     * @return the {@code String}'s last character
     */
    public static char last(@NotNull String s) {
        return s.charAt(s.length() - 1);
    }

    /**
     * Equivalent of Haskell's {@code tail} function. Returns all elements of an {@code Iterable} but the first.
     * {@code xs} may be infinite, in which the result will also be infinite. Uses O(1) additional memory. The
     * {@code Iterable} produced does not support removing elements.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return an {@code Iterable} containing all elements of {@code xs} but the first
     */
    public static @NotNull <T> Iterable<T> tail(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            throw new NoSuchElementException();
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
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
        };
    }

    /**
     * Equivalent of Haskell's {@code tail} function. Given a {@code String}, returns a {@code String} containing
     * all of its characters but the first. Uses O(n) additional memory, where n is the length of {@code s}.
     *
     * <ul>
     *  <li>{@code s} must be non-empty.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * Result length is |{@code s}|–1
     *
     * @param s a {@code String}
     * @return a {@code String} containing all characters of {@code s} but the first
     */
    public static @NotNull String tail(@NotNull String s) {
        return s.substring(1);
    }

    /**
     * Equivalent of Haskell's {@code init} function. Returns all elements of an {@code Iterable} but the last.
     * {@code xs} may be infinite, in which the result will be {@code xs}. Uses O(1) additional memory. The
     * {@code Iterable} produced does not support removing elements.
     *
     * <ul>
     *  <li>{@code xs} must be non-empty.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return an {@code Iterable} containing all elements of {@code xs} but the last
     */
    public static @NotNull <T> Iterable<T> init(@NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            throw new NoSuchElementException();
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next = xsi.next();

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
                };
            }
        };
    }

    /**
     * Equivalent of Haskell's {@code init} function. Given a {@code String}, returns a {@code String} containing
     * all of its characters but the last. Uses O(n) additional memory, where n is the length of {@code s}.
     *
     * <ul>
     *  <li>{@code s} must be non-empty.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * Result length is |{@code s}|–1
     *
     * @param s a {@code String}
     * @return a {@code String} containing all characters of {@code s} but the last
     */
    public static @NotNull String init(@NotNull String s) {
        return s.substring(0, s.length() - 1);
    }

    public static @NotNull <T> Iterable<T> middle(@NotNull Iterable<T> xs) {
        //todo better implementation
        return tail(init(xs));
    }

    public static @NotNull String middle(@NotNull String s) {
        return s.substring(1, s.length() - 1);
    }

    /**
     * Equivalent of Haskell's {@code null} function. Tests whether an {@code Iterable} contains no elements.
     * {@code xs} may be infinite. Uses O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return whether {@code xs} is empty
     */
    public static <T> boolean isEmpty(@NotNull Iterable<T> xs) {
        return !xs.iterator().hasNext();
    }

    /**
     * Equivalent of Haskell's {@code null} function. Tests whether a {@code Collection} contains no elements. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param xs a {@code Collection}
     * @param <T> the {@code Collection}'s element type
     * @return whether {@code xs} is empty
     */
    public static <T> boolean isEmpty(@NotNull Collection<T> xs) {
        return xs.isEmpty();
    }

    /**
     * Equivalent of Haskell's {@code null} function. Tests whether a {@code String} contains no characters. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param s a {@code String}
     * @return whether {@code s} is empty
     */
    public static boolean isEmpty(@NotNull String s) {
        return s.isEmpty();
    }

    /**
     * Equivalent of Haskell's {@code length} function. Returns the number of elements in an {@code Iterable}. Only
     * works on finite {@code Iterable}s. Uses O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return the {@code Iterable}'s length
     */
    public static <T> int length(@NotNull Iterable<T> xs) {
        int i = 0;
        for (T x : xs) {
            i++;
        }
        return i;
    }

    /**
     * Equivalent of Haskell's {@code length} function. Returns the number of elements in an {@code Iterable}. Only
     * works on finite {@code Iterable}s. Uses O(log(n)) additional space, where n is {@code xs}'s length; but it's
     * effectively constant space.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return the {@code Iterable}'s length
     */
    public static @NotNull <T> BigInteger bigIntegerLength(@NotNull Iterable<T> xs) {
        BigInteger i = BigInteger.ZERO;
        for (T x : xs) {
            i = i.add(BigInteger.ONE);
        }
        return i;
    }

    /**
     * Equivalent of Haskell's {@code length} function. Returns the number of elements in a {@code Collection}. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs a {@code Collection}
     * @param <T> the {@code Collection}'s element type
     * @return the {@code Collection}'s length
     */
    public static <T> int length(@NotNull Collection<T> xs) {
        return xs.size();
    }

    /**
     * Equivalent of Haskell's {@code length} function. Returns the number of characters in a {@code String}. Uses
     * O(1) additional space.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param s a {@code String}
     * @return the {@code String}'s length
     */
    public static int length(@NotNull String s) {
        return s.length();
    }

    //todo docs
    public static <T> boolean lengthAtLeast(int length, @NotNull Iterable<T> xs) {
        int i = 0;
        for (T x : xs) {
            i++;
            if (i >= length) return true;
        }
        return false;
    }

    public static <T> boolean lengthAtLeast(int length, @NotNull Collection<T> xs) {
        return xs.size() >= length;
    }

    public static <T> boolean lengthAtLeast(int length, @NotNull String s) {
        return s.length() >= length;
    }

    /**
     * Equivalent of Haskell's {@code map} function. Transforms one {@code Iterable} into another by applying a
     * function to each element. {@code xs} may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. Does not support removal.
     *
     * <ul>
     *  <li>{@code f} must be non-null.</li>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>{@code xs} must only contain elements that are valid inputs for {@code f}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code xs}|
     *
     * @param f the function that transforms each element in the {@code Iterable}
     * @param xs the {@code Iterable}
     * @param <A> the type of the original {@code Iterable}'s elements
     * @param <B> the type of the output {@code Iterable}'s elements
     * @return an {@code Iterable} containing the elements of {@code xs} transformed by {@code f}
     */
    public static @NotNull <A, B> Iterable<B> map(@NotNull Function<A, B> f, @NotNull Iterable<A> xs) {
        return () -> new NoRemoveIterator<B>() {
            private final @NotNull Iterator<A> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public B next() {
                return f.apply(xsi.next());
            }
        };
    }

    /**
     * Equivalent of Haskell's {@code map} function. Transforms one {@code String} into another by applying a
     * function to each character. Uses O(n) additional memory, where n is the length of the input string.
     *
     * <ul>
     *  <li>{@code f} must be non-null.</li>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>{@code xs} must only contain characters that are valid inputs for {@code f}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code s}|
     *
     * @param f the function that transforms each character in the {@code String}
     * @param s the {@code String}
     * @return a {@code String} containing the characters of {@code s} transformed by {@code f}
     */
    public static @NotNull String map(@NotNull Function<Character, Character> f, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(f.apply(s.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * Equivalent of Haskell's {@code reverse} function. Reverses an {@code Iterable}. {@code xs} must be finite.
     * Uses O(n) additional memory, where n is the length of {@code xs}. The resulting list may be modified, but the
     * modifications will not affect the original {@code Iterable}.
     *
     * <ul>
     *  <li>{@code xs} must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code xs}|
     *
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return a {@code List} containing {@code xs}'s elements in reverse order
     */
    public static @NotNull <T> List<T> reverse(@NotNull Iterable<T> xs) {
        List<T> list = toList(xs);
        Collections.reverse(list);
        return list;
    }

    /**
     * Equivalent of Haskell's {@code reverse} function. Reverses a {@code String}. Uses O(n) additional memory,
     * where n is the length of {@code s}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is |{@code s}|
     *
     * @param s a {@code String}
     * @return a {@code String} containing {@code s}'s characters in reverse order
     */
    public static @NotNull String reverse(@NotNull String s) {
        char[] reversed = new char[s.length()];
        int length = s.length();
        for (int i = 0; i < length; i++) {
            reversed[i] = s.charAt(length - i - 1);
        }
        return new String(reversed);
    }

    /**
     * Equivalent of Haskell's {@code intersperse} function. Given an {@code Iterable} {@code xs} and a seperator
     * {@code sep}, returns an {@code Iterable} consisting of the elements of {@code xs} with {@code sep} between
     * every adjacent pair. {@code xs} may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. Does not support removal.
     *
     * <ul>
     *  <li>{@code sep} may be anything.</li>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result is an {@code Iterable} whose odd-indexed (using 0-based indexing) elements are identical.</li>
     * </ul>
     *
     * Result length is 0 when |{@code xs}|=0, 2|{@code xs}|–1 otherwise
     *
     * @param sep a separator
     * @param xs an {@code Iterable}
     * @param <T> the {@code Iterable}'s element type
     * @return an {@code Iterable} consisting of the elements of {@code xs} interspersed with {@code sep}
     */
    public static @NotNull <T> Iterable<T> intersperse(@Nullable T sep, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
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
        };
    }

    /**
     * Equivalent of Haskell's {@code intersperse} function. Given a {@code String} {@code s} and a seperator
     * {@code sep}, returns a {@code String} consisting of the characters of {@code s} with {@code sep} between
     * every adjacent pair. Uses O(n) additional memory, where n is the length of {@code s}.
     *
     * <ul>
     *  <li>{@code sep} may be any {@code char}.</li>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is a {@code String} whose odd-indexed (using 0-based indexing) characters are identical.</li>
     * </ul>
     *
     * Result length is 0 when |{@code s}|=0, 2|{@code s}|–1 otherwise
     *
     * @param sep a separator
     * @param s a {@code String}
     * @return a {@code String} consisting of the characters of {@code s} interspersed with {@code sep}
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
     * Equivalent of Haskell's {@code intercalate} function. Inserts an {@code Iterable} between every two adjacent
     * {@code Iterable}s in an {@code Iterable} of {@code Iterable}s, flattening the result. {@code xss}, any
     * element of {@code xss}, or {@code xs} may be infinite, in which case the result is also infinite. Uses O(1)
     * additional memory. Does not support removal.
     *
     * Result length is the sum of the lengths of {@code xs}'s elements and (0 if |{@code xss}|=0,
     * |{@code xss}|(|{@code xs}|–1) otherwise)
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>{@code xss} must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param xs the separating {@code Iterable}
     * @param xss the separated {@code Iterable}
     * @param <T> the resulting {@code Iterable}'s element type
     * @return {@code xss} intercalated by {@code xs}
     */
    public static @NotNull <T> Iterable<T> intercalate(@NotNull Iterable<T> xs, @NotNull Iterable<Iterable<T>> xss) {
        return concat(intersperse(xs, xss));
    }

    /**
     * Equivalent of Haskell's {@code intercalate} function. Inserts a {@code String} between every two adjacent
     * {@code String}s in an {@code Iterable} of {@code String}s, flattening the result. Uses O(abc) additional
     * memory, where a is the length of {@code strings}, b is the maximum length of any string in {@code strings},
     * and c is the length of {@code sep}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code sep} must be non-null.</li>
     *  <li>{@code strings} must be finite.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Result length is the sum of the lengths of {@code xs}'s elements and (0 if |{@code strings}|=0,
     * |{@code strings}|(|{@code sep}|–1) otherwise)
     *
     * @param sep the separating {@code String}
     * @param strings the separated {@code String}s
     * @return {@code strings} intercalated by {@code sep}
     */
    public static @NotNull String intercalate(@NotNull String sep, @NotNull Iterable<String> strings) {
        return concatStrings(intersperse(sep, strings));
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code Iterables}. If the rows have different lengths, then the "overhanging" elements still end up in the
     * result. See test cases for examples. Any element of {@code xss} may be infinite, in which case the result will
     * be infinite. Uses O(nm) additional memory, where n is then length of {@code xss} and m is the largest amount of
     * memory used by any {@code Iterable} in {@code xss}. Does not support removal.
     *
     * <ul>
     *  <li>{@code xss} must be finite.</li>
     *  <li>The lengths of the result's elements are finite, non-increasing, and never 0.</li>
     * </ul>
     *
     * Result length is the maximum length of {@code xss}'s elements
     *
     * @param xss an {@code Iterable} of {@code Iterable}s
     * @param <T> the {@code Iterable}'s elements' element type
     * @return {@code xss}, transposed
     */
    public static @NotNull <T> Iterable<List<T>> transpose(@NotNull Iterable<Iterable<T>> xss) {
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

            @Override
            public boolean hasNext() {
                return any(Iterator::hasNext, iterators);
            }

            @Override
            public List<T> next() {
                List<T> nextList = new ArrayList<>();
                for (Iterator<T> iterator : iterators) {
                    if (iterator.hasNext()) {
                        nextList.add(iterator.next());
                    }
                }
                if (nextList.isEmpty()) {
                    throw new NoSuchElementException();
                }
                return nextList;
            }
        };
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code String}s. If the rows have different lengths, then the "overhanging" characters still end up in the
     * result. See test cases for examples. Uses O(nm) additional memory, where n is then length of {@code xss} and m
     * is the length of the longest {@code String} in {@code xss}. Does not support removal.
     *
     * <ul>
     *  <li>{@code strings} must be non-null.</li>
     *  <li>The lengths of the result's elements are non-increasing and never 0.</li>
     * </ul>
     *
     * Result length is the maximum length of {@code strings}'s elements
     *
     * @param strings an {@code Iterable} of {@code String}s
     * @return {@code strings}, transposed
     */
    public static @NotNull Iterable<String> transposeStrings(@NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transpose(map(s -> fromString(s), strings))
        );
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code Iterables}. If the rows have different lengths, then the "overhanging" elements will be truncated; the
     * result's rows will all have equal lengths. See test cases for examples. Any element of {@code xss} may be
     * infinite, in which case the result will be infinite. Uses O(nm) additional memory, where n is then length of
     * {@code xss} and m is the largest amount of memory used by any {@code Iterable} in {@code xss}. The
     * {@code Iterable} produced does not support removing elements.
     *
     * <ul>
     *  <li>{@code xss} must be finite.</li>
     *  <li>The lengths of the result's elements are finite and equal.</li>
     * </ul>
     *
     * Result length is the minimum length of {@code xss}'s elements
     *
     * @param xss an {@code Iterable} of {@code Iterable}s
     * @param <T> the {@code Iterable}'s elements' element type
     * @return {@code xss}, transposed
     */
    public static @NotNull <T> Iterable<List<T>> transposeTruncating(@NotNull Iterable<Iterable<T>> xss) {
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

            @Override
            public boolean hasNext() {
                return !iterators.isEmpty() && all(Iterator::hasNext, iterators);
            }

            @Override
            public List<T> next() {
                List<T> nextList = new ArrayList<>();
                for (Iterator<T> iterator : iterators) {
                    nextList.add(iterator.next());
                }
                return nextList;
            }
        };
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code String}s. If the rows have different lengths, then the "overhanging" characters will be truncated. See
     * test cases for examples. Uses O(nm) additional memory, where n is then length of {@code xss} and m is the
     * length of the longest {@code String} in {@code xss}. Does not support removal.
     *
     * <ul>
     *  <li>{@code strings} must be non-null.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * Result length is the minimum length of {@code strings}'s elements
     *
     * @param strings an {@code Iterable} of {@code String}s
     * @return {@code strings}, transposed
     */
    public static @NotNull Iterable<String> transposeStringsTruncating(@NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transposeTruncating(map(s -> fromString(s), strings))
        );
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code Iterables}. If the rows have different lengths, then the gaps will be padded; the result's rows will all
     * have equal lengths. See test cases for examples. Any element of {@code xss} may be infinite, in which case the
     * result will be infinite. Uses O(nm) additional memory, where n is then length of {@code xss} and m is the
     * largest amount of memory used by any {@code Iterable} in {@code xss}. Does not support removal.
     *
     * <ul>
     *  <li>{@code xss} must be finite.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * Result length is the maximum length of {@code xss}'s elements
     *
     * @param xss an {@code Iterable} of {@code Iterable}s
     * @param pad the padding
     * @param <T> the {@code Iterable}'s elements' element type
     * @return {@code xss}, transposed
     */
    public static @NotNull <T> Iterable<Iterable<T>> transposePadded(
            @Nullable T pad,
            @NotNull Iterable<Iterable<T>> xss
    ) {
        return () -> new NoRemoveIterator<Iterable<T>>() {
            private final @NotNull List<Iterator<T>> iterators = toList(map(Iterable::iterator, xss));

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
        };
    }

    /**
     * Equivalent of Haskell's {@code transpose} function. Swaps rows and columns of an {@code Iterable} of
     * {@code String}s. If the rows have different lengths, then the gaps will be padded; the result's rows will all
     * have equal lengths. Uses O(nm) additional memory, where n is then length of {@code xss} and m is the length of
     * the longest {@code String} in {@code xss}. Does not support removal.
     *
     * <ul>
     *  <li>{@code strings} must be non-null.</li>
     *  <li>The lengths of the result's elements are equal.</li>
     * </ul>
     *
     * Result length is the maximum length of {@code strings}'s elements
     *
     * @param strings an {@code Iterable} of {@code String}s
     * @param pad the padding
     * @return {@code strings}, transposed
     */
    public static @NotNull Iterable<String> transposeStringsPadded(char pad, @NotNull Iterable<String> strings) {
        return map(
                IterableUtils::charsToString,
                transposePadded(pad, map(s -> fromString(s), strings))
        );
    }

    public static <A, B> B foldl(@NotNull BiFunction<B, A, B> f, @Nullable B z, @NotNull Iterable<A> xs) {
        B result = z;
        for (A x : xs) {
            result = f.apply(result, x);
        }
        return result;
    }

    public static <A> A foldl1(@NotNull BiFunction<A, A, A> f, @NotNull Iterable<A> xs) {
        A result = null;
        boolean started = false;
        for (A x : xs) {
            if (started) {
                result = f.apply(result, x);
            } else {
                result = x;
                started = true;
            }
        }
        if (!started) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static <A, B> B foldr(@NotNull BiFunction<A, B, B> f, @Nullable B z, @NotNull Iterable<A> xs) {
        //noinspection unchecked
        return foldl((x, y) -> f.apply(y, x), z, reverse(xs));
    }

    public static <A> A foldr1(@NotNull BiFunction<A, A, A> f, @NotNull Iterable<A> xs) {
        //noinspection unchecked
        return foldl1((x, y) -> f.apply(y, x), reverse(xs));
    }

    public static @NotNull <T> Iterable<T> concat(@NotNull Iterable<Iterable<T>> xss) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<Iterable<T>> xssi = xss.iterator();
            private @NotNull Iterator<T> xsi = xssi.hasNext() ? xssi.next().iterator() : null;

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

    public static @NotNull String concatStrings(@NotNull Iterable<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static @NotNull <A, B> Iterable<B> concatMap(@NotNull Function<A, Iterable<B>> f, @NotNull Iterable<A> xs) {
        return concat(map(f, xs));
    }

    public static @NotNull <T> String concatMapStrings(@NotNull Function<T, String> f, @NotNull Iterable<T> xs) {
        StringBuilder sb = new StringBuilder();
        for (T x : xs) {
            sb.append(f.apply(x));
        }
        return sb.toString();
    }

    public static @NotNull <A> Iterable<A> optionalFilter(@NotNull Iterable<Optional<A>> xs) {
        return map(Optional::get, filter(Optional::isPresent, xs));
    }

    public static @NotNull <A> Iterable<A> nullableOptionalFilter(@NotNull Iterable<NullableOptional<A>> xs) {
        return map(NullableOptional::get, filter(NullableOptional::isPresent, xs));
    }

    public static @NotNull <A> Iterable<A> optionalFilterInfinite(@NotNull Iterable<Optional<A>> xs) {
        return map(Optional::get, filterInfinite(Optional::isPresent, xs));
    }

    public static @NotNull <A> Iterable<A> nullableOptionalFilterInfinite(@NotNull Iterable<NullableOptional<A>> xs) {
        return map(NullableOptional::get, filterInfinite(NullableOptional::isPresent, xs));
    }

    public static @NotNull <A, B> Iterable<B> optionalMap(
            @NotNull Function<A, Optional<B>> f,
            @NotNull Iterable<A> xs
    ) {
        return optionalFilter(map(f, xs));
    }

    public static @NotNull <A, B> Iterable<B> nullableOptionalMap(
            @NotNull Function<A, NullableOptional<B>> f,
            @NotNull Iterable<A> xs
    ) {
        return nullableOptionalFilter(map(f, xs));
    }

    public static @NotNull <A, B> Iterable<B> optionalMapInfinite(
            @NotNull Function<A, Optional<B>> f,
            @NotNull Iterable<A> xs
    ) {
        return optionalFilterInfinite(map(f, xs));
    }

    public static @NotNull <A, B> Iterable<B> nullableOptionalMapInfinite(
            @NotNull Function<A, NullableOptional<B>> f,
            @NotNull Iterable<A> xs
    ) {
        return nullableOptionalFilterInfinite(map(f, xs));
    }

    public static boolean and(@NotNull Iterable<Boolean> xs) {
        for (boolean x : xs) {
            if (!x) return false;
        }
        return true;
    }

    public static boolean or(@NotNull Iterable<Boolean> xs) {
        for (boolean x : xs) {
            if (x) return true;
        }
        return false;
    }

    public static <T> boolean any(@NotNull Predicate<T> predicate, @NotNull Iterable<T> xs) {
        for (T x : xs) {
            if (predicate.test(x)) return true;
        }
        return false;
    }

    public static boolean any(@NotNull Predicate<Character> predicate, @NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (predicate.test(s.charAt(i))) return true;
        }
        return false;
    }

    public static <T> boolean all(@NotNull Predicate<T> predicate, @NotNull Iterable<T> xs) {
        for (T x : xs) {
            if (!predicate.test(x)) return false;
        }
        return true;
    }

    public static boolean all(@NotNull Predicate<Character> predicate, @NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!predicate.test(s.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Returns the sum of all the {@code Byte}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 0 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Byte}s.
     * @return Σxs
     */
    public static byte sumByte(@NotNull List<Byte> xs) {
        return foldl((x, y) -> (byte) (x + y), (byte) 0, xs);
    }

    /**
     * Returns the sum of all the {@code Short}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 0 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Short}s.
     * @return Σxs
     */
    public static short sumShort(@NotNull List<Short> xs) {
        return foldl((x, y) -> (short) (x + y), (short) 0, xs);
    }

    /**
     * Returns the sum of all the {@code Integer}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 0 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Integer}s.
     * @return Σxs
     */
    public static int sumInteger(@NotNull List<Integer> xs) {
        return foldl((x, y) -> x + y, 0, xs);
    }

    /**
     * Returns the sum of all the {@code Long}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 0 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Long}s.
     * @return Σxs
     */
    public static long sumLong(@NotNull List<Long> xs) {
        return foldl((x, y) -> x + y, 0L, xs);
    }

    /**
     * Returns the left-to-right sum of all the {@code Float}s in {@code xs}. Overflow may occur. If {@code xs} is
     * empty, 0.0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code float}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Float}s.
     * @return Σxs
     */
    public static float sumFloat(@NotNull List<Float> xs) {
        if (xs.isEmpty()) return 0.0f;
        return foldl1((x, y) -> x + y, xs);
    }

    /**
     * Returns the left-to-right sum of all the {@code Double}s in {@code xs}. Overflow may occur. If {@code xs} is
     * empty, 0.0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code double}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Double}s.
     * @return Σxs
     */
    public static double sumDouble(List<Double> xs) {
        if (xs.isEmpty()) return 0.0;
        return foldl1((x, y) -> x + y, xs);
    }

    /**
     * Returns the sum of all the {@code BigInteger}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code BigInteger}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code BigInteger}s.
     * @return Σxs
     */
    public static @NotNull BigInteger sumBigInteger(@NotNull List<BigInteger> xs) {
        return foldl(BigInteger::add, BigInteger.ZERO, xs);
    }

    /**
     * Returns the sum of all the {@code BigDecimal}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code BigDecimal}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code BigDecimal}s.
     * @return Σxs
     */
    public static @NotNull BigDecimal sumBigDecimal(@NotNull List<BigDecimal> xs) {
        if (xs.isEmpty()) return BigDecimal.ZERO;
        if (head(xs) == null)
            throw new NullPointerException();
        return foldl1(BigDecimal::add, xs);
    }

    /**
     * Returns the product of all the {@code Byte}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 1 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Byte}s.
     * @return Πxs
     */
    public static byte productByte(@NotNull List<Byte> xs) {
        return foldl((x, y) -> (byte) (x * y), (byte) 1, xs);
    }

    /**
     * Returns the product of all the {@code Short}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 1 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Short}s.
     * @return Πxs
     */
    public static short productShort(@NotNull List<Short> xs) {
        return foldl((x, y) -> (short) (x * y), (short) 1, xs);
    }

    /**
     * Returns the product of all the {@code Integer}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 1 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Integer}s.
     * @return Πxs
     */
    public static int productInteger(@NotNull List<Integer> xs) {
        return foldl((x, y) -> x * y, 1, xs);
    }

    /**
     * Returns the product of all the {@code Long}s in {@code xs}. Overflow may occur. If {@code xs} is empty, 1 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Long}s.
     * @return Πxs
     */
    public static long productLong(@NotNull List<Long> xs) {
        return foldl((x, y) -> x * y, 1L, xs);
    }

    /**
     * Returns the left-to-right product of all the {@code Float}s in {@code xs}. Overflow or underflow may occur. If
     * {@code xs} is empty, 1.0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code float}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Float}s.
     * @return Πxs
     */
    public static float productFloat(@NotNull List<Float> xs) {
        return foldl((x, y) -> x * y, 1.0f, xs);
    }

    /**
     * Returns the left-to-right product of all the {@code Double}s in {@code xs}. Overflow or underflow may occur. If
     * {@code xs} is empty, 1.0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code double}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Double}s.
     * @return Πxs
     */
    public static double productDouble(@NotNull List<Double> xs) {
        return foldl((x, y) -> x * y, 1.0, xs);
    }

    /**
     * Returns the product of all the {@code BigInteger}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code BigInteger}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code BigInteger}s.
     * @return Πxs
     */
    public static @NotNull BigInteger productBigInteger(List<BigInteger> xs) {
        if (any(x -> x == null, xs)) throw new NullPointerException();
        if (any(x -> x.equals(BigInteger.ZERO), xs)) return BigInteger.ZERO;
        return foldl(BigInteger::multiply, BigInteger.ONE, xs);
    }

    /**
     * Returns the product of all the {@code BigDecimal}s in {@code xs}. If {@code xs} is empty, 1 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code BigDecimal}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code BigDecimal}s.
     * @return Πxs
     */
    public static @NotNull BigDecimal productBigDecimal(@NotNull List<BigDecimal> xs) {
        if (xs.isEmpty()) return BigDecimal.ONE;
        if (any(x -> x == null, xs)) throw new NullPointerException();
        if (any(x -> eq(x, BigDecimal.ZERO), xs)) return BigDecimal.ZERO;
        return foldl(BigDecimal::multiply, BigDecimal.ONE, xs);
    }

    public static int sumSignBigInteger(@NotNull List<BigInteger> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                int lastPositiveIndex = xs.size();
                int lastNegativeIndex = xs.size();
                boolean positiveSeen = false;
                boolean negativeSeen = false;
                for (int i = xs.size() - 1; i >= 0 && !(positiveSeen && negativeSeen); i--) {
                    int sign = xs.get(i).signum();
                    if (!positiveSeen && sign == 1) {
                        lastPositiveIndex = i;
                        positiveSeen = true;
                    } else if (!negativeSeen && sign == -1) {
                        lastNegativeIndex = i;
                        negativeSeen = true;
                    }
                }
                BigInteger sum = BigInteger.ZERO;
                int signum = 0;
                for (int i = 0; i < xs.size(); i++) {
                    sum = sum.add(xs.get(i));
                    signum = sum.signum();
                    if (signum == 1 && i > lastNegativeIndex) return 1;
                    if (signum == -1 && i > lastPositiveIndex) return -1;
                }
                return signum;
        }
    }

    public static int sumSignBigDecimal(@NotNull List<BigDecimal> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                int lastPositiveIndex = xs.size();
                int lastNegativeIndex = xs.size();
                boolean positiveSeen = false;
                boolean negativeSeen = false;
                for (int i = xs.size() - 1; i >= 0 && !(positiveSeen && negativeSeen); i--) {
                    int sign = xs.get(i).signum();
                    if (!positiveSeen && sign == 1) {
                        lastPositiveIndex = i;
                        positiveSeen = true;
                    } else if (!negativeSeen && sign == -1) {
                        lastNegativeIndex = i;
                        negativeSeen = true;
                    }
                }
                BigDecimal sum = BigDecimal.ZERO;
                int signum = 0;
                for (int i = 0; i < xs.size(); i++) {
                    sum = sum.add(xs.get(i));
                    signum = sum.signum();
                    if (signum == 1 && i > lastNegativeIndex) return 1;
                    if (signum == -1 && i > lastPositiveIndex) return -1;
                }
                return signum;
        }
    }

    public static @NotNull <A, B> Iterable<B> scanl(
            @NotNull BiFunction<B, A, B> f,
            @Nullable B z,
            @NotNull Iterable<A> xs
    ) {
        return () -> new NoRemoveIterator<B>() {
            private final @NotNull Iterator<A> xsi = xs.iterator();
            private @Nullable B result = z;
            private boolean firstTime = true;

            @Override
            public boolean hasNext() {
                return firstTime || xsi.hasNext();
            }

            @Override
            public B next() {
                if (firstTime) {
                    firstTime = false;
                    return result;
                } else {
                    result = f.apply(result, xsi.next());
                    return result;
                }
            }
        };
    }

    public static @NotNull <A> Iterable<A> scanl1(@NotNull BiFunction<A, A, A> f, @NotNull Iterable<A> xs) {
        return scanl(f, head(xs), tail(xs));
    }

    public static @NotNull <A, B> Iterable<B> scanr(
            @NotNull BiFunction<A, B, B> f,
            @NotNull B z,
            @NotNull Iterable<A> xs
    ) {
        return scanl((x, y) -> f.apply(y, x), z, reverse(xs));
    }

    public static @NotNull <A> Iterable<A> scanr1(@NotNull BiFunction<A, A, A> f, @NotNull Iterable<A> xs) {
        return scanl1((x, y) -> f.apply(y, x), reverse(xs));
    }

    public static @NotNull <X, Y, ACC> Pair<ACC, List<Y>> mapAccumL(
            @NotNull BiFunction<ACC, X, Pair<ACC, Y>> f,
            @Nullable ACC s,
            @NotNull Iterable<X> xs
    ) {
        List<Y> ys = new ArrayList<>();
        for (X x : xs) {
            Pair<ACC, Y> p = f.apply(s, x);
            s = p.a;
            ys.add(p.b);
        }
        return new Pair<>(s, ys);
    }

    public static @NotNull <X, Y, ACC> Pair<ACC, List<Y>> mapAccumR(
            @NotNull BiFunction<ACC, X, Pair<ACC, Y>> f,
            @Nullable ACC s,
            @NotNull Iterable<X> xs
    ) {
        return mapAccumL(f, s, reverse(xs));
    }

    public static @NotNull <T> Iterable<T> iterate(@NotNull Function<T, T> f, @Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
            private @Nullable T current = x;
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

    public static @NotNull <T> Iterable<T> repeat(@Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
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

    public static @NotNull <T> Iterable<T> replicate(int n, @Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < n;
            }

            @Override
            public T next() {
                if (i >= n) {
                    throw new NoSuchElementException();
                }
                i++;
                return x;
            }
        };
    }

    public static @NotNull <T> Iterable<T> replicate(@NotNull BigInteger n, @Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull BigInteger i = BigInteger.ZERO;

            @Override
            public boolean hasNext() {
                return lt(i, n);
            }

            @Override
            public T next() {
                if (ge(i, n)) {
                    throw new NoSuchElementException();
                }
                i = i.add(BigInteger.ONE);
                return x;
            }
        };
    }

    public static @NotNull String replicateString(int n, char c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static @NotNull String replicate(@NotNull BigInteger n, char c) {
        StringBuilder sb = new StringBuilder();
        for (BigInteger i : ExhaustiveProvider.INSTANCE.rangeIncreasing(BigInteger.ONE, n)) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static @NotNull <T> Iterable<T> cycle(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return xs;
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();

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

    public static @NotNull <T> List<T> unrepeat(@NotNull Iterable<T> xs) {
        return unrepeat(toList(xs));
    }

    /**
     * Given a {@code xs}, returns the shortest {@code List} {@code ys} such that {@code xs} is equal to {@code ys}
     * repeated some number of times. If {@code xs} consists of a single element repeated multiple times, the result is
     * a {@code List} containing that element once; if {@code xs} does not repeat, the result is {@code xs}. If
     * {@code xs} is empty, the empty list is returned.
     *
     * <ul>
     *  <li>{@code xs} must be non-null.</li>
     *  <li>The result is non-null and not made up of repetitions of any smaller list.</li>
     * </ul>
     *
     * Length is a positive factor of |{@code xs}|
     *
     * @param xs the input {@code List}
     * @param <T> the type of {@code xs}'s elements
     * @return the smallest {@code List} such that {@code xs} is made up of repetitions of that {@code List}
     */
    public static @NotNull <T> List<T> unrepeat(@NotNull List<T> xs) {
        if (xs.isEmpty()) return xs;
        outer:
        for (int i : MathUtils.factors(xs.size())) {
            if (i == xs.size()) break;
            for (int j = 0; j < i; j++) {
                T first = xs.get(j);
                for (int k = j + i; k < xs.size(); k += i) {
                    if (!Objects.equals(first, xs.get(k))) continue outer;
                }
            }
            return toList(take(i, xs));
        }
        return xs;
    }

    public static @NotNull <A, B> Iterable<A> unfoldr(@NotNull Function<B, Optional<Pair<A, B>>> f, @NotNull B x) {
        return new Iterable<A>() {
            @Override
            public NoRemoveIterator<A> iterator() {
                return new NoRemoveIterator<A>() {
                    private boolean hasNext = true;
                    private @Nullable A next;
                    private @Nullable B seed = x;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public A next() {
                        A oldNext = next;
                        advance();
                        return oldNext;
                    }

                    private void advance() {
                        Optional<Pair<A, B>> p = f.apply(seed);
                        if (p.isPresent()) {
                            next = p.get().a;
                            seed = p.get().b;
                        } else {
                            hasNext = false;
                        }
                    }
                };
            }
        };
    }

    public static @NotNull <T> Iterable<T> take(int n, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private int i = 0;
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return i < n && xsi.hasNext();
            }

            @Override
            public T next() {
                if (i == n) {
                    throw new NoSuchElementException();
                }
                i++;
                return xsi.next();
            }
        };
    }

    public static @NotNull <T> Iterable<T> take(@NotNull BigInteger n, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull BigInteger i = BigInteger.ZERO;
            private final @NotNull Iterator<T> xsi = xs.iterator();

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

    public static @NotNull String take(int n, @NotNull String s) {
        return s.substring(0, Math.min(s.length(), n));
    }

    public static @NotNull String take(@NotNull BigInteger n, @NotNull String s) {
        return s.substring(0, Math.min(s.length(), n.intValueExact()));
    }

    public static @NotNull <T> Iterable<T> drop(int n, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
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

    public static @NotNull <T> Iterable<T> drop(@NotNull BigInteger n, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
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

    public static @NotNull String drop(int n, @NotNull String s) {
        return s.substring(n);
    }

    public static @NotNull String drop(@NotNull BigInteger n, @NotNull String s) {
        return s.substring(n.intValueExact());
    }

    public static @NotNull <T> Iterable<T> rotateLeft(int amount, @NotNull Iterable<T> xs) {
        return concat(drop(amount, xs), take(amount, xs));
    }

    public static @NotNull <T> Iterable<T> rotateRight(int amount, @NotNull Iterable<T> xs) {
        return rotateLeft(length(xs) - amount, xs);
    }

    public static @NotNull String rotateLeft(int amount, @NotNull String s) {
        return concat(drop(amount, s), take(amount, s));
    }

    public static @NotNull String rotateRight(int amount, @NotNull String s) {
        return rotateLeft(length(s) - amount, s);
    }

    public static @NotNull <T> Iterable<T> pad(@NotNull T pad, int length, @NotNull Iterable<T> xs) {
        if (length < 0)
            throw new IllegalArgumentException("cannot pad with a negative length");
        return take(length, concat(xs, repeat(pad)));
    }

    public static @NotNull <T> Iterable<T> pad(@NotNull T pad, @NotNull BigInteger length, @NotNull Iterable<T> xs) {
        if (length.signum() == -1)
            throw new IllegalArgumentException("cannot pad with a negative length");
        return take(length, (Iterable<T>) concat(xs, repeat(pad)));
    }

    public static @NotNull String pad(char pad, int length, @NotNull String s) {
        if (s.length() == length) return s;
        if (s.length() > length) return take(length, s);
        return s + replicate(length - s.length(), pad);
    }

    public static @NotNull String pad(char pad, @NotNull BigInteger length, @NotNull String s) {
        if (s.length() == length.intValueExact()) return s;
        if (s.length() > length.intValueExact()) return take(length, s);
        return s + replicate(length.intValueExact() - s.length(), pad);
    }

    public static @NotNull <T> Pair<Iterable<T>, Iterable<T>> splitAt(int n, @NotNull Iterable<T> xs) {
        return new Pair<>(take(n, xs), drop(n, xs));
    }

    public static @NotNull <T> Pair<Iterable<T>, Iterable<T>> splitAt(@NotNull BigInteger n, @NotNull Iterable<T> xs) {
        return new Pair<>(take(n, xs), drop(n, xs));
    }

    public static @NotNull Pair<String, String> splitAt(int n, @NotNull String s) {
        return new Pair<>(s.substring(0, n), s.substring(n));
    }

    public static @NotNull Pair<String, String> splitAt(@NotNull BigInteger i, @NotNull String s) {
        return splitAt(i.intValueExact(), s);
    }

    public static @NotNull <T> Iterable<T> takeWhile(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public NoRemoveIterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T current = next;
                        advance();
                        return current;
                    }

                    private void advance() {
                        if (xsi.hasNext()) {
                            next = xsi.next();
                            hasNext = p.test(next);
                        } else {
                            hasNext = false;
                        }
                    }
                };
            }
        };
    }

    public static @NotNull String takeWhile(@NotNull Predicate<Character> p, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!p.test(c)) break;
            sb.append(c);
        }
        return sb.toString();
    }

    public static @NotNull <T> Iterable<T> stopAt(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public NoRemoveIterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T current = next;
                        advance();
                        return current;
                    }

                    private void advance() {
                        if (next != null && p.test(next)) {
                            hasNext = false;
                        } else {
                            hasNext = xsi.hasNext();
                            if (xsi.hasNext()) {
                                next = xsi.next();
                            }
                        }
                    }
                };
            }
        };
    }

    public static @NotNull String stopAt(@NotNull Predicate<Character> p, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sb.append(c);
            if (p.test(c)) break;
        }
        return sb.toString();
    }

    public static @NotNull <T> Iterable<T> dropWhile(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();
            private @Nullable T x;
            private boolean first = false;
            {
                while (xsi.hasNext()) {
                    x = xsi.next();
                    if (!p.test(x)) {
                        first = true;
                        break;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return first || xsi.hasNext();
            }

            @Override
            public T next() {
                if (first) {
                    first = false;
                    return x;
                } else {
                    return xsi.next();
                }
            }
        };
    }

    public static @NotNull String dropWhile(@NotNull Predicate<Character> p, @NotNull String s) {
        int startIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            if (p.test(s.charAt(i))) {
                startIndex = i;
                break;
            }
        }
        return startIndex == -1 ? "" : s.substring(startIndex);
    }

    public static @NotNull <T> Iterable<T> dropWhileEnd(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        List<T> list = toList(xs);
        int index = -1;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!p.test(list.get(i))) {
                index = i;
                break;
            }
        }
        return take(index + 1, list);
    }

    public static @NotNull String dropWhileEnd(@NotNull Predicate<Character> p, @NotNull String s) {
        int index = -1;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (!p.test(s.charAt(i))) {
                index = i;
                break;
            }
        }
        return take(index + 1, s);
    }

    public static @NotNull <T> Iterable<List<T>> chunk(int size, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

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

    public static @NotNull <T> Iterable<List<T>> chunkInfinite(int size, @NotNull Iterable<T> xs) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative. Invalid size: " + size);
        }
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public List<T> next() {
                List<T> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(xsi.next());
                }
                return list;
            }
        };
    }

    public static @NotNull <T> Iterable<Pair<T, T>> chunkPairsInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Pair<T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Pair<T, T> next() {
                return new Pair<>(xsi.next(), xsi.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Triple<T, T, T>> chunkTriplesInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Triple<T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Triple<T, T, T> next() {
                return new Triple<>(xsi.next(), xsi.next(), xsi.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Quadruple<T, T, T, T>> chunkQuadruplesInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Quadruple<T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quadruple<T, T, T, T> next() {
                return new Quadruple<>(xsi.next(), xsi.next(), xsi.next(), xsi.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> chunkQuintuplesInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Quintuple<T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quintuple<T, T, T, T, T> next() {
                return new Quintuple<>(xsi.next(), xsi.next(), xsi.next(), xsi.next(), xsi.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> chunkSextuplesInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Sextuple<T, T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Sextuple<T, T, T, T, T, T> next() {
                return new Sextuple<>(xsi.next(), xsi.next(), xsi.next(), xsi.next(), xsi.next(), xsi.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> chunkSeptuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<Septuple<T, T, T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Septuple<T, T, T, T, T, T, T> next() {
                return new Septuple<>(
                        xsi.next(),
                        xsi.next(),
                        xsi.next(),
                        xsi.next(),
                        xsi.next(),
                        xsi.next(),
                        xsi.next()
                );
            }
        };
    }

    public static @NotNull Iterable<String> chunk(int size, @NotNull String s) {
        return () -> new NoRemoveIterator<String>() {
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

    public static @NotNull <T> Iterable<List<T>> distinctChunkInfinite(int size, @NotNull Iterable<T> xs) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative. Invalid size: " + size);
        }
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public List<T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < size) {
                    set.add(xsi.next());
                }
                return toList(set);
            }
        };
    }

    public static @NotNull <T> Iterable<Pair<T, T>> distinctChunkPairsInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Pair<T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Pair<T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 2) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Pair<>(setIterator.next(), setIterator.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Triple<T, T, T>> distinctChunkTriplesInfinite(@NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<Triple<T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Triple<T, T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 3) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Triple<>(setIterator.next(), setIterator.next(), setIterator.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctChunkQuadruplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<Quadruple<T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quadruple<T, T, T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 4) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Quadruple<>(setIterator.next(), setIterator.next(), setIterator.next(), setIterator.next());
            }
        };
    }

    public static @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctChunkQuintuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<Quintuple<T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quintuple<T, T, T, T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 5) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Quintuple<>(
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next()
                );
            }
        };
    }

    public static @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctChunkSextuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<Sextuple<T, T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Sextuple<T, T, T, T, T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 6) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Sextuple<>(
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next()
                );
            }
        };
    }

    public static @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctChunkSeptuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<Septuple<T, T, T, T, T, T, T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Septuple<T, T, T, T, T, T, T> next() {
                Set<T> set = new LinkedHashSet<>();
                while (set.size() < 7) {
                    set.add(xsi.next());
                }
                Iterator<T> setIterator = set.iterator();
                return new Septuple<>(
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next(),
                        setIterator.next()
                );
            }
        };
    }

    public static @NotNull <T extends Comparable<T>> Iterable<List<T>> sortedChunkInfinite(
            int size,
            @NotNull Iterable<T> xs
    ) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative. Invalid size: " + size);
        }
        return map(IterableUtils::sort, chunkInfinite(size, xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> sortedChunkPairsInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(p -> Pair.fromList(sort(Pair.toList(p))), chunkPairsInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> sortedChunkTriplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(t -> Triple.fromList(sort(Triple.toList(t))), chunkTriplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> sortedChunkQuadruplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(q -> Quadruple.fromList(sort(Quadruple.toList(q))), chunkQuadruplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> sortedChunkQuintuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(q -> Quintuple.fromList(sort(Quintuple.toList(q))), chunkQuintuplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> sortedChunkSextuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(s -> Sextuple.fromList(sort(Sextuple.toList(s))), chunkSextuplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>>
    sortedChunkSeptuplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(s -> Septuple.fromList(sort(Septuple.toList(s))), chunkSeptuplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<List<T>> sortedDistinctChunkInfinite(
            int size,
            @NotNull Iterable<T> xs
    ) {
        if (size < 0) {
            throw new IllegalArgumentException("size cannot be negative. Invalid size: " + size);
        }
        return map(IterableUtils::sort, distinctChunkInfinite(size, xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> sortedDistinctChunkPairsInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(p -> Pair.fromList(sort(Pair.toList(p))), distinctChunkPairsInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> sortedDistinctChunkTriplesInfinite(
            @NotNull Iterable<T> xs
    ) {
        return map(t -> Triple.fromList(sort(Triple.toList(t))), distinctChunkTriplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>>
    sortedDistinctChunkQuadruplesInfinite(@NotNull Iterable<T> xs) {
        return map(q -> Quadruple.fromList(sort(Quadruple.toList(q))), distinctChunkQuadruplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>>
    sortedDistinctChunkQuintuplesInfinite(@NotNull Iterable<T> xs) {
        return map(q -> Quintuple.fromList(sort(Quintuple.toList(q))), distinctChunkQuintuplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>>
    sortedDistinctChunkSextuplesInfinite(@NotNull Iterable<T> xs) {
        return map(s -> Sextuple.fromList(sort(Sextuple.toList(s))), distinctChunkSextuplesInfinite(xs));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>>
    sortedDistinctChunkSeptuplesInfinite(@NotNull Iterable<T> xs) {
        return map(s -> Septuple.fromList(sort(Septuple.toList(s))), distinctChunkSeptuplesInfinite(xs));
    }

    public static @NotNull <T> Iterable<List<T>> chunkPadded(@Nullable T pad, int size, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<List<T>>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

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

    public static @NotNull <T> Pair<Iterable<T>, Iterable<T>> span(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return new Pair<>(takeWhile(p, xs), dropWhile(p, xs));
    }

    public static @NotNull Pair<String, String> span(@NotNull Predicate<Character> p, @NotNull String s) {
        return new Pair<>(takeWhile(p, s), dropWhile(p, s));
    }

    public static @NotNull <T> Pair<Iterable<T>, Iterable<T>> breakIterable(Predicate<T> p, Iterable<T> xs) {
        return span(p.negate(), xs);
    }

    public static @NotNull Pair<String, String> breakString(@NotNull Predicate<Character> p, @NotNull String s) {
        return span(p.negate(), s);
    }

    public static @NotNull <T> Optional<Iterable<T>> stripPrefix(Iterable<T> prefix, Iterable<T> xs) {
        return isPrefixOf(prefix, xs) ? Optional.of(take(length(prefix), xs)) : Optional.<Iterable<T>>empty();
    }

    public static @NotNull <T> Iterable<Pair<T, Integer>> countAdjacent(@NotNull Iterable<T> xs) {
        return new Iterable<Pair<T, Integer>>() {
            @Override
            public NoRemoveIterator<Pair<T, Integer>> iterator() {
                return new NoRemoveIterator<Pair<T, Integer>>() {
                    private @NotNull Iterator<T> xsi = xs.iterator();
                    private boolean hasNext = xsi.hasNext();
                    private boolean isLast = false;
                    private @Nullable T nextX = null;
                    private @Nullable Pair<T, Integer> next = null;
                    {
                        if (hasNext) {
                            nextX = xsi.next();
                        }
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public Pair<T, Integer> next() {
                        if (isLast) {
                            hasNext = false;
                            return next;
                        } else {
                            Pair<T, Integer> oldNext = next;
                            advance();
                            return oldNext;
                        }
                    }

                    private void advance() {
                        T original = nextX;
                        int count = 0;
                        do {
                            if (count != Integer.MAX_VALUE) count++;
                            if (!xsi.hasNext()) {
                                isLast = true;
                                break;
                            }
                            nextX = xsi.next();
                        } while (Objects.equals(original, nextX));
                        next = new Pair<>(original, count);
                    }
                };
            }
        };
    }

    public static @NotNull Iterable<Pair<Character, Integer>> countAdjacent(@NotNull String s) {
        return countAdjacent(fromString(s));
    }

    public static @NotNull <T> Iterable<List<T>> group(@NotNull Iterable<T> xs) {
        return group(p -> Objects.equals(p.a, p.b), xs);
    }

    public static @NotNull <T> Iterable<String> group(@NotNull String s) {
        return group(p -> p.a == p.b, s);
    }

    public static @NotNull <T> Iterable<List<T>> inits(@NotNull Iterable<T> xs) {
        return cons(new ArrayList<T>(), () -> new NoRemoveIterator<List<T>>() {
            private
            @NotNull
            Iterator<T> xsi = xs.iterator();
            private
            @NotNull
            List<T> currentList = new ArrayList<>();

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> nextList = new ArrayList<>();
                nextList.addAll(currentList);
                nextList.add(xsi.next());
                currentList = nextList;
                return currentList;
            }
        });
    }

    public static @NotNull Iterable<String> inits(@NotNull String s) {
        return map(i -> s.substring(0, i), ExhaustiveProvider.INSTANCE.rangeIncreasing(0, s.length()));
    }

    public static @NotNull <T> Iterable<List<T>> tails(@NotNull Iterable<T> xs) {
        List<T> list = toList(xs);
        return map(
                i -> {
                    List<T> subList = new ArrayList<>();
                    for (int j = i; j < list.size(); j++) {
                        subList.add(list.get(j));
                    }
                    return subList;
                },
                ExhaustiveProvider.INSTANCE.rangeIncreasing(0, list.size())
        );
    }

    public static @NotNull Iterable<String> tails(@NotNull String s) {
        return map(s::substring, ExhaustiveProvider.INSTANCE.rangeIncreasing(0, s.length()));
    }

    public static <T> boolean isPrefixOf(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        Iterator<T> xsi = xs.iterator();
        Iterator<T> ysi = ys.iterator();
        while (xsi.hasNext()) {
            if (!ysi.hasNext()) return false;
            T x = xsi.next();
            T y = ysi.next();
            if (!Objects.equals(x, y)) return false;
        }
        return true;
    }

    public static boolean isPrefixOf(@NotNull String s, @NotNull String t) {
        return s.length() <= t.length() && s.substring(0, t.length()).equals(t);
    }

    public static <T> boolean isSuffixOf(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return isPrefixOf(reverse(xs), reverse(ys));
    }

    public static boolean isSuffixOf(@NotNull String s, @NotNull String t) {
        return s.length() <= t.length() && s.substring(t.length() - s.length()).equals(t);
    }

    public static @NotNull <T> Iterable<List<T>> windows(int size, @NotNull Iterable<T> xs) {
        List<T> firstWindow = toList(take(size, xs));
        if (firstWindow.size() < size) return new ArrayList<>();
        return cons(firstWindow, () -> new NoRemoveIterator<List<T>>() {
            private final
            @NotNull
            Iterator<T> xsi = drop(size, xs).iterator();
            private
            @NotNull
            List<T> previousWindow = firstWindow;

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public List<T> next() {
                previousWindow = toList(concat(tail(previousWindow), Collections.singletonList(xsi.next())));
                return previousWindow;
            }
        });
    }

    public static @NotNull Iterable<String> windows(int size, @NotNull String s) {
        String firstWindow = take(size, s);
        if (firstWindow.length() < size) return new ArrayList<>();
        return cons(firstWindow, () -> new NoRemoveIterator<String>() {
            private
            @NotNull
            Iterator<Character> xsi = fromString(drop(size, s)).iterator();
            private
            @NotNull
            String previousWindow = firstWindow;

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public String next() {
                previousWindow = concat(tail(previousWindow), Character.toString(xsi.next()));
                return previousWindow;
            }
        });
    }

    public static @NotNull <T> Iterable<T> skipLastIf(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        if (isEmpty(xs))
            throw new NoSuchElementException();
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next = xsi.next();
                    private boolean lastIsNext = false;
                    {
                        if (!xsi.hasNext() && !p.test(next)) {
                            lastIsNext = true;
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return xsi.hasNext() || lastIsNext;
                    }

                    @Override
                    public T next() {
                        if (lastIsNext) {
                            lastIsNext = false;
                            return next;
                        }
                        T oldNext = next;
                        next = xsi.next();
                        if (!xsi.hasNext() && !p.test(next)) {
                            lastIsNext = true;
                        }
                        return oldNext;
                    }
                };
            }
        };
    }

    public static @NotNull <A, B> Iterable<B> adjacentPairsWith(
            @NotNull BiFunction<A, A, B> f,
            @NotNull Iterable<A> xs
    ) {
        return map(list -> f.apply(list.get(0), list.get(1)), windows(2, xs));
    }

    /**
     * Returns the differences between successive {@code Byte}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Byte}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Byte}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Byte> deltaByte(@NotNull Iterable<Byte> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> (byte) (y - x), xs);
    }

    /**
     * Returns the differences between successive {@code Short}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Short}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Short}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Short> deltaShort(@NotNull Iterable<Short> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> (short) (y - x), xs);
    }

    /**
     * Returns the differences between successive {@code Integer}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Integer}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Integer}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Integer> deltaInteger(@NotNull Iterable<Integer> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y - x, xs);
    }

    /**
     * Returns the differences between successive {@code Long}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Long}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Long}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Long> deltaLong(@NotNull Iterable<Long> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y - x, xs);
    }

    /**
     * Returns the differences between successive {@code BigInteger}s in {@code xs}. If {@code xs} contains a single
     * {@code BigInteger}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code BigInteger}s.
     * @return Δxs
     */
    public static @NotNull Iterable<BigInteger> deltaBigInteger(@NotNull Iterable<BigInteger> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns the differences between successive {@code BigDecimal}s in {@code xs}. If {@code xs} contains a single
     * {@code BigDecimal}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code BigDecimal}s.
     * @return Δxs
     */
    public static @NotNull Iterable<BigDecimal> deltaBigDecimal(@NotNull Iterable<BigDecimal> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns the differences between successive {@code Float}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Float}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and does not contain any nulls or negative zeros.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Float}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Float> deltaFloat(@NotNull Iterable<Float> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return map(FloatingPointUtils::absNegativeZeros, adjacentPairsWith((x, y) -> y - x, xs));
    }

    /**
     * Returns the differences between successive {@code Double}s in {@code xs}. Overflow may occur. If {@code xs}
     * contains a single {@code Double}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls or negative zeros.</li>
     *  <li>The result is finite and does not contain any nulls or negative zeros.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Double}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Double> deltaDouble(@NotNull Iterable<Double> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return map(FloatingPointUtils::absNegativeZeros, adjacentPairsWith((x, y) -> y - x, xs));
    }

    /**
     * Returns the differences between successive {@code Character}s in {@code xs}. If {@code xs} contains a single
     * {@code Character}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must be finite, must not be empty and may not contain any nulls.</li>
     *  <li>The result is finite and only contains {@code Integer}s with absolute value less than 2<sup>16</sup>.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Character}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Integer> deltaCharacter(@NotNull Iterable<Character> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y - x, xs);
    }

    public static <T> boolean same(@NotNull Iterable<T> xs) {
        if (isEmpty(xs)) return true;
        T head = head(xs);
        return all(x -> Objects.equals(x, head), tail(xs));
    }

    public static boolean same(@NotNull String s) {
        if (isEmpty(s)) return true;
        char head = head(s);
        return all(c -> c == head, tail(s));
    }

    public static <T> boolean isInfixOf(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return any(zs -> equal(xs, zs), windows(length(xs), ys));
    }

    public static boolean isInfixOf(@NotNull String s, @NotNull String t) {
        return t.contains(s);
    }

    public static @NotNull <T> Iterable<T> mux(@NotNull List<Iterable<T>> xss) {
        return concat(map(list -> list, transpose(xss)));
    }

    public static @NotNull String muxStrings(@NotNull List<String> xss) {
        return concatStrings(transposeStrings(xss));
    }

    public static @NotNull <T> List<Iterable<T>> demux(int size, @NotNull Iterable<T> xs) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        List<Iterable<T>> demuxed = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Iterable<Boolean> mask = concat(
                    replicate(i, false),
                    cycle(cons(true, (Iterable<Boolean>) replicate(size - 1, false)))
            );
            demuxed.add(select(mask, xs));
        }
        return demuxed;
    }

    public static @NotNull List<String> demux(int size, @NotNull String s) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        List<String> demuxed = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Iterable<Boolean> mask = concat(
                    replicate(i, false),
                    cycle(cons(true, (Iterable<Boolean>) replicate(size - 1, false)))
            );
            demuxed.add(select(mask, s));
        }
        return demuxed;
    }

    public static <T> boolean elem(@Nullable T x, @NotNull Iterable<T> xs) {
        return any(y -> Objects.equals(x, y), xs);
    }

    public static <T> boolean elem(@Nullable T x, @NotNull Collection<T> xs) {
        return xs.contains(x);
    }

    public static boolean elem(char c, @NotNull String s) {
        return s.indexOf(c) != -1;
    }

    public static <T> boolean notElem(@Nullable T x, @NotNull Iterable<T> xs) {
        return all(y -> !Objects.equals(x, y), xs);
    }

    public static <T> boolean notElem(@Nullable T x, @NotNull Collection<T> xs) {
        return !xs.contains(x);
    }

    public static boolean notElem(char c, @NotNull String s) {
        return s.indexOf(c) == -1;
    }

    public static @NotNull <A, B> NullableOptional<B> lookup(@Nullable A x, @NotNull Iterable<Pair<A, B>> xys) {
        for (Pair<A, B> xy : xys) {
            if (Objects.equals(x, xy.a)) return NullableOptional.of(xy.b);
        }
        return NullableOptional.empty();
    }

    public static <A extends Comparable<A>, B> NullableOptional<B> lookupSorted(List<Pair<A, B>> xs, A key) {
        int low = 0;
        int high = xs.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            A midKey = xs.get(mid).a;
            int cmp = midKey.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return NullableOptional.of(xs.get(mid).b);
            }
        }
        return NullableOptional.empty();
    }

    public static @NotNull <T> Optional<T> find(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        for (T x : xs) {
            if (p.test(x)) return Optional.of(x);
        }
        return Optional.empty();
    }

    public static @NotNull Optional<Character> find(@NotNull Predicate<Character> p, @NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (p.test(c)) return Optional.of(c);
        }
        return Optional.empty();
    }

    public static @NotNull <T> Iterable<T> filter(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        if (!hasNext) {
                            throw new NoSuchElementException();
                        }
                        T current = next;
                        advance();
                        return current;
                    }

                    private void advance() {
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

    public static @NotNull <T> Iterable<T> filterInfinite(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                T result;
                do {
                    result = xsi.next();
                } while (!p.test(result));
                return result;
            }
        };
    }

    public static @NotNull <T> Iterable<T> filter(int n, @NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < n;
            }

            @Override
            public T next() {
                T result;
                do {
                    result = xsi.next();
                } while (!p.test(result));
                i++;
                return result;
            }
        };
    }

    public static @NotNull <T> Iterable<T> filter(
            @NotNull BigInteger n,
            @NotNull Predicate<T> p,
            @NotNull Iterable<T> xs
    ) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private @NotNull BigInteger i = BigInteger.ZERO;

            @Override
            public boolean hasNext() {
                return lt(i, n);
            }

            @Override
            public T next() {
                T result;
                do {
                    result = xsi.next();
                } while (!p.test(result));
                i = i.add(BigInteger.ONE);
                return result;
            }
        };
    }

    public static @NotNull String filter(@NotNull Predicate<Character> p, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (p.test(c)) sb.append(c);
        }
        return sb.toString();
    }

    public static @NotNull <T> Pair<Iterable<T>, Iterable<T>> partition(
            @NotNull Predicate<T> p,
            @NotNull Iterable<T> xs) {
        return new Pair<>(filter(p, xs), filter(x -> !p.test(x), xs));
    }

    public static @NotNull Pair<String, String> partition(@NotNull Predicate<Character> p, @NotNull String s) {
        StringBuilder sba = new StringBuilder();
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            (p.test(c) ? sba : sbb).append(c);
        }
        return new Pair<>(sba.toString(), sbb.toString());
    }

    public static <T> T get(@NotNull Iterable<T> xs, int i) {
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

    public static <T> T get(@NotNull Iterable<T> xs, @NotNull BigInteger i) {
        if (lt(i, BigInteger.ZERO))
            throw new IndexOutOfBoundsException();
        Iterator<T> xsi = xs.iterator();
        T element = null;
        for (BigInteger j : ExhaustiveProvider.INSTANCE.rangeIncreasing(BigInteger.ONE, i)) {
            if (!xsi.hasNext())
                throw new IndexOutOfBoundsException();
            element = xsi.next();
        }
        return element;
    }

    public static <T> T get(@NotNull List<T> xs, int i) {
        return xs.get(i);
    }

    public static char get(@NotNull String s, int i) {
        return s.charAt(i);
    }

    public static @NotNull <T> Iterable<T> set(@NotNull Iterable<T> xs, int i, @Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();
            private int j = 0;

            @Override
            public boolean hasNext() {
                return xsi.hasNext();
            }

            @Override
            public T next() {
                T originalNext = xsi.next();
                T next = i == j ? x : originalNext;
                j++;
                return next;
            }
        };
    }

    public static @NotNull String set(@NotNull String s, int i, char c) {
        StringBuilder sb = new StringBuilder(s);
        sb.setCharAt(i, c);
        return sb.toString();
    }

    public static @NotNull <T> Iterable<T> insert(@NotNull Iterable<T> xs, int i, @Nullable T x) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();
            private int j = 0;

            @Override
            public boolean hasNext() {
                return i == j || xsi.hasNext();
            }

            @Override
            public T next() {
                T next = i == j ? x : xsi.next();
                j++;
                return next;
            }
        };
    }

    public static @NotNull String insert(@NotNull String s, int i, char c) {
        StringBuilder sb = new StringBuilder(s);
        sb.insert(i, c);
        return sb.toString();
    }

    public static @NotNull <T> Iterable<T> select(@NotNull Iterable<Boolean> bs, @NotNull Iterable<T> xs) {
        return map(p -> p.b, filter(p -> p.a, zip(bs, xs)));
    }

    public static @NotNull String select(@NotNull Iterable<Boolean> bs, @NotNull String s) {
        return charsToString(
                map(p -> p.b, filter(p -> p.a, (Iterable<Pair<Boolean, Character>>) zip(bs, fromString(s))))
        );
    }

    public static @NotNull <T> Optional<Integer> elemIndex(@Nullable T x, @NotNull Iterable<T> xs) {
        int i = 0;
        for (T y : xs) {
            if (Objects.equals(x, y)) return Optional.of(i);
            i++;
        }
        return Optional.empty();
    }

    public static @NotNull <T> Optional<BigInteger> bigIntegerElemIndex(@Nullable T x, @NotNull Iterable<T> xs) {
        BigInteger i = BigInteger.ZERO;
        for (T y : xs) {
            if (Objects.equals(x, y)) return Optional.of(i);
            i = i.add(BigInteger.ONE);
        }
        return Optional.empty();
    }

    public static @NotNull Optional<Integer> elemIndex(char c, @NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) return Optional.of(i);
        }
        return Optional.empty();
    }

    public static @NotNull <T> Iterable<Integer> elemIndices(@Nullable T x, @NotNull Iterable<T> xs) {
        return map(
                p -> p.a,
                filter(p -> Objects.equals(x, p.b), zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0), xs))
        );
    }

    public static @NotNull <T> Iterable<BigInteger> bigIntegerElemIndices(@Nullable T x, @NotNull Iterable<T> xs) {
        return map(
                p -> p.a,
                filter(
                        p -> Objects.equals(x, p.b),
                        zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ZERO), xs)
                )
        );
    }

    public static @NotNull Iterable<Integer> elemIndices(char c, @NotNull String s) {
        return map(
                p -> p.a,
                filter(p -> p.b == c, zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0), fromString(s)))
        );
    }

    public static @NotNull <T> Optional<Integer> findIndex(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        int i = 0;
        for (T x : xs) {
            if (p.test(x)) return Optional.of(i);
            i++;
        }
        return Optional.empty();
    }

    public static @NotNull <T> Optional<BigInteger> bigIntegerFindIndex(
            @NotNull Predicate<T> p,
            @NotNull Iterable<T> xs
    ) {
        BigInteger i = BigInteger.ZERO;
        for (T x : xs) {
            if (p.test(x)) return Optional.of(i);
            i = i.add(BigInteger.ONE);
        }
        return Optional.empty();
    }

    public static @NotNull Optional<Integer> findIndex(@NotNull Predicate<Character> p, @NotNull String s) {
        for (int i = 0; i < s.length(); i++) {
            if (p.test(s.charAt(i))) return Optional.of(i);
        }
        return Optional.empty();
    }

    public static @NotNull <T> Iterable<Integer> findIndices(@NotNull Predicate<T> p, @NotNull Iterable<T> xs) {
        return map(q -> q.a, filter(q -> p.test(q.b), zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0), xs)));
    }

    public static @NotNull <T> Iterable<BigInteger> bigIntegerFindIndices(
            @NotNull Predicate<T> p,
            @NotNull Iterable<T> xs
    ) {
        return map(
                q -> q.a,
                filter(q -> p.test(q.b), zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ZERO), xs))
        );
    }

    public static @NotNull Iterable<Integer> findIndices(@NotNull Predicate<Character> p, @NotNull String s) {
        return map(
                q -> q.a,
                filter(q -> p.test(q.b), zip(ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0), fromString(s)))
        );
    }

    public static <A, B> Iterable<Pair<A, B>> zip(Iterable<A> as, Iterable<B> bs) {
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();

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
        return () -> new NoRemoveIterator<Triple<A, B, C>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();

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

    public static <A, B, C, D> Iterable<Quadruple<A, B, C, D>> zip4(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds
    ) {
        return () -> new NoRemoveIterator<Quadruple<A, B, C, D>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();

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

    public static <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> zip5(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es
    ) {
        return () -> new NoRemoveIterator<Quintuple<A, B, C, D, E>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() && bsi.hasNext() && csi.hasNext() && dsi.hasNext() && esi.hasNext();
            }

            @Override
            public Quintuple<A, B, C, D, E> next() {
                return new Quintuple<>(asi.next(), bsi.next(), csi.next(), dsi.next(), esi.next());
            }
        };
    }

    public static <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> zip6(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs
    ) {
        return () -> new NoRemoveIterator<Sextuple<A, B, C, D, E, F>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() &&
                        bsi.hasNext() &&
                        csi.hasNext() &&
                        dsi.hasNext() &&
                        esi.hasNext() &&
                        fsi.hasNext();
            }

            @Override
            public Sextuple<A, B, C, D, E, F> next() {
                return new Sextuple<>(asi.next(), bsi.next(), csi.next(), dsi.next(), esi.next(), fsi.next());
            }
        };
    }

    public static <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> zip7(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs,
            Iterable<G> gs
    ) {
        return () -> new NoRemoveIterator<Septuple<A, B, C, D, E, F, G>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();
            private final @NotNull Iterator<G> gsi = gs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() &&
                        bsi.hasNext() &&
                        csi.hasNext() &&
                        dsi.hasNext() &&
                        esi.hasNext() &&
                        fsi.hasNext() &&
                        gsi.hasNext();
            }

            @Override
            public Septuple<A, B, C, D, E, F, G> next() {
                return new Septuple<>(
                        asi.next(),
                        bsi.next(),
                        csi.next(),
                        dsi.next(),
                        esi.next(),
                        fsi.next(),
                        gsi.next()
                );
            }
        };
    }

    public static <A, B> Iterable<Pair<A, B>> zipInfinite(Iterable<A> as, Iterable<B> bs) {
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Pair<A, B> next() {
                return new Pair<>(asi.next(), bsi.next());
            }
        };
    }

    public static <A, B, C> Iterable<Triple<A, B, C>> zip3Infinite(Iterable<A> as, Iterable<B> bs, Iterable<C> cs) {
        return () -> new NoRemoveIterator<Triple<A, B, C>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Triple<A, B, C> next() {
                return new Triple<>(asi.next(), bsi.next(), csi.next());
            }
        };
    }

    public static <A, B, C, D> Iterable<Quadruple<A, B, C, D>> zip4Infinite(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds
    ) {
        return () -> new NoRemoveIterator<Quadruple<A, B, C, D>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quadruple<A, B, C, D> next() {
                return new Quadruple<>(asi.next(), bsi.next(), csi.next(), dsi.next());
            }
        };
    }

    public static <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> zip5Infinite(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es
    ) {
        return () -> new NoRemoveIterator<Quintuple<A, B, C, D, E>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Quintuple<A, B, C, D, E> next() {
                return new Quintuple<>(asi.next(), bsi.next(), csi.next(), dsi.next(), esi.next());
            }
        };
    }

    public static <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> zip6Infinite(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs
    ) {
        return () -> new NoRemoveIterator<Sextuple<A, B, C, D, E, F>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Sextuple<A, B, C, D, E, F> next() {
                return new Sextuple<>(asi.next(), bsi.next(), csi.next(), dsi.next(), esi.next(), fsi.next());
            }
        };
    }

    public static <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> zip7Infinite(
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs,
            Iterable<G> gs
    ) {
        return () -> new NoRemoveIterator<Septuple<A, B, C, D, E, F, G>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();
            private final @NotNull Iterator<G> gsi = gs.iterator();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Septuple<A, B, C, D, E, F, G> next() {
                return new Septuple<>(
                        asi.next(),
                        bsi.next(),
                        csi.next(),
                        dsi.next(),
                        esi.next(),
                        fsi.next(),
                        gsi.next()
                );
            }
        };
    }

    public static <A, B> Iterable<Pair<A, B>> zipPadded(A aPad, B bPad, Iterable<A> as, Iterable<B> bs) {
        return () -> new NoRemoveIterator<Pair<A, B>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();

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

    public static <A, B, C> Iterable<Triple<A, B, C>> zip3Padded(
            A aPad,
            B bPad,
            C cPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs
    ) {
        return () -> new NoRemoveIterator<Triple<A, B, C>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();

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

    public static <A, B, C, D> Iterable<Quadruple<A, B, C, D>> zip4Padded(
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds
    ) {
        return () -> new NoRemoveIterator<Quadruple<A, B, C, D>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();

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

    public static <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> zip5Padded(
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es) {
        return () -> new NoRemoveIterator<Quintuple<A, B, C, D, E>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() || bsi.hasNext() || csi.hasNext() || dsi.hasNext() || esi.hasNext();
            }

            @Override
            public Quintuple<A, B, C, D, E> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                C c = csi.hasNext() ? csi.next() : cPad;
                D d = dsi.hasNext() ? dsi.next() : dPad;
                E e = esi.hasNext() ? esi.next() : ePad;
                return new Quintuple<>(a, b, c, d, e);
            }
        };
    }

    public static <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> zip6Padded(
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            F fPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs) {
        return () -> new NoRemoveIterator<Sextuple<A, B, C, D, E, F>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() ||
                        bsi.hasNext() ||
                        csi.hasNext() ||
                        dsi.hasNext() ||
                        esi.hasNext() ||
                        fsi.hasNext();
            }

            @Override
            public Sextuple<A, B, C, D, E, F> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                C c = csi.hasNext() ? csi.next() : cPad;
                D d = dsi.hasNext() ? dsi.next() : dPad;
                E e = esi.hasNext() ? esi.next() : ePad;
                F f = fsi.hasNext() ? fsi.next() : fPad;
                return new Sextuple<>(a, b, c, d, e, f);
            }
        };
    }

    public static <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> zip7Padded(
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            F fPad,
            G gPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs,
            Iterable<G> gs) {
        return () -> new NoRemoveIterator<Septuple<A, B, C, D, E, F, G>>() {
            private final @NotNull Iterator<A> asi = as.iterator();
            private final @NotNull Iterator<B> bsi = bs.iterator();
            private final @NotNull Iterator<C> csi = cs.iterator();
            private final @NotNull Iterator<D> dsi = ds.iterator();
            private final @NotNull Iterator<E> esi = es.iterator();
            private final @NotNull Iterator<F> fsi = fs.iterator();
            private final @NotNull Iterator<G> gsi = gs.iterator();

            @Override
            public boolean hasNext() {
                return asi.hasNext() ||
                        bsi.hasNext() ||
                        csi.hasNext() ||
                        dsi.hasNext() ||
                        esi.hasNext() ||
                        fsi.hasNext() ||
                        gsi.hasNext();
            }

            @Override
            public Septuple<A, B, C, D, E, F, G> next() {
                A a = asi.hasNext() ? asi.next() : aPad;
                B b = bsi.hasNext() ? bsi.next() : bPad;
                C c = csi.hasNext() ? csi.next() : cPad;
                D d = dsi.hasNext() ? dsi.next() : dPad;
                E e = esi.hasNext() ? esi.next() : ePad;
                F f = fsi.hasNext() ? fsi.next() : fPad;
                G g = gsi.hasNext() ? gsi.next() : gPad;
                return new Septuple<>(a, b, c, d, e, f, g);
            }
        };
    }

    public static <A, B, O> Iterable<O> zipWith(BiFunction<A, B, O> f, Iterable<A> as, Iterable<B> bs) {
        return map(p -> f.apply(p.a, p.b), zip(as, bs));
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

    public static <A, B, C, D, E, O> Iterable<O> zipWith5(
            Function<Quintuple<A, B, C, D, E>, O> f,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es
    ) {
        return map(f, zip5(as, bs, cs, ds, es));
    }

    public static <A, B, C, D, E, F, O> Iterable<O> zipWith6(
            Function<Sextuple<A, B, C, D, E, F>, O> f,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs
    ) {
        return map(f, zip6(as, bs, cs, ds, es, fs));
    }

    public static <A, B, C, D, E, F, G, O> Iterable<O> zipWith6(
            Function<Septuple<A, B, C, D, E, F, G>, O> f,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs,
            Iterable<G> gs
    ) {
        return map(f, zip7(as, bs, cs, ds, es, fs, gs));
    }

    public static <A, B, O> Iterable<O> zipWithPadded(
            BiFunction<A, B, O> f,
            A aPad,
            B bPad,
            Iterable<A> as,
            Iterable<B> bs
    ) {
        return map(p -> f.apply(p.a, p.b), zipPadded(aPad, bPad, as, bs));
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

    public static <A, B, C, D, E, O> Iterable<O> zipWith5Padded(
            Function<Quintuple<A, B, C, D, E>, O> f,
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es
    ) {
        return map(f, zip5Padded(aPad, bPad, cPad, dPad, ePad, as, bs, cs, ds, es));
    }

    public static <A, B, C, D, E, F, O> Iterable<O> zipWith6Padded(
            Function<Sextuple<A, B, C, D, E, F>, O> f,
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            F fPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs
    ) {
        return map(f, zip6Padded(aPad, bPad, cPad, dPad, ePad, fPad, as, bs, cs, ds, es, fs));
    }

    public static <A, B, C, D, E, F, G, O> Iterable<O> zipWith7Padded(
            Function<Septuple<A, B, C, D, E, F, G>, O> f,
            A aPad,
            B bPad,
            C cPad,
            D dPad,
            E ePad,
            F fPad,
            G gPad,
            Iterable<A> as,
            Iterable<B> bs,
            Iterable<C> cs,
            Iterable<D> ds,
            Iterable<E> es,
            Iterable<F> fs,
            Iterable<G> gs
    ) {
        return map(f, zip7Padded(aPad, bPad, cPad, dPad, ePad, fPad, gPad, as, bs, cs, ds, es, fs, gs));
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

    public static <A, B, C, D, E> Quintuple<
            Iterable<A>,
            Iterable<B>,
            Iterable<C>,
            Iterable<D>,
            Iterable<E>
            > unzip5(Iterable<Quintuple<A, B, C, D, E>> ps) {
        return new Quintuple<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps),
                map(p -> p.c, ps),
                map(p -> p.d, ps),
                map(p -> p.e, ps)
        );
    }

    public static <A, B, C, D, E, F> Sextuple<
            Iterable<A>,
            Iterable<B>,
            Iterable<C>,
            Iterable<D>,
            Iterable<E>,
            Iterable<F>
            > unzip6(Iterable<Sextuple<A, B, C, D, E, F>> ps) {
        return new Sextuple<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps),
                map(p -> p.c, ps),
                map(p -> p.d, ps),
                map(p -> p.e, ps),
                map(p -> p.f, ps)
        );
    }

    public static <A, B, C, D, E, F, G> Septuple<
            Iterable<A>,
            Iterable<B>,
            Iterable<C>,
            Iterable<D>,
            Iterable<E>,
            Iterable<F>,
            Iterable<G>
            > unzip7(Iterable<Septuple<A, B, C, D, E, F, G>> ps) {
        return new Septuple<>(
                map(p -> p.a, ps),
                map(p -> p.b, ps),
                map(p -> p.c, ps),
                map(p -> p.d, ps),
                map(p -> p.e, ps),
                map(p -> p.f, ps),
                map(p -> p.g, ps)
        );
    }

    public static @NotNull Iterable<String> lines(@NotNull String s) {
        Iterable<String> grouped = group(p -> (p.a == '\n') == (p.b == '\n'), s);
        Iterable<Boolean> selection = cycle(Arrays.asList(true, false));
        if (head(head(grouped)) == '\n') selection = tail(selection);
        return select(selection, grouped);
    }

    public static @NotNull Iterable<String> words(@NotNull String s) {
        Iterable<String> grouped = group(p -> Character.isWhitespace(p.a) == Character.isWhitespace(p.b), s);
        Iterable<Boolean> selection = cycle(Arrays.asList(true, false));
        if (head(head(grouped)) == '\n') selection = tail(selection);
        return select(selection, grouped);
    }

    public static @NotNull String unlines(@NotNull Iterable<String> lines) {
        return concatMapStrings(line -> line + "\n", lines);
    }

    public static @NotNull String unwords(@NotNull Iterable<String> words) {
        return intercalate(" ", words);
    }

    public static @NotNull <T> Iterable<T> nub(@NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private Set<T> seen = new HashSet<>();
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T current = next;
                        advance();
                        return current;
                    }

                    private void advance() {
                        while (xsi.hasNext()) {
                            next = xsi.next();
                            if (seen.add(next)) {
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

    public static @NotNull String nub(@NotNull String s) {
        Set<Character> set = new LinkedHashSet<>();
        for (int i = 0; i < s.length(); i++) {
            set.add(s.charAt(i));
        }
        return charsToString(set);
    }

    public static <T> boolean unique(@NotNull Iterable<T> xs) {
        Set<T> seen = new HashSet<>();
        for (T x : xs) {
            if (seen.contains(x)) return false;
            seen.add(x);
        }
        return true;
    }

    public static boolean unique(@NotNull String s) {
        Set<Character> seen = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (seen.contains(c)) return false;
            seen.add(c);
        }
        return true;
    }

    public static @NotNull <T> Iterable<T> delete(@Nullable T x, @NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    private boolean seenOnce;

                    {
                        hasNext = true;
                        seenOnce = false;
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T oldNext = next;
                        advance();
                        return oldNext;
                    }

                    private void advance() {
                        if (!xsi.hasNext()) {
                            hasNext = false;
                            return;
                        }
                        next = xsi.next();
                        if (!seenOnce && Objects.equals(next, x)) {
                            seenOnce = true;
                            advance();
                        }
                    }
                };
            }
        };
    }

    public static @NotNull String delete(char c, @NotNull String s) {
        StringBuilder sb = new StringBuilder();
        boolean seenOnce = false;
        for (int i = 0; i < s.length(); i++) {
            char d = s.charAt(i);
            if (!seenOnce && c == d) {
                seenOnce = true;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static <T> boolean isSubsetOf(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        if (isEmpty(xs)) return true;
        HashSet<T> set = toHashSet(xs);
        for (T y : ys) {
            set.remove(y);
            if (set.isEmpty()) return true;
        }
        return false;
    }

    public static boolean isSubsetOf(@NotNull String s, @NotNull String t) {
        return isSubsetOf(fromString(s), fromString(t));
    }

    public static @NotNull <T> Iterable<T> intersect(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return () -> new NoRemoveIterator<T>() {
            private final @NotNull Iterator<T> xsi = xs.iterator();
            private final @NotNull Iterator<T> ysi = ys.iterator();
            private final @NotNull Map<T, Integer> seenXs = new HashMap<>();
            private final @NotNull Map<T, Integer> seenYs = new HashMap<>();
            private T next_;
            private boolean hasNext_ = advance();

            private boolean advance() {
                while (xsi.hasNext() && ysi.hasNext()) {
                    T x = xsi.next();
                    Integer xCount = seenYs.get(x);
                    if (xCount != null) {
                        if (xCount == 1) {
                            seenYs.remove(x);
                        } else {
                            seenYs.put(x, xCount - 1);
                        }
                        next_ = x;
                        return true;
                    }
                    xCount = seenXs.get(x);
                    if (xCount == null) {
                        xCount = 0;
                    }
                    seenXs.put(x, xCount + 1);

                    T y = ysi.next();
                    Integer yCount = seenXs.get(y);
                    if (yCount != null) {
                        if (yCount == 1) {
                            seenXs.remove(y);
                        } else {
                            seenXs.put(y, yCount - 1);
                        }
                        next_ = y;
                        return true;
                    }
                    yCount = seenYs.get(y);
                    if (yCount == null) {
                        yCount = 0;
                    }
                    seenYs.put(y, yCount + 1);
                }
                while (xsi.hasNext()) {
                    T x = xsi.next();
                    Integer xCount = seenYs.get(x);
                    if (xCount != null) {
                        if (xCount == 1) {
                            seenYs.remove(x);
                        } else {
                            seenYs.put(x, xCount - 1);
                        }
                        next_ = x;
                        return true;
                    }
                }
                while (ysi.hasNext()) {
                    T y = ysi.next();
                    Integer yCount = seenXs.get(y);
                    if (yCount != null) {
                        if (yCount == 1) {
                            seenXs.remove(y);
                        } else {
                            seenXs.put(y, yCount - 1);
                        }
                        next_ = y;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean hasNext() {
                return hasNext_;
            }

            @Override
            public T next() {
                T oldNext = next_;
                hasNext_ = advance();
                return oldNext;
            }
        };
    }

    public static @NotNull String intersect(@NotNull String s, @NotNull String t) {
        return charsToString(intersect(fromString(s), fromString(t)));
    }

    public static @NotNull <T extends Comparable<T>> Iterable<T> merge(
            @NotNull Iterable<T> xs,
            @NotNull Iterable<T> ys
    ) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();
            private @NotNull Iterator<T> ysi = ys.iterator();
            private @NotNull NullableOptional<T> ox = NullableOptional.empty();
            private @NotNull NullableOptional<T> oy = NullableOptional.empty();

            @Override
            public boolean hasNext() {
                return xsi.hasNext() || ysi.hasNext();
            }

            @Override
            public T next() {
                if (!xsi.hasNext()) {
                    if (oy.isPresent()) {
                        T y = oy.get();
                        oy = NullableOptional.empty();
                        return y;
                    } else {
                        return ysi.next();
                    }
                }
                if (!xsi.hasNext()) {
                    if (ox.isPresent()) {
                        T x = ox.get();
                        ox = NullableOptional.empty();
                        return x;
                    } else {
                        return xsi.next();
                    }
                }
                if (!ox.isPresent() && xsi.hasNext()) {
                    ox = NullableOptional.of(xsi.next());
                }
                if (!oy.isPresent() && ysi.hasNext()) {
                    oy = NullableOptional.of(ysi.next());
                }
                T next;
                if (lt(ox.get(), oy.get())) {
                    next = ox.get();
                    ox = NullableOptional.empty();
                } else {
                    next = oy.get();
                    oy = NullableOptional.empty();
                }
                return next;
            }
        };
    }

    public static @NotNull <T extends Comparable<T>> Iterable<T> orderedIntersection(
            @NotNull Iterable<T> xs,
            @NotNull Iterable<T> ys
    ) {
        Iterable<Pair<T, Integer>> merged = merge(
                (p, q) -> {
                    Ordering o = compare(p.a, q.a);
                    if (o != EQ) return o.toInt();
                    return Integer.compare(p.b, q.b);
                },
                countAdjacent(xs),
                countAdjacent(ys)
        );
        Iterable<Pair<T, Integer>> intersected = map(
                ps -> {
                    int frequency = ps.size() == 1 ? 0 : min(ps.get(0).b, ps.get(1).b);
                    return new Pair<>(ps.get(0).a, frequency);
                },
                group(p -> p.a.a.equals(p.b.a), merged)
        );
        return concatMap(p -> replicate(p.b, p.a), intersected);
    }

    public static @NotNull <T extends Comparable<T>> Iterable<T> orderedIntersection(
            @NotNull Comparator<T> comparator,
            @NotNull Iterable<T> xs,
            @NotNull Iterable<T> ys
    ) {
        Iterable<Pair<T, Integer>> merged = merge(
                (p, q) -> {
                    Ordering o = compare(comparator, p.a, q.a);
                    if (o != EQ) return o.toInt();
                    return Integer.compare(p.b, q.b);
                },
                countAdjacent(xs),
                countAdjacent(ys)
        );
        Iterable<Pair<T, Integer>> intersected = map(
                ps -> {
                    int frequency = ps.size() == 1 ? 0 : min(ps.get(0).b, ps.get(1).b);
                    return new Pair<>(ps.get(0).a, frequency);
                },
                group(p -> p.a.a.equals(p.b.a), merged)
        );
        return concatMap(p -> replicate(p.b, p.a), intersected);
    }

    public static @NotNull <T> Iterable<T> merge(
            @NotNull Comparator<T> comparator,
            @NotNull Iterable<T> xs,
            @NotNull Iterable<T> ys
    ) {
        return () -> new NoRemoveIterator<T>() {
            private @NotNull Iterator<T> xsi = xs.iterator();
            private @NotNull Iterator<T> ysi = ys.iterator();
            private @NotNull NullableOptional<T> ox = NullableOptional.empty();
            private @NotNull NullableOptional<T> oy = NullableOptional.empty();

            @Override
            public boolean hasNext() {
                return xsi.hasNext() || ysi.hasNext();
            }

            @Override
            public T next() {
                if (!xsi.hasNext()) {
                    if (oy.isPresent()) {
                        T y = oy.get();
                        oy = NullableOptional.empty();
                        return y;
                    } else {
                        return ysi.next();
                    }
                }
                if (!xsi.hasNext()) {
                    if (ox.isPresent()) {
                        T x = ox.get();
                        ox = NullableOptional.empty();
                        return x;
                    } else {
                        return xsi.next();
                    }
                }
                if (!ox.isPresent() && xsi.hasNext()) {
                    ox = NullableOptional.of(xsi.next());
                }
                if (!oy.isPresent() && ysi.hasNext()) {
                    oy = NullableOptional.of(ysi.next());
                }
                T next;
                if (lt(comparator, ox.get(), oy.get())) {
                    next = ox.get();
                    ox = NullableOptional.empty();
                } else {
                    next = oy.get();
                    oy = NullableOptional.empty();
                }
                return next;
            }
        };
    }

    public static @NotNull <T extends Comparable<T>> List<T> sort(@NotNull Iterable<T> xss) {
        List<T> list = toList(xss);
        Collections.sort(list);
        return list;
    }

    public static @NotNull String sort(@NotNull String s) {
        List<Character> list = toList(s);
        Collections.sort(list);
        return charsToString(list);
    }

    public static @NotNull <T> Iterable<T> nub(@NotNull Predicate<Pair<T, T>> p, @NotNull Iterable<T> xs) {
        return new Iterable<T>() {
            private Set<T> seen = new HashSet<>();
            @Override
            public Iterator<T> iterator() {
                return new NoRemoveIterator<T>() {
                    private final @NotNull Iterator<T> xsi = xs.iterator();
                    private @Nullable T next;
                    private boolean hasNext;
                    {
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        T current = next;
                        advance();
                        return current;
                    }

                    private void advance() {
                        while (xsi.hasNext()) {
                            next = xsi.next();
                            boolean good = !seen.contains(next) && !any(x -> p.test(new Pair<T, T>(next, x)), seen);
                            if (good) {
                                seen.add(next);
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

    public static @NotNull String nub(@NotNull Predicate<Pair<Character, Character>> p, @NotNull String s) {
        Set<Character> seen = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!seen.contains(c) && !any(x -> p.test(new Pair<Character, Character>(c, x)), seen)) {
                seen.add(c);
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static @NotNull <T> List<T> sort(@NotNull Comparator<T> comparator, @NotNull Iterable<T> xss) {
        List<T> list = toList(xss);
        Collections.sort(list, comparator);
        return list;
    }

    public static @NotNull String sort(@NotNull Comparator<Character> comparator, @NotNull String s) {
        List<Character> list = toList(s);
        Collections.sort(list, comparator);
        return charsToString(list);
    }

    public static @NotNull <T> Iterable<List<T>> group(
            @NotNull Predicate<Pair<T, T>> p,
            @NotNull Iterable<T> xs
    ) {
        return new Iterable<List<T>>() {
            @Override
            public Iterator<List<T>> iterator() {
                return new NoRemoveIterator<List<T>>() {
                    private @NotNull Iterator<T> xsi = xs.iterator();
                    private boolean hasNext = xsi.hasNext();
                    private boolean isLast = false;
                    private @Nullable T nextX = null;
                    private @NotNull List<T> next = null;
                    {
                        if (hasNext) {
                            nextX = xsi.next();
                        }
                        advance();
                    }

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public List<T> next() {
                        if (isLast) {
                            hasNext = false;
                            return next;
                        } else {
                            List<T> oldNext = next;
                            advance();
                            return oldNext;
                        }
                    }

                    private void advance() {
                        T original = nextX;
                        List<T> list = new ArrayList<>();
                        do {
                            list.add(nextX);
                            if (!xsi.hasNext()) {
                                isLast = true;
                                break;
                            }
                            nextX = xsi.next();
                        } while (p.test(new Pair<T, T>(original, nextX)));
                        next = list;
                    }
                };
            }
        };
    }

    public static @NotNull <T> Iterable<Pair<T, Integer>> frequencies(@NotNull Iterable<T> xs) {
        Map<T, Integer> frequencies = new LinkedHashMap<>();
        for (T x : xs) {
            Integer frequency = frequencies.get(x);
            if (frequency == null) {
                frequency = 0;
            }
            frequency++;
            frequencies.put(x, frequency);
        }
        return fromMap(frequencies);
    }

    public static @NotNull Iterable<Boolean> booleansFromIndices(@NotNull Iterable<BigInteger> indices) {
        return () -> new NoRemoveIterator<Boolean>() {
            private final @NotNull Iterator<BigInteger> is = indices.iterator();
            private @NotNull Optional<BigInteger> nextIndex = is.hasNext() ? Optional.of(is.next()) : Optional.empty();
            private @NotNull BigInteger i = BigInteger.ZERO;

            @Override
            public boolean hasNext() {
                return nextIndex.isPresent();
            }

            @Override
            public @NotNull Boolean next() {
                boolean result = nextIndex.get().equals(i);
                if (result) {
                    nextIndex = is.hasNext() ? Optional.of(is.next()) : Optional.empty();
                }
                i = i.add(BigInteger.ONE);
                return result;
            }
        };
    }

    public static @NotNull Iterable<String> group(
            @NotNull Predicate<Pair<Character, Character>> p,
            @NotNull String s
    ) {
        return map(IterableUtils::charsToString, group(p, fromString(s)));
    }

    public static <T> boolean equal(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        Iterator<T> xsi = xs.iterator();
        Iterator<T> ysi = ys.iterator();
        while (xsi.hasNext()) {
            if (!ysi.hasNext()) return false;
            T x = xsi.next();
            T y = ysi.next();
            if (!Objects.equals(x, y)) return false;
        }
        return !ysi.hasNext();
    }

    public static <T> boolean equal(int limit, @NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        return equal(take(limit, xs), take(limit, ys));
    }

    public static @NotNull <T> Pair<List<T>, List<T>> minimize(@NotNull List<T> a, @NotNull List<T> b) {
        List<T> oldA = new ArrayList<>();
        List<T> oldB = new ArrayList<>();
        while (!a.equals(oldA) || !b.equals(oldB)) {
            int longestCommonSuffixLength = 0;
            for (int i = 0; i < min(a.size(), b.size()); i++) {
                if (!a.get(a.size() - i - 1).equals(b.get(b.size() - i - 1))) break;
                longestCommonSuffixLength++;
            }
            oldA = a;
            oldB = b;
            a = toList(take(a.size() - longestCommonSuffixLength, a));
            b = unrepeat(rotateRight(longestCommonSuffixLength, b));
        }
        return new Pair<>(a, b);
    }
}
