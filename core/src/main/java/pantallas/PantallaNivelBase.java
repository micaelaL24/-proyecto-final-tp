package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import utiles.Recursos;
import utiles.Render;
import utiles.Timer;

public abstract class PantallaNivelBase implements Screen {

    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;

    // HUD
    protected BitmapFont font;
    protected GlyphLayout layout;
    protected Texture estrellaTexture;

    // Timer
    protected Timer timer;

    public PantallaNivelBase() {
        batch = Render.batch;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();
        estrellaTexture = new Texture(Recursos.ESTRELLA);

        // Iniciar el timer (60s)
        timer = new Timer(60f);
    }

    @Override
    public void render(float delta) {
        // Toggle pausa
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            timer.alternarPausa();
        }

        // Actualizar timer
        timer.actualizar(delta);

        // Si el timer llegó a 0, pasar a PantallaDerrota
        if (timer.estaTerminado()) {
            Render.app.setScreen(new PantallaDerrota());
            return; // salimos para no seguir dibujando el nivel
        }

        // Si no está pausado, actualizar la lógica del nivel
        if (!timer.estaPausado()) {
            updateNivel(delta);
        }

        // Si está pausado, dibujar cartel
        if (timer.estaPausado()) {
            drawPausa();
        }

        // Dibujar HUD (siempre visible)
        drawHud();
    }

    // Método que cada nivel debe implementar
    protected abstract void updateNivel(float delta);

    // HUD: icono estrella, contador y timer
    protected void drawHud() {
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();

        // Coordenadas HUD en base a la cámara del nivel
        float hudX = getCamera().position.x - getCamera().viewportWidth / 2 + 20;
        float hudY = getCamera().position.y + getCamera().viewportHeight / 2 - 20;

        float iconSize = 64;
        font.getData().setScale(2f);

        // Icono estrella
        batch.draw(estrellaTexture, hudX, hudY - iconSize, iconSize, iconSize);

        // Texto estrellas
        String estrellasTexto = "x " + getEstrellasRecolectadas();
        layout.setText(font, estrellasTexto);
        font.draw(batch, estrellasTexto, hudX + iconSize + 15,
            hudY - iconSize / 2 + layout.height / 2);

        // Timer (al lado derecho)
        String timerTexto = "Tiempo: " + (int) timer.getTiempoRestante();
        layout.setText(font, timerTexto);
        font.draw(batch, timerTexto,
            getCamera().position.x + getCamera().viewportWidth / 2 - layout.width - 20,
            hudY - iconSize / 2 + layout.height / 2);

        batch.end();
    }

    // Cartel de pausa
    protected void drawPausa() {
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
        float rectWidth = getCamera().viewportWidth / 2;
        float rectHeight = getCamera().viewportHeight / 4;
        shapeRenderer.rect(getCamera().position.x - rectWidth / 2,
            getCamera().position.y - rectHeight / 2,
            rectWidth, rectHeight);
        shapeRenderer.end();

        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();
        font.getData().setScale(4f);
        String pausaTexto = "PAUSA";
        layout.setText(font, pausaTexto);
        font.draw(batch, pausaTexto,
            getCamera().position.x - layout.width / 2,
            getCamera().position.y + layout.height / 2);
        batch.end();
    }

    // Cada nivel debe devolver su cámara
    protected abstract com.badlogic.gdx.graphics.OrthographicCamera getCamera();

    // Cada nivel debe devolver las estrellas recolectadas
    protected abstract int getEstrellasRecolectadas();

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        estrellaTexture.dispose();
    }
}
