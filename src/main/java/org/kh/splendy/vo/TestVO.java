package org.kh.splendy.vo;

import java.sql.Timestamp;

public class TestVO {

	private String id;
	private String pw;
	private int age;
	private String name;
	private Timestamp reg;
	
	@Override
	public String toString() {
		return "Test [id=" + id + ", pw=" + pw + ", age=" + age + ", name=" + name + ", reg=" + reg + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getReg() {
		return reg;
	}
	public void setReg(Timestamp reg) {
		this.reg = reg;
	}
}