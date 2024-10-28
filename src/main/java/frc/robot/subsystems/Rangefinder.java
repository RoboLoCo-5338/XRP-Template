// Credit to Team 5338 for any changes to the base command XRP template.
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.xrp.XRPRangefinder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Rangefinder extends SubsystemBase{
    private final XRPRangefinder m_rangefinder;
    public Rangefinder(){
        m_rangefinder=new XRPRangefinder();
    }
    //TODO: Task 8-Write a method to get the distance returned by the sensor in inches. Name it getDistance
    //HINT: Type 'm_rangefinder.' (without quotes) in a method body to see all of the different methods the rangefinder has.
}
