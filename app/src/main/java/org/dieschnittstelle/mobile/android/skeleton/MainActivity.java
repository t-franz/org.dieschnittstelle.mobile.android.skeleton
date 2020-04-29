package org.dieschnittstelle.mobile.android.skeleton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final int CALL_DETAILVIEW_FOR_NEW_ITEM = 0;
    private ViewGroup listView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = this.findViewById(R.id.listView);
        this.fab = this.findViewById(R.id.fab);

        for (int i=0; i<this.listView.getChildCount();i++) {
            TextView currentChild = (TextView)this.listView.getChildAt(i);
            currentChild.setOnClickListener((view) -> {
                //Toast.makeText(this, "Click on: " + currentChild.getText() +"!", Toast.LENGTH_SHORT).show();
                //this.showFeedbackMessage("Click on: " + currentChild.getText() +"!");
                this.onListitemSelected(String.valueOf(currentChild.getText()));
            });
        }

        this.fab.setOnClickListener((view) -> {
            //this.showFeedbackMessage("Add new Element!");
            this.onAddNewListitem();
        });

/*        TextView welcomeMessage = this.findViewById(R.id.welcomeMessage);
        welcomeMessage.setOnClickListener(view -> {
            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
        });
*/
    }

    private void onListitemSelected(String itemName) {
        Intent callDetailviewIntent = new Intent(this,DetailviewActivity.class);
        callDetailviewIntent.putExtra(DetailviewActivity.ARG_ITEM_NAME,itemName);
        startActivity(callDetailviewIntent);
    }

    private void onAddNewListitem() {
        Intent callDetailviewIntentForReturnValue = new Intent(this,DetailviewActivity.class);
        startActivityForResult(callDetailviewIntentForReturnValue, CALL_DETAILVIEW_FOR_NEW_ITEM);
    }

    private void addNewItemToList(String itemName) {
        ViewGroup newListItem = (ViewGroup)getLayoutInflater().inflate(R.layout.activity_main_listitem,null, false);

        TextView itemNameText = newListItem.findViewById(R.id.itemName);
        itemNameText.setText(itemName);

        listView.addView(newListItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CALL_DETAILVIEW_FOR_NEW_ITEM) {
           if (resultCode == Activity.RESULT_OK) {
               String newItemName = data.getStringExtra(DetailviewActivity.ARG_ITEM_NAME);
               showFeedbackMessage("got new item name: " + newItemName);
               addNewItemToList(newItemName);
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
       // Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.viewRoot),msg, Snackbar.LENGTH_LONG).show(); // Kurze Darstellung
        // Snackbar.make(findViewById(R.id.viewRoot),msg, Snackbar.LENGTH_INDEFINITE).show(); // Bleibt stehen
    }
}
