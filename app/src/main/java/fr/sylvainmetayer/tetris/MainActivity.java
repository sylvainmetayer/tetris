package fr.sylvainmetayer.tetris;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fr.sylvainmetayer.tetris.metier.Game;
import fr.sylvainmetayer.tetris.metier.Piece;
import fr.sylvainmetayer.tetris.metier.pieces.Piece_I;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private GridView layout;
    private TextView scoreBox;
    private Button left, right, rotation;
    private Timer timer;
    private MediaPlayer mediaPlayer;
    private Chronometer chronometer;
    /**
     * @see "https://stackoverflow.com/a/7064928"
     */
    private long timeWhenStopped;
    private ArrayList<Piece> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int nb_columns = getResources().getInteger(R.integer.maxColumns);
        int nb_lines = getResources().getInteger(R.integer.maxLines);

        this.mediaPlayer = null;

        layout = (GridView) findViewById(R.id.grid);
        scoreBox = (TextView) findViewById(R.id.score);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        rotation = (Button) findViewById(R.id.rotation);

        initDatas();
        game = new Game(datas, nb_lines, nb_columns);

        MyAdapter adapter = new MyAdapter(MainActivity.this, R.layout.item, game.getGameboard());
        layout.setAdapter(adapter);

        initListeners();

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setFormat(getResources().getString(R.string.chrono) + " - %s");
        timeWhenStopped = 0;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!MainActivity.this.game.isPause()) {
                    game.performAction(MainActivity.this);
                    MainActivity.this.refresh();
                }

            }
        }, 0, getResources().getInteger(R.integer.timer));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart:
                if (mediaPlayer != null)
                    this.mediaPlayer.stop();
                this.recreate();
                return true;
            case R.id.menu_help:
                showHelp();
                return true;
            case R.id.menu_music_toggle:
                toggleMusic();
                return true;
            case R.id.menu_play_pause:
                game.togglePause();
                if (game.isPause()) {
                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleMusic() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = MediaPlayer.create(this, R.raw.main);
            mediaPlayer.setLooping(true);
        }

        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    private void showHelp() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Do you want to logout?");
                alert.setMessage("Message");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });
    }

    public void refresh() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //adapter.notifyDataSetChanged(); // Isn't working :(
                layout.setAdapter(new MyAdapter(MainActivity.this, R.layout.item, game.getGameboard()));
                scoreBox.setText(game.getScore());
            }
        });
    }

    public void initListeners() {

        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game.rotate();
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
                game.moveRight();
                refresh();
            }

            public void onSwipeLeft() {
                game.moveLeft();
                refresh();
            }

        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.moveRight();
                refresh();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.moveLeft();
                refresh();
            }
        });

        rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.rotate();
                refresh();
            }
        });
    }

    public void initDatas() {
        datas = new ArrayList<>();
        Piece start_piece = new Piece_I(0, 2, this);
        datas.add(start_piece);
    }

    public void endGame() {

        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                timer.cancel();
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                chronometer.stop();
                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.end_game), Toast.LENGTH_LONG).show();
            }
        });
    }
}
