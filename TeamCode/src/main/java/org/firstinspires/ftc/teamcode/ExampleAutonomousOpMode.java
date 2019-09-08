// the current package this class resides in
package org.firstinspires.ftc.teamcode;

// access data outside of this class by importing it
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// the main class (extending inherits all the data from opmode)
@Autonomous(name = "ExampleAutonomous", group = "ExampleOpModes")
public class ExampleAutonomousOpMode extends OpMode {
    // here we declare variables and sometimes initialize them as well
    public DcMotor exampleMotor;

    // used for initializing variables
    @Override
    public void init(){
        // the parent class opMode has a built in hardwareMap variable it gets from the phone on runtime
        // Here VV We are accessing the phone's hardware map, then finding the "component" we want, and setting it equal to a local variable
        exampleMotor = hardwareMap.dcMotor.get("ExampleMotorName");
    }

    // used for code you want to run ONLY when this opmode starts up
    @Override
    public void start(){
        // here, we access our motor local variable and set up some properties beforehand
        // An encoder is a device physically attached to the motor that monitors rotations and such, which we are not using here!
        exampleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        exampleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        exampleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // code that runs over and over during runtime
    @Override
    public void loop(){
        // here is where we lay down the instructions for how the robot should behave during the autonomous phase
        // turning the motor on
        exampleMotor.setPower(0.5f);
        // waiting for a specified amount of time
        sleepForSeconds(1f);
        // turning off motor
        exampleMotor.setPower(0f);
    }

    // used for code you only want to run when this opmode stops
    @Override
    public void stop(){
        // here I am just making sure everything is turned OFF so we don't have a rampant robot!
        exampleMotor.setPower(0);
    }

    // this custom method waits for a # of seconds, then breaks
    private void sleepForSeconds(float waitT){
        double startT = getRuntime();
        while((getRuntime()-startT) < waitT){
        }
        return;
    }
}
