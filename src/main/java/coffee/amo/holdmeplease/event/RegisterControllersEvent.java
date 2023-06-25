package coffee.amo.holdmeplease.event;

import coffee.amo.holdmeplease.HoldMePlease;
import coffee.amo.holdmeplease.controllers.DoubleTapController;
import coffee.amo.holdmeplease.controllers.LongPressController;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterControllersEvent extends Event {
    private Map<KeyMapping, LongPressController> LONG_PRESS_CONTROLLERS = new HashMap<>();
    private Map<KeyMapping, DoubleTapController> DOUBLE_TAP_CONTROLLERS = new HashMap<>();
    private Player player;

    public RegisterControllersEvent(Player player) {
        this.player = player;
    }

    public void registerDoubleTapController(KeyMapping key, DoubleTapController controller) {
        HoldMePlease.LOGGER.info("Registering double tap controller for key " + key.getName());
        DOUBLE_TAP_CONTROLLERS.put(key, controller);
    }

    public void registerLongPressController(KeyMapping key, LongPressController controller) {
        HoldMePlease.LOGGER.info("Registering long press controller for key " + key.getName());
        LONG_PRESS_CONTROLLERS.put(key, controller);
    }

    public Map<KeyMapping, LongPressController> getLongPressControllers() {
        return LONG_PRESS_CONTROLLERS;
    }

    public Map<KeyMapping, DoubleTapController> getDoubleTapControllers() {
        return DOUBLE_TAP_CONTROLLERS;
    }

    public Player getPlayer() {
        return player;
    }
}
