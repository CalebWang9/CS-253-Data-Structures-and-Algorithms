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
    ArrayList<Integer> danglingNodes = new ArrayList<>();

    public pageRank(DirectedGraph dGraph) {
        int n = dGraph.adj.size();
        rand = (1.0 - d) / n;
        pr1 = new double[n];
        pr2 = new double[n];
        double initValue = 1.0 / n;
        this.dGraph = dGraph;
        this.revGraph = dGraph.returnReverseGraph();
        identifyDanglingNodes();

        for (int i = 0; i < n; i++) {
            pr2[i] = initValue;
        }

        do {
            //if a node is dangling, then its inlfucence is divied up to all nodes in the coming go through
            // this is to not change up the values of the previous pageranks and put everything out of wack
            pr1 = pr2;
            double dangle = danglingNodeAdjustment();
            pr2 = new double[n];

            for (int i = 0; i < n; i++) {
                pr2[i] = rand + d*(sigmaInfluece(i)+dangle);
            }
            iterationsOfPR++;

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

    public double sigmaInfluece(int node) {
        // get all the nodes who point to it (get neightbers of the reverese graph)
        // divide each of supporters personal PR values by the amount of nodes the
        // suporter points to
        // add them all up
        double sum = 0;
        DirectedGraph.Node temp = (revGraph.get(node).getNext());
        int outDeg;
        if (temp == null) {
            return sum;
        }
            while (temp != null) {
                outDeg = dGraph.getOutDegree(temp);
                sum += (pr1[temp.getValue()] / outDeg);
                temp = temp.getNext();
            }
        
        return sum;

    }

    public int getIterations() {
        return iterationsOfPR;
    }

    public void getTopten() {
        //fills in first 10 then identifies smallest fo them. for each next node can replace smallest
        topTen.clear();
        int min=0;
        boolean changed = false;
        for (int i = 0; i < pr2.length; i++) {
            if (topTen.size() < 10) {
                topTen.add(i);
                if (topTen.size()==10){changed=true;}
            }
            if (changed){
                min = topTen.get(0);
                for(int a:topTen){
                    if(pr2[a]<pr2[min]){
                        min=a;
                    }
                }
                changed=false;
            }
            if (i>=10 && (pr2[i]> pr2[min])){
                topTen.remove(topTen.indexOf(min));
                topTen.add(i);
                changed = true;
            } 
        }

        for(int l:topTen){
            System.out.println("Node: "+l+" | PR: "+pr2[l]+" | INDeg: "+dGraph.getInDegree(dGraph.getAllVertices().get(l)));
        }
    }

    public void identifyDanglingNodes(){
        for(int i =0; i<pr1.length;i++){
            if (dGraph.getOutDegree(dGraph.getAllVertices().get(i))==0){
                this.danglingNodes.add(i);
            }
        }
    }

    public double danglingNodeAdjustment(){
        double dangle=0;
        for (int i:danglingNodes){
            dangle+=pr1[i]/dGraph.adj.size();
        }
        return dangle;
    }
}
