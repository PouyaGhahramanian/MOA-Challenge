
///     ******************************************************************************
///     This script generates 10.000 data, using RandomRBFGenerator, as well as 
///     storing data in the RBFdataset file located in the same directory as the project.
///     ******************************************************************************

package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import moa.core.InstanceExample;
import moa.streams.generators.RandomRBFGenerator;

public class generate {
	
	final static String fileName="./RBFdataset";
	final static int size=10000;
	
	public static void main(String[] args) {
		///Generate Data Set
		int i=0;
		InstanceExample ie = null;
		RandomRBFGenerator generator=new RandomRBFGenerator();
		generator.numClassesOption.setValue(2);
		generator.numAttsOption.setValue(10);
		generator.prepareForUse();
		
		//cleaning dataset file before writing new data in it
		try {
			print_to_file("",size-1,false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//writing new data in the dataset file
		while(generator.hasMoreInstances() && i<size) {
			ie=generator.nextInstance();
			try {
				print_to_file(ie.getData().toString(),i,true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("Data generated and stored in RBFdataset file successfully...");
	}
	public static void print_to_file(String data, int j,boolean append) 
			  throws IOException {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
				writer.append(data);
				if(j!=size-1) {
				    writer.append("\n");
				}
	     		writer.close();
			}
}
