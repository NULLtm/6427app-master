package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;
/*
/\_/\
('_')
(___)
 */
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
@Autonomous(name = "WABOTPlayground", group = "WABOT_BETA")
public class WABOTPlayground extends OpMode {

    public ColorSensor color1;
    public ColorSensor color2;
    public GyroSensor gyro1;
    public OpticalDistanceSensor ODS1;

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public DcMotor BLMotor;
    public DcMotor BRMotor;

    public DcMotor armMotor;
    public DcMotor intakeMotor;
    public DcMotor LiftMotor;

    // https://developer.vuforia.com/license-manager
    private static final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

    public final String side = "blue";
    public final boolean hasMarkerSlot = true;

    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;

    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;
    List<VuforiaTrackable> allTrackables;
    VuforiaTrackables targetsRoverRuckus;

    public void init(){
        FLMotor = hardwareMap.dcMotor.get("FLMotor");
        FRMotor = hardwareMap.dcMotor.get("FRMotor");
        BLMotor = hardwareMap.dcMotor.get("BLMotor");
        BRMotor = hardwareMap.dcMotor.get("BRMotor");
        armMotor = hardwareMap.dcMotor.get("ArmMotor");
        intakeMotor = hardwareMap.dcMotor.get("IntakeMotor");
        LiftMotor = hardwareMap.get(DcMotor.class, "LiftMotor");


        //color1 = hardwareMap.colorSensor.get("color1");
        //color2 = hardwareMap.colorSensor.get("color2");

        //ODS1 = hardwareMap.opticalDistanceSensor.get("ODS1");

        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        LiftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        FLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LiftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsRoverRuckus);

        //gyro1.calibrate();

        //while(gyro1.isCalibrating()){
        //}
    }

    public void start() {
        targetsRoverRuckus.activate();
        targetVisible = false;
        //telemetry.addLine("STARTED AUTONOMOUS!");
        //ODS1.enableLed(true);
        // color1.enableLed(true);
        //color2.enableLed(true);
    }
    public void loop(){

        telemetry.addLine("LOOP");
        //LiftMotor.setPower(1f);

        //drive(0, 0.75f, 0);
        FLMotor.setPower(1);
        FRMotor.setPower(1);
        BLMotor.setPower(1);
        BRMotor.setPower(1);

        sleepThread(10000);

        //LiftMotor.setPower(0f);
        stopMotors();
        /*
        sleepThread(1000);*/

        /*FLMotor.setPower(1);
        FRMotor.setPower(1);
        BLMotor.setPower(1);
        BRMotor.setPower(1);
        sleepThread(2500);
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);*/

        //while(!colorScan().equalsIgnoreCase(side)) {
        //    driveMotors(0.3f);
        //}

    }

    public void driveMotors(float power){
        FLMotor.setPower(power);
        FRMotor.setPower(power);
        BLMotor.setPower(power);
        BRMotor.setPower(power);
    }

    public void checkBlocks(){
        while(colorScan().equals("white") || colorScan().equals("nothing")){
            drive(90, 0.3f, 0);
        }
    }

    public void markerPlace(){

    }

    public void turnRobot(int degrees){
        int startH = gyro1.getHeading();
        while(gyro1.getHeading()-startH < degrees){
            int distanceFromTarget = Math.abs(degrees-gyro1.getHeading());
            if(degrees < 0){
                FLMotor.setPower(-1*distanceFromTarget);
                FRMotor.setPower(1*distanceFromTarget);
                BLMotor.setPower(-1*distanceFromTarget);
                BRMotor.setPower(1*distanceFromTarget);
            }
            else if(degrees > 0){
                FLMotor.setPower(1*distanceFromTarget);
                FRMotor.setPower(-1*distanceFromTarget);
                BLMotor.setPower(1*distanceFromTarget);
                BRMotor.setPower(-1*distanceFromTarget);
            }
        }
    }

    public void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public String vuScan(){
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                targetVisible = true;
                return trackable.getName();
            }
        }
        return "Nothing";
    }


    // Approx. RGB of gold element: 240,189,80
    public String colorScan(){
        if(color1.blue() > color1.green() && color1.blue() > color1.red()){
            return "blue";
        }
        if(color1.red() > color1.green() && color1.red() > color1.blue()){
            return "red";
        }
        if(color1.green() > color1.red() && color1.green() > color1.blue()){
            return "green";
        }
        if(color1.alpha() > color1.green() && color1.alpha() > color1.red() && color1.alpha() > color1.blue()){
            return "white";
        }
        return "nothing";
    }

    public void stop(){
        FLMotor.setPower(0f);
        FRMotor.setPower(0f);
        BLMotor.setPower(0f);
        BRMotor.setPower(0f);
        LiftMotor.setPower(0f);
        armMotor.setPower(0f);
        intakeMotor.setPower(0f);
    }
    // Moduler Methods
    private void checkEncoder(){
        if(FLMotor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(FRMotor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(BLMotor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(BRMotor.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    private void linearDrive(double p1, double p2, double p3, double p4){
        // Sets encoders up
        checkEncoder();
        // Sets motor power
        FLMotor.setPower(p1);
        FRMotor.setPower(p2);
        BLMotor.setPower(p3);
        BRMotor.setPower(p4);
    }
    private void linearDrive(float power){
        // Sets encoders up
        checkEncoder();
        // Sets motor power
        FLMotor.setPower(power);
        FRMotor.setPower(power);
        BLMotor.setPower(power);
        BRMotor.setPower(power);
    }
    private void stopMotors(){
        // Just stops motors
        linearDrive(0);
    }
    private void driveToPosition(int rev){
        // Resets Encoder Values
        FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Checking
        checkEncoder();
        // Sets the encoder target position
        FLMotor.setTargetPosition(rev);
        FRMotor.setTargetPosition(rev);
        BLMotor.setTargetPosition(rev);
        BRMotor.setTargetPosition(rev);
        // Sets the mode to run to position
        FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Sets the power of motors
        linearDrive(0.5f);
        // Waits until the encoders reach their goal
        while(FLMotor.isBusy()&&FRMotor.isBusy()&&BLMotor.isBusy()&&BRMotor.isBusy()){

        }
        // Motors turned off
        stopMotors();
        // Turns mode back to normal
        FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void drive(double angle, double power, double aTurn) {
        double r = power;
        double robotAngle = (angle % 360) * Math.PI / 180;
        double turn = aTurn;
        final double MAX_SPEED = 1;
        double v1 = MAX_SPEED * (r * Math.cos(robotAngle) + turn);
        double v2 = MAX_SPEED * (r * Math.sin(robotAngle) - turn);
        double v3 = MAX_SPEED * (r * Math.sin(robotAngle) + turn);
        double v4 = MAX_SPEED * (r * Math.cos(robotAngle) - turn);

        linearDrive(v1, v2, v3, v4);


    }
    private void turnRight() {
        linearDrive(0.5f, -0.5f, 0.5f, -0.5f);
    }
    private void turnLeft() {
        linearDrive(-0.5f, 0.5f, -0.5f, 0.5f);
    }

}