import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import java.io.*; 
import java.nio.file.Files; 
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