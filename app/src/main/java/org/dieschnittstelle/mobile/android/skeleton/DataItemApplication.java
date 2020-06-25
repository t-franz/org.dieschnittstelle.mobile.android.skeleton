package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import java.util.function.Consumer;

import model.IDataItemCRUDOperations;
import model.RoomDataItemCRUDOperationsImpl;
import model.SyncedDataItemCRUDOperations;
import tasks.CheckConnectivityTask;

public class DataItemApplication extends Application {

    private IDataItemCRUDOperations crudOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        crudOperations = new SyncedDataItemCRUDOperations(this);

    }

    public IDataItemCRUDOperations getCrudOperations() {
        return crudOperations;
    }

    public void verifyWebappAvailable(Consumer<Boolean> onDone) {
        new CheckConnectivityTask(available -> {
            if (!available) {
                this.crudOperations = new RoomDataItemCRUDOperationsImpl(this);
                Toast.makeText(DataItemApplication.this,"The webapp is NOT running!",Toast.LENGTH_SHORT).show();
                onDone.accept(available);
            }
            else {
                Toast.makeText(DataItemApplication.this,"The webapp is running!",Toast.LENGTH_SHORT).show();
                // Erstelle hier einen neuen AsyncTast, entweder inline oder als AsyncTask-Klasse,
                // der die doAbgleich-Methode auf CrudOperations aufruft
                // und in seinem onDone-Callback onDone.Accept aufruft
                // BESSER: ABGLEICH nach LOGIN
                onDone.accept(available);
            }
        }).execute();

    }
}
