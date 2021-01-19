package net.lldv.llamabanners.components.api;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import lombok.RequiredArgsConstructor;
import net.lldv.llamabanners.LlamaBanners;
import net.lldv.llamabanners.components.language.Language;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
public class API {

    private final LlamaBanners instance;

    public final LinkedHashMap<String, Double> patternPrices = new LinkedHashMap<>();

    public void buyBanner(final Player player, final String pattern) {
        Item item = player.getInventory().getItemInHand();
        if (item.getId() != ItemID.BANNER) {
            return;
        }

    }

    public String convertPatternTypeToName(final String type) {
        return Language.getNP("pattern." + type.toLowerCase());
    }

    public String convertPatternNameToType(final String name) {
        return null;
    }

}
