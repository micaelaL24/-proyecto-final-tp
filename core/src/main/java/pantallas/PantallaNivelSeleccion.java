package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import elementos.Imagen;
import utiles.GestorMusica;
import utiles.Recursos;
import utiles.Render;

public class PantallaNivelSeleccion implements Screen {

    Imagen fondo, btnNivel1, btnNivel2, btnNivel3, btnNivel4, btnNivel5, btnNivel6, btnNivel7, btnNivel8, btnNivel9, btnNivel10;
    SpriteBatch b;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDOSELECNIVEL);
        fondo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        b = Render.batch;

        btnNivel1 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel1.setSize(80,86);
        btnNivel1.setPosition((Gdx.graphics.getWidth() - btnNivel1.getWidth()) / 6*1, (Gdx.graphics.getHeight() - btnNivel1.getHeight()) / 2 - 70);

        btnNivel2 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel2.setSize(80,86);
        btnNivel2.setPosition((Gdx.graphics.getWidth() - btnNivel2.getWidth()) / 6*2, (Gdx.graphics.getHeight() - btnNivel2.getHeight()) / 2 - 70);

        btnNivel3 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel3.setSize(80,86);
        btnNivel3.setPosition((Gdx.graphics.getWidth() - btnNivel3.getWidth()) / 6*3, (Gdx.graphics.getHeight() - btnNivel3.getHeight()) / 2 - 70);

        btnNivel4 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel4.setSize(80,86);
        btnNivel4.setPosition((Gdx.graphics.getWidth() - btnNivel4.getWidth()) / 6*4, (Gdx.graphics.getHeight() - btnNivel4.getHeight()) / 2 - 70);

        btnNivel5 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel5.setSize(80,86);
        btnNivel5.setPosition((Gdx.graphics.getWidth() - btnNivel5.getWidth()) /6*5 , (Gdx.graphics.getHeight() - btnNivel5.getHeight()) / 2 - 70);

        btnNivel6 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel6.setSize(80,86);
        btnNivel6.setPosition((Gdx.graphics.getWidth() - btnNivel6.getWidth()) / 6*1, (Gdx.graphics.getHeight() - btnNivel6.getHeight()) / 2 - 200   );

        btnNivel7 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel7.setSize(80,86);
        btnNivel7.setPosition((Gdx.graphics.getWidth() - btnNivel7.getWidth()) / 6*2, (Gdx.graphics.getHeight() - btnNivel7.getHeight()) / 2 - 200);

        btnNivel8 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel8.setSize(80,86);
        btnNivel8.setPosition((Gdx.graphics.getWidth() - btnNivel8.getWidth()) / 6*3, (Gdx.graphics.getHeight() - btnNivel8.getHeight()) / 2 - 200);

        btnNivel9 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel9.setSize(80,86);
        btnNivel9.setPosition((Gdx.graphics.getWidth() - btnNivel9.getWidth()) / 6*4, (Gdx.graphics.getHeight() - btnNivel9.getHeight()) / 2 - 200);

        btnNivel10 = new Imagen(Recursos.BOTONNIVEL, Recursos.BOTONNIVELHOVER);
        btnNivel10.setSize(80,86);
        btnNivel10.setPosition((Gdx.graphics.getWidth() - btnNivel10.getWidth()) /6*5 , (Gdx.graphics.getHeight() - btnNivel10.getHeight()) / 2 - 200);

        // La música del menú sigue reproduciéndose automáticamente
        // Solo reproducir si no está sonando ya
        GestorMusica.reproducir(GestorMusica.TipoMusica.MENU);
    }

    @Override
    public void render(float delta) {
        b.begin();
        fondo.dibujar();
        btnNivel1.dibujar();
        btnNivel2.dibujar();
        btnNivel3.dibujar();
        btnNivel4.dibujar();
        btnNivel5.dibujar();
        btnNivel6.dibujar();
        btnNivel7.dibujar();
        btnNivel8.dibujar();
        btnNivel9.dibujar();
        btnNivel10.dibujar();

        if (btnNivel1.isClicked()) {
            // Aquí SÍ cambiar la música porque vamos al nivel
            GestorMusica.reproducir(GestorMusica.TipoMusica.NIVEL);
            Render.app.setScreen(new PantallaNivel1());
        }

        b.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        GestorMusica.pausar();
    }

    @Override
    public void resume() {
        GestorMusica.reanudar();
    }

    @Override
    public void hide() {
        // NO parar la música si volvemos al menú
        // Solo parar si vamos a un nivel (eso se maneja en el click)
    }

    @Override
    public void dispose() {
        // NO dispose la música aquí, se hace globalmente
    }
}
