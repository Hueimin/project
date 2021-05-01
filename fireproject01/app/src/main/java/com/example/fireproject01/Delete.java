package com.example.fireproject01;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Delete {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference data = database.getReference();

    public static void delete(String name, String game, String time) {
        data.child("data").child(name).child(game).child(time).removeValue();
    }
}
