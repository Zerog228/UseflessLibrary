package io.github.zerog228.usefless.data;

import io.github.zerog228.usefless.UseflessLibrary;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class PersistentData {

    //Has data
    public static <T extends PersistentDataHolder> boolean hasData(T entity, String key){
        return entity.getPersistentDataContainer().has(new NamespacedKey(UseflessLibrary.getPlugin(), key));
    }
    public static <T extends PersistentDataHolder> boolean hasData(T entity, String key, PersistentDataType type){
        return entity.getPersistentDataContainer().has(new NamespacedKey(UseflessLibrary.getPlugin(), key), type);
    }

    //Set data
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, String value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.STRING, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Integer value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Long value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.LONG, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Double value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.DOUBLE, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Boolean value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BOOLEAN, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Byte value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BYTE, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Short value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.SHORT, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setData(T t, String key, Float value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.FLOAT, value);
        return t;
    }

    public static <T extends PersistentDataHolder> PersistentDataHolder setDataF(T t, String key, Float value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.FLOAT, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setDataArrayB(T t, String key, byte[] value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BYTE_ARRAY, value);
        return t;
    }

    /*public static <T extends PersistentDataHolder, V extends Collection<? extends Byte>> void setDataArrayB(T t, String key, V value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(usefless.getPlugin(), key), PersistentDataType.BYTE_ARRAY, Bytes.toArray(value));
    }*/

    public static <T extends PersistentDataHolder> PersistentDataHolder setDataArrayI(T t, String key, int[] value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER_ARRAY, value);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setDataListI(T t, String key, List<Integer> value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        int[] intArray = new int[value.size()];
        IntStream.range(0, value.size()).forEach(i -> intArray[i] = value.get(i));

        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER_ARRAY, intArray);
        return t;
    }
    public static <T extends PersistentDataHolder> PersistentDataHolder setDataArrayL(T t, String key, long[] value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.LONG_ARRAY, value);
        return t;
    }


    //Remove data
    public static <T extends PersistentDataHolder> PersistentDataHolder removeData(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.remove(new NamespacedKey(UseflessLibrary.getPlugin(), key));
        return t;
    }

    //Get data
    public static <T extends PersistentDataHolder> Integer getDataI(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER);
    }
    public static <T extends PersistentDataHolder> Integer getDataI(T t, String key, Integer def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            Integer value = container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER);
            return value == null ? def : value;
        }catch (Exception ignored){
            return def;
        }
    }
    public static <T extends PersistentDataHolder> String getDataS(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.STRING);
    }
    public static <T extends PersistentDataHolder> String getDataS(T t, String key, String def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            String val = container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.STRING);
            return val == null ? def : val;
        }catch (Exception ignored){
            return def;
        }
    }
    public static <T extends PersistentDataHolder> Double getDataD(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.DOUBLE);
    }
    public static <T extends PersistentDataHolder> Double getDataD(T t, String key, double def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            Double val = container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.DOUBLE);
            return val == null ? def : val;
        }catch (Exception ignored){
            return def;
        }
    }
    public static <T extends PersistentDataHolder> Long getDataL(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.LONG);
    }
    public static <T extends PersistentDataHolder> Boolean getDataBo(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BOOLEAN);
    }
    public static <T extends PersistentDataHolder> Boolean getDataBo(T t, String key, boolean def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            Boolean val = container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BOOLEAN);
            return val == null ? def : val;
        }catch (Exception ignored){
            return def;
        }
    }
    public static <T extends PersistentDataHolder> Byte getDataBy(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BYTE);
    }
    public static <T extends PersistentDataHolder> Short getDataSh(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.SHORT);
    }
    public static <T extends PersistentDataHolder> Float getDataF(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.FLOAT);
    }
    public static <T extends PersistentDataHolder> Float getDataF(T t, String key, float def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            Float val = container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.FLOAT);
            return val == null ? def : val;
        }catch (Exception ignored){
            return def;
        }
    }
    public static <T extends PersistentDataHolder> byte[] getDataArrayB(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BYTE_ARRAY);
    }
    @Nullable
    public static <T extends PersistentDataHolder> int[] getDataArrayI(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER_ARRAY);
    }
    @Nullable
    public static <T extends PersistentDataHolder> List<Integer> getDataListI(T t, String key){
        try{
            return new ArrayList<>(Arrays.stream(PersistentData.getDataArrayI(t, key)).boxed().toList());
        }catch (Exception ignored){
            return null;
        }
    }
    public static <T extends PersistentDataHolder> long[] getDataArrayL(T t, String key){
        PersistentDataContainer container = t.getPersistentDataContainer();
        return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.LONG_ARRAY);
    }

    //Get or create default
    public static <T extends PersistentDataHolder, V extends Integer> Integer getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends String> String getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.STRING);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Long> Long getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.LONG);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Double> Double getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.DOUBLE);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Boolean> Boolean getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BOOLEAN);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Byte> Byte getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.BYTE);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Short> Short getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.SHORT);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }
    public static <T extends PersistentDataHolder, V extends Float> Float getOrCreateDefault(T t, String key, V def){
        PersistentDataContainer container = t.getPersistentDataContainer();
        try{
            return container.get(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.FLOAT);
        }catch (Exception ignored){
            setData(t, key, def);
            return def;
        }
    }

    public static <T extends PersistentDataHolder> PersistentDataHolder setDataI(T t, String key, Integer value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.INTEGER, value);
        return t;
    }

    public static <T extends PersistentDataHolder> PersistentDataHolder setDataD(T t, String key, Double value){
        PersistentDataContainer container = t.getPersistentDataContainer();
        container.set(new NamespacedKey(UseflessLibrary.getPlugin(), key), PersistentDataType.DOUBLE, value);
        return t;
    }

}
