package com.example.headfirstlabs10messenger;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends Activity {

    private static final int PICK_CONTACT_REQ_CODE=0;
    private Uri contactUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renderContact(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void renderContact(Uri uri){
        TextView contactNameView = (TextView) findViewById(R.id.contact_name);
        TextView contactPhoneView = (TextView) findViewById(R.id.contact_phone);
        ImageView photoView = (ImageView) findViewById(R.id.contact_portrait);

        if(uri == null){
            contactNameView.setText("Select a contact");
            contactPhoneView.setText("");
            photoView.setImageBitmap(null);
        }else{
            contactNameView.setText(getDisplayName(uri));
            contactPhoneView.setText(getMobileNumber(uri));
            photoView.setImageBitmap(getPhoto(uri));
        }
    }

    private Bitmap getPhoto(Uri uri) {
        Bitmap photo = null;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts._ID},null,null,null);
        String id = null;
        if(cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursor.close();
        if(id != null){
//            try{
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                                new Long(id).longValue()));
                if(input != null){
                    photo = BitmapFactory.decodeStream(input);
                }
//            }catch (IOException e){
//                Log.e("Error", "Error in getting contact's photo", e);
//            }
        }
        return photo;
    }

    private String getMobileNumber(Uri uri) {
        String phoneNumber = null;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts._ID},null,null,null);
        String id = null;
        if(cursor.moveToFirst()){
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursor.close();
        if(id != null){
            Cursor phoneCursor = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + "=" +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    new String[]{id},
                    null);
            if(phoneCursor.moveToFirst()){
                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                ));
            }
            phoneCursor.close();
        }
        return phoneNumber;
    }

    private String getDisplayName(Uri uri) {
        ContentResolver resolver = getContentResolver();
        String displayName = null;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if(cursor.moveToFirst()){
            displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return displayName;
    }

    public void onUpdateContact(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQ_CODE);
    }

    public void onImCool(View view){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(getMobileNumber(contactUri),null, "Babe, I'm Cool.", null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT_REQ_CODE){
            if(resultCode == RESULT_OK){
                Log.d("Selection", data.toString());
                contactUri = data.getData();
                renderContact(contactUri);
            }
        }
    }
}
