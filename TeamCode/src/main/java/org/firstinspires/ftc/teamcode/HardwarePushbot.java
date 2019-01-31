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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwarePushbot
{
    /* Motor and Servo declaration */
    public DcMotor leftForward  = null;
    public DcMotor rightForward = null;
    public DcMotor leftBack  = null;
    public DcMotor rightBack = null;

    public DcMotor arm = null;
    public DcMotor balls = null;

    public Servo leftClaw = null;
    public Servo rightClaw = null;

    // Servo Positions
    public static final double mid_pos = 0.5;
    public static final double left_pos = 0;
    public static final double right_pos = 1;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushbot(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftForward = hwMap.get(DcMotor.class, "leftForward");
        leftBack = hwMap.get(DcMotor.class, "leftBack");

        rightForward = hwMap.get(DcMotor.class, "rightForward");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        arm = hwMap.get(DcMotor.class, "arm");
        balls = hwMap.get(DcMotor.class, "balls");

        // Define Servos
        leftClaw = hwMap.get(Servo.class, "leftClaw");
        rightClaw = hwMap.get(Servo.class, "rightClaw");

        // Set Motor Directions
        rightForward.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        leftForward.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        arm.setDirection(DcMotor.Direction.FORWARD);
        balls.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        rightForward.setPower(0);
        rightBack.setPower(0);

        leftForward.setPower(0);
        leftBack.setPower(0);

        arm.setPower(0);
        balls.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        rightForward.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftForward.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        balls.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize Servos
        leftClaw.setPosition(mid_pos);
        rightClaw.setPosition(mid_pos);
    }

    public void setLeft(double speed){
        leftBack.setPower(speed);
        leftForward.setPower(speed);
    }

    public void setRight(double speed){
        rightBack.setPower(speed);
        rightForward.setPower(speed);
    }

    public void setArm(double speed){
        arm.setPower(speed);
    }

    public void setBall(double speed){
        balls.setPower(speed);
    }

    public void setClaw(double position){
        leftClaw.setPosition(position);
        rightClaw.setPosition(1-position);
    }
 }

