import java.util.Arrays;

public class DiscardPile extends Pile{
    public DiscardPile(){
    }

    @Override
    public String toString() {
        return "DiscardPile{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }
    public void discardCard(Card card){
        if(card == null)
            throw new IllegalArgumentException();
        cards[availableCardsCount++] = card;
    }
    public Card[] getCards(){
        return cards;
    }
}
