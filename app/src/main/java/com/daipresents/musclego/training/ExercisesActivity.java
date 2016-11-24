package com.daipresents.musclego.training;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daipresents.musclego.MuscleGoConst;
import com.daipresents.musclego.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExercisesActivity extends AppCompatActivity {

    private final String THIS_CLASS_NAME = ExercisesActivity.class.getName();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        activity = this;

        loadTrainingMenu();
    }

    private void loadTrainingMenu(){
        SharedPreferences sp = getSharedPreferences(MuscleGoConst.SP_NAME, MODE_PRIVATE);
        String trainingMenuString = sp.getString(MuscleGoConst.SP_KEY_TRAINING_MENU, "");

        if("".equals(trainingMenuString)) {
            Toast.makeText(activity, "No training menu.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Load existing data.
            JSONObject trainingMenuJSON = new JSONObject(trainingMenuString);
            JSONArray exercisesArray = trainingMenuJSON.getJSONArray(MuscleGoConst.SP_KEY_TRAINING_MENU_EXERCISES);
            Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "exercises is " + exercisesArray.toString());

            LinearLayout layout = (LinearLayout) findViewById(R.id.exercisesLayout);

            for(int i = 0; i < exercisesArray.length(); i++) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View exerciseView = inflater.inflate(R.layout.activity_exercises_exercise, null);
                layout.addView(exerciseView);
                TextView exerciseNameView = (TextView) exerciseView.findViewById(R.id.exerciseName);
                exerciseNameView.setText(exercisesArray.getString(i));
            }

        }catch (JSONException e) {
            Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "Failed to load JSON object. " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}
