package com.houzhi.simbad;

import javax.vecmath.Vector3d;

import simbad.sim.Agent;

public class AttackPurposeRobot extends Agent {
	private Agent purpose ;
	
	public AttackPurposeRobot(Vector3d pos,Agent purpose) {
		super(pos, "AttackPurposeRobot");
		
		this.purpose  = purpose ;
	}

    /** This method is call cyclically (20 times per second)  by the simulator engine. */
    public void performBehavior() {

        // progress at 0.5 m/s
        setTranslationalVelocity(0.5);
        // frequently change orientation
        if ((getCounter() % 50) == 0)
            setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));
        if((getCounter()% 100 ) == 0 ){
        	rotateY(Math.PI  * (0.5 - Math.random()));
        }
        // print front sonar every 100 frames
        if (getCounter() % 100 == 0)
           ;

    }
	
}
