package io.github.vampirestudios.raa.registries;

import io.github.vampirestudios.raa.RandomlyAddingAnything;
import io.github.vampirestudios.raa.api.enums.GeneratesIn;
import io.github.vampirestudios.raa.api.enums.OreTypes;
import io.github.vampirestudios.raa.api.namegeneration.INameGenerator;
import io.github.vampirestudios.raa.blocks.LayeredOreBlock;
import io.github.vampirestudios.raa.blocks.RAABlock;
import io.github.vampirestudios.raa.generation.materials.Material;
import io.github.vampirestudios.raa.generation.materials.MaterialBuilder;
import io.github.vampirestudios.raa.items.*;
import io.github.vampirestudios.raa.utils.*;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Materials {
    public static final Set<Identifier> MATERIAL_IDS = new HashSet<>();
    public static final Registry<Material> MATERIALS = new DefaultedRegistry<>("materials");

    public static boolean ready = false;
    public static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    public static void init() {
        for (int a = 0; a < RandomlyAddingAnything.CONFIG.materialNumber; a++) {
            Color RGB = Rands.randColor();
            Random random = Rands.getRandom();
			INameGenerator nameGenerator = RandomlyAddingAnything.CONFIG.namingLanguage.getMaterialNameGenerator();

			String name;
			Identifier id;
			do {
                name = nameGenerator.generate();
                id = new Identifier(RandomlyAddingAnything.MOD_ID, nameGenerator.asId(name));
            } while (MATERIAL_IDS.contains(id));
			MATERIAL_IDS.add(id);

            Material material = MaterialBuilder.create(id, name)
                    .oreType(Rands.values(OreTypes.values()))
                    .color(RGB.getColor())
                    .generatesIn(Rands.values(GeneratesIn.values()))
                    .armor(random.nextBoolean())
                    .tools(Rands.chance(3))
                    .oreFlower(Rands.chance(4))
                    .weapons(Rands.chance(7))
                    .glowing(Rands.chance(4))
                    .minXPAmount(0)
                    .maxXPAmount(Rands.randIntRange(0, 4))
                    .oreClusterSize(Rands.randIntRange(2, 6))
                    .food(Rands.chance(4))
                    .build();

            Registry.register(MATERIALS, id, material);

            // Debug Only
            if (RandomlyAddingAnything.CONFIG.debug) {
                DebugUtils.materialDebug(material, RGB);
            }
        }
        ready = true;
    }

    public static boolean isReady() {
        return ready;
    }

    public static void createMaterialResources() {
        if (RandomlyAddingAnything.CONFIG.debug) {
            RegistryUtils.registerItem(new RAADebugItem(), new Identifier(RandomlyAddingAnything.MOD_ID, "debug_stick"));
        }

        MATERIALS.forEach(material -> {
            Identifier identifier = material.getId();
            Item repairItem;
            FabricBlockSettings blockSettings = FabricBlockSettings.copy(material.getOreInformation().getGeneratesIn().getBlock());

            Block baseBlock = material.getOreInformation().getGeneratesIn().getBlock();
            net.minecraft.block.Material baseBlockMaterial = baseBlock.getMaterial(baseBlock.getDefaultState());
            if(baseBlockMaterial == net.minecraft.block.Material.STONE) {
                blockSettings.breakByTool(FabricToolTags.PICKAXES, material.getMiningLevel());
            } else if (baseBlockMaterial == net.minecraft.block.Material.EARTH) {
                blockSettings.breakByTool(FabricToolTags.SHOVELS, material.getMiningLevel());
            } else {
                blockSettings.breakByHand(true);
            }

            RegistryUtils.register(
                new RAABlock(),
                Utils.append(identifier, "_block"),
                RandomlyAddingAnything.RAA_RESOURCES,
                material.getName(),
                RAABlockItem.BlockType.BLOCK
            );
            if (material.getOreInformation().getGeneratesIn() != GeneratesIn.DOES_NOT_APPEAR) {
                RegistryUtils.register(
                    new LayeredOreBlock(material, blockSettings.build()),
                    Utils.append(identifier, "_ore"),
                    RandomlyAddingAnything.RAA_ORES,
                    material.getName(),
                    RAABlockItem.BlockType.ORE);
            }
            if (material.getOreInformation().getOreType() == OreTypes.METAL) {
                RegistryUtils.registerItem(
                    repairItem = new RAASimpleItem(
                        material.getName(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.INGOT
                    ),
                    Utils.append(identifier, "_ingot")
                );
                RegistryUtils.registerItem(
                    new RAASimpleItem(
                        material.getName(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.NUGGET
                    ),
                    Utils.append(identifier, "_nugget")
                );
            } else if (material.getOreInformation().getOreType() == OreTypes.GEM) {
                RegistryUtils.registerItem(
                    repairItem = new RAASimpleItem(
                        material.getName(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.GEM
                    ),
                    Utils.append(identifier, "_gem")
                );
            } else {
                RegistryUtils.registerItem(
                    repairItem = new RAASimpleItem(
                        material.getName(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_RESOURCES),
                        RAASimpleItem.SimpleItemType.CRYSTAL
                    ),
                    Utils.append(identifier, "_crystal")
                );
            }
            if (material.hasArmor()) {
                RegistryUtils.registerItem(
                    new RAAArmorItem(
                        material,
                        EquipmentSlot.HEAD,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_helmet")
                );
                RegistryUtils.registerItem(
                    new RAAArmorItem(
                        material,
                        EquipmentSlot.CHEST,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_chestplate")
                );
                RegistryUtils.registerItem(
                    new RAAArmorItem(
                        material,
                        EquipmentSlot.LEGS,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_leggings")
                );
                RegistryUtils.registerItem(
                    new RAAArmorItem(
                        material,
                        EquipmentSlot.FEET,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_ARMOR).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_boots")
                );
                RegistryUtils.registerItem(
                    new RAAHorseArmorItem(material),
                    Utils.append(identifier, "_horse_armor")
                );
            }
            if (material.hasTools()) {
                RegistryUtils.registerItem(
                    new RAAPickaxeItem(
                        material,
                        material.getToolMaterial(),
                        1,
                        -2.8F,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_pickaxe")
                );
                RegistryUtils.registerItem(
                    new RAAAxeItem(
                        material,
                        material.getToolMaterial(),
                        5.0F + material.getToolMaterial().getAxeAttackDamage(),
                        -3.2F + material.getToolMaterial().getAxeAttackSpeed(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_axe")
                );
                RegistryUtils.registerItem(
                    new RAAShovelItem(
                        material,
                        material.getToolMaterial(),
                        1.5F,
                        -3.0F,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_shovel")
                );
                RegistryUtils.registerItem(
                    new RAAHoeItem(
                        material,
                        material.getToolMaterial(),
                        -3.0F + material.getToolMaterial().getHoeAttackSpeed(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_hoe")
                );
                RegistryUtils.registerItem(
                    new RAAShearItem(
                        material,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_TOOLS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_shears")
                );
            }
            if (material.hasWeapons()) {
                RegistryUtils.registerItem(
                    new RAASwordItem(
                        material,
                        new Item.Settings().group(RandomlyAddingAnything.RAA_WEAPONS).recipeRemainder(repairItem)
                    ),
                    Utils.append(identifier, "_sword")
                );
            }
            if (material.hasFood()) {
                RegistryUtils.registerItem(
                    new RAAFoodItem(
                        material.getName(),
                        new Item.Settings().group(RandomlyAddingAnything.RAA_FOOD).food(FoodComponents.GOLDEN_CARROT)
                    ),
                    Utils.append(identifier, "_fruit")
                );
            }
        });
    }

}
