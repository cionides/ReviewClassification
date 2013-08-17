
import com.google.gson.JsonObject;

class Review {  
    public JsonObject votes;
    public String user_id;
    public String review_id;
    public String business_id;
    public int stars;
    public String date;
    public String text;
    
    public int usefulVotes;
   
    public Review() {
        
    }

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