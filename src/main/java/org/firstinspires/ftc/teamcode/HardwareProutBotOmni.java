package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
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
public class HardwareProutBotOmni
{
    // Public OpMode members.
    public DcMotor  frMotor   = null;
    public DcMotor  flMotor  = null;
    public DcMotor  rrMotor = null;
    public DcMotor  rlMotor = null;
    public DcMotor  brushMotor  = null;
    public DcMotor  pitchMotor  = null;

    //public Servo    buttonServo = null;
    //public Servo    loadServo   = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double FLY_POWER    =  0.15 ;
    public static final double BRUSH_POWER  = 1.0;
    public static final double DRIVE_POWER = 1.0;
    public static final double PITCH_POWER = 0.15;

    public Double loadDelta = 0.05;
    public Double loadPosition = 0.5;


    LightSensor      llightSensor;                                                          // could also use HardwarePushbotMatrix class.
    LightSensor      rlightSensor;      // Primary LEGO Light sensor,
    UltrasonicSensor backDis;
    UltrasonicSensor frontDis;
    CompassSensor    compassSensor;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime timing = new ElapsedTime();


    /* Constructor */
    public HardwareProutBotOmni(){

    }

    public void SetHeading(double heading, double desiredHeading) {
        if (heading < desiredHeading) {
            YawLeft();
        }
        else if (heading > desiredHeading) {
            YawRight();
        }

    }
    public void StopMovement() {
        flMotor.setPower(0.0);
        frMotor.setPower(0.0);
        rlMotor.setPower(0.0);
        rrMotor.setPower(0.0);
    }
    public void GoForward() {
        flMotor.setPower(DRIVE_POWER);
        frMotor.setPower(DRIVE_POWER);
        rlMotor.setPower(DRIVE_POWER);
        rrMotor.setPower(DRIVE_POWER);
    }
    public void GoBackward() {
        flMotor.setPower(-DRIVE_POWER);
        frMotor.setPower(-DRIVE_POWER);
        rlMotor.setPower(-DRIVE_POWER);
        rrMotor.setPower(-DRIVE_POWER);
    }
    public void GoRight() {
        flMotor.setPower(DRIVE_POWER);
        frMotor.setPower(-DRIVE_POWER);
        rlMotor.setPower(-DRIVE_POWER);
        rrMotor.setPower(DRIVE_POWER);
    }
    public void GoLeft() {
        flMotor.setPower(-DRIVE_POWER);
        frMotor.setPower(DRIVE_POWER);
        rlMotor.setPower(DRIVE_POWER);
        rrMotor.setPower(-DRIVE_POWER);
    }
    public void GoNE() {
        flMotor.setPower(-DRIVE_POWER);
        rrMotor.setPower(DRIVE_POWER);
    }
    public void GoNW() {
        frMotor.setPower(DRIVE_POWER);
        rlMotor.setPower(DRIVE_POWER);
    }
    public void GoSE() {
        frMotor.setPower(-DRIVE_POWER);
        rlMotor.setPower(-DRIVE_POWER);
    }
    public void GoSW() {
        flMotor.setPower(-DRIVE_POWER);
        rrMotor.setPower(-DRIVE_POWER);
    }
    public void YawLeft() {
        flMotor.setPower(DRIVE_POWER);
        frMotor.setPower(DRIVE_POWER);
        rlMotor.setPower(-DRIVE_POWER);
        rrMotor.setPower(DRIVE_POWER);
    }
    public void YawRight() {
        flMotor.setPower(DRIVE_POWER);
        frMotor.setPower(-DRIVE_POWER);
        rlMotor.setPower(DRIVE_POWER);
        rrMotor.setPower(-DRIVE_POWER);
    }
    public void ShootParticle() {
        timing.reset();
        while (timing.seconds() < 1.0 ) {
            loadPosition += loadDelta;
            //loadServo.setPosition(loadPosition);
        }

    }
    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frMotor   = hwMap.dcMotor.get("fr");
        flMotor  = hwMap.dcMotor.get("fl");
        rrMotor   = hwMap.dcMotor.get("rr");
        rlMotor  = hwMap.dcMotor.get("rl");
        brushMotor  = hwMap.dcMotor.get("brush drive");
        pitchMotor  = hwMap.dcMotor.get("pitch drive");

        frMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        flMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        rrMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rlMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        brushMotor.setDirection(DcMotor.Direction.REVERSE);
        pitchMotor.setDirection(DcMotor.Direction.REVERSE);




        // Set all motors to zero power
        frMotor.setPower(0);
        flMotor.setPower(0);
        rrMotor.setPower(0);
        rlMotor.setPower(0);
        brushMotor.setPower(0);
        pitchMotor.setPower(0);

        llightSensor = hwMap.lightSensor.get("left light");
        rlightSensor = hwMap.lightSensor.get("right light");
        backDis = hwMap.ultrasonicSensor.get("back dis");
        frontDis = hwMap.ultrasonicSensor.get("front dis");
        compassSensor = hwMap.compassSensor.get("compass");


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rrMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rlMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brushMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pitchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
            //buttonServo = hwMap.servo.get("tilt_servo");
        //loadServo = hwMap.servo.get("gate");
            //buttonServo.setPosition(MID_SERVO);
        //loadServo.setPosition(MID_SERVO);
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

