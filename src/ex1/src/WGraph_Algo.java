package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    static weighted_graph G;

    public WGraph_Algo(weighted_graph g) {
        init(g);
    }

    public WGraph_Algo() {

    }

    @Override
    public void init(weighted_graph g) {
        G = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.G;
    }


    /*
    copy method is implemented using the same technique as save and load methods (Serialization).
    instead of writing to a file, we write to a byte array in the memory (which is faster),
    and then build the object (graph) again, resulting in a deep copy.

    implementation was referenced in StackOverflow.
     */
    @Override
    public weighted_graph copy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(G);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
            return (weighted_graph)(new ObjectInputStream(bis).readObject());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isConnected() {
        // empty graph is connected.
        if (G.getV().isEmpty())
            return true;

        bfs();
        // use BFS algorithm to check graph connectivity
        for (node_info node : G.getV())
            if (node.getTag() == Double.MAX_VALUE)
                return false;
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if(shortestPath(src,dest) == null)
            return -1;
        return G.getNode(dest).getTag();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        initDijkstra();

        G.getNode(src).setTag(0);
        HashMap<node_info, node_info> parent = new HashMap<>();
        for (node_info node : G.getV()) {
            for (node_info neighbor : G.getV(node.getKey())) {
                if (neighbor.getInfo().equals("Visited"))
                    continue;
                double edgeWeight = G.getEdge(node.getKey(), neighbor.getKey());
                double nodeWeight = node.getTag();
                if (nodeWeight + edgeWeight < neighbor.getTag()) {
                    neighbor.setTag(nodeWeight + edgeWeight);
                    parent.put(neighbor, node);
                }
            }
            node.setInfo("Visited");
            if (G.getNode(dest).getInfo().equals("Visited"))
                break;
        }

        if (!G.getNode(dest).getInfo().equals("Visited"))
            return null;

        List<node_info> shortestPath = new ArrayList<>();
        node_info node = G.getNode(dest);
        shortestPath.add(node);
        while (node != G.getNode(src)) {
            node = parent.get(node);
            shortestPath.add(node);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream myGraph = new FileOutputStream(file);
            ObjectOutputStream gos = new ObjectOutputStream(myGraph);
            gos.writeObject(G);
            gos.close();
            myGraph.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try {
            FileInputStream myGraph = new FileInputStream(file);
            ObjectInputStream gis = new ObjectInputStream(myGraph);
            init((weighted_graph) gis.readObject());
            gis.close();
            myGraph.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initDijkstra() {
        for (node_info n : G.getV()) {
            n.setTag(Double.POSITIVE_INFINITY);
            n.setInfo("");
        }
    }

    /*private List<node_info> Dijkstra(node_info src, node_info dest) {
        src.setTag(0);
        HashMap<node_info, node_info> parent = new HashMap<node_info, node_info>();
        for (node_info node : G.getV()) {
            for (node_info neighbor : G.getV(node.getKey())) {
                if (neighbor.getInfo() == "Visited")
                    continue;
                double edgeWeight = G.getEdge(node.getKey(), neighbor.getKey());
                double nodeWeight = node.getTag();
                if (nodeWeight + edgeWeight < neighbor.getTag()) {
                    neighbor.setTag(nodeWeight + edgeWeight);
                    parent.put(neighbor, node);
                }
            }
            node.setInfo("Visited");
            if (dest.getInfo() == "Visited")
                break;
        }

        List<node_info> shortestPath = new ArrayList<node_info>();
        node_info node = dest;
        shortestPath.add(node);
        while (node != src) {
            node = parent.get(node);
            shortestPath.add(node);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }*/

    private void initBFS() {
        // initialize tags on all nodes to MAX_VALUE
        for (node_info n : G.getV())
            n.setTag(Double.MAX_VALUE);
    }

    private void bfs() {
        /*
        this function implements the BFS algorithm on the entire graph (without a destination)
        it is used as a way to check if the graph is connected
         */
        Queue<node_info> q = new LinkedList<>();
        int level = 0;
        initBFS();

        // find random node in the graph
        int i = 0;
        while (G.getNode(i) == null)
            i++;
        node_info currentNode = G.getNode(i);
        currentNode.setTag(0);

        while (currentNode != null) {
            level++;
            for (node_info neighbor : G.getV(currentNode.getKey())) {
                if (neighbor.getTag() == Double.MAX_VALUE) {
                    q.add(neighbor);
                    if (neighbor.getTag() > level)
                        neighbor.setTag(level);
                }
            }
            currentNode = q.poll();
        }
    }

}
