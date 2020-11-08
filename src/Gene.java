import java.util.*;

public class Gene {
    Map<String, String> attributeMap = new HashMap<>();

    public Gene(final String[] geneArray) {
        attributeMap.put("geneId", geneArray[0]);
        attributeMap.put("Essential", geneArray[1]);
        attributeMap.put("Class", geneArray[2]);
        attributeMap.put("Complex", geneArray[3]);
        attributeMap.put("Phenotype", geneArray[4]);
        attributeMap.put("Motif", geneArray[5]);
        attributeMap.put("Chromosome", geneArray[6]);
        attributeMap.put("Localization", geneArray[8]);
    }


    /*
        computeDistance is a mixed Euclidian distance formula for determining how similar two gene tuples are.
        I used the same weights the winning KDD competition winners used in their solution for determining the weights of each attribute
     */
    public double computeDistance(Gene o) {
        double distance = 0;

        for (Map.Entry<String, String> pair : attributeMap.entrySet()) {
            switch (pair.getKey()) {
                case ("Essential"):
                    if (o.attributeMap.get("Essential").equals(pair.getValue()))
                        distance++;
                    break;
                case ("Class"):
                    if (o.attributeMap.get("Class").equals(pair.getValue()))
                        distance += Math.pow(100, 2);
                    break;
                case ("Complex"):
                    if (o.attributeMap.get("Complex").equals(pair.getValue()))
                        distance += Math.pow(1000, 2);
                    break;
                case ("Phenotype"):
                    if (o.attributeMap.get("Phenotype").equals(pair.getValue()))
                        distance++;
                    break;
                case ("Motif"):
                    if (o.attributeMap.get("Motif").equals(pair.getValue()))
                        distance += Math.pow(10, 2);
                    break;
            }
        }

        return Math.sqrt(distance);
    }

    public String getLocalization() {
        return attributeMap.get("Localization");
    }

    public String getGeneId() {
        return attributeMap.get("geneId");
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        attributeMap.forEach((key, value) -> returnString.append(key).append(": ").append(" ").append(value).append(" "));

        return returnString.toString();
    }

    /*
        This method determines the value of the 'Localization' attribute based on the K-nearest neighbors (in this case there are 3 neighbors)
     */
    public void decideLocalization(List<Gene> neighbors) {
        HashMap<String, Integer> countMap = new HashMap<>();

        for (Gene g : neighbors) {
            countMap.putIfAbsent(g.getLocalization(), 0);
            countMap.put(g.getLocalization(), countMap.get(g.getLocalization()) + 1);
        }

        attributeMap.put("Localization", Collections.max(countMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey());
    }
}
