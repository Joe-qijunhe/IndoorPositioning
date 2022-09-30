package joe.ComputeTime;

import joe.CrossValidation.Algorithm;
import joe.Model.Fingerprint;
import joe.Model.Position;
import joe.Utils.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ComputeTimeCalculator {
    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(Paths.get("data", "distribution"));
        JSONArray jsonArray = JSONUtils.loadJSONArray(Paths.get("data", "local_records.json"));
        for (int j = 0; j < 10; j++) {
            JSONObject data = jsonArray.getJSONObject(j);
            double reference_x = data.getDouble("ref_x");
            double reference_y = data.getDouble("ref_y");
            Fingerprint fingerprint = jsonObjectToFingerprint(data);
            long start = System.currentTimeMillis();
            // use algorithm to estimate location given fingerprint
            Position estimateLocation = algorithm.getLocation(fingerprint);
            long end = System.currentTimeMillis();
            System.out.println((end - start) + " ms");
        }
    }

    private static Fingerprint jsonObjectToFingerprint(JSONObject data) {
        double angle = data.getDouble("angle");
        JSONArray RSSIObservations = data.getJSONArray("rssi_observations");

        Fingerprint fingerprint = new Fingerprint();
        fingerprint.setDegreesFromNorth(angle);
        fingerprint.setRssiValues(RSSIObservations);
        return fingerprint;
    }
}
