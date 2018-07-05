package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Message;

import org.w3c.dom.Text;

import static com.example.myapplication.R.drawable.jjump;

/*전체적인 구현방법
 * -Jump button touch event : Character를 위로갔다가 내려오게 하기. 이 시간 동안 img 를 jump.png 로 설정
 * -Character와 장애물의 image가 만나는 event : alive = false 으로 설정
 * - Restart button touch event: alive = true, GameStart()
 * public void GameStart(){
 * int cnt =0;
 * while(alive){
 *   -Character : (대략)0.5s 마다 Left 이미지와 Right이미지를 번갈아 나오게 하기
 *   -장애물 image (아마 surface view인듯)이 왼쪽으로 이동하게 한다. (아마 이동폭을 velocity로 결정)
 *   -velocity는 계속 증가하게끔.(cnt++;설정한 후 대략적으로 cnt가 어떤 자연수의 배수일 때마다 5정도씩 증가하게끔 하면 되지 않을까 싶다)
 *   -Current score = Current score + velocity/5 (임의로 정함);
 *   -game_view의 TextView id 따와서 .settext = "HI"+ high_score + current_score;
 *   if(alive == false) high_score = current_score;
 * }
 *}
 */

public class Game extends AppCompatActivity {

    static private int DEFAULT_VELOCITY=50;
    private boolean alive = true; // false is die, true is alive.
    private int high_score = 0;
    private int current_score=0;
    private int velocity = DEFAULT_VELOCITY;
    private TextView score_board;
    private TextView begin;
    private String score_string;
    private ImageView view1;
    private ImageView character;
    private ImageView obstacle;
    private View lin;

    private boolean IS_LEFT = true;
    private int cnt = 0;
    private static Timer timer1;
    private static Timer timer2;
    private Button jump_but;
    private static TimerTask tt1;
    private static TimerTask tt2;
    public Game() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_view);
        view1 = (ImageView) findViewById(R.id.imageAnimation);
        view1.setBackgroundResource(R.drawable.ground);
        character = (ImageView) findViewById(R.id.dino);
        character.setBackgroundResource(R.drawable.left);
        obstacle = (ImageView) findViewById(R.id.obstacle);
        lin = findViewById(R.id.lin);
        jump_but = (Button)findViewById(R.id.jump);
        final Animation animTransUp = AnimationUtils.loadAnimation(
                this, R.anim.anim_translate_up);
        timer1 = new Timer();
        timer2 = new Timer();

        jump_but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tt1.cancel();
                character.setImageResource(jjump);
                character.startAnimation(animTransUp);
                final Handler handler = new Handler(){
                    public void handleMessage(Message msg){
                        Move();
                    }
                };
                tt1 = new TimerTask(){
                    @Override
                    public void run(){
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                    @Override
                    public boolean cancel(){
                        return super.cancel();
                    }
                };
                timer1 = new Timer();
                timer1.schedule(tt1,0,100);
            }
        });



        /*Button jump_but = (Button)findViewById(R.id.jump);
        Button start_but = (Button)findViewById(R.id.start);
        Button restart_but = (Button)findViewById(R.id.restart);*/



        /*View.OnClickListener start_listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alive = true;
                final Handler handler = new Handler(){
                    public void handleMessage(Message msg){
                        StartGame(cnt);
                    }
                };

                TimerTask tt = new TimerTask(){
                    @Override
                    public void run(){
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                        cnt++;
                    }
                    @Override
                    public boolean cancel() {
                        return super.cancel();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(tt,0,100);
            }
        };*/
        /*start_but.setOnClickListener(start_listener);
        restart_but.setOnClickListener(start_listener);*/
        score_board = (TextView) findViewById(R.id.scoreboard);
        begin = (TextView) findViewById(R.id.begin);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        begin.setText("");
        alive = true;
        MovingBackground();
        final Handler handler = new Handler(){
            public void handleMessage(Message msg){
                Move();
            }
        };
        final Handler handler2 = new Handler(){
            public void handleMessage(Message msg){
                StartGame(cnt);
            }
        };
        tt1 = new TimerTask(){
            @Override
            public void run(){
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
              }
            @Override
            public boolean cancel(){
                return super.cancel();
            }
        };
        tt2 = new TimerTask(){
            @Override
            public void run(){
                Message msg = handler2.obtainMessage();
                handler2.sendMessage(msg);
                cnt++;
            }
            @Override
            public boolean cancel(){
                return super.cancel();
            }
        };
        timer1.schedule(tt1,0,100);
        timer2.schedule(tt2,0,100);
        return super.onTouchEvent(event);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void StartGame(int cnt){

        score_string = ("HI "+ high_score) + "   " + current_score;
        if(cnt%10==0){ velocity += 3;} // velocity update
        current_score += velocity/5; //score update
        score_board.setText(score_string); //score_board update
        if(!alive) high_score = current_score; //high_score update
        score_board.setText(score_string);
            /*
            1. if(character의 img == R.Drawable.right) img.set(R.Drawable.left);
            2. 장애물 이미지를 game_view.xml 에 등록시키고, while의 한 주기동안 왼쪽으로 이동하게 끔한다.
            즉, 장애물. x좌표 = x좌표 - velocity; 가 되게끔!!
             */

}
    public void Move(){
        if (IS_LEFT){
            character.setImageResource(R.drawable.rright);
            IS_LEFT = false;
        }
        else{
            character.setImageResource(R.drawable.left);
            IS_LEFT = true;
        }

    }

    public void MovingBackground(){
        final Animation animTransLeft = AnimationUtils.loadAnimation(
                this, R.anim.anim_translate_left);
        obstacle.setImageResource(R.drawable.obstacle1);
        lin.startAnimation(animTransLeft);
    }
}
