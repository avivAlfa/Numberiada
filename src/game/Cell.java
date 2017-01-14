package game;

public class Cell {
    private int value;
    private int color;
    private boolean isEmpty = false;
    private boolean isCursor = false;

    Cell(){}
    Cell(int value) {
        this.value = value;
    }

    Cell(int value, int color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {

        if(value == 999) {
            this.value = value;
            this.isEmpty = false;
            this.isCursor = true;
        }if(value == -999) {
            this.value = value;
            this.isEmpty = true;
            this.isCursor = false;
        }else {
            this.value = value;
            this.isEmpty = false;
            this.isCursor = false;
        }
    }

    public int getColor() {return color; }

    public void setColor(int color) {this.color = color; }

    public boolean isEmpty() {return this.isEmpty;}

    public void setAsEmpty(){
        this.value = -999;
        this.isEmpty = true;
        this.isCursor = false;
    }

    public boolean isCursor() {return  this.isCursor;}

    public void setAsCursor(){
        this.value = 999;
        this.isCursor = true;
        this.isEmpty = false;
    }
}
