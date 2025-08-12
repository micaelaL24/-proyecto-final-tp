package elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Jugador {
    private Animation<TextureRegion> animacionCaminar;
    private Animation<TextureRegion> animacionIdle;
    private Animation<TextureRegion> animacionSaltar;

    private TextureRegion frameActual;
    private float tiempoAnimacion = 0f;

    private float x, y;
    private float ancho, alto; // Tamaño personalizable
    private float velocidad = 150f;
    private float velY = 0;
    private final float gravedad = -500f;
    private boolean enSuelo = false;
    private Rectangle hitbox;
    private boolean vivo = true;

    // Estados de animación
    private EstadoAnimacion estadoActual = EstadoAnimacion.IDLE;
    private boolean mirandoDerecha = true;

    private int estrellasRecolectadas = 0;

    private enum EstadoAnimacion {
        IDLE, CAMINANDO, SALTANDO
    }

    public Jugador(float x, float y) {
        this.x = x;
        this.y = y;

        cargarAnimaciones();

        // Usar el primer frame para calcular el tamaño inicial
        frameActual = animacionIdle.getKeyFrame(0);
        this.ancho = frameActual.getRegionWidth();
        this.alto = frameActual.getRegionHeight();
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    // Constructor con tamaño personalizado
    public Jugador(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        cargarAnimaciones();

        frameActual = animacionIdle.getKeyFrame(0);
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    private void cargarAnimaciones() {
        // Cargar las 4 imágenes
        TextureRegion frame1 = new TextureRegion(new Texture("quieto.png"));
        TextureRegion frame2 = new TextureRegion(new Texture("caminando.png"));
        TextureRegion frame3 = new TextureRegion(new Texture("corriendo 1.png"));
        TextureRegion frame4 = new TextureRegion(new Texture("corriendo2.png"));

        // Animación de caminar: solo intercambia entre frame 3 y 4
        TextureRegion[] framesCaminar = new TextureRegion[2];
        framesCaminar[0] = frame3;
        framesCaminar[1] = frame4;
        animacionCaminar = new Animation<>(0.3f, framesCaminar); // Más lento para ver bien el cambio
        animacionCaminar.setPlayMode(Animation.PlayMode.LOOP);

        // Para idle usar el primer frame
        TextureRegion[] framesIdle = new TextureRegion[1];
        framesIdle[0] = frame1;
        animacionIdle = new Animation<>(0.5f, framesIdle);

        // Para saltar usar el segundo frame
        TextureRegion[] framesSaltar = new TextureRegion[1];
        framesSaltar[0] = frame2;
        animacionSaltar = new Animation<>(0.1f, framesSaltar);
    }

    public void update(float delta, Array<Rectangle> colisiones) {
        if (!vivo) return;

        tiempoAnimacion += delta;
        float dx = 0;
        boolean moviendose = false;

        // Movimiento horizontal
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx = -velocidad * delta;
            mirandoDerecha = false;
            moviendose = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx = velocidad * delta;
            mirandoDerecha = true;
            moviendose = true;
        }

        // Salto
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && enSuelo) {
            velY = 380f;
            enSuelo = false;
        }

        // Determinar estado de animación
        if (!enSuelo) {
            estadoActual = EstadoAnimacion.SALTANDO;
        } else if (moviendose) {
            estadoActual = EstadoAnimacion.CAMINANDO;
        } else {
            estadoActual = EstadoAnimacion.IDLE;
        }

        // Aplicar gravedad
        velY += gravedad * delta;

        // Mover y colisionar
        moverEnX(dx, colisiones);
        moverEnY(velY * delta, colisiones);

        // Actualizar frame actual según el estado
        actualizarAnimacion();
    }

    private void actualizarAnimacion() {
        switch (estadoActual) {
            case IDLE:
                frameActual = animacionIdle.getKeyFrame(tiempoAnimacion);
                break;
            case CAMINANDO:
                frameActual = animacionCaminar.getKeyFrame(tiempoAnimacion);
                break;
            case SALTANDO:
                frameActual = animacionSaltar.getKeyFrame(tiempoAnimacion);
                break;
        }
    }

    private void moverEnX(float dx, Array<Rectangle> colisiones) {
        x += dx;
        hitbox.setPosition(x, y);

        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                x -= dx;
                hitbox.setPosition(x, y);
                break;
            }
        }
    }

    private void moverEnY(float dy, Array<Rectangle> colisiones) {
        y += dy;
        hitbox.setPosition(x, y);

        boolean colisionVertical = false;
        for (Rectangle rect : colisiones) {
            if (hitbox.overlaps(rect)) {
                colisionVertical = true;
                break;
            }
        }

        if (colisionVertical) {
            if (dy < 0) {
                enSuelo = true;
                velY = 0;
                y -= dy;
                hitbox.setPosition(x, y);
            } else {
                velY = 0;
                y -= dy;
                hitbox.setPosition(x, y);
            }
        } else {
            enSuelo = false;
        }
    }

    public void morir() {
        vivo = false;
        System.out.println("¡Jugador muerto!");
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void sumarEstrella() {
        estrellasRecolectadas++;
        System.out.println("Estrellas recolectadas: " + estrellasRecolectadas);
    }

    public int getEstrellasRecolectadas() {
        return estrellasRecolectadas;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        hitbox.setPosition(x, y);
    }

    public void dibujar(SpriteBatch batch) {
        // Dibujar el frame actual, volteándolo si es necesario
        if (mirandoDerecha) {
            batch.draw(frameActual, x, y);
        } else {
            // Voltear horizontalmente cuando mira a la izquierda
            batch.draw(frameActual, x + frameActual.getRegionWidth(), y,
                -frameActual.getRegionWidth(), frameActual.getRegionHeight());
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        // Las texturas se disponen automáticamente cuando se dispone el spritesheet
    }
}
