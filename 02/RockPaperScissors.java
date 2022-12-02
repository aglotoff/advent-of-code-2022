import java.util.Scanner;

enum Shapes {
  ROCK,
  PAPER,
  SCISSORS
}

public class RockPaperScissors {
  private static Shapes getShapeByLetter(String s) {
    if (s.equals("A") || s.equals("X")) {
      return Shapes.ROCK;
    }
    if (s.equals("B") || s.equals("Y")) {
      return Shapes.PAPER;
    }
    if (s.equals("C") || s.equals("Z")) {
      return Shapes.SCISSORS;
    }
    throw new IllegalArgumentException("Wrong shape: " + s);
  }

  private static int getShapeScore(Shapes shape) {
    switch (shape) {
      case ROCK:
        return 1;
      case PAPER:
        return 2;
      case SCISSORS:
      default:
        return 3;
    }
  }

  private static Shapes getWinningShape(Shapes shape) {
    switch (shape) {
      case ROCK:
        return Shapes.PAPER;
      case PAPER:
        return Shapes.SCISSORS;
      case SCISSORS:
      default:
        return Shapes.ROCK;
    }
  }

  private static Shapes getLoosingShape(Shapes shape) {
    switch (shape) {
      case ROCK:
        return Shapes.SCISSORS;
      case PAPER:
        return Shapes.ROCK;
      case SCISSORS:
      default:
        return Shapes.PAPER;
    }
  }

  private static int getOutcomeScore(Shapes shape1, Shapes shape2) {
    if (shape1 == getWinningShape(shape2)) {
      return 0;
    }
    if (shape1 == getLoosingShape(shape2)) {
      return 6;
    }
    return 3;
  }

  public static void main(String[] args) {
    long total = 0;
    
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] shapes = scanner.nextLine().split(" ");
      
      Shapes elfShape = getShapeByLetter(shapes[0]);
      Shapes myShape = getShapeByLetter(shapes[1]);

      total += getShapeScore(myShape);
      total += getOutcomeScore(elfShape, myShape);
    }

    scanner.close();

    System.out.println(total);
  }
}
