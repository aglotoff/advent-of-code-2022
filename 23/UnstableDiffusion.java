import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class UnstableDiffusion {
    private static final int NORTH = 0;
    private static final int SOUTH = 1;
    private static final int WEST = 2;
    private static final int EAST = 3;

    private static class Coordinates {
        int i;
        int j;

        public Coordinates(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (!(o instanceof Coordinates)) return false;

            Coordinates p = (Coordinates) o;
            return p.i == this.i && p.j == this.j;
        }

        @Override
        public int hashCode() {
            return i * 31 + j;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HashMap<Integer, Coordinates> positions = new HashMap<>();
        HashSet<Coordinates> occupied = new HashSet<>();
        int elfCount = 0;

        for (int i = 0; scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();

            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    Coordinates p = new Coordinates(i, j);
                    positions.put(elfCount, p);
                    occupied.add(p);
                    elfCount++;
                }
            }
        }

        scanner.close();

        int round = 1;

        for (;;) {
            HashMap<Integer, Coordinates> moves = new HashMap<>();
            HashMap<Coordinates, Integer> moveCount = new HashMap<>();

            for (int elf = 0; elf < elfCount; elf++) {
                Coordinates pos = positions.get(elf);

                boolean[] invalid = new boolean[4];
                boolean hasNeighbors = false;
                
                for (int i = pos.i - 1; i <= pos.i + 1; i++) {
                    for (int j = pos.j - 1; j <= pos.j + 1; j++) {
                        if (i == pos.i && j == pos.j) {
                            continue;
                        }

                        if (occupied.contains(new Coordinates(i, j))) {
                            if (i == pos.i - 1) invalid[NORTH] = true;
                            if (i == pos.i + 1) invalid[SOUTH] = true;
                            if (j == pos.j - 1) invalid[WEST] = true;
                            if (j == pos.j + 1) invalid[EAST] = true;
                            hasNeighbors = true;
                        }
                    }
                }

                if (!hasNeighbors) {
                    continue;
                }

                for (int k = 0; k < 4; k++) {
                    int dir = ((round - 1) % 4 + k) % 4;
                    if (!invalid[dir]) {


                        Coordinates move = new Coordinates(pos.i, pos.j);
                        switch (dir) {
                            case NORTH: move.i = pos.i - 1; break;
                            case SOUTH: move.i = pos.i + 1; break;
                            case WEST: move.j = pos.j - 1; break;
                            case EAST: move.j = pos.j + 1; break;
                        }

                        moves.put(elf, move);

                        if (moveCount.containsKey(move)) {
                            moveCount.put(move, moveCount.get(move) + 1);
                        } else {
                            moveCount.put(move, 1);
                        }

                        break;
                    }
                }
            }

            int totalMoves = 0;

            for (int elf = 0; elf < elfCount; elf++) {
                Coordinates pos = positions.get(elf);
                Coordinates move = moves.get(elf);
                if (move != null && (moveCount.get(move) < 2)) {
                    occupied.remove(pos);
                    occupied.add(move);

                    positions.put(elf, move);

                    totalMoves++;
                }
            }

            if (round == 10) {
                int minI = Integer.MAX_VALUE;
                int maxI = Integer.MIN_VALUE;
                int minJ = Integer.MAX_VALUE;
                int maxJ = Integer.MIN_VALUE;

                for (int elf = 0; elf < elfCount; elf++) {
                    Coordinates pos = positions.get(elf);

                    minI = Math.min(minI, pos.i);
                    maxI = Math.max(maxI, pos.i);
                    minJ = Math.min(minJ, pos.j);
                    maxJ = Math.max(maxJ, pos.j);
                }

                System.out.println((maxI - minI + 1) * (maxJ - minJ + 1) - elfCount);
            }

            if (totalMoves == 0) {
                break;
            }

            round++;
        }

        System.out.println(round);
    }
}
