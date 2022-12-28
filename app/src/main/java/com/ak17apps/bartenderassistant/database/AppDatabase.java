package com.ak17apps.bartenderassistant.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ak17apps.bartenderassistant.dao.BoardDao;
import com.ak17apps.bartenderassistant.dao.CategoryDao;
import com.ak17apps.bartenderassistant.dao.ComponentDao;
import com.ak17apps.bartenderassistant.dao.CompositeItemDao;
import com.ak17apps.bartenderassistant.dao.ItemDao;
import com.ak17apps.bartenderassistant.dao.OrderDao;
import com.ak17apps.bartenderassistant.dao.SellingAmountDao;
import com.ak17apps.bartenderassistant.dao.SupplyDao;
import com.ak17apps.bartenderassistant.dao.UnitDao;
import com.ak17apps.bartenderassistant.dao.UnitExchangeDao;
import com.ak17apps.bartenderassistant.entity.Board;
import com.ak17apps.bartenderassistant.entity.Category;
import com.ak17apps.bartenderassistant.entity.Component;
import com.ak17apps.bartenderassistant.entity.CompositeItem;
import com.ak17apps.bartenderassistant.entity.Item;
import com.ak17apps.bartenderassistant.entity.Order;
import com.ak17apps.bartenderassistant.entity.SellingAmount;
import com.ak17apps.bartenderassistant.entity.Supply;
import com.ak17apps.bartenderassistant.entity.Unit;
import com.ak17apps.bartenderassistant.entity.UnitExchange;

@Database(entities = {Board.class, Category.class, Component.class, CompositeItem.class, Item.class,
        Order.class, SellingAmount.class, Supply.class, Unit.class, UnitExchange.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BoardDao boardDao();
    public abstract CategoryDao categoryDao();
    public abstract ComponentDao componentDao();
    public abstract CompositeItemDao compositeItemDao();
    public abstract ItemDao itemDao();
    public abstract OrderDao orderDao();
    public abstract SellingAmountDao sellingAmountDao();
    public abstract SupplyDao supplyDao();
    public abstract UnitDao unitDao();
    public abstract UnitExchangeDao unitExchangeDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bartender_assistant_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
