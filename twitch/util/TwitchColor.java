package twitch.util;

import twitch.Main;


public enum TwitchColor {

        BLUE("Blue"),
        LIGHTBLUE("DodgerBlue"),
        DARKBLUE("CadetBlue"),
        PURPLE("BlueViolet"),
        DARKPINK("HotPink"),
        RED("Red"),
        REDORANGE("Coral"),
        ORANGE("OrangeRed"),
        BRICK("FireBrick"),
        BROWN("GoldenRod"),
        CHOCOLATE("chocolate"),
        GREEN("green"),
        LIGHTGREEN("SeaGreen"),
        DARKGREEN("YellowGreen"),
        SPRINGGREEN("SpringGreen");


    private String colorCode;

    TwitchColor(String code){
        this.colorCode = code;
    }

    /**
     * @return the colorCode
     */
    public String getColorCode() {
        return colorCode;
    }
    
    
    public static TwitchColor getTwitchColor(String color){
        for(TwitchColor tcolor : TwitchColor.values())
            if(tcolor.getColorCode().equalsIgnoreCase(color)) return tcolor;
        
        switch(color.toLowerCase()){
            case "bleu" : return BLUE;
            case "light blue" :return LIGHTBLUE;
            case "bleuclair" : return LIGHTBLUE;
            case "bleu clair" : return LIGHTBLUE;
            case "violet" : return PURPLE;
            case "darkpink" : return DARKPINK;
            
            
            //TODO finish this
            default : return Main.defColor;
        }
    }

    public static TwitchColor getNextColor(TwitchColor color) {
        switch(color){
            case BLUE: return BRICK;
            case BRICK: return BROWN;
            case BROWN: return CHOCOLATE;
            case CHOCOLATE: return DARKBLUE;
            case DARKBLUE:return DARKGREEN;
            case DARKGREEN: return GREEN;
            case GREEN: return LIGHTBLUE;
            case LIGHTBLUE: return LIGHTGREEN;
            case LIGHTGREEN: return ORANGE;
            case ORANGE: return DARKPINK;
            case DARKPINK: return PURPLE;
            case PURPLE: return RED;
            case RED: return REDORANGE;
            case REDORANGE: return SPRINGGREEN;
            case SPRINGGREEN: return BLUE;
            default: return color;
            //TODO organise it
        }
    }

}
