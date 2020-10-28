The stack can be traced. This can be used to created classic exceptions. Exception handling without stack trace
collection should be faster.

given
```
use curry/system/println
use curry/system/stacktrace

let main () {
    println(nested())
}

let nested () {
    return stacktrace()
}
```
outputs
```
in nested:9
in main:5
```
