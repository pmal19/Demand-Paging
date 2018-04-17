import java.util.*;
import java.io.*;

public class DemandPaging{

	static int machineSize;
	static int pageSize;
	static int processSize;
	static int jobMix;
	static int numberOfReferences;
	static String algorithm;
	static FrameTable frame = null;
	static Scanner random = null;
	static int numberOfFrames;

	public DemandPaging(String[] args){
		machineSize = Integer.parseInt(args[0]);
		pageSize = Integer.parseInt(args[1]);
		processSize = Integer.parseInt(args[2]);
		jobMix = Integer.parseInt(args[3]);
		numberOfReferences = Integer.parseInt(args[4]);
		algorithm = args[5];
		numberOfFrames = machineSize/pageSize;
		try{
			String fileName = "random-numbers.txt";
			random = new Scanner(new File(fileName));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		if(algorithm.equals("lru")){
			frame = new FrameTable_LRU(numberOfFrames);
		}
		else if(algorithm.equals("fifo")){
			frame = new FrameTable_FIFO(numberOfFrames);
		}
		else if(algorithm.equals("random")){
			frame = new FrameTable_Random(numberOfFrames,random);
		}
		else{
			System.out.println("ERROR INPUT FORMAT!");
		}
    }

	public static void main(String[] args){
        if(args.length > 7 || args.length < 1)
            throw new IllegalArgumentException("Incorrect number of parameters.");
        DemandPaging paging = new DemandPaging(args);
        paging.run();
    }

    private void run(){
    	Driver driver = new Driver(machineSize, pageSize, processSize, jobMix, numberOfReferences, algorithm, random, frame);
		driver.run();
		driver.print();
        System.out.println("---------------");
    }
}