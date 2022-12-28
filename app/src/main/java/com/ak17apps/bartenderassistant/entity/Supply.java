package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "t_supply", foreignKeys = @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "item_id", onDelete = RESTRICT))
public class Supply {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "creation_time")
    private long creationTime;

    @ColumnInfo(name = "expiration_date")
    private long expirationDate;

    @ColumnInfo(name = "item_id")
    private int itemId;

    @ColumnInfo(name = "item")
    private String item;

    public Supply(){}

    @Ignore
    public Supply(int id, long creationTime, long expirationDate, int itemId, String item){
        this(creationTime, expirationDate, itemId, item);
        this.id = id;
    }

    @Ignore
    public Supply(long creationTime, long expirationDate, int itemId, String item){
        this.creationTime = creationTime;
        this.expirationDate = expirationDate;
        this.itemId = itemId;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
