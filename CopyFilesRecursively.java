import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.*;
public class CopyFilesRecursively {
    public static boolean moveFiles(String oldPath, String newPath) throws IOException{
        Path sourceLocation = Paths.get(oldPath);
        Path targetLocation = Paths.get(newPath);
        System.out.println(oldPath);
        System.out.println(newPath);
        CustomFileVisitor fileVisitor = new CustomFileVisitor(sourceLocation, targetLocation);
        //You can specify your own FileVisitOption
        Files.walkFileTree(sourceLocation, fileVisitor);
        return true;
    }
}