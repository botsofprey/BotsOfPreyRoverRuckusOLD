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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import ExampleBaseCode.Actions.HardwareWrappers.ServoHandler;
import ExampleBaseCode.UserControlled.JoystickHandler;

@TeleOp(name="TysonBrosCode", group="Linear Opmode")
//@Disabled
public class TysonBrosTeam extends LinearOpMode {
    private final int DEPOSIT_DEGREE = 10;
    private final int HOLD_DEGREE = 90;
    private final int COLLECT_DEGREE = 59;

    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor intakeMotor;
    DcMotor liftMotor;
    ServoHandler depositor;
    Servo latch;
    int curDeg = 0;

    //DcMotor backMotor;
    //DcMotor liftMotor;
//    DcMotor backMiddle;
    JoystickHandler leftStick;
    JoystickHandler rightStick;
    //Servo servoTurn;

    @Override
    public void runOpMode() {

        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        depositor =  new ServoHandler("depositor", hardwareMap);
        latch = hardwareMap.servo.get("latch");


        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        depositor.setDirection(Servo.Direction.FORWARD);
        latch.setDirection(Servo.Direction.FORWARD);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        depositor.setServoRanges(DEPOSIT_DEGREE, HOLD_DEGREE);
        depositor.setDegree(COLLECT_DEGREE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if(gamepad1.x){
                curDeg++;
                while(gamepad1.x);
                latch.setPosition(63.0/180.0);
            }
            else if(gamepad1.y){

                latch.setPosition(42.0/180.0);
            }
            if(gamepad1.dpad_up){
                depositor.setDegree(HOLD_DEGREE);
            }
            else if (gamepad1.dpad_left){
                depositor.setDegree(COLLECT_DEGREE);
            }
            else if (gamepad1.dpad_down){
                depositor.setDegree(DEPOSIT_DEGREE);
            }
            if (gamepad1.left_bumper) {
                leftMotor.setPower(1); // WHAT IS WRONG HERE?????
            } else if (gamepad1.left_trigger > 0.1) {
                leftMotor.setPower(-1); // AND HERE????
            } else {
                leftMotor.setPower(0);
            }
            if (gamepad1.right_bumper) {
                rightMotor.setPower(1); // AND HERE????
            } else if (gamepad1.right_trigger > 0.1) {
                rightMotor.setPower(-1); // AND HERE????
            } else {
                rightMotor.setPower(0);
            }
            liftMotor.setPower(gamepad1.left_stick_y);

            if (gamepad1.a) intakeMotor.setPower(.75);
            else if (gamepad1.b) intakeMotor.setPower(-.75);
            else intakeMotor.setPower(0);


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

        }
    }
}

