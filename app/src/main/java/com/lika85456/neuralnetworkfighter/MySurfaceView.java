package com.lika85456.neuralnetworkfighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.lika85456.neuralnetworkfighter.Game.Fighter;
import com.lika85456.neuralnetworkfighter.Game.Game;
import com.lika85456.neuralnetworkfighter.NeuralNetwork.NeuralNetwork;

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
        setWillNotDraw(false);
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
        super.draw(canvas);
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
