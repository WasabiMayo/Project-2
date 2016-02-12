package com.example.wasabi.claireleeprojecttwo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity {

    CursorAdapter cursorAdapter;
    String query, priceFilter;
    int wifiFilter, creditCardFilter;
    boolean isFilterApplied;
    String receivedName, receivedTodaysPick, receivedFavoriteKeyword, receivedNeighbor;
    ImageView favoriteIV;
    CheckBox wifiCheckBox, creditcardCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        isFilterApplied = false;

        ListView resultListView = (ListView)findViewById(R.id.result_listview);
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(ResultActivity.this);
        Cursor cursor = helper.getNeighborList();
        receivedName = getIntent().getStringExtra("GREATFOR_NAME");
        receivedTodaysPick = getIntent().getStringExtra("KEYWORD");
        receivedFavoriteKeyword = getIntent().getStringExtra("FAVORITE");
        receivedNeighbor = getIntent().getStringExtra("NEIGHBOR");

        if(receivedName != null) {
            cursor = helper.getNeighborListByGreatFor(receivedName, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
        }
        if(receivedTodaysPick != null){
            cursor = helper.mainSearchFunction(receivedTodaysPick, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
        }
        if(receivedFavoriteKeyword != null){
            cursor = helper.getFavoriteList();
        }
        if(receivedNeighbor != null){
            cursor = helper.getNeighborListByNeighbor(receivedNeighbor, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
        }

        cursorAdapter = new CursorAdapter(ResultActivity.this,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.result_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                ImageView imageView = (ImageView)view.findViewById(R.id.result_cover_iv);
                TextView titleTV = (TextView)view.findViewById(R.id.result_title_tv);
                TextView neighborTV = (TextView)view.findViewById(R.id.result_neighbor_tv);
                TextView keywordTV = (TextView)view.findViewById(R.id.result_keyword_tv);
                TextView priceTV = (TextView)view.findViewById(R.id.result_price_tv);
                TextView ratingTV = (TextView)view.findViewById(R.id.result_rating_tv);
                TextView votesTV = (TextView)view.findViewById(R.id.result_votes_tv);
                favoriteIV = (ImageView)view.findViewById(R.id.result_favorite_iv);

                String photoId = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_PHOTO_ID));
                int drawableId = ResultActivity.this.getResources().getIdentifier(photoId, "drawable", context.getPackageName());
                imageView.setImageResource(drawableId);

                titleTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)));
                neighborTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NEIGHBOR)));
                keywordTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_KEYWORD)));
                priceTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_PRICE)));
                ratingTV.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_RATING))));
                votesTV.setText(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_VOTES)) + "VOTES");

                switch (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_FAVORITE))){
                    case 0:
                        favoriteIV.setImageResource(R.drawable.ic_favorite_border_small);
                        break;
                    case 1:
                        favoriteIV.setImageResource(R.drawable.ic_favorite_small);
                        break;
                }
            }
        };
        resultListView.setAdapter(cursorAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                Cursor newCursor = cursorAdapter.getCursor();
                newCursor.moveToPosition(position);
                intent.putExtra("ITEM_ID", newCursor.getInt(newCursor.getColumnIndex(MySQLiteOpenHelper.COL_ID)));
                startActivity(intent);
            }
        });


        Button filterButton = (Button)findViewById(R.id.result_filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilterDialog();
            }
        });

        handleIntent(getIntent());
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(ResultActivity.this, "Query : " + query, Toast.LENGTH_SHORT).show();
            Cursor newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).mainSearchFunction(query, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
            cursorAdapter.changeCursor(newCursor);
        }
    }

    public void launchFilterDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        LayoutInflater inflater = ResultActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.filter_dialog_layout, null);
        builder.setView(dialogView);

        builder.setTitle("Refine your search");

        final RadioGroup mPriceRadioGroup = (RadioGroup) dialogView.findViewById(R.id.filter_price_radiogroup);
        wifiCheckBox = (CheckBox) dialogView.findViewById(R.id.filter_wifi_checkbox);
        creditcardCheckBox = (CheckBox) dialogView.findViewById(R.id.filter_creditcard_checkbox);
        if(wifiFilter == 1){
            wifiCheckBox.setChecked(true);
        }
        if (creditCardFilter == 1) {
            creditcardCheckBox.setChecked(true);
        }

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedPriceButtonId = mPriceRadioGroup.getCheckedRadioButtonId();
                switch (selectedPriceButtonId) {
                    case R.id.filter_onedollar_button:
                        priceFilter = "$";
                        break;
                    case R.id.filter_twodollars_button:
                        priceFilter = "$$";
                        break;
                    case R.id.filter_threedollars_button:
                        priceFilter = "$$$";
                        break;
                    case R.id.filter_free_button:
                        priceFilter = "Free";
                        break;
                    default:
                        priceFilter = null;
                        break;
                }



                if(wifiCheckBox.isChecked()){
                    wifiFilter = 1;
                }
                if(creditcardCheckBox.isChecked()){
                    creditCardFilter = 1;
                }
                if(priceFilter != null || wifiFilter == 1 || creditCardFilter == 1) {
                    isFilterApplied = true;
                    swapToNewCursor();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog addingDialog = builder.create();
        addingDialog.show();
    }


    public void swapToNewCursor(){
        Cursor newCursor;
        if (isFilterApplied){
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).mainSearchFunction(query,isFilterApplied,priceFilter,wifiFilter,creditCardFilter);
            cursorAdapter.swapCursor(newCursor);
        }
        if(receivedFavoriteKeyword != null){
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).getFavoriteList();
            cursorAdapter.swapCursor(newCursor);
        }
        if(receivedName != null) {
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).getNeighborListByGreatFor(receivedName, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
            cursorAdapter.swapCursor(newCursor);
        }
        if(receivedTodaysPick != null){
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).mainSearchFunction(receivedTodaysPick, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
            cursorAdapter.swapCursor(newCursor);
        }
        if(receivedFavoriteKeyword != null){
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).getFavoriteList();
            cursorAdapter.swapCursor(newCursor);
        }
        if(receivedNeighbor != null){
            newCursor = MySQLiteOpenHelper.getInstance(ResultActivity.this).getNeighborListByNeighbor(receivedNeighbor, isFilterApplied, priceFilter, wifiFilter, creditCardFilter);
            cursorAdapter.swapCursor(newCursor);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //Swapping cursor to update any changes when the user comes back from the detail activity
        swapToNewCursor();
    }
}
