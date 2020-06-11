package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.function.Consumer;

import model.IDataItemCRUDOperations;
import model.RetrofitDataItemCRUDOperationsImpl;

public class DataItemApplication extends Application {

    private IDataItemCRUDOperations crudOperations;

    @Override
    public void onCreate() {
        super.onCreate();
        crudOperations = new RetrofitDataItemCRUDOperationsImpl();
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
