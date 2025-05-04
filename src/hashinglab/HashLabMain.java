package hashinglab;

import java.io.*;
import java.util.Scanner;

public class HashLabMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask user for input file
        System.out.print("Enter the full path of the input file: ");
        String inputFile = scanner.nextLine().trim();
        while (!new File(inputFile).exists()) {
            System.out.println("Invalid file. Try again:");
            inputFile = scanner.nextLine().trim();
        }

        // Ask user for output filename
        System.out.print("Enter name for the consolidated output file (default is 'AllHashingResults.txt'): ");
        String outputFile = scanner.nextLine().trim();
        if (outputFile.isEmpty()) outputFile = "AllHashingResults.txt";

        // Use PrintWriter to write to a single file
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {

            int[] mods = {120, 127, 113, 41};
            int[] bucketSizes;

            for (int mod : mods) {
                // For mod = 41, only bucket size 3 is valid
                if (mod == 41) {
                    bucketSizes = new int[]{3};
                } else {
                    bucketSizes = new int[]{1};
                }

                for (int bucketSize : bucketSizes) {
                    for (HashTable.Strategy strategy : HashTable.Strategy.values()) {
                        if (bucketSize == 3 && strategy == HashTable.Strategy.CHAINING) {
                            continue; // chaining not allowed for bucketSize 3
                        }
                        writer.printf("=== Scheme: mod=%d, bucketSize=%d, strategy=%s ===%n", mod, bucketSize, strategy);
                        HashTable table = new HashTable(mod, strategy, "division", mod, bucketSize);
                        table.processFile(inputFile, writer);
                        writer.println("\n\n");
                    }
                }
            }

            // Student hash schemes (only bucket size 1)
            for (HashTable.Strategy strategy : HashTable.Strategy.values()) {
                writer.printf("=== Scheme: studentHash, bucketSize=1, strategy=%s ===%n", strategy);
                HashTable table = new HashTable(120, strategy, "student", -1, 1);
                table.processFile(inputFile, writer);
                writer.println("\n\n");
            }

            System.out.println("✅ All 14 hashing results saved to: " + outputFile);

        } catch (IOException e) {
            System.out.println("❌ Error writing to consolidated file.");
            e.printStackTrace();
        }

        scanner.close();
    }
}
