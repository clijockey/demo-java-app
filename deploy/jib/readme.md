
# Using jib to containerise Java Apps

Jib is an open source Java tool maintained by Google to simplify the creation of Docker containers for your Java app without the need to create `dockerfiles` (in fact no need to have Java deamon installed to create or publish it). 

You have the option of either using a Maven or Gradle plugin.

Traditional Dockerfile approach;
[](https://cloud.google.com/java/images/docker_build_flow.png)

Compared to Jib;
[](https://cloud.google.com/java/images/jib_build_flow.png)

## Prereq
To run this demo it assumes you have;
    * cloned the repo in a place you can run Maven commands
    * have authenticated to you container registry  

If you have not already done the above steps;

```
gcloud auth list
gcloud config list project
gcloud config set project <PROJECT_ID>
gcloud services enable containerregistry.googleapis.com

```

## Demo

[optional] What does the app look like;
```
./mvnw -DskipTests spring-boot:run
```

```
./mvnw com.google.cloud.tools:jib-maven-plugin:1.8.0:build \
    -Dimage=gcr.io/$GOOGLE_CLOUD_PROJECT/demo-java-app-jib
```
```
    mvn compile com.google.cloud.tools:jib-maven-plugin:2.0.0:build \
    -Dimage=gcr.io/PROJECT/IMAGE
```

Output
```
[INFO] Built and pushed image as gcr.io/PROJECT_ID/demo-java-app-jib
...
[INFO] BUILD SUCCESS
```
 Cloud Console (https://console.cloud.google.com/), click on the navigation menu button, and select Container Registry on the left panel. You should now see your image has been pushed.

 You can now deploy your container in which ever way you prefer (K8s, Cloud Run etc.)

 ### Extended

 You can help shorten the command by configuring the plugin in the `pom.xml`;

 ```
 <project>
    ...
    <build>
        <plugins>
            ...
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>0.9.10</version>
                <configuration>
                    <to>
                        <image>${image.path}</image>
                    </to>
                </configuration>
            </plugin>
            ...
        </plugins>
    </build>
    ...
</project>
```

Then allows;
```
mvn compile app:build
```

### Customisation

By default Jib will assume reasonable config for the container image (e.g FROM, ENTRYPOINT). But dont worry you can change these to suite your needs.

Lets say you don't want to use port 8080 and need to use 8082. 


```
configuration>
    ...
    <container>
        <ports>
            <port>8082</port>
        </ports>
    </container>
</configuration>
```

Or what about using a prefered base image - by default you will use Googles distroless image (https://github.com/GoogleContainerTools/distroless/tree/master/java)

```
<configuration>
    ...
    <from>                           
        <image>openjdk:alpine</image>
    </from>
    ...
</configuration>
```

How about inserting env variables? 
```
<plugin>
  <groupId>com.google.cloud.tools</groupId>
  <artifactId>jib-maven-plugin</artifactId>
  <version>2.0.0</version>
  <configuration>
    <to>
      <image>gcr.io/PROJECT/IMAGE</image>
    </to>
    <container>
      <environment>
        <ENV_VAR>VALUE</ENV_VAR>
      </environment>
    </container>
  </configuration>
</plugin>
```



More options are detailed here. (https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin). The GCP guide is https://cloud.google.com/java/getting-started/jib