package com.houzhi.simbad;


import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.houzhi.simbad.algorithm.AbsPathControl;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;


/**
 * 路径机器人类
 * @author houzhi
 *
 */
public class PathRobot extends Agent {

	/**
	 * 机器人路径算法
	 */
	protected AbsPathControl control ;
	
	/**
	 * 声呐
	 */
	private RangeSensorBelt sonars;
	public RangeSensorBelt getSonars() {
		return sonars;
	}

	
	private RangeSensorBelt bumpers ;
	
	public RangeSensorBelt getBumpers() {
		return bumpers;
	}

	
	private Vector3d initPosition = new Vector3d(-(Constant.MAX_HEIGHT-1), 0, -(Constant.MAX_WIDTH-1)) ;
	/**
	 * 
	 * @param pos
	 * @param name
	 */
	public PathRobot() {
		
		super((new Vector3d(-(Constant.MAX_HEIGHT-1), 0, -(Constant.MAX_WIDTH-1))) , "start");
		setColor(new Color3f(0, 255, 0));
		sonars = RobotFactory.addSonarBeltSensor(this, 24);
		bumpers = RobotFactory.addBumperBeltSensor(this, 16);
	}

	@Override
	protected void reset() {
		super.reset();
		moveToPosition(initPosition);
		control.init();
	}

	@Override
	protected void resetPosition() {
		super.resetPosition();
		moveToPosition(initPosition);
		control.init();
	}

	@Override
	protected void initBehavior() {
		super.initBehavior();
		moveToPosition(initPosition);
		control.init();
	}


	@Override
	protected void performBehavior() {
		super.performBehavior();
		control.run();
	}

	public void setControl(AbsPathControl control){
		this.control = control ;
	}

}
