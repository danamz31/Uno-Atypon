import java.util.Arrays;
import java.util.Random;

public class DrawPile extends Pile{
    protected int dealtCardsCount;
    public DrawPile(){
        initializeCards();
        shuffle();
    }
    private void initializeCards(){
        cards[availableCardsCount++] = new NumberCard(Color.RED,0);
        cards[availableCardsCount++] = new NumberCard(Color.BLUE,0);
        cards[availableCardsCount++] = new NumberCard(Color.GREEN,0);
        cards[availableCardsCount++] = new NumberCard(Color.YELLOW,0);

        for(int i=1; i<=9; i++){
            cards[availableCardsCount++] = new NumberCard(Color.RED,i);
            cards[availableCardsCount++] = new NumberCard(Color.RED,i);
            cards[availableCardsCount++] = new NumberCard(Color.BLUE,i);
            cards[availableCardsCount++] = new NumberCard(Color.BLUE,i);
            cards[availableCardsCount++] = new NumberCard(Color.GREEN,i);
            cards[availableCardsCount++] = new NumberCard(Color.GREEN,i);
            cards[availableCardsCount++] = new NumberCard(Color.YELLOW,i);
            cards[availableCardsCount++] = new NumberCard(Color.YELLOW,i);
        }
        for(int i=0; i<2; i++){
            cards[availableCardsCount++] = new ActionCard(Color.RED,Action.PASS);
            cards[availableCardsCount++] = new ActionCard(Color.BLUE,Action.PASS);
            cards[availableCardsCount++] = new ActionCard(Color.GREEN,Action.PASS);
            cards[availableCardsCount++] = new ActionCard(Color.YELLOW,Action.PASS);
            cards[availableCardsCount++] = new ActionCard(Color.RED,Action.REVERT);
            cards[availableCardsCount++] = new ActionCard(Color.BLUE,Action.REVERT);
            cards[availableCardsCount++] = new ActionCard(Color.GREEN,Action.REVERT);
            cards[availableCardsCount++] = new ActionCard(Color.YELLOW,Action.REVERT);
            cards[availableCardsCount++] = new ActionCard(Color.RED,Action.PLUSTWO);
            cards[availableCardsCount++] = new ActionCard(Color.BLUE,Action.PLUSTWO);
            cards[availableCardsCount++] = new ActionCard(Color.GREEN,Action.PLUSTWO);
            cards[availableCardsCount++] = new ActionCard(Color.YELLOW,Action.PLUSTWO);
        }
        for(int i=0; i<4; i++){
            cards[availableCardsCount++] = new ActionCard(Color.MULTICOLOR,Action.PLUSFOUR);
            cards[availableCardsCount++] = new ActionCard(Color.MULTICOLOR,Action.CHANGECOLOR);
        }
    }
    private void shuffle(){
        Random random = new Random();
        Card[] tempCards = new Card[108];
        int j;
        for(int i=0; i<108; i++){
            j = random.nextInt(108);
            while(cards[j]==null){
                j = random.nextInt(108);
            }
            tempCards[i] = cards[j];
            cards[j] = null;
        }
        cards = tempCards;
    }
    @Override
    public String toString() {
        return "DrawPile{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }
    public void setDealtCardsCount(int dealtCardsCount){
        this.dealtCardsCount = dealtCardsCount;
    }
    public int getDealtCardsCount() {
        return dealtCardsCount;
    }
    private void reFill(DiscardPile discardPile){
        cards = discardPile.getCards();
        availableCardsCount = discardPile.getCards().length;
    }
    public Card topCard(DiscardPile discardPile){
        if(availableCardsCount == 0)
            reFill(discardPile);
        Card card = super.topCard();
        cards[--availableCardsCount] = null;
        return card;
    }
    public Card[] topCards(int amount,DiscardPile discardPile){
        Card[] topCards = new Card[amount];
        for(int i=0; i<amount; i++){
            topCards[i] = topCard(discardPile);
        }
        return topCards;
    }
    private boolean cardIsAction(Card card){
        return card instanceof ActionCard;
    }
    private void returnCardToBack(Card card){
        availableCardsCount++;
        for(int i=availableCardsCount-1; i>0; i--){
            cards[i] = cards[i-1];
        }
        cards[0] = card;
    }
    public Card getStartCard(){
        Card card = topCard();
        while(cardIsAction(card)){
            returnCardToBack(card);
            card = topCard();
        }
        return card;
    }
}
