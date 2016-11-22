package com.daipresents.musclego.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.daipresents.musclego.MainActivity;
import com.daipresents.musclego.MuscleGoConst;
import com.daipresents.musclego.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON
 * {
 *     "trainningMenuName": セントラルスポーツ用メニュー
 *     "exercises": [
 *          "ベンチプレス",
 *          "ベンチプレスインクライン"
 *      ]
 * }
 */
public class TrainingMenuDetailActivity extends AppCompatActivity {

    private final String THIS_CLASS_NAME = TrainingMenuDetailActivity.class.getName();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_menu_detail);

        activity = this;

        loadTrainingMenu();

        // Save button.
        findViewById(R.id.saveExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(THIS_CLASS_NAME + ".onClick", "Start to save.");

                JSONObject trainingMenuJSON = createTrainingMenuJSON();

                if (trainingMenuJSON != null) {
                    saveTrainingMenuJSON(trainingMenuJSON);
                }

                Toast.makeText(activity, "Saved.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadTrainingMenu(){
        SharedPreferences sp = getSharedPreferences(MuscleGoConst.SP_NAME, MODE_PRIVATE);
        String trainingMenuString = sp.getString(MuscleGoConst.SP_KEY_TRAINING_MENU, "");

        if("".equals(trainingMenuString)) {
            Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "No training menu data so first time to save.");
           return;
        }

        Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "Loaded data is " + trainingMenuString);

        try {
            // Load existing data.
            JSONObject trainingMenuJSON = new JSONObject(trainingMenuString);

            EditText trainingMenuName = (EditText) findViewById(R.id.trainingMenuName);
            trainingMenuName.setText(trainingMenuJSON.getString(MuscleGoConst.SP_KEY_TRAINING_MENU_TRAINING_MENU_NAME));

            JSONArray exercisesArray = trainingMenuJSON.getJSONArray(MuscleGoConst.SP_KEY_TRAINING_MENU_EXERCISES);
            Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "exercises is " + exercisesArray.toString());

            for(int i = 0; i < exercisesArray.length(); i++) {
                if (getString(R.string.ex_bench_press_barbell).equals(exercisesArray.getString(i))){
                    CheckBox exBenchPressBarbell = (CheckBox) findViewById(R.id.ex_bench_press_barbell);
                    exBenchPressBarbell.setChecked(true);
                    Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "exBenchPressBarbell checked.");
                } else if (getString(R.string.ex_bench_press_incline).equals(exercisesArray.getString(i))){
                    CheckBox exBenchPressIncline = (CheckBox) findViewById(R.id.ex_bench_press_incline);
                    exBenchPressIncline.setChecked(true);
                    Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "exBenchPressIncline checked.");
                } else if (getString(R.string.ex_bench_press_decline).equals(exercisesArray.getString(i))){
                    CheckBox exBenchPressDecline = (CheckBox) findViewById(R.id.ex_bench_press_decline);
                    exBenchPressDecline.setChecked(true);
                    Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "exBenchPressDecline checked.");
                }
            }

        } catch (JSONException e){
            Log.v(THIS_CLASS_NAME + ".loadTrainingMenu", "Failed to create JSON object or getting data by using loaded data. " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    private JSONObject createTrainingMenuJSON(){

        JSONObject trainingMenuJSON = new JSONObject();
        JSONArray exercisesJSON = new JSONArray();

        // TrainingMenuName
        EditText trainingMenuName = (EditText) findViewById(R.id.trainingMenuName);

        // Exercises
        // TODO 一気に全部のCheckBoxを取得できないものか
        StringBuffer trainingMenu = new StringBuffer();

        CheckBox exBenchPressBarbell = (CheckBox) findViewById(R.id.ex_bench_press_barbell);
        if (exBenchPressBarbell.isChecked()) {
            exercisesJSON.put(getString(R.string.ex_bench_press_barbell));
        }

        CheckBox exBenchPressIncline = (CheckBox) findViewById(R.id.ex_bench_press_incline);
        if (exBenchPressIncline.isChecked()){
            exercisesJSON.put(getString(R.string.ex_bench_press_incline));
        }

        CheckBox exBenchPressDecline = (CheckBox) findViewById(R.id.ex_bench_press_decline);
        if (exBenchPressDecline.isChecked() ){
            exercisesJSON.put(getString(R.string.ex_bench_press_decline));
        }

        try {
            trainingMenuJSON.put(MuscleGoConst.SP_KEY_TRAINING_MENU_TRAINING_MENU_NAME, trainingMenuName.getText());
            trainingMenuJSON.put(MuscleGoConst.SP_KEY_TRAINING_MENU_EXERCISES, exercisesJSON);

            Log.v(THIS_CLASS_NAME + ".createTrainingMenuJSON", trainingMenuJSON.toString());

            return trainingMenuJSON;

        } catch (JSONException e){
            Log.v(THIS_CLASS_NAME + ".createTrainingMenuJSON", "JSONException occurred at new training data creation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    private void saveTrainingMenuJSON(JSONObject trainingMenuJSON){
        SharedPreferences sp = getSharedPreferences("com.daipresents.musclego", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MuscleGoConst.SP_KEY_TRAINING_MENU, trainingMenuJSON.toString());
        editor.commit();

        Log.v(THIS_CLASS_NAME + ".saveTrainingMenuJSON", "Saved data is " + sp.getString("trainingMenu", "No data."));
    }
}
