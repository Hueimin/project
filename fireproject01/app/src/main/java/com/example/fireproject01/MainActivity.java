package com.example.fireproject01;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPassword;
    Button buttonAdd;

    DatabaseReference databaseReference;

    public static  final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonAdd = (Button)findViewById(R.id.buttonAddPatient);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPatient();
            }
        });
    }

    private void addPatient() {
        final String name = editTextName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) { //如果name和password裡面不是空的
            //創建監聽器
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

                    if(dataSnapshot.child("patients").hasChild(name)) { //patients中有同樣的Username
                        //String ID = dataSnapshot.child("IDs/" + name).getValue(String.class); //同child(IDs).child(name).getValue(String.class)
                        String PASSWORD = dataSnapshot.child("patients/" + name + "/patientPassword").getValue(String.class);
                        if(password.equals(PASSWORD)) {
                            //傳ID給Baseball.java中，方便找尋手指資料
                            Bundle bundle = new Bundle(); //new一個Bundle物件，並將要傳遞的資料傳入
                            bundle.putString("NAME",name);

                            //顯示小標在下方提示成功登入
                            Toast.makeText(MainActivity.this, "Log in successfully", Toast.LENGTH_LONG).show();
                            //切換畫面(進入使用頁面)
                            Intent start = new Intent(MainActivity.this, Options.class);
                            start.putExtras(bundle);
                            startActivity(start);
                        }
                        else {
                            //密碼錯誤
                            Toast.makeText(MainActivity.this, "Password is incorrect！", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        //帳號錯誤
                        Toast.makeText(MainActivity.this, "Username is incorrect！", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {} //誤刪 //解除監聽器用
            });
        }
        else {
            Toast.makeText(this, "You should enter name and password！", Toast.LENGTH_LONG).show();
        }
    }

    //返回兩次離開
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "Press again to exit the program", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
