package principal.dijkstra;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import principal.Constantes;
import principal.entes.Enemigo;

public class Dijkstra {
    private int anchoMapaEnTiles;
    private int altoMapaEnTiles;

    private ArrayList<Nodo> nodosMapa;   // Lista de todos los nodos en el mapa
    private ArrayList<Nodo> pendientes;  // Lista de nodos pendientes por evaluar
    private ArrayList<Nodo> visitados;   // Lista de nodos ya evaluados
    private boolean constructor = true;  // Indicador de si el objeto está en construcción

    public Dijkstra(final Point centroCalculo, final int anchoMapaEnTiles, final int altoMapaEnTiles,
                    final ArrayList<Rectangle> zonasSolidas) {
        this.anchoMapaEnTiles = anchoMapaEnTiles;
        this.altoMapaEnTiles = altoMapaEnTiles;
        nodosMapa = new ArrayList<>();

        // Crear nodos del mapa, marcando los no transitables
        for (int y = 0; y < altoMapaEnTiles; y++) {
            for (int x = 0; x < anchoMapaEnTiles; x++) {
                final int lado = Constantes.LADO_SPRITE;
                final Rectangle ubicacionNodo = new Rectangle(x * lado, y * lado, lado, lado);

                boolean transitable = true;
                for (Rectangle area : zonasSolidas) {
                    if (ubicacionNodo.intersects(area)) {
                        transitable = false;
                        break;
                    }
                }

                if (!transitable) {
                    continue;
                }

                Nodo nodo = new Nodo(new Point(x, y), Double.MAX_VALUE);
                nodosMapa.add(nodo);
            }
        }
        pendientes = new ArrayList<>(nodosMapa);

        reiniciarYEvaluar(centroCalculo);
        constructor = false;
    }

    // Obtiene las coordenadas del nodo que coinciden con el punto del jugador
    public Point obtenerCoordenadasNodoCoincidente(final Point puntoJugador) {
        Rectangle rectanguloPuntoExacto = new Rectangle(puntoJugador.x / Constantes.LADO_SPRITE,
                                                        puntoJugador.y / Constantes.LADO_SPRITE, 1, 1);

        Point puntoExacto = null;

        for (Nodo nodo : nodosMapa) {
            if (nodo.obtenerArea().intersects(rectanguloPuntoExacto)) {
                puntoExacto = new Point(rectanguloPuntoExacto.x, rectanguloPuntoExacto.y);
                return puntoExacto;
            }
        }

        return puntoExacto;
    }

    // Clona la lista de nodos del mapa a la lista de nodos pendientes
    private ArrayList<Nodo> clonarNodosMapaANodosPendientes() {
        ArrayList<Nodo> nodosClonados = new ArrayList<>();
        for (Nodo nodo : nodosMapa) {
            Point posicion = nodo.obtenerPosicion();
            double distancia = nodo.obtenerDistancia();
            Nodo nodoClonado = new Nodo(posicion, distancia);
            nodosClonados.add(nodoClonado);
        }

        return nodosClonados;
    }

    // Reinicia los nodos y evalúa el centro de cálculo
    public void reiniciarYEvaluar(final Point centroCalculo) {
        if (!constructor) {
            if (visitados.size() == 0) {
                clonarNodosMapaANodosPendientes();
            } else {
                pendientes = new ArrayList<>(visitados);
                for (Nodo nodo : pendientes) {
                    nodo.cambiarDistancia(Double.MAX_VALUE);
                }
            }
        }

        definirCentroCalculoEnPendientes(centroCalculo);
        visitados = new ArrayList<>();
        evaluarHeuristicaGlobal();
    }

    // Define el centro de cálculo en los nodos pendientes
    private void definirCentroCalculoEnPendientes(final Point centroCalculo) {
        for (Nodo nodo : pendientes) {
            if (nodo.obtenerPosicion().equals(centroCalculo)) {
                nodo.cambiarDistancia(0.0);
            }
        }
    }

    // Evalúa la heurística global de los nodos pendientes
    private void evaluarHeuristicaGlobal() {
        while (!pendientes.isEmpty()) {
            int cambios = 0;

            for (Iterator<Nodo> iterador = pendientes.iterator(); iterador.hasNext();) {
                Nodo nodo = iterador.next();

                if (nodo.obtenerDistancia() == Double.MAX_VALUE) {
                    continue;
                } else {
                    evaluarHeuristicaVecinos(nodo);
                    visitados.add(nodo);
                    iterador.remove();
                    cambios++;
                }
            }

            if (cambios == 0) {
                break;
            }
        }
    }

    // Evalúa la heurística de los nodos vecinos
    private void evaluarHeuristicaVecinos(final Nodo nodo) {
        int inicialY = nodo.obtenerPosicion().y;
        int inicialX = nodo.obtenerPosicion().x;

        final double DISTANCIA_DIAGONAL = 1.42412;

        for (int y = inicialY - 1; y < inicialY + 2; y++) {
            for (int x = inicialX - 1; x < inicialX + 2; x++) {
                if (x <= -1 || y <= -1 || x >= anchoMapaEnTiles || y >= altoMapaEnTiles) {
                    continue;
                }

                if (inicialX == x && inicialY == y) {
                    continue;
                }

                int indiceNodo = obtenerIndiceNodoPorPosicionEnPendientes(new Point(x, y));
                if (indiceNodo == -1) {
                    continue;
                }

                if (pendientes.get(indiceNodo).obtenerDistancia() == Double.MAX_VALUE - 1) {
                    double distancia;
                    if (inicialX != x && inicialY != y) {
                        distancia = DISTANCIA_DIAGONAL;
                    } else {
                        distancia = 1;
                    }

                    pendientes.get(indiceNodo).cambiarDistancia(nodo.obtenerDistancia() + distancia);
                }
            }
        }
    }

    // Obtiene los nodos vecinos de un nodo dado
    private ArrayList<Nodo> obtenerNodosVecinos(Nodo nodo) {
        int inicialY = nodo.obtenerPosicion().y;
        int inicialX = nodo.obtenerPosicion().x;

        ArrayList<Nodo> nodosVecinos = new ArrayList<>();

        for (int y = inicialY - 1; y < inicialY + 2; y++) {
            for (int x = inicialX - 1; x < inicialX + 2; x++) {
                if (x <= -1 || y <= -1 || x >= anchoMapaEnTiles || y >= altoMapaEnTiles) {
                    continue;
                }

                if (inicialX == x && inicialY == y) {
                    continue;
                }

                int indiceNodo = obtenerIndiceNodoPorPosicionEnVisitados(new Point(x, y));
                if (indiceNodo == -1) {
                    continue;
                }
                nodosVecinos.add(visitados.get(indiceNodo));
            }
        }

        return nodosVecinos;
    }

    // Encuentra el siguiente nodo para un enemigo
    public Nodo encontrarSiguienteNodoParaEnemigo(Enemigo enemigo) {
        ArrayList<Nodo> nodosAfectados = new ArrayList<>();

        Nodo siguienteNodo = null;

        for (Nodo nodo : visitados) {
            if (enemigo.obtenerAreaPosicional().intersects(nodo.obtenerAreaPixeles())) {
                nodosAfectados.add(nodo);
            }
        }

        if (nodosAfectados.size() == 1) {
            Nodo nodoBase = nodosAfectados.get(0);
            nodosAfectados = obtenerNodosVecinos(nodoBase);
        }

        for (int i = 0; i < nodosAfectados.size(); i++) {
            if (i == 0) {
                siguienteNodo = nodosAfectados.get(0);
            } else {
                if (siguienteNodo.obtenerDistancia() > nodosAfectados.get(i).obtenerDistancia()) {
                    siguienteNodo = nodosAfectados.get(i);
                }
            }
        }

        return siguienteNodo;
    }

    // Obtiene el índice de un nodo por su posición en la lista de pendientes
    private int obtenerIndiceNodoPorPosicionEnPendientes(final Point posicion) {
        for (Nodo nodo : pendientes) {
            if (nodo.obtenerPosicion().equals(posicion)) {
                return pendientes.indexOf(nodo);
            }
        }

        return -1;
    }

    // Obtiene el índice de un nodo por su posición en la lista de visitados
    private int obtenerIndiceNodoPorPosicionEnVisitados(final Point posicion) {
        for (Nodo nodo : visitados) {
            if (nodo.obtenerPosicion().equals(posicion)) {
                return visitados.indexOf(nodo);
            }
        }

        return -1;
    }

    // Obtiene la lista de nodos visitados
    public ArrayList<Nodo> obtenerVisitados() {
        return visitados;
    }

    // Obtiene la lista de nodos pendientes
    public ArrayList<Nodo> obtenerPendientes() {
        return pendientes;
    }
}

