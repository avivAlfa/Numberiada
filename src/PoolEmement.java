/**
 * Created by alfav on 12/13/2016.
 */
public class PoolEmement {
    int number;
    int numOfImp;

    PoolEmement(int number, int numOfImp) {
        this.number = number;
        this.numOfImp = numOfImp;
    }

    public int getNumber() {
        return number;
    }

    public int getNumOfImp() {
        return numOfImp;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setNumOfImp(int numOfImp) {
        this.numOfImp = numOfImp;
    }

    public void decreaseNumOfImp() {
        numOfImp--;
    }


}
