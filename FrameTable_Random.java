import java.util.*;
import java.io.*;

public class FrameTable_Random implements FrameTable{

	int numberOfFrames;
	Scanner random;
	Frame[] frameTable;

	public FrameTable_Random(int numberOfFrames, Scanner random){
		this.numberOfFrames = numberOfFrames;
		this.random = random;
		this.frameTable = new Frame[numberOfFrames];
	}

	@Override
	public boolean hasPageFault(int pageNumber, int processId, int currentTime){
		boolean ret = true;
		for(int i = 0; i < numberOfFrames; i++){
			if(frameTable[i] != null){
				if(frameTable[i].pageNumber == pageNumber && frameTable[i].processId == processId){
					frameTable[i].leastRecentlyUsedTime = currentTime;
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	@Override
	public void replace(Process[] processes, int pageNumber, int processId, int currentTime){
		for(int i = (numberOfFrames - 1); i >= 0; i--){
			if(frameTable[i] == null){
				frameTable[i] = new Frame(pageNumber, processId, currentTime, currentTime);
				return;
			}
		}
		int randomNumber = random.nextInt();
		int frameEvicted = randomNumber%numberOfFrames;
		int evictedProcessNumber = frameTable[frameEvicted].processId;
		Process evictedProcess = processes[evictedProcessNumber - 1];
		evictedProcess.increaseEvictTime();
		int loadTime = frameTable[frameEvicted].loadTime;
		int residencyTime = currentTime - loadTime;
		evictedProcess.addResidencyTime(residencyTime);
		frameTable[frameEvicted] = new Frame(pageNumber, processId, currentTime, currentTime);
		return;
	}	
}