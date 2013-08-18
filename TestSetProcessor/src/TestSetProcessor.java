
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestSetProcessor {

    static ArrayList<Review> reviewArray = new ArrayList<Review>();
    static ArrayList<User> userArray = new ArrayList<User>();
    static ArrayList<Business> businessArray = new ArrayList<Business>();
    static ArrayList<String> restaurantBusinesses = new ArrayList<String>();
    private final String jsonSource;
    private final boolean sourceFromFile;

    public TestSetProcessor(String jsonSource, boolean sourceFromFile) {
        this.jsonSource = jsonSource;
        this.sourceFromFile = sourceFromFile;
    }

    public static void main(String[] args) {
        TestSetProcessor reviewData =
                new TestSetProcessor("yelp_test_set/yelp_test_set_review.json", true);
        TestSetProcessor businessData =
                new TestSetProcessor("yelp_test_set/yelp_test_set_business.json", true);
        TestSetProcessor userData =
                new TestSetProcessor("yelp_test_set/yelp_test_set_user.json", true);

        readUserData(userData);
        readBusinessData(businessData);

        //filter the business and review data so that it is just restaurants
        //System.out.println("Number of businesses: " + businessArray.size());
        for (Business b : businessArray) {
            for (String c : b.categories) {
                if (c.equals("Restaurants") || c.equals("Food")) {
                    restaurantBusinesses.add(b.business_id);
                }
            }
        }
        System.out.println("Number of restuarants: " + restaurantBusinesses.size());
        readReviewData(reviewData);
    }

    public static void readUserData(TestSetProcessor tsp) {
        try {
            Gson myGson = new Gson();
            JsonReader jsonReader = tsp.getJsonReader();

            JsonParser jsonParser = new JsonParser();

            for (int i = 0; i < 43872; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                User aUserEntry = myGson.fromJson(jo, User.class);
                userArray.add(aUserEntry);
            }
            System.out.println("Number of Users: " + userArray.size());
            printUserData();
        } catch (Exception e) {
        }
    }

    public static void readBusinessData(TestSetProcessor tsp) {
        try {
            Gson myGson = new Gson();
            JsonReader jsonReader = tsp.getJsonReader();

            JsonParser jsonParser = new JsonParser();

            for (int i = 0; i < 1204; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                Business aBusinessEntry = myGson.fromJson(jo, Business.class);
                businessArray.add(aBusinessEntry);
            }
            System.out.println("Number of Businesses: " + businessArray.size());
            printBusinessData();

        } catch (Exception e) {
        }
    }

    public static void readReviewData(TestSetProcessor tsp) {
         try {
            Gson myGson = new Gson();
            JsonReader jsonReader = tsp.getJsonReader();
            JsonParser jsonParser = new JsonParser();
            
            for (int i = 0; i < 22955; i++) {
                JsonObject jo = jsonParser.parse(jsonReader).getAsJsonObject();
                Review aReviewEntry = myGson.fromJson(jo, Review.class); 
                //System.out.println(aReviewEntry);
                if (restaurantBusinesses.contains(aReviewEntry.business_id)) {
                   // System.out.println(aReviewEntry);
                    reviewArray.add(aReviewEntry);
                }
            }
            System.out.println("Number of reviews: " + reviewArray.size());
            printReviewData();
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

    public static void printUserData() {
        try {
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

            //System.out.println("Done");
        } catch (Exception e) {
        }
    }

    public static void printReviewData() {
        try {
            File file = new File("review_object.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Review review : reviewArray) {
                bw.write(review.toString());
                bw.newLine();
            }
            bw.close();
            //System.out.println("Done. Size: " + reviewArray.size());
        } catch (Exception e) {
        }
    }

    public static void printBusinessData() {
        try {
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

            //System.out.println("Done");
        } catch (Exception e) {
        }
    }
}
