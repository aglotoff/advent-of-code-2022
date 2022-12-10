import java.util.Scanner;

public class CathodeRayTube {
  private static final int SCREEN_WIDTH = 40;

  private int cycle = 0;
  private int x = 1;
  private int signalStrengthSum = 0;

  public void nextCycle() {
    cycle++;

    int screenPos = (cycle - 1) % SCREEN_WIDTH;
    if (screenPos >= (x - 1) && screenPos <= (x + 1)) {
      System.out.print("#");
    } else {
      System.out.print('.');
    }

    if (screenPos % SCREEN_WIDTH == SCREEN_WIDTH - 1) {
      System.out.println();
    }

    if ((cycle >= 20) && (((cycle - 20) % 40) == 0)) {
      signalStrengthSum += cycle * x;
    }
  }

  public static void main(String[] args) {  
    CathodeRayTube device = new CathodeRayTube();

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");

      String instruction = line[0];

      if (instruction.equals("noop")) {
        device.nextCycle();
      } else if (instruction.equals("addx")) {
        device.nextCycle();
        device.nextCycle();

        device.x += Integer.parseInt(line[1]);
      }
    }

    scanner.close();

    System.out.println(device.signalStrengthSum);
  }
}
