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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/api/books")
public class BookResource {

    @RestClient
    NumberProxy numberProxy;

    public List<Book> bookList = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        return Response.ok(bookList).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Book getBook(@PathParam("id") int id) {
        return bookList.get(id);
    }

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
        bookList.add(book);
        return Response.status(201).entity(book).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int id) {
        Optional<Book> bookToDelete = bookList.stream().filter(book -> book.getId() == id).findFirst();
        boolean isDeleted = false;
        if (bookToDelete.isPresent()) {
            isDeleted = bookList.remove(bookToDelete.get());
        }
        if (isDeleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
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
