package app;

public class test {
    private String date;
    private String venue_id;

    public test() {
    }

    public test(String date, String venue_id) {
        this.date = date;
        this.venue_id = venue_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }
}
