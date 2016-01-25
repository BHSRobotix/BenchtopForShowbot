package org.usfirst.frc.team2876.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Joystick stick;
	AnalogGyro gyro;
	PIDController pid;
	CANTalon frontLeftMotor = new CANTalon(3);
	CANTalon frontRightMotor = new CANTalon(1);
	CANTalon rearLeftMotor = new CANTalon(0);
	CANTalon rearRightMotor = new CANTalon(2);
	int autoLoopCounter;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    	stick = new Joystick(0);
    	gyro = new AnalogGyro(0);
    	pid = new PIDController(.5, 0, 0, gyro, 
    		new PIDOutput(){ 
    			public void pidWrite(double output) {
    				myRobot.setLeftRightMotorOutputs(output, -output);
    				//myRobot.arcadeDrive(0,output);
    				//myRobot.tankDrive(.3+output, -.3-output);
    				System.out.println(output +":"+ gyro.pidGet() +":"+pid.getError());
    			}
    		}
    				);
    	pid.setContinuous(false);
    	pid.setAbsoluteTolerance(10.0);
    	pid.setInputRange(-90, 90);
    	pid.setOutputRange(-.3, .3);
    	System.out.println("riolog");
    	myRobot.setSafetyEnabled(false);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	this.autoLoopCounter = 0;
    	gyro.reset();
    	pid.reset();
    	pid.setSetpoint(90);
    	pid.enable();
    	Timer.delay(1);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) {
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			autoLoopCounter++;
		} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    }
    public void autonomousDisabled(){
    	pid.disable();
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	System.out.println("TeleopInIt");
//    	gyro.reset();
//    	while(gyro.getAngle() < 90) myRobot.setLeftRightMotorOutputs(.25, -.25);
//    	myRobot.setLeftRightMotorOutputs(0, 0);
    	pid.disable();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	double constant = .75;
    	double yValue = -(constant * Math.pow(stick.getY(), 3) + (1 - constant)* stick.getY());
    	double xValue = -stick.getX() * .6;
    	myRobot.arcadeDrive(yValue, xValue, true);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
