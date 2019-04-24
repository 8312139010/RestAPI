package se.iths.selenium.assignmentrestAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class UserJacksonTest {

    public static String createUserUrl = "https://swagger-petstore.azurewebsites.net/v2/user";
    public static String createUserLoginUrl = "https://swagger-petstore.azurewebsites.net/v2/user/login";
    public static String getCreateUserWrongLoginUrl = "https://swagger-petstore.azurewebsites.net/v2/user/login?abc&xyz";

    @Test
    public void createUser() throws JsonProcessingException, UnirestException {
        User qasim = new User(1280, "qasim", "Qasim", "khan",
                "qasim_ajk@yahoo.com", "kashmir");

        ObjectMapper mapper = new ObjectMapper();
        String qasimAsJason = mapper.writeValueAsString(qasim);
        System.out.println(qasimAsJason);

        HttpResponse<String> createUser = Unirest.post(createUserUrl)
                .header("Content-Type", "application/json")
                .body(qasimAsJason)
                .asString();
        Assert.assertEquals(200,
                createUser.getStatus());

    }

    @Test
    public void createManyUser() throws JsonProcessingException, UnirestException {
        User qasim = new User(1280, "qasim", "Qasim", "khan",
                "qasim_ajk@yahoo.com", "kashmir");
        User asim = new User(1280, "asim", "asim", "khan",
                "qasim_ajk@yahoo.com", "kashmir");

        ObjectMapper mapper = new ObjectMapper();
        String qasimAsJason = mapper.writeValueAsString(qasim);
        String asimAsJason = mapper.writeValueAsString(asim);

        System.out.println(qasimAsJason);
        System.out.println(asimAsJason);

        HttpResponse<String> createUser = Unirest.post(createUserUrl)
                .header("Content-Type", "application/json")
                .body(qasimAsJason)
                .body(asimAsJason)
                .asString();
        Assert.assertEquals(200,
                createUser.getStatus());

    }

    //login User with Right username and password by creating object

    @Test
    public void createUserAndLoginWithRightUnamePwd() throws JsonProcessingException, UnirestException {

        createUser();

        HttpResponse<String> loginUser = Unirest.get
                (createUserLoginUrl)
                .asString();
        Assert.assertEquals(200,
                loginUser.getStatus());
    }

    //login User with wrong username and password by creating object

    @Test
    public void createUserAndLoginWithWrongUnamePwd() throws JsonProcessingException, UnirestException {

        createUser();
        HttpResponse<String> loginUser = Unirest.get
                (getCreateUserWrongLoginUrl)
                .asString();
        Assert.assertEquals(400,
                loginUser.getStatus());

        //I think this has a bug, Because it shows actual status is 200 which it should not
        //allow to user get log in with invalid user name and pwd.
    }

    @Test
    public void updateEmailOfUser() throws JsonProcessingException, UnirestException {

        createUser();

        User qasim = new User(1280, "qasim", "Qasim", "khan",
                "bbc_ajk@yahoo.com", "kashmir");

        ObjectMapper mapper = new ObjectMapper();
        String qasimAsJason = mapper.writeValueAsString(qasim);
        System.out.println(qasimAsJason);

        HttpResponse<String> updateEmail = Unirest.put
                ("https://swagger-petstore.azurewebsites.net/v2/user/qasim")
                .header("Content-Type", "application/json")
                .body(qasimAsJason)
                .asString();

        Assert.assertEquals(200,
                updateEmail.getStatus());

        /* This method also has bug, because when i try to getStatus with wrong user name
        even then it got passed, which should not be
         */

    }

    @Test
    public void createManyUsersLoginThemAndDeleteThem() throws JsonProcessingException, UnirestException {

        createManyUser();

        HttpResponse<String> manyUsersLogin = Unirest.get
                ("https://swagger-petstore.azurewebsites.net/v2/user/login")
                .asString();
        Assert.assertEquals(200,
                manyUsersLogin.getStatus());

        HttpResponse<String> deleteUser1 = Unirest.delete
                ("https://swagger-petstore.azurewebsites.net/v2/user/qasim")
                .asString();
        Assert.assertEquals(404,
                deleteUser1.getStatus());

        HttpResponse<String> deleteUser2 = Unirest.delete
                ("https://swagger-petstore.azurewebsites.net/v2/user/asim")
                .asString();
        Assert.assertEquals(404,
                deleteUser1.getStatus());

    }

    UserPetStoreClient client = new UserPetStoreClient();

    @Test
    public void getUser()throws UnirestException, IOException {
        User qasim = new User(1280, "qasim", "Qasim", "khan",
                "qasim_ajk@yahoo.com", "kashmir");

        client.createUser(qasim);

        User user = client.getUser("qasim");

        Assert.assertEquals("Qasim",
                user.getFirstName());



    }
}
