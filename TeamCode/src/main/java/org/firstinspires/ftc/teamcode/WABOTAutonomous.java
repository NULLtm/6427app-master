package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

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

@Autonomous(name = "WABOTAutonomous", group = "WABOT")
public class WABOTAutonomous extends LinearOpMode {

    WABOTHardwareMap m;
    public int pPos;

    @Override
    public void runOpMode() throws InterruptedException {

        m = new WABOTHardwareMap(hardwareMap);

        m.setSide("blue");
        m.setSlot(true);

        m.initMap();
        m.initVuEngine();
        m.initTensor();

        telemetry.addData("Status:", "Ready to run");
        telemetry.update();

        if (m.tfod != null) {
            m.tfod.activate();
        }

        waitForStart();

        List<Recognition> updatedRecognitions = m.tfod.getUpdatedRecognitions();
        while (updatedRecognitions == null) {
            updatedRecognitions = m.tfod.getUpdatedRecognitions();
            linearDrive(-0.5f);
        }

        linearDrive(0f);

        if (m.tfod != null) {
            m.tfod.shutdown();
        }

        /*while (opModeIsActive()) {

            linearDrive(0.0f);

            if (m.tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                updatedRecognitions = m.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(m.LABEL_GOLD_MINERAL)) {
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
                                pPos = -1;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                                pPos = 1;
                            } else {
                                telemetry.addData("Gold Mineral Position", "Center");
                                pPos = 0;
                            }
                        }
                    }
                    telemetry.update();
                }
            }

            if(pPos == 0){
                linearDrive(-0.5f);
                sleep(1000);
            } else if (pPos == -1){
                m.FLMotor.setPower(-0.2f);
                m.FRMotor.setPower(0.2f);
                m.BLMotor.setPower(-0.2f);
                m.BRMotor.setPower(0.2f);
            }

            while (pPos == -1) {
                if (m.tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    updatedRecognitions = m.tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(m.LABEL_GOLD_MINERAL)) {
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
                                    pPos = -1;
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    pPos = 1;
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    pPos = 0;
                                }
                            }
                        }
                        telemetry.update();
                    }
                }
            }

            linearDrive(0f);

            sleep(500);

            linearDrive(-0.5f);

            sleep(500);

            linearDrive(0f);

            m.markerServo.setPower(0.5);




        }



            if (m.tfod != null) {
                m.tfod.shutdown();
            }


        }*/
    }


    // angle = Angle to strafe at if aTurn is 0, Power = POWWWER, aTurn = Angle to turn at
    private void dynamicDrive(double angle, double power, double aTurn) {
        double r = power;
        double robotAngle = (angle % 360 - 45) * Math.PI / 180;
        double turn = aTurn;
        final double MAX_SPEED = 0.5;
        double v1 = MAX_SPEED * (r * Math.cos(robotAngle) + turn);
        double v2 = MAX_SPEED * (r * Math.sin(robotAngle) - turn);
        double v3 = MAX_SPEED * (r * Math.sin(robotAngle) + turn);
        double v4 = MAX_SPEED * (r * Math.cos(robotAngle) - turn);

        m.FLMotor.setPower(v1);
        m.FRMotor.setPower(v2);
        m.BLMotor.setPower(v3);
        m.BRMotor.setPower(v4);

    }
    private void linearDrive(double power){
        m.FLMotor.setPower(power);
        m.FRMotor.setPower(power);
        m.BLMotor.setPower(power);
        m.BRMotor.setPower(power);
    }
    private void stopMotors() {
        m.FLMotor.setPower(0);
        m.FRMotor.setPower(0);
        m.BLMotor.setPower(0);
        m.BRMotor.setPower(0);
    }
}