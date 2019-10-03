package org.firstinspires.ftc.teamcode;

/*
 * Wright Angle Robotics #6427 2019-2020
 *
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="WABOTTeleop", group="WABOT")
//@Disabled
public class  WABOTTeleop extends OpMode {
    // Declare OpMode members.
    WABOTHardware h;
    double left = 1;
    double right = 0;

    // Constant
    private final double PRECISION_SPEED_MODIFIER = 0.5;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        final double PRECISION_SPEED_MODIFIER = 0.5;
        // Tell the driver that initialization is complete.
        h = new WABOTHardware(hardwareMap);
        h.leftLatch.setPosition(0);
        h.rightLatch.setPosition(1);
        runEncoder(false);
        telemetry.addData("Status", "Initialized");

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

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //if(gamepad2.dpad_down){
        //    h.leftLatch.setPosition(0.8);
        //    h.rightLatch.setPosition(0.8);
        //}
        //if(gamepad2.dpad_up){
        //    h.leftLatch.setPosition(0);
        //    h.rightLatch.setPosition(0);
        //}


        // SERVO RANGE: LEFT: 0.0 (CLOSED) - 0.6 RIGHT: 1.0 (CLOSED) - 0.4
        telemetry.addData("Right Position: ", right);
        telemetry.addData("Left Position: ", left);
        telemetry.update();

        telemetry.addData("input y: ", gamepad2.left_stick_y);
        telemetry.update();

        holoDrive();
        h.leftLatch.setPosition(left);
        h.rightLatch.setPosition(right);
        if(gamepad2.left_bumper){
            left += 0.1;
        }
        if(gamepad2.left_bumper){
            left -= 0.1;
        }

        h.leftIntake.setPower(gamepad2.left_stick_y);
        h.rightIntake.setPower(gamepad2.right_stick_y);

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

    private void runEncoder(boolean withEncoder){
        if(withEncoder) {
            h.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            h.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }else{
            h.FLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.FRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.BLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            h.BRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

    }

    private void tankDrive(){
        double leftStickY = gamepad1.left_stick_y;
        double rightStickY = gamepad1.right_stick_y;

        h.FLMotor.setPower(leftStickY);
        h.FRMotor.setPower(rightStickY);
        h.BLMotor.setPower(leftStickY);
        h.BRMotor.setPower(rightStickY);
    }

    private void holoDrive(){
        double leftStickX = -gamepad1.right_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = -gamepad1.left_stick_x;
        double rightStickY = -gamepad1.right_stick_y;

        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - (Math.PI / 4);
        double rightX = rightStickX;
        double turn = rightX;

        double v1 = r * Math.cos(robotAngle) + turn;
        double v2 = r * Math.sin(robotAngle) - turn;
        double v3 = r * Math.sin(robotAngle) + turn;
        double v4 = r * Math.cos(robotAngle) - turn;

        if(gamepad1.right_bumper || gamepad1.left_bumper){
            v1 = v1 * PRECISION_SPEED_MODIFIER;
            v2 = v2 * PRECISION_SPEED_MODIFIER;
            v3 = v3 * PRECISION_SPEED_MODIFIER;
            v4 = v4 * PRECISION_SPEED_MODIFIER;
        }
        h.FLMotor.setPower(v1);
        h.FRMotor.setPower(v2);
        h.BLMotor.setPower(v3);
        h.BRMotor.setPower(v4);
    }

}
