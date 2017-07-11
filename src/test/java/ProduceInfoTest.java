import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 生产信息test
 * Created by Luo_xuri on 2017/7/5.
 */
public class ProduceInfoTest {

    @Test
    public void addProduceInfo() {
        String urlNameString = "http://localhost:8080/produceinfo?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";
        String result = null;
        try {
            String json = "{\"Id\": \"id3\",\"BatchNumber\": \"BatchNumber\",\"CheckDate\": 6534321,\"CheckResult\": \"CheckResult\",\"CheckWay\": \"CheckWay\",\"EnterpriseId\": \"EnterpriseId\",\"EnvironmentalMonitoring\": \"EnvironmentalMonitoring\",\"GoodsCount\": 123,\"InStorageTime\": 45643,\"InspectorName\": \"InspectorName\",\"LastCount\": 234,\"OutStorageTime\": 123435,\"ProductId\": \"ProductId\",\"ProductionParameters\": \"ProductionParameters\",\"ProductionProcessName\": \"ProductionProcessName\",\"ProductionTime\": 65454454,\"QualitySafety\": \"QualitySafety\"}";
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

    @Test
    public void queryProduceInfo(){
        String url = "http://localhost:8080/produceinfo/id3?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.err.println("-->result:" + result);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
