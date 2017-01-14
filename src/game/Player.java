package game;

public class Player {
    private String name;
    private int id;
    private int score = 0;
    private int color;
    private boolean isHuman = false;


    public Player() {}

    public Player(String name, int score, boolean isHuman) {
        this.name = name;
        this.score = score;
        this.isHuman = isHuman;
    }

    public Player(String name,int id, int score,int color, boolean isHuman) {
        this.name = name;
        this.id = id;
        this.score = score;
        this.color = color;
        this.isHuman = isHuman;
    }

//    public Player(Player otherPlayer) {
//        new Player(otherPlayer.getName(),
//                otherPlayer.getId(), otherPlayer.getScore(), otherPlayer.getColor(), otherPlayer.isHuman());
//    }

    public Player clonePlayer() {
        return new Player(name, id, score, color, isHuman);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int scoreToAdd){
        score += scoreToAdd;
    }

    public boolean isHuman() { return this.isHuman;}

    public void setAsHuman(){ this.isHuman = true;}

    public void setAsComputer() { this.isHuman = false; }


}
