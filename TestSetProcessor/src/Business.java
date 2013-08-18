
import java.util.ArrayList;

class Business {
    //DATA MEMBERS   
    public String city;
    public int reviewCount;
    public String name;
    public String[] neighborhoods;
    public String type;
    public String business_id;
    public String full_address;
    public String state;
    public float longitude;    
    public float stars;
    public float latitude;
    public boolean open;
    public String[] categories;
    
    public static ArrayList<String> restaurantBusinessIDs;

    //CTOR
    public Business() {
    }

    @Override
    public String toString() {

        ArrayList<String> cat = new ArrayList<String>();
        for(String c : this.categories) {
            cat.add(c);         
        }
        return this.business_id + "\t"
                + cat + "\t"
                + this.city + "\t"
                + this.state + "\t"
                + this.latitude + "\t"
                + this.longitude + "\t"
                + this.stars + "\t"
                + this.reviewCount;
    }
}
