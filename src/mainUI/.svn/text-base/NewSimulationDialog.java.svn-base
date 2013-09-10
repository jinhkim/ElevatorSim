package mainUI;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

public class NewSimulationDialog extends javax.swing.JDialog {

	private mainWindow parent;

	private Boolean pentInSystem = false;

	private Vector<Integer>[] eleBounds;
	private int maxFloor = -1;
	private int eles = -1;

	public NewSimulationDialog(mainWindow p, boolean modal) {
		super(p, modal);
		parent = p;

		initComponents();
		initDocListeners();
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		numEle = new javax.swing.JTextField();
		numFloors = new javax.swing.JTextField();
		OKButton = new javax.swing.JButton();
		CancelButton = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		elevatorList = new javax.swing.JList(new DefaultListModel());
		jLabel3 = new javax.swing.JLabel();
		penthouseCheck = new javax.swing.JCheckBox();
		advancedSimCheck = new javax.swing.JCheckBox();
		bounds = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setText("Number of Elevators (1-10):");

		jLabel2.setText("Number of Floors (2-100):");

		OKButton.setText("OK");
		OKButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OKButtonActionPerformed(evt);
			}
		});

		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CancelButtonActionPerformed(evt);
			}
		});

		elevatorList.setEnabled(false);
		elevatorList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				elevatorListValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(elevatorList);

		jLabel3.setText("Floor Bounds: (separate with commas i.e. 0-10,15-20)");

		penthouseCheck.setText("Penthouse In System");
		penthouseCheck.setEnabled(false);
		penthouseCheck.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				penthouseCheckActionPerformed(evt);
			}
		});

		advancedSimCheck.setText("Advanced Simulation");
		advancedSimCheck.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				advancedSimCheckActionPerformed(evt);
			}
		});

		bounds.setEnabled(false);

		jLabel4.setText("Leave empty for no restrictions");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel1)
								.addComponent(jLabel2)
								.addComponent(advancedSimCheck)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addComponent(numFloors)
														.addComponent(numEle, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
														.addGap(3, 3, 3))
														.addGroup(layout.createSequentialGroup()
																.addGap(4, 4, 4)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(bounds, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
																		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(layout.createSequentialGroup()
																						.addComponent(OKButton)
																						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(CancelButton)))
																						.addComponent(jLabel4)))
																						.addComponent(penthouseCheck))
																						.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(numEle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(numFloors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(advancedSimCheck)
												.addComponent(penthouseCheck))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(layout.createSequentialGroup()
																.addGap(78, 78, 78)
																.addComponent(jLabel3)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(bounds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(jLabel4))
																.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
																.addContainerGap(29, Short.MAX_VALUE))
																.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																		.addContainerGap(278, Short.MAX_VALUE)
																		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(OKButton)
																				.addComponent(CancelButton))
																				.addContainerGap())
		);

		pack();
	}

	private void initDocListeners() {
		bounds.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {}

			@Override
			public void insertUpdate(DocumentEvent arg0) {boundsChanged();}

			@Override
			public void removeUpdate(DocumentEvent arg0) {boundsChanged();}

		});

		numEle.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {}

			@Override
			public void insertUpdate(DocumentEvent arg0) {numEleChanged();}

			@Override
			public void removeUpdate(DocumentEvent arg0) {numEleChanged();}

		});

		numFloors.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {}

			@Override
			public void insertUpdate(DocumentEvent arg0) {numFloorsChanged();}

			@Override
			public void removeUpdate(DocumentEvent arg0) {numFloorsChanged();}

		});
	}

	protected void numEleChanged() {
		try {
			eles = Integer.parseInt(numEle.getText().trim());
			DefaultListModel model = (DefaultListModel) elevatorList.getModel();
			model.clear();

			eleBounds = new Vector[eles];
			for (int i=0; i < eles; i++) {
				model.add(i, "Elevator "+i);
				eleBounds[i] = new Vector<Integer>();
			}

			if (advancedSimCheck.isSelected())
				elevatorList.setSelectedIndex(0);			

		} catch (NumberFormatException e) {
			DefaultListModel model = (DefaultListModel) elevatorList.getModel();
			model.clear();
			eles = -1;
		}
	}

	protected void numFloorsChanged() {
		try {
			maxFloor = Integer.parseInt(numFloors.getText().trim());
		} catch (NumberFormatException e) {
			maxFloor = -1;
		}
	}

	protected void boundsChanged() {
		int id = elevatorList.getSelectedIndex();
		eleBounds[id].clear();
		String input = bounds.getText().trim();
		String[] b = input.split(",");

		try {
			for (int i=0; i<b.length; i++) {
				String[] f = b[i].trim().split("-");
				if (f.length == 1) {
					if (Integer.parseInt(f[0]) >= maxFloor) {
						parent.displayError("Bound value is larger than the maximum floor");
						return;
					}
					eleBounds[id].add(Integer.parseInt(f[0]));
				} else if (f.length == 2) {
					if (Integer.parseInt(f[1]) >= maxFloor) {
						parent.displayError("Bound value is larger than the maximum floor");
						return;
					}
					if (Integer.parseInt(f[0]) == Integer.parseInt(f[1]))
						eleBounds[id].add(Integer.parseInt(f[0]));
					if (Integer.parseInt(f[0]) < Integer.parseInt(f[1])) {
						for (int j=Integer.parseInt(f[0]); j<=Integer.parseInt(f[1]); j++)
							eleBounds[id].add(j);
					}				
				}
			}
		} catch (NumberFormatException e) {
			// do nothing
		}
	}

	protected void advancedSimCheckActionPerformed(ActionEvent evt) {
		if (advancedSimCheck.isSelected()) {
			if (maxFloor < 0 || eles < 0) {
				advancedSimCheck.setSelected(false);
				parent.displayError("Set up elevator and floor info before advanced simulation input.");
				return;
			}

			numFloors.setEnabled(false);
			numEle.setEnabled(false);

			bounds.setEnabled(true);
			elevatorList.setEnabled(true);
			penthouseCheck.setEnabled(true);

			elevatorList.setSelectedIndex(0);
		} else {
			numFloors.setEnabled(true);
			numEle.setEnabled(true);

			bounds.setEnabled(false);
			elevatorList.setEnabled(false);
			penthouseCheck.setEnabled(false);
		}
	}

	protected void penthouseCheckActionPerformed(ActionEvent evt) {
		if (penthouseCheck.isSelected()) 
			pentInSystem = true;
		else
			pentInSystem = false;					
	}

	protected void elevatorListValueChanged(ListSelectionEvent evt) {
		int id = elevatorList.getSelectedIndex();
		if (id < 0)
			return;

		if (eleBounds[id].size() == 0)
			bounds.setText("");
		else {
			Integer last = eleBounds[id].get(0);
			String t = last.toString();
			Boolean range = false;

			for (int i=1; i < eleBounds[id].size(); i++) {
				if (eleBounds[id].get(i) == last+1) {
					range = true;
				} else {
					if (range)
						t = t+"-"+last;
					else
						t = t+","+eleBounds[id].get(i);
				}
				last = eleBounds[id].get(i);					
			}
			if (range)
				t = t+"-"+last;

			bounds.setText(t);
		}
	}

	private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
		try {
			parent.createWorld(Integer.parseInt(numEle.getText().trim()),Integer.parseInt(numFloors.getText().trim()),pentInSystem, eleBounds);
			dispose();
		} catch (NumberFormatException e) {
			parent.displayError("Please put the number of floors and elevators you want to be in the simulation in the boxes");
			this.setVisible(true);
		}        
	}

	private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private javax.swing.JButton CancelButton;
	private javax.swing.JButton OKButton;
	private javax.swing.JCheckBox advancedSimCheck;
	private javax.swing.JTextField bounds;
	private javax.swing.JList elevatorList;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField numEle;
	private javax.swing.JTextField numFloors;
	private javax.swing.JCheckBox penthouseCheck;

}
