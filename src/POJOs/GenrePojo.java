
package POJOs;

import java.util.List;
import javax.persistence.*;

@Entity
@Table (name="genres")
public class GenrePojo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "genreId")
    private int genreId;

    @Column (name = "genreName")
    private String genreName;

    @ManyToMany(mappedBy = "genres")
    private List<MoviePojo> movies;

    public GenrePojo(int genreId, String genreName, List<MoviePojo> movies) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.movies = movies;
    }

    public GenrePojo() {
        
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<MoviePojo> getMovies() {
        return movies;
    }

    public void setMovies(List<MoviePojo> movies) {
        this.movies = movies;
    }
    
    @Override
    public String toString() {
        return genreName;  // Assuming genreName is the name of the genre
    }
}
