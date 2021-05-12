package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class DijkstraAlgorithmTest {
    public static int NB_GRAPHES = 3;
    private static String[] mapNames = {"/home/morganp/Documents/cartes/carre.mapgr", "/home/morganp/Documents/cartes/insa.mapgr"};

    private static DijkstraAlgorithm dijkstraAlgorithm;
    private static Graph[] graphes;

    @BeforeClass
    public static void initAll() throws IOException {
        graphes = new Graph[NB_GRAPHES];
        GraphReader reader;
        // carte 1
        reader = new BinaryGraphReader(new DataInputStream(new FileInputStream(mapNames[0])));
        graphes[0] = new Graph(mapNames[0], mapNames[0], reader.read().getNodes(), reader.read().getGraphInformation());
        // carte 0
        reader = new BinaryGraphReader(new DataInputStream(new FileInputStream(mapNames[1])));
        graphes[1] = new Graph(mapNames[1], mapNames[1], reader.read().getNodes(), reader.read().getGraphInformation());
    }
}