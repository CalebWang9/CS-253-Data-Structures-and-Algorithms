import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class floydWarshall {
    double[][] dist;
    int[][] next; // To store the path

    public floydWarshall(int vertices) {
        dist = new double[vertices][vertices];
        next = new int[vertices][vertices];
        
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                next[i][j] = -1; // -1 means no path exists yet
            }
        }
    }

    public void addEdge(int u, int v, double weight) {
        dist[u][v] = weight;
        next[u][v] = v; // To go from u to v, the next step is v
    }

	
    static void floydWarshall(double[][] dist, int[][] next) {
        int V = dist.length;
        int INF = (int)1e8;

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            // The path from i to j now starts by following the path from i to k
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }
    }
    
    public String getPath(int u, int v) {
        if (next[u][v] == -1) return "No path";

        ArrayList<Integer> fullPath = new ArrayList<>();
        int curr = u;
        fullPath.add(curr);
        
        while (curr != v) {
            curr = next[curr][v];
            fullPath.add(curr);
        }

        // Logic to trim display if path > 10 vertices
        if (fullPath.size() > 10) {
            StringBuilder sb = new StringBuilder();
            
            // Add first 5
            for (int i = 0; i < 5; i++) {
                sb.append(fullPath.get(i)).append(" -> ");
            }
            
            sb.append("... -> ");
            
            // Add last 5
            for (int i = fullPath.size() - 5; i < fullPath.size(); i++) {
                sb.append(fullPath.get(i));
                if (i < fullPath.size() - 1) sb.append(" -> ");
            }
            
            return sb.toString();
        } else {
            // Return full path if 10 or fewer vertices
            return String.join(" -> ", fullPath.stream().map(Object::toString).toArray(String[]::new));
        }
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String sizeChoice;
        String fileChoice;

        boolean invalid = true;
        do {
            System.out.println("Please select which size file: tinyEWD, Rome, 10000EWD, or largeEWD");
            sizeChoice = scan.nextLine();
            if (sizeChoice != null && (sizeChoice.equals("tinyEWD") || sizeChoice.equals("Rome") || sizeChoice.equals("10000EWD") || sizeChoice.equals("largeEWD"))) {
                invalid = false;
            } else {
                System.out.println("Invalid option.");
            }
        } while (invalid);



        // NEW: User input for k

        long startTime = System.nanoTime();
        scan.close(); // Close scanner after all inputs are collected

        floydWarshall Test1 = new floydWarshall(1);
        try (BufferedReader reader = new BufferedReader(new FileReader("Project2/dataset/dataset/"+ sizeChoice + ".txt"))) {
            String line = reader.readLine();
            if (line == null) return;
            
            String[] dimensions = line.trim().split("\\s+");
            int numVertices = Integer.parseInt(dimensions[0]);
            Test1 = new floydWarshall(numVertices);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 3) {
                    int u = Integer.parseInt(parts[0]);
                    int v = Integer.parseInt(parts[1]);
                    Double w = Double.parseDouble(parts[2]);
                    Test1.addEdge(u, v, w);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        
        for (int i = 0; i < Test1.dist.length; i++) {
        	for (int j = 0; j < Test1.dist.length; j++) {
        		if (i != j && (Math.abs(Test1.dist[i][j] - 0) < 0.00001)) {
        			Test1.dist[i][j] = (int)1e8;
        		}
        	}
        }
        
        floydWarshall(Test1.dist, Test1.next);
        
        
        System.out.println();
        
        System.out.println("Single source from vertex 0");
        
       
        	for (int j = 0; j < Test1.dist.length; j++) {
        		System.out.println(0 + " to " + j + " (" + Test1.dist[0][j] + ") " +  Test1.getPath(0, j));
        	}
        
        System.out.println();
        	
        	
        System.out.println("All pairs (truncated path if > 10 nodes):");	
        for (int i = 0; i < Test1.dist.length; i++) {
            for (int j = 0; j < Test1.dist.length; j++) {
                    System.out.println(i + " to " + j + " (" + Test1.dist[i][j] + ") " + Test1.getPath(i, j));
                
            }
        }


       


        long endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1e6;
        System.out.println("Runtime: " + delta + " ms");
    }

}
