package com.ak17apps.bartenderassistant.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import com.ak17apps.bartenderassistant.database.BoardRepository;
import com.ak17apps.bartenderassistant.database.CategoryRepository;
import com.ak17apps.bartenderassistant.database.ComponentRepository;
import com.ak17apps.bartenderassistant.database.CompositeItemRepository;
import com.ak17apps.bartenderassistant.database.ItemRepository;
import com.ak17apps.bartenderassistant.database.OrderRepository;
import com.ak17apps.bartenderassistant.database.SellingAmountRepository;
import com.ak17apps.bartenderassistant.database.SupplyRepository;
import com.ak17apps.bartenderassistant.database.UnitExchangeRepository;
import com.ak17apps.bartenderassistant.database.UnitRepository;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class DataHandler {
    private static AppCompatActivity act;
    private static String fileName;
    private static String filePath;
    private static SupplyRepository supplyRepository;
    private static OrderRepository orderRepository;
    private static BoardRepository boardRepository;
    private static ComponentRepository componentRepository;
    private static CompositeItemRepository compositeItemRepository;
    private static SellingAmountRepository sellingAmountRepository;
    private static CategoryRepository categoryRepository;
    private static ItemRepository itemRepository;
    private static UnitExchangeRepository unitExchangeRepository;
    private static UnitRepository unitRepository;
    private static BufferedReader br;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static void initializeRepositories(){
        if(unitRepository == null){ unitRepository = new UnitRepository(act.getApplication()); }
        if(unitExchangeRepository == null){ unitExchangeRepository = new UnitExchangeRepository(act.getApplication()); }
        if(itemRepository == null){ itemRepository = new ItemRepository(act.getApplication()); }
        if(categoryRepository == null){ categoryRepository = new CategoryRepository(act.getApplication()); }
        if(sellingAmountRepository == null){ sellingAmountRepository = new SellingAmountRepository(act.getApplication()); }
        if(compositeItemRepository == null){ compositeItemRepository = new CompositeItemRepository(act.getApplication()); }
        if(componentRepository == null){ componentRepository = new ComponentRepository(act.getApplication()); }
        if(boardRepository == null){ boardRepository = new BoardRepository(act.getApplication()); }
        if(orderRepository == null){ orderRepository = new OrderRepository(act.getApplication()); }
        if(supplyRepository == null){ supplyRepository = new SupplyRepository(act.getApplication()); }
    }

    public static void saveData(AppCompatActivity activity){
        act = activity;
        //verifyStoragePermissions();
        initializeRepositories();

        try {
            File file = new File(act.getExternalMediaDirs()[0].toURI());
            if (!file.exists()) {
                file.mkdirs();
            }
            Calendar cal = Calendar.getInstance();
            fileName = "data_" + cal.get(Calendar.YEAR) + dateStr(cal.get(Calendar.MONTH) + 1) + dateStr(cal.get(Calendar.DAY_OF_MONTH))
                    + dateStr(cal.get(Calendar.HOUR_OF_DAY)) + dateStr(cal.get(Calendar.MINUTE)) + dateStr(cal.get(Calendar.SECOND)) + ".csv";

            filePath = file.getAbsolutePath() + File.separator + fileName;
            final FileOutputStream out = new FileOutputStream(filePath);

            saveUnits(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String dateStr(int date){
        return date < 10 ? "0" + date : date + "";
    }

    private static void saveUnits(final FileOutputStream out){
        unitRepository.getAllUnits().observe(act, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                writeToFile(out, Unit.class.getSimpleName() + "\n");
                writeToFile(out, "id;name\n");
                if(units != null) {
                    for (Unit u : units) {
                        writeToFile(out, u.getId() + ";" + u.getName() + "\n");
                    }
                }

                saveUnitExchanges(out);
            }
        });
    }

    private static void saveUnitExchanges(final FileOutputStream out){
        unitExchangeRepository.getAllUnitExchanges().observe(act, new Observer<List<UnitExchange>>() {
            @Override
            public void onChanged(@Nullable final List<UnitExchange> unitExchanges) {
                writeToFile(out, UnitExchange.class.getSimpleName() + "\n");
                writeToFile(out, "id;from_unit_id;from_unit;from_value;to_unit_id;to_unit;to_value\n");
                if(unitExchanges != null) {
                    for (UnitExchange ue : unitExchanges) {
                        writeToFile(out, ue.getId() + ";" + ue.getFromUnitId() + ";" + ue.getFromUnit() + ";" + ue.getFromValue() + ";" + ue.getToUnitId() + ";" + ue.getToUnit() + ";" + ue.getToValue() + "\n");
                    }
                }

                saveItems(out);
            }
        });
    }

    private static void saveItems(final FileOutputStream out){
        itemRepository.getAllItems().observe(act, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> items) {
                writeToFile(out, Item.class.getSimpleName() + "\n");
                writeToFile(out, "id;name;stored_amount;packaging_unit_id;packaging_unit;sellable_individually;max_amount\n");
                if(items != null) {
                    for (Item i : items) {
                        writeToFile(out, i.getId() + ";" + i.getName() + ";" + i.getStoredAmount() + ";" + i.getPackagingUnitId() + ";" + i.getPackagingUnit() + ";" + i.isSellableIndividually() + ";" + i.getMaxAmount() + "\n");
                    }
                }

                saveCategories(out);
            }
        });
    }

    private static void saveCategories(final FileOutputStream out){
        categoryRepository.getAllCategories().observe(act, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                writeToFile(out, Category.class.getSimpleName() + "\n");
                writeToFile(out, "id;name;parent_category_id;parent_category\n");
                if(categories != null) {
                    for (Category c : categories) {
                        writeToFile(out, c.getId() + ";" + c.getName() + ";" + c.getParentCategoryId() + ";" + c.getParentCategory() + "\n");
                    }
                }

                saveSellingAmounts(out);
            }
        });
    }

    private static void saveSellingAmounts(final FileOutputStream out){
        sellingAmountRepository.getAllSellingAmounts().observe(act, new Observer<List<SellingAmount>>() {
            @Override
            public void onChanged(@Nullable final List<SellingAmount> sellingAmounts) {
                writeToFile(out, SellingAmount.class.getSimpleName() + "\n");
                writeToFile(out, "id;amount;unit_id;unit;price;item_id;item;category_id;category\n");
                if(sellingAmounts != null) {
                    for (SellingAmount sa : sellingAmounts) {
                        writeToFile(out, sa.getId() + ";" + sa.getAmount() + ";" + sa.getUnitId() + ";" + sa.getUnit() + ";" + sa.getPrice() + ";" + sa.getItemId() + ";" + sa.getItem() + ";" + sa.getCategoryId() + ";" + sa.getCategory() + "\n");
                    }
                }

                saveCompositeItems(out);
            }
        });
    }

    private static void saveCompositeItems(final FileOutputStream out){
        compositeItemRepository.getAllCompositeItems().observe(act, new Observer<List<CompositeItem>>() {
            @Override
            public void onChanged(@Nullable final List<CompositeItem> compositeItems) {
                writeToFile(out, CompositeItem.class.getSimpleName() + "\n");
                writeToFile(out, "id;name;price;category_id;category\n");
                if(compositeItems != null) {
                    for (CompositeItem ci : compositeItems) {
                        writeToFile(out, ci.getId() + ";" + ci.getName() + ";" + ci.getPrice() + ";" + ci.getCategoryId() + ";" + ci.getCategory() + "\n");
                    }
                }

                saveComponents(out);
            }
        });
    }

    private static void saveComponents(final FileOutputStream out){
        componentRepository.getAllComponents().observe(act, new Observer<List<Component>>() {
            @Override
            public void onChanged(@Nullable final List<Component> components) {
                writeToFile(out, Component.class.getSimpleName() + "\n");
                writeToFile(out, "id;item_id;item;amount;unit_id;unit;composite_item_id;composite_item\n");
                if(components != null) {
                    for (Component c : components) {
                        writeToFile(out, c.getId() + ";" + c.getItemId() + ";" + c.getItem() + ";" + c.getAmount() + ";" + c.getUnitId() + ";" + c.getUnit() + ";" + c.getCompositeItemId() + ";" + c.getCompositeItem() + "\n");
                    }
                }

                saveBoards(out);
            }
        });
    }

    private static void saveBoards(final FileOutputStream out){
        boardRepository.getAllBoards().observe(act, new Observer<List<Board>>() {
            @Override
            public void onChanged(@Nullable final List<Board> boards) {
                writeToFile(out, Board.class.getSimpleName() + "\n");
                writeToFile(out, "id;name\n");
                if(boards != null) {
                    for (Board b : boards) {
                        writeToFile(out, b.getId() + ";" + b.getName() + "\n");
                    }
                }

                saveOrders(out);
            }
        });
    }

    private static void saveOrders(final FileOutputStream out){
        orderRepository.getAllOrders().observe(act, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable final List<Order> orders) {
                writeToFile(out, Order.class.getSimpleName() + "\n");
                writeToFile(out, "id;board_id;board;quantity;orderable_id;orderable;fulfilled;price;orderable_type;signed\n");
                if(orders != null) {
                    for (Order o : orders) {
                        writeToFile(out, o.getId() + ";" + o.getBoardId() + ";" + o.getBoard() + ";" + o.getQuantity() + ";" + o.getOrderableId() + ";" + o.getOrderable() + ";" + o.getPrice() + ";" + o.getOrderableType() + ";" + o.isSigned() + "\n");
                    }
                }

                saveSupplies(out);
            }
        });
    }

    private static void saveSupplies(final FileOutputStream out){
        supplyRepository.getAllSupplies().observe(act, new Observer<List<Supply>>() {
            @Override
            public void onChanged(@Nullable final List<Supply> supplies) {
                writeToFile(out, Supply.class.getSimpleName() + "\n");
                writeToFile(out, "id;creation_time;expiration_date;item_id;item\n");
                if(supplies != null) {
                    for (Supply s : supplies) {
                        writeToFile(out, s.getId() + ";" + s.getCreationTime() + ";" + s.getExpirationDate() + ";" + s.getItemId() + ";" + s.getItem() + "\n");
                    }
                }

                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    Toast.makeText(act, "Mentve ide: " + filePath, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void writeToFile(FileOutputStream out, String row){
        try {
            out.write(row.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    act,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void chooseCSV(AppCompatActivity activity, int loadCsvRequestCode){
        act = activity;
        verifyStoragePermissions();

        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("*/csv");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("*/*");
        act.startActivityForResult(Intent.createChooser(intent, "Select a CSV file"), loadCsvRequestCode);
    }

    public static void cleanTables(BufferedReader bufferedReader) throws IOException {
        //verifyStoragePermissions();
        initializeRepositories();

        br = bufferedReader;
        checkSupply();
    }

    private static void loadData() {
        Object currentTable = "";

        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(Unit.class.getSimpleName())) {
                    currentTable = Unit.class.getSimpleName();
                } else if(line.equals(UnitExchange.class.getSimpleName())) {
                    currentTable = UnitExchange.class.getSimpleName();
                } else if (line.equals(Item.class.getSimpleName())) {
                    currentTable = Item.class.getSimpleName();
                } else if (line.equals(Category.class.getSimpleName())) {
                    currentTable = Category.class.getSimpleName();
                } else if (line.equals(SellingAmount.class.getSimpleName())) {
                    currentTable = SellingAmount.class.getSimpleName();
                } else if (line.equals(CompositeItem.class.getSimpleName())) {
                    currentTable = CompositeItem.class.getSimpleName();
                } else if (line.equals(Component.class.getSimpleName())) {
                    currentTable = Component.class.getSimpleName();
                } else if (line.equals(Board.class.getSimpleName())) {
                    currentTable = Board.class.getSimpleName();
                } else if (line.equals(Order.class.getSimpleName())) {
                    currentTable = Order.class.getSimpleName();
                } else if (line.equals(Supply.class.getSimpleName())) {
                    currentTable = Supply.class.getSimpleName();
                }

                if(currentTable.equals("")) {
                    Toast.makeText(act, "A fájl nem olvasható", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentTable.equals(Unit.class.getSimpleName())) {
                    if (!line.equals(Unit.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(2, line);
                        unitRepository.insert(new Unit(Integer.parseInt(fields[0]), fields[1]));
                    }
                }else if (currentTable.equals(UnitExchange.class.getSimpleName())) {
                    if (!line.equals(UnitExchange.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(7, line);
                        unitExchangeRepository.insert(new UnitExchange(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2], Float.parseFloat(fields[3]), Integer.parseInt(fields[4]), fields[5], Float.parseFloat(fields[6])));
                    }
                } else if (currentTable.equals(Item.class.getSimpleName())) {
                    if (!line.equals(Item.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(7, line);
                        itemRepository.insert(new Item(Integer.parseInt(fields[0]), fields[1], Float.parseFloat(fields[2]), Integer.parseInt(fields[3]), fields[4], Boolean.parseBoolean(fields[5]), Float.parseFloat(fields[6])));
                    }
                } else if (currentTable.equals(Category.class.getSimpleName())) {
                    if (!line.equals(Category.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(4, line);
                        categoryRepository.insert(new Category(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3]));
                    }
                } else if (currentTable.equals(SellingAmount.class.getSimpleName())) {
                    if (!line.equals(SellingAmount.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(9, line);
                        sellingAmountRepository.insert(new SellingAmount(Integer.parseInt(fields[0]), Float.parseFloat(fields[1]), Integer.parseInt(fields[2]), fields[3], Float.parseFloat(fields[4]), Integer.parseInt(fields[5]), fields[6], Integer.parseInt(fields[7]), fields[8]));
                    }
                } else if (currentTable.equals(CompositeItem.class.getSimpleName())) {
                    if (!line.equals(CompositeItem.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(5, line);
                        compositeItemRepository.insert(new CompositeItem(Integer.parseInt(fields[0]), fields[1], Float.parseFloat(fields[2]), Integer.parseInt(fields[3]), fields[4]));
                    }
                } else if (currentTable.equals(Component.class.getSimpleName())) {
                    if (!line.equals(Component.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(8, line);
                        componentRepository.insert(new Component(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2], Float.parseFloat(fields[3]), Integer.parseInt(fields[4]), fields[5], Integer.parseInt(fields[6]), fields[7]));
                    }
                } else if (currentTable.equals(Board.class.getSimpleName())) {
                    if (!line.equals(Board.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(2, line);
                        boardRepository.insert(new Board(Integer.parseInt(fields[0]), fields[1]));
                    }
                } else if (currentTable.equals(Order.class.getSimpleName())) {
                    if (!line.equals(Order.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(9, line);
                        orderRepository.insert(new Order(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), fields[2], Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], Float.parseFloat(fields[6]), fields[7], Boolean.parseBoolean(fields[8])));
                    }
                } else if (currentTable.equals(Supply.class.getSimpleName())) {
                    if (!line.equals(Supply.class.getSimpleName()) && !line.startsWith("id")) {
                        String[] fields = getFields(5, line);
                        supplyRepository.insert(new Supply(Integer.parseInt(fields[0]), Long.parseLong(fields[1]), Long.parseLong(fields[2]), Integer.parseInt(fields[3]), fields[4]));
                    }
                }
            }
            Toast.makeText(act, "Betöltve", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String[] getFields(int arraySize, String line){
        String[] fields = new String[arraySize];
        for(int i = 0; i < line.split(";").length; i++){
            fields[i] = line.split(";")[i];
        }
        return fields;
    }

    private static void checkSupply(){
        supplyRepository.deleteAll();
        supplyRepository.getAllSupplies().observe(act, new Observer<List<Supply>>() {
            @Override
            public void onChanged(@Nullable final List<Supply> supplies) {
                if(supplies == null || supplies.isEmpty()) {
                    checkOrder();
                }
            }
        });
    }

    private static void checkOrder(){
        orderRepository.deleteAll();
        orderRepository.getAllOrders().observe(act, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable final List<Order> orders) {
                if(orders == null || orders.isEmpty()) {
                    checkBoard();
                }
            }
        });
    }

    private static void checkBoard(){
        boardRepository.deleteAll();
        boardRepository.getAllBoards().observe(act, new Observer<List<Board>>() {
            @Override
            public void onChanged(@Nullable final List<Board> boards) {
                if(boards == null || boards.isEmpty()) {
                    checkComponent();
                }
            }
        });
    }

    private static void checkComponent(){
        componentRepository.deleteAll();
        componentRepository.getAllComponents().observe(act, new Observer<List<Component>>() {
            @Override
            public void onChanged(@Nullable final List<Component> components) {
                if(components == null || components.isEmpty()) {
                    checkCompositeItem();
                }
            }
        });
    }

    private static void checkCompositeItem(){
        compositeItemRepository.deleteAll();
        compositeItemRepository.getAllCompositeItems().observe(act, new Observer<List<CompositeItem>>() {
            @Override
            public void onChanged(@Nullable final List<CompositeItem> compositeItems) {
                if(compositeItems == null || compositeItems.isEmpty()) {
                    checkSellingAmount();
                }
            }
        });
    }

    private static void checkSellingAmount(){
        sellingAmountRepository.deleteAll();
        sellingAmountRepository.getAllSellingAmounts().observe(act, new Observer<List<SellingAmount>>() {
            @Override
            public void onChanged(@Nullable final List<SellingAmount> sellingAmounts) {
                if(sellingAmounts == null || sellingAmounts.isEmpty()) {
                    checkCategory();
                }
            }
        });
    }

    private static void checkCategory(){
        categoryRepository.deleteAll();
        categoryRepository.getAllCategories().observe(act, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                if(categories == null || categories.isEmpty()) {
                    checkItem();
                }
            }
        });
    }

    private static void checkItem(){
        itemRepository.deleteAll();
        itemRepository.getAllItems().observe(act, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable final List<Item> items) {
                if(items == null || items.isEmpty()) {
                    checkUnit();
                }
            }
        });
    }

    private static void checkUnit(){
        unitRepository.deleteAll();
        unitRepository.getAllUnits().observe(act, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                if(units == null || units.isEmpty()) {
                    loadData();
                }
            }
        });
    }
}
