package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "NieR", group = "Pushbot")
public class NieR extends LinearOpMode {
    HardwarePushbot bot = new HardwarePushbot();

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AWphh2//////AAAAmUm81LA0nUluugKp+89yTulPQGxht0Tf3VeiQc1u3QZvCSUA4y3w5iP7PxKhBYy320lDDzGrxv9yfx+42k3ZvONPWlNVZsZ9LsROUls6uP0C9+snWow/A21rtZG6xmS5M4AcJFnU1/WHGMjA13NjlyrtwxvtR8bhvDTfTgzNWQJqxNo5qe8VE8aTiFhtzqJRwwaleVjr+yep2RoVe3JHhVgWgMFlSEfv2TP1KVARxmDnxoTbtuMK3aYlM3GYAowsZXvAvivPrvmLydJrB1kXOXkP/rySHC0NZcPjg4OEbVRuQwBEC1247AOKVP71EbQPlztibVm+RqCXYKQGM4aG8fYxPI5//sUlvYng6r/Uutue";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        bot.init(hardwareMap);

        bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        if (opModeIsActive()) {
            if (tfod != null) {
                tfod.activate();
            }

            boolean Gotcha = false;
            int GoldMineralPos = -1;

            while (Gotcha == false) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
                            int goldMineralX = -1;
                            int silverMineral1X = -1;
                            int silverMineral2X = -1;
                            for (Recognition recognition : updatedRecognitions) {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                    goldMineralX = (int) recognition.getLeft();
                                } else if (silverMineral1X == -1) {
                                    silverMineral1X = (int) recognition.getLeft();
                                } else {
                                    silverMineral2X = (int) recognition.getLeft();
                                }
                            }
                            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                Gotcha = true;
                                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                    GoldMineralPos = 0;
                                    telemetry.addData("Gold Mineral Position", "Left");
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    GoldMineralPos = 2;
                                    telemetry.addData("Gold Mineral Position", "Right");
                                } else {
                                    GoldMineralPos = 1;
                                    telemetry.addData("Gold Mineral Position", "Center");
                                }
                            }
                        }
                        telemetry.update();
                    }
                }
            }

            moveArm(-11/2, -0.4);
            moveRobot(0.2, 0.2);
            moveArm(4, 0.4);
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void moveArm(double rotations, double speed){
        bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.arm.setPower(0);
        bot.arm.setTargetPosition( (int)(1120 * rotations) );
        bot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.arm.setPower(speed);
        while (bot.arm.isBusy() && !isStopRequested()) {
            idle();
        }
        bot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.arm.setPower(0);
    }

    public void moveRobot(double rotations, double speed){
        bot.leftForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.rightForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.setLeft(0);
        bot.setRight(0);

        bot.leftForward.setTargetPosition((int)(1120 * rotations));
        bot.rightForward.setTargetPosition((int)(1120 * rotations));

        bot.leftForward.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.rightForward.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        bot.setLeft(speed);
        bot.setRight(speed);
        while (bot.leftForward.isBusy() && !isStopRequested()) {
            idle();
        }
        bot.leftForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.rightForward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bot.setLeft(0);
        bot.setRight(0);
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
