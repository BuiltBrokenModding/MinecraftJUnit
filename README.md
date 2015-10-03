# MinecraftJUnit
Lib designed to support JUnit testing in a minecraft forge workspace

## Add to dev workspace


```
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
