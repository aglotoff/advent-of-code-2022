import java.util.Scanner;

enum Shapes {
  ROCK,
  PAPER,
  SCISSORS
}

enum Outcomes {
  LOOSE,
  DRAW,
  WIN
}

public class RockPaperScissors2 {
  private static Shapes getShapeByLetter(String s) {
    if (s.equals("A")) {
      return Shapes.ROCK;
    }
    if (s.equals("B")) {
      return Shapes.PAPER;
    }
    if (s.equals("C")) {
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

  private static Outcomes getOutcomeByLetter(String s) {
    if (s.equals("X")) {
      return Outcomes.LOOSE;
    }
    if (s.equals("Y")) {
      return Outcomes.DRAW;
    }
    if (s.equals("Z")) {
      return Outcomes.WIN;
    }
    throw new IllegalArgumentException("Wrong outcome: " + s);
  }

  private static int getOutcomeScore(Outcomes outcome) {
    switch (outcome) {
      case LOOSE:
        return 0;
      case DRAW:
        return 3;
      case WIN:
      default:
        return 6;
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

  public static void main(String[] args) {
    long total = 0;
    
    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");
      Shapes elfShape = getShapeByLetter(line[0]);
      Outcomes outcome = getOutcomeByLetter(line[1]);

      Shapes myShape;
      if (outcome == Outcomes.LOOSE) {
        myShape = getLoosingShape(elfShape);
      } else if (outcome == Outcomes.WIN) {
        myShape = getWinningShape(elfShape);
      } else {
        myShape = elfShape;
      }

      total += getShapeScore(myShape);
      total += getOutcomeScore(outcome);
    }

    scanner.close();

    System.out.println(total);
  }
}
