package dev.ryanhcode.zonai.movement;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleTapController {
    private int doubleTapWindow = 0;
    private static int TAP_MAX_TIME = 20;
    private boolean wasDown = false;
    private KeyMapping KEY;
    private Player player;
    private Consumer<Player> doubleTapAction;

    public boolean hasStarted() {
        return wasDown;
    }
    public DoubleTapController(KeyMapping KEY, Player player, Consumer<Player> doubleTapAction) {
        this.KEY = KEY;
        this.player = player;
        this.doubleTapAction = doubleTapAction;
    }

    public void tick() {
        if (doubleTapWindow == 0) {
            wasDown = false;
        }
        if (doubleTapWindow > 0) {
            doubleTapWindow--;
        }
    }

    public void doubleTapped(Consumer<Player> playerSupplier) {
        if (wasDown) {
            playerSupplier.accept(player);
            wasDown = false;
        }
    }

    public void doubleTap() {
        doubleTapWindow = 0;
        doubleTapped(doubleTapAction);
    }

    public void tap(){
        if (!wasDown) {
            wasDown = true;
            doubleTapWindow = TAP_MAX_TIME;
        } else {
            doubleTap();
        }
    }

    public void reset() {
        doubleTapWindow = TAP_MAX_TIME;
        wasDown = false;
    }

    public KeyMapping getKey() {
        return KEY;
    }
}
