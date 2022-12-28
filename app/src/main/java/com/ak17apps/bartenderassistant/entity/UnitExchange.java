package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_unit_exchange")
public class UnitExchange {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "from_unit_id")
    private int fromUnitId;

    @ColumnInfo(name = "from_unit")
    private String fromUnit;

    @ColumnInfo(name = "from_value")
    private float fromValue;

    @ColumnInfo(name = "to_unit_id")
    private int toUnitId;

    @ColumnInfo(name = "to_unit")
    private String toUnit;

    @ColumnInfo(name = "to_value")
    private float toValue;

    public UnitExchange(){}

    @Ignore
    public UnitExchange(int id, int fromUnitId, String fromUnit, float fromValue, int toUnitId, String toUnit, float toValue){
        this(fromUnitId, fromUnit, fromValue, toUnitId, toUnit, toValue);
        this.id = id;
    }

    @Ignore
    public UnitExchange(int fromUnitId, String fromUnit, float fromValue, int toUnitId, String toUnit, float toValue){
        this.fromUnitId = fromUnitId;
        this.fromUnit = fromUnit;
        this.fromValue = fromValue;
        this.toUnitId = toUnitId;
        this.toUnit = toUnit;
        this.toValue = toValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUnitId() {
        return fromUnitId;
    }

    public void setFromUnitId(int fromUnitId) {
        this.fromUnitId = fromUnitId;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
    }

    public float getFromValue() {
        return fromValue;
    }

    public void setFromValue(float fromValue) {
        this.fromValue = fromValue;
    }

    public int getToUnitId() {
        return toUnitId;
    }

    public void setToUnitId(int toUnitId) {
        this.toUnitId = toUnitId;
    }

    public String getToUnit() {
        return toUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }

    public float getToValue() {
        return toValue;
    }

    public void setToValue(float toValue) {
        this.toValue = toValue;
    }
}
