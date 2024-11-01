// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.ServoCommands;
import frc.robot.subsystems.Rangefinder;
import frc.robot.subsystems.Servo;
import frc.robot.subsystems.XRPDrivetrain;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  private SendableChooser<Integer> m_chooser = new SendableChooser<>();
  private boolean showingSensorDistance = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    for(int i=1; i<=10; i++){
      m_chooser.addOption("Task " + i, i);
    }
    m_chooser.addOption("Task 3 Bonus", 11);
    m_chooser.addOption("Task 7 Bonus", 12);
    SmartDashboard.putData("Task Selector", m_chooser);
    SmartDashboard.putString("What should happen: ", "");
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    // schedule the autonomous command (example)
    // if (m_autonomousCommand != null) {
    //   m_autonomousCommand.schedule();
    // }
    switch (m_chooser.getSelected()) {
      case 1:
        SmartDashboard.putString("What should happen: ", "The XRP should turn about 90 degrees(don't worry if it's not accurate!)");
        DriveCommands.turnDegreesCommand(1, 90).schedule();
        break;
      case 2:
      //TODO for Kavin: Replace the instant commands with runOnce
        SmartDashboard.putString("What should happen: ", "The XRP should drive straight for 3 seconds, turn right for 2 seconds, turn left for 2 seconds, and then stop.");
        new SequentialCommandGroup(
          new ParallelDeadlineGroup(
            new WaitCommand(3), 
            new RunCommand(() -> {
              try {
                  XRPDrivetrain.class.getMethod("tankDrive", double.class, double.class).invoke(RobotContainer.m_xrpDrivetrain, 1, 1);
              } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              }, 
              RobotContainer.m_xrpDrivetrain
            )
          ),
          new ParallelDeadlineGroup(
            new WaitCommand(2), 
            new RunCommand(() -> {
              try {
                  XRPDrivetrain.class.getMethod("tankDrive", double.class, double.class).invoke(RobotContainer.m_xrpDrivetrain, 1, -1);
              } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              }, 
              RobotContainer.m_xrpDrivetrain
            )
          ),
          new ParallelDeadlineGroup(
            new WaitCommand(2), 
            new RunCommand(() -> {
              try {
                  XRPDrivetrain.class.getMethod("tankDrive", double.class, double.class).invoke(RobotContainer.m_xrpDrivetrain, -1, 1);
              } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              }, 
              RobotContainer.m_xrpDrivetrain
            )
          ),
          new InstantCommand(() -> {
            try {
                XRPDrivetrain.class.getMethod("tankDrive", double.class, double.class).invoke(RobotContainer.m_xrpDrivetrain, 0, 0);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
            }, 
            RobotContainer.m_xrpDrivetrain
        )).schedule();
        break;
      case 3:
        SmartDashboard.putString("What should happen: ", "The XRP should drive 5 inches(don't worry if it's not accurate!). tankDriveCommand will be tested in Task 4");
        try {
          ((Command) DriveCommands.class.getMethod("tankDriveDistance", double.class).invoke(null, 5)).schedule();
        } catch (NoSuchMethodException e) {
          // TODO Auto-generated catch block
          try {
            ((Command) Class.forName("frc.robot.commands.DriveCommands$TankDriveDistance").getDeclaredConstructor(double.class).newInstance(5)).schedule();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case 4:
        SmartDashboard.putString("What should happen: ", "Change the mode from Auto to Teleop. You should now be able to control the XRP using a tank drive scheme!");
        break;
      case 5:
        SmartDashboard.putString("What should happen: ", "The arm should move to 30 degrees, move to 142.5 degrees, and put the number of degrees on SmartDashboard(should be 142.5)");
        try {
          Servo.class.getMethod("setServoAngle", double.class).invoke(RobotContainer.m_servo, 30);
          double t=System.currentTimeMillis();
          while(System.currentTimeMillis()-t<=50){}
          Servo.class.getMethod("setServoAngle", double.class).invoke(RobotContainer.m_servo, 142.5);
          SmartDashboard.putNumber("Servo Angle", (Double) Servo.class.getMethod("getServoAngle").invoke(RobotContainer.m_servo));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case 6:
        SmartDashboard.putString("What should happen: ", "The XRP should move its servo to each of the presets.");
        for(int i=0; i<4; i++){
          try {
            ( (Command) ServoCommands.class.getMethod("servoPresetCommand", int.class).invoke(null, i)).schedule();
          } 
          catch(NoSuchMethodException e){
            try {
              ((Command) Class.forName("frc.robot.commands.ServoCommands$ServoPresetCommand").getDeclaredConstructor(int.class).newInstance(i)).schedule();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
              | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        break;
      case 7:
        SmartDashboard.putString("What should happen: ", "Change the mode from Auto to Teleop. You should now be able to control the XRP's servo!");
        break;
      case 8:
        SmartDashboard.putString("What should happen: ", "You should now see the distance the rangefinder reads!");
        showingSensorDistance=true;
        break;
      case 9:
        SmartDashboard.putString("What should happen: ", "The XRP should execute the command and move until an object in front of it is less than 2 inches away.");
        try {
          ((Command) DriveCommands.class.getMethod("driveToWall").invoke(null)).schedule();
        } catch (NoSuchMethodException e) {
          // TODO Auto-generated catch block
          try {
            ((Command) Class.forName("frc.robot.commands.DriveCommands$DriveToWall").getDeclaredConstructor().newInstance()).schedule();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case 10:
        SmartDashboard.putString("What should happen: ", "The XRP should now go through the Parallel Command Group you programmed!");
        try {
          ((Command) DriveCommands.class.getMethod("sequentialCommand").invoke(null)).schedule();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
            | SecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case 11:
        SmartDashboard.putString("What should happen: ", "The XRP should turn 38 degrees(don't worry if its not accurate!)");
        try {
          ((Command) DriveCommands.class.getMethod("tankTurnDegrees", double.class).invoke(null, 38)).schedule();
        } catch (NoSuchMethodException e) {
          // TODO Auto-generated catch block
          try {
            ((Command) Class.forName("frc.robot.commands.DriveCommands$TankTurnDegrees").getDeclaredConstructor(double.class).newInstance(38)).schedule();
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      case 12:
        SmartDashboard.putString("What should happen: ", "The XRP's servo should now continuously move without a RepeatCommand!");
        break;
    }
  }
  

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if(showingSensorDistance)
      try {
        SmartDashboard.putNumber("Rangefinder Distance", (double) Rangefinder.class.getMethod("getDistance").invoke(RobotContainer.m_rangefinder));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
