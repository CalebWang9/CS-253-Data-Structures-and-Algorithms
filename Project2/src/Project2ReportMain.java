import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs SCC analysis and PageRank on every dataset file under Project2/dataset,
 * then writes a single CSV report. Run with working directory = Project2
 * (same as DirectedGraph main).
 * <p>
 * CSV columns: record_type, file, field_1..field_5 (meaning depends on record_type):
 * <ul>
 *   <li>{@code summary} — metric name in field_1, value in field_2 (optional field_3..)</li>
 *   <li>{@code scc} — field_1=scc_index, field_2=size, field_3=members (semicolon-separated ids)</li>
 *   <li>{@code scc_ge_k} — field_1=k (tested for k=1,2,3,4,5), field_2=scc_index, field_3=size, field_4=members</li>
 *   <li>{@code top10_pagerank} — field_1=rank(1..10), field_2=node_id, field_3=PageRank, field_4=in_degree</li>
 *   <li>{@code error} — load failure</li>
 * </ul>
 * After {@link DirectedGraph#findSCCs()}, {@link DirectedGraph#reverseGraph()} is called once so
 * PageRank runs on the original edge directions (Kosaraju leaves the graph reversed).
 */
public class Project2ReportMain {

    private static final String[] DATASET_FILES = {
            "Project2/dataset/dataset A/small.txt",
            "Project2/dataset/dataset A/medium.txt",
            "Project2/dataset/dataset A/large.txt",
            "Project2/dataset/dataset B/small.txt",
            "Project2/dataset/dataset B/medium.txt",
            "Project2/dataset/dataset B/large.txt"
    };

    private static final String DEFAULT_CSV_OUT = "project2_report.csv";

    /** Minimum SCC sizes k for which SCCs with size &gt;= k are listed and counted. */
    private static final int[] K_VALUES = {1, 2, 3, 4, 5};

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("CSV output path [" + DEFAULT_CSV_OUT + "]: ");
        String outPath = scan.nextLine().trim();
        if (outPath.isEmpty()) {
            outPath = DEFAULT_CSV_OUT;
        }
        scan.close();

        try (BufferedWriter w = new BufferedWriter(new FileWriter(outPath))) {
            w.write("record_type,file,field_1,field_2,field_3,field_4,field_5\n");

            for (String relPath : DATASET_FILES) {
                processFile(relPath, w);
            }
            System.out.println("Wrote report to: " + outPath);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processFile(String relPath, BufferedWriter w) throws IOException {
        DirectedGraph g = loadGraph(relPath);
        if (g == null) {
            w.write(csvRow("error", relPath, "load_failed", "", "", "", ""));
            return;
        }

        int n = g.adj.size();

        // SCC (Kosaraju mutates graph to reversed adjacency; restore for PageRank)
        g.findSCCs();
        g.reverseGraph();

        int totalSccs = g.totalSCCs;
        int largestSize = 0;
        int largestIndex = -1;
        for (int i = 0; i < g.sizesSCC.length; i++) {
            if (g.sizesSCC[i] > largestSize) {
                largestSize = g.sizesSCC[i];
                largestIndex = i;
            }
        }

        w.write(csvRow("summary", relPath, "total_sccs", String.valueOf(totalSccs), "", "", ""));
        w.write(csvRow("summary", relPath, "largest_scc_size", String.valueOf(largestSize), "", "", ""));
        w.write(csvRow("summary", relPath, "largest_scc_index", String.valueOf(largestIndex), "", "", ""));
        if (largestIndex >= 0 && largestIndex < g.SCCs.size()) {
            w.write(csvRow("summary", relPath, "largest_scc_members",
                    joinInts(g.SCCs.get(largestIndex)), "", "", ""));
        }

        for (int i = 0; i < g.SCCs.size(); i++) {
            int sz = g.sizesSCC[i];
            w.write(csvRow("scc", relPath, String.valueOf(i), String.valueOf(sz),
                    joinInts(g.SCCs.get(i)), "", ""));
        }

        for (int k : K_VALUES) {
            int countGeK = 0;
            for (int i = 0; i < g.sizesSCC.length; i++) {
                if (g.sizesSCC[i] >= k) {
                    countGeK++;
                    w.write(csvRow("scc_ge_k", relPath, String.valueOf(k), String.valueOf(i),
                            String.valueOf(g.sizesSCC[i]), joinInts(g.SCCs.get(i)), ""));
                }
            }
            w.write(csvRow("summary", relPath, "count_sccs_size_ge_k", String.valueOf(countGeK),
                    "k=" + k, "", ""));
        }

        pageRank pr = new pageRank(g);
        w.write(csvRow("summary", relPath, "pagerank_iterations", String.valueOf(pr.getIterations()),
                "", "", ""));

        List<Integer> order = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        order.sort((a, b) -> {
            int c = Double.compare(pr.pr2[b], pr.pr2[a]);
            if (c != 0) {
                return c;
            }
            return Integer.compare(a, b);
        });

        int top = Math.min(10, n);
        for (int r = 0; r < top; r++) {
            int node = order.get(r);
            int inDeg = g.getInDegree(g.getAllVertices().get(node));
            w.write(csvRow("top10_pagerank", relPath, String.valueOf(r + 1), String.valueOf(node),
                    String.valueOf(pr.pr2[node]), String.valueOf(inDeg), ""));
        }
    }

    private static DirectedGraph loadGraph(String relPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(relPath))) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            String[] dimensions = line.trim().split("\\s+");
            int numVertices = Integer.parseInt(dimensions[0]);
            DirectedGraph g = new DirectedGraph(numVertices);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    int u = Integer.parseInt(parts[0]);
                    int v = Integer.parseInt(parts[1]);
                    g.addEdge(g.new Node(u, null), g.new Node(v, null));
                }
            }
            return g;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading " + relPath + ": " + e.getMessage());
            return null;
        }
    }

    private static String joinInts(int[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(';');
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    private static String csvEscape(String s) {
        if (s == null) {
            return "";
        }
        boolean needQuote = s.indexOf(',') >= 0 || s.indexOf('"') >= 0 || s.indexOf('\n') >= 0;
        String t = s.replace("\"", "\"\"");
        if (needQuote) {
            return "\"" + t + "\"";
        }
        return t;
    }

    private static String csvRow(String a, String b, String c, String d, String e, String f, String g) {
        return csvEscape(a) + "," + csvEscape(b) + "," + csvEscape(c) + "," + csvEscape(d) + ","
                + csvEscape(e) + "," + csvEscape(f) + "," + csvEscape(g) + "\n";
    }
}
