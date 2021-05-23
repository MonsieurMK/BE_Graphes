package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShortestPathAlgorithmTest {
    public static int NB_GRAPHES = 3;
    private static String[] mapNames = {"/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/cartes/carre.mapgr",
            "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/cartes/insa.mapgr",
            "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/cartes/toulouse.mapgr"}; // path doit être changé selon la machine et l'utilisation

    private static Graph[] graphes; // 0 : carre, 1 : insa, 2 : toulouse

    private static ArrayList<ArcInspector> arcInspectors;

     enum Algorithme {
        BELLMAN_FORD, DIJKSTRA, A_STAR, DIJKSTRA_V;
    }

    private static Algorithme algorithme;

    @BeforeClass
    public static void initAll() throws IOException {
        // initialisation des graphes
        graphes = new Graph[NB_GRAPHES];
        GraphReader reader;
        for (int i = 0; i < NB_GRAPHES; i++) {
            reader = new BinaryGraphReader(new DataInputStream(new FileInputStream(mapNames[i])));
            graphes[i] = reader.read();
        }

        // initialisation ArcInspectors
        ArcInspectorFactory arcInspectorFactory = new ArcInspectorFactory();
        arcInspectors = (ArrayList) arcInspectorFactory.getAllFilters();

        algorithme = Algorithme.DIJKSTRA; // algorithm à tester
    }

    @Test
    public void testDijkstraCarreDiagonaleSansFiltre() throws IOException {
        // obtention d'un fichier .path généré avec Bellman-Ford
        PathReader reader = new BinaryPathReader(new DataInputStream(new FileInputStream(
                "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/paths_test/carree_diagonale_sans_filtre_assert.path")));
        Path path = reader.readPath(graphes[0]);

        // algorithme dijkstra
        ShortestPathData data = new ShortestPathData(graphes[0], path.getOrigin(), path.getDestination(), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        // equals redifined in Path and Arc
        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid()); // est valide
        assertEquals(path.getOrigin(), solution.getPath().getOrigin());
        assertEquals(path.getDestination(), solution.getPath().getDestination());
        assertEquals(path, solution.getPath());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0.0D); // comparaison du cout avec cout de path
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0.0D);
    }

    @Test
    public void testDijkstraCarreLongueurNulle() {
        // algorithme dijkstra
        ShortestPathData data = new ShortestPathData(graphes[0], graphes[0].getNodes().get(0), graphes[0].getNodes().get(0), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        assertFalse(solution.isFeasible());
    }

    @Test
    public void testDijkstraINSASansFiltre1() throws IOException {
        // obtention d'un fichier .path généré avec Bellman-Ford
        PathReader reader = new BinaryPathReader(new DataInputStream(new FileInputStream(
                "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/paths_test/insa_sans_filtre_1.path")));
        Path path = reader.readPath(graphes[1]);

        // algorithme dijkstra
        ShortestPathData data = new ShortestPathData(graphes[1], path.getOrigin(), path.getDestination(), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        // equals redifined in Path and Arc
        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid());
        assertEquals(path.getOrigin(), solution.getPath().getOrigin());
        assertEquals(path.getDestination(), solution.getPath().getDestination());
        assertEquals(path, solution.getPath());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0.0D);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0.0D);
    }

    @Test
    public void testDijkstraINSASansFiltre2() throws IOException {
        // obtention d'un fichier .path généré avec Bellman-Ford
        PathReader reader = new BinaryPathReader(new DataInputStream(new FileInputStream(
                "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/paths_test/insa_sans_filtre_2.path")));
        Path path = reader.readPath(graphes[1]);

        // algorithme dijkstra
        ShortestPathData data = new ShortestPathData(graphes[1], path.getOrigin(), path.getDestination(), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        // equals redifined in Path and Arc
        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid());
        assertEquals(path.getOrigin(), solution.getPath().getOrigin());
        assertEquals(path.getDestination(), solution.getPath().getDestination());
        assertEquals(path, solution.getPath());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0.0D);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0.0D);
    }

    @Test
    public void testDijkstraINSASansFiltre3() throws IOException {
        // obtention d'un fichier .path généré avec Bellman-Ford
        PathReader reader = new BinaryPathReader(new DataInputStream(new FileInputStream(
                "/home/morganp/IdeaProjects/BE_Graphes/src/main/resources/paths_test/insa_sans_filtre_3.path")));
        Path path = reader.readPath(graphes[1]);

        // algorithme dijkstra
        ShortestPathData data = new ShortestPathData(graphes[1], path.getOrigin(), path.getDestination(), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        // equals redifined in Path and Arc
        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid());
        assertEquals(path.getOrigin(), solution.getPath().getOrigin());
        assertEquals(path.getDestination(), solution.getPath().getDestination());
        assertEquals(path, solution.getPath());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0.0D);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0.0D);
    }

    @Test
    public void testDijkstraINSALongueurNulle() {
        ShortestPathData data = new ShortestPathData(graphes[1], graphes[1].getNodes().get(5), graphes[1].getNodes().get(5), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        assertFalse(solution.isFeasible());

        data = new ShortestPathData(graphes[1], graphes[1].getNodes().get(15), graphes[1].getNodes().get(15), arcInspectors.get(0));
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        solution = algorithm.doRun();

        assertFalse(solution.isFeasible());

        data = new ShortestPathData(graphes[1], graphes[1].getNodes().get(105), graphes[1].getNodes().get(105), arcInspectors.get(0));
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        solution = algorithm.doRun();

        assertFalse(solution.isFeasible());
    }

    @Test
    public void testDijkstraINSABikiniInfaisable() {
        // ce chemin est censé emprunter le canal du midi, en autorisant uniquement les routes accessibles en voiture
        ShortestPathData data = new ShortestPathData(graphes[1], graphes[1].getNodes().get(261), graphes[1].getNodes().get(985), arcInspectors.get(1));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        assertFalse(solution.isFeasible());
    }

    // pour les tests sur les grandes cartes, on peut vérifier l'ordre de grandeur de la distance
    // et tester si les chemins sont valides
    @Test
    public void testDijkstraToulouseSansFiltre() {
        ShortestPathData data = new ShortestPathData(graphes[2], graphes[2].getNodes().get(14109), graphes[2].getNodes().get(5948), arcInspectors.get(0));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractInputData.Mode.LENGTH, data.getMode());
        assertEquals(7300.0D, solution.getPath().getLength(), 500.0D); // la distance de ce trajet devrait faire environ 7,3 km selon d'autres services
                                                                              // de cartographie (Google Maps notamment), on accorde une incertitude de 500m
    }

    // cette fois on teste un autre trajet, en se limitant aux routes pour voitures
    // c'est un trajet entre les stations de métro Capitole et Empalot
    @Test
    public void testDijkstraToulouseFiltreVoitures() {
        ShortestPathData data = new ShortestPathData(graphes[2], graphes[2].getNodes().get(1003), graphes[2].getNodes().get(14021), arcInspectors.get(1));
        ShortestPathAlgorithm algorithm;
        switch (algorithme) {
            case BELLMAN_FORD:
                algorithm = new BellmanFordAlgorithm(data);
                break;
            case DIJKSTRA:
                algorithm = new DijkstraAlgorithm(data);
                break;
            case A_STAR:
                algorithm = new AStarAlgorithm(data);
                break;
            default:
                algorithm = new DijkstraVeloAlgorithm(data);
                break;
        }
        ShortestPathSolution solution = algorithm.doRun();

        assertTrue(solution.isFeasible());
        assertTrue(solution.getPath().isValid());
        assertEquals(3700.0D, solution.getPath().getLength(), 500.0D); // cette fois la distance indiquée est de 8,2 km
    }
}