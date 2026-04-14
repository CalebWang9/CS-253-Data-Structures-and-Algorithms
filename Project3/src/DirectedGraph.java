import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Queue;

public class DirectedGraph {
	ArrayList<Node> adj;
	boolean[] exists;

	public DirectedGraph(int Vertexs) {
		adj = new ArrayList<Node>();
		exists = new boolean[Vertexs];
		for (int i = 0; i < Vertexs; i++) {
			adj.add(new Node(i, null));
		}
	}

	public void addEdge(Node u, Node v, double w) {
		exists[u.getValue()] = true;
		exists[v.getValue()] = true;

		Node head = adj.get(u.getValue());
		Node newNode = new Node(v.getValue(), head.getNext());
		head.setNext(newNode);
		newNode.setEdgeWeight(w);
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

	public void BellmanFord(int s) {
		// Initialize distances to infinity
		double[] distances = new double[adj.size()];
		int[] previous = new int[adj.size()];
		boolean[] inQueue = new boolean[adj.size()];
		Arrays.fill(distances, Double.MAX_VALUE);
		Arrays.fill(previous, -1);
		distances[s] = 0;

		// make a queue and enqueue the source node
		Queue<Integer> queue = new ArrayDeque<>();
		queue.add(s);
		inQueue[s] = true;

		// while the queue is not empty, dequeue a node u
		// for each neighbor v of u, if the distance to v is greater than the distance
		// to u + the weight of the edge uv, update the distance to v
		// if v is not in the queue, enqueue it
		while (!queue.isEmpty()) {
			int uId = queue.poll();
			inQueue[uId] = false;
			Node u = adj.get(uId);

			for (Node v = getNeighbors(u); v != null; v = v.getNext()) {
				int vId = v.getValue();
				double w = v.getNextEdgeWeight();
				if (distances[vId] > distances[uId] + w) {
					distances[vId] = distances[uId] + w;
					previous[vId] = uId;
					if (!inQueue[vId]) {
						queue.add(vId);
						inQueue[vId] = true;
					}
				}
			}
		}
		// return the distances in format "1 to 2 (1.65) 1 -> 3 -> 6 -> 0 -> 2"
		// If a travel path has more than 10 vertices, display only the first 5 and the last 5. For example:
		// 0 to 1 (0.71) 0 -> 44 -> 50 -> 20 -> 3 -> ... 10 -> 148 -> 120 -> 7 -> 1
		// for (int i = 0; i < adj.size(); i++) {
		// 	System.out.print("\n" + s + " to " + i + " (" + distances[i] + ") ");
		// 	if (i == s) {
		// 		System.out.print(s);
		// 		continue;
		// 	}
		// 	if (distances[i] == Double.MAX_VALUE || previous[i] == -1) {
		// 		System.out.print("unreachable");
		// 		continue;
		// 	}

		// 	StringBuilder stringBuilder = new StringBuilder();
		// 	int currentNum = i;
		// 	stringBuilder.insert(0, currentNum);
		// 	while (currentNum != s && previous[currentNum] != -1) {
		// 		currentNum = previous[currentNum];
		// 		stringBuilder.insert(0, currentNum + " -> ");
		// 	}
		// 	System.out.print(stringBuilder.toString());
		// }
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String fileChoice;

		boolean invalid2 = true;
		do {
			System.out.println("Please select which dataset: 10000EWD   Rome    tinyEWD");
			fileChoice = scan.nextLine();
			if (fileChoice != null
					&& (fileChoice.equals("10000EWD") || fileChoice.equals("Rome") || fileChoice.equals("tinyEWD"))) {
				invalid2 = false;
			} else {
				System.out.println("Invalid option.");
			}
		} while (invalid2);

		scan.close(); // Close scanner after all inputs are collectedtiny
		long startTimeCon = System.nanoTime();
		DirectedGraph Test1 = new DirectedGraph(1);

		// try (BufferedReader reader = new BufferedReader(new FileReader("Project3/dataset/" + fileChoice + ".txt"))) {
			try (BufferedReader reader = new BufferedReader(new FileReader("user/desktop/largeEWD.txt"))) {

			String line = reader.readLine();
			if (line == null)
				return;
			String[] dimensions = line.trim().split("\\s+");
			int numVertices = Integer.parseInt(dimensions[0]);
			Test1 = new DirectedGraph(numVertices);

			while ((line = reader.readLine()) != null) {
				String[] parts = line.trim().split("\\s+");
				if (parts.length == 3) {
					int u = Integer.parseInt(parts[0]);
					int v = Integer.parseInt(parts[1]);
					double w = Double.parseDouble(parts[2]);
					Test1.addEdge(Test1.new Node(u, null), Test1.new Node(v, null), w);
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}
		long endTimeCon = System.nanoTime();
		double deltaCon = (endTimeCon - startTimeCon) / 1e6;
		System.out.println("\n " + deltaCon);
				
		// Run the algorithm
		System.out.println("Single source from vertex 0");
		long startTime = System.nanoTime();
		Test1.BellmanFord(0);
		long endTime = System.nanoTime();
		double delta = (endTime - startTime) / 1e6;
		System.out.println("\n" + delta );
		
		System.out.println("\nAll pairs");
		long startTimeAll = System.nanoTime();

		for (int i = 0; i < Test1.adj.size(); i++) {
			Test1.BellmanFord(i);
		}
		long endTimeAll = System.nanoTime();
		double deltaAll = (endTimeAll - startTimeAll) / 1e6;
		System.out.println("\n"+ deltaAll);
		

	}

	public class Node {
		private Node next;
		private double edgeWeight;
		private int value;

		public Node(int value, Node next) {
			this.value = value;
			this.next = next;
		}

		public void setNext(Node node) {
			next = node;
		}

		public void setEdgeWeight(double weight) {
			edgeWeight = weight;
		}

		public Node getNext() {
			return next;
		}

		public double getNextEdgeWeight() {
			return edgeWeight;
		}

		public int getValue() {
			return value;
		}
	}

}