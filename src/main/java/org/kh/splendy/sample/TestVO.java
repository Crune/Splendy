package org.kh.splendy.sample;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TestVO {

	private String id;
	private String pw;
	private int age;
	private String name;
	private Timestamp reg;
	
}