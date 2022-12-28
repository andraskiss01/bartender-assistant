package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "t_selling_amount", foreignKeys = { @ForeignKey(entity = Unit.class, parentColumns = "id", childColumns = "unit_id", onDelete = RESTRICT),
        @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "item_id", onDelete = RESTRICT),
        @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "category_id", onDelete = RESTRICT)})
public class SellingAmount extends Orderable{

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "unit_id")
    private int unitId;

    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "item_id")
    private int itemId;

    @ColumnInfo(name = "item")
    private String item;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "category")
    private String category;

    public SellingAmount(){}

    @Ignore
    public SellingAmount(int id, float amount, int unitId, String unit, float price, int itemId, String item, int categoryId, String category){
        this(amount, unitId, unit, price, itemId, item, categoryId, category);
        this.id = id;
    }

    @Ignore
    public SellingAmount(float amount, int unitId, String unit, float price, int itemId, String item, int categoryId, String category){
        this.amount = amount;
        this.unitId = unitId;
        this.unit = unit;
        this.price = price;
        this.itemId = itemId;
        this.item = item;
        this.categoryId = categoryId;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
