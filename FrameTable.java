import java.util.*;
import java.io.*;

public interface FrameTable{
	boolean hasPageFault(int pageNumber, int processNumber, int currentTime);
	void replace(Process[] processes, int pageNumber, int processNumber, int currentTime);
}