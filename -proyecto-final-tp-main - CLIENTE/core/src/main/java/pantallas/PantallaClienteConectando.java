package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import online.ClientNetworkListener;
import online.ClientThread;
import utiles.Render;

public class PantallaClienteConectando implements Screen, ClientNetworkListener {

    private int nivelDestino;

    public PantallaClienteConectando(int nivel) {
        this.nivelDestino = nivel;
    }

    private Texture imagenConectando;
    private Texture imagenError;
    private ClientThread client;

    private boolean startGame = false;
    private int myPlayerNum = -1;

    // NUEVAS VARIABLES para el mensaje de error
    private boolean mostrarMensajeError = false;
    private float tiempoMensajeError = 0;
    private static final float TIEMPO_MOSTRAR_ERROR = 2.5f; // 2.5 segundos


    @Override
    public void show() {

        imagenConectando = new Texture("conectando.png");

        imagenError = new Texture("fondos/fondoNivelInc.png");

        // Pasar el nivel al ClientThread
        client = new ClientThread(this, nivelDestino);
        client.start();



        System.out.println("[CLIENT] Cliente inicializado para nivel " + nivelDestino);
    }

    @Override
    public void render(float delta) {

        // Si hay un error de nivel diferente
        if (mostrarMensajeError) {
            tiempoMensajeError += delta;

            if (tiempoMensajeError >= TIEMPO_MOSTRAR_ERROR) {
                Render.app.setScreen(new PantallaMenu());
                return;
            }

            Render.limpiarPantalla(0,0,0);

            Render.batch.begin();
            Render.batch.draw(
                imagenError,
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
            );
            Render.batch.end();
            return;
        }

        if (startGame && myPlayerNum != -1) {

                // 1. Crear el nivel base según nivelDestino
                PantallaNivelBase nivelReal;

                switch (nivelDestino) {
                    case 1: nivelReal = new PantallaNivel1(); break;
                    case 2: nivelReal = new PantallaNivel2(); break;
                    case 3: nivelReal = new PantallaNivel3(); break;
                    case 4: nivelReal = new PantallaNivel4(); break;
                    case 5: nivelReal = new PantallaNivel5(); break;
                    case 6: nivelReal = new PantallaNivel6(); break;
                    case 7: nivelReal = new PantallaNivel7(); break;
                    case 8: nivelReal = new PantallaNivel8(); break;
                    case 9: nivelReal = new PantallaNivel9(); break;
                    case 10: nivelReal = new PantallaNivel10(); break;

                    default:
                        throw new RuntimeException("Nivel no implementado: " + nivelDestino);
                }

                // 2. Crear la pantalla cliente que envuelve al nivel real
                Screen nuevaPantalla = new PantallaNivelCliente(
                    nivelReal,
                    client,
                    myPlayerNum
                );

                // 3. Dispara "start"
                ((ClientNetworkListener)nuevaPantalla).onStart();

                // 4. Cambiar pantalla
                Render.app.setScreen(nuevaPantalla);
                return;
            }


            Render.limpiarPantalla(0, 0, 0);

        Render.batch.begin();
        Render.batch.draw(
            imagenConectando,
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        Render.batch.end();
    }

    // NETWORK EVENTS

    @Override
    public void onConnected(int playerNum) {
        System.out.println("[CLIENT] Asignado playerNum=" + playerNum);
        myPlayerNum = playerNum;
    }

    @Override
    public void onStart() {
        System.out.println("[CLIENT] RECIBIDO START");
        startGame = true;
    }

    @Override
    public void onFull() {
        System.out.println("[CLIENT] Servidor lleno (FULL)");
    }

    @Override
    public void onUpdatePos(int playerNum, float x, float y, boolean mirandoDerecha) {
        // NO hacemos nada aquí.
    }

    @Override
    public void onDisconnect() {
        System.out.println("[CLIENT] Desconectado del servidor");
    }

    @Override
    public void onPauseGame() {

    }

    @Override
    public void onResumeGame() {

    }

    @Override
    public void onWin() {
        Render.app.setScreen(new PantallaVictoria());
    }

    @Override
    public void onLose() {
        Render.app.setScreen(new PantallaDerrota());
    }


    @Override
    public void onLevelMismatch() {
        System.out.println("[CLIENT] ERROR: El servidor está en un nivel diferente");

        // Cerrar la conexión
        if (client != null) {
            client.terminate();
        }

        // Activar el mensaje de error
        mostrarMensajeError = true;
    }



    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        if (imagenConectando != null) imagenConectando.dispose();
        if (imagenError != null) imagenConectando.dispose();
    }
}
