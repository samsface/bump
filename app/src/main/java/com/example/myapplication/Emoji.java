package com.example.myapplication;

public class Emoji {

    public String body = "";
    public String leftArm = "";
    public String rightArm = "";

    public Emoji(float motion) {

        System.out.println(motion);

        if (motion <= 0.2) body = "(•‿•)";
        else body = "(ﾟДﾟ)";

        if (motion <= 0.2) {
            leftArm = " ";
            rightArm = " ";
        }
        else {
            leftArm = "ヽ";
            rightArm = "ﾉ";
        }
    }

    public String toString() {
        return leftArm + body + rightArm;
    }
}
