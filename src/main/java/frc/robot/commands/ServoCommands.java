// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot.commands;

public class ServoCommands {
    //TODO: Task 6-Write commands to move the arm up, down, and to a specific preset. The latter should be done by taking in an integer input.
    //The presets are in the constants file.
    public static InstantCommand moveArmUp(integer numberr){
        return new InstantCommand()->{
            double angleNew = servo.getArmAngle() + numberr;
            servo.setArmAngle(angleNew);
        }
    }
    public static InstantCommand moveArmDown(integer numberr){
        return new InstantCommand () -> {
            double angleNew = servo.getArmAngle() - numberr;
            servo.setArmAngle(angleNew);
        }
    }
    public static InstantCommand presetArm(int presetI){
        return new InstantCommand () -> {
            if(presetI < Constants.servoPresets.length && presetI >=0){
                double AngleA = Constants.servoPresets[presetI];
                servo.setArmAngle(AngleA);
            }
        }
    }
}
