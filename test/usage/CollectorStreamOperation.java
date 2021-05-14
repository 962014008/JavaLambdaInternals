import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * stream规约操作（折叠操作）之collect
 */
public class CollectorStreamOperation {

    private static double PASS_THRESHOLD = 60.0;

    @Data
    @NoArgsConstructor
    @SuperBuilder
    private static class Student {
        @Builder.Default
        double grade = 0.0;
    }

    double computeGPA(Student student) {
        return student.grade / 100.0;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    private static class Department {
        @Builder.Default
        long id = -1;
    }

    @Data
    @NoArgsConstructor
    @SuperBuilder
    private static class Employee {
        @Builder.Default
        long id = -1;
        @Builder.Default
        String name = "noName";
        Department department;
    }

    @Test
    public void test() {
        // collect能够将一个Stream转换成一个容器（List或者Set），或者Map，还能够并行进行规约，将多个部分结果进行合并
        // (1)<R > R collect(Supplier < R > supplier, BiConsumer < R, ? super T > accumulator, BiConsumer < R, R > combiner)：指定supplier=目标容器，accumulator=累加器，combiner=并行执行时合并多个部分结果的拼接器
        // (2)<R, A> R collect(Collector < ? super T, A, R > collector)：指定collector=通过静态方法生成的常用Collector
        // 1.将Stream规约成List或者Set
        // 1.1多参数版本
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        List<String> list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(list);
        // 1.2单参数版本
        stream = Stream.of("I", "love", "you", "too");
        list = stream.collect(Collectors.toList());
        System.out.println(list);
        // 1.3直接将Stream规约为数组Object[]
        Stream<Integer> streamOfInteger = Stream.of(1, 2, 3, 4);
        Integer[] integerArr = (Integer[]) streamOfInteger.toArray();
        int[] intArr = streamOfInteger.mapToInt(Integer::intValue).toArray();

        // 2.将Stream转换成List或Set
        stream = Stream.of("I", "love", "you", "too");
        list = stream.collect(Collectors.toList());
        stream = Stream.of("I", "love", "you", "too");
        Set<String> set = stream.collect(Collectors.toSet());

        // 3.使用toCollection()指定目标容器的类型，可通过Collectors.toCollection(Supplier < C > collectionFactory) 方法完成
        stream = Stream.of("I", "love", "you", "too");
        ArrayList<String> arrayList = stream.collect(Collectors.toCollection(ArrayList::new));
        stream = Stream.of("I", "love", "you", "too");
        HashSet<String> hashSet = stream.collect(Collectors.toCollection(HashSet::new));

        // 4.使用collect()生成Map
        // 前面提过Stream的数据源可以是一个数组、Java容器或I/O channel等，但不能是Map；反过来从Stream生成Map是可以的
        // (1)使用Collectors.toMap()生成的收集器，需要指定如何生成key和value，该方法与Collectors.toCollection()异曲同工
        // (2)使用Collectors.partitioningBy()生成的收集器，用于对元素进行二值逻辑的分区操作
        // (3)使用Collectors.groupingBy()生成的收集器，用于对元素进行group操作
        // 4.1需求：使用toMap()统计<学生, GPA>的Map，其中key=student identity，value=computeGPA结果
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        students.add(student);
        Student.StudentBuilder studentBuilder = Student.builder();
        studentBuilder.grade(80.0);
        students.add(studentBuilder.build());
        Map<Student, Double> studentToGPA = students.stream().collect(Collectors.toMap(Function.identity(), stu -> computeGPA(stu)));
        // 4.2需求：将学生分成成绩及格或不及格的两部分
        // 使用partitioningBy()生成的收集器，用于对元素进行二值逻辑的分区操作（满足条件，或不满足），如男女性别、成绩及格与否等两部分
        Map<Boolean, List<Student>> passingFailing = students.stream().collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));
        // 4.3需求：将员工按照部门进行分组
        // 使用groupingBy()生成的收集器，用于对元素进行group操作，属性相同的元素会被映射到Map的同一个key
        Department department = new Department();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setDepartment(department);
        employees.add(employee);
        Employee.EmployeeBuilder employeeBuilder = Employee.builder();
        employeeBuilder.id(1).department(department);
        employees.add(employeeBuilder.build());
        // (1)使用默认的下游收集器，即Collectors.toList()
        Map<Department, List<Employee>> employeesByDeptWithDefaultDownstream = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
        // (2)显式指定Collectors.toList()下游收集器
        // 这里的groupingBy()是上游收集器，Collectors.toList()是下游的收集器
        // groupingBy()获得按照department分组的employees，Collectors.toList()将每个分组的employees合并为list（注意与4.5需求对比）
        Map<Department, List<Employee>> employeesByDeptWithListDownstream = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.toList()));
        // 4.4需求：使用下游收集器统计每个部门的人数
        // 在SQL中使用group by是为了协助其他查询，比如先将员工按照部门分组，再统计每个部门员工的人数
        // 增强版的groupingBy()允许我们对元素分组之后再执行某种运算，比如求和、计数、平均值、类型转换等
        // 上游收集器(upstream Collector)=先将元素分组的收集器，如这里的groupingBy()；下游收集器(downstream Collector)=再执行其他运算的收集器，如这里的Collectors.counting()
        Map<Department, Long> countsByDept = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        // 4.5需求：按照部门对员工分组，并只保留员工的名字
        // 这里的groupingBy()是上游收集器，Collectors.mapping()是下游收集器，Collectors.toList()是更下游的收集器
        // groupingBy()获得按照department分组的employees，Collectors.mapping()对每个分组的employees获取name，Collectors.toList()将每个分组的employees的name合并为list（注意与4.3需求(2)对比）
        Map<Department, List<String>> namesByDept = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.mapping(Employee::getName, Collectors.toList())));

        // 5.使用Collectors.joining()拼接字符串
        // Collectors.joining()方法有三种重写形式，如下
        // Iloveyou
        stream = Stream.of("I", "love", "you");
        String joined = stream.collect(Collectors.joining());
        // I,love,you
        stream = Stream.of("I", "love", "you");
        joined = stream.collect(Collectors.joining(","));
        // {I,love,you}
        stream = Stream.of("I", "love", "you");
        joined = stream.collect(Collectors.joining(",", "{", "}"));

        System.out.println("");
    }
}
