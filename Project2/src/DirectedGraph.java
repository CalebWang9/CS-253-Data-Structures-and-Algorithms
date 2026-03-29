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

	public int getInDegree(Node v) {
		int counter = 0;
		for (Node n: adj) {
			Node temp = getNeighbors(n);
			while (temp != null) {
				if (temp.getValue() == v.getValue()) {
					counter++;
				}
				temp = temp.next;
			}
		}
		return counter;
	}
	
	public int getOutDegree(Node v) {
		int counter = 0;
		Node temp = getNeighbors(v);
		while (temp != null) {
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

		for (Node n : adj) {
			if (adj.get(n.getValue()).next != null) {
				Node temp = getNeighbors(n);
				while (temp != null) {
					Node replacement = adj1.get(temp.getValue()).next;
					if (replacement == null) {
						adj1.get(temp.getValue()).next = new Node(n.getValue(), null);
					} else {
						adj1.get(temp.getValue()).next = new Node(n.getValue(), replacement);
					}
					temp = temp.next;
				}
			}
		}

		adj = adj1;
	}

	public ArrayList<Node> returnReverseGraph() {
		ArrayList<Node> adj1 = new ArrayList<Node>();
		for (int i = 0; i < adj.size(); i++) {
			adj1.add(new Node(i, null));
		}

		for (Node n : adj) {
			if (adj.get(n.getValue()).next != null) {
				Node temp = getNeighbors(n);
				while (temp != null) {
					Node replacement = adj1.get(temp.getValue()).next;
					if (replacement == null) {
						adj1.get(temp.getValue()).next = new Node(n.getValue(), null);
					} else {
						adj1.get(temp.getValue()).next = new Node(n.getValue(), replacement);
					}
					temp = temp.next;
				}
			}
		}

		return adj1;
	}

	public ArrayList<Node> getAllVertices() {
		return adj;
	}
	
	public void dfs(int startIndex) {
	    boolean[] visited = new boolean[adj.size()];
	    dfsRecursive(adj.get(startIndex), visited);
	}

	private void dfsRecursive(Node current, boolean[] visited) {
	    visited[current.getValue()] = true;
	    System.out.print(current.getValue() + " ");

	    Node neighbor = current.getNext();

	    while (neighbor != null) {
	        int neighborValue = neighbor.getValue();
	        
	        if (!visited[neighborValue]) {
	            dfsRecursive(adj.get(neighborValue), visited);
	        }
	        
	        neighbor = neighbor.getNext();
	    }
	}
	
	public void findSCCs() {
        int n = adj.size();
        boolean[] visited = new boolean[n];
        stack.clear();

        // Pass 1: Fill stack
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                fillOrder(adj.get(i), visited);
            }
        }

        reverseGraph();

        visited = new boolean[n];
        totalSCCs = 0;
        SCCs.clear();
        ArrayList<Integer> tempSizes = new ArrayList<>();

        // Pass 2: Collect SCCs
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                ArrayList<Integer> currentSCCList = new ArrayList<>();
                collectSCC(adj.get(v), visited, currentSCCList);
                
                // Store the SCC as an int array
                int[] sccArray = new int[currentSCCList.size()];
                for(int i = 0; i < currentSCCList.size(); i++) sccArray[i] = currentSCCList.get(i);
                SCCs.add(sccArray);
                
                // Record the size
                tempSizes.add(currentSCCList.size());
                totalSCCs++;
            }
        }

        // Convert tempSizes to your sizesSCC array
        this.sizesSCC = new int[SCCs.size()];
        for (int i = 0; i < SCCs.size(); i++) {
            this.sizesSCC[i] = SCCs.get(i).length;
        }
    }

    private void fillOrder(Node current, boolean[] visited) {
        visited[current.getValue()] = true;
        Node neighbor = current.getNext();
        while (neighbor != null) {
            if (!visited[neighbor.getValue()]) {
                fillOrder(adj.get(neighbor.getValue()), visited);
            }
            neighbor = neighbor.getNext();
        }
        stack.push(current.getValue());
    }

    private void collectSCC(Node current, boolean[] visited, ArrayList<Integer> list) {
        visited[current.getValue()] = true;
        list.add(current.getValue());
        Node neighbor = current.getNext();
        while (neighbor != null) {
            if (!visited[neighbor.getValue()]) {
                collectSCC(adj.get(neighbor.getValue()), visited, list);
            }
            neighbor = neighbor.getNext();
        }
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

        // NEW: User input for k
        System.out.println("Enter the minimum SCC size to display (k):");
        if (scan.hasNextInt()) {
            kRequirement = scan.nextInt();
        }
        scan.nextLine(); // Consume newline

        long startTime = System.nanoTime();
        scan.close(); // Close scanner after all inputs are collected

        DirectedGraph Test1 = new DirectedGraph(1);

        try (BufferedReader reader = new BufferedReader(new FileReader("dataset/dataset " + fileChoice + "/" + sizeChoice + ".txt"))) {
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
        Test1.findSCCs();

        System.out.println("\n--- SCC Search Results (k >= " + kRequirement + ") ---");
        int countMatching = 0;
        
        // Iterate through the results found by the algorithm
        for (int i = 0; i < Test1.SCCs.size(); i++) {
            int[] currentSCC = Test1.SCCs.get(i);
            
            if (currentSCC.length >= kRequirement) {
                countMatching++;
                System.out.print("SCC #" + countMatching + " [Size: " + currentSCC.length + "]: ");
                
                // Print the nodes in this specific SCC
                for (int j = 0; j < currentSCC.length; j++) {
                    System.out.print(currentSCC[j] + (j == currentSCC.length - 1 ? "" : ", "));
                }
                System.out.println();
            }
        }

        System.out.println("---------------------------------");
        System.out.println("Total SCCs found in graph: " + Test1.totalSCCs);
        int max = 0;
        for (int i = 0; i < Test1.sizesSCC.length; i++) {
        	if (Test1.sizesSCC[i] > max) {
        		max = Test1.sizesSCC[i];
        	}
        }
        System.out.println("The Max SCC size is: " + max);
        System.out.println("SCCs meeting size requirement (>= " + kRequirement + "): " + countMatching);
        
        long endTime = System.nanoTime();
        double delta = (endTime - startTime) / 1e6;
        System.out.println("Runtime: " + delta + " ms");

        ArrayList<DirectedGraph.Node> temp = Test1.getAllVertices();
		for (int i = 0; i < temp.size(); i++) {
			DirectedGraph.Node temp2 = temp.get(i);
			while (temp2 != null) {
				System.out.print(temp2.getValue() + " ");
				temp2 = temp2.getNext();
			}
			System.out.println();
		}

        // Run the algorithm
        pageRank pr = new pageRank(Test1);

		for (int i = 0; i < pr.pr2.length; i++) {
			System.out.println("Page Rank of node " + i + " is " + pr.pr2[i]);
		}

		System.out.println("number of iterations: "+pr.iterationsOfPR);

		pr.getTopten();
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