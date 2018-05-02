# library
个人的library
<br/>使用方式：

在app的build.gradle中，写上依赖
```
dependencies {
        implementation 'com.github.linqinen708:library:1.0.4'
}

```
再在项目的build.gralde中使用jitpack的库
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
}
```

