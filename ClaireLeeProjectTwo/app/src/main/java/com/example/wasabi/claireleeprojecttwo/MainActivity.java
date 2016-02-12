package com.example.wasabi.claireleeprojecttwo;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    FrameLayout mWhatshot, mTodaysPick, mFavorite;
    TextView mWhatshotTitle, mWhatshotNeighbor, mWhatshotKeyword;
    ImageView mWhatshotImage;

    Button mBreakfastButton, mLunchButton, mDinnerButton,
            mCafeButton, mFunButton, mNightLifeButton,
            mHappyHourButton, mShoppingButton, mDessertButton;

    Button mMidtown, mChelsea, mFlatiron, mLes, mUws, mUes, mUnionSq, mFinancial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        mBreakfastButton = (Button)findViewById(R.id.breakfast_button);
        mLunchButton = (Button)findViewById(R.id.lunch_button);
        mDinnerButton = (Button)findViewById(R.id.dinner_button);
        mFunButton = (Button)findViewById(R.id.fun_button);
        mCafeButton = (Button)findViewById(R.id.cafe_button);
        mNightLifeButton = (Button)findViewById(R.id.nightLife_button);
        mHappyHourButton = (Button)findViewById(R.id.happyHour_button);
        mShoppingButton = (Button)findViewById(R.id.shopping_button);
        mDessertButton = (Button)findViewById(R.id.dessert_button);

        mBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Breakfast");
                startActivity(intent);
            }
        });

        mLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Lunch");
                startActivity(intent);
            }
        });

        mDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Dinner");
                startActivity(intent);
            }
        });

        mCafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Cafe");
                startActivity(intent);
            }
        });

        mFunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Fun");
                startActivity(intent);
            }
        });

        mNightLifeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Night");
                startActivity(intent);
            }
        });

        mHappyHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Happy");
                startActivity(intent);
            }
        });

        mShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Shopping");
                startActivity(intent);
            }
        });

        mDessertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("GREATFOR_NAME","Dessert");
                startActivity(intent);
            }
        });


        populateTodaysPickFrame();
        populateFavoriteFrame();
        populateNeighborButtons();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager)getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();

        SearchableInfo info = searchManager.getSearchableInfo(new ComponentName(this, ResultActivity.class));
        searchView.setSearchableInfo(info);
        return true;
    }

    public void populateTrendingFrame(){
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(MainActivity.this);

        mWhatshot = (FrameLayout) findViewById(R.id.trending_framelayout);
        mWhatshotTitle = (TextView) findViewById(R.id.trending_title);
        mWhatshotNeighbor = (TextView) findViewById(R.id.trending_neighbor);
        mWhatshotKeyword = (TextView) findViewById(R.id.trending_keyword);
        mWhatshotImage = (ImageView) findViewById(R.id.trending_imageview);

        Cursor cursor = helper.getRandomVenue(1);
        cursor.moveToFirst();
        mWhatshotTitle.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)));
        mWhatshotNeighbor.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NEIGHBOR)));
        mWhatshotKeyword.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_KEYWORD)));

        String photoId = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_PHOTO_ID));
        int drawableId = MainActivity.this.getResources().getIdentifier(photoId, "drawable", MainActivity.this.getPackageName());
        mWhatshotImage.setImageResource(drawableId);

        final int rowId = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_ID));

        mWhatshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("ITEM_ID", rowId);
                startActivity(intent);
            }
        });
    }

    public void populateTodaysPickFrame(){
        mTodaysPick = (FrameLayout)findViewById(R.id.todays_pic_framelayout);
        mTodaysPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("KEYWORD","Rooftop");
                startActivity(intent);
            }
        });
    }

    public void populateFavoriteFrame(){
        mFavorite = (FrameLayout)findViewById(R.id.favorite_framelayout);
        mFavorite
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("FAVORITE","Favorite");
                startActivity(intent);
            }
        });
    }

    public void populateNeighborButtons(){
        mMidtown = (Button)findViewById(R.id.main_midtown_button);
        mChelsea = (Button)findViewById(R.id.main_chelsea_button);
        mFlatiron = (Button)findViewById(R.id.main_flatiron_button);
        mLes = (Button)findViewById(R.id.main_les_button);
        mUws = (Button)findViewById(R.id.main_uws_button);
        mUes = (Button)findViewById(R.id.main_ues_button);
        mUnionSq = (Button)findViewById(R.id.main_unionsq_button);
        mFinancial = (Button)findViewById(R.id.main_financial_button);

        mMidtown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Midtown");
                startActivity(intent);
            }
        });
        mChelsea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Chelsea");
                startActivity(intent);
            }
        });
        mFlatiron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Flatiron");
                startActivity(intent);
            }
        });
        mLes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Lower East");
                startActivity(intent);
            }
        });
        mUws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Upper West");
                startActivity(intent);
            }
        });
        mUes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Upper East");
                startActivity(intent);
            }
        });
        mUnionSq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Union");
                startActivity(intent);
            }
        });
        mFinancial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                intent.putExtra("NEIGHBOR","Financial");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTrendingFrame();
    }
}
