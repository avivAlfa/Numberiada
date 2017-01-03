package game;

/**
 * Created by alfav on 12/13/2016.
 */
public class PoolElement {
    int num1;
    int num2;

    public PoolElement() {}

    public PoolElement(int number, int numOfImp) {
        this.num1 = number;
        this.num2 = numOfImp;
    }

    public int getNumber() {
        return num1;
    }

    public int getNum1() {
        return num1;
    }

    public int getNum2() { return num2; }

    public void setNum1(int num1) { this.num1 = num1; }

    public void setNum2(int num2) { this.num2 = num2; }

    public void decreaseNum2() {
        num2--;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!PoolElement.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final PoolElement other = (PoolElement) obj;

        if (this.num1 != other.num1 || this.num2 != other.num2) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.num1;
        hash += 53 * hash + this.num2;
        return hash;
    }

}
