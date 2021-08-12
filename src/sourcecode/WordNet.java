package sourcecode;

import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final Digraph wordNet;
    // sMap contains pairs noun as key and Bag as value that consists of an ID, which noun is contains
    private final HashMap<String, Bag<Integer>> sMap;
    // sSet contains pairs noun as key and String as value that consists of synset noun
    private final HashMap<Integer, String> sSet;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        sMap = new HashMap<String, Bag<Integer>>();
        sSet = new HashMap<Integer, String>();
        int count = countSynsets(synsets);
        wordNet = new Digraph(count);
        fillWordNet(hypernyms);

        //Check that WordNet is a rooted DAG
        DirectedCycle check = new DirectedCycle(wordNet);
        if(check.hasCycle()) throw new IllegalArgumentException("Digraph has cycle.");
        sap = new SAP(wordNet);
        //Check that WordNet is singly rooted DAG
        int root = 0;
        for(int i = 0; i < count; ++i) if(wordNet.outdegree(i) == 0) root++;
        if(root != 1) throw  new IllegalArgumentException("Digraph has " + root + "roots.");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return sMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if(word == null) throw new IllegalArgumentException("Argument is null");
        return sMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if(isNoun(nounA) || isNoun(nounB)) throw new IllegalArgumentException("Argument is null");

        return sap.length(sMap.get(nounA), sMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if(isNoun(nounA) || isNoun(nounB)) throw new IllegalArgumentException("Argument is null");
        SAP sap = new SAP(wordNet);
        int ancestor = sap.ancestor(sMap.get(nounA), sMap.get(nounB));
        return sSet.get(ancestor);
    }


    /**
     * Fills sSet, where the key is the ID of the line, and value is the current synset
     * Fills sMap, where the key is the noun in synset, and value is Bag consisting of id where this noun occurs.
     * @param synsets
     * @return the number of synsets
     */
    int countSynsets(String synsets) {
        if (synsets == null) throw new IllegalArgumentException("Argument is null");
        In in = new In(synsets);
        int count = 0;
        while (in.hasNextLine()){
            count++;
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            sSet.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String noun : nouns) {
                if (sMap.get(noun) != null) {
                    Bag<Integer> bag = sMap.get(noun);
                    bag.add(id);
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    sMap.put(noun, bag);
                }
            }
        }
        return count;
    }

    //Build WordNet
    void fillWordNet(String hypernyms){
        if (hypernyms == null) throw new IllegalArgumentException("Argument is null");
        In in = new In(hypernyms);
        while(in.hasNextLine()){
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            for(int i = 1; i < parts.length; ++i){
                int hypernym = Integer.parseInt(parts[i]);
                wordNet.addEdge(id, hypernym);
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}
