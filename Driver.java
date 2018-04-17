import java.util.*;
import java.io.*;

public class Driver{

	int machineSize;
	int pageSize;
	int processSize;
	int jobMix;
	int numberOfReferences;
	String algorithm;
	FrameTable frameTable = null;
	Scanner random = null;
	Process[] processes;

	public Driver(int machineSize, int pageSize, int processSize, int jobMix, int numberOfReferences, String algorithm, Scanner random, FrameTable frame){
		this.machineSize = machineSize;
		this.pageSize = pageSize;
		this.processSize = processSize;
		this.jobMix = jobMix;
		this.numberOfReferences = numberOfReferences;
		this.algorithm = algorithm;
		this.random = random;
		this.frameTable = frame;
	}
	
	public void run(){
		if(jobMix == 1){
			processes = new Process[1];
			processes[0] = new Process(processSize, 1);
			case1();
		}
		else{
			processes = new Process[4];
			for(int i = 0; i < 4; i++){
				processes[i] = new Process(processSize, i + 1);
			}
			case234(jobMix);
		}
	}
	
	public void case1(){
		for(int time = 0; time < numberOfReferences; time++){
			int pageNumber = processes[0].getNextWord()/pageSize;
			int currentTime = time + 1;
			if(frameTable.hasPageFault(pageNumber, 1, currentTime)){
				frameTable.replace(processes, pageNumber, 1, currentTime);
				processes[0].increaseFaultTime();
			}
			processes[0].nextReference(1, 0, 0, random);
		}
	}

	public void case234(int jobMix){
		int totalCycle = numberOfReferences/3;
		double[][] possibility = new double[4][3];

		if(jobMix == 2){
			for(int i = 0; i < 4; i++){
				possibility[i][0] = 1;
				possibility[i][1] = 0;
				possibility[i][2] = 0;
			}
		}
		else if(jobMix == 4){
			possibility[0][0] = 0.750;	possibility[0][1] = 0.250;	possibility[0][2] = 0.000;
			possibility[1][0] = 0.750;	possibility[1][1] = 0.000;	possibility[1][2] = 0.250;
			possibility[2][0] = 0.750;	possibility[2][1] = 0.125;	possibility[2][2] = 0.125;
			possibility[3][0] = 0.500;	possibility[3][1] = 0.125;	possibility[3][2] = 0.125;
		}

		for (int cycle = 0; cycle <= totalCycle; cycle++){
			for(int i = 0; i < 4; i++){
				runProcess(i + 1, possibility[i][0], possibility[i][1], possibility[i][2], cycle, totalCycle);
			}
		}
	}
	
	public void runProcess(int processId, double A, double B, double C, int cycle, int totalCycle){
		int referenceTimes;
		if(cycle != totalCycle){
			referenceTimes = 3;
		}
		else{
			referenceTimes = numberOfReferences%3;
		}
		for(int ref = 0; ref < referenceTimes; ref++){
			int pageNumber = processes[processId - 1].getNextWord()/pageSize;
			int currentTime = (12 * cycle) + ref + 1 + (processId - 1)*referenceTimes;
			if(frameTable.hasPageFault(pageNumber, processId, currentTime)){
				frameTable.replace(processes, pageNumber, processId, currentTime);
				processes[processId - 1].increaseFaultTime();
			}
			processes[processId - 1].nextReference(A, B, C, random);
		}
	}
	
	public void print(){
		int totalFaultTimes = 0;
		int totalResidencyTimes = 0;
		int totalEvictTimes = 0;
		System.out.println("The machine size is " + Integer.toString(this.machineSize));
		System.out.println("The page size is " + Integer.toString(this.pageSize));
		System.out.println("The process size is " + Integer.toString(this.processSize));
		System.out.println("The job mix number is " + Integer.toString(this.jobMix));
		System.out.println("The number of references per process is " + Integer.toString(this.numberOfReferences));
		System.out.println("The replacement algorithm is " + this.algorithm);
		System.out.println("The level of debugging output is 0\n");
		for(int i = 0; i < processes.length; i++){
			int faultTime = processes[i].pageFaultTimes;
			int residencyTime = processes[i].residencyTime;
			int evictTime = processes[i].evictTimes;
			System.out.println("Process " + Integer.toString(i + 1) + " faultTime - " + Integer.toString(faultTime) + " residencyTime - " + Integer.toString(residencyTime) + " evictTime - " + Integer.toString(evictTime));
			if(evictTime == 0){
				System.out.println("Process " + Integer.toString(i + 1) + " had " + Integer.toString(faultTime) + " faults.\n\tWith no evictions, the average residence is undefined.");
			} 
			else{
				double averageResidency = (double)residencyTime/evictTime;
				System.out.println("Process " + Integer.toString(i + 1) + " had " + Integer.toString(faultTime) + " faults and " + Double.toString(averageResidency) + " average residency.");
			}
			totalFaultTimes += faultTime;
			totalResidencyTimes += residencyTime;
			totalEvictTimes += evictTime;
		}
		if(totalEvictTimes == 0){
			System.out.println("\nThe total number of faults is " + Integer.toString(totalFaultTimes) + ".\n\tWith no evictions, the overall average residency is undifined.");
		}
		else{
			double totalAverageResidency = (double)totalResidencyTimes/totalEvictTimes;
			System.out.println("\nThe total number of faults is "+ Integer.toString(totalFaultTimes) + " and the overall average residency is " + Double.toString(totalAverageResidency));
		}
		return;
	}
}
