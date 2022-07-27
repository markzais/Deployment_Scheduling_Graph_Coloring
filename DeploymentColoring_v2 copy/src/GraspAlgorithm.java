
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Zais
 */
public class GraspAlgorithm {
    
    public static int [] U1;
    public static int [] U2;
    public static int [] E;
    public static int [] F;
    public static int [] dw;
    
    public static int [][] colorAssignments;
    
    public GraspAlgorithm(int[][] edges, int[][] locationTracker, int numDeployments, int locations, int[][]deploymentDates, int d){
         //The number of deployments needed is equal to deployCounter-1
       
        int [] solutionSet = new int[3];
        int [][] colorAssignments = new int [numDeployments][3];
        int [][] finalAssignments = new int [numDeployments][3];
        int [] U1 = new int[numDeployments];
        int [] U2 = new int[numDeployments];
        int [] dw = new int[numDeployments];
        int size = edges.length;
        int fewestColors = 999999999;
        int mainCounter = 0;
        double statistic = 99999.99;
        
        //*****************************************************
        //*****Perform 100 GRASP Iterations********************
        for (int iteration = 0; iteration < 100; iteration++){

            int [][] edges2 = new int[edges.length][2];
            //Make a copy of the original edges list; need to preserve original
            for (int i = 0; i < edges.length; i++){
                edges2[i][0] = edges[i][0];
                edges2[i][1] = edges[i][1];
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
                    if ((edges2[j][0] == i)&&(edges2[j][1] == justColored)){
                    adjacent = true;
                    }   
                    if ((edges2[j][0] == justColored)&&(edges2[j][1] == i)){
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
            if ((edges2[j][0] == justColored)||(edges2[j][1] == justColored)){
                   edges2[j][0] = 0;
                   edges2[j][1] = 0;
            }
        }    
            
        int temp = 0;
 
        double greedyLambda = Math.random(); 
        Random randomGenerator = new Random();
        //GRASP
//        //Generate an integer in the first 10% of size U1
//        int randomSelection = (int) Math.round((greedyLambda/10)*(U1.length));
        
        //Generate an integer between 1 to 3
        int randomSelection = (int) Math.ceil((greedyLambda)*(3));
        int graspCounter = 0;
        
        
        for (int i = 1; i < U1.length; i++){
            if (U1[i] > 0){
                temp = i;
                graspCounter++;
                if (graspCounter == randomSelection){
                    break;
                }
            }
        }

        if (temp == 0){
            emptyU1 = true;
  
            //****Re-Inititialization****
                for (int i = 1; i < numDeployments; i++){
                    dw[i] = 0;
                    //Count the number of adjacent nodes to node i
                    for (int j = 0; j < size; j++){
                        if (edges2[j][0] == i){
                            dw[i]++;
                        }   
                        if (edges2[j][1] == i){
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
     for (int i = 0; i < color; i++){
         for (int j = 0; j < locations+1; j++){
             unitStatistics[i] = unitStatistics[i]+locationCounts[i][j];
         }
         aggregateSum = aggregateSum + unitStatistics[i];
     }
        aggregateStatistic = aggregateSum/color; 
        
        // Minimize colors
//        if (color < fewestColors){
//            fewestColors = color;
//            finalAssignments = colorAssignments;
//            mainCounter = counter;
//            statistic = aggregateStatistic;
//        }
        
        // Minimize colors and location statistic
        if ((color <= fewestColors)&&(aggregateStatistic < statistic)){
            fewestColors = color;
            mainCounter = counter;
            statistic = aggregateStatistic;
            finalAssignments = new int[numDeployments][3];
            //Make a copy of the original edges list; need to preserve original
            for (int i = 0; i < numDeployments; i++){
                finalAssignments[i][0] = colorAssignments[i][0];
                finalAssignments[i][1] = colorAssignments[i][1];
                finalAssignments[i][2] = colorAssignments[i][2];
            }
        }
 //        System.out.println("Fewest: " + fewestColors + "low Stat: " + statistic);
    }
//     System.out.println("These are the assignments:");
//     System.out.println("(Deployment/Unit/Location)"); 
//     printAssignments(finalAssignments);
     System.out.println("**************************************************************");
     System.out.println("There are " + mainCounter + " deployments and " + fewestColors + " colors"); 
     System.out.println("There are " + size + " incompatible edges"); 
     System.out.println("There is an average of "+ statistic + " locations per unit");
     
     LocationSwap LocationSwap = new LocationSwap(finalAssignments, edges, fewestColors, locations, deploymentDates, d);
       
//      solutionSet[0] = counter;
//      solutionSet[1] = color;
//      solutionSet[2] = size;
//      return solutionSet;
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
