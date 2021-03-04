package dev.barfuzzle99.taplno99chunks.taplno99chunks;

/*
 *  Credits to DerFrZocker (https://www.spigotmc.org/members/derfrzocker.555696/)
 *  Code inspired by theirs posted on this thread:
 *  https://www.spigotmc.org/threads/how-to-clear-chunk.480916/
 */

import com.mojang.serialization.Codec;
import net.minecraft.server.v1_16_R3.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;

public class ChunkOverrider extends ChunkGenerator {

    private final static Method a;

    static {
        try {
            a = ChunkGenerator.class.getDeclaredMethod("a");
            a.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error getting declared method");
        }
    }

    @Nonnull
    private final ChunkGenerator parent;

    public ChunkOverrider(@Nonnull ChunkGenerator parent) {
        super(null, null);
        this.parent = parent;
    }

    boolean shouldGenerateChunk(IChunkAccess iChunkAccess) {
        ChunkCoordIntPair pos = iChunkAccess.getPos();
        return pos.hashCode() % 100 == 0; // Only generate 1% of the chunks
    }

    @Override
    public void buildBase(RegionLimitedWorldAccess regionLimitedWorldAccess, IChunkAccess iChunkAccess) {
        if (!shouldGenerateChunk(iChunkAccess)) return;
        parent.buildBase(regionLimitedWorldAccess, iChunkAccess);
    }

    @Override
    public void buildNoise(GeneratorAccess generatorAccess, StructureManager structureManager,
                           IChunkAccess iChunkAccess) {
        if (!shouldGenerateChunk(iChunkAccess)) return;
        parent.buildNoise(generatorAccess, structureManager, iChunkAccess);
    }

    @Override
    public void doCarving(long i, BiomeManager biomemanager, IChunkAccess iChunkAccess,
                          WorldGenStage.Features worldgenstage_features) {
        if (!shouldGenerateChunk(iChunkAccess)) return;
        parent.doCarving(i, biomemanager, iChunkAccess, worldgenstage_features);
    }

    @Override
    public void addDecorations(RegionLimitedWorldAccess regionLimitedWorldAccess, StructureManager structureManager) {
        parent.addDecorations(regionLimitedWorldAccess, structureManager);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Codec<? extends ChunkGenerator> a() {
        try {
            return (Codec<? extends ChunkGenerator>) a.invoke(parent);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error invoking getCarvingBiome", ex);
        }
    }

    @Override
    public int getBaseHeight(int i, int i1, HeightMap.Type type) {
        return parent.getBaseHeight(i, i1, type);
    }

    @Override
    public IBlockAccess a(int i, int i1) {
        return parent.a(i, i1);
    }

    @javax.annotation.Nullable
    @Override
    public BlockPosition findNearestMapFeature(WorldServer worldserver, StructureGenerator<?> structuregenerator,
                                               BlockPosition blockposition, int i, boolean flag) {
        return parent.findNearestMapFeature(worldserver, structuregenerator, blockposition, i, flag);
    }

    @Override
    public boolean a(ChunkCoordIntPair chunkcoordintpair) {
        return parent.a(chunkcoordintpair);
    }

    @Override
    public int b(int i, int j, HeightMap.Type heightmap_type) {
        return parent.b(i, j, heightmap_type);
    }

    @Override
    public int c(int i, int j, HeightMap.Type heightmap_type) {
        return parent.c(i, j, heightmap_type);
    }

    @Override
    public int getGenerationDepth() {
        return parent.getGenerationDepth();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Override
    public int getSpawnHeight() {
        return parent.getSpawnHeight();
    }

    @Override
    public List<BiomeSettingsMobs.c> getMobsFor(BiomeBase biomebase, StructureManager structuremanager,
                                                EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        return parent.getMobsFor(biomebase, structuremanager, enumcreaturetype, blockposition);
    }

    @Override
    public StructureSettings getSettings() {
        return parent.getSettings();
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
        parent.addMobs(regionlimitedworldaccess);
    }

    @Override
    public WorldChunkManager getWorldChunkManager() {
        return parent.getWorldChunkManager();
    }

    @Override
    public void createBiomes(IRegistry<BiomeBase> iregistry, IChunkAccess ichunkaccess) {
        parent.createBiomes(iregistry, ichunkaccess);
    }

    @Override
    public void createStructures(IRegistryCustom iregistrycustom, StructureManager structuremanager,
                                 IChunkAccess ichunkaccess, DefinedStructureManager definedstructuremanager, long i) {
        parent.createStructures(iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i);
    }

    @Override
    public void storeStructures(GeneratorAccessSeed generatoraccessseed, StructureManager structuremanager,
                                IChunkAccess ichunkaccess) {
        parent.storeStructures(generatoraccessseed, structuremanager, ichunkaccess);
    }
}
