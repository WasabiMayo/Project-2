package com.example.wasabi.claireleeprojecttwo;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends AppCompatActivity {

    EditText mCommentEdit;
    Button mDoneButton, mCancelButton;
    RadioGroup mRadioGroup;
    RadioButton mGreatRadioButton, mOKRadioButton, mDislikeRadioButton;
    MySQLiteOpenHelper helper;
    int receivedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        TextView titleTV = (TextView)findViewById(R.id.comment_title_tv);
        mCommentEdit = (EditText)findViewById(R.id.comment_review_edittext);
        mDoneButton = (Button)findViewById(R.id.comment_done_button);
        mCancelButton = (Button)findViewById(R.id.comment_cancel_button);
        mRadioGroup = (RadioGroup)findViewById(R.id.comment_radiogroup);
        mGreatRadioButton = (RadioButton)findViewById(R.id.comment_great_button);
        mOKRadioButton = (RadioButton)findViewById(R.id.comment_normal_button);
        mDislikeRadioButton = (RadioButton)findViewById(R.id.comment_dislike_button);

        receivedId = getIntent().getIntExtra("ITEM_ID", -1);
        helper = MySQLiteOpenHelper.getInstance(CommentActivity.this);
        Cursor cursor = helper.getDetailsById(receivedId);
        cursor.moveToFirst();
        titleTV.setText("How was " + cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COL_NAME)) + "?");

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRating();

                String comment = mCommentEdit.getText().toString();
                helper.updateComment(comment, receivedId);
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void handleRating(){
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        switch (selectedId){
            case R.id.comment_great_button:
                Toast.makeText(CommentActivity.this, "Great", Toast.LENGTH_SHORT).show();
                helper.updateRating(10, receivedId);
                break;
            case R.id.comment_normal_button:
                Toast.makeText(CommentActivity.this, "OK", Toast.LENGTH_SHORT).show();
                helper.updateRating(7, receivedId);
                break;
            case R.id.comment_dislike_button:
                Toast.makeText(CommentActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
                helper.updateRating(5, receivedId);
                break;
        }
    }




}
