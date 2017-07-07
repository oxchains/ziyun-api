import com.google.api.Http;
import com.oxchains.bean.dto.datav.NameValue;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.omg.CORBA.NameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品信息test
 * Created by Luo_xuri on 2017/7/5.
 */
public class ProductTest {

    @Test
    public void addProduct(){
        String urlNameString = "http://localhost:8080/product";
        String result = null;
        try {
            String json = "{\"ApprovalNumber\": \"ApprovalNumber\",\"Describe\": \"Describe\",\"Pack\": \"Pack\",\"ProductAddress\": \"中关村SOHO\",\"ProductCode\": \"ProductCode\",\"ProductDeadline\": 12,\"ProductName\": \"阿拉丁神油\",\"ProductOriginalUrl\": \"屌的不行\",\"ProductTags\": \"ProductTags\",\"ProductTime\": 1234566,\"ProductType\": \"ProductType\",\"ProductVolume\": \"ProductVolume\",\"ProductWeight\": \"ProductWeight\",\"Remarks\": \"Remarks\",\"Size\": \"Size\",\"Storage\": \"Storage\"}";
            StringEntity stringEntity = new StringEntity(json);
            HttpPost post = new HttpPost(urlNameString);
            post.setEntity(stringEntity);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.err.println("-->result: " + result);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryProduct(){
        String url = "http://localhost:8080/product/ApprovalNumber/ProductCode";
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.err.println("-->result:" + result);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
