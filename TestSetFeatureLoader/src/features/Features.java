package features;

import java.util.ArrayList;

/**
 * This class defines the Feature object which is created for every Review instance.
 * It is comprised of the star rating, a bit vector representing the top used noun phrases in 
 * "useful" reviews, the length of non-stopword tokens, and the business id for that review.
 * 
 * @author cionides
 */
class Features {

    public int starRating;
    public ArrayList<Integer> nounPhrases;
    public int totalNounPhrases;
    public int length;
    public int usefulRating;
    public int useful;
    public String business_id;
    

    /**
     * Constructor for Feature object.
     * 
     * @param sr is the star rating
     * @param np is the top noun phrase bit vector
     * @param bid is the business id
     * @param len is the length of non-stopword tokens in a Review instance
     */
    public Features(int sr, ArrayList<Integer> np, String bid, int len) {
        this.starRating = sr;
        this.nounPhrases = np;
        this.length = len;
        this.business_id = bid;
    }

    /**
     * Includes the star rating and top noun phrase bit vector.
     * 
     * @return the String representation of a Feature object
     */
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
