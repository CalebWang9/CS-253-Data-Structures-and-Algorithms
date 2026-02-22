import java.util.ArrayList;

public class QuickUnion {
	ArrayList<Node> QuickUnionArray;

	long arrayAccesses = 0;
	long revisitConnections = 0;

	public QuickUnion(int n) {
		QuickUnionArray = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			Node temp = new Node(i, null);
			temp.setNext(temp);
			QuickUnionArray.add(temp);
		}

	}

	public ArrayList<Node> getQuickUnionArray() {
		return QuickUnionArray;
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
		arrayAccesses++;  // get(a)
		Node rootA = getRoot(temp);
		arrayAccesses++;  // get(b) for getRoot argument
		Node rootB = getRoot(QuickUnionArray.get(b));
		rootA.setNext(rootB);
		arrayAccesses++;  // setNext

		rootB.setHeight(Math.max(rootB.getHeight(), rootA.getHeight() + 1));
	}

	public Node getRoot(Node a) {
		Node temp = a;
		while (true) {
			arrayAccesses++;  // count each getNext (including condition read)
			Node next = temp.getNext();
			if (next == temp) break;
			temp = next;
		}
		return temp;
	}

	/** Max stored height among roots (rank in weighted variants). */
	public int getMaxTreeHeight() {
		int max = 0;
		for (Node n : QuickUnionArray) {
			if (n.getNext() == n) { // only check roots
				if (n.getHeight() > max)
					max = n.getHeight();
			}
		}
		return max;
	}

	protected class Node {
		private Node next;
		private int value;
		private int height;

		public Node(int value, Node next) {
			this.value = value;
			this.next = next;
			this.height = 0;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int h) {
			height = h;
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
