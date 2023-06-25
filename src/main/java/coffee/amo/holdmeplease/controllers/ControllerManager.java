package coffee.amo.holdmeplease.controllers;

import coffee.amo.holdmeplease.event.RegisterControllersEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class ControllerManager {
    private Player player;
    private Map<KeyMapping, DoubleTapController> DOUBLE_TAP_CONTROLLERS;
    private Map<KeyMapping, LongPressController> LONG_PRESS_CONTROLLERS;

    public void registerDoubleTapController(KeyMapping key, DoubleTapController controller) {
        DOUBLE_TAP_CONTROLLERS.put(key, controller);
    }

    public void registerLongPressController(KeyMapping key, LongPressController controller) {
        LONG_PRESS_CONTROLLERS.put(key, controller);
    }

    public Map<KeyMapping, DoubleTapController> getDoubleTapControllers() {
        return DOUBLE_TAP_CONTROLLERS;
    }

    public Map<KeyMapping, LongPressController> getLongPressControllers() {
        return LONG_PRESS_CONTROLLERS;
    }

    public void tick() {
        if(DOUBLE_TAP_CONTROLLERS == null) return;
        for (DoubleTapController tapController : DOUBLE_TAP_CONTROLLERS.values()) {
            if (tapController.hasStarted()) {
                tapController.tick();
            }
        }
        if(LONG_PRESS_CONTROLLERS == null) return;
        for (LongPressController longPressController : LONG_PRESS_CONTROLLERS.values()) {
            longPressController.tick();
        }
    }

    public ControllerManager() {
    }

    public Player getPlayer() {
        return player;
    }

    public void init(Player player) {
        this.player = player;
        RegisterControllersEvent event = new RegisterControllersEvent(player);
        MinecraftForge.EVENT_BUS.post(event);
        this.DOUBLE_TAP_CONTROLLERS = event.getDoubleTapControllers();
        this.LONG_PRESS_CONTROLLERS = event.getLongPressControllers();
    }

    public void reset() {
        for (DoubleTapController tapController : DOUBLE_TAP_CONTROLLERS.values()) {
            tapController.reset();
        }
    }

    private KeyMapping lastPressed = null;

    public void tap(KeyMapping key) {
        if(DOUBLE_TAP_CONTROLLERS == null) return;
        DOUBLE_TAP_CONTROLLERS.get(key).tap();
    }

    public void tap(int key) {
        if(DOUBLE_TAP_CONTROLLERS == null) return;
        DOUBLE_TAP_CONTROLLERS.forEach((k, v) -> {
            if (k.getKey().getValue() == key) {
                tap(k);
            }
        });
    }

    public void press(KeyMapping key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.get(key).press();
    }

    public void press(int key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.forEach((k, v) -> {
            if (k.getKey().getValue() == key) {
                press(k);
            }
        });
    }

    public void release(KeyMapping key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.get(key).release();
    }

    public void release(int key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.forEach((k, v) -> {
            if (k.getKey().getValue() == key) {
                release(k);
            }
        });
    }


    public void hold(int key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.forEach((k, v) -> {
            if (k.getKey().getValue() == key) {
                hold(k);
            }
        });
    }

    public void hold(KeyMapping key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.get(key).tick();
    }
}
