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
            rootB.setNext(rootA);
            rootA.setHeight(Math.max(rootA.getHeight(), rootB.getHeight() + 1));
            QuickArray[rootA.getValue()] = rootB.getValue();//we stupid
            arrayAccesses++;
        } else if (rootA.getHeight() < rootB.getHeight()) {
            rootA.setNext(rootB);
            rootB.setHeight(Math.max(rootB.getHeight(), rootA.getHeight() + 1));
            QuickArray[rootB.getValue()] = rootA.getValue();//we stupid
            arrayAccesses++;
        } else {
            rootA.setNext(rootB);
            rootB.setHeight(Math.max(rootB.getHeight(), rootA.getHeight() + 1));
            QuickArray[rootB.getValue()] = rootA.getValue();//we stupid
            arrayAccesses++;
        }
        arrayAccesses++;
    
    }
}
