/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zais
 */
public class RlfAlgorithm {
    
    public static int [] U1;
    public static int [] U2;
    public static int [] E;
    public static int [] F;
    public static int [] dw;
    
    public static int [][] colorAssignments;
    
    
    public RlfAlgorithm(int[][] edges1, int[][] locationTracker, int numDeployments, int locations){
         //The number of deployments needed is equal to deployCounter-1
       
        
        int [][] colorAssignments = new int [numDeployments][3];
        int [] U1 = new int[numDeployments];
        int [] U2 = new int[numDeployments];
        int size = edges1.length;
        
        int [] dw = new int[numDeployments];//used to count adjacent edges
        int [] E = new int[numDeployments];
        int [] F = new int[numDeployments];
           
        //****Inititialization****
        for (int i = 1; i < numDeployments; i++){//set up Assignment list
            colorAssignments[i][0] = i;
            colorAssignments[i][1] = 0;
            U1[i] = 1;
            U2[i] = 0;
            dw[i] = 0;
            //Count the number of adjacent nodes to node i
            for (int j = 0; j < size; j++){
                if (edges1[j][0] == i){
                    dw[i]++;
                }   
                if (edges1[j][1] == i){
                    dw[i]++;
                }
            }  
           E[i]=dw[i];
           F[i]=dw[i];
        } 
  
        
        int counter = 0;
        int color = 0;
        int justColored = 0;
        int justColoredLocation = 0;
         
        
        //**** Coloring Iteration *************************************//
        while (counter < numDeployments-1){
       
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
                 E[i] = -1;
                 F[i] = -1;
                 U1[i] = 0;
                 U2[i] = 0;
            }
            
            //Determine if i is adjacent
            if  (colorAssignments[i][1] == 0){//Not colored
                for (int j = 0; j < size; j++){
                if ((edges1[j][0] == i)&&(edges1[j][1] == justColored)){
                    adjacent = true;
                }   
                if ((edges1[j][0] == justColored)&&(edges1[j][1] == i)){
                    adjacent = true;
                }//If uncolored and adjacent update U2 and E
                if (adjacent == true){
                    U1[i] = 0;
                    U2[i] = 1;
                    E[i] = -1;
                    F[i] = F[i]-1;
                }//If uncolored and not adjacent
                if (adjacent != true){
                    U1[i] = 1;
                    U2[i] = 0;
                }
            }  
            }      
        } 
        //Determine the number of nodes in U1 adjacent to i and colored
        for (int i = 1; i < numDeployments; i++){
            dw[i] = 0;
            int node = 0;
            if (U1[i] == 1){
                for (int j = 0; j < size; j++){
                    node = edges1[j][1];
                if ((edges1[j][0] == i) && (U2[node] == 1)){
                    dw[i]++;
                }   
                    node = edges1[j][0];
                if ((U2[node]== 1) && (edges1[j][1] == i)){
                    dw[i]++;
                }
            }  
                E[i] = E[i] - dw[i];
                //E[i] = dw[i];
                F[i] = F[i] - 1;
            }
        }

        // Delete the edges with nodes already colored by changing to zeros
        for (int j = 0; j < size; j++){
            if ((edges1[j][0] == justColored)||(edges1[j][1] == justColored)){
                   edges1[j][0] = 0;
                   edges1[j][1] = 0;
            }
        }    
            
        //Find the manximum of F(i)-E(i)
        int maxValue = -1000;
        int temp = 0;
        int eValue = -1;
        
        for (int i = 1; i < F.length; i++){
            if ((F[i]-E[i] > maxValue)&&(E[i]>=0)){
                maxValue = F[i]-E[i];
                temp = i;
                eValue = E[i];
                
            }//If a tie, choose one with smalled E[i]
            if (F[i]-E[i] == maxValue){
                if ((E[i] < eValue)&&(eValue > 0)&&(E[i]>=0)){
                maxValue = F[i]-E[i];
                temp = i;
                eValue = E[i];
                }
                if ((eValue < 0)&&(E[i]>=0)){
                maxValue = F[i]-E[i];
                temp = i;
                eValue = E[i];
                }
            }
        }

        //All E[]are negative which means U1 is empty
        if (eValue < 0){
            emptyU1 = true;
            // ****Re-Inititialize****       
            for (int i = 1; i < numDeployments; i++){ 
                F[i]=F[i]+1;
                E[i]=F[i];
            } 
            //****Re-Inititialization****
                for (int i = 1; i < numDeployments; i++){
                    dw[i] = 0;
                    //Count the number of adjacent nodes to node i
                    for (int j = 0; j < size; j++){
                        if (edges1[j][0] == i){
                            dw[i]++;
                        }   
                        if (edges1[j][1] == i){
                            dw[i]++;
                        }
                    }  
                   E[i]=dw[i];
                   F[i]=dw[i];
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
        
        
//     System.out.println("These are the assignments:"); 
//     System.out.println("(Deployment/Unit/Location)"); 
//     printAssignments(colorAssignments);
     System.out.println("**************************************************************");
     System.out.println("There are " + counter + " deployments and " + color + " colors"); 
     System.out.println("There are " + size + " incompatible edges"); 
     System.out.println("There is an average of "+ aggregateStatistic + " locations per unit");
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
