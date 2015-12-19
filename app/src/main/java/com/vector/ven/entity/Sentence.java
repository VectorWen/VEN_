package com.vector.ven.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 封装句子属性
 *
 *  2015年12月19日20:58:49
 * @author vector
 * 
 */
public class Sentence extends DataSupport implements Serializable{

	public static Sentence createByJson(String json) {
		try {
			return new Gson().fromJson(json,Sentence.class);
		}catch (Exception e){
			return null;
		}
	}

	private int id;
	
	/**
	 * mp3 下载路径
	 */
	private String tts;

	/**
	 * 英文句子
	 */
	private String content;

	/**
	 * 中文解析
	 */
	private String note;

	/**
	 * 小图片下载路径
	 */
	private String picture;

	/**
	 * 大图片下载路径
	 */
	private String picture2;

	/**
	 * 分享图片
	 */
	@SerializedName("fenxiang_img")
	private String shareImg;

	/**
	 * 日期，也是mp3 文件名称
	 */
	private String dateline;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShareImg() {
		return shareImg;
	}

	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	public String getTts() {
		return tts;
	}

	public void setTts(String tts) {
		this.tts = tts;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

}
