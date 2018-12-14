// the current package this class resides in
package org.firstinspires.ftc.teamcode;

// access data outside of this class by importing it
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

// the main class (extending inherits all the data from opmode)
@Disabled
@Autonomous(name = "ExampleTeleop", group = "ExampleOpModes")
public class ExampleOpMode extends OpMode {
    DcMotor FLMotor;
    DcMotor FRMotor;
    DcMotor BLMotor;
    DcMotor BRMotor;
    ColorSensor color1;
    GyroSensor gyro1;
    CompassSensor compass1;

    public void init(){
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        gyro1 = hardwareMap.get(GyroSensor.class, "gyro1");
        compass1 = hardwareMap.get(CompassSensor.class, "compass1");

        BRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FRMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FLMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gyro1.calibrate();

        while(gyro1.isCalibrating()){

        }
    }

    public void loop(){
        turnRobot(90);
        telemetry.addData("Heading: ", gyro1.getHeading());
    }

    public void turnRobot(int degrees){
        int startH = gyro1.getHeading();
        while(gyro1.getHeading()-startH < degrees){
            int distanceFromTarget = Math.abs(degrees-gyro1.getHeading());
            if(degrees < 0){
                FLMotor.setPower(-0.2f);
                FRMotor.setPower(0.2f);
                BLMotor.setPower(-0.2f);
                BRMotor.setPower(0.2f);
            }
            else if(degrees > 0){
                FLMotor.setPower(0.2f);
                FRMotor.setPower(-0.2f);
                BLMotor.setPower(0.2f);
                BRMotor.setPower(-0.2f);
            }
        }
        FLMotor.setPower(0);
        FRMotor.setPower(0);
        BLMotor.setPower(0);
        BRMotor.setPower(0);
    }
}
