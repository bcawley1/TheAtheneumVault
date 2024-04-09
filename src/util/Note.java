package util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Note {
    @JsonProperty
    private int pageNumber;
    @JsonProperty
    private String note;

    private Note(){}

    Note(int pageNumber, String note) {
        this.pageNumber = pageNumber;
        this.note = note;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
