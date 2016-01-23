package org.usfirst.frc.team2876.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
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
	//these are for the jaguar based showbot
//	int frontLeftMotorPWM = 4;
//	int frontRightMotorPWM = 3;
//	int rearLeftMotorPWM = 1;
//	int rearRightMotorPWM = 2;
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
//    	SpeedController frontLeftMotor, SpeedController rearLeftMotor,
//        SpeedController frontRightMotor, SpeedController rearRightMotor
//    	myRobot = new RobotDrive(frontLeftMotorPWM, frontRightMotorPWM, rearLeftMotorPWM, rearRightMotorPWM);
//    	myRobot = new RobotDrive(frontLeftMotor, frontRightMotor, rearLeftMotor, rearRightMotor);
    	myRobot = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    	stick = new Joystick(0);
    	System.out.print("riolog");
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	this.autoLoopCounter = 0;
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
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	System.out.print("TeleopInIt");
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
//        myRobot.arcadeDrive(stick);
    	
    	myRobot.arcadeDrive(-stick.getY() * .75, -stick.getX() * .75, true);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
