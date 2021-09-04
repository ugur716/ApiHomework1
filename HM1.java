package HomeWork;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class HM1 {

    @BeforeClass
    public void beforeclass(){
        baseURI= ConfigurationReader.get("hr_api_url");
    }

    /*
    Q1:
  - Given accept type is Json
  - Path param value- US
  - When users sends request to /countries
  - Then status code is 200
  - And Content - Type is Json
  - And country_id is US
  - And Country_name is United States of America
  - And Region_id is
     */


    @Test
    public void Q1(){
        Response response=given().accept(ContentType.JSON)
                .and().pathParam("country_id","US")
                .when().get("/countries/{country_id}");

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");
        Assert.assertEquals(response.path("country_id"),"US");
        Assert.assertEquals(response.path("country_name"),"United States of America");
        int Rid=response.path("region_id");
        Assert.assertEquals(Rid,2);
    }

    /*
     Given accept type is Json
- Query param value - q={"department_id":80}
- When users sends request to /employees
- Then status code is 200
- And Content - Type is Json
- And all job_ids start with 'SA'
- And all department_ids are 80
- Count is 25

     */

    @Test
    public void Q2(){
        Response response=given().accept(ContentType.JSON)
                .and().queryParams("q","{\"department_id\":80}")
                .when().get("/employees");

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");

        JsonPath jsonPath=response.jsonPath();
        List<String> jobIDs= jsonPath.getList("items.job_id");
        for (String jobID : jobIDs) {
            Assert.assertTrue(jobID.startsWith("SA"));

        }

        List<Object> departmentIDS= jsonPath.getList("items.department_id");
        for (Object departmentID : departmentIDS) {
            Assert.assertEquals(departmentID,80);

        }
        int count=response.path("count");
        Assert.assertEquals(count,25);




    }
    /*
    Q3:
- Given accept type is Json
-Query param value q= region_id 3
- When users sends request to /countries
- Then status code is 200
- And all regions_id is 3
- And count is 6
- And hasMore is false
- And Country_name are;
Australia,China,India,Japan,Malaysia,Singapore
     */

    @Test
    public void Q3(){
        Response response=given().accept(ContentType.JSON)
                .and().queryParams("q","{\"region_id\":\"3\"}")
                .when().get("/countries");

        Assert.assertEquals(response.statusCode(),200);
        JsonPath jsonPath=response.jsonPath();
        List<Object> rIDS= jsonPath.getList("items.region_id");
        for (Object rID : rIDS) {
            Assert.assertEquals(rID,3);
        }
        int count=response.path("count");
        Assert.assertEquals(count,6);
        Assert.assertFalse(response.path("hasMore"));
        List<String> countries= jsonPath.getList("items.country_name");
        List<String> expectCountry=new ArrayList<>();
        String[] list={"Australia","China","India","Japan","Malaysia","Singapore"};
        Collections.addAll(expectCountry,list);
        Assert.assertEquals(countries,expectCountry);

    }


}
