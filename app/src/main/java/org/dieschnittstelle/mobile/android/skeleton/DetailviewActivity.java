package org.dieschnittstelle.mobile.android.skeleton;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;

import model.DataItem;
import model.IDataItemCRUDOperations;
import tasks.DeleteDataItemTask;
import tasks.UpdateDataItemTask;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int CALL_CONTACT_PICKER = 0;
    private DataItem item;
    private ActivityDetailviewBinding binding;
    private IDataItemCRUDOperations crudOperations;

    private ViewGroup contactsWrapper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        FloatingActionButton fab = binding.getRoot().findViewById(R.id.fab);
        FloatingActionButton fabDelete = binding.getRoot().findViewById(R.id.fabDelete);
        EditText itemName = binding.getRoot().findViewById(R.id.itemName);
        contactsWrapper = findViewById(R.id.contactsWrapper);

        fab.setEnabled(false);

        TextWatcher nameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (itemName.getText().toString().trim().length() == 0) {
                    itemName.setError("You need to input a name for the item!");
                    fab.setEnabled(false);
                }
                else {
                    fab.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        };
        itemName.addTextChangedListener(nameWatcher);

        itemName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (itemName.getText().toString().trim().length() == 0) {
                    itemName.setError("You need to input a name for the item!");
                    fab.setEnabled(false);
                }
            }
        });

        this.item = (DataItem)getIntent().getSerializableExtra(ARG_ITEM);
        if (this.item == null) {
            this.item = new DataItem();
        }

        binding.setController(this);

        if (item.getContacts() != null && item.getContacts().size() > 0) {
            item.getContacts().forEach(contactUriAsString -> {
                this.showContactDetails(Uri.parse(contactUriAsString));
            });
        }



    }

    public void setDateAndTime() {
        Log.i("DetailviewActivity","item.getExpiry: " + item.getExpiry());
        final Calendar calendar = Calendar.getInstance();
        if (item.getExpiry() > 0) {
            calendar.setTimeInMillis(item.getExpiry());
        }
        int initYear = calendar.get(Calendar.YEAR);
        int initMonth = calendar.get(Calendar.MONTH);
        int initDay = calendar.get(Calendar.DAY_OF_MONTH);
        int initHour = calendar.get(Calendar.HOUR_OF_DAY);
        int initMinute = calendar.get(Calendar.MINUTE);

        new DatePickerDialog(this, (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            new TimePickerDialog(DetailviewActivity.this,
                (timePicker, hour, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    item.setExpiry(calendar.getTimeInMillis());
                    TextView dateAndTime = findViewById(R.id.expiry);
                    dateAndTime.setText(item.getDateString());
                },initHour,initMinute,true
            ).show();
        },initYear,initMonth,initDay
        ).show();
    }



    public void onSaveItem(View view) {
        if (this.item.getExpiry() == 0) {
            this.item.setExpiry(System.currentTimeMillis()+ (86400 * 7 * 1000));
        }
        Intent returnData = new Intent();
        returnData.putExtra(ARG_ITEM,this.item);
        this.setResult(Activity.RESULT_OK, returnData);
        finish();
    }



    public void onDeleteItem(View view) {

        if (this.item.getId() == -1) return;

        DialogInterface.OnClickListener dialogClickListener = (dialogInterface, i) -> {
            if (i == DialogInterface.BUTTON_POSITIVE) {

                Intent returnData = new Intent();
                returnData.putExtra(ARG_ITEM,this.item);
                this.setResult(2,returnData);
                finish();

            }
            else if (i == DialogInterface.BUTTON_NEGATIVE){
                this.showFeedbackMessage("Item " + this.item.getName() + " not deleted.");
            }
        };

        new AlertDialog.Builder(this)
                .setMessage("Delete item " + item.getName() + "?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }


    public DataItem getItem() {
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addContact:selectAndAddContact();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectAndAddContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
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

        Cursor cursor = getContentResolver().query(contactId,null,null,null,null );
        if (cursor != null && cursor.moveToFirst()) {
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        showContactDetails(contactId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void showContactDetails(Uri contactId) {

        final ViewGroup listitemLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_detailview_contacts, null);
        final TextView contactNameText = listitemLayout.findViewById(R.id.contactName);
        final TextView contactEmailText = listitemLayout.findViewById(R.id.contactEmail);
        final TextView contactPhoneText = listitemLayout.findViewById(R.id.contactPhone);


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
            contactNameText.setText(contactName);
            showFeedbackMessage("Selected contact: " + contactName + " with id " +  internalContactId);


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
                    contactPhoneText.setText(number);
                    contactPhoneText.setOnClickListener(view -> {
                        sendSMS(number, item.getName(), item.getDescription());
                    });
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
                contactEmailText.setText(email);
                contactEmailText.setOnClickListener(view -> {
                    sendMail(email, item.getName(), item.getDescription());
                });
            }
            Log.i("DetailViewActivity","no (further) email addresses found for contact " + contactName);

        }

        contactsWrapper.addView(listitemLayout);
    }

    private void sendSMS(String phoneNumber, String title, String description ) {
        Intent sendSMSIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
        sendSMSIntent.putExtra("sms_body",title + ": " + description);
        startActivity(sendSMSIntent);
    }

    private void sendMail(String emailAddress, String name, String description) {

        Intent sendMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, name);
        sendMailIntent.putExtra(Intent.EXTRA_TEXT, description);
        startActivity(Intent.createChooser(sendMailIntent, null));

    }

    private void showFeedbackMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
