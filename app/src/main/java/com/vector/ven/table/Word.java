package com.vector.ven.table;

import org.litepal.crud.DataSupport;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/19
 * Description:<p>{TODO: 用一句话描述}
 */
public class Word extends DataSupport{

    private int id;
    private String query;//需要查询的单词
    private String json;//有道翻译翻译回来的json 数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
