package joe.AccuracyTest;

import joe.CrossValidation.CrossValidationConstant;
import joe.DistributionFile.DistributionGenerator;
import joe.Model.Position;
import joe.Utils.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Preprocess {

    private Map<Point, List<JSONObject>> map = new HashMap<>();

    public static void initDirectories() {
        for (int i = 1; i <= 10; i++) {
            Path p = Paths.get("data", "p", i+"");
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        JSONArray jsonArray = JSONUtils.loadJSONArray(Paths.get("data", "local_records.json"));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int x = jsonObject.getInt("ref_x");
            int y = jsonObject.getInt("ref_y");
            Point p = new Point(x, y);
            if (map.containsKey(p)) {
                map.get(p).add(jsonObject);
            } else {
                List<JSONObject> l = new ArrayList<>();
                l.add(jsonObject);
                map.put(p, l);
            }
        }
    }

    public void process() {
        init();
        initDirectories();
        for (int i = 1; i <= 10; i++) {
            List<JSONObject> trainData = new ArrayList<>();
            List<JSONObject> testData = new ArrayList<>();
            for (List<JSONObject> l : map.values()) {
                Collections.shuffle(l);
                testData.addAll(l.subList(0, 75));
                trainData.addAll(l.subList(75,l.size()));
            }
            JSONArray testJSONDate = new JSONArray(testData);
            JSONUtils.writeFile(Paths.get("data", "p", i+"", "test.json"), testJSONDate.toString());

            JSONArray trainJSONData = new JSONArray(trainData);
            // generate distributions.json using training data
            List<Map<String, Map<Position, List<Double>>>> RSSIData = DistributionGenerator.getRSSIData(trainJSONData);
            List<Map<String, Map<Position, List<Double>>>> RSSIDistributions = DistributionGenerator.processDistributions(RSSIData);
            Path distributionFilePath = Paths.get("data", "p", i+"", "distributions.json");
            DistributionGenerator.saveDataJSON(RSSIDistributions, distributionFilePath);
        }
    }

    public static void main(String[] args) {
        Preprocess preprocess = new Preprocess();
        preprocess.process();
    }
}
