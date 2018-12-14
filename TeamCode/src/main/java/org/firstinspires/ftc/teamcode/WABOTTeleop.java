package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "WABOTTeleop", group = "WABOT")
public class WABOTTeleop extends OpMode {

    public WABOTHardwareMap m;

    double rotation, turn, position = 0.7;
    final double SLOW = 0.5;
    final double SLOWER = 0.25;
    boolean noHolo = false, enableN = true;

    public void init() {
        //m.initMap();
        //m.initDevices();
    }

    public void loop() {
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = -gamepad1.left_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        double rightStickY = -gamepad1.right_stick_y;

        //m.intakeMotor.setPower(gamepad2.left_stick_y);
        //m.armMotor.setPower(gamepad2.right_stick_y);
        //m.liftMotor.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

        if (noHolo) {
            leftStickX = 0;
        }

        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - (Math.PI / 4); /*- gyroSensor.getHeading()*/
        double rightX = rightStickX;
        turn = rightX;

        double v1 = r * Math.cos(robotAngle) + turn;
        double v2 = r * Math.sin(robotAngle) - turn;
        double v3 = r * Math.sin(robotAngle) + turn;
        double v4 = r * Math.cos(robotAngle) - turn;

        if (gamepad1.right_bumper) {
            v1 *= SLOW;
            v2 *= SLOW;
            v3 *= SLOW;
            v4 *= SLOW;
        } else if (gamepad1.left_bumper) {
            v1 *= SLOWER;
            v2 *= SLOWER;
            v3 *= SLOWER;
            v4 *= SLOWER;

        }else {
            double largest = Math.abs(v1);
            if (largest < Math.abs(v2)) {
                largest = Math.abs(v2);
            }
            if (largest < Math.abs(v3)) {
                largest = Math.abs(v3);
            }
            if (largest < Math.abs(v4)) {
                largest = Math.abs(v4);
            }
            if (largest != 0) {
                v1 /= largest;
                v2 /= largest;
                v3 /= largest;
                v4 /= largest;

                v1 *= (Math.abs(r) > Math.abs(turn) ? Math.abs(r) : Math.abs(turn));
                v2 *= (Math.abs(r) > Math.abs(turn) ? Math.abs(r) : Math.abs(turn));
                v3 *= (Math.abs(r) > Math.abs(turn) ? Math.abs(r) : Math.abs(turn));
                v4 *= (Math.abs(r) > Math.abs(turn) ? Math.abs(r) : Math.abs(turn)) ;
            }
        }

        telemetry.addData("r", r);
        telemetry.addData("v1:", v1);
        telemetry.addData("v2:", v2);
        telemetry.addData("v3:", v3);
        telemetry.addData("v4:", v4);
        telemetry.addData(" r:", r);
        telemetry.addData("Lx: ", leftStickX + " Ly: " + leftStickY);

        m.FLMotor.setPower(v1);
        m.FRMotor.setPower(v2);
        m.BLMotor.setPower(v3);
        m.BRMotor.setPower(v4);

        telemetry.addLine().addData("LeftX", gamepad1.left_stick_x).addData("LeftY", -gamepad1.left_stick_y);

    }
}
