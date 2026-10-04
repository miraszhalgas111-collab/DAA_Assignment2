# DAA Assignment 2

Data Structures & Empirical Analysis project implemented in Java.

## Data Structures

- DynamicArray
- MyLinkedList
- MinHeap

## Workloads

- W1 — Random indexed access
- W2 — Contains miss
- W3 — Insert/remove at head and middle
- W4 — MinHeap insert and extractMin

## Input Sizes

- 100
- 1,000
- 10,000
- 100,000

Each benchmark case is executed 5 times and the median time is reported.

## Project Structure

- `src/main/java/org/example/` — implementations and benchmark
- `src/test/java/org/example/` — JUnit 5 tests
- `results/results.csv` — benchmark results
- `plots/` — benchmark plots
- `REPORT.md` — analysis report

## Run

Run:

`Benchmark.java`

Benchmark results will be saved to:

`results/results.csv`

To generate plots, run:

`PlotGenerator.java`

## Testing

The project contains 12 JUnit 5 tests.

Test classes:

- `DynamicArrayTest`
- `MyLinkedListTest`
- `MinHeapTest`

## Git Branches

- `main`
- `feature/array`
- `feature/list`
- `feature/heap`
- `feature/metrics`