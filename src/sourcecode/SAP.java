package sourcecode;

import edu.princeton.cs.algs4.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.Stack;

public class SAP {

    public final int INF = Integer.MAX_VALUE;
    public final Digraph net;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        net = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        return checkWayFromVtoW(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        return checkWayFromVtoW(v, w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        return checkWayFromVtoW(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        return checkWayFromVtoW(v, w)[1];
    }

    /**
     * Find the minimal path. And vertex at which the minimum path is reached will be ancestor.
     *
     * @param v - vertex v
     * @param w - vertex w
     * @return new arr[2], where arr[0] is shortest ancestral length, arr[1] is common ancestor
     */
    int[] checkWayFromVtoW(int v, int w){
        validateArg(v);
        validateArg(w);

        // use BreadthFirstDirectedPaths to find all possible paths from the vertex
        BreadthFirstDirectedPaths way_v = new BreadthFirstDirectedPaths(net, v);
        BreadthFirstDirectedPaths way_w = new BreadthFirstDirectedPaths(net, w);


        // check all vertex and find those that there are in one of the paths of both vertices
        int current_distance;
        int min_distance = INF;
        int ancestor = 0;
        for(int i = 0; i < net.V() && i != w && i != v; ++i){
            if(way_v.hasPathTo(i) && way_v.distTo(i) < INF && way_w.hasPathTo(i) && way_w.distTo(i) < INF){
                current_distance = way_v.distTo(i) + way_w.distTo(i);
                if(min_distance > current_distance){
                    min_distance = current_distance;
                    ancestor = i;
                }
            }
        }
        return min_distance != INF ? new int[]{min_distance, ancestor} : new int[]{-1, -1};
    }

    /**
     * Work with set vertices v and w
     * Find the minimal path. And vertex at which the minimum path is reached will be ancestor.
     *
     * @param v - vertices v
     * @param w - vertices w
     * @return new arr[2], where arr[0] is shortest ancestral length, arr[1] is common ancestor
     */
    int[] checkWayFromVtoW(Iterable<Integer> v, Iterable<Integer> w){
        validateArg(v);
        validateArg(w);

        // use BreadthFirstDirectedPaths to find all possible paths from the vertex
        BreadthFirstDirectedPaths ways_v = new BreadthFirstDirectedPaths(net, v);
        BreadthFirstDirectedPaths ways_w = new BreadthFirstDirectedPaths(net, w);

        // check all vertex and find those that there are in one of the paths of both vertices
        int current_distance;
        int min_distance = INF;
        int ancestor = 0;
        for(int i = 0; i < net.V(); ++i){
            if(ways_v.hasPathTo(i) && ways_v.distTo(i) < INF && ways_w.hasPathTo(i) && ways_w.distTo(i) < INF){
                current_distance = ways_v.distTo(i) + ways_w.distTo(i);
                if(min_distance > current_distance){
                    min_distance = current_distance;
                    ancestor = i;
                }
            }
        }
        return min_distance != INF ? new int[]{min_distance, ancestor} : new int[]{-1, -1};
    }


    //Check the argument with type int for the correctness
    void validateArg(int v){
        if(v < 0 || v >= net.V()) throw new IllegalArgumentException("Argument is not in the range 0 to n - 1");
    }

    //Check the argument with iterable type for the correctness
    void validateArg(Iterable<Integer> v){
        if(v == null) throw new IllegalArgumentException("Argument is null");
        Iterator<Integer> it = v.iterator();
        while(it.hasNext()){
            int i = it.next();
            if(i < 0 && i >= net.V()) throw new IllegalArgumentException("Argument is not in the range 0 to n - 1");
        }
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
