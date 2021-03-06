/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving up to a line and then stopping.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code shows using two different light sensors:
 *   The Primary sensor shown in this code is a legacy NXT Light sensor (called "light sensor")
 *   Alternative "commented out" code uses a MR Optical Distance Sensor (called "sensor_ods")
 *   instead of the LEGO sensor.  Chose to use one sensor or the other.
 *
 *   Setting the correct WHITE_THRESHOLD value is key to stopping correctly.
 *   This should be set half way between the light and dark values.
 *   These values can be read on the screen once the OpMode has been INIT, but before it is STARTED.
 *   Move the senso on asnd off the white line and not the min and max readings.
 *   Edit this code to make WHITE_THRESHOLD half way between the min and max.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="ProutBot: Gyro Drive To Line BLUE ALT ", group="ProutBot")

public class ProutBotGyroDriveToLine_LinearBLUETEAM_ALT extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareProutBot         robot   = new HardwareProutBot();   // Use a Proutbot's hardware
    private ElapsedTime              runtime = new ElapsedTime();
    boolean rightIsBlue = false;


    // OpticalDistanceSensor   lightSensor;   // Alternative MR ODS sensor

    static final double     cWHITE_THRESHOLD = 0.25;
    static final double     lWhite_THRESHOLD = 0.165;
    static final double     rWHITE_THRESHOLD = 0.23;
    static final double     APPROACH_SPEED  = -0.5;


    @Override
    public void runOpMode() throws InterruptedException {

        /* Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        double rdetected = robot.rlightSensor.getLightDetected();
        double ldetected = robot.llightSensor.getLightDetected();
        double cdetected = robot.clightSensor.getLightDetected();


        // turn on LED of light sensor.
        robot.rlightSensor.enableLed(true);
        robot.llightSensor.enableLed(true);
        robot.clightSensor.enableLed(true);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // start calibrating the gyro.
        robot.gyro.calibrate();
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();


        // make sure the gyro is calibrated.
        while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }
        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        robot.initialheading = robot.gyro.getIntegratedZValue();


        // Wait for the game to start (driver presses PLAY)
        while (!isStarted()) {

            // Display the light level while we are waiting to start
            telemetry.addData("R Light Level", robot.rlightSensor.getLightDetected());
            telemetry.addData("L Light Level", robot.llightSensor.getLightDetected());
            telemetry.addData("C Light Level", robot.clightSensor.getLightDetected());
            telemetry.addData("Back Distance", robot.backDis.getUltrasonicLevel());
            telemetry.addData("Front Distance", robot.frontDis.getUltrasonicLevel());
            telemetry.addData("Original Heading", robot.initialheading);
            telemetry.addData("Current Heading", robot.gyro.getIntegratedZValue());

            telemetry.update();
            idle();
        }

        // Aim the Robot towards the Vortex and Shoot Twice
        while (opModeIsActive() && (robot.gyro.getIntegratedZValue() < 5)) {

            robot.rlMotor.setPower(0.1);
            robot.rrMotor.setPower(-0.3);

            telemetry.addData("Leg 1: %2.5f Sec Elapsed", runtime.seconds());
            telemetry.addData("Original Bearing", robot.initialheading);
            telemetry.addData("Bearing", robot.gyro.getIntegratedZValue());
            telemetry.addLine("Aiming Towards Vortex");
            telemetry.update();
            idle();
        }
        robot.rrMotor.setPower(0.0);
        robot.rlMotor.setPower(0.0);
        robot.ShootParticle(0.11, 4.0, 3.0);
        sleep(1000);


        //Aim Towards Beacons
        while (opModeIsActive() && (robot.gyro.getIntegratedZValue() > -24)) {
            robot.rlMotor.setPower(-0.3);
            robot.rrMotor.setPower(0.3);
            telemetry.addData("Original Bearing", robot.initialheading);
            telemetry.addData("Bearing", robot.gyro.getHeading());
            telemetry.addLine("Aiming Towards Line");
            telemetry.update();
            idle();
        }
        robot.rrMotor.setPower(0.0);
        robot.rlMotor.setPower(0.0);

        //Go Until White Line is Found
        while (opModeIsActive() && (robot.clightSensor.getLightDetected() < cWHITE_THRESHOLD)) {
            while (Math.abs(robot.gyro.getIntegratedZValue() - 25) <= 1 && opModeIsActive() && (robot.clightSensor.getLightDetected() < cWHITE_THRESHOLD)) {
                robot.rrMotor.setPower(-0.2);
                robot.rlMotor.setPower(-0.2);
                telemetry.addData("heading", robot.gyro.getIntegratedZValue());
                telemetry.update();
            }
            while (Math.abs(robot.gyro.getIntegratedZValue() - 25) > 1 && opModeIsActive() && (robot.clightSensor.getLightDetected() < cWHITE_THRESHOLD)) {
                if (robot.gyro.getIntegratedZValue() < -25) {
                    robot.rrMotor.setPower(-0.3);
                    robot.rlMotor.setPower(-0.1);
                    telemetry.addData("heading", robot.gyro.getIntegratedZValue());
                    telemetry.addLine("adjust left");
                    telemetry.update();
                }
                if (robot.gyro.getIntegratedZValue() > -25) {
                    robot.rrMotor.setPower(-0.1);
                    robot.rlMotor.setPower(-0.3);
                    telemetry.addLine("right");
                    telemetry.addData("heading", robot.gyro.getIntegratedZValue());
                    telemetry.update();
                }
                telemetry.addData("C Light Level", robot.clightSensor.getLightDetected());
                telemetry.addLine("Looking for Line");
                telemetry.update();
                idle();
            }
        }
        robot.rrMotor.setPower(0.0);
        robot.rlMotor.setPower(0.0);
        sleep(1000);

        //Adjust onto Line towards Beacon and Move Forward Until Set Distance from beacon
        while (opModeIsActive() && (robot.gyro.getIntegratedZValue() > -80)) {
            telemetry.addData("Original Heading", robot.initialheading);
            telemetry.addData("Bearing", robot.gyro.getIntegratedZValue());
            telemetry.addLine("Aiming Towards Beacon");
            robot.rlMotor.setPower(-0.3);
            robot.rrMotor.setPower(0.4);
            telemetry.update();
        }
        robot.rrMotor.setPower(0.0);
        robot.rlMotor.setPower(0.0);
        sleep(1000);


        //Analyze colors and Press Respective Beacon (**Color Sensor is on Right Side**On Red Team**)
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 10) {

            telemetry.addData("Red Level",  robot.colorSensor.red());
            telemetry.addData("Blue Level", robot.colorSensor.blue());
            telemetry.addData("Dis To Wall", robot.backDis.getUltrasonicLevel());
            telemetry.addLine("Analyzing Beacon");

            if (robot.colorSensor.red() == robot.colorSensor.blue()) {
                robot.rrMotor.setPower(-0.2);
                robot.rlMotor.setPower(-0.2);
            }
            if (robot.colorSensor.red() < robot.colorSensor.blue()) {
                robot.buttonServo.setPosition(robot.RIGHT_BUTTON);
                sleep(1000);
                robot.rrMotor.setPower(-0.2);
                robot.rlMotor.setPower(-0.2);
                telemetry.addLine("Pressing Right Button");
                rightIsBlue = true;
                //Press Right Button
            } else if (robot.colorSensor.blue() < robot.colorSensor.red() && !rightIsBlue) {
                robot.buttonServo.setPosition(robot.LEFT_BUTTON);
                sleep(1000);
                robot.rlMotor.setPower(-0.2);
                robot.rrMotor.setPower(-0.2);
                telemetry.addLine("Pressing Left Button");
                //Press Left Button
            }

            telemetry.update();
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }

        //Turn towards Center Vortex and Go until 30s is up
        robot.rrMotor.setPower(-1);
        robot.rlMotor.setPower(-1);
    }
}
