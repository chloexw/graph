package cs601.graph;

import java.util.*;

public class UnweightedGraph<ID extends Comparable, N extends Node<ID>>
    implements Graph<ID,N>
{
    private final LinkedHashMap<ID, N> map;

    public UnweightedGraph() {
        map = new LinkedHashMap<ID, N>();
    }

    /** Add a node to the graph by mapping node.getName() to node.
     *  If node already exists in the graph, return that one
     *  and do not insert the node parameter into the graph.
     *  If the node does not exist already, add it to the graph and return.
     *
     *  Do nothing upon null node.
     */
	public N addNode(N node) {
        if (node == null) {
            return null;
        }
        ID name = node.getName();
        if (!map.containsKey(node.getName())) {
            map.put(name, node);
            return node;
        } else {
            return map.get(name);
        }
    }

    /** Return a sorted (by name) list of all nodes between start and stop, inclusively.
     *  If either the start or stop nodes are null, return Collections.emptyList()
     */
    @Override
    public List<N> getAllNodes(ID start, ID stop) {
        List<N> result = new ArrayList<N>();
        if (start != null && stop != null) {
            N startEdge = map.get(start);
            N stopEdge = map.get(stop);
            if (startEdge != null && stopEdge != null) {
                TreeSet<N> sets = new TreeSet<N>();
                TreeSet<N> visited = new TreeSet<N>();
                ArrayList<N> path = new ArrayList<N>();
                //dfs
                generatePath(startEdge, stopEdge, sets, visited, path);
                result = new ArrayList<N>(sets);
            }
        }
        return result;
    }

    private void generatePath(N start, N stop, TreeSet<N> result, TreeSet<N> visited, ArrayList<N> path) {
        if (start.equals(stop)) {
            //reach the end
            result.addAll(path);
            result.add(stop);
            return;
        }
        visited.add(start);

        path.add(start);
        for (ID edge : start.edges()) {
            N edgeNode = map.get(edge);
            if (!visited.contains(edgeNode)) {
                generatePath(edgeNode, stop, result, visited, path);
            }
        }
        path.remove(path.size()-1);
    }

    @Override
    /** Return the minimum number of edges traversed to go from start to stop.
     *  Return -1 if there is no path from start to stop or one of the
     *  parameters is null. If stop == start, return 0.
     */
    public int getMinPathLength(ID start, ID stop) {
        if (start == null || stop == null) {
            return -1;
        }
        N startNode = map.get(start);
        N stopNode = map.get(stop);
        if (startNode == null || stopNode == null) {
            return -1;
        }
        //use BFS, need to record visited nodes
        //level + 1 only when a level of nodes are visited
        int level = 0;
        HashSet<N> visited = new HashSet<N>();
        ArrayList<N> levelList = new ArrayList<N>();
        visited.add(startNode);
        levelList.add(startNode);

        while (!levelList.isEmpty()) {
            ArrayList<N> updateList = new ArrayList<N>();
            for (N node : levelList) {
                if (node.equals(stopNode)) {
                    return level;
                } else {
                    for (ID id : node.edges()) {
                        N idNode = map.get(id);
                        if (visited.contains(idNode))
                            continue;
                        visited.add(idNode);
                        updateList.add(idNode);
                    }
                }
            }
            levelList = updateList;
            level ++;
        }
        return -1;
    }

    @Override
    /** Return a sorted list of all nodes reachable from start, inclusively.
     *  If start node is null, return Collections.emptyList().
     */
    public List<N> getAllReachableNodes(ID start) {
        List<N> result = new ArrayList<N>();
        if (start == null) {
            return result;
        }
        N startNode = map.get(start);
        if (startNode == null) {
            return result;
        }

        //dont need a visited array here, can use nodes to check if the node has been visited
        TreeSet<N> nodes = new TreeSet<N>();
        Queue<N> q = new LinkedList<N>();
        q.add(startNode);
        //bfs
        while (!q.isEmpty()) {
            N curr = q.poll();
            nodes.add(curr);
            for (ID id : curr.edges()) {
                N idNode = map.get(id);
                if (!nodes.contains(idNode)) {
                    nodes.add(idNode);
                    q.add(idNode);
                }
            }
        }
        result = new ArrayList<N>(nodes);
        return result;
    }

    @Override
    /** Return a list of graph roots. A graph root is any node has no
     *  incident (incoming) edges. This returns an empty list if there
     *  are no roots.
     */
    public List<ID> getRootNames() {
        HashSet<ID> list = new HashSet<ID>();
        //put all the edges in the final list
        for (ID id : map.keySet()) {
            list.add(id);
        }
        //loop through all the node, to check their edges
        //for every edges you found, delete it in the final list
        //the left edges should be the root one(that has no incoming edges)
        for (ID id : map.keySet()) {
            N idNode = map.get(id);
            for (ID edgeId : idNode.edges()) {
                if (list.contains(edgeId)) {
                    list.remove(edgeId);
                }
            }
        }
        return new ArrayList<ID>(list);
    }

    /** Return an edge list for graph in the form of a string, one edge per line */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<ID,N> entry : map.entrySet()) {
            for (ID edge : entry.getValue().edges()) {
                result.append(entry.getKey() + " -> " + edge + "\n");
            }
        }
        return result.toString();
    }
}
