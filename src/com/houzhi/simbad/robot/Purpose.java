package com.houzhi.simbad.robot;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.houzhi.simbad.util.Constant;

import simbad.sim.Agent;

public class Purpose extends Agent {
	public Purpose() {
		super(new Vector3d(Constant.MAX_HEIGHT-1,0,Constant.MAX_WIDTH-1), "purpose");
		
		setColor(new Color3f(255, 0, 0));
	}
}
