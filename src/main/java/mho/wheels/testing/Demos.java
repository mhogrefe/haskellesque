package mho.wheels.testing;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;

import static mho.wheels.testing.Testing.LARGE_LIMIT;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;

public class Demos {
    protected int LIMIT;
    protected IterableProvider P;

    public Demos(boolean useRandom) {
        if (useRandom) {
            P = RandomProvider.example();
            LIMIT = MEDIUM_LIMIT;
        } else {
            P = ExhaustiveProvider.INSTANCE;
            LIMIT = LARGE_LIMIT;
        }
    }
}
