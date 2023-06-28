import java.util.Arrays;
import java.util.Scanner;

public abstract class Game {
    Scanner s = new Scanner(System.in);
    protected final DiscardPile discardPile;
    protected final DrawPile drawPile;
    protected final Player [] players;
    protected final int playersCount;
    protected int currentPlayer;
    protected Color chosenColor;
    protected boolean roundPassed = false;
    protected String winner;
    protected Game(){
        playersCount = insertPlayersNumber();
        drawPile = new DrawPile();
        discardPile = new DiscardPile();
        players = new Player[playersCount];
        String [] names = insertPlayersName(playersCount);
        for(int i = 0; i< playersCount; i++){
            players[i] = new Player(names[i]);
        }
        currentPlayer = 0;
        chosenColor = Color.MULTICOLOR;
    }
    private String[] insertPlayersName(int playersCount){
        Scanner s = new Scanner(System.in);
        String [] names = new String[playersCount];
        System.out.println("Enter Players Firstname only:");
        for(int i=0; i<playersCount;i++){
            System.out.print("Player ["+i+"]: ");
            names[i] = s.next();
        }
        return names;
    }
    private int insertPlayersNumber(){
        Scanner s = new Scanner(System.in);
        int playersCountAsInt = 0;
        String playersCountAsString;
        while(playersCountAsInt<2 || playersCountAsInt > 10){
            System.out.print("How many players would to play? [Allowed number of players is between 2 and 8]: ");
            playersCountAsString = s.next();
            try{
                playersCountAsInt = Integer.parseInt(playersCountAsString);
            }catch (NumberFormatException e){
                System.out.println("please insert valid number.");
                playersCountAsInt = 0;
            }
        }
        return playersCountAsInt;
    }
    @Override
    public String toString() {
        return "Game{" +
                "players=" + Arrays.toString(players) +
                '}';
    }
    protected void dealCards(){
        for(int i=0; i<playersCount; i++){
            players[i].setCards(drawPile.topCards(drawPile.getDealtCardsCount(),discardPile));
        }
    }
    protected void setGame(){
        drawPile.setDealtCardsCount(7);
        dealCards();
        discardPile.discardCard(drawPile.getStartCard());
    }
    protected boolean cardsHaveSameColor(Card topCard, Card cardToPlay){
        return cardToPlay.getColor()==topCard.getColor();
    }
    protected boolean cardsHaveSameNumber(Card topCard, Card cardToPlay){
        if(topCard instanceof NumberCard numberTopCard && cardToPlay instanceof NumberCard numberCardToPlay) {
            return numberCardToPlay.getNumber() == numberTopCard.getNumber();
        }
        return false;
    }
    protected boolean cardsHaveSameAction(Card topCard, Card cardToPlay){
        if(topCard instanceof ActionCard actionTopCard && cardToPlay instanceof ActionCard actionCardToPlay) {
            return actionCardToPlay.getAction() == actionTopCard.getAction();
        }
        return false;
    }
    protected boolean cardToPlayIsMultiColor(Card cardToPlay){
        return cardToPlay.getColor()==Color.MULTICOLOR;
    }
    protected boolean cardToPlayColorAsChosen(Card topCard,Card cardToPlay){
        return cardToPlay.getColor() == chosenColor && topCard.getColor() == Color.MULTICOLOR;
    }
    protected boolean satisfyGameConditions(int cardIndex){
        Card topCard = discardPile.topCard();
        Card cardToPlay = players[currentPlayer].getCard(cardIndex-1);
        return cardsHaveSameColor(topCard,cardToPlay) ||
                cardsHaveSameNumber(topCard,cardToPlay) ||
                cardsHaveSameAction(topCard,cardToPlay) ||
                cardToPlayIsMultiColor(cardToPlay) ||
                cardToPlayColorAsChosen(topCard,cardToPlay);
    }
    //needs to be separated
    protected void showTopCard(){
        System.out.println("Top card: "+(discardPile.topCard()).toString());
    }
    protected Card getTopCard(){
        return discardPile.topCard();
    }
    protected void showCardsSet(){
        System.out.print("\nPlayer "+currentPlayer+" ["+players[currentPlayer].getName()+"] Cards:");
        System.out.println(players[currentPlayer].toString());
        System.out.println();
        showTopCard();
    }
    protected int chooseCardToPlay(){
        String option = "";
        String indexAsString;
        int indexAsInt = 0;
        showCardsSet();
        while(!option.equals("y") && !option.equals("n")) {
            System.out.println("Do you want to draw a card?yes[y] no[n]");
            option = s.next();
        }
        if(option.equals("y")){
            players[currentPlayer].drawCard(drawPile);
            showCardsSet();
            while(!option.equals("p") && !option.equals("c")) {
                System.out.println("Pass or Continue?pass[p] continue[c]");
                option = s.next();
            }
        }
        if(option.equals("n") || option.equals("c")) {
            while(indexAsInt<1 || indexAsInt>players[currentPlayer].getCardsAmount() ) {
                System.out.println("Choose card to play?(Choose Card Index)");
                indexAsString = s.next();
                try{
                    indexAsInt = Integer.parseInt(indexAsString);
                }catch (NumberFormatException e){
                    System.out.println("please insert valid number.");
                    indexAsInt = 0;
                }
            }
        }
        return indexAsInt;
    }
    protected void playCard(){
        roundPassed = false;
        int cardIndex = 1;
        boolean satisfyGameConditions = false;
        while(!satisfyGameConditions){
                System.out.print("Please choose a card with the same color or number.");
                cardIndex = chooseCardToPlay();
                if(cardIndex==0) {
                    roundPassed = true;
                    break;
                }
                satisfyGameConditions = satisfyGameConditions(cardIndex);
        }
        if(satisfyGameConditions){ //round not passed
            chosenColor = Color.MULTICOLOR;
            players[currentPlayer].playCard(cardIndex-1,discardPile);
        }
    }
    protected boolean thereIsWinner(){
        for(int i=0; i<playersCount; i++){
            if(players[i].getCardsAmount()==0) {
                winner = players[i].getName();
                return true;
            }
        }
        return false;
    }
    protected Player[] revertPlayers(){
        Player []revertPlayers = new Player[playersCount];
        for(int i=0; i<playersCount; i++){
            revertPlayers[i] = players[playersCount-1-i];
        }
        return revertPlayers;
    }
    protected int searchPlayerIndex(Player []revertPlayers,Player player){
        for(int i=0; i<playersCount; i++){
            if(revertPlayers[i].getName().equals(player.getName()))
                return i;
        }
        return -1;
    }
    protected void revertAction(){
        Player[] revertPlayers = revertPlayers();
        int index = searchPlayerIndex(revertPlayers,players[currentPlayer]) - currentPlayer;
        index = index<0? playersCount+index: index;
        for(int i=0; i<playersCount; i++){
            players[i] = revertPlayers[(i+index)%playersCount];
        }
    }
    protected void passAction(){
        currentPlayer =  (currentPlayer + 1)%playersCount;
    }
    protected void plusCards(int cardsCount){
        players[currentPlayer].setCards(drawPile.topCards(cardsCount,discardPile));
    }
    protected void plusTwoAction(){
        passAction();
        plusCards(2);
    }
    protected void plusFourAction(){
        passAction();
        changeColorAction();
        plusCards(4);
    }
    protected void setChosenColor(String colorSymbol){
        switch (colorSymbol) {
            case "r" -> chosenColor = Color.RED;
            case "b" -> chosenColor = Color.BLUE;
            case "g" -> chosenColor = Color.GREEN;
            case "y" -> chosenColor = Color.YELLOW;
            default -> {
            }
        }
    }
    protected void changeColorAction(){
        System.out.print("Choose Color of [r for RED, g for GREEN, b for BLUE, y for YELLOW] to play with: ");
        String colorSymbol = s.next();
        while(!colorSymbol.equals("r") && !colorSymbol.equals("b") && !colorSymbol.equals("y") && !colorSymbol.equals("g")){
            System.out.print("Please choose a correct symbol [r for RED, g for GREEN, b for BLUE, y for YELLOW] to play with: ");
            colorSymbol = s.next();
        }
        setChosenColor(colorSymbol);
    }
    protected void doAction(){
        Card card = getTopCard();
        if (card instanceof ActionCard) {
            Action action = ((ActionCard) card).getAction();
            switch (action){
                case PASS -> passAction();
                case REVERT -> revertAction();
                case PLUSTWO -> plusTwoAction();
                case PLUSFOUR -> plusFourAction();
                case CHANGECOLOR -> changeColorAction();
                default -> {
                }
            }
        }
    }
    protected void nextPlayer(){
        currentPlayer = (currentPlayer+1)%playersCount;
    }
    protected void playGame(){
        setGame();
        while (true) {
            playCard();
            if (thereIsWinner()) {
                break;
            }
            if (roundPassed) {
                nextPlayer();
                continue;
            }
            doAction();
            nextPlayer();
        }
        System.out.print("The winner is: "+winner);
    }
}
