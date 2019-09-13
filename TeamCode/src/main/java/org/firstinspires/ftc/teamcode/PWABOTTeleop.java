package org.firstinspires.ftc.teamcode;

/*
 * Wright Angle Robotics #6427 2019-2020
 * This class is specific for the practice robot
 * It omits holonomic drive and other features
 * Focuses on testing
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name="WABOTPracticeTeleop", group="WABOT")
//@Disabled
public class PWABOTTeleop extends OpMode {

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public DcMotor BLMotor;
    public DcMotor BRMotor;
    public ColorSensor color;
    public TouchSensor touch;
    public OpticalDistanceSensor ods;
    public GyroSensor gyro;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Tell the driver that initialization is complete.
        telemetry.addData("Status:", "Initializing");
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        ods = hardwareMap.get(OpticalDistanceSensor.class, "ods");
        color = hardwareMap.get(ColorSensor.class, "color");
        touch = hardwareMap.get(TouchSensor.class, "touch");
        gyro = hardwareMap.get(GyroSensor.class, "gyro");

        gyro.calibrate();

        while(gyro.isCalibrating()){
            // Left blank
        }

        // Once this message prints, driver is allowed to press start!
        telemetry.addData("Status:", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.addData("Status:", "Start");
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop(){
        double leftStickY = gamepad1.left_stick_y;
        double rightStickY = -gamepad1.right_stick_y;

        FLMotor.setPower(leftStickY);
        FRMotor.setPower(rightStickY);
        BLMotor.setPower(leftStickY);
        BRMotor.setPower(rightStickY);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status:", "Stopped");
    }
}
