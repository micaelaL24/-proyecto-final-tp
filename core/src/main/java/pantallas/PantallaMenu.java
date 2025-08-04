package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import elementos.BarraVolumen;
import elementos.Imagen;
import utiles.GestorMusica;
import utiles.Recursos;
import utiles.Render;

public class PantallaMenu implements Screen {

    Imagen fondo, btn1, btn2;
    SpriteBatch b;
    BarraVolumen barraVolumen;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDOMENU);
        fondo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        b = Render.batch;

        btn1 = new Imagen(Recursos.BOTONJUGAR, Recursos.BOTONJUGARHOVER);
        btn1.setSize(220,80);
        btn1.setPosition((Gdx.graphics.getWidth() - btn1.getWidth()) / 2, (Gdx.graphics.getHeight() - btn1.getHeight()) / 2 - 70);

        btn2 = new Imagen(Recursos.BOTONINSTRUC, Recursos.BOTONINSTRUCHOVER);
        btn2.setSize(280,80);
        btn2.setPosition((Gdx.graphics.getWidth() - btn2.getWidth()) / 2, (Gdx.graphics.getHeight() - btn2.getHeight()) / 2 - 180);

        // Cargar músicas al inicio (solo la primera vez)
        if (!GestorMusica.estanCargadas()) {
            GestorMusica.cargarMusicas();
        }

        // Reproducir música del menú
        GestorMusica.reproducir(GestorMusica.TipoMusica.MENU);

        // Crear barra de volumen en la esquina superior derecha
        float barraAncho = 200;
        float barraAlto = 20;
        float barraX = Gdx.graphics.getWidth() - barraAncho - 30;
        float barraY = Gdx.graphics.getHeight() - 60;
        barraVolumen = new BarraVolumen(barraX, barraY, barraAncho, barraAlto);
    }

    @Override
    public void render(float delta) {
        // Actualizar barra de volumen
        barraVolumen.update();

        b.begin();
        fondo.dibujar();
        btn1.dibujar();
        btn2.dibujar();

        // Dibujar barra de volumen
        barraVolumen.dibujar(b);

        if (btn1.isClicked()) {
            Render.app.setScreen(new PantallaNivelSeleccion());
            System.out.println("Pantalla cambiada");
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
        // NO parar la música aquí para que continúe en la siguiente pantalla
    }

    @Override
    public void dispose() {
        if (barraVolumen != null) {
            barraVolumen.dispose();
        }
    }


}
