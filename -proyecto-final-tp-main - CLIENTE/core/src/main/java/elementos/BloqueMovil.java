package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import utiles.Recursos;

public class BloqueMovil {

    private Rectangle hitbox;
    private float velocidadEmpuje = 140f;

    private float velX = 0;
    private float velY = 0;
    private final float gravedad = -500f;
    private boolean enSuelo = false;

    private Texture textura;

    public BloqueMovil(float x, float y, float ancho, float alto) {
        this(x, y, ancho, alto, Recursos.BLOQUEMOVIL);
    }

    public BloqueMovil(float x, float y, float ancho, float alto, String rutaImagen) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.textura = new Texture(rutaImagen);
    }

    public void update(float delta, Array<Rectangle> colisionesEstaticas,
                       Jugador jugadorAgua, Jugador jugadorFuego) {

        // Aplicar gravedad
        velY += gravedad * delta;
        velX = 0;

        // Determinar empuje
        if (jugadorAgua != null && jugadorAgua.estaVivo()) calcularEmpuje(jugadorAgua, delta);
        if (jugadorFuego != null && jugadorFuego.estaVivo()) calcularEmpuje(jugadorFuego, delta);

        // Mover bloque
        moverEnX(velX, colisionesEstaticas);
        moverEnY(velY * delta, colisionesEstaticas);
    }

    private void calcularEmpuje(Jugador jugador, float delta) {

        if (empujandoPorDerecha(jugador)) {
            velX = velocidadEmpuje * delta;
        }
        else if (empujandoPorIzquierda(jugador)) {
            velX = -velocidadEmpuje * delta;
        }
    }


    //  DETECCIÓN DE EMPUJE


    private static final float TOLERANCIA = 18f;

    // Jugador está a la izquierda del bloque → empuja a la derecha
    private boolean empujandoPorDerecha(Jugador jugador) {
        Rectangle j = jugador.getHitbox();

        float jRight = j.x + j.width;
        float bLeft = hitbox.x;

        return jRight > bLeft - TOLERANCIA &&
            jRight < bLeft + TOLERANCIA &&
            j.y < hitbox.y + hitbox.height &&
            j.y + j.height > hitbox.y;
    }

    // Jugador está a la derecha del bloque → empuja a la izquierda
    private boolean empujandoPorIzquierda(Jugador jugador) {
        Rectangle j = jugador.getHitbox();

        float jLeft = j.x;
        float bRight = hitbox.x + hitbox.width;

        return jLeft < bRight + TOLERANCIA &&
            jLeft > bRight - TOLERANCIA &&
            j.y < hitbox.y + hitbox.height &&
            j.y + j.height > hitbox.y;
    }



    //  MOVIMIENTO Y COLISIONES


    private void moverEnX(float dx, Array<Rectangle> colisiones) {
        hitbox.x += dx;

        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                hitbox.x -= dx;
                velX = 0;
                break;
            }
        }
    }

    private void moverEnY(float dy, Array<Rectangle> colisiones) {
        hitbox.y += dy;

        boolean colisionVertical = false;

        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                colisionVertical = true;
                break;
            }
        }

        if (colisionVertical) {
            if (dy < 0) enSuelo = true;
            velY = 0;
            hitbox.y -= dy;
        } else {
            enSuelo = false;
        }
    }


    //  DIBUJAR


    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean estaEnSuelo() {
        return enSuelo;
    }

    public void dispose() {
        if (textura != null) textura.dispose();
    }
}
