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
    float intakePow = 0;

    boolean normalS = true;

    double as1 = 0;
    double as2 = 0;
    double as3 = 0;
    double motorPos = 0;

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
        h.leftLatch.setPosition(0.3f);
        h.rightLatch.setPosition(0.9f);
        h.foundServo.setPosition(0.5f);
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
        holoDrive2();
        controllerTwo();




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


    private void controllerTwo(){
        /* ~~~INTAKE~~~ */
        if(gamepad2.right_trigger > 0){
            intakePow = gamepad2.right_trigger;
        }else if(gamepad2.left_trigger > 0){
            intakePow = -gamepad2.left_trigger;
        } else {
            intakePow = 0;
        }

        h.leftIntake.setPower(intakePow);
        h.rightIntake.setPower(intakePow);

        // IMPORTANT: Ideal servo pos for intake: LEFT: 0.4 RIGHT: 0.5
        if(gamepad2.a){
            h.leftLatch.setPosition(0.4f);
            h.rightLatch.setPosition(0.5f);
        }

        if(gamepad2.b){
            h.leftLatch.setPosition(0.3f);
            h.rightLatch.setPosition(0.9f);
        }
        /* ~~~END INTAKE~~~ */

        // 1 = middle 2 = top 3 = end
        /* ~~~Output~~~ */
        h.armMotor.setPower(gamepad2.right_stick_y);

        motorPos += gamepad2.right_stick_y;

        as1 += 0.005 * gamepad2.left_stick_x;
        as2 += 0.005 * gamepad2.left_stick_y;
        as3 += 0.005 * gamepad2.right_stick_x;

        as1 = clamp(0.2, 0.8, as1);
        as2 = clamp(0, 1, as2);
        as3 = clamp(0, 1, as3);

        h.armServo1.setPosition(as1);
        h.armServo2.setPosition(as2);
        //h.armServo3.setPosition(as3);

        telemetry.addData("Motor Position: ", motorPos);

//        if(gamepad2.dpad_down){
//            as1 += 0.1;
//        }
//        if(gamepad2.dpad_up){
//            as1 -= 0.1;
//        }
//
//        if(gamepad2.dpad_left){
//           as2 += 0.1;
//        }
//
//        if(gamepad2.dpad_right){
//            as2 -= 0.1;
//        }
//
//        if(gamepad2.left_bumper){
//            as3 += 0.1;
//        }
//        if(gamepad2.right_bumper){
//            as3 -= 0.1;
//        }

        /* ~~~END Output~~~ */
    }
// RIGHT IS IN
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

    private void owenDrive(){
        double leftStickX = -gamepad1.right_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = -gamepad1.left_stick_x;
        double rightStickY = -gamepad1.right_stick_y;

        double r = Math.hypot(rightStickX, rightStickY);
        double robotAngle = Math.atan2(rightStickY, rightStickX);

        double v1 = rightStickY + r * Math.cos(robotAngle);
        double v2 = rightStickY - r * Math.sin(robotAngle);
        double v3 = rightStickY + r * Math.sin(robotAngle);
        double v4 = rightStickY - r * Math.cos(robotAngle);

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

    private void holoDrive(){
        double leftStickX = -gamepad1.right_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = -gamepad1.left_stick_x;
        double rightStickY = -gamepad1.right_stick_y;

        double r = Math.hypot(rightStickX, rightStickY);
        double robotAngle = Math.atan2(rightStickY, rightStickX) - (Math.PI / 4);
        double leftX = leftStickX;
        double turn = leftX;

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

    private void holoDrive2(){
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = gamepad1.right_stick_y;
        double rightStickX = gamepad1.right_stick_x;
        double rightStickY = -gamepad1.left_stick_y;

        double r = Math.hypot(leftStickX, rightStickY);
        double robotAngle = Math.atan2(rightStickY, leftStickX) - (Math.PI / 4);
        double turn = rightStickX;

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

    public double clamp(double min, double max, double value){
        if(value < min){
            value = min;
        } else if(value > max){
            value = max;
        }

        return value;
    }

}
