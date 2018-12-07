package ac.origination;

import ac.common.BlockId;

public class BlockInfo<T> {
    int id;
    T leftValue;
    T rightValue;
    T acc;


    @Override
    public String toString() {
        return "BlockInfo{" +
                "id=" + id +
                ", leftValue=" + leftValue +
                ", rightValue=" + rightValue +
                ", acc=" + acc +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(T leftValue) {
        this.leftValue = leftValue;
    }

    public T getRightValue() {
        return rightValue;
    }

    public void setRightValue(T rightValue) {
        this.rightValue = rightValue;
    }

    public T getAcc() {
        return acc;
    }

    public void setAcc(T acc) {
        this.acc = acc;
    }


    public BlockInfo(int id, T leftValue, T rightValue, T acc) {
        this.id = id;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.acc = acc;
    }
}
