package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void insertAndExtractMinTest() {
        MinHeap heap = new MinHeap();

        heap.insert(30);
        heap.insert(10);
        heap.insert(20);

        assertEquals(10, heap.extractMin());
        assertEquals(20, heap.extractMin());
        assertEquals(30, heap.extractMin());
    }

    @Test
    void sizeTest() {
        MinHeap heap = new MinHeap();

        heap.insert(10);
        heap.insert(20);
        heap.insert(30);

        assertEquals(3, heap.size());

        heap.extractMin();

        assertEquals(2, heap.size());
    }

    @Test
    void duplicateValuesTest() {
        MinHeap heap = new MinHeap();

        heap.insert(5);
        heap.insert(5);
        heap.insert(5);

        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }

    @Test
    void heapOrderingTest() {
        MinHeap heap = new MinHeap();

        heap.insert(50);
        heap.insert(10);
        heap.insert(40);
        heap.insert(20);
        heap.insert(30);

        int previous = Integer.MIN_VALUE;

        while (heap.size() > 0) {
            int current = heap.extractMin();

            assertTrue(current >= previous);

            previous = current;
        }
    }
}