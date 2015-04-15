package cs601.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** A node with a name and list of directed edges (edges do not have labels). */
public class NamedNode implements Node<String> {
    private String id;
    private List<String> edges;

    public NamedNode(String name) {
        id = name;
        edges = new ArrayList<String>();
    }

    @Override
    public String getEdge(int i) throws IndexOutOfBoundsException {
        Iterator<String> itr = edges.iterator();
        String res = null;
        while (itr.hasNext() && i >= 0) {
            res = itr.next();
            i --;
        }
        if (res == null) {
            throw new IndexOutOfBoundsException();
        }
        return res;
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public void addEdge(Node<String> target) {
        String id = target.getName();
        if (id != null) {
            edges.add(id);
        }
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public Iterable<String> edges() {
        return edges;
    }

    @Override
    public int compareTo(Node<String> o) {
        return id.compareTo(o.getName());
    }

    /** Return the node's name as a String */
    public String toString() {
        return id;
    }
}
