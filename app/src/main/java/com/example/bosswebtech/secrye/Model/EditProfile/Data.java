package com.example.bosswebtech.secrye.Model.EditProfile;


import com.google.gson.annotations.SerializedName;


public class Data{

	@SerializedName("zip")
	private Object zip;

	@SerializedName("member_type_id")
	private String memberTypeId;

	@SerializedName("address")
	private String address;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("city")
	private Object city;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private Object mobile;

	@SerializedName("state")
	private Object state;

	@SerializedName("email")
	private String email;

	@SerializedName("userimage")
	private String userimage;

	public void setZip(Object zip){
		this.zip = zip;
	}

	public Object getZip(){
		return zip;
	}

	public void setMemberTypeId(String memberTypeId){
		this.memberTypeId = memberTypeId;
	}

	public String getMemberTypeId(){
		return memberTypeId;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setCity(Object city){
		this.city = city;
	}

	public Object getCity(){
		return city;
	}

	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(Object mobile){
		this.mobile = mobile;
	}

	public Object getMobile(){
		return mobile;
	}

	public void setState(Object state){
		this.state = state;
	}

	public Object getState(){
		return state;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUserimage(String userimage){
		this.userimage = userimage;
	}

	public String getUserimage(){
		return userimage;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"zip = '" + zip + '\'' + 
			",member_type_id = '" + memberTypeId + '\'' + 
			",address = '" + address + '\'' + 
			",user_id = '" + userId + '\'' + 
			",city = '" + city + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",name = '" + name + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",state = '" + state + '\'' + 
			",email = '" + email + '\'' +
					",userimage = '" + userimage + '\'' +
					"}";
		}
}