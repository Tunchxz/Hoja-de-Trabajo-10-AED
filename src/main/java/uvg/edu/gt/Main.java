package uvg.edu.gt;

import java.io.*;
import java.util.*;

/**
 * Clase principal que contiene el método main para ejecutar el programa
 * interactivo del grafo de ciudades.
 * 
 * @author Cristian Túnchez
 * @version 1.0
 * @since 26/05/2024
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = new Grafo(100); // Se inicializa el grafo con capacidad para hasta 100 ciudades

        try {
            // Lectura del archivo que contiene las conexiones entre ciudades y sus
            // distancias
            BufferedReader br = new BufferedReader(
                    new FileReader("src\\main\\java\\uvg\\edu\\gt\\guategrafo.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                // Se divide la línea en partes: ciudad_origen ciudad_destino distancia
                String[] parts = line.split(" ");
                String fromCity = parts[0];
                String toCity = parts[1];
                int weight = Integer.parseInt(parts[2]);
                // Se agrega la conexión al grafo
                grafo.addEdge(fromCity, toCity, weight);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        grafo.floydWarshall(); // Se ejecuta el algoritmo de Floyd-Warshall para encontrar todos los caminos
                               // más cortos

        boolean running = true;
        while (running) {
            // Menú interactivo
            System.out.println("\n---------------| Centro de Respuesta al Covid-19 |---------------");
            System.out.println("1. Mostrar ruta más corta entre dos ciudades");
            System.out.println("2. Mostrar el centro del grafo");
            System.out.println("3. Modificar el grafo");
            System.out.println("4. Mostrar matriz de adyacencia");
            System.out.println("5. Finalizar programa");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea después de leer el entero

            switch (choice) {
                case 1:
                    // Opción 1: Mostrar ruta más corta entre dos ciudades
                    System.out.print("\nCiudad origen: ");
                    String origin = scanner.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destination = scanner.nextLine();
                    // Obtener la distancia y el camino más corto
                    int distance = grafo.getDistance(origin, destination);
                    if (distance == Integer.MAX_VALUE / 2) {
                        System.out.println("\nNo hay camino entre estas dos ciudades.");
                    } else {
                        System.out.println("\nLa distancia más corta es: " + distance + " KM");
                        List<String> path = grafo.getPath(origin, destination);
                        System.out.println("La ruta más corta pasa por las ciudades: " + path);
                    }
                    break;
                case 2:
                    // Opción 2: Mostrar el centro del grafo
                    String center = grafo.getCenter();
                    System.out.println("\nEl centro del grafo es la ciudad: " + center);
                    break;
                case 3:
                    // Opción 3: Modificar el grafo (eliminar o agregar conexiones)
                    System.out.print("\n1. Interrupción de tráfico\n2. Nueva conexión\n");
                    int modChoice = scanner.nextInt();
                    scanner.nextLine(); // Consumir nueva línea después de leer el entero

                    switch (modChoice) {
                        case 1: {
                            // Subopción 1: Interrupción de tráfico entre dos ciudades
                            System.out.print("Ciudad origen: ");
                            String from = scanner.nextLine();
                            System.out.print("Ciudad destino: ");
                            String to = scanner.nextLine();
                            // Eliminar la conexión entre las ciudades
                            grafo.removeEdge(from, to);
                            System.out.println("\nNueva interrupción establecida con éxito.");
                            break;
                        }
                        case 2: {
                            // Subopción 2: Establecer una nueva conexión entre dos ciudades
                            System.out.print("\nCiudad origen: ");
                            String from = scanner.nextLine();
                            System.out.print("Ciudad destino: ");
                            String to = scanner.nextLine();
                            System.out.print("Distancia en KM: ");
                            int newDistance = scanner.nextInt();
                            scanner.nextLine(); // Consumir nueva línea después de leer el entero
                            // Agregar la nueva conexión al grafo
                            grafo.addEdge(from, to, newDistance);
                            System.out.println("\nNueva conexión establecida con éxito.");
                            break;
                        }
                        default: {
                            System.out.println("Ingrese una opción válida.");
                            break;
                        }
                    }
                    grafo.floydWarshall(); // Re-calcular los caminos más cortos después de modificar el grafo
                    break;
                case 4:
                    // Opción 4: Mostrar la matriz de adyacencia del grafo
                    System.out.println("\n---------------| Matriz de Adyacencia |---------------");
                    grafo.printMatrix();
                    break;
                case 5:
                    // Opción 5: Finalizar el programa
                    running = false;
                    break;
                default:
                    // Opción no válida
                    System.out.println("\nOpción no válida");
            }
        }
        scanner.close(); // Cerrar el Scanner al finalizar
    }
}