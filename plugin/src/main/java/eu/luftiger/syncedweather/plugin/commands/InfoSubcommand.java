package eu.luftiger.syncedweather.plugin.commands;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class InfoSubcommand {

    private final SyncedWeather plugin;

    public InfoSubcommand(SyncedWeather plugin){
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args){
        PluginDescriptionFile descriptionFile = plugin.getDescription();

        sender.sendMessage("§8§m]§7§m------§8§m[§r §aPlugin - Info§r §8§m]§7§m------§8§m[");
        sender.sendMessage("§bName§7» §f" + descriptionFile.getName());
        sender.sendMessage("§bVersion§7» §f" + descriptionFile.getVersion());
        sender.sendMessage("§bAuthor§7» §f" + descriptionFile.getAuthors());
        sender.sendMessage(" ");

        if(sender instanceof Player){
            Player player = (Player) sender;

            TextComponent clickComponent = new TextComponent("§8[§6Spigot§8]");
            clickComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§fclick to go to the spigot page")));
            clickComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/syncedweather.97574/"));

            player.spigot().sendMessage(clickComponent);
        }else {
            sender.sendMessage("https://www.spigotmc.org/resources/syncedweather.97574/");
        }

        sender.sendMessage("§8§m]§7§m------§8§m[§r §aPlugin - Info§r §8§m]§7§m------§8§m[");
    }
}
