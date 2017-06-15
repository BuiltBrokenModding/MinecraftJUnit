# MinecraftJUnit
Lib designed to support JUnit testing in a minecraft forge workspace

#License
The code and its usage is licensed under the Built Broken Public License
https://github.com/VoltzEngine-Project/Engine/blob/development/license.md


## Add to dev workspace


```groovy
repositories {
    jcenter()
    maven {
        name="bbm"
        url="http://api.dmodoomsirius.me/"
    }
}

dependencies 
{
    compile 'junit:junit:4.11'
    compile "com.builtbroken.codinglib:CodingLib:0.0.1b20:deobf"
    compile "com.builtbroken.minecraftjunit:MinecraftJUnit:1.7.10-0.+:deobf"
}
```
