package hex.action.examples;

/**
 * Created by jason on 14-12-14.
 */
public class Movie {
    private int id;

    private String title;

    private int releaseYear;

    private String director;

    private String refid;

    private Person[] stars;

    private int[] starIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public Person[] getStars() {
        return stars;
    }

    public void setStars(Person[] stars) {
        this.stars = stars;
    }

    public int[] getStarIds() {
        return starIds;
    }

    public void setStarIds(int[] starIds) {
        this.starIds = starIds;
    }
}
