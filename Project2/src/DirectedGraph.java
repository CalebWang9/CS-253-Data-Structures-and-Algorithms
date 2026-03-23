import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DirectedGraph {
	ArrayList<Node> adj;
	
	public DirectedGraph(int Vertexs) {
		adj = new ArrayList<Node>();
		for (int i = 0; i < Vertexs; i++) {
			adj.add(new Node(i, null));
		}
		
	}
	
	public void addVertex(Node Vertex) {
		adj.add(Vertex);
	}
	
	public void addEdge(Node u, Node v) {
		Node temp = adj.get(u.getValue());
		while (temp.next != null) {
			temp = temp.next;
		}
		temp.next = v;
	}
	
	public Node getNeighbors(Node v) {
		if (adj.get(v.getValue()) == null || adj.get(v.getValue()).next == null) {
			return null;
		}
				
		Node result = adj.get(v.getValue());
		return result.next;
	}
	
	public int getInDegree(Node v) {
		int counter = 0;
		for (Node n: adj) {
			Node temp = getNeighbors(v);
			while (temp.next != null) {
				temp = temp.next;
				if (temp.getValue() == v.getValue()) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	public int getOutDegree(Node v) {
		int counter = 0;
		Node temp = getNeighbors(v);
		while (temp.next != null) {
			temp = temp.next;
			counter++;
		}
		return counter;
	}
	
	public void reverseGraph() {
		ArrayList<Node> adj1 = new ArrayList<Node>();
		for (int i = 0; i < adj.size(); i++) {
			adj1.add(new Node(i, null));
		}
		
		
		for (Node n: adj) {
			if (adj.get(n.getValue()).next != null) {
				Node temp = getNeighbors(n);
				while (temp != null) {
					Node replacement = adj1.get(temp.getValue()).next;
					if (replacement == null) {
						adj1.get(temp.getValue()).next = new Node(n.getValue(), null);
					}
					else {
					adj1.get(temp.getValue()).next = new Node(n.getValue(), replacement);
					}
					temp = temp.next;
				}
			}
		}
		
		adj = adj1;
	}
	
	public ArrayList<Node> getAllVertices() {
		return adj;
	}
			
			
	

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String sizeChoice;
		String fileChoice;
		
		boolean invalid = true;
		
		do {
			System.out.println("Please select which size file you want to read/run: small, medium, or large");
			
			sizeChoice = scan.nextLine();
			
			if (sizeChoice != null && (sizeChoice.equals("small") || sizeChoice.equals("medium") || sizeChoice.equals("large"))) {
				invalid = false;
			}
			
			else {
			System.out.println("Invalid option please pick small, medium, or large");
			}
			
		} while (invalid);
		
		
		boolean invalid2 = true;
		
		do {
		System.out.println("Please select which dataset you want to read/run: A or B");
		
		fileChoice = scan.nextLine();
		
		if (fileChoice != null && (fileChoice.equals("A") || fileChoice.equals("B"))) {
			invalid2 = false;
		}
		
		else {
		System.out.println("Invalid option please pick A or B");
		}
		
		
		} while (invalid2);
		
		long startTime = System.nanoTime();
		
		scan.close();
		
		DirectedGraph Test1 = new DirectedGraph(100);
		
		
		try (BufferedReader reader = new BufferedReader(new FileReader("dataset/dataset " + fileChoice + "/" + sizeChoice + ".txt"))) { //
            String line;
            String first;
            String second;
            int firstNumber = 0;
            int secondNumber = 0;

            while ((line = reader.readLine()) != null) { 
            	 for (int i = 0; i < line.length(); i++) {
                 	if (line.charAt(i) == 32) {
                 		int spaceIndex = i;
                 		first = line.substring(0, i);
                 		second = line.substring(i + 1);
                 		firstNumber = Integer.parseInt(first);
                 		secondNumber = Integer.parseInt(second);
                 		
                 		break;
                 	}
                 }
            	 	
            	 
            	 Test1.addEdge(Test1.new Node(firstNumber, null), Test1.new Node(secondNumber, null));
                	
                }
 
                
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
		
		
		
		Test1.reverseGraph();
		
		ArrayList<Node> temp = Test1.getAllVertices();
		for (int i = 0; i < temp.size(); i++) {
			Node temp2 = temp.get(i);
			while (temp2 != null) {
				System.out.print(temp2.getValue() + " ");
				temp2 = temp2.next;
			}
			System.out.println();
		}
		
		
		long endTime = System.nanoTime();
	     double delta = (endTime - startTime) / 1e6;
	     System.out.println("Runtime: " + delta + " ms");

	}
	
	private class Node {
		private Node next;
		private int value;
		
		public Node (int value, Node next) {
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
