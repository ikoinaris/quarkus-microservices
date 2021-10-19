package org.ikoinaris.microservices;

import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

@Path("/api/books")
public class BookResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Creates new book")
    public Response createBook(@FormParam("title") String title,
                               @FormParam("author") String author,
                               @FormParam("year") int yearOfPublication,
                               @FormParam("genre") String genre) {
        Book book = new Book();
        book.isbn13 = "13- We get it from Number Microservice";
        book.title = title;
        book.author = author;
        book.yearOfPublication = yearOfPublication;
        book.creationDate = Instant.now();
        book.genre = genre;
        return Response.status(201).entity(book).build();
    }
}
