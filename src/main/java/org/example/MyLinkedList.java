package org.example;

public class MyLinkedList {

    private Node head;
    private int size;
    private Metrics metrics = new Metrics();

    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    public void add(int x) {
        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
            metrics.move();
        } else {
            Node current = head;

            while (current.next != null) {
                current = current.next;
                metrics.step();
            }

            current.next = newNode;
            metrics.move();
        }

        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node newNode = new Node(x);

        if (index == 0) {
            newNode.next = head;
            head = newNode;

            metrics.move();
            metrics.move();
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                metrics.step();
            }

            newNode.next = current.next;
            current.next = newNode;

            metrics.move();
            metrics.move();
        }

        size++;
    }

    public int remove(int index) {
        checkIndex(index);

        int removed;

        if (index == 0) {
            removed = head.data;
            metrics.step();

            head = head.next;
            metrics.move();
        } else {
            Node current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
                metrics.step();
            }

            removed = current.next.data;
            metrics.step();

            current.next = current.next.next;
            metrics.move();
        }

        size--;
        return removed;
    }

    public int get(int index) {
        checkIndex(index);

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
            metrics.step();
        }

        metrics.step();
        return current.data;
    }

    public boolean contains(int x) {
        Node current = head;

        while (current != null) {
            metrics.step();
            metrics.comparison();

            if (current.data == x) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return size;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
    }
}