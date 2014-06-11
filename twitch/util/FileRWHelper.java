package twitch.util;

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
    
    public static boolean deleteString(Path path,String s){
        try{
            try{
                List<String> buf = new ArrayList<String>();
                buf = Files.readAllLines(path, StandardCharsets.UTF_8);
                for(String line : buf) {
                    System.out.println(line);
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
    
}
