import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuickUnion {
	ArrayList<Node> QuickUnionArray;
	
	long arrayAccesses = 0;
    long revisitConnections = 0;

	public QuickUnion (int n) {
		QuickUnionArray = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			Node temp = new Node(i, null);
			temp.setNext(temp);
			QuickUnionArray.add(temp);
		}
		
	}
	
	
	public boolean connected(Node x, Node y) {
		if (getRoot(x).equals(getRoot(y))) {
			return true;
		}
		return false;
	}
	
	public void Union(int a, int b) {
		arrayAccesses += 2;
		if (connected(QuickUnionArray.get(a), QuickUnionArray.get(b))) {
			revisitConnections++;
			return;
		}
		Node temp = QuickUnionArray.get(a);
		arrayAccesses++;
		getRoot(temp).setNext(QuickUnionArray.get(b).getNext());
		arrayAccesses++;
	}
	
	public Node getRoot(Node a) {
		Node temp = a;
		arrayAccesses++;
		while (temp.getNext() != temp) {
			temp = temp.next;
			arrayAccesses++;
		}
		return temp;
	}
	
	public int getMaxTreeHeight() {
        int max = 0;
        for (Node n : QuickUnionArray) {
            int h = n.getHeight();
            if (h > max) max = h;
        }
        return max;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		String sizeChoice;
		String fileChoice;
		long arrayAccesses = 0;
		long revisitConnections = 0; 
		
		boolean invalid = true;
		
		do {
			System.out.println("Please select which size file you want to read/run: 1k, 10k, 100k, or 1000k");
			
			sizeChoice = scan.nextLine();
			
			if (sizeChoice != null && (sizeChoice.equals("1k") || sizeChoice.equals("10k") || sizeChoice.equals("100k") || sizeChoice.equals("1000k"))) {
				invalid = false;
			}
			
			else {
			System.out.println("Invalid option please pick 1k, 10k, 100k, or 1000k");
			}
			
		} while (invalid);
		
		
		boolean invalid2 = true;
		
		do {
		System.out.println("Please select which file you want to read/run: a, b, c, d, or e");
		
		fileChoice = scan.nextLine();
		
		if (fileChoice != null && (fileChoice.equals("a") || fileChoice.equals("b") || fileChoice.equals("c") || fileChoice.equals("d") || fileChoice.equals("e"))) {
			invalid2 = false;
		}
		
		else {
		System.out.println("Invalid option please pick a, b, c, d, or e");
		}
		
		
		} while (invalid2);
		
		long startTime = System.nanoTime();
		
		scan.close();
		int n = 0;
		if (sizeChoice.equals("1k")) {
			n = 1000;
		}
		else if (sizeChoice.equals("10k")) {
			n = 10000;
		}
		else if (sizeChoice.equals("100k")) {
			n = 100000;
		}
		else {
			n = 1000000;
		}
		
		QuickUnion Test1 = new QuickUnion(n);
		
		try (BufferedReader reader = new BufferedReader(new FileReader("datasets/" + sizeChoice + "/" + fileChoice + ".txt"))) { //
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
            		Test1.Union(firstNumber, secondNumber);
                	
                }
 
                
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
		
		long endTime = System.nanoTime();
	     double delta = (endTime - startTime) / 1e6;
	      // Display the solution you discovered:
	     System.out.println("Max Tree Height: " + Test1.getMaxTreeHeight());
	     System.out.println("Array Accesses: " + Test1.arrayAccesses);
	     System.out.println("Re-visits: " + Test1.revisitConnections);
	     System.out.println("Runtime: " + delta + " ms");
		


	}
	
	private class Node {
		private Node next;
		private int value;
		
		public Node (int value, Node next) {
			this.value = value;
			this.next = next;
		}
		
		public int getHeight() {
			int count = 0;
			Node current = this;
			while (current.next != current) {
				count++;
				current = current.next;
			}
			return count;
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
