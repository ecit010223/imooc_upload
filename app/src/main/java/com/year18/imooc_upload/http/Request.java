package com.year18.imooc_upload.http;

import android.text.TextUtils;

import com.year18.imooc_upload.http.Way2.Binary;
import com.year18.imooc_upload.util.CounterOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * 作者：张玉辉 on 2018/5/1 15:14.
 */
public abstract class Request<T> {
    private String mUrl;
    private RequestMethod mMethod;
    /** 请求参数 **/
    private List<KeyValue> mKeyValues;
    private SSLSocketFactory mSSLSocketFactory;
    private HostnameVerifier mHostnameVerifier;
    /** 请求头 **/
    private Map<String,String> mRequestHeader;
    /** ContentType只在用户把所有的参数设置好之后才能产生,不能在设置mRequestHeader处设置 **/
    private String mContentType;
    /**  是否强制开启表单提交 **/
    private boolean mEnableFormData;
    /** 代表表间之间的分割符**/
    private String boundary = createBoundary();
    private String startBoundary = "--"+boundary;
    private String endBoundary = startBoundary+"--";
    private String mCharSet = "utf-8";

    public Request(String url) {
        this(url,RequestMethod.GET);
    }

    public Request(String url, RequestMethod method) {
        this.mUrl = url;
        this.mMethod = method;
        mKeyValues = new ArrayList<>();
        mRequestHeader = new HashMap<>();
    }

    /** 外部设置请求头,重复设置会被覆盖 **/
    public void setRequestHeader(String key,String value) {
        mRequestHeader.put(key,value);
    }

    /** 拿到请求头 **/
    Map<String,String> getRequestHeader(){
        return mRequestHeader;
    }

    /**
     * 有文件ContentType与普通的Stream是不一样的
     * @param contentType
     */
    public void setContentType(String contentType){
        this.mContentType = contentType;
    }

    public void setCharset(String charSet){
        this.mCharSet = charSet;
    }

    public String getContentType(){
        if(!TextUtils.isEmpty(mContentType)) {
            // 返回开发者自己设置的ContentType
            return this.mContentType;
        }else if(mEnableFormData|| hasFile()){  //是否强制表单提交,是否有文件（文件只能通过模拟表单和body提交）
            // Content-Type:multipart/form-data;boundary=sfdsdfsdfasfdsf(随机字符串,用作表单之间的分割符)
            // ------------------------------------------------
            // ------------表单中的String Item---------------
            // --boundary  //前面有两个“-”表示开始分割符
            // Content-Disposition:form-data; name="keyName"
            // Content-Type:text/plain; charset="utf-8"
            //
            // String数据......
            // ------------表单中的File Item------------------
            // --boundary
            // Content-Disposition:form-data; name="keyName"; filename="dcx.jpg"
            // Content-Type:image/jpeg
            //
            // file stream
            // startBoundary--  //开始分割符后面有两个“-”表示结束分割符
            return "multipart/form-data;boundary="+boundary;
        }
        //如果用户没有设置，并且没有文件，则视为一般性的提交
        return "application/x-www-form-urlencoded";
    }

    /** 判断是否还有文件  **/
    protected boolean hasFile(){
        for (KeyValue keyValue : mKeyValues) {
            Object value = keyValue.getValue();
//            if(value instanceof File){
            if(value instanceof Binary){  // 修改为way2方式
                return true;
            }
        }
        return false;
    }

    /**
     * 当上传文件过大时，还需设置文件长度
     */
    public long getContentLength(){
        //post请求的才需知道文件大小,普通数据的post不需要Content-Length
        CounterOutputStream counterOutputStream = new CounterOutputStream();
        try {
            onWriteBody(counterOutputStream);
        } catch (Exception e) {
            return 0;
        }
        return counterOutputStream.get();
    }

    public void onWriteBody(OutputStream outputStream) throws IOException{
        if(mEnableFormData||hasFile()){
            writeFormData(outputStream);
        }else{
            writeStringData(outputStream);
        }
    }

    /**
     * 写出普通数据.
     */
    private void writeStringData(OutputStream outputStream) throws IOException{
        String params = buildParamsString();
        outputStream.write(params.getBytes());
    }

    /**
     * 写出表单数据
     **/
    private void writeFormData(OutputStream outputStream) throws IOException {
        for (KeyValue keyValue : mKeyValues) {
            String key = keyValue.getKey();
            Object value = keyValue.getValue();
//            if(value instanceof File){
            if(value instanceof Binary){  //使用way2方式实现上行代码
//                writeFormFileData(outputStream,key,(File)value);
                writeFormFileData(outputStream,key,(Binary) value);
            }else{  //String
                writeFormStringData(outputStream,key,(String)value);
            }
            outputStream.write("\r\n".getBytes());//每个Item写完后换行
        }
        outputStream.write(endBoundary.getBytes());
    }

    /**
     * 写出表单中的文件item
     // ------------表单中的File Item------------------
     // --boundary
     // Content-Disposition:form-data; name="keyName"; filename="dcx.jpg"
     // Content-Type:image/jpeg
     //
     // file stream
     */
//    private void writeFormFileData(OutputStream outputStream, String key, File value) throws IOException{
//        String fileName = value.getName();
//
//        String mimeType = "application/octet-stream";  //默认值，流形式
//        if(MimeTypeMap.getSingleton().hasExtension(fileName)){
//            String extendsion = MimeTypeMap.getFileExtensionFromUrl(fileName);
//            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extendsion);
//        }
//
//        String builder = startBoundary + "\r\n" +
//                "Content-Disposition:form-data; name=\"" +
//                key + "\"; filename=\"" +
//                fileName +
//                "\"" +
//                "\r\n" +
//                "Content-Type:" +
//                mimeType +
//                "\r\n" +
//                "\r\n";
//        outputStream.write(builder.getBytes(mCharSet));
//
//        if(outputStream instanceof CounterOutputStream){
//            ((CounterOutputStream)outputStream).write(value.length());
//        }else{
//            InputStream inputStream = new FileInputStream(value);
//            byte[] buffer = new byte[2048];
//            int len;
//            while((len=inputStream.read(buffer))!=-1){
//                outputStream.write(buffer,0,len);
//            }
//        }
//    }

    /**
     * 用way2方式实现上面方法的功能
     * 写出表单中的文件item
     // ------------表单中的File Item------------------
     // --boundary
     // Content-Disposition:form-data; name="keyName"; filename="dcx.jpg"
     // Content-Type:image/jpeg
     //
     // file stream
     */
    private void writeFormFileData(OutputStream outputStream, String key, Binary value) throws IOException{
        String fileName = value.getFileName();
        String mimeType = value.getMimeType();
        String builder = startBoundary + "\r\n" +
                "Content-Disposition:form-data; name=\"" +
                key + "\"; filename=\"" +
                fileName +
                "\"" +
                "\r\n" +
                "Content-Type:" +
                mimeType +
                "\r\n" +
                "\r\n";
        outputStream.write(builder.getBytes(mCharSet));

        if(outputStream instanceof CounterOutputStream){
            ((CounterOutputStream)outputStream).write(value.getBinaryLength());
        }else{
//            InputStream inputStream = new FileInputStream(value);
//            byte[] buffer = new byte[2048];
//            int len;
//            while((len=inputStream.read(buffer))!=-1){
//                outputStream.write(buffer,0,len);
//            }
            //用way方式实现上面注释代码
            value.onWriteBinary(outputStream);
       }
    }

    /**
     * 写出表单中的Stream item
     // ------------表单中的String Item---------------
     // --boundary  //前面有两个“-”表示开始分割符
     // Content-Disposition:form-data; name="keyName"
     // Content-Type:text/plain; charset="utf-8"
     //
     // String数据.....
     */
    private void writeFormStringData(OutputStream outputStream, String key, String value) throws IOException {
        String builder = startBoundary + "\r\n" +
                "Content-Disposition:form-data; name=\"" +
                key + "\"" + "\r\n" +
                "Content-Type:text/plain; charset=\"" +
                mCharSet + "\"" + "\r\n" +
                "\r\n\r\n" + value;
        outputStream.write(builder.getBytes(mCharSet));
    }

    /** 是否强制开启表单提交 **/
    public void openFormData(boolean enable){
        if(!mMethod.isOutputMethod()){
            throw  new IllegalArgumentException(mMethod.value() +" is not support output stream");
        }
        mEnableFormData =enable;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory SSLSocketFactory) {
        mSSLSocketFactory = SSLSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }

    /** 拿到请求的完整的URL **/
    public String getUrl() {
        StringBuilder urlBuilder = new StringBuilder(mUrl);
        String paramsStr = buildParamsString();
        if(!mMethod.isOutputMethod()){  //url:http://www.baidu.com?name=123
            if(paramsStr.length()>0&&mUrl.contains("?")&&mUrl.contains("=")){
                urlBuilder.append("&");
            }else if(paramsStr.length()>0&&!mUrl.endsWith("?")){  //url:http://www.baidu.com?
                urlBuilder.append("?");
            }
            urlBuilder.append(paramsStr);
        }
        return urlBuilder.toString();
    }

    public RequestMethod getMethod() {
        return mMethod;
    }

    List<KeyValue> getKeyValues() {
        return mKeyValues;
    }

    public void add(String key, String value){
        mKeyValues.add(new KeyValue(key,value));
    }

    /**
     * 添加文件
     * @param key
     * @param value
     */
//    public void add(String key, File value){
//        mKeyValues.add(new KeyValue(key,value));
//    }

    /**
     * 用way2方式使用上面的方法
     * @param key
     * @param value
     */
    public void add(String key, Binary value){
        mKeyValues.add(new KeyValue(key,value));
    }


    public void add(String key, int value){
        mKeyValues.add(new KeyValue(key,String.valueOf(value)));
    }

    public void add(String key, long value){
        mKeyValues.add(new KeyValue(key,String.valueOf(value)));
    }

    protected String createBoundary(){
        StringBuilder stringBuilder = new StringBuilder("--IMooc"); //IMooc可随便写
        stringBuilder.append(UUID.randomUUID());
        return stringBuilder.toString();
    }

    /** 构造用户添加的所有String参数 **/
    protected String buildParamsString(){
        StringBuilder builder = new StringBuilder();
        for (KeyValue keyValue : mKeyValues) {
            Object value = keyValue.getValue();
            if(value instanceof String){
                builder.append("&");
                try {
                    builder.append(URLEncoder.encode(keyValue.getKey(),mCharSet));
                    builder.append("=");
                    builder.append(URLEncoder.encode(String.valueOf(value),mCharSet));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if(buildParamsString().length()>0){
            //结果是：&key=value&key1=value1
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 解析服务器的数据
     * @param responseBody
     * @return
     */
    public abstract T parseResponse(byte[] responseBody) throws Exception;

    @Override
    public String toString() {
        return "mUrl:"+ mUrl +",mMethod:"+ mMethod +",params:"+ mKeyValues.toString();
    }
}
