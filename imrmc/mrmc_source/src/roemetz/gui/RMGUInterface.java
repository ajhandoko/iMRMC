/*
 * RMGUInterface.java
 * 
 * v1.0b
 * 
 * @Author Rohan Pathare
 * 
 * This software and documentation (the "Software") were developed at the Food and Drug Administration (FDA) 
 * by employees of the Federal Government in the course of their official duties. Pursuant to Title 17, Section 
 * 105 of the United States Code, this work is not subject to copyright protection and is in the public domain. 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of the Software, to deal in the 
 * Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, or sell copies of the Software or derivatives, and to permit persons to whom the 
 * Software is furnished to do so. FDA assumes no responsibility whatsoever for use by other parties of the 
 * Software, its source code, documentation or compiled executables, and makes no guarantees, expressed or 
 * implied, about its quality, reliability, or any other characteristic.   Further, use of this code in no way 
 * implies endorsement by the FDA or confers any advantage in regulatory decisions.  Although this software 
 * can be redistributed and/or modified freely, we ask that any derivative works bear some notice that they 
 * are derived from it, and any modified versions bear some notice that they have been modified.
 * 	
 * 
 * 		This class describes the interface for iRoeMetz application. It contains a panel for
 * 		inputting means, components of variance, and experiment size. The next panel performs
 * 		multiple simulation experiments based on the input. The last panel estimates the components
 * 		of variance for the given input.
 */

package roemetz.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.random.MersenneTwisterRNG;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import mrmc.core.dbRecord;
import mrmc.core.matrix;

import roemetz.core.CofVGenRoeMetz;
import roemetz.core.RoeMetz;
import roemetz.core.SimRoeMetz;

public class RMGUInterface {

	private JTextField vR00;
	private JTextField vC00;
	private JTextField vRC00;
	private JTextField vR10;
	private JTextField vC10;
	private JTextField vRC10;
	private JTextField vR01;
	private JTextField vC01;
	private JTextField vRC01;
	private JTextField vR11;
	private JTextField vC11;
	private JTextField vRC11;
	private JTextField vR0;
	private JTextField vC0;
	private JTextField vRC0;
	private JTextField vR1;
	private JTextField vC1;
	private JTextField vRC1;
	private JTextField mu0;
	private JTextField mu1;
	private JTextField n0;
	private JTextField n1;
	private JTextField nr;
	private JTextField numExp;
	private JTextField seed;
	private JDialog progDialog;
	private JCheckBox useBias = new JCheckBox("Use Bias");
	private int useBiasM = 0;
	private String simSaveDirectory;
	private static JProgressBar simProgress;
	private static RoeMetz appl;

	public RMGUInterface(RoeMetz lsttemp, Container cp) {
		appl = lsttemp;
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		/*
		 * Panel to handle CofV inputs
		 */
		JPanel cofvInputPanel = new JPanel();
		cofvInputPanel
				.setLayout(new BoxLayout(cofvInputPanel, BoxLayout.Y_AXIS));

		/*
		 * Panel within cofvInputPanel with description of input, type
		 */
		JPanel inputLabels = new JPanel(new FlowLayout());

		JLabel inputDesc = new JLabel(
				"Input Means, Variances, and Experiment Size: ");
		inputLabels.add(inputDesc);

		/*
		 * Panel within cofvInputPanel with fields to input variances (row 1)
		 */
		JPanel varianceFields1 = new JPanel();
		varianceFields1.setLayout(new FlowLayout());

		vR00 = new JTextField("0.166", 4);
		vR00.setMaximumSize(vR00.getPreferredSize());
		vC00 = new JTextField("0.166", 4);
		vC00.setMaximumSize(vC00.getPreferredSize());
		vRC00 = new JTextField("0.166", 4);
		vRC00.setMaximumSize(vRC00.getPreferredSize());
		vR10 = new JTextField("0.166", 4);
		vR10.setMaximumSize(vR10.getPreferredSize());
		vC10 = new JTextField("0.166", 4);
		vC10.setMaximumSize(vC10.getPreferredSize());
		vRC10 = new JTextField("0.166", 4);
		vRC10.setMaximumSize(vRC10.getPreferredSize());

		JLabel vR00Label = new JLabel("vR00: ");
		JLabel vC00Label = new JLabel("vC00: ");
		JLabel vRC00Label = new JLabel("vRC00: ");
		JLabel vR10Label = new JLabel("vR10: ");
		JLabel vC10Label = new JLabel("vC10: ");
		JLabel vRC10Label = new JLabel("vRC10: ");

		varianceFields1.add(vR00Label);
		varianceFields1.add(vR00);
		varianceFields1.add(vC00Label);
		varianceFields1.add(vC00);
		varianceFields1.add(vRC00Label);
		varianceFields1.add(vRC00);
		varianceFields1.add(vR10Label);
		varianceFields1.add(vR10);
		varianceFields1.add(vC10Label);
		varianceFields1.add(vC10);
		varianceFields1.add(vRC10Label);
		varianceFields1.add(vRC10);

		/*
		 * Panel within cofvInputPanel with fields to input variances (row 2)
		 */
		JPanel varianceFields2 = new JPanel();
		varianceFields2.setLayout(new FlowLayout());

		vR01 = new JTextField("0.166", 4);
		vR01.setMaximumSize(vR01.getPreferredSize());
		vC01 = new JTextField("0.166", 4);
		vC01.setMaximumSize(vC01.getPreferredSize());
		vRC01 = new JTextField("0.166", 4);
		vRC01.setMaximumSize(vRC01.getPreferredSize());
		vR11 = new JTextField("0.166", 4);
		vR11.setMaximumSize(vR11.getPreferredSize());
		vC11 = new JTextField("0.166", 4);
		vC11.setMaximumSize(vC11.getPreferredSize());
		vRC11 = new JTextField("0.166", 4);
		vRC11.setMaximumSize(vRC11.getPreferredSize());

		JLabel vR01Label = new JLabel("vR01: ");
		JLabel vC01Label = new JLabel("vC01: ");
		JLabel vRC01Label = new JLabel("vRC01: ");
		JLabel vR11Label = new JLabel("vR11: ");
		JLabel vC11Label = new JLabel("vC11: ");
		JLabel vRC11Label = new JLabel("vRC11: ");

		varianceFields2.add(vR01Label);
		varianceFields2.add(vR01);
		varianceFields2.add(vC01Label);
		varianceFields2.add(vC01);
		varianceFields2.add(vRC01Label);
		varianceFields2.add(vRC01);
		varianceFields2.add(vR11Label);
		varianceFields2.add(vR11);
		varianceFields2.add(vC11Label);
		varianceFields2.add(vC11);
		varianceFields2.add(vRC11Label);
		varianceFields2.add(vRC11);

		/*
		 * Panel within cofvInputPanel with fields to input variances (row 3)
		 */
		JPanel varianceFields3 = new JPanel();
		varianceFields3.setLayout(new FlowLayout());

		vR0 = new JTextField("0.166", 4);
		vR0.setMaximumSize(vR0.getPreferredSize());
		vC0 = new JTextField("0.166", 4);
		vC0.setMaximumSize(vC0.getPreferredSize());
		vRC0 = new JTextField("0.166", 4);
		vRC0.setMaximumSize(vRC0.getPreferredSize());
		vR1 = new JTextField("0.166", 4);
		vR1.setMaximumSize(vR1.getPreferredSize());
		vC1 = new JTextField("0.166", 4);
		vC1.setMaximumSize(vC1.getPreferredSize());
		vRC1 = new JTextField("0.166", 4);
		vRC1.setMaximumSize(vRC1.getPreferredSize());

		JLabel vR0Label = new JLabel("vR0: ");
		JLabel vC0Label = new JLabel("vC0: ");
		JLabel vRC0Label = new JLabel("vRC0: ");
		JLabel vR1Label = new JLabel("vR1: ");
		JLabel vC1Label = new JLabel("vC1: ");
		JLabel vRC1Label = new JLabel("vRC1: ");

		varianceFields3.add(vR0Label);
		varianceFields3.add(vR0);
		varianceFields3.add(vC0Label);
		varianceFields3.add(vC0);
		varianceFields3.add(vRC0Label);
		varianceFields3.add(vRC0);
		varianceFields3.add(vR1Label);
		varianceFields3.add(vR1);
		varianceFields3.add(vC1Label);
		varianceFields3.add(vC1);
		varianceFields3.add(vRC1Label);
		varianceFields3.add(vRC1);

		/*
		 * Panel to input means
		 */
		JPanel meansFields = new JPanel(new FlowLayout());

		mu0 = new JTextField("1.5", 4);
		mu1 = new JTextField("1.0", 4);
		JLabel mu0Label = new JLabel("\u00B50: ");
		JLabel mu1Label = new JLabel("\u00B51: ");

		meansFields.add(mu0Label);
		meansFields.add(mu0);
		meansFields.add(mu1Label);
		meansFields.add(mu1);

		/*
		 * Panel to input experiment size
		 */
		JPanel sizeFields = new JPanel(new FlowLayout());

		n0 = new JTextField("20", 4);
		n1 = new JTextField("20", 4);
		nr = new JTextField("4", 4);
		JLabel n0Label = new JLabel("n0: ");
		JLabel n1Label = new JLabel("n1: ");
		JLabel nrLabel = new JLabel("nr: ");

		sizeFields.add(n0Label);
		sizeFields.add(n0);
		sizeFields.add(n1Label);
		sizeFields.add(n1);
		sizeFields.add(nrLabel);
		sizeFields.add(nr);

		/*
		 * Panel to populate fields with default values
		 */
		JPanel populateFields = new JPanel(new FlowLayout());

		JButton clearButton = new JButton("Clear Fields");
		clearButton.addActionListener(new ClearBtnListner());

		JButton popFromFile = new JButton("Populate Components from File");
		popFromFile.addActionListener(new PopFromFileListener());

		JButton saveFields = new JButton("Save Components to File");
		saveFields.addActionListener(new SaveFieldsListener());

		populateFields.add(clearButton);
		populateFields.add(popFromFile);
		populateFields.add(saveFields);

		/*
		 * Add sub-panels to cofvInputPanel
		 */
		cofvInputPanel.add(inputLabels);
		cofvInputPanel.add(varianceFields1);
		cofvInputPanel.add(varianceFields2);
		cofvInputPanel.add(varianceFields3);
		cofvInputPanel.add(meansFields);
		cofvInputPanel.add(sizeFields);
		cofvInputPanel.add(populateFields);

		/*
		 * Panel to perform simulation experiment
		 */
		JPanel simExpPanel = new JPanel();
		simExpPanel.setLayout(new BoxLayout(simExpPanel, BoxLayout.Y_AXIS));

		/*
		 * Panel within simExpPanel to describe function
		 */
		JPanel simExpDesc = new JPanel(new FlowLayout());

		JLabel expLabel = new JLabel("Simulation Experiments:");
		simExpDesc.add(expLabel);

		/*
		 * Panel within simExpPanel to show simulation experiment results
		 */
		JPanel simulationExperiment = new JPanel(new FlowLayout());

		numExp = new JTextField(4);
		JLabel numExpLabel = new JLabel("# of Experiments");
		JButton doSimExp = new JButton("Perform Simulation Experiments");
		doSimExp.addActionListener(new DoSimBtnListner());
		JLabel seedLabel = new JLabel("Seed for RNG");
		seed = new JTextField(9);
		seed.setText(Long.toString(System.currentTimeMillis()));
		useBias.addItemListener(new UseBiasListner());
		JButton saveLoc = new JButton("Output Location");
		saveLoc.addActionListener(new SaveSimulationListner());

		simulationExperiment.add(seedLabel);
		simulationExperiment.add(seed);
		simulationExperiment.add(numExpLabel);
		simulationExperiment.add(numExp);
		simulationExperiment.add(useBias);
		simulationExperiment.add(saveLoc);
		simulationExperiment.add(doSimExp);

		simExpPanel.add(simExpDesc);
		simExpPanel.add(simulationExperiment);

		/*
		 * Panel to estimate moments/components of variance
		 */
		JPanel cofvResultsPanel = new JPanel();
		cofvResultsPanel.setLayout(new BoxLayout(cofvResultsPanel,
				BoxLayout.Y_AXIS));

		/*
		 * Panel within cofvResultsPanel to describe function
		 */
		JPanel cofvResultsDesc = new JPanel(new FlowLayout());
		JLabel cofvLabel = new JLabel("Calculate Components of Variance:");
		cofvResultsDesc.add(cofvLabel);

		/*
		 * Panel within cofvResultsPanel to display cofv Results
		 */
		JPanel cofvResults = new JPanel(new FlowLayout());

		JButton doGenRoeMetz = new JButton("Perform Calculation");
		doGenRoeMetz.addActionListener(new DoGenRoeMetzBtnListner());

		cofvResults.add(doGenRoeMetz);

		cofvResultsPanel.add(cofvResultsDesc);
		cofvResultsPanel.add(cofvResults);

		cp.add(cofvInputPanel);
		cp.add(new JSeparator());
		cp.add(simExpPanel);
		cp.add(new JSeparator());
		cp.add(cofvResultsPanel);
	}

	private void clearInputs() {
		vR00.setText("");
		vC00.setText("");
		vRC00.setText("");
		vR10.setText("");
		vC10.setText("");
		vRC10.setText("");
		vR01.setText("");
		vC01.setText("");
		vRC01.setText("");
		vR11.setText("");
		vC11.setText("");
		vRC11.setText("");
		vR0.setText("");
		vC0.setText("");
		vRC0.setText("");
		vR1.setText("");
		vC1.setText("");
		vRC1.setText("");
		mu0.setText("");
		mu1.setText("");
		n0.setText("");
		n1.setText("");
		nr.setText("");
	}

	private ArrayList<String> readFile(InputStreamReader isr) {
		BufferedReader br = new BufferedReader(isr);
		ArrayList<String> content = new ArrayList<String>();
		String strtemp;
		try {
			while ((strtemp = br.readLine()) != null) {
				content.add(strtemp);
			}
		} catch (Exception e) {
			System.err.println("read file Error in RMGUInterface.java: "
					+ e.getMessage());
		}
		return content;
	}

	private void parseCofVfile(File f) {
		ArrayList<String> fileContent = new ArrayList<String>();

		if (f != null) {
			String filename = f.getPath();
			try {
				InputStreamReader isr;
				DataInputStream din;
				FileInputStream fstream = new FileInputStream(filename);
				din = new DataInputStream(fstream);
				isr = new InputStreamReader(din);
				fileContent = readFile(isr);
				din.close();
			} catch (Exception e) {
				System.err.println("Error reading file" + filename
						+ e.getMessage());
			}
		} else {
			return;
		}

		clearInputs();
		int totalLine = fileContent.size();
		int counter = 0;
		while (counter < totalLine) {
			String tempstr = fileContent.get(counter).toUpperCase();
			int loc = tempstr.indexOf("R00:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR00.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C00:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC00.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC00:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC00.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("R10:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR10.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C10:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC10.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC10:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC10.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("R01:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR01.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C01:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC01.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC01:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC01.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("R11:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR11.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C11:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC11.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC11:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC11.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("R0:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR0.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C0:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC0.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC0:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC0.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("R1:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vR1.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("C1:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vC1.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("RC1:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				vRC1.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("U0:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				mu0.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("U1:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				mu1.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("N0:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				n0.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("N1:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				n1.setText(tempstr.substring(tmploc + 1).trim());
			}
			loc = tempstr.indexOf("NR:");
			if (loc != -1) {
				int tmploc = tempstr.indexOf(":");
				nr.setText(tempstr.substring(tmploc + 1).trim());
			}
			counter++;
		}
	}

	class ClearBtnListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			vR00.setText("");
			vC00.setText("");
			vRC00.setText("");
			vR10.setText("");
			vC10.setText("");
			vRC10.setText("");
			vR01.setText("");
			vC01.setText("");
			vRC01.setText("");
			vR11.setText("");
			vC11.setText("");
			vRC11.setText("");
			vR0.setText("");
			vC0.setText("");
			vRC0.setText("");
			vR1.setText("");
			vC1.setText("");
			vRC1.setText("");
			mu0.setText("");
			mu1.setText("");
			n0.setText("");
			n1.setText("");
			nr.setText("");
		}
	}

	class PopFromFileListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"iRoeMetz CofV Input (.irm)", "irm");
			fc.setFileFilter(filter);
			@SuppressWarnings("unused")
			int fcReturn = fc.showOpenDialog((Component) e.getSource());
			File f = fc.getSelectedFile();
			parseCofVfile(f);
		}
	}

	class SaveFieldsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"iRoeMetz CofV Input (.irm)", "irm");
				fc.setFileFilter(filter);
				int fcReturn = fc.showOpenDialog((Component) e.getSource());
				if (fcReturn == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					String fPath = f.getPath();
					if (!fPath.toLowerCase().endsWith(".irm")) {
						f = new File(fPath + ".irm");
					}
					if (!f.exists()) {
						f.createNewFile();
					}
					FileWriter fw = new FileWriter(f.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("R00: " + vR00.getText() + "\n");
					bw.write("C00: " + vC00.getText() + "\n");
					bw.write("RC00: " + vRC00.getText() + "\n");
					bw.write("R10: " + vR10.getText() + "\n");
					bw.write("C10: " + vC10.getText() + "\n");
					bw.write("RC10: " + vRC10.getText() + "\n");
					bw.write("R01: " + vR01.getText() + "\n");
					bw.write("C01: " + vC01.getText() + "\n");
					bw.write("RC01: " + vRC01.getText() + "\n");
					bw.write("R11: " + vR11.getText() + "\n");
					bw.write("C11: " + vC11.getText() + "\n");
					bw.write("RC11: " + vRC11.getText() + "\n");
					bw.write("R0: " + vR0.getText() + "\n");
					bw.write("C0: " + vC0.getText() + "\n");
					bw.write("RC0: " + vRC0.getText() + "\n");
					bw.write("R1: " + vR1.getText() + "\n");
					bw.write("C1: " + vC1.getText() + "\n");
					bw.write("RC1: " + vRC1.getText() + "\n");
					bw.write("u0: " + mu0.getText() + "\n");
					bw.write("u1: " + mu1.getText() + "\n");
					bw.write("n0: " + n0.getText() + "\n");
					bw.write("n1: " + n1.getText() + "\n");
					bw.write("nr: " + nr.getText() + "\n");
					bw.close();
				}
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class UseBiasListner implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (useBias.isSelected()) {
				useBiasM = 1;
			} else {
				useBiasM = 0;
			}
		}
	}

	class SaveSimulationListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int fcReturn = fc.showSaveDialog((JComponent) arg0.getSource());
			if (fcReturn == JFileChooser.APPROVE_OPTION) {
				simSaveDirectory = fc.getSelectedFile().toString();
				System.out.println(simSaveDirectory);
			} else {
				System.out.println("No save directory selected");
			}
		}
	}

	private class SimExperiments extends SwingWorker<double[][][], Integer> {
		double[] u;
		double[] var_t;
		int[] n;
		int numTimes;
		Random rand;
		AtomicInteger val;
		String filenameTime;
		int whichTask;

		public SimExperiments(double[] u, double[] var_t, int[] n, Random rand,
				int numTimes, AtomicInteger val, String filenameTime,
				int whichTask) {
			this.u = u;
			this.var_t = var_t;
			this.n = n;
			this.rand = rand;
			this.numTimes = numTimes;
			this.val = val;
			this.filenameTime = filenameTime;
			this.whichTask = whichTask;
		}

		public double[][][] doInBackground() {
			double[][] avgBDGdata = new double[4][8];
			double[][] avgBCKdata = new double[4][7];
			double[][] avgDBMdata = new double[4][6];
			double[][] avgORdata = new double[4][6];
			double[][] avgMSdata = new double[4][6];
			double[] avgAUC = new double[3];

			for (int i = 0; i < numTimes; i++) {
				SimRoeMetz currSim = new SimRoeMetz(u, var_t, n, rand, useBiasM);

				writeMRMCFile(currSim.gett00(), currSim.gett01(),
						currSim.gett10(), currSim.gett11(), currSim.getAUC(),
						filenameTime, ((whichTask * numTimes) + i));

				avgBDGdata = matrix.matrixAdd(avgBDGdata, currSim.getBDGdata());
				avgBCKdata = matrix.matrixAdd(avgBCKdata, currSim.getBCKdata());
				avgDBMdata = matrix.matrixAdd(avgDBMdata, currSim.getDBMdata());
				avgORdata = matrix.matrixAdd(avgORdata, currSim.getORdata());
				avgMSdata = matrix.matrixAdd(avgMSdata, currSim.getMSdata());
				avgAUC = matrix.matrixAdd(avgAUC, currSim.getAUC());

				publish(val.getAndIncrement());
				setProgress(100 * i / numTimes);
			}

			double scaleFactor = 1.0 / (double) numTimes;
			avgBDGdata = matrix.scaleMatrix(avgBDGdata, scaleFactor);
			avgBCKdata = matrix.scaleMatrix(avgBCKdata, scaleFactor);
			avgDBMdata = matrix.scaleMatrix(avgDBMdata, scaleFactor);
			avgORdata = matrix.scaleMatrix(avgORdata, scaleFactor);
			avgMSdata = matrix.scaleMatrix(avgMSdata, scaleFactor);
			avgAUC = matrix.scaleVector(avgAUC, scaleFactor);

			return new double[][][] { avgBDGdata, avgBCKdata, avgDBMdata,
					avgORdata, avgMSdata, { avgAUC } };
		}

		protected void process(List<Integer> chunks) {
			for (int num : chunks)
				simProgress.setValue(num);
		}

		protected void done() {
			firePropertyChange("done", 0, 1);
		}
	}

	class DoSimBtnListner implements ActionListener {
		int doneTasks = 0;
		final int numCores = Runtime.getRuntime().availableProcessors();
		int numTasks;
		double[][][][] results = new double[numCores][][][];
		int[] n;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				double[] u = getMeans();
				double[] var_t = getVariances();
				n = getSizes();
				long seedVar = Long.parseLong(seed.getText());
				final int numTimes = Integer.valueOf(numExp.getText());

				if (simSaveDirectory == null || simSaveDirectory.equals("")) {
					JOptionPane
							.showMessageDialog(
									appl.getFrame(),
									"Save directory not specified.\nExperiment output files will not be written.",
									"Warning", JOptionPane.WARNING_MESSAGE);
				}

				// create string representation of current time to use in
				// filename
				DateFormat date = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
				Date currDate = new Date();
				String filenameTime = date.format(currDate);

				// create global RNG which is used across all experiments
				byte[] byteSeed = ByteBuffer.allocate(16).putLong(seedVar)
						.array();
				Random rand = new MersenneTwisterRNG(byteSeed);

				// create and display progress bar for simulation progress
				final AtomicInteger progVal = new AtomicInteger(0);
				simProgress = new JProgressBar(0, numTimes);
				simProgress.setValue(progVal.get());
				progDialog = new JDialog(appl.getFrame(), "Simulation Progress");
				JPanel pane = new JPanel(new FlowLayout());
				pane.add(simProgress);
				progDialog.setContentPane(pane);
				progDialog.pack();
				progDialog.setVisible(true);

				// divide simulations into separate tasks
				if (numTimes < numCores) {
					numTasks = numTimes;
				} else {
					numTasks = numCores;
				}
				final SimExperiments[] allTasks = new SimExperiments[numTasks];
				for (int i = 0; i < numTasks; i++) {
					final int taskNum = i;
					allTasks[i] = new SimExperiments(u, var_t, n, rand,
							numTimes / numTasks, progVal, filenameTime, i);
					// Check to see when each task finishes and get its results
					allTasks[i]
							.addPropertyChangeListener(new PropertyChangeListener() {
								public void propertyChange(
										PropertyChangeEvent evt) {
									if (evt.getPropertyName().equals("done")) {
										try {
											results[taskNum] = allTasks[taskNum]
													.get();
											doneTasks++;
											if (doneTasks == numTasks) {
												doneTasks = 0;
												processResults();
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										} catch (ExecutionException e) {
											e.printStackTrace();
										}
									}
								}
							});
				}
				// run each task in its own thread, to spread across cores
				for (int i = 0; i < numTasks; i++) {
					allTasks[i].execute();
				}

			} catch (NumberFormatException e1) {
				System.out.println(e1.toString());
				JOptionPane.showMessageDialog(appl.getFrame(),
						"Incorrect / Incomplete Input", "Error",
						JOptionPane.ERROR_MESSAGE);

			}
		}

		// Executed only after all simulation tasks are finished, averages
		// together their results.
		public void processResults() {
			progDialog.setVisible(false);

			double[][] allBDG = results[0][0];
			double[][] allBCK = results[0][1];
			double[][] allDBM = results[0][2];
			double[][] allOR = results[0][3];
			double[][] allMS = results[0][4];
			double[] allAUC = results[0][5][0];
			double[][] BDGcoeff = dbRecord.genBDGCoeff(n[2], n[0], n[1]);
			double[][] BCKcoeff = dbRecord.genBCKCoeff(n[2], n[0], n[1]);
			double[][] DBMcoeff = dbRecord.genDBMCoeff(n[2], n[0], n[1]);
			double[][] MScoeff = dbRecord.genMSCoeff(n[2], n[0], n[1]);
			double[][] ORcoeff = dbRecord.genORCoeff(n[2], n[0], n[1]);

			for (int i = 1; i < numTasks; i++) {
				allBDG = matrix.matrixAdd(allBDG, results[i][0]);
				allBCK = matrix.matrixAdd(allBCK, results[i][1]);
				allDBM = matrix.matrixAdd(allDBM, results[i][2]);
				allOR = matrix.matrixAdd(allOR, results[i][3]);
				allMS = matrix.matrixAdd(allMS, results[i][4]);
				allAUC = matrix.matrixAdd(allAUC, results[i][5][0]);
			}

			allBDG = matrix.scaleMatrix(allBDG, 1.0 / (double) numTasks);
			allBCK = matrix.scaleMatrix(allBCK, 1.0 / (double) numTasks);
			allDBM = matrix.scaleMatrix(allDBM, 1.0 / (double) numTasks);
			allOR = matrix.scaleMatrix(allOR, 1.0 / (double) numTasks);
			allMS = matrix.scaleMatrix(allMS, 1.0 / (double) numTasks);
			allAUC = matrix.scaleVector(allAUC, 1.0 / (double) numTasks);

			JDialog simOutput = new JDialog(appl.getFrame(),
					"Simulation Results");
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			JPanel tablePanel = new JPanel();
			tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

			JTabbedPane tabTables = new JTabbedPane();
			JComponent BDGpane = makeTablePane(new String[] { "", "M1", "M2",
					"M3", "M4", "M5", "M6", "M7", "M8" });
			JComponent BCKpane = makeTablePane(new String[] { "", "N", "D",
					"N~D", "R", "N~R", "D~R", "R~N~D" });
			JComponent DBMpane = makeTablePane(new String[] { "", "R", "C",
					"R~C", "T~R", "T~C", "T~R~C" });
			JComponent ORpane = makeTablePane(new String[] { "", "R", "TR",
					"COV1", "COV2", "COV3", "ERROR" });
			JComponent MSpane = makeTablePane(new String[] { "", "R", "C",
					"RC", "MR", "MC", "MRC" });
			tabTables.addTab("BDG", BDGpane);
			tabTables.addTab("BCK", BCKpane);
			tabTables.addTab("DBM", DBMpane);
			tabTables.addTab("OR", ORpane);
			tabTables.addTab("MS", MSpane);
			updateBDGpane(BDGpane, 0, allBDG, BDGcoeff);
			updateBCKpane(BCKpane, 0, allBCK, BCKcoeff);
			updateDBMpane(DBMpane, 0, allDBM, DBMcoeff);
			updateORpane(ORpane, 0, allOR, ORcoeff);
			updateMSpane(MSpane, 0, allMS, MScoeff);

			// Display AUCs
			DecimalFormat df = new DecimalFormat("0.###");
			JLabel AUCs = new JLabel("AUC1: " + df.format(allAUC[0])
					+ "   AUC2: " + df.format(allAUC[1]) + "   AUC1-AUC2: "
					+ df.format(allAUC[2]) + "   ");

			// create modality select buttons
			String str1 = "Modality 1";
			JRadioButton mod1SimButton = new JRadioButton(str1);
			mod1SimButton.setActionCommand(str1);
			mod1SimButton.setSelected(true);
			String str2 = "Modality 2";
			JRadioButton mod2SimButton = new JRadioButton(str2);
			mod2SimButton.setActionCommand(str2);
			String strD = "Difference";
			JRadioButton modDSimButton = new JRadioButton(strD);
			modDSimButton.setActionCommand(strD);
			// Group the radio buttons.
			ButtonGroup group = new ButtonGroup();
			group.add(mod1SimButton);
			group.add(mod2SimButton);
			group.add(modDSimButton);
			// Register a listener for the radio buttons.
			ModSimListner gListener = new ModSimListner(tabTables, allBDG,
					allBCK, allDBM, allOR, allMS, BDGcoeff, BCKcoeff, DBMcoeff,
					ORcoeff, MScoeff);
			mod1SimButton.addActionListener(gListener);
			mod2SimButton.addActionListener(gListener);
			modDSimButton.addActionListener(gListener);

			tablePanel.add(tabTables);
			buttonPanel.add(AUCs);
			buttonPanel.add(mod1SimButton);
			buttonPanel.add(mod2SimButton);
			buttonPanel.add(modDSimButton);
			panel.add(tablePanel);
			panel.add(buttonPanel);
			simOutput.add(panel);
			simOutput.pack();
			simOutput.setVisible(true);
		}

		/*
		 * radio buttons to select the type of modality when performing
		 * simulation experiments
		 */
		class ModSimListner implements ActionListener {
			JTabbedPane tabTables;
			private double[][] allBDG;
			private double[][] allBCK;
			private double[][] allDBM;
			private double[][] allOR;
			private double[][] allMS;
			private double[][] BDGcoeff;
			private double[][] BCKcoeff;
			private double[][] DBMcoeff;
			private double[][] MScoeff;
			private double[][] ORcoeff;

			public ModSimListner(JTabbedPane tabTables, double[][] allBDG,
					double[][] allBCK, double[][] allDBM, double[][] allOR,
					double[][] allMS, double[][] BDGcoeff, double[][] BCKcoeff,
					double[][] DBMcoeff, double[][] ORcoeff, double[][] MScoeff) {
				this.tabTables = tabTables;
				this.allBDG = allBDG;
				this.allBCK = allBCK;
				this.allDBM = allDBM;
				this.allOR = allOR;
				this.allMS = allMS;
				this.BDGcoeff = BDGcoeff;
				this.BCKcoeff = BCKcoeff;
				this.DBMcoeff = DBMcoeff;
				this.MScoeff = MScoeff;
				this.ORcoeff = ORcoeff;
			}

			public void actionPerformed(ActionEvent e) {
				String str;
				str = e.getActionCommand();
				if (str == "Modality 1") {
					updatePanes(0);
				}
				if (str == "Modality 2") {
					updatePanes(1);
				}
				if (str == "Difference") {
					updatePanes(3);
				}
			}

			private void updatePanes(int mod) {
				updateBDGpane((JComponent) tabTables.getComponent(0), mod,
						allBDG, BDGcoeff);
				updateBCKpane((JComponent) tabTables.getComponent(1), mod,
						allBCK, BCKcoeff);
				updateDBMpane((JComponent) tabTables.getComponent(2), mod,
						allDBM, DBMcoeff);
				updateORpane((JComponent) tabTables.getComponent(3), mod,
						allOR, ORcoeff);
				updateMSpane((JComponent) tabTables.getComponent(4), mod,
						allMS, MScoeff);
			}
		}
	}

	private class CalculateCofV extends SwingWorker<double[][][], Integer> {
		double[] u;
		double[] var_t;
		int[] n;

		public CalculateCofV(double[] u, double[] var_t, int[] n) {
			this.u = u;
			this.var_t = var_t;
			this.n = n;
		}

		public double[][][] doInBackground() {
			CofVGenRoeMetz.genRoeMetz(u, var_t, n);
			return new double[][][] { CofVGenRoeMetz.getBDGdata(),
					CofVGenRoeMetz.getBCKdata(), CofVGenRoeMetz.getDBMdata(),
					CofVGenRoeMetz.getORdata(), CofVGenRoeMetz.getMSdata() };
		}

		protected void done() {
			firePropertyChange("done", 0, 1);
		}
	}

	class DoGenRoeMetzBtnListner implements ActionListener {
		double[][][] results;
		int[] n;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				double[] u = getMeans();
				double[] var_t = getVariances();
				n = getSizes();
				;

				final CalculateCofV calcTask = new CalculateCofV(u, var_t, n);
				calcTask.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("done")) {
							try {
								results = calcTask.get();
								processResults();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
						}
					}
				});

				calcTask.execute();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(appl.getFrame(),
						"Incorrect / Incomplete Input", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		public void processResults() {
			JDialog estOutput = new JDialog(appl.getFrame(),
					"Calculation Results");
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			JPanel tablePanel = new JPanel();
			tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

			JTabbedPane tabTables = new JTabbedPane();
			JComponent BDGpane = makeTablePane(new String[] { "", "M1", "M2",
					"M3", "M4", "M5", "M6", "M7", "M8" });
			JComponent BCKpane = makeTablePane(new String[] { "", "N", "D",
					"N~D", "R", "N~R", "D~R", "R~N~D" });
			JComponent DBMpane = makeTablePane(new String[] { "", "R", "C",
					"R~C", "T~R", "T~C", "T~R~C" });
			JComponent ORpane = makeTablePane(new String[] { "", "R", "TR",
					"COV1", "COV2", "COV3", "ERROR" });
			JComponent MSpane = makeTablePane(new String[] { "", "R", "C",
					"RC", "MR", "MC", "MRC" });
			tabTables.addTab("BDG", BDGpane);
			tabTables.addTab("BCK", BCKpane);
			tabTables.addTab("DBM", DBMpane);
			tabTables.addTab("OR", ORpane);
			tabTables.addTab("MS", MSpane);

			double[][] BDG = results[0];
			double[][] BCK = results[1];
			double[][] DBM = results[2];
			double[][] OR = results[3];
			double[][] MS = results[4];
			double[][] BDGcoeff = dbRecord.genBDGCoeff(n[2], n[0], n[1]);
			double[][] BCKcoeff = dbRecord.genBCKCoeff(n[2], n[0], n[1]);
			double[][] DBMcoeff = dbRecord.genDBMCoeff(n[2], n[0], n[1]);
			double[][] MScoeff = dbRecord.genMSCoeff(n[2], n[0], n[1]);
			double[][] ORcoeff = dbRecord.genORCoeff(n[2], n[0], n[1]);
			updateBDGpane(BDGpane, 0, results[0], BDGcoeff);
			updateBCKpane(BCKpane, 0, results[1], BCKcoeff);
			updateDBMpane(DBMpane, 0, results[2], DBMcoeff);
			updateORpane(ORpane, 0, results[3], ORcoeff);
			updateMSpane(MSpane, 0, results[4], MScoeff);

			// create AUCs label
			DecimalFormat df = new DecimalFormat("0.###");
			JLabel AUCs = new JLabel("AUC1: " + df.format(BDG[0][0])
					+ "   AUC2: " + df.format(BDG[1][0]) + "   AUC1-AUC2: "
					+ df.format(BDG[0][0] - BDG[1][0]) + "    ");

			// create modality select buttons
			String str1 = "Modality 1";
			JRadioButton mod1EstButton = new JRadioButton(str1);
			mod1EstButton.setActionCommand(str1);
			mod1EstButton.setSelected(true);
			String str2 = "Modality 2";
			JRadioButton mod2EstButton = new JRadioButton(str2);
			mod2EstButton.setActionCommand(str2);
			String strD = "Difference";
			JRadioButton modDEstButton = new JRadioButton(strD);
			modDEstButton.setActionCommand(strD);
			// Group the radio buttons.
			ButtonGroup groupEst = new ButtonGroup();
			groupEst.add(mod1EstButton);
			groupEst.add(mod2EstButton);
			groupEst.add(modDEstButton);

			// Register a listener for the radio buttons.
			ModEstListner gListenerEst = new ModEstListner(tabTables, BDG, BCK,
					DBM, OR, MS, BDGcoeff, BCKcoeff, DBMcoeff, ORcoeff, MScoeff);
			mod1EstButton.addActionListener(gListenerEst);
			mod2EstButton.addActionListener(gListenerEst);
			modDEstButton.addActionListener(gListenerEst);

			tablePanel.add(tabTables);
			buttonPanel.add(AUCs);
			buttonPanel.add(mod1EstButton);
			buttonPanel.add(mod2EstButton);
			buttonPanel.add(modDEstButton);
			panel.add(tablePanel);
			panel.add(buttonPanel);
			estOutput.add(panel);
			estOutput.pack();
			estOutput.setVisible(true);
		}

		/*
		 * radio buttons to select the type of modality when performing variance
		 * calculation
		 */
		class ModEstListner implements ActionListener {
			JTabbedPane tabTables;
			double[][] BDG;
			double[][] BCK;
			double[][] DBM;
			double[][] OR;
			double[][] MS;
			double[][] BDGcoeff;
			double[][] BCKcoeff;
			double[][] DBMcoeff;
			double[][] ORcoeff;
			double[][] MScoeff;

			public ModEstListner(JTabbedPane tabTables, double[][] BDG,
					double[][] BCK, double[][] DBM, double[][] OR,
					double[][] MS, double[][] BDGcoeff, double[][] BCKcoeff,
					double[][] DBMcoeff, double[][] ORcoeff, double[][] MScoeff) {
				this.tabTables = tabTables;
				this.BDG = BDG;
				this.BCK = BCK;
				this.DBM = DBM;
				this.OR = OR;
				this.MS = MS;
				this.BDGcoeff = BDGcoeff;
				this.BCKcoeff = BCKcoeff;
				this.DBMcoeff = DBMcoeff;
				this.ORcoeff = ORcoeff;
				this.MScoeff = MScoeff;
			}

			public void actionPerformed(ActionEvent e) {
				String str;
				str = e.getActionCommand();
				if (str == "Modality 1") {
					updatePanes(0);
				}
				if (str == "Modality 2") {
					updatePanes(1);
				}
				if (str == "Difference") {
					updatePanes(3);
				}
			}

			private void updatePanes(int mod) {
				updateBDGpane((JComponent) tabTables.getComponent(0), mod,
						results[0], BDGcoeff);
				updateBCKpane((JComponent) tabTables.getComponent(1), mod,
						results[1], BCKcoeff);
				updateDBMpane((JComponent) tabTables.getComponent(2), mod,
						results[2], DBMcoeff);
				updateORpane((JComponent) tabTables.getComponent(3), mod,
						results[3], ORcoeff);
				updateMSpane((JComponent) tabTables.getComponent(4), mod,
						results[4], MScoeff);
			}
		}
	}

	public void writeMRMCFile(double[][] t00, double[][] t01, double[][] t10,
			double[][] t11, double[] auc, String filename, int fileNum) {
		try {
			File file = new File(simSaveDirectory + "/" + filename + "-"
					+ String.format("%05d", fileNum) + ".imrmc");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			DecimalFormat df = new DecimalFormat("0.###");

			bw.write("Simulated iMRMC input from " + filename + "\n");
			bw.write("\n");
			bw.write("NR: " + t00.length + "\n");
			bw.write("N0: " + t00[0].length + "\n");
			bw.write("N1: " + t10[0].length + "\n");
			bw.write("NM: 2\n");
			bw.write("\n");
			bw.write("AUC1: " + df.format(auc[0]) + "\n");
			bw.write("AUC2: " + df.format(auc[1]) + "\n");
			bw.write("DAUC: " + df.format(auc[2]) + "\n");
			bw.write("\n");
			bw.write("BEGIN DATA:\n");

			int caseNum = 1;
			for (int j = 0; j < t00[0].length; j++) {
				bw.write("-1," + caseNum + ",0,0\n");
				caseNum++;
			}
			for (int k = 0; k < t10[0].length; k++) {
				bw.write("-1," + caseNum + ",0,1\n");
				caseNum++;
			}

			for (int i = 0; i < t00.length; i++) {
				caseNum = 1;
				for (int m = 0; m < t00[i].length; m++) {
					bw.write((i + 1) + "," + caseNum + ",1," + t00[i][m] + "\n");
					bw.write((i + 1) + "," + caseNum + ",2," + t01[i][m] + "\n");
					caseNum++;
				}
				for (int n = 0; n < t10[i].length; n++) {
					bw.write((i + 1) + "," + caseNum + ",1," + t10[i][n] + "\n");
					bw.write((i + 1) + "," + caseNum + ",2," + t11[i][n] + "\n");
					caseNum++;
				}
			}
			bw.close();
		} catch (IOException e) {

		}
	}

	public int[] getSizes() {
		return new int[] { Integer.valueOf(n0.getText()),
				Integer.valueOf(n1.getText()), Integer.valueOf(nr.getText()) };
	}

	public double[] getMeans() {
		return new double[] { Double.valueOf(mu0.getText()),
				Double.valueOf(mu1.getText()) };
	}

	public double[] getVariances() {
		return new double[] { Double.valueOf(vR00.getText()),
				Double.valueOf(vC00.getText()),
				Double.valueOf(vRC00.getText()),
				Double.valueOf(vR10.getText()), Double.valueOf(vC10.getText()),
				Double.valueOf(vRC10.getText()),
				Double.valueOf(vR01.getText()), Double.valueOf(vC01.getText()),
				Double.valueOf(vRC01.getText()),
				Double.valueOf(vR11.getText()), Double.valueOf(vC11.getText()),
				Double.valueOf(vRC11.getText()), Double.valueOf(vR0.getText()),
				Double.valueOf(vC0.getText()), Double.valueOf(vRC0.getText()),
				Double.valueOf(vR1.getText()), Double.valueOf(vC1.getText()),
				Double.valueOf(vRC1.getText()) };
	}

	private JComponent makeTablePane(String[] colNames) {
		JPanel thePane = new JPanel(false);
		Object[][] theData = new Object[3][colNames.length];
		JTable table = new JTable(theData, colNames);
		table.setValueAt("components", 0, 0);
		table.setValueAt("coeff", 1, 0);
		table.setValueAt("total", 2, 0);
		thePane.setLayout(new BorderLayout());
		thePane.add(table.getTableHeader(), BorderLayout.PAGE_START);
		thePane.add(table);
		thePane.add(new JLabel("sqrt(Var) = "), BorderLayout.EAST);
		return thePane;
	}

	private void updateBDGpane(JComponent BDGpane, int mod, double[][] allBDG,
			double[][] BDGcoeff) {
		JTable table = (JTable) BDGpane.getComponent(1);
		JLabel varLabel = (JLabel) BDGpane.getComponent(2);
		double[][] BDGdata = new double[3][8];
		double[][] tempBDG = dbRecord.getBDGTab(mod, allBDG, BDGcoeff);
		if (mod == 0) {
			BDGdata[0] = tempBDG[0];
			BDGdata[1] = tempBDG[1];
		} else if (mod == 1) {
			BDGdata[0] = tempBDG[2];
			BDGdata[1] = tempBDG[3];
		} else if (mod == 3) {
			BDGdata[0] = tempBDG[4];
			BDGdata[1] = matrix.scaleVector(tempBDG[5], 0.5);
		}
		BDGdata[2] = tempBDG[6];
		double currVar = matrix.total(tempBDG[6]);
		DecimalFormat df1 = new DecimalFormat("0.###E0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				table.setValueAt(df1.format(BDGdata[i][j]), i, j + 1);
			}
		}
		String output = df2.format(Math.sqrt(currVar));
		varLabel.setText("sqrt(Var) = " + output);
	}

	private void updateBCKpane(JComponent BCKpane, int mod, double[][] allBCK,
			double[][] BCKcoeff) {
		JTable table = (JTable) BCKpane.getComponent(1);
		JLabel varLabel = (JLabel) BCKpane.getComponent(2);
		double[][] BCKdata = new double[3][7];
		double[][] tempBCK = dbRecord.getBCKTab(mod, allBCK, BCKcoeff);
		if (mod == 0) {
			BCKdata[0] = tempBCK[0];
			BCKdata[1] = tempBCK[1];
		} else if (mod == 1) {
			BCKdata[0] = tempBCK[2];
			BCKdata[1] = tempBCK[3];
		} else if (mod == 3) {
			BCKdata[0] = tempBCK[4];
			BCKdata[1] = matrix.scaleVector(tempBCK[5], 0.5);
		}
		BCKdata[2] = tempBCK[6];
		double currVar = matrix.total(tempBCK[6]);
		DecimalFormat df1 = new DecimalFormat("0.###E0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				table.setValueAt(df1.format(BCKdata[i][j]), i, j + 1);
			}
		}
		String output = df2.format(Math.sqrt(currVar));
		varLabel.setText("sqrt(Var) = " + output);
	}

	private void updateDBMpane(JComponent DBMpane, int mod, double[][] allDBM,
			double[][] DBMcoeff) {
		JTable table = (JTable) DBMpane.getComponent(1);
		JLabel varLabel = (JLabel) DBMpane.getComponent(2);
		double[][] DBMdata = dbRecord.getDBMTab(mod, allDBM, DBMcoeff);
		DecimalFormat df1 = new DecimalFormat("0.###E0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				table.setValueAt(df1.format(DBMdata[i][j]), i, j + 1);
			}
		}
		double currVar = matrix.total(DBMdata[2]);
		String output = df2.format(Math.sqrt(currVar));
		varLabel.setText("sqrt(Var) = " + output);
	}

	private void updateORpane(JComponent ORpane, int mod, double[][] allOR,
			double[][] ORcoeff) {
		JTable table = (JTable) ORpane.getComponent(1);
		JLabel varLabel = (JLabel) ORpane.getComponent(2);
		double[][] ORdata = dbRecord.getORTab(mod, allOR, ORcoeff);
		DecimalFormat df = new DecimalFormat("0.###E0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				table.setValueAt(df.format(ORdata[i][j]), i, j + 1);
			}
		}
		double currVar = matrix.total(ORdata[2]);
		String output = df2.format(Math.sqrt(currVar));
		varLabel.setText("sqrt(Var) = " + output);
	}

	private void updateMSpane(JComponent MSpane, int mod, double[][] allMS,
			double[][] MScoeff) {
		JTable table = (JTable) MSpane.getComponent(1);
		JLabel varLabel = (JLabel) MSpane.getComponent(2);
		double[][] MSdata = dbRecord.getMSTab(mod, allMS, MScoeff);
		DecimalFormat df = new DecimalFormat("0.###E0");
		DecimalFormat df2 = new DecimalFormat("0.000");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				table.setValueAt(df.format(MSdata[i][j]), i, j + 1);
			}
		}
		double currVar = matrix.total(MSdata[2]);
		String output = df2.format(Math.sqrt(currVar));
		varLabel.setText("sqrt(Var) = " + output);
	}
}
