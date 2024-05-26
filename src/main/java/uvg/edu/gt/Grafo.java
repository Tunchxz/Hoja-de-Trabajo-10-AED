package uvg.edu.gt;

import java.util.*;

public class Grafo {
    private int[][] adjMatrix;
    private int[][] next; // Matriz de predecesores
    private int numVertices;
    private Map<String, Integer> cityToIndex;
    private Map<Integer, String> indexToCity;

    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new int[numVertices][numVertices];
        next = new int[numVertices][numVertices];
        cityToIndex = new HashMap<>();
        indexToCity = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    adjMatrix[i][j] = 0;
                } else {
                    adjMatrix[i][j] = Integer.MAX_VALUE / 2;
                }
                next[i][j] = -1;
            }
        }
    }

    public int getCityIndex(String city) {
        return cityToIndex.getOrDefault(city, -1);
    }

    public void addCity(String city) {
        if (!cityToIndex.containsKey(city)) {
            int index = cityToIndex.size();
            cityToIndex.put(city, index);
            indexToCity.put(index, city);
        }
    }

    public void addEdge(String fromCity, String toCity, int weight) {
        addCity(fromCity);
        addCity(toCity);
        int from = cityToIndex.get(fromCity);
        int to = cityToIndex.get(toCity);
        adjMatrix[from][to] = weight;
        next[from][to] = to;
    }

    public void removeEdge(String fromCity, String toCity) {
        int fromIndex = getCityIndex(fromCity);
        int toIndex = getCityIndex(toCity);
        if (fromIndex != -1 && toIndex != -1) {
            adjMatrix[fromIndex][toIndex] = Integer.MAX_VALUE / 2;
        }
    }

    public void floydWarshall() {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (adjMatrix[i][k] != Integer.MAX_VALUE / 2 && adjMatrix[k][j] != Integer.MAX_VALUE / 2) {
                        if (adjMatrix[i][j] > adjMatrix[i][k] + adjMatrix[k][j]) {
                            adjMatrix[i][j] = adjMatrix[i][k] + adjMatrix[k][j];
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }
    }

    public List<String> getPath(String fromCity, String toCity) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);
        List<String> path = new ArrayList<>();
        if (next[from][to] == -1) {
            return path; // No hay camino
        }
        int current = from;
        while (current != to) {
            path.add(indexToCity.get(current));
            current = next[current][to];
        }
        path.add(toCity);
        return path;
    }

    public int getDistance(String fromCity, String toCity) {
        int from = getCityIndex(fromCity);
        int to = getCityIndex(toCity);
        return adjMatrix[from][to];
    }

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
                // Remueve la última coma y el espacio
                if (connections.length() > 0) {
                    connections.setLength(connections.length() - 2);
                }
                System.out.println(fromCity + " -> " + connections.toString());
            }
        }
    }
}