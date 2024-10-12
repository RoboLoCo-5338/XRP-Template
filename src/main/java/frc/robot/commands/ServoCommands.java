// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Servo;
import frc.robot.subsystems.XRPDrivetrain;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ServoCommands {
    //private XRPServo test;
    //private static Servo hiIhopeThisWorks = new Servo();
    private static Constants constants = new Constants();

    //TODO: Task 6-Write commands to move the arm up, down, and to a specific preset. The latter should be done by taking in an integer input.
    //The presets are in the constants file.
    public static Command servoAdjustAngleCommand(double adjustAmount) {
        return new RunCommand(
            () -> RobotContainer.m_servo.adjustAngleBy(adjustAmount)
            //() -> frc.robot.subsystems.Servo.m_armServo.adjustAngleBy() or maybe RobotContainer.m_servo
            //note to self: if hiIhopeThisWorks doesn't work, then do ^^^ and change m_armServo to public
        );
    }

    public static Command servoSetAngleCommand(double angle) {
        return new InstantCommand(
            () -> RobotContainer.m_servo.setAngle(angle)
        );
    }

    public static Command servoPresetCommand(int index) {
        return new InstantCommand(
            () -> RobotContainer.m_servo.setAngle(constants.servoPresets[index])
        );
    }
}
