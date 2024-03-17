package ale.rains.demo.uiautomator;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ale.rains.nanohttpd.protocols.http.IHTTPSession;
import ale.rains.nanohttpd.protocols.http.NanoHTTPD;
import ale.rains.nanohttpd.protocols.http.content.ContentType;
import ale.rains.nanohttpd.protocols.http.request.Method;
import ale.rains.nanohttpd.protocols.http.response.Response;
import ale.rains.nanohttpd.protocols.http.response.Status;
import ale.rains.util.LogUtils;

public class AutomatorHttpServer extends NanoHTTPD {

    public AutomatorHttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        // 解决客户端请求参数携带中文，出现中文乱码问题
        ContentType ct = new ContentType(session.getHeaders().get("content-type")).tryUTF8();
        session.getHeaders().put("content-type", ct.getContentTypeHeader());
        return dealWith(session);
    }

    private Response dealWith(IHTTPSession session) {
        LogUtils.i("session.getMethod() = " + session.getMethod());
        if (Method.POST == session.getMethod()) {
            //获取请求头数据
            Map<String, String> header = session.getHeaders();
            //获取传参参数
            Map<String, String> params = new HashMap<String, String>();
            try {
                session.parseBody(params);
                String paramStr = params.get("postData");
                if (TextUtils.isEmpty(paramStr)) {
                    return newFixedLengthResponse("success");
                }
                paramStr = paramStr.replace("\r\n", " ");

                JSONObject jsonParam = new Gson().fromJson(paramStr, JSONObject.class);
                Map<String, Object> result = new HashMap<>();
                //TODO 写你的业务逻辑.....

                //响应客户端
                return newFixedLengthResponse("success");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return newFixedLengthResponse("success");
        } else if (Method.GET == session.getMethod()) {
            Map<String, List<String>> parameters = session.getParameters();
            return newFixedLengthResponse("success");
        }
        return newFixedLengthResponse("404");
    }

    public static Response newFixedLengthResponse(String msg) {
        return Response.newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_HTML, msg);
    }
}