package com.vector.ven.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * 有道翻译得到的结果
 *
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class YouWord {

    private int errorCode;
    private String[] translation; //精简的翻译
    private String query;
    private Basic basic;
    private List<Web> web;

    public static YouWord createByJson(String json){
        try {
            return new Gson().fromJson(json,YouWord.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getTranslation() {
        return translation;
    }

    public void setTranslation(String[] translation) {
        this.translation = translation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

    public static class Basic{

        @SerializedName("uk-phonetic")
        private String ukPhonetic;//音标
        @SerializedName("us-phonetic")
        private String usPhonetic;//音标

        private String[] explains;//翻译

        public String getUkPhonetic() {
            return ukPhonetic;
        }

        public void setUkPhonetic(String ukPhonetic) {
            this.ukPhonetic = ukPhonetic;
        }

        public String getUsPhonetic() {
            return usPhonetic;
        }

        public void setUsPhonetic(String usPhonetic) {
            this.usPhonetic = usPhonetic;
        }

        public String[] getExplains() {
            return explains;
        }

        public void setExplains(String[] explains) {
            this.explains = explains;
        }
    }

    //网络扩展
    public static class Web{
        private String key;
        private String[] value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String[] getValue() {
            return value;
        }

        public void setValue(String[] value) {
            this.value = value;
        }
    }

}
