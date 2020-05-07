package org.dieschnittstelle.mobile.android.skeleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import model.DataItem;
import model.IDataItemCRUDOperations;
import model.SimpleDataItemCRUDOperationsimpl;

public class MainActivity extends AppCompatActivity {

    public static final int CALL_DETAILVIEW_FOR_NEW_ITEM = 0;
    private ViewGroup listView;
    private ArrayAdapter<DataItem> listViewAdapter;
    private FloatingActionButton fab;

    private IDataItemCRUDOperations crudOperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.crudOperations = new SimpleDataItemCRUDOperationsimpl();

        this.listView = this.findViewById(R.id.listView);
        this.fab = this.findViewById(R.id.fab);

        this.listViewAdapter = new ArrayAdapter<DataItem>(this,R.layout.activity_main_listitem,R.id.itemName) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewGroup itemView = (ViewGroup)getLayoutInflater().inflate(R.layout.activity_main_listitem,null,false);

                DataItem item = getItem(position);

                TextView itemNameView = itemView.findViewById(R.id.itemName);
                TextView itemIdView = itemView.findViewById(R.id.itemId);

                itemNameView.setText(item.getName());
                itemIdView.setText(String.valueOf(item.getId()));

                return itemView;
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

//        Listenansicht befüllen
        this.listViewAdapter.addAll(crudOperations.readAllDataItems());

    }

    private void onListitemSelected(DataItem item) {
        Intent callDetailviewIntent = new Intent(this,DetailviewActivity.class);
        callDetailviewIntent.putExtra(DetailviewActivity.ARG_ITEM,item);
        startActivity(callDetailviewIntent);
    }

    private void onAddNewListitem() {
        Intent callDetailviewIntentForReturnValue = new Intent(this,DetailviewActivity.class);
        startActivityForResult(callDetailviewIntentForReturnValue, CALL_DETAILVIEW_FOR_NEW_ITEM);
    }

    private void addNewItemToList(DataItem item) {
        this.listViewAdapter.add(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CALL_DETAILVIEW_FOR_NEW_ITEM) {
           if (resultCode == Activity.RESULT_OK) {
               DataItem item = (DataItem)data.getSerializableExtra(DetailviewActivity.ARG_ITEM);
               showFeedbackMessage("got new item name: " + item);
               addNewItemToList(item);
           }
           else if (resultCode == Activity.RESULT_CANCELED) {
               showFeedbackMessage("no item name input was cancelled.");
           }
           else {
               showFeedbackMessage("no item name received and no cancellation detected. What's wrong?");
           }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showFeedbackMessage(String msg) {
        Snackbar.make(findViewById(R.id.viewRoot),msg, Snackbar.LENGTH_LONG).show(); // Kurze Darstellung
    }
}
