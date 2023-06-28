public abstract class Pile {
    protected Card [] cards;
    private static final int MAXCARDSCOUNT = 108;
    protected int availableCardsCount;
    protected Pile(){
        cards = new Card[MAXCARDSCOUNT];
    }
    protected Card topCard(){
        return cards[availableCardsCount-1];
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(int i=0 ; i<availableCardsCount; i++){
            string.append("\n").append(i).append(": ").append(cards[i].toString());
        }
        return string.toString();
    }
}
