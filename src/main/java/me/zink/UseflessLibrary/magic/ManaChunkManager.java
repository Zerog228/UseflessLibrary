package me.zink.UseflessLibrary.magic;

import me.zink.UseflessLibrary.data.PersistentData;
import me.zink.UseflessLibrary.util.TPair;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ManaChunkManager {

    protected static final int DEF_MANA = 100, DEF_ANTI_MANA = 10;
    protected static final String MANA_KEY = "mana", ANTI_MANA_KEY = "anti_mana", GENERATED_MANA_KEY = "mana_generated", CHUNK_MANA_UPDATED_KEY = "chunk_m_upd";
    protected static final int[][] NEIGHBOUR_CHUNK_CORDS = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            {0,  -1},          { 0, 1},
            {1,  -1}, { 1, 0}, { 1, 1}
    };
    //Mana flow chunk operation indexes
    protected static final int REC_M = 0, GIV_M = 1, REC_A = 2, GIV_A = 3, X_I = 0, Z_I = 1, UPDATED = 2;
    //Chunk key -> mana/a-mana

    protected static final ConcurrentHashMap<World, Map<Long, TPair<Float, Float>>> mana_buses = new ConcurrentHashMap<>();
    protected static final Random rand = new Random();

    //TODO Check if "isChunkLoaded()" is really needed
    protected static boolean ifSafeForOperations(World world, int x, int z){
        return world.isChunkLoaded(x, z) && world.isChunkGenerated(x, z);
    }

    public static boolean isChunkManaUpdated(Chunk chunk){
        return PersistentData.hasData(chunk, CHUNK_MANA_UPDATED_KEY);
    }

    public static boolean consumpMana(Chunk chunk, float mana){
        if (getCFMana(chunk) >= mana){
            addManaToBus(chunk, -mana);
            return true;
        }
        return false;
    }

    protected static void flagChunkManaUpdated(Chunk chunk){
        PersistentData.setData(chunk, CHUNK_MANA_UPDATED_KEY, "upd");
    }

    protected static void unflagChunkManaUpdated(Chunk chunk){
        PersistentData.removeData(chunk, CHUNK_MANA_UPDATED_KEY);
    }

    public static void genManaFromNeighbours(Chunk chunk) {
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            int processed_chunk_count = 0;
            float sum_mana = 0f, sum_anti_mana = 0f;
            Chunk neighbour;
            for (int[] cords : NEIGHBOUR_CHUNK_CORDS) {
                if (!ifSafeForOperations(chunk.getWorld(), chunk.getX() + cords[X_I], chunk.getZ() + cords[Z_I])) {
                    continue;
                }

                neighbour = chunk.getWorld().getChunkAt(chunk.getX() + cords[X_I], chunk.getZ() + cords[Z_I]); //This causes

                if (neighbour.isGenerated() && neighbour.isLoaded()) {
                    sum_mana += getMana(neighbour);
                    sum_anti_mana += getAntiMana(neighbour);
                    processed_chunk_count++;
                }
            }
            //If there are neighbours
            if (processed_chunk_count > 0) {
                genMana(chunk, sum_mana / processed_chunk_count, sum_anti_mana / processed_chunk_count);
            } else {
                genMana(chunk);
            }
        }
    }

    /**Resets mana in given chunk not counting on neighbours and anything else*/
    public static void unsafeGenMana(Chunk chunk){
        genMana(chunk);
    }

    protected static void genMana(Chunk chunk){
        removeChunkFromBus(chunk);
        Biome biome = chunk.getBlock(5, 100, 5).getBiome();

        PersistentData.setDataF(chunk, MANA_KEY, (float) ((DEF_MANA / rand.nextDouble(0.9, 1.1))));
        PersistentData.setDataF(chunk, ANTI_MANA_KEY, (float) ((DEF_ANTI_MANA / rand.nextDouble(0.8, 1.2))));
        PersistentData.setData(chunk, GENERATED_MANA_KEY, true);

        flagChunkManaUpdated(chunk);
    }

    /**Generates chunk with average between default random and defined values*/
    protected static void genMana(Chunk chunk, float mana, float antiMana){
        genMana(chunk);
        setMana(chunk, (getMana(chunk) + mana) / 2);
        setAntiMana(chunk, (getAntiMana(chunk) + antiMana) / 2);
    }

    /**Gets the immediate mana amount in the chunk (Current-Future) without waiting for the delay of manaBus to run.
     * Because the Actual amount of mana in the chunk is usually lower than the value from getCF(),
     * by using this method in some situations mana could be drained up to negative numbers.
     * But after manaBus runs mana level should return to positives.
     * (Server crash could clean manaBus and cause negative mana amounts in region)
     * But, at the same time, it fixes some bugs and inconsistencies so it should work
     */
    public static float getCFMana(Chunk chunk){
        float sum = 0;
        try{
            sum = mana_buses.get(chunk.getWorld()).get(chunk.getChunkKey()).getFirst();
        }catch (Exception ignored){}
        return sum + getMana(chunk);
    }

    protected static float getMana(Chunk chunk){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        //Some chunks were incorrectly populated with double tga so this section of excessive code is needed until new world will be generated
        try{
            return PersistentData.getDataF(chunk, MANA_KEY);
        }catch (Exception ignored){
            float value = PersistentData.getDataD(chunk, MANA_KEY).floatValue();
            PersistentData.setDataF(chunk, MANA_KEY, value);
            return value;
        }
    }

    /**Gets the immediate mana amount in the chunk (Current-Future) without waiting for the delay of manaBus to run.
     * Because the Actual amount of mana in the chunk is usually lower than the value from getCF(),
     * by using this method in some situations mana could be drained up to negative numbers.
     * But after manaBus runs mana level should return to positives.
     * (Server crash could clean manaBus and cause negative mana amounts in region)
     * But, at the same time, it fixes some bugs and inconsistencies so it should work
     */
    public static float getCFAntiMana(Chunk chunk){
        float sum = 0;
        try{
            sum = mana_buses.get(chunk.getWorld()).get(chunk.getChunkKey()).getSecond();
        }catch (Exception ignored){}
        return sum + getAntiMana(chunk);
    }

    protected static float getAntiMana(Chunk chunk){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        //Some chunks were incorrectly populated with double tga so this section of excessive code is needed until new world will be generated
        try{
            return PersistentData.getDataF(chunk, ANTI_MANA_KEY);
        }catch (Exception ignored){
            float value = PersistentData.getDataD(chunk, ANTI_MANA_KEY).floatValue();
            PersistentData.setDataF(chunk, ANTI_MANA_KEY, value);
            return value;
        }
    }

    protected static void addMana(Chunk chunk, float mana){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        PersistentData.setData(chunk, MANA_KEY, PersistentData.getDataF(chunk, MANA_KEY) + mana);
        flagChunkManaUpdated(chunk);
    }

    /**Adds mana to queue.
     * Instead of assigning values right away it adds mana to queue to be assigned.
     * It can increase performance when there are hundreds operations of adding mana per tick
     * */
    public static void addManaToBus(Chunk chunk, float mana){
        if(!mana_buses.containsKey(chunk.getWorld())){
            mana_buses.put(chunk.getWorld(), new ConcurrentHashMap<>(Map.of(0L, TPair.of(0F, 0F))));
        }

        Map<Long, TPair<Float, Float>> chunk_to_updates = mana_buses.get(chunk.getWorld());
        TPair<Float, Float> mana_values = chunk_to_updates.get(chunk.getChunkKey());
        if(mana_values != null){
            chunk_to_updates.put(chunk.getChunkKey(), mana_values.setFirst(mana_values.getFirst() + mana));
        }else {
            chunk_to_updates.put(chunk.getChunkKey(), TPair.of(mana, 0f));
        }
        mana_buses.put(chunk.getWorld(), chunk_to_updates);
    }

    protected static void addAntiMana(Chunk chunk, float antiMana){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        PersistentData.setData(chunk, ANTI_MANA_KEY, PersistentData.getDataF(chunk, ANTI_MANA_KEY) + antiMana);
        flagChunkManaUpdated(chunk);
    }

    /**Adds anti-mana to queue.
     * Instead of assigning values right away it adds mana to queue to be assigned.
     * It can increase performance when there are hundreds operations of adding mana per tick
     * */
    public static void addAntiManaToBus(Chunk chunk, float antiMana){
        if(!mana_buses.containsKey(chunk.getWorld())){
            mana_buses.put(chunk.getWorld(), new ConcurrentHashMap<>(Map.of(0L, TPair.of(0F, 0F))));
        }

        Map<Long, TPair<Float, Float>> chunk_to_updates = mana_buses.get(chunk.getWorld());
        TPair<Float, Float> mana_values = chunk_to_updates.get(chunk.getChunkKey());
        if(mana_values != null){
            chunk_to_updates.put(chunk.getChunkKey(), mana_values.setSecond(mana_values.getSecond() + antiMana));
        }else {
            chunk_to_updates.put(chunk.getChunkKey(), TPair.of(0f, antiMana));
        }
        mana_buses.put(chunk.getWorld(), chunk_to_updates);
    }

    protected static void setMana(Chunk chunk, float mana){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        PersistentData.setData(chunk, MANA_KEY, mana);
        flagChunkManaUpdated(chunk);

        removeChunkFromBus(chunk);
    }

    protected static void removeChunkFromBus(Chunk chunk){
        try{
            Map<Long, TPair<Float, Float>> chunk_to_updates = mana_buses.get(chunk.getWorld());
            if(chunk_to_updates == null){
                return;
            }
            chunk_to_updates.remove(chunk.getChunkKey());
            mana_buses.put(chunk.getWorld(), chunk_to_updates);
        }catch (Exception ignored){
            System.out.println("Failed to remove chunk from mana bus in world "+chunk.getWorld().getName()+" with cords ("+chunk.getX()+ " "+chunk.getZ()+")");
            //ignored.printStackTrace();
        }
    }

    protected static void setAntiMana(Chunk chunk, float antiMana){
        if(!PersistentData.hasData(chunk, GENERATED_MANA_KEY)){
            genManaFromNeighbours(chunk);
        }

        PersistentData.setData(chunk, ANTI_MANA_KEY, antiMana);
        flagChunkManaUpdated(chunk);

        clearAntiManaFromBus(chunk);
    }

    protected static void clearAntiManaFromBus(Chunk chunk){
        try{
            Map<Long, TPair<Float, Float>> chunk_to_updates = mana_buses.get(chunk.getWorld());
            if(chunk_to_updates == null){
                return;
            }
            chunk_to_updates.remove(chunk.getChunkKey());
            mana_buses.put(chunk.getWorld(), chunk_to_updates);
        }catch (Exception ignored){
            System.out.println("Failed to remove chunk from anti-mana bus in world "+chunk.getWorld().getName()+" with cords ("+chunk.getX()+ " "+chunk.getZ()+")");
            //ignored.printStackTrace();
        }
    }

    /**
     * Adds locally stored mana to related chunks.
     * This operation does not need to be run every tick but rather once in a while
     * to save all changes from RAM to world chunk containers
     * */
    public static void runManaBus(World world){
        mana_buses.get(world).forEach((cords, data) -> {
            addMana(world.getChunkAt(cords), data.getFirst());
            addAntiMana(world.getChunkAt(cords), data.getSecond());
        });
        mana_buses.remove(world);
    }

    /**Runs mana bus for every world available*/
    public static void runManaBuses(){
        for(World world : mana_buses.keySet()){
            runManaBus(world);
        }
    }

    /**Scans all neighbour chunks and transfers mana from one of them to central or wise-versa. Should be fired for each chunk separately.
     * @param chunk Chunk where event fired
     * @param effectiveness Effectiveness of mana transfer. Should be between 0 and 1
     * @param min_diff_in_percents Minimal difference between values to fire mana flow. >= 0
     * */
    public static void flow(Chunk chunk, float effectiveness, float min_diff_in_percents){
        effectiveness = Math.clamp(effectiveness, 0f, 1f);
        min_diff_in_percents = Math.clamp(min_diff_in_percents, 0f, Math.abs(min_diff_in_percents));
        if(effectiveness == 0){
            return;
        }

        //First index - operation type (1. Flow min-mana, 2. Flow max-mana, 3. Flow min-anti_mana, 4. Flow max-anti_mana)
        //Second index - data (1. X index, 2. Z index, 3. Updated)
        //You should probably start with receiving mana from the richest chunk to giving mana to the purest
        int[][] operations = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        //Fill operations
        //If filled at least 1 operation
        if(fillFlowOperations(chunk, operations, min_diff_in_percents, min_diff_in_percents)){

            //Firing operations
            fireFlowOperations(chunk, operations, effectiveness, effectiveness);
        }else {
            unflagChunkManaUpdated(chunk);
        }
    }

    /**Scans all neighbours and transfers mana from one of them to central or wise-versa. Should be fired for each chunk separately.
     * @param chunk Chunk where event fired
     * @param mana_eff Effectiveness of mana transfer. Should be between 0 and 1
     * @param anti_eff Effectiveness of anti-mana transfer. Should be between 0 and 1
     * @param min_mana_diff Minimal difference between values to fire mana flow. >= 0
     * @param min_anti_diff Minimal difference between values to fire anti-mana flow. >= 0
     * */
    public static void flow(Chunk chunk, float mana_eff, float anti_eff, float min_mana_diff, float min_anti_diff){
        mana_eff = Math.clamp(mana_eff, 0f, 1f);
        anti_eff = Math.clamp(anti_eff, 0f, 1f);
        if(mana_eff == anti_eff && anti_eff == 0){
            return;
        }

        min_mana_diff = Math.clamp(min_mana_diff, 0f, Math.abs(min_mana_diff));
        min_anti_diff = Math.clamp(min_anti_diff, 0f, Math.abs(min_anti_diff));

        int[][] operations = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        if(fillFlowOperations(chunk, operations, min_mana_diff, min_anti_diff)){
            fireFlowOperations(chunk, operations, mana_eff, anti_eff);
        }else {
            unflagChunkManaUpdated(chunk);
        }
    }

    protected static boolean fillFlowOperations(Chunk chunk, int[][] operations, float min_mana_diff, float min_anti_mana_diff){
        float min_mana, max_mana, min_anti_mana, max_anti_mana, curr_mana, curr_anti_mana;
        Chunk neighbour;
        min_mana = max_mana = getMana(chunk);
        min_anti_mana = max_anti_mana = getAntiMana(chunk);

        boolean updated = false;

        for(int[] offsets : NEIGHBOUR_CHUNK_CORDS){
            if (!ifSafeForOperations(chunk.getWorld(), chunk.getX() + offsets[X_I], chunk.getZ() + offsets[Z_I])) {
                continue;
            }

            neighbour = chunk.getWorld().getChunkAt(chunk.getX() + offsets[X_I], chunk.getZ() + offsets[Z_I]);
            if(neighbour.isGenerated() && neighbour.isLoaded()){
                curr_mana = getMana(neighbour);
                curr_anti_mana = getAntiMana(neighbour);

                //Even though it's possible that 2 or more chunks will have same min/max mana values, I would not account for such cases
                if(curr_mana < min_mana && (1 - curr_mana / min_mana) > min_mana_diff){
                    //Specifying chunk for receiving mana from central
                    min_mana = curr_mana;
                    operations[GIV_M][X_I] = offsets[X_I];
                    operations[GIV_M][Z_I] = offsets[Z_I];
                    operations[GIV_M][UPDATED] = 1;
                    updated = true;
                } else if (curr_mana > max_mana && (1 - max_mana / curr_mana) > min_mana_diff) {
                    max_mana = curr_mana;
                    operations[REC_M][X_I] = offsets[X_I];
                    operations[REC_M][Z_I] = offsets[Z_I];
                    operations[REC_M][UPDATED] = 1;
                    updated = true;
                }

                //Same but for anti-mana
                if(curr_anti_mana < min_anti_mana && (1 - curr_anti_mana / min_anti_mana) > min_anti_mana_diff){
                    //Specifying chunk for receiving mana from central
                    min_anti_mana = curr_anti_mana;
                    operations[GIV_A][X_I] = offsets[X_I];
                    operations[GIV_A][Z_I] = offsets[Z_I];
                    operations[GIV_A][UPDATED] = 1;
                    updated = true;
                } else if (curr_anti_mana > max_anti_mana && (1 - max_anti_mana / curr_anti_mana) > min_anti_mana_diff) {
                    max_anti_mana = curr_anti_mana;
                    operations[REC_A][X_I] = offsets[X_I];
                    operations[REC_A][Z_I] = offsets[Z_I];
                    operations[REC_A][UPDATED] = 1;
                    updated = true;
                }

                //If no operations for neighbour are found
                unflagChunkManaUpdated(neighbour);
            }
        }
        return updated;
    }

    protected static void fireFlowOperations(Chunk chunk, int[][] operations, float mana_eff, float anti_mana_eff){
        Chunk neighbour;
        float mana_diff;
        //Receive mana from neighbour
        if(mana_eff != 0){
            if (operations[REC_M][UPDATED] != 0) {
                //Giver
                neighbour = chunk.getWorld().getChunkAt(chunk.getX() + operations[REC_M][X_I], chunk.getZ() + operations[REC_M][Z_I]);
                mana_diff = (getMana(neighbour) - getMana(chunk)) * mana_eff;

                addMana(chunk, mana_diff);
                addMana(neighbour, -mana_diff);
            }

            //Give mana to neighbour
            if (operations[GIV_M][UPDATED] != 0) {
                //Receiver
                neighbour = chunk.getWorld().getChunkAt(chunk.getX() + operations[GIV_M][X_I], chunk.getZ() + operations[GIV_M][Z_I]);
                mana_diff = (getMana(chunk) - getMana(neighbour)) * mana_eff;

                addMana(chunk, -mana_diff);
                addMana(neighbour, mana_diff);
            }
        }

        //Receive anti-mana from neighbour
        if(anti_mana_eff != 0){
            if (operations[REC_A][UPDATED] != 0) {
                //Giver
                neighbour = chunk.getWorld().getChunkAt(chunk.getX() + operations[REC_A][X_I], chunk.getZ() + operations[REC_A][Z_I]);
                mana_diff = (getAntiMana(neighbour) - getAntiMana(chunk)) * anti_mana_eff;

                addAntiMana(chunk, mana_diff);
                addAntiMana(neighbour, -mana_diff);
            }

            //Give anti-mana to neighbour
            if (operations[GIV_A][UPDATED] != 0) {
                //Receiver
                neighbour = chunk.getWorld().getChunkAt(chunk.getX() + operations[GIV_A][X_I], chunk.getZ() + operations[GIV_A][Z_I]);
                mana_diff = (getAntiMana(chunk) - getAntiMana(neighbour)) * anti_mana_eff;

                addAntiMana(chunk, -mana_diff);
                addAntiMana(neighbour, mana_diff);
            }
        }
    }
}
