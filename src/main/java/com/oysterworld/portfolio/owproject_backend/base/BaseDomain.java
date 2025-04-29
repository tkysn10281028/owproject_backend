package com.oysterworld.portfolio.owproject_backend.base;

public abstract class BaseDomain {
    /** データベース接続用Model */
    public abstract class Model {}
    
    /** リクエスト受け取り用DTO */
    public abstract class Request {}

    /** レスポンス返却用DTO */
    public abstract class Response {}
}
