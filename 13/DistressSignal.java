import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class DistressSignal {
  private static abstract class PacketItem {}

  private static class PacketInteger extends PacketItem {
    private int value;
  }

  private static class PacketList extends PacketItem {
    private ArrayList<PacketItem> value = new ArrayList<>();
  }

  private static class PacketParser {
    private String s;
    private int pos;
    private char current;

    private int readNext() {
      if (pos >= s.length()) {
        return -1;
      }

      current = s.charAt(pos++);

      return current;
    }

    public PacketParser(String input) {
      this.s = input;
    }

    public PacketInteger parseInteger() throws ParseException {
      PacketInteger data = new PacketInteger();

      while (Character.isDigit(current)) {
        data.value = data.value * 10 + (current - '0');
        readNext();
      }

      return data;
    }

    public PacketList parseList() throws ParseException {
      PacketList data = new PacketList();

      readNext();

      while (current != ']') {
        data.value.add(parseValue());
        if (current == ',') {
          readNext();
        }
      }

      readNext();

      return data;
    }

    public PacketItem parseValue() throws ParseException {
      if (current == '[') {
        return parseList();
      }
      
      if (Character.isDigit(current)) {
        return parseInteger();
      }

      throw new ParseException("Unexpected character", pos);
    }
  }

  private static PacketItem parsePacket(String s) throws ParseException {
    PacketParser parser = new PacketParser(s);

    parser.readNext();

    PacketItem data = parser.parseValue();

    if (parser.pos < s.length()) {
      throw new ParseException("Unprocessed input", parser.pos);
    }

    return data;
  }

  private static class PacketComparator implements Comparator<PacketItem> {
    @Override
    public int compare(PacketItem left, PacketItem right) {
      if ((left instanceof PacketInteger) && (right instanceof PacketInteger)) {
        PacketInteger leftInt = (PacketInteger) left;
        PacketInteger rightInt = (PacketInteger) right;
        return Integer.compare(leftInt.value, rightInt.value);
      }
  
      PacketList leftList;
      if (left instanceof PacketList) {
        leftList = (PacketList) left;
      } else {
        leftList = new PacketList();
        leftList.value.add(left);
      }
  
      PacketList rightList;
      if (right instanceof PacketList) {
        rightList = (PacketList) right;
      } else {
        rightList = new PacketList();
        rightList.value.add(right);
      }
  
      int leftLength = leftList.value.size();
      int rightLength = rightList.value.size();
  
      for (int i = 0; i < Math.max(leftLength, rightLength); i++) {
        if (i >= leftLength) {
          return -1;
        }
        if (i >= rightLength) {
          return 1;
        }
  
        int result = compare(leftList.value.get(i), rightList.value.get(i));
        if (result != 0) {
          return result;
        }
      }
  
      return 0;
    }
  }

  public static void main(String[] args) throws ParseException {
    int sumOfIndicies = 0;

    ArrayList<PacketItem> packets = new ArrayList<>();

    PacketComparator comparator = new PacketComparator();

    Scanner scanner = new Scanner(System.in);

    for (int i = 1; scanner.hasNextLine(); i++) {
      PacketItem left = parsePacket(scanner.nextLine());
      packets.add(left);

      PacketItem right = parsePacket(scanner.nextLine());
      packets.add(right);
      
      if (comparator.compare(left, right) <= 0) {
        sumOfIndicies += i;
      }

      if (scanner.hasNextLine()) {
        scanner.nextLine();
      }
    }

    scanner.close();

    System.out.println(sumOfIndicies);

    PacketItem divider1 = parsePacket("[[2]]");
    PacketItem divider2 = parsePacket("[[6]]");

    packets.add(divider1);
    packets.add(divider2);

    packets.sort(comparator);

    int divider1Index = packets.indexOf(divider1) + 1;
    int divider2Index = packets.indexOf(divider2) + 1;

    System.out.println(divider1Index * divider2Index);
  }
}
