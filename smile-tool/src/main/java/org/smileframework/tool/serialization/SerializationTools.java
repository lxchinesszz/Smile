package org.smileframework.tool.serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.ProtobufIOUtil;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * @Package: org.smileframework.tool.serialization
 * @Description:
 * @author: liuxin
 * @date: 2018/3/13 下午2:23
 */
public class SerializationTools {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationTools() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        //
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
//            return   ProtobufIOUtil.toByteArray(obj, schema, buffer);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = (T) objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 将C# 中的数组转成成java的数组
     * <p>
     * C# byte: [0~255]
     * Java byte: [-128~127]
     *
     * @param integers
     * @return
     */
    public static byte[] CToJavaByte(List<Integer> integers) {
        byte[] bytes = new byte[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            bytes[i] = (byte) (integers.get(i) - 256);
        }
        return bytes;
    }

    /**
     * 将java byte转成C#
     * @param bytes
     * @return
     */
    public static int[] JavaToCByte(byte[] bytes) {
        int[] ints = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                ints[i] = bytes[i] + 256;
            } else {
                ints[i] = bytes[i];
            }

        }
        return ints;
    }


    public static void main(String[] args) throws Exception{
//        byte[] bytes = serialize(new Student("小明", "12"));
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.print(Integer.toString(bytes[i], 16));
//        }
//        System.out.println();
//        String string = Integer.toString(13, 16);
//        System.out.println(string);


        /**
         * //TODO
         * C# -> Java
         */
        List<Integer> integers = Arrays.asList(10, 6, 229, 176, 143, 230, 152, 142, 16, 1);
        byte[] bytes1 = CToJavaByte(integers);

        System.out.println("C-Byte:10, 6, 229, 176, 143, 230, 152, 142, 16, 1");
        System.out.print("Java-Byte:");
        for (int i = 0; i < bytes1.length; i++) {
            System.out.print(bytes1[i] + ", ");
        }
        System.out.println();

        System.out.print("C-Byte:");
        Arrays.stream(JavaToCByte(bytes1)).forEach(x->{
            System.out.print(x+", ");
        });

        System.out.println();
        byte[] bytes = new byte[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            bytes[i] = (byte) (integers.get(i) - 256);
        }
        Person deserialize = deserialize(bytes, Person.class);
        System.out.println(deserialize);


        byte[] ps = serialize(deserialize);
        for (int i = 0; i < ps.length; i++) {
            System.out.print(ps[i] + ", ");
        }
        System.out.println();


        byte[] xs = serialize(new Person("销账", false));
        int[] ints = JavaToCByte(xs);
        System.out.println();
        for (int i = 0; i <ints.length ; i++) {
            System.out.print(ints[i]+",");
        }

        System.out.println();
        byte[] bytes2 = CToJavaByte(Arrays.asList(1, 0, 0, 0, 20, 0, 0, 0));
        System.out.println(new String(bytes2,"utf-8"));


        byte[] bytes3 = CToJavaByte(Arrays.asList(8, 0, 16, 0));
        System.out.println(new String(bytes3,"unicode"));

        byte[] bytes4 = "00".getBytes();

        byte[]bytes5=new byte[4];
        bytes5[0]=8;
        bytes5[1]=0;
        bytes5[2]=16;
        bytes5[3]=0;
        HeartBeatPackage deserialize1 = deserialize(bytes5, HeartBeatPackage.class);
        System.out.println(deserialize1);
    }
}
