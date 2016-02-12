package com.example.wasabi.claireleeprojecttwo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ListView mDetailListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mCommentList;
    int receivedId;

    TextView ratingScoreTV, ratingVotesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        receivedId = intent.getIntExtra("ITEM_ID", -2);
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(DetailActivity.this);
        Cursor cursor = helper.getDetailsById(receivedId);
        cursor.moveToFirst();

        populateInitialData(cursor);
        mCommentList = new ArrayList<>();

        if (cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_COMMENT)) != null) {
            {
                String commentString = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_COMMENT));
                String[] commentArray = commentString.split("~");
                for (int i = 1; i < commentArray.length-1; i++) {
                    mCommentList.add(commentArray[i]);
                }
            }
        }
            mAdapter = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1, mCommentList);
            mDetailListView.setAdapter(mAdapter);

    }



    public void populateInitialData(Cursor cursor){

        mDetailListView = (ListView)findViewById(R.id.detail_listview);
        View header = getLayoutInflater().inflate(R.layout.detail_header,null,false);
        ImageView coverIV = (ImageView)header.findViewById(R.id.detail_cover_imageview);
        TextView titleTV = (TextView)header.findViewById(R.id.detail_title_textview);
        TextView neighborTV = (TextView)header.findViewById(R.id.detail_neighbor_textview);
        TextView wifiTV = (TextView)header.findViewById(R.id.detail_wifi_textview);
        TextView creditTV = (TextView)header.findViewById(R.id.detail_credit_textview);
        TextView addressTV = (TextView)header.findViewById(R.id.detail_address_textview);
        Button reviewButton = (Button)header.findViewById(R.id.detail_review_button);
        Button rateButton = (Button)header.findViewById(R.id.detail_rate_button);
        final FloatingActionButton favoriteButton = (FloatingActionButton)header.findViewById(R.id.favoriteFAB);
        ratingScoreTV = (TextView) header.findViewById(R.id.detail_rating_score_tv);
        ratingVotesTV = (TextView) header.findViewById(R.id.detail_rating_votes_tv);

        String photoId = cursor.getString(cursor.getColumnIndex("PHOTO_ID"));
        int drawableId = DetailActivity.this.getResources().getIdentifier(photoId, "drawable", DetailActivity.this.getPackageName());
        coverIV.setImageResource(drawableId);

        titleTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)));
        neighborTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NEIGHBOR)));
        addressTV.setText(cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_ADDRESS)));
        ratingScoreTV.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_RATING))));
        ratingVotesTV.setText((cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_VOTES)))+" VOTES");

        switch (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_WIFI))){
            case 0:
                wifiTV.setText("Wifi : No");
                break;
            case 1:
                wifiTV.setText("Wifi : Yes");
        }

        switch (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_CREDITCARD))){
            case 0:
                creditTV.setText("Credit card : No");
                break;
            case 1:
                creditTV.setText("Credit card : Yes");
        }

        switch (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_FAVORITE))){
            case 0:
                break;
            case 1:
                favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                break;
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(DetailActivity.this);
                Cursor cursor = helper.getDetailsById(receivedId);
                cursor.moveToFirst();
                switch (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_FAVORITE))){
                    case 0:
                        favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                        helper.updateFavorite(1, receivedId);
                        Toast.makeText(DetailActivity.this, "Bookmarked "+cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        helper.updateFavorite(0, receivedId);
                        Toast.makeText(DetailActivity.this, "Canceled the bookmark", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CommentActivity.class);
                intent.putExtra("ITEM_ID",receivedId);
                startActivity(intent);
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRatingDialog();
            }
        });



        mDetailListView.addHeaderView(header);
    }

    public void createRatingDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        LayoutInflater inflater = DetailActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_dialog_layout, null);
        builder.setView(dialogView);

        final RadioGroup mRadioGroup = (RadioGroup) dialogView.findViewById(R.id.rating_radiogroup);
        final MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(DetailActivity.this);
        Cursor cursor = helper.getDetailsById(receivedId);
        cursor.moveToFirst();

        builder.setTitle("How was " + cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)) + "?");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.rating_great_button:
                        helper.updateRating(10, receivedId);
                        break;
                    case R.id.rating_normal_button:
                        helper.updateRating(7, receivedId);
                        break;
                    case R.id.rating_dislike_button:
                        helper.updateRating(5, receivedId);
                        break;
                }
                Cursor cursor = helper.getDetailsById(receivedId);
                cursor.moveToFirst();
                ratingScoreTV.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_RATING))));
                ratingVotesTV.setText((cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_VOTES))) + " VOTES");
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

    @Override
    protected void onResume() {
        super.onResume();
        MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(DetailActivity.this);
        Cursor cursor = helper.getDetailsById(receivedId);
        cursor.moveToFirst();
        if (cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_COMMENT)) != null) {
            {
                String commentString = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_COMMENT));
                String[] commentArray = commentString.split("~");
                if(commentArray.length > mCommentList.size()) {
                    mCommentList.add(commentArray[commentArray.length - 1]);
                    ratingScoreTV.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_RATING))));
                    ratingVotesTV.setText((cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COL_VOTES))) + " VOTES");
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
