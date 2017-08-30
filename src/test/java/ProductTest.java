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
        String urlNameString = "http://localhost:8080/product?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmNCIsImlhdCI6MTQ5OTc2NzE4Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTQ5OTc3NDM4Mn0.7sCv3QFvCos4GfeBIJunQ0SKfQYiFv4tdoKHHKVr5iY";
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
        String url = "http://localhost:8090/productGmp/产品名称?Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ6aXl1biIsImlhdCI6MTUwMzQ2OTcxNiwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTUwNDA3NDUxNn0.K0FCAvRgWjXyjqD4ONwr9o1H2FSaud-249z3sShP5pU";
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
