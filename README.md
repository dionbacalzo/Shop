
## Getting Started

### Dependencies

Java 8

Eclipse Oxygen
 * [download](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen2)
 * Recommended Java EE IDE due to gradle integration
 * After installing/setting up Tomcat 9 and adding it on the Servers Tab view, double click on the Server. Under Server Location choose *Use Tomcat installation (takes control of Tomcat installation)* 

Tomcat 9
 * [download](https://tomcat.apache.org/download-90.cgi)
	
Mongodb 
 * [download](https://www.mongodb.com/download-center?jmp=nav#compass)
 * Follow the steps in setting up the MongoDB Server at *Run MongoDB Community Edition section* of this page: [https://docs.mongodb.com/tutorials/install-mongodb-on-windows/](https://docs.mongodb.com/tutorials/install-mongodb-on-windows/)	
 * Create a database named **shop** and a collection named **ItemDomainObject**

## How to Run

After installing the dependencies stated above run the following commands

```
	git clone https://github.com/dionbacalzo/Shop //download the repository
	gradlew.bat build // or ./gradlew if using Unix based system
	gradlew.bat eclipse // to setup eclipse setting and be ready for import
	
```


### Sample Curl scripts

Using a bash terminal the following are sample scripts used to test app

```
curl -i -X POST -H "Content-Type:application/json" http://localhost:7080/shop/viewList

curl -i -X POST -H "Content-Type:application/json" -d "{  \"manufacturer\" : \"dell\" }" http://localhost:7080/shop/manufacturer

curl -i -X POST -H "Content-Type:application/json" http://localhost:7080/shop/viewPagedList

curl -i -X POST -H "Content-Type:application/json" -d "[{ \"title\":\"4750Z\", \"manufacturer\" : \"acer\", \"type\":\"laptop\", \"price\":\"1100\", \"releaseDate\":\"2017-01-01 00:00:00.000\" }, { \"title\":\"NITRO 5 Spin\", \"manufacturer\" : \"acer\", \"type\":\"laptop\", \"price\":\"1300\", \"releaseDate\":\"2017-01-01 00:00:00.000\" }]" http://localhost:7080/shop/addItems
```