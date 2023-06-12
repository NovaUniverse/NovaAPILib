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
	<version>1.0.0-SNAPSHOT</version>
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
