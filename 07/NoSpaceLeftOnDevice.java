import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NoSpaceLeftOnDevice {
  private static final long DIR_SIZE_LIMIT = 100000L;
  private static final long AVAILABLE_SPACE = 70000000L;
  private static final long REQUIRED_FREE_SPACE = 30000000L;
  
  public static abstract class FileSystemNode {
    private Directory parent;
    protected String name;

    public FileSystemNode(String name, Directory parent) {
      this.name = name;
      this.parent = parent;
    }

    public Directory getParent() {
      return parent;
    }

    public String getName() {
      return name;
    }

    public abstract long getSize();
  }

  public static class Directory extends FileSystemNode {
    private List<FileSystemNode> entries;

    public Directory(String name, Directory parent) {
      super(name, parent);
      entries = new LinkedList<>();
    }

    public void addChild(FileSystemNode node) {
      entries.add(node);
    }

    public List<FileSystemNode> getEntries() {
      return entries;
    }
    
    public FileSystemNode getEntry(String name) {
      for (FileSystemNode entry: entries) {
        if (entry.getName().equals(name)) {
          return entry;
        }
      }
      return null;
    }

    public long getSize() {
      long totalSize = 0;
      for (FileSystemNode entry: entries) {
        totalSize += entry.getSize();
      }
      return totalSize;
    }

    public String toString() {
      return name + " (dir)";
    }
  }

  public static class File extends FileSystemNode {
    private long size;

    public File(String name, Directory parent, long size) {
      super(name, parent);
      this.size = size;
    }

    public long getSize() {
      return size;
    }

    public String toString() {
      return name + " (file, size=" + size + ")";
    }
  }

  private static Directory findDirectory(Directory parent, String name) {
    FileSystemNode entry = parent.getEntry(name);
    if (entry == null || !(entry instanceof Directory))
      throw new Error(name + ": not found");
    return (Directory) entry;
  }

  public static void printTree(FileSystemNode node, int indent) {
    System.out.println(" ".repeat(indent) + "- " + node);
    if (node instanceof Directory) {
      Directory dir = (Directory) node;
      for (FileSystemNode child: dir.getEntries()) {
        printTree(child, indent + 2);
      }
    }
  }

  public static void printTree(FileSystemNode node) {
    printTree(node, 0);
  }

  private static class DirectoryIterator implements Iterator<Directory> {
    private LinkedList<Directory> queue;

    public DirectoryIterator(Directory root) {
      queue = new LinkedList<>();
      queue.add(root);
    }

    @Override
    public boolean hasNext() {
      return queue.size() > 0;
    }

    @Override
    public Directory next() {
      Directory nextNode = queue.removeFirst();

      for (FileSystemNode node : nextNode.getEntries()) {
        if (node instanceof Directory) {
          queue.addLast((Directory) node);
        }
      }

      return nextNode;
    }
  }

  private static long getTotalSize(Directory root, long limit) {
    long totalSize = 0;

    DirectoryIterator iterator = new DirectoryIterator(root);
    while (iterator.hasNext()) {
      Directory dir = iterator.next();
      long dirSize = dir.getSize();
      
      if (dirSize <= limit) {
        totalSize += dirSize;
      }
    }

    return totalSize;
  }

  private static Directory getDirectoryToDelete(Directory root, long spaceToFree) {
    Directory dirToDelete = root;

    DirectoryIterator iterator = new DirectoryIterator(root);
    while (iterator.hasNext()) {
      Directory dir = iterator.next();
      long dirSize = dir.getSize();

      if ((dirSize < dirToDelete.getSize()) && (dirSize >= spaceToFree)) {
        dirToDelete = dir;
      }
    }

    return dirToDelete;
  }

  public static void main(String[] args) {
    Directory root = new Directory("/", null);
    Directory currentDirectory = root;
    Directory lsDirectory = root;

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.startsWith("$")) {
        String[] command = line.split(" ");
        String commandName = command[1];

        if (commandName.equals("cd")) {
          String directoryName = command[2];

          // Change directory
          if (directoryName.equals("/")) {
            currentDirectory = root;
          } else if (directoryName.equals("..")) {
            currentDirectory = currentDirectory.getParent();
          } else {
            currentDirectory = findDirectory(currentDirectory, directoryName);
          }
        } else if (commandName.equals("ls")) {
          // List directory
          if (command.length < 3) {
            lsDirectory = currentDirectory;
          } else {
            lsDirectory = findDirectory(currentDirectory, command[2]);
          }
        }
      } else {
        String[] entry = line.split(" ");
        FileSystemNode node;

        if (entry[0].equals("dir")) {
          node = new Directory(entry[1], lsDirectory);
        } else {
          node = new File(entry[1], lsDirectory, Long.parseLong(entry[0]));
        }

        lsDirectory.addChild(node);
      }
    }

    scanner.close();

    // printTree(root);

    long totalSize = getTotalSize(root, DIR_SIZE_LIMIT);
    System.out.println(totalSize);

    long spaceToFree = REQUIRED_FREE_SPACE - (AVAILABLE_SPACE - root.getSize());
    Directory directoryToDelete = getDirectoryToDelete(root, spaceToFree);
    System.out.println(directoryToDelete.getSize());
  }
}
