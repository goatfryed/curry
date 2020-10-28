In curry, any method can be considered an either. To return the right value, use return.
To return an exceptional value, throw it.

given
```
use curry/system/println
use curry/Map

let main () {
    try {
        throwsValue()
    } catch {
        string:String -> println(string)
    }
}

let throwsValue:Map throws String () {
    throws "There is no map for you"
}
```
outputs
```
There is no map for you
```
