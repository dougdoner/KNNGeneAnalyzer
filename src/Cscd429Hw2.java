import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cscd429Hw2 {
    public static void main(String[] args) throws IOException {
        InputStream trainingGenesStream = Cscd429Hw2.class.getResourceAsStream("/Genes_relation.data");
        InputStream testGenesStream = Cscd429Hw2.class.getResourceAsStream("/Genes_relation.test");
        InputStream keyStream = Cscd429Hw2.class.getResourceAsStream("/keys.txt");
        assert trainingGenesStream != null;
        assert testGenesStream != null;
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(trainingGenesStream));
        ArrayList<Gene> trainingGeneList = new ArrayList<>();
        ArrayList<Gene> testGeneList = new ArrayList<>();
        Map<String, String> geneKeyMap = new HashMap<>();
        String fileLine;

        fileReader.readLine();

        while ((fileLine = fileReader.readLine()) != null) {
            trainingGeneList.add(new Gene(fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)));
        }

        fileReader.close();
        fileReader = new BufferedReader(new InputStreamReader(testGenesStream));
        fileReader.readLine();

        while ((fileLine = fileReader.readLine()) != null) {
            testGeneList.add(new Gene(fileLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)));
        }
        fileReader.close();
        fileReader = new BufferedReader(new InputStreamReader(keyStream));
        fileReader.readLine();
        while ((fileLine = fileReader.readLine()) != null) {
            String[] line = fileLine.split(",");
            geneKeyMap.put(line[0], line[1]);
        }

        for (Gene testGene : testGeneList) {
            List<Gene> neighbors = new ArrayList<>();
            int smallestNeighborDistanceIndex = 0;

            for (Gene trainingGene : trainingGeneList) {
                double distance = testGene.computeDistance(trainingGene);

                if (neighbors.size() < 3) {
                    neighbors.add(trainingGene);
                    smallestNeighborDistanceIndex = neighbors.indexOf(trainingGene);
                } else if (distance > testGene.computeDistance(neighbors.get(smallestNeighborDistanceIndex))) {
                    neighbors.remove(smallestNeighborDistanceIndex);
                    neighbors.add(trainingGene);
                }

                for (Gene g : neighbors) {
                    if (testGene.computeDistance(g) < testGene.computeDistance(neighbors.get(smallestNeighborDistanceIndex))) {
                        smallestNeighborDistanceIndex = neighbors.indexOf(g);
                    }
                }
            }

            testGene.decideLocalization(neighbors);
        }

        double truePositiveCount = 0;
        for (Gene g : testGeneList) {
            if (geneKeyMap.get(g.getGeneId()).equals(g.getLocalization())) {
                truePositiveCount++;
            }
        }
        System.out.printf("Accuracy of K-NN prediction of gene Localization: %2.2f%%", (truePositiveCount / testGeneList.size()) * 100);
    }
}
