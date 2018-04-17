import java.util.*;
import java.io.*;

public class Process{

	int processSize;
	int nextWord;
	int pageFaultTimes;
	int evictTimes;
	int residencyTime;
	int processId;
	
	public Process(int processSize, int processId){
		this.processSize = processSize;
		this.processId = processId;
		this.nextWord = (111*processId)%processSize;
		this.pageFaultTimes = 0;
		this.residencyTime = 0;
		this.evictTimes = 0;
	}
	
	public void nextReference(double A, double B, double C, Scanner random){
		int randomNum = random.nextInt();
		double y = randomNum/(Integer.MAX_VALUE + 1d);
		if(y < A){
			nextWord = (nextWord + 1)%processSize;
		}
		else if(y < A + B){
			nextWord = (nextWord - 5 + processSize)%processSize;
		}
		else if(y < A + B + C){
			nextWord = (nextWord + 4)%processSize;
		}
		else{
			int randomRef = random.nextInt()%processSize;
			nextWord = randomRef;
		}
	}

	public void addResidencyTime(int time){
		residencyTime += time;
	}

	public int getNextWord(){
		return nextWord;
	}

	public void increaseFaultTime(){
		pageFaultTimes++;
	}

	public void increaseEvictTime(){
		evictTimes++;
	}
}
