import org.junit.Test;

import java.util.*;
import java.util.function.*;

/**
 * Collection：removeIf() spliterator() stream() parallelStream() forEach()
 * List：replaceAll() sort()
 * Map：getOrDefault() forEach() replaceAll() putIfAbsent() remove() replace() computeIfAbsent() computeIfPresent() compute() merge()
 * 这些新加入的方法大部分要用到java.util.function包下的接口，分类如下：
 */
public class NewInterfaceAndFunction {

    @Test
    public void test() {
        // 1.Collection接口（Set、List和Queue的父接口）
        // 1.1 void forEach(Consumer<? super E> action)，作用是对容器中的每个元素执行action指定的动作，其中Consumer是个函数接口，里面只有一个待实现方法void accept(T t)
        // 需求：假设有一个字符串列表，需要打印出其中所有长度大于3的字符串
        // 1.1.1 Java7及以前我们可以用增强的for循环实现
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for (String str : list) {
            if (str.length() > 3) {
                System.out.println(str);
            }
        }
        // 1.1.2 使用forEach()方法结合匿名内部类实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String str) {
                if (str.length() > 3) {
                    System.out.println(str);
                }
            }
        });
        // 1.1.3 使用forEach()方法结合Lambda表达式实现，类型推导帮我们做了一切：
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.forEach(str -> {
            if (str.length() > 3) {
                System.out.println(str);
            }
        });

        // 1.2 boolean removeIf(Predicate<? super E> filter)，作用是删除容器中所有满足filter指定条件的元素，其中Predicate是一个函数接口，里面只有一个待实现方法boolean test(T t)
        // 需求：假设有一个字符串列表，需要删除其中所有长度大于3的字符串
        // 如果需要在迭代过程冲对容器进行删除操作必须使用迭代器，否则会抛出ConcurrentModificationException
        // 1.2.1 使用迭代器删除列表元素
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().length() > 3) {
                it.remove();
            }
        }
        // 1.2.2 使用removeIf()方法结合匿名内部类实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return str.length() > 3;
            }
        });
        // 1.2.3 使用removeIf()结合Lambda表达式实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.removeIf(str -> str.length() > 3);

        // 1.3 Spliterator<E> spliterator()，该方法返回容器的可拆分迭代器，该方法跟iterator()方法有点像，Spliterator也有类似迭代容器的作用，但二者有如下不同
        // (1) Spliterator既可以像Iterator那样逐个迭代，也可以批量迭代，批量迭代可以降低迭代的开销
        // (2) Spliterator可拆分，可以通过Spliterator<T> trySplit()方法来尝试分成两个迭代器，一个是this，另一个是新返回的（元素无重叠）；可多次调用trySplit()方法来分解负载，以便多线程处理

        // 1.4 stream()和parallelStream()，分别返回容器的Stream视图表示，不同点在于parallelStream()返回并行的Stream，Stream是Java函数式编程的核心类

        // 2.List接口
        // 2.1 void replaceAll(UnaryOperator<E> operator)，作用是对每个元素执行operator指定的操作，并用操作结果来替换原来的元素，其中UnaryOperator是一个函数接口，里面只有一个待实现函数T apply(T t)
        // 需求：假设有一个字符串列表，将其中所有长度大于3的元素转换成大写，其余元素不变
        // 2.1.1 使用下标实现元素替换
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (str.length() > 3) {
                list.set(i, str.toUpperCase());
            }
        }
        // 2.1.2 使用replaceAll()方法结合匿名内部类实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.replaceAll(new UnaryOperator<String>() {
            @Override
            public String apply(String str) {
                if (str.length() > 3) {
                    return str.toUpperCase();
                }
                return str;
            }
        });
        // 2.1.3 使用Lambda表达式实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.replaceAll(str -> {
            if (str.length() > 3) {
                return str.toUpperCase();
            }
            return str;
        });

        // 2.2 void sort(Comparator<? super E> c)，该方法根据c指定的比较规则对容器元素进行排序。Comparator接口我们并不陌生，其中有一个方法int compare(T o1, T o2)需要实现，显然该接口是个函数接口
        // 需求：假设有一个字符串列表，按照字符串长度增序对元素排序
        // 2.2.1 使用Collections.sort()方法实现，Java7及以前sort()方法在Collections工具类中
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.length() - str2.length();
            }
        });
        // 2.2.2 Java8可以直接使用List.sort()方法，结合Lambda表达式实现
        list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.sort((str1, str2) -> str1.length() - str2.length());

        // 3.Map接口
        // 3.1 void forEach(BiConsumer<? super K,? super V> action)，作用是对Map中的每个映射执行action指定的操作，其中BiConsumer是一个函数接口，里面有一个待实现方法void accept(T t, U u)
        // 需求：假设有一个数字到对应英文单词的Map，请输出Map中的所有映射关系
        // 3.1.1 Java7及以前迭代Map
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        // 3.1.2 使用forEach()结合匿名内部类实现
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer k, String v) {
                System.out.println(k + "=" + v);
            }
        });
        // 使用forEach()结合Lambda表达式实现
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.forEach((k, v) -> System.out.println(k + "=" + v));

        // 3.2 V getOrDefault(Object key, V defaultValue)，按照给定的key查询Map中对应的value，如果没有找到则返回defaultValue；该方法跟Lambda表达式没关系，但是很有用
        // 需求：假设有一个数字到对应英文单词的Map，输出4对应的英文单词，如果不存在则输出NoValue
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        // Java7及以前实现
        if (map.containsKey(4)) {
            System.out.println(map.get(4));
        } else {
            System.out.println("NoValue");
        }
        // Java8使用Map.getOrDefault()实现
        System.out.println(map.getOrDefault(4, "NoValue"));
        // 3.3 V putIfAbsent(K key, V value)，将条件判断和赋值合二为一，仅当key的映射值不存在或为null时，才将value值放入Map中，否则不做更改；该方法跟Lambda表达式没关系，但是很有用
        // 3.4 remove(Object key, Object value)，Java8新增，仅当Map中key正好映射到value时才删除该映射，否则什么也不做；Map在Java7中已经有一个remove(Object key)方法，删除Map中指定key的映射关系
        // 3.5 replace方法；Map在Java7中，要可通过put(K key, V value) 方法替换映射值，该方法总是会用新值替换原来的值，为了更精确的控制替换行为，Java8加入了两个replace方法
        // (1) V replace(K key, V value)，仅当Map中key的映射存在时才用value去替换原来的值，否则什么也不做  
        // (2) boolean replace(K key, V oldValue, V newValue)，仅当Map中key的映射存在且等于oldValue时，才用newValue去替换原来的值，否则什么也不做
        // 3.6 void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
        // 作用是对Map中的每个映射执行function指定的操作，并用function的执行结果替换原来的value；其中BiFunction是一个函数接口，里面有一个待实现方法R apply (K k, V v)
        // 需求：假设有一个数字到对应英文单词的Map，请将原来映射关系中的单词都转换成大写
        // 3.6.1 Java7的常规实现
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            entry.setValue(entry.getValue().toUpperCase());
        }
        // 3.6.2 使用replaceAll()结合匿名内部类实现
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.replaceAll(new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer k, String v) {
                return v.toUpperCase();
            }
        });
        // 3.6.3 使用replaceAll()结合Lambda表达式实现
        map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.replaceAll((k, v) -> v.toUpperCase());
        // 3.7 V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
        // (1) 如果Map中key对应的映射不存在或者为null时，将value（不能是null）关联到key上
        // (2) 否则执行remappingFunction，如果执行结果非null，则用该结果跟key关联，否则在Map中删除key的映射；其中BiFunction是一个函数接口，里面有一个待实现方法R apply (K k, V v)
        // 需求：将新的错误信息拼接到原来的信息上
        map = new HashMap<>();
        // value如果传入null，会报错NullPointerException
//        map.merge(1, null, (v1, v2) -> v1 + v2);
        map.merge(1, "oldMsg", (v1, v2) -> v1 + v2);
        map.merge(1, "newMsg", (v1, v2) -> v1 + v2);
        // 3.8 V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)，如果计算结果为null，则在Map中删除key的映射，否则把remappingFunction的计算结果跟key关联
        // 需求：将新的错误信息拼接到原来的信息上
        map.compute(1, (k, v) -> v == null ? "newMsg again" : v.concat("newMsg again"));
        // 3.9 V computeIfAbsent (K key, Function<? super K, ? extends V> mappingFunction)，将条件判断和添加操作合二为一
        // 仅当Map中key值的映射不存在或为null时，才调用mappingFunction，并在mappingFunction执行结果非null时，将结果跟key关联；Function是一个函数接口，里面有一个待实现方法R apply (T t)
        // 需求：对Map的某个key值建立初始化映射．比如我们要实现一个多值映射，Map的定义可能是Map<K, Set<V>>，要向Map中放入新值
        Map<Integer, Set<String>> multiValueMap = new HashMap<>();
        // 3.9.1 Java7及以前的实现方式
        if (multiValueMap.containsKey(1)) {
            multiValueMap.get(1).add("one");
        } else {
            Set<String> valueSet = new HashSet<>();
            valueSet.add("one");
            multiValueMap.put(1, valueSet);
        }
        // 3.9.2 Java8的实现方式
        multiValueMap.computeIfAbsent(1, v -> new HashSet<>()).add("two");
        // 3.10 V computeIfPresent (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)；其中BiFunction是一个函数接口，里面有一个待实现方法R apply (K k, V v)
        // 仅当Map中存在key值的映射且非null时，才调用remappingFunction，如果remappingFunction执行结果为null，则删除key的映射，否则使用该结果替换key原来的映射
        multiValueMap.computeIfPresent(1, (k, v) -> new HashSet<>());
        multiValueMap.computeIfPresent(1, (k, v) -> {
            multiValueMap.get(k).add("one");
            v.add("two");
            return v;
        });
        System.out.println(multiValueMap);
    }

}
