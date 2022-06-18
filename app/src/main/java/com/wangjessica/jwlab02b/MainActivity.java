package com.wangjessica.jwlab02b;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Map<String, String[]> categories;
    ArrayList<CheckBox> boxes;
    ArrayList<String> activeCategories;
    HashMap<String, Boolean> done;
    EditText input;
    TextView score;
    TextView prompt;
    ImageView image;
    String correctAnswer;
    String curCategory;
    String prefix;
    int suggest_ind;
    int correct;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate variables
        categories = new HashMap<String, String[]>();
        categories.put("grassland", getResources().getStringArray(R.array.grassland_arr));
        categories.put("arctic", getResources().getStringArray(R.array.tundra_arr));
        categories.put("aquatic", getResources().getStringArray(R.array.aquatic_arr));
        done = new HashMap<String, Boolean>();
        input = findViewById(R.id.input);
        score = findViewById(R.id.score);
        prompt = findViewById(R.id.prompt);
        image = findViewById(R.id.image);
        correct = total = suggest_ind = 0;
        prefix="";

        System.out.println(categories);

        //determine the categories the user would like to play with
        boxes = new ArrayList<CheckBox>();
        boxes.add(findViewById(R.id.grassland));
        boxes.add(findViewById(R.id.arctic));
        boxes.add(findViewById(R.id.aquatic));

        activeCategories = new ArrayList<String>();
        for(CheckBox box: boxes){
            if(box.isChecked())
                activeCategories.add(box.getText().toString());
        }

        System.out.println(activeCategories);
    }

    public void suggest(View view) {
        String[] options = categories.get(curCategory);
        assert options != null;
        suggest_ind %= options.length;
        input.setText(options[suggest_ind]);
        suggest_ind++;
    }

    public void submit(View view) {
        // Check whether the answer is correct
        String response = input.getText().toString().toLowerCase(Locale.ROOT);
        if(response.equals(correctAnswer)){
            // !! do something to indicate correctness !!
            score.setText(getString(R.string.score_display, ++correct, total));
            done.put(correctAnswer, true);
            prefix = "Correct! ";
            System.out.println("Correct");
        }
        else{
            // !! 2/1/2022 do something to indicate incorrectness !!
            done.put(correctAnswer, false);
            prefix=correctAnswer==null?"":"The answer was "+correctAnswer+". ";
            System.out.println("Incorrect");
        }

        // Are we done?
        if(done.size()==total+1){
            prompt.setText("Game complete!");
            score.setText("Final "+getString(R.string.score_display, correct, total));
            return;
        }

        // Choose the next question
        curCategory = activeCategories.get((int) (Math.random() * activeCategories.size()));
        String[] options = categories.get(curCategory);
        assert options != null;
        correctAnswer = "";
        System.out.println(done);
        while(correctAnswer.equals("") || done.containsKey(correctAnswer)){
            correctAnswer = options[(int)(Math.random()*options.length)];
        }

        // Update the display
        prompt.setText(prefix+"What "+curCategory+" animal is this?");
        String correct_modified = correctAnswer.replaceAll(" ", "_");
        image.setImageResource(getResources().getIdentifier(correct_modified, "drawable", this.getApplicationContext().getPackageName()));
        suggest_ind = 0;
    }

    public void categoryChecked(View view) {
        activeCategories = new ArrayList<String>();
        int new_total = 0;
        int new_correct = 0;
        for(CheckBox box: boxes){
            if(box.isChecked()){
                String category = box.getText().toString();
                activeCategories.add(category);
                new_total += categories.get(category).length;
                for(String s: categories.get(category)){
                    if(done.containsKey(s) && done.get(s)){
                        new_correct++;
                    }
                }
            }
        }
        total = new_total;
        correct = new_correct;
        score.setText(getString(R.string.score_display, correct, total));
        System.out.println(activeCategories);
    }

    public void set_theme1(View view) {
        Button b1 = findViewById(R.id.button_submit);
        Button b2 = findViewById(R.id.button_suggest);
        ConstraintLayout body = findViewById(R.id.body);
        ConstraintLayout body2 = findViewById(R.id.body2);
        body.setBackgroundResource(R.color.lightest_purple);
        body2.setBackgroundResource(R.color.lavender);
        b2.setBackgroundResource(R.color.grape);
        b1.setBackgroundResource(R.color.crayola);
    }

    public void set_theme2(View view) {
        Button b1 = findViewById(R.id.button_submit);
        Button b2 = findViewById(R.id.button_suggest);
        ConstraintLayout body = findViewById(R.id.body);
        ConstraintLayout body2 = findViewById(R.id.body2);
        body.setBackgroundResource(R.color.lightest_pink);
        body2.setBackgroundResource(R.color.bisque);
        b2.setBackgroundResource(R.color.macaroni);
        b1.setBackgroundResource(R.color.midred);
    }
}