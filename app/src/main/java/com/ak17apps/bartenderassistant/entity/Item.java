package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "t_item", foreignKeys = @ForeignKey(entity = Unit.class, parentColumns = "id", childColumns = "packaging_unit_id", onDelete = RESTRICT))
public class Item {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "stored_amount")
    private float storedAmount;

    @ColumnInfo(name = "packaging_unit_id")
    private int packagingUnitId;

    @ColumnInfo(name = "packaging_unit")
    private String packagingUnit;

    @ColumnInfo(name = "sellable_individually")
    private boolean sellableIndividually;

    @ColumnInfo(name = "max_amount")
    private float maxAmount;

    public Item(){}

    @Ignore
    public Item(int id, String name, float storedAmount, int packagingUnitId, String packagingUnit, boolean sellableIndividually, float maxAmount) {
        this(name, storedAmount, packagingUnitId, packagingUnit, sellableIndividually, maxAmount);
        this.id = id;
    }

    @Ignore
    public Item(String name, float storedAmount, int packagingUnitId, String packagingUnit, boolean sellableIndividually, float maxAmount) {
        this.name = name;
        this.storedAmount = storedAmount;
        this.packagingUnitId = packagingUnitId;
        this.packagingUnit = packagingUnit;
        this.sellableIndividually = sellableIndividually;
        this.maxAmount = maxAmount;
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

    public float getStoredAmount() {
        return storedAmount;
    }

    public void setStoredAmount(float storedAmount) {
        this.storedAmount = storedAmount;
    }

    public int getPackagingUnitId() {
        return packagingUnitId;
    }

    public void setPackagingUnitId(int packagingUnitId) {
        this.packagingUnitId = packagingUnitId;
    }

    public String getPackagingUnit() {
        return packagingUnit;
    }

    public void setPackagingUnit(String packagingUnit) {
        this.packagingUnit = packagingUnit;
    }

    public boolean isSellableIndividually() {
        return sellableIndividually;
    }

    public void setSellableIndividually(boolean sellableIndividually) {
        this.sellableIndividually = sellableIndividually;
    }

    public float getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(float maxAmount) {
        this.maxAmount = maxAmount;
    }
}
