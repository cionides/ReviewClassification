import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The review processor class reads in all training data and outputs the User,
 * Business, and Review objects to files
 *
 * @author cionides
 */
public class ReviewProcessor {

    /**
     * Holds the list of review objects
     */
    public static ArrayList<Review> reviewArray = new ArrayList<Review>();
    /**
     * Holds the list of user objects
     */
    private static ArrayList<User> userArray = new ArrayList<User>();
    /**
     * Holds the list of business objects
     */
    private static ArrayList<Business> businessArray = new ArrayList<Business>();
    /**
     * Holds a list of all businesses that have the category "Restaurant" or
     * "Food"
     */
    private static ArrayList<String> restaurantBusinesses = new ArrayList<String>();
    /**
     * Holds the source of the JSON files
     */
    private final String jsonSource;
    /**
     * boolean value to determine source
     */
    private final boolean sourceFromFile;
    /**
     * The key is the useful vote count, and the value is the number of reviews
     * with that count
     */
    private static HashMap<Integer, Integer> uvMap = new HashMap<Integer, Integer>();
    /**
     *
     */
    private static HashMap<String, ArrayList<String>> bidToRid = new HashMap<String, ArrayList<String>>();
    /**
     *
     */
    private static ArrayList<String> topThird = new ArrayList<String>();
    /**
     *
     */
    private static ArrayList<String> bottomThird = new ArrayList<String>();
    /**
     *
     */
    private static boolean DESC = false;
    /**
     *
     */
    public static HashMap<String, Review> ridToReview = new HashMap<String, Review>();

    /**
     * Constructor for the Review Processor class
     *
     * @param jsonSource
     * @param sourceFromFile
     */
    public ReviewProcessor(String jsonSource, boolean sourceFromFile) {
        this.jsonSource = jsonSource;
        this.sourceFromFile = sourceFromFile;

    }

    public static void main(String[] args) {
        //CREATE REVIEW PROCESSOR INSTANES FOR EACH OBJECT TYPE
        ReviewProcessor businessData =
                new ReviewProcessor("yelp_training_set/yelp_training_set_business.json", true);
        ReviewProcessor userData =
                new ReviewProcessor("yelp_training_set/yelp_training_set_user.json", true);
        ReviewProcessor reviewData =
                new ReviewProcessor("yelp_training_set/yelp_training_set_review.json", true);

        //Read and print the User, Business, and Review Objects
        readUserData(userData);
        readBusinessData(businessData);
        readReviewData(reviewData);

        //Print just the text of the reviews
        //printReviews();

        //BREAK THE TEXT UP TO BE TAGGED BY STANFORD CORE NLP
        //batchFileCreation();

        //PRINT THE USEFUL VOTE COUNT DISTRIBUTION
        //printUsefulVoteCountDist();
    }

    private static void printReviews() {
        try {
            File file = new File("review_content.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Review review : reviewArray) {
                String txt = review.text.replaceAll("[\n\r]", "");
                bw.write(txt);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param objType
     */
    private static void printData(String objType) {
        try {
            if (objType.equals("user")) {
                File file = new File("user_object.txt");
                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (User user : userArray) {

                    bw.write(user.toString());
                    bw.newLine();
                }
                bw.close();

            }
            if (objType.equals("business")) {
                File file = new File("business_object.txt");
                // if file doesnt exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                for (Business business : businessArray) {

                    bw.write(business.toString());
                    bw.newLine();
                }
                bw.close();

            }

            if (objType.equals("review")) {


                File file = new File("review_object.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for (Review review : reviewArray) {
                    if (bottomThird.contains(review.review_id) || topThird.contains(review.review_id)) {

                        bw.write(review.toString());
                        bw.newLine();

                    }
                }
                bw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param rd is a ReviewProcessor object that has the JSON review file as
     * it's source
     */
    private static void readUserData(ReviewProcessor rd) {
        try {
            Gson myGson = new Gson();
            JsonReader jsonReader = rd.getJsonReader();

            JsonParser jsonParser = new JsonParser();
            for (int i = 0; i < 43872; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                User aUserEntry = myGson.fromJson(jo, User.class);
                userArray.add(aUserEntry);
            }
            printData("user");

        } catch (Exception e) {
        }
    }

    /**
     * Read the JSON business objects in and creates a Business object for each
     * one Then calls the print function
     *
     * @param rd
     */
    public static void readBusinessData(ReviewProcessor rd) {
        try {
            Gson myGson = new Gson();
            JsonReader jsonReader = rd.getJsonReader();

            JsonParser jsonParser = new JsonParser();

            for (int i = 0; i < 11536; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                Business aBusinessEntry = myGson.fromJson(jo, Business.class);
                businessArray.add(aBusinessEntry);
            }
            printData("business");
            System.out.println("Number of Businesses : " + businessArray.size());
        } catch (Exception e) {
        }
    }

    /**
     * Read the JSON review objects in and extracts only the Restaurant and Food
     * reviews Creates a Review Object for each one and then calls the print
     * function
     *
     * @param rd
     */
    public static void readReviewData(ReviewProcessor rd) {
        //filter the business and review data so that it is just restaurants
        try {
            for (Business b : businessArray) {
                for (String c : b.categories) {
                    if (c.equals("Restaurants") || c.equals("Food")) {
                        restaurantBusinesses.add(b.business_id);
                    }
                }
            }
            System.out.println("Number of Businesses that are Restaurants : " + restaurantBusinesses.size());

            Gson myGson = new Gson();
            JsonReader jsonReader = rd.getJsonReader();
            JsonParser jsonParser = new JsonParser();
            for (int i = 0; i < 178743; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                Review aReviewEntry = myGson.fromJson(jo, Review.class);
                if (restaurantBusinesses.contains(aReviewEntry.business_id)) {
                    reviewArray.add(aReviewEntry);
                    ridToReview.put(aReviewEntry.review_id, aReviewEntry);
                }
            }
            System.out.println("Initial Number of Reviews : " + reviewArray.size());
            getTopAndBottomReviews();
            printData("review");


        } catch (Exception e) {
        }

    }

    public static void getTopAndBottomReviews() {
        //loop through all review entries and add them to the appropriate business review list

        for (Review r : reviewArray) {
            String rid = r.review_id;
            String bid = r.business_id;
            if (bidToRid.containsKey(bid)) {
                ArrayList<String> tempRidList = bidToRid.get(bid);
                tempRidList.add(rid);
                bidToRid.put(bid, tempRidList);
            } else {
                ArrayList<String> ridList = new ArrayList<String>();
                ridList.add(rid);
                bidToRid.put(bid, ridList);
            }

        }
        //for each business get the set of reviews
        System.out.println("Number of businesses with reviews: " + bidToRid.size());
        for (String bid : bidToRid.keySet()) {
            //pull set of reviews for each business
            ArrayList<String> reviewSet = bidToRid.get(bid);
            //if the set of reviews is greater than three
            int size = reviewSet.size();
            if (size > 3) {
                //get top third
                int oneThird = size / 3;
                //create a hashMap of rid and useful rating for the review set
                HashMap<String, Integer> reviewRating = new HashMap<String, Integer>();
                int goodReviewCount = 0;
                int badReviewCount = 0;

                for (String reviewID : reviewSet) {
                    Review review = ridToReview.get(reviewID);
                    int rating = review.usefulVotes;
                    reviewRating.put(reviewID, rating);
                    if (rating >= 1) {
                        goodReviewCount++;
                    }
                    if (rating == 0) {
                        badReviewCount++;
                    }
                }

                reviewRating = sortByComparator(reviewRating, DESC);
                //if there are fewere good or bad reviews than oneThird, take that number
                //from the top and bottom
                if ((goodReviewCount < oneThird && goodReviewCount > 0)|| (badReviewCount < oneThird && badReviewCount >= 0)) {
                    if (goodReviewCount < badReviewCount) {
                        oneThird = goodReviewCount;
                    }
                    if (badReviewCount < goodReviewCount) {
                        oneThird = badReviewCount;
                    }
                    if (badReviewCount == goodReviewCount) {
                        oneThird = badReviewCount;
                    }
                }
                int len = reviewRating.size();
                String[] reviewIDs = reviewRating.keySet().toArray(new String[len]);

                for (int i = 0; i < oneThird; i++) {
                    bottomThird.add(reviewIDs[i]);
                    topThird.add(reviewIDs[len - (i + 1)]);
                }
            }
        }
        System.out.println("Number of reviews that make up the top third: " + topThird.size());
        System.out.println("Number of reviews that make up the bottom third: " + bottomThird.size());
        System.out.println("Total number of reviews to be tagged: " + (topThird.size()+bottomThird.size()));
    }

    public static HashMap<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * Chunks the review content up into batch files for tagging and prints them
     */
    public static void batchFileCreation() {
        try {
            //create random access file
            RandomAccessFile raf = new RandomAccessFile("review_content.txt", "r");
            //initialize the position to the start of the file
            long pos = 0;
            for (int i = 0; i < 499; i++) {
                raf.seek(pos);

                //create an ArrayList to read in to
                ArrayList<String> review_part = new ArrayList<String>();

                for (int j = 0; j < 277; j++) {
                    review_part.add(raf.readLine());
                }

                //write the filled array to a new file
                String fn = ("batch_files/review_part" + (i + 1) + ".txt");
                File f = new File(fn);

                FileWriter fw = new FileWriter(f.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for (String s : review_part) {

                    bw.write(s);
                    bw.newLine();
                }
                bw.close();
                //set pos to the finish point
                pos = raf.getFilePointer();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Obtain the JsonReader for the given source details.
     *
     * @return the JsonReader instance.
     * @throws FileNotFoundException.
     */
    public JsonReader getJsonReader() throws FileNotFoundException {
        JsonReader reader = null;
        if (sourceFromFile) {
            reader = new JsonReader(
                    new InputStreamReader(new FileInputStream(this.jsonSource)));
        }
        return reader;
    }

    /**
     * Prints the useful vote count distribution
     */
    private static void printUsefulVoteCountDist() {

        for (Review r : reviewArray) {
            int usefulVotes = r.votes.get("useful").getAsInt();

            if (uvMap.containsKey(usefulVotes)) {
                int tmpCount = uvMap.get(usefulVotes);
                uvMap.put(usefulVotes, tmpCount + 1);
            } else {
                uvMap.put(usefulVotes, 1);
            }
        }

        try {
            File file = new File("uvMap.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Integer i : uvMap.keySet()) {
                bw.write(i + " " + uvMap.get(i));
                bw.newLine();
            }
            bw.close();

        } catch (Exception e) {
        }

    }
}
