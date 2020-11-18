package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book implements Serializable {
    /**
     * 	id int primary key auto_increment comment "ID",
     * 	card_id varchar(20) comment "图书编号",
     * 	search_id varchar(30) comment "检索号",
     * 	book_name varchar(50) comment "书名",
     * 	cid int comment "类别",
     * 	author varchar(20) comment "作者",
     * 	cover varchar(100) comment "封面",
     * 	press varchar(50) comment "出版社",
     * 	press_date date comment "出版时间",
     * 	book_desc varchar(250) comment "书的描述",
     * 	book_shelf int comment "所在书架",
     * 	book_floor int comment "所在楼层",
     * 	count int comment "书的数量" default 0,
     * 	price double comment "书的价格" default 0,
     * 	is_lend int comment "是否可借，0表示不可借，1表示可借" default 0,
     * 	day_click_count int comment "每日点击量" default 0,
     * 	month_click_count int comment "每月点击量" default 0,
     * 	total_click_count int comment "总点击量" default 0,
     * 	publish_date date comment "上架时间"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cardId;
    private String searchId;
    private String bookName;
    private Integer cid;
    private String author;
    private String cover;
    private String press;
    private String pressDate;
    private String bookDesc;
    private Integer bookShelf;
    private Integer bookFloor;
    private Integer count;
    private Double price;
    private Integer isLend;
    private Integer dayClickCount;
    private Integer monthClickCount;
    private Integer totalClickCount;
    private String publishDate;

    @Transient
    private com.library.pojo.Category category;

    public Book(Integer id){
        this.id = id;
    }



}
