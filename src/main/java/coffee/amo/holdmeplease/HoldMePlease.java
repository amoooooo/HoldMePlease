package coffee.amo.holdmeplease;

import coffee.amo.holdmeplease.controllers.ControllerManager;
import coffee.amo.holdmeplease.controllers.DoubleTapController;
import coffee.amo.holdmeplease.controllers.LongPressController;
import coffee.amo.holdmeplease.event.RegisterControllersEvent;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.extensions.IForgeKeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(HoldMePlease.MODID)
public class HoldMePlease {

    public static final String MODID = "holdmeplease";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ControllerManager CONTROLLER_MANAGER = new ControllerManager();

    public HoldMePlease() {
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if(CONTROLLER_MANAGER.getPlayer() == null && Minecraft.getInstance().player != null) {
                CONTROLLER_MANAGER.init(Minecraft.getInstance().player);
            }
            if(event.phase == TickEvent.Phase.START) {
                CONTROLLER_MANAGER.tick();
            }
        }

        @SubscribeEvent
        public static void onKeyPress(InputEvent.Key event){
            if(event.getAction() == InputConstants.PRESS) {
                CONTROLLER_MANAGER.press(event.getKey());
            }
            if(event.getAction() == InputConstants.RELEASE) {
                CONTROLLER_MANAGER.release(event.getKey());
                CONTROLLER_MANAGER.tap(event.getKey());
            }
            if(event.getAction() == InputConstants.REPEAT) {
                CONTROLLER_MANAGER.hold(event.getKey());
            }
        }
    }
}
