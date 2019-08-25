package minespeed.tp.components;

import java.util.function.Supplier;

import minespeed.tp.main.Event;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class TogPacket {
		public TogPacket(PacketBuffer buf){}
		public TogPacket(){}
		protected void encode(TogPacket p,PacketBuffer buf) {}
		protected void handle(TogPacket p,Supplier<NetworkEvent.Context>ctx) {
			ctx.get().enqueueWork(new Runnable() {
				
				@Override
				public void run() {
					Event.togglePlacing(ctx.get().getSender());
				}
			});
		}
}
