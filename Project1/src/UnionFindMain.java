import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UnionFindMain {

    private static final String[] SIZES = { "1k", "10k", "100k", "1000k" };
    private static final String[] FILES = { "a", "b", "c", "d", "e" };
    private static final String DATASETS_BASE = "datasets";

    private static int getN(String sizeChoice) {
        switch (sizeChoice) {
            case "1k":   return 1000;
            case "10k":  return 10000;
            case "100k":  return 100000;
            case "1000k": return 1000000;
            default:      return 1000;
        }
    }

    /**
     * Runs one dataset file on a fresh UF instance and returns metrics.
     */
    private static String[] runOneDataset(String ufName, int ufType, String sizeChoice, String fileChoice) {
        int n = getN(sizeChoice);
        QuickUnion uf = null;
        if (ufType == 1) {
            uf = new QuickUnion(n);
        } else if (ufType == 2) {
            uf = new WeightedQuickUnion(n);
        } else {
            uf = new WQUUFPathCompression(n);
        }

        String path = DATASETS_BASE + "/" + sizeChoice + "/" + fileChoice + ".txt";
        long startTime = System.nanoTime();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int spaceIdx = line.indexOf(' ');
                if (spaceIdx < 0) continue;
                int firstNumber = Integer.parseInt(line.substring(0, spaceIdx));
                int secondNumber = Integer.parseInt(line.substring(spaceIdx + 1));
                uf.Union(firstNumber, secondNumber);
            }
        } catch (IOException e) {
            return new String[] { ufName, sizeChoice, fileChoice, "ERROR", "", "", e.getMessage() };
        }

        long endTime = System.nanoTime();
        double runtimeMs = (endTime - startTime) / 1e6;

        return new String[] {
            ufName,
            sizeChoice,
            fileChoice,
            String.valueOf(uf.getMaxTreeHeight()),
            String.valueOf(uf.arrayAccesses),
            String.valueOf(uf.revisitConnections),
            String.format("%.2f", runtimeMs)
        };
    }

    /**
     * Tests one Union-Find implementation on all dataset combinations (all sizes × all files).
     */
    private static List<String[]> testAllDatasetsOnFind(String ufName, int ufType) {
        List<String[]> rows = new ArrayList<>();
        for (String sizeChoice : SIZES) {
            for (String fileChoice : FILES) {
                System.out.println("  " + ufName + " @ " + sizeChoice + "/" + fileChoice + ".txt");
                String[] row = runOneDataset(ufName, ufType, sizeChoice, fileChoice);
                rows.add(row);
            }
        }
        return rows;
    }

    private static void writeResultsToCsv(String path, List<String[]> allRows) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.println("UF,Size,File,MaxTreeHeight,ArrayAccesses,RevisitConnections,RuntimeMs");
            for (String[] row : allRows) {
                out.println(String.join(",", row));
            }
        }
    }

    public static void main(String[] args) {
        List<String[]> allRows = new ArrayList<>();

        System.out.println("Running QuickUnion on all datasets...");
        allRows.addAll(testAllDatasetsOnFind("QuickUnion", 1));

        System.out.println("Running WeightedQuickUnion on all datasets...");
        allRows.addAll(testAllDatasetsOnFind("WeightedQuickUnion", 2));

        System.out.println("Running WQUUFPathCompression on all datasets...");
        allRows.addAll(testAllDatasetsOnFind("WQUUFPathCompression", 3));

        String csvPath = "union_find_results.csv";
        try {
            writeResultsToCsv(csvPath, allRows);
            System.out.println("Results written to " + csvPath);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}
