package com.houzhi.simbad.algorithm;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.RangeSensorBelt;

import com.houzhi.simbad.robot.PathRobot;


/**
 * 机器人路径规划算法抽象类
 * @author houzhi
 *
 */
public abstract class AbsPathControl implements Runnable{
	public interface OnCompleteInterface{
		void onCompleteListener();
	}
	
	protected OnCompleteInterface completeInterface = null ;
	
	
	
	public OnCompleteInterface getCompleteInterface() {
		return completeInterface;
	}

	public void setCompleteInterface(OnCompleteInterface completeInterface) {
		this.completeInterface = completeInterface;
	}

	protected PathRobot pathRobot ;

	/**
	 * 声呐
	 */
	protected RangeSensorBelt sonars;

	
	protected RangeSensorBelt bumpers ;
	
	
	public AbsPathControl(PathRobot absPathRobot){
		this.pathRobot = absPathRobot ;
		
		sonars = pathRobot.getSonars() ;
		bumpers = pathRobot.getSonars() ;
		
		sonars.setUpdateOnEachFrame(true);
		bumpers.setUpdateOnEachFrame(true);
	}
	
	protected Point3d purpose ;
	public Point3d getPurpose() {
		return purpose;
	}
	
	public void setPurpose(Point3d purpose){
		this.purpose = purpose ;
	}
	
	public double getAngle(double transX, double transZ){
		double angle =  Math.atan(transZ/transX);
		if(transZ < 0 && transX < 0 || transZ > 0 && transX < 0 ){
			angle +=Math.PI ; 
		}
		
		if(Double.isNaN(angle)){
			if(transX > 0){
				angle = 0 ;
			}else{
				angle = Math.PI ;
			}
		}
		
		return angle ;
	}
	
	//机器人旋转角度
	protected Vector3d rotate = new Vector3d() ;
	
	class TouchToAvoid{
		
		public boolean avoid(){
			boolean hasTouched = false ; 
			
			for(int i = 0 ; i != sonars.getNumSensors() ; ++ i){
				
				if( sonars.getMeasurement(i) < 0.1 ){
					//有撞击 避开
					double d = sonars.getSensorAngle(i);
					d += Math.PI ;
					pathRobot.rotateY( d - rotate.y);
					rotate.y =  rotate.y -d ;
					
					pathRobot.setTranslationalVelocity(1);
					hasTouched = true ;
					break; 
				}
			}
			
			return hasTouched ;
		}
	}
	TouchToAvoid t = new TouchToAvoid() ;
	protected boolean touchToAvoid(){
		return  t.avoid() ;
	}
	
	
	protected boolean arive(){
		boolean res = pathRobot.anOtherAgentIsVeryNear();
		if(res){
			if(completeInterface != null ){
				completeInterface .onCompleteListener();
			}
		}
		return res ;
	}
	
	protected boolean arive(Point3d coord){
		
		if(Math.abs(purpose.x - coord.x) < 0.2 && Math.abs(purpose.z - coord.z ) < 0.2){
			if(completeInterface != null ){
				completeInterface .onCompleteListener();
			}
			return true ;
		}else
			return false ;
	}
	
	/**
	 * 初始化
	 */
	public void init(){}

	
	
	
}
