package com.year18.imooc_upload.http;

import com.year18.imooc_upload.http.error.ParseError;
import com.year18.imooc_upload.http.error.TimeoutError;
import com.year18.imooc_upload.http.error.URLError;
import com.year18.imooc_upload.http.error.UnknownHostError;
import com.year18.imooc_upload.http.url_connection.UrlConnectionFactory;
import com.year18.imooc_upload.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * 作者：张玉辉 on 2018/5/1 15:28.
 */
public class RequestTask<T> implements Runnable {
    private Request<T> mRequest;
    private HttpListener<T> mHttpListener;

    public RequestTask(Request<T> request,HttpListener<T> httpListener) {
        mRequest = request;
        mHttpListener = httpListener;
    }

    /** 执行请求 **/
    @Override
    public void run() {
        Logger.i(mRequest.toString());
        Exception exception = null;
        int requestCode = -1;
        Map<String,List<String>> responseHeaders = null;
        byte[] responseBody = null;

        String urlStr = mRequest.getUrl();
        RequestMethod requestMethod = mRequest.getMethod();
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(urlStr);
            //用系统自带的URLConnection
//            httpURLConnection = (HttpURLConnection)url.openConnection();
            //替换成OkHttpUrlConnection
            httpURLConnection = UrlConnectionFactory.getInstance().openUrl(url);

            if(httpURLConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                if (mRequest.getSSLSocketFactory() != null) {
                    httpsURLConnection.setSSLSocketFactory(mRequest.getSSLSocketFactory());//https证书相关信息
                }
                if (mRequest.getHostnameVerifier() != null) {
                    httpsURLConnection.setHostnameVerifier(mRequest.getHostnameVerifier()); //主机认证
                }
            }
            //设置请求头
            httpURLConnection.setRequestMethod(requestMethod.value());
            httpURLConnection.setDoInput(true);
            // 如果强制设置会报异常
            httpURLConnection.setDoOutput(requestMethod.isOutputMethod());

            setHeader(httpURLConnection,mRequest);

            //TODO 发送数据
            if(requestMethod.isOutputMethod()){
                outputStream = httpURLConnection.getOutputStream();
                mRequest.onWriteBody(outputStream);
            }
            //TODO 读取响应
            int responseCode = httpURLConnection.getResponseCode();
            responseHeaders = httpURLConnection.getHeaderFields();
            if(hasResponseBody(requestMethod,responseCode)){
                InputStream inputStream = getInputStream(httpURLConnection,responseCode);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[2048];
                while((len=inputStream.read(buffer))!=-1){
                    arrayOutputStream.write(buffer,0,len);
                }
                arrayOutputStream.close();
                responseBody = arrayOutputStream.toByteArray();
            }

        } catch (SocketTimeoutException e){  //网络请求超时异常
            exception = new TimeoutError("socket time out error");
        }catch (MalformedURLException e){  //表示URL错误
            exception = new URLError("The url is error");
        }catch (UnknownHostException e){  //找不到服务器
            exception = new UnknownHostError("The host is unknown");
        }catch (IOException e) {
            exception = e;
        }finally {
            if(httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //解析服务器返回的数据
        T t = null;
        try {
            t = mRequest.parseResponse(responseBody);
        }catch (Exception e){
            exception = new ParseError("the data parse error");
        }

        Response<T> response = new Response<>(requestCode,
                responseHeaders,exception,mRequest);
        response.setResponseResult(t);
        //获取的响应,模拟数据
//        Response response = new Response(100,"我是响应数据",
//                null,null,mRequest);
        Message message = new Message(response,mHttpListener);
        //发送响应数据至主线程
        Poster.getInstance().post(message);
    }

    /**
     * 判断是否响应body
     * @param requestMethod
     * @param responseCode
     * @return
     */
    private boolean hasResponseBody(RequestMethod requestMethod,int responseCode){
        return requestMethod  != RequestMethod.HEAD&&
                !(responseCode>=100&&responseCode<200)&&
                responseCode!=204&&responseCode!=205&&
                !(responseCode>=300&&responseCode<400);
    }

    /** 拿到服务器的流 **/
    private InputStream getInputStream(HttpURLConnection httpURLConnection, int responseCode) throws IOException{
        InputStream inputStream;
        if(responseCode >= 400){
            inputStream = httpURLConnection.getErrorStream();
        }else{
            inputStream = httpURLConnection.getInputStream();
        }
        //服务器发送过来的数据是否被压缩了
        String contentEncoding = httpURLConnection.getContentEncoding();
        if(contentEncoding!=null&&contentEncoding.contains("gzip")){
            //进行解压操作
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }

    /** 设置请求头 **/
    private void setHeader(HttpURLConnection httpURLConnection,Request request){
        Map<String,String> requestHeader = mRequest.getRequestHeader();
        //处理Content-Type
        String contentType = request.getContentType();
        // 会将用户之前设置的值覆盖
        requestHeader.put("Content-Type",contentType);

        // 处理ContentLength
        long contentLength = request.getContentLength();
        requestHeader.put("Content-Length",String.valueOf(contentLength));

        // 重新设置用户之前设置的请求头
        for (Map.Entry<String, String> stringEntry : requestHeader.entrySet()) {
            String headKey = stringEntry.getKey();
            String headValue = stringEntry.getValue();
            httpURLConnection.setRequestProperty(headKey,headValue);
        }
    }
}
