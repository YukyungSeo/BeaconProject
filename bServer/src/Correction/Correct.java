package Correction;

import java.util.ArrayList;
import pack.MakeSpline;
import pack.SplineGraph;

import pack.MakeSpline;

public class Correct {
    int sizeX;
    int sizeY;
    double LengthX;
    double LengthY;
    double ErrorRange;
    MakeSpline sp;

    ArrayList<Phone> List = new ArrayList();

    public Correct(int sizeX, int sizeY, double lengthX, double lengthY, ArrayList<Phone> list,MakeSpline ms) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.LengthX = lengthX;
        this.LengthY = lengthY;
        this.ErrorRange = this.LengthX / this.sizeX;
        this.List = list;
        this.sp=ms;
    }

    public boolean run() {
        boolean[] result = new boolean[4];

        for (int index = 0; index < List.size(); index++) {
            result[index] = correction(List.get(index), index);
        }
        return true;
    }

    public boolean correction(Phone phone, int index) {
        int phoneX = phone.getX();
        int phoneY = phone.getY();

        int correctcount = 0;

        for (int i = index; i < List.size(); i++) {
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

    public double getDistance(Phone phoneSender, Phone phoneReceiver) {
        // 채워야함
        double rssi = 0.;
        sp.checkIDandInsertX(phoneSender.getID(), phoneReceiver.getID(), rssi);
        return 0.;
    }
}
