/*
 * Name: Wilson, Tyler
 * Date: 15NOV22
 * Course: CMSC 451
 * Professor: Dr Potolea, Rodica
 */
// Import(s)
import java.io.FileWriter; // for writing to files
import java.io.IOException;
import java.util.Random; // for randomizing lists
import src.QuickSort;
import src.UnsortedException;

public class BenchmarkSorts {
    // JVM warmup
    static {
        int[] list = listN(10000);
        for (int i = 0; i < 10000; i++) {
            QuickSort inst = new QuickSort();
            r_list(list);
            try {
                inst.recursiveSort(list);
            } catch (UnsortedException e) {
                e.printStackTrace();
            }
            r_list(list);
            try {
                inst.iterativeSort(list);
            } catch (UnsortedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] Args) {
        try {
            run();
        } catch (UnsortedException e) {
            System.out.println("Exception: " + e);
        }
    }

    // Alot of this came from different snippets found in
    // https://www.cs.cornell.edu/courses/JavaAndDS/definitions.html
    public static void run() throws UnsortedException {
        int outerIndex = 10; // Count for current run
        int innerIndex = 50; // 0 is iterative, 1 is recursive

        int N = 100;
        int[] list = null;

        int[][][] opCounts = new int[2][outerIndex][innerIndex];
        long[][][] computeTimings = new long[2][outerIndex][innerIndex];
        QuickSort inst = new QuickSort();
        for (int i = 0; i < outerIndex; i++) {
            list = listN(N);
            for (int j = 0; j < innerIndex; j++) {
                r_list(list);
                inst.iterativeSort(list);
                inst.verifySort(list);
                opCounts[0][i][j] = inst.getCount();
                computeTimings[0][i][j] = inst.getTime();

                r_list(list);
                inst.recursiveSort(list);
                inst.verifySort(list);
                opCounts[1][i][j] = inst.getCount();
                computeTimings[1][i][j] = inst.getTime();
            }

            N += 100;
        }

        // File generation
        String iterOutput = "data/OutputStatsIterative.txt";
        String recurOutput = "data/OutputStatsRecursive.txt";
        String del = ":";
        try {
            FileWriter[] fileWriter = {
                    new FileWriter(iterOutput),
                    new FileWriter(recurOutput)
            };

            // Writing to file
            for (int a = 0; a < fileWriter.length; a++) {
                N = 100;
                for (int b = 0; b < outerIndex; b++) {
                    fileWriter[a].write(N + del);
                    for (int c = 0; c < innerIndex; c++) {
                        fileWriter[a].write(opCounts[a][b][c] + "," + computeTimings[a][b][c] + del);
                    }

                    fileWriter[a].write("\n");
                    N += 100;
                }
                fileWriter[a].close();
            }
            System.out.println("\nFiles created.\n");
        } catch (IOException ex) {
            System.out.println("Couldn't open file for writing.");
            System.exit(1);
        }
    }

    // Make a list of size N
    public static int[] listN(int N) {
        int[] list = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            list[i] = random.nextInt(N);
        }

        return list;
    }

    // Swap positions in a list
    public static void r_list(int[] list) {
        Random random = new Random();
        int j = 0;
        int temp = 0;
        for (int i = 0; i < list.length; i++) {
            j = random.nextInt(list.length);
            temp = list[i];
            list[i] = list[j];
            list[j] = temp;
        }

        return;
    }
}
