package ao.com.wundu.integration.plaid;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class PlaidIntegrationTest {

    private static final String CLIENT_ID = "67e016cd90625d0022c97d1f";
    private static final String SECRET = "c472aa12f766c0775e81424b237312";
    private static final String BASE_URL = "https://sandbox.plaid.com";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        System.out.println("🔍 Iniciando teste de integração com Plaid...");

        String userId = UUID.randomUUID().toString();
        String linkToken = createLinkToken(userId);
        System.out.println("✅ link_token: " + linkToken);

        String publicToken = createSandboxPublicToken();
        System.out.println("🔑 public_token gerado: " + publicToken);

        Map<String, String> tokenData = exchangePublicToken(publicToken);
        String accessToken = tokenData.get("access_token");
        String itemId = tokenData.get("item_id");

        System.out.println("✅ access_token: " + accessToken);
        System.out.println("🆔 item_id: " + itemId);

        getAccounts(accessToken);
        getItem(accessToken);
    }

    private static String createLinkToken(String userId) throws Exception {
        Map<String, Object> user = Map.of("client_user_id", userId);
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("secret", SECRET);
        body.put("client_name", "Wundu Finance App");
        body.put("country_codes", new String[]{"US"});
        body.put("language", "en");
        body.put("user", user);
        body.put("products", new String[]{"auth", "transactions"});

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/link/token/create"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> json = mapper.readValue(response.body(), new TypeReference<>() {});
        return (String) json.get("link_token");
    }

    private static String createSandboxPublicToken() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("secret", SECRET);
        body.put("institution_id", "ins_109508");
        body.put("initial_products", List.of("transactions"));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/sandbox/public_token/create"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> json = mapper.readValue(response.body(), new TypeReference<>() {});
        return (String) json.get("public_token");
    }

    private static Map<String, String> exchangePublicToken(String publicToken) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("secret", SECRET);
        body.put("public_token", publicToken);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/item/public_token/exchange"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> json = mapper.readValue(response.body(), new TypeReference<>() {});

        Map<String, String> result = new HashMap<>();
        result.put("access_token", (String) json.get("access_token"));
        result.put("item_id", (String) json.get("item_id"));
        return result;
    }

    private static void getAccounts(String accessToken) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("secret", SECRET);
        body.put("access_token", accessToken);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/accounts/get"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("🏦 Contas vinculadas:\n" + response.body());
    }

    private static void getItem(String accessToken) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("secret", SECRET);
        body.put("access_token", accessToken);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/item/get"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("📦 Dados do item:\n" + response.body());
    }
}
