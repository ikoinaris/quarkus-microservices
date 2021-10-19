package org.ikoinaris.microservices;

import java.time.Instant;

public class IsbnNumbers {

    public String isbn13;
    public String isbn10;

    public Instant generationDate;

    @Override
    public String toString() {
        return "IsbnNumbers{" +
                "isbn13='" + isbn13 + '\'' +
                ", isbn10='" + isbn10 + '\'' +
                ", generationDate=" + generationDate +
                '}';
    }
}
