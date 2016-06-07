package ru.slippy.n_back;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends Activity {

    private LinkedList<Integer> list;
    private int level;
    private int count;
    private ArrayList<Integer> fields;
    private Handler handler;
    private Drawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        list = new LinkedList<Integer>();
        fields = new ArrayList<Integer>();
        fields.add(R.id.field1);
        fields.add(R.id.field2);
        fields.add(R.id.field3);
        fields.add(R.id.field4);
        fields.add(R.id.field5);
        fields.add(R.id.field6);
        fields.add(R.id.field7);
        fields.add(R.id.field8);
        fields.add(R.id.field9);
        count = 0;
    }

    public void selectLevel(View v) {
        level = Integer.parseInt(String.valueOf(((Button) v).getText()));

        String message = "Choose level " + level;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        list.clear();
        nextElement();
        setContentView(R.layout.game);
        setEnableFields(false);
    }

    public void selectField(View v) {
        int expected = list.removeFirst();
        int result = fields.indexOf(v.getId());
        if (expected == result) {
            showCorrect();
        } else {
            showIncorrect();
        }
        setEnableFields(false);
    }

    public void showElement(final int i) {
        View view = findViewById(fields.get(i));
        background = view.getBackground();
        view.setBackgroundColor(getResources().getColor(android.R.color.black));
        handler.postDelayed(new Runnable() {
            public void run() {
                hideElement(i);
            }
        }, 700);
    }

    public void hideElement(int i) {
        findViewById(fields.get(i)).setBackground(background);
        if (list.size() == level) {
            setEnableFields(true);
        }
    }

    public void nextElement() {
        Random r = new Random();
        final int i = r.nextInt(8) + 1;
        list.add(i);
        handler.postDelayed(new Runnable() {
            public void run() {
                showElement(i);
                if (list.size() < level) {
                    nextElement();
                }
            }
        }, 1000);
    }

    public void hideCorrect() {
        TextView result = (TextView) findViewById(R.id.text_result);
        result.setBackgroundColor(getResources().getColor(android.R.color.white));
        result.setText("");
    }

    public void setEnableFields(boolean enabled) {
        for (Integer id: fields) {
            findViewById(id).setClickable(enabled);
        }
    }

    private void showCorrect() {
        TextView result = (TextView) findViewById(R.id.text_result);
        result.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        result.setText(R.string.correct);
        handler.postDelayed(new Runnable() {
            public void run() {
                hideCorrect();
            }
        }, 500);
        count++;
        nextElement();
    }

    private void showIncorrect() {
        setEnableFields(false);
        TextView result = (TextView) findViewById(R.id.text_result);
        result.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        result.setText(R.string.incorrect);
        Toast.makeText(this, "Count " + count, Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            public void run() {
                setContentView(R.layout.activity_main);
                list = new LinkedList<Integer>();
                count = 0;
            }
        }, 3000);
    }
}
