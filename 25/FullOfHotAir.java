import java.util.Scanner;

public class FullOfHotAir {
  private static long fromSnafu(String snafu) {
    long decimal = 0;
    long pow = 1;

    for (int i = snafu.length() - 1; i >= 0; i--, pow *= 5) {
      char snafuDigit = snafu.charAt(i);
      int decimalDigit = 0;
      switch (snafuDigit) {
      case '0': decimalDigit = 0; break;
      case '1': decimalDigit = 1; break;
      case '2': decimalDigit = 2; break;
      case '-': decimalDigit = -1; break;
      case '=': decimalDigit = -2; break;
      }

      decimal += decimalDigit * pow;
    }

    return decimal;
  }

  private static String toSnafu(long decimal) {
    String snafu = "";

    do {
      long remainder = decimal % 5;

      if (remainder <= 2) {
        snafu = remainder + snafu;
        decimal /= 5;
      } else if (remainder == 4) {
        snafu = "-" + snafu;
        decimal = (decimal + 1) / 5;
      } else {
        snafu = "=" + snafu;
        decimal = (decimal + 2) / 5;
      }
    } while (decimal != 0);

    return snafu;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    long sum = 0;

    while (scanner.hasNextLine()) {
      long decimal = fromSnafu(scanner.nextLine());
      // System.out.println(decimal + " = " + toSnafu(decimal));
      sum += decimal;
    }

    scanner.close();

    System.out.println(sum);
    System.out.println(toSnafu(sum));
    System.out.println(fromSnafu(toSnafu(sum)));
  }
}
