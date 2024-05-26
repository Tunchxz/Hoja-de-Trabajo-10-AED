package uvg.edu.gt;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la clase Grafo.
 */
public class GrafoTest {
    private Grafo grafo;

    /**
     * Configuración inicial para las pruebas.
     * Se crea un grafo con 6 ciudades y se agregan conexiones entre ellas.
     */
    @Before
    public void setUp() {
        grafo = new Grafo(6);
        grafo.addEdge("CiudadA", "CiudadB", 10);
        grafo.addEdge("CiudadB", "CiudadC", 20);
        grafo.addEdge("CiudadC", "CiudadD", 15);
        grafo.addEdge("CiudadD", "CiudadE", 30);
        grafo.addEdge("CiudadE", "CiudadA", 40);
        grafo.floydWarshall(); // Se ejecuta el algoritmo de Floyd-Warshall para preparar el grafo
    }

    /**
     * Prueba unitaria para verificar la indexación de ciudades en el grafo.
     */
    @Test
    public void testAddEdgeAndCities() {
        assertEquals(0, grafo.getCityIndex("CiudadA"));
        assertEquals(1, grafo.getCityIndex("CiudadB"));
        assertEquals(2, grafo.getCityIndex("CiudadC"));
        assertEquals(3, grafo.getCityIndex("CiudadD"));
        assertEquals(4, grafo.getCityIndex("CiudadE"));
    }

    /**
     * Prueba unitaria para verificar los caminos más cortos entre ciudades.
     */
    @Test
    public void testShortestPath() {
        List<String> path = grafo.getPath("CiudadA", "CiudadC");
        assertEquals(3, path.size());
        assertEquals("CiudadA", path.get(0));
        assertEquals("CiudadB", path.get(1));
        assertEquals(30, grafo.getDistance("CiudadA", "CiudadC"));

        path = grafo.getPath("CiudadA", "CiudadE");
        assertEquals(5, path.size());
        assertEquals("CiudadA", path.get(0));
        assertEquals("CiudadB", path.get(1));
        assertEquals("CiudadC", path.get(2));
        assertEquals("CiudadD", path.get(3));
        assertEquals(75, grafo.getDistance("CiudadA", "CiudadE"));
    }

    /**
     * Prueba unitaria para verificar el cálculo del centro del grafo.
     */
    @Test
    public void testGetCenter() {
        String center = grafo.getCenter();
        assertEquals("CiudadA", center);
    }

    /**
     * Prueba unitaria para verificar el manejo de ciudades sin conexión directa.
     */
    @Test
    public void testNoPath() {
        grafo.addEdge("CiudadE", "CiudadF", Integer.MAX_VALUE / 2);
        int distance = grafo.getDistance("CiudadA", "CiudadF");
        assertEquals(Integer.MAX_VALUE / 2, distance);

        List<String> path = grafo.getPath("CiudadA", "CiudadF");
        assertTrue(path.isEmpty());
    }
}