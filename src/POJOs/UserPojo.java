
package POJOs;

import java.util.List;
import javax.persistence.*;

@Entity
@Table (name="users")
public class UserPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "userId")
    private int userId;

    @Column (name = "userName", nullable = false)
    private String userName;
    @Column (name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_movie",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "movieId")
    )
    private List<MoviePojo> purchasedMovies;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PurchasePojo> purchases; 

    public List<PurchasePojo> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchasePojo> purchases) {
        this.purchases = purchases;
    }

    public UserPojo(List<PurchasePojo> purchases) {
        this.purchases = purchases;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewPojo> reviews;

    public UserPojo(int userId, String userName, String email, 
            List<MoviePojo> purchasedMovies, List<ReviewPojo> reviews, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.purchasedMovies = purchasedMovies;
        this.reviews = reviews;
        this.password = password;
    }

    public UserPojo() {
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MoviePojo> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(List<MoviePojo> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public List<ReviewPojo> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewPojo> reviews) {
        this.reviews = reviews;
    }
    
}
