import java.util.stream.IntStream;

public class Player {
    private final String name;
    private final Card[] cards;
    private int cardsAmount;
    public Player(String name){
        cards = new Card[50];
        this.name = name;
        cardsAmount = 0;
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(int i=0 ; i<cardsAmount; i++){
            string.append("\n").append(i + 1).append(": ").append(cards[i].toString());
        }
        return string.toString();
    }
    public String getName(){
        return name;
    }
    public int getCardsAmount(){
        return cardsAmount;
    }
    public void setCards(Card[] cards){
        if(cards == null)
            throw new IllegalArgumentException();
        IntStream.range(0, cards.length).forEach(i -> this.cards[cardsAmount++] = cards[i]);
    }
    public Card getCard(int i){
        if(i<0 || i>=cardsAmount)
            throw new IllegalArgumentException();
        return cards[i];
    }
    public void drawCard(DrawPile drawPile){
        if(drawPile == null)
            throw new IllegalArgumentException();
        cards[cardsAmount++] = drawPile.topCard();
    }
    public void playCard(int index,DiscardPile discardPile){
        if(discardPile == null)
            throw new IllegalArgumentException();
        if(index<0 || index>=cardsAmount)
            throw new IllegalArgumentException();
        discardPile.discardCard(cards[index]);
        cards[index] = cards[cardsAmount-1];
        cards[--cardsAmount] = null;
    }

}
