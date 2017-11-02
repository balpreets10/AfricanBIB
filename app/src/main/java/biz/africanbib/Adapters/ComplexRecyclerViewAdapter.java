package biz.africanbib.Adapters;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.africanbib.Models.Add;
import biz.africanbib.Models.Divider;
import biz.africanbib.Models.DropDown;
import biz.africanbib.Models.Heading;
import biz.africanbib.Models.MultiSelectDropdown;
import biz.africanbib.Models.SimpleDate;
import biz.africanbib.Models.SimpleEditText;
import biz.africanbib.Models.SimpleImage;
import biz.africanbib.Models.SimpleText;
import biz.africanbib.R;
import biz.africanbib.Tools.DatabaseHelper;
import biz.africanbib.Tools.Helper;
import biz.africanbib.ViewHolders.MyCustomMultiSelectionSpinner;
import biz.africanbib.ViewHolders.ViewHolderHeading;


/**
 * Created by Balpreet on 30-Jul-17.
 */

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Object> items;
    Helper helper;
    FragmentManager fragmentManager;
    DatabaseHelper databaseHelper;
    private int currentRowOffers = 1;
    private int currentRowReferences = 1;
    private int currentRowOwners = 1;
    private int currentRowAwards = 1;
    private int currentRowIndustry = 1;
    private int currentRowLatestNews = 1;
    private int currentRowNeeds = 1;
    private int currentRowManagers = 1;
    private int currentRowProducts = 1;
    private int currentRowServices = 1;

    Fragment context;

   ImagePicker imagePicker;


    private final int
            HEADING = 0,
            SIMPLEEDITTEXT = 1,
            DROPDOWN = 2,
            ADD = 3,
            SIMPLETEXT = 4,
            DIVIDER = 5,
            MULTISELECT = 6,
            DATE = 7,
            IMAGE = 8;


    public ComplexRecyclerViewAdapter(List<Object> items, FragmentManager manager, Fragment context) {
        this.items = items;
        this.fragmentManager = manager;
        helper = new Helper();
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {

        int val = -1;
        //Log.v("Adapter", "Instance of  = " + items.get(position).toString());
        if (items.get(position) instanceof Heading) {
            val = HEADING;
        } else if (items.get(position) instanceof SimpleEditText) {
            val = SIMPLEEDITTEXT;
        } else if (items.get(position) instanceof DropDown) {
            val = DROPDOWN;
        } else if (items.get(position) instanceof Add) {
            val = ADD;
        } else if (items.get(position) instanceof SimpleText) {
            val = SIMPLETEXT;
        } else if (items.get(position) instanceof Divider) {
            val = DIVIDER;
        } else if (items.get(position) instanceof MultiSelectDropdown) {
            val = MULTISELECT;
        } else if (items.get(position) instanceof SimpleDate) {
            val = DATE;
        } else if (items.get(position) instanceof SimpleImage) {
            val = IMAGE;
        }
        //Log.v("Adapter", "Case = " + val);
        return val;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        databaseHelper = new DatabaseHelper(viewGroup.getContext(), DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        switch (viewType) {
            case HEADING:
                View v0 = inflater.inflate(R.layout.viewholder_heading, viewGroup, false);
                viewHolder = new ViewHolderHeading(v0);
                break;
            case SIMPLEEDITTEXT:
                View v1 = inflater.inflate(R.layout.viewholder_simple_edit_text, viewGroup, false);
                viewHolder = new ViewHolderSimpleEditText(v1, new CustomEditTextListener(), new CustomFocusChangeListener());
                break;
            case DROPDOWN:
                View viewDropDown = inflater.inflate(R.layout.viewholder_spinner, viewGroup, false);
                viewHolder = new ViewHolderDropDown(viewDropDown, new CustomItemSelectedListener());
                break;
            case ADD:
                View viewAdd = inflater.inflate(R.layout.viewholder_add_button, viewGroup, false);
                viewHolder = new ViewHolderAdd(viewAdd, new CustomOnClickListener());
                break;
            case SIMPLETEXT:
                View viewSimpleText = inflater.inflate(R.layout.viewholder_simple_text, viewGroup, false);
                viewHolder = new ViewHolderSimpleText(viewSimpleText);
                break;
            case DIVIDER:
                View viewDivider = inflater.inflate(R.layout.viewholder_divider, viewGroup, false);
                viewHolder = new ViewHolderDivider(viewDivider);
                break;

            case MULTISELECT:
                View viewMultiSelect = inflater.inflate(R.layout.viewholder_multi_select, viewGroup, false);
                viewHolder = new ViewHolderMultiSelectSpinner(viewMultiSelect, new CustomOnMultiSelectClickListener());
                break;
            case DATE:
                View viewDate = inflater.inflate(R.layout.viewholder_simple_date, viewGroup, false);
                viewHolder = new ViewHolderDate(viewDate, new CustomDateChooser());
                break;
            case IMAGE:
                View viewImage = inflater.inflate(R.layout.viewholder_simple_image, viewGroup, false);
                viewHolder = new ViewHolderSimpleImage(viewImage, new CustomImageChooser());
                break;
            default:
                //View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                //viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //Log.v("Adapter", "Binding at position = " + position);
        switch (viewHolder.getItemViewType()) {
            case HEADING:
                ViewHolderHeading vh0 = (ViewHolderHeading) viewHolder;
                configureViewHolderHeading(vh0, position);
                break;
            case SIMPLEEDITTEXT:
                ViewHolderSimpleEditText viewHolderSimpleEditText = (ViewHolderSimpleEditText) viewHolder;
                configureViewHolderSimpleEditText(viewHolderSimpleEditText, position);
                break;
            case DROPDOWN:
                ViewHolderDropDown viewHolderDropDown = (ViewHolderDropDown) viewHolder;
                configureViewHolderDropDown(viewHolderDropDown, position);
                break;
            case ADD:
                ViewHolderAdd viewHolderAdd = (ViewHolderAdd) viewHolder;
                configureViewHolderAdd(viewHolderAdd, position);
                break;
            case SIMPLETEXT:
                ViewHolderSimpleText viewHolderSimpleText = (ViewHolderSimpleText) viewHolder;
                configureViewHolderSimpleText(viewHolderSimpleText, position);
                break;
            case DIVIDER:
                ViewHolderDivider viewHolderDivider = (ViewHolderDivider) viewHolder;
                configureViewHolderDivider(viewHolderDivider, position);
                break;
            case MULTISELECT:
                ViewHolderMultiSelectSpinner viewHolderMultiSelectSpinner = (ViewHolderMultiSelectSpinner) viewHolder;
                configureViewHolderMultiSelect(viewHolderMultiSelectSpinner, position);
                break;
            case DATE:
                ViewHolderDate viewHolderDate = (ViewHolderDate) viewHolder;
                configureViewHolderSimpleDate(viewHolderDate, position);
                break;
            case IMAGE:
                ViewHolderSimpleImage viewHolderImage = (ViewHolderSimpleImage) viewHolder;
                configureViewHolderSimpleImage(viewHolderImage, position);
                break;
            default:
                break;
        }
    }

    public Object getItem(int position) {
        //Log.v("Adapter", "Position = " + position);
        return items.get(position);

    }


    public class ViewHolderSimpleEditText extends RecyclerView.ViewHolder {

        private EditText editText;
        private TextView textview;
        public CustomEditTextListener customEditTextListener;
        public CustomFocusChangeListener customFocusChangeListener;

        public ViewHolderSimpleEditText(View v, CustomEditTextListener customEditTextListener, CustomFocusChangeListener customFocusChangeListener) {
            super(v);
            editText = (EditText) v.findViewById(R.id.edit_text_simple_edit_text);
            textview = (TextView) v.findViewById(R.id.text_view_simple_edit_text);
            this.customEditTextListener = customEditTextListener;
            this.customFocusChangeListener = customFocusChangeListener;
            this.editText.setFocusable(true);
            this.editText.addTextChangedListener(this.customEditTextListener);
            this.editText.setOnFocusChangeListener(this.customFocusChangeListener);
        }


        public void setEditTextType(int type) {
            if (type == SimpleEditText.TEXT) {
                this.editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
            } else if (type == SimpleEditText.NUMBER) {
                this.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (type == SimpleEditText.DATE) {
                this.editText.setInputType(InputType.TYPE_CLASS_DATETIME);
            } else if (type == SimpleEditText.EMAIL) {
                this.editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
        }

        public void setHint(String hint) {
            this.editText.setHint(hint);
        }

        public void setValue(String value) {
            this.editText.setText(value);
        }

        public String getValue() {
            return this.editText.getText().toString();
        }


        public void setTitle(String value) {
            this.textview.setText(value);
        }

        public void setTag(String value) {
            this.editText.setTag(value);
        }

        public String getTitle() {
            return this.textview.getText().toString();
        }

        public void setFocus(boolean requestFocus) {
            if (requestFocus) {
                this.editText.requestFocus();
            }
        }
    }

    public class ViewHolderDropDown extends RecyclerView.ViewHolder {
        private TextView textview;
        private AppCompatSpinner dropdownlist;
        private CustomItemSelectedListener customItemSelectedListener;

        ArrayAdapter<String> arrayAdapter;


        public ViewHolderDropDown(View v, CustomItemSelectedListener customItemSelectedListener) {
            super(v);
            arrayAdapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_row);
            textview = (TextView) v.findViewById(R.id.text_view_dropdown_title);
            dropdownlist = (AppCompatSpinner) v.findViewById(R.id.spinner_dropdown);
            this.customItemSelectedListener = customItemSelectedListener;
            this.dropdownlist.setOnItemSelectedListener(this.customItemSelectedListener);
            this.dropdownlist.setOnTouchListener(this.customItemSelectedListener);
        }

        public void setTitle(String value) {
            this.textview.setText(value);
            this.dropdownlist.setTag(value);
        }

        public String getTitle() {
            return this.textview.getText().toString();
        }

        public void setDropdownlist(String[] list) {
            arrayAdapter.clear();
            arrayAdapter.addAll(list);
            dropdownlist.setAdapter(arrayAdapter);
        }

        public void setSelectedItem(int selectedPosition) {
            Log.v("Adapter", "Selected item of " + textview.getText() + " = " + selectedPosition);
            this.dropdownlist.setSelection(selectedPosition);
            //this.arrayAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolderAdd extends RecyclerView.ViewHolder {
        private FloatingActionButton add;
        private int rows;

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        private CustomOnClickListener onClickListener;

        public ViewHolderAdd(View v, CustomOnClickListener customOnClickListener) {
            super(v);
            this.add = (FloatingActionButton) v.findViewById(R.id.button_add);
            this.onClickListener = customOnClickListener;
            this.add.setOnClickListener(this.onClickListener);
        }
    }

    public class ViewHolderSimpleText extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolderSimpleText(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.text_view_simple_text);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public String getTitle() {
            return this.title.getText().toString();
        }
    }

    public class ViewHolderMultiSelectSpinner extends RecyclerView.ViewHolder {
        private TextView title;
        private MyCustomMultiSelectionSpinner myCustomMultiSelectionSpinner;
        private CustomOnMultiSelectClickListener customOnMultiSelectClickListener;

        public ViewHolderMultiSelectSpinner(View v, CustomOnMultiSelectClickListener customOnMultiSelectClickListener) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.text_view_multi_select_title);
            this.myCustomMultiSelectionSpinner = (MyCustomMultiSelectionSpinner) v.findViewById(R.id.multi_select_Spinner);
            this.customOnMultiSelectClickListener = customOnMultiSelectClickListener;
            this.myCustomMultiSelectionSpinner.setListener(this.customOnMultiSelectClickListener);

        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setItems(String[] items) {
            if (items != null)
                this.myCustomMultiSelectionSpinner.setItems(items);
        }

        public void setSelected(List<Integer> selected) {

            if (selected == null) {
                this.myCustomMultiSelectionSpinner.setSelection(new int[]{});
            } else {
                for (int i :
                        selected) {
                    Log.v("Adapter", "Setting = " + i);

                }


                int[] selection = new int[selected.size()];
                for (int i = 0; i < selected.size(); i++) {
                    selection[i] = selected.get(i);
                }
                this.myCustomMultiSelectionSpinner.setSelection(selection);
            }
        }

        public void setTag(String tag) {
            this.myCustomMultiSelectionSpinner.setTag(tag);
        }


    }

    public class ViewHolderDivider extends RecyclerView.ViewHolder {
        private View divider;

        public ViewHolderDivider(View v) {
            super(v);
            this.divider = (View) v.findViewById(R.id.divider);
        }

    }

    public class ViewHolderDate extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView textview;
        private CustomDateChooser customDateChooser;

        public ViewHolderDate(View v, CustomDateChooser customDateChooser) {
            super(v);
            date = (TextView) v.findViewById(R.id.text_date);
            textview = (TextView) v.findViewById(R.id.text_view_simple_date);
            this.date.setFocusable(true);
            this.customDateChooser = customDateChooser;
            date.setOnClickListener(this.customDateChooser);
        }

        public void setHint(String hint) {
            this.date.setHint(hint);
        }

        public void setValue(String value) {
            this.date.setText(value);
        }

        public String getValue() {
            return this.date.getText().toString();
        }


        public void setTitle(String value) {
            this.textview.setText(value);
        }

        public void setTag(String value) {
            this.date.setTag(value);
        }

        public String getTitle() {
            return this.textview.getText().toString();
        }
    }

    public class ViewHolderSimpleImage extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textview;
        private CustomImageChooser customImageChooser;

        public void setImage(Bitmap image) {
            this.imageView.setImageBitmap(image);
        }

        public ViewHolderSimpleImage(View v, CustomImageChooser customImageChooser) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image_view);
            textview = (TextView) v.findViewById(R.id.text_view_simple_image);
            this.customImageChooser = customImageChooser;
            imageView.setOnClickListener(this.customImageChooser);
        }


        public void setTitle(String value) {
            this.textview.setText(value);
        }

        public String getTitle() {
            return this.textview.getText().toString();
        }
    }


    private void configureViewHolderHeading(ViewHolderHeading viewHolderHeading, int position) {
        Heading heading = (Heading) items.get(position);
        if (heading != null) {
            viewHolderHeading.setHeading(heading.getHeading());

        }
    }

    private void configureViewHolderDropDown(ViewHolderDropDown viewHolderDropDown, int position) {
        DropDown dropDown = (DropDown) items.get(position);
        if (dropDown != null) {
            viewHolderDropDown.customItemSelectedListener.updatePosition(position);
            viewHolderDropDown.setTitle(dropDown.getHeading());
            viewHolderDropDown.setDropdownlist(dropDown.getList());
            viewHolderDropDown.setSelectedItem(dropDown.getSelectedPosition());
        }
    }

    private void configureViewHolderSimpleEditText(ViewHolderSimpleEditText viewHolderSimpleEditText, int position) {
        SimpleEditText simpleEditText = (SimpleEditText) items.get(position);
        if (simpleEditText != null) {
            viewHolderSimpleEditText.customEditTextListener.updatePosition(position);
            viewHolderSimpleEditText.customFocusChangeListener.updatePosition(position);
            viewHolderSimpleEditText.setTitle(simpleEditText.getTitle());
            viewHolderSimpleEditText.setHint("Enter " + simpleEditText.getTitle());
            viewHolderSimpleEditText.setValue(simpleEditText.getValue());
            viewHolderSimpleEditText.setTag(simpleEditText.getTitle());
            viewHolderSimpleEditText.setEditTextType(simpleEditText.getType());
            viewHolderSimpleEditText.setFocus(simpleEditText.isFocused());
        }
    }

    private void configureViewHolderSimpleImage(ViewHolderSimpleImage viewHolderSimpleImage, int position) {
        SimpleImage simpleImage = (SimpleImage) items.get(position);
        if (simpleImage != null) {
            viewHolderSimpleImage.customImageChooser.updatePosition(position);
            viewHolderSimpleImage.setTitle(simpleImage.getTitle());
            if (simpleImage.getImage() == null) {

            } else {
                viewHolderSimpleImage.setImage(simpleImage.getImage());
            }

        }
    }

    private void configureViewHolderSimpleText(ViewHolderSimpleText viewHolderSimpleText, int position) {
        SimpleText simpleText = (SimpleText) items.get(position);
        if (simpleText != null) {
            viewHolderSimpleText.setTitle(simpleText.getTitle());
        }
    }

    private void configureViewHolderMultiSelect(ViewHolderMultiSelectSpinner viewHolderMultiSelectSpinner, int position) {
        MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) items.get(position);
        if (multiSelectDropdown != null) {
            viewHolderMultiSelectSpinner.customOnMultiSelectClickListener.updatePosition(position);
            viewHolderMultiSelectSpinner.setTitle(multiSelectDropdown.getTitle());
            viewHolderMultiSelectSpinner.setItems(multiSelectDropdown.getItems());
            Log.v("Adapter", "Selecting Indices for " + multiSelectDropdown.getTitle() + " = " + multiSelectDropdown.getSelectedIndices().toString());
            viewHolderMultiSelectSpinner.setSelected(multiSelectDropdown.getSelectedIndices());

            viewHolderMultiSelectSpinner.setTag(multiSelectDropdown.getTitle());
        }
    }

    private void configureViewHolderDivider(ViewHolderDivider viewHolderDivider, int position) {

    }

    private void configureViewHolderAdd(ViewHolderAdd viewHolderAdd, int position) {
        Add add = (Add) items.get(position);
        if (add != null) {
            viewHolderAdd.onClickListener.updatePosition(position);
            viewHolderAdd.setRows(add.getRows());
        }

    }

    private void configureViewHolderSimpleDate(ViewHolderDate viewHolderDate, int position) {
        SimpleDate simpleDate = (SimpleDate) items.get(position);
        if (simpleDate != null) {
            viewHolderDate.customDateChooser.updatePosition(position);
            viewHolderDate.setTitle(simpleDate.getTitle());
            viewHolderDate.setValue(simpleDate.getValue());
            viewHolderDate.setTag(simpleDate.getTitle());
        }
    }


    private class CustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ((SimpleEditText) items.get(position)).setValue(charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {

            //notifyItemChanged(position);
        }
    }

    private class CustomItemSelectedListener implements AppCompatSpinner.OnItemSelectedListener, View.OnTouchListener {
        private int position;
        boolean userSelect = false;

        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (userSelect) {
                DropDown dropDown = (DropDown) items.get(position);
                notifyItemChanged(position);
                if (dropDown.getRowno() == -1) {
                    if (dropDown.getHeading().equals("Place of Collection")) {
                        if (i == 2) {
                            items.add(position + 1, helper.buildEditText("Place of Collection (Specify)", "", DatabaseHelper.TABLE_SOURCE_OF_DATA, DatabaseHelper.COLUMN_OTHERS_SPECIFY, -1, "placeofcollection"));
                            notifyItemInserted(position + 1);
                        }
                    }
                    databaseHelper.updateIntValue(dropDown.getTableName(), dropDown.getColumnName(), i);
                } else {
                    if (dropDown.getHeading().equals("Industry")) {
                        Log.v("Adapter", "Industry Selected = " + i);
                        manageMultiSelectList(i);
                        MultiSelectDropdown multiselect = (MultiSelectDropdown) items.get(position + 1);
                        multiselect.setSelectedIndices(new ArrayList<Integer>());
                        notifyItemChanged(position + 1);
                    }
                    databaseHelper.updateRowWithInt(dropDown.getTableName(), dropDown.getRowno(), dropDown.getColumnName(), i);
                }


                //Log.v("Adapter", "Selected item of " + dropDown.getHeading() + " = " + i);
                dropDown.setSelectedPosition(i);

                notifyDataSetChanged();
                userSelect = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            userSelect = true;
            Log.v("Adapter", "Clicked");
            return false;
        }

        private void manageMultiSelectList(int i) {
            MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) items.get(position + 1);
            switch (i) {
                case 0:
                    multiSelectDropdown.setItems(new String[]{
                            "Horticulture & Forestry",
                            "Cereals & Grains	",
                            "Fruits & Vegetables",
                            "Animal Production",
                            "Plants & Fertilizers",
                            "Agricultural Production",
                            "Animals & Livestock"
                    });
                    break;
                case 1:
                    multiSelectDropdown.setItems(new String[]{
                            "Banking Institutions",
                            "Tax Services",
                            "Non-Compulsory Pensions",
                            "Insurance",
                            "Financial Consultants",
                            "insurance & Savings",
                            "Venture capital/Private Equity",
                            "Micro & SME Finance",
                            "Payment Systems, Securities Clearance & Settlement"
                    });
                    break;
                case 2:
                    multiSelectDropdown.setItems(new String[]{
                            "Aerospace",
                            "Electrical Engineering",
                            "Equipment",
                            "Structural Engineering",
                            "Traffic Engineering",
                            "Civil Engineering",
                            "Architecture & Planning",
                            "Finishings",
                            "Mechanical / Industrial Engineering",
                            "Public Works",
                            "Landscape Architecture",
                            "Construction",
                            "Building Materials",
                            "Metrology / Control Engineering"
                    });
                    break;
                case 3:
                    multiSelectDropdown.setItems(new String[]{
                            "Design",
                            "Media Production",
                            "Online Media",
                            "Print Media",
                            "Graphic Design",
                            "Photography"
                    });
                    break;
                case 4:
                    multiSelectDropdown.setItems(new String[]{
                            "Administrative Services",
                            "Retail Outlets",
                            "Property",
                            "Translating & Interpreting",
                            "Storage",
                            "Design/Research Offices",
                            "Maintenance/Repairs",
                            "Training",
                            "Professional Bodies",
                            "Security Services",
                            "Packaging",
                            "Environment",
                            "Cleaning"
                    });
                    break;
                case 5:
                    multiSelectDropdown.setItems(new String[]{
                            "Chemicals – Basic Products & Derivates",
                            "Pharmaceuticals",
                            "Cosmetics"
                    });
                    break;
                case 6:
                    multiSelectDropdown.setItems(new String[]{
                            "Bread & Cakes",
                            "Meats",
                            "Food Processing",
                            "Frozen Foods",
                            "Drinks",
                            "Wines",
                            "Dairy Products",
                            "Tinned Foods"
                    });
                    break;
                case 7:
                    multiSelectDropdown.setItems(new String[]{
                            "Energy",
                            "Raw Materials",
                            "Stones & Minerals",
                            "Water"
                    });
                    break;
                case 8:
                    multiSelectDropdown.setItems(new String[]{
                            "Sheet Material & Tubes",
                            "Tools & Hardware",
                            "Steels & Metals",
                            "Transformation",
                            "Steels & Metals",
                            "Finished Metal Products"
                    });
                    break;
                case 9:
                    multiSelectDropdown.setItems(new String[]{
                            "Agriculture",
                            "Environment",
                            "Metals",
                            "Temperature Control",
                            "Food Industry",
                            "Paper / Printing",
                            "Wood",
                            "Textiles",
                            "Construction",
                            "Handling",
                            "Plastics & Rubber",
                            "Chemicals & Pharmaceutics",
                            "Maritime"
                    });
                    break;
                case 10:
                    multiSelectDropdown.setItems(new String[]{
                            "Aeronautics",
                            "Materials & Equipment",
                            "Rental",
                            "Industrial Vehicles",
                            "Cars",
                            "Boats",
                            "Cycles"
                    });
                    break;
                case 11:
                    multiSelectDropdown.setItems(new String[]{
                            "Packaging - Plastic",
                            "Plastics",
                            "Rubber"
                    });
                    break;
                case 12:
                    multiSelectDropdown.setItems(new String[]{
                            "Furniture for Business",
                            "Wood Carving",
                            "Home Furniture",
                            "Wood – Finished Products",
                            "Wood"
                    });
                    break;
                case 13:
                    multiSelectDropdown.setItems(new String[]{
                            "Ceramics",
                            "Construction Materials",
                            "Glass"
                    });
                    break;
                case 14:
                    multiSelectDropdown.setItems(new String[]{
                            "Clock / Watch Making",
                            "Measurements - Equipment / Instruments",
                            "Optical Equipment / Instruments"
                    });
                    break;
                case 15:
                    multiSelectDropdown.setItems(new String[]{
                            "PC Hardware",
                            "IT Services",
                            "Peripherals - PC",
                            "Software"
                    });
                    break;
                case 16:
                    multiSelectDropdown.setItems(new String[]{
                            "Audio - Visual",
                            "Cables & Networks",
                            "Internet & Telephony",
                            "Office Automation"
                    });
                    break;
                case 17:
                    multiSelectDropdown.setItems(new String[]{
                            "Motors",
                            "Lighting",
                            "Electronic Equipment",
                            "Electricity",
                            "Security",
                            "Hi-Fi & Household Appliances",
                            "Electrical / Electronic Components"
                    });
                    break;
                case 18:
                    multiSelectDropdown.setItems(new String[]{
                            "Finished Products",
                            "Raw Materials",
                            "Packaging Materials – Paper & Cardboard"
                    });
                    break;
                case 19:
                    multiSelectDropdown.setItems(new String[]{
                            "Leather – Raw Materials",
                            "Leather Goods",
                            "Shoes"
                    });
                    break;
                case 20:
                    multiSelectDropdown.setItems(new String[]{
                            "Clothes",
                            "Clothing Accessories",
                            "Fabrics",
                            "Household Linen & Fabrics"
                    });
                    break;
                case 21:
                    multiSelectDropdown.setItems(new String[]{
                            "Electronic Publishing",
                            "Printing",
                            "Publishing"
                    });
                    break;
                case 22:
                    multiSelectDropdown.setItems(new String[]{
                            "Arts & Entertainment",
                            "Presents",
                            "Sport - Articles & Equipment",
                            "Jewellery",
                            "Games & Toys",
                            "Co-packing"
                    });
                    break;
                case 23:
                    multiSelectDropdown.setItems(new String[]{
                            "Air Transport",
                            "Specialized Transport",
                            "Sea & River Transport",
                            "Road Transport",
                            "Rail Transport",
                            "Transport Auxiliaries"
                    });
                    break;
                case 24:
                    multiSelectDropdown.setItems(new String[]{
                            "Hotel Business & Catering",
                            "Sports & Leisure Activities",
                            "Trade Shows",
                            "Travel"
                    });
                    break;
                case 25:
                    multiSelectDropdown.setItems(new String[]{
                            "Advertising",
                            "Advertising Media",
                            "Marketing",
                            "Print Media"
                    });
                    break;
                case 26:
                    multiSelectDropdown.setItems(new String[]{
                            "Biotechnologies",
                            "Fitness & Spa",
                            "Nursing",
                            "Health Centres",
                            "Medical Services",
                            "Medical Equipment",
                            "Physical Therapy",
                            "Rehabilitation",
                            "Home Care"
                    });
                    break;
                case 27:
                    multiSelectDropdown.setItems(new String[]{
                            "Academia",
                            "Libraries",
                            "E-learning Centers",
                            "Day care",
                            "Adult Literacy / Non-Formal Education",
                            "Pre - Primary Education"
                    });
                    break;
                case 28:
                    multiSelectDropdown.setItems(new String[]{
                            "Primary Education",
                            "University",
                            "Secondary Education",
                            "Tertiary Education",
                            "Vocational Training"
                    });
                    break;
            }
            notifyItemChanged(position + 1);
            //notifyDataSetChanged();
            //items.add(position+1,multiSelectDropdown);
        }
    }


    private class CustomOnClickListener implements View.OnClickListener {
        private int position;
        private boolean clicked = false;

        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View view) {

            Add add = (Add) items.get(position);
            Log.v("Adapter", "Clicked at = " + add.getTableName());
            items.add(position, new Divider());
            String[] columnNames = add.getColumnNames();
            String[] tableColumnNames = add.getTableColumnNames();
            String[] xmlTags = add.getXmlTags();
            int currentRowNo = manageRowNo(add);
            Log.v("Adapter", "Current Row = " + currentRowNo);

            databaseHelper.insertRow(add.getTableName(), currentRowNo);

            for (int i = 0; i < add.getRows(); i++) {
                Log.v("Adapter", "Current Column = " + tableColumnNames[i]);

                if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_SECTOR)) {
                    items.add(position, helper.buildMultiSelectDropdown(columnNames[i],
                            add.getTableName(),
                            tableColumnNames[i],
                            helper.manageMultiSelectList(0),
                            null,
                            currentRowNo, "sector"
                    ));
                    notifyItemInserted(position);
                } else if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_INDUSTRY)) {
                    items.add(position, helper.buildDropDown(
                            columnNames[i],
                            helper.getIndustryList(),
                            databaseHelper.getIntFromRow(add.getTableName(), tableColumnNames[i], currentRowNo),
                            add.getTableName(), tableColumnNames[i], currentRowNo, "industry"));
                    notifyItemInserted(position);
                } else if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_TYPE_OF_ORGANISATION)) {
                    items.add(position, helper.buildDropDown(columnNames[i],
                            new String[]{"Business Partnership",
                                    "International NGO",
                                    "Freelance",
                                    "Public Institution",
                                    "Individual Enterprise",
                                    "Local NGO",
                                    "Privately Held Company",
                                    "Publicly Held Institution"},
                            databaseHelper.getIntFromRow(add.getTableName(), tableColumnNames[i], currentRowNo),
                            add.getTableName(), tableColumnNames[i], currentRowNo, xmlTags[i]));
                    notifyItemInserted(position);
                } else if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_DATE)) {
                    items.add(position, helper.buildDate(
                            columnNames[i],
                            databaseHelper.getStringFromRow(add.getTableName(), tableColumnNames[i], currentRowNo),
                            add.getTableName(),
                            tableColumnNames[i],
                            currentRowNo, "date"));
                    Log.v("Adapter", "Inserting Date " + columnNames[i] + " at " + i);
                    notifyItemInserted(position);
                } else if (columnNames[i].equals("Country")) {
                    items.add(position, helper.buildDropDown(columnNames[i], helper.getCountryNames(), 0, add.getTableName(), tableColumnNames[i], currentRowNo, "country"));
                    notifyItemInserted(position);
                } else if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_MEDIA)) {
                    Bitmap image = null;
                    try {
                        image = helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(tableColumnNames[i], add.getTableName(), currentRowNo));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    items.add(position, helper.buildImage(columnNames[i], currentRowNo, image, add.getTableName(), tableColumnNames[i], xmlTags[i]));
                    notifyItemInserted(position);
                } else if (tableColumnNames[i].equals(DatabaseHelper.COLUMN_LOGO)) {
                    Bitmap image = null;
                    try {
                        image = helper.createBitmapFromByteArray(databaseHelper.getBlobFromRow(tableColumnNames[i], add.getTableName(), currentRowNo));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    items.add(position, helper.buildImage(columnNames[i], currentRowNo, image, add.getTableName(), tableColumnNames[i], xmlTags[i]));
                    notifyItemInserted(position);
                } else {
                    items.add(position, helper.buildEditText(columnNames[i], "", add.getTableName(), tableColumnNames[i], currentRowNo, xmlTags[i]));
                    Log.v("Adapter", "Inserting Edittext " + columnNames[i] + " at " + i);
                    notifyItemInserted(position);
                }

            }

            if (!clicked) {
                items.add(position, new Divider());
                clicked = true;
            }
            Log.v("Adapter", "Inserted");
            notifyDataSetChanged();
        }


    }

    private class CustomImageChooser implements View.OnClickListener {
        private int position;
        private ComplexRecyclerViewAdapter adapter;


        public void updatePosition(int position) {
            this.position = position;
            adapter = ComplexRecyclerViewAdapter.this;

        }

        @Override
        public void onClick(final View view) {
            Log.v("Adapter", "Clicked on :" + position);
            final int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (!checkIfAlreadyhavePermission()) {
                    ActivityCompat.requestPermissions(context.getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    showImageChooser(adapter, position);
                }
            } else
                showImageChooser(adapter, position);
        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(context.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void showImageChooser(final ComplexRecyclerViewAdapter adapter, final int position) {
        final SimpleImage simpleImage = (SimpleImage) items.get(position);
        Log.v("Adapter", "Clicked at = " + simpleImage.getTitle());
        imagePicker = new ImagePicker();
        imagePicker.setTitle("Select Image");
        imagePicker.setCropImage(true);
        imagePicker.startChooser(context, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {
                Log.v("Adapter", "Picked Path = " + imageUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                simpleImage.setImage(bitmap);
                adapter.notifyItemChanged(position);
                if (simpleImage.getRowno() == -1)
                    databaseHelper.updateBlobValue(simpleImage.getTableName(), simpleImage.getColumnName(), helper.createByteArrayFromBitmap(bitmap));
                else
                    databaseHelper.updateRowWithBlob(simpleImage.getTableName(), simpleImage.getRowno(), simpleImage.getColumnName(), helper.createByteArrayFromBitmap(bitmap));
            }

            @Override
            public void onCropImage(Uri imageUri) {
                Log.v("Adapter", "Cropped Path = " + imageUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                simpleImage.setImage(bitmap);
                adapter.notifyItemChanged(position);
                //databaseHelper.updateBlobValue(simpleImage.getTableName(), simpleImage.getColumnName(), helper.createByteArrayFromBitmap(bitmap));
                if (simpleImage.getRowno() == -1)
                    databaseHelper.updateBlobValue(simpleImage.getTableName(), simpleImage.getColumnName(), helper.createByteArrayFromBitmap(bitmap));
                else
                    databaseHelper.updateRowWithBlob(simpleImage.getTableName(), simpleImage.getRowno(), simpleImage.getColumnName(), helper.createByteArrayFromBitmap(bitmap));

                //draweeView.setImageURI(imageUri);
                //draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
            }

            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                Point size = new Point();
                Point ratio = new Point();
                if (simpleImage.getTitle().equals("Corporate Logo")) {
                    size.set(210, 145);
                    ratio.set(42, 29);
                } else if (simpleImage.getTitle().equals("Keyvisual (Photo)")) {
                    size.set(942, 292);
                    ratio.set(471, 146);
                } else if (simpleImage.getTitle().equals("")) {

                } else {
                    size.set(210, 145);
                    ratio.set(42, 29);
                }
                builder
                        .setMultiTouchEnabled(true)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setRequestedSize(size.x, size.y)
                        .setAspectRatio(ratio.x, ratio.y);
            }


            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {

            }


        });

    }

    private class CustomDateChooser implements View.OnClickListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(final View view) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setAdapter(ComplexRecyclerViewAdapter.this);
            datePickerFragment.updatePosition(position);
            datePickerFragment.show(fragmentManager, "Date");
        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        private ComplexRecyclerViewAdapter adapter;

        public void setAdapter(ComplexRecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            SimpleDate simpleDate = (SimpleDate) adapter.items.get(position);
            month = month + 1;
            String stringOfDate = day + " - " + month + " - " + year;

            simpleDate.setValue(stringOfDate);
            adapter.notifyItemChanged(position);
            if (simpleDate.getRowno() == -1) {
                adapter.databaseHelper.updateStringValue(simpleDate.getTableName(), simpleDate.getColumnName(), stringOfDate);
            } else {
                adapter.databaseHelper.updateRowWithString(simpleDate.getTableName(), simpleDate.getRowno(), simpleDate.getColumnName(), stringOfDate);
            }

        }
    }

    private class CustomFocusChangeListener implements View.OnFocusChangeListener {
        private int position;
        private int currentlyFocusedPosition = -1;

        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                currentlyFocusedPosition = position;
                //Log.v("Complex Adapter", "Focused Position  = " + currentlyFocusedPosition + " and title = " + ((SimpleEditText) items.get(currentlyFocusedPosition)).getTitle());
            } else {
                if (currentlyFocusedPosition != -1) {
                    if (items.get(currentlyFocusedPosition) instanceof SimpleEditText) {
                        SimpleEditText simpleEditText = (SimpleEditText) items.get(currentlyFocusedPosition);
                        Log.v("Complex Adapter", "Lost Focus Position  = " + currentlyFocusedPosition +
                                "\nTitle = " + simpleEditText.getTitle() +
                                "\nColumn = " + simpleEditText.getColumnName() +
                                "\nTable = " + simpleEditText.getTableName());
                        if (simpleEditText.getRowno() == -1) {
                            databaseHelper.updateStringValue(simpleEditText.getTableName(), simpleEditText.getColumnName(), simpleEditText.getValue());
                        } else {
                            databaseHelper.updateRowWithString(simpleEditText.getTableName(), simpleEditText.getRowno(), simpleEditText.getColumnName(), simpleEditText.getValue());
                        }
                    }
                }
            }
        }
    }

    private class CustomOnMultiSelectClickListener implements MyCustomMultiSelectionSpinner.OnMultipleItemsSelectedListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }


        @Override
        public void selectedIndices(List<Integer> indices) {
            MultiSelectDropdown multiSelectDropdown = (MultiSelectDropdown) items.get(position);

            Log.v("Adapter", "Selected Indices of " + multiSelectDropdown.getTitle() + " = " + indices.size());
            multiSelectDropdown.setSelectedIndices(indices);
            if (multiSelectDropdown.getRowno() == -1) {
                databaseHelper.updateStringValue(multiSelectDropdown.getTableName(), multiSelectDropdown.getColumnName(), helper.getStringFromIndices(indices));
            } else {
                String si = helper.getStringFromIndices(indices);
                Log.v("Adapter", "String updated =  " + si);
                databaseHelper.updateRowWithString(multiSelectDropdown.getTableName(), multiSelectDropdown.getRowno(), multiSelectDropdown.getColumnName(), si);

                databaseHelper.getTableData(DatabaseHelper.TABLE_SECTORS);
            }
            notifyItemChanged(position);
        }

        @Override
        public void selectedStrings(List<String> strings) {

        }
    }


    private int manageRowNo(Add add) {
        String tableName = add.getTableName();
        if (tableName.equals(DatabaseHelper.TABLE_OFFERS)) {
            return currentRowOffers++;
        } else if (tableName.equals(DatabaseHelper.TABLE_NEEDS)) {
            return currentRowNeeds++;
        } else if (tableName.equals(DatabaseHelper.TABLE_REFERENCES)) {
            return currentRowReferences++;
        } else if (tableName.equals(DatabaseHelper.TABLE_MANAGERS)) {
            return currentRowManagers++;
        } else if (tableName.equals(DatabaseHelper.TABLE_OWNERS)) {
            return currentRowOwners++;
        } else if (tableName.equals(DatabaseHelper.TABLE_PRODUCTS_AND_PRODUCT_DETAILS)) {
            return currentRowProducts++;
        } else if (tableName.equals(DatabaseHelper.TABLE_SERVICES)) {
            return currentRowServices++;
        } else if (tableName.equals(DatabaseHelper.TABLE_AWARDS)) {
            return currentRowAwards++;
        } else if (tableName.equals(DatabaseHelper.TABLE_LATEST_NEWS)) {
            return currentRowLatestNews++;
        } else if (tableName.equals(DatabaseHelper.TABLE_SECTORS)) {
            return currentRowIndustry++;
        }
        return 0;
    }

    public void updateRow(String tableName, int value) {
        Log.d("Adapter", "Updating within adapter");
        if (tableName.equals(DatabaseHelper.TABLE_OFFERS)) {
            currentRowOffers = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_NEEDS)) {
            currentRowNeeds = value;
        } /*else if (tableName.equals(DatabaseHelper.TABLE_ACADEMIC_BACKGROUND)) {
            currentRowAcademicBackground = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_PROFESSIONAL_BACKGROUND)) {
            currentRowProfessionalBackground = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_AFFILIATION)) {
            currentRowAffiliation = value;
        }*/ else if (tableName.equals(DatabaseHelper.TABLE_PRODUCTS_AND_PRODUCT_DETAILS)) {
            currentRowProducts = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_SERVICES)) {
            currentRowServices = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_AWARDS)) {
            currentRowAwards = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_LATEST_NEWS)) {
            currentRowLatestNews = value;
        } else if (tableName.equals(DatabaseHelper.TABLE_SECTORS)) {
            currentRowIndustry = value;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Adapter", "Request Code  " + requestCode);
        imagePicker.onActivityResult(context, requestCode, resultCode, data);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        imagePicker.onRequestPermissionsResult(context, requestCode, permissions, grantResults);
    }


    public void setFocus() {
        int a = 0;
        for (Object o :
                items) {
            if (o instanceof SimpleEditText) {
                SimpleEditText simpleEditText = (SimpleEditText) o;
                simpleEditText.setFocused(true);
                notifyItemChanged(a);
                return;
            }
            a++;
        }
    }
}