package app.model;

public class Rating {
    private int id;
    private int bookingId;
    private int reviewerId;
    private Integer ratedUserId;
    private int ratingValue;
    private String comment;
    private String createdTime;

    public Rating(int id, int bookingId, int reviewerId, Integer ratedUserId, int ratingValue, String comment, String createdTime) {
        this.id = id;
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.ratedUserId = ratedUserId;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.createdTime = createdTime;
    }

    public Rating(int bookingId, int reviewerId, Integer ratedUserId, int ratingValue, String comment) {
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.ratedUserId = ratedUserId;
        this.ratingValue = ratingValue;
        this.comment = comment;
    }

    // Getters
    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getReviewerId() { return reviewerId; }
    public Integer getRatedUserId() { return ratedUserId; }
    public int getRatingValue() { return ratingValue; }
    public String getComment() { return comment; }
    public String getCreatedTime() { return createdTime; }
}
