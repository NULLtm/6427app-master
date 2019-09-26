package org.firstinspires.ftc.teamcode;

/*
 * Wright Angle Robotics #6427 2019-2020
 * This class is specific for the practice robot
 * It omits holonomic drive and other features
 * Focuses on testing
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="WABOTPracticeTeleop", group="WABOT")
//@Disabled
public class PWABOTTeleop extends OpMode {

    public PWABOTHardware h;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Tell the driver that initialization is complete.
        telemetry.addData("Status:", "Initializing");

        h = new PWABOTHardware(hardwareMap);

        h.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        h.gyro.calibrate();

        while(h.gyro.isCalibrating()){
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
        double rightStickY = gamepad1.right_stick_y;

        h.FLMotor.setPower(leftStickY);
        h.FRMotor.setPower(rightStickY);
        h.BLMotor.setPower(leftStickY);
        h.BRMotor.setPower(rightStickY);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status:", "Stopped");

        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);

    }
}
