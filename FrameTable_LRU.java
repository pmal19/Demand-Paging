import java.util.*;
import java.io.*;

public class FrameTable_LRU implements FrameTable{

	int numberOfFrames;
	PriorityQueue<Frame> frameTable = new PriorityQueue<Frame>(10000,Frame.LeastRecentlyUsedTimeComparator);
	
	public FrameTable_LRU(int numberOfFrames){
		this.numberOfFrames = numberOfFrames;
	}

	@Override
	public boolean hasPageFault(int pageNumber, int processId, int currentTime){
		boolean ret = true;
		LinkedList<Frame> tempList = new LinkedList<Frame>();
		while(frameTable.size() > 0){
			Frame temp = frameTable.poll();
			if(temp.pageNumber == pageNumber && temp.processId == processId){
				temp.leastRecentlyUsedTime = currentTime;
				ret = false;
			}
			tempList.add(temp);
			if(!ret){
				break;
			}
		}
		while(tempList.size() > 0){
			Frame temp = tempList.poll();
			frameTable.add(temp);
		}
		return ret;
	}

	@Override
	public void replace(Process[] processes, int pageNumber, int processId, int currentTime){
		if(numberOfFrames == frameTable.size()){
			Frame evictedFrame = frameTable.poll();
			int evictedProcessNumber = evictedFrame.processId;
			Process evictedProcess = processes[evictedProcessNumber - 1];
			evictedProcess.increaseEvictTime();	
			int loadTime = evictedFrame.loadTime;
			int residencyTime = currentTime - loadTime;
			evictedProcess.addResidencyTime(residencyTime);
		} 
		Frame newFrame = new Frame(pageNumber, processId, currentTime, currentTime);
		frameTable.add(newFrame);
		return;
	}
}