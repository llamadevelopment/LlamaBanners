package net.lldv.llamabanners;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.lldv.llamabanners.commands.BannerCommand;
import net.lldv.llamabanners.components.api.API;
import net.lldv.llamabanners.components.forms.FormListener;
import net.lldv.llamabanners.components.forms.FormWindows;
import net.lldv.llamabanners.components.language.Language;
import net.lldv.llamabanners.listeners.EventListener;

public class LlamaBanners extends PluginBase {

    @Getter
    private static API api;

    @Getter
    private FormWindows formWindows;

    @Override
    public void onEnable() {
        try {
            this.saveDefaultConfig();
            api = new API(this);
            this.formWindows = new FormWindows(api);
            Language.init(this);
            this.loadPlugin();
            this.getLogger().info("§aLlamaBanners successfully started.");
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().error("§4Failed to load LlamaBanners.");
        }
    }

    private void loadPlugin() {
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FormListener(), this);

        this.getServer().getCommandMap().register("llamabanners", new BannerCommand(this));

        this.getConfig().getSection("BannerShop.PatternPrices").getAll().getKeys(false).forEach(s -> {
            api.patternPrices.put(s.toUpperCase(), this.getConfig().getDouble("BannerShop.PatternPrices." + s));
        });
    }

}