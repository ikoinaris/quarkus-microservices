package org.ikoinaris.microservices;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;

@Path("/api/books")
public class BookResource {

    @RestClient
    NumberProxy numberProxy;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Creates new book")
    @Fallback(fallbackMethod = "fallingBackOnCreatingBook")
    @Retry(maxRetries = 3, delay = 3000)
    public Response createBook(@FormParam("title") String title,
                               @FormParam("author") String author,
                               @FormParam("year") int yearOfPublication,
                               @FormParam("genre") String genre) {
        Book book = new Book();
        book.isbn13 = numberProxy.generateIsbnNumbers().isbn13;
        book.title = title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.creationDate = Instant.now();
        book.genre = genre;
        return Response.status(201).entity(book).build();
    }

    public Response fallingBackOnCreatingBook(String title, String author, int yearOfPublication, String genre) {
        Book book = new Book();
        book.isbn13 = "Will be set later";
        book.title = title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.creationDate = Instant.now();
        book.genre = genre;
        saveBookOnDisc(book);
        return Response.status(206).entity(book).build();
    }

    private void saveBookOnDisc(Book book) {
        String bookJson = JsonbBuilder.create().toJson(book);
        try (PrintWriter out = new PrintWriter("book-" + Instant.now().toEpochMilli() + ".json")) {
            out.println(bookJson);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
