package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a ProutBot.
 * See PushbotTeleopTank_Iterative and others classes starting with "ProutBot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *

 */
public class HardwareProutBot
{
    // Public OpMode members.

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    public DcMotor  rrMotor = null;
    public DcMotor  rlMotor = null;
    public DcMotor  brushMotor  = null;
    public DcMotor  pitchMotor  = null;
    public DcMotor  loadbrushMotor = null;

    public Servo    gateServo   = null;
    public Servo    buttonServo = null;

    public static final double GATE_CLOSED  =  0.5 ;
    public static final double GATE_OPEN    = 1.0;
    public static final double BRUSH_POWER  = 1.0;
    public static final double DRIVE_POWER  = 1.0;
    public static final double RIGHT_BUTTON = 0.0;
    public static final double LEFT_BUTTON = 0.9;
    public double PITCH_POWER  = 0.10;
    public double initialBearing;
    public double initialtoZero;





    LightSensor      llightSensor;                                                          // could also use HardwarePushbotMatrix class.
    LightSensor      rlightSensor;      // Primary LEGO Light sensor,
    LightSensor      clightSensor;
    UltrasonicSensor backDis;
    UltrasonicSensor frontDis;
    CompassSensor    compassSensor;
    OpticalDistanceSensor oDis;
    ColorSensor colorSensor;




    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime timing = new ElapsedTime();

    public void SetHeading(double current, double desired) {
        if (current > desired) { //Adjust Left
            while (current > desired) {
                rrMotor.setPower(0.5);
                rlMotor.setPower(0.0);
            }
        } else if (current < desired) { //Adjust Right
            while (current < desired) {
                rrMotor.setPower(0.0);
                rlMotor.setPower(0.5);
            }
        }
    }

    public void ShootParticle(double power, double firstrev, double secondrev) {
        timing.reset();
        while (timing.seconds() < firstrev) {
            pitchMotor.setPower(power);
        }
        timing.reset();
        while (timing.seconds() < 0.7) {
            gateServo.setPosition(GATE_OPEN);
            loadbrushMotor.setPower(BRUSH_POWER);
        }
        gateServo.setPosition(GATE_CLOSED);
        loadbrushMotor.setPower(0.0);
        pitchMotor.setPower(0.0);
        timing.reset();
        while (timing.seconds() < secondrev) {
            pitchMotor.setPower(power);
        }
        timing.reset();
        while (timing.seconds() < 1.0) {
            gateServo.setPosition(GATE_OPEN);
            loadbrushMotor.setPower(BRUSH_POWER);
        }
        gateServo.setPosition(GATE_CLOSED);
        loadbrushMotor.setPower(0.0);
        pitchMotor.setPower(0.0);

    }


    /* Constructor */
    public HardwareProutBot(){

    }


    /* Initialize standard Hardware interfaces */
        public void init(HardwareMap ahwMap) {
            // Save reference to Hardware map
            hwMap = ahwMap;

            // Define and Initialize Motors

            rrMotor = hwMap.dcMotor.get("rr");
            rlMotor = hwMap.dcMotor.get("rl");
            brushMotor = hwMap.dcMotor.get("brush drive");
            pitchMotor = hwMap.dcMotor.get("pitch drive");
            loadbrushMotor = hwMap.dcMotor.get("load drive");


            rrMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            rlMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
            brushMotor.setDirection(DcMotor.Direction.REVERSE);
            pitchMotor.setDirection(DcMotor.Direction.REVERSE);
            loadbrushMotor.setDirection(DcMotor.Direction.REVERSE);


            // Set all motors to zero power

            rrMotor.setPower(0);
            rlMotor.setPower(0);
            brushMotor.setPower(0);
            pitchMotor.setPower(0);
            loadbrushMotor.setPower(0);

            //Define and intialize Sensors

            llightSensor = hwMap.lightSensor.get("left light");
            rlightSensor = hwMap.lightSensor.get("right light");
            backDis = hwMap.ultrasonicSensor.get("back dis");
            frontDis = hwMap.ultrasonicSensor.get("front dis");
            compassSensor = hwMap.compassSensor.get("compass");
            colorSensor = hwMap.colorSensor.get("color");
            clightSensor = hwMap.lightSensor.get("center light");
            //oDis = hwMap.opticalDistanceSensor.get("ods");

            colorSensor.enableLed(false);
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);


            // Set all motors to run without encoders.
            // May want to use RUN_USING_ENCODERS if encoders are installed.

            rrMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rlMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            brushMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            pitchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            loadbrushMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // Define and initialize ALL installed servos.

            gateServo = hwMap.servo.get("gate servo");
            gateServo.setPosition(GATE_CLOSED);

            buttonServo = hwMap.servo.get("button servo");
            buttonServo.setPosition(0.0);

            initialBearing = compassSensor.getDirection();


        }


    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

