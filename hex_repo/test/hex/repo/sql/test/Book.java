package hex.repo.sql.test;

import hex.repo.sql.Metadata;

/**
 * Created by jason on 14-10-26.
 */
public class Book {
    private int id;

    private String title;

    private int publishedYear;

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

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public static Metadata metadata() {
        Book keyRecord = new Book();
        keyRecord.setId(1);
        keyRecord.setTitle("title");
        keyRecord.setPublishedYear(2);
        Metadata metadata = new Metadata(keyRecord);
        metadata.registerField(1, "id");
        metadata.registerField("title", "title");
        metadata.registerField(2, "published_year");
        return metadata;
    }
}
