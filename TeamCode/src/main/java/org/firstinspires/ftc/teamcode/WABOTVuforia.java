package org.firstinspires.ftc.teamcode;

/*

Official Wright Angles 2019-2020 Class

This class contains vuforia code

NOTE: Setup for 2018-2019 Rover Ruckus ATM

 */

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

public class WABOTVuforia {

    VuforiaLocalizer vuforia;
    VuforiaTrackables targets;
    List<VuforiaTrackable> allTrackables;

    // Constructor
    public WABOTVuforia(String licenseKey, VuforiaLocalizer.CameraDirection camDir){
        init(licenseKey);
    }

    // Initializes Vuforia Engine
    public void init(String key){

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = key;
        parameters.cameraDirection   = parameters.cameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        targets = this.vuforia.loadTrackablesFromAsset("RoverRuckus");

        VuforiaTrackable blueRover = targets.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targets.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targets.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targets.get(3);
        backSpace.setName("Back-Space");

        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);
    }

    // Activates vuforia to begin searching
    public void activate(){
        targets.activate();
    }

    // This method scans for objects ONCE when called
    public boolean run(){
        for(VuforiaTrackable vuMarks : allTrackables){
            if(((VuforiaTrackableDefaultListener)vuMarks.getListener()).isVisible()){
                return true;
            }
        }
        return false;
    }
}
