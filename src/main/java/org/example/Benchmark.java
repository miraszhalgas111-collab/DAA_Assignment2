package org.example;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {
            100, 1000, 10000, 100000
    };

    public static void main(String[] args) throws Exception {

        File folder = new File("results");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        PrintWriter writer = new PrintWriter("results/results.csv");

        writer.println(
                "workload,variant,structure,n,time_ms,steps,moves,comparisons"
        );

        for (int n : SIZES) {

            System.out.println("Testing n = " + n);

            int[] data = generateData(n);

            runW1(data, n, writer);
            runW2(data, n, writer);
            runW3(data, n, writer);
            runW4(data, n, writer);
        }

        writer.close();

        System.out.println("Benchmark finished!");
        System.out.println("results/results.csv created.");
    }

    // ---------------- W1 ----------------
    // Random Access

    private static void runW1(
            int[] data,
            int n,
            PrintWriter writer) {

        DynamicArray array = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        for (int value : data) {
            array.add(value);
            list.add(value);
        }

        Random random = new Random(42);

        // warm-up
        for (int i = 0; i < 1000; i++) {
            int index = random.nextInt(n);
            array.get(index);
            list.get(index);
        }

        array.resetMetrics();

        double[] times = new double[5];

        for (int run = 0; run < 5; run++) {

            Random runRandom = new Random(42 + run);

            long start = System.nanoTime();

            for (int i = 0; i < 10000; i++) {
                array.get(runRandom.nextInt(n));
            }

            long end = System.nanoTime();

            times[run] = (end - start) / 1_000_000.0;
        }

        double medianTime = median(times);

        writeResult(
                writer,
                "W1",
                "-",
                "DynamicArray",
                n,
                medianTime,
                array.getMetrics()
        );

        list.resetMetrics();

        list.resetMetrics();

        double[] listTimes = new double[5];

        for (int run = 0; run < 5; run++) {

            Random runRandom = new Random(42 + run);

            long start = System.nanoTime();

            for (int i = 0; i < 10000; i++) {
                list.get(runRandom.nextInt(n));
            }

            long end = System.nanoTime();

            listTimes[run] = (end - start) / 1_000_000.0;
        }

        double listMedianTime = median(listTimes);

        writeResult(
                writer,
                "W1",
                "-",
                "MyLinkedList",
                n,
                listMedianTime,
                list.getMetrics()
        );
    }

    // ---------------- W2 ----------------
    // Search

    private static void runW2(
            int[] data,
            int n,
            PrintWriter writer) {

        DynamicArray array = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        for (int value : data) {
            array.add(value);
            list.add(value);
        }

        int[] queries = new int[1000];

        for (int i = 0; i < 500; i++) {
            queries[i] = data[i % n];
        }

        for (int i = 500; i < 1000; i++) {
            queries[i] = Integer.MIN_VALUE + i;
        }


        array.resetMetrics();

        double[] arrayTimes = new double[5];

        for (int run = 0; run < 5; run++) {

            long start = System.nanoTime();

            for (int x : queries) {
                array.contains(x);
            }

            long end = System.nanoTime();

            arrayTimes[run] = (end - start) / 1_000_000.0;
        }

        double arrayMedianTime = median(arrayTimes);

        writeResult(
                writer,
                "W2",
                "-",
                "DynamicArray",
                n,
                arrayMedianTime,
                array.getMetrics()
        );

        list.resetMetrics();

        double[] listTimes = new double[5];

        for (int run = 0; run < 5; run++) {

            long start = System.nanoTime();

            for (int x : queries) {
                list.contains(x);
            }

            long end = System.nanoTime();

            listTimes[run] = (end - start) / 1_000_000.0;
        }

        double listMedianTime = median(listTimes);

        writeResult(
                writer,
                "W2",
                "-",
                "MyLinkedList",
                n,
                listMedianTime,
                list.getMetrics()
        );
    }

    // ---------------- W3 ----------------
    // Insert & Remove

    private static void runW3(
            int[] data,
            int n,
            PrintWriter writer) {

        runW3Variant(data, n, writer, "head");
        runW3Variant(data, n, writer, "middle");
    }

    private static void runW3Variant(
            int[] data,
            int n,
            PrintWriter writer,
            String variant) {

        DynamicArray array = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        for (int value : data) {
            array.add(value);
            list.add(value);
        }

        double[] arrayTimes = new double[5];
        Metrics arrayMetrics = null;

        for (int run = 0; run < 5; run++) {

            DynamicArray testArray = new DynamicArray();

            for (int value : data) {
                testArray.add(value);
            }

            testArray.resetMetrics();

            long start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                int index;

                if (variant.equals("head")) {
                    index = 0;
                } else {
                    index = testArray.size() / 2;
                }

                testArray.add(index, i);
            }

            for (int i = 0; i < 1000; i++) {
                int index;

                if (variant.equals("head")) {
                    index = 0;
                } else {
                    index = testArray.size() / 2;
                }

                testArray.remove(index);
            }

            long end = System.nanoTime();

            arrayTimes[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                arrayMetrics = testArray.getMetrics();
            }
        }

        double arrayMedianTime = median(arrayTimes);

        writeResult(
                writer,
                "W3",
                variant,
                "DynamicArray",
                n,
                arrayMedianTime,
                arrayMetrics
        );

        double[] listTimes = new double[5];
        Metrics listMetrics = null;

        for (int run = 0; run < 5; run++) {

            MyLinkedList testList = new MyLinkedList();

            for (int value : data) {
                testList.add(value);
            }

            testList.resetMetrics();

            long start = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                int index;

                if (variant.equals("head")) {
                    index = 0;
                } else {
                    index = testList.size() / 2;
                }

                testList.add(index, i);
            }

            for (int i = 0; i < 1000; i++) {
                int index;

                if (variant.equals("head")) {
                    index = 0;
                } else {
                    index = testList.size() / 2;
                }

                testList.remove(index);
            }

            long end = System.nanoTime();

            listTimes[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                listMetrics = testList.getMetrics();
            }
        }

        double listMedianTime = median(listTimes);

        writeResult(
                writer,
                "W3",
                variant,
                "MyLinkedList",
                n,
                listMedianTime,
                listMetrics
        );
    }

    // ---------------- W4 ----------------
    // Priority Processing

    private static void runW4(
            int[] data,
            int n,
            PrintWriter writer) {

        double[] heapTimes = new double[5];
        Metrics heapMetrics = null;

        for (int run = 0; run < 5; run++) {

            MinHeap testHeap = new MinHeap();
            testHeap.resetMetrics();

            long start = System.nanoTime();

            for (int value : data) {
                testHeap.insert(value);
            }

            int previous = Integer.MIN_VALUE;

            while (testHeap.size() > 0) {

                int current = testHeap.extractMin();

                if (current < previous) {
                    throw new IllegalStateException(
                            "Heap output is not sorted!"
                    );
                }

                previous = current;
            }

            long end = System.nanoTime();

            heapTimes[run] = (end - start) / 1_000_000.0;

            if (run == 0) {
                heapMetrics = testHeap.getMetrics();
            }
        }

        double heapMedianTime = median(heapTimes);

        writeResult(
                writer,
                "W4",
                "-",
                "MinHeap",
                n,
                heapMedianTime,
                heapMetrics
        );
    }

    // ---------------- DATA ----------------

    private static int[] generateData(int n) {

        Random random = new Random(42);

        int[] data = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(1_000_000);
        }

        return data;
    }

    // ---------------- CSV ----------------

    private static void writeResult(
            PrintWriter writer,
            String workload,
            String variant,
            String structure,
            int n,
            double time,
            Metrics metrics) {

        writer.printf(
                "%s,%s,%s,%d,%.6f,%d,%d,%d%n",
                workload,
                variant,
                structure,
                n,
                time,
                metrics.getSteps(),
                metrics.getMoves(),
                metrics.getComparisons()
        );
    }

    private static double median(double[] times) {
        Arrays.sort(times);
        return times[times.length / 2];
    }
}