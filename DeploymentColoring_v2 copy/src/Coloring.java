import java.io.File;
import java.io.FileNotFoundException;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
/**
 *
 * @author Zais
 */
public class Coloring {
    
    
//    public static int d1 = 2; //deployment length
//    public static int d2 = 2; //deployment length
//    public static int policyChange = 5; //deployment length
//    public static int T = 10; //time horizon
//    public static int L = 3; //number of locations
//    public static int r = 0; //number of deployment schedule rows
//    public static int Max = 20; //Used to create array big enough to contain all deployments
//    public static int dw = 1; //Dwell multiplier for ratio of minimum dwell time after deployment time
    
   
    public static int d1 = 15; //deployment length
    public static int d2 = 15; //deployment length
    public static int policyChange = 45; //deployment length
    public static int T = 86; //time horizon
    public static int L = 8; //number of locations
    public static int r = 0; //number of deployment schedule rows
    public static int Max = 1000; //used to create array big enough to contain all deployments
    public static double dw = 1; //dwell multiplier for ratio of minimum dwell time after deployment time
    
    
public static void main(String[] args) throws FileNotFoundException {

       
        //  Import deployment demand data     
        Scanner scanner1 = new Scanner(new File("/Users/Zais/Dropbox/PhD Research/Deployment Metaheuristics/"
                + "DeploymentColoring_v2/coloring_data/unit_demand_location_2"));
//        Scanner scanner1 = new Scanner(new File("/Users/Zais/Dropbox/PhD Research/Deployment Metaheuristics/"
//                + "DeploymentColoring_v2/coloring_data/testing"));
                int [][] demand = new int [L][T];//specify array length. MZ
               // int n1 = 0;
                while(scanner1.hasNextInt()){
                    for (int m = 0; m < L; m++){
                    for (int n = 0; n < T; n++)
                        demand[m][n] = scanner1.nextInt();
                    }}
                //Find the maximum value in each demand row;
                int[] maxDemands = new int [L];
                for (int i = 0; i < L; i++){
                    maxDemands[i] = 0;
                    for (int j = 0; j < T; j++){
                        if (demand[i][j] > maxDemands[i]){
                            maxDemands[i] = demand[i][j];
                        }
                    }
                }
                System.out.println("This is the demand scenario (each row is a location):");
                printSolution(demand);
                //System.out.println("****************************************************************");
            
            //Make the number of schedule rows equal the hgihest of the location max demands
            int maxValue = 0;    
            for (int i = 0; i < L; i++){    
                if (maxDemands[i] > maxValue ){
                    maxValue = maxDemands[i];       
                }
            }
            r = maxValue;
       
            int[][][] initialSolution = new int[r][T][L];
            
                for (int i = 0; i < r; i++) {
                    for (int j = 1; j < T;j++){
                        for (int k = 0; k < L; k++){
                        initialSolution[i][j][k] = 0;
                        }
                    }   
                }

                int[][][] currSolution = initialSolution;
                //printSolution2(currSolution);
                //System.out.println("****************************************************************");
        
        int deployCounter = 1;//Used to count the number of deployments needed.
        int nextBeginRow = 0;//The next row to start copying location schedule into current schedule.
        int[][] locationTracker = new int [Max][2];      
       
        //Build a schedule one column at a time
        for  (int month = 0; month < T; month++) {
            
            for (int loc = 0; loc < L; loc++){
                                           
            for (int j = 0; j < maxDemands[loc]; j++){
            
            //for (int loc = 0; loc < L; loc++){
                
                // currCount counts the number units deployed in a month
                int[] currCount = Coloring.columnCount(currSolution, loc);
                 
                
                //Create deployment schedule
                if ((currCount[month] < demand[loc][month])&&(currSolution[j][month][loc] < 1)){
                    if(month < policyChange){
                        for (int i = 0; i < d1; i++){
                        currSolution[j][month+i][loc]=deployCounter;
                        
                    }}
                    if((month >= policyChange)&&(month < T-d2)){
                        for (int i = 0; i < d2; i++){
                        currSolution[j][month+i][loc]=deployCounter;
                    }}
                    if(month >= T-d2){
                        for (int i = 0; i < T-month; i++){
                        currSolution[j][month+i][loc]=deployCounter;
                    }}
                    //Create a separate array to track deployment locations 
                    locationTracker[deployCounter][0] = deployCounter;
                    locationTracker[deployCounter][1] = loc;
                    deployCounter ++;
                }
              
            }
            
        }
        }
        // Create a table of the start and end dates for each deployment
        int deployment = 0;
        int[][] deploymentDates = new int[deployCounter][3];  
        
         for (int i = 0; i < r; i++) {
             for (int k = 0; k < L; k++) {
             for (int j = 0; j < T; j++) {
                 
                     if ((currSolution[i][j][k] != deployment)&&(currSolution[i][j][k] > 0)){
                         deploymentDates[deployment][2] = j;
                         deployment = currSolution[i][j][k];
                         deploymentDates[deployment][0] = deployment;
                         deploymentDates[deployment][1] = j;
                     }
                      if ((currSolution[i][j][k] != deployment)&&(currSolution[i][j][k] == 0)){
                         deploymentDates[deployment][2] = j;
                         deployment = currSolution[i][j][k];
                     }
                      
                }
             }        
         }
        //printDeployDates(deploymentDates); 
        
        //Shrink the array by eliminating unused cells
        int[][] locationList = Arrays.copyOfRange(locationTracker, 0, deployCounter);
        
//        }

        int[][] List = Coloring.IncompatibleList(deployCounter, currSolution, d1, d2);
        
   sort2(List);//Sort by column number 2
   sort1(List);//Sort by column number 1

   int cut = Coloring.findFirstNum(List);//Find the first row without zeros in the sorted list.
   int[][] shortList = Arrays.copyOfRange(List, cut, List.length);//Copy the array elements that aren't zero.
   
//   printList(shortList);
//   AdjacencyMatrix AdjacencyMatrix = new AdjacencyMatrix(shortList, deployCounter);
   
//   System.out.println("****************************************************************");
//   long startTime1 = System.currentTimeMillis();
//   RlfAlgorithm rlfAlgorithm = new RlfAlgorithm(shortList, locationTracker, deployCounter, L);
//   long totalTime1 = (System.currentTimeMillis()-startTime1)/1000;
//   System.out.println("RLF Algorithm time: " + totalTime1 + " seconds");
//   
//   List = Coloring.IncompatibleList(deployCounter, currSolution, d1, d2);
//   sort2(List);//Sort by column number 2
//   sort1(List);//Sort by column number 1
//   cut = Coloring.findFirstNum(List);//Find the first row without zeros in the sorted list.
//   shortList = Arrays.copyOfRange(List, cut, List.length);

//   System.out.println("****************************************************************");
//   long startTime2 = System.currentTimeMillis();
//   GraspAlgorithm GraspAlgorithm = new GraspAlgorithm(shortList, locationTracker, deployCounter, L);
//   long totalTime2 = (System.currentTimeMillis()-startTime2)/1000;
//   System.out.println("GRASP Algorithm time: " + totalTime2 + " seconds");
//
//   List = Coloring.IncompatibleList(deployCounter, currSolution, d1, d2);
//   sort2(List);//Sort by column number 2
//   sort1(List);//Sort by column number 1
//   cut = Coloring.findFirstNum(List);//Find the first row without zeros in the sorted list.
//   shortList = Arrays.copyOfRange(List, cut, List.length);
   
   System.out.println("****************************************************************");
   
   FirstFitAlgorithm lessGreedyAlgorithm = new FirstFitAlgorithm(shortList, locationTracker, deployCounter, L, deploymentDates, d1);
   
   
   List = Coloring.IncompatibleList(deployCounter, currSolution, d1, d2);
   sort2(List);//Sort by column number 2
   sort1(List);//Sort by column number 1
   cut = Coloring.findFirstNum(List);//Find the first row without zeros in the sorted list.
   shortList = Arrays.copyOfRange(List, cut, List.length);
   
   System.out.println("****************************************************************");

   FirstFitLocationAlgorithm minUnitsLocationAlgorithm = new FirstFitLocationAlgorithm(shortList, locationTracker, deployCounter, L, deploymentDates, d1);

   
   

 
}

public static int[][] IncompatibleList(int numDeploy, int [][][] currSolution, int deploylength1, int deployLength2){
          
         int [][] incompatibleList = new int[numDeploy*numDeploy][2];
         //System.out.print("Length: "+ incompatibleList.length);
    
         int tracker = 0;
         int listTracker = 0;
         int deploy1 = 0;
         
         for (int i = 0; i < currSolution.length; i++) {
            for (int j = 0; j < policyChange; j++) {
                for (int k = 0; k < L; k++) {
                
              if (currSolution[i][j][k] > 0){
                   
                   for (int ii=0; ii < r; ii++ ){
                       int deploy2 = 0;
                       for (int jj = 0; jj < T; jj++ ){
                           for (int kk = 0; kk < L; kk++ ){
                           
                           if ((currSolution[i][j][k] < currSolution[ii][jj][kk])&&(deploy2 < currSolution[ii][jj][kk])&&(jj-j<(dw*deploylength1)+1)){
                               deploy1 = currSolution[i][j][k];
                               //System.out.println("deploy1: "+ deploy1);
                               deploy2 = currSolution[ii][jj][kk];
                               //System.out.println("deploy2: "+ deploy2);
                               
                               boolean isDifferent = compare(incompatibleList, deploy1, deploy2);
                           
                               if (isDifferent == true){
                               incompatibleList[listTracker][0] = deploy1;
                               incompatibleList[listTracker][1] = deploy2;
                               listTracker ++;
                               //System.out.println("listTracker: "+ listTracker);
                               }
                           }
                       }
                       }
                    }
                }
            }}
         }      
         for (int i = 0; i < r; i++) {
            for (int j = policyChange; j < T; j++) {
                for (int k = 0; k < L; k++) {
                
              if (currSolution[i][j][k] > 0){
                   
                   for (int ii = 0; ii < r; ii++ ){
                       int deploy2 = 0;
                       for (int jj = 0; jj < T; jj++ ){
                           for (int kk = 0; kk < L; kk++ ){
                           
                           if ((currSolution[i][j][k] < currSolution[ii][jj][kk])&&(deploy2 < currSolution[ii][jj][kk])&&(jj-j<dw*(deployLength2)+1)){
                               deploy1 = currSolution[i][j][k];
                               //System.out.println("deploy1: "+ deploy1);
                               deploy2 = currSolution[ii][jj][kk];
                               //System.out.println("deploy2: "+ deploy2);
                               
                               boolean isDifferent = compare(incompatibleList, deploy1, deploy2);
                           
                               if (isDifferent == true){
                               incompatibleList[listTracker][0] = deploy1;
                               incompatibleList[listTracker][1] = deploy2;
                               listTracker ++;
                               //System.out.println("listTracker: "+ listTracker);
                               }
                           }
                       }}
                    }
                   //tracker = deploy1;
                }
            }}
         }      
            
return incompatibleList;
}


public static int[] columnCount(int [][][] array, int location){
int size = array[0].length; // Replace it with the size of maximum length inner array
int temp[] = new int[size];

for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array[i].length; j++){
        if (array[i][j][location] != 0){
        temp[j] ++;}
            }}
//System.out.println(Arrays.toString(temp));
return temp;
}

public static void printSolution(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < T; j++) {
            System.out.print(solution[i][j] + " ");
        }
        System.out.println();
    }
}

public static void printDeployDates(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(solution[i][j] + " ");
        }
        System.out.println();
    }
}

public static void printSolution2(int[][][] solution) {
    for (int i = 0; i < solution.length; i++) {
            for (int k = 0; k < L; k++) {
            for (int j = 0; j < T; j++) {
            System.out.print(solution[i][j][k] + " ");
        }
        System.out.println();
            }}
    
}
public static void printDemand(int[] demand) {
        for (int i = 0; i < demand.length; i++) {
            System.out.print(demand[i] + " ");
        System.out.println();
    
}
}

     public static void printList(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < 2; j++) {
            System.out.print(solution[i][j] + " ");
        }
        System.out.println();
    }
}
public static boolean compare (int[][] solution1, int solution2, int solution3) {
       
        boolean isDifferent = true;
        for (int i = 0; i < solution1.length; i++) {

        if ((solution1[i][0] == solution2)&&(solution1[i][1] == solution3)){
            isDifferent = false;
            break;
            }
        }
        return isDifferent;
    }  
// Sort the array by column 1
public static void sort1(int[][] theArray)
{
Arrays.sort(theArray, new Comparator<int[]>() 
{
    @Override
    public int compare(int[] int1, int[] int2) {
        Integer numOfKeys1 = int1[0];
        Integer numOfKeys2 = int2[0];
        return numOfKeys1.compareTo(numOfKeys2);
    }
});
}
// Sort the array by column 2
public static void sort2(int[][] theArray)
{
Arrays.sort(theArray, new Comparator<int[]>() 
{
    @Override
    public int compare(int[] int1, int[] int2) {
        Integer numOfKeys1 = int1[1];
        Integer numOfKeys2 = int2[1];
        return numOfKeys1.compareTo(numOfKeys2);
    }
});
}

public static int findFirstNum(int[][] array)
{
    int marker = 0;
    for(int i = 0; i < array.length; i++)
    {
        if (array[i][0]>0){
            marker = i;
            break;
        }
    }
return marker;
}

}