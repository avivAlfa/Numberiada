
public class Cell {
    private char value;
    private boolean isEmpty = false;

    Cell(){}
    Cell(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
        this.isEmpty = false;
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public void setAsEmpty(){
        this.isEmpty = true;
    }

    public void setAsCursor(){
        this.isEmpty = true;
        this.setValue('@');
    }
}
