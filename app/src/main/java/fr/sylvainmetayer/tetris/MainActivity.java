package fr.sylvainmetayer.tetris;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private Button left, right;

    private ArrayList<Piece> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int nb_columns = getResources().getInteger(R.integer.maxColumns);
        int nb_lines = getResources().getInteger(R.integer.maxLines);

        layout = (GridView) findViewById(R.id.grid);
        scoreBox = (TextView) findViewById(R.id.score);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);


        initDatas();
        game = new Game(datas, nb_lines, nb_columns);

        MyAdapter adapter = new MyAdapter(MainActivity.this, R.layout.item, game.getGameboard());
        layout.setAdapter(adapter);

        initListeners();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!MainActivity.this.game.isPause()) {
                    game.performAction(MainActivity.this);
                    MainActivity.this.refresh();
                }

            }
        }, 0, 1000);

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

        final Button pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.togglePause();
                if (game.isPause()) {
                    pause.setText(getResources().getString(R.string.restart));
                } else {
                    pause.setText(getResources().getString(R.string.pause));
                }
            }
        });

        layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                game.rotate(MainActivity.this);
            }
        });
        layout.setOnTouchListener(new OnSwipeTouchListenerImpl(this));

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.moveRight(MainActivity.this);
                refresh();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.moveLeft(MainActivity.this);
                refresh();
            }
        });
    }

    public void initDatas() {
        datas = new ArrayList<>();
        Piece start_piece = new Piece_I(0, 2, this);
        datas.add(start_piece);
    }
}
