package minespeed.tp.components;

import java.util.function.BiConsumer;

import minespeed.tp.main.Torchplacer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class TorchplacerPackageHandler  {
	private static final String PROTOCOL_VERSION="1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Torchplacer.MODID,"components"), ()->PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void register() {
		TogPacket p = new TogPacket();
		INSTANCE.registerMessage(0, TogPacket.class,(BiConsumer<TogPacket, PacketBuffer>)p::encode,TogPacket::new,p::handle);
	}
	
}
