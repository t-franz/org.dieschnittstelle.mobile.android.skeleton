package model;

import android.content.Context;

import java.util.List;

public class SyncedDataItemCRUDOperations implements IDataItemCRUDOperations {

    private IDataItemCRUDOperations localCRUD;
    private IDataItemCRUDOperations remoteCRUD;

    public SyncedDataItemCRUDOperations(Context context) {
        this.localCRUD = new RoomDataItemCRUDOperationsImpl(context);
        this.remoteCRUD = new RetrofitDataItemCRUDOperationsImpl();
    }
    @Override
    public DataItem createDataItem(DataItem item) {
        item = this.localCRUD.createDataItem(item);
        this.remoteCRUD.createDataItem(item);

        return item;
    }

    @Override
    public List<DataItem> readAllDataItems() {
        return this.localCRUD.readAllDataItems();
    }

    @Override
    public DataItem readDataItem(long id) {
        return null;
    }

    @Override
    public boolean updateDataItem(DataItem item) {
        if (localCRUD.updateDataItem(item)) {
            return remoteCRUD.updateDataItem(item);
        }
        return false;
    }

    @Override
    public boolean deleteDataItem(DataItem item) {
        if (localCRUD.deleteDataItem(item)) {
            return remoteCRUD.deleteDataItem(item);
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        if (localCRUD.deleteAll()) {
            return remoteCRUD.deleteAll();
        }
        return false;
    }

    @Override
    public boolean deleteAllLocal() {
        return this.localCRUD.deleteAllLocal();
    }
    @Override
    public boolean deleteAllRemote() {
        return this.remoteCRUD.deleteAllRemote();
    }
}
