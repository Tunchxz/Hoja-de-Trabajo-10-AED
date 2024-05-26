package uvg.edu.gt;

/**
 * Clase que proporciona los métodos y lógica para manipular el grafo principal de Ciudades.
 * 
 * @author Cristian Túnchez
 * @version 1.0
 * @since 26/05/2024
 */
import java.util.*;

/**
 * Clase que representa un grafo ponderado de ciudades y distancias entre ellas.
 */
public class Grafo {
    private int[][] adjMatrix; // Matriz de adyacencia para almacenar las distancias entre ciudades
    private int[][] next; // Matriz de predecesores para reconstruir caminos
    private int numVertices; // Número de vértices (ciudades) en el grafo
    private Map<String, Integer> cityToIndex; // Mapa para mapear el nombre de la ciudad a su índice en la matriz
    private Map<Integer, String> indexToCity; // Mapa para mapear el índice de la ciudad a su nombre

    /**
     * Constructor para inicializar el grafo con el número dado de vértices.
     * 
     * @param numVertices Número de ciudades (vértices) en el grafo.
     */
    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new int[numVertices][numVertices];
        next = new int[numVertices][numVertices];
        cityToIndex = new HashMap<>();
        indexToCity = new HashMap<>();

        // Inicializa la matriz de adyacencia y la matriz de predecesores
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    adjMatrix[i][j] = 0; // La distancia de un nodo a sí mismo es 0
                } else {
                    adjMatrix[i][j] = Integer.MAX_VALUE / 2; // Inicializa con un valor alto para representar infinito
                }
                next[i][j] = -1; // Inicializa la matriz de predecesores con -1
            }
        }
    }

    /**
     * Obtiene el índice de una ciudad en el grafo.
     * 
     * @param city Nombre de la ciudad.
     * @return Índice de la ciudad si está presente, -1 si no está en el grafo.
     */
    public int getCityIndex(String city) {
        return cityToIndex.getOrDefault(city, -1);
    }

    /**
     * Agrega una ciudad al grafo si no está presente.
     * 
     * @param city Nombre de la ciudad a agregar.
     */
    public void addCity(String city) {
        if (!cityToIndex.containsKey(city)) {
            int index = cityToIndex.size();
            cityToIndex.put(city, index);
            indexToCity.put(index, city);
        }
    }

    /**
     * Agrega una arista (con peso) entre dos ciudades en el grafo.
     * 
     * @param fromCity Ciudad de origen.
     * @param toCity   Ciudad de destino.
     * @param weight   Peso o distancia entre las ciudades.
     */
    public void addEdge(String fromCity, String toCity, int weight) {
        addCity(fromCity);
        addCity(toCity);
        int from = cityToIndex.get(fromCity);
        int to = cityToIndex.get(toCity);
        adjMatrix[from][to] = weight;
        next[from][to] = to; // Establece el sucesor directo de from a to como to
    }

    /**
     * Remueve una arista entre dos ciudades en el grafo.
     * 
     * @param fromCity Ciudad de origen.
     * @param toCity   Ciudad de destino.
     */
    public void removeEdge(String fromCity, String toCity) {
        int fromIndex = getCityIndex(fromCity);
        int toIndex = getCityIndex(toCity);
        if (fromIndex != -1 && toIndex != -1) {
            adjMatrix[fromIndex][toIndex] = Integer.MAX_VALUE / 2; // "Elimina" la arista estableciendo la distancia a
                                                                   // infinito
        }
    }

    /**
     * Algoritmo de Floyd-Warshall para encontrar caminos más cortos entre todas las
     * parejas de nodos.
     */
    public void floydWarshall() {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (adjMatrix[i][k] != Integer.MAX_VALUE / 2 && adjMatrix[k][j] != Integer.MAX_VALUE / 2) {
                        if (adjMatrix[i][j] > adjMatrix[i][k] + adjMatrix[k][j]) {
                            adjMatrix[i][j] = adjMatrix[i][k] + adjMatrix[k][j];
                            next[i][j] = next[i][k]; // Actualiza el sucesor de i a j a través de k
                        }
                    }
                }
            }
        }
    }

    /**
     * Obtiene el camino más corto entre dos ciudades.
     * 
     * @param fromCity Ciudad de origen.
     * @param toCity   Ciudad de destino.
     * @return Lista de ciudades que forman el camino más corto desde fromCity a
     *         toCity.
     */
    public List<String> getPath(String fromCity, String toCity) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);
        List<String> path = new ArrayList<>();
        if (next[from][to] == -1) {
            return path; // No hay camino entre fromCity y toCity
        }
        int current = from;
        while (current != to) {
            path.add(indexToCity.get(current));
            current = next[current][to]; // Avanza al siguiente nodo en el camino
        }
        path.add(toCity);
        return path;
    }

    /**
     * Obtiene la distancia entre dos ciudades.
     * 
     * @param fromCity Ciudad de origen.
     * @param toCity   Ciudad de destino.
     * @return Distancia entre fromCity y toCity.
     */
    public int getDistance(String fromCity, String toCity) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);
        return adjMatrix[from][to];
    }

    /**
     * Encuentra el centro del grafo, es decir, la ciudad con la menor excentricidad
     * (máxima distancia a otras ciudades).
     * 
     * @return Nombre de la ciudad que es el centro del grafo.
     */
    public String getCenter() {
        int[] eccentricity = new int[numVertices];
        Arrays.fill(eccentricity, Integer.MAX_VALUE / 2);

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && adjMatrix[i][j] < eccentricity[i]) {
                    eccentricity[i] = adjMatrix[i][j];
                }
            }
        }

        int center = -1;
        int minEccentricity = Integer.MAX_VALUE / 2;
        for (int i = 0; i < numVertices; i++) {
            if (eccentricity[i] < minEccentricity) {
                minEccentricity = eccentricity[i];
                center = i;
            }
        }

        return indexToCity.get(center);
    }

    /**
     * Imprime la matriz de adyacencia del grafo mostrando las conexiones entre
     * ciudades con sus respectivas distancias.
     */
    public void printMatrix() {
        for (int i = 0; i < numVertices; i++) {
            String fromCity = indexToCity.get(i);
            boolean hasConnections = false;
            StringBuilder connections = new StringBuilder();
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] != Integer.MAX_VALUE / 2 && i != j) {
                    String toCity = indexToCity.get(j);
                    connections.append(toCity).append(" (").append(adjMatrix[i][j]).append(" KM), ");
                    hasConnections = true;
                }
            }
            if (hasConnections) {
                // Elimina la última coma y espacio
                if (connections.length() > 0) {
                    connections.setLength(connections.length() - 2);
                }
                System.out.println(fromCity + " -> " + connections.toString());
            }
        }
    }
}