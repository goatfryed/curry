given
```
scope FooScope
    throws
        LogicException

function foo@FooScope () {
    throws "string"
}
```
invalid
```
`foo` in scope `FooScope` throws undeclared type String
```
