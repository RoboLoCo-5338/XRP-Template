// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Servo extends SubsystemBase {
    private final XRPServo m_armServo;
    public Servo(){
        // Device number 4 maps to the physical Servo 1 port on the XRP
        m_armServo = new XRPServo(4);
        
    }
    //TODO: Task 5-Write functions to control the arm. You should be able to set the arm's angle and get the angle
    //it is at.
    //HINT: Type 'm_armServo.' (without quotes) in a method body to see all of the different methods the servo has.
    public void setAngle(double angle) {
        //sets angle to this
        m_armServo.setAngle(angle);
    }

    public void adjustAngleBy(double adjustAmount) {
        //makes the current angle larger or smaller by adjusting angle by adjustAmount
        m_armServo.setAngle(adjustAmount+m_armServo.getAngle());
    }
}
