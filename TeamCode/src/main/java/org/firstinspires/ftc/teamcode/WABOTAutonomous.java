package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name="WABOTAutonomous", group="WABOT")
public class WABOTAutonomous extends LinearOpMode {

    // This provides the tick count for each rotation of an encoder, it's helpful for using run to position, 10CM DIAMETER
    private final int ENCODER_TICK = 1440;

    private final int DIAMETER = 10;

    // This value is the distance of 1 rev of the wheels measured in CM!!!!
    private final double CIRCUMFERENCE = 2*Math.PI*DIAMETER;

    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;
    List<VuforiaTrackable> allTrackables;
    VuforiaTrackables targetsRoverRuckus;

    private WABOTHardware h = new WABOTHardware(hardwareMap);

    @Override
    public void runOpMode() {

        // Init of robot

        telemetry.addLine("Loading Robot... Please Wait");
        telemetry.update();

        runEncoder(true);

        telemetry.addLine("Hardware Map Complete!");
        telemetry.update();

        telemetry.addLine("Starting up IMU...");
        telemetry.update();

        telemetry.addLine("Starting Vuforia...");
        telemetry.update();

        VuforiaLocalizer.Parameters vuParameters = new VuforiaLocalizer.Parameters();
        vuParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuParameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.getInstance().createVuforia(vuParameters);

        telemetry.addLine("Vuforia Complete");
        telemetry.update();

        telemetry.addLine("Calibrating Gyro...");
        telemetry.update();

        telemetry.addLine("Init Complete! Ready to Go!");
        telemetry.update();

        waitForStart();
    }

    // Uses encoders to move a specific distance away
    private void runToPos(float revolution, float power){
        int ticksToRun = Math.round(revolution * ENCODER_TICK);
        runEncoder(true);
        resetEncoder();
        h.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        h.FLMotor.setTargetPosition(ticksToRun);
        h.FRMotor.setTargetPosition(ticksToRun);
        h.BLMotor.setTargetPosition(ticksToRun);
        h.BRMotor.setTargetPosition(ticksToRun);
        linearDrive(power);
        while (h.FLMotor.isBusy() && h.FRMotor.isBusy() && h.BLMotor.isBusy() && h.BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
        runEncoder(false);
    }

    // Set's encoder values to zero
    private void resetEncoder(){
        h.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        h.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // Switch between non-encoder and encoder modes of the motors
    private void runEncoder(boolean withEncoder){
        if(withEncoder) {
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

    private void linearDrive(float power){
        h.FLMotor.setPower(power);
        h.FRMotor.setPower(power);
        h.BLMotor.setPower(power);
        h.BRMotor.setPower(power);
    }

    private void stopMotors() {
        h.FLMotor.setPower(0);
        h.FRMotor.setPower(0);
        h.BLMotor.setPower(0);
        h.BRMotor.setPower(0);
    }

    private void strafe (int direction, float power){
        runEncoder(true);
        if(direction == -1){
            h.FLMotor.setPower(-power);
            h.BRMotor.setPower(-power);
            h.FRMotor.setPower(power);
            h.BLMotor.setPower(power);
        } else if (direction == 1){
            h.FLMotor.setPower(power);
            h.BRMotor.setPower(power);
            h.FRMotor.setPower(-power);
            h.BLMotor.setPower(-power);
        }
    }

    private void turn (int direction, float power){
        runEncoder(true);
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

    /*private void turnByDegree (int degree, float power) {

        // find new angle to rotate to
        float turnTo = degree + getHeading();

        // convert new angle into our "gyro"'s scope
        if(turnTo >= 180){
            turnTo = -180-(turnTo-180);
        } else if(turnTo <= -180){
            turnTo = 180+(Math.abs(turnTo-180));
        }


        runEncoder(true);

        // if to the right, turn right, vise versa
        if(getHeading() < turnTo){

            FLMotor.setPower(-power);
            FRMotor.setPower(power);
            BLMotor.setPower(-power);
            BRMotor.setPower(power);

            while(getHeading() < turnTo){
            }
        } else if (getHeading() > turnTo){
            FLMotor.setPower(power);
            FRMotor.setPower(-power);
            BLMotor.setPower(power);
            BRMotor.setPower(-power);

            while(getHeading() > turnTo){
            }
        }

        stopMotors();

    }
    */

}
