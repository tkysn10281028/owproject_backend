package com.oysterworld.portfolio.owproject_backend.app.presentation.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogRequest {
    private String title;
    private String content;
}
