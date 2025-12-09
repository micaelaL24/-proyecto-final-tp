package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elementos.Barrera;
import elementos.BloqueMovil;
import elementos.Boton;
import elementos.Jugador;
import elementos.Jugador.TipoJugador;
import utiles.Recursos;
import utiles.Render;
import utiles.MapaHelper;

public class PantallaNivel4 extends PantallaNivelBase implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    public Jugador jugadorLuna;
    public Jugador jugadorSol;
    private Music musicaNivel;

    private Array<Rectangle> colisiones;
    private Array<Rectangle> peligroLava;
    private Array<Rectangle> peligroAgua;
    private Array<Rectangle> peligro;
    private Array<Rectangle> puertaLuna;
    private Array<Rectangle> puertaSol;


    private Array<BloqueMovil> bloquesMoviles;
    private Array<Boton> botones;
    private Array<Barrera> barreras;

    @Override
    public void show() {
        // Cargar mapa
        mapa = new TmxMapLoader().load(Recursos.MENUNIVEL4);
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);


        numeroNivel = 4;

        camera = configurarCamara(mapa);

        // Crear jugadores
        jugadorLuna = new Jugador(150, 300, TipoJugador.AGUA);
        jugadorSol = new Jugador(250, 300, TipoJugador.FUEGO);

        // Cargar objetos del mapa
        colisiones = MapaHelper.cargarRectangulos(mapa, "Colisiones");
        peligroLava = MapaHelper.cargarRectangulos(mapa, "PeligroLava");
        peligroAgua = MapaHelper.cargarRectangulos(mapa, "PeligroAgua");
        peligro = MapaHelper.cargarRectangulos(mapa, "Peligro");
        estrellas = MapaHelper.cargarRectangulos(mapa, "Estrellas");
        puertaLuna = MapaHelper.cargarRectangulos(mapa, "PuertaLuna");
        puertaSol = MapaHelper.cargarRectangulos(mapa, "PuertaSol");

        // Cargar bloques móviles
        bloquesMoviles = new Array<>();
        Array<Rectangle> bloquesData = MapaHelper.cargarRectangulos(mapa, "BloquesMoviles");
        for (Rectangle rect : bloquesData) {
            bloquesMoviles.add(new BloqueMovil(rect.x, rect.y, rect.width, rect.height));
        }

        // Cargar botones
        botones = new Array<>();
        Array<Rectangle> botonesData = MapaHelper.cargarRectangulos(mapa, "Botones");
        for (Rectangle rect : botonesData) {
            botones.add(new Boton(rect.x, rect.y, rect.width, rect.height, Recursos.BOTONNORMAL, Recursos.BOTONPRESIONADO));
        }

        barreras = new Array<>();
        Array<Rectangle> barrerasData = MapaHelper.cargarRectangulos(mapa, "Barrera");

        // Crear barreras que se abren si CUALQUIER botón está presionado
        if (barrerasData.size > 0 && botones.size > 0) {
            for (Rectangle rect : barrerasData) {
                // Pasar todos los botones a la barrera (lógica OR automática)
                barreras.add(new Barrera(rect.x, rect.y, rect.width, rect.height, botones));
            }
        }

        System.out.println("Nivel 4 cargado:");
        System.out.println("- Estrellas: " + estrellas.size);
        System.out.println("- Bloques móviles: " + bloquesMoviles.size);
        System.out.println("- Botones: " + botones.size);
        System.out.println("- Barreras: " + barreras.size);
        System.out.println("- Peligros: " + peligro.size);


        timer.setTiempoRestante(90.0f);

        // Música
        musicaNivel = Gdx.audio.newMusic(Gdx.files.internal("musicanivel1.mp3"));
        musicaNivel.setLooping(true);
        musicaNivel.setVolume(0.6f);
        musicaNivel.play();
    }

    @Override
    protected void updateNivel(float delta) {
        // Actualizar botones primero
        for (Boton boton : botones) {
            boton.update(jugadorLuna, jugadorSol);
        }

        // Actualizar barreras según estado de botones
        for (Barrera barrera : barreras) {
            barrera.update(delta);
        }

        // Crear array de colisiones dinámicas
        Array<Rectangle> todasLasColisiones = new Array<>();
        todasLasColisiones.addAll(colisiones);

        for (BloqueMovil bloque : bloquesMoviles) {
            todasLasColisiones.add(bloque.getHitbox());
        }

        for (Barrera barrera : barreras) {
            if (barrera.estaActiva()) {
                todasLasColisiones.add(barrera.getHitbox());
            }
        }

        // Actualizar jugadores
        jugadorLuna.update(delta, todasLasColisiones);
        jugadorSol.update(delta, todasLasColisiones);

        // Actualizar bloques móviles
        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.update(delta, colisiones, jugadorLuna, jugadorSol);
        }

        manejarRecoleccionEstrellas(jugadorLuna, jugadorSol);
        if (verificarPeligros(jugadorLuna, jugadorSol, peligroLava, peligroAgua)) return;
        if (verificarPeligrosComunes(jugadorLuna, jugadorSol, peligro)) return;
        if (verificarVictoria(jugadorLuna, jugadorSol, puertaLuna, puertaSol)) return;

        // Dibujar todo
        Render.limpiarPantalla(0, 0, 0);
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        dibujarEstrellas();

        // Dibujar bloques móviles
        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.dibujar(batch);
        }

        // Dibujar barreras
        for (Barrera barrera : barreras) {
            barrera.dibujar(batch);
        }

        // Dibujar botones
        for (Boton boton : botones) {
            boton.dibujar(batch);
        }


        jugadorLuna.dibujar(batch);
        jugadorSol.dibujar(batch);

        batch.end();
    }

    @Override
    protected OrthographicCamera getCamera() {
        return camera;
    }


    @Override
    protected Jugador getJugadorLuna() {
        return jugadorLuna;
    }

    @Override
    protected Jugador getJugadorSol() {
        return jugadorSol;
    }

    @Override
    public Array<Rectangle> getColisiones() { return colisiones; }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        super.pause();
        if (musicaNivel != null) musicaNivel.pause();
    }

    @Override
    public void resume() {
        super.resume();
        if (musicaNivel != null) musicaNivel.play();
    }

    @Override
    public void hide() {
        if (musicaNivel != null) musicaNivel.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        mapa.dispose();
        mapRenderer.dispose();
        jugadorLuna.dispose();
        jugadorSol.dispose();

        for (BloqueMovil bloque : bloquesMoviles) {
            bloque.dispose();
        }

        for (Boton boton : botones) {
            boton.dispose();
        }

        for (Barrera barrera : barreras) {
            barrera.dispose();
        }

        if (musicaNivel != null) musicaNivel.dispose();
    }
}
