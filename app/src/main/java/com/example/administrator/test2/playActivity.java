package com.example.administrator.test2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class playActivity extends AppCompatActivity {
    Vibrator vibrator;
    SoundPool sound;
    int isound;
    TextView score, time;
    Button button[] = new Button[9];
    Button Gbutton[] = new Button[2];
    static int iscore = 0;
    int itime = 60;
    boolean bool = true;
    boolean bool2 =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        sound = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
        isound = sound.load (this, R.raw.sound1, 1);
        itime = 61;
        iscore = 0;
        bool = true;

        score = (TextView) findViewById(R.id.score);
        score.setText(iscore + "점");
        time = (TextView) findViewById(R.id.time);
        time.setText(itime + "초");
        Gbutton[0] = (Button) findViewById(R.id.golddocin1);
        Gbutton[1] = (Button) findViewById(R.id.golddocin2);
        button[0] = (Button) findViewById(R.id.docin1);
        button[1] = (Button) findViewById(R.id.docin2);
        button[2] = (Button) findViewById(R.id.docin3);
        button[3] = (Button) findViewById(R.id.docin4);
        button[4] = (Button) findViewById(R.id.docin5);
        button[5] = (Button) findViewById(R.id.docin6);
        button[6] = (Button) findViewById(R.id.docin7);
        button[7] = (Button) findViewById(R.id.docin8);
        button[8] = (Button) findViewById(R.id.docin9);
        for (int i = 0; i < 9; i++) {
            setting(i);
        }
        Gbutton[0].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iscore+=10;
                sound.play(isound,1,1,0,0,1.5f);
                vibrator.vibrate(100);
                score.setText(iscore + "점");
                Gbutton[0].setEnabled(false);
                return false;
            }
        });
        Gbutton[1].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iscore+=10;
                sound.play(isound,1,1,0,0,1.5f);
                vibrator.vibrate(100);
                score.setText(iscore + "점");
                Gbutton[1].setEnabled(false);
                return false;
            }
        });


        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(timer);

        thread1.start();
        thread2.start();
    }

    // 두더지 소환술
    final Handler handler = new Handler() {
        int Rn;
        int GRn;
        @Override
        public void handleMessage(Message msg) {
            Rn = (int) (Math.random() * 9);
            GRn = (int) (Math.random() * 30);
            if(GRn == 0){
                Gbutton[(int)(Math.random()*2)].setEnabled(true);
            }
            button[Rn].setEnabled(true);
            if(!bool2) {
                Rn = (int) (Math.random() * 9);
                button[Rn].setEnabled(false);
            }
        }
    };
    final Runnable task = new Runnable() {
        public void run() {
            while (bool) {
                try {
                    int Rn = (int) ((Math.random() * 400) + 200 - (iscore * 5));
                    if (Rn < 100) Rn = 100;
                    if(itime <= 10) bool2 = true;
                    Thread.sleep(Rn);
                } catch (InterruptedException e) {
                }
                    handler.sendEmptyMessage(1);
            }
        }
    };


    // 시간
    final Handler timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            itime--;
            time.setText(itime + "초");
        }
    };
    Runnable timer = new Runnable() {
        public void run() {
            while (bool) {
                try {
                    timehandler.sendEmptyMessage(0);
                    if (itime == 1) {
                        bool = false;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
            Intent intent = new Intent(playActivity.this,endActivity.class);
            intent.putExtra("점수",iscore);
            startActivity(intent);
            finish();
        }
    };

    //두더지 셋팅
    void setting(final int i) {
        button[i].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iscore++;
                sound.play(isound,1,1,0,0,1.5f);
                if(!bool2)
                    vibrator.vibrate(30);
                else vibrator.vibrate(100);
                score.setText(iscore + "점");
                button[i].setEnabled(false);
                return false;
            }
        });
    }
}