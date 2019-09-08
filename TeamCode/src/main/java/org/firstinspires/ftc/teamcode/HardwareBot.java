package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;

enum MOTORSTATE {
    DEFAULT, REVERSE, ENCODER, OFF;
}

public class HardwareBot {
    public DcMotor backRightMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor frontLeftMotor;

    HardwareDevice[] motors = {frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor};

    public HardwareDevice[][] device = {
            motors,
    };

    public static void changeMotorState(MOTORSTATE m){
        if(m == MOTORSTATE.DEFAULT){
            for(int i = 0; i < motors.length; i++){
                ((DcMotor) motors[i]).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                ((DcMotor) motors[i]).setDirection(DcMotorSimple.Direction.FORWARD);
                ((DcMotor) motors[i]).setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
        if(m == MOTORSTATE.REVERSE){
            for(int i = 0; i < motors.length; i++){
                ((DcMotor) motors[i]).setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                ((DcMotor) motors[i]).setDirection(DcMotorSimple.Direction.REVERSE);
                ((DcMotor) motors[i]).setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
        if(m == MOTORSTATE.ENCODER) {
            for (int i = 0; i < motors.length; i++) {
                ((DcMotor) motors[i]).setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                ((DcMotor) motors[i]).setDirection(DcMotorSimple.Direction.FORWARD);
                ((DcMotor) motors[i]).setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
        }
        if(m == MOTORSTATE.OFF) {
            for (int i = 0; i < motors.length; i++) {
                ((DcMotor) motors[i]).setPower(0f);
            }
        }
    }
}
