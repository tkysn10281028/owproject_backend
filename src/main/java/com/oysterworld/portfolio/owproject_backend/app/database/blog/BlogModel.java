package com.oysterworld.portfolio.owproject_backend.app.database.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BlogModel {
    private String id;
    private String title;
    private String content;
    private String innerId;
}
