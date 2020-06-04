package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityMainListitemBinding;

import java.util.List;

import model.DataItem;
import model.IDataItemCRUDOperations;
import model.RetrofitDataItemCRUDOperationsImpl;
import model.RoomDataItemCRUDOperationsImpl;
import model.SimpleDataItemCRUDOperationsimpl;
import tasks.CreateDataItemTask;
import tasks.ReadAllDataItemsTask;
import tasks.UpdateDataItemTask;

public class MainActivity extends AppCompatActivity {

    private static String logger = "MainActivity";

    public static final int CALL_DETAILVIEW_FOR_NEW_ITEM = 0;
    public static final int CALL_DETAILVIEW_FOR_EXISTING_ITEM = 1;
    private ViewGroup listView;
    private ArrayAdapter<DataItem> listViewAdapter;
    private FloatingActionButton fab;
    private ProgressBar progressBar;



    private IDataItemCRUDOperations crudOperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.crudOperations = new RetrofitDataItemCRUDOperationsImpl();//new RoomDataItemCRUDOperationsImpl(this);//this.crudOperations = new SimpleDataItemCRUDOperationsimpl();

        this.listView = this.findViewById(R.id.listView);
        this.fab = this.findViewById(R.id.fab);
        this.progressBar = findViewById(R.id.progressBar);

        this.listViewAdapter = new ArrayAdapter<DataItem>(this,R.layout.activity_main_listitem,R.id.itemName) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View existingView, @NonNull ViewGroup parent) {
                Log.i(logger,"using existingView for position:" + position +": " + existingView);

                ActivityMainListitemBinding binding = null;
                View currentView = null;

                if (existingView != null) {
                    currentView = existingView;
                    binding = (ActivityMainListitemBinding) existingView.getTag();
                }
                else {
                    binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_main_listitem,null, false);
                    currentView = binding.getRoot();
                    currentView.setTag(binding);
                }

                DataItem item = getItem(position);
                binding.setItem(item);
                binding.setController(MainActivity.this);

                return currentView;
            }

        };



        ((ListView)this.listView).setAdapter(this.listViewAdapter);

        ((ListView)this.listView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataItem item = listViewAdapter.getItem(i);
                onListitemSelected(item);
            }
        });

        this.fab.setOnClickListener((view) -> {
            this.onAddNewListitem();
        });


        new ReadAllDataItemsTask(progressBar,
                crudOperations,
                items -> listViewAdapter.addAll(items)
        ).execute();

    }

    private void onListitemSelected(DataItem item) {
        Intent callDetailviewIntent = new Intent(this,DetailviewActivity.class);
        callDetailviewIntent.putExtra(DetailviewActivity.ARG_ITEM,item);
        startActivityForResult(callDetailviewIntent, CALL_DETAILVIEW_FOR_EXISTING_ITEM);
    }

    private void onAddNewListitem() {
        Intent callDetailviewIntentForReturnValue = new Intent(this,DetailviewActivity.class);
        startActivityForResult(callDetailviewIntentForReturnValue, CALL_DETAILVIEW_FOR_NEW_ITEM);
    }

    private void createItemAndAddItToList(DataItem item) {
        new CreateDataItemTask(
                progressBar,
                crudOperations,
                createdItem -> {
                    this.listViewAdapter.add(createdItem);
                    ((ListView)this.listView).smoothScrollToPosition(
                            this.listViewAdapter.getPosition(createdItem));
                }
        ).execute(item);
    }

    private void updateItemAndUpdateList(DataItem changedItem) {

        new UpdateDataItemTask(progressBar,crudOperations,updated -> {
          handleResultFromUpdateTask(changedItem,updated);
        }).execute(changedItem);

    }

    private  void handleResultFromUpdateTask(DataItem changedItem, boolean updated) {
        if (updated) {
            int existingItemInListPos = this.listViewAdapter.getPosition(changedItem);
            if (existingItemInListPos > -1) {
                DataItem existingItem = this.listViewAdapter.getItem(existingItemInListPos);
                existingItem.setName((changedItem.getName()));
                existingItem.setChecked(changedItem.isChecked());
                existingItem.setDescription(changedItem.getDescription());
                this.listViewAdapter.notifyDataSetChanged();
            }
            else {
                showFeedbackMessage("Updated: " + changedItem.getName() + " cannot be found in list.");
            }
        }
        else {
            showFeedbackMessage("Updated: " + changedItem.getName() + " item could not be updated in database.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CALL_DETAILVIEW_FOR_NEW_ITEM) {
           if (resultCode == Activity.RESULT_OK) {
               DataItem item = (DataItem)data.getSerializableExtra(DetailviewActivity.ARG_ITEM);
//               showFeedbackMessage("got new item name: " + item);
               createItemAndAddItToList(item);
           }
           else if (resultCode == Activity.RESULT_CANCELED) {
               showFeedbackMessage("no item name input was cancelled.");
           }
           else {
               showFeedbackMessage("no item name received and no cancellation detected. What's wrong?");
           }
        }
        else if (requestCode == CALL_DETAILVIEW_FOR_EXISTING_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                DataItem item = (DataItem) data.getSerializableExtra(DetailviewActivity.ARG_ITEM);
                updateItemAndUpdateList(item);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showFeedbackMessage(String msg) {
        Snackbar.make(findViewById(R.id.viewRoot),msg, Snackbar.LENGTH_LONG).show(); // Kurze Darstellung
    }

    public void onListItemChangedInList(DataItem changedItem) {
        new UpdateDataItemTask(progressBar,this.crudOperations,
                updated ->
                        showFeedbackMessage("changedItem " + changedItem.getName() + " has been updated"))
                .execute(changedItem);
    }
}
