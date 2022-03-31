
# USCIS I-751 and N-400 Eligiblibility count-down app 

- This is a personal Javafx app, which was created based on my own USCIS I-751 case.
- This app looks like a regular note app on windows 11.
- The library used in this project:
  - org.json: https://mvnrepository.com/artifact/org.json/json 
  - com.dustinredmond.fxtrayicon: https://mvnrepository.com/artifact/com.dustinredmond.fxtrayicon/FXTrayIcon
  - Kotlinx Coroutines JavaFX: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-javafx
## Authors

- [@QuanPham](https://github.com/phamminhquan2002/)


## Contributing

Contributions are always welcome!

See `contributing.md` for ways to get started.

Please adhere to this project's `code of conduct`.


## Deploy And Create *.exe and *.msi files
###### **+++++DEPLOY++++++++++++**
***Step 1:*** Create a class call App:

-- **Java language:**
```ruby
public class App {
    public static void main(String[] args) {
        HelloApplication.main(args);
    }
}
```
-- **Kotlin:**

•	Add ``` args: Array<String> ```to main argument so it looks like:

```ruby
    fun main(args: Array<String>) {
        Application.launch(HelloApplication::class.java)
    }
```

•	Create this class
```ruby
public class App {
    public static void main(String[] args) {
        HelloApplicationKt.main(args);
    }
}
```
***Step 2:*** Set up right artifact:

![1](https://user-images.githubusercontent.com/62160749/160962720-8fc6364f-a903-461f-b585-1cc01d15b95c.png)

- Open Project Structure *(Ctrl + Shift + Alt + S)* -> navigate to Artifact tab -> Add ->Jar -> From modules with dependencies
  - ***Main class:*** *App class* in step 1
  - ***Directory for META-INF/MANIFEST.MF:*** *java/Kotlin* folder to *resources* folder
  - Add copy of (+ button) -> File: add all files in ***\javafx-sdk-17.0.1\bin\\*** folder

![2](https://user-images.githubusercontent.com/62160749/160962730-1652f0b8-a407-4ded-bcde-abf1d856014d.png)

- Build -> Build Artifacts -> Build
- Your Jar file will be in out/artifacts/uscismonitor_jar!

###### **+++++CREATE EXECUTABLE FILE USING JPackage++++++++++++**
*******requirements*******:
1)	JMODS 17.0.1: https://gluonhq.com/products/javafx/
![image](https://user-images.githubusercontent.com/62160749/160964023-b785f8ef-10f2-4c34-aaf1-51a191df37d3.png)
2)	WiX Toolset v3.11.2 is available from https://wixtoolset.org/releases/
 -	Open Environment Variable --> path (users variables) –> add C:\Program Files (x86)\WiX Toolset v3.11\bin
 [image](https://user-images.githubusercontent.com/62160749/160964073-523dff66-0b61-4dab-80a8-b59002243142.png)
 -	Open terminal at this address out/artifacts/name_jar
Source: https://docs.oracle.com/en/java/javase/17/docs/specs/man/jpackage.html
**example command:**

```ruby
jpackage --type exe --input . --dest . --main-jar .\JavaFXHelloWorld.jar --main-class com.example.javafxhelloworld.App --module-path "C:\Program Files\Java\javafx-jmods-17.0.1" --add-modules javafx.controls,javafx.fxml --win-shortcut --win-menu 
```

The following defaults are also applied: 
- The default type of package generated is exe. 
- The desktop shortcut is created, and the application is added to the Start menu.

