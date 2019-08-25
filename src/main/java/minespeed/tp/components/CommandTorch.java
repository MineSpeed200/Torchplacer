package minespeed.tp.components;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import minespeed.tp.main.Event;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public final class CommandTorch {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("torch")
				.requires((cmdSource) -> {
					try {
						return cmdSource.asPlayer().isAddedToWorld();
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
						return false;
			}	        
				}).executes((player) ->{ return Event.togglePlacing(player.getSource().asPlayer());}
						
						));
	}
}
