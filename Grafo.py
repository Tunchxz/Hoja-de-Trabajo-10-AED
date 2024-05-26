import networkx as nx

class Grafo:
    def __init__(self):
        self.grafo = nx.DiGraph()  # Se inicializa un grafo dirigido vacío

    def cargar_desde_archivo(self, archivo):
        with open(archivo, 'r') as f:
            for line in f:
                partes = line.strip().split()
                if len(partes) == 3:
                    ciudad_origen, ciudad_destino, distancia = partes
                    distancia = int(distancia)
                    self.grafo.add_edge(ciudad_origen, ciudad_destino, weight=distancia)
                    # Se agregan conexiones con pesos desde el archivo

    def calcular_caminos_mas_cortos(self):
        self.distancias = dict(nx.shortest_path_length(self.grafo, weight='weight'))
        # Calcula las distancias más cortas entre todos los pares de nodos usando Dijkstra

    def encontrar_centro_grafo(self):
        excentricidades = {}
        for nodo in self.grafo.nodes:
            if nodo in self.distancias and any(self.distancias[nodo].values()):
                excentricidades[nodo] = max(self.distancias[nodo].values())
            else:
                excentricidades[nodo] = float('inf')
        centro = min(excentricidades, key=excentricidades.get)
        return centro
        # Encuentra el centro del grafo basado en las excentricidades calculadas

    def agregar_conexion(self, origen, destino, distancia):
        self.grafo.add_edge(origen, destino, weight=distancia)
        self.calcular_caminos_mas_cortos()
        # Agrega una nueva conexión y recalcula los caminos más cortos

    def eliminar_conexion(self, origen, destino):
        if self.grafo.has_edge(origen, destino):
            self.grafo.remove_edge(origen, destino)
            self.calcular_caminos_mas_cortos()
        # Elimina una conexión existente y recalcula los caminos más cortos

    def obtener_distancia(self, origen, destino):
        return self.distancias[origen][destino]
        # Obtiene la distancia más corta entre dos nodos

    def obtener_camino_mas_corto(self, origen, destino):
        return nx.shortest_path(self.grafo, source=origen, target=destino, weight='weight')
        # Obtiene el camino más corto entre dos nodos

    def mostrar_matriz_adyacencia(self):
        print("\n---------------| Matriz de Adyacencia |---------------")
        for u, v, d in self.grafo.edges(data=True):
            print(f"{u} -> {v} ({d['weight']} KM)")
        # Muestra la matriz de adyacencia del grafo

if __name__ == '__main__':
    grafo = Grafo()

    # Cargar grafo desde archivo
    grafo.cargar_desde_archivo('guategrafo.txt')

    # Calcular caminos más cortos
    grafo.calcular_caminos_mas_cortos()

    while True:
        print("\n---------------| Centro de Respuesta al Covid-19 |---------------")
        print("1. Mostrar ruta más corta entre dos ciudades")
        print("2. Mostrar el centro del grafo")
        print("3. Modificar el grafo")
        print("4. Mostrar matriz de adyacencia")
        print("5. Finalizar programa")

        choice = input("Seleccione una opción: ")

        if choice == '1':
            # Opción para mostrar la ruta más corta entre dos ciudades
            origen = input("\nCiudad origen: ")
            destino = input("Ciudad destino: ")
            try:
                distancia = grafo.obtener_distancia(origen, destino)
                if distancia == float('inf'):
                    print("\nNo hay camino entre estas dos ciudades.")
                else:
                    print(f"\nLa distancia más corta es: {distancia} KM")
                    camino = grafo.obtener_camino_mas_corto(origen, destino)
                    print(f"La ruta más corta pasa por las ciudades: {camino}")
            except KeyError:
                print("\nUna o ambas ciudades no existen en el grafo.")

        elif choice == '2':
            # Opción para mostrar el centro del grafo
            centro = grafo.encontrar_centro_grafo()
            print(f"\nEl centro del grafo es la ciudad: {centro}")

        elif choice == '3':
            # Opción para modificar el grafo (eliminar o agregar conexiones)
            print("\n1. Interrupción de tráfico")
            print("2. Nueva conexión")
            mod_choice = input("Seleccione una opción de modificación: ")

            if mod_choice == '1':
                # Submenú para interrupción de tráfico
                origen = input("\nCiudad origen: ")
                destino = input("Ciudad destino: ")
                grafo.eliminar_conexion(origen, destino)
                print("\nInterrupción de tráfico establecida con éxito.")

            elif mod_choice == '2':
                # Submenú para agregar nueva conexión
                try:
                    origen = input("\nCiudad origen: ")
                    destino = input("Ciudad destino: ")
                    distancia = int(input("Distancia en KM: "))
                    grafo.agregar_conexion(origen, destino, distancia)
                    print("\nNueva conexión establecida con éxito.")
                except ValueError:
                    print("\nPor favor, ingrese un número válido para la distancia.")

        elif choice == '4':
            # Opción para mostrar la matriz de adyacencia
            grafo.mostrar_matriz_adyacencia()

        elif choice == '5':
            # Opción para finalizar el programa
            break

        else:
            print("\nOpción no válida")