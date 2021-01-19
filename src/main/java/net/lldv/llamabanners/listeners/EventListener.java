package net.lldv.llamabanners.listeners;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBanner;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BannerPattern;
import cn.nukkit.utils.DyeColor;
import lombok.RequiredArgsConstructor;
import net.lldv.llamabanners.LlamaBanners;

import java.util.HashMap;

@RequiredArgsConstructor
public class EventListener implements Listener {

    private final LlamaBanners instance;

    @EventHandler
    public void on(final BlockPlaceEvent event) {
        final Block block = event.getBlock();
        final Item item = event.getItem();
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            final BlockEntity blockEntity = block.getLevel().getBlockEntity(block);
            if (block.getId() == BlockID.STANDING_BANNER) {
                if (item.getNamedTag() != null) {
                    final String rawPatterns = item.getNamedTag().getString("llamabanners_patterns");
                    if (rawPatterns == null || rawPatterns.isEmpty()) return;
                    final HashMap<String, String> patterns = new HashMap<>();
                    for (final String s : rawPatterns.split("#")) {
                        patterns.put(s.split(":")[0], s.split(":")[1]);
                    }
                    final BlockEntityBanner entityBanner = (BlockEntityBanner) blockEntity;
                    patterns.forEach((pattern, color) -> {
                        entityBanner.addPattern(new BannerPattern(BannerPattern.Type.valueOf(pattern.toUpperCase()), DyeColor.valueOf(color.toUpperCase())));
                    });
                    entityBanner.scheduleUpdate();
                    Server.getInstance().getOnlinePlayers().values().forEach(entityBanner::spawnTo);
                }
            }
        }, 10);
    }

}
