
///     ***************************************************************************************
///     This script classifies data stored in the RBFdataset file located in the same directory
///     by using a Naive Bayes classifier, a Hoeffding Tree and an ensemble classifier based on
///     two afore-mentioned classifiers with equal weights
///     ***************************************************************************************

package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.InstanceExample;
import moa.streams.generators.RandomRBFGenerator;
import main.graph;

public class train {
	
	final static String fileName="./RBFdataset";
	final static int size=10000;
	
	public static void main(String[] args) throws Exception {
		
		// Defining variables
		ArrayList<String> arrds=new ArrayList<>();
		InstanceExample ie=null;
		int class_id=0, i=0;
		String class_name="class1";
		int ensemble_crr=0, NBcrr=0, HTcrr=0, samples=0;
		double ensemble_pred_value_1=0.0, ensemble_pred_value_2=0.0
				,NB_pred_value_1=0.0, NB_pred_value_2=0.0
				,HT_pred_value_1=0.0, HT_pred_value_2=0.0;
		String ensemble_pred="";
		double ensemble_acc=0.0, NB_acc=0.0, HT_acc=0;
		
		double[] ensemble_accuracy=new double[100], NB_accuracy=new double[100], HT_accuracy=new double[100];
								
		// Initializing generator
		RandomRBFGenerator generator=new RandomRBFGenerator();
		generator.numClassesOption.setValue(2);
		generator.numAttsOption.setValue(10);
		generator.prepareForUse();
		
		// Initializing classifiers
		Classifier HT=new HoeffdingTree();
		Classifier NB=new NaiveBayes();
		HT.prepareForUse();
		NB.prepareForUse();
		
		// Initialize Instance variable
		ie=generator.nextInstance();
		
		// Reading instances from file and storing in a ArrayList for training classifiers
		try {
			arrds=read_from_file(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set InstanceExample variable (ie) parameters with regard to the data rows for training classifiers on them
		while(i<size) {
			ie.instance.setValue(0, Double.parseDouble(arrds.get(i).split(",")[0]));
			ie.instance.setValue(1, Double.parseDouble(arrds.get(i).split(",")[1]));
			ie.instance.setValue(2, Double.parseDouble(arrds.get(i).split(",")[2]));
			ie.instance.setValue(3, Double.parseDouble(arrds.get(i).split(",")[3]));
			ie.instance.setValue(4, Double.parseDouble(arrds.get(i).split(",")[4]));
			ie.instance.setValue(5, Double.parseDouble(arrds.get(i).split(",")[5]));
			ie.instance.setValue(6, Double.parseDouble(arrds.get(i).split(",")[6]));
			ie.instance.setValue(7, Double.parseDouble(arrds.get(i).split(",")[7]));
			ie.instance.setValue(8, Double.parseDouble(arrds.get(i).split(",")[8]));
			ie.instance.setValue(9, Double.parseDouble(arrds.get(i).split(",")[9]));
			class_name=arrds.get(i).split(",")[10].toString();
			if(class_name.equals("class1")) {
				class_id=0;
			}
			else {
				class_id=1;
			}
			ie.instance.setClassValue(0,class_id);
			System.out.println(ie.instance.toString());
			
			// Calculating ensemble classifier prediction for both of the classes
			ensemble_pred_value_1=(NB.getPredictionForInstance(ie.instance).getVote(0, 0)+HT.getPredictionForInstance(ie.instance).getVote(0, 0))/2;
			ensemble_pred_value_2=(NB.getPredictionForInstance(ie.instance).getVote(0, 1)+HT.getPredictionForInstance(ie.instance).getVote(0, 1))/2;
						
			if(ensemble_pred_value_1 > ensemble_pred_value_2) {
				ensemble_pred="class1";
			}
			else {
				ensemble_pred="class2";
			}
			if(class_name.equals(ensemble_pred)) {
				ensemble_crr++;
			}
			
			if(NB.correctlyClassifies(ie.instance)) {
				NBcrr++;
			}
			
			NB_pred_value_1=NB.getPredictionForInstance(ie.instance).getVote(0, 0);
			NB_pred_value_2=NB.getPredictionForInstance(ie.instance).getVote(0, 1);
			
			System.out.println("NB pred 1: "+Double.toString(NB_pred_value_1));
			System.out.println("NB pred 2: "+Double.toString(NB_pred_value_2));
			System.out.println("NB pred true??: "+Boolean.toString(NB.correctlyClassifies(ie.instance)));
			NB.trainOnInstance(ie.instance);
			if(HT.correctlyClassifies(ie.instance)) {
				HTcrr++;
			}
			
			HT_pred_value_1=HT.getPredictionForInstance(ie.instance).getVote(0, 0);
			HT_pred_value_2=HT.getPredictionForInstance(ie.instance).getVote(0, 1);
			
			System.out.println("HT pred 1: "+Double.toString(HT_pred_value_1));
			System.out.println("HT pred 2: "+Double.toString(HT_pred_value_2));
			System.out.println("HT pred true??: "+Boolean.toString(HT.correctlyClassifies(ie.instance)));
			HT.trainOnInstance(ie.instance);
			
			System.out.println("ensemble pred 1= "+ensemble_pred_value_1);
			System.out.println("ensemble pred 2= "+ensemble_pred_value_2);
			System.out.println("ensemble pred: "+ensemble_pred);
			System.out.println("ensemble pred true??: "+Boolean.toString(ensemble_pred.equals(class_name)));
			
			samples++;
			i++;
						
			// Calculating accuracy for classifiers
			NB_acc=100.0*(double)NBcrr/(double)samples;
			HT_acc=100.0*(double)HTcrr/(double)samples;
			ensemble_acc=100.0*(double) ensemble_crr/(double)samples;
			
			if(i%100==0) {
				NB_accuracy[i/100-1]=NB_acc;
				HT_accuracy[i/100-1]=HT_acc;
				ensemble_accuracy[i/100-1]=ensemble_acc;
			}
			
		}

		// Calculating accuracy for classifiers
		NB_acc=100.0*(double)NBcrr/(double)samples;
		HT_acc=100.0*(double)HTcrr/(double)samples;
		ensemble_acc=100.0*(double) ensemble_crr/(double)samples;
		
		System.out.println("--------------------------------------");
		System.out.println(" Results: ");
		System.out.println("--------------------------------------");
		
		System.out.print("NB Accuracy: ");
		System.out.println(Double.toString(NB_acc));
		System.out.print("HT Accuracy: ");
		System.out.println(Double.toString(HT_acc));
		System.out.print("Ensemble Accuracy: ");
		System.out.println(Double.toString(ensemble_acc));
		System.out.println("--------------------------------------");
		
		graph graph_accuracy = new graph("Accuracy Plot", 100, NB_accuracy, HT_accuracy, ensemble_accuracy);
		graph_accuracy.pack();
		graph_accuracy.setVisible(true);
	}
	
	// Function for reading data from RBFdataset file and store in a ArrayList
	public static ArrayList<String> read_from_file(String path) throws IOException {
		
		Scanner s = new Scanner(new File(path));
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.next());
		}
		s.close();
		return list;	
	}
		

}
