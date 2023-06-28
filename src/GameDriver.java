public class GameDriver extends Game {
    private static GameDriver game;
    public static GameDriver getGame(){
        if(game == null)
            game = new GameDriver();
        return game;
    }
    private GameDriver(){
        super();
    }
    public static void main(String[] args) {
        Game game = getGame();
        game.playGame();
    }
}