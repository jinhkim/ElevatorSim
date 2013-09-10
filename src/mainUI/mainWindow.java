package mainUI;


import j3dSimView.ElevatorSimView;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.Vector;


import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import javax.swing.JPopupMenu;

import javax.swing.SwingUtilities;

import javax.swing.UIManager;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import mainUI.graphs.ElevatorDistanceGraph;
import mainUI.graphs.PassengerWaitGraph;
import mainUI.graphs.PassengerRideTime;
import mainUI.graphs.ElevatorPositionGraph;


import remoteUI.Website;
import remoteUI.Website.Method;

import elevatorSystem.BossLiftGeneralException;
import elevatorSystem.Controller;
import elevatorSystem.Elevator;
import elevatorSystem.Log;
import elevatorSystem.Passenger;
import elevatorSystem.ProbabilityOutofBoundsException;
import elevatorSystem.RandomEventGenerator;
import elevatorSystem.RandomEventGenerator.Probability;
import elevatorSystem.Safety;

public class mainWindow extends javax.swing.JFrame {
	
	// 3DCanvas simulation class
	private ElevatorSimView view;
	
	// elevator system
	private Controller controller;
	private RandomEventGenerator randomEventGen;
	private Safety safety;
	
	// whether world is created or not
	private Boolean simStarted; 

	// document used for MsgBox at bottom of ui
	private StyledDocument doc;	

	// passenger tables
	private PassengerTableModel nextTable;
	private PassengerTableModel nextFloorTable;
	
	// elevator tab array
	private ElevatorInfoPanel[] elevatorTabs;
	
    public mainWindow(Controller c, RandomEventGenerator r, Safety s) {
    	
    	// use system look and feel
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// do nothing
		}
		
    	
		view = new ElevatorSimView();
    	safety = s;
    	controller = c;
    	randomEventGen = r;
    	simStarted = false;
    	
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents(); 
        
        setGUIEnabled(false);
        
        SimViewPanel.add(view.getCanvas(), BorderLayout.CENTER);
    	this.validate();
        
        AlgorithmDescBox.setText(controller.getAlgorithmDesc(0));
        
        doc = MsgBox.getStyledDocument();
        
        Style style = MsgBox.addStyle("Faults", null); 
        StyleConstants.setForeground(style, Color.red);
        StyleConstants.setItalic(style, true);
        StyleConstants.setBold(style, true);
        
        style = MsgBox.addStyle("Passengers", null);
        StyleConstants.setForeground(style, new Color(0,204,204));
        StyleConstants.setItalic(style, true);
        
        style = MsgBox.addStyle("Normal", null);
        
        style = MsgBox.addStyle("Elevators", null);
        StyleConstants.setForeground(style, new Color(51,255,0));
        StyleConstants.setBold(style, true);
        
        style = MsgBox.addStyle("Emergs", null);
        StyleConstants.setForeground(style, Color.red);
        StyleConstants.setBold(style, true);
        
        style = MsgBox.addStyle("Maintenance", null);
        StyleConstants.setForeground(style, Color.ORANGE);
        StyleConstants.setBold(style, true);
        
      //Hijack the keyboard manager
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher( new KeyDispatcher(this) );
        
        //add window listener
        this.addWindowListener(new mainWindowListener());
    }
  
 
    private void initComponents() {

        Start_Stop_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        MsgBox = new javax.swing.JTextPane();
        SimStatusLabel = new javax.swing.JLabel();
        simStatusLabel = new javax.swing.JLabel();
        SimTimeLabel = new javax.swing.JLabel();
        simTimeLabel = new javax.swing.JLabel();
        ElevatorPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        AlgorithmDropList = new javax.swing.JComboBox(controller.getAlgorithms());
        jScrollPane2 = new javax.swing.JScrollPane();
        AlgorithmDescBox = new javax.swing.JTextPane();
        PassengerPane = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        passengerTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        FloorPassengerTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        floorDropDownList = new javax.swing.JComboBox();
        surroundSimViewPanel = new javax.swing.JPanel();
        SimViewPanel = new javax.swing.JPanel(new BorderLayout());
        mainMenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        file_newSim = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        file_quit = new javax.swing.JMenuItem();
        SimViewMenu = new javax.swing.JMenu();
        resetSimView = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        ElevatorFocusSubMenu = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        EventsMenu = new javax.swing.JMenu();
        inject_passenger = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        injectEmergMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        inject_fault = new javax.swing.JMenuItem();
        resolve_fault = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        setup_randomEvents = new javax.swing.JMenuItem();
        halt_randomEvents = new javax.swing.JMenuItem();
        RemoteServerMenu = new javax.swing.JMenu();
        startRemoteServer = new javax.swing.JMenuItem();
        maintenance = new javax.swing.JMenu();
        maintenanceMenu = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        returnToActiveMenu = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        returnAlltoActive = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        graphs_waitTime = new javax.swing.JMenuItem();
        graphs_rideTime = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        graphs_elevatorPositions = new javax.swing.JMenuItem();
        graphs_distance = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Boss Lift");

        Start_Stop_button.setText("Start/Stop");
        Start_Stop_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_Stop_buttonActionPerformed(evt);
            }
        });

        MsgBox.setEditable(false);
        jScrollPane1.setViewportView(MsgBox);

        SimStatusLabel.setText("Simulation Status:");

        simStatusLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        simStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
        simStatusLabel.setText("NOT RUNNING");

        SimTimeLabel.setText("Simulation Time:");

        simTimeLabel.setText("00:00:00");

        jLabel1.setText("Algorithm:");

        AlgorithmDropList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlgorithmDropListActionPerformed(evt);
            }
        });

        AlgorithmDescBox.setBackground(new java.awt.Color(240, 240, 240));
        jScrollPane2.setViewportView(AlgorithmDescBox);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AlgorithmDropList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(AlgorithmDropList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        ElevatorPane.addTab("Algorithms", jPanel1);

        passengerTable.setModel(new PassengerTableModel());
        passengerTable.setShowHorizontalLines(false);
        passengerTable.setShowVerticalLines(false);
        passengerTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(passengerTable);

        PassengerPane.addTab("Passengers", jScrollPane3);

        FloorPassengerTable.setModel(new PassengerTableModel());
        FloorPassengerTable.setShowHorizontalLines(false);
        FloorPassengerTable.setShowVerticalLines(false);
        FloorPassengerTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(FloorPassengerTable);

        jLabel2.setText("Floor:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(floorDropDownList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(floorDropDownList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        PassengerPane.addTab("Floors", jPanel3);

        SimViewPanel.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout surroundSimViewPanelLayout = new javax.swing.GroupLayout(surroundSimViewPanel);
        surroundSimViewPanel.setLayout(surroundSimViewPanelLayout);
        surroundSimViewPanelLayout.setHorizontalGroup(
            surroundSimViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(surroundSimViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SimViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addContainerGap())
        );
        surroundSimViewPanelLayout.setVerticalGroup(
            surroundSimViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(surroundSimViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SimViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        FileMenu.setText("File");

        file_newSim.setText("New Simulation");
        file_newSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                file_newSimActionPerformed(evt);
            }
        });
        FileMenu.add(file_newSim);
        FileMenu.add(jSeparator2);

        file_quit.setText("Quit");
        file_quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                file_quitActionPerformed(evt);
            }
        });
        FileMenu.add(file_quit);

        mainMenuBar.add(FileMenu);

        SimViewMenu.setText("Sim View");

        resetSimView.setText("Reset View to Default");
        resetSimView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetSimViewActionPerformed(evt);
            }
        });
        SimViewMenu.add(resetSimView);
        SimViewMenu.add(jSeparator3);

        ElevatorFocusSubMenu.setText("Focus on Elevator...");

        jMenuItem5.setText("(none)");
        jMenuItem5.setEnabled(false);
        ElevatorFocusSubMenu.add(jMenuItem5);

        SimViewMenu.add(ElevatorFocusSubMenu);

        mainMenuBar.add(SimViewMenu);

        EventsMenu.setText("Events");

        inject_passenger.setText("Inject Passenger");
        inject_passenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inject_passengerActionPerformed(evt);
            }
        });
        EventsMenu.add(inject_passenger);

        jMenu3.setText("Passenger Emergency");

        injectEmergMenu.setText("Inject Emergency");

        jMenuItem1.setText("(none)");
        jMenuItem1.setEnabled(false);
        injectEmergMenu.add(jMenuItem1);

        jMenu3.add(injectEmergMenu);

        EventsMenu.add(jMenu3);

        jMenu2.setText("Hardware Faults");

        inject_fault.setText("Inject Fault");
        inject_fault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inject_faultActionPerformed(evt);
            }
        });
        jMenu2.add(inject_fault);

        resolve_fault.setText("Resolve Fault");
        resolve_fault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resolve_faultActionPerformed(evt);
            }
        });
        jMenu2.add(resolve_fault);

        EventsMenu.add(jMenu2);
        EventsMenu.add(jSeparator4);

        setup_randomEvents.setText("Setup Random Events");
        setup_randomEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setup_randomEventsActionPerformed(evt);
            }
        });
        EventsMenu.add(setup_randomEvents);

        halt_randomEvents.setText("Halt Random Events");
        halt_randomEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                halt_randomEventsActionPerformed(evt);
            }
        });
        EventsMenu.add(halt_randomEvents);

        mainMenuBar.add(EventsMenu);

        RemoteServerMenu.setText("Remote Server");

        startRemoteServer.setText("Start");
        startRemoteServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startRemoteServerActionPerformed(evt);
            }
        });
        RemoteServerMenu.add(startRemoteServer);

        mainMenuBar.add(RemoteServerMenu);

        maintenance.setText("Maintenance");

        maintenanceMenu.setText("Set Elevator to Maintenance Mode");

        jMenuItem14.setText("(none)");
        jMenuItem14.setEnabled(false);
        maintenanceMenu.add(jMenuItem14);

        maintenance.add(maintenanceMenu);

        returnToActiveMenu.setText("Return Elevator to Active Mode");

        jMenuItem15.setText("(none)");
        jMenuItem15.setEnabled(false);
        returnToActiveMenu.add(jMenuItem15);

        maintenance.add(returnToActiveMenu);
        maintenance.add(jSeparator5);

        returnAlltoActive.setText("Return All to Active Mode");
        returnAlltoActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnAlltoActiveActionPerformed(evt);
            }
        });
        maintenance.add(returnAlltoActive);

        mainMenuBar.add(maintenance);

        jMenu1.setText("Graphs");

        graphs_waitTime.setText("View Avg. Passenger Wait Time");
        graphs_waitTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphs_waitTimeActionPerformed(evt);
            }
        });
        jMenu1.add(graphs_waitTime);

        graphs_rideTime.setText("View Avg. Passenger Riding Time");
        graphs_rideTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphs_rideTimeActionPerformed(evt);
            }
        });
        jMenu1.add(graphs_rideTime);
        jMenu1.add(jSeparator6);

        graphs_elevatorPositions.setText("View Elevator Positions ");
        graphs_elevatorPositions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphs_elevatorPositionsActionPerformed(evt);
            }
        });
        jMenu1.add(graphs_elevatorPositions);

        graphs_distance.setText("View Elevator Distance Traveled");
        graphs_distance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphs_distanceActionPerformed(evt);
            }
        });
        jMenu1.add(graphs_distance);

        mainMenuBar.add(jMenu1);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(surroundSimViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ElevatorPane, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(PassengerPane, 0, 278, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simStatusLabel)
                            .addComponent(SimStatusLabel)
                            .addComponent(Start_Stop_button, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SimTimeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(simTimeLabel)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ElevatorPane, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PassengerPane, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                    .addComponent(surroundSimViewPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SimTimeLabel)
                            .addComponent(simTimeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SimStatusLabel)
                        .addGap(3, 3, 3)
                        .addComponent(simStatusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Start_Stop_button, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }    
    
	protected void graphs_elevatorPositionsActionPerformed(ActionEvent evt) {
		ElevatorPositionGraph e = new ElevatorPositionGraph("Elevator Position", controller, true);
	}

	protected void graphs_waitTimeActionPerformed(ActionEvent evt) {
		PassengerWaitGraph e = new PassengerWaitGraph("Average Passenger Wait Time", true);
	}

	protected void graphs_rideTimeActionPerformed(ActionEvent evt) {
		PassengerRideTime e = new PassengerRideTime("Average Passenger Ride Time", true);	
	}

	protected void graphs_distanceActionPerformed(ActionEvent evt) {
		ElevatorDistanceGraph e = new ElevatorDistanceGraph("GOTOSKOO" ,controller, true);
	}

	protected void AlgorithmDropListActionPerformed(ActionEvent evt) {
		if (simStarted)
			controller.setAlgorithm(AlgorithmDropList.getSelectedIndex());
		AlgorithmDescBox.setText(controller.getAlgorithmDesc(AlgorithmDropList.getSelectedIndex()));
	}

	protected void startRemoteServerActionPerformed(ActionEvent evt) {
		PassengerWaitGraph a = new PassengerWaitGraph("PassengerWaitGraph", false);
		ElevatorDistanceGraph b = new ElevatorDistanceGraph("ElevatorDistanceGraph", controller, false);
		ElevatorPositionGraph c = new ElevatorPositionGraph("ElevatorPositionGraph", controller, false);
		PassengerRideTime d = new PassengerRideTime("PassengerRideTime", false);
		a.setVisible(false);
		b.setVisible(false);
		c.setVisible(false);
		d.setVisible(false);
		
		Website A = new Website(controller, safety, Method.Start, 8070, controller.getElevNum(), controller.getMaxFloor() + 1, a, b, c, d);
		A.start();
	}
	
	/*
	 * SIMULATION VIEW CONTROLS
	 * 
	 */

	/*
	 * focusViewOnElevator(int id)
	 * 
	 * Informs the ElevatorSimView class which elevator to focus the view on.
	 */
	public void focusViewOnElevator(int id) {
		try {
			view.focusViewOnElevator(id);
		} catch (BossLiftGeneralException e) {
			displayError(e.getMessage());
		}
	}

	/*
	 * resetSimViewActionPerformed(ActionEvent evt)
	 * 
	 * Called when "Reset View" menu item selected.  Resets the 3D view to default.
	 */
	protected void resetSimViewActionPerformed(ActionEvent evt) {
		view.resetView();		
	}
	
	/*
	 * The following rotate and zoom the 3D when the user combines CTRL with the arrow keys.
	 * Called by KeyDispatcher class.
	 */
	public void rotateSimViewRight() {
		view.rotateViewRight();
	}
	
	public void rotateSimViewLeft() {
		view.rotateViewLeft();
	}
	
	public void zoomInSimView() {
		view.zoomIn();
	}

	public void zoomOutSimView() {
		view.zoomOut();
	}



	private void setGUIEnabled(Boolean simOnline) {
    	Start_Stop_button.setEnabled(simOnline);
        halt_randomEvents.setEnabled(simOnline);
        inject_fault.setEnabled(simOnline);
        resolve_fault.setEnabled(simOnline);
        inject_passenger.setEnabled(simOnline);
        setup_randomEvents.setEnabled(simOnline); 
        resetSimView.setEnabled(simOnline);
        returnAlltoActive.setEnabled(simOnline);
        startRemoteServer.setEnabled(simOnline);
        
        graphs_distance.setEnabled(simOnline);
        graphs_rideTime.setEnabled(simOnline);
        graphs_elevatorPositions.setEnabled(simOnline);
        graphs_waitTime.setEnabled(simOnline);
    }
     

    protected void file_newSimActionPerformed(ActionEvent evt) {
    	 NewSimulationDialog dialog = new NewSimulationDialog(this, true);
         dialog.setVisible(true);
	}


	protected void file_quitActionPerformed(ActionEvent evt) {
		view.cleanup();
		Log.printStatusReport();
		System.exit(0);
	}


	/*
	 * EVENT INJECTION
	 * 
	 */
	// RANDOM EVENTS
	protected void setup_randomEventsActionPerformed(ActionEvent evt) {
		SetupRandomEventsDialog dialog = new SetupRandomEventsDialog(this, true);
		dialog.setSliders(randomEventGen.getPassengerProb(), randomEventGen.getEmergencyProb(), randomEventGen.getFaultProb());
        dialog.setVisible(true);
	}
	
	public void setupRandomEvents(float pass_prob, float emerg_prob,float hardware_prob) {
		try {			
				randomEventGen.setProbability(Probability.PASSENGER, pass_prob);
				randomEventGen.setProbability(Probability.FAULT, hardware_prob);
				randomEventGen.setProbability(Probability.EMERGENCY, emerg_prob);
			if (!randomEventGen.isRunning())
				randomEventGen.start();
			if (controller.isRunning()) {
				randomEventGen.runEvents();
			}
			
		} catch (ProbabilityOutofBoundsException e) {
			displayError(e.getMessage());
		}
	}

	protected void halt_randomEventsActionPerformed(ActionEvent evt) {
		if (randomEventGen.isRunning()) {
			try {
				randomEventGen.setProbability(Probability.PASSENGER, 0f);
				randomEventGen.setProbability(Probability.FAULT, 0f);
				randomEventGen.setProbability(Probability.EMERGENCY, 0f);
			} catch (ProbabilityOutofBoundsException e) {
				displayError(e.getMessage());
			}
		}
	}

	// PASSENGERS
	private void inject_passengerActionPerformed(ActionEvent evt) {
    	 InjectPassengerDialog dialog = new InjectPassengerDialog(this, true);
         dialog.setVisible(true);
	}
    
    public void createPassenger(int arrivalFloor, int destFloor, Boolean vip) {
    	try {
    		Passenger p = new Passenger(arrivalFloor, destFloor, 100, vip);
    		controller.newPassengerRequest(p);
		} catch (BossLiftGeneralException e) {
			displayError(e.getMessage());
		}
	}
    
    // HARDWARE FAULTS
    protected void inject_faultActionPerformed(ActionEvent evt) {
    	FaultFloorDialog dialog = new FaultFloorDialog(this, true,false);
        dialog.setVisible(true);
	}
    
    protected void resolve_faultActionPerformed(ActionEvent evt) {
    	FaultFloorDialog dialog = new FaultFloorDialog(this, true,true);
        dialog.setVisible(true);
	}
    
    public void floorFault(int f) {
    	safety.injectFloorFault(f);
    }
    
    public void resolveFault(int f) {
    	safety.resolveFloorFault(f);
    }
    
    // MAINTENANCE
    public void setToMaintenanceMode(int eleID) {
		safety.autoMaintenanceRequest(eleID);
	}

	public void returnElevatortoActiveMode(int eleID) {
		safety.finishElevatorMaintenance(eleID);
	}
	
	protected void returnAlltoActiveActionPerformed(ActionEvent evt) {
		for (int i=0; i<controller.getElevNum(); i++) {
			safety.finishElevatorMaintenance(i);
		}
	}
    
   // ELEVATOR EMERGENCIES 
    public void elevatorEmerg(int id) {
    	try {
			safety.passengerEmergencyRequest(id);
		} catch (BossLiftGeneralException e) {
			displayError(e.getCause()+"\n"+e.getMessage());
		}
    }
    
    public void resolveEmerg(int id) {
    	safety.finishElevatorEmergency(id);
    }

	private void Start_Stop_buttonActionPerformed(java.awt.event.ActionEvent evt) {      
		if (controller.isRunning()) {
			controller.pauseSim();
			randomEventGen.pauseEvents();
			simStatusLabel.setText("PAUSED");
			simStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
		} else {
			controller.runSim();
			randomEventGen.runEvents();
			simStatusLabel.setText("RUNNING");
			simStatusLabel.setForeground(new Color(0,128,0));
		}
    }                                                 
    
    public void createWorld(int numElevators, int numFloors, Boolean penthouse, Vector<Integer>[] bounds) {
    	if (simStarted)
    		destroyWorld();
    	
    	try {
    		controller.setFloors(numFloors);
			controller.createElevators(numElevators);	
			
			if (penthouse)
			controller.addPenthouse();
			for (int e=0; e<bounds.length; e++) {
				for (int f=0; f<bounds[e].size(); f++)
					controller.lockFloor(e, bounds[e].get(f));
			}
			
	    	view.createWorld(numElevators, numFloors, penthouse, bounds);
	    	
	    	controller.setSimView(this,view);
			controller.start();
			safety.start();
			randomEventGen.start();		
	    	
	    	postGeneralEventMsg("Welcome to BOSS LIFT Elevator Simulator!");
	    	postGeneralEventMsg("The simulation is PAUSED");
			simStatusLabel.setText("PAUSED");
			simStatusLabel.setForeground(new java.awt.Color(255, 0, 0));
			
			elevatorTabs = new ElevatorInfoPanel[numElevators];
			for(int i=0; i< numElevators; i++) {
				elevatorTabs[i] = new ElevatorInfoPanel();
				if (bounds[i].size() > 0)
					elevatorTabs[i].setElevatorBounds(controller.getElevatorBounds(i));
				JScrollPane p = new JScrollPane();
				p.setViewportView(elevatorTabs[i]);
				ElevatorPane.addTab("E"+i, p);
				
			}
			
			setGUIEnabled(true);
			populateElevatorMenus(numElevators);
			
			for (int i=0; i<numFloors; i++) {
				floorDropDownList.addItem(i);
			}
	    	
			simStarted = true;
			
			controller.setAlgorithm(AlgorithmDropList.getSelectedIndex());
		} catch (BossLiftGeneralException e1) {
			displayError(e1.getMessage());
		}		
    }
    
    private void populateElevatorMenus(int numElevators) {
		returnToActiveMenu.removeAll();
    	
		for (int menus = 0; menus < numElevators; menus++) {
    		JMenuItem j = new JMenuItem();
    		
    		j.setText("Elevator "+menus);
    		j.addActionListener(new MaintenanceMenuActionListener(this,menus,true));
    		returnToActiveMenu.add(j);
    	}
    	
    	maintenanceMenu.removeAll();
    	
    	for (int menus = 0; menus < numElevators; menus++) {
    		JMenuItem j = new JMenuItem();
    		
    		j.setText("Elevator "+menus);
    		j.addActionListener(new MaintenanceMenuActionListener(this,menus,false));
    		maintenanceMenu.add(j);
    	}
    	
    	
        ElevatorFocusSubMenu.removeAll();    	
        	
       	for (int menus = 0; menus < numElevators; menus++) {
       		JMenuItem j = new JMenuItem();
        		
       		j.setText("Elevator "+menus);
       		j.addActionListener(new ElevatorFocusActionListener(this,menus));
       		ElevatorFocusSubMenu.add(j);
       	}
       	
       	injectEmergMenu.removeAll();    	
    	
       	for (int menus = 0; menus < numElevators; menus++) {
       		JMenuItem j = new JMenuItem();
        		
       		j.setText("Elevator "+menus);
       		j.addActionListener(new ElevatorEmergActionListener(this,menus,false));
       		injectEmergMenu.add(j);
       	}
	}
    
    private void destroyWorld() {
    	if (simStarted) {
    		simStarted = false;
    		
    		// reset safety thread
	    	safety.interrupt();
	    	
    		// destroy elevator system
	    	controller.interrupt();
	    	controller.destroy();
	    	
	    	
	    	// stop random events
			randomEventGen.interrupt();
	    	
			// reset logs
	    	Log.cleanLogs();
	    	
	    	// reset MsgBox
			deleteAllMsgs();
			
	    	// reset tabs, passenger table
			for (int i=ElevatorPane.getTabCount()-1; i>0; i--)
  				ElevatorPane.remove(i);
			elevatorTabs = null;
			floorDropDownList.removeAllItems();
			SwingUtilities.invokeLater(new Runnable() {
		          public void run() {		        	
		        	passengerTable.setModel(new PassengerTableModel());
		  			FloorPassengerTable.setModel(new PassengerTableModel());
		          }
		    });
			
			// clean up simulation view
			view.cleanup();
			view.restart();			
			
	    	// reset timer
			simTimeLabel.setText("00:00:00");
			
			
			// recreate objects
			controller = new Controller();			
			safety = new Safety(controller);			
			try {
				randomEventGen = new RandomEventGenerator(controller, safety, 0.0f, 0.0f, 0.0f);
			} catch (ProbabilityOutofBoundsException e) {
				displayError(e.getMessage());
			}
    	}
    }
    
    
	
	/*
	 * PASSENGER TABLE CONTROL
	 * --MAIN PASSENGER TAB
	 * 
	 * newPassengerInSystem(Passenger p)
	 * 
	 * Adds the passenger to the side table using a name form passengerNames in combination with a number to create
	 * a unique name in the system.  Also populates the table with information on the passenger. Called when
	 * passenger is injected into the elevator system, either manually or by the random event generator.
	 * 
	 */
	public void updateTotalPassengersInSystem(Vector<Passenger> p) {
		if (!simStarted)
			return;
		
		nextTable = new PassengerTableModel(p.size());
		
		for (int rows=0; rows<p.size(); rows++){
			nextTable.setValueAt(p.get(rows).getName(), rows, 0);
			nextTable.setValueAt(p.get(rows).getCurrentFloor(), rows, 1);
			nextTable.setValueAt(p.get(rows).getDestFloor(), rows, 2);
			nextTable.setValueAt(p.get(rows).getStatus(), rows, 3);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	        	  passengerTable.setModel(nextTable);
	          }
	    });		
	}
	
	/*
	 * PASSENGER TABLE CONTROL
	 * --FLOORS TAB
	 * 
	 * floorDropDownListActionPerformed(ActionEvent evt)
	 * 
	 * Called when user makes a selection in the floor drop down list on the 
	 * "Floors" table.  Iterates through the main passenger table and lists
	 * the ones waiting on the floor chosen in the drop down list.
	 * 
	 */
	public void updatePassengersOnFloor(Vector<Passenger>[] p) {
		if (!simStarted)
			return;
		
		int floor = floorDropDownList.getSelectedIndex();
		nextFloorTable = new PassengerTableModel(p[floor].size());
		
		for (int rows=0; rows<p[floor].size(); rows++){
			nextFloorTable.setValueAt(p[floor].get(rows).getName(), rows, 0);
			nextFloorTable.setValueAt(p[floor].get(rows).getCurrentFloor(), rows, 1);
			nextFloorTable.setValueAt(p[floor].get(rows).getDestFloor(), rows, 2);
			nextFloorTable.setValueAt(p[floor].get(rows).getStatus(), rows, 3);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	        	  FloorPassengerTable.setModel(nextFloorTable);
	          }
	    });		
	}
	
	/*
	 * ELEVATOR TABS
	 */

	public void updateElevatorTabs(Elevator[] els) {
		for (int e=0; e< els.length; e++) {
			ElevatorInfoPanel p = elevatorTabs[e];
			p.setStatus(els[e].getStatus());
			p.setCurrentFloor(els[e].getCurrentFloor());
			p.setTotalFloors(els[e].getTotalFloors());
			p.setCurrentPassengers(els[e].getCurrentPassengerCount());
			p.setTotalPassengers(els[e].getTotalPassengerCount());
			p.setQueue(els[e].getQueue());
			p.setWeight(els[e].getCurrentWeight());
		}
	}
	
	/*
	 * METHODS FOR POSTING MESSAGES TO THE TEXT FIELD AT THE BOTTOM OF THE UI
	 * 
	 * 
	 */    
    public void postPassengerEventMsg(String msg) {
    	try {
			doc.insertString(doc.getLength(), msg+"\n", MsgBox.getStyle("Passengers"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
    }
    
    public void postFaultEventMsg(String msg) {
    	try {
			doc.insertString(doc.getLength(),msg+"\n", MsgBox.getStyle("Faults"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
    }
    
    public void postGeneralEventMsg(String msg) {
    	try {
			doc.insertString(doc.getLength(),msg+"\n", MsgBox.getStyle("Normal"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
	}

	public void postElevatorEventMsg(String msg) {
		try {
			doc.insertString(doc.getLength(),msg+"\n", MsgBox.getStyle("Elevators"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
	}

	public void postEmergEventMsg(String msg) {
		try {
			doc.insertString(doc.getLength(),msg+"\n", MsgBox.getStyle("Emergs"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
	}
	
	public void postMaintenanceEventMsg(String msg) {
		try {
			doc.insertString(doc.getLength(),msg+"\n", MsgBox.getStyle("Maintenance"));
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
		
		MsgBox.setCaretPosition(doc.getLength());
	}
	
	public void deleteAllMsgs() {
		try {
			doc.remove(0,doc.getLength());
			MsgBox.setCaretPosition(doc.getLength());
		} catch (BadLocationException e) {
			displayError(e.getMessage());
		}
	}
	
	/*
	 * setSimTime(long simRunTime)
	 * 
	 * Sets the label in the UI to display to correct simulation running time
	 * in minutes, seconds and milliseconds.
	 */
	public void setSimTime(long simRunTime) {		
		long mins = simRunTime/60000;
		simRunTime -= mins*60000;
		
		long secs = simRunTime/1000;
		simRunTime -= secs*1000;
		
		String s;
		if (secs < 10)
			s = "0"+secs;
		else
			s = String.valueOf(secs);
		String m;
		if (simRunTime < 100)
			m = "0"+simRunTime;
		else
			m = String.valueOf(simRunTime);
		
		simTimeLabel.setText(mins+":"+s+":"+m);		
	}
	
	/*
	 * displayError(String msg)
	 * 
	 * Displays an error dialog box printing the message msg.
	 */    
    public void displayError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
    
    // UI VARIABLES
    private javax.swing.JTextPane AlgorithmDescBox;
    private javax.swing.JComboBox AlgorithmDropList;
    private javax.swing.JMenu ElevatorFocusSubMenu;
    private javax.swing.JTabbedPane ElevatorPane;
    private javax.swing.JMenu EventsMenu;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JTable FloorPassengerTable;
    private javax.swing.JTextPane MsgBox;
    private javax.swing.JTabbedPane PassengerPane;
    private javax.swing.JMenu RemoteServerMenu;
    private javax.swing.JLabel SimStatusLabel;
    private javax.swing.JLabel SimTimeLabel;
    private javax.swing.JMenu SimViewMenu;
    private javax.swing.JPanel SimViewPanel;
    private javax.swing.JButton Start_Stop_button;
    private javax.swing.JMenuItem file_newSim;
    private javax.swing.JMenuItem file_quit;
    private javax.swing.JComboBox floorDropDownList;
    private javax.swing.JMenuItem graphs_distance;
    private javax.swing.JMenuItem graphs_elevatorPositions;
    private javax.swing.JMenuItem graphs_rideTime;
    private javax.swing.JMenuItem graphs_waitTime;
    private javax.swing.JMenuItem halt_randomEvents;
    private javax.swing.JMenu injectEmergMenu;
    private javax.swing.JMenuItem inject_fault;
    private javax.swing.JMenuItem inject_passenger;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenu maintenance;
    private javax.swing.JMenu maintenanceMenu;
    private javax.swing.JTable passengerTable;
    private javax.swing.JMenuItem resetSimView;
    private javax.swing.JMenuItem resolve_fault;
    private javax.swing.JMenuItem returnAlltoActive;
    private javax.swing.JMenu returnToActiveMenu;
    private javax.swing.JMenuItem setup_randomEvents;
    private javax.swing.JLabel simStatusLabel;
    private javax.swing.JLabel simTimeLabel;
    private javax.swing.JMenuItem startRemoteServer;
    private javax.swing.JPanel surroundSimViewPanel;
	
        
}