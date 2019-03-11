
# Case for Today

A reader application that allows you to translate English text from Moby Dick to Spanish.

![](./images/Translate_screencast.gif)

## The Flow of Information in the App

![](images/translation_activity.png)

# Agenda

  * Intro to HTTP
  * Intro to JSON
  * Intro to Realm DB
  -----
  * Implicit Intents
  

# Intro to HTTP

## A Bit About the Web...

![](https://sureshemre.files.wordpress.com/2014/05/synapticnetwork.png)

## Web Servers

  > Web server refers to server software, or hardware dedicated to running said software, that can serve contents to the World Wide Web. A web server processes incoming network requests over the HTTP protocol (and several other related protocols).
  >
  > https://en.wikipedia.org/wiki/Web_server

The communication between your application and web servers runs likely over a _HTTP_, the _Hyper-text Transfer Protocol_, see https://www.w3.org/History/1989/proposal.html.

The protocol was developed by Tim Berners-Lee at CERN in 1989 and forms the basis of the _Web_ as you know it.

![](http://static3.businessinsider.com/image/578f5dfedd0895cb0c8b456e-403-302/tim-berners-lee-the-inventor-of-the-web-has-3-big-fears-for-its-future.jpg)

### The first webserver...

<img src="https://1.bp.blogspot.com/-PClkpypOQag/V6WxPoqsSMI/AAAAAAAApEY/CaJk8sjd-6wG5alOU7FMdUUvQl169UkNgCLcB/s1600/first-web-server.png" alt="Drawing" style="width: 500px;"/>

You can see that your communication runs over HTTP when inspecting the scheme part of a given URL.

For example, 

  * the resource served from http://itu.dk is transfered via HTTP 
  * a call to https://itu.dk transfers the same content via encrypted HTTP, i.e., _secure_.

## Hyper-text Transfer Protocol (HTTP)

  > HTTP is a stateless, connectionless, reliable protocol. In the simplest form, each request from a user agent is handled reliably and then the connection is broken.
  > 
  >
  > HTTP 1.1 added many optimizations in HTTP, which added complexity to the simple structure, but created a more efficient and reliable protocol. 
  > 
  > HTTP/2 has adopted a binary form for further efficient gains.
  >
  > _Network Programming with Go_ Jan Newmarch

### HTTP 0.9 (1991)

Now obsolete...

#### Request format

```
Request = "GET" SP Request-URI CRLF
```

#### Response format

```
Response = [Entity-Body]
```

### HTTP 1.1 (1999)

#### Request format

```
Request = Simple-Request | Full-Request
Simple-Request = "GET" SP Request-URI CRLF
Full-Request = Request-Line
                       *(General-Header
                          | Request-Header
                          | Entity-Header)
                      CRLF
                       [Entity-Body]
Request-Line = Method SP Request-URI SP HTTP-Version CRLF
Method = "GET" | "HEAD" | "POST" | "PUT" | "DELETE" | "OPTIONS" | "TRACE" | "CONNECT" | "PATCH" | ...
```

#### Response format

```
Response = Simple-Response | Full-Response
Simple-Response = [Entity-Body]
Full-Response = Status-Line
                *(General-Header
                   | Response-Header
                   | Entity-Header)
                CRLF
                [Entity-Body]
Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
```

<small>The above grammars are from _Network Programming with Go_ Jan Newmarch</small>

An example of a header of an HTTP 1.1.

```
HTTP/1.1 200 OK                              // status line
Date: Fri, 29 Aug 2003 00:59:56 GMT          // general header
Server: Apache/2.0.40 (Unix)                 // response header
Content-Length: 1595                         // entity header
Content-Type: text/html; charset=ISO-8859-1  // entity header
```

### HTTP in Practice


```bash
%%bash
curl -v http://httpbin.org/uuid
```

    {
      "uuid": "874dcf20-6f6a-4a82-a36c-08d654d0412a"
    }


      % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                     Dload  Upload   Total   Spent    Left  Speed
      0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying 52.71.234.219...
    * TCP_NODELAY set
    * Connected to httpbin.org (52.71.234.219) port 80 (#0)
    > GET /uuid HTTP/1.1
    > Host: httpbin.org
    > User-Agent: curl/7.63.0
    > Accept: */*
    > 
    < HTTP/1.1 200 OK
    < Access-Control-Allow-Credentials: true
    < Access-Control-Allow-Origin: *
    < Content-Type: application/json
    < Date: Mon, 11 Mar 2019 21:31:02 GMT
    < Server: nginx
    < Content-Length: 53
    < Connection: keep-alive
    < 
    { [53 bytes data]
    100    53  100    53    0     0    144      0 --:--:-- --:--:-- --:--:--   144
    * Connection #0 to host httpbin.org left intact


What is really going on? Check what is transmitted and received for example with [`tcpdump`](https://www.tcpdump.org/manpages/tcpdump.1.html) or -in case you like it more visual- [WireShark](https://www.wireshark.org).

```bash
sudo tcpdump -A -vv -X dst httpbin.org
```

```
$ sudo tcpdump -A -vv -X  dst or src host httpbin.org
tcpdump: data link type PKTAP
tcpdump: listening on pktap, link-type PKTAP (Apple DLT_PKTAP), capture size 262144 bytes
16:42:09.009661 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 64)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [S], cksum 0x91fc (correct), seq 4017165466, win 65535, options [mss 1460,nop,wscale 6,nop,nop,TS val 1285755506 ecr 0,sackOK,eol], length 0
	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0040 0000 4000 4006 e68c 0a1c 2aed 3447  .@..@.@.....*.4G
	0x0020:  eadb ed62 0050 ef71 149a 0000 0000 b002  ...b.P.q........
	0x0030:  ffff 91fc 0000 0204 05b4 0103 0306 0101  ................
	0x0040:  080a 4ca3 1272 0000 0000 0402 0000       ..L..r........
16:42:09.113577 IP (tos 0x0, ttl 232, id 0, offset 0, flags [DF], proto TCP (6), length 60)
    ec2-52-71-234-219.compute-1.amazonaws.com.http > 10.28.42.237.60770: Flags [S.], cksum 0x9013 (correct), seq 2922355996, ack 4017165467, win 26847, options [mss 1460,sackOK,TS val 310266929 ecr 1285755506,nop,wscale 8], length 0
	0x0000:  4865 ee1b 96f8 943f c273 7fe2 0800 4500  He.....?.s....E.
	0x0010:  003c 0000 4000 e806 3e90 3447 eadb 0a1c  .<..@...>.4G....
	0x0020:  2aed 0050 ed62 ae2f 9d1c ef71 149b a012  *..P.b./...q....
	0x0030:  68df 9013 0000 0204 05b4 0402 080a 127e  h..............~
	0x0040:  4c31 4ca3 1272 0103 0308                 L1L..r....
16:42:09.113617 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 52)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [.], cksum 0x1f50 (correct), seq 1, ack 1, win 2058, options [nop,nop,TS val 1285755608 ecr 310266929], length 0
	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0034 0000 4000 4006 e698 0a1c 2aed 3447  .4..@.@.....*.4G
	0x0020:  eadb ed62 0050 ef71 149b ae2f 9d1d 8010  ...b.P.q.../....
	0x0030:  080a 1f50 0000 0101 080a 4ca3 12d8 127e  ...P......L....~
	0x0040:  4c31                                     L1
16:42:09.113758 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 131)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [P.], cksum 0x8c43 (correct), seq 1:80, ack 1, win 2058, options [nop,nop,TS val 1285755608 ecr 310266929], length 79: HTTP, length: 79
	GET /uuid HTTP/1.1
	Host: httpbin.org
	User-Agent: curl/7.63.0
	Accept: */*

	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0083 0000 4000 4006 e649 0a1c 2aed 3447  ....@.@..I..*.4G
	0x0020:  eadb ed62 0050 ef71 149b ae2f 9d1d 8018  ...b.P.q.../....
	0x0030:  080a 8c43 0000 0101 080a 4ca3 12d8 127e  ...C......L....~
	0x0040:  4c31 4745 5420 2f75 7569 6420 4854 5450  L1GET./uuid.HTTP
	0x0050:  2f31 2e31 0d0a 486f 7374 3a20 6874 7470  /1.1..Host:.http
	0x0060:  6269 6e2e 6f72 670d 0a55 7365 722d 4167  bin.org..User-Ag
	0x0070:  656e 743a 2063 7572 6c2f 372e 3633 2e30  ent:.curl/7.63.0
	0x0080:  0d0a 4163 6365 7074 3a20 2a2f 2a0d 0a0d  ..Accept:.*/*...
	0x0090:  0a                                       .
16:42:09.217603 IP (tos 0x0, ttl 232, id 53590, offset 0, flags [DF], proto TCP (6), length 52)
    ec2-52-71-234-219.compute-1.amazonaws.com.http > 10.28.42.237.60770: Flags [.], cksum 0x2688 (correct), seq 1, ack 80, win 105, options [nop,nop,TS val 310266955 ecr 1285755608], length 0
	0x0000:  4865 ee1b 96f8 943f c273 7fe2 0800 4500  He.....?.s....E.
	0x0010:  0034 d156 4000 e806 6d41 3447 eadb 0a1c  .4.V@...mA4G....
	0x0020:  2aed 0050 ed62 ae2f 9d1d ef71 14ea 8010  *..P.b./...q....
	0x0030:  0069 2688 0000 0101 080a 127e 4c4b 4ca3  .i&........~LKL.
	0x0040:  12d8                                     ..
16:42:09.218811 IP (tos 0x0, ttl 232, id 53591, offset 0, flags [DF], proto TCP (6), length 324)
    ec2-52-71-234-219.compute-1.amazonaws.com.http > 10.28.42.237.60770: Flags [P.], cksum 0xb014 (correct), seq 1:273, ack 80, win 105, options [nop,nop,TS val 310266956 ecr 1285755608], length 272: HTTP, length: 272
	HTTP/1.1 200 OK
	Access-Control-Allow-Credentials: true
	Access-Control-Allow-Origin: *
	Content-Type: application/json
	Date: Thu, 07 Mar 2019 15:42:09 GMT
	Server: nginx
	Content-Length: 53
	Connection: keep-alive

	{
	  "uuid": "11b0d3aa-6975-4ad1-8bf9-aacad02a5849"
	}
	0x0000:  4865 ee1b 96f8 943f c273 7fe2 0800 4500  He.....?.s....E.
	0x0010:  0144 d157 4000 e806 6c30 3447 eadb 0a1c  .D.W@...l04G....
	0x0020:  2aed 0050 ed62 ae2f 9d1d ef71 14ea 8018  *..P.b./...q....
	0x0030:  0069 b014 0000 0101 080a 127e 4c4c 4ca3  .i.........~LLL.
	0x0040:  12d8 4854 5450 2f31 2e31 2032 3030 204f  ..HTTP/1.1.200.O
	0x0050:  4b0d 0a41 6363 6573 732d 436f 6e74 726f  K..Access-Contro
	0x0060:  6c2d 416c 6c6f 772d 4372 6564 656e 7469  l-Allow-Credenti
	0x0070:  616c 733a 2074 7275 650d 0a41 6363 6573  als:.true..Acces
	0x0080:  732d 436f 6e74 726f 6c2d 416c 6c6f 772d  s-Control-Allow-
	0x0090:  4f72 6967 696e 3a20 2a0d 0a43 6f6e 7465  Origin:.*..Conte
	0x00a0:  6e74 2d54 7970 653a 2061 7070 6c69 6361  nt-Type:.applica
	0x00b0:  7469 6f6e 2f6a 736f 6e0d 0a44 6174 653a  tion/json..Date:
	0x00c0:  2054 6875 2c20 3037 204d 6172 2032 3031  .Thu,.07.Mar.201
	0x00d0:  3920 3135 3a34 323a 3039 2047 4d54 0d0a  9.15:42:09.GMT..
	0x00e0:  5365 7276 6572 3a20 6e67 696e 780d 0a43  Server:.nginx..C
	0x00f0:  6f6e 7465 6e74 2d4c 656e 6774 683a 2035  ontent-Length:.5
	0x0100:  330d 0a43 6f6e 6e65 6374 696f 6e3a 206b  3..Connection:.k
	0x0110:  6565 702d 616c 6976 650d 0a0d 0a7b 0a20  eep-alive....{..
	0x0120:  2022 7575 6964 223a 2022 3131 6230 6433  ."uuid":."11b0d3
	0x0130:  6161 2d36 3937 352d 3461 6431 2d38 6266  aa-6975-4ad1-8bf
	0x0140:  392d 6161 6361 6430 3261 3538 3439 220a  9-aacad02a5849".
	0x0150:  7d0a                                     }.
16:42:09.218842 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 52)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [.], cksum 0x1d72 (correct), seq 80, ack 273, win 2054, options [nop,nop,TS val 1285755712 ecr 310266956], length 0
	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0034 0000 4000 4006 e698 0a1c 2aed 3447  .4..@.@.....*.4G
	0x0020:  eadb ed62 0050 ef71 14ea ae2f 9e2d 8010  ...b.P.q.../.-..
	0x0030:  0806 1d72 0000 0101 080a 4ca3 1340 127e  ...r......L..@.~
	0x0040:  4c4c                                     LL
16:42:09.221316 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 52)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [F.], cksum 0x1d6f (correct), seq 80, ack 273, win 2054, options [nop,nop,TS val 1285755714 ecr 310266956], length 0
	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0034 0000 4000 4006 e698 0a1c 2aed 3447  .4..@.@.....*.4G
	0x0020:  eadb ed62 0050 ef71 14ea ae2f 9e2d 8011  ...b.P.q.../.-..
	0x0030:  0806 1d6f 0000 0101 080a 4ca3 1342 127e  ...o......L..B.~
	0x0040:  4c4c                                     LL
16:42:09.325010 IP (tos 0x0, ttl 232, id 53592, offset 0, flags [DF], proto TCP (6), length 52)
    ec2-52-71-234-219.compute-1.amazonaws.com.http > 10.28.42.237.60770: Flags [F.], cksum 0x24f1 (correct), seq 273, ack 81, win 105, options [nop,nop,TS val 310266982 ecr 1285755714], length 0
	0x0000:  4865 ee1b 96f8 943f c273 7fe2 0800 4500  He.....?.s....E.
	0x0010:  0034 d158 4000 e806 6d3f 3447 eadb 0a1c  .4.X@...m?4G....
	0x0020:  2aed 0050 ed62 ae2f 9e2d ef71 14eb 8011  *..P.b./.-.q....
	0x0030:  0069 24f1 0000 0101 080a 127e 4c66 4ca3  .i$........~LfL.
	0x0040:  1342                                     .B
16:42:09.325064 IP (tos 0x0, ttl 64, id 0, offset 0, flags [DF], proto TCP (6), length 52)
    10.28.42.237.60770 > ec2-52-71-234-219.compute-1.amazonaws.com.http: Flags [.], cksum 0x1ced (correct), seq 81, ack 274, win 2054, options [nop,nop,TS val 1285755817 ecr 310266982], length 0
	0x0000:  0000 5e00 0179 4865 ee1b 96f8 0800 4500  ..^..yHe......E.
	0x0010:  0034 0000 4000 4006 e698 0a1c 2aed 3447  .4..@.@.....*.4G
	0x0020:  eadb ed62 0050 ef71 14eb ae2f 9e2e 8010  ...b.P.q.../....
	0x0030:  0806 1ced 0000 0101 080a 4ca3 13a9 127e  ..........L....~
	0x0040:  4c66                                     Lf

```

## URLs? URIs???

Structure of URIs: 


```
                    hierarchical part
        ┌───────────────────┴─────────────────────┐
                    authority               path
        ┌───────────────┴───────────────┐┌───┴────┐
  abc://username:password@example.com:123/path/data?key=value&key2=value2#fragid1
  └┬┘   └───────┬───────┘ └────┬────┘ └┬┘           └─────────┬─────────┘ └──┬──┘
scheme  user information     host     port                  query         fragment
```


Source: https://en.wikipedia.org/wiki/Uniform_Resource_Identifier#Examples.

### Your Turn!

  * What is the meaning of _URI_?
  * What is the meaning of _URL_?
  * What is the difference between a _URI_ and a _URL_?

## HTTP Requests from Plain Java


```java
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGet {

	public byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlString);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
	
	
	public static void main(String[] args) {
		// Equivalent to:
		// curl http://httpbin.org/uuid
		HTTPGet httpGetter = new HTTPGet();
		String urlString = "http://httpbin.org/uuid";
		
		byte[] response;
		try {
			response = httpGetter.getUrlBytes(urlString);
			String responseString = new String(response);
			System.out.println(responseString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```

That is, HTTP 1.1 is a plain text protocol in which you open a TCP connection to a host and write the bytes corresponding to you request on to that connection.



To prevent everyone reading your messages use encrypted HTTP!

Use the URI:

https://httpbin.org/uuid


instead of:

http://httpbin.org/uuid

## Enabling HTTP Requests in Your App

Add the following line above the `<application>` element in your `AndroidManifest.xml`.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Source: https://developer.android.com/training/basics/network-ops/connecting.html

## HTTP Requests from Android


```java
package dk.itu.helge.httpgetfrommainthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String urlString = "https://httpbin.org/uuid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        byte[] response;
        try {
            response = getUrlBytes(urlString);
            String responseString = new String(response);
            textView.setText(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlString);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
```

### Your Turn!

Go and clone the repository from

https://github.com/helgecph/Network_and_Realm_Lecture

```bash
$ git clone https://github.com/HelgeCPH/Network_and_Realm_Lecture.git
```

Build the Android project `HTTPGetFromMainThread`.

  * Discuss with your neighbor what is going on.
  * What is the issue?

The issue:



  * How to make this work?
    - Use pages the 480 and 494 in the book to make this example work.
    
**Hints:**

  * You have to extend the class `AsyncTask`
  * and override the two methods `doInBackground` and `onPostExecute`.

### A Possible Solution



Find a more elaborate example at [https://github.com/googlesamples/android-NetworkConnect](https://github.com/googlesamples/android-NetworkConnect)

##  HTTP Request via Volley

We build the example using regular Java libraries. There HTTP interactions are a bit hairy.

For your projects it is likely worth to check the [Volley](https://github.com/google/volley) library, which makes HTTP calls and responses more easy and more efficient.

<img src="https://i1.wp.com/tutorial.eyehunts.com/wp-content/uploads/2018/06/Volley-android-example-and-tutotiral-HTTP-GET.png" alt="Drawing" style="width: 700px;"/>

To use it you have to add the following line to the dependencies of your `build.gradle (Module: app)` file.

```groovy
dependencies {
    ...
    implementation 'com.android.volley:volley:1.1.1'
}
```

## HTTP Requests from Android with Volley

```java
package dk.itu.helge.httpgetvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private TextView textView;
    private static String urlString = "https://httpbin.org/uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        httpGet(urlString);
    }

    public void httpGet(String urlString) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringReq = new StringRequest(Request.Method.GET, urlString,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    textView.setText(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG,"Error :" + error.toString());
                }
            });
        queue.add(stringReq);
    }
}
```

# Intro to JSON

So far, we were receiving JSON responses just as a stream of bytes, which we then converted into strings.



JavaScript Object Notation (JSON) is a lightweight data interchange format. Unlike with XML you cannot specify schemas for your JSON documents. However, an attempt on such schemas is currently ongoing: https://json-schema.org.


A JavaScript object:

```javascript
let reponse = {
  translations: [{
    translation: "El elefante en la habitación."
  }],
  word_count : 6,
  character_count : 25
};
```

in JSON:

```json
{
  "translations" : [ {
    "translation" : "El elefante en la habitación."
  } ],
  "word_count" : 6,
  "character_count" : 25
}
```

## Receiving a JSON Translation Object from IBM Watson Translate Service


```bash
%%bash
curl -u "apikey:-BV_YJRiNag-61Gb99oGLZFqV8tiXzckdO0zB-hvbOMz" \
    --request POST \
    --header "Content-Type: application/json" \
    --data "{\"text\":[\"The elephant in the room.\"],\"model_id\":\"en-es\"}" \
    "https://gateway-lon.watsonplatform.net/language-translator/api/v3/translate?version=2018-05-01"
```

    {
      "translations" : [ {
        "translation" : "El elefante en la habitación."
      } ],
      "word_count" : 5,
      "character_count" : 25
    }

      % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                     Dload  Upload   Total   Spent    Left  Speed
    100   188  100   131  100    57     87     37  0:00:01  0:00:01 --:--:--   125


**OBS**: I will revoke the API Key on Thursday. Then you have to replace it with your own.

## IBM Watson

IBM Watson provides many AI webservices. The guest lecturer Lars Kjær Ipsen will tell you way more about these services and how to use them.

In this lecture, I am going to use the _Translate_ webservice. To exemplify:

  * performing HTTP requests
  * parsing JSON from HTTP responses
  * using the object database Realm

Find information on how to use and setup IBM Watson services:

  * API documentation: https://console.bluemix.net/apidocs/language-translator
  * https://console.bluemix.net/docs/services/language-translator/getting-started.html#gettingstarted

  
After registering you get 1000 requests per month and per service for free.

## Using IBM Watson Translation Service


  * Navigate to https://www.ibm.com/watson/services/language-translator/
  ![](images/ibm_watson_1.png)
  * Click under **Resource summary** `Add more resources`
  ![](images/ibm_watson_2.png)
  * Choose `AI` in the menu to the left and then select `Language Translator`
  ![](images/ibm_watson_3.png)
  * Remain with the default values and click `create`
  ![](images/ibm_watson_4.png)
  * Thereafter, copy the API key, which you can use in your app and on the command line
  
  

# Parsing JSON from Strings

The information below is just a string:

```
{
  "translations" : [ {
    "translation" : "El elefante en la habitación."
  } ],
  "word_count" : 6,
  "character_count" : 25
}
```

```java
try {
    JSONObject jsonBody = new JSONObject(jsonResponse);
    JSONArray translationsFromResponse = jsonBody.getJSONArray("translations");

    List<String> translations = new ArrayList<>();
    for (int i = 0; i < translationsFromResponse.length(); i++) {
        JSONObject translationJsonObject = translationsFromResponse.getJSONObject(i);

        String translation = translationJsonObject.getString("translation");
        translations.add(translation);
    }
    
    int characterCount = jsonBody.getInt("character_count");
    int wordCount = jsonBody.getInt("word_count");
} catch (JSONException e) {
    e.printStackTrace();
}
```

# Introduction to Realm

## The Object-Relational Mismatch

![](http://www.agiledata.org/images/impedanceMismatchClassDiagram.gif)

![](http://www.agiledata.org/images/impedanceMismatchPDM.gif)

http://www.agiledata.org/essays/impedanceMismatch.html

Using an object database frees you from the translation to the relational model. Your data objects are the data model directly.

## Realm an Object Database
    
<img src="https://realm.io/assets/svg/general_logo.svg" alt="Drawing" style="width: 30%;"/>

  > Our object database is a simple alternative to SQLite and Core Data... 
  >
  > https://realm.io/
 
 
 

<img src="https://venturebeat.com/wp-content/uploads/2015/03/Realm-Alex-Bjarne-lower-res.png?w=1000&strip=all" alt="Drawing" style="width: 40%;"/>
Realm founders Alexander Stigsen and Bjarne Christiansen, who worked earlier for Nokia in Copenhagen.


They have an office in Copenhagen in case you are interested in your later career.

### Features

  * Offline-first functionality
  * Fast queries
  * Safe threading
  * Cross-platform apps
  * Encryption
  * Reactive architecture
  
https://realm.io/products/realm-database

### Storing your data

  * In memory
  * Local storage (in this lecture), Realm DB
  * Remote storage as a shared DB (in your exercises), Realm platform
  
  
https://realm.io/docs/java/latest/

<img src="https://images.contentful.com/emmiduwd41v7/4BZWBXxaP6oA2OKYymCwiW/74d169bec3e0774dc00b0f723a6c88a0/Screen_Shot_2016-09-26_at_8.56.48_PM.png" alt="Drawing" style="width: 60%;"/>

### Initializing Realm

```java
public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Realm (just once per application)
        Realm.init(this);
        ...
    }
}
```

In each thread in which you want to interact with the database

```java
Realm realm = Realm.getDefaultInstance();
```

### Querying for Objects from DB

```java
RealmResults<TranslationModel> results = realm.where(TranslationModel.class).findAll();
```

```
[TranslationModel = proxy[
    {text:Call me Ishmael.},
    {wordCount:3},
    {characterCount:16},
    {translations:RealmList<String>[1]}], 
 TranslationModel = proxy[
    {text:I thought I would sail about a little and see the watery part},
    {wordCount:13},
    {characterCount:61},
    {translations:RealmList<String>[1]}]]
```

Queries are evaluated lazily, remember sequences in Kotlin? Just like that!

### Mapping Your Application Model to DB

No DB schemas as such anymore. Your model classes are directly handled by the database, as long as you subclass `RealmObject`.


```java
public class TranslationModel extends RealmObject {

    @PrimaryKey
    private String text;
    private int wordCount;
    private int characterCount;
    private RealmList<String> translations;

    // Getters and setters according to your liking
    ...
}
```

### Create Objects from JSON Stings Directly

```java
public class TranslationModel extends RealmObject {

    @PrimaryKey
    private String text;
    private int wordCount;
    private int characterCount;
    private RealmList<String> translations;
}

// Insert from a string
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        String obj = "{\"text\": \"Hello\", \"translations\" : " + 
                     "[ { \"translation\" : \"Hola\" } ]," + 
                     "\"word_count\" : 1, \"character_count\" : 5}"
        realm.createObjectFromJson(TranslationModel.class, obj);
    }
});
```

We have a class `HTTPRequester` which wraps an HTTP POST call to the IBM Watson translate API via `sendTranslationRequest`.


```java
text = "Tell me the meaning of live."
String remoteTranslation = new HTTPRequester().sendTranslationRequest(text);
```

After calling the above, the variable `remoteTranslation` contains the following string:

```
{
  "translations" : [ {
    "translation" : "Cuénteme el significado de vivir."
  } ],
  "word_count" : 6,
  "character_count" : 27
}
```

### Writing to a DB

```java
Realm realm = Realm.getDefaultInstance();

String remoteTranslation = new HTTPRequester().sendTranslationRequest(text);

TranslationModel tm = Translator.createModel(text, remoteTranslation);

// push it to the DB
realm.beginTransaction();
realm.copyToRealm(tm);
realm.commitTransaction();

RealmList<String> remoteTranslations = tm.getTranslations();
translations.addAll(remoteTranslations);
realm.close();
```


#### You should not write to Realm from the Main Thread

![](images/translation_activity.png)

```java
realm.executeTransactionAsync(new Realm.Transaction() {
        @Override
        public void execute(Realm bgRealm) {
            bgRealm.copyToRealm(tm);
        }
    }, new Realm.Transaction.OnSuccess() {
        @Override
        public void onSuccess() {
            // Transaction was a success.
        }
    }, new Realm.Transaction.OnError() {
        @Override
        public void onError(Throwable error) {
            // Transaction failed and was automatically canceled.
        }
    });
```

# Implicit Intents


  > An intent allows you to start an activity in another app by describing a simple action you'd like to perform (such as "view a map" or "take a picture") in an Intent object. This type of intent is called an implicit intent because it does not specify the app component to start, but instead specifies an action and provides some data with which to perform the action.
  >
  > https://developer.android.com/guide/components/intents-common.html


![](https://www.tutlane.com/images/android/android_implicit_intents_process_flow.png)

Explicit Intents (class name of activity to be started)

```java
Intent i = new Intent(getActivity(), TheActivity.class); 
startActivity(i);
```

Implicit Intents (search for functionality)

```java
Intent i= new Intent(Intent.ACTION_VIEW, ... );
startActivity(i);
```

### Calling Activities via Implicit Intents

```java
package dk.itu.helge.implicitintenddemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showWebPage(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.itu.dk"));
        startActivity(intent);
    }
}
```

  > Caution: If there are no apps on the device that can receive the implicit intent, your app will crash when it calls `startActivity()`. To first verify that an app exists to receive the intent, call `resolveActivity()` on your Intent object. If the result is non-null, there is at least one app that can handle the intent and it's safe to call `startActivity()`. If the result is null, you should not use the intent and, if possible, you should disable the feature that invokes the intent.
  > 
  > https://developer.android.com/guide/components/intents-common.html

### Receiving Implicit Intents


`AndroidManifest.xml`

```xml
...
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data android:scheme="https" />
        </intent-filter>
    </activity>
...
```

```java
package dk.itu.helge.ituwebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = getIntent().getDataString();
        if (null != url) {
            WebView webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }
}
```

<img src="images/intent_selection.png" alt="Drawing" style="width: 30%;"/>

# Next Week


  * Getting a video stream from the camera
  * Android Media Player
  * Location aware apps
  * Introduction to OpenCV
