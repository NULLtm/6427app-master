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
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

// the main class (extending inherits all the data from opmode)

// (!)(!)(!) OWEN DO NOT TOUCH (!) (!) (!)

@TeleOp(name = "WABOTTeleopFIXED", group = "WABOT")
public class WABOTTeleopFIXED extends OpMode {
    DcMotor FLMotor;
    DcMotor FRMotor;
    DcMotor BLMotor;
    DcMotor BRMotor;

    DistanceSensor ods;

    DcMotor liftMotor;
    DcMotor armMotor;
    DcMotor intakeMotor;
    DcMotor gearboxMotor;

    final double MAX_SPEED = 0.75;

    double liftVar = 0;
    double intakeVar = 0;

    public void init(){
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");

        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        gearboxMotor = hardwareMap.get(DcMotor.class, "gearboxMotor");

        ods = hardwareMap.get(DistanceSensor.class, "ods");

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

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        gearboxMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {

        telemetry.addData("ODS DISTANCE (CM): ", ods.getDistance(DistanceUnit.CM));


        if(gamepad2.right_bumper){
            intakeVar = 0.9;
        } else if (gamepad2.left_bumper){
            intakeVar = -0.9;
        } else {
            intakeVar = 0;
        }

        if(gamepad2.dpad_up) {
            liftVar = -1;
        } else if(gamepad2.dpad_down){
            liftVar = 1;
        } else{
            liftVar = 0;
        }

        liftMotor.setPower(liftVar);
        armMotor.setPower(-gamepad2.right_stick_y/(1+(1/3)));
        intakeMotor.setPower(intakeVar);
        gearboxMotor.setPower((-gamepad2.left_stick_y)/(1+(1/3)));


        // (!) (!) (!) HOLONOMIC DRIVE DO NOT TOUCH (!) (!) (!)
        double leftStickX = -gamepad1.right_stick_x;
        double leftStickY = gamepad1.left_stick_y;
        double rightStickX = -gamepad1.left_stick_x;
        double rightStickY = -gamepad1.right_stick_y;

        double r = Math.hypot(rightStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, rightStickX) - (Math.PI / 4); /*- gyroSensor.getHeading()*/
        double rightX = leftStickX;
        double turn = -rightX;

        double v1 = r * Math.cos(robotAngle) + turn;
        double v2 = r * Math.sin(robotAngle) - turn;
        double v3 = r * Math.sin(robotAngle) + turn;
        double v4 = r * Math.cos(robotAngle) - turn;

        FLMotor.setPower(v1);
        FRMotor.setPower(v2);
        BLMotor.setPower(v3);
        BRMotor.setPower(v4);

        /*
        double r = Math.hypot(-gamepad1.right_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - (Math.PI / 4);
        double turn = -gamepad1.left_stick_x;
        */
        /*double v1 = MAX_SPEED * (r * Math.cos(robotAngle) + turn);
        double v2 = MAX_SPEED * (r * Math.sin(robotAngle) - turn);
        double v3 = MAX_SPEED * (r * Math.sin(robotAngle) + turn);
        double v4 = MAX_SPEED * (r * Math.cos(robotAngle) - turn);

        FLMotor.setPower(v1);
        FRMotor.setPower(v2);
        BLMotor.setPower(v3);
        BRMotor.setPower(v4);*/
        // (!) (!) (!) HOLONOMIC DRIVE DO NOT TOUCH (!) (!) (!)
    }

}
