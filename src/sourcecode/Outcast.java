package sourcecode;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Outcast {
    private final WordNet wordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int distance = distanceSum(outcast, nouns);
        int currDistance;
        for (String noun: nouns) {
            currDistance = distanceSum(noun, nouns);
            if (currDistance > distance) {
                distance = currDistance;
                outcast = noun;
            }
        }
        return outcast;
    }

    // Counts the total distance from currNoun to nouns
    private int distanceSum(String currNoun, String[] nouns) {
        int distance = 0;
        for (String noun: nouns) {
            distance += wordnet.distance(noun, currNoun);
        }
        return distance;
    }

    // see test client below
    public static void main(String[] args) {

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
