
## Getting Started

### Dependencies

Java 8
 * [download](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 * [setup java PATH system variable](https://www.java.com/en/download/help/path.xml)

Eclipse Oxygen
 * [download](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen2)
 * Recommended Java EE IDE due to gradle integration
 * After installing/setting up Tomcat 9 and adding it on the Servers Tab view, double click on the Server. Under Server Location choose *Use Tomcat installation (takes control of Tomcat installation)* 

Tomcat 9
 * [download](https://tomcat.apache.org/download-90.cgi)
	
Mongodb 
 * Download the [Community server](https://www.mongodb.com/download-center?jmp=nav#community)
 * Follow the steps in setting up the MongoDB Server at *Run MongoDB Community Edition section* of this page: [https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/)	
 * Create a database named **shop** and a collection named **ItemDomainObject**

## How to Run

After installing the dependencies stated above run the following commands

```
	git clone https://github.com/dionbacalzo/Shop //download the repository
	gradlew.bat build -x test // or ./gradlew build -x test if using Unix based system, don't run tests on first build
	gradlew.bat eclipse // to setup eclipse setting and be ready for import
	
```
access the homepage by going to: http://localhost:8080/shop/content

### Sample Curl scripts

Using a bash terminal the following are sample scripts used to test app

```
curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/shop/viewList

curl -i -X POST -H "Content-Type:application/json" -d "{  \"manufacturer\" : \"dell\" }" http://localhost:8080/shop/manufacturer

curl -i -X POST -H "Content-Type:application/json" http://localhost:8080/shop/viewPagedList

curl -i -X POST -H "Content-Type:application/json" -d "[{ \"title\":\"4750Z\", \"manufacturer\" : \"acer\", \"type\":\"laptop\", \"price\":\"1100\", \"releaseDate\":\"2017-01-01 00:00:00.000\" }, { \"title\":\"NITRO 5 Spin\", \"manufacturer\" : \"acer\", \"type\":\"laptop\", \"price\":\"1300\", \"releaseDate\":\"2017-01-01 00:00:00.000\" }]" http://localhost:8080/shop/addItems
```

### File Upload

Access the page by going to: http://localhost:8080/shop/upload

Javascript libraries used:
 - [Jquery](https://jquery.com/)
 - [Pagination.js](http://pagination.js.org/)
 - [Handlebars.js](https://handlebarsjs.com/)

text file format: 

**items separated by newline, content is separated with commas but ignore commas within double-quotes**
 - title,price,type,manufacturer,release date(yyyy-MM-dd HH:mm:ss.SSS)

e.g.

Shop.txt

Pixel 2,600,phone,Google,2018-03-22 00:00:00.000

Galaxy S9,719,phone,Samsung,2018-05-00 00:00:00.000 
 