package POJOs;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class PurchasePojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchaseId")
    private int purchaseId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserPojo user;

    @ManyToOne
    @JoinColumn(name = "movieId")
    private MoviePojo movie;

    @Column(name = "purchaseDate", nullable = false)
    private Date purchaseDate;

    // Constructors, Getters, Setters
    public PurchasePojo(int purchaseId, UserPojo user, MoviePojo movie, Date purchaseDate) {
        this.purchaseId = purchaseId;
        this.user = user;
        this.movie = movie;
        this.purchaseDate = purchaseDate;
    }

    public PurchasePojo() { }

    public int getPurchaseId() { return purchaseId; }
    public void setPurchaseId(int purchaseId) { this.purchaseId = purchaseId; }

    public UserPojo getUser() { return user; }
    public void setUser(UserPojo user) { this.user = user; }

    public MoviePojo getMovie() { return movie; }
    public void setMovie(MoviePojo movie) { this.movie = movie; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
}
