package com.example.hello.domain.book.example;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class BookDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String titleKorean;

        @NotBlank
        private String titleEnglish;

        @NotBlank
        private String description;

        @NotBlank
        private String author;

        @NotBlank
        private String isbn;

        @NotBlank
        private String publishDate;
    }

    @Getter
    @Builder
    public static class Patch {
        @Setter
        private long bookId;
        private String titleKorean;
        private String titleEnglish;
        private String description;
        private String author;
        private String isbn;
        private String publishDate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long bookId;
        private String titleKorean;
        private String titleEnglish;
        private String description;
        private String author;
        private String isbn;
        private String publishDate;
    }
}
