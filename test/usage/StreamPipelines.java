import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Stream流水线的原理，这是Stream实现的关键所在
 */
public class StreamPipelines {

    @Test
    public void test() {
        // 1.中间操作和结束操作
        // 1.1需求：输出A1B1C1 A2B2C2 A3B3C3，每个字符串独立一行
        // 第一次forEach执行的时候，会回溯peek操作，peek会回溯上一步的limit操作，limit会回溯上一步的peek操作，顶层没有操作了，开始自上向下开始执行，输出：A1B1C1 并继续下一次forEach
        // 第四次forEach执行的时候，会回溯peek操作，peek会回溯上一步的limit操作，到limit的时候，发现limit(3)这个job已经完成，终止循环
        IntStream.range(1, 10).peek(x -> System.out.print("\nA" + x)).limit(3).peek(x -> System.out.print("B" + x)).forEach(x -> System.out.print("C" + x));
        System.out.println("\n");

        // 1.2需求：输出A1 A2 A3 A4 A5 A6 A7B7C7 A8B8C8 A9B9C9，每个字符串独立一行
        // skip操作相当于循环里面的continue，会结束本次循环
        // 第一次forEach执行的时候，会回溯peek操作，peek会回溯上一步的skip操作，skip回溯到上一步的peek操作，顶层没有操作了，开始自上向下开始执行，因为6>1执行skip(6)相当于continue，输出A1并继续下一次forEach
        // 第七次forEach执行的时候，会回溯peek操作，peek会回溯上一步的skip操作，skip回溯到上一步的peek操作，顶层没有操作了，开始自上向下开始执行，因为6<7说明skip(6)已经执行完毕，输出A7B7C7并继续下一次forEach
        IntStream.range(1, 10).peek(x -> System.out.print("\nA" + x)).skip(6).peek(x -> System.out.print("B" + x)).forEach(x -> System.out.print("C" + x));
        System.out.println("\n");

        // 

        System.out.println("");
    }
}
