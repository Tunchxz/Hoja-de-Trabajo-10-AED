package uvg.edu.gt;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = new Grafo(100);

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("hdt10\\src\\main\\java\\uvg\\edu\\gt\\guategrafo.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String fromCity = parts[0];
                String toCity = parts[1];
                int weight = Integer.parseInt(parts[2]);
                grafo.addEdge(fromCity, toCity, weight);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        grafo.floydWarshall();

        boolean running = true;
        while (running) {
            System.out.println("\n---------------| Centro de Respuesta al Covid-19 |---------------");
            System.out.println("1. Mostrar ruta más corta entre dos ciudades");
            System.out.println("2. Mostrar el centro del grafo");
            System.out.println("3. Modificar el grafo");
            System.out.println("4. Mostrar matriz de adyacencia");
            System.out.println("5. Finalizar programa");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea
            switch (choice) {
                case 1:
                    System.out.print("\nCiudad origen: ");
                    String origin = scanner.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destination = scanner.nextLine();
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
                    String center = grafo.getCenter();
                    System.out.println("\nEl centro del grafo es la ciudad: " + center);
                    break;
                case 3:
                    System.out.print("\n1. Interrupción de tráfico\n2. Nueva conexión\n");
                    int modChoice = scanner.nextInt();
                    scanner.nextLine(); // Consumir nueva línea
                    if (modChoice == 1) {
                        System.out.print("Ciudad origen: ");
                        String from = scanner.nextLine();
                        System.out.print("Ciudad destino: ");
                        String to = scanner.nextLine();
                        grafo.removeEdge(from, to);
                        System.out.println("\nNueva interrupción establecida con éxito.");
                    } else if (modChoice == 2) {
                        System.out.print("\nCiudad origen: ");
                        String from = scanner.nextLine();
                        System.out.print("Ciudad destino: ");
                        String to = scanner.nextLine();
                        System.out.print("Distancia en KM: ");
                        int nuevaDistancia = scanner.nextInt();
                        scanner.nextLine(); // Consumir nueva línea
                        grafo.addEdge(from, to, nuevaDistancia);
                        System.out.println("\nNueva conexión establecida con éxito.");
                    }
                    grafo.floydWarshall();
                    break;
                case 4:
                    System.out.println("\n---------------| Matriz de Adyacencia |---------------");
                    grafo.printMatrix();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("\nOpción no válida");
            }
        }
        scanner.close();
    }
}