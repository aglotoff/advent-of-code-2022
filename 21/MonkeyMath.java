import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonkeyMath {
    private static HashMap<String, Monkey> monkeys = new HashMap<>();

    private static interface Monkey {
        long eval();
        long findHumn(long value);
        boolean hasHumn();
    }

    

    private static class NumberMonkey implements Monkey  {
        private long number;

        public NumberMonkey(long number) {
            this.number = number;
        }

        public long eval() {
            return number;
        }

        public boolean hasHumn() {
            return false;
        }

        public long findHumn(long value) {
            return value;
        }
    }

    private static class HumnMonkey extends NumberMonkey  {
        public HumnMonkey(long number) {
            super(number);
        }

        public boolean hasHumn() {
            return true;
        }
    }

    private static class OperationMonkey implements Monkey {
        protected char op;
        protected String leftName;
        protected String rightName;

        public OperationMonkey(char op, String leftName, String rightName) {
            this.op = op;
            this.leftName = leftName;
            this.rightName = rightName;
        }

        public long eval() {
            long left = monkeys.get(leftName).eval();
            long right = monkeys.get(rightName).eval();

            switch (op) {
                case '+':
                    return left + right;
                case '-':
                    return left - right;
                case '/':
                    return left / right;
                case '*':
                    return left * right;
                default:
                    return 0;
            }
        }

        public long findHumn(long value) {
            Monkey left = monkeys.get(leftName);
            Monkey right = monkeys.get(rightName);

            if (left.hasHumn()) {
                long rightValue = right.eval();

                switch (op) {
                case '+':
                    return left.findHumn(value - rightValue);
                case '-':
                    return left.findHumn(value + rightValue);
                case '/':
                    return left.findHumn(value * rightValue);
                case '*':
                    return left.findHumn(value / rightValue);
                default:
                    return 0;
                }
            } else if (right.hasHumn()) {
                long leftValue = left.eval();

                switch (op) {
                case '+':
                    return right.findHumn(value - leftValue);
                case '-':
                    return right.findHumn(leftValue - value);
                case '/':
                    return right.findHumn(leftValue / value);
                case '*':
                    return right.findHumn(value / leftValue);
                default:
                    return 0;
                }
            } else {
                return eval();
            }
        }

        public boolean hasHumn() {
            return monkeys.get(leftName).hasHumn() || monkeys.get(rightName).hasHumn();
        }
    }

    private static class RootMonkey extends OperationMonkey {
        public RootMonkey(char op, String leftName, String rightName) {
            super(op, leftName, rightName);
        }

        public long findHumn(long value) {
            Monkey left = monkeys.get(leftName);
            Monkey right = monkeys.get(rightName);

            if (left.hasHumn()) {
                return left.findHumn(right.eval());
            } else {
                return right.findHumn(left.eval());
            }
        }
    }

    public static void main(String[] args) {
        Pattern numberPattern = Pattern.compile("(\\w+): (\\d+)");
        Pattern operationPattern = Pattern.compile("(\\w+): (\\w+) ([-+/*]) (\\w+)");
    
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher numberMatcher = numberPattern.matcher(line);
            Matcher operationMatcher = operationPattern.matcher(line);

            if (numberMatcher.find()) {
                String name = numberMatcher.group(1);
                long number = Long.parseLong(numberMatcher.group(2));

                if (name.equals("humn")) {
                    monkeys.put(name, new HumnMonkey(number));
                } else {
                   monkeys.put(name, new NumberMonkey(number));
                }
            } else if (operationMatcher.find()) {
                String name = operationMatcher.group(1);
                String leftName = operationMatcher.group(2);
                char op = operationMatcher.group(3).charAt(0);
                String rightName = operationMatcher.group(4);

                if (name.equals("root")) {
                    monkeys.put(name, new RootMonkey(op, leftName, rightName));
                } else {
                    monkeys.put(name, new OperationMonkey(op, leftName, rightName));
                }
            }
        }

        scanner.close();

        System.out.println(monkeys.get("root").eval());
        System.out.println(monkeys.get("root").findHumn(0));
    }
}
