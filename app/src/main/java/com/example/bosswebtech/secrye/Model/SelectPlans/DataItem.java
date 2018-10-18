package com.example.bosswebtech.secrye.Model.SelectPlans;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("armed_guard")
	private String armedGuard;

	@SerializedName("unarmed_guard")
	private String unarmedGuard;

	@SerializedName("price")
	private String price;

	@SerializedName("is_selected")
	private String isSelected;

	@SerializedName("description")
	private Object description;

	@SerializedName("id")
	private String id;

	@SerializedName("plan")
	private String plan;

	@SerializedName("status")
	private String status;

	public void setArmedGuard(String armedGuard){
		this.armedGuard = armedGuard;
	}

	public String getArmedGuard(){
		return armedGuard;
	}

	public void setUnarmedGuard(String unarmedGuard){
		this.unarmedGuard = unarmedGuard;
	}

	public String getUnarmedGuard(){
		return unarmedGuard;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setIsSelected(String isSelected){
		this.isSelected = isSelected;
	}

	public String getIsSelected(){
		return isSelected;
	}

	public void setDescription(Object description){
		this.description = description;
	}

	public Object getDescription(){
		return description;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPlan(String plan){
		this.plan = plan;
	}

	public String getPlan(){
		return plan;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"armed_guard = '" + armedGuard + '\'' + 
			",unarmed_guard = '" + unarmedGuard + '\'' + 
			",price = '" + price + '\'' + 
			",is_selected = '" + isSelected + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",plan = '" + plan + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}