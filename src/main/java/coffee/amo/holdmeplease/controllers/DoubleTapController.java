package coffee.amo.holdmeplease.controllers;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class DoubleTapController {
    public static class ActionData {
        public Player player;
        public int doubleTapWindow;
        public int tapWindowDuration;

        public ActionData(Player player, int doubleTapWindow, int tapWindowDuration) {
            this.player = player;
            this.doubleTapWindow = doubleTapWindow;
            this.tapWindowDuration = tapWindowDuration;
        }
    }
    private int doubleTapWindow = 0;
    private int tapWindowDuration = 20;
    private boolean wasDown = false;
    private KeyMapping KEY;
    private Player player;
    private Consumer<ActionData> doubleTapAction;
    private Consumer<ActionData> tickAction;
    private Consumer<ActionData> timeOutAction;
    private ActionData tickData;

    public boolean hasStarted() {
        return wasDown;
    }
    public DoubleTapController(KeyMapping KEY, Player player, Consumer<ActionData> doubleTapAction, Consumer<ActionData> tickAction, Consumer<ActionData> timeOutAction) {
        this.KEY = KEY;
        this.player = player;
        this.doubleTapAction = doubleTapAction;
        this.tickAction = tickAction;
        this.timeOutAction = timeOutAction;
    }

    public void tick() {
        if(!getKey().getKeyConflictContext().isActive()) reset();
        if (doubleTapWindow == 0) {
            wasDown = false;
            timeOutAction.accept(tickData);
        }
        if (doubleTapWindow > 0) {
            if(tickData == null) tickData = new ActionData(player, doubleTapWindow, tapWindowDuration);
            tickData.doubleTapWindow = doubleTapWindow;
            tickData.tapWindowDuration = tapWindowDuration;
            doubleTapWindow--;
            tickAction.accept(tickData);
        }
    }

    public void doubleTapped(Consumer<ActionData> playerSupplier) {
        if (wasDown) {
            playerSupplier.accept(tickData);
            wasDown = false;
        }
    }

    public void doubleTap() {
        doubleTapWindow = 0;
        doubleTapped(doubleTapAction);
    }

    public void tap(){
        if(!getKey().getKeyConflictContext().isActive()) return;
        if (!wasDown) {
            wasDown = true;
            doubleTapWindow = tapWindowDuration;
        } else {
            doubleTap();
        }
    }

    public void reset() {
        doubleTapWindow = tapWindowDuration;
        wasDown = false;
    }

    public KeyMapping getKey() {
        return KEY;
    }

    public void setTapWindowDuration(int tapWindowDuration) {
        this.tapWindowDuration = tapWindowDuration;
    }

    public int getTapWindowDuration() {
        return tapWindowDuration;
    }


}
