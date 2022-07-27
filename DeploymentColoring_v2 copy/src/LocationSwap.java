

/**
 *
 * @author Zais
 */
public class LocationSwap {
    
    long startTime = System.currentTimeMillis();
    
    int stakeUnit = 0;
    int candidate1 = 0;
    int candidate2 = 0;
    int color1 = 0;
    int color2 = 0;
    int index = 0;
    int iteration = 0;
    
    public LocationSwap (int[][]colorAssignments, int[][]incompatibleEdges, int colors, int locations, int[][]deploymentDates, int d){ 
 //  printList(incompatibleEdges);   
        for (int run = 0; run < 1000; run++){  
            boolean terminator = true;
        for (int i = 1; i < colorAssignments.length; i++){     
            int location1 = 9999;
            int location2 = 9999;
            //stakeUnit = i;
            stakeUnit = colorAssignments[i][1];
            index = colorAssignments[i][0];
            //color1 = colorAssignments[stakeUnit][1];
             color1 = colorAssignments[i][1];
           // int color2 = 0;
//            //Find the first assignment for unit i and record the location
//            for (int ii = 1; ii < colorAssignments.length; ii++){
//                if (colorAssignments[ii][1] == i){
//                    location1 = colorAssignments[ii][2];
//                    break;
//                }
//            }
            location1 = colorAssignments[i][2];
            int candidate1 = 0;
         
//System.out.println("Stake Unit:" + stakeUnit + " Index: " + index); 
            
            //Find assignments for unit i at different locations
              for (int ii = 1; ii < colorAssignments.length; ii++){
//            for (int ii = stakeUnit+1; ii < colorAssignments.length; ii++){
                //if ((colorAssignments[ii][1] == i)&&(colorAssignments[ii][2] != location1)){
                if ((colorAssignments[ii][1] == stakeUnit)&&(colorAssignments[ii][2] != location1)){    
                    candidate1 = colorAssignments[ii][0];
//System.out.println("Candidate 1:" + candidate1); 
                    
                    //Look at assignments for different units at the desired location
                    for (int j = candidate1+1; j < colorAssignments.length; j++){
//                    for (int j = 1; j < colorAssignments.length; j++){
                        if ((colorAssignments[j][1] != i)&&(colorAssignments[j][2] == location1)&&(colorAssignments[j][1] > stakeUnit)){
                            candidate2 = colorAssignments[j][0];
//System.out.println("Candidate 2:" + candidate2); 
                            color2 = colorAssignments[j][1];
                            location2 = colorAssignments[j][2];
                            
                            //See if the candidate is also 
                            
                            //Determine if a candidate is icompatible
                            boolean compatible = true; 
                            for (int jj = 0; jj < incompatibleEdges.length; jj++){
//                                if ((incompatibleEdges[jj][0]==stakeUnit)&&(incompatibleEdges[jj][1]==candidate2)&&(candidate1 != candidate2)){
//                                    compatible = false;
//                                    break;
//                                }
//                                if ((incompatibleEdges[jj][0]==candidate2)&&(incompatibleEdges[jj][1]==stakeUnit)&&(candidate1 != candidate2)){
//                                    compatible = false;
//                                    break;
//                                }
                                 if ((incompatibleEdges[jj][0]==index)&&(incompatibleEdges[jj][1]==candidate2)&&(candidate1 != candidate2)){
                                    compatible = false;
                                    break;
                                }
                                if ((incompatibleEdges[jj][0]==candidate2)&&(incompatibleEdges[jj][1]==index)&&(candidate1 != candidate2)){
                                    compatible = false;
                                    break;
                                }
                                
//                                if ((incompatibleEdges[jj][0]==index)&&(incompatibleEdges[jj][1]==color2)){
//                                    compatible = false;
//                                    break;
//                                }
//                                if ((incompatibleEdges[jj][0]==color2)&&(incompatibleEdges[jj][1]==index)){
//                                    compatible = false;
//                                    break;
//                                }
                                
                            }
                            //Check for incompatibility with previous swaps
                            for (int k = 1; k < colorAssignments.length; k++){
                                if (colorAssignments[k][1] == color2){
                                    for (int kk = 0; kk < incompatibleEdges.length; kk++){
                                        if  ((incompatibleEdges[kk][0]==candidate1)&&(incompatibleEdges[kk][1]==k)&&(k!=candidate2)){
                                             compatible = false;
                                             break;
                                        }
                                        if  ((incompatibleEdges[kk][0]==k)&&(incompatibleEdges[kk][1]==candidate1)&&(k!=candidate2)){
                                             compatible = false;
                                             break;
                                        }
                                    }
                                }
                            }
                            //Check for incompatibility with previous swaps
                            for (int k = 1; k < colorAssignments.length; k++){
                                if (colorAssignments[k][1] == color1){
                                    for (int kk = 0; kk < incompatibleEdges.length; kk++){
                                        if  ((incompatibleEdges[kk][0]==candidate2)&&(incompatibleEdges[kk][1]==k)&&(k!=candidate1)){
                                             compatible = false;
                                             break;
                                        }
                                        if  ((incompatibleEdges[kk][0]==k)&&(incompatibleEdges[kk][1]==candidate2)&&(k!=candidate1)){
                                             compatible = false;
                                             break;
                                        }
                                    }
                                }
                            }
                            
                            
                            
//System.out.println("Compatible:" + compatible); 
                                    
                                    //Swap colors & locations
                                    if (compatible == true){
                                    colorAssignments[candidate1][1] = color2;
                                    colorAssignments[candidate2][1] = color1;
                                    terminator = false;
//System.out.println("Swap:" + color1 + " and " + color2); 
                                    break;
                                }
                            
                        }
                    }
                }
            }
            
            
            
            
        }   iteration++;
            if (terminator == true){
                break;
           }
       }
        
     long totalTime = (System.currentTimeMillis()-startTime)/1000;     
        
     //Generate Locations per Unit statistic   
     int [][] locationCounts = new int [colors][locations+1];
     for (int i = 0; i < colors; i++){
         for (int j = 1; j < colorAssignments.length; j++){
             if (colorAssignments[j][1] == i+1){
                locationCounts[i][colorAssignments[j][2]] = 1;
             }             
         }
     }
     int[] unitStatistics = new int [colors];
     double aggregateSum = 0;
     double aggregateStatistic = 0;
     int maxLocations = 0;
     for (int i = 0; i < colors; i++){
         for (int j = 0; j < locations+1; j++){
             unitStatistics[i] = unitStatistics[i]+locationCounts[i][j];
         }
         aggregateSum = aggregateSum + unitStatistics[i];
         if (unitStatistics[i] > maxLocations){
             maxLocations = unitStatistics[i];
         }
     }
        aggregateStatistic = aggregateSum/colors;   
        
        //Calculate Average BOG:Dwell Ratio
        double [] ratio = new double [colors+1];
        double eligibleUnits = 0;
        double sumRatios = 0;
        double bogdwellAverage = 0;
        int minIndex = 0;
        double minRatio = 99999;
        double maxRatio = 0;
        for (int i = 1; i < colors+1; i++){
 //           System.out.println("i: " + i);
            int start = -1;
            int previousEnd=0;
            int end = 0;
            int totalDwell = 0;
            double count = 0;
            int eligible = 0;
            for (int j = 0; j < colorAssignments.length; j++){
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
                minIndex = i;
                minRatio = ratio[i];
            }
            }
        }
        bogdwellAverage = sumRatios/eligibleUnits;
//        printRatios(ratio);
//        System.out.println("sum of Ratios: " + sumRatios + " eligible units " + eligibleUnits);         
   
//     System.out.println("These are the post-swap assignments:"); 
//     System.out.println("(Deployment/Unit/Location)"); 
//     printAssignments(colorAssignments);
     System.out.println("**************************************************************");
     System.out.println("Post-Swap ("+iteration+" iterations): There is an average of "+ aggregateStatistic + " locations per unit"); 
     System.out.println("The maximum number of locations for a unit is: " + maxLocations);
     System.out.println("The average BOG:Dwell ratio is 1:" + bogdwellAverage);
     System.out.println("The min BOG:Dwell ratio is 1:" + minRatio + "for color" + minIndex); 
     System.out.println("The max BOG:Dwell ratio is 1:" + maxRatio);     
     System.out.println("Swapping time: " + totalTime + " seconds");
    }
     
    public static void printAssignments(int[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < 3; j++) {
            System.out.print(solution[i][j] + " ");
            }
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
    
}
