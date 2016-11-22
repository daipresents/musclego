package com.daipresents.musclego.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

        findViewById(R.id.saveExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "save", Toast.LENGTH_SHORT).show();

                JSONObject trainingMenuJSON = createJSON();

                if (trainingMenuJSON != null) {
                    saveJSON(trainingMenuJSON);
                }
            }
        });
    }

    private JSONObject createJSON(){

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
            exercisesJSON.put(getString(R.string.ex_bench_press_barbell));
        }

        try {
            trainingMenuJSON.put("trainingName", trainingMenuName.getText());
            trainingMenuJSON.put("exercises", exercisesJSON);

            Log.v(THIS_CLASS_NAME, trainingMenuJSON.toString());

            return trainingMenuJSON;

        } catch (JSONException e){
            Log.v(THIS_CLASS_NAME, "JSONException occurred at new training data creation: " + e.getMessage());
            return null;
        }

    }

    private void saveJSON (JSONObject trainingMenuJSON){
        SharedPreferences sp = getSharedPreferences("com.daipresents.musclego", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("trainingMenu", trainingMenuJSON.toString());
        editor.commit();

        Log.v(THIS_CLASS_NAME, "Saved data is " + sp.getString("trainingMenu", "No data."));
    }
}
