import java.util.HashMap;
import java.util.Map;

public class WQUUFPathCompression extends WeightedQuickUnion {
    public WQUUFPathCompression(int n) {
        super(n);
    }

    @Override
    public Node getRoot(Node a) {
        Node temp = a;
        while (true) {
            arrayAccesses++; // count getNext
            Node next = temp.getNext();
            if (next == temp)
                break;
            temp = next;
        }

        Node traveler = a;
        while (true) {
            arrayAccesses++; // getNext
            Node parent = traveler.getNext();
            if (parent == temp)
                break;
            traveler.setNext(temp);
            arrayAccesses++; // setNext
            traveler = parent;
        }
        return temp;
    }

    
    @Override
    public int getMaxTreeHeight() {
        Map<Node, Integer> maxDepthUnderRoot = new HashMap<>();
        for (Node node : QuickUnionArray) {
            int depth = 0;
            Node cur = node;
            while (cur.getNext() != cur) {
                depth++;
                cur = cur.getNext();
            }
            Node root = cur;
            maxDepthUnderRoot.merge(root, depth, Math::max);
        }
        return maxDepthUnderRoot.values().stream().mapToInt(Integer::intValue).max().orElse(0);

    }

}