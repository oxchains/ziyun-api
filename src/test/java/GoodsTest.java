import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oxchains.bean.model.ziyun.DrugInformation;
import com.oxchains.bean.model.ziyun.FoodInformation;
import com.oxchains.bean.model.ziyun.Goods;
import org.apache.http.Header;
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
import org.apache.http.message.BasicHeader;
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
        String urlNameString = "http://localhost:8090/goods?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";
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
        String url = "http://localhost:8090/goods/12";

        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization","Bearer eyJhbGciOiJFUzI1NiJ9.eyJqdGkiOiI4MDVjMmI5ZS0zYTI5LTQxMTgtYjkyNC00Y2NmZTYzOTk2ZGUiLCJzdWIiOiJmYWJyaWMiLCJleHAiOjE1MDQ2ODY0Nzl9.FEvIIPkqpZUBErR6Ma8eEbxtzAcMN46_29k-5ljXprG1LGnChv_Is3--ipe86llGME1Em5cK6FvhhjVoIhSEpQ");
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpResponse response = httpClient.execute(get);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("===result===\n" + result);
            }
            else{
                System.out.println("===error==="+response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
