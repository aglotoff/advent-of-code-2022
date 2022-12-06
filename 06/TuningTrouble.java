import java.util.Scanner;
import java.util.HashSet;

public class TuningTrouble {
  public static final int PACKET_MARKER_LENGTH = 4;
  public static final int MESSAGE_MARKER_LENGTH = 14;

  private static boolean isSequenceUnique(String s, int start, int length) {
    HashSet<Character> seenCharacters = new HashSet<>();

    for (int i = start; i < start + length; i++) {
      char c = s.charAt(i);
      if (seenCharacters.contains(c)) {
        return false;
      }
      seenCharacters.add(c);
    }

    return true;
  }

  private static int findFirstUniqueSequence(String s, int length) {
    for (int i = 0; i < s.length() - length + 1; i++) {
      if (isSequenceUnique(s, i, length)) {
        return i + length;
      }
    }
    return 0;
  }

  public static int findStartOfPacket(String buffer) {
    return findFirstUniqueSequence(buffer, PACKET_MARKER_LENGTH);
  }

  public static int findStartOfMessage(String buffer) {
    return findFirstUniqueSequence(buffer, MESSAGE_MARKER_LENGTH);
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    if (scanner.hasNextLine()) {
      String buffer = scanner.nextLine();
      System.out.println("Start of packet: " + findStartOfPacket(buffer));
      System.out.println("Start of message: " + findStartOfMessage(buffer));
    }

    scanner.close();
  }
}
