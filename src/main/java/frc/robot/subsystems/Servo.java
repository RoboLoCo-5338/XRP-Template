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
}
