package fr.arthurbambou.randomlyaddinganything;

import fr.arthurbambou.randomlyaddinganything.api.enums.AppearsIn;
import fr.arthurbambou.randomlyaddinganything.config.Config;
import fr.arthurbambou.randomlyaddinganything.config.SavingSystem;
import fr.arthurbambou.randomlyaddinganything.materials.Material;
import fr.arthurbambou.randomlyaddinganything.registries.Materials;
import fr.arthurbambou.randomlyaddinganything.registries.Textures;
import fr.arthurbambou.randomlyaddinganything.world.gen.feature.OreFeature;
import fr.arthurbambou.randomlyaddinganything.world.gen.feature.OreFeatureConfig;
import fr.arthurbambou.randomlyaddinganything.world.gen.feature.SimpleRangeDecoratorConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;

public class RandomlyAddingAnything implements ModInitializer {

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("raa", "itemgroup"), () -> new ItemStack(Items.FIREWORK_ROCKET));
	public static final String MOD_ID = "raa";
	public static final Config CONFIG = new Config();

	@Override
	public void onInitialize() {
		Textures.init();
		if (SavingSystem.init()) {
			Materials.init();
			SavingSystem.createFile();
		} else {
			SavingSystem.readFile();
		}
		Materials.createMaterialResources();
		Registry.BIOME.forEach(biome -> {
			for (Material material : Materials.MATERIAL_LIST) {
				if (material.getOreInformation().getGenerateIn() == AppearsIn.SAND_DESERT) {
					if (biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS || biome == Biomes.DESERT_LAKES) {
						biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(
						        new OreFeature(OreFeatureConfig::deserialize), new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE,
                                        Registry.BLOCK.get(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ore")).getDefaultState(), 9),
                                        Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(20, 30, 256)));
					}
				}
				if (material.getOreInformation().getGenerateIn() == AppearsIn.SAND_ANY) {
					if (biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS || biome == Biomes.DESERT_LAKES || biome == Biomes.BEACH) {
						biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(
								new OreFeature(OreFeatureConfig::deserialize), new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE,
										Registry.BLOCK.get(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ore")).getDefaultState(), 9),
								Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(20, 30, 256)));
					}
				}
				if (material.getOreInformation().getGenerateIn() == AppearsIn.NETHERRACK) {
					if (biome == Biomes.NETHER) {
						biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(
								new OreFeature(OreFeatureConfig::deserialize), new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE,
										Registry.BLOCK.get(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ore")).getDefaultState(), 9),
								Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(20, 30, 256)));
					}
				}
				if (material.getOreInformation().getGenerateIn() == AppearsIn.END_STONE) {
					if (biome == Biomes.END_BARRENS || biome == Biomes.END_HIGHLANDS || biome == Biomes.END_MIDLANDS || biome == Biomes.THE_END || biome == Biomes.SMALL_END_ISLANDS) {
						biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(
								new OreFeature(OreFeatureConfig::deserialize), new OreFeatureConfig(OreFeatureConfig.Target.END_STONE,
										Registry.BLOCK.get(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ore")).getDefaultState(), 9),
								Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(20, 30, 256)));
					}
				}
				if (material.getOreInformation().getGenerateIn() == AppearsIn.SAND_BEACH) {
					if (biome == Biomes.BEACH ) {
                        biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Biome.configureFeature(
                                new OreFeature(OreFeatureConfig::deserialize), new OreFeatureConfig(OreFeatureConfig.Target.SAND,
                                        Registry.BLOCK.get(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ore")).getDefaultState(), 9),
                                Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(20, 30, 256)));
					}
				}
			}
		});
	}
}