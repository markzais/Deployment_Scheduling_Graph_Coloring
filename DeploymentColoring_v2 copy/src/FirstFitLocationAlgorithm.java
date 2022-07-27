
import java.util.Random;

/**
 *
 * @author Zais
 */
public class FirstFitLocationAlgorithm {
    
    long startTime = System.currentTimeMillis();
    
    public static int [] U1;
    public static int [] U2;
    public static int [] E;
    public static int [] F;
    public static int [] dw;
    
    public static int [][] colorAssignments;
    
    
    public FirstFitLocationAlgorithm(int[][] edges, int[][] locationTracker, int numDeployments, int locations, int[][]deploymentDates, int d){
         //The number of deployments needed is equal to deployCounter-1
       
        
        int [][] colorAssignments = new int [numDeployments][3];
        int [] U1 = new int[numDeployments];
        int [] U2 = new int[numDeployments];
        int [] dw = new int[numDeployments];
        int size = edges.length;
        
        int [][] edges3 = new int[edges.length][2];
            //Make a copy of the original edges list; need to preserve original
            for (int i = 0; i < edges.length; i++){
                edges3[i][0] = edges[i][0];
                edges3[i][1] = edges[i][1];
        }
        
           
        //****Inititialization****
        for (int i = 1; i < numDeployments; i++){//set up Assignment list
            colorAssignments[i][0] = i;
            colorAssignments[i][1] = 0;
            U1[i] = 1;
            U2[i] = 0;
        } 
  
        
        int counter = 0;
        int color = 0;
        int justColored = 0;
        int justColoredLocation = 0;
         
        
        //**** Coloring Iteration *************************************//
        while (counter < numDeployments-1){
            for (int i = 1; i < numDeployments; i++){
            U1[i] = 1;
            U2[i] = 0;
            }
            //Identify first uncolored deployment;
            for (int i = 1; i < numDeployments; i++){
                if (colorAssignments[i][1] == 0){
                    color++;
                    colorAssignments[i][1] = color;
                    counter++;
                    justColored = i;
                    justColoredLocation = locationTracker[justColored][1];
                    colorAssignments[i][2] = justColoredLocation;
                    break;
                }
            }
        boolean emptyU1 = false;
               
        while (emptyU1 == false){
        for (int i = 1; i < numDeployments; i++){//set up Assignment list
  
            boolean adjacent = false;
            
            //Already colored
            if  (colorAssignments[i][1] > 0){
                
                 U1[i] = 0;
                 U2[i] = 0;
            }
            
            //Determine if i is adjacent
            if  (colorAssignments[i][1] == 0){//Not colored
                for (int j = 0; j < size; j++){
                    if ((edges3[j][0] == i)&&(edges3[j][1] == justColored)){
                    adjacent = true;
                    }   
                    if ((edges3[j][0] == justColored)&&(edges3[j][1] == i)){
                        adjacent = true;
                    }//If uncolored and adjacent update U2 and E
                    if (adjacent == true){
                        U1[i] = 0;
                        U2[i] = 1;
                    }
                    }  
                }      
        } 

        // Delete the edges with nodes already colored by changing to zeros
        for (int j = 0; j < size; j++){
            if ((edges3[j][0] == justColored)||(edges3[j][1] == justColored)){
                   edges3[j][0] = 0;
                   edges3[j][1] = 0;
            }
        }    
            
        int temp = 0;

        //Choose the first node in U1 with same location, else first node       
        for (int i = 1; i < U1.length; i++){
            if ((U1[i] > 0)&&(locationTracker[i][1]==justColoredLocation)) {
                temp = i;    
                    break;       
            }
        }
        //If there isn't node with the same location, choose the first node in U1    
        if (temp == 0){
            for (int i = 1; i < U1.length; i++){
                if (U1[i] > 0){
                temp = i;    
                    break;       
                }
            }
        }
        // If true, then U1 is empty.  Re-Initialize
        if (temp == 0){
            emptyU1 = true;
  
            //****Re-Inititialization****
                for (int i = 1; i < numDeployments; i++){
                    dw[i] = 0;
                    //Count the number of adjacent nodes to node i
                    for (int j = 0; j < size; j++){
                        if (edges3[j][0] == i){
                            dw[i]++;
                        }   
                        if (edges3[j][1] == i){
                            dw[i]++;
                        }
                    }  
                    } 
            break;
        }
         justColored = temp;
         colorAssignments[justColored][1] = color; 
         justColoredLocation = locationTracker[justColored][1];
         colorAssignments[justColored][2] = justColoredLocation;
         counter++;
        }
     }
     
     long totalTime = (System.currentTimeMillis()-startTime)/1000;  
        
     //Generate Locations per Unit statistic   
     int [][] locationCounts = new int [color][locations+1];
     for (int i = 0; i < color; i++){
         for (int j = 1; j < counter+1; j++){
             if (colorAssignments[j][1] == i+1){
                locationCounts[i][colorAssignments[j][2]] = 1;
             }             
         }
     }
     int[] unitStatistics = new int [color];
     double aggregateSum = 0;
     double aggregateStatistic = 0;
     int maxLocations = 0;
     for (int i = 0; i < color; i++){
         for (int j = 0; j < locations+1; j++){
             unitStatistics[i] = unitStatistics[i]+locationCounts[i][j];
         }
         aggregateSum = aggregateSum + unitStatistics[i];
         if (unitStatistics[i] > maxLocations){
             maxLocations = unitStatistics[i];
         }
     }
        aggregateStatistic = aggregateSum/color;
        
        //Calculate Average BOG:Dwell Ratio
        double [] ratio = new double [color+1];
        double eligibleUnits = 0;
        double sumRatios = 0;
        double bogdwellAverage = 0;
        double minRatio = 99999;
        double maxRatio = 0;
        for (int i = 1; i < color+1; i++){
 //           System.out.println("i: " + i);
            int start = -1;
            int previousEnd=0;
            int end = 0;
            int totalDwell = 0;
            double count = 0;
            int eligible = 0;
            for (int j = 0; j < numDeployments; j++){
                if (colorAssignments[j][1] == i){
                    count++;
//                    System.out.println("Count: " + count);
                    previousEnd = end;
//                    System.out.println("Previous End: " + previousEnd);
                    start = deploymentDates[j][1];
//                    System.out.println("Start: " + start);
                    end = deploymentDates[j][2];
//                    System.out.println("End: " + end);
                    if (count > 1){
                        totalDwell = totalDwell + (start-previousEnd);
//                        System.out.println("Total Dwell: " + totalDwell); 
                        eligible = 1;
                    }
                }              
            } 
            
//            System.out.println("Count: " + count);
            
            if (eligible == 1){
            ratio[i] = totalDwell/((count-1)*d);
            eligibleUnits++;
            sumRatios = sumRatios + ratio[i];
            if (ratio[i] > maxRatio){
                maxRatio = ratio[i];
            }
            if (ratio[i] < minRatio){
                minRatio = ratio[i];
            }
            }
        }
        bogdwellAverage = sumRatios/eligibleUnits;
//        printRatios(ratio);
//        System.out.println("sum of Ratios: " + sumRatios + " eligible units " + eligibleUnits); 
//        System.out.println("The average BOG:Dwell ratio is 1:" + bogdwellAverage); 
     
        
//     System.out.println("These are the assignments:"); 
//     System.out.println("(Deployment/Unit/Location)"); 
//     printAssignments(colorAssignments);
     System.out.println("**************************************************************");
     System.out.println("There are " + counter + " deployments and " + color + " colors"); 
     System.out.println("There are " + size + " incompatible edges"); 
     System.out.println("There is an average of "+ aggregateStatistic + " locations per unit");
     System.out.println("The maximum number of locations for a unit is: " + maxLocations);
     System.out.println("The average BOG:Dwell ratio is 1:" + bogdwellAverage);
     System.out.println("The min BOG:Dwell ratio is 1:" + minRatio); 
     System.out.println("The max BOG:Dwell ratio is 1:" + maxRatio);
     System.out.println("FFL Algorithm time: " + totalTime + " seconds"); 
    // printAssignments(locationCounts);
     
     LocationSwap LocationSwap = new LocationSwap(colorAssignments, edges, color, locations, deploymentDates, d);
    }
 
    
    
    
    public static void printSolution(int[] solution) {
        for (int i = 0; i < solution.length; i++) {
            System.out.print(solution[i] + " ");
       System.out.println();
        }
}     
    public static void printAssignments(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(solution[i][j] + " ");
        }
        System.out.println();
    }
}
    
}
