package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "t_component", foreignKeys = {@ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "item_id", onDelete = RESTRICT),
        @ForeignKey(entity = Unit.class, parentColumns = "id", childColumns = "unit_id", onDelete = RESTRICT),
        @ForeignKey(entity = CompositeItem.class, parentColumns = "id", childColumns = "composite_item_id", onDelete = RESTRICT)})
public class Component {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "item_id")
    private int itemId;

    @ColumnInfo(name = "item")
    private String item;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "unit_id")
    private int unitId;

    @ColumnInfo(name = "unit")
    private String unit;

    @ColumnInfo(name = "composite_item_id")
    private int compositeItemId;

    @ColumnInfo(name = "composite_item")
    private String compositeItem;

    public Component(){}

    @Ignore
    public Component(int id, int itemId, String item, float amount, int unitId, String unit, int compositeItemId, String compositeItem){
        this(itemId, item, amount, unitId, unit, compositeItemId, compositeItem);
        this.id = id;
    }

    @Ignore
    public Component(int itemId, String item, float amount, int unitId, String unit, int compositeItemId, String compositeItem){
        this.itemId = itemId;
        this.item = item;
        this.amount = amount;
        this.unitId = unitId;
        this.unit = unit;
        this.compositeItemId = compositeItemId;
        this.compositeItem = compositeItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public int getCompositeItemId() {
        return compositeItemId;
    }

    public void setCompositeItemId(int compositeItemId) {
        this.compositeItemId = compositeItemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCompositeItem() {
        return compositeItem;
    }

    public void setCompositeItem(String compositeItem) {
        this.compositeItem = compositeItem;
    }
}
