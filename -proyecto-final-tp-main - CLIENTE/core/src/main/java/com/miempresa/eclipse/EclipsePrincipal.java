package com.miempresa.eclipse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import pantallas.*;
import utiles.InputManager;
import utiles.Render;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class EclipsePrincipal extends Game {


    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        this.setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        // Verificar ESC para cerrar el juego
        if (InputManager.isEscapePressed()) {
            Gdx.app.exit();
            return;
        }
        super.render();

    }

    @Override
    public void dispose() {

    }
}
