package features;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class reads in the Review objects, and parses out their features
 */
public class FeaturesLoader {

    /**
     * This hash map contains all of the Feature objects for each Review object.
     * The key is the Review object's review ID and the value is the Feature object for that review.
     */
    private static final HashMap<String, Features> reviewFeatures = new HashMap<String, Features>();
    
    /**
     * ArrayList to hold the top noun phrases. 
     */
    public static ArrayList<String> topNP = new ArrayList<String>();
    
    /**
     * ArrayList to hold the stopwords read in from file.
     */
    public static ArrayList<String> stopWords = new ArrayList<String>();
   
    /**
     * Variable used to specify the ordering when sorting a hashmap. 
     */
    public static boolean DESC = false;

    
    public static void main(String[] args) {
        
        readUtilFiles();    
        readReviewFeatures();
        printReviewFeatures();

    }

    /**
     * This method generates a text file of review features, 
     * which are the Review objects' feature table.
     */
    public static void printReviewFeatures() {

        try {

            File file = new File("review_feautures.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String rid : reviewFeatures.keySet()) {

                bw.write(reviewFeatures.get(rid).toString());
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This utility method reads in the text file containing top noun phrases 
     * and the text file containing stop words. They are both stored in Array Lists
     */
    public static void readUtilFiles() {
        try {
            File f = new File("top_np.txt");
            File sw = new File("stopwords.txt");
            BufferedReader in = new BufferedReader(new FileReader(f));
            BufferedReader sIn = new BufferedReader(new FileReader(sw));
            String line = null;
            String swLine = null;

            while ((line = in.readLine()) != null) {
                topNP.add(line);
            }
            
            while ((swLine = sIn.readLine()) != null) {
                stopWords.add(swLine);
            }

        } catch (Exception e) {
        }
    }

    /**
     * This method reads in the text file containing all of the previously 
     * generated Review objects and parses out the values needed for a new Features object.
     * Each review object has a Features object created, and then the Features object is 
     * added to a HashMap where the key is the review ID, and the value is the Feature object.
     */
    public static void readReviewFeatures() {
        int count = 0;
        try {
            File f = new File("review_object.txt");
            BufferedReader in = new BufferedReader(new FileReader(f));
            String line = null;

            while ((line = in.readLine()) != null) {
                count++;
                String[] reviewTokens = line.split("\\t");
                String rid = reviewTokens[0];
                String bid = reviewTokens[1];
                int strs = Integer.parseInt(reviewTokens[2]);
                int uv = Integer.parseInt(reviewTokens[3]);
                String txt = reviewTokens[4];
                int len = getReviewLen(txt);

                ArrayList<Integer> np = getNounPhraseBitVector(txt);
                Features ft = new Features(strs, np, bid, len, uv);
                reviewFeatures.put(rid, ft);
            }

        } catch (Exception e) {
        }
        System.out.println("Count: " + count);
    }

    /**
     * This method checks to see if a word is part of the top NP list.
     * 
     * @param text the review text passed in 
     * @return an array list of 1s and 0s representing the text as top NP or not top NP
     */
    public static ArrayList<Integer> getNounPhraseBitVector(String text) {
        ArrayList<Integer> isNP = new ArrayList<Integer>();
        for (String n : topNP) {
            if (text.contains(n)) {
                isNP.add(1);
            } else {
                isNP.add(0);
            }
        }
        return isNP;
    }

    /**
     * Get the review object's text length.
     * 
     * @param text The text of a review as one String
     * @return the length of non-stopword tokens in a review's text
     */
    public static int getReviewLen(String text) {
        ArrayList<String> reviewTokens = new ArrayList<String>();
        String[] tokens = text.split(" ");
        for (String t : tokens) {
            if (stopWords.contains(t)) {
                t = t.replace(t, "");

            }
            if (!t.isEmpty()) {
                reviewTokens.add(t);
            }
        }
        int len = reviewTokens.size();

        return len;
    }
}

