import java.util.*;
import java.io.*;

public class FrameTable_FIFO implements FrameTable{
	
	int numberOfFrames;
	Queue<Frame> frameTable = new LinkedList<Frame>();
	
	public FrameTable_FIFO(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}
	
	@Override
	public boolean hasPageFault(int pageNumber, int processId, int currentTime){
		Iterator<Frame> it = frameTable.iterator();
		boolean ret = true;
		while(it.hasNext()){
			Frame temp = it.next();
			if(temp.pageNumber == pageNumber && temp.processId == processId){
				temp.leastRecentlyUsedTime = currentTime;
				ret = false;
				break;
			}
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
