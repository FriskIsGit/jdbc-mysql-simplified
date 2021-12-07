package wargame;

class Game{
    Player player1;
    Player player2;
    boolean isFinished = false;
    Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }
    public static void main(String[] args){
        int tests = 100_100;
        while(tests-- >0){
            Player player = new Player("John", new Hand());
            Player computer = new Player("Computer", new Hand());
            new Game(player, computer).runGame();
        }
    }
    public void runGame(){
        while(!isFinished){
            duel();
            gameFinishChecks();
        }
    }

    private void gameFinishChecks(){
        if(!player1.hasCards()){
            printWinnerAndChangeFlag(player2);
        }else if(!player2.hasCards()){
            printWinnerAndChangeFlag(player1);
        }
    }

    private void printWinnerAndChangeFlag(Player p){
        System.out.println(p.name + " won the game");
        isFinished = true;
    }

    private void duel(){
        player1.hand.remove();
        player2.hand.remove();
        if(player1.getRandom(1,13) < player2.getRandom(1,13)){
            player2.hand.add();
        }else{
            player1.hand.add();
        }
    }
}
