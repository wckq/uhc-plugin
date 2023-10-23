package io.github.wickeddroid.api.util.numbers;

import java.util.ArrayList;
import java.util.List;

public class RangedNumbers {

    public static List<Integer> fromRange(int from, int to) {
        List<Integer> ints = new ArrayList<>();

        for(int i = from; i < to; i++) {
            ints.add(i);
        }

        return ints;
    }
}
