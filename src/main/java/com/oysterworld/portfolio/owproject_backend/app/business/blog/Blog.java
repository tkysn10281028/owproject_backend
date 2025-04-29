package com.oysterworld.portfolio.owproject_backend.app.business.blog;

import com.oysterworld.portfolio.owproject_backend.base.BaseDomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog extends BaseDomain {
    private String id;
    private String title;
    private String content;
    private String innerId;

    /**
     * データベース接続用Model
     */
    @Getter
    @AllArgsConstructor
    public static class Model {
        private String id;
        private String title;
        private String content;
        private String innerId;
    }

    /**
     * リクエスト受け取り用DTO
     */
    @Getter
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String content;
    }

    /**
     * レスポンス返却用DTO
     */
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String content;
    }

    /**
     * 変換用メソッド
     */
    public Model toModel() {
        return new Model(id, title, content, innerId);
    }

    public Response toResponse() {
        return new Response(id, title, content);
    }

    public Request toRequest() {
        return new Request(title, content);
    }

    public static Blog fromModel(Model model) {
        return new Blog(model.id, model.title, model.content, model.innerId);
    }
}
