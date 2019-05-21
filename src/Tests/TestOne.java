package Tests;

import com.sun.deploy.util.StringUtils;

public class TestOne {
    public static void main(String[] args) {
//        studentPrintTest();
        System.out.println(factorial(25L));
    }


    public static long factorial(long n) {
        return (n > 1) ? n * factorial(n - 1) : n;
    }

    public static void studentPrintTest() {
        Student student = new Student();
        Student2 student2 = new Student2();
        System.out.println("Student: " + student.getId());
        System.out.println("Student2: " + student2.getId());
        System.out.println("Student: " + ((Student) comMethod(student)).getId());
        System.out.println("Student2: " + ((Student2) comMethod(student2)).getId());
        System.out.println("!!!!!");
    }

    public static <T> T comMethod(T t) {
        try {
            t.getClass().getMethod("setId", String.class).invoke(t, "3");
            return t;
        } catch (Exception e) {
            return null;
        }
    }
}
