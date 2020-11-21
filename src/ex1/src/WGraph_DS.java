package ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph, Serializable {
    private int id = 0, MC = 0, edges = 0;
    private final HashMap<Integer, node_info> nodes_list = new HashMap<>(); // list of all the nodes in the graph
    private final HashMap<Integer, HashMap<Integer, Double>> neighbors = new HashMap<>(); // list of all the neighbors in the graph


    // class that represents the type node in a graph
    private class NodeData implements node_info, Serializable {
        int key;
        double tag;
        String info;

        /*
        Constructor
         */
        public NodeData() {
            this.key = id;
            id++;
            this.tag = 0;
            this.info = "";
        }

        /*
        Copy Constructor
         */
        public NodeData(NodeData node) {
            this.key = node.getKey();
            this.tag = node.getTag();
            this.info = node.getInfo();
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public String toString() {
            return "Node " + key + "[ " +
                    "tag = " + tag +
                    ", info = " + info +
                    "]\t";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NodeData)) return false;
            NodeData nodeData = (NodeData) o;
            return this.key == nodeData.key &&
                    Double.compare(nodeData.tag, tag) == 0 &&
                    this.info.equals(nodeData.info);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = key;
            temp = Double.doubleToLongBits(tag);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + info.hashCode();
            return result;
        }
    }


    @Override
    public node_info getNode(int key) {
        return this.nodes_list.get(key);
    }


    @Override
    public String toString() {
        String s = "WGraph_DS: Nodes #: " + nodeSize() + ", MC: " + MC + ", Edges: " + edges + "\n";
        for (node_info node : getV()) {
            s += node + " Neighbors: {";
            for (node_info neighbor : getV(node.getKey()))
                s += " Node #: " + neighbor.getKey() + " Weight: " + getEdge(node.getKey(), neighbor.getKey()) + ",";
            s += "}\n";
            //+ neighbors.get(node.getKey()).keySet() + "}\n";

        }
        return s;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (getNode(node1) == getNode(node2))
            return true;
        if (getV(node1).contains(getNode(node2)))
            return true;
        else
            return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return this.neighbors.get(node1).get(node2);
        else
            return -1;
    }

    /*
    checks if the node already exists, if true -> do nothing
    if false -> adds the node to the graph's nodes list and create an entry for it in neighbors list
     */
    @Override
    public void addNode(int key) {
        if (getV().contains(getNode(key)))
            return;
        this.nodes_list.put(key, new NodeData());
        this.neighbors.put(key, new HashMap<>());
        MC++;
    }

    /*
    if either node are null (doesnt exist on the graph) -> do nothing
    else add each node to the others neighbors list
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (this.nodes_list.get(node1) == null ||
                this.nodes_list.get(node2) == null ||
                hasEdge(node1, node2)) {
            return;
        }
        this.neighbors.get(node1).put(node2, w);
        this.neighbors.get(node2).put(node1, w);
        edges++;
        MC++;
    }

    @Override
    public Collection<node_info> getV() {
        return this.nodes_list.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        ArrayList<node_info> list = new ArrayList<>();
        for (int nodeKey : this.neighbors.get(node_id).keySet())
            list.add(getNode(nodeKey));
        return list;
    }

    /*
    if the node doesnt exist -> return null as nothing was removed
    else iterates through all the nodes in the graph, and remove the selected node from all neighbors list's
    then removes the node from the graph's nodes list and returns the removed node
     */
    @Override
    public node_info removeNode(int key) {
        if (getNode(key) == null)
            return null;

        List<Integer> toRemove = new ArrayList<>();
        node_info removedNode = getNode(key);
        for (node_info n : getV(key)) {
            toRemove.add(n.getKey());
            //
        }
        for (int i : toRemove)
            removeEdge(i, key);
        this.nodes_list.remove(key);
        return removedNode;
    }

    /*
    if the edge doesnt exist -> do nothing
    else remove the edge
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            this.neighbors.get(node1).remove(node2);
            this.neighbors.get(node2).remove(node1);
            edges--;
            MC++;
        }
    }

    @Override
    public int nodeSize() {
        return nodes_list.size();
    }

    @Override
    public int edgeSize() {
        return edges;
    }

    @Override
    public int getMC() {
        return this.MC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_DS)) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return edges == wGraph_ds.edges &&
                Objects.equals(nodes_list, wGraph_ds.nodes_list) &&
                Objects.equals(neighbors, wGraph_ds.neighbors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, MC, edges, nodes_list, neighbors);
    }
}
