package mho.wheels.structures;

import mho.wheels.testing.Testing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Either<A, B> {
    public enum Slot {
        A, B
    }

    private final @NotNull Slot slot;
    private final @Nullable A a;
    private final @Nullable B b;

    private Either(@NotNull Slot slot, @Nullable A a, @Nullable B b) {
        this.slot = slot;
        this.a = a;
        this.b = b;
    }

    public static @NotNull <A, B> Either<A, B> ofA(@Nullable A a) {
        return new Either<>(Slot.A, a, null);
    }

    public static @NotNull <A, B> Either<A, B> ofB(@Nullable B b) {
        return new Either<>(Slot.B, null, b);
    }

    public @NotNull Slot whichSlot() {
        return slot;
    }

    public A a() {
        switch (slot) {
            case A: return a;
            case B: throw new IllegalStateException();
            default: throw new IllegalStateException("unreachable");
        }
    }

    public B b() {
        switch (slot) {
            case A: throw new IllegalStateException();
            case B: return b;
            default: throw new IllegalStateException("unreachable");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Either<?, ?> either = (Either<?, ?>) o;
        if (slot != either.slot) return false;
        switch (slot) {
            case A: return a == null ? either.a == null : a.equals(either.a);
            case B: return b == null ? either.b == null : b.equals(either.b);
            default: throw new IllegalStateException("unreachable");
        }
    }

    @Override
    public int hashCode() {
        int result = 31 * slot.hashCode();
        switch (slot) {
            case A: return result + (a == null ? 0 : a.hashCode());
            case B: return result + (b == null ? 0 : b.hashCode());
            default: throw new IllegalStateException("unreachable");
        }
    }

    public @NotNull String toString() {
        switch (slot) {
            case A: return "<" + a + ", >";
            case B: return "<, " + b + ">";
            default: throw new IllegalStateException("unreachable");
        }
    }

    public void validate() {
        switch (slot) {
            case A:
                Testing.assertNull(this, b);
                return;
            case B:
                Testing.assertNull(this, a);
                return;
            default: throw new IllegalStateException("unreachable");
        }
    }
}
