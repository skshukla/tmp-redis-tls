import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ABC {

    public static void main(String[] args) {
        me();
    }

    private static void me() {
        Calendar c = Calendar.getInstance();
        c.set(2100, 8, 24);

        System.out.println(c.getTime().getTime());
        System.out.println(new Date().getTime());

    }

    private void greet() {
        System.out.println("Write your name: ");
        final String s = new Scanner(System.in).nextLine();
        System.out.println(String.format("Hello %s!, How are you?", s));
    }
    private void printTable(int n) {
        IntStream.range(0, 10).forEach(i -> {
            System.out.println((i + 1) * n);
        });
    }

    private void printABC() {
        IntStream.range(0, 26).forEach(i -> {
            System.out.print((char) (65 + i) + " ");
            if (i % 4 == 3) {
                System.out.println();
            }
        });
    }
}