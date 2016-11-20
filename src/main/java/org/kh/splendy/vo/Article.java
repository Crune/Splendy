package org.kh.splendy.vo;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class Article {
	private int at_id; 
    private int u_id;
	private int bd_id; 
    private String at_subject;    
    private String at_content;
    private String at_pass;
    private Timestamp at_reg_date;
    private int at_readcount;
    private String at_ip;
    private String at_category;
}
