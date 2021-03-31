package com.example.fireproject01.chart.line;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fireproject01.R;
import com.example.fireproject01.gui.Result;
import com.google.firebase.database.DatabaseReference;

public class LineFingerSpeed extends Fragment {
    private String name;
    private int fingerNumber;

    private View view;

    DatabaseReference databaseAngle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_finger_speed, container, false);
    }

    @Override
    public void onAttach(Context context) {
        //傳NAME的路徑至此
        super.onAttach(context);
        Bundle take = getArguments();
        name = take.getString("NAME");
        fingerNumber = take.getInt("FINGER_NAME");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        TextView text = view.findViewById(R.id.result_finger_speed_text);
        text.setText(Result.FINGER_NAME[fingerNumber] + "'s Speed");
    }
}
