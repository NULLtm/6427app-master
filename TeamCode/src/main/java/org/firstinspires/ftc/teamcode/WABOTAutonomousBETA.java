package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.WABOTHardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

/*
Official Autonomous 6427 Wright Angles
--------------------------------------

Current Programmers (Task # Working On):
- Connor
- Jessica
- Owen (#1)
- Louis
- Jack
- Ryan
- Jaden

Task List:
1. Find consistent way to scan each of the little elements in checkBlocks();
2. Find consistent way to put marker in safe zone after detecting blocks in markerPlace();
3. Test out vuforia's distance, to see how much assistance we need to give it
4. Should we have multiple op modes for autonomous? or just one.
 */
@Disabled
@Autonomous(name = "WABOTDriveForward", group = "WABOT_BETA")
public class WABOTAutonomousBETA extends LinearOpMode {

    WABOTHardwareMap m;


    @Override
    public void runOpMode() throws InterruptedException {

        m = new WABOTHardwareMap(hardwareMap);

        m.setSide("blue");
        m.setSlot(true);

        m.initMap();
        m.initDevices();

        linearDrive(-0.5f);

        sleep(2000);

        linearDrive(0f);



    }

    // angle = Angle to strafe at if aTurn is 0, Power = POWWWER, aTurn = Angle to turn at
    private void dynamicDrive(double angle, double power, double aTurn) {
        double r = power;
        double robotAngle = (angle % 360 - 45) * Math.PI / 180;
        double turn = aTurn;
        final double MAX_SPEED = 0.5;
        double v1 = MAX_SPEED * (r * Math.cos(robotAngle) + turn);
        double v2 = MAX_SPEED * (r * Math.sin(robotAngle) - turn);
        double v3 = MAX_SPEED * (r * Math.sin(robotAngle) + turn);
        double v4 = MAX_SPEED * (r * Math.cos(robotAngle) - turn);

        m.FLMotor.setPower(v1);
        m.FRMotor.setPower(v2);
        m.BLMotor.setPower(v3);
        m.BRMotor.setPower(v4);

    }
    private void linearDrive(double power){
        m.FLMotor.setPower(power);
        m.FRMotor.setPower(power);
        m.BLMotor.setPower(power);
        m.BRMotor.setPower(power);
    }
    private void stopMotors() {
        m.FLMotor.setPower(0);
        m.FRMotor.setPower(0);
        m.BLMotor.setPower(0);
        m.BRMotor.setPower(0);
    }
}