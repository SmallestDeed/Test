package com.sandu.search.common.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 序列化类
 *
 * @date 2018/3/13
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
public class SerializeUtil {

    /**
     * serialize Object
     *
     * @param object
     * @return byte[]
     */
    public static byte[] serialize(final Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        byte[] result;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.writeObject(null);
            result = baos.toByteArray();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * unserialize byte[]
     *
     * @param bytes
     * @return Object
     */
    public static Object unserialize(final byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * serialize hash Map<Object, Object>
     *
     * @param hash
     * @return Map<byte[], byte[]>
     */
    public static Map<byte[], byte[]> serializehmoo2mbb(Map<Object, Object> hash) {
        Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
        try {
            Set<Object> keys = hash.keySet();
            if (keys.size() > 0) {
                for (Object key : keys) {
                    result.put(serialize(key), serialize(hash.get(key)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * unserialize hash Map<byte[], byte[]>
     *
     * @param hash
     * @return Map<Object, Object>
     */
    public static Map<Object, Object> unserializehmbb2moo(final Map<byte[], byte[]> hash) {
        Map<Object, Object> result = new HashMap<Object, Object>();
        try {
            Set<byte[]> keys = hash.keySet();
            if (keys.size() > 0) {
                for (byte[] key : keys) {
                    result.put(unserialize(key), unserialize(hash.get(key)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * serialize hash Map<String, Object>
     *
     * @param hash
     * @return Map<byte[], byte[]>
     */
    public static Map<byte[], byte[]> serializehmso2mbb(final Map<String, Object> hash) {
        Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
        try {
            Set<String> keys = hash.keySet();
            if (keys.size() > 0) {
                for (String key : keys) {
                    result.put(serialize(key), serialize(hash.get(key)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * unserialize hash Map<byte[], byte[]>
     *
     * @param hash
     * @return Map<String, Object>
     */
    public static Map<String, Object> unserializehmbb2mso(final Map<byte[], byte[]> hash) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Set<byte[]> keys = hash.keySet();
            if (keys.size() > 0) {
                for (byte[] key : keys) {
                    Object unserializeKey = unserialize(key);
                    Object unserializeHashKey = unserialize(hash.get(key));
                    if (null != unserializeKey) {
                        result.put(unserializeKey.toString(), unserializeHashKey);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
