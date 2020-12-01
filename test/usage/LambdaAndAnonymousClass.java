package usage;

import org.junit.Test;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class LambdaAndAnonymousClass {

    @Test
    public void test1() {
        // 例子1：无参函数的简写，新建线程
        // JDK7 匿名内部类写法
        new Thread(new Runnable() {// 接口名
            @Override
            public void run() {// 方法名
                System.out.println("Thread run()");
            }
        }).start();

        // JDK8 Lambda表达式写法，省略接口名和方法名
        new Thread(
                () -> System.out.println("Thread run()")
        ).start();

        // JDK8 Lambda表达式代码块写法
        new Thread(
                () -> {
                    System.out.print("Hello");
                    System.out.println(" Hoolee");
                }
        ).start();

        // 例子2：带参函数的简写，给一个字符串列表通过自定义比较器，按照字符串长度进行排序；通过内部类重载了Comparator接口的compare()方法，实现比较逻辑
        // JDK7 匿名内部类写法
        List<String> list = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list, new Comparator<String>() {// 接口名
            @Override
            public int compare(String s1, String s2) {// 方法名
                if (s1 == null) {
                    return -1;
                }
                if (s2 == null) {
                    return 1;
                }
                return s1.length() - s2.length();
            }
        });

        // JDK8 Lambda表达式写法，除了省略接口名和方法名，把参数表的类型也省略了
        // 这得益于javac的类型推断机制，编译器能够根据上下文信息推断出参数的类型，当然也有推断失败的时候，这时就需要手动指明参数类型了（Java是强类型语言，每个变量和对象都必须有明确的类型）
        list = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list, (s1, s2) -> {
            if (s1 == null) {
                return -1;
            }
            if (s2 == null) {
                return 1;
            }
            return s1.length() - s2.length();
        });

        // 能够使用Lambda表达式的依据1：必须有相应的函数接口（函数接口，是指内部只有一个抽象方法的接口），这跟Java是强类型语言吻合，实际上Lambda的类型就是对应函数接口的类型
        // 能够使用Lambda表达式的依据2：类型推断机制，在上下文信息足够的情况下，编译器可以推断出参数表的类型，而不需要显式指定
        // 无参函数的简写
        Runnable run = () -> System.out.println("Hello World");
        // 有参函数的简写，以及类型推断机制
        ActionListener listener = event -> System.out.println("button clicked");
        // 代码块的写法
        Runnable multiLine = () -> {// 3 代码块
            System.out.print("Hello");
            System.out.println(" Hoolee");
        };
        // 类型推断机制
        BinaryOperator<Long> add = (Long x, Long y) -> x + y;
        BinaryOperator<Long> addImplicit = (x, y) -> x + y;
    }

    // 自定义函数接口，只需要编写一个只有一个抽象方法的接口即可
    @FunctionalInterface
    public interface ConsumerInterface<T> {
        void accept(T t);
    }

    @Test
    public void test2() {
        // 例子3：基于函数式接口的用法
        // 上面代码中的@FunctionalInterface是可选的，但加上该标注编译器会检查接口是否符合函数接口规范，就像加入@Override标注会检查是否重载函数
        ConsumerInterface<String> consumer = str -> System.out.println(str);

        class MyStream<T> {
            private List<T> list;

            public void myForEach(ConsumerInterface<T> consumer) {// 1
                for (T t : list) {
                    consumer.accept(t);
                }
            }
        }
        MyStream<String> stream = new MyStream<>();
        stream.myForEach(str -> System.out.println(str));// 使用自定义函数接口书写Lambda表达式
    }

    public class Hello {
        Runnable r1 = () -> {
            System.out.println(this);
        };
        Runnable r2 = () -> {
            System.out.println(toString());
        };

        // 例子4：lambda表达式和匿名表达式的区别是没有生成匿名class
        // Lambda表达式不是内部类的简写，在Lambda表达式中，this的意义跟在表达式外部完全一样，下列代码将输出两遍Hello Hoolee
        @Test
        public void test3(String[] args) {
            new Hello().r1.run();
            new Hello().r2.run();
        }

        @Override
        public String toString() {
            return "Hello Hoolee";
        }
    }
}
