public class WeightedQuickUnion extends QuickUnion {
    public WeightedQuickUnion(int n) {
        super(n);
    }

    @Override
    public void Union(int a, int b) {
        arrayAccesses += 2;
        Node A = getQuickUnionArray().get(a);
        Node B = getQuickUnionArray().get(b);
        Node rootA = getRoot(A);
        Node rootB = getRoot(B);

        if (rootA == rootB) {
            revisitConnections++;
            return;
        }

        if (rootA.getHeight() > rootB.getHeight()) {
            rootB.setNext(A.getNext());
        } else if (rootA.getHeight() < rootB.getHeight()) {
            rootA.setNext(B.getNext());
        } else {
            rootB.setNext(A.getNext());
            rootA.setHeight(rootA.getHeight() + 1);
        }
        arrayAccesses++;
    }
}
