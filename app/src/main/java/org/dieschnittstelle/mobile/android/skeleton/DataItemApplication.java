package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.function.Consumer;

import model.IDataItemCRUDOperations;
import model.RetrofitDataItemCRUDOperationsImpl;
import model.RoomDataItemCRUDOperationsImpl;
import model.SyncedDataItemCRUDOperations;

public class DataItemApplication extends Application {

    private IDataItemCRUDOperations crudOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        // crudOperations = new RetrofitDataItemCRUDOperationsImpl();
        // crudOperations = new RoomDataItemCRUDOperationsImpl(this);
        crudOperations = new SyncedDataItemCRUDOperations(this);

    }

    public IDataItemCRUDOperations getCrudOperations() {
        return crudOperations;
    }

    public void verifyWebappAvailable(Consumer<Boolean> onDone) {
        new AsyncTask<Void,Void,Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                }
                catch (Exception e) {

                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean available) {
                Toast.makeText(DataItemApplication.this,"The webapp is running!",Toast.LENGTH_SHORT).show();
                onDone.accept(available);
            }
        }.execute();
    }
}
