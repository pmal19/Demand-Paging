import java.util.*;
import java.io.*;

public class Frame{
	
	int pageNumber;
	int processId;
	int loadTime;
	int leastRecentlyUsedTime;
	
	public Frame(int pageNumber, int processId, int loadTime, int leastRecentlyUsedTime){
		this.pageNumber = pageNumber;
		this.processId = processId;
		this.loadTime = loadTime;
		this.leastRecentlyUsedTime = leastRecentlyUsedTime;
	}
	public static final Comparator<Frame> LeastRecentlyUsedTimeComparator = new Comparator<Frame>(){
        public int compare(Frame f1, Frame f2){
            int comp = f1.leastRecentlyUsedTime - f2.leastRecentlyUsedTime;
            if(comp == 0){
                return f1.processId - f2.processId;
            }
            else{
                return comp;
            }
        }
    };

}
