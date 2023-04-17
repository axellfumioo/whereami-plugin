package lynx.whereami;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


import java.io.File;
import java.io.IOException;

public class Whereami extends Plugin {

    private String proxyName;
    private String regionName;

    @Override
    public void onEnable() {
        // Create a config.yml file if it doesn't already exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Failed to create config.yml!");
            }
        }

        // Get the configuration from the config.yml file
        Configuration config = getConfiguration();

        // Retrieve values from config.yml
        proxyName = config.getString("proxyName", "HSProxy-1");
        regionName = config.getString("regionName", "SGP");

        // Do something with the values retrieved from the config.yml
        getLogger().info("Proxy name: " + proxyName);
        getLogger().info("Region name: " + regionName);
        // Added the "/whereami", "/debug-whereami", and "/dev-whereami" commands.
        getProxy().getPluginManager().registerCommand(this, new WhereAmICommand("whereami"));
        getProxy().getPluginManager().registerCommand(this, new WhereAmICommand("debug-whereami"));
        getProxy().getPluginManager().registerCommand(this, new WhereAmICommand("dev-whereami"));
    }

    private class WhereAmICommand extends Command {

        public WhereAmICommand(String name) {
            super(name);
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            // Checks whether the command sender is a player
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage(ChatColor.RED + "Commands can only be performed by players.");
                return;
            }

            // Get the player who sent the command
            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Gets the name of the server where the player is located
            String serverName = player.getServer().getInfo().getName();

            // Create a message to be sent to the player
            String message = ChatColor.GREEN + "You are on the " + ChatColor.YELLOW + proxyName +
                    ChatColor.GREEN + " proxy, and you are on the " + ChatColor.YELLOW + serverName + ChatColor.GREEN + "server.";

            // Send message to the player
            player.sendMessage(message);
        }
    }

    private Configuration getConfiguration() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().severe("Failed to read config.yml! Try to reset it.");
            return null;
        }
    }
}
