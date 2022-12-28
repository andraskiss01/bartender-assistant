package com.ak17apps.bartenderassistant.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ak17apps.bartenderassistant.database.SellingAmountRepository;
import com.ak17apps.bartenderassistant.entity.SellingAmount;

import java.util.List;

public class SellingAmountViewModel extends AndroidViewModel {
    private SellingAmountRepository sellingAmountRepository;
    private LiveData<List<SellingAmount>> allSellingAmounts;

    public SellingAmountViewModel(Application application) {
        super(application);
        sellingAmountRepository = new SellingAmountRepository(application);
        allSellingAmounts = sellingAmountRepository.getAllSellingAmounts();
    }

    public void setCategoryId(int parentCategoryId){ sellingAmountRepository.setCategoryId(parentCategoryId); }
    public LiveData<List<SellingAmount>> getSellingAmountsOfCategory() { return sellingAmountRepository.getSellingAmountsOfCategory(); }
    public LiveData<List<SellingAmount>> getAllSellingAmounts() { return allSellingAmounts; }
    public void insert(SellingAmount sellingAmount) { sellingAmountRepository.insert(sellingAmount); }
    public void update(SellingAmount sellingAmount) { sellingAmountRepository.update(sellingAmount); }
    public void delete(SellingAmount sellingAmount) { sellingAmountRepository.delete(sellingAmount); }
}