import java.util.ArrayList;
import java.util.Scanner;

public class GrovePositioningSystem {
    private static class Node {
        long value;
        Node next;
        Node prev;
    }

    private static void insertAfter(Node x, Node t) {
        x.prev = t;
        x.next = t.next;
        x.prev.next = x;
        x.next.prev = x;
    }

    private static void insertBefore(Node x, Node t) {
        x.next = t;
        x.prev = t.prev;
        x.prev.next = x;
        x.next.prev = x;
    }

    private static void remove(Node x) {
        x.prev.next = x.next;
        x.next.prev = x.prev;
    }

    private static Node skipPrev(Node x, long count) {
        for (long i = 0; i < count; i++) {
            x = x.prev;
        }
        return x;
    }

    private static Node skipNext(Node x, long count) {
        for (long i = 0; i < count; i++) {
            x = x.next;
        }
        return x;
    }

    private static Node mix(ArrayList<Integer> list, long key, int steps) {
        ArrayList<Node> nodes = new ArrayList<>();
        Node last = null;
        Node zero = null;

        for (int i = 0; i < list.size(); i++) {
            Node x = new Node();
            x.value = list.get(i) * key;

            if (last == null) {
                x.prev = x;
                x.next = x;
            } else {
                insertAfter(x, last);
            }

            last = x;

            if (x.value == 0) {
                zero = x;
            }

            nodes.add(x);
        }

        int length = list.size() - 1;

        for (int s = 0; s < steps; s++) {
            for (Node x : nodes) {
                if (x.value % length == 0) {
                    continue;
                }

                remove(x);

                if (x.value < 0) {
                    insertBefore(x, skipPrev(x, (-x.value % length)));
                } else {
                    insertAfter(x, skipNext(x, x.value % length));
                }
            }
        }

        return zero;
    }

    private static long sumCoordinates(Node zero) {
        long sum = 0;
        Node x = zero;
        for (int i = 0; i < 3; i++) {
            x = skipNext(x, 1000);
            sum += x.value;
        }
        return sum;
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        scanner.close();

        System.out.println(sumCoordinates(mix(list, 1, 1)));
        System.out.println(sumCoordinates(mix(list, 811589153, 10)));
    }
}
