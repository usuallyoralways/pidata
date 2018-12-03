package ac.client;

public class PointIndex {
    int blockid;//桶的id
    int hash;   //哈希的值

    @Override
    public String toString() {
        return "PointIndex{" +
                "blockid=" + blockid +
                ", hash=" + hash +
                '}';
    }

    public int getBlockid() {
        return blockid;
    }

    public void setBlockid(int blockid) {
        this.blockid = blockid;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
