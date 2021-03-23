package com.example.fireproject01;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {

    private Button button;
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Result
        button = (Button)findViewById(R.id.button);
        Button nextPageBtn = (Button)findViewById(R.id.button);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Options.this,Result.class);
                intent.putExtras(getIntent().getExtras()); //bundle //傳NAME
                startActivity(intent);
            }
        });


        //Baseball
        button1 = (ImageButton) findViewById(R.id.imageButton);
        ImageButton nextPageBtn1 = (ImageButton)findViewById(R.id.imageButton);
        nextPageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Options.this,Baseball.class);
                intent.putExtras(getIntent().getExtras()); //bundle //傳NAME
                startActivity(intent);
            }
        });

        //Pingpong
        button2 = (ImageButton)findViewById(R.id.imageButton2);
        ImageButton nextPageBtn2 = (ImageButton)findViewById(R.id.imageButton2);
        nextPageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Options.this,Pingpong.class);
                intent.putExtras(getIntent().getExtras()); //bundle //傳NAME
                startActivity(intent);
            }
        });


        //Poker
        button3 = (ImageButton)findViewById(R.id.imageButton3);
        ImageButton nextPageBtn3 = (ImageButton)findViewById(R.id.imageButton3);
        nextPageBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Options.this,Poker.class);
                intent.putExtras(getIntent().getExtras()); //bundle //傳NAME
                startActivity(intent);
            }
        });

        //Flappybee
        button4 = (ImageButton)findViewById(R.id.imageButton4);
        ImageButton nextPageBtn4 = (ImageButton)findViewById(R.id.imageButton4);
        nextPageBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Options.this,Flappybee.class);
                intent.putExtras(getIntent().getExtras()); //bundle //傳NAME
                startActivity(intent);
            }
        });
    }

    //返回兩次離開
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "Press again to log out", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
//                finish();
//                System.exit(0);
                Intent intent=new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(Options.this, MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
