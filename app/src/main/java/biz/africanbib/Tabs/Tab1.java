package biz.africanbib.Tabs;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import biz.africanbib.Activity.MainActivity;
import biz.africanbib.Adapters.ComplexRecyclerViewAdapter;
import biz.africanbib.Models.Add;
import biz.africanbib.Models.Divider;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.SimpleEditTextBuilder;
import biz.africanbib.Models.SimpleText;
import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Helper;

//Our class extending fragment
public class Tab1 extends Fragment {

    RecyclerView recyclerView;
    ComplexRecyclerViewAdapter adapter;
    Helper helper;
    boolean isTab;
    DatabaseHelper databaseHelper;
    ArrayList<Object> items = new ArrayList<>();
    public String businessName = "Business Name";
    public String telephone = "Telephone";
    public String city_town = "City / Town";
    public String state = "District / State";
    public String country = "Country";


    int a = 0;
    public ArrayList<Object> getList()
    {
        return items ;
    }
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_1, container, false);
        Log.d("Company", "Trying to initialize");
        helper = new Helper(this.getContext());
        isTab = helper.isTab();
        databaseHelper = new DatabaseHelper(view.getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        init(view);
        a = 10;
        return view;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_1);
        adapter = new ComplexRecyclerViewAdapter(getSampleArrayList(), getFragmentManager(),this);
        if(MainActivity.first) {
            if (isTab) {
                setupGridLayout(true);
            } else {
                setupGridLayout(false);
            }
            MainActivity.first = false;
        }
        /*manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItem(position) instanceof Heading)
                    return 2;
                else
                    return 1;
            }
        });
        */
        recyclerView.setAdapter(adapter);
        //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        //snapHelper.attachToRecyclerView(recyclerView);
        Log.d("Company", "Adapter set");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        /*
        // Check for the rotation
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this.getContext(), "LANDSCAPE", Toast.LENGTH_SHORT).show();
            setupGridLayout(true);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this.getContext(), "PORTRAIT", Toast.LENGTH_SHORT).show();
            if (isTab) {
                setupGridLayout(true);
            } else {
                setupGridLayout(false);
            }


        }
        */
    }


    private void setupGridLayout(boolean multiColumn) {
        if (multiColumn) {
            GridLayoutManager manager = new GridLayoutManager(this.getContext(), 2);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Object ob = adapter.getItem(position);
                    if (ob instanceof Heading || ob instanceof Add || ob instanceof SimpleText || ob instanceof Divider)
                        return 2;
                    else
                        return 1;
                }
            });
            recyclerView.setLayoutManager(manager);
        } else {
            GridLayoutManager manager = new GridLayoutManager(this.getContext(), 1);
            recyclerView.setLayoutManager(manager);
        }

    }

    public ComplexRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    private ArrayList<Object> getSampleArrayList() {
        items = new ArrayList<>();


        items.add(new Heading("COMPANY / INSTITUION PROFILE",null));

        String columnName = DatabaseHelper.COLUMN_COMPANY_NAME;
        String tableName = DatabaseHelper.TABLE_COMPANY_PROFILE;

        String value = databaseHelper.getStringValue(columnName, tableName);
        Log.v("Tab1", "Value = " + value);

        items.add(new SimpleEditTextBuilder()
                .setTableName(tableName)
                .setColumnName(columnName)
                .setTitle(businessName)
                .setValue(value)
                .setRowno(-1)
                .createSimpleEditText());


        columnName = DatabaseHelper.COLUMN_REGISTERATION_NO;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Registeration No", value, tableName, columnName, -1,"registrationNumber"));

        columnName = DatabaseHelper.COLUMN_LOGO;
        Bitmap image = null;
        try
        {
            image = helper.createBitmapFromByteArray(databaseHelper.getBlobValue(columnName,tableName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //=  databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildImage("Corporate Logo", image, tableName, columnName,"logo"));
        //items.add(helper.buildDropDown("Corporate Logo", new String[]{"Collected", "Not Collected"}, selectedPosition, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_KEYVISUAL_PHOTO;
        Bitmap keyvisual = null;
        try
        {
            keyvisual = helper.createBitmapFromByteArray(databaseHelper.getBlobValue(columnName,tableName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        items.add(helper.buildImage("Keyvisual (Photo)", keyvisual, tableName, columnName,"sdryger"));
        //items.add(helper.buildDropDown("Keyvisual (Photo)", new String[]{"Collected", "Not Collected"}, selectedPosition, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_LOGO_NOTE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Logo Note", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_KEYVISUAL_NOTE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Key Visual Note", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_BRIEF_DESCRIPTION;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Brief Description of the Company / Instituion", value, tableName, columnName, -1));

        items.add(new Heading("COMPANY CONTACT","contact"));

        columnName = DatabaseHelper.COLUMN_TELEPHONE;
        tableName = DatabaseHelper.TABLE_COMPANY_CONTACT;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText(telephone, value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_CELLPHONE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Cellphone", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_FASCIMILE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Fascimile", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_EMAIL;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Email", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_WEBSITE;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Website", value, tableName, columnName, -1));

        items.add(new Heading("COMPANY POSTAL ADRESS","contact"));

        columnName = DatabaseHelper.COLUMN_STREET;
        tableName = DatabaseHelper.TABLE_COMPANY_POSTAL_ADDRESS;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Street & Number", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_PO_BOX;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Post Office Box", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_POSTAL_CODE;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Postal Code", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_CITY;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("City / Town", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_COUNTRY;
        int selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Country", helper.getCountryNames(), selectedPosition, tableName, columnName, -1));


        items.add(new Heading("COMPANY PHYSICAL ADRESS","contact"));

        columnName = DatabaseHelper.COLUMN_STREET;
        tableName = DatabaseHelper.TABLE_COMPANY_PHYSICAL_ADDRESS;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Street & Number", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_CITY;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText(city_town, value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_POSTAL_CODE;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Postal Code", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_DISTRICT;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText(state, value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_STREET;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Street & Number", value, tableName, columnName, -1));

        columnName = DatabaseHelper.COLUMN_COUNTRY;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown(country, helper.getCountryNames(), selectedPosition, tableName, columnName, -1));


        items.add(new Heading("COMPANY SPECIFIC INFORMATION","about"));

        columnName = DatabaseHelper.COLUMN_LEGAL_FORM;
        tableName = DatabaseHelper.TABLE_COMPANY_SPECIFIC_INFORMATION;

        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Legal Form", value, tableName, columnName, -1,"legalForm"));

        columnName = DatabaseHelper.COLUMN_TYPE_OF_ORGANISATION;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Type of Orgainsation", new String[]{"Business Partnership",
                "International NGO",
                "Freelance",
                "Public Institution",
                "Individual Enterprise",
                "Local NGO",
                "Privately Held Company",
                "Publicly Held Institution"}, selectedPosition, tableName, columnName, -1,"typeOrganization"));
        columnName = DatabaseHelper.COLUMN_TYPE_OF_ACTIVITIES;
        selectedPosition = databaseHelper.getIntValue(columnName, tableName);
        items.add(helper.buildDropDown("Type of Activities", new String[]{"Manufacturing", "Service Provider"}, selectedPosition, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_ABOUT_US;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("About Us", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_VISION;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Vision", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_MISSION_STATEMENT;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Mission Statement", value, tableName, columnName, -1));
        columnName = DatabaseHelper.COLUMN_GUIDING_PRINCIPALS;
        value = databaseHelper.getStringValue(columnName, tableName);
        items.add(helper.buildEditText("Guiding Principals", value, tableName, columnName, -1));

        return items;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("Tab1", "Request Code  " + requestCode);
            adapter.onActivityResult(requestCode, resultCode, data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}