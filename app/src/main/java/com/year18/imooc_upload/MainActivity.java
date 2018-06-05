package com.year18.imooc_upload;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.year18.imooc_upload.entity.UserInfo;
import com.year18.imooc_upload.http.HttpListener;
import com.year18.imooc_upload.http.Request;
import com.year18.imooc_upload.http.RequestExecutor;
import com.year18.imooc_upload.http.RequestMethod;
import com.year18.imooc_upload.http.Response;
import com.year18.imooc_upload.http.Way2.FileBinary;
import com.year18.imooc_upload.http.error.ParseError;
import com.year18.imooc_upload.http.error.TimeoutError;
import com.year18.imooc_upload.http.error.URLError;
import com.year18.imooc_upload.http.error.UnknownHostError;
import com.year18.imooc_upload.http.request.StringRequest;
import com.year18.imooc_upload.util.Constant;
import com.year18.imooc_upload.util.Logger;
import com.year18.imooc_upload.util.ThreadUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GET、HEAD请求特点：
 *  1.不建议发送过大数据
 *  2.参数以url?key=value形式拼接url末尾
 *  3.不能获取OutputStream
 * POST、PUT、DELETE请求特点：
 *  1.一般发送大数据
 *  2.可以把任何数据以body形式write到服务器端
 *  3.可以以表单形式发送数据、传文件等
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
        findViewById(R.id.btn_head).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
//                getRequest();
                getRequest2();
                break;
            case R.id.btn_post:
//                postRequest();
                break;
            case R.id.btn_head:
//                headRequest();
                break;
        }
    }

    /**
     * 封装后的请求
     */
    private void getRequest2(){
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file1 = new File(rootPath+"/image/01.jpg");
        File file2 = new File(rootPath+"/image/02.jpg");

//        Request<String> request = new Request<String>(Constant.URL_BASE, RequestMethod.POST){
//
//            @Override
//            public String parseResponse(byte[] responseBody) {
//                //解析返回的数据
//                if(responseBody!=null&&responseBody.length>0){
//                    return new String(responseBody);
//                }
//                return " ";
//            }
//        };
        //对Request进行封装
        Request<String> request = new StringRequest(Constant.URL_BASE, RequestMethod.POST);

        request.add("username","hoho");
//        request.add("image",file1);
        //用way2方式实现
        request.add("image",new FileBinary(file1));
        RequestExecutor.INSTANCE.execute(request, new HttpListener<String>() {
            @Override
            public void onSuccessed(Response<String> response) {
                String str = response.get();
            }

            @Override
            public void onFailed(Exception e) {
                if(e instanceof ParseError){
                    //数据解析异常
                }else if(e instanceof TimeoutError){
                    //超时异常
                }else if(e instanceof UnknownHostError){
                    //找不到服务器
                }else if(e instanceof URLError){
                    //URL错误
                }else{

                }
            }

//            @Override
//            public void onSuccessed(Response<String> response) {
//                Logger.i(response.getResponseCode());
//                byte[] responseBody = response.getResponseBody();
//                String str = new String(responseBody);
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//            }
        });
    }

    /**
     * get请求
     **/
    private void getRequest() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                executeGet();
            }
        });
    }

    /**
     * 在子线程中执行
     **/
    private void executeGet() {
        //建立连接
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            url = new URL(Constant.URL_BASE);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            //设置请求方式
            httpURLConnection.setRequestMethod("GET");

            //设置请求头
//        httpURLConnection.setRequestProperty();

            //get与head请求不能获取输出流
//        httpURLConnection.getOutputStream();

            httpURLConnection.connect();

            //获取响应码
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                readServiceData(inputStream);  //Head无body,读取为空
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * head请求
     **/
    private void headRequest() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                executeHead();
            }
        });
    }

    private void executeHead() {
        //建立连接
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            url = new URL(Constant.URL_BASE);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            //设置请求方式，HEAD服务器只能发送响应头，不能发送body,客户端也读取不到body
            httpURLConnection.setRequestMethod("HEAD");

            //设置请求头
//        httpURLConnection.setRequestProperty();

            //get与head请求不能获取输出流
//        httpURLConnection.getOutputStream();

            httpURLConnection.connect();

            //获取响应码
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                Map<String,List<String>> headFields = httpURLConnection.getHeaderFields();
                Set<Map.Entry<String, List<String>>> entrySet = headFields.entrySet();
                for (Map.Entry<String, List<String>> entry : entrySet) {
                    String headKey = entry.getKey();
                    List<String> headValues = entry.getValue();
                    Logger.i("head key:"+headKey+"------");
                    for (String headValue : headValues) {
                        Logger.i("head value:"+headValue);
                    }
                }
                inputStream = httpURLConnection.getInputStream();
                readServiceData(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post请求
     **/
    private void postRequest() {
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                executePost();
            }
        });
    }

    private void executePost() {
        //建立连接
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            url = new URL(Constant.URL_BASE);

            httpURLConnection = (HttpURLConnection) url.openConnection();

            //设置请求方式
            httpURLConnection.setRequestMethod("POST");

            //设置请求头
//        httpURLConnection.setRequestProperty();

            httpURLConnection.connect();

            //POST方式可以获取输出流，实例一：
//            outputStream = httpURLConnection.getOutputStream();
//            String iLikeYou = "I like you";
//            outputStream.write(iLikeYou.getBytes());

            /**************POST方式可以获取输出流，实例二：*****************/
            //提交是的json，则请求头也设置一下
            httpURLConnection.setRequestProperty("ContentType","application/json");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("IMooc");
            userInfo.setPassword("123456");
            userInfo.setSex("Man");

            String json = JSON.toJSONString(userInfo);
            Logger.d(json);

            //获取响应码
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                readServiceData(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取输入流
     */
    public void readServiceData(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        String data = new String(outputStream.toByteArray());
        Logger.i("服务器响应数据：" + data);
    }
}
