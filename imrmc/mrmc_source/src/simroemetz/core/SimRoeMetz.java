/*
 * SimRoeMetz.java
 * 
 * v2.0b
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
 *     Simulates scores according to Roe and Metz simulation. Adapted for java from sim_roemetz.pro 
 *     (Brandon D. Gallas, PhD)
 */

package simroemetz.core;

import java.util.Arrays;
import java.util.Random;

import mrmc.core.matrix;

public class SimRoeMetz {
	static double[][] t00;
	static double[][] t01;
	static double[][] t10;
	static double[][] t11;
	static double[] auc;

	public static void main(String[] args) {
		// double[] u = { 1.5, 3 };
		// double[] var_t = { 0.836205, 6.06649, 0.076157, 0.0645917, 0.30713,
		// 0.0529776 };
		// int[] n = { 259, 51, 15 };
		double[] u = { 1.5, 1.0 };

		double[] var_t = { 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 };
		int[] n = { 200, 200, 20 };

		doSim(u, var_t, n);

		System.out.println("t00:");
		matrix.printMatrix(t00);
		System.out.println();
		System.out.println("t01:");
		matrix.printMatrix(t01);
		System.out.println();
		System.out.println("t10:");
		matrix.printMatrix(t10);
		System.out.println();
		System.out.println("t11:");
		matrix.printMatrix(t11);
		System.out.println();
		System.out.println("AUC:");
		System.out.println(Arrays.toString(auc));
	}

	// TODO verify correctness
	public static void doSim(double[] u, double[] var_t, int[] n) {
		if (u.length != 2) {
			System.out.println("input u is of incorrect size");
			return;
		}
		if (var_t.length != 18) {
			System.out.println("input var_t is of incorrect size");
			return;
		}
		if (n.length != 3) {
			System.out.println("input n is of incorrect size");
			return;
		}

		double mu_0 = u[0];
		double mu_1 = u[1];

		double[] stdDevs = new double[var_t.length];
		for (int i = 0; i < var_t.length; i++) {
			stdDevs[i] = Math.sqrt(var_t[i]);
		}

		int n0 = n[0];
		int n1 = n[1];
		int nr = n[2];

		double snr_0 = mu_0 / matrix.total(var_t);
		double snr_1 = mu_1 / matrix.total(var_t);
		double auc_0 = snrToAUC(snr_0);
		double auc_1 = snrToAUC(snr_1);
		auc = new double[] { auc_0, auc_1, auc_0 - auc_1 };

		Random rand = new Random(); // uses currentTimeMillis() as seed by
									// default
		double[] R00 = fillGaussian(stdDevs[0], rand, nr);
		double[] C00 = fillGaussian(stdDevs[1], rand, n0);
		double[][] RC00 = fillGaussian(stdDevs[2], rand, nr, n0);
		double[] R10 = fillGaussian(stdDevs[3], rand, nr);
		double[] C10 = fillGaussian(stdDevs[4], rand, n1);
		double[][] RC10 = fillGaussian(stdDevs[5], rand, nr, n1);
		double[] R01 = fillGaussian(stdDevs[6], rand, nr);
		double[] C01 = fillGaussian(stdDevs[7], rand, n0);
		double[][] RC01 = fillGaussian(stdDevs[8], rand, nr, n0);
		double[] R11 = fillGaussian(stdDevs[9], rand, nr);
		double[] C11 = fillGaussian(stdDevs[10], rand, n1);
		double[][] RC11 = fillGaussian(stdDevs[11], rand, nr, n1);
		double[] R0 = fillGaussian(stdDevs[12], rand, nr);
		double[] C0 = fillGaussian(stdDevs[13], rand, n0);
		double[][] RC0 = fillGaussian(stdDevs[14], rand, nr, n0);
		double[] R1 = fillGaussian(stdDevs[15], rand, nr);
		double[] C1 = fillGaussian(stdDevs[16], rand, n1);
		double[][] RC1 = fillGaussian(stdDevs[17], rand, nr, n1);

		t00 = new double[nr][n0];
		t01 = new double[nr][n0];
		t10 = new double[nr][n1];
		t11 = new double[nr][n1];

		for (int i = 0; i < nr; i++) {
			Arrays.fill(t10[i], mu_0);
			Arrays.fill(t11[i], mu_1);
		}

		for (int r = 0; r < nr; r++) {
			for (int i = 0; i < n0; i++) {
				t00[r][i] += R0[r];
				t00[r][i] += R00[r];
				t01[r][i] += R0[r];
				t01[r][i] += R01[r];

				t00[r][i] += C0[i];
				t00[r][i] += C00[i];
				t01[r][i] += C0[i];
				t01[r][i] += C01[i];
			}
			for (int j = 0; j < n1; j++) {
				t10[r][j] += R1[r];
				t10[r][j] += R10[r];
				t11[r][j] += R1[r];
				t11[r][j] += R11[r];

				t10[r][j] += C1[j];
				t10[r][j] += C10[j];
				t11[r][j] += C1[j];
				t11[r][j] += C11[j];
			}
		}

		t00 = matrix.matrixAdd(t00, RC0);
		t01 = matrix.matrixAdd(t01, RC0);
		t00 = matrix.matrixAdd(t00, RC00);
		t01 = matrix.matrixAdd(t01, RC01);

		t10 = matrix.matrixAdd(t10, RC1);
		t11 = matrix.matrixAdd(t11, RC1);
		t10 = matrix.matrixAdd(t10, RC10);
		t11 = matrix.matrixAdd(t11, RC11);

	}

	public static double snrToAUC(double snr) {
		double toReturn = 0;
		toReturn = 0.5 + (0.5 * ErrorFunction.erf(0.5 * snr));
		return toReturn;
	}

	public static double[] fillGaussian(double scalar, Random rand, int x) {
		double[] toReturn = new double[x];
		for (int i = 0; i < x; i++) {
			toReturn[i] = scalar * rand.nextGaussian();
		}
		return toReturn;
	}

	public static double[][] fillGaussian(double scalar, Random rand, int x,
			int y) {
		double[][] toReturn = new double[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				toReturn[i][j] = scalar * rand.nextGaussian();
			}
		}
		return toReturn;
	}

}