# log-parser

The task is to implement a log parser to scrap some of the details from access log file.
The scrapped information is stored in the in-memory H2 database for convenient of use.

The implementation performs following set of operations.

* Identifying the number of unique IP addresses within a given time-range. 
* The top-n visited URLs within a given time range. The URL's with the return status 4xx and 5xx are ignored.
* The top-n most active IP addresses. 

If the time-range is not provided, the whole duration is considered with each request.

LOG Format
----------

The Log format of the file adheres to the following regular expression.

**^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] "(.+?)" (\\d{3}) (\\d+) "([^"]+)" "([^"]+)".*$**

One sample entry of the log is mentioned below.

**72.44.32.10 - - [09/Jul/2018:15:48:07 +0200] "GET / HTTP/1.1" 200 3574 "-" "Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0" junk extra **

The size of the log file could be varied. Therefore, in order to support large files, NIO package has been used( Lazy File Reading).

Execution Steps
---------------

(1) The application is created using java8, spring-boot and maven. 
    Following command could be used to build the code.
    
    mvn package install
    
(2) The relevant jar file is created in the **target** directory. Hence, the following 
    command could be used to run the jar.
    
    java -jar target/log-parser-1.0.0.jar --log=<PATH>/access.log
    
    ** --log param is used to set the path of the access log file to be processed by the app.

(3) The tool uses a in-mem h2 database for ease of packing in this use case. JPA layer is 
    used to access the h2 database.
    
(4) The relevant SQL file could be viewed in the **src/main/resources/data.sql** location. 

Test Cases
==========

* The tool consists of set of unit test cases to validate the functionality.
* Each functionality is exposed as a REST service. Hence the services could be tested over the 
  Postman as well.
  
  Scenario - 1 ( Identifying the number of unique IP addresses within a given time-range )
  ===============
  
  This service is exposed as a post service via  **/log/ip/unique** api context
  
  The request consists of following optional fields. Even the empty payload body would also work with the default values.
  ```
    {
      "startTime": 10, 
      "endTime": 10000000000000 #(Optional. Default is current time in Unix timestamp)
    }
    
    * startTime - Optional field. The default value is 0.
    * endTime - Optional field. The default vale is the current time in Unix timestamp.
  ```
  The response for the above request would be in the following format.
  ```
    {
        "count": 9
    }
  ```
   Scenario - 2 ( The top-n visited URLs )
   ==============
  
   The request checks for the URLs within a given time range. The URL's with the return status 4xx and 5xx are ignored. 
   This service is exposed via the api context **/log/url/unique.**
  
   The request consists of following optional fields. Even the empty payload body would also work with the default values.

  ```
    {
      "startTime": 10,#(Optional. Default is 0)
      "endTime": 10000000000000,
      "count": 4
    }
    
    
    * startTime - Optional field. The default value is 0.
    * endTime - Optional field. The default vale is the current time in Unix timestamp.
    * count - Optional field. The default vale is 3.
  ```
   The response for the above request would be in the following format.
    ```
    {
        "urls": [
            {
                "count": 3,
                "url": "/docs/manage-websites/"
            },
            {
                "count": 2,
                "url": "/intranet-analytics/"
            },
            {
                "count": 2,
                "url": "/moved-permanently"
            },
            {
                "count": 2,
                "url": "/doc"
            }
        ]
    }
    ```
   Scenario - 3
   ==========

       The top-n most active IP addresses. This service can be accessed via the api context **/log/ip/active**.
       
       The request consists of following optional fields. Even the empty payload body would also work with the default values.
```
       {
         "startTime": 10,
         "endTime": 10000000000000,
          "count": 4
       }
       
        * startTime - Optional field. The default value is 0.
        * endTime - Optional field. The default vale is the current time in Unix timestamp.
         * count - Optional field. The default vale is 3.
    ```   
    
    The response for the above request would be in the following format.
    ```
       {
           "ips": [
               {
                   "ip": "168.41.191.40",
                   "count": 4
               },
               {
                   "ip": "50.112.00.11",
                   "count": 3
               },
               {
                   "ip": "177.71.128.21",
                   "count": 3
               }
           ]
       }
```

The code segment consists of unit tests as well.


