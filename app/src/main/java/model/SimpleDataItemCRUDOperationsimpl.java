package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDataItemCRUDOperationsimpl implements IDataItemCRUDOperations {
     private static String[] ITEM_NAMES = new String[]{"lorem","ipsum","dolor","sit","amet","elit"};

    @Override
    public DataItem createDataItem(DataItem item) {
        return item;
    }

    @Override
    public List<DataItem> readAllDataItems() {
        try {
            Thread.sleep(5000);
        }
        catch (Exception e) {

        }

        return Arrays
                .stream(ITEM_NAMES)
                .map(name -> new DataItem(name))
                .collect(Collectors.toList());

//        # Same as (< Java8):
//        List<DataItem> items = new ArrayList<>();
//        for (String name : ITEM_NAMES) {
//            items.add(new DataItem(name));
//        }
    }

    @Override
    public DataItem readDataItem(long id) {
        return null;
    }

    @Override
    public boolean updateDataItem(DataItem item) {
        return false;
    }

    @Override
    public boolean deleteDataItem(DataItem item) {
        return false;
    }

//    @Override
//    public boolean deleteDataItem(long id) {
//        return false;
//    }
}
