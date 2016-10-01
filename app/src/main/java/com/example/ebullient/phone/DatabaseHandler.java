package com.example.ebullient.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ebullient on 04-09-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_NAME = "contactManager",
                TABLE_CONTACTS = "contacts",
                KEY_ID = "id",
                KEY_NAME = "name",
                KEY_PHONE = "phone",
                KEY_EMAIL = "email";


        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT ASC," + KEY_PHONE + " TEXT," + KEY_EMAIL + " TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

            onCreate(db);
        }

        public void createContact(Contact contact) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PHONE, contact.getPhone());
            values.put(KEY_EMAIL, contact.getEmail());



            db.insert(TABLE_CONTACTS, null, values);
            db.close();
        }

        public Contact getContact(int id) {
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            db.close();
            cursor.close();
            return contact;
        }

        public void deleteContact(Contact contact) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[] { String.valueOf(contact.getId()) });
            db.close();
        }

        public int getContactsCount() {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS ORDER BY NAME ASC", null);
            int count = cursor.getCount();
            db.close();
            cursor.close();

            return count;
        }

        public int updateContact(Contact contact) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PHONE, contact.getPhone());
            values.put(KEY_EMAIL, contact.getEmail());


            int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[] { String.valueOf(contact.getId()) });
            db.close();

            return rowsAffected;
        }

        public List<Contact> getAllContacts() {
            List<Contact> contacts = new ArrayList<Contact>();

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS ORDER BY NAME ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contacts;
        }
        public List<Contact> getAllContacts2() {
            List<Contact> contacts = new ArrayList<Contact>();

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE NAME LIKE"  + "'a%", null);

            if (cursor.moveToFirst()) {
                do {
                    contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contacts;
        }
}


