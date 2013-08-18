

class Review {

    public String user_id;
    public String review_id;
    public String text;
    public String business_id;
    public int stars;
    public String date;
    public String type;

    public Review() {
    }

    @Override
    public String toString() {
        this.text = this.text.replaceAll("[\n\r]", "");
        return this.review_id + "\t"
                + this.business_id + "\t"
                + this.stars + "\t"
                + this.text;
    }
}