package util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Library {
    @JsonProperty
    List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public List<Book> filterBooks(String tag){
        return books.stream()
                .filter(b -> b.hasTag(tag))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String name){
        return books.stream()
                .filter(b -> b.getName().contains(name))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public Set<String> getAllTags(){
        Set<String> tags = new HashSet<>();
        books.forEach(book -> tags.addAll(book.getTags()));
        return tags;
    }
}
