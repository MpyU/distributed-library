package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "book")
@Document(indexName = "book_index",type = "book")
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
    @org.springframework.data.annotation.Id
    private Integer id;
    private String cardId;
    @Field
    private String searchId;
    @Field
    private String bookName;
    @Field
    private Integer cid;
    @Field
    private String author;
    @Field
    private String cover;
    @Field
    private String press;
    @Field
    private String pressDate;
    @Field
    private String bookDesc;
    @Field
    private Integer bookShelf;
    @Field
    private Integer bookFloor;
    @Field
    private Integer count;
    @Field
    private Double price;
    @Field
    private Integer isLend;
    @Field
    private Integer dayClickCount;
    @Field
    private Integer monthClickCount;
    @Field
    private Integer totalClickCount;
    @Field
    private String publishDate;

    @Transient
    @Field
    private com.library.pojo.Category category;

    public Book(Integer id){
        this.id = id;
    }



}
