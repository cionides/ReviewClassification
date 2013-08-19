package data;


import com.google.gson.JsonObject;

/**
 * Class for Review objects. 
 * 
 * @author cionides
 */
public class Review {  
    
    //TODO: Make all fields private
    public JsonObject votes;
    public String user_id;
    public String review_id;
    public String business_id;
    public int stars;
    public String date;
    public String text;
    
    public int usefulVotes;
   
    //TODO: Create accessor and modifier methods for all fields.  
    //Have all modifier methods perform error checking.
    
    /**
     * CTOR
     */
    public Review() {
        
    }

    /**
     * Overrides the toString method and prints: useful vote count,
     * reviewID, businessID, star rating, and the text of the of the review instance.
     * @return 
     */
    @Override
    public String toString() {
         usefulVotes = this.votes.get("useful").getAsInt();
        text = text.replaceAll("[\n\r]", "");
        return this.review_id + "\t"
                + this.business_id + "\t"
                + this.stars + "\t"
                + usefulVotes + "\t"
                + this.text;             
    }
}