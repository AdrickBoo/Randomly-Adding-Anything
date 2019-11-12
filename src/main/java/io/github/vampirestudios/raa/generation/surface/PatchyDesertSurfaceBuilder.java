package io.github.vampirestudios.raa.generation.surface;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class PatchyDesertSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static final OctaveSimplexNoiseSampler DESERT_NOISE = new OctaveSimplexNoiseSampler(new ChunkRandom(79L), 6, 0);
    public PatchyDesertSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> function_1) {
        super(function_1);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState state, BlockState state2, int int1, long long1, TernarySurfaceConfig config) {
        double desertNoise = DESERT_NOISE.sample(x*0.049765625D, z*0.049765625D, false);
//        if (SHATTERED_NOISE.sample(x*0.049765625D, height*0.049765625D, false) > 0.0D) {
//            SurfaceBuilder.SHATTERED_SAVANNA.generate(rand, chunk, biome, x, height, z, noise, state, state2, int1, long1, SurfaceBuilder.GRASS_CONFIG);
//        } else if (GRAVEL_NOISE.sample(x*0.049765625D, height*0.049765625D, false) > 0.0D) {
//            SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, height, z, noise, state, state2, int1, long1, SurfaceBuilder.GRAVEL_CONFIG);
//        }
         if (desertNoise > 0.0D) {
            SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, z, height, noise, state, state2, int1, long1, SurfaceBuilder.SAND_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, z, height, noise, state, state2, int1, long1, config);
        }
    }
}
