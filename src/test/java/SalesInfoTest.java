import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by root on 17-7-5.
 */
public class SalesInfoTest {
    @Test
    public void addPurchaseInfo() {
        String url = "http://localhost:8080/salesinfo?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";
        HttpPost post = new HttpPost(url);

        String json = "{'Id':'12','No':'No','SalesTitle':'SalesTitle','PurchaseId':'PurchaseId','ProductAddress':'ProductAddress','ProductionName':'ProductionName'," +
                "'ProductionSpecification':'ProductionSpecification','CreateSalesEnterpriseId':'CreateSalesEnterpriseId','TranstitSalesEnterpriseId':'TranstitSalesEnterpriseId','SalesCount':'12'," +
                "'ProductTime':'1','ProductBatch':'ProductBatch','ProductDeadline':'123','GoodsOriginalUrl':'GoodsOriginalUrl','SalesDate':'123','BuyerName':'BuyerName'," +
                "'BuyerAddress':'BuyerAddress','BuyerTel':'BuyerTel','ResponsibilityName':'ResponsibilityName','InspectionCertificateNumber':'InspectionCertificateNumber'," +
                "'ProductionProcessId':'ProductionProcessId','GoodsId':'GoodsId','SalsesId':'SalsesId'}";
        System.out.println("json==="+json);
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            StringEntity se = new StringEntity(json);
            post.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                System.out.println("===result===\n" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryGoodsList(){
        String url = "http://localhost:8080/salesinfo/No/PurchaseId/GoodsId/ProductBatch?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";

        HttpGet get = new HttpGet(url);
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpResponse response = httpClient.execute(get);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("===result===\n" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
