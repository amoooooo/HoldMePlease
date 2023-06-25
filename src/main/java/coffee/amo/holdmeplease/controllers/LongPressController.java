package coffee.amo.holdmeplease.controllers;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class LongPressController {
    public static class ActionData {
        public Player player;
        public int ticksDown;
        public int longPressTime;

        public ActionData(Player player, int ticksDown, int longPressTime) {
            this.player = player;
            this.ticksDown = ticksDown;
            this.longPressTime = longPressTime;
        }
    }
    private KeyMapping KEY;
    private Player player;
    private Consumer<ActionData> longPressAction;
    private Consumer<ActionData> releaseAction;
    private Consumer<ActionData> tickAction;
    private boolean isDown = false;
    private int ticksDown = 0;
    private int longPressTime;
    private ActionData tickData;

    public LongPressController(KeyMapping KEY, Player player, Consumer<ActionData> longPressAction, Consumer<ActionData> releaseAction, Consumer<ActionData> tickAction, int longPressTime) {
        this.KEY = KEY;
        this.player = player;
        this.longPressAction = longPressAction;
        this.longPressTime = longPressTime;
        this.releaseAction = releaseAction;
        this.tickAction = tickAction;
    }

    public void tick() {
        if(!getKey().getKeyConflictContext().isActive() && isDown) forceRelease();
        if(tickData == null) tickData = new ActionData(player, ticksDown, longPressTime);
        if (isDown) {
            ticksDown++;
            tickData.ticksDown = ticksDown;
            tickData.longPressTime = longPressTime;
            tickAction.accept(tickData);
            if (ticksDown >= longPressTime) {
                longPressAction.accept(new ActionData(player, ticksDown, longPressTime));
                ticksDown = 0;
                isDown = false;
            }
        }
    }

    public void press() {
        if(!getKey().getKeyConflictContext().isActive()) return;
        isDown = true;
    }

    public void release() {
        if(!getKey().getKeyConflictContext().isActive()) return;
        releaseAction.accept(new ActionData(player, ticksDown, longPressTime));
        isDown = false;
        ticksDown = 0;
    }

    public void forceRelease() {
        releaseAction.accept(new ActionData(player, ticksDown, longPressTime));
        isDown = false;
        ticksDown = 0;
    }

    public void reset() {
        isDown = false;
        ticksDown = 0;
    }

    public boolean isDown() {
        return isDown;
    }

    public int getTicksDown() {
        return ticksDown;
    }

    public int getLongPressTime() {
        return longPressTime;
    }

    public void setLongPressTime(int longPressTime) {
        this.longPressTime = longPressTime;
    }

    public KeyMapping getKey() {
        return KEY;
    }

    public void setKey(KeyMapping KEY) {
        this.KEY = KEY;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
