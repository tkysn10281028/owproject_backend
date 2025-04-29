package com.oysterworld.portfolio.owproject_backend.app.presentation.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BlogRequest {
    private String title;
    private String content;
}
