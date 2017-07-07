import com.oxchains.bean.dto.datav.NameValue;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.omg.CORBA.NameValuePair;

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
            String json = "{\n" +
                    "\"ApprovalNumber\": \"ApprovalNumber\",\n" +
                    "\"Describe\": \"Describe\",\n" +
                    "\"EnterpriseId\": \"110\",\n" +
                    "\"Pack\": \"Pack\",\n" +
                    "\"ProductAddress\": \"中关村SOHO\",\n" +
                    "\"ProductCode\": \"ProductCode\",\n" +
                    "\"ProductDeadline\": \"12\",\n" +
                    "\"ProductName\": \"阿拉丁神油\",\n" +
                    "\"ProductOriginalUrl\": \"屌的不行\",\n" +
                    "\"ProductTags\": \"ProductTags\",\n" +
                    "\"ProductTime\": \"1234566\",\n" +
                    "\"ProductType\": \"ProductType\",\n" +
                    "\"ProductVolume\": \"ProductVolume\",\n" +
                    "\"ProductWeight\": \"ProductWeight\",\n" +
                    "\"Remarks\": \"Remarks\",\n" +
                    "\"Size\": \"Size\",\n" +
                    "\"Storage\": \"Storage\"\n" +
                    "}";
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
}
