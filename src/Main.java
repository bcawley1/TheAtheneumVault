import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.Book;
import util.Library;
import util.Note;
import util.Range;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
    private Library library;
    private Book selectedBook;
    private SplitPane pageSplit;
    private Note selectedNote;
    private String currentFilter = "";
    private String search = "";

    @Override
    public void start(Stage stage) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        library = objectMapper.readValue(new File("library/library.json"), Library.class);

        VBox page = new VBox();
        pageSplit = new SplitPane();
        pageSplit.setPrefHeight(2000);
        pageSplit.setDividerPositions(0.25, 0.75);

        updateTags();

        updateBookView();

        updateDetails();

        Label name = new Label("The Atheneum Vault");
        name.setTextFill(Color.rgb(159, 159, 159));
        name.setFont(Font.font("System", 11));
        name.setPadding(new Insets(5));
        Label version = new Label("v1.0.0");
        version.setTextFill(Color.rgb(159, 159, 159));
        version.setFont(Font.font("System", 11));
        version.setPadding(new Insets(5));
        BorderPane info = new BorderPane();
        info.setLeft(name);
        info.setRight(version);
        Button important = new Button("Why is Reading Important?");
        important.setOnMouseClicked(mouseEvent -> {
            Stage stage1 = new Stage();
            Scene scene = new Scene(new Label("""
                    - Strengthened vocabulary to sound smarter to others
                                        
                    - Improved writing
                                        
                    - Gain ideas and improve creativity
                                        
                    - Memory - Workout for the brain
                                        
                    - Very cheap entertainment
                                        
                    - Develops imagination
                                         
                    Source: Grand Canyon University
                    """), 400, 230);
            stage1.setTitle("Why is Reading Important?");
            stage1.setScene(scene);
            stage1.show();
        });
        info.setCenter(important);

        page.getChildren().addAll(pageSplit, info);

        Scene scene = new Scene(page, 900, 600);
        stage.setTitle("The Atheneum Vault");
        stage.setScene(scene);
        stage.show();
    }

    public void updateBookView() {
        VBox booksView = new VBox();
        TextField search = new TextField(this.search);
        search.setPromptText("Search For A Book");
        search.setOnAction(actionEvent -> {
            currentFilter = "";
            this.search = search.getText();
            updateBookView();
        });

        GridPane books = new GridPane();
        books.setHgap(10);
        books.setVgap(10);
        ScrollPane sp = new ScrollPane(books);
        List<Book> booksFiltered;
        if (!currentFilter.isEmpty()) {
            booksFiltered = library.filterBooks(currentFilter);
        } else if (!this.search.isEmpty()) {
            booksFiltered = library.searchBooks(this.search);
        } else {
            booksFiltered = library.getBooks();
        }
//        List<Book> booksFiltered = currentFilter.isEmpty() ? library.getBooks() : library.filterBooks(currentFilter);
        for (int i = 0; i < booksFiltered.size(); i++) {
            Book book = booksFiltered.get(i);

            VBox vBox = new VBox();
            ImageView iv = new ImageView(getImage(book));
            iv.setOnMouseClicked(ev -> {
                selectedBook = book;
                updateDetails();
            });

            Label name = new Label(book.getName());

            vBox.getChildren().addAll(iv, name);
            books.add(vBox, i % 2, i / 2);
        }

        try {
            ImageView addBook = new ImageView(new Image(new File("Images/add.jpg").toURI().toURL().toString(), 200, 1000, true, true));
            addBook.setOnMouseClicked(mouseEvent -> {
                Book newBook = new Book("", "", "", "");
                library.addBook(newBook);
                selectedBook = newBook;
                updateBookView();
                updateDetails();
            });
            VBox vBox = new VBox(addBook);
            books.add(vBox, books.getChildren().size() % 2, books.getChildren().size() / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }


        booksView.getChildren().addAll(search, sp);
        if (pageSplit.getItems().size() == 3) {
            double split = pageSplit.getDividerPositions()[1];
            pageSplit.getItems().set(1, booksView);
            pageSplit.setDividerPosition(1, split);
        } else {
            pageSplit.getItems().add(booksView);
        }
    }

    private Image getImage(Book book) {
        Image image = null;
        try {
            if (new File(book.getImageURL()).exists()) {
                image = new Image(new File(book.getImageURL()).toURI().toURL().toString(), 200, 1000, true, true);
            } else {
                image = new Image(new File("Images/image_unknown.png").toURI().toURL().toString(), 200, 1000, true, true);
            }
        } catch (Exception e) {

        }
        return image;
    }

    public void updateTags() {
        double split = 0.25;
        VBox lists = new VBox();
        Button clear = new Button("No Filter");
        clear.setPrefWidth(1000);
        clear.setOnMouseClicked(mouseEvent -> {
            currentFilter = "";
            search = "";
            updateTags();
            updateBookView();
            updateDetails();
        });
        lists.getChildren().add(clear);
        for (String tag : library.getAllTags()) {
            Button button = new Button(tag);
            button.setPrefWidth(1000);
            button.setOnMouseClicked(mouseEvent -> {
                search = "";
                currentFilter = tag;
                updateTags();
                updateBookView();
                updateDetails();
            });

            lists.getChildren().add(button);
        }
        if (pageSplit.getItems().size() == 3) {
            split = pageSplit.getDividerPositions()[0];
            pageSplit.getItems().set(0, lists);
        } else {
            pageSplit.getItems().add(lists);
        }
        pageSplit.setDividerPosition(0, split);
    }

    public void updateDetails() {
        double split = 0.75;
        double scrollAmount = 0;
        if (pageSplit.getItems().size() == 3) {
            split = pageSplit.getDividerPositions()[1];
            scrollAmount = ((ScrollPane) pageSplit.getItems().get(2)).getVvalue();
            pageSplit.getItems().remove(2);
        }

        VBox details = new VBox();
        ImageView iv = new ImageView(getImage(selectedBook));
        details.getChildren().add(iv);
        Label title = new Label("Title:");
        TextField titleTF = new TextField(selectedBook == null ? "" : selectedBook.getName());
        titleTF.setPromptText("Title");

        Label author = new Label("Author:");
        TextField authorTF = new TextField(selectedBook == null ? "" : selectedBook.getAuthor());
        authorTF.setPromptText("Author");

        Label imageURL = new Label("Image URL:");
        TextField imageURLTF = new TextField(selectedBook == null ? "" : selectedBook.getImageURL());
        imageURLTF.setPromptText("Image URL");

        Label summary = new Label("Summary:");
        TextArea summaryTF = new TextArea(selectedBook == null ? "" : selectedBook.getSummary());
        summaryTF.setWrapText(true);
        summaryTF.setPrefHeight(100);
        summaryTF.setPromptText("Summary");

        Label pagesRead = new Label("Pages Read:");
        TextField pagesReadTF = new TextField(selectedBook == null ? "" : convertPagesRead(selectedBook.getReadPages()));
        pagesReadTF.setPromptText("Pages Read");

        Label tags = new Label("Tags:");
        TextField tagsTF = new TextField(selectedBook == null ? "" : selectedBook.getTags().toString().substring(1, selectedBook.getTags().toString().length() - 1));
        tagsTF.setPromptText("Tags");

        Label warning = new Label();
        warning.setTextFill(Color.RED);

        Button save = new Button("Save");
        save.setOnMouseClicked(mouseEvent -> {
            if (isValidPageNotation(pagesReadTF.getText())) {
                selectedBook.setName(titleTF.getText());
                selectedBook.setAuthor(authorTF.getText());
                selectedBook.setImageURL(imageURLTF.getText());
                selectedBook.setSummary(summaryTF.getText());
                selectedBook.setReadPages(convertPagesReadFromString(pagesReadTF.getText()));
                selectedBook.setTags(tagsTF.getText().isBlank() ? new ArrayList<>() : List.of(tagsTF.getText().split(", ")));

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    objectMapper.writeValue(new File("library/library.json"), library);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateDetails();
                updateBookView();
                updateTags();
            } else {
                warning.setText("Pages Read is Invalid!!!");
            }

        });


        Label newNote = new Label("New Note:");
        TextField pageNum = new TextField();
        pageNum.setPromptText("Page Number");
        TextArea noteTextArea = new TextArea();
        noteTextArea.setPromptText("Note");
        noteTextArea.setWrapText(true);
        noteTextArea.setPrefHeight(100);

        Button addNote = new Button("Add Note");
        addNote.setOnMouseClicked(mouseEvent -> {
            try {
                selectedBook.addNote(Integer.parseInt(pageNum.getText()), noteTextArea.getText());
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(new File("library/library.json"), library);

                updateDetails();
                updateBookView();
                updateTags();
            } catch (Exception ignored) {
            }
        });

        Button remove = new Button("Remove Book");
        remove.setOnMouseClicked(mouseEvent -> {
            library.getBooks().remove(selectedBook);
            selectedBook = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(new File("library/library.json"), library);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTags();
            updateBookView();
            updateDetails();
        });

        details.getChildren().addAll(remove, title, titleTF, author, authorTF, imageURL, imageURLTF, summary, summaryTF, pagesRead, pagesReadTF, tags, tagsTF, warning, save, newNote, pageNum, noteTextArea, addNote);
        if (selectedBook != null) {
            for (Note note : selectedBook.getNotes()) {
                VBox cool = new VBox();
                cool.setSpacing(0);
                HBox buttons = new HBox();
                buttons.setSpacing(0);

                Button button = new Button(String.valueOf(note.getPageNumber()));
//                button.setPrefWidth(100);
                button.setOnMouseClicked(mouseEvent -> {
                    selectedNote = note;
                    updateDetails();
                });

                Button removeNote = new Button("Remove");
//                removeNote.setPrefWidth(100);
                removeNote.setOnMouseClicked(mouseEvent -> {
                    selectedBook.removeNote(note);
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        objectMapper.writeValue(new File("library/library.json"), library);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateDetails();
                });

                buttons.getChildren().addAll(button, removeNote);
                cool.getChildren().add(buttons);

                if (note.equals(selectedNote)) {
                    TextArea noteText = new TextArea(note.getNote());
                    noteText.setWrapText(true);
                    noteText.setPrefHeight(100);
                    noteText.setPromptText("Note");
                    cool.getChildren().add(noteText);

                    Button saveNote = new Button("Save");
//                    saveNote.setPrefWidth(50);
                    saveNote.setOnMouseClicked(mouseEvent -> {
                        note.setNote(noteText.getText());
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            objectMapper.writeValue(new File("library/library.json"), library);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateDetails();
                    });
                    buttons.getChildren().add(saveNote);
                }
                details.getChildren().add(cool);
            }
        }

        ScrollPane sp = new ScrollPane(details);
        sp.setVvalue(scrollAmount);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setFitToWidth(true);

        pageSplit.getItems().add(sp);
        pageSplit.setDividerPosition(1, split);
    }

    public Set<Integer> convertPagesReadFromString(String s) {
        Set<Integer> set = new HashSet<>();

        s = s.replaceAll("\\s", "");
        String[] split = s.split(",");
        for (String string : split) {
            if (string.contains("-")) {
                int[] edges = Arrays.stream(string.split("-")).mapToInt(Integer::parseInt).toArray();
                for (int i = edges[0]; i <= edges[1]; i++) {
                    set.add(i);
                }
            } else {
                set.add(Integer.parseInt(string));
            }
        }
        return set;
    }

    public String convertPagesRead(Set<Integer> pages) {
        if(pages.isEmpty()){
            return "";
        }
        List<Integer> listPages = new ArrayList<>(pages);
        Collections.sort(listPages);
        List<Range> range = new ArrayList<>(List.of(new Range()));
        boolean inRange = false;
        int rangeIndex = 0;

        for (int i = 0; i < listPages.size(); i++) {
            if (!inRange) {
                if (listPages.get(range.get(rangeIndex).getStart()) == (listPages.get(i) - 1)) {
                    inRange = true;
                    range.get(rangeIndex).setEnd(i);
                } else {
                    range.get(rangeIndex).setStart(i);
                }
            } else {
                if (listPages.get(range.get(rangeIndex).getEnd()) == (listPages.get(i) - 1)) {
                    range.get(rangeIndex).setEnd(i);
                } else {
                    inRange = false;
                    rangeIndex++;
                    range.add(new Range());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listPages.size(); i++) {
            if (!inRange(range, i)) {
                sb.append("%d, ".formatted(listPages.get(i)));
            } else {
                Range range1 = range.get(rangeIndex(range, i));
                if (range1.getStart() == i) {
                    sb.append("%d-%d, ".formatted(listPages.get(range1.getStart()), listPages.get(range1.getEnd())));
                }
            }
        }
        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();
    }

    public boolean isValidPageNotation(String s) {
        String[] split = s.replaceAll("\\s", "").split(",");
        for (String string : split) {
            if (string.isBlank()) {
                return false;
            } else if (string.contains("-")) {
                String[] range = string.split("-");
                try {
                    Integer.parseInt(range[0]);
                    Integer.parseInt(range[1]);
                } catch (Exception e) {
                    return false;
                }
            } else {
                try {
                    Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean inRange(List<Range> ranges, int index) {
        for (Range range : ranges) {
            if (index >= range.getStart() && index <= range.getEnd()) {
                return true;
            }
        }
        return false;
    }

    private int rangeIndex(List<Range> ranges, int index) {
        for (int i = 0; i < ranges.size(); i++) {
            if (index >= ranges.get(i).getStart() && index <= ranges.get(i).getEnd()) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        launch(args);
    }
}