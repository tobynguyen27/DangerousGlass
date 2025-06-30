package dev.tobynguyen27.dangerousglass.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DGConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER.comment("Enables or disables the mod.").define("enabled", true);
    public static final ModConfigSpec.BooleanValue ENABLE_ARMOR_PROTECTION = BUILDER.comment("Wearing armor will protect players when breaking glass").define("armorProtection", true);
    public static final ModConfigSpec.IntValue RANGE = BUILDER.comment("Radius from the center that players will receive damage when stand in").defineInRange("range", 2, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
