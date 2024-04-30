# The Atheneum Vault
## A simple book management system using Java and JavaFX

## Features
- **Storing Books:** Users can store book information such as title, author, and summary.
- **Keeping Track Of Pages:** The application can keep track of read pages that the user has input.
- **Note Taking:** Notes can be inputted for a page by the user, and saved for future use.
- **Saving Data:** Using a JSON file and Jackson, books created by the user will be automatically saved.

## Building and Running
1. Download the source code of the app.
2. Open up the project using an IDE.
3. Import the required libraries(See Below)
4. Run the program.

## Required Libraries
- [JavaFX 21](https://gluonhq.com/products/javafx/)
- [Jackson Core 2.17.0](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core/2.17.0)
- [Jackson Databind 2.17.0](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind/2.17.0)
- [Jackson Annotations 2.17.0](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations/2.17.0)

## User Manual
- ### Adding a Book
    1. Click the plus image at the bottom of the book grid
    2. Fill out the information of the book in the information tab
    3. Click save
- ### Removing a Book
    1. Select the book you want to remove
    2. Click the remove button at the bottom in the information tab
- ### Editing a Book
    1. Select the book you want to edit
    2. Edit the information in the information tab
    3. Click save
- ### Filtering by a Tag
    1. Click on the tag you want to filter by on the left
    2. To clear the filter, click `No Filter` at the top of the filters list
