package com.longway.litesdk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by longway on 16/6/14.
 * Email:longway1991117@sina.com
 */

public class HttpsActivity extends Activity {
    private static final String TAG = HttpsActivity.class.getSimpleName();
    private static final String URI = "https://kyfw.12306.cn/otn/leftTicket/init";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_theme1);
        new HttpsTask(this).execute(URI);
    }

    private static class HttpsTask extends AsyncTask<String, Void, String> {
        private WeakReference<HttpsActivity> weakReference;

        public HttpsTask(HttpsActivity httpsActivity) {
            weakReference = new WeakReference<>(httpsActivity);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection httpsURLConnection = null;
            InputStream inputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            InputStream cerInputStream;
            URL url;
            try {
                setDefault();
                url = new URL(URI);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                //创建X .509 格式的CertificateFactory
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                //从asserts中获取证书的流
                cerInputStream = weakReference.get().getAssets().open("SRCA.crt");//SRCA.cer
                //ca是java.security.cert.Certificate，不是java.security.Certificate，
                //也不是javax.security.cert.Certificate
                Certificate ca;
                try {
                    //证书工厂根据证书文件的流生成证书Certificate
                    ca = cf.generateCertificate(cerInputStream);
                } finally {
                    if (cerInputStream != null) {
                        cerInputStream.close();
                        cerInputStream = null;
                    }
                }

                // 创建一个默认类型的KeyStore，存储我们信任的证书
                String keyStoreType = KeyStore.getDefaultType();

                KeyStore keyStore = KeyStore.getInstance(keyStoreType);

                keyStore.load(cerInputStream, null);

                //将证书ca作为信任的证书放入到keyStore中
                keyStore.setCertificateEntry("ca", ca);

                //TrustManagerFactory是用于生成TrustManager的，我们创建一个默认类型的TrustManagerFactory

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);

                //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书

                tmf.init(keyStore);

                //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书

                TrustManager[] trustManagers = tmf.getTrustManagers();

                //创建TLS类型的SSLContext对象， that uses our TrustManager

                SSLContext sslContext = SSLContext.getInstance("TLS");

                //用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, null);

                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());

                //通过sslContext获取SSLSocketFactory对象

                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                //将sslSocketFactory通过setSSLSocketFactory方法作用于HttpsURLConnection对象

                //这样conn对象就会信任我们之前得到的证书对象
                //httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
                //setDefault();
                inputStream = httpsURLConnection.getInputStream();
                Log.d(TAG, httpsURLConnection.getRequestMethod());
                byte[] buf = new byte[1024];
                byteArrayOutputStream = new ByteArrayOutputStream();
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } finally {
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                    httpsURLConnection = null;
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        inputStream = null;
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        byteArrayOutputStream = null;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            File filesDir = weakReference.get().getFilesDir();
            File file = new File(filesDir, "test.html");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(s.getBytes());
                Log.d(TAG, "save success");
                Log.e(TAG, s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileOutputStream = null;
                }
            }
        }

        private void setDefault() {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }};
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, null);
                //httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("msg", "router");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
