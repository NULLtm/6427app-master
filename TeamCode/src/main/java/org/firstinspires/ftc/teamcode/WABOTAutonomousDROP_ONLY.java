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
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name="WABOTAutonomousDROP", group="WABOT")
public class WABOTAutonomousDROP_ONLY extends LinearOpMode {

    // variable set up here!
    DcMotor FLMotor;
    DcMotor FRMotor;
    DcMotor BLMotor;
    DcMotor BRMotor;

    DcMotor liftMotor;
    DcMotor armMotor;
    DcMotor intakeMotor;
    DcMotor gearboxMotor;

    Servo markerServo;
    DistanceSensor ods;
    ColorSensor color1;
    BNO055IMU imu;

    Orientation angles;

    // This provides the tick count for each rotation of an encoder, it's helpful for using run to position
    private final int ENCODER_TICK = 1440;

    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";

    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;
    List<VuforiaTrackable> allTrackables;
    VuforiaTrackables targetsRoverRuckus;

    final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    final String LABEL_GOLD_MINERAL = "Gold Mineral";
    final String LABEL_SILVER_MINERAL = "Silver Mineral";

    List<Recognition> updatedRecognitions;

    TFObjectDetector tfod;

    @Override
    public void runOpMode() {

        // Init of robot

        telemetry.addLine("Loading Robot... Please Wait");
        telemetry.update();

        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");

        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        gearboxMotor = hardwareMap.get(DcMotor.class, "gearboxMotor");

        markerServo = hardwareMap.get(Servo.class, "markerServo");
        ods = hardwareMap.get(DistanceSensor.class, "ods");
        imu = hardwareMap.get(BNO055IMU.class, "imu");


        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gearboxMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        runEncoder(true);

        telemetry.addLine("Hardware Map Complete!");
        telemetry.update();

        telemetry.addLine("Starting up IMU...");
        telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);

        telemetry.addLine("Starting Vuforia...");
        telemetry.update();

        VuforiaLocalizer.Parameters vuParameters = new VuforiaLocalizer.Parameters();

        vuParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuParameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        vuforia = ClassFactory.getInstance().createVuforia(vuParameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

        telemetry.addLine("Vuforia and Tensorflow Complete");
        telemetry.update();

        telemetry.addLine("Calibrating Gyro...");
        telemetry.update();

        markerServo.setPosition(1f);

        while (!imu.isGyroCalibrated()) {
        }

        if (tfod != null) {
            tfod.activate();
        }

        telemetry.addLine("Init Complete! Ready to Go!");
        telemetry.update();

        waitForStart();

        // Our auto while loop body
        while (opModeIsActive()) {
            // First, detect any minerals and drive forward

            liftMotor.setPower(-1f);

            while(ods.getDistance(DistanceUnit.CM)>9.1){

            }

            sleep(600);

            stopMotors();

            runToPos(1, 0.5f);

            strafe(-1, 1f);

            sleep(1000);

            stopMotors();

            turnByDegree(90, 0.5f);

            runToPos(4, 0.5f);



            return;
        }

        stopMotors();

    }

    // Updates the current heading value for our imu
    private float getHeading(){
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return angles.firstAngle;
    }

    // Updates gold position using tensor flow
    private int runTFod(){
        if (tfod != null) {
            updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() != 0) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1) {
                        if (goldMineralX < silverMineral1X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            return 1;
                        } else if (goldMineralX > silverMineral1X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
                telemetry.update();
            }
        }
        return 10;
    }

    // Uses encoders to move a specific distance away
    private void runToPos(float revolution, float power){
        int ticksToRun = Math.round(revolution * ENCODER_TICK);
        runEncoder(true);
        resetEncoder();
        FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FLMotor.setTargetPosition(ticksToRun);
        FRMotor.setTargetPosition(ticksToRun);
        BLMotor.setTargetPosition(ticksToRun);
        BRMotor.setTargetPosition(ticksToRun);
        linearDrive(power);
        while (FLMotor.isBusy() && FRMotor.isBusy() && BLMotor.isBusy() && BRMotor.isBusy()){
            //This page was intentionally left blank
        }
        stopMotors();
        runEncoder(false);
    }

    // Set's encoder values to zero
    private void resetEncoder(){
        FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // Switch between non-encoder and encoder modes of the motors
    private void runEncoder(boolean withEncoder){
        if(withEncoder) {
            FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }else{
            FLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            FRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

    }

    // [DEPRECATED] A flexible form of driving
    private void dynamicDrive(double angle, double power, double aTurn) {
        double r = power;
        double robotAngle = (angle % 360 - 45) * Math.PI / 180;
        double turn = aTurn;
        final double MAX_SPEED = 0.5;
        double v1 = MAX_SPEED * (r * Math.cos(robotAngle) + turn);
        double v2 = MAX_SPEED * (r * Math.sin(robotAngle) - turn);
        double v3 = MAX_SPEED * (r * Math.sin(robotAngle) + turn);
        double v4 = MAX_SPEED * (r * Math.cos(robotAngle) - turn);

        FLMotor.setPower(v1);
        FRMotor.setPower(v2);
        BLMotor.setPower(v3);
        BRMotor.setPower(v4);

    }

    // A simple drive method (linear)
    private void linearDrive(float power){
        FLMotor.setPower(power);
        FRMotor.setPower(power);
        BLMotor.setPower(power);
        BRMotor.setPower(power);
    }

    // Sets all motor powers to zero.
    private void stopMotors() {
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);
        liftMotor.setPower(0);

    }

    // A simple strafing method that takes a direction (-1 = left, 1 = right)
    private void strafe (int direction, float power){
        runEncoder(true);
        if(direction == -1){
            FLMotor.setPower(-power);
            BRMotor.setPower(-power);
            FRMotor.setPower(power);
            BLMotor.setPower(power);
        } else if (direction == 1){
            FLMotor.setPower(power);
            BRMotor.setPower(power);
            FRMotor.setPower(-power);
            BLMotor.setPower(-power);
        }
    }

    // A simple tank turn method (-1 = left, 1 = right)
    private void turn (int direction, float power){
        runEncoder(true);
        if(direction == -1){
            FLMotor.setPower(-power);
            BRMotor.setPower(power);
            FRMotor.setPower(power);
            BLMotor.setPower(-power);
        } else if (direction == 1){
            FLMotor.setPower(power);
            BRMotor.setPower(-power);
            FRMotor.setPower(-power);
            BLMotor.setPower(power);
        }
    }

    // Uses the built in REV imu as a gyro and turns a certain degree
    private void turnByDegree (int degree, float power) {

        if(degree > 0){
            degree -= 9;
        } else if (degree < 0){
            degree += 9;
        }

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

}
