import java.util.ArrayList;

public class pageRank {
    double rand;
    final double d = 0.85;
    double[] pr1;
    double[] pr2;
    DirectedGraph dGraph;
    ArrayList<DirectedGraph.Node> revGraph;
    int iterationsOfPR = 0;
    ArrayList<Integer> topTen = new ArrayList<>(10);

    public pageRank(DirectedGraph dGraph) {
        int n = dGraph.adj.size();
        rand = (1.0 - d) / n;
        pr1 = new double[n];
        pr2 = new double[n];
        double initValue = 1.0 / n;
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
            iterationsOfPR++;

        } while (deltaChange(pr1, pr2));
    }

    public int getIterations() {
        return iterationsOfPR;
    }

    public void getTopten() {
        int min=0;
        boolean changed = false;
        for (int i = 0; i < pr2.length; i++) {
            if (topTen.size() < 10) {
                topTen.add(i);
            }
            if (changed||topTen.size()==10){
                min = topTen.get(0);
                for(int a:topTen){
                    if(pr2[a]<pr2[min]){
                        min=a;
                    }
                }
                changed=false;
            }
            else if (pr2[i]> pr2[min]){
                topTen.remove(topTen.indexOf(min));
                topTen.add(i);
                changed = true;
            } 
        }

        for(int l:topTen){
            System.out.println("Node: "+l+" | PR: "+pr2[l]+" | INDeg: "+dGraph.getInDegree(dGraph.getAllVertices().get(l)));
        }
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
        influenceGiven = rand + d * influenceGiven;
        return influenceGiven;
    }

    public double sigmaInfluece(int node) {
        // get all the nodes who point to it (get neightbers of the reverese graph)
        // divide each of supporters personal PR values by the amount of nodes the
        // suporter points to
        // add them all up
        double sum = 0;
        DirectedGraph.Node temp = dGraph.getNeighbors(revGraph.get(node));
        int outDeg;
        if (temp == null) {
            return sum;
        }
            while (temp != null) {
                outDeg = dGraph.getOutDegree(temp);
                if (outDeg == 0) {
                    outDeg = dGraph.adj.size();
                }
                sum += (pr1[temp.getValue()] / outDeg);
                temp = temp.getNext();
            }
        
        return sum;

    }
}
