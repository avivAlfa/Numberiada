
public class Cell {
    private char value;
    boolean isEmpty = false;

    Cell(){}
    Cell(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setAsEmpty(){
        this.isEmpty = true;
        this.setValue(' ');
    }

    public void setAsCursor(){
        this.isEmpty = true;
        this.setValue('@');
    }
}
