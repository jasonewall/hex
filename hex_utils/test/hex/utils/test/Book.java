package hex.utils.test;

import hex.utils.generics.CollectionType;

import java.util.List;

/**
 * Created by jason on 14-12-14.
 */
public class Book {
    private int id;

    private String title;

    private int publishYear;

    private Person author;

    @CollectionType(Person.class)
    private List<Person> characters;

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

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public List<Person> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Person> characters) {
        this.characters = characters;
    }
}
