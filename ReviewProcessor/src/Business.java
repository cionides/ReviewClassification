
import java.util.ArrayList;

class Business {
    
    public String business_id;
    public String full_address;
    public boolean open;
    public String[] categories;
    public String city;
    public int reviewCount;
    public String name;
    public String[] neighborhoods;
    public float longitude;
    public String state;
    public float stars;
    public float latitude;
    public String type;

    //CTOR
    public Business() {
    }

    @Override
    public String toString() {
        ArrayList<String> cat = new ArrayList<String>();
        for (String c : this.categories) {
            cat.add(c);
        }
        return this.business_id + "\t"
                + cat + "\t"
                + this.stars + "\t"
                + this.reviewCount + "\t";
    }
}
