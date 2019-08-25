package minespeed.tp.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import minespeed.tp.components.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(Torchplacer.MODID)
public class Torchplacer {

	public static final String MODID = "torchpl";

	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static final Event EVENTHANDLER = new Event();
	

	public static CommonProxy PROXY = DistExecutor.runForDist(()->ClientProxy::new, ()->CommonProxy::new);
		
	
	public Torchplacer() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EVENTHANDLER);
		TorchplacerPackageHandler.register();
	}
	
	@SubscribeEvent
	public void clientInit(FMLClientSetupEvent event) {
		PROXY.registerKeybinds();
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		CommandTorch.register(event.getCommandDispatcher());
	}
}
