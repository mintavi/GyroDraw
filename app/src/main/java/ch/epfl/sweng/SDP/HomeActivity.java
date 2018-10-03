package ch.epfl.sweng.SDP;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private final static int TOP_BUTTONS_FREQUENCY = 10;
    private final static int DRAW_BUTTON_FREQUENCY = 20;
    private final static int LEAGUE_IMAGE_FREQUENCY = 30;

    private final static double MAIN_AMPLITUDE = 0.1;
    private final static double DRAW_BUTTON_AMPLITUDE = 0.2;

    private final static int LEFT_PADDING = 140;
    private final static int TOP_PADDING = -5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Typeface typeMuro = Typeface.createFromAsset(getAssets(),"fonts/Muro.otf");
        Typeface typeOptimus = Typeface.createFromAsset(getAssets(),"fonts/Optimus.otf");

        final ImageView drawButton = findViewById(R.id.drawButton);
        final Button trophiesButton = findViewById(R.id.trophiesButton);
        final Button starsButton = findViewById(R.id.starsButton);
        final ImageView leagueImage = findViewById(R.id.leagueImage);
        TextView leagueText = findViewById(R.id.leagueText);

        trophiesButton.setTypeface(typeMuro);
        starsButton.setTypeface(typeMuro);
        leagueText.setTypeface(typeOptimus);

        trophiesButton.setPadding(LEFT_PADDING, TOP_PADDING, 0, 0);
        starsButton.setPadding(LEFT_PADDING, TOP_PADDING, 0, 0);

        setDrawButtonListener(drawButton);
        setListener(trophiesButton, MAIN_AMPLITUDE, TOP_BUTTONS_FREQUENCY);
        setListener(starsButton, MAIN_AMPLITUDE, TOP_BUTTONS_FREQUENCY);
        setListener(leagueImage, MAIN_AMPLITUDE, LEAGUE_IMAGE_FREQUENCY);
    }

    private void setListener(final View view, final double amplitude, final int frequency) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressButton(view);
                        break;
                    case MotionEvent.ACTION_UP:
                        bounceButton(view, amplitude, frequency);
                    default:
                }
                return true;
            }
        });
    }

    private void setDrawButtonListener(final ImageView drawButton) {
        drawButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawButton.setImageResource(R.drawable.draw_button_pressed);
                        pressButton(drawButton);
                        break;
                    case MotionEvent.ACTION_UP:
                        drawButton.setImageResource(R.drawable.draw_button);
                        bounceButton(drawButton, DRAW_BUTTON_AMPLITUDE, DRAW_BUTTON_FREQUENCY);
                    default:
                }
                return true;
            }
        });
    }

    private void bounceButton(View view, double amplitude, int frequency) {
        final Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(amplitude, frequency);
        bounce.setInterpolator(interpolator);
        view.startAnimation(bounce);
    }

    private void pressButton(View view) {
        final Animation press = AnimationUtils.loadAnimation(this, R.anim.press);
        press.setFillAfter(true);
        view.startAnimation(press);
    }
}