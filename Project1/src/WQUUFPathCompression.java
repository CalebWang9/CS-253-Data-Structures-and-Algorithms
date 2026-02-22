public class WQUUFPathCompression extends WeightedQuickUnion {
    public WQUUFPathCompression(int n) {
        super(n);
    }

    @Override
    public Node getRoot(Node a) {
        Node temp = a;
        while (true) {
            arrayAccesses++;  // count getNext
            Node next = temp.getNext();
            if (next == temp) break;
            temp = next;
        }

        Node traveler = a;
        while (true) {
            arrayAccesses++;  // getNext
            Node parent = traveler.getNext();
            if (parent == temp) break;
            traveler.setNext(temp);
            arrayAccesses++;  // setNext
            traveler = parent;
        }
        return temp;
    }
}