package com.ak17apps.bartenderassistant.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "t_order", foreignKeys = @ForeignKey(entity = Board.class, parentColumns = "id", childColumns = "board_id", onDelete = RESTRICT))
public class Order {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "board_id")
    private int boardId;

    @ColumnInfo(name = "board")
    private String board;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "orderable_id")
    private int orderableId;

    @ColumnInfo(name = "orderable")
    private String orderable;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "orderable_type")
    private String orderableType;

    @ColumnInfo(name = "signed")
    private boolean signed;

    public Order(){}

    @Ignore
    public Order(int id, int boardId, String board, int quantity, int orderableId, String orderable, float price, String orderableType, boolean signed){
        this(boardId, board, quantity, orderableId, orderable, price, orderableType, signed);
        this.id = id;
    }

    @Ignore
    public Order(int boardId, String board, int quantity, int orderableId, String orderable, float price, String orderableType, boolean signed){
        this.boardId = boardId;
        this.board = board;
        this.quantity = quantity;
        this.orderableId = orderableId;
        this.orderable = orderable;
        this.price = price;
        this.orderableType = orderableType;
        this.signed = signed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public int getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(int orderableId) {
        this.orderableId = orderableId;
    }

    public String getOrderable() {
        return orderable;
    }

    public void setOrderable(String orderable) {
        this.orderable = orderable;
    }

    public String getOrderableType() {
        return orderableType;
    }

    public void setOrderableType(String orderableType) {
        this.orderableType = orderableType;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
}
