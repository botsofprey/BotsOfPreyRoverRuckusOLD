package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.IOException;

import ExampleBaseCode.Actions.HardwareWrappers.ServoHandler;
import ExampleBaseCode.Actions.HardwareWrappers.SpoolMotor;
import ExampleBaseCode.DriveEngine.HolonomicDriveSystemTesting;
import ExampleBaseCode.MotorControllers.MotorController;
import ExampleBaseCode.UserControlled.JoystickHandler;

/**
 * Created by robotics on 2/16/18.
 */
@TeleOp(name="Spoon V1", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class spoonV1 extends LinearOpMode {

    final double movementScale = 1;
    final double turningScale = .75;
    final double RELEASE_ANGLE = 41;
    final double LATCH_ANGLE = 69;
    SpoolMotor extendotron;
    SpoolMotor armLifter;
    ServoHandler deadbolt;

    @Override
    public void runOpMode() throws InterruptedException {
        HolonomicDriveSystemTesting driveSystem = new HolonomicDriveSystemTesting(hardwareMap,"RobotConfig/JennyV2.json");
        try {
            extendotron = new SpoolMotor(new MotorController("extendotron", "MotorConfig/FunctionMotors/ArmExtender.json", hardwareMap), 5, 5, 50, hardwareMap);
            armLifter = new SpoolMotor(new MotorController("lifter", "MotorConfig/FunctionMotors/Lifter.json", hardwareMap), 3, 5, 50, hardwareMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        deadbolt = new ServoHandler("deadbolt", hardwareMap);
        deadbolt.setDirection(Servo.Direction.FORWARD);
        deadbolt.setServoRanges(40, 70);
        deadbolt.setDegree(LATCH_ANGLE);
        extendotron.setDirection(DcMotorSimple.Direction.REVERSE);
        JoystickHandler leftStick = new JoystickHandler(gamepad1, JoystickHandler.LEFT_JOYSTICK);
        JoystickHandler rightStick = new JoystickHandler(gamepad1, JoystickHandler.RIGHT_JOYSTICK);
        waitForStart();
        double movementPower = 0;
        double turningPower = 0;
        double initialOrientation = driveSystem.orientation.getOrientation();
        extendotron.setExtendPower(1);

        while(opModeIsActive()){
            movementPower = movementScale * Math.abs(leftStick.magnitude());
            turningPower = turningScale * Math.abs(rightStick.magnitude()) * Math.abs(rightStick.x())/rightStick.x();
            if(gamepad1.a) extendotron.extendWithPower();
            else if(gamepad1.b) extendotron.retractWithPower();
            else extendotron.pause();
            if(gamepad1.left_trigger > 0.1) armLifter.extendWithPower();
            else if(gamepad1.left_bumper) armLifter.retractWithPower();
            else armLifter.pause();
            if(gamepad1.dpad_right) deadbolt.setDegree(RELEASE_ANGLE);
            else if(gamepad1.dpad_left) deadbolt.setDegree(LATCH_ANGLE);
            driveSystem.cartesianDriveOnHeadingWithTurning(leftStick.angle(), movementPower, turningPower);
//            driveSystem.driveOnHeadingPID(0, 1, initialOrientation);
            telemetry.addData("Gamepad1 left Joystick",leftStick.toString());
            telemetry.addData("Gamepad1 right Joystick", rightStick.toString());
            telemetry.addData("Deadbolt Degree", deadbolt.getActualDegree());
            telemetry.update();
        }
    }

}
