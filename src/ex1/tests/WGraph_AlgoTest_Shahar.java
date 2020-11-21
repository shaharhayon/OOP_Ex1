package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;

class WGraph_AlgoTest_Shahar {

    weighted_graph g;
    weighted_graph_algorithms wga;
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
    void createAlgo() {
        wga = new WGraph_Algo(g);
    }

    @BeforeEach
    void createGraph() {
        g = createGraph(VERTICES, EDGES, JUMP);
    }


    @Test
    void init() {
        Assertions.assertAll(
                () -> {
                    wga.init(g);
                    Assertions.assertSame(wga.getGraph(), g, "Initiated graph should be g");
                },

                () -> {
                    wga.init(null);
                    Assertions.assertNull(wga.getGraph(), "Initiated graph should be null");
                }
        );

    }

    @Test
    void getGraph() {
        Assertions.assertTrue(g.equals(wga.getGraph()));
    }

    @Test
    void copy() {

        Assertions.assertTrue(g.equals(wga.copy()));
    }

    @Test
    void isConnected() {
        wga.init(createGraph(1000, 1000, 1));
        Assertions.assertAll(
                () -> Assertions.assertTrue(wga.isConnected(), "Graph should be connected"),
                () -> {
                    wga.getGraph().removeEdge(500, 501);
                    System.out.println(wga.getGraph().hasEdge(500,501));
                    System.out.println(wga.getGraph().hasEdge(501,502));
                    Assertions.assertFalse(wga.isConnected(), "Graph should not be connected");
                }
        );
    }

    @Test
    void shortestPathDist() {
        wga.init(createGraph(10, 10, 2));
        Assertions.assertEquals(9, wga.shortestPathDist(0, 9));

    }

    @Test
    void shortestPath() {
        wga.init(createGraph(10, 10, 2));
        List<node_info> list = new ArrayList<>();
        list.add(wga.getGraph().getNode(0));
        list.add(wga.getGraph().getNode(1));
        list.add(wga.getGraph().getNode(3));
        list.add(wga.getGraph().getNode(5));
        list.add(wga.getGraph().getNode(7));
        list.add(wga.getGraph().getNode(9));
        Assertions.assertEquals(list, wga.shortestPath(0, 9));
    }

    @Test
    void save() {
        Assertions.assertTrue(wga.save("saveTest.ser"));
    }

    @Test
    void load() {
        Assertions.assertAll(
                () -> wga.load("saveTest.ser"),
                () -> Assertions.assertEquals(wga.getGraph(), g)
        );
    }
}