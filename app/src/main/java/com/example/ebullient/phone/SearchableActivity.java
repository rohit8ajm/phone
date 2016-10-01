package com.example.ebullient.phone;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends Activity {

    private static final int DELETE = 0;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        contactListView = (ListView) findViewById(R.id.searchView);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        dbHandler = new DatabaseHandler(getApplicationContext());
        registerForContextMenu(contactListView);
        if (dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts2());

        populateList();

    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Contact Options");
        //menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case EDIT:
            // TODO: Implement editing a contact
            // break;
            case DELETE:
                dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
                Contacts.remove(longClickedItemIndex);
                contactAdapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    private boolean contactExists(Contact contact) {
        String name = contact.getName();
        int contactCount = Contacts.size();


        for (int i = 0; i < contactCount; i++) {
            if (name.compareToIgnoreCase(Contacts.get(i).getName()) == 0)
                return true;
        }
        return false;
    }


    private void populateList() {
        contactListView.setAdapter(contactAdapter);
    }

    private void doMySearch(String query) {
        class ContactListAdapter extends ArrayAdapter<Contact> {
            public ContactListAdapter() {
                super(SearchableActivity.this, R.layout.listview_item, Contacts);
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                if (view == null)
                    view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

                Contact currentContact = Contacts.get(position);

                TextView name = (TextView) view.findViewById(R.id.contactName);
                name.setText(currentContact.getName());
                TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
                phone.setText(currentContact.getPhone());
                TextView email = (TextView) view.findViewById(R.id.emailAddress);
                email.setText(currentContact.getEmail());



                return view;
            }
        }
        //perform query here

    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.SEARCH_SERVICE, true);
        startSearch(null, false, appData, false);

        appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            boolean jargon = appData.getBoolean(SearchableActivity.SEARCH_SERVICE);
        }
        return true;
    }



    @Override
    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("KEY", "VALUE");
        }

        super.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.listView).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
}
