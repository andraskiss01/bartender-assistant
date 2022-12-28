package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_category")
public class Category{

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "parent_category_id")
    private int parentCategoryId;

    @ColumnInfo(name = "parent_category")
    private String parentCategory;

    public Category(){}

    @Ignore
    public Category(int id, String name, int parentCategoryId, String parentCategory){
        this(name, parentCategoryId, parentCategory);
        this.id = id;
    }

    @Ignore
    public Category(String name, int parentCategoryId, String parentCategory){
        this.name = name;
        this.parentCategoryId = parentCategoryId;
        this.parentCategory = parentCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
}
