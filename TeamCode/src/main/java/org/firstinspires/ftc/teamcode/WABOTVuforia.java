package org.firstinspires.ftc.teamcode;

/*

Official Wright Angles 2019-2020 Class

This class contains vuforia code

NOTE: Setup for 2018-2019 Rover Ruckus ATM

 */

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

public class WABOTVuforia {

    VuforiaLocalizer vuforia;
    VuforiaTrackables targetsSkyStone;
    List<VuforiaTrackable> allTrackables;

    // Constructor
    public WABOTVuforia(String licenseKey, VuforiaLocalizer.CameraDirection camDir, HardwareMap m, boolean showScreen){
        init(licenseKey, showScreen, camDir, m);
    }

    // Initializes Vuforia Engine
    public void init(String key, boolean show, VuforiaLocalizer.CameraDirection camDir, HardwareMap map){
        VuforiaLocalizer.Parameters parameters;
        if(show){
            int cameraMonitorViewId = map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        // https://developer.vuforia.com/license-manager
        parameters.vuforiaLicenseKey = key;
        parameters.cameraDirection   = camDir;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);
    }

    // Activates vuforia to begin searching
    public void activate(){
        targetsSkyStone.activate();
    }

    // This method scans for objects ONCE when called
    public String run() {
        for (VuforiaTrackable vuMarks : allTrackables) {
            if (((VuforiaTrackableDefaultListener) vuMarks.getListener()).isVisible()) {
                return vuMarks.getName();
            }
        }
        return null;
    }
}
