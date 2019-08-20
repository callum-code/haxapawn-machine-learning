/*
   _____      _ _                                 _        ___   ___  __  ___  
  / ____|    | | |                               | |      |__ \ / _ \/_ |/ _ \ 
 | |     __ _| | |_   _ _ __ ___     ___ ___   __| | ___     ) | | | || | (_) |
 | |    / _` | | | | | | '_ ` _ \   / __/ _ \ / _` |/ _ \   / /| | | || |> _ < 
 | |___| (_| | | | |_| | | | | | | | (_| (_) | (_| |  __/  / /_| |_| || | (_) |
  \_____\__,_|_|_|\__,_|_| |_| |_|  \___\___/ \__,_|\___| |____|\___/ |_|\___/ 
 
*/

package cs448_hexapawn;

/**
 *
 * @author callumijohnston
 */
public class Pawn {
    int x;
    int y;
    boolean black;
    boolean alive;

    public Pawn(int x, int y, boolean black) {
        this.x = x;
        this.y = y;
        this.black = black;
        alive = true;
    }
    
    
    
}
