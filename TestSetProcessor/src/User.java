
class User {

    private int review_count;
    private String name;
    private float average_stars;
    private String user_id;
    private String type;
      
    
    public User() {
    }

    @Override
    public String toString() {
        return  this.review_count + "\t"
                + this.average_stars+ "\t"
                + this.user_id;
                

    }
}