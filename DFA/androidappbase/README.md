Top level gradle file:  
```
    allprojects {
        repositories {
            google()
            jcenter()
            maven { url "https://jcenter.bintray.com" }
            maven { url 'https://jitpack.io' }
            mavenCentral()
        }
    }
```
    
Add git submodule:  
```
    git submodule add https://gitlab.com/MyITSolver/androidappbase.git
``` 
settings.gradle:  
```
    include ':baseapp'
    project(":baseapp").projectDir = file("androidappbase/baseapp")
```
    optional:  
```
    include ':chat'
    project(":chat").projectDir = file("androidappbase/chat")
    include ':mediapicker'
    project(":mediapicker").projectDir = file("androidappbase/mediapicker")
    include ':imageeditor'
    project(":imageeditor").projectDir = file("androidappbase/ImageEditor")
    include ':basebranch'
    project(":basebranch").projectDir = file("androidappbase/baseapp-branch")
```
    
app level gradle:  
```
    implementation project(':baseapp')```
    optional:  
```
    implementation project(':mediapicker')
    implementation project(':chat')
    implementation project(':imageeditor')
    implementation project(':basebranch')
```