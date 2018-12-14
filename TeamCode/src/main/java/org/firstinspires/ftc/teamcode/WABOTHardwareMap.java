package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class WABOTHardwareMap {

    // https://developer.vuforia.com/license-manager
    private final String VUFORIA_KEY = "AQc7P77/////AAAAGRkj9xpwbUV3lGEfqxdnuDCJ/2Rml7cEF7R7SqndRsU6cegdDxLs9sSsk8x5AqituFBD6dCrCZFJB/P4+tc3O3uooja7zTjZ+knDbMYmJq7t35B0ZSRUp84N0e7bkiDq+rGvM7qWl7rOMCJL0tN8CPXDL843WleEAUrvMl0Ba5jnAz8ZX4UTpk+/8e3Hz1F4s/F7/VjkJejp9JbPDEYdvwMOwwFedcAumO+NTZfe5mWqFY2MBBwLJi6h6SZ1g4a7qWThAorw0G0AZK0WiIWYiQVzPLaKTiq8jEKAY9lxSFon02LXkGtaLi6X5krlNiiacNQcSYSj9Y+6oxCUGH0zUvBZgpbG5tKQJqzyovqqP5UT";
    private final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;

    public String side = "blue";
    public boolean hasMarkerSlot = true;

    OpenGLMatrix lastLocation = null;
    boolean targetVisible = false;

    VuforiaLocalizer vuforia;
    VuforiaLocalizer.Parameters parameters;
    List<VuforiaTrackable> allTrackables;
    VuforiaTrackables targetsRoverRuckus;

    final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    final String LABEL_GOLD_MINERAL = "Gold Mineral";
    final String LABEL_SILVER_MINERAL = "Silver Mineral";

    TFObjectDetector tfod;

    DcMotor FLMotor;
    DcMotor FRMotor;
    DcMotor BLMotor;
    DcMotor BRMotor;

    DcMotor liftMotor;
    DcMotor armMotor;
    DcMotor intakeMotor;

    GyroSensor gyro;
    ColorSensor color1;
    ColorSensor color2;
    OpticalDistanceSensor ods;

    CRServo markerServo;

    HardwareMap map;

    public WABOTHardwareMap (HardwareMap m) {
        this.map = m;
    }

    public void initMap(){

        map.get(DcMotor.class, "BLMotor");
        map.get(DcMotor.class, "FLMotor");
        map.get(DcMotor.class, "FRMotor");
        map.get(DcMotor.class, "BRMotor");
        map.get(DcMotor.class, "liftMotor");
        map.get(DcMotor.class, "armMotor");
        map.get(DcMotor.class, "intakeMotor");

        //map.get(GyroSensor.class, "gyro");
        //map.get(ColorSensor.class, "color1");
        //map.get(ColorSensor.class, "color2");
        //map.get(OpticalDistanceSensor.class, "ods");

        map.get(CRServo.class, "markerServo");

        initDevices();

    }

    public void initMapPR(){

        FLMotor = map.get(DcMotor.class, "FLMotor");
        FRMotor = map.get(DcMotor.class, "FRMotor");
        BLMotor = map.get(DcMotor.class, "BLMotor");
        BRMotor = map.get(DcMotor.class, "BRMotor");
        liftMotor = map.get(DcMotor.class, "liftMotor");

        color1 = map.get(ColorSensor.class, "color1");
        ods = map.get(OpticalDistanceSensor.class, "ods");
    }


    public void initDevices(){
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //color1.enableLed(true);
        //color2.enableLed(true);
        //ods.enableLed(true);
        //gyro.calibrate();
        //while (gyro.isCalibrating()) {
        //}
    }
    public void initDevicesPR(){

        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        color1.enableLed(true);
        ods.enableLed(true);
    }



    public void initVuEngine(){
        int cameraMonitorViewId = map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void initVu(){
        targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
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
    }

    public void initTensor(){
        int tfodMonitorViewId = map.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", map.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void setSide(String s){
        if(s.equalsIgnoreCase("blue")){
            this.side = "blue";
        } else if(s.equalsIgnoreCase("red")){
            this.side = "red";
        }
    }

    public void checkTensor() {
    }

    public void setSlot(boolean b){
        hasMarkerSlot = b;
    }

}
