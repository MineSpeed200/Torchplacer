package minespeed.tp.main;

import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.maven.artifact.versioning.ComparableVersion;

import com.mojang.realmsclient.gui.ChatFormatting;

import minespeed.tp.components.ClientProxy;
import minespeed.tp.components.TogPacket;
import minespeed.tp.components.TorchplacerPackageHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;

public class Event {

	private static ArrayList<ServerPlayerEntity>activePlayers = new ArrayList<ServerPlayerEntity>();
	
	private static boolean shown = false;
	
	public static int togglePlacing(ServerPlayerEntity player) {
		if(!player.world.isRemote) {
		if(activePlayers.contains(player)) {
			activePlayers.remove(player);		
			player.sendMessage(new StringTextComponent("Torchplacing disabled!"));
		}else {
			activePlayers.add(player);
			player.sendMessage(new StringTextComponent("Torchplacing enabled!"));
		}
		}
		
		return 0;
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		for (ServerPlayerEntity mp:activePlayers) {
		BlockPos c = mp.getPosition();
		float light = mp.getEntityWorld().getLight(c);
		Torchplacer.LOGGER.log(Level.DEBUG,"First Light Level:"+light+" Side:");
		if(light<8) {
			Torchplacer.LOGGER.log(Level.DEBUG,"Light Level:"+light);

			if(!mp.isInWater()&&!mp.isInLava()) {
				if(mp.inventory.hasItemStack(new ItemStack(Blocks.TORCH))){
					int x=Math.round(c.getX());
					int y=Math.round(c.getY());
					int z=Math.round(c.getZ());
					if(!Block.func_220055_a(mp.world,new BlockPos(x, y, z),Direction.DOWN)&&mp.world.getFluidState(new BlockPos(x,y,z)).isEmpty()&&Block.func_220055_a(mp.world,new BlockPos(x, y-1, z),Direction.DOWN)&&mp.getEntityWorld().getBlockState(new BlockPos(x, y, z))!=Blocks.TORCH.getDefaultState()) {
					mp.getEntityWorld().setBlockState(new BlockPos(x, y, z), Blocks.TORCH.getDefaultState());
					int index =getTorchStackSlot(mp.inventory);
					if(index==-1) {
						if(mp.getHeldItemOffhand().isItemEqual(new ItemStack(Blocks.TORCH)))
								mp.getHeldItemOffhand().setCount(mp.getHeldItemOffhand().getCount()-1);
					}else {
						mp.inventory.decrStackSize(index, 1);
						 
					}
					}
				
				}
			}
		}
		}
		}
	
	private int getTorchStackSlot(PlayerInventory inven) {
		for(int i=0;i<27;i++) {
			if(ItemStack.areItemsEqual(inven.getStackInSlot(i),new ItemStack(Blocks.TORCH))){
				return i;
			}
		}
		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if(!shown && Minecraft.getInstance().currentScreen==null) {
			CheckResult resoult =VersionChecker.getResult(ModList.get().getModContainerById(Torchplacer.MODID).get().getModInfo());
			switch (resoult.status) {
			case FAILED:{Torchplacer.LOGGER.info("Torchplacer Version check failed!"); Minecraft.getInstance().player.sendMessage(new StringTextComponent(ChatFormatting.DARK_RED+"Torchplacer was unable to check for updates!\n Either somethings wrong with your Internet connection or i messed up my update file."));shown=true;}break;
			case OUTDATED:{Torchplacer.LOGGER.info("Torchplacer update available!\n"+resoult.url+"\n"+"Changes:"+getChanges(resoult.changes)); Minecraft.getInstance().player.sendMessage(new StringTextComponent(ChatFormatting.AQUA+"Torchplacer update available!\n"+resoult.url+"\n"+"Changes:"+getChanges(resoult.changes)));shown=true;};break;
			case BETA:{Torchplacer.LOGGER.info("You are using a beta version i can't guarantee for anything! If you encounter any bugs please report them at https://github.com/MineSpeed200/Torchplacer/issues");Minecraft.getInstance().player.sendMessage(new StringTextComponent(ChatFormatting.GREEN+"You are using a beta version, i can't guarantee for anything!\n If you encounter any Bugs please report them at https://github.com/MineSpeed200/Torchplacer/issues"));shown=true;}break;
			case BETA_OUTDATED:{Torchplacer.LOGGER.info("Torchplacer beta update available!\n"+resoult.url+"\n"+"Changes:"+getChanges(resoult.changes)); Minecraft.getInstance().player.sendMessage(new StringTextComponent(ChatFormatting.AQUA+"Torchplacer beta update available!\n"+resoult.url+"\n"+"Changes:"+getChanges(resoult.changes)));shown=true;}
			default:
				break;
			}
		}
	}
	
	private String getChanges(Map<ComparableVersion, String>chan) {
		String changes="";
		for(String s:chan.values()) {
			changes+=s;
		}
		return changes;
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void keyhandler(InputEvent.KeyInputEvent event){
		if(ClientProxy.KEY.isPressed()) {
			TorchplacerPackageHandler.INSTANCE.sendToServer(new TogPacket());
		}
	}
}
