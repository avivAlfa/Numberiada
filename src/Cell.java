
public class Cell {
    private int value;
    boolean isEmpty = false;

    Cell(){}
    Cell(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setAsEmpty(){
        this.isEmpty = true;
        this.setValue(0);
    }
}
