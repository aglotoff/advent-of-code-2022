import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonkeyMap2 {
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

    private static int getCubeNumber(int y, int x) {
        if (y < 50) {
            return x < 100 ? 1 : 2;
        } else if (y < 100) {
            return 3;
        } else if (y < 150) {
            return x < 50 ? 4 : 5;
        } else {
            return 6;
        }
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

                    for (int i = 0; i < n; i++) {
                        int nextX = x;
                        int nextY = y;
                        int nextFacing = facing;

                        switch (facing) {
                        case RIGHT:
                            if (x % 50 == 49) {
                                switch (getCubeNumber(y, x)) {
                                case 1: nextX = x + 1; break;
                                case 2: nextX = 99; nextY = 149 - y; nextFacing = LEFT; break;
                                case 3: nextX = 50 + y; nextY = 49; nextFacing = UP; break;
                                case 4: nextX = x + 1; break;
                                case 5: nextX = 149; nextY = 49 - (y - 100); nextFacing = LEFT; break;
                                case 6: nextX = y - 100; nextY = 149; nextFacing = UP; break;
                                }
                            } else {
                                nextX = x + 1;
                            }
                            break;
                        case DOWN:
                            if (y % 50 == 49) {
                                switch (getCubeNumber(y, x)) {
                                case 1: nextY = y + 1; break;
                                case 2: nextX = 99; nextY = x - 50; nextFacing = LEFT; break;
                                case 3: nextY = y + 1; break;
                                case 4: nextY = y + 1; break;
                                case 5: nextX = 49; nextY = 100 + x; nextFacing = LEFT; break;
                                case 6: nextX = 100 + x; nextY = 0; break;
                                }
                            } else {
                                nextY = y + 1;
                            }
                            
                            break;
                        case LEFT:
                            if (x % 50 == 0) {
                                switch (getCubeNumber(y, x)) {
                                case 1: nextX = 0; nextY = 149 - y; nextFacing = RIGHT; break;
                                case 2: nextX = x - 1; break;
                                case 3: nextX = y - 50; nextY = 100; nextFacing = DOWN; break;
                                case 4: nextX = 50; nextY = 49 - (y - 100); nextFacing = RIGHT; break;
                                case 5: nextX = x - 1; break;
                                case 6: nextX = y - 100; nextY = 0; nextFacing = DOWN; break;
                                }
                            } else {
                                nextX = x - 1;
                            }
                            break;
                            
                        case UP:
                            if (y % 50 == 0) {
                                switch (getCubeNumber(y, x)) {
                                case 1: nextX = 0; nextY = 100 + x; nextFacing = RIGHT; break;
                                case 2: nextX = x - 100; nextY = 199; break;
                                case 3: nextY = y - 1; break;
                                case 4: nextX = 50; nextY = 50 + x; nextFacing = RIGHT; break;
                                case 5: nextY = y - 1; break;
                                case 6: nextY = y - 1; break;
                                }
                            } else {
                                nextY = y - 1;
                            }
                            break;
                        }

                        if (map.get(nextY).get(nextX) == '#') {
                            break;
                        }

                        x = nextX;
                        y = nextY;
                        facing = nextFacing;
                    }
                    
                    break;
            }
        }

        System.out.println((y + 1) * 1000 + (x + 1) * 4 + facing);
    }
}
