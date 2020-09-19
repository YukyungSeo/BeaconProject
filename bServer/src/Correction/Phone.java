package Correction;

public class Phone {
    String ID;
    int X;
    int Y;

    public Phone(String id, int x, int y) {
        ID = id;
        X = x;
        Y = y;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
