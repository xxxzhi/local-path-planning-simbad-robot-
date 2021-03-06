package com.houzhi.simbad.env;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.houzhi.simbad.RobotTestSimBad;
import com.houzhi.simbad.algorithm.AbsPathControl;
import com.houzhi.simbad.algorithm.GridPathControl;
import com.houzhi.simbad.algorithm.PotentialControl;
import com.houzhi.simbad.robot.RandomRobot;
import com.houzhi.simbad.util.Constant;

import simbad.demo.DemoManager;
import simbad.gui.Simbad;
import simbad.sim.Arch;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

public class HardEnvironmentDescription extends AbsTestEnvironmentDescription {

	protected void initEnv(){
		light1IsOn = true;
        light2IsOn = false;
        Wall w1 = new Wall(new Vector3d(Constant.MAX_HEIGHT, 0, 0), 19, 1, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-Constant.MAX_HEIGHT, 0, 0), 19, 2, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, Constant.MAX_WIDTH), 19, 1, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -Constant.MAX_WIDTH), 19, 2, this);
        add(w4);
        
        
        Box b1 = new Box(new Vector3d(-3, 0, -3), new Vector3f(1, 1, 1),
                this);
        add(b1);
        add(new Arch(new Vector3d(3, 0, -5), this));

        add(new Arch(new Vector3d(-4, 0, 3), this));
        
        add(new Arch(new Vector3d(-1, 0, 8), this));
        
        Arch a = new Arch(new Vector3d(6, 0, 0), this);
        a .rotate90(1);
        add(a);
        
        add(new Box(new Vector3d(2, 0, 3), new Vector3f(2, 1, 3), this));
        
        Box purpose = new Box(new Vector3d(7,1, 7), new Vector3f(1, 1, 1), this) ;
        purpose.setCanBeTraversed(true);
        purpose.setColor(new Color3f(255, 0, 0));
        add(purpose);
//        add(new Purpose());
	}
	
	protected void addRandomRobot(){
		//随机机器人
	   add(new RandomRobot(new Vector3d(2, 0, 1)));
	   
	   add(new RandomRobot(new Vector3d(2, 0, -7)));
	   add(new RandomRobot(new Vector3d(-5, 0, 2)));
	   
	   //起点位置
	   add(new RandomRobot(new Vector3d(-5, 0, -5)));
	   add(new RandomRobot(new Vector3d(-5, 0, -7)));
	   add(new RandomRobot(new Vector3d(-7, 0, -5)));
	   
	   add(new RandomRobot(new Vector3d(-1, 0, -1)));
	   
	   add(new RandomRobot(new Vector3d(3, 0, -5)));
	   
	   add(new RandomRobot(new Vector3d(-5, 0, 3)));
	   
	   add(new RandomRobot(new Vector3d(-5, 0, 6)));
	   
	   add(new RandomRobot(new Vector3d(-1, 0, 5)));
	   
	   
	   //接近目標
	   add(new RandomRobot(new Vector3d(5, 0, 5)));
	   
	   add(new RandomRobot(new Vector3d(5, 0, 7)));
	}
	

	
    public HardEnvironmentDescription() {
    	initEnv();
    	
    	addRandomRobot();
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(Math.PI / (Math.PI/2) == 2 );
		final HardEnvironmentDescription myEnv1 = new HardEnvironmentDescription();
		
		AbsPathControl control1 = myEnv1.addPotentialRobot();
		final RobotTestSimBad frame1 = new RobotTestSimBad(myEnv1, false);
		frame1.setTitle("PotentialRobot Test");
		control1.setCompleteInterface(new AbsPathControl.OnCompleteInterface() {
			
			@Override
			public void onCompleteListener() {
				frame1.stop();
			}
		});
		
		
//		frame1.add
//		PopupMenu menu = new PopupMenu("select") ;
//		MenuItem item = new MenuItem("PotentialControl");
//		item.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				if(arg0.getID() == ActionEvent.MOUSE_EVENT_MASK){
//					
//				}
//			}
//		});
//		menu.add(item);
//		frame.add(menu);
		
		final HardEnvironmentDescription myEnv = new HardEnvironmentDescription();
		AbsPathControl control = myEnv.addGridRobot();
		final RobotTestSimBad frame = new RobotTestSimBad(myEnv, false);
		
		control.setCompleteInterface(new AbsPathControl.OnCompleteInterface() {
			
			@Override
			public void onCompleteListener() {
				
				frame.stop();
			}
		});
		
		frame.setTitle("GridRobot Test");
		frame.setBounds(frame1.getWidth(), 0, frame.getWidth(), frame.getHeight());
	}
}
