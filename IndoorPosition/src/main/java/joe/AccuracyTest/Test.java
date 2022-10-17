package joe.AccuracyTest;

import joe.CrossValidation.Algorithm;
import joe.Model.Fingerprint;
import joe.Model.Position;
import joe.Utils.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Test t = new Test();
        t.doTest();
    }

    public void doTest() {
        List<Map<Point, List<Double>>> mapList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            System.out.println("Test Round " + i);
            Map<Point, List<Double>> pointErrorsMap = new HashMap<>();

            Path testDataPath = Paths.get("data", "p", i+"", "test.json");
            // load test.json
            JSONArray jsonArray = JSONUtils.loadJSONArray(testDataPath);
            // get model
            Path modelPath = Paths.get("data", "m", i+"");
            Algorithm algorithm = new Algorithm(modelPath);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject data = jsonArray.getJSONObject(j);
                double reference_x = data.getDouble("ref_x");
                double reference_y = data.getDouble("ref_y");
                Fingerprint fingerprint = jsonObjectToFingerprint(data);
                // use algorithm to estimate location given fingerprint
                Position estimateLocation = algorithm.getLocation(fingerprint);
                double error = calculateError(reference_x, reference_y, estimateLocation.x, estimateLocation.y);

                int x = data.getInt("ref_x");
                int y = data.getInt("ref_y");
                Point p = new Point(x, y);
                if (pointErrorsMap.containsKey(p)) {
                    pointErrorsMap.get(p).add(error);
                } else {
                    List<Double> l = new ArrayList<>();
                    l.add(error);
                    pointErrorsMap.put(p, l);
                }
            }
            mapList.add(pointErrorsMap);
        }

        Map<Point, Double> pointErrorMap = new HashMap<>();
        for (Map<Point, List<Double>> m : mapList) {
            for (Map.Entry<Point, List<Double>> entry : m.entrySet()) {
                Point key = entry.getKey();
                List<Double> value = entry.getValue();
                double avg = calculateAvg(value);
                if (!pointErrorMap.containsKey(key)) {
                    pointErrorMap.put(key, avg);
                } else {
                    Double prev = pointErrorMap.get(key);
                    pointErrorMap.put(key, prev + avg);
                }
            }
        }
        for (Map.Entry<Point, Double> entry : pointErrorMap.entrySet()) {
            double v = entry.getValue() / 10;
            pointErrorMap.put(entry.getKey(), v);
        }

        for (Map.Entry<Point, Double> entry : pointErrorMap.entrySet()) {
            System.out.println(entry);
        }

    }

    private static double calculateError(double relX, double relY, double estX, double estY) {
        return Math.sqrt(Math.pow(relX - estX, 2) + Math.pow(relY - estY, 2));
    }

    private static Fingerprint jsonObjectToFingerprint(JSONObject data) {
        double angle = data.getDouble("angle");
        JSONArray RSSIObservations = data.getJSONArray("rssi_observations");

        Fingerprint fingerprint = new Fingerprint();
        fingerprint.setDegreesFromNorth(angle);
        fingerprint.setRssiValues(RSSIObservations);
        return fingerprint;
    }

    private static double calculateAvg(List<Double> list) {
        return list.stream().mapToDouble(d -> d).average().orElse(0.0);
    }
}
