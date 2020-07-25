package com.yzit.shop.entity;
import com.yzit.framework.web.ui.BaseEntity;

/**
 * 
 * <br>
 * <b>功能：</b>用户表实体类<br>
 * <b>作者：</b>Administrator<br>
 * <b>日期：</b> 2020-06-12 <br>
 * <b>版权所有： 2020，云优众<br>
 */ 
public class User extends BaseEntity {
   private static final long serialVersionUID = 1L;
    private Integer  id;//
    private String  username;//用户名
    private String  password;//密码，加密存储
    private String  phone;//注册手机号
    private String  email;//注册邮箱
    private java.util.Date  created;//
    private java.util.Date  updated;//
    
   	public Integer getId() {
		return id;
	}
	public void setId(Integer  id) {
		this.id = id;
	}
	
   	public String getUsername() {
		return username;
	}
	public void setUsername(String  username) {
		this.username = username;
	}
	
   	public String getPassword() {
		return password;
	}
	public void setPassword(String  password) {
		this.password = password;
	}
	
   	public String getPhone() {
		return phone;
	}
	public void setPhone(String  phone) {
		this.phone = phone;
	}
	
   	public String getEmail() {
		return email;
	}
	public void setEmail(String  email) {
		this.email = email;
	}
	
   	public java.util.Date getCreated() {
		return created;
	}
	public void setCreated(java.util.Date  created) {
		this.created = created;
	}
	
   	public java.util.Date getUpdated() {
		return updated;
	}
	public void setUpdated(java.util.Date  updated) {
		this.updated = updated;
	}
	
}