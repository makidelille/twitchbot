
package twitch.lib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import twitch.Main;
import twitch.util.FileRWHelper;


public class RandomText {
    
    private static ArrayList<String> join, leave, random;
    private static Path              joinPath, leavePath, ranPath;
    
    public static void load() {
        joinPath = Paths.get(Main.APPDATA + "random/join.txt");
        leavePath = Paths.get(Main.APPDATA + "random/leave.txt");
        ranPath = Paths.get(Main.APPDATA + "random/random.txt");
        if (!Files.exists(joinPath)) {
            FileRWHelper.createNewFile(joinPath);
            FileRWHelper.writeEndStringInFile(joinPath, "#joinfile");
        }
        if (!Files.exists(leavePath)) {
            FileRWHelper.createNewFile(leavePath);
            FileRWHelper.writeEndStringInFile(leavePath, "#leavefile");
        }
        if (!Files.exists(ranPath)) {
            FileRWHelper.createNewFile(ranPath);
            FileRWHelper.writeEndStringInFile(ranPath, "#randfile");
        }
        join = new ArrayList<String>();
        try {
            join = (ArrayList<String>) Files.readAllLines(joinPath, StandardCharsets.UTF_8);
            if (join.size() < 2) join = new ArrayList<String>();
            for (int index = 0; index < join.size(); index++) {
                String line = join.get(index);
                if (line.contains("#")) {
                    join.remove(index);
                    continue;
                }
                Main.log("Ajout à 'join' : " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (join.isEmpty()) join.add("La phrase par dÃ©faut pour dire 'bonjour' :D");
        }
        leave = new ArrayList<String>();
        try {
            leave = (ArrayList<String>) Files.readAllLines(leavePath, StandardCharsets.UTF_8);
            if (leave.size() < 2) leave = new ArrayList<String>();
            for (int index = 0; index < leave.size(); index++) {
                String line = leave.get(index);
                if (line.contains("#")){
                    leave.remove(index);
                    continue;
                }
                Main.log("Ajout à 'leave' : " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (leave.isEmpty()) leave.add("La phrase par dÃ©faut pour dire 'au revoir' :D");
        }
        random = new ArrayList<String>();
        try {
            random = (ArrayList<String>) Files.readAllLines(ranPath, StandardCharsets.UTF_8);
            if (random.size() < 2) random = new ArrayList<String>();
            for (int index = 0; index < random.size(); index++) {
                String line = random.get(index);
                if (line.contains("#")) {
                    random.remove(index);
                    continue;
                }
                Main.log("Ajout à 'random' : " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (random.isEmpty()) random.add("ceci est une phrase random par dÃ©faut ^^");
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
        if (FileRWHelper.writeEndStringInFile(joinPath, string)) return true;
        else return false;
    }
    
    public static boolean addRanLeave(String string) {
        if (FileRWHelper.writeEndStringInFile(leavePath, string)) return true;
        else return false;
    }
    
    public static boolean addRanSentence(String string) {
        if (FileRWHelper.writeEndStringInFile(ranPath, string)) return true;
        else return false;
    }
    
    public static char getRandomChar() {
        Random ran = new Random();
        return (char) (65 + ran.nextInt(123));
    }
    
    public static String getRandomString(int par) {
        String s = "";
        for (int i = 0; i < par; i++) {
            s += getRandomChar();
        }
        return s;
    }
}
