package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elementos.Jugador;
import utiles.Recursos;
import utiles.Render;
import utiles.MapaHelper;
import utiles.Timer;

public class PantallaNivel1 extends PantallaNivelBase implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Jugador jugador;
    private SpriteBatch b;
    private Music musicaNivel;

    private Array<Rectangle> colisiones;
    private Array<Rectangle> peligroLava;
    private Array<Rectangle> estrellas;
    private Array<Rectangle> puerta;

    // HUD
    private Texture estrellaTexture;
    private BitmapFont font;
    private GlyphLayout layout;
    private Timer timer;

    @Override
    public void show() {
        // Cargar mapa
        mapa = new TmxMapLoader().load(Recursos.MENUNIVEL1);
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);

        // Cámara
        int mapWidth = mapa.getProperties().get("width", Integer.class)
            * mapa.getProperties().get("tilewidth", Integer.class);
        int mapHeight = mapa.getProperties().get("height", Integer.class)
            * mapa.getProperties().get("tileheight", Integer.class);

        camera = new OrthographicCamera();
        float screenRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        float mapRatio = (float) mapWidth / mapHeight;

        if (mapRatio > screenRatio) {
            camera.viewportWidth = mapWidth;
            camera.viewportHeight = mapWidth / screenRatio;
        } else {
            camera.viewportHeight = mapHeight;
            camera.viewportWidth = mapHeight * screenRatio;
        }

        camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);
        camera.update();

        // Jugador
        jugador = new Jugador(100, 100);
        b = Render.batch;

        // Cargar objetos
        colisiones = MapaHelper.cargarRectangulos(mapa, "Colisiones");
        peligroLava = MapaHelper.cargarRectangulos(mapa, "PeligroLava");
        estrellas = MapaHelper.cargarRectangulos(mapa, "Estrellas");
        puerta = MapaHelper.cargarRectangulos(mapa, "Puerta");

        System.out.println("Puertas cargadas: " + puerta.size); // DEBUG

        // HUD
        estrellaTexture = new Texture(Recursos.ESTRELLA);
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Inicializar timer (60 segundos por ejemplo)
        timer = new Timer(60.0f);

        // Cargar y reproducir música del nivel
        musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("musicanivel1.mp3"));
        musicaNivel.setLooping(true); // Para que se repita en bucle
        musicaNivel.setVolume(0.6f); // Volumen al 60%
        musicaNivel.play();
    }

    @Override
    public void render(float delta) {
        // Verificar si se presiona P para pausar/despausar
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            timer.alternarPausa();
            if (timer.estaPausado()) {
                if (musicaNivel != null) {
                    musicaNivel.pause();
                }
            } else {
                if (musicaNivel != null) {
                    musicaNivel.play();
                }
            }
        }

        // Actualizar timer
        timer.actualizar(delta);

        // Si se acaba el tiempo -> derrota
        if (timer.estaTerminado()) {
            Render.app.setScreen(new PantallaDerrota());
            return;
        }

        // Si el juego está pausado, no actualizar nada más
        if (timer.estaPausado()) {
            // Solo dibujar todo sin actualizar
            // Limpiar pantalla
            Render.limpiarPantalla(0, 0, 0);

            // Dibujar mapa
            mapRenderer.setView(camera);
            mapRenderer.render();

            // Dibujar objetos y HUD
            b.setProjectionMatrix(camera.combined);
            b.begin();

            // Estrellas
            for (Rectangle rectEstrella : estrellas) {
                b.draw(estrellaTexture, rectEstrella.x, rectEstrella.y);
            }

            // Jugador
            jugador.dibujar(b);

            // HUD
            float hudX = camera.position.x - camera.viewportWidth / 2 + 20;
            float hudY = camera.position.y + camera.viewportHeight / 2 - 20;
            float iconSize = 64;

            font.getData().setScale(2f);
            b.draw(estrellaTexture, hudX, hudY - iconSize, iconSize, iconSize);

            String estrellasTexto = "x " + jugador.getEstrellasRecolectadas();
            layout.setText(font, estrellasTexto);
            font.draw(b, estrellasTexto, hudX + iconSize + 15, hudY - iconSize / 2 + layout.height / 2);

            // Mostrar timer
            String timerTexto = "Tiempo: " + (int) Math.ceil(timer.getTiempoRestante());
            if (timer.estaPausado()) {
                timerTexto += " (PAUSADO)";
            }
            layout.setText(font, timerTexto);
            font.draw(b, timerTexto, hudX, hudY - iconSize - 60);

            b.end();
            return; // Salir sin actualizar nada más
        }

        jugador.update(delta, colisiones);

        // Colisión con estrellas
        for (int i = estrellas.size - 1; i >= 0; i--) {
            Rectangle rectEstrella = estrellas.get(i);
            if (jugador.getHitbox().overlaps(rectEstrella)) {
                jugador.sumarEstrella();
                estrellas.removeIndex(i);
            }
        }

        // Si recolecta todas las estrellas -> victoria
        if (estrellas.size == 0) {
            Render.app.setScreen(new PantallaVictoria());
        }

        // Colisión con peligroLava
        for (Rectangle rectPeligroLava : peligroLava) {
            if (jugador.getHitbox().overlaps(rectPeligroLava)) {
                if (jugador.estaVivo()) {
                    jugador.morir();
                    Render.app.setScreen(new PantallaDerrota());
                }
                break;
            }
        }

        // Colisión con puerta
        for (Rectangle rectPuerta : puerta) {
            if (jugador.getHitbox().overlaps(rectPuerta)) {
                System.out.println("¡Victoria detectada!"); // DEBUG
                Render.app.setScreen(new PantallaVictoria());
                break;
            }
        }

        // Limpiar pantalla
        Render.limpiarPantalla(0, 0, 0);

        // Dibujar mapa
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Dibujar objetos y HUD
        b.setProjectionMatrix(camera.combined);
        b.begin();

        // Estrellas
        for (Rectangle rectEstrella : estrellas) {
            b.draw(estrellaTexture, rectEstrella.x, rectEstrella.y);
        }

        // Jugador
        jugador.dibujar(b);

        // HUD
        float hudX = camera.position.x - camera.viewportWidth / 2 + 20;
        float hudY = camera.position.y + camera.viewportHeight / 2 - 20;
        float iconSize = 64;

        font.getData().setScale(2f);
        b.draw(estrellaTexture, hudX, hudY - iconSize, iconSize, iconSize);

        String estrellasTexto = "x " + jugador.getEstrellasRecolectadas();
        layout.setText(font, estrellasTexto);
        font.draw(b, estrellasTexto, hudX + iconSize + 15, hudY - iconSize / 2 + layout.height / 2);

        // Mostrar timer
        String timerTexto = "Tiempo: " + (int) Math.ceil(timer.getTiempoRestante());
        if (timer.estaPausado()) {
            timerTexto += " (PAUSADO)";
        }
        layout.setText(font, timerTexto);
        font.draw(b, timerTexto, hudX, hudY - iconSize - 60);

        b.end();
    }

    @Override
    protected void updateNivel(float delta) {
        jugador.update(delta, colisiones);

        // Colisión estrellas
        for (int i = estrellas.size - 1; i >= 0; i--) {
            if (jugador.getHitbox().overlaps(estrellas.get(i))) {
                jugador.sumarEstrella();
                estrellas.removeIndex(i);
            }
        }

        // Si recolecta todas las estrellas -> victoria
        if (estrellas.size == 0) {
            Render.app.setScreen(new PantallaVictoria());
        }

        // Colisión peligro
        for (Rectangle r : peligroLava) {
            if (jugador.getHitbox().overlaps(r)) {
                jugador.morir();
                Render.app.setScreen(new PantallaDerrota());
                break;
            }
        }

        // Colisión puerta
        for (Rectangle r : puerta) {
            if (jugador.getHitbox().overlaps(r)) {
                Render.app.setScreen(new PantallaVictoria());
                break;
            }
        }

        // Dibujar mapa y jugador
        Render.limpiarPantalla(0, 0, 0);
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar estrellas
        for (Rectangle rectEstrella : estrellas) {
            batch.draw(estrellaTexture, rectEstrella.x, rectEstrella.y);
        }

        // Dibujar jugador
        jugador.dibujar(batch);

        batch.end();
    }

    @Override
    protected OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    protected int getEstrellasRecolectadas() {
        return jugador.getEstrellasRecolectadas();
    }

    @Override public void resize(int width, int height) {}

    @Override
    public void pause() {
        timer.pausar(); // Pausar timer cuando se pausa el juego
        if (musicaNivel != null) {
            musicaNivel.pause();
        }
    }

    @Override
    public void resume() {
        timer.despausar(); // Continuar timer cuando se reanuda el juego
        if (musicaNivel != null) {
            musicaNivel.play();
        }
    }

    @Override
    public void hide() {
        if (musicaNivel != null) {
            musicaNivel.stop();
        }
    }

    @Override
    public void dispose() {
        mapa.dispose();
        jugador.dispose();
        mapRenderer.dispose();
        estrellaTexture.dispose();
        font.dispose();

        if (musicaNivel != null) {
            musicaNivel.dispose();
        }
    }
}
