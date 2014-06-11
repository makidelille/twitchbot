package twitch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import twitch.Main;


public class FileRWHelper {
    
    public static boolean writeEndStringInFile(Path path,String s) {
        FileWriter writer;
        try{
            writer = new FileWriter(path.toFile(), true);
            writer.write(s+"\n");
            writer.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void writeEndStringWithDateInFile(Path path, String string) {
        writeEndStringInFile(path, Main.getTimeString() + string);
        
    }
    
    public static boolean deleteString(Path path,String s, String fileHeader){
        try{
            try{
                List<String> buf = new ArrayList<String>();
                buf = Files.readAllLines(path, StandardCharsets.UTF_8);
                if(!createNewFile(path)) return false;
                for(String line : buf) {
                    System.out.println(line);
                    System.out.println(s);
                    if(line.equalsIgnoreCase(s) || line.startsWith("?")) continue;
                    writeEndStringInFile(path, line);
                }
                return true;
            }catch(FileNotFoundException e1){
                e1.printStackTrace();
                return false;
            }
        }catch(IOException  e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean createNewFile(Path path) {
        try{
            Main.log("Creating new file : " + path.toAbsolutePath().toString());
            Files.deleteIfExists(path);
            File file = new File(path.toString());
            file.createNewFile();
            Main.log("File created succesfully");
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    
}
