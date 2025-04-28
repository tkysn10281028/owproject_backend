package com.oysterworld.portfolio.owproject_backend.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogModel{
    private String id;
    private String title;
    private String content;
    private String innerId;
}
