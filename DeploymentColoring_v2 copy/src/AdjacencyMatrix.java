

/**
 *
 * @author Zais
 */
public class AdjacencyMatrix {
    
    public AdjacencyMatrix (int[][] edges, int nodes){
        
        System.out.println("nodes:" + nodes); 
        
        int [][] A = new int[nodes-1][nodes-1];
        
        for (int i = 0; i < nodes-1; i++){
             for (int j = 0; j < nodes-1; j++){
            A[i][j] = 0;
             }
        }
        
        for (int i = 0; i < edges.length; i++){
            A[edges[i][0]-1][edges[i][1]-1]=1;
            A[edges[i][1]-1][edges[i][0]-1]=1;
        }
         printMatrix(A);
    }
   
    
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
            System.out.print(matrix[i][j] + " ");
        }
        System.out.println();
    }
}
    
}
