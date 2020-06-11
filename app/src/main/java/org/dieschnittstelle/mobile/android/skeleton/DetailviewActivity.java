package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;

import java.util.ArrayList;

import model.DataItem;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int CALL_CONTACT_PICKER = 0;

    private DataItem item;
    private ActivityDetailviewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab);
        EditText itemName = binding.getRoot().findViewById(R.id.itemName);
       // fab.setEnabled(false);

        itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (textView.getText().toString().trim().length() == 0) {
                        textView.setError("You need to input a name for the item!");
                    }
                    else {
                        fab.setEnabled(true);
                    }
                }
                return false;
            }
        });

        this.item = (DataItem)getIntent().getSerializableExtra(ARG_ITEM);
        if (this.item == null) {
            this.item = new DataItem();
        }

        binding.setController(this);

        this.showFeedbackMessage("Item has contacts: " + this.item.getContacts());

    }


    public void onSaveItem(View view) {
        Intent returnData = new Intent();

        returnData.putExtra(ARG_ITEM,this.item);

        this.setResult(Activity.RESULT_OK, returnData);
        finish();
    }

    public DataItem getItem() {
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addContact:selectAndAddContact();return true;
            case R.id.doSomethingElse:
                Toast.makeText(this,
                        "Something else",
                        Toast.LENGTH_SHORT).show();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectAndAddContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContactIntent,CALL_CONTACT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CALL_CONTACT_PICKER && resultCode == Activity.RESULT_OK) {
            // _15 15:00
            addSelectedContactToContacts(data.getData());
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void addSelectedContactToContacts(Uri contactId) {

        Cursor cursor = getContentResolver().query(contactId,null,null,null,null );
        if (cursor.moveToFirst()) {
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String internalContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            showFeedbackMessage("Selected contact: " + contactName + " with id " +  internalContactId);

            if (item.getContacts() == null) {
                item.setContacts(new ArrayList<>());
            }
            if (item.getContacts().indexOf(internalContactId) == -1) {
                item.getContacts().add(contactId.toString());
            }
        }
    }

    private void showFeedbackMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
