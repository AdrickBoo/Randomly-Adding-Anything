package io.github.vampirestudios.raa.api.interfaces;

import io.github.vampirestudios.raa.generation.dimensions.DimensionData;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public interface DimensionChunkGenerator {

    public ChunkGenerator<?> getChunkGenerator(World world, BiomeSource biomeSource, DimensionData data);
}