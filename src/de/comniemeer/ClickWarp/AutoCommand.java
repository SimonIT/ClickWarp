package de.comniemeer.ClickWarp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AutoCommand<P extends JavaPlugin> extends Command {

	/*
	 * AutoCommand method by PostCrafter Visit his YouTube channel:
	 * http://www.youtube.com/PostCrafter Visit his forum:
	 * http://postcrafter.de/
	 */

	private static String VERSION;

	static {
		String path = Bukkit.getServer().getClass().getPackage().getName();

		AutoCommand.VERSION = path.substring(path.lastIndexOf(".") + 1);

		System.out.println("[ClickWarp] AutoCommand hook for Bukkit " + AutoCommand.VERSION + " by PostCrafter");
		System.out.println(
				"[ClickWarp] Visit his YouTube channel for very good german video tutorials: http://www.youtube.com/PostCrafter");
		System.out.println("[ClickWarp] Visit his german forum for help with Bukkit-plugins: http://postcrafter.de/");
	}

	protected final P plugin;
	protected final String command;

	public AutoCommand(P plugin, String command, String description, String... aliases) {
		super(command);
		this.plugin = plugin;
		this.command = command;

		super.setDescription(description);
		List<String> aliasList = new ArrayList<>(Arrays.asList(aliases));
		super.setAliases(aliasList);

		this.register();
	}

	private void register() {
		try {
			Field f = Class.forName("org.bukkit.craftbukkit." + AutoCommand.VERSION + ".CraftServer")
					.getDeclaredField("commandMap");
			f.setAccessible(true);

			CommandMap map = (CommandMap) f.get(Bukkit.getServer());
			map.register(this.plugin.getName(), this);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public abstract boolean execute(@NotNull CommandSender cs, @NotNull String label, @NotNull String[] args);

	@NotNull
	public abstract List<String> tabComplete(@NotNull CommandSender cs, @NotNull String label, @NotNull String[] args);

	@NotNull
	public String buildString(@NotNull String[] args, int start) {
		StringBuilder str = new StringBuilder();
		if (args.length > start) {
			str.append(args[start]);
			for (int i = start + 1; i < args.length; i++) {
				str.append(" ").append(args[i]);
			}
		}
		return str.toString();
	}

	public P getPlugin() {
		return this.plugin;
	}
}
