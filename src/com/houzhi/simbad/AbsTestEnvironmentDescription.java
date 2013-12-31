package com.houzhi.simbad;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.houzhi.simbad.algorithm.AbsPathControl;
import com.houzhi.simbad.algorithm.GridPathAlgorithm;
import com.houzhi.simbad.algorithm.PotentialControl;

import simbad.sim.Arch;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

public abstract class AbsTestEnvironmentDescription extends EnvironmentDescription {
	protected abstract void initEnv();
	
	protected abstract void addRandomRobot();
	
	
	public AbsPathControl addGridRobot(){
		PathRobot robot = new PathRobot();
        AbsPathControl control ;
        control = new GridPathAlgorithm(robot, Constant.MAX_WIDTH, Constant.MAX_HEIGHT);
        control.setPurpose(new Point3d(new double[]{7,0,7}));
        
        
        robot.setControl(control);
        add(robot);
        return control ;
	}
	
	public AbsPathControl addPotentialRobot(){
		PathRobot robot = new PathRobot();
		AbsPathControl control ;
		//	      control = new GridPathAlgorithm(robot, Constant.MAX_WIDTH, Constant.MAX_HEIGHT);
		control = new PotentialControl(robot);
		control.setPurpose(new Point3d(new double[]{7,0,7}));
	        
		robot.setControl(control);
		add(robot);
		
		return control ;
	}
}
