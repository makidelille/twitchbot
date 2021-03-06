
package twitch.bots;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import twitch.Main;
import twitch.files.FileRWHelper;


public class RBot extends PircBot {
    
    private Path   path;
    private String channel;
    
    public RBot(Bot bot) {
        this(bot.getServer(), bot.getPort(), bot.getPassword());
    }
    
    public RBot(String server, int port, String pass) {
        try {
            this.setName(Main.MASTER);
            this.setVerbose(true);
            this.connect(server, port, pass);
            Main.log("R BOT LOADED");
        } catch (IOException | IrcException e) {
            e.printStackTrace();
            this.dispose();
        }
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (path != null) FileRWHelper.writeEndStringWithDateInFile(path, sender + " : " + message);
    }
    
    @Override
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        if (path != null) FileRWHelper.writeEndStringWithDateInFile(path, sender + "/me " + action);
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            this.setChannel(channel);
            this.changePath();
            Main.log("R BOT READY");
        } else if (path != null) {
            FileRWHelper.writeEndStringWithDateInFile(path, login + " joined");
        }
    }
    
    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        super.onPart(channel, sender, login, hostname);
        if (login.equalsIgnoreCase(this.getName())) {
            this.setChannel(null);
            this.changePath();
        } else if (path != null) {
            FileRWHelper.writeEndStringWithDateInFile(path, login + " left");
        }
    }
    
    private void changePath() {
        if (getChannel().isEmpty()) {
            path = null;
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String dateFormater = format.format(time);
        path = Paths.get(Main.APPDATA + "log/" + this.getChannel() + "_" + dateFormater + ".txt");
        Main.log("Accesing file : " + path);
        if (!Files.exists(path)) try {
            File file = new File(path.toAbsolutePath().toString());
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel.startsWith("#") ? channel : "#" + channel;
    }
}
