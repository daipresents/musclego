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

                SharedPreferences sp = getSharedPreferences("com.daipresents.musclego", MODE_PRIVATE);
                String trainingmenuList = sp.getString("trainingMenuList", "");

                JSONObject trainingMenuJson = new JSONObject();
                JSONArray exercisesJson = new JSONArray();

                if ("".equals(trainingmenuList)){
                    Log.v(THIS_CLASS_NAME, "Save training menu for the first time.");

                    // TrainingMenuName
                    EditText trainingMenuName = (EditText) findViewById(R.id.trainingMenuName);

                    // Exercises
                    // TODO 一気に全部のCheckBoxを取得できないものか
                    StringBuffer trainingMenu = new StringBuffer();

                    CheckBox exBenchPressBarbell = (CheckBox) findViewById(R.id.ex_bench_press_barbell);
                    if (exBenchPressBarbell.isChecked()) {
                        exercisesJson.put(R.string.ex_bench_press_barbell);
                    }

                    CheckBox exBenchPressIncline = (CheckBox) findViewById(R.id.ex_bench_press_incline);
                    if (exBenchPressIncline.isChecked()){
                        exercisesJson.put(R.string.ex_bench_press_incline);
                    }

                    CheckBox exBenchPressDecline = (CheckBox) findViewById(R.id.ex_bench_press_decline);
                    if (exBenchPressDecline.isChecked() ){
                        exercisesJson.put(R.string.ex_bench_press_decline);
                    }

                    try {
                        trainingMenuJson.put("trainingName", trainingMenuName.getText());
                        trainingMenuJson.put("exercises", exercisesJson);

                        Log.v(THIS_CLASS_NAME, trainingMenuJson.toString());

                    } catch (JSONException e){
                        Log.v(THIS_CLASS_NAME, "JSONException occurred at new training data creation: " + e.getMessage());
                    }


                } else {
                    Log.v(THIS_CLASS_NAME, "Update training menu.");

                }


//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("name", name);
//                editor.putInt("age", age);
//                editor.commit();
            }
        });
    }
}
