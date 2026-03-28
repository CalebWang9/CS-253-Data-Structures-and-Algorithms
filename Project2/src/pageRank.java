import java.util.ArrayList;

public class pageRank {
    double rand;
    final double d = 0.85;
    double[] pr1;
    double[] pr2;
    DirectedGraph dGraph;
    ArrayList<DirectedGraph.Node> revGraph;

    public pageRank(DirectedGraph dGraph) {
        int n = dGraph.adj.size();
        rand = (1 - d) / n;
        pr1 = new double[n];
        pr2 = new double[n];
        double initValue = 1 / n;
        this.dGraph = dGraph;
        this.revGraph = dGraph.returnReverseGraph();

        for (int i = 0; i < n; i++) {
            pr2[i] = initValue;
        }

        do {
            pr1 = pr2;
            pr2 = new double[n];
            for (int i = 0; i < n; i++) {
                pr2[i] = pageRankOfNode(i);
            }

        } while (deltaChange(pr1, pr2));
    }

    public boolean deltaChange(double[] pr1, double[] pr2) {
        // checks change beween old and new values
        double change = 0;
        for (int i = 0; i < pr1.length; i++) {
            change += Math.abs(pr2[i] - pr1[i]);
        }
        return ((change) >= 0.000001);
    }

    public double pageRankOfNode(int node) {
        // returns the new PR of the node
        double influenceGiven = sigmaInfluece(node);
        return rand + d * influenceGiven;
    }

    public double sigmaInfluece(int node) {
        // get all the nodes who point to it (get neightbers of the reverese graph)
        // divide each of supporters personal PR values by the amount of nodes the
        // suporter points to
        // add them all up
        double sum = 0;
        DirectedGraph.Node temp = dGraph.getNeighbors(revGraph.get(node));
        int outDeg;
        if (temp != null) {
            while (temp.getNext() != null) {
                outDeg = dGraph.getOutDegree(temp);
                if (outDeg == 0) {
                    outDeg = dGraph.adj.size();
                }
                sum += (pr1[temp.getValue()] / outDeg);
                temp = temp.getNext();
            }
        }
        return sum;

    }
}
