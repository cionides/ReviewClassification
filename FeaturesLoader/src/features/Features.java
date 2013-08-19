package features;

import java.util.ArrayList;

/**
 * This class generates a Features object which is created for all relevant
 * review objects. These objects are then printed out in the format of a
 * features table to be used as input in RapidMiner
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
     * Constructor for the Features class 
     *
     * @param sr is the star rating
     * @param np is a bit vector of top noun phrases
     * @param bid is the business id of the review object
     * @param len is the length of non-stopword tokens in a review
     * @param ur is the useful rating of a review
     */
    public Features(int sr, ArrayList<Integer> np, String bid, int len, int ur) {
        this.starRating = sr;
        this.nounPhrases = np;
        this.length = len;
        this.usefulRating = ur;
        
        //To create a "classification" of reviews; -1 being not useful and 0 being useful
        //Primarily to make for easier reading in RapidMiner
        if (this.usefulRating == 0) {
            this.useful = -1;
        }
        if (this.usefulRating >= 1) {
            this.useful = 1;
        }
        this.business_id = bid;
    }

    /**
     *
     * @return a String representing a Feature object's content.  
     * The star rating, length of the text, a bit array of noun phrases, 
     * and the binary value of "useful" or "not useful"
     */
    @Override
    public String toString() {
        String np = this.nounPhrases.get(0) + "";
        for (int i = 1; i < nounPhrases.size(); i++) {
            np = np + "," + nounPhrases.get(i);
        }
        return this.starRating + ","
                + this.length + ","
                + np + ","
                + this.useful;
    }
}

