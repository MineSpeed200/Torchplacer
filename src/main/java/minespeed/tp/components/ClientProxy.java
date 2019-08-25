package minespeed.tp.components;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{

	public static final KeyBinding KEY = new KeyBinding("key.torchpl.desc",66 ,"key.categories.misc");
	
	@Override
	public void registerKeybinds() {
		ClientRegistry.registerKeyBinding(KEY);
	}
}
