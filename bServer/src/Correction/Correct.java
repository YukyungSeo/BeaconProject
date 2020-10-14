package Correction;

import java.util.ArrayList;

public class Correct {
    int sizeX;
    int sizeY;
    double LengthX;
    double LengthY;
    double ErrorRange;
    DBConnector dbConnector;
    ArrayList<Phone> List;

    public Correct(int sizeX, int sizeY, double lengthX, double lengthY, ArrayList<Phone> list) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.LengthX = lengthX;
        this.LengthY = lengthY;
        this.ErrorRange = this.LengthX / this.sizeX;
        this.List = list;
        this.dbConnector = new DBConnector();
    }

    public boolean run() throws ClassNotFoundException {
        boolean[] result = new boolean[4];

        for (int index = 0; index < List.size(); index++) {
            result[index] = correction(List.get(index));
        }
        return true;
    }

    public boolean correction(Phone phone) throws ClassNotFoundException {
        int phoneX = phone.getX();
        int phoneY = phone.getY();

        int correctcount = 0;

        for (int i = 0; i < List.size(); i++) {
            Phone otherPhone = List.get(i);
            int otherPhoneX = otherPhone.getX();
            int otherPhoneY = otherPhone.getY();
            double rssiDistance = getDistance(phone, otherPhone);

            double gridDistance = Math.sqrt((Math.pow((phoneX - otherPhoneX) * (LengthX / sizeX), 2)
                    + Math.pow((phoneY - otherPhoneY) * (LengthY / sizeY), 2)));

            if (gridDistance - ErrorRange < rssiDistance && rssiDistance < gridDistance + ErrorRange) {
                correctcount++;
            }
        }
        if (correctcount > List.size() / 2) {
            return true;
        }
        return false;
    }

    public double getDistance(Phone phoneSender, Phone phoneReceiver) throws ClassNotFoundException {
        return dbConnector.connectANDquery(phoneSender.getID(), phoneReceiver.getID());
    }
}
