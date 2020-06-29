package org.dieschnittstelle.mobile.android.skeleton;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;

import java.util.ArrayList;
import java.util.Calendar;

import model.DataItem;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int CALL_CONTACT_PICKER = 0;

    private DataItem item;
    private ActivityDetailviewBinding binding;

    DatePickerDialog datePicker;
    EditText expiryDate;
//    Button btnGetExpiry;
//    TextView expiryTextView;
    TimePickerDialog timePicker;
    EditText expiryTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab);
        EditText itemName = binding.getRoot().findViewById(R.id.itemName);
       // fab.setEnabled(false);

        FloatingActionButton fabDelete = binding.getRoot().findViewById(R.id.fabDelete);

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

        if (item.getContacts() != null && item.getContacts().size() > 0) {
            item.getContacts().forEach(contactUriAsString -> {
                this.showContactDetails(Uri.parse(contactUriAsString));
            });
        }

        // Date Picker Dialog
//        expiryTextView = binding.getRoot().findViewById(R.id.expiryTextView);
        expiryDate = binding.getRoot().findViewById(R.id.expiry);
        expiryDate.setInputType(InputType.TYPE_NULL);
        expiryDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            datePicker = new DatePickerDialog(DetailviewActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) ->
                            expiryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePicker.show();
        });
//        btnGetExpiry=findViewById(R.id.expiryBtn);
//        btnGetExpiry.setOnClickListener(v -> expiryTextView.setText("Selected Date: "+ expiryDate.getText()));

        // Time Picker Dialog
        expiryTime = binding.getRoot().findViewById(R.id.expiryTime);
        expiryTime.setInputType(InputType.TYPE_NULL);
        expiryTime.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            timePicker = new TimePickerDialog(DetailviewActivity.this,
                    (tp, sHour, sMinute) -> expiryTime.setText(sHour + ":" + sMinute), hour, minutes, true);
            timePicker.show();
        });
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
            addSelectedContactToContacts(data.getData());
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void addSelectedContactToContacts(Uri contactId) {

        if (item.getContacts() == null) {
            item.setContacts(new ArrayList<>());
        }
        if (item.getContacts().indexOf(contactId.toString()) == -1) {
            item.getContacts().add(contactId.toString());
        }

        showContactDetails(contactId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void showContactDetails(Uri contactId) {

        int hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},4);
            return;
        }
        else {
            showFeedbackMessage("Contact Permissions have been granted!");
        }

        Cursor cursor = getContentResolver().query(contactId,null,null,null,null );
        if (cursor != null && cursor.moveToFirst()) {
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String internalContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            showFeedbackMessage("Selected contact: " + contactName + " with id " +  internalContactId);

            // Zugriff auf Telefonnummer und Mail-Adresse siehe Aufzeichnung letztes Jahr
            // Kapitel Zugriff auf CONTACT_ID

            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                    new String[] {internalContactId},
                    null,null);
            while (phoneCursor.moveToNext()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int phoneNumberType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));

                if (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    Log.i("DetailviewActivity","found mobile number: " + number);
                }
                else {
                    Log.i("DetailViewActivity","found other number: " + number);
                }
            }
            Log.i("DetailViewActivity","no (further) phone numbers found for contact " + contactName);

            Cursor emailCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                    new String[] {internalContactId},
                    null,null);
            while (emailCursor.moveToNext()) {
                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                Log.i("DetailViewActivity","email is: " + email);
            }
            Log.i("DetailViewActivity","no (further) email addresses found for contact " + contactName);

            // Aufruf siehe 77ff item.getContacts().forEach(contactUriAsString -> { ...
        }
    }

    private void showFeedbackMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
