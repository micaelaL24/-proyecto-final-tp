package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import elementos.Jugador;
import online.ClientNetworkListener;
import online.ClientThread;
import utiles.InputManager;
import utiles.Render;

public class PantallaNivelCliente extends PantallaNivelBase implements ClientNetworkListener {

    private final PantallaNivelBase nivelReal;   // ← el nivel verdadero
    private ClientThread clientThread;
    private int myPlayerNum;

    private Texture imagenConectando;
    private boolean gameStarted = false;

    private Jugador jugadorLuna;
    private Jugador jugadorSol;

    public PantallaNivelCliente(PantallaNivelBase nivelReal, ClientThread client, int playerNum) {
        this.nivelReal = nivelReal;
        this.clientThread = client;
        this.myPlayerNum = playerNum;
    }

    @Override
    public void show() {

        nivelReal.show();
        nivelReal.setNotificador(this);

        imagenConectando = new Texture("conectando.png");
        clientThread.setListener(this);

        // copiar referencias
        this.jugadorLuna = nivelReal.getJugadorLuna();
        this.jugadorSol = nivelReal.getJugadorSol();
    }

    @Override
    public void render(float delta) {

        if (!gameStarted) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            Render.batch.begin();
            Render.batch.draw(imagenConectando, 0,0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Render.batch.end();
            return;
        }

        // PAUSA
        if (InputManager.isPausePressed()) {
            if (!nivelReal.timer.estaPausado()) {
                nivelReal.timer.pausar();
                clientThread.sendMessage("Pause");
            } else {
                nivelReal.timer.despausar();
                clientThread.sendMessage("Resume");
            }
        }

        if (!nivelReal.timer.estaPausado()) {

            if (myPlayerNum == 1)
                jugadorLuna.update(delta, nivelReal.getColisiones());
            else
                jugadorSol.update(delta, nivelReal.getColisiones());
        }

        // Render del nivel real
        nivelReal.render(delta);

        // Enviar posición
        if (!nivelReal.timer.estaPausado()) {

            if (myPlayerNum == 1)
                clientThread.sendPosition(jugadorLuna.getX(), jugadorLuna.getY(), jugadorLuna.isMirandoDerecha());
            else
                clientThread.sendPosition(jugadorSol.getX(), jugadorSol.getY(), jugadorSol.isMirandoDerecha());
        }
    }

    @Override
    protected void updateNivel(float delta) {

    }

    @Override
    protected OrthographicCamera getCamera() {
        return null;
    }

    @Override
    protected Jugador getJugadorLuna() {
        return null;
    }

    @Override
    protected Jugador getJugadorSol() {
        return null;
    }


    //   NETWORK


    @Override
    public void onStart() {
        gameStarted = true;
        nivelReal.timer.despausar();
    }
    @Override
    protected void notificarVictoria() {
        System.out.println("[CLIENT] Enviando WIN al servidor");
        clientThread.sendMessage("Win");
    }

    @Override
    protected void notificarDerrota() {
        System.out.println("[CLIENT] Enviando LOSE al servidor");
        clientThread.sendMessage("Lose");
    }


    @Override
    public void onUpdatePos(int playerNum, float x, float y, boolean mirandoDerecha) {

        if (playerNum == myPlayerNum) return;

        if (playerNum == 1) {
            jugadorLuna.setPosition(x, y);
            jugadorLuna.setMirandoDerecha(mirandoDerecha);
        } else {
            jugadorSol.setPosition(x, y);
            jugadorSol.setMirandoDerecha(mirandoDerecha);
        }
    }


    @Override
    public void onWin() {
        Gdx.app.postRunnable(() -> {
            System.out.println("[CLIENT] Cambiando a PantallaVictoria");
            Render.app.setScreen(new PantallaVictoria());
        });
    }

    @Override
    public void onLose() {
        Gdx.app.postRunnable(() -> {
            System.out.println("[CLIENT] Cambiando a PantallaDerrota");
            Render.app.setScreen(new PantallaDerrota());
        });
    }


    @Override public void onPauseGame() { nivelReal.timer.pausar(); }
    @Override public void onResumeGame() { nivelReal.timer.despausar(); }

    @Override
    public void hide() {
        System.out.println("[CLIENT] Cerrando PantallaNivelCliente");
        if (clientThread != null) clientThread.terminate();
    }

    @Override public void onConnected(int p) {}
    @Override public void onFull() {}
    @Override public void onDisconnect() {}
    @Override public void onLevelMismatch() {}

    @Override public void resize(int w,int h){}

    @Override
    protected Array<Rectangle> getColisiones() {
        return null;
    }

    @Override public void pause(){}
    @Override public void resume(){}
    @Override public void dispose(){}
}
