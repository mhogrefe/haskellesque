package mho.wheels.iterables;

import mho.wheels.io.Readers;
import mho.wheels.numberUtils.BigDecimalUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public strictfp class IterableUtilsTest {
    private static <T> void simpleProviderHelper(@NotNull Iterable<T> xs, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, xs, output);
        testNoRemove(TINY_LIMIT, xs);
    }

    //todo clean
    @Test
    public void testAddTo_Iterable_Collection() {
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(Arrays.asList(4, 1, 5, 9, 2));
        ArrayList<Integer> iArrayList = new ArrayList<>();
        addTo(set, iArrayList);
        assertEquals(iArrayList.size(), 5);
        assertEquals(iArrayList.get(0).intValue(), 4);
        assertEquals(iArrayList.get(1).intValue(), 1);
        assertEquals(iArrayList.get(2).intValue(), 5);
        assertEquals(iArrayList.get(3).intValue(), 9);
        assertEquals(iArrayList.get(4).intValue(), 2);
        LinkedList<Integer> iLinkedList = new LinkedList<>();
        addTo(set, iLinkedList);
        assertEquals(iLinkedList.size(), 5);
        assertEquals(iLinkedList.get(0).intValue(), 4);
        assertEquals(iLinkedList.get(1).intValue(), 1);
        assertEquals(iLinkedList.get(2).intValue(), 5);
        assertEquals(iLinkedList.get(3).intValue(), 9);
        assertEquals(iLinkedList.get(4).intValue(), 2);
        HashSet<Integer> iHashSet = new HashSet<>();
        addTo(set, iHashSet);
        assertEquals(iHashSet.size(), 5);
        assertTrue(iHashSet.contains(4));
        assertTrue(iHashSet.contains(1));
        assertTrue(iHashSet.contains(5));
        assertTrue(iHashSet.contains(9));
        assertTrue(iHashSet.contains(2));
        TreeSet<Integer> iTreeSet = new TreeSet<>();
        addTo(set, iTreeSet);
        assertEquals(iTreeSet.size(), 5);
        assertTrue(iTreeSet.contains(4));
        assertTrue(iTreeSet.contains(1));
        assertTrue(iTreeSet.contains(5));
        assertTrue(iTreeSet.contains(9));
        assertTrue(iTreeSet.contains(2));
        LinkedHashSet<Integer> iLinkedHashSet = new LinkedHashSet<>();
        addTo(set, iLinkedHashSet);
        assertEquals(iLinkedHashSet.size(), 5);
        Iterator<Integer> iLinkedHashSetIterator = iLinkedHashSet.iterator();
        assertEquals(iLinkedHashSetIterator.next().intValue(), 4);
        assertEquals(iLinkedHashSetIterator.next().intValue(), 1);
        assertEquals(iLinkedHashSetIterator.next().intValue(), 5);
        assertEquals(iLinkedHashSetIterator.next().intValue(), 9);
        assertEquals(iLinkedHashSetIterator.next().intValue(), 2);

        set = new HashSet<>();
        iArrayList = new ArrayList<>();
        addTo(set, iArrayList);
        assertTrue(iArrayList.isEmpty());
        iLinkedList = new LinkedList<>();
        assertTrue(iLinkedList.isEmpty());
        iHashSet = new HashSet<>();
        assertTrue(iHashSet.isEmpty());
        iTreeSet = new TreeSet<>();
        assertTrue(iTreeSet.isEmpty());
        iLinkedHashSet = new LinkedHashSet<>();
        assertTrue(iLinkedHashSet.isEmpty());

        LinkedList<Float> lList = new LinkedList<>();
        lList.add(0.2f);
        lList.add(-5f);
        lList.add(null);
        lList.add(1e30f);
        ArrayList<Float> fArrayList = new ArrayList<>();
        addTo(lList, fArrayList);
        assertEquals(fArrayList.size(), 4);
        assertEquals(fArrayList.get(0), Float.valueOf(0.2f));
        assertEquals(fArrayList.get(1), Float.valueOf(-5f));
        assertNull(fArrayList.get(2));
        assertEquals(fArrayList.get(3), Float.valueOf(1e30f));
        LinkedList<Float> fLinkedList = new LinkedList<>();
        addTo(lList, fLinkedList);
        assertEquals(fLinkedList.size(), 4);
        assertEquals(fLinkedList.get(0), Float.valueOf(0.2f));
        assertEquals(fLinkedList.get(1), Float.valueOf(-5f));
        assertNull(fLinkedList.get(2));
        assertEquals(fLinkedList.get(3), Float.valueOf(1e30f));
        HashSet<Float> fHashSet = new HashSet<>();
        addTo(lList, fHashSet);
        assertEquals(fHashSet.size(), 4);
        assertTrue(fHashSet.contains(Float.valueOf(0.2f)));
        assertTrue(fHashSet.contains(Float.valueOf(-5f)));
        assertTrue(fHashSet.contains(null));
        assertTrue(fHashSet.contains(Float.valueOf(1e30f)));
        TreeSet<Float> fTreeSet = new TreeSet<>(Comparator.nullsFirst(Comparator.<Float>naturalOrder()));
        addTo(lList, fTreeSet);
        assertEquals(fTreeSet.size(), 4);
        assertTrue(fTreeSet.contains(Float.valueOf(0.2f)));
        assertTrue(fTreeSet.contains(Float.valueOf(-5f)));
        assertTrue(fTreeSet.contains(null));
        assertTrue(fTreeSet.contains(Float.valueOf(1e30f)));
        LinkedHashSet<Float> fLinkedHashSet = new LinkedHashSet<>();
        addTo(lList, fLinkedHashSet);
        assertEquals(fLinkedHashSet.size(), 4);
        Iterator<Float> fLinkedHashSetIterator = fLinkedHashSet.iterator();
        assertEquals(fLinkedHashSetIterator.next(), Float.valueOf(0.2f));
        assertEquals(fLinkedHashSetIterator.next(), Float.valueOf(-5f));
        assertNull(fLinkedHashSetIterator.next());
        assertEquals(fLinkedHashSetIterator.next(), Float.valueOf(1e30f));
    }

    //todo clean
    @Test
    public void testAddTo_String_Collection() {
        ArrayList<Character> arrayList = new ArrayList<>();
        addTo("hello", arrayList);
        assertEquals(arrayList.size(), 5);
        assertEquals(arrayList.get(0), Character.valueOf('h'));
        assertEquals(arrayList.get(1), Character.valueOf('e'));
        assertEquals(arrayList.get(2), Character.valueOf('l'));
        assertEquals(arrayList.get(3), Character.valueOf('l'));
        assertEquals(arrayList.get(4), Character.valueOf('o'));
        LinkedList<Character> linkedList = new LinkedList<>();
        addTo("hello", linkedList);
        assertEquals(linkedList.size(), 5);
        assertEquals(linkedList.get(0), Character.valueOf('h'));
        assertEquals(linkedList.get(1), Character.valueOf('e'));
        assertEquals(linkedList.get(2), Character.valueOf('l'));
        assertEquals(linkedList.get(3), Character.valueOf('l'));
        assertEquals(linkedList.get(4), Character.valueOf('o'));
        HashSet<Character> hashSet = new HashSet<>();
        addTo("hello", hashSet);
        assertEquals(hashSet.size(), 4);
        assertTrue(hashSet.contains(Character.valueOf('h')));
        assertTrue(hashSet.contains(Character.valueOf('e')));
        assertTrue(hashSet.contains(Character.valueOf('l')));
        assertTrue(hashSet.contains(Character.valueOf('o')));
        TreeSet<Character> treeSet = new TreeSet<>();
        addTo("hello", treeSet);
        assertEquals(treeSet.size(), 4);
        assertTrue(treeSet.contains(Character.valueOf('h')));
        assertTrue(treeSet.contains(Character.valueOf('e')));
        assertTrue(treeSet.contains(Character.valueOf('l')));
        assertTrue(treeSet.contains(Character.valueOf('o')));
        LinkedHashSet<Character> linkedHashSet = new LinkedHashSet<>();
        addTo("hello", linkedHashSet);
        assertEquals(treeSet.size(), 4);
        Iterator<Character> linkedHashSetIterator = linkedHashSet.iterator();
        assertEquals(linkedHashSetIterator.next(), Character.valueOf('h'));
        assertEquals(linkedHashSetIterator.next(), Character.valueOf('e'));
        assertEquals(linkedHashSetIterator.next(), Character.valueOf('l'));
        assertEquals(linkedHashSetIterator.next(), Character.valueOf('o'));

        arrayList = new ArrayList<>();
        addTo("", arrayList);
        assertTrue(arrayList.isEmpty());
        linkedList = new LinkedList<>();
        addTo("", linkedList);
        assertTrue(linkedList.isEmpty());
        hashSet = new HashSet<>();
        addTo("", hashSet);
        assertTrue(hashSet.isEmpty());
        treeSet = new TreeSet<>();
        addTo("", treeSet);
        assertTrue(treeSet.isEmpty());
        linkedHashSet = new LinkedHashSet<>();
        addTo("", linkedHashSet);
        assertTrue(linkedHashSet.isEmpty());
    }

    //todo clean
    @Test
    public void testToList_Iterable() {
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(Arrays.asList(4, 1, 5, 9, 2));
        List<Integer> iList = toList(set);
        assertEquals(iList.size(), 5);
        assertEquals(iList.get(0).intValue(), 4);
        assertEquals(iList.get(1).intValue(), 1);
        assertEquals(iList.get(2).intValue(), 5);
        assertEquals(iList.get(3).intValue(), 9);
        assertEquals(iList.get(4).intValue(), 2);

        set = new HashSet<>();
        iList = toList(set);
        assertTrue(iList.isEmpty());

        LinkedList<Float> lList = new LinkedList<>();
        lList.add(0.2f);
        lList.add(-5f);
        lList.add(null);
        lList.add(1e30f);
        List<Float> fList = toList(lList);
        assertEquals(fList.size(), 4);
        assertEquals(fList.get(0), Float.valueOf(0.2f));
        assertEquals(fList.get(1), Float.valueOf(-5f));
        assertNull(fList.get(2));
        assertEquals(fList.get(3), Float.valueOf(1e30f));
    }

    //todo clean
    @Test
    public void testToList_String() {
        List<Character> list = toList("hello");
        assertEquals(list.size(), 5);
        assertEquals(list.get(0), Character.valueOf('h'));
        assertEquals(list.get(1), Character.valueOf('e'));
        assertEquals(list.get(2), Character.valueOf('l'));
        assertEquals(list.get(3), Character.valueOf('l'));
        assertEquals(list.get(4), Character.valueOf('o'));

        list = toList("");
        assertTrue(list.isEmpty());
    }

    //todo clean
    @Test
    public void testToString_Iterable() {
        assertEquals(IterableUtils.toString(Arrays.asList(4, 1, 5, 9, 2)), "[4, 1, 5, 9, 2]");
        assertEquals(IterableUtils.toString(new HashSet<Integer>()), "[]");
        LinkedList<Float> lList = new LinkedList<>();
        lList.add(0.2f);
        lList.add(-5f);
        lList.add(null);
        lList.add(1e30f);
        assertEquals(IterableUtils.toString(lList), "[0.2, -5.0, null, 1.0E30]");
    }

    //todo clean
    @Test
    public void testToString_int_Iterable() {
        aeq(IterableUtils.toString(10, Arrays.asList(4, 1, 5, 9, 2)), "[4, 1, 5, 9, 2]");
        aeq(IterableUtils.toString(5, Arrays.asList(4, 1, 5, 9, 2)), "[4, 1, 5, 9, 2]");
        aeq(IterableUtils.toString(4, Arrays.asList(4, 1, 5, 9, 2)), "[4, 1, 5, 9, ...]");
        aeq(IterableUtils.toString(3, Arrays.asList(4, 1, 5, 9, 2)), "[4, 1, 5, ...]");
        aeq(IterableUtils.toString(1, Arrays.asList(4, 1, 5, 9, 2)), "[4, ...]");
        aeq(IterableUtils.toString(0, Arrays.asList(4, 1, 5, 9, 2)), "[...]");
        aeq(IterableUtils.toString(10, new HashSet<Integer>()), "[]");
        aeq(IterableUtils.toString(1, new HashSet<Integer>()), "[]");
        aeq(IterableUtils.toString(0, new HashSet<Integer>()), "[]");
        LinkedList<Float> lList = new LinkedList<>();
        lList.add(0.2f);
        lList.add(-5f);
        lList.add(null);
        lList.add(1e30f);
        aeq(IterableUtils.toString(10, lList), "[0.2, -5.0, null, 1.0E30]");
        aeq(IterableUtils.toString(4, lList), "[0.2, -5.0, null, 1.0E30]");
        aeq(IterableUtils.toString(2, lList), "[0.2, -5.0, ...]");
        aeq(IterableUtils.toString(1, lList), "[0.2, ...]");
        aeq(IterableUtils.toString(0, lList), "[...]");
        aeq(IterableUtils.toString(10, ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0)),
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, ...]");
        aeq(IterableUtils.toString(2, ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0)), "[0, 1, ...]");
        aeq(IterableUtils.toString(1, ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0)), "[0, ...]");
        aeq(IterableUtils.toString(0, ExhaustiveProvider.INSTANCE.rangeUpIncreasing(0)), "[...]");
        try {
            //noinspection ResultOfMethodCallIgnored
            IterableUtils.toString(-1, Arrays.asList(1, 2, 3));
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    private static void fromStringHelper(@NotNull String input, @NotNull String output) {
        aeqit(fromString(input), output);
    }

    @Test
    public void testFromString() {
        fromStringHelper("hello", "[h, e, l, l, o]");
        fromStringHelper("", "[]");
    }

    private static void charsToStringHelper(@NotNull String input, @NotNull String output) {
        aeq(charsToString(Readers.readListWithNullsStrict(Readers::readCharacterStrict).apply(input).get()), output);
    }

    @Test
    public void testCharsToString() {
        charsToStringHelper("[h, e, l, l, o]", "hello");
        charsToStringHelper("[]", "");
        try {
            charsToStringHelper("[h, null, l, l, o]", "");
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testCons_Iterable() {
        aeqit(cons(5, Arrays.asList(1, 2, 3, 4, 5)), "[5, 1, 2, 3, 4, 5]");
        aeqit(cons(null, Arrays.asList(1, 2, 3, 4, 5)), "[null, 1, 2, 3, 4, 5]");
        aeqit(cons(5, Arrays.asList(1, 2, null, 4, 5)), "[5, 1, 2, null, 4, 5]");
        aeqit(cons(5, new ArrayList<Integer>()), "[5]");
        simpleProviderHelper(cons(5, repeat(1)), "[5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");
    }

    @Test
    public void testCons_String() {
        aeq(cons('A', " SMALL CAT"), "A SMALL CAT");
        aeq(cons('A', ""), "A");
    }

    @Test
    public void testConcat_Iterable_Iterable() {
        aeqit(concat(Arrays.asList(1, 2, 3), Arrays.asList(90, 80, 70)), "[1, 2, 3, 90, 80, 70]");
        aeqit(concat(Arrays.asList(1, null, 3), Arrays.asList(90, 80, 70)), "[1, null, 3, 90, 80, 70]");
        aeqit(concat(Arrays.asList(1, 2, 3), Arrays.asList(90, null, 70)), "[1, 2, 3, 90, null, 70]");
        aeqit(concat(new ArrayList<Integer>(), Arrays.asList(90, 80, 70)), "[90, 80, 70]");
        aeqit(concat(Arrays.asList(1, 2, 3), new ArrayList<Integer>()), "[1, 2, 3]");
        aeqit(concat(new ArrayList<Integer>(), new ArrayList<Integer>()), "[]");
        aeqit(
                take(10, (Iterable<Integer>) concat(Arrays.asList(1, 2, 3), repeat(5))),
                "[1, 2, 3, 5, 5, 5, 5, 5, 5, 5]"
        );
        aeqit(
                take(10, (Iterable<Integer>) concat(repeat(5), Arrays.asList(1, 2, 3))),
                "[5, 5, 5, 5, 5, 5, 5, 5, 5, 5]"
        );
        aeqit(
                take(10, (Iterable<Integer>) concat(repeat(5), repeat(1))),
                "[5, 5, 5, 5, 5, 5, 5, 5, 5, 5]"
        );
    }

    @Test
    public void testConcat_String_String() {
        aeq(concat("Hello ", "World"), "Hello World");
        aeq(concat("Hello", ""), "Hello");
        aeq(concat("", "World"), "World");
        aeq(concat("", ""), "");
    }

    @Test
    public void testHead_Iterable() {
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Arrays.asList(5, 4, 3, 2, 1));
        aeq(head(linkedHashSet), 5);

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Collections.singletonList(5));
        aeq(head(linkedHashSet), 5);

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Arrays.asList(null, 4, 3, 2, 1));
        assertNull(head(linkedHashSet));

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(null);
        assertNull(head(linkedHashSet));

        aeq(head((Iterable<Integer>) repeat(5)), 5);

        linkedHashSet = new LinkedHashSet<>();
        try {
            assertNull(head(linkedHashSet));
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testHead_List() {
        aeq(head((List<Integer>) Arrays.asList(5, 4, 3, 2, 1)), 5);
        assertNull(head((List<Integer>) Arrays.asList(null, 4, 3, 2, 1)));
        aeq(head((List<Integer>) Collections.singletonList(5)), 5);
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertNull(head(nullList));
        try {
            head(new ArrayList<Integer>());
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testHead_SortedSet() {
        SortedSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(Arrays.asList(1, 2, 3, 4, 5));
        aeq(head(sortedSet), 1);

        sortedSet = new TreeSet<>(Comparator.nullsFirst(Comparator.<Integer>naturalOrder()));
        sortedSet.addAll(Arrays.asList(null, 2, 3, 4, 5));
        assertNull(head(sortedSet));

        sortedSet = new TreeSet<>();
        sortedSet.addAll(Collections.singletonList(1));
        aeq(head(sortedSet), 1);

        sortedSet = new TreeSet<>(Comparator.nullsFirst(Comparator.<Integer>naturalOrder()));
        sortedSet.add(null);
        assertNull(head(sortedSet));

        sortedSet = new TreeSet<>();
        try {
            head(sortedSet);
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testHead_String() {
        aeq(head("hello"), 'h');
        aeq(head("h"), 'h');
        try {
            head("");
        } catch (StringIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testLast_Iterable() {
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Arrays.asList(5, 4, 3, 2, 1));
        aeq(last(linkedHashSet), 1);

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Arrays.asList(5, 4, 3, 2, null));
        assertNull(last(linkedHashSet));

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(Collections.singletonList(1));
        aeq(last(linkedHashSet), 1);

        linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(null);
        assertNull(last(linkedHashSet));

        linkedHashSet = new LinkedHashSet<>();
        try {
            assertNull(last(linkedHashSet));
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testLast_List() {
        aeq(last((List<Integer>) Arrays.asList(5, 4, 3, 2, 1)), 1);
        assertNull(last((List<Integer>) Arrays.asList(5, 4, 3, 2, null)));
        aeq(last((List<Integer>) Collections.singletonList(1)), 1);
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertNull(last(nullList));
        try {
            last(new ArrayList<Integer>());
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testLast_SortedSet() {
        SortedSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(Arrays.asList(1, 2, 3, 4, 5));
        aeq(last(sortedSet), 5);

        sortedSet = new TreeSet<>(Comparator.nullsFirst(Comparator.<Integer>naturalOrder()));
        sortedSet.add(null);
        assertNull(last(sortedSet));

        sortedSet = new TreeSet<>();
        sortedSet.addAll(Collections.singletonList(5));
        aeq(last(sortedSet), 5);

        sortedSet = new TreeSet<>();
        try {
            last(sortedSet);
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testLast_String() {
        aeq(last("o"), 'o');
        try {
            last("");
        } catch (StringIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testTail_Iterable() {
        aeqit(tail(Arrays.asList(5, 4, 3, 2, 1)), "[4, 3, 2, 1]");
        aeqit(tail(Arrays.asList(5, 4, null, 2, 1)), "[4, null, 2, 1]");
        aeqit(tail(Collections.singletonList(5)), "[]");
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        aeqit(tail(nullList), "[]");
        try {
            tail(new ArrayList<Integer>());
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testTail_String() {
        aeq(tail("hello"), "ello");
        aeq(tail("h"), "");
        try {
            toList(tail(""));
            fail();
        } catch (StringIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testInit_Iterable() {
        aeqit(init(Arrays.asList(5, 4, 3, 2, 1)), "[5, 4, 3, 2]");
        aeqit(init(Arrays.asList(5, 4, null, 2, 1)), "[5, 4, null, 2]");
        aeqit(init(Collections.singletonList(5)), "[]");
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        aeqit(init(nullList), "[]");
        simpleProviderHelper(init(repeat(5)), "[5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, ...]");
        try {
            init(new ArrayList<>());
            fail();
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    public void testInit_String() {
        aeq(init("hello"), "hell");
        aeq(init("h"), "");
        try {
            toList(init(""));
            fail();
        } catch (StringIndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testIsEmpty_Iterable() {
        assertFalse(isEmpty((Iterable<Integer>) cons(6, Arrays.asList(5, 4, 3, 2, 1))));
        assertFalse(isEmpty((Iterable<Integer>) cons(null, Arrays.asList(null, 2, 1))));
        assertFalse(isEmpty(repeat(5)));
        assertTrue(isEmpty((Iterable<Integer>) tail(Collections.singletonList(2))));
    }

    @Test
    public void testIsEmpty_Collection() {
        assertFalse(isEmpty(Arrays.asList(5, 4, 3, 2, 1)));
        assertFalse(isEmpty(Arrays.asList(5, 4, null, 2, 1)));
        assertFalse(isEmpty(Collections.singletonList(5)));
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertFalse(isEmpty(nullList));
        assertTrue(isEmpty(new ArrayList<Integer>()));
    }

    @Test
    public void testIsEmpty_String() {
        assertFalse(isEmpty("hello"));
        assertFalse(isEmpty("h"));
        assertTrue(isEmpty(""));
    }

    @Test
    public void testLength_Iterable() {
        assertEquals(length(cons(6, Arrays.asList(5, 4, 3, 2, 1))), 6);
        assertEquals(length(cons(null, Arrays.asList(null, 2, 1))), 4);
        assertEquals(length(tail(Collections.singletonList(2))), 0);
    }

    @Test
    public void testBigIntegerLength() {
        aeq(bigIntegerLength(cons(6, Arrays.asList(5, 4, 3, 2, 1))), 6);
        aeq(bigIntegerLength(cons(null, Arrays.asList(null, 2, 1))), 4);
        aeq(bigIntegerLength(tail(Collections.singletonList(2))), 0);
    }

    @Test
    public void testLength_Collection() {
        assertEquals(length(Arrays.asList(5, 4, 3, 2, 1)), 5);
        assertEquals(length(Arrays.asList(5, 4, null, 2, 1)), 5);
        assertEquals(length(Collections.singletonList(5)), 1);
        List<Integer> nullList = new ArrayList<>();
        nullList.add(null);
        assertEquals(length(nullList), 1);
        assertEquals(length(new ArrayList<>()), 0);
    }

    @Test
    public void testLength_String() {
        assertFalse(isEmpty("hello"));
        assertFalse(isEmpty("h"));
        assertTrue(isEmpty(""));
    }

    @Test
    public void testMap_Iterable() {
        aeqit(map(i -> i + 3, Arrays.asList(1, 5, 3, 1, 6)), "[4, 8, 6, 4, 9]");
        aeqit(map(i -> i == null ? -1 : i + 3, Arrays.asList(1, 5, null, 1, 6)), "[4, 8, -1, 4, 9]");
        simpleProviderHelper(map(i -> i + 3, repeat(5)),
                "[8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, ...]");
        aeqit(map(s -> s + "!", Arrays.asList("BIFF", "BANG", "POW")), "[BIFF!, BANG!, POW!]");
        aeqit(map(s -> s + "!", new ArrayList<String>()), "[]");
        try {
            IterableUtils.toList((Iterable<Integer>) map(i -> i + 3, Arrays.asList(1, 5, null, 1, 6)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testMap_String() {
        aeq(map(Character::toUpperCase, "hello"), "HELLO");
        aeq(map(Character::toUpperCase, ""), "");
        Function<Character, Character> f = c -> {
            if (c == 'l')
                throw new IllegalArgumentException("L exception");
            return c;
        };
        try {
            map(f, "hello");
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReverse_Iterable() {
        aeq(reverse(Arrays.asList(5, 4, 3, 2, 1)), "[1, 2, 3, 4, 5]");
        aeq(reverse(new ArrayList<Integer>()), "[]");
    }

    @Test
    public void testReverse_String() {
        aeq(reverse("hello"), "olleh");
        aeq(reverse("thanks"), "sknaht");
        aeq(reverse(""), "");
    }

    @Test
    public void testIntersperse_Iterable() {
        aeqit(intersperse(0, Arrays.asList(1, 2, 3, 4, 5)), "[1, 0, 2, 0, 3, 0, 4, 0, 5]");
        aeqit(intersperse(0, new ArrayList<Integer>()), "[]");
    }

    @Test
    public void testIntersperse_String() {
        aeq(intersperse(',', "abcde"), "a,b,c,d,e");
        aeq(intersperse('*', "MASH"), "M*A*S*H");
        aeq(intersperse('*', ""), "");
    }

    @Test
    public void testIntercalate_Iterable() {
        aeqit(intercalate(Arrays.asList(0, 0, 0), Arrays.asList(
                (Iterable<Integer>) Arrays.asList(1, 2, 3),
                (Iterable<Integer>) Arrays.asList(4, 5, 6),
                (Iterable<Integer>) Arrays.asList(7, 8, 9))), "[1, 2, 3, 0, 0, 0, 4, 5, 6, 0, 0, 0, 7, 8, 9]");
        aeqit((Iterable<Integer>) intercalate(new ArrayList<Integer>(), Arrays.asList(
                (Iterable<Integer>) Arrays.asList(1, 2, 3),
                (Iterable<Integer>) Arrays.asList(4, 5, 6),
                (Iterable<Integer>) Arrays.asList(7, 8, 9))), "[1, 2, 3, 4, 5, 6, 7, 8, 9]");
        simpleProviderHelper(intercalate(repeat(5), Arrays.asList(
                (Iterable<Integer>) Arrays.asList(1, 2, 3),
                (Iterable<Integer>) Arrays.asList(4, 5, 6),
                (Iterable<Integer>) Arrays.asList(7, 8, 9))),
                        "[1, 2, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, ...]");
        simpleProviderHelper(intercalate(Arrays.asList(0, 0, 0), Arrays.asList(
                (Iterable<Integer>) repeat(1),
                (Iterable<Integer>) repeat(2),
                (Iterable<Integer>) repeat(3))), "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");
        simpleProviderHelper(intercalate(new ArrayList<Integer>(), Arrays.asList(
                (Iterable<Integer>) repeat(1),
                (Iterable<Integer>) repeat(2),
                (Iterable<Integer>) repeat(3))), "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]");
        aeqit(intercalate(Arrays.asList(0, 0, 0), new ArrayList<Iterable<Integer>>()), "[]");
        aeqit((Iterable<Integer>) intercalate(new ArrayList<>(), new ArrayList<Iterable<Integer>>()), "[]");
        aeqit(intercalate(repeat(5), new ArrayList<Iterable<Integer>>()), "[]");
        aeqit(
                take(10, (Iterable<Integer>) intercalate(
                        Arrays.asList(0, 0, 0),
                        repeat((Iterable<Integer>) Arrays.asList(1, 2, 3))
                )),
                "[1, 2, 3, 0, 0, 0, 1, 2, 3, 0]"
        );
        aeqit(
                (Iterable<Integer>) take(10, (Iterable<Integer>) intercalate(
                        new ArrayList<>(),
                        repeat((Iterable<Integer>) Arrays.asList(1, 2, 3))
                )),
                "[1, 2, 3, 1, 2, 3, 1, 2, 3, 1]"
        );
        aeqit(
                take(10, (Iterable<Integer>) intercalate(
                        repeat(5),
                        repeat((Iterable<Integer>) Arrays.asList(1, 2, 3))
                )),
                "[1, 2, 3, 5, 5, 5, 5, 5, 5, 5]"
        );
    }

    @Test
    public void testIntercalate_String() {
        aeq(intercalate(", ", (Iterable<String>) Arrays.asList("bread", "milk", "eggs")), "bread, milk, eggs");
        aeq(intercalate("", (Iterable<String>) Arrays.asList("bread", "milk", "eggs")), "breadmilkeggs");
        aeq(intercalate(", ", new ArrayList<String>()), "");
        aeq(intercalate("", new ArrayList<String>()), "");
    }

    @Test
    public void testTranspose() {
        aeqit(transpose(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9]]");
        aeqit(transpose(Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9], [-1, -2], [-1]]");
        aeqit(transpose(Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                new ArrayList<Integer>(),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 7], [2, 8], [3, 9], [-1, -2], [-1]]");
        aeqit(transpose(Arrays.asList(
                new ArrayList<Integer>(),
                new ArrayList<Integer>(),
                new ArrayList<>()
        )), "[]");
        aeqit(transpose(new ArrayList<Iterable<Integer>>()), "[]");
        simpleProviderHelper(transpose(Arrays.asList(
                (Iterable<Integer>) Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                repeat(7)
        )),
                "[[1, 4, 7], [2, 5, 7], [3, 6, 7], [7], [7], [7], [7], [7], [7], [7], [7], [7], [7], [7], [7], [7]," +
                " [7], [7], [7], [7], ...]");
    }

    @Test
    public void testTransposeStrings() {
        aeqit(transposeStrings(Arrays.asList("cat", "dog", "pen")), "[cdp, aoe, tgn]");
        aeqit(transposeStrings(Arrays.asList("cater", "doghouse", "pen")), "[cdp, aoe, tgn, eh, ro, u, s, e]");
        aeqit(transposeStrings(Arrays.asList("cater", "", "pen")), "[cp, ae, tn, e, r]");
        aeqit(transposeStrings(Arrays.asList("", "", "")), "[]");
        aeqit(transposeStrings(new ArrayList<String>()), "[]");
    }

    @Test
    public void testTransposeTruncating() {
        aeqit(transposeTruncating(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9]]");
        aeqit(transposeTruncating(Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9]]");
        aeqit(transposeTruncating(Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                new ArrayList<Integer>(),
                Arrays.asList(7, 8, 9, -2)
        )), "[]");
        aeqit(transposeTruncating(Arrays.asList(
                new ArrayList<Integer>(),
                new ArrayList<Integer>(),
                new ArrayList<>()
        )), "[]");
        aeqit(transposeTruncating(new ArrayList<Iterable<Integer>>()), "[]");
        aeqit(transposeTruncating(Arrays.asList(
                (Iterable<Integer>) Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                repeat(7)
        )), "[[1, 4, 7], [2, 5, 7], [3, 6, 7]]");
    }

    @Test
    public void testTransposeStringsTruncating() {
        aeqit(transposeStringsTruncating(Arrays.asList("cat", "dog", "pen")), "[cdp, aoe, tgn]");
        aeqit(transposeStringsTruncating(Arrays.asList("cater", "doghouse", "pen")), "[cdp, aoe, tgn]");
        aeqit(transposeStringsTruncating(Arrays.asList("cater", "", "pen")), "[]");
        aeqit(transposeStringsTruncating(Arrays.asList("", "", "")), "[]");
        aeqit(transposeStringsTruncating(new ArrayList<String>()), "[]");
    }

    @Test
    public void testTransposePadded() {
        aeqit(transposePadded(0, Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9]]");
        aeqit(transposePadded(0, Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9], [-1, 0, -2], [-1, 0, 0]]");
        aeqit(transposePadded(null, Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 4, 7], [2, 5, 8], [3, 6, 9], [-1, null, -2], [-1, null, null]]");
        aeqit(transposePadded(0, Arrays.asList(
                Arrays.asList(1, 2, 3, -1, -1),
                new ArrayList<Integer>(),
                Arrays.asList(7, 8, 9, -2)
        )), "[[1, 0, 7], [2, 0, 8], [3, 0, 9], [-1, 0, -2], [-1, 0, 0]]");
        aeqit(transposePadded(0, Arrays.asList(
                new ArrayList<Integer>(),
                new ArrayList<Integer>(),
                new ArrayList<Integer>()
        )), "[]");
        aeqit(transposePadded(0, new ArrayList<Iterable<Integer>>()), "[]");
        aeqit(
                take(
                        10,
                        transposePadded(
                                0,
                                Arrays.asList(
                                        (Iterable<Integer>) Arrays.asList(1, 2, 3),
                                        (Iterable<Integer>) Arrays.asList(4, 5, 6),
                                        repeat(7)
                        )
                )
                ),
                "[[1, 4, 7], [2, 5, 7], [3, 6, 7], [0, 0, 7], [0, 0, 7], [0, 0, 7], [0, 0, 7], [0, 0, 7], [0, 0, 7]," +
                " [0, 0, 7]]"
        );
    }

    @Test
    public void testTransposeStringsPadded() {
        aeqit(transposeStringsPadded('_', Arrays.asList("cat", "dog", "pen")), "[cdp, aoe, tgn]");
        aeqit(
                transposeStringsPadded('_', Arrays.asList("cater", "doghouse", "pen")),
                "[cdp, aoe, tgn, eh_, ro_, _u_, _s_, _e_]"
        );
        aeqit(transposeStringsPadded('_', Arrays.asList("cater", "", "pen")), "[c_p, a_e, t_n, e__, r__]");
        aeqit(transposeStringsPadded('_', Arrays.asList("", "", "")), "[]");
        aeqit(transposeStringsPadded('_', new ArrayList<String>()), "[]");
    }

    @Test
    public void testUnrepeat() {
        aeq(unrepeat(readIntegerList("[1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3]")), "[1, 2, 3]");
        aeq(
                unrepeat(readBigIntegerListWithNulls("[1, null, 3, 1, null, 3, 1, null, 3, 1, null, 3]")),
                "[1, null, 3]"
        );
        aeq(unrepeat(readIntegerList("[1, 2, 3, 4, 5]")), "[1, 2, 3, 4, 5]");
        aeq(unrepeat(readIntegerListWithNulls("[1, 2, 3, null, 5]")), "[1, 2, 3, null, 5]");
        aeq(unrepeat(readIntegerList("[1, 1, 1]")), "[1]");
        aeq(unrepeat(readIntegerListWithNulls("[null, null, null]")), "[null]");
        aeq(unrepeat(readIntegerList("[1]")), "[1]");
        aeq(unrepeat(readIntegerListWithNulls("[null]")), "[null]");
        aeq(unrepeat(new ArrayList<>()), "[]");
    }

    @Test
    public void testSumByte() {
        aeq(sumByte(Arrays.asList((byte) 10, (byte) 11, (byte) 12)), 33);
        aeq(sumByte(Arrays.asList((byte) -4, (byte) 6, (byte) -8)), -6);
        aeq(sumByte(Arrays.asList(Byte.MAX_VALUE, (byte) 1)), Byte.MIN_VALUE);
        aeq(sumByte(new ArrayList<>()), 0);
        try {
            sumByte(Arrays.asList((byte) 10, null, (byte) 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumShort() {
        aeq(sumShort(Arrays.asList((short) 10, (short) 11, (short) 12)), 33);
        aeq(sumShort(Arrays.asList((short) -4, (short) 6, (short) -8)), -6);
        aeq(sumShort(Arrays.asList(Short.MAX_VALUE, (short) 1)), Short.MIN_VALUE);
        aeq(sumShort(new ArrayList<>()), 0);
        try {
            sumShort(Arrays.asList((short) 10, null, (short) 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumInteger() {
        aeq(sumInteger(Arrays.asList(10, 11, 12)), 33);
        aeq(sumInteger(Arrays.asList(-4, 6, -8)), -6);
        aeq(sumInteger(Arrays.asList(Integer.MAX_VALUE, 1)), Integer.MIN_VALUE);
        aeq(sumInteger(new ArrayList<>()), 0);
        try {
            sumInteger(Arrays.asList(10, null, 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumLong() {
        aeq(sumLong(Arrays.asList(10L, 11L, 12L)), 33);
        aeq(sumLong(Arrays.asList(-4L, 6L, -8L)), -6);
        aeq(sumLong(Arrays.asList(Long.MAX_VALUE, 1L)), Long.MIN_VALUE);
        aeq(sumLong(new ArrayList<>()), 0);
        try {
            sumLong(Arrays.asList(10L, null, 12L));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumFloat() {
        aeq(sumFloat(Arrays.asList(10.0f, 10.5f, 11.0f)), 31.5f);
        aeq(sumFloat(Arrays.asList(-4.0f, 6.0f, -8.0f)), -6.0f);
        aeq(sumFloat(Arrays.asList(1.0e9f, 1.0f)), 1.0e9f);
        aeq(sumFloat(Arrays.asList(32.0f, 32.0f, 1.0e9f)), 1.00000006e9f);
        aeq(sumFloat(Arrays.asList(1.0e9f, 32.0f, 32.0f)), 1.0e9f);
        aeq(sumFloat(Arrays.asList(1.0e9f, Float.NaN, 32.0f)), Float.NaN);
        aeq(sumFloat(new ArrayList<>()), 0.0f);
        try {
            sumFloat(Arrays.asList(10.0f, null, 11.0f));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumDouble() {
        aeq(sumDouble(Arrays.asList(10.0, 10.5, 11.0)), 31.5);
        aeq(sumDouble(Arrays.asList(-4.0, 6.0, -8.0)), -6.0);
        aeq(sumDouble(Arrays.asList(1.0e16, 1.0)), 1.0e16);
        aeq(sumDouble(Arrays.asList(1.0, 1.0, 1.0e16)), 1.0000000000000002e16);
        aeq(sumDouble(Arrays.asList(1.0e16, 1.0, 1.0)), 1.0e16);
        aeq(sumDouble(Arrays.asList(1.0e16, Double.NaN, 1.0)), Double.NaN);
        aeq(sumDouble(new ArrayList<>()), 0.0);
        try {
            sumDouble(Arrays.asList(10.0, null, 11.0));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumBigInteger() {
        aeq(sumBigInteger(readBigIntegerList("[10, 11, 12]")), 33);
        aeq(sumBigInteger(readBigIntegerList("[-4, 6, -8]")), -6);
        aeq(sumBigInteger(new ArrayList<>()), 0);
        try {
            sumBigInteger(readBigIntegerListWithNulls("[10, null, 12]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumBigDecimal() {
        aeq(sumBigDecimal(readBigDecimalList("[10, 10.5, 11]")), 31.5);
        aeq(sumBigDecimal(readBigDecimalList("[-4, 6, -8]")), -6);
        aeq(sumBigDecimal(new ArrayList<>()), 0);
        try {
            sumBigDecimal(readBigDecimalListWithNulls("[10, null, 11]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductByte() {
        aeq(productByte(Arrays.asList((byte) 2, (byte) 5, (byte) 12)), 120);
        aeq(productByte(Arrays.asList((byte) -3, (byte) 5, (byte) -7)), 105);
        aeq(productByte(Arrays.asList((byte) (1 << 6), (byte) 2)), Byte.MIN_VALUE);
        aeq(productByte(new ArrayList<>()), 1);
        try {
            productByte(Arrays.asList((byte) 10, null, (byte) 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductShort() {
        aeq(productShort(Arrays.asList((short) 2, (short) 5, (short) 12)), 120);
        aeq(productShort(Arrays.asList((short) -3, (short) 5, (short) -7)), 105);
        aeq(productShort(Arrays.asList((short) (1 << 14), (short) 2)), Short.MIN_VALUE);
        aeq(productShort(new ArrayList<>()), 1);
        try {
            productShort(Arrays.asList((short) 10, null, (short) 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductInteger() {
        aeq(productInteger(Arrays.asList(2, 5, 12)), 120);
        aeq(productInteger(Arrays.asList(-3, 5, -7)), 105);
        aeq(productInteger(Arrays.asList(1 << 30, 2)), Integer.MIN_VALUE);
        aeq(productInteger(new ArrayList<>()), 1);
        try {
            productInteger(Arrays.asList(10, null, 12));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductLong() {
        aeq(productLong(Arrays.asList(2L, 5L, 12L)), 120);
        aeq(productLong(Arrays.asList(-3L, 5L, -7L)), 105);
        aeq(productLong(Arrays.asList(1L << 62, 2L)), Long.MIN_VALUE);
        aeq(productLong(new ArrayList<>()), 1);
        try {
            productLong(Arrays.asList(10L, null, 12L));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductFloat() {
        aeq(productFloat(Arrays.asList(10.0f, 10.5f, 11.0f)), 1155.0f);
        aeq(productFloat(Arrays.asList(-4.0f, 6.0f, -8.0f)), 192.0f);
        aeq(productFloat(Arrays.asList(Float.MAX_VALUE, 2.0f)), Float.POSITIVE_INFINITY);
        aeq(productFloat(Arrays.asList(2.0f, 0.5f, Float.MAX_VALUE)), 3.4028235E38f);
        aeq(productFloat(Arrays.asList(Float.MAX_VALUE, 2.0f, 0.5f)), Float.POSITIVE_INFINITY);
        aeq(productFloat(Arrays.asList(Float.MAX_VALUE, Float.NaN, 0.5f)), Float.NaN);
        aeq(productFloat(new ArrayList<>()), 1.0f);
        try {
            productFloat(Arrays.asList(10.0f, null, 11.0f));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductDouble() {
        aeq(productDouble(Arrays.asList(10.0, 10.5, 11.0)), 1155.0);
        aeq(productDouble(Arrays.asList(-4.0, 6.0, -8.0)), 192.0);
        aeq(productDouble(Arrays.asList(Double.MAX_VALUE, 2.0)), Double.POSITIVE_INFINITY);
        aeq(productDouble(Arrays.asList(0.2, 0.5, Double.MAX_VALUE)), 1.7976931348623158E307);
        aeq(productDouble(Arrays.asList(Double.MAX_VALUE, 2.0, 0.5)), Double.POSITIVE_INFINITY);
        aeq(productDouble(Arrays.asList(Double.MAX_VALUE, Double.NaN, 0.5)), Double.NaN);
        aeq(productDouble(new ArrayList<>()), 1.0);
        try {
            productDouble(Arrays.asList(10.0, null, 11.0));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductBigInteger() {
        aeq(productBigInteger(readBigIntegerList("[10, 11, 12]")), 1320);
        aeq(productBigInteger(readBigIntegerList("[-4, 6, -8]")), 192);
        aeq(productBigInteger(new ArrayList<>()), 1);
        try {
            productBigInteger(readBigIntegerListWithNulls("[10, null, 12]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProductBigDecimal() {
        aeq(productBigDecimal(readBigDecimalList("[10, 10.5, 11]")), 1155.0);
        aeq(productBigDecimal(readBigDecimalList("[-4, 6, -8]")), 192);
        aeq(productBigDecimal(new ArrayList<>()), 1);
        try {
            productBigDecimal(readBigDecimalListWithNulls("[10, null, 11]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    private static void bigIntegerSumSign_helper(@NotNull String input, int output) {
        aeq(sumSignBigInteger(readBigIntegerList(input)), output);
    }

    private static void bigIntegerSumSign_fail_helper(@NotNull String input) {
        try {
            sumSignBigInteger(readBigIntegerListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumSignBigInteger() {
        bigIntegerSumSign_helper("[]", 0);
        bigIntegerSumSign_helper("[0]", 0);
        bigIntegerSumSign_helper("[5]", 1);
        bigIntegerSumSign_helper("[-5]", -1);
        bigIntegerSumSign_helper("[23, -2]", 1);
        bigIntegerSumSign_helper("[23, -25]", -1);
        bigIntegerSumSign_helper("[23, -23]", 0);
        bigIntegerSumSign_helper("[1, 2, 4, -7]", 0);
        bigIntegerSumSign_helper("[1, 2, 4, -8]", -1);
        bigIntegerSumSign_helper("[1, 2, 4, -6]", 1);
        bigIntegerSumSign_fail_helper("[null]");
        bigIntegerSumSign_fail_helper("[1, 2, null]");
    }

    private static void bigDecimalSumSign_helper(@NotNull String input, int output) {
        aeq(sumSignBigDecimal(readBigDecimalList(input)), output);
    }

    private static void bigDecimalSumSign_fail_helper(@NotNull String input) {
        try {
            sumSignBigDecimal(readBigDecimalListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSumSignBigDecimal() {
        bigDecimalSumSign_helper("[]", 0);
        bigDecimalSumSign_helper("[0]", 0);
        bigDecimalSumSign_helper("[5.32]", 1);
        bigDecimalSumSign_helper("[-5E+10]", -1);
        bigDecimalSumSign_helper("[23.08, -2]", 1);
        bigDecimalSumSign_helper("[23.08, -25]", -1);
        bigDecimalSumSign_helper("[23.08, -23.08]", 0);
        bigDecimalSumSign_helper("[1, 2, 4.00, -7]", 0);
        bigDecimalSumSign_helper("[1, 2, 4.00, -8]", -1);
        bigDecimalSumSign_helper("[1, 2, 4.00, -6]", 1);
        bigDecimalSumSign_fail_helper("[null]");
        bigDecimalSumSign_fail_helper("[1, 2E-10, null]");
    }

    @Test
    public void testDeltaByte() {
        aeqit(deltaByte(Arrays.asList((byte) 3, (byte) 1, (byte) 4, (byte) 1, (byte) 5, (byte) 9, (byte) 3)),
                "[-2, 3, -3, 4, 4, -6]");
        aeqit(deltaByte(Arrays.asList(Byte.MIN_VALUE, Byte.MAX_VALUE)), "[-1]");
        aeqit(deltaByte(Collections.singletonList((byte) 3)), "[]");
        simpleProviderHelper(deltaByte(map(i -> (byte) (i * i), ExhaustiveProvider.INSTANCE.naturalIntegers())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaByte(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaByte(Arrays.asList((byte) 10, null, (byte) 12)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaShort() {
        aeqit(deltaShort(Arrays.asList((short) 3, (short) 1, (short) 4, (short) 1, (short) 5, (short) 9, (short) 3)),
                "[-2, 3, -3, 4, 4, -6]");
        aeqit(deltaShort(Arrays.asList(Short.MIN_VALUE, Short.MAX_VALUE)), "[-1]");
        aeqit(deltaShort(Collections.singletonList((short) 3)), "[]");
        simpleProviderHelper(deltaShort(map(i -> (short) (i * i), ExhaustiveProvider.INSTANCE.naturalIntegers())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaShort(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaShort(Arrays.asList((short) 10, null, (short) 12)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaInteger() {
        aeqit(deltaInteger(Arrays.asList(3, 1, 4, 1, 5, 9, 3)), "[-2, 3, -3, 4, 4, -6]");
        aeqit(deltaInteger(Arrays.asList(Integer.MIN_VALUE, Integer.MAX_VALUE)), "[-1]");
        aeqit(deltaInteger(Collections.singletonList(3)), "[]");
        simpleProviderHelper(deltaInteger(map(i -> i * i, ExhaustiveProvider.INSTANCE.naturalIntegers())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaInteger(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaInteger(Arrays.asList(10, null, 12)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaLong() {
        aeqit(deltaLong(Arrays.asList(3L, 1L, 4L, 1L, 5L, 9L, 3L)), "[-2, 3, -3, 4, 4, -6]");
        aeqit(deltaLong(Arrays.asList(Long.MIN_VALUE, Long.MAX_VALUE)), "[-1]");
        aeqit(deltaLong(Collections.singletonList(3L)), "[]");
        simpleProviderHelper(deltaLong(map(l -> l * l, ExhaustiveProvider.INSTANCE.naturalLongs())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaLong(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaLong(Arrays.asList(10L, null, 12L)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaBigInteger() {
        aeqit(deltaBigInteger(readBigIntegerList("[3, 1, 4, 1, 5, 9, 3]")), "[-2, 3, -3, 4, 4, -6]");
        aeqit(deltaBigInteger(Collections.singletonList(BigInteger.valueOf(3))), "[]");
        simpleProviderHelper(deltaBigInteger(map(i -> i.pow(2), ExhaustiveProvider.INSTANCE.naturalBigIntegers())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaBigInteger(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaBigInteger(readBigIntegerListWithNulls("[10, null, 12]")));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaBigDecimal() {
        aeqit(deltaBigDecimal(readBigDecimalList("[3.1, 4.1, 5.9, 2.3]")), "[1.0, 1.8, -3.6]");
        aeqit(deltaBigDecimal(Collections.singletonList(BigDecimal.valueOf(3))), "[]");
        simpleProviderHelper(deltaBigDecimal(iterate(bd -> BigDecimalUtils.shiftRight(bd, 1), BigDecimal.ONE)),
                "[-0.5, -0.25, -0.125, -0.0625, -0.03125, -0.015625, -0.0078125, -0.00390625, -0.001953125," +
                " -0.0009765625, -0.00048828125, -0.000244140625, -0.0001220703125, -0.00006103515625," +
                " -0.000030517578125, -0.0000152587890625, -0.00000762939453125, -0.000003814697265625," +
                " -0.0000019073486328125, -9.5367431640625E-7, ...]");
        try {
            deltaBigDecimal(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaBigDecimal(readBigDecimalListWithNulls("[10, null, 12]")));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaFloat() {
        aeqit(deltaFloat(Arrays.asList(3.1f, 4.1f, 5.9f, 2.3f)), "[1.0, 1.8000002, -3.6000001]");
        aeqit(deltaFloat(Arrays.asList(-Float.MAX_VALUE, Float.MAX_VALUE)), "[Infinity]");
        aeqit(deltaFloat(Arrays.asList(3.0f, Float.NaN)), "[NaN]");
        aeqit(deltaFloat(Arrays.asList(0.0f, -0.0f)), "[0.0]");
        aeqit(deltaFloat(Collections.singletonList(3.0f)), "[]");
        simpleProviderHelper(deltaFloat(iterate(f -> f / 2.0f, 1.0f)),
                "[-0.5, -0.25, -0.125, -0.0625, -0.03125, -0.015625, -0.0078125, -0.00390625, -0.001953125," +
                " -9.765625E-4, -4.8828125E-4, -2.4414062E-4, -1.2207031E-4, -6.1035156E-5, -3.0517578E-5," +
                " -1.5258789E-5, -7.6293945E-6, -3.8146973E-6, -1.9073486E-6, -9.536743E-7, ...]");
        try {
            deltaFloat(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaFloat(Arrays.asList(10.0f, null, 12.0f)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaDouble() {
        aeqit(deltaDouble(Arrays.asList(3.1, 4.1, 5.9, 2.3)),
                "[0.9999999999999996, 1.8000000000000007, -3.6000000000000005]");
        aeqit(deltaDouble(Arrays.asList(-Double.MAX_VALUE, Double.MAX_VALUE)), "[Infinity]");
        aeqit(deltaDouble(Arrays.asList(3.0, Double.NaN)), "[NaN]");
        aeqit(deltaDouble(Arrays.asList(0.0, -0.0)), "[0.0]");
        aeqit(deltaDouble(Collections.singletonList(3.0)), "[]");
        simpleProviderHelper(deltaDouble(iterate(d -> d / 2.0, 1.0)),
                "[-0.5, -0.25, -0.125, -0.0625, -0.03125, -0.015625, -0.0078125, -0.00390625, -0.001953125," +
                " -9.765625E-4, -4.8828125E-4, -2.44140625E-4, -1.220703125E-4, -6.103515625E-5, -3.0517578125E-5," +
                " -1.52587890625E-5, -7.62939453125E-6, -3.814697265625E-6, -1.9073486328125E-6," +
                " -9.5367431640625E-7, ...]");
        try {
            deltaDouble(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaDouble(Arrays.asList(10.0, null, 12.0)));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testDeltaCharacter() {
        aeqit(deltaCharacter(fromString("hello")), "[-3, 7, 0, 3]");
        aeqit(deltaCharacter(Collections.singletonList('a')), "[]");
        simpleProviderHelper(deltaCharacter(map(i -> (char) (i * i), ExhaustiveProvider.INSTANCE.naturalIntegers())),
                "[1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, ...]");
        try {
            deltaCharacter(new ArrayList<>());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(deltaCharacter(Arrays.asList('a', null, 'z')));
            fail();
        } catch (NullPointerException ignored) {}
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

    private static @NotNull List<BigDecimal> readBigDecimalList(@NotNull String s) {
        return Readers.readListStrict(Readers::readBigDecimalStrict).apply(s).get();
    }

    private static @NotNull List<BigDecimal> readBigDecimalListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readBigDecimalStrict).apply(s).get();
    }
}
