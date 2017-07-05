import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * 生产信息test
 * Created by Luo_xuri on 2017/7/5.
 */
public class ProduceInfoTest {

    @Test
    public void addProduceInfo() {
        String urlNameString = "http://localhost:8080/produceinfo";
        String result = null;
        try {
            String json = "{\n" +
                    "\"BatchNumber\": \"BatchNumber\",\n" +
                    "\"CheckDate\": \"CheckDate\",\n" +
                    "\"CheckResult\": \"CheckResult\",\n" +
                    "\"CheckWay\": \"CheckWay\",\n" +
                    "\"EnterpriseId\": \"EnterpriseId\",\n" +
                    "\"EnvironmentalMonitoring\": \"EnvironmentalMonitoring\",\n" +
                    "\"GoodsCount\": \"GoodsCount\",\n" +
                    "\"InStorageTime\": \"InStorageTime\",\n" +
                    "\"InspectorName\": \"InspectorName\",\n" +
                    "\"LastCount\": \"LastCount\",\n" +
                    "\"OutStorageTime\": \"OutStorageTime\",\n" +
                    "\"ProductId\": \"ProductId\",\n" +
                    "\"ProductionParameters\": \"ProductionParameters\",\n" +
                    "\"ProductionProcessName\": \"ProductionProcessName\",\n" +
                    "\"ProductionTime\": \"ProductionTime\",\n" +
                    "\"QualitySafety\": \"QualitySafety\"\n" +
                    "}";
            StringEntity stringEntity = new StringEntity(json);
            HttpPost post = new HttpPost(urlNameString);
            post.setEntity(stringEntity);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.err.println("-->result:" + result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
