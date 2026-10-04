package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class PlotGenerator {

    static class Row {
        String workload;
        String variant;
        String structure;
        int n;
        double time;

        Row(String workload, String variant,
            String structure, int n, double time) {
            this.workload = workload;
            this.variant = variant;
            this.structure = structure;
            this.n = n;
            this.time = time;
        }
    }

    public static void main(String[] args) throws Exception {

        List<Row> rows = readCSV("results/results.csv");

        new File("plots").mkdirs();

        createPlot(
                rows,
                "W1",
                "-",
                "W1 Random Access",
                "plots/w1_random_access.png"
        );

        createPlot(
                rows,
                "W2",
                "-",
                "W2 Contains Miss",
                "plots/w2_contains_miss.png"
        );

        createPlot(
                rows,
                "W3",
                "middle",
                "W3 Middle Insert Remove",
                "plots/w3_middle.png"
        );

        createPlot(
                rows,
                "W4",
                "-",
                "W4 MinHeap",
                "plots/w4_minheap.png"
        );

        System.out.println("Plots created successfully!");
    }

    private static List<Row> readCSV(String path) throws Exception {

        List<Row> rows = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new FileReader(path)
        );

        reader.readLine();

        String line;

        while ((line = reader.readLine()) != null) {

            String[] p = line.split(",");

            rows.add(new Row(
                    p[0],
                    p[1],
                    p[2],
                    Integer.parseInt(p[3]),
                    Double.parseDouble(p[4])
            ));
        }

        reader.close();

        return rows;
    }

    private static void createPlot(
            List<Row> rows,
            String workload,
            String variant,
            String title,
            String output
    ) throws Exception {

        int width = 900;
        int height = 600;

        BufferedImage image =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);

        g.drawLine(100, 500, 820, 500);
        g.drawLine(100, 80, 100, 500);

        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(title, 320, 40);

        List<Row> selected = new ArrayList<>();

        for (Row row : rows) {
            if (row.workload.equals(workload)
                    && row.variant.equals(variant)) {
                selected.add(row);
            }
        }

        double maxTime = 0;

        for (Row row : selected) {
            maxTime = Math.max(maxTime, row.time);
        }

        if (maxTime == 0) {
            maxTime = 1;
        }

        Map<String, Color> colors = new HashMap<>();
        colors.put("DynamicArray", Color.BLUE);
        colors.put("MyLinkedList", Color.RED);
        colors.put("MinHeap", Color.GREEN);

        Map<String, List<Row>> groups = new LinkedHashMap<>();

        for (Row row : selected) {
            groups.computeIfAbsent(
                    row.structure,
                    k -> new ArrayList<>()
            ).add(row);
        }

        int legendY = 70;

        for (Map.Entry<String, List<Row>> entry
                : groups.entrySet()) {

            List<Row> group = entry.getValue();

            group.sort(
                    Comparator.comparingInt(r -> r.n)
            );

            Color color = colors.getOrDefault(
                    entry.getKey(),
                    Color.BLACK
            );

            g.setColor(color);

            int previousX = -1;
            int previousY = -1;

            for (int i = 0; i < group.size(); i++) {

                Row row = group.get(i);

                int x = 140 + i * 200;

                int y = 500 -
                        (int) ((row.time / maxTime) * 380);

                g.fillOval(x - 5, y - 5, 10, 10);

                if (previousX != -1) {
                    g.drawLine(
                            previousX,
                            previousY,
                            x,
                            y
                    );
                }

                g.setColor(Color.BLACK);
                g.drawString(
                        String.valueOf(row.n),
                        x - 20,
                        525
                );

                g.setColor(color);

                previousX = x;
                previousY = y;
            }

            g.fillRect(650, legendY - 10, 20, 5);

            g.setColor(Color.BLACK);
            g.drawString(
                    entry.getKey(),
                    680,
                    legendY
            );

            legendY += 25;
        }

        g.setColor(Color.BLACK);

        g.drawString("n", 450, 560);

        g.rotate(-Math.PI / 2);
        g.drawString("Time (ms)", -330, 40);
        g.rotate(Math.PI / 2);

        g.dispose();

        ImageIO.write(
                image,
                "png",
                new File(output)
        );
    }
}