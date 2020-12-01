import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * stream规约操作（折叠操作）之reduce
 * 规约操作（reduction operation）又称折叠操作（fold），通过某个连接动作将所有元素汇总成一个汇总结果
 * 元素求和、求最大值或最小值、求出元素总个数、将所有元素转换成一个列表或集合，都属于规约操作
 * Stream类库有两个通用的规约操作reduce()和collect()，也有一些为简化书写而设计的专用规约操作，比如sum()、max()、min()、count()等，这里着重介绍reduce()和collect()
 */
public class ReductionStreamOperation {

    @Test
    public void test() {
        // 1.reduce()是多面手，方法定义有三种重写形式，sum()、max()、min()、count() 等常用函数，本质都是reduce操作
        // (1)Optional<T> reduce(BinaryOperator<T> accumulator)：指定accumulator=累加器，BinaryOperator参数、BinaryOperator返回值、Stream的类型=T
        // (2)T reduce(T identity, BinaryOperator<T> accumulator)：指定identity=自身初始值，identity参数、BinaryOperator参数、BinaryOperator返回值、Stream类型=T
        // (3)<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)：
        //     指定combiner=并行执行时合并多个部分结果的拼接器，BiFunction参数二、Stream的类型=T，identity参数、BinaryOperator参数一、BinaryOperator返回值的类型=U
        // 1.1需求：从一组单词中找出最长的单词，这里“大”的含义就是“长”
        // 下面代码会选出最长的单词love，其中Optional是单值的容器，使用它可以避免null值的麻烦
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        Optional<String> longest = stream.reduce((s1, s2) -> s1.length() >= s2.length() ? s1 : s2);
        System.out.println(longest.get());
        // 当然也可以使用Stream.max(Comparator<? super T> comparator)实现，但reduce()自有其存在的价值
        stream = Stream.of("I", "love", "you", "too");
        longest = stream.max((s1, s2) -> s1.length() - s2.length());
        System.out.println(longest.get());

        // 1.2需求：求出一组单词的长度之和，这是个“求和”操作，操作对象输入类型是String，而结果类型是Integer
        // reduce()只需要一步操作，有助于提升性能
        stream = Stream.of("I", "love", "you", "too");
        Integer lengthSum = stream.reduce(0, (sum, str) -> sum + str.length(), (a, b) -> a + b);
        System.out.println(lengthSum);
        // 也可以分两步来实现，先map()将字符串长度映射成Int，再sum()获取Int的和
        stream = Stream.of("I", "love", "you", "too");
        lengthSum = stream.mapToInt(str -> str.length()).sum();
        System.out.println(lengthSum);
        // 1.3需求：将一组单词的全部拼接，这是个“求和”操作，操作对象输入类型是String，而结果类型是String
        stream = Stream.of("I", "love", "you", "too");
        String concatSum = stream.reduce("", (sum, str) -> StringUtils.isNotBlank(sum) ? sum + " " + str : sum + str, (a, b) -> a + "|" + b);
        System.out.println(concatSum);

        // 2.collect()是终极武器，能将Stream转化为集合或者Map等复杂对象，是Stream接口方法中最灵活的，也是真正入门Java函数式编程的证明
        // 需求：将Stream转换成容器或Map
        stream = Stream.of("I", "love", "you", "too");
        List<String> list = stream.collect(Collectors.toList());
        stream = Stream.of("I", "love", "you", "too");
        Set<String> set = stream.collect(Collectors.toSet());
        stream = Stream.of("I", "love", "you", "too");
        Map<String, Integer> map = stream.collect(Collectors.toMap(Function.identity(), String::length));
        System.out.println("");
    }
}
