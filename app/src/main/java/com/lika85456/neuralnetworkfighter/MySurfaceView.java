package com.lika85456.neuralnetworkfighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import com.lika85456.neuralnetworkfighter.Game.Bullet;
import com.lika85456.neuralnetworkfighter.Game.Fighter;
import com.lika85456.neuralnetworkfighter.Game.Game;

import java.util.ArrayList;
import java.util.Collections;

public class MySurfaceView extends View {
    public static final int numberOfPlayers = 2;
    public Game game;
    public ArrayList<NeuralFighter> neuralNetworks;
    public Bitmap bitPlayer;
    public Bitmap bitBullet;
    public int generation = 0;
    private Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    public NeuralFighter theBest = null;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paintLine.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLine.setColor(0xFF2255FF);
        neuralNetworks = new ArrayList<NeuralFighter>();

        bitPlayer = drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.shooter, null));
        bitBullet = drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.bullet, null));

        new Thread(new Runnable() {
            public void run() {
                Trainer trainer = new Trainer();
                theBest = trainer.train(500);
            }
        }).start();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    private Bitmap drawableToBitmap(Drawable d) {
        return ((BitmapDrawable) d).getBitmap();
    }

    public void onDraw(Canvas canvas) {
        //Log.d("RENDER","rendering");
        invalidate();
        if (theBest == null) return;
        if (game == null) {
            game = new Game(10);
            for (int i = 0; i < 10; i++) {
                NeuralFighter temp = theBest.mutate(0.1f);
                temp.fighter = game.fighters[i];
                neuralNetworks.add(temp);
            }

        } else {
            if (!game.isEnd()) {
                for (NeuralFighter neuralFighter : neuralNetworks) {
                    neuralFighter.play(game);
                }
                game.nextTurn();
            } else {
                theBest = Collections.max(neuralNetworks);
                game = null;
            }

        }

        render(canvas);
    }

    public void render(Canvas canvas) {

        //draw players
        for (int i = 0; i < game.fighters.length; i++) {
            Fighter fighter = game.fighters[i];
            if (fighter.dead) continue;

            int x1 = (int) ((fighter.x - 15f) / 500f * canvas.getWidth());
            int y1 = (int) ((fighter.y - 15f) / 500f * canvas.getWidth());
            int x2 = (int) ((fighter.x + 15f) / 500f * canvas.getWidth());
            int y2 = (int) ((fighter.y + 15f) / 500f * canvas.getWidth());
            Rect rect = new Rect(x1, y1, x2, y2);
            canvas.drawBitmap(bitPlayer, null, rect, null);

            //draw info text
            canvas.drawText("Fighter " + i, x1, y2 + 15, this.paintLine);
            canvas.drawText("HP " + fighter.hp, x1, y2 + 30, this.paintLine);
            //draw FoV
            int lineX1 = (int) (fighter.x + Math.sin((fighter.angle - fighter.FoV / 2f / 360f) * 2f * Math.PI) * 200f);
            int lineY1 = (int) (fighter.y + Math.cos((fighter.angle - fighter.FoV / 2f / 360f) * 2f * Math.PI) * 200f);
            int lineX2 = (int) (fighter.x + Math.sin((fighter.angle + fighter.FoV / 2f / 360f) * 2f * Math.PI) * 200f);
            int lineY2 = (int) (fighter.y + Math.cos((fighter.angle + fighter.FoV / 2f / 360f) * 2f * Math.PI) * 200f);
            int lineX3 = (int) (fighter.x + Math.sin(fighter.angle * 2f * Math.PI) * 400f);
            int lineY3 = (int) (fighter.y + Math.cos(fighter.angle * 2f * Math.PI) * 400f);

            canvas.drawLine((int) fighter.x, (int) fighter.y, lineX1, lineY1, paintLine);
            canvas.drawLine((int) fighter.x, (int) fighter.y, lineX2, lineY2, paintLine);
            canvas.drawLine((int) fighter.x, (int) fighter.y, lineX3, lineY3, paintLine);

        }

        //draw bullets
        for (int i = 0; i < game.bullets.size(); i++) {
            Bullet bullet = game.bullets.get(i);
            int x1 = (int) ((bullet.x - 5f) / 500f * canvas.getWidth());
            int y1 = (int) ((bullet.y - 5f) / 500f * canvas.getWidth());
            int x2 = (int) ((bullet.x + 5f) / 500f * canvas.getWidth());
            int y2 = (int) ((bullet.y + 5f) / 500f * canvas.getWidth());
            Rect rect = new Rect(x1, y1, x2, y2);
            canvas.drawBitmap(bitBullet, null, rect, null);
        }

        //draw info stuff
        canvas.drawText("Generation " + generation, 0, 15, this.paintLine);
        canvas.drawText("Turn " + game.turns, 0, 25, this.paintLine);

    }


}

/*
public class MySurfaceView extends SurfaceView {
    public Game game;
    public NeuralNetwork n1;
    public NeuralNetwork n2;
    public Bitmap player;
    public Bitmap bullet;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        this.game = new Game(2);
        n1 = new NeuralNetwork(7, 5, 100, 100);
        n2 = new NeuralNetwork(7, 5, 10, 10);


        player = drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.shooter, null));
        bullet = drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.bullet, null));
    }

    private Bitmap drawableToBitmap(Drawable d) {
        return ((BitmapDrawable) d).getBitmap();
    }

    public void onDraw(Canvas canvas) {
        Log.d("RENDER","rendering");
        if (this.game == null) return;
        render(canvas);
        if (!game.isEnd()) {
            float[] output1 = n1.getOutput(generateInput(game, 0));
            float[] output2 = n2.getOutput(generateInput(game, 1));
            proceedOutput(output1, game, 0);
            proceedOutput(output2, game, 1);
            game.nextTurn();
        }


    }

    public void render(Canvas canvas) {

        //draw players
        for (int i = 0; i < game.fighters.length; i++) {
            Fighter fighter = game.fighters[i];
            int x1 = (int) (fighter.x + Math.sin(fighter.angle * 2 * Math.PI) * 25f / 500f * canvas.getWidth());
            int x2 = (int) (fighter.x - Math.sin(fighter.angle * 2 * Math.PI) * 25f / 500f * canvas.getWidth());
            int y1 = (int) (fighter.y + Math.cos(fighter.angle * 2 * Math.PI) * 50f / 500f * canvas.getHeight());
            int y2 = (int) (fighter.y - Math.cos(fighter.angle * 2 * Math.PI) * 50f / 500f * canvas.getHeight());
            Rect rect = new Rect(x1, y1, x2, y2);
            canvas.drawBitmap(player, null, rect, null);
        }

    }

    public void proceedOutput(float[] output, Game game, int playerId) {
        if (output[0] > 0) game.shoot(playerId);
        game.rotate(playerId, Math.max(output[1], output[2]) == output[1], (output[3] + 1f) / 2f);
        game.setVelocity(playerId, output[4] > 0 ? 1f : 0f);
    }

    public float[] generateInput(Game game, int playerId) {
        Fighter f = game.fighters[playerId];
        return new float[]{f.FoV / 45f, f.canSeeBullet(game.bullets) ? 1f : -1f, f.canSeeEnemy(game.fighters) ? 1f : -1f, f.canShoot() ? 1f : -1f, f.hp * 2 - 1f, f.x / Game.size, f.y / Game.size};
    }
}
*/