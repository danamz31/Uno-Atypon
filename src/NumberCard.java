public class NumberCard extends Card{
    private final int number;
    public NumberCard(Color color, int number){
        super(color);
        this.number = number;
    }
    @Override
    public String toString() {
        return getColor() +
                "\t" + number;
    }
    public int getNumber(){
        return number;
    }
}
