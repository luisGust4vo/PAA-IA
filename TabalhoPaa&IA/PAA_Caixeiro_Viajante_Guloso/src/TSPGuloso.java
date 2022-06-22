import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TSPGuloso {

    public static void main(String[] args) throws IOException {

        String caminho = "src/CidadesTSP.txt";

        int[][] tspMatrix = readFile(caminho);

        Instant startTime = Instant.now();

        findMinRoute(tspMatrix);

        Instant endTime = Instant.now();
        Duration totalTime = Duration.between(startTime, endTime);

        System.out.println("\n"+totalTime);
    }

    public static int[][] createMatrix(int n){
        int[][] matrix = new int[n][n];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                matrix[i][j]=-1;
            }
        }
        return matrix;
    }
    public static void printMatrix(int[][] matrix){
        for (int l = 0; l < matrix.length; l++)  {
            for (int c = 0; c < matrix[0].length; c++)     {
                System.out.print(matrix[l][c] + " ");
            }
            System.out.println(" ");
        }
    }
    public static int[][] readFile(String path){
        String linha = "";
        Reader r = null;
        try {
            r = new FileReader(path);
            int c;
            while ((c = r.read()) != -1) {
                linha +=(char) c;
            }
            String[] result = linha.split("\n");

            int tam = Integer.parseInt(result[0].trim());

            int[][] matrix;
            matrix  = createMatrix(tam);

            for(int i = 1; i< result.length;i++){
                String columns[] = result[i].split(" ");

                for(int j=0;j<columns.length;j++){
                    matrix[i-1][j] = Integer.valueOf(columns[j].trim());
                }
            }
            //printMatrix(matrix);
            return matrix;
        } catch (FileNotFoundException ex) {
            System.out.println(path + " nao existe.");
        } catch (IOException ex) {
            System.out.println("Erro de leitura de arquivo.");
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException ex) {
                System.out.println("Erro ao fechar o arquivo " + path);
            }
        }
        return null;
    }
    static void findMinRoute(int[][] tsp) throws IOException {
        int sum = 0;
        int counter = 0;
        int j = 0, i = 0;
        int min = Integer.MAX_VALUE;
        List<Integer> visitedRouteList
                = new ArrayList<>();

        visitedRouteList.add(0);
        int[] route = new int[tsp.length];


        while (i < tsp.length && j < tsp[i].length) {

            if (counter >= tsp[i].length - 1) {
                break;
            }
            //Se o caminho nao foi visitado e o custo for menor, atualiza o custo
            if (j != i && !(visitedRouteList.contains(j))) {
                if (tsp[i][j] < min) {
                    min = tsp[i][j];
                    route[counter] = j + 1;
                }
            }
            j++;
            // verifica todos os caminhos a partir do indice i
            if (j == tsp[i].length) {
                sum += min;
                min = Integer.MAX_VALUE;
                visitedRouteList.add(route[counter] - 1);
                j = 0;
                i = route[counter] - 1;
                counter++;
            }
        }
        // Atualiza a cidade final no array
        i = route[counter - 1] - 1;

        for (j = 0; j < tsp.length; j++) {

            if ((i != j) && tsp[i][j] < min) {
                min = tsp[i][j];
                route[counter] = j + 1;
            }
        }
        sum += min;
        System.out.print("Custo minimo: ");
        System.out.println(sum);

        writeFile(sum,route);
    }
    public static void writeFile(int cost,int[] route) throws IOException {
        String routeString = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddHH-mm-ss");

        OutputStream os = new FileOutputStream("resultadoTSPGuloso"+dtf.format(LocalDateTime.now())+".txt");
        Writer wr = new OutputStreamWriter(os); // criação de um escritor
        BufferedWriter br = new BufferedWriter(wr); // adiciono a um escritor de buffer

        br.write(String.valueOf(cost));
        br.newLine();
        for (int i=0;i<route.length;i++){
            routeString+= route[i] + " ";
        }

        br.write(routeString);
        br.close();


    }
}


