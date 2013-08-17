import com.google.gson.JsonObject;

class User {

    private String type;
    private String user_id;
    private String name;
    private int review_count;
    private float average_stars;
    private JsonObject votes;

    public User() {
    }

    @Override
    public String toString() {
        
        return  this.user_id + "\t"
                + this.review_count + "\t"
                + this.average_stars + "\t"
                + this.votes + "\t";


    }
}
