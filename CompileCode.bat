javac *.java
path C:\Program Files\Java\jdk-16\bin;%path%
jar cvfm Solar.jar manifest.txt *.class
pause
java mainBody
pause