
public class Cell {
    private int value;
    private boolean isEmpty = false;
    private boolean isCursor = false;

    Cell(){}
    Cell(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.isEmpty = false;
    }

    public boolean isEmpty() {return this.isEmpty;}

    public void setAsEmpty(){
        this.isEmpty = true;
        this.isCursor = false;
    }

    public boolean isCursor() {return  this.isCursor;}

    public void setAsCursor(){
        this.isCursor = true;
        this.isEmpty = false;
    }
}
