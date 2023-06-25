package coffee.amo.holdmeplease.controllers;

import coffee.amo.holdmeplease.event.RegisterControllersEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class MovementController {
    private Player player;
    private Map<KeyMapping, DoubleTapController> DOUBLE_TAP_CONTROLLERS;
    private Map<KeyMapping, LongPressController> LONG_PRESS_CONTROLLERS;
    private int dashCooldown = 0;
    private static int DASH_COOLDOWN = 100;

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
        for (DoubleTapController dashController : DOUBLE_TAP_CONTROLLERS.values()) {
            if (dashController.hasStarted()) {
                dashController.tick();
            }
        }
        if (dashCooldown > 0) {
            dashCooldown--;
            System.out.println(dashCooldown);
        }
        if(LONG_PRESS_CONTROLLERS == null) return;
        for (LongPressController longPressController : LONG_PRESS_CONTROLLERS.values()) {
            longPressController.tick();
        }
    }

    public MovementController() {
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
        for (DoubleTapController dashController : DOUBLE_TAP_CONTROLLERS.values()) {
            dashController.reset();
        }
    }

    private KeyMapping lastPressed = null;

    public void tap(KeyMapping key) {
        DOUBLE_TAP_CONTROLLERS.get(key).tap();
    }

    public void press(KeyMapping key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.get(key).press();
    }

    public void release(KeyMapping key) {
        if(LONG_PRESS_CONTROLLERS == null) return;
        LONG_PRESS_CONTROLLERS.get(key).release();
    }

}
