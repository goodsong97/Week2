package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Message;

import org.w3c.dom.Text;

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
    private int current_score;
    private int velocity = DEFAULT_VELOCITY;
    private TextView score_board;
    private String score_string;
    private ImageView view1;
    private ImageView character;
    private boolean IS_LEFT = true;
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

        Button jump_but = (Button)findViewById(R.id.jump);
        Button start_but = (Button)findViewById(R.id.start);
        Button restart_but = (Button)findViewById(R.id.restart);




        jump_but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //이 부분 해야합니다!! -Jump button touch event : Character를 위로갔다가 내려오게 하기. 이 시간 동안 img 를 jump.png 로 설정
            }
        });

        View.OnClickListener start_listener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                alive = true;
                GameStart();
            }
        };

        start_but.setOnClickListener(start_listener);
        restart_but.setOnClickListener(start_listener);
        score_board = (TextView) findViewById(R.id.scoreboard);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void GameStart(){
        int cnt=0;
        current_score=0;
        while(alive){
            Move();
            /*
            1. if(character의 img == R.Drawable.right) img.set(R.Drawable.left);
            2. 장애물 이미지를 game_view.xml 에 등록시키고, while의 한 주기동안 왼쪽으로 이동하게 끔한다.
            즉, 장애물. x좌표 = x좌표 - velocity; 가 되게끔!!
             */

            score_string = ("HI "+ high_score) + current_score;
            if(cnt%10==0) velocity += 3; // velocity update
            current_score += velocity/5; //score update
            score_board.setText(score_string); //score_board update
            if(!alive) high_score = current_score; //high_score update
            cnt++;
        }
        score_board.setText(score_string);
    }

    public void Move(){
        if (IS_LEFT){
            character.setImageResource(R.drawable.rright);
        }
        else{
            character.setImageResource(R.drawable.left);
            IS_LEFT = false;
        }
    }

    // Character와 장애물이 만나는 event.
    // alive =false;

}
