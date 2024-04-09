import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.Book;
import util.Library;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends Application {
    private Library library;
    private Book selectedBook;
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        library = objectMapper.readValue(new File("library/library.json"), Library.class);

        VBox page = new VBox();
        SplitPane pageSplit = new SplitPane();
        pageSplit.setPrefHeight(580);

        VBox lists = new VBox();
        for (String tag : library.getAllTags()) {
            Button button = new Button(tag);
            button.setPrefWidth(1000);

            lists.getChildren().add(button);
        }
        pageSplit.setDividerPositions(0.25, 0.75);
        pageSplit.getItems().add(lists);

        //TODO: READ VALUES FROM BOOKS AND DISPLAY

        VBox booksView = new VBox();
        TextField search = new TextField();
        search.setPromptText("Search For A Book");

        GridPane books = new GridPane();
        books.setHgap(10);
        books.setVgap(10);
        ScrollPane sp = new ScrollPane(books);
        for (int i = 0; i < library.getBooks().size(); i++) {
            Book book = library.getBooks().get(i);

            VBox vBox = new VBox();
            Image image = new Image(new File(book.getImageURL()).toURI().toURL().toString(), 200, 1000, true, true);
            ImageView iv = new ImageView(image);
            iv.setOnMouseClicked(ev -> {
                double split = pageSplit.getDividerPositions()[1];
                selectedBook = book;
                pageSplit.getItems().remove(2);
                pageSplit.getItems().add(getDetails());
                pageSplit.setDividerPosition(1, split);
            });

            Label name = new Label(book.getName());

            vBox.getChildren().addAll(iv, name);
            books.add(vBox, i%2, i/2);
        }

        booksView.getChildren().addAll(search, sp);
        pageSplit.getItems().add(booksView);


        //TODO: details

        pageSplit.getItems().add(getDetails());

        Label name = new Label("The Atheneum Vault");
        name.setTextFill(Color.rgb(159,159,159));
        name.setFont(Font.font("System", 11));
        name.setPadding(new Insets(5));
        Label version = new Label("v0.1.0 -beta");
        version.setTextFill(Color.rgb(159,159,159));
        version.setFont(Font.font("System", 11));
        version.setPadding(new Insets(5));
        BorderPane info = new BorderPane();
        info.setLeft(name);
        info.setRight(version);

        page.getChildren().addAll(pageSplit, info);

        Library library = new Library();
        library.addBook(new Book("Intro to java programming", "Daniel", "An introduction to java programming for beginners", "Images/IntroJava.jpg"));
        library.getBooks().get(0).addTag("John Porks");
        library.getBooks().get(0).addNote(67, "I HATE PATRICK");
        library.getBooks().get(0).addReadPage(7);

//        objectMapper.writeValue(new File("library/library.json"), library);

        Scene scene = new Scene(page, 900, 600);
        stage.setTitle("The Atheneum Vault");
        stage.setScene(scene);
        stage.show();
    }

    public VBox getDetails(){
        VBox details = new VBox();
        Label title = new Label("Title:");
        TextField titleTF = new TextField(selectedBook == null ? "" : selectedBook.getName());
        titleTF.setPromptText("Title");

        Label author = new Label("Author:");
        TextField authorTF = new TextField(selectedBook == null ? "" : selectedBook.getAuthor());
        authorTF.setPromptText("Author");

        details.getChildren().addAll(title, titleTF, author, authorTF);

        return details;
    }

    public static void main(String[] args) {
        launch(args);
    }
}