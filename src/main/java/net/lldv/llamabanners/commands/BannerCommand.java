package net.lldv.llamabanners.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import net.lldv.llamabanners.LlamaBanners;
import net.lldv.llamabanners.components.language.Language;

public class BannerCommand extends PluginCommand<LlamaBanners> {

    public BannerCommand(LlamaBanners owner) {
        super(owner.getConfig().getString("Commands.Banner.Name"), owner);
        this.setDescription(owner.getConfig().getString("Commands.Banner.Description"));
        this.setPermission(owner.getConfig().getString("Commands.Banner.Permission"));
        this.setAliases(owner.getConfig().getStringList("Commands.Banner.Aliases").toArray(new String[]{}));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(this.getPermission())) {
                this.getPlugin().getFormWindows().openBannerShop(player);
            } else player.sendMessage(Language.get("permission.insufficient"));
        }
        return true;
    }

}
