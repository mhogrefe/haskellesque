package mho.wheels.ordering.comparators;

import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;

import static mho.wheels.ordering.Ordering.*;

/**
 * Compares two {@code Iterable}s via shortlex order. First the lengths of the {@code Iterable}s are compared. If one
 * is shorter, it is smaller than the other in this ordering. If both are equal in length, the {@code Comparator} then
 * looks at their elements in parallel, left-to-right. The first pair of elements which aren't equal determine the
 * ordering.
 *
 * @param <T> the type of the {@code Iterable}s' values
 */
public class ShortlexComparator<T extends Comparable<T>> implements Comparator<Iterable<T>> {
    /**
     * The {@code Comparator} used to compare values of type {@code T}. It can be null, in which case the natural
     * ordering of {@code T} is used.
     */
    private final @Nullable Comparator<T> elementComparator;

    /**
     * Constructs a {@code ShortlexComparator} which uses the natural ordering to compare elements.
     */
    public ShortlexComparator() {
        this.elementComparator = null;
    }

    /**
     * Constructs a {@code ShortLexComparator} which uses a provided {@code Comparator} to compare elements.
     *
     * <ul>
     *  <li>{@code elementComparator} must obey its contract. See {@link java.util.Comparator}.</li>
     * </ul>
     *
     * @param elementComparator the {@code Comparator} used to compare values of type {@code T}
     */
    @SuppressWarnings("NullableProblems")
    public ShortlexComparator(@NotNull Comparator<T> elementComparator) {
        this.elementComparator = elementComparator;
    }

    /**
     * Compares two {@code Iterable}s via shortlex order. If the {@code Iterable}s are both infinite, this method will
     * hang, since it won't be able to compare their sizes. However, if only one is infinite the method will return a
     * correct result.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>{@code ys} cannot be null.</li>
     *  <li>{@code xs} and {@code ys} must contain valid arguments for either {@code T}'s {@code compareTo} method (if
     *  {@code elementComparator} is null) or {@code elementComparator}'s {@code compare} method.</li>
     *  <li>{@code xs} and {@code ys} cannot both be infinite.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param xs the first {@code Iterable}
     * @param ys the second {@code Iterable}
     * @return –1, 0, 1, depending on whether {@code xs} is less than, equal to, or greater than {@code ys},
     * respectively
     */
    @Override
    public int compare(@NotNull Iterable<T> xs, @NotNull Iterable<T> ys) {
        Iterator<T> xsi = xs.iterator();
        Iterator<T> ysi = ys.iterator();
        while (xsi.hasNext()) {
            if (!ysi.hasNext()) return GT.toInt();
            xsi.next();
            ysi.next();
        }
        if (ysi.hasNext()) return LT.toInt();
        xsi = xs.iterator();
        ysi = ys.iterator();
        while (xsi.hasNext()) {
            Ordering elementOrdering;
            if (elementComparator == null) {
                elementOrdering = Ordering.compare(xsi.next(), ysi.next());
            } else {
                elementOrdering = Ordering.compare(elementComparator, xsi.next(), ysi.next());
            }
            if (elementOrdering != EQ) return elementOrdering.toInt();
        }
        return EQ.toInt();
    }
}