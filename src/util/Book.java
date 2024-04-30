package util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Book {
    @JsonProperty
    private String name;
    @JsonProperty
    private String author;
    @JsonProperty
    private String summary;
    @JsonProperty
    private String imageURL;
    @JsonProperty
    private Set<Integer> readPages;
    @JsonProperty
    private List<Note> notes;
    @JsonProperty
    private List<String> tags;

    private Book(){}
    public Book(String name, String author, String summary, String imageURL) {
        this.name = name;
        this.author = author;
        this.summary = summary;
        this.imageURL = imageURL;
        readPages = new HashSet<>();
        notes = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Set<Integer> getReadPages() {
        return readPages;
    }
    public boolean hasReadPage(int page){
        return readPages.contains(page);
    }

    public void setReadPages(Set<Integer> readPages) {
        this.readPages = readPages;
    }

    public void addReadPage(int page){
        readPages.add(page);
    }

    public void removeReadPage(int page){
        readPages.remove(page);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(int pageNumber, String note){
        notes.add(new Note(pageNumber, note));
    }

    public void removeNote(Note note){
        notes.remove(note);
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public void removeTag(String tag){
        tags.remove(tag);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
