
package POJOs;

import javax.persistence.*;

@Entity
@Table (name="reviews")
public class ReviewPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "reviewId")
    private int reviewId;

    @Column (name = "rating")
    private int rating;
    @Column (name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserPojo user;

    @ManyToOne
    @JoinColumn(name = "movieId")
    private MoviePojo movie;

    public ReviewPojo(int reviewId, int rating, String description, UserPojo user, MoviePojo movie) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.description = description;
        this.user = user;
        this.movie = movie;
    }
    
    @Override
    public String toString() {
        return "Review ID: " + reviewId + ", Description: " + description + ", Rating: " + rating;
    }

    public ReviewPojo() {
        
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserPojo getUser() {
        return user;
    }

    public void setUser(UserPojo user) {
        this.user = user;
    }

    public MoviePojo getMovie() {
        return movie;
    }

    public void setMovie(MoviePojo movie) {
        this.movie = movie;
    }
    
    
}
