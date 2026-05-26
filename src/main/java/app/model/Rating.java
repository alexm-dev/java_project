package app.model;

import java.time.LocalDateTime;

/**
 * Rating class representing the ratings table in the database.
 */
public class Rating {
    private int id;
    private int bookingId;
    private int reviewerId;
    private Integer ratedUserId;
    private int ratingValue;
    private String comment;
    private LocalDateTime createdTime;

    /**
     * Constructor to load from the DB.
     */
    public Rating(int id, int bookingId, int reviewerId, Integer ratedUserId,
                  int ratingValue, String comment, LocalDateTime createdTime) {
        this.id = id;
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.ratedUserId = ratedUserId;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.createdTime = createdTime;
    }

    /**
     * Constructor to create a new Rating (id and createdTime are set by the DB).
     */
    public Rating(int bookingId, int reviewerId, Integer ratedUserId, int ratingValue, String comment) {
        this.bookingId = bookingId;
        this.reviewerId = reviewerId;
        this.ratedUserId = ratedUserId;
        this.ratingValue = ratingValue;
        this.comment = comment;
    }

    // Setter for the DB-generated id only, ratings are otherwise immutable
    public void setId(int id) { this.id = id; }

    // Getters
    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getReviewerId() { return reviewerId; }
    public Integer getRatedUserId() { return ratedUserId; }
    public int getRatingValue() { return ratingValue; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedTime() { return createdTime; }
}
