package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

/**
 * OFFICIAL AUTONOMOUS CODE
 *
 * WRIGHT ANGLE ROBOTICS (Skystone 2019-2020)
 *
 * Contributor:
 * Owen Boseley (2016-2021)
 *
 * NOTES:
 * - Anything marked with the comment (// DO NOT TOUCH) should not be touched xD
 *
 *
 */


@Autonomous(name="PWABOTAutonomous", group="PWABOT")
public class PWABOTAutonomous extends LinearOpMode {


    // This provides the tick count for each rotation of an encoder, it's helpful for using run to position
    private final int ENCODER_TICK = 1680;

    // NOTE: Measured in cm
    private final double DIAMETER = 10.12;

    // This value is the distance of 1 rev of the wheels measured in CM!!!!
    private final double CIRCUMFERENCE = Math.PI*DIAMETER;

    // Parameters for initializing vuforia
    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = FRONT;

    // Our custom vuforia object
    private WABOTVuforia vuforia;

    // Hardware map object
    private PWABOTHardware h;



















    // Initializing robot here!
    @Override
    public void runOpMode() {

        // Init of robot

        telemetry.addLine("READ ME --> !!!!UNTIL YOU SEE: \"READY\" DO NOT PRESS PLAY!!!!");
        telemetry.update();

        h = new PWABOTHardware(hardwareMap);

        telemetry.addLine("Status: Hardware Map");
        telemetry.update();

        runEncoder(false);

        telemetry.addLine("Status: Initializing Vuforia");
        telemetry.update();

        vuforia = new WABOTVuforia(VUFORIA_KEY, CAMERA_DIRECTION, hardwareMap, true);

        telemetry.addLine("Status: Calibrating Gyro");
        telemetry.update();

        h.gyro.calibrate();

        while(h.gyro.isCalibrating()){
            // Left blank
        }

        vuforia.activate();

        telemetry.addLine("Status: READY!");
        telemetry.update();


        waitForStart();

        run();
    }















    // Actual instructions for robot! All autonomous code goes here!!!
    private void run(){
        while (opModeIsActive()) {
            driveStraight(0, 0.5);
        }
    }
















    // Uses encoders to move a specific distance away given powers for each motor
    /*private void runToPos(int distanceCM, float power1, float power2, float power3, float power4){
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        runEncoder(true);
        h.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FLMotor.setTargetPosition(ticksToRun);
        h.FRMotor.setTargetPosition(ticksToRun);
        h.BLMotor.setTargetPosition(ticksToRun);
        h.BRMotor.setTargetPosition(ticksToRun);
        h.FLMotor.setPower(power1);
        h.FRMotor.setPower(power2);
        h.BLMotor.setPower(power3);
        h.BRMotor.setPower(power4);
        while (h.FLMotor.isBusy() && h.FRMotor.isBusy() && h.BLMotor.isBusy() && h.BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
        runEncoder(false);
    }*/















    // DO NOT TOUCH
    // Robot moves some distance (CM) with a specified power applied
    private void runToPos(int distanceCM, float power){
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        runEncoder(true);
        h.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FLMotor.setTargetPosition(ticksToRun);
        h.FRMotor.setTargetPosition(ticksToRun);
        h.BLMotor.setTargetPosition(ticksToRun);
        h.BRMotor.setTargetPosition(ticksToRun);
        h.FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        h.BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        h.FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        h.BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        linearDrive(power);
        while (h.FLMotor.isBusy() && h.FRMotor.isBusy() && h.BLMotor.isBusy() && h.BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
        runEncoder(false);
    }
















    // DO NOT TOUCH
    // Switch between non-encoder and encoder modes of the motors
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















    // DO NOT TOUCH
    // Runs motors at a specified power
    private void linearDrive(float power){
        h.FLMotor.setPower(power);
        h.FRMotor.setPower(power);
        h.BLMotor.setPower(power);
        h.BRMotor.setPower(power);
    }















    // DO NOT TOUCH
    // Same as "linearDrive" but sets power to 0
    private void stopMotors() {
        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }
















    // DO NOT TOUCH
    // NOTE: Used only with mecanum wheels!
    // Strafes based on power and distance (NOTE: Negative distance is allowed!)
    /*private void strafe (int distanceCM, float power){
        if(distanceCM < 0){
            distanceCM *= -1;
            runToPos(distanceCM, -power, power, power,-power);
        } else if (distanceCM > 0){
            runToPos(distanceCM, power, -power, -power,power);
        }
    }*/














    // DO NOT TOUCH
    // Turns robot
    private void turn (int direction, float power){
        if(direction == -1){
            h.FLMotor.setPower(-power);
            h.BRMotor.setPower(power);
            h.FRMotor.setPower(power);
            h.BLMotor.setPower(-power);
        } else if (direction == 1){
            h.FLMotor.setPower(power);
            h.BRMotor.setPower(-power);
            h.FRMotor.setPower(-power);
            h.BLMotor.setPower(power);
        }
    }















    // IN PROGRESS
    // Maintains heading and adjusts if pushed
    private void driveStraight(int targetHeading, double startSpeed){
        int heading = h.gyro.getHeading();
        if(heading > 180){
            heading = heading - 360;
        }

        int difference = targetHeading - heading;
        double power = difference/90.0;

        h.FLMotor.setPower(startSpeed + power);
        h.FRMotor.setPower(startSpeed - power);
        h.BLMotor.setPower(startSpeed + power);
        h.BRMotor.setPower(startSpeed - power);

    }



















    // DO NOT TOUCH
    // Usable is there is a gyro installed
    private void turnByDegree (int degree) {
        double currentPower = 1;
        boolean right;
        double turnTo;

        if(degree < 0){
            right = false;
            turnTo = convertedHeading((degree*-1) + h.gyro.getHeading());
        } else {
            right = true;
            turnTo = convertedHeading(degree + h.gyro.getHeading());
        }

        // if to the right, turn right, vise versa
        if (right) {
            while (convertedHeading(h.gyro.getHeading()) < turnTo) {

                double difference = turnTo - convertedHeading(h.gyro.getHeading());

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(h.gyro.getHeading()));
                telemetry.addData("CurrentPower: ", currentPower);
                telemetry.update();


                h.FLMotor.setPower(currentPower);
                h.FRMotor.setPower(-currentPower);
                h.BLMotor.setPower(currentPower);
                h.BRMotor.setPower(-currentPower);

                if(difference < 12){
                    currentPower = Math.pow(1.2, 0.3*difference) - 1;
                }
            }
        } else if (!right) {
            while (convertedHeading(h.gyro.getHeading()) < turnTo) {

                double difference = turnTo - convertedHeading(h.gyro.getHeading());

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(h.gyro.getHeading()));
                telemetry.addData("CurrentPower: ", currentPower);
                telemetry.update();

                h.FLMotor.setPower(-currentPower);
                h.FRMotor.setPower(currentPower);
                h.BLMotor.setPower(-currentPower);
                h.BRMotor.setPower(currentPower);

                if(difference < 12){
                    currentPower = Math.pow(1.2, 0.3*difference) - 1;
                }
            }
        }

        stopMotors();
    }


















    // DO NOT TOUCH
    // Used for "turn by degree" ONLY
    public int convertedHeading(int h){
        int heading = h;
        while(heading > 180){
            heading -= (2*(heading-180));

            if(heading < 0){
                heading *= -1;
            }
        }
        return heading;
    }
}
