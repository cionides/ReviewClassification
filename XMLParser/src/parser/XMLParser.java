package parser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class is used to parse out the noun phrases from the XML formatted Review text.
 * The text has been tagged with POS tags using the Stanford Core NLP POS tagger. 
 * 
 * @author cionides
 */
public class XMLParser {

    /**
     * This holds the noun phrases extracted from all tagged Review text.
     * The key is the noun phrase and the value is the count-i.e the number of times
     * that noun phrase appears in all review text.
     */
    public static HashMap<String, Integer> nounPhraseMap = new HashMap<String, Integer>();
    /**
     * This value determines the order that a hash map will e sorted.
     */
    public static boolean DESC = false;
    /**
     * This arraylist holds the tagged string.
     */
    public static ArrayList<String> taggedString = new ArrayList<String>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //read in all xml files
        for (int i = 1; i < 100; i++) {
            String fileName = "xml_output/review_part" + i + ".txt.xml";
            readXML(fileName);
        }
       
        printParsedXML(taggedString);
        processNounPhrases();

        //remove plurals(when singular exists) and remove singulars (when plural already exists)
        nounPhraseMap = removePlurals(nounPhraseMap);
        nounPhraseMap = sortByComparator(nounPhraseMap, DESC);
        printNounPhraseMap(nounPhraseMap);
    }

    /**
     * Combines the singular and plural versions of noun phrases.
     * 
     * @param npm is the noun phrase map
     * @return a map of noun phrases and their count without plurals.
     */
    public static HashMap<String, Integer> removePlurals(HashMap<String, Integer> npm) {
        ArrayList<String> toRemove = new ArrayList<String>();
        HashMap<String, Integer> toAddFreq = new HashMap<String, Integer>();
        for (String s : npm.keySet()) {
            String tmp = s + "s";

            if (npm.containsKey(tmp)) {

                int tmpFreq = npm.get(tmp);
                int sFreq = npm.get(s);
                if (sFreq > tmpFreq) {

                    toRemove.add(tmp);
                    toAddFreq.put(s, sFreq + tmpFreq);
                } else if (tmpFreq > sFreq) {

                    toRemove.add(s);
                    toAddFreq.put(tmp, sFreq + tmpFreq);
                } else {

                    toRemove.add(s);
                    toAddFreq.put(s, sFreq + tmpFreq);
                }
            }
        }

        for (String tr : toRemove) {
            npm.remove(tr);
        }
        for (String ta : toAddFreq.keySet()) {
            npm.put(ta, toAddFreq.get(ta));
        }
        return npm;
    }

    /**
     * For each XML file, this method is called and any tagged noun phrases are
     * read in and added to the class level var "taggedString".
     * 
     * @param fileName is the name of the XML file to be read in.
     */
    public static void readXML(String fileName) {

        try {
            //System.out.println(fileName);
            File f = new File(fileName);
            BufferedReader in = new BufferedReader(new FileReader(f));

            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.contains("<parse>")) {
                    line = line.replace("<parse>", "");
                    line = line.replace("</parse>", "");
                    line = line.replaceAll("[\n\r]", "");

                    ArrayList<String> tags = getNestedTags(line);
                    for (String t : tags) {
                        taggedString.add(t);
                    }
                }
            }
            in.close();
        } catch (Exception e) {
        }
    }

    /**
     * This parses out the nested tags(the noun phrases within noun phrases).
     * 
     * @param s is the tagged string to be parsed 
     * @return the ArrayList with ALL tags.
     */
    public static ArrayList<String> getNestedTags(String s) {

        ArrayList<String> tags = new ArrayList<String>();

        int rightParenCount = 0;
        int leftParenCount = 0;

        int leftIndex = -1;
        int rightIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            //if it is the first left paren
            if (s.charAt(i) == '(' && s.charAt(i + 1) == 'N' && s.charAt(i + 2) == 'P'
                    && leftParenCount == 0) {
                leftParenCount++;
                leftIndex = i;

            }
            if (s.charAt(i) == '(' && s.charAt(i + 1) == 'N' && s.charAt(i + 2) == 'P'
                    && leftParenCount > 0) {
                leftParenCount++;
            }
            if (s.charAt(i) == ')' && leftParenCount > 0) {
                rightParenCount++;
                //check to see if right - left = 0
                if (rightParenCount - leftParenCount == 0) {
                    rightIndex = i;
                    String ss = s.substring(leftIndex, rightIndex + 1);
                    tags.add(ss);
                    //reset values for index and counts
                    leftParenCount = 0;
                    rightParenCount = 0;
                }

            } else {
            }
        }
        return tags;
    }

    /**
     * Prints the noun phrase map, the noun phrases and their respective counts.
     * 
     * @param npm is the map of noun phrases and their counts. 
     */
    public static void printNounPhraseMap(HashMap<String, Integer> npm) {
        try {
            File file = new File("nounPhraseMap.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String np : npm.keySet()) {
                bw.write(np + " " + "Freq: " + " " + npm.get(np));
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorts a hashmap by the value, so in this case the noun phrase map
     * will be sorted in ascending order.
     * 
     * @param unsortMap the unsorted hash map of noun phrases and their counts
     * @param order the order it is to be sorted in
     * @return a sorted hash map of noun phrases and their counts
     */
    private static HashMap<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order) {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * prints the parsed xml, so the noun phrases.
     * 
     * @param taggedSentences the sentences that were tagged as noun phrases
     */
    public static void printParsedXML(ArrayList<String> taggedSentences) {
        try {
            File file = new File("tagged_review_sentences.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String review : taggedSentences) {

                bw.write(review);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Break this method into multiple methods, create util method for reading files in
     * Once the tagged text has been pulled from the XML files, the punctuation is stripped
     * everything is lower case, and all stopwords are discounted. lastly, the remaining noun phrases
     * are added to the map.
     */
    public static void processNounPhrases() {
        //have an array list of all tags
        ArrayList<String> posTags = new ArrayList<String>();
        ArrayList<String> nounPhrases = new ArrayList<String>();
        ArrayList<String> stopWords = new ArrayList<String>();

        ArrayList<String> preProcessedNPs = new ArrayList<String>();
        ArrayList<String> processedNPs = new ArrayList<String>();
        try {
            File pos = new File("posTags.txt");
            File nps = new File("tagged_review_sentences.txt");
            File sw = new File("stopwords.txt");
            BufferedReader posIn = new BufferedReader(new FileReader(pos));
            BufferedReader npIn = new BufferedReader(new FileReader(nps));
            BufferedReader sIn = new BufferedReader(new FileReader(sw));
            String pLine = null;
            String nLine = null;
            String swLine = null;
            //read the pos tags in
            while ((pLine = posIn.readLine()) != null) {
                posTags.add(pLine);
            }
            //read all the noun phrases in 
            while ((nLine = npIn.readLine()) != null) {
                nounPhrases.add(nLine);
            }
            //read the stop words in
            while ((swLine = sIn.readLine()) != null) {
                stopWords.add(swLine);
            }
        } catch (Exception e) {
        }

        //iterate over each noun phrase in the review
        for (String np : nounPhrases) {
            String[] tokens = np.split(" ");
            for (String t : tokens) {
                preProcessedNPs.add(t);
            }
        }
        for (String nounPhrase : preProcessedNPs) {
            //remove parens           
            if (nounPhrase.contains("(")) {
                nounPhrase = nounPhrase.replace("(", "");
            }
            if (nounPhrase.contains(")")) {
                nounPhrase = nounPhrase.replace(")", "");
            }
            for (String pt : posTags) {
                if (nounPhrase.equals(pt)) {
                    nounPhrase = nounPhrase.replace(pt, "");
                }
            }

            //remove remaining punctuation, spaces, etc
            nounPhrase = stripPunctuation(nounPhrase);
            //remove all white spaces, tabs, new lines
            nounPhrase = nounPhrase.replaceAll("\\s", "");
            //make lower case
            nounPhrase = nounPhrase.toLowerCase();
            //remove stop words

            for (String sw : stopWords) {
                if (nounPhrase.equals(sw)) {
                    nounPhrase = nounPhrase.replace(sw, "");
                }
            }
            //remove one letter words
            if (nounPhrase.length() == 1) {
                nounPhrase = nounPhrase.replace(nounPhrase, "");
            }
            //remove any empty strings
            if (nounPhrase.isEmpty()) {
            } //finally, add each processed noun phrase to final list of noun phrases
            else {
                processedNPs.add(nounPhrase);
            }
        }
        for (String p : processedNPs) {
            if (nounPhraseMap.containsKey(p)) {
                nounPhraseMap.put(p, nounPhraseMap.get(p) + 1);
            } else {
                nounPhraseMap.put(p, 1);
            }
        }
    }

    /**
     * Method to strip all punctuation from a noun phrase.
     * 
     * @param s the string to remove punctuation from.
     * @return the cleaned string.
     */
    public static String stripPunctuation(String s) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) >= 65 && s.charAt(i) <= 90) || (s.charAt(i) >= 97
                    && s.charAt(i) <= 122)) {

                sb = sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }
}
