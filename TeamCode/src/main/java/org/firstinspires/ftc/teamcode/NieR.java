package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "NieR", group = "Pushbot")
public class NieR extends LinearOpMode {
    HardwarePushbot bot = new HardwarePushbot();

    @Override
    public void runOpMode() {
        bot.init(hardwareMap);

        bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            bot.arm.setTargetPosition(-1120 * 11 / 2);
            bot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bot.arm.setPower(-0.4);
            while (bot.arm.isBusy() && !isStopRequested()) {
                idle();
            }

            bot.leftForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.rightForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.setLeft(0);
            bot.setRight(0);

            bot.leftForward.setTargetPosition(1120);
            bot.rightForward.setTargetPosition(1120);
            bot.leftForward.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bot.rightForward.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bot.setLeft(0.4);
            bot.setRight(0.4);
            while (bot.leftForward.isBusy() && !isStopRequested()) {
                idle();
                telemetry.addData("pi", "%7d", bot.leftForward.getCurrentPosition());
                telemetry.update();
            }
            bot.leftForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.rightForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.setLeft(0);
            bot.setRight(0);

            sleep(1000);
            bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.arm.setPower(0);
            bot.arm.setTargetPosition(1120 * 4);
            bot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bot.arm.setPower(0.4);
            while (bot.arm.isBusy() && !isStopRequested()) {
                idle();
            }
            bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bot.arm.setPower(0);
        }
    }
}
