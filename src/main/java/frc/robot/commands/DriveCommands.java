// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.XRPDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/** A file that should contain all the commands that relate to the drivetrain. */
public class DriveCommands {

  /**
   * Method that causes the XRP to drive a certain distance
   * @param distance Distance that the XRP drives in inches
   * @return Command that causes the XRP to execute the functions below
   */
  public Command driveDistance(double distance){
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
      //Causes the XRP to stop to prepare for movement.
      () -> RobotContainer.m_xrpDrivetrain.tankDrive(0, 0), 
      
      //The second parameter is what is called every time the scheduler runs while the command is scheduled., aka what the main portion of the command is.
      //Causes the XRP to drive.
      () -> RobotContainer.m_xrpDrivetrain.tankDrive(1, 1),

      //The third parameter is what happens upon ending the command.
      //Causes the XRP to stop.
      interrupted -> RobotContainer.m_xrpDrivetrain.tankDrive(0, 0),

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
  public class AltDriveDistance extends Command{
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
     * Sets the XRP's speed to zero to prepare it for moving.
     */
    @Override
    public void initialize() {
      RobotContainer.m_xrpDrivetrain.tankDrive(0, 0);
    }

    /** 
     * Called every time the scheduler runs while the command is scheduled.
     * Makes the XRP drive forward.
     */
    @Override
    public void execute() {
      RobotContainer.m_xrpDrivetrain.tankDrive(1, 1);
    }

    /** 
     * Called once the command ends or is interrupted.
     * Stops the XRP.
     */
    @Override
    public void end(boolean interrupted) {
      RobotContainer.m_xrpDrivetrain.tankDrive(0, 0);
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
}
