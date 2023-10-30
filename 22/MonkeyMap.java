import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonkeyMap {
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    private static int turnLeft(int facing) {
        return facing == RIGHT ? UP : facing - 1;
    }

    private static int turnRight(int facing) {
        return facing == UP ? RIGHT : facing + 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        int x = 0;
        int y = 0;
        int facing = RIGHT;

        boolean positionInitialized = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.length() == 0) break;

            ArrayList<Character> row = new ArrayList<>();
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                row.add(c);

                if (!positionInitialized && c == '.') {
                    x = i;
                    positionInitialized = true;
                }
            }

            map.add(row);
        }

        String pathString = scanner.nextLine();
        scanner.close();

        Pattern pattern = Pattern.compile("\\d+|L|R");
        Matcher matcher = pattern.matcher(pathString);

        System.out.println(y + "," + x);

        while (matcher.find()) {
            String command = matcher.group(0);
            switch (command.charAt(0)) {
                case 'L':
                    facing = turnLeft(facing);
                    break;
                case 'R':
                    facing = turnRight(facing);
                    break;
                default:
                    int n = Integer.parseInt(command);
                    
                    switch (facing) {
                        case RIGHT:
                            for (int i = 0; i < n; i++) {
                                int nextX = x + 1;

                                if ((nextX == map.get(y).size()) || (map.get(y).get(nextX) == ' ')) {
                                    for (nextX = 0; map.get(y).get(nextX) == ' '; nextX++)
                                        ;
                                }

                                if (map.get(y).get(nextX) == '#') break;
                                x = nextX;
                            }
                            break;
                        case LEFT:
                            for (int i = 0; i < n; i++) {
                                int nextX = x - 1;

                                if ((nextX == -1) || (map.get(y).get(nextX) == ' ')) {
                                    for (nextX = map.get(y).size() - 1; map.get(y).get(nextX) == ' '; nextX--)
                                        ;
                                }

                                if (map.get(y).get(nextX) == '#') break;
                                x = nextX;
                            }
                            break;
                        case UP:
                            for (int i = 0; i < n; i++) {
                                int nextY = y - 1;

                                if ((nextY == -1) || x >= map.get(nextY).size() || (map.get(nextY).get(x) == ' ')) {
                                    for (nextY = map.size() - 1; x >= map.get(nextY).size() || map.get(nextY).get(x) == ' '; nextY--)
                                        ;
                                }

                                if (map.get(nextY).get(x) == '#') break;
                                y = nextY;
                            }
                            break;
                        case DOWN:
                            for (int i = 0; i < n; i++) {
                                int nextY = y + 1;

                                if ((nextY == map.size()) || x >= map.get(nextY).size() || (map.get(nextY).get(x) == ' ')) {
                                    for (nextY = 0; x >= map.get(nextY).size() || map.get(nextY).get(x) == ' '; nextY++)
                                        ;
                                }

                                if (map.get(nextY).get(x) == '#') break;
                                y = nextY;
                            }
                            break;
                    }

                    break;
            }
        }

        System.out.println((y + 1) * 1000 + (x + 1) * 4 + facing);
    }
}
