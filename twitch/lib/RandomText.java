
package twitch.lib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitch.Main;
import twitch.util.FileRWHelper;


public class RandomText {
    
    private static List<String> join,leave,random;
    private static Path joinPath,leavePath,ranPath;
    
    public static void load() {
        joinPath = Paths.get(Main.APPDATA + "random/join.txt");
        leavePath = Paths.get(Main.APPDATA + "random/leave.txt");
        ranPath = Paths.get(Main.APPDATA + "random/random.txt");        
        if(!Files.exists(joinPath)){
            try {
                Files.createFile(joinPath);
                Main.log("'Join' file created succesfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!Files.exists(leavePath)){
            try {
                Files.createFile(leavePath);
                Main.log("'Leave' file created succesfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if(!Files.exists(ranPath)){
            try {
                Files.createFile(ranPath);
                Main.log("'Random' file created succesfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        join = new ArrayList<String>();
        try {
            join = Files.readAllLines(joinPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(join.isEmpty())
            join.add("La phrase par défaut pour dire 'bonjour' :D");
        }
        
        leave = new ArrayList<String>();
        try{
            leave = Files.readAllLines(leavePath, StandardCharsets.UTF_8);
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            if(leave.isEmpty())
                leave.add("La phrase par défaut pour dire 'au revoir' :D");
        }
        
        random = new ArrayList<String>();
        try{
           random = Files.readAllLines(ranPath, StandardCharsets.UTF_8);
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            if(random.isEmpty())
                random.add("ceci est une phrase random par défaut ^^");
        }
    }
    
    public static String getRanJoin() {
        Random rand = new Random();
        int nb = rand.nextInt(join.size());
        try {
            return join.get(nb);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getRanLeave() {
        Random rand = new Random();
        int nb = rand.nextInt(leave.size());
        try {
            return leave.get(nb);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getRanSentence() {
        Random rand = new Random();
        int nb = rand.nextInt(random.size());
        try {
            return random.get(nb);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addRanJoin(String string) {
        if(FileRWHelper.writeEndStringInFile(joinPath, string)) return true;
        else return false;
    }
    
    public static boolean addRanLeave(String string) {
        if(FileRWHelper.writeEndStringInFile(leavePath, string)) return true;
        else return false;        
    }
    
    public static boolean addRanSentence(String string) {
        if(FileRWHelper.writeEndStringInFile(ranPath, string)) return true;
        else return false;
        }
    
}
