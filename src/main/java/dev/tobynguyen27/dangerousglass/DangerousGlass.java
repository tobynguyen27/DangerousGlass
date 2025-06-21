package dev.tobynguyen27.dangerousglass;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.slf4j.Logger;

@Mod(DangerousGlass.MODID)
public class DangerousGlass {
    public static final String MODID = "dangerousglass";

    private static final Logger LOGGER = LogUtils.getLogger();

    public DangerousGlass(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Glass is dangerous, don't break it by using your hands");
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        Vec3 playerPos = event.getPlayer().position();
        BlockPos blockPos = event.getPos();

        Player player = event.getPlayer();
        BlockState state = event.getLevel().getBlockState(blockPos);

        if (state.is(Tags.Blocks.GLASS_BLOCKS) || state.is(Tags.Blocks.GLASS_PANES) || state.is(Tags.Blocks.GLASS_BLOCKS_TINTED)) {
            if (isInRange(blockPos, playerPos) && (!isWearingFullArmorSet(player))) {
                MobEffectInstance blindnessEffect = new MobEffectInstance(MobEffects.BLINDNESS, 80, 1, false, false);
                MobEffectInstance poisonEffect = new MobEffectInstance(MobEffects.POISON, 75, 2, false, false);

                player.addEffect(blindnessEffect);
                player.addEffect(poisonEffect);
            }
        }
    }

    private boolean isWearingFullArmorSet(Player player) {
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET}) {
            if (player.getItemBySlot(slot).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private boolean isInRange(BlockPos blockPos, Vec3 playerPos) {
        int xX = blockPos.getX();
        int yY = blockPos.getY();
        int zZ = blockPos.getZ();

        int pX = (int) playerPos.x();
        int pY = (int) playerPos.y();
        int pZ = (int) playerPos.z();

        return ((pX - xX) ^ 2 + (pY - yY) ^ 2 + (pZ - zZ) ^ 2) < 4;
    }
}
