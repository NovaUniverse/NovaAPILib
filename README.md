# NovaAPILib
A library to implement apis in your java projects

[API Documentation](https://novauniverse.net/apidocs/NovaAPILib/)

## Maven
To add this to your maven project add the following to your pom.xml file in the `<repositories>` section
```xml
<repository>
	<id>novauniverse</id>
	<url>https://nexus2.novauniverse.net/repository/novauniverse/</url>
</repository>
```
and the following to the `<dependencies>` section
```xml
<dependency>
	<groupId>net.novauniverse</groupId>
	<artifactId>NovaAPILib</artifactId>
	<version>1.2.1-SNAPSHOT</version>
	<scope>compile</scope>
</dependency>
```

If your project does not already include `org.json` and `commons-io` make sure to add them like shown below.

org.json
```xml
<dependency>
	<groupId>org.json</groupId>
	<artifactId>json</artifactId>
	<version>20230227</version>
	<scope>compile</scope>
</dependency>
```

commons-io
```xml
<dependency>
	<groupId>commons-io</groupId>
	<artifactId>commons-io</artifactId>
	<version>2.11.0</version>
	<scope>compile</scope>
</dependency>
```

### Meven relocation
you can also relocate the package by adding the folowing to the `<configuration>` section of your `maven-shade-plugin`
```xml
<relocations>
	<relocation>
		<pattern>net.novauniverse.apilib</pattern>
		<shadedPattern>com.yourname.apilib</shadedPattern>
	</relocation>
</relocations>
```
