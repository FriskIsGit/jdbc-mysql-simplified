package wargame;

class Player{
    protected String name;
    protected Hand hand;
    Player(String name, Hand hand){
        this.name = name;
        this.hand = hand;
    }
    boolean hasCards(){
        return hand.cards > 0;
    }
    public int getRandom(int min, int max){
        return (int)(Math.random()*(max-min+1))+min;
    }

}
