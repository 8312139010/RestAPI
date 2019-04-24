package se.iths.selenium.assignmentrestAPI;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

public class SwaggerUserTest {

    @Test
    public void createUser() throws UnirestException {

        HttpResponse<String> postUserResponse = Unirest.post
                ("https://swagger-petstore.azurewebsites.net/v2/user")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"id\": 1280,\n" +
                        "  \"username\": \"qas\",\n" +
                        "  \"firstName\": \"qasim\",\n" +
                        "  \"lastName\": \"khan\",\n" +
                        "  \"email\": \"qasim_ajk@yahoo.com\",\n" +
                        "  \"password\": \"kashmir\",\n" +
                        "  \"phone\": \"0723134080\",\n" +
                        "  \"userStatus\": 0\n" +
                        "}")
                .asString();

        Assert.assertEquals(200,
                postUserResponse.getStatus());

    }

    @Test
    public void createUserLoginWithRightUserNamePwd() throws UnirestException {

        createUser();

        HttpResponse<String> getLogin = Unirest.get
                ("https://swagger-petstore.azurewebsites.net/v2/user/login")
                .asString();
        Assert.assertEquals(200,
                getLogin.getStatus());

    }

    @Test
    public void createUserAndLoginWithWrongPwd() throws UnirestException {

        createUser();

        HttpResponse<String> getUserLoginWithWrongPwd = Unirest.get
                ("https://swagger-petstore.azurewebsites.net//v2/user/login/?qasimkhan&kashmir")
                .asString();
        Assert.assertEquals(400,
                getUserLoginWithWrongPwd.getStatus());

        //I think this has a bug, Because it shows actual status is 200 which it should not
        //allow to user get log in with invalid user name and pwd.
    }

    @Test
    public void createManyUsers() throws UnirestException {
        HttpResponse<JsonNode> createManyUsers = Unirest.post
                ("https://swagger-petstore.azurewebsites.net/v2/user/createWithList")
                .header("Content-Type", "application/json")
                .body("[\n" +
                        "\n" +
                        "{\n" +
                        "  \"id\": 1280,\n" +
                        "  \"username\": \"qas\",\n" +
                        "  \"firstName\": \"qasim\",\n" +
                        "  \"lastName\": \"khan\",\n" +
                        "  \"email\": \"abc@yahoo.com\",\n" +
                        "  \"password\": \"kashmir\",\n" +
                        "  \"phone\": \"072313\",\n" +
                        "  \"userStatus\": 0\n" +
                        "},\n" +
                        "{\n" +
                        "  \"id\": 1281,\n" +
                        "  \"username\": \"asi\",\n" +
                        "  \"firstName\": \"asim\",\n" +
                        "  \"lastName\": \"khan\",\n" +
                        "  \"email\": \"abc@yahoo.com\",\n" +
                        "  \"password\": \"kashmir\",\n" +
                        "  \"phone\": \"072313\",\n" +
                        "  \"userStatus\": 0\n" +
                        "}\n" +
                        "]")
                .asJson();

        Assert.assertEquals(200,
                createManyUsers.getStatus());

        HttpResponse<String> allUsersLogin = Unirest.get
                ("https://swagger-petstore.azurewebsites.net/v2/user/login")
                .asString();
        Assert.assertEquals(200,
                allUsersLogin.getStatus());

        HttpResponse<String> delUser1 = Unirest.delete
                ("https://swagger-petstore.azurewebsites.net/v2/user/qas")
                .asString();
        Assert.assertEquals(404,
                delUser1.getStatus());

        HttpResponse<String> delUser2 = Unirest.delete
                ("https://swagger-petstore.azurewebsites.net/v2/user/asi")
                .asString();
        Assert.assertEquals(404,
                delUser2.getStatus());

        /* It seems delete function has bug too, because it is showing actual status 200
        But it should be 404 according to Swagger store User delete Response
         */

    }

    @Test
    public void updateUserEmail() throws UnirestException {

        createUser();

        HttpResponse<String> updateEmail = Unirest.put
                ("https://swagger-petstore.azurewebsites.net//v2/user/qas")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"id\": 1280,\n" +
                        "  \"username\": \"qas\",\n" +
                        "  \"firstName\": \"qasim\",\n" +
                        "  \"lastName\": \"khan\",\n" +
                        "  \"email\": \"xyz@yahoo.com\",\n" +
                        "  \"password\": \"kashmir\",\n" +
                        "  \"phone\": \"072313\",\n" +
                        "  \"userStatus\": 0\n" +
                        "}")
                .asString();
        Assert.assertEquals(200,
                updateEmail.getStatus());

        HttpResponse<String> getUpdatedUser = Unirest.get
                ("https://swagger-petstore.azurewebsites.net//v2/user/qas")
                .asString();
        Assert.assertEquals(200,
                getUpdatedUser.getStatus());
    }

}
