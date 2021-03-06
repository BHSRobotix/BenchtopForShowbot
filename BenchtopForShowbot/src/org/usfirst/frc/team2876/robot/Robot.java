package org.usfirst.frc.team2876.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Joystick controller, left, right;
	JoystickButton aButton, bButton, xButton, yButton, lStick, rStick;
	AnalogGyro gyro;
	boolean isTankDrive, isAButtonBeingPressed, isBButtonBeingPressed, isSensitive;
//	DigitalInput limitSwitch;
	PIDController pid;
	Encoder leftEnc, rightEnc;
	CameraServer microsoftCam;
	Talon frontLeftMotor = new Talon(7);
	Talon frontRightMotor = new Talon(6);
	Talon rearLeftMotor = new Talon(8);
	Jaguar rearRightMotor = new Jaguar(9);
	int autoLoopCounter;
	double sensitivity;
	boolean isDPadTopPressed = false, isDPadBottomPressed = false;
	
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    	controller = new Joystick(0);
    	gyro = new AnalogGyro(0);
    	gyro.setSensitivity(.007);
    	rightEnc = new Encoder(7, 6);
    	leftEnc = new Encoder(9, 8);
    	sensitivity = .65;
    	aButton = new JoystickButton(controller, 1);
    	bButton = new JoystickButton(controller, 2);
    	xButton = new JoystickButton(controller, 3);
    	yButton = new JoystickButton(controller, 4);
    	isTankDrive = false;
    	isSensitive = true;
    	microsoftCam = CameraServer.getInstance();
    	microsoftCam.startAutomaticCapture("cam1");
    	isAButtonBeingPressed = false;
    	isBButtonBeingPressed = false;
//    	limitSwitch = new DigitalInput(0);
    	pid = new PIDController(.03, .0, .025, gyro, 
    		new PIDOutput(){ 
    			public void pidWrite(double output) {
    				myRobot.setLeftRightMotorOutputs(output, -output);
    				//myRobot.arcadeDrive(0,output);
    				//myRobot.tankDrive(.3+output, -.3-output);
    				System.out.println(output +":"+ gyro.pidGet() +":"+pid.getError());
    			}
    		}
    				);
    	pid.setContinuous(true);
    	pid.setAbsoluteTolerance(.05);
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
    }

    /**
     * This function is called periodically during autonomous
     */
//    public void autonomousPeriodic() {
//    	if(autoLoopCounter < 100) {
//			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
//			autoLoopCounter++;
//		} else {
//			myRobot.drive(0.0, 0.0); 	// stop robot
//		}
//    }
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
    	updateSmartDashboard();
    	if(controller.getPOV() == 180) sensitivity = .65;
    	if(controller.getPOV() == 0) sensitivity = 1;
    	if(!aButton.get() && isAButtonBeingPressed) toggleTankDrive();
    	isAButtonBeingPressed = aButton.get();
//    	if(!bButton.get() && isBButtonBeingPressed) toggleSensitivity();
//    	isBButtonBeingPressed = bButton.get();
    	double constant = .75;
//    	sensitivity = isSensitive ? .65 : 1;
    	double leftY = -(constant * Math.pow(controller.getY(), 3) + (1 - constant)* controller.getY()) * sensitivity;
    	if (isTankDrive) {
    		double rightY = -(constant * Math.pow(getRightY(), 3) + (1 - constant)* getRightY()) * sensitivity;
    		myRobot.tankDrive(leftY, rightY, true);
    	} else {
        	double leftX = -getRightX() * .75;
    		myRobot.arcadeDrive(leftY, leftX, true);
    	}
    }
    
    public void updateSmartDashboard(){
    	SmartDashboard.putData("Right Encoder", rightEnc);
    	SmartDashboard.putData("Left Encoder", leftEnc);
    }
    
    public void toggleTankDrive(){
    	isTankDrive = !isTankDrive;
    }
    
    public void toggleSensitivity(){
    	isSensitive = !isSensitive;
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    public double getRightX() {
        return controller.getRawAxis(4);
    }

    public double getRightY() {
        return controller.getRawAxis(5);
    }
    
}
