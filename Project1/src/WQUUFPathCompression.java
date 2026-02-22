public class WQUUFPathCompression extends WeightedQuickUnion {
    public WQUUFPathCompression(int n){
        super(n);
    }
    @Override
    public void Union(int a, int b) {
        arrayAccesses += 2;
        Node A = getQuickUnionArray().get(a);
        Node B = getQuickUnionArray().get(b);
        Node rootA = getRoot(A);
        Node rootB = getRoot(B);

        int aHeight = rootA.getHeight();
        int bHeight = rootB.getHeight();
        if (rootA == rootB) {
            compressPath(A,B,rootA);
            revisitConnections++;
            return;
        }

        if (aHeight > bHeight) {
            rootB.setNext(rootA);
        } else if (aHeight < bHeight) {
            rootA.setNext(rootB);
        } else {
            rootB.setNext(rootA);
            rootA.setHeight(rootA.getHeight() + 1);
        }
        arrayAccesses++;
    }

    private void compressPath(Node A,Node B, Node root){
        Node traveler1 = A;
        Node traveler2 = B;

        while (traveler1!=root){
            traveler1.setNext(root);
            traveler1=traveler1.getNext();
        }
        while (traveler2!=root){
            traveler2.setNext(root);
            traveler2=traveler2.getNext();
        }
    }
}