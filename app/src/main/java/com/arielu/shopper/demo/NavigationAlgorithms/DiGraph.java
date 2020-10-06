package com.arielu.shopper.demo.NavigationAlgorithms;

/* Generic Directed Weighted Graph with Dijkstra's Shortest Path Algorithm
 * by /u/Philboyd_Studge
 * for /r/javaexamples
 */

import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unchecked")
/**
 * Creates a directed, weighted <tt>Graph</tt> for any Comparable type
 * <p> add Edge date with <code>add(T valueforVertexFrom, T valueForVertexTo, int cost)</code>
 * <p> use <code>getPath(T valueFrom, T valueTo)</code> to get the shortest path between
 * the two using Dijkstra's Algorithm
 * <p> If returned List has a size of 1 and a cost of Integer.Max_Value then no conected path
 * was found
 *
 * @author /u/Philboyd_Studge
 */
public class DiGraph<T extends Comparable<T>> {
    public enum State {UNVISITED, VISITED, COMPLETE}

    ;

    public ArrayList<Vertex> vertices;
    public ArrayList<Edge> edges;

    /**
     * Default Constructor
     */
    public DiGraph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    /**
     * Creates Edge from two values T directed from -- to
     *
     * @param from Value for Vertex 1
     * @param to   Value for Vertex 2
     * @param cost Cost or weight of edge
     */
    public void add(T from, T to, int cost) {
        Edge temp = findEdge(from, to);
        if (temp != null) {
            // Don't allow multiple edges, update cost.
            System.out.println("Edge " + from + "," + to + " already exists. Changing cost.");
            temp.cost = cost;
        } else {
            // this will also create the vertices
            Edge e = new Edge(from, to, cost);
            edges.add(e);
        }
    }

    /**
     * find Vertex in Graph from value
     *
     * @param v value of type T
     * @return Vertex, or <code>null</code> if not found.
     */
    private Vertex findVertex(T v) {
        for (Vertex each : vertices) {
            if (each.value.compareTo(v) == 0)
                return each;
        }
        return null;
    }

    /**
     * Find edge containg two vertices
     * in direction v1 -> v2
     *
     * @param v1 Vertex 'from'
     * @param v2 Vertex 'to'
     * @return Edge, or <code>null</code> if not found.
     */
    private Edge findEdge(Vertex v1, Vertex v2) {
        for (Edge each : edges) {
            if (each.from.equals(v1) && each.to.equals(v2)) {
                return each;
            }
        }
        return null;
    }

    /**
     * Find edge from two values
     *
     * @param from from value of type T
     * @param to   to value of type T
     * @return Edge, or <code>null</code> if not found.
     */
    private Edge findEdge(T from, T to) {
        for (Edge each : edges) {
            if (each.from.value.equals(from) && each.to.value.equals(to)) {
                return each;
            }
        }
        return null;
    }

    /**
     * Sets all states to UNVISITED
     */
    private void clearStates() {
        for (Vertex each : vertices) {
            each.state = State.UNVISITED;
        }
    }

    /**
     * Test if DFS or BFS returned a connected graph
     *
     * @return true if connected, false if not.
     */
    public boolean isConnected() {
        for (Vertex each : vertices) {
            if (each.state != State.COMPLETE)
                return false;
        }
        return true;
    }

    /**
     * Performs a recursive Depth First Search on the
     * 'root' node (the first vertex created)
     *
     * @return true if connected, false if empty or not connected
     */
    public boolean DepthFirstSearch() {
        if (vertices.isEmpty()) return false;

        clearStates();
        // get first node
        Vertex root = vertices.get(0);
        if (root == null) return false;

        // call recursive function
        DepthFirstSearch(root);
        return isConnected();
    }

    /**
     * Helper for Depth first search
     *
     * @param v vertex
     */
    private void DepthFirstSearch(Vertex v) {
        v.state = State.VISITED;

        // loop through neighbors
        for (Vertex each : v.outgoing) {
            if (each.state == State.UNVISITED) {
                DepthFirstSearch(each);
            }
        }
        v.state = State.COMPLETE;
    }

    /**
     * Performs a Breadth-First Search
     * starting at 'root' node (First created vertex)
     *
     * @return true is connected, false is not or if empty
     */
    public boolean BreadthFirstSearch() {
        if (vertices.isEmpty()) return false;
        clearStates();

        Vertex root = vertices.get(0);
        if (root == null) return false;

        Queue<Vertex> queue = new LinkedList<>();
        queue.add(root);
        root.state = State.COMPLETE;

        while (!queue.isEmpty()) {
            root = queue.peek();
            for (Vertex each : root.outgoing) {

                if (each.state == State.UNVISITED) {
                    each.state = State.COMPLETE;
                    queue.add(each);
                }
            }
            queue.remove();
        }
        return isConnected();
    }

    /**
     * Perfoms a Breadth-First Search on a given starting vertex
     *
     * @param v1 Value of type T for starting vertex
     * @return true if connected, false if not or if empty
     */
    public boolean BreadthFirstSearch(T v1) {
        if (vertices.isEmpty()) return false;
        clearStates();

        Vertex root = findVertex(v1);
        if (root == null) return false;

        Queue<Vertex> queue = new LinkedList<>();
        queue.add(root);
        root.state = State.COMPLETE;

        while (!queue.isEmpty()) {
            root = queue.peek();
            for (Vertex each : root.outgoing) {

                if (each.state == State.UNVISITED) {
                    each.state = State.COMPLETE;
                    queue.add(each);
                }
            }
            queue.remove();
        }
        return isConnected();
    }

    /**
     * Creates path information on the graph using the Dijkstra Algorithm,
     * Puts the information into the Vertices, based on given starting vertex.
     *
     * @param v1 Value of type T for starting vertex
     * @return true if successfull, false if empty or not found.
     */
    private boolean Dijkstra(T v1) {
        if (vertices.isEmpty()) return false;

        // reset all vertices minDistance and previous
        resetDistances();

        // make sure it is valid
        Vertex source = findVertex(v1);
        if (source == null) return false;

        // set to 0 and add to heap
        source.minDistance = 0;
        PriorityQueue<Vertex> pq = new PriorityQueue<>();
        pq.add(source);

        while (!pq.isEmpty()) {
            //pull off top of queue
            Vertex u = pq.poll();

            // loop through adjacent vertices
            for (Vertex v : u.outgoing) {
                // get edge
                Edge e = findEdge(u, v);
                if (e == null) return false;
                // add cost to current
                int totalDistance = u.minDistance + e.cost;
                if (totalDistance < v.minDistance) {
                    // new cost is smaller, set it and add to queue
                    pq.remove(v);
                    v.minDistance = totalDistance;
                    // link vertex
                    v.previous = u;
                    pq.add(v);
                }
            }
        }
        return true;
    }

    /**
     * Goes through the result tree created by the Dijkstra method
     * and steps backward
     *
     * @param target Vertex end of path
     * @return string List of vertices and costs
     */
    private List<String> getShortestPath(Vertex target) {
        List<String> path = new ArrayList<String>();

        // check for no path found
        if (target.minDistance == Integer.MAX_VALUE) {
            path.add("No path found");
            return path;
        }

        // loop through the vertices from end target
        for (Vertex v = target; v != null; v = v.previous) {
            path.add(v.value + " : cost : " + v.minDistance);
        }

        // flip the list
        Collections.reverse(path);
        return path;
    }

    /**
     * for Dijkstra, resets all the path tree fields
     */
    private void resetDistances() {
        for (Vertex each : vertices) {
            each.minDistance = Integer.MAX_VALUE;
            each.previous = null;
        }
    }

    /**
     * PUBLIC WRAPPER FOR PRIVATE FUNCTIONS
     * Calls the Dijkstra method to build the path tree for the given
     * starting vertex, then calls getShortestPath method to return
     * a list containg all the steps in the shortest path to
     * the destination vertex.
     *
     * @param from value of type T for Vertex 'from'
     * @param to   value of type T for vertex 'to'
     * @return ArrayList of type String of the steps in the shortest path.
     */
    public List<String> getPath(T from, T to) {
        boolean test = Dijkstra(from);
        if (test == false) return null;
        List<String> path = getShortestPath(findVertex(to));

        return path;
    }

    /**
     * @return string of vertices
     */
    @Override
    public String toString() {
        String retval = "";
        for (Vertex each : vertices) {
            retval += each.toString() + "\n";
        }
        return retval;
    }

    /**
     * list all the edges into a string
     *
     * @return string of edge data
     */
    public String edgesToString() {
        String retval = "";
        for (Edge each : edges) {
            retval += each + "\n";
        }
        return retval;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public class Vertex implements Comparable<Vertex> {
        T value;

        // variables for Dijkstra Tree
        Vertex previous = null;
        int minDistance = Integer.MAX_VALUE;

        List<Vertex> incoming;
        List<Vertex> outgoing;
        State state;

        /**
         * Creates new Vertex with value T
         *
         * @param value T
         */
        public Vertex(T value) {
            this.value = value;
            incoming = new ArrayList<>();
            outgoing = new ArrayList<>();
            state = State.UNVISITED;
        }

        /**
         * @param other Vertex to compare to
         */
        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(minDistance, other.minDistance);
        }

        /**
         * Add Vertex to adjacent incoming list
         *
         * @param vert Vertex of incoming adjacent
         */
        public void addIncoming(Vertex vert) {
            incoming.add(vert);
        }

        public void addOutgoing(Vertex vert) {
            outgoing.add(vert);
        }

        /**
         * Get string of Vertex with all it's ingoing and outgoing adjacencies
         *
         * @ return string
         */
        @Override
        public String toString() {
            String retval = "";
            retval += "Vertex: " + value + " : ";
            retval += " In: ";
            for (Vertex each : incoming) retval += each.value + " ";
            retval += "Out: ";
            for (Vertex each : outgoing) retval += each.value + " ";
            return retval;
        }

        public T getValue() {
            return value;
        }
    }

    public class Edge {
        public Vertex from;
        public Vertex to;
        public int cost;

        /**
         * @param v1   value of type T for 'from' vertex
         * @param v2   value of type T for 'to' vertex
         * @param cost integer value for cost/weight of edge
         */
        public Edge(T v1, T v2, int cost) {
            from = findVertex(v1);
            if (from == null) {
                from = new Vertex(v1);
                vertices.add(from);
            }
            to = findVertex(v2);
            if (to == null) {
                to = new Vertex(v2);
                vertices.add(to);
            }
            this.cost = cost;

            from.addOutgoing(to);
            to.addIncoming(from);
        }

        /**
         * @return Edge as string
         */
        @Override
        public String toString() {
            return "Edge From: " + from.value + " to: " + to.value + " cost: " + cost;
        }
    }
}

