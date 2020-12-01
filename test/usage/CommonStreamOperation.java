import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CommonStreamOperation {

    @Test
    public void test() {
        // 1.void forEach(Consumer<? super E> action)，作用是对容器中的每个元素执行action指定的动作，也就是对元素进行遍历；forEach()是结束操作，代码会立即执行
        // 需求：使用Stream.forEach()迭代
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        stream.forEach(str -> System.out.println(str));

        // 2.Stream<T> filter(Predicate<? super T> predicate)，作用是返回一个只包含满足predicate条件元素的Stream；filter()是中间操作，不会有实际计算
        // 需求：保留长度等于3的字符串
        stream = Stream.of("I", "love", "you", "too");
        stream.filter(str -> str.length() == 3).forEach(str -> System.out.println(str));

        // 3.Stream<T> distinct()，作用是返回一个去除重复元素之后的Stream；distinct()是中间操作，不会有实际计算
        // 需求：去除重复元素
        stream = Stream.of("I", "love", "you", "too", "too");
        stream.distinct().forEach(str -> System.out.println(str));

        // 4.Stream<T> sorted()和Stream<T> sorted(Comparator<? super T> comparator)，分别是自然顺序排序（升序）、自定义比较器排序
        stream = Stream.of("I", "love", "you", "too");
        stream.sorted((str1, str2) -> str1.length() - str2.length()).forEach(str -> System.out.println(str));

        // 5.<R> Stream<R> map(Function<? super T, ? extends R> mapper)，返回一个对当前所有元素执行mapper之后的结果组成的Stream，即对每个元素按照某种操作进行转换
        // 转换前后Stream中元素个数不会改变，但元素类型取决于转换之后的类型；map()是中间操作，不会有实际计算
        // 需求：将String数组的元素都转换成大写形式
        // 5.1 转换对象是String...
        stream = Stream.of("I", "love", "you", "too");
        stream.map(str -> str.toUpperCase()).forEach(str -> System.out.println(str));
        // 5.2 转换对象是List<Integer>...（注意和6.对比，源码实现里面是直接调用apply）
        Stream<List<Integer>> streamOfList = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        streamOfList.map(list -> list.stream()).forEach(i -> System.out.println(i));

        // 6.<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)，对每个元素执行mapper指定的操作，并用所有mapper返回的Stream中的元素，组成一个新的Stream作为最终返回结果
        // 即原stream中的所有元素都 "摊平" 之后组成的Stream，转换前后元素的个数和类型都可能会改变；flatMap()是中间操作，不会有实际计算
        // 需求：将List<Integer>数组的元素都“摊平”成一个新的Integer数组，输出两个List<Integer>里面的所有数字，包括1~5
        // 转换对象是List<Integer>...（注意和5.2对比，源码实现里面调用的是apply{foreach}）
        streamOfList = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        streamOfList.flatMap(list -> list.stream()).forEach(i -> System.out.println(i));
        System.out.println(streamOfList);
    }
}
