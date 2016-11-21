
public class Player {
    private String name;
    private int score = 0;
    private boolean isHuman = false;

    public Player(String name, int score, boolean isHuman) {
        this.name = name;
        this.score = score;
        this.isHuman = isHuman;
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


}
