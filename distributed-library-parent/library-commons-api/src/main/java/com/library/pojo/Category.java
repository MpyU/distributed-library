package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    /**
     * 	id int primary key auto_increment comment "ID",
     * 	category_name varchar(20) comment "类别名称",
     * 	category_desc varchar(255) comment "类别详情",
     * 	floor int comment "该类别所在楼层",
     * 	parent_id int comment "父类别ID"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoryName;
    private String categoryDesc;
    private Integer floor;
    private Integer parentId;

    public Category(Integer id){
        this.id = id;
    }

    public Category(String categoryName,Integer floor,Integer parentId){
        this.categoryName = categoryName;
        this.floor = floor;
        this.parentId = parentId;
    }

}
