package net.lldv.llamabanners.components.forms;

import cn.nukkit.Player;
import cn.nukkit.form.element.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.nbt.tag.CompoundTag;
import lombok.RequiredArgsConstructor;
import net.lldv.llamabanners.components.api.API;
import net.lldv.llamabanners.components.forms.custom.CustomForm;
import net.lldv.llamabanners.components.forms.simple.SimpleForm;
import net.lldv.llamabanners.components.language.Language;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.util.Arrays;

@RequiredArgsConstructor
public class FormWindows {

    private final API api;

    public void openBannerShop(final Player player) {
        final SimpleForm.Builder form = new SimpleForm.Builder(Language.getNP("ui.bannershop.title"), Language.getNP("ui.bannershop.content"));
        this.api.patternPrices.forEach((k, v) -> {
            form.addButton(new ElementButton(Language.getNP("ui.bannershop.pattern", this.api.convertPatternTypeToName(k.toLowerCase()), v),
                    new ElementButtonImageData("url", "http://system01.lldv.net:3000/img/llamabanners/llamabanners_" + k.toLowerCase() + ".png")), e -> {
                this.openConfirmBuyPattern(e, k.toLowerCase(), v);
            });
        });
        form.build().send(player);
    }

    public void openConfirmBuyPattern(final Player player, final String pattern, final double price) {
        final CustomForm form = new CustomForm.Builder(Language.getNP("ui.confirmbuy.title"))
                .addElement(new ElementLabel(Language.getNP("ui.confirmbuy.content", this.api.convertPatternTypeToName(pattern.toLowerCase()), price)))
                .addElement(new ElementDropdown(Language.getNP("ui.confirmbuy.selectcolor"), Arrays.asList("BLACK", "RED", "GREEN", "BROWN", "BLUE", "PURPLE", "CYAN", "LIGHT_GRAY", "GRAY", "PINK", "LIME", "YELLOW", "LIGHT_BLUE", "MAGENTA", "ORANGE", "WHITE"), 0))
                .addElement(new ElementToggle(Language.getNP("ui.confirmbuy.confirm"), false))
                .onSubmit((e, f) -> {
                    final String color = f.getDropdownResponse(1).getElementContent();
                    if (f.getToggleResponse(2)) {
                        final Item item = player.getInventory().getItemInHand();
                        if (item.getId() == ItemID.BANNER) {
                            if (item.getCount() == 1) {
                                if (LlamaEconomy.getAPI().getMoney(player.getName()) >= price) {
                                    LlamaEconomy.getAPI().reduceMoney(player.getName(), price);

                                    final Item newItem = Item.get(item.getId(), item.getDamage());
                                    String tag = "";
                                    if (item.hasCompoundTag()) {
                                        tag = item.getNamedTag().getString("llamabanners_patterns");
                                    }
                                    newItem.setNamedTag(new CompoundTag().putString("llamabanners_patterns", tag + pattern.toLowerCase() + ":" + color.toLowerCase() + "#"));
                                    player.getInventory().clear(player.getInventory().getHeldItemIndex(), true);
                                    player.getInventory().addItem(newItem);

                                    player.sendMessage(Language.get("pattern.bought", this.api.convertPatternTypeToName(pattern.toLowerCase()), price));
                                    player.sendMessage(Language.get("pattern.bought.info"));
                                } else player.sendMessage(Language.get("pattern.invalid.money"));
                            } else player.sendMessage(Language.get("pattern.invalid.item.count"));
                        } else player.sendMessage(Language.get("pattern.invalid.item"));
                    }
                })
                .build();
        form.send(player);
    }

}
