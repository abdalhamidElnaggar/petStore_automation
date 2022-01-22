package utils;

public class EndPoints {

    public static final String baseUrl = "https://petstore.swagger.io/v2/";
    public static final String petStatusEndPoint = baseUrl + "pet/findByStatus";
    public static final String petCreationEndPoint = baseUrl + "pet";
    public static final String getPetByIdEndPoint = baseUrl + "pet/" + 1;
   public static final String getPetByInvalidIdEndPoint = baseUrl + "pet/" + "-1";
    public static String deletePetByIdEndPoint = baseUrl + "pet/";
    public  static final String userCreationWithArray = baseUrl + "user/createWithArray";
    public static final String getUserByName = baseUrl + "user/thanos";








}
