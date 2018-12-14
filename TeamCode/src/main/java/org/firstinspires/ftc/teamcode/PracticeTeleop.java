package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@TeleOp(group = "WABOT_Practice", name = "PracticeTeleop")
public class PracticeTeleop extends OpMode {
    public WABOTHardwareMap m;
    public void init(){
        m = new WABOTHardwareMap(hardwareMap);

        m.initMapPR();
        m.initDevicesPR();
        m.initVuEngine();
        m.initTensor();

        m.tfod.activate();
    }

    public void loop(){
        float leftMotor = -gamepad1.left_stick_y;
        float rightMotor = gamepad1.right_stick_y;

        m.liftMotor.setPower(-gamepad2.left_stick_y);

        m.FLMotor.setPower(leftMotor);
        m.FRMotor.setPower(rightMotor);
        m.BLMotor.setPower(leftMotor);
        m.BRMotor.setPower(rightMotor);

        if (m.tfod != null) {
            List<Recognition> updatedRecognitions = m.tfod.getUpdatedRecognitions();
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
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                }
                telemetry.update();
            }
        }

        telemetry.addData("ODS: ", m.ods.getLightDetected());
    }

    public void stop(){
        m.tfod.shutdown();
    }
}
