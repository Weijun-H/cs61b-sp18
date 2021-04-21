import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */
    public  class Node {
        private final double lon, lat;
        private final long id;
        private String location;

        public Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }

        public long getId() {
            return this.id;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Double.compare(node.lon, lon) == 0 &&
                    Double.compare(node.lat, lat) == 0 &&
                    id == node.id &&
                    Objects.equals(location, node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lon, lat, id, location);
        }
    }

    public  class Edge {
        private long id;
        private HashSet<Long> nodeList = new HashSet<>();
        private boolean valid;
//        private int maxSpeed;
        private String name;
        public Edge(long id) {
            this.id = id;
            name = "X";
        }

        public long getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }

        public void addNode (long id) {
            nodeList.add(id);
        }
    }

    private Map<Node, HashSet<Node>> adj = new HashMap<>();
    private Map<Long, Node> V = new HashMap<>();
    private Map<Long, Edge> E = new HashMap<>();
    private int numEdges;
    private int numVertices;

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        List<Long> alones = new ArrayList<>();
        for (Node node : V.values()) {
            if (!adj.containsKey(node)){
                alones.add(node.getId());
            }
        }
        for (long i : alones){
            V.remove(i);
        }
        alones.clear();
        for (Edge e: E.values()) {
            if (!e.isValid() | e.nodeList.size() <= 1) {
                alones.add(e.id);
            }
        }
        for (long i : alones){
            E.remove(i);
        }
    }


    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.

        return V.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        ArrayList<Long> vList = new ArrayList<Long>();
        for (Node i : adj.get(V.get(v))) {
            vList.add(i.getId());
        }
        return vList;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Node closestNode = null;
        double min = distance(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT) + 10000;
        for (Node i: V.values()){
            double dis = distance(i.lon, i.lat, lon, lat);
            if (dis <= min) {
                min = dis;
                closestNode = i;
            }
        }
        return closestNode.getId();
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        if (V.containsKey(v)) {
            return V.get(v).getLon();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        if (V.containsKey(v)) {
            return V.get(v).getLat();
        } else {
            throw new NullPointerException();
        }
    }

    void addAdj(Long a, Long b) {
        Node n1 = V.get(a);
        Node n2 = V.get(b);
        if (adj.containsKey(n1)) {
            adj.get(n1).add(n2);
        } else {
            HashSet<Node> treeSet = new HashSet<Node>();
            treeSet.add(n2);
            adj.put(n1, treeSet);
        }
        if (adj.containsKey(n2)) {
            adj.get(n2).add(n1);
        } else {
            HashSet<Node> treeSet = new HashSet<Node>();
            treeSet.add(n1);
            adj.put(n2, treeSet);
        }
        numEdges++;
    }

    void addNode(Node a) {
        if (!V.containsKey(a.getId())) {
            V.put(a.getId(), a);
            numVertices++;
        }
    }

    void removeNode(Long id) {
        Node n;
        if (V.containsKey(id)) {
            n = V.remove(id);
            if (adj.containsKey(n)) {
                for (Node i : adj.get(n)) {
                    adj.get(i).remove(n);
                }
            }
            adj.remove(n);
            numEdges--;
        }
    }

    public void connectNd(List<Long> ndList, Edge edge) {
        E.put(edge.getId(), edge);
        Node n1 = V.get(ndList.get(0));
        Node n2;
        edge.addNode(n1.getId());
        for (int i = 1; i < ndList.size(); i++) {
            n2 = V.get(ndList.get(i));
            addAdj(n1.getId(), n2.getId());
            edge.addNode(n2.getId());
            n1 = n2;
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

    public Node getNode(long id) {
        return V.get(id);
    }

    Iterable<Node> getNeighbors(long id){
        return this.adj.get(V.get(id));
    }

    public String getWayName(Node node) {
        for ( Edge e: E.values()) {
            if (e.nodeList.contains(node.getId())) {
                return e.name;
            }
        }
        return "XXX";
    }
}