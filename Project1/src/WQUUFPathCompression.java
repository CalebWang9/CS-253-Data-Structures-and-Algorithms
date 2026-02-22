public class WQUUFPathCompression extends WeightedQuickUnion {
    public WQUUFPathCompression(int n) {
        super(n);
    }

    @Override
    public Node getRoot(Node a) {
        Node temp = a;
        arrayAccesses++;
        while (temp.getNext() != temp) {
            temp = temp.getNext();
            arrayAccesses++;
        }

        Node traveler = a;
        arrayAccesses++;
        while (traveler.getNext() != temp) {
            Node parent = traveler.getNext();
            traveler.setNext(temp);
            arrayAccesses+=2;
            traveler = parent;
        }
        return temp;

    }
}