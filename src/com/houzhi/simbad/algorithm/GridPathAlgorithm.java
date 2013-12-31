package com.houzhi.simbad.algorithm;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.RangeSensorBelt;

import com.houzhi.simbad.Constant;
import com.houzhi.simbad.PathRobot;

public class GridPathAlgorithm extends AbsPathControl {
	
	
	/**
	 * 环境的宽和高
	 */
	private final int envWidth ,envHeight;
	
	private static final int GRID_SIDE = 1;
	
	public GridPathAlgorithm(PathRobot absPathRobot,int width,int height) {
		super(absPathRobot);
		this.envHeight = height*2;
		this.envWidth = width*2 ;
		
		
		
		
//		frameSeconds = 1 / pathRobot.getFramesPerSecond();
		
		initGrid();
		
		absPathRobot.rotateY(Math.PI);
	}
	

	@Override
	public void init() {
		super.init();
		rotate = new Vector3d() ;
		isFirst = true ;
	}
	
	@Override
	public void setPurpose(Point3d purpose) {
		super.setPurpose(purpose);
		
		purposeGrid.x =(int ) purpose.x ;
		purposeGrid.y =(int ) purpose.y ;
		purposeGrid.z =(int ) purpose.z ;
	}



	private Point3d purposeGrid = new Point3d();
	
	private int sumGridX,sumGridZ;
	private void initGrid(){
		sumGridZ = envHeight / GRID_SIDE ;
		sumGridX = envWidth / GRID_SIDE ;
		
		Point3d coord = new Point3d();
		pathRobot.getCoords(coord);
		curGridX =  (int) (coord.x /GRID_SIDE);
		curGridZ = (int) (coord.z /GRID_SIDE);
		
		hasEnter = new boolean [sumGridX+2][sumGridZ+2];
	}
	
	
	private int curGridX,curGridZ;
	
	
	public static final double HINT_MAX_DIS = GRID_SIDE * 1.5;
	
	//帧速率 1/20 秒
	private double frameSeconds = 1/20;  
	boolean isFirst = true ;
	
	
	boolean hasEnter [][] ;
	
	@Override
	public void run() {
		
        if(isFirst){
        	rotate.y = Math.PI / 4 ;
        	pathRobot.rotateY(- rotate.y );
        	pathRobot.setTranslationalVelocity(GRID_SIDE);
        	isFirst = false ;
        	return ;
        }
		
//        if(touchToAvoid()){
//        	return ;
//        }
        
        //计算目的方向
        Point3d coord = new Point3d() ;
        pathRobot.getCoords(coord);
        
        if(arive(coord)){
        	pathRobot.setTranslationalVelocity(0);
        	pathRobot.setRotationalVelocity(0);
//        	pathRobot.detach();
        	return ;
        }
        
        //相对于x轴的角度
        Point3d purpose = getPurpose() ;
        curGridX =  (int) (coord.x /GRID_SIDE);
		curGridZ = (int) (coord.z /GRID_SIDE);
		
		hasEnter [ curGridX+Constant.MAX_WIDTH][curGridZ +Constant.MAX_WIDTH] = true ;
		
        double rotateAngle = getAngle(purposeGrid.x - curGridX ,purposeGrid.z - curGridZ) ;
		

        
        
        
		double beginCenterAngle = rotateAngle - rotate.y;
		
		double temp = beginCenterAngle /( Math.PI/4) ;
		int q = (int)temp ;   //第几个象限开始
		if(temp - q >(Math.PI / 8) ){
			q ++ ;
		}
		int index = 0 ;
		while(index< 4){
			
			boolean end = false;
			for(int j = - 1 ; j <=1 ; j+=2){   //两个方向
				q = (q + index*j) % 5; 
				
				
				// -1 表示逆时针方向
				double s1 = 0 ,s2 = 0 ;
				
				double  tempRotate = Math.PI/4 * q;
				switch(q){
				case 0:
					s1 = sonars.getFrontQuadrantMeasurement() ;
					s2 = bumpers.getFrontQuadrantHits() ;
					break ;
				case 1:
					s1 = sonars.getFrontRightQuadrantMeasurement() ;
					s2 = bumpers.getFrontRightQuadrantHits() ;
					
					break ;
				case -1:
					s1 = sonars.getFrontLeftQuadrantMeasurement();
					s2 = bumpers.getFrontLeftQuadrantHits() ;
					break ;
				case 2:
					s1 = sonars.getRightQuadrantMeasurement() ;
					s2 = bumpers.getRightQuadrantHits() ;
					break;
				case -2:
					s1 = sonars.getLeftQuadrantMeasurement() ;
					s2 = bumpers.getLeftQuadrantHits() ;
					break;
				case 3:
					s1 = sonars.getBackRightQuadrantMeasurement() ;
					s2 = bumpers.getBackRightQuadrantHits() ;
					break;
				case -3:
					s1 = sonars.getBackLeftQuadrantMeasurement() ;
					s2 = bumpers.getBackLeftQuadrantHits() ;
					break;
				case 4:
					s1 = sonars.getBackQuadrantMeasurement() ;
					s2 = bumpers.getBackQuadrantHits() ;
					break;
				}
				
				if(s1 < 1*3 
						 || s2 >= 1
						){
					//有障碍物
					
					continue;
				}
				rotate.y += tempRotate ;
				pathRobot.rotateY(- tempRotate);
				end = true ;
				break ;
			}
			index++;
			if(end)
				break;
		}
//		for(int i = 0 ; i != 5 ; ++ i){
//			boolean end = false;
//			
//			for(int j = - 1 ; j <=1 ; j+=2){   //两个方向
//				// -1 表示逆时针方向
//				double tempAngle = beginCenterAngle + j *i* Math.PI /4;
//				double minAngle = tempAngle - Math.PI / 4 ;
//				double maxAngle = tempAngle + Math.PI / 4 ;
//				
//				int s1 = sonars.getQuadrantHits(minAngle, maxAngle);
//				int s2 = bumpers.getQuadrantHits(minAngle, maxAngle);
//				
//				double  tempRotate = Math.PI/4 * i * j;
//				switch(i*j){
//				case 0:
//					s1 = sonars.getFrontQuadrantHits() ;
//					s2 = bumpers.getFrontQuadrantHits() ;
//					break ;
//				case 1:
//					s1 = sonars.getFrontRightQuadrantHits() ;
//					s2 = bumpers.getFrontRightQuadrantHits() ;
//					
//					break ;
//				case -1:
//					s1 = sonars.getFrontLeftQuadrantHits() ;
//					s2 = bumpers.getFrontLeftQuadrantHits() ;
//					break ;
//				case 2:
//					s1 = sonars.getRightQuadrantHits() ;
//					s2 = bumpers.getRightQuadrantHits() ;
//					break;
//				case -2:
//					s1 = sonars.getLeftQuadrantHits() ;
//					s2 = bumpers.getLeftQuadrantHits() ;
//					break;
//				case 3:
//					s1 = sonars.getBackRightQuadrantHits() ;
//					s2 = bumpers.getBackRightQuadrantHits() ;
//					break;
//				case -3:
//					s1 = sonars.getBackLeftQuadrantHits() ;
//					s2 = bumpers.getBackLeftQuadrantHits() ;
//					break;
//				case 4:
//					s1 = sonars.getBackQuadrantHits() ;
//					s2 = bumpers.getBackQuadrantHits() ;
//					break;
//				}
//				
//				double measurement = sonars.getQuadrantMeasurement(minAngle, maxAngle);
//				
//				if(s1 >2 
//						 || s2 >= 1
//						){
//					//有障碍物
//					
//					continue;
//				}
//				
//				
//				int xadd = 1 ;
//				int zadd = 1 ;
//				switch(i){
//				case 0:
//					xadd = 1 ;
//					zadd =  1 ;
//					break;
//				case 1:
//					if( j == 1 ){
//						zadd =  1 ;
//						zadd = 0 ;
//					}else{
//						zadd = 1 ;
//						zadd =  0 ;
//					}
//					break;
//				case 2:
//					
//					if( j == 1 ){
//						xadd = -1 ;
//						zadd =  1 ;
//					}else{
//						xadd =  1 ;
//						zadd =  -1 ;
//					}
//					break;
//				case 3:
//					
//					if( j == 1 ){
//						xadd = -1 ;
//						zadd =  0 ;
//					}else{
//						xadd = 0 ;
//						zadd =  -1 ;
//					}
//					break;
//				
//				case 4:
//					xadd = -1 ;
//					zadd =  -1 ;
//					break;
//				}
//				try{
////				if(hasEnter [ curGridX+Constant.MAX_WIDTH+xadd][curGridZ +Constant.MAX_HEIGHT+zadd] ){
////					//已经经历过
////					continue ; 
////				}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				rotate.y += tempRotate ;
//				pathRobot.rotateY(- tempRotate);
//				end = true ;
//				break ;
//			}
//			if(end)
//				break;
//		}
		
	}
}
