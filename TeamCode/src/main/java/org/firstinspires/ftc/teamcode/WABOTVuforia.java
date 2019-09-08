package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

public class WABOTVuforia extends OpMode {

    VuforiaLocalizer vuforia;
    VuforiaTrackables targetsRoverRuckus;
    List<VuforiaTrackable> allTrackables;

    public void init(){

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "";
        parameters.cameraDirection   = parameters.cameraDirection.BACK;

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
    }

    public void start(){
        targetsRoverRuckus.activate();
    }

    public void loop(){
        for(VuforiaTrackable vuMarks : allTrackables){
            if(((VuforiaTrackableDefaultListener)vuMarks.getListener()).isVisible()){
            }

        }
    }

    public void stop(){
    }
}
