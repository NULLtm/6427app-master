/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="WABOTAutonomousFIXED", group="WABOT")
public class WABOTAutonomousFIXED extends LinearOpMode {

    DcMotor FLMotor;
    DcMotor FRMotor;
    DcMotor BLMotor;
    DcMotor BRMotor;

    DcMotor liftMotor;
    DcMotor armMotor;
    DcMotor intakeMotor;
    DcMotor gearboxMotor;

    CRServo markerServo;
    OpticalDistanceSensor ods;

    final double MAX_SPEED = 0.75;

    boolean isBusy = false;

    double liftVar = 0;
    double intakeVar = 0;

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

    List<Recognition> updatedRecognitions;

    TFObjectDetector tfod;

    @Override
    public void runOpMode() {


        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");

        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        gearboxMotor = hardwareMap.get(DcMotor.class, "gearboxMotor");

        markerServo = hardwareMap.get(CRServo.class, "markerServo");
        //ods = hardwareMap.get(OpticalDistanceSensor.class, "ods");

        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gearboxMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        runEncoder(false);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

        if (tfod != null) {
            tfod.activate();
        }

        waitForStart();

        boolean mineralLeft = false;
        boolean mineralRight = false;
        boolean mineralMiddle = false;

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
                            mineralRight = true;
                        } else if (goldMineralX > silverMineral1X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            mineralLeft = true;
                        } else {
                            telemetry.addLine("CANNOT Detect Position");
                        }
                    }
                }
                telemetry.update();
            }
        }

        if(mineralLeft){
            linearDrive(0.5f);
            sleep(3000);
            stopMotors();
        } else if (mineralRight) {
            FLMotor.setPower(0.5f);
            FRMotor.setPower(-0.5f);
            BLMotor.setPower(0.5f);
            BRMotor.setPower(-0.5f);
            sleep(700);
            stopMotors();
            linearDrive(0.5f);
            sleep(1500);
            stopMotors();
            FLMotor.setPower(-0.5f);
            FRMotor.setPower(0.5f);
            BLMotor.setPower(-0.5f);
            BRMotor.setPower(0.5f);
            sleep(900);
            stopMotors();
            linearDrive(0.5f);
            sleep(1900);
            stopMotors();
            FLMotor.setPower(-0.5f);
            FRMotor.setPower(0.5f);
            BLMotor.setPower(-0.5f);
            BRMotor.setPower(0.5f);
            sleep(250);
            stopMotors();
            linearDrive(0.5f);
            sleep(300);
            stopMotors();
        }

        sleep(250);

        markerServo.setPower(1f);

        sleep(1500);

        markerServo.setPower(0f);

        /*
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
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            mineralLeft = true;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            mineralRight = true;
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            mineralMiddle = true;
                        }
                    }
                }
                telemetry.update();
            }
        }

        //If mineral is in middle position, drives forward
        if(mineralMiddle){
            linearDrive(-0.5f);
        } else {
            FLMotor.setPower(-0.5);
            BLMotor.setPower(0.5);
            FRMotor.setPower(0.5);
            BRMotor.setPower(-0.5);
            mineralLeft = false;
            mineralRight = false;
            mineralMiddle = false;
            while(!mineralMiddle){
                updatedRecognitions = tfod.getUpdatedRecognitions();
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
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            mineralLeft = true;
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            mineralRight = true;
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            mineralMiddle = true;
                        }
                    }
                }
            }

            stopMotors();

            linearDrive(-0.5f);
        }

        sleep(250);

        stopMotors();*/

        //Drive into claiming zone
        /*resetEncoder();

        runEncoder(1440);

        if(FLMotor.isBusy() || FRMotor.isBusy() || BLMotor.isBusy() || BRMotor.isBusy()){
            isBusy = true;
        }
        while(isBusy && opModeIsActive()){
            //This page was intentionally left blank
        }

        stopMotors();
        //Turn robot so servo side is facing claiming zone
        FLMotor.setPower(0.75);
        FRMotor.setPower(0.75);
        BLMotor.setPower(0.75);
        BRMotor.setPower(0.75);

        stopMotors();
        //Drops marker
        markerServo.setPower(0.8);
        sleep(1000);
        markerServo.setPower(0);
        //Strafe left until hits wall
        FLMotor.setPower(-0.5);
        BLMotor.setPower(0.5);
        FRMotor.setPower(0.5);
        BRMotor.setPower(-0.5);

        sleep(100);

        linearDrive(1);

        sleep(5000);

        stopMotors();
        */
    }

    private void runEncoder(int position){
        resetEncoder();
        FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FLMotor.setTargetPosition(position);
        FRMotor.setTargetPosition(position);
        BLMotor.setTargetPosition(position);
        BRMotor.setTargetPosition(position);
        while (FLMotor.isBusy() || FRMotor.isBusy() || BLMotor.isBusy() || BRMotor.isBusy()){
            //This page was intentionally left blank
        }
        stopMotors();
        FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void resetEncoder(){
        FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

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
}
