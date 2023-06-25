package dev.ryanhcode.zonai.movement;

import net.minecraft.client.KeyMapping;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class LongPressController {
    static class ActionData {
        private Player player;
        private int ticksDown;
        private int longPressTime;

        public ActionData(Player player, int ticksDown, int longPressTime) {
            this.player = player;
            this.ticksDown = ticksDown;
            this.longPressTime = longPressTime;
        }
    }
    private KeyMapping KEY;
    private Player player;
    private Consumer<Player> longPressAction;
    private Consumer<ActionData> releaseAction;
    private boolean isDown = false;
    private int ticksDown = 0;
    private int longPressTime;

    public LongPressController(KeyMapping KEY, Player player, Consumer<Player> longPressAction, Consumer<ActionData> releaseAction, int longPressTime) {
        this.KEY = KEY;
        this.player = player;
        this.longPressAction = longPressAction;
        this.longPressTime = longPressTime;
    }

    public void tick() {
        if (isDown) {
            ticksDown++;
            if (ticksDown >= longPressTime) {
                longPressAction.accept(player);
                ticksDown = 0;
            }
        }
    }

    public void press() {
        isDown = true;
    }

    public void release() {
        isDown = false;
        ticksDown = 0;
        releaseAction.accept(new ActionData(player, ticksDown, longPressTime));
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
