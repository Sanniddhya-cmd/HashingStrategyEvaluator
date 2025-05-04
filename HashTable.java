package hashinglab;

import java.io.*;
import java.util.*;

public class HashTable {
    public enum Strategy { LINEAR, QUADRATIC, CHAINING }

    private final int tableSize;
    private final Strategy strategy;
    private final String hashType; // "division" or "student"
    private final int mod;         // divisor used if hashType = "division"
    private final int bucketSize;  // 1 or 3

    private int[] table1;
    private int[][] table3;
    private LinkedList<Integer>[] chainTable;

    private int comparisons = 0;
    private int collisions = 0;
    private int inserted = 0;

    @SuppressWarnings("unchecked")
    public HashTable(int tableSize, Strategy strategy, String hashType, int mod, int bucketSize) {
        this.tableSize = tableSize;
        this.strategy = strategy;
        this.hashType = hashType;
        this.mod = mod;
        this.bucketSize = bucketSize;

        if (bucketSize == 1) {
            if (strategy == Strategy.CHAINING) {
                chainTable = new LinkedList[tableSize];
                for (int i = 0; i < tableSize; i++) chainTable[i] = new LinkedList<>();
            } else {
                table1 = new int[tableSize];
                Arrays.fill(table1, -1);
            }
        } else {
            table3 = new int[tableSize / 3][3];
            for (int[] row : table3) Arrays.fill(row, -1);
        }
    }

    private int hash(int key) {
        if (hashType.equals("student")) {
            double A = 0.6180339887; // multiplicative hashing constant
            return (int)(tableSize * ((key * A) % 1));
        } else {
            return key % mod;
        }
    }

    public void insert(int key) {
        int index = hash(key);
        comparisons++;

        if (bucketSize == 1) {
            if (strategy == Strategy.CHAINING) {
                if (!chainTable[index].isEmpty()) collisions++;
                if (!chainTable[index].contains(key)) {
                    chainTable[index].add(key);
                    inserted++;
                }
            } else {
                int i = 0, pos = index;
                while (table1[pos] != -1 && table1[pos] != key) {
                    collisions++;
                    comparisons++;
                    i++;
                    if (strategy == Strategy.LINEAR)
                        pos = (index + i) % tableSize;
                    else if (strategy == Strategy.QUADRATIC)
                        pos = (index + i * i) % tableSize;
                    if (i == tableSize) return;
                }
                comparisons++;
                if (table1[pos] == -1) {
                    table1[pos] = key;
                    inserted++;
                }
            }
        } else {
            int row = index % (tableSize / 3);
            boolean placed = false;

            for (int j = 0; j < 3; j++) {
                comparisons++;
                if (table3[row][j] == -1) {
                    table3[row][j] = key;
                    inserted++;
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                collisions++;
                int i = 1;
                while (i < (tableSize / 3)) {
                    int newRow;
                    if (strategy == Strategy.LINEAR)
                        newRow = (row + i) % (tableSize / 3);
                    else if (strategy == Strategy.QUADRATIC)
                        newRow = (row + i * i) % (tableSize / 3);
                    else
                        return;

                    for (int j = 0; j < 3; j++) {
                        comparisons++;
                        if (table3[newRow][j] == -1) {
                            table3[newRow][j] = key;
                            inserted++;
                            return;
                        }
                    }
                    collisions++;
                    i++;
                }
            }
        }
    }

    public void processFile(String inputFile, PrintWriter writer) {
        long start = System.nanoTime();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        int key = Integer.parseInt(line.replaceFirst("^0+(?!$)", ""));
                        insert(key);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFile);
        }

        long end = System.nanoTime();
        saveToWriter(writer, end - start);
    }

    private void saveToWriter(PrintWriter writer, long runtime) {
        writer.write(String.format("Runtime: %.7f ms\n", runtime / 1_000_000.0));
        writer.write("=========================\n");
        writer.write("Hash table size: " + tableSize + "\n");
        writer.write("Strategy: " + strategy + "\n");
        writer.write("Bucket Size: " + bucketSize + "\n");
        writer.write("Hashing Method: " + hashType + "\n");
        writer.write("Collisions: " + collisions + "\n");
        writer.write("Comparisons: " + comparisons + "\n");
        writer.write("Items not inserted: " + (tableSize - inserted) + "\n");
        writer.write(String.format("Load factor: %.2f\n\n", (double) inserted / tableSize));
        writer.write("Resulting table:\n");

        if (bucketSize == 1) {
            for (int i = 0; i < tableSize; i++) {
                if (strategy == Strategy.CHAINING) {
                    writer.write("[ ");
                    for (int v : chainTable[i]) writer.write(v + " ");
                    writer.write("]    ");
                } else {
                    writer.write(String.format("[%s]\t", (table1[i] == -1 ? "  " : table1[i])));
                }
                if ((i + 1) % 5 == 0) writer.write("\n");
            }
        } else {
            for (int i = 0; i < table3.length; i++) {
                writer.write("[ ");
                for (int j = 0; j < 3; j++) {
                    if (table3[i][j] == -1) {
                        writer.write("----- ");
                    } else {
                        writer.write(String.format("%05d ", table3[i][j]));
                    }
                }
                writer.write("] ");
                if ((i + 1) % 3 == 0) writer.write("\n");
            }
        }
    }
}

