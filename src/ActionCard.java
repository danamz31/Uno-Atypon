public class ActionCard extends Card{
    private final Action action;
    public ActionCard(Color color,Action action){
        super(color);
        this.action = action;
    }
    @Override
    public String toString() {
        return getColor() +
                "\t" + action;
    }
    public Action getAction(){
        return action;
    }
}
