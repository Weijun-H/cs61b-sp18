import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */

public class Router {

    private static class Status {
        private GraphDB.Node node;
        private double priority;

        public Status(GraphDB.Node node, double priority) {
            this.node = node;
            this.priority = priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Status status = (Status) o;
            return this.node.getId() == status.node.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(node.getId());
        }
    }

    private static class StatusComparator implements Comparator<Status> {
        public int compare(Status o1, Status o2) {
            Router.Status s1 = o1;
            Router.Status s2 = o2;
            return Double.compare(s1.priority, s2.priority);
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        List<Long> res = new ArrayList<>();
        HashMap<Long, Double> disTO = new HashMap<>();
        HashMap<Long, Long> edgeTo = new HashMap<>();
        Set<Long> marked = new HashSet<>();
        PriorityQueue<Status> pq = new PriorityQueue<>(new StatusComparator());
        GraphDB.Node start = g.getNode(g.closest(stlon,stlat));
        GraphDB.Node target = g.getNode(g.closest(destlon,destlat));

        /* Initialize start node. */
        double dis = 0.0;
//        double h = g.distance(start.getId(), target.getId());
        double h = 0;
        disTO.put(start.getId(), dis);
        pq.add(new Status(start, dis + h));
        /* Start Search*/
        while (!pq.isEmpty()){
            Status curr = pq.poll();
            long currId = curr.node.getId();
            marked.add(currId);
            /* Reach destination*/
            if (currId == target.getId()){
//                System.out.println("Bingo!!!");
//                break;
            }
            /* Update neighbors */
            for (GraphDB.Node node : g.getNeighbors(currId)){
                /* This spot has not been reached. */
                if (!marked.contains(node.getId())){
//                    h = g.distance(node.getId(), target.getId());
                    dis = disTO.get(currId) + g.distance(currId, node.getId());
                    /* Find an more optimum way */
                    if (!disTO.containsKey(node.getId()) || dis < disTO.get(node.getId())){
                        disTO.put(node.getId(), dis);
                        edgeTo.put(node.getId(), currId);
                        Status neighbor = new Status(node, 0);
                        pq.remove(neighbor);
                        pq.add(new Status(node, disTO.get(node.getId()) + h ) );
                    }
                }
            }
        }
        long tmp = target.getId();
//        System.out.println("start: " + start.getId());
//        System.out.println("target: " + target.getId());

        while (tmp != start.getId()) {
//            System.out.println(tmp);
            if (res.contains(tmp)){
                break;
            }
            res.add(tmp);
            tmp = edgeTo.get(tmp);
        }
        res.add(start.getId());
        Collections.reverse(res);
        return res;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> res = new ArrayList<>();
        GraphDB.Node node1 = null;
        GraphDB.Node node2 = null;
        GraphDB.Node prevNode = null;
        int flagTurn = 0;
        int flagStart = 1;
        int direction1 = -1, direction2;
        NavigationDirection ND = new NavigationDirection();
        for (long id: route) {
            if (node1 == null) {
                node1 = g.getNode(id);
                prevNode = node1;
                continue;
            }
            node2 = g.getNode(id);
            double dis = g.distance(node1.getId(),node2.getId());
            direction2 = findDirection(g.bearing(node1.getId(), node2.getId()));

            /* initialize the start node */
            if (flagStart == 1) {
                direction1 = findDirection(g.bearing(node1.getId(), node2.getId()));
                ND.direction = 0;
                ND.distance = dis;
                ND.way = g.getWayName(node1);
                res.add(ND);
                flagStart = 2;
                node1 = node2;
                continue;
            } else if (direction2 == direction1 || res.get(res.size()-1).way.equals(g.getWayName(node2))) {
                updateWay(res, dis, prevNode, node2, flagStart);
            } else {
                ND = new NavigationDirection();
                ND.distance = dis;
                ND.way = g.getWayName(node2);
                ND.direction = findDirection(GraphDB.bearing(prevNode.getLon(), prevNode.getLat(), node2.getLon(), node2.getLat()));
//                if (flagStart != 2) {
//                    updateDirection(res, (GraphDB.bearing(prevNode.getLon(), prevNode.getLat(), node2.getLon(), node2.getLat())), g.bearing(node1.getId(), node2.getId()));
//                }
                flagStart = 0;
                prevNode = node1;
                res.add(ND);
            }
            direction1 = direction2;
            node1 = node2;
        }
        System.out.println(res.toString());
        System.out.println(res.size());
        return res;
    }

    public static int findDirection(double degree) {
        int res = -1;
        if (degree >= -100  && degree <= 100) {
            if (degree >= -30  && degree <= 30){
                if (degree >= -15  && degree <= 15) {
                    res = 1;
                } else {
                    res = degree < 0 ? 2 : 3;
                }
            } else {
                res = degree < 0 ? 4 : 5;
            }
        } else {
            res = degree < 0 ? 6 : 7;
        }
        return res;
    }

    public static void updateWay(List<NavigationDirection> res, double dis, GraphDB.Node prev, GraphDB.Node currNode, int flag) {
        int index = res.size() - 1;
        NavigationDirection nd = res.get(index);
        double deg = GraphDB.bearing(prev.getLon(), prev.getLat(), currNode.getLon(), currNode.getLat());
        if (flag != 2){
            nd.direction = findDirection(deg);
        }
//        nd.distance = GraphDB.distance(prev.getLon(), prev.getLat(), currNode.getLon(), currNode.getLat());
        nd.distance += dis;
        res.remove(index);
        res.add(nd);
    }

    public static void updateDirection(List<NavigationDirection> res, double prevD, double currD) {
        int index = res.size() - 1;
        NavigationDirection nd = res.get(index);
        nd.direction = (findDirection(currD - prevD));
        res.remove(index);
        res.add(nd);
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
