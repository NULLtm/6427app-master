package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class PWABOTHardware {
    private HardwareMap hardwareMap;

    public DcMotor FLMotor;
    public DcMotor FRMotor;
    public DcMotor BLMotor;
    public DcMotor BRMotor;
    public ColorSensor color;
    public TouchSensor touch;
    public OpticalDistanceSensor ods;
    public GyroSensor gyro;

    protected PWABOTHardware(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        initializeMap();
    }
    private void initializeMap() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLMotor = hardwareMap.get(DcMotor.class, "FLMotor");
        FRMotor = hardwareMap.get(DcMotor.class, "FRMotor");
        BLMotor = hardwareMap.get(DcMotor.class, "BLMotor");
        BRMotor = hardwareMap.get(DcMotor.class, "BRMotor");
        ods = hardwareMap.get(OpticalDistanceSensor.class, "ods");
        color = hardwareMap.get(ColorSensor.class, "color");
        touch = hardwareMap.get(TouchSensor.class, "touch");
        gyro = hardwareMap.get(GyroSensor.class, "gyro");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        BRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        BLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        FRMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        FLMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}