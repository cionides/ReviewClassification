import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestSetFeatureLoader {

    public static HashMap<String, Features> reviewFeatures = new HashMap<String, Features>();
    public static HashMap<String, Integer> usefulVotes = new HashMap<String, Integer>();
    public static HashMap<String, Integer> totalNP = new HashMap<String, Integer>();
    public static HashMap<String, Integer> npPerReview = new HashMap<String, Integer>();
    public static ArrayList<String> topNP = new ArrayList<String>();
    public static ArrayList<String> stopWords = new ArrayList<String>();   

    public static void main(String[] args) {
        //read in nounPhrases
        readUtilFiles();
        //read in review data
        readReviewFeatures();
        //print features table
        printReviewFeatures();
        
    }
    
    public static void printReviewFeatures() {
        try {
            //System.out.println("Review Featuers " + reviewFeatures.size());
            File file = new File("review_feauturesTest.txt");

            // if file doesnt exists, then create it
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
            //read the stop words in
            while ((swLine = sIn.readLine()) != null) {
                stopWords.add(swLine);
            }

        } catch (Exception e) {
        }
    }

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
                String txt = reviewTokens[3];

                int len = getReviewLen(txt);

                ArrayList<Integer> np = getNPList(txt);
                Features ft = new Features(strs, np, bid, len);
                reviewFeatures.put(rid, ft);                 
                
            }
        } catch (Exception e) {
        }
        //System.out.println("Count: " + count);
    }

    public static ArrayList<Integer> getNPList(String text) {
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

class Features {

    public int starRating;
    public ArrayList<Integer> nounPhrases;
    public int totalNounPhrases;
    public int length;
    public int usefulRating;
    public int useful;
    public String business_id;
    

    public Features(int sr, ArrayList<Integer> np, String bid, int len) {
        this.starRating = sr;
        this.nounPhrases = np;
        this.length = len;
        this.business_id = bid;
    }

    @Override
    public String toString() {
        String np = this.nounPhrases.get(0) + "";
        for (int i = 1; i < nounPhrases.size(); i++) {
            np = np + "," + nounPhrases.get(i);
        }
        return this.starRating + ","
                + this.length + ","
                + np;
    }
}
