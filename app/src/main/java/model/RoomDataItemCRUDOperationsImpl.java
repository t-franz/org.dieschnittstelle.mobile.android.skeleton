package model;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

public class RoomDataItemCRUDOperationsImpl implements IDataItemCRUDOperations {
    @Dao
    public static interface RoomDataItemDao {

      @Query("select * from dataitem")
      public List<DataItem> readAll();

      @Insert
      public long create(DataItem item);

      @Update
      public int update(DataItem item);

    }

    @Database(entities = {DataItem.class}, version = 1)
    public abstract static class DataItemDatabase extends RoomDatabase {
        public abstract RoomDataItemDao getDato();
    }

    private DataItemDatabase db;

    public RoomDataItemCRUDOperationsImpl(Context context) {
        db = Room.databaseBuilder(context,DataItemDatabase.class, "dataitems.db").build();
    }


    @Override
    public DataItem createDataItem(DataItem item) {
        long id = db.getDato().create(item);
        item.setId(id);
        return item;
    }

    @Override
    public List<DataItem> readAllDataItems() {
//        try{
//            Thread.sleep(2000);
//        }
//        catch (Exception e) {
//
//        }
        return db.getDato().readAll();
    }

    @Override
    public DataItem readDataItem(long id) {
        return null;
    }

    @Override
    public boolean updateDataItem(DataItem item) {
        if (db.getDato().update(item) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDataItem(long id) {
        return false;
    }
}
