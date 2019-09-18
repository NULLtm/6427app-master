package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name="PWABOTAutonomous", group="WABOT")
public class PWABOTAutonomous extends LinearOpMode {

    // This provides the tick count for each rotation of an encoder, it's helpful for using run to position, 10CM DIAMETER
    private final int ENCODER_TICK = 1680;

    // NOTE: Diameter is dependent on wheels!
    private final double DIAMETER = 10.12;

    // This value is the distance of 1 rev of the wheels measured in CM!!!!
    private final double CIRCUMFERENCE = 2*Math.PI*DIAMETER;

    //private WABOTHardware h;

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public DcMotor BLMotor;
    public DcMotor BRMotor;
    public ColorSensor color;
    public TouchSensor touch;
    public OpticalDistanceSensor ods;
    public GyroSensor gyro;

    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = FRONT;

    private WABOTVuforia vuforia;

    @Override
    public void runOpMode() {

        // Init of robot

        telemetry.addLine("Loading Robot... Please Wait");
        telemetry.update();

        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        ods = hardwareMap.get(OpticalDistanceSensor.class, "ods");
        color = hardwareMap.get(ColorSensor.class, "color");
        touch = hardwareMap.get(TouchSensor.class, "touch");
        gyro = hardwareMap.get(GyroSensor.class, "gyro");

        FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Hardware Map Complete!");
        telemetry.update();

        runEncoder(true);

        telemetry.addLine("Encoders Functioning");
        telemetry.update();

        telemetry.addLine("Activating Vuforia...");
        telemetry.update();

        //vuforia = new WABOTVuforia(VUFORIA_KEY, CAMERA_DIRECTION);

        //vuforia.activate();

        // REST OF THE SET UP HERE!

        gyro.calibrate();

        while(gyro.isCalibrating()){
            // Left blank
        }

        telemetry.addLine("Init Complete! Ready to Go!");
        telemetry.update();

        waitForStart();

        runToPos(50, 0.5f);

        //sleep(500);

        //turnByDegree(90, 0.4f);

        //sleep(500);

        //runToPos(10, 0.3f);
    }




    // Uses encoders to move a specific distance away given powers for each motor
    private void runToPos(int distanceCM, float power1, float power2, float power3, float power4){
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        resetEncoder();
        runEncoder(true);
        FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FLMotor.setTargetPosition(ticksToRun);
        FRMotor.setTargetPosition(ticksToRun);
        BLMotor.setTargetPosition(ticksToRun);
        BRMotor.setTargetPosition(ticksToRun);
        FLMotor.setPower(power1);
        FRMotor.setPower(power2);
        BLMotor.setPower(power3);
        BRMotor.setPower(power4);
        while (FLMotor.isBusy() && FRMotor.isBusy() && BLMotor.isBusy() && BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
    }











    private void runToPos(int distanceCM, float power){
        double revs = distanceCM/CIRCUMFERENCE;
        int ticksToRun = (int)(revs * ENCODER_TICK);
        resetEncoder();
        runEncoder(true);
        FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FLMotor.setTargetPosition(ticksToRun);
        FRMotor.setTargetPosition(ticksToRun);
        BLMotor.setTargetPosition(ticksToRun);
        BRMotor.setTargetPosition(ticksToRun);
        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        linearDrive(power);
        while (FLMotor.isBusy() && FRMotor.isBusy() && BLMotor.isBusy() && BRMotor.isBusy()){
            //This line was intentionally left blank
        }
        stopMotors();
    }








    // Sets encoder values to zero
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





    private void linearDrive(float power){
        FLMotor.setPower(power);
        FRMotor.setPower(power);
        BLMotor.setPower(power);
        BRMotor.setPower(power);
    }

    private void stopMotors() {
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);
    }






    // Strafes based on power and distance (NOTE: Negative distance is allowed!)
    private void strafe (int distanceCM, float power){
        if(distanceCM < 0){
            distanceCM *= -1;
            runToPos(distanceCM, -power, power, power,-power);
        } else if (distanceCM > 0){
            runToPos(distanceCM, power, -power, -power,power);
        }
    }

    // Turns robot
    private void turn (int direction, float power){
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

    // Usable is there is a gyro installed
    private void turnByDegree (int degree, float power) {

        float currentPower = power;

        // find new angle to rotate to
        double turnTo = convertedHeading(degree + gyro.getHeading());

        // if to the right, turn right, vise versa
        if (convertedHeading(gyro.getHeading()) < turnTo) {
            while (convertedHeading(gyro.getHeading()) < turnTo) {

                double difference = turnTo - convertedHeading(gyro.getHeading());

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(gyro.getHeading()));
                telemetry.addData("To Heading: ", turnTo);
                telemetry.update();


                FLMotor.setPower(currentPower);
                FRMotor.setPower(-currentPower);
                BLMotor.setPower(currentPower);
                BRMotor.setPower(-currentPower);

                if(difference < 3.9){
                    currentPower *= Math.pow(1.2, difference) - 1;
                }
            }
        } else if (convertedHeading(gyro.getHeading()) > turnTo) {
            while (convertedHeading(gyro.getHeading()) > turnTo) {

                double difference = convertedHeading(gyro.getHeading()) - turnTo;

                telemetry.addData("Difference: ", difference);
                telemetry.addData("Heading: ", convertedHeading(gyro.getHeading()));
                telemetry.addData("To Heading: ", turnTo);
                telemetry.update();

                FLMotor.setPower(-currentPower);
                FRMotor.setPower(currentPower);
                BLMotor.setPower(-currentPower);
                BRMotor.setPower(currentPower);

                if(difference < 3.9){
                    currentPower *= Math.pow(1.2, difference) - 1;
                }
            }
        }

        stopMotors();
    }

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
