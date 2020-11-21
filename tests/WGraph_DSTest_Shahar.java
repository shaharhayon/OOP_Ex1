import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest_Shahar {


    weighted_graph g;
    static final int VERTICES = 10, EDGES = 15, JUMP = 2;

    weighted_graph createGraph(int v, int e, int jump) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < v - 1; i++) {
            for (int j = 1; j < jump + 1; j++) {
                g.connect(i, i + j, j);
            }
        }
        return g;
    }

    @BeforeEach
    void createGraph() {
        g = createGraph(VERTICES, EDGES, JUMP);
    }



    @Test
    void getNode() {
    }

    @Test
    void testToString() {
    }

    @Test
    void hasEdge() {
        assertTrue(g.hasEdge(0,1));
    }

    @Test
    void getEdge() {
    }

    @Test
    void addNode() {
    }

    @Test
    void connect() {
    }

    @Test
    void getV() {
    }

    @Test
    void testGetV() {
    }

    @Test
    void removeNode() {
    }

    @Test
    void removeEdge() {
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }
}