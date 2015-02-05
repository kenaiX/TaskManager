# TaskManager

----

恕我脑残，之前移除了对桌面应用的支持。重新加回桌面应用的支持和html的支持！

初步规则如下：

1、优先在桌面应用的模式下进行调试，效率大幅提升（cd desktop && gradle run）；

2、所有的代码放于core目录下；

3、用core下的policy目录模拟多任务的真实场景，后期再迁回android/desktop/html目录；


running or debugging：

Desktop: Run -> Edit Configurations..., click the plus (+) button and select Application. Set the Nameto Desktop.Set the field Use classpath of module to desktop, then click on the button of the Main class field and select the DesktopLauncher class. Set the Working directory to your android/assets/ (or your_project_path/core/assets/) folder! Click Apply and then OK. You have now created a run configuration for your desktop project. You can now select the configuration and run it.

Android: A configuration for the Android project should be automatically created on project import. As such, you only have to select the configuration and run it!

iOS: Run -> Edit Configurations..., click the plus (+) button and select Gradle. Set the Name to iOS, set the Gradle Project to ios, set the Tasks to launchIPhoneSimulator (alternatives are launchIPadSimulator and launchIOSDevicefor provisioned devices). Click Apply and then OK. You have now created a run configuration for your iOS project. You can now select the configuration and run it. The first run will take a bit longer as RoboVM has to compile the entire JDK for iOS. Subsequent runs will compile considerably faster!

HTML: View -> Tool Window -> Terminal, in the terminal, make sure you are in the root folder of your project. Then execute gradlew.bat html:superDev (Windows) or ./gradlew html:superDev (Linux, Mac OS X). This will take a while, as your Java code is compiled to Javascript. Once you see the message The code server is ready, fire up your browser and go to http://localhost:8080/html. This is your app running in the browser! When you change any of your Java code or assets, just click the SuperDev refresh button while you are on the site and the server will recompile your code and reload the page! To kill the process, simply press CTRL + C in the terminal window.


