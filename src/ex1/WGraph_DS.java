package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph, Serializable {
    int id = 0, MC = 0, edges = 0;
    private HashMap<Integer, node_info> nodes_list = new HashMap<Integer, node_info>();
    private HashMap<Integer, HashMap<node_info, Double>> neighbors = new HashMap<Integer, HashMap<node_info, Double>>();


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
            this.info = new String();
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
            return this.key % 50;
        }
    }


    @Override
    public node_info getNode(int key) {
        return this.nodes_list.get(key);
    }


    @Override
    public String toString() {
        String s = "WGraph_DS: Nodes #: " + nodeSize() + ", MC: " + MC + ", Edges: " + edges + "\n";
        for (node_info node : nodes_list.values()) {
            s += node + " Neighbors: {";
            for (node_info neighbor : neighbors.get(node.getKey()).keySet())
                s += " Node #: " + neighbor.getKey() + " Weight: " + getEdge(node.getKey(), neighbor.getKey()) + ",";
            s += "}\n";
            //+ neighbors.get(node.getKey()).keySet() + "}\n";

        }
        return s;
    }

    // checks if node2 is on the neighbor list of node1.
    // if true returns true, else returns false.
    @Override
    public boolean hasEdge(int node1, int node2) {
        node_info nodeB = nodes_list.get(node2);
        if (this.neighbors.get(node1).containsKey(nodeB))
            return true;
        else
            return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return this.neighbors.get(node1).get(getNode(node2));
        else
            return -1;
    }

    @Override
    public void addNode(int key) {
        if (this.nodes_list.containsKey(key))
            return;
        this.nodes_list.put(key, new NodeData());
        this.neighbors.put(key, new HashMap<node_info, Double>());
        MC++;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (this.nodes_list.get(node1) == null || this.nodes_list.get(node2) == null) {
            System.out.println("One of the nodes does not exist.");
            return;
        }
        this.neighbors.get(node1).put(getNode(node2), w);
        this.neighbors.get(node2).put(getNode(node1), w);
        edges++;
        MC++;
    }

    @Override
    public Collection<node_info> getV() {
        return this.nodes_list.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        return this.neighbors.get(node_id).keySet();
    }

    @Override
    public node_info removeNode(int key) {
        if (getNode(key) == null)
            return null;

        node_info removedNode = getNode(key);
        for (node_info n : this.neighbors.get(removedNode).keySet()) {
            removeEdge(n.getKey(), key);
        }
        this.nodes_list.remove(key);
        return removedNode;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            this.neighbors.get(node1).remove(getNode(node2));
            this.neighbors.get(node2).remove(getNode(node1));
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
