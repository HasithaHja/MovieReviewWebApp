
package POJOs;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table (name="movies")
public class MoviePojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "movieId")
    private int movieId;
    
    @Column (name = "movieName")
    private String movieName;
    @Column (name = "director")
    private String director;
    @Column (name = "runtime")
    private int runtime;
    
    @Column(name = "releaseDate")
    @Temporal(TemporalType.DATE) // Use Temporal annotation to specify that it's a date
    private Date releaseDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "movie_genre",
        joinColumns = @JoinColumn(name = "movieId"),
        inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<GenrePojo> genres;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReviewPojo> reviews;

    public MoviePojo(int movieId, String movieName, String director, int runtime, List<GenrePojo> genres, 
            List<ReviewPojo> reviews, Date releaseDate) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.director = director;
        this.runtime = runtime;
        this.genres = genres;
        this.reviews = reviews;
        this.releaseDate = releaseDate;
    }

    public MoviePojo() {
        
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<GenrePojo> getGenres() {
        return genres;
    }

    public void setGenres(List<GenrePojo> genres) {
        this.genres = genres;
    }

    public List<ReviewPojo> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewPojo> reviews) {
        this.reviews = reviews;
    }
    
    public double getAverageRating() {
    if (reviews == null || reviews.isEmpty()) {
        return 0.0; // No reviews, so average rating is 0
    }

    // Calculate the sum of all ratings
    int totalRating = 0;
    for (ReviewPojo review : reviews) {
        totalRating += review.getRating();
    }

    // Calculate the average rating
    return (double) totalRating / reviews.size();
}


    public String getId() {
        return null;
        
    }

    public String getTitle() {
        return null;
        
    }

    public void setTitle(String title) {
        
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
     
}
