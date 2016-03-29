package com.jike.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.jike.beans.Contact;

import java.util.ArrayList;

/**
 * 注意权限问题：permission.READ_CONTACTS
 * Created by wancc on 2016/3/28.
 */
public class ContactUtils {
    public static ArrayList<Contact> getAllContact(Context context){
        ArrayList<Contact> contacts=new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            int contact_id = cursor.getInt(0);
            if (contact_id==0) {
                continue;
            }
            Cursor cursor2 =contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                    new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{""+contact_id}, null);
            Contact onecontact = new Contact();

            while (cursor2.moveToNext()) {
                String data1 = cursor2.getString(0);
                String mimetype = cursor2.getString(1);
                if ("vnd.android.cursor.item/name".equals(mimetype)){
                    onecontact.setName(data1);
                }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                    onecontact.setTele(data1);
                }
            }
            Log.i("show one contact", onecontact.toString());
            contacts.add(onecontact);
        }
        return contacts;
    }
}
