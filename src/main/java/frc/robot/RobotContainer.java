// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.ServoCommands;
import frc.robot.subsystems.Rangefinder;
import frc.robot.subsystems.Servo;
import frc.robot.subsystems.XRPDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static final XRPDrivetrain m_xrpDrivetrain = new XRPDrivetrain();
  public static final Servo m_servo = new Servo();
  public static final Rangefinder m_rangefinder = new Rangefinder();
  public final CommandXboxController m_controller = new CommandXboxController(0);



  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    //The default command is the command that executes if no other command is using the specified subsystem.
    //This causes the xrp to drive based on the left joystick y and right joystick x of the controller by default.
    //It uses lambdas to make the parameters suppliers, which means that instead of them being constant, they rerun each time the command is scheduled.
    //The values are negated because the axes are flipped for some reason.
    //m_xrpDrivetrain.setDefaultCommand(DriveCommands.arcadeDriveCommand(()-> -m_controller.getLeftY(), () -> -m_controller.getRightX()));
    m_xrpDrivetrain.setDefaultCommand(DriveCommands.tankDriveCommand(()-> -m_controller.getLeftY(), () -> -m_controller.getRightY()));
    //TODO: Task 4-Comment out the above line by adding // to the left of it. Then, set the default command to be your tankDriveCommand.
    //HINT: In tank drive, the left wheel is controlled by the y axis of the left joystick and the y axis of the right joystick.
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //TODO: Task 7-Uncomment the lines below and replace the null values with the commands you created to move the servo up and down, such that the y button moves it up
    //and the a button moves it down.
    //BONUS:Complete one of the following: Look into all of the different types of commands in the documentation for WPILib. Try to find a command that does not need to be encapsulated
    //in a RepeatCommand to repeat, and replace the type of moveServoUp and moveServoDown(or whatever they are called) with that type of Command.
    //Then, delete the RepeatCommand, because your command should already repeat.

    m_controller.y().whileTrue(ServoCommands.servoAdjustAngleCommand(1));
    m_controller.a().whileTrue(ServoCommands.servoAdjustAngleCommand(-1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new FunctionalCommand(
      () -> {
        RobotContainer.m_xrpDrivetrain.tankDrive(0, 0);
        RobotContainer.m_xrpDrivetrain.resetEncoders();
      }, 
      () -> RobotContainer.m_xrpDrivetrain.tankDrive(1, 1),
      interrupted -> RobotContainer.m_xrpDrivetrain.tankDrive(0, 0),
      () -> (RobotContainer.m_rangefinder.findRange()) <= 2.0,
      RobotContainer.m_xrpDrivetrain, RobotContainer.m_rangefinder
    );
  }
}
