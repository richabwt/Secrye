package com.example.bosswebtech.secrye.Model.Incident;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem implements Serializable{

	@SerializedName("client_lat")
	private String clientLat;

	@SerializedName("is_accepted")
	private String isAccepted;

	@SerializedName("notes")
	private List<NotesItem> notes;

	@SerializedName("create_time")
	private String createTime;

	@SerializedName("is_assigned")
	private String isAssigned;

	@SerializedName("id")
	private String id;

	@SerializedName("client_address")
	private String clientAddress;

	@SerializedName("client_lng")
	private String clientLng;

	@SerializedName("client_name")
	private String clientName;

	@SerializedName("incident")
	private String incident;

	@SerializedName("incident_status")
	private String incidentStatus;

	public void setClientLat(String clientLat){
		this.clientLat = clientLat;
	}

	public String getClientLat(){
		return clientLat;
	}

	public void setIsAccepted(String isAccepted){
		this.isAccepted = isAccepted;
	}

	public String getIsAccepted(){
		return isAccepted;
	}

	public void setNotes(List<NotesItem> notes){
		this.notes = notes;
	}

	public List<NotesItem> getNotes(){
		return notes;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setIsAssigned(String isAssigned){
		this.isAssigned = isAssigned;
	}

	public String getIsAssigned(){
		return isAssigned;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setClientAddress(String clientAddress){
		this.clientAddress = clientAddress;
	}

	public String getClientAddress(){
		return clientAddress;
	}

	public void setClientLng(String clientLng){
		this.clientLng = clientLng;
	}

	public String getClientLng(){
		return clientLng;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}

	public String getClientName(){
		return clientName;
	}

	public void setIncident(String incident){
		this.incident = incident;
	}

	public String getIncident(){
		return incident;
	}

	public void setIncidentStatus(String incidentStatus){
		this.incidentStatus = incidentStatus;
	}

	public String getIncidentStatus(){
		return incidentStatus;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"client_lat = '" + clientLat + '\'' + 
			",is_accepted = '" + isAccepted + '\'' + 
			",notes = '" + notes + '\'' + 
			",create_time = '" + createTime + '\'' + 
			",is_assigned = '" + isAssigned + '\'' + 
			",id = '" + id + '\'' + 
			",client_address = '" + clientAddress + '\'' + 
			",client_lng = '" + clientLng + '\'' + 
			",client_name = '" + clientName + '\'' + 
			",incident = '" + incident + '\'' + 
			",incident_status = '" + incidentStatus + '\'' + 
			"}";
		}
}