
CLASSPATH=$CLASSPATH:.
CLASSPATH=$CLASSPATH:./commons-codec-1.9.jar
CLASSPATH=$CLASSPATH:./commons-io-2.4.jar
CLASSPATH=$CLASSPATH:./commons-lang3-3.4.jar
CLASSPATH=$CLASSPATH:./commons-logging-1.2.jar
CLASSPATH=$CLASSPATH:./ews-java-api-2.1-SNAPSHOT.jar
CLASSPATH=$CLASSPATH:./hamcrest-all-1.3.jar
CLASSPATH=$CLASSPATH:./hamcrest-core-1.3.jar
CLASSPATH=$CLASSPATH:./httpclient-4.4.1.jar
CLASSPATH=$CLASSPATH:./httpcore-4.4.1.jar
CLASSPATH=$CLASSPATH:./jcl-over-slf4j-1.7.12.jar
CLASSPATH=$CLASSPATH:./joda-time-2.8.jar
CLASSPATH=$CLASSPATH:./logback-classic-1.1.3.jar
CLASSPATH=$CLASSPATH:./logback-core-1.1.3.jar
CLASSPATH=$CLASSPATH:./objenesis-2.1.jar
CLASSPATH=$CLASSPATH:./slf4j-api-1.7.12.jar

javac -classpath $CLASSPATH DownLoadEmail.java

java -classpath $CLASSPATH DownLoadEmail



