import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphMain {
	public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String sizeChoice;
        String fileChoice;
        int kRequirement = 1; // Default to 1 (all SCCs)

        boolean invalid = true;
        do {
            System.out.println("Please select which size file: small, medium, or large");
            sizeChoice = scan.nextLine();
            if (sizeChoice != null && (sizeChoice.equals("small") || sizeChoice.equals("medium") || sizeChoice.equals("large"))) {
                invalid = false;
            } else {
                System.out.println("Invalid option.");
            }
        } while (invalid);

        boolean invalid2 = true;
        do {
            System.out.println("Please select which dataset: A or B");
            fileChoice = scan.nextLine();
            if (fileChoice != null && (fileChoice.equals("A") || fileChoice.equals("B"))) {
                invalid2 = false;
            } else {
                System.out.println("Invalid option.");
            }
        } while (invalid2);

        // NEW: User input for k
        System.out.println("Enter the minimum SCC size to display (k):");
        if (scan.hasNextInt()) {
            kRequirement = scan.nextInt();
        }
        scan.nextLine(); // Consume newline

        long startTime = System.nanoTime();
        scan.close(); // Close scanner after all inputs are collected

        DirectedGraph Test1 = new DirectedGraph(1);

        try (BufferedReader reader = new BufferedReader(new FileReader("Project2/dataset/dataset " + fileChoice + "/" + sizeChoice + ".txt"))) {
            String line = reader.readLine();
            if (line == null) return;
            
            String[] dimensions = line.trim().split("\\s+");
            int numVertices = Integer.parseInt(dimensions[0]);
            Test1 = new DirectedGraph(numVertices);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    int u = Integer.parseInt(parts[0]);
                    int v = Integer.parseInt(parts[1]);
                    Test1.addEdge(Test1.new Node(u, null), Test1.new Node(v, null));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // Run the algorithm
        pageRank pr = new pageRank(Test1);

		for (int i = 0; i < pr.pr2.length; i++) {
			System.out.println("Page Rank of node " + i + " is " + pr.pr2[i]);
		}

		System.out.println("number of iterations: "+pr.iterationsOfPR);

		pr.getTopten();
        
        long endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1e6;
        System.out.println("Runtime: " + delta + " ms");
    }
		
	
}
