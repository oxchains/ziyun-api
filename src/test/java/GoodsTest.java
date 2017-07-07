import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oxchains.bean.model.ziyun.DrugInformation;
import com.oxchains.bean.model.ziyun.FoodInformation;
import com.oxchains.bean.model.ziyun.Goods;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-7-5.
 */
public class GoodsTest {
    @Test
    public void addGoods(){
        String urlNameString = "http://localhost:8080/goods";
        String result = "";
        try {
           String json = "{\"Type\":\"Type\",\"GoodsType\": \"GoodsType\",\"ParentCode\":\"ParentCode\",\"ProduceInfoId\":\"ProduceInfoId\"," +
                    "\"ProductCode\":\"ProductCode\",\"UniqueCode\":\"UniqueCode\",\"CommodityCode\":\"CommodityCode\",\"ProductionBatch\":\"ProductionBatch\"," +
                    "\"DrugInformationList\": [{\"DrugName\":\"DrugName\",\"ApprovalNumber\":\"ApprovalNumber\",\"Size\":\"Size\",\"Form\":\"Form\",\"Manufacturer\":\"Manufacturer\",\"NDCNumber\":\"NDCNumber\",\"NDCNumberRemark\":\"NDCNumberRemark\",\"MedicineInstruction\":\"MedicineInstruction\"}]," +
                    "\"FoodInformationList\": [{\"FoodName\":\"FoodName\",\"Manufacturer\":\"Manufacturer\"}]," +
                    "\"ProduceInformationList\":[{\"Address\":\"Address\",\"ProductionBatch\":\"ProductionBatch\",\"ProductionTime\":\"12345678\",\"ValidDate\":\"12345678\"}]}";

            System.out.println("json==="+json);
            StringEntity stringentity = new StringEntity(json);

            HttpPost post = new HttpPost(urlNameString);
            //post.addHeader(HTTP.CONTENT_TYPE, "application/json");
            //post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            post.setEntity(stringentity);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("===result===\n" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryGoodsList(){
        String url = "http://localhost:8080/goods/ProductCode/UniqueCode/CommodityCode/ProductionBatch";

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
