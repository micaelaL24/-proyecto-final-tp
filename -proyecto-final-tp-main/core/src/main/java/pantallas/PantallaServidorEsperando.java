package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import online.ServerThread;

import utiles.Render;



public class PantallaServidorEsperando implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;

    private ServerThread serverThread;

    @Override
    public void show() {

        batch = Render.batch;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);

        serverThread = new ServerThread();
        serverThread.start();

        System.out.println("[SERVER] Iniciado desde PantallaServidorEsperando()");
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch,
            "Servidor iniciado",
            50,
            Gdx.graphics.getHeight() - 50);


        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (serverThread != null) {
            serverThread.terminate();
        }
        if (font != null) font.dispose();
    }
}

