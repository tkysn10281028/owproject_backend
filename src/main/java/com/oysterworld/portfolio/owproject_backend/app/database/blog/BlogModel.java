package com.oysterworld.portfolio.owproject_backend.app.database.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogModel {
    private String id;
    private String title;
    private String content;
    private String innerId;
}
