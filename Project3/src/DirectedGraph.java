import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class DirectedGraph {
	ArrayList<Node> adj;
	int totalSCCs = 0;
	ArrayList<int []> SCCs = new ArrayList<int []>();
	int [] sizesSCC;
	int [] largestSCC;
	Stack<Integer> stack = new Stack<Integer>();

	boolean[] exists;

	public DirectedGraph(int Vertexs) {
	    adj = new ArrayList<Node>();
	    exists = new boolean[Vertexs]; 
	    for (int i = 0; i < Vertexs; i++) {
	        adj.add(new Node(i, null));
	    }
	}

	public void addVertex(Node Vertex) {
		adj.add(Vertex);
	}

	public void addEdge(Node u, Node v) {
	    exists[u.getValue()] = true; 
	    exists[v.getValue()] = true; 
	    
	    Node head = adj.get(u.getValue());
	    Node newNode = new Node(v.getValue(), head.getNext());
	    head.setNext(newNode);
	}

	public Node getNeighbors(Node v) {
		if (adj.get(v.getValue()) == null || adj.get(v.getValue()).next == null) {
			return null;
		}

		Node result = adj.get(v.getValue());
		return result.next;
	}

	public ArrayList<Node> getAllVertices() {
		return adj;
	}
	
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
        
        
        long endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1e6;
        System.out.println("Runtime: " + delta + " ms");
        
    }

	public class Node {
		private Node next;
		private int value;

		public Node(int value, Node next) {
			this.value = value;
			this.next = next;
		}

		public void setNext(Node node) {
			next = node;
		}

		public Node getNext() {
			return next;
		}

		public int getValue() {
			return value;
		}
	}

}