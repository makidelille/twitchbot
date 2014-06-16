package twitch.scripts;

import twitch.bots.Bot;


public class LinkCheckerScript extends Script{

    public static final String[] exts = {"academy","aero","asia","bike","biz","blue","build","builders","buzz","cab","camera","camp","careers","cat","catholic","center","ceo","christmas","clothing","club","cofee","com"};
    
    @Override
    public boolean execute(Bot bot, String channel, String sender, String msg) {
        if(bot.getStream().isUserOp(sender)) return false;
        String text = removeSpace(msg);
        if(text.contains("http://") || text.contains("www.")) return true;
        if(isTLD(text)) return true;
        return false;
    }
    private String removeSpace(String msg) {
        String[] msgArray = msg.split(" ");
        String text ="";
        for(String line : msgArray) {
            text +=line.toLowerCase();
        }
        return text;
    }
    
    private boolean isTLD(String text) {
        String[] array = text.split(".");
 //       String prevLine="";
        for(String line : array) {
            //TODO the hardest part ^^
            
            //might be a the ccTLD extention
            boolean flag = line.length() == 2;
            
            //check des extensions
            if(!flag)
                for(String ext : exts) {
                    if(line.contains(ext)){
                        flag = true;
                        break;
                    }
                }

            //check si utilise en mots ou en lien 
            //i need spaces --'
            if(flag) {
                
            }
            

 //           prevLine = line;
        }
        
        return false;
        
    }
}
