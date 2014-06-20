
package twitch.files;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import twitch.Main;


public class Lib {
    
    public static class LibProcessing {
        
        public static ArrayList<String> removeCommentedSection(ArrayList<String> lines, String commentMarker) {
            if (lines == null || lines.isEmpty()) return new ArrayList<String>();
            ArrayList<String> process = new ArrayList<String>();
            process.addAll(lines);
            for (String line : lines) {
                if (line.contains(commentMarker)) process.remove(line);
            }
            return process;
        }
    }
    
    private Path libPath;
    private Path dirPath;
    
    public Lib(String dir, String libName, String ext) {
        dirPath = Paths.get(Main.APPDATA + dir);
        libPath = Paths.get(dirPath +"/" +  libName + "." + ext);
    }
    
    public ArrayList<String> readAllLines(Charset cs) {
        try {
            return (ArrayList<String>) Files.readAllLines(libPath, cs);
        } catch (IOException e) {
            return new ArrayList<String>();
        }
    }
    
    public boolean loadFile() {
        Main.log("loading file : " + libPath);
        try {
            if (Files.exists(libPath)) return true;
            else {
                Files.createDirectories(dirPath);
                if (FileRWHelper.createNewFile(libPath)) return true;
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Path getpath() {
        return libPath;
    }
    
    public ArrayList<String> readAllLines() {
        return this.readAllLines(StandardCharsets.UTF_8);
    }
}
