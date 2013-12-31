package com.houzhi.simbad.algorithm;

import java.util.Random;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.houzhi.simbad.Constant;
import com.houzhi.simbad.PathRobot;

public class PotentialControl extends AbsPathControl {

	public PotentialControl(PathRobot absPathRobot) {
		super(absPathRobot);
		
		sonars.setUpdateOnEachFrame(true);
	}
	
	boolean isFirst = true ;
	
	private Vector3d rotate = new Vector3d() ;
	
	
	@Override
	public void init() {
		super.init();
		rotate = new Vector3d() ;
		isFirst = true ;
	}

	@Override
	public void run() {
		if(isFirst){
        	rotate.y = Math.PI / 4 ;
        	pathRobot.rotateY(- rotate.y );
        	pathRobot.setTranslationalVelocity(0);
        	isFirst = false ;
        	return ;
        }
		
//		if(touchToAvoid()){
//        	return ;
//        }
		
		Point3d coord = new Point3d();
		pathRobot.getCoords(coord);
		if(arive(coord)){
			
			pathRobot.setRotationalVelocity(0);
			pathRobot.setTranslationalVelocity(0);
//			pathRobot.detach() ;
			return ;
		}
		
		double transX = 0 ,transZ = 0 ;
		double angle = 0;
		double measure ;
		for(int i = 0 ; i != sonars.getNumSensors() ; ++ i){
			measure = sonars.getMeasurement(i);
			if( Double.isInfinite(measure) ){
				//û������
				continue ;
			}
			angle = sonars.getSensorAngle(i) + Math.PI ; //����pi ������
			angle = - angle + rotate.y;   //ֱ������
			//ʮ������ϲ�
			double strangs ;
			if(measure <= 0.5 ){
				strangs = 1 ;
			}else if(measure <= 1){
				strangs = 1 /2 ;
			}else{
				strangs = 1 / measure /2 ;
			}
			
			transX += (strangs) * Math.cos(angle);
			transZ += (strangs) * Math.sin(angle);
		}
		//�ϰ������ ���õķ���
		double otherAngle = getAngle( transX, transZ);;
		
		//�յ���
		angle = Math.atan((purpose.z - coord.z)/(purpose.x - coord.x));
		transX += getPurposeVelocity(coord) *Math.cos(angle);
		transZ += getPurposeVelocity(coord) * Math.sin(angle);
		
		double purposeAngle = angle ;
		//
		angle =getAngle( transX, transZ);
		
		double dtemp =  (purposeAngle - otherAngle) / Math.PI ;
		int temp = (int) (dtemp);
		if( Math.abs(temp - dtemp) <0.00001 && ! Double.isNaN(otherAngle)){
			angle =purposeAngle + ( new Random().nextDouble() -0.5 ) * Math.PI/4;
		}
		
		double strangs = Math.sqrt(transZ*transZ + transX* transX) ;
		if(strangs <= 0.01){
			strangs = (new Random().nextDouble() / 2 +0.3);
		}
		
		pathRobot.setTranslationalVelocity(strangs);
		
		//����Ҫ����ƫ�ƵĽǶ�
		pathRobot.rotateY(-(angle - rotate.y ) );
		
		//���������������Ӧ��Ҫƫ��x��ĽǶ�
		rotate.y = angle ;
	}
	
	public double getPurposeVelocity(Point3d coord ){
		double distance = Math.abs((purpose.z - coord.z)* (purpose.z - coord.z) 
				+(purpose.x - coord.x) * (purpose.x - coord.x) ) ;
//		if(distance < PURPOSE_POTENTIAL ){
//			return 1 / distance ;
//		}else{
			return PURPOSE_POTENTIAL ;
//		}
	}
	
	
	final double PURPOSE_POTENTIAL = 1;
}
