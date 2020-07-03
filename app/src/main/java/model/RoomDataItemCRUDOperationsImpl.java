package model;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
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

      @Delete
      public int delete(DataItem item);

        @Query("delete from dataitem")
      public int deleteAllLocal();

        @Query("delete from dataitem")
      public int deleteAll();
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
//        item.beforePersist();
        long id = db.getDato().create(item);
        item.setId(id);
        return item;
    }

    @Override
    public List<DataItem> readAllDataItems() {
        return db.getDato().readAll();
    }

    @Override
    public DataItem readDataItem(long id) {
        return null;
    }

    @Override
    public boolean updateDataItem(DataItem item) {
        item.beforePersist();
        if (db.getDato().update(item) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDataItem(DataItem item) {
        if (db.getDato().delete(item) > 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteAllLocal() {
        if (db.getDato().deleteAllLocal() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAllRemote() {
        return false;
    }

    @Override
    public boolean deleteAll() {
        if (db.getDato().deleteAll() > 0) {
            return true;
        }
        return false;
    }

}
