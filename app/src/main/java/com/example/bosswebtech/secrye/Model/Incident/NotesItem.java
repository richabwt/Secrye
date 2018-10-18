package com.example.bosswebtech.secrye.Model.Incident;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NotesItem implements Serializable{

	@SerializedName("note")
	private String note;

	@SerializedName("created_on")
	private String createdOn;

	@SerializedName("userimage")
	private String userimage;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("photos")
	private List<Object> photos;

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setCreatedOn(String createdOn){
		this.createdOn = createdOn;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public void setUserimage(String userimage){
		this.userimage = userimage;
	}

	public String getUserimage(){
		return userimage;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPhotos(List<Object> photos){
		this.photos = photos;
	}

	public List<Object> getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"NotesItem{" + 
			"note = '" + note + '\'' + 
			",created_on = '" + createdOn + '\'' + 
			",userimage = '" + userimage + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",photos = '" + photos + '\'' + 
			"}";
		}
}