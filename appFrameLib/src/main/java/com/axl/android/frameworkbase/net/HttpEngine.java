package com.axl.android.frameworkbase.net;

import com.axl.android.frameworkbase.BaseApplication;
import com.axl.android.frameworkbase.BuildConfig;
import com.axl.android.frameworkbase.utils.netstatus.NetUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/5/27
 * Time: 下午4:33
 * Info: Retrofit 请求类
 */

public class HttpEngine {
    private static String BASE_URL = null;
    private static Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttpClient = null;

    private static final String NET_CACHE = BaseApplication.getAppCacheDir() + File.separator + "NetCache";

    private void init() {
        initOkHttp();
        initRetrofit();
    }

    private HttpEngine() {
        init();
    }

    public static void init(String BASE_URL) {
        HttpEngine.BASE_URL = BASE_URL;
    }

    public static HttpEngine getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpEngine INSTANCE = new HttpEngine();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(NET_CACHE);
        if (!cacheFile.exists())
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!NetUtils.isNetworkConnected(BaseApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            if (!NetUtils.isNetworkConnected(BaseApplication.getAppContext())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return newBuilder.build();
        };

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(Logger::d);
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }


        builder.cache(cache).addInterceptor(cacheInterceptor);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(chain -> {
            Logger.i("GzipRequestInterceptor　chain.request().toString():" + chain.request().toString());
            Request request = chain.request()
                    .newBuilder()
                    .header("Content-Encoding", "gzip")
                    .build();
            Logger.i("GzipRequestInterceptor　request.toString():" + request.toString());
            return chain.proceed(request);
        });

        //设置超时
        builder.connectTimeout(70, TimeUnit.SECONDS);
        builder.readTimeout(70, TimeUnit.SECONDS);
        builder.writeTimeout(70, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        builder.hostnameVerifier((s, sslSession) -> true);
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sOkHttpClient = builder.build();

    }


    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(sOkHttpClient)
                .baseUrl(BASE_URL)
                .build();
    }

    public <T> T createServices(Class<T> service) {
        return sRetrofit.create(service);
    }


    public class GzipRequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null) {
                Request newRequest = originalRequest.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .build();
                return chain.proceed(newRequest);
            }

            if (originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }
            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), forceContentLength(gzip(originalRequest.body())))
                    .build();
            return chain.proceed(compressedRequest);
        }

        /**
         * https://github.com/square/okhttp/issues/350
         */
        private RequestBody forceContentLength(final RequestBody requestBody) throws IOException {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return requestBody.contentType();
                }

                @Override
                public long contentLength() {
                    return buffer.size();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    sink.write(buffer.snapshot());
                }
            };
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }
}