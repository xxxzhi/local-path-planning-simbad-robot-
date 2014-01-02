package com.houzhi.simbad;


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.houzhi.simbad.algorithm.AbsPathControl;
import com.houzhi.simbad.algorithm.PotentialControl;
import com.houzhi.simbad.env.HardEnvironmentDescription;
import com.houzhi.simbad.robot.PathRobot;

import simbad.demo.DemoManager;
import simbad.gui.AgentInspector;
import simbad.gui.Console;
import simbad.gui.ControlWindow;
import simbad.gui.Simbad;
import simbad.gui.WorldWindow;
import simbad.sim.Agent;
import simbad.sim.EnvironmentDescription;
import simbad.sim.SimpleAgent;
import simbad.sim.Simulator;
import simbad.sim.World;
//import javax.swing.UIManager;

/**
 * This is the Simbad application mainframe.
 *  
 */
public class RobotTestSimBad extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	static final String version="1.4";
	static int SIZEX = 800;
	static int SIZEY = 750;
	JMenuBar menubar;
    JDesktopPane desktop;
    WorldWindow worldWindow=null;
    ControlWindow controlWindow=null;
    World world;
    Simulator simulator;
    Console console=null;
    AgentInspector agentInspector=null;
    boolean backgroundMode;
   
    static  RobotTestSimBad simbadInstance=null;
    
    /** Construct Simbad application with the given environement description */
    public RobotTestSimBad(EnvironmentDescription ed, boolean backgroundMode) {
        super("Simbad  - version "+ version);
        simbadInstance = this;
        this.backgroundMode = backgroundMode;
        desktop = new JDesktopPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SIZEX, SIZEY);
        createGUI();
        start(ed);
        setVisible(true);
       
        }
 

    /** Create the main GUI. Only called once.*/
    private void createGUI() {
        desktop.setFocusable(true);
        getContentPane().add(desktop);
        menubar = new JMenuBar();
        menubar.add(DemoManager.createMenu(this));
        setJMenuBar(menubar);
    }
    
    /** Starts (or Restarts after releaseRessources) the world and simulator.*/
    private void start(EnvironmentDescription ed){
        System.out.println("Starting environmentDescription: "+ed.getClass().getName());
        world = new World(ed);
        simulator = new Simulator(desktop, world, ed);
        createInternalFrames();
        if (backgroundMode) {
            runBackgroundMode();
        }
    }
    
    /** Release all ressources. */
    private void releaseRessources(){
        simulator.dispose();
        world.dispose();
        disposeInternalFrames();
    }
    


    /**
     * Creates the windows as Swing InternalFrames
     */
    private void createInternalFrames() {
        worldWindow = new WorldWindow(world);
        desktop.add(worldWindow);
        worldWindow.show();
        worldWindow.setLocation(300, 20);
        agentInspector = createAgentInspector(simulator, 20, 20);
        if (!backgroundMode){
        controlWindow = new ControlWindow(world, simulator);
        desktop.add(controlWindow);
        controlWindow.show();
        controlWindow.setLocation(300, 450);
        }
    }
    /**
     * Dispose the windows- used before restart.
     */
    private void disposeInternalFrames() {
        simulator.dispose();
        worldWindow.dispose();
        if (agentInspector != null) agentInspector.dispose();
        if (controlWindow != null){
            controlWindow.dispose();
        }
    }
  
    /**
     * creates agent inspector window
     */
    private AgentInspector createAgentInspector(Simulator simulator, int x,
            int y) {
        ArrayList agents = simulator.getAgentList();
        SimpleAgent a = ((SimpleAgent) agents.get(agents.size() - 1 ));
        if (a instanceof Agent) {
            AgentInspector ai = new AgentInspector((Agent) a, !backgroundMode,simulator);
            desktop.add(ai);
            ai.show();
            ai.setLocation(x, y);
            return ai;
        } else
            return null;
    }
    boolean hasStop= false ;
    
    private void createResult(){
    	
    }
    
    public void stop(){
//    	releaseRessources();
    	if(hasStop)
    		return ;
    	hasStop = true ;
    	JTextField jtext = new JTextField();
    	
    	Rectangle rec = controlWindow.getBounds();
    	jtext.setBounds(rec.x,rec.y+rec.height + 10, rec.width,50);
    	desktop.add(jtext);
    	
		jtext.setVisible(true);
		Agent agent = (Agent) simulator.getAgentList().get(simulator.getAgentList().size() - 1 ) ;
		String s = agent.getName()+"\n    Counter:"+ agent.getCounter() +"\n Timer:"+agent.getLifeTime()
				+"\n Odometer:"+agent.getOdometer();
		System.out.println(s);
		jtext.setText(s);
    }
    
    
    public void actionPerformed(ActionEvent event) {
       if (event.getActionCommand()=="demo"){
            releaseRessources();
            start( DemoManager.getDemoFromActionEvent(event));
       }
    }
    /** 
     * Runs Simbad in background mode for computation intensive application. 
     * Minimize graphic display and renderer computation.
     */
    private void runBackgroundMode(){
        //TODO pb with collision , pb with camera in this mode.
        setTitle(this.getTitle()+" - Background Mode");
        System.out.println("---------------------------------------------");
        System.out.println("Simbad is running in 'Background Mode");
        System.out.println("World is rendered very rarely. UI is disabled");
        System.out.println("--------------------------------------------");
        // slow down
        agentInspector.setFramesPerSecond(0.5f);
        // Show a small indication window
        JInternalFrame frame = new JInternalFrame();
        JPanel p = new JPanel();
        p.add(new JLabel("BACKGROUND MODE"));
        frame.setContentPane(p); frame.setClosable(false);
        frame.pack();frame.setLocation(SIZEX/2,SIZEY*3/4);
        desktop.add(frame);
        frame.show();
        world.changeViewPoint(World.VIEW_FROM_TOP,null);
        // start
        simulator.startBackgroundMode();
    }
    
    /** The simbad main. Process command line arguments and launch simbad.*/
    public static void main(String[] args) {
        // process options
        boolean backgroundMode = false;
        for (int i = 0; i < args.length;i++)
        {
            if ("-bg".compareTo(args[i])==0) backgroundMode = true;
        }
        // Check java3d presence
        try {
            Class.forName("javax.media.j3d.VirtualUniverse");
        } catch (ClassNotFoundException e1) {
            JOptionPane.showMessageDialog(null,  "Simbad requires Java 3D", "Simbad 3D",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        //request antialising 
        System.setProperty("j3d.implicitAntialiasing", "true");

        
        final HardEnvironmentDescription myEnv1 = new HardEnvironmentDescription();
		AbsPathControl control = myEnv1.addPotentialRobot();
		final RobotTestSimBad s = new RobotTestSimBad(myEnv1,false);
		control.setCompleteInterface(new AbsPathControl.OnCompleteInterface() {
			
			@Override
			public void onCompleteListener() {
				s.stop();
			}
		});
     }


    public JDesktopPane getDesktopPane() {
        return desktop;
    }
}