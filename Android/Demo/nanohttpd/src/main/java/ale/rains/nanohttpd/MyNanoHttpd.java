package ale.rains.nanohttpd;

import static ale.rains.nanohttpd.protocols.http.response.Response.newFixedLengthResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import ale.rains.nanohttpd.protocols.http.IHTTPSession;
import ale.rains.nanohttpd.protocols.http.NanoHTTPD;
import ale.rains.nanohttpd.protocols.http.request.Method;
import ale.rains.nanohttpd.protocols.http.response.Response;
import ale.rains.nanohttpd.protocols.http.response.Status;

public class MyNanoHttpd extends NanoHTTPD {
    public MyNanoHttpd(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Running! Point your browsers to http://localhost:" + port + "/");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        String ip = session.getHeaders().get("http-client-ip");
        System.out.println(ip + " [" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] >>> " + uri + " ===> " + method);
        String html;
        String mimetype = "text/html";
        if (uri.endsWith(".html") || uri.endsWith("htm")) {
            mimetype = "text/html";
        } else if (uri.endsWith(".js")) {
            mimetype = "text/javascript";
        } else if (uri.endsWith(".css")) {
            mimetype = "text/css";
        } else if (uri.endsWith(".ico")) {
            return newFixedLengthResponse("ico 404");
        } else {
            return handler(session);
        }
        html = readHtml(uri);
        return newFixedLengthResponse(Status.OK, mimetype, html);
    }

    private Response handler(IHTTPSession session) {
        if ("/exec".equals(session.getUri())) {
            Map param = session.getParms();
            if (param != null) {
                if (param.get("path") != null && param.get("authCode") != null) {
                    String r = exec((String) param.get("path"), (String) param.get("authCode"));
                    return newFixedLengthResponse(r);
                } else {
                    return newFixedLengthResponse("授权码不可为空!");
                }
            }
        }
        return newFixedLengthResponse("404!");
    }

    public String exec(String path, String authCode) {
        String result;
        if ("passwd".equals(authCode)) {
            System.out.println("exec > " + path);
            String fullPath = "";
            if ("api".equals(path)) {
                fullPath = "/home/server/run_api";
            } else if ("api_upload".equals(path)) {
                fullPath = "/home/server/run_upload";
            } else if ("backend".equals(path)) {
                fullPath = "/home/server/run_backend";
            } else {
                return "操作失败，未知服务!";
            }
            System.out.println(path + " ====> " + fullPath);
            try {
                Runtime.getRuntime().exec(new String[]{"/bin/bash", fullPath});
                result = "exec ok";
            } catch (Exception e) {
                System.err.println(e.getMessage());
                result = "exec fail > " + e.getMessage();
            }
        } else {
            result = "authCode error!";
        }
        return result;
    }

    private String readHtml(String pathName) {
        BufferedReader br;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(MyNanoHttpd.class.getResourceAsStream(pathName), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "404!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Missing operating system!";
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        new MyNanoHttpd(8090);
    }
}
