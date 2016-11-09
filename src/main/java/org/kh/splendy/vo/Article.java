package org.kh.splendy.vo;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class Article {
	private int at_id; 
    private int u_id;
	private int bd_id; 
    private String subject;    
    private String content;
    private String pass;
    private Timestamp reg_date;
    private int readcount;
    private String ip;
    private int reply;
    private int re_step;	
    private int re_level;
    private String category;
}
