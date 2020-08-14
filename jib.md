
# Using jib to containerise Java Apps

[Jib](https://github.com/GoogleContainerTools/jib) is an open source Java tool maintained by Google to simplify the creation of Docker containers for your Java app without the need to create `dockerfiles` (in fact no need to have Java deamon installed to create or publish it). 

You have the option of either using a Maven or Gradle plugin.

Traditional Dockerfile approach;
![](https://cloud.google.com/java/images/docker_build_flow.png)

Compared to Jib;
![](https://cloud.google.com/java/images/jib_build_flow.png)

## Prereq
To run this demo it assumes you have;
* clone the repo in a place you can run Maven commands
* have authenticated to you container registry  

If you have not already set up for enviroment for the correct GCP project or GCR do the following;

```
gcloud auth list
gcloud config list project
gcloud config set project <PROJECT_ID>
gcloud services enable containerregistry.googleapis.com

gcloud auth configure-docker

```

## Demo

[optional] What does the app look like;
```java
./mvnw -DskipTests spring-boot:run
```
Now browse to [http://localhost:8080](http://localhost:8080), you should see a great ☕ based app.

So you know the app compiles and works as wanted so you now want to containerise the app and put it in the container registry;

```bash
./mvnw com.google.cloud.tools:jib-maven-plugin:1.8.0:build \
    -Dimage=gcr.io/$PROJECT_ID/demo-java-app-jib
```

Output
```bash
[INFO] Built and pushed image as gcr.io/$PROJECT_ID/demo-java-app-jib
...
[INFO] BUILD SUCCESS
```
Great, you should see your image in the container registry - in the Google console browse to `Container Registry` page and have a look. The build is done due to the Jib dependancy being added into `pom.xml`;

```xml
<!-- Jib Plugin config -->
		 	<plugin>
               <groupId>com.google.cloud.tools</groupId>
               <artifactId>jib-maven-plugin</artifactId>
                <version>2.4.0</version>
           </plugin> 
```
 
You can now deploy your container in which ever way you prefer (K8s, Cloud Run etc.)

 ### Extended

 You can help shorten the command by configuring the plugin in the `pom.xml`;

 ```xml
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
mvn compile jib:build
```

### Customisation

By default Jib will assume reasonable config for the container image (e.g FROM, ENTRYPOINT). But dont worry you can change these to suite your needs.

Lets say you don't want to use port 8080 and need to use 8082. 


```xml
<configuration>
    ...
    <container>
        <ports>
            <port>8082</port>
        </ports>
    </container>
</configuration>
```

Or what about using a prefered base image - by default you will use Googles distroless image (https://github.com/GoogleContainerTools/distroless/tree/master/java)

```xml
<configuration>
    ...
    <from>                           
        <image>openjdk:alpine</image>
    </from>
    ...
</configuration>
```

How about inserting env variables? 
```xml
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

### Cloud Build

Lets be honest most devs should be pushing code to a git repo and letting automation take care of building, test etc. Lets set this up in Cloud Build;

```bash
gcloud builds submit --config deploy/jib/ci/cloud-build-jib.yaml
```
Browse to Cloud Build.