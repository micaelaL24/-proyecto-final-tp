package elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import utiles.Render;

public class Imagen {
    private Texture normal;
    private Texture hover;
    private Sprite s;


    // Constructor con solo una imagen (sin hover)
    public Imagen(String rutaNormal) {
        this(rutaNormal, null);
    }

    // Constructor con hover opcional
    public Imagen(String rutaNormal, String rutaHover) {
        normal = new Texture(rutaNormal);
        hover = (rutaHover != null) ? new Texture(rutaHover) : null;
        s = new Sprite(normal);
    }

    // Detectar si el mouse está encima
    private boolean isMouseOver() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        return mouseX > s.getX() && mouseX < s.getX() + s.getWidth()
            && mouseY > s.getY() && mouseY < s.getY() + s.getHeight();
    }

    // Dibujar el botón (si tiene hover cambia de imagen)
    public void dibujar() {
        if (hover != null) {
            s.setTexture(isMouseOver() ? hover : normal);
        }
        s.draw(Render.batch);
    }

    // Detectar click
    public boolean isClicked() {
        return isMouseOver() && Gdx.input.justTouched();
    }

    public void setTransperencia(float a) {
        s.setAlpha(a);
    }

    public void setSize(float ancho, float alto) {
        s.setSize(ancho, alto);
    }

    public void setPosition(float x, float y){
        s.setPosition(x, y);
    }

    public float getWidth() {
        return s.getWidth();
    }

    public float getHeight() {
        return s.getHeight();
    }
}
