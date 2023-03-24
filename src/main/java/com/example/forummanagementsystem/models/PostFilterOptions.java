package com.example.forummanagementsystem.models;

import java.time.LocalDateTime;
import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<Long> rating;
    private Optional<LocalDateTime> createDateTime;
    private Optional<LocalDateTime> updateDateTime;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;


    public PostFilterOptions(){
        this(null, null, null, null, null, null, null);
    }

    public PostFilterOptions(
            String title,
            String content,
            Long rating,
            LocalDateTime createDate,
            LocalDateTime updateDate,
            String sortBy,
            String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.rating = Optional.ofNullable(rating);
        this.createDateTime = Optional.ofNullable(createDate);
        this.updateDateTime = Optional.ofNullable(updateDate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<Long> getRating() {
        return rating;
    }

    public Optional<LocalDateTime> getCreateDateTime() {
        return createDateTime;
    }

    public Optional<LocalDateTime> getUpdateDateTime() {
        return updateDateTime;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
