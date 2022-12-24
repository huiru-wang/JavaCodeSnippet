package com.snippet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class MinBinaryHeapTest {

    @Test
    public void min_heap_test() {
        MinBinaryHeap<Integer> minBinaryHeap = new MinBinaryHeap<>(10, Comparator.comparingInt(e -> e));
        minBinaryHeap.push(5);
        minBinaryHeap.push(3);
        Assertions.assertEquals(3, minBinaryHeap.peek());
        Assertions.assertEquals(2, minBinaryHeap.size());
        Assertions.assertFalse(minBinaryHeap.isEmpty());
        minBinaryHeap.push(1);
        int pop = minBinaryHeap.pop();
        Assertions.assertEquals(1, pop);
        int max = Integer.MAX_VALUE;
        System.out.println(max);
        System.out.println(max >> 1);
        System.out.println(max + 3);
    }
}
