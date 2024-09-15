// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.XRPDrivetrain;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/** A file that should contain all the commands that relate to the drivetrain. */
public class DriveCommands {

  /**
   * Method that causes the XRP to drive a certain distance
   * @param distance Distance that the XRP drives in inches
   * @return Command that causes the XRP to execute the functions below
   */
  public static Command driveDistance(double distance){
    //A functional command is returned. A functional command is a basic command with parameters for initialization code, execution code, ending code, a boolean supplier to cause the command to end, and required subsystems.
    return new FunctionalCommand(
      /* 
       * The arrow -> causes the code to be what is called a "lambda". This means that the code is considered to be a method without needing to define it like normal.
       * If you want to make multiple lines of code to occur, you can use 
       * () -> {
       *  line1; 
       *  line2; 
       *  line3;
       * }
      */
      //The first parameter is what happens upon initialization of the command, aka what should prepare the robot for the command to occur.
      //Causes the XRP to stop to prepare for movement, and resets the sensors that detect how much the wheels have turned.
      () -> {
        RobotContainer.m_xrpDrivetrain.arcadeDrive(0, 0); 
        RobotContainer.m_xrpDrivetrain.resetEncoders();
      }, 
      
      //The second parameter is what is called every time the scheduler runs while the command is scheduled., aka what the main portion of the command is.
      //Causes the XRP to drive.
      () -> RobotContainer.m_xrpDrivetrain.arcadeDrive(1, 0),

      //The third parameter is what happens upon ending the command.
      //Causes the XRP to stop.
      interrupted -> RobotContainer.m_xrpDrivetrain.arcadeDrive(0, 0),

      //The fourth parameter is the boolean that determines if the command ended.
      //Checks if the average distance traveled by both of the wheels is at least the distance the XRP needs to travel.
      () -> Math.abs(RobotContainer.m_xrpDrivetrain.getAverageDistanceInch()) >= distance,

      //The fifth parameter onwards are the required subsystems. This means that the command doesn't occur if there is a conflicting command that uses the same subsystem.
      //Sets the drivetrain as a requirement.
      RobotContainer.m_xrpDrivetrain
      );
  }

  /**
   * Alternate implementation of the above method using a class instead of a command. Slightly more verbose, but easier to read.
   * Our team usually uses code that looks like the above command.
   * Usage is basically the same, however add new in front of the function call.
   * Example: new DriveCommands.AltDriveDistance(5)
   */
  public static class AltDriveDistance extends Command{
    /**
     * Distance in inches the command makes the XRP move.
     */
    private double distance;
    /**
     * Constructor, or what parameters the code takes in.
     * @param distance The distance the XRP drives in inches
     */
    public AltDriveDistance(double distance){
      //Adds drivetrain as a requirement. This means that the command doesn't occur if there is a conflicting command that uses the same subsystem.
      //Copy and paste the command for more subsystems.
      addRequirements(RobotContainer.m_xrpDrivetrain);


    }

    /** 
     * Called when the command is initially scheduled.
     * Sets the XRP's speed to zero to prepare it for moving, and resets the sensors that detect how much the wheels have turned.
     */
    @Override
    public void initialize() {
      RobotContainer.m_xrpDrivetrain.arcadeDrive(0, 0);
      RobotContainer.m_xrpDrivetrain.resetEncoders();
    }

    /** 
     * Called every time the scheduler runs while the command is scheduled.
     * Makes the XRP drive forward.
     */
    @Override
    public void execute() {
      RobotContainer.m_xrpDrivetrain.arcadeDrive(1, 0);
    }

    /** 
     * Called once the command ends or is interrupted.
     * Stops the XRP.
     */
    @Override
    public void end(boolean interrupted) {
      RobotContainer.m_xrpDrivetrain.arcadeDrive(0, 0);
    }

    /** 
     * Returns true when the command should end.
     * Checks if the average distance traveled by the XRP's wheels is high enough.
     */
    @Override
    public boolean isFinished() {
      return Math.abs(RobotContainer.m_xrpDrivetrain.getAverageDistanceInch()) >= distance;
    }

  }

  /**
   * A command that sets the speed of the robot based on an arcade control scheme, using an Instant Command.
   * Because this value is something that changes based on controller input, we want the values to be rechecked periodically.
   * For this, we use what is called a supplier, which is any function which returns a double.
   * By using a supplier, instead of the drive speed being a constant 0, the command instead runs the supplier each time it runs to determine the correct speed.
   * @param forwardSpeed Speed the robot goes forward. Is a supplier, and therefore must be a method or a lambda.
   * @param turnSpeed Speed the robot turns. Is a supplier, and therefore must be a method or a lambda.
   * @return Command to arcade drive the XRP
   */
  public static Command arcadeDriveCommand(Supplier<Double> forwardSpeed, Supplier<Double> turnSpeed){
    System.out.println("runs");
    //This uses an InstantCommand, which shouldn't be a class. An Instant Command immediately executes, and only takes in fields for what it should do
    //and the required subsystems.
    //Useful for simple commands.
    return new InstantCommand(
      //Tells the XRP to drive at the given speeds by using .get(), which gets the value returned by the supplier
      ()->RobotContainer.m_xrpDrivetrain.arcadeDrive(forwardSpeed.get(), turnSpeed.get()), 
      RobotContainer.m_xrpDrivetrain
      );
  }

  public static class TurnDegrees extends Command {
    private final double m_degrees;
    private final double m_speed;
    private final XRPDrivetrain m_drive;
    /**
     * Creates a new TurnDegrees. This command will turn your robot for a desired rotation (in
     * degrees) and rotational speed.
     *
     * @param speed The speed which the robot will drive. Negative is in reverse.
     * @param degrees Degrees to turn. Leverages encoders to compare distance.
     * @param drive The drive subsystem on which this command will run
     */
    public TurnDegrees(double speed, double degrees) {
      m_degrees = degrees;
      m_speed = speed;
      m_drive = RobotContainer.m_xrpDrivetrain;
      addRequirements(m_drive);
    }
  
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      // Set motors to stop, read encoder values for starting point
      m_drive.arcadeDrive(0, 0);
      m_drive.resetEncoders();
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      m_drive.arcadeDrive(0, m_speed);
    }
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      m_drive.arcadeDrive(0, 0);
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      /* Need to convert distance travelled to degrees. The Standard
         XRP Chassis found here, https://www.sparkfun.com/products/22230,
         has a wheel placement diameter (163 mm) - width of the wheel (8 mm) = 155 mm
         or 6.102 inches. We then take into consideration the width of the tires.
      */
      double inchPerDegree = Math.PI * 6.102 / 360;
      // Compare distance travelled from start to distance based on degree turn
      return getAverageTurningDistance() >= (inchPerDegree * m_degrees);
    }
  
    private double getAverageTurningDistance() {
      double leftDistance = Math.abs(m_drive.getLeftDistanceInch());
      double rightDistance = Math.abs(m_drive.getRightDistanceInch());
      return (leftDistance + rightDistance) / 2.0;
    }
  }

  //TODO: Task 1-Rewrite TurnDegrees as a function
  //Code here:

  //TODO: Task 3-Rewrite the following functions to use tank drive
  //driveDistance or AltDriveDistance(choose 1)
  //arcadeDriveCommand(rename as tankDriveCommand)
  //TurnDegrees(either class or function, choose 1)

  /**
   * Commands can also be sequential, which is where they execute one after another. This is really good for automatic behavior.
   * @return A command that drives forward 5 inches, turns 90 degrees, and drives forward 4 inches.
   */
  public static Command sequentialExampleCommand(){
    return new SequentialCommandGroup(
      driveDistance(5),
      new DriveCommands.TurnDegrees(1, 90),
      driveDistance(4)
    );
  }

  //TODO: Task 9-Write a Command(function or class) that causes the XRP to drive until the distance returned by the rangefinder
  //is less than 2 inches. Afterwards, test it by replacing the return value of getAutonomousCommand() in RobotContainer and setting it to instead return the command you wrote.

  //TODO: Task 10-Write a Sequential Command Group to move the XRP backwards 2 inches, set the arm preset to index 1, spin the XRP 360 degrees
  //and move the XRP forward 3 inches. Afterwards, test it by replacing the return value of getAutonomousCommand() in RobotContainer and setting it to instead return the command you wrote.
}
