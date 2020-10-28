
# Scopes
## Lexical Scopes
[see wikipedia](https://en.wikipedia.org/wiki/Scope_(computer_science)#Lexical_scope_vs._dynamic_scope)

## Typed scopes
Curry introduces the concept of statically typed scopes.
```
scope ServiceScope
    [throws {List of Types}]
    [uses {Parameters}]
```

### Using scopes

```
scope FooScope
function foo@FooScope () {}
```

### static checked exceptions by scopes
The Problem:

There are a lot of different approaches towards error workflow handling and type checking.
There are simply unchecked exceptions, checked exceptions, multiple return values
or Either/Try Wrappers.
We think, that static checking can significantly improve code quality, but should support rapid development flows, where
not every error needs immediate handling.
Often, type checked exceptions use the concept of runtime exceptions that allow escaping the exception mechanism, but there
is no in between and people tend to simply wrap a lot in runtime exceptions and then checking on the wrapped exception.

Approach:

The concept of scope allows the developer to document errors within a sub system,
and to ensure that unintended errors do not leave the system

A scope can declare to throw exceptions.
```
scope ServiceScope
    throws
        ValidationError
        LogicException
```

Exception throwing is only checked at scope boundaries.

```
scope FooScope throws FooException
scope BarScope throws BarException, FooException

function foo in FooScope () {}

function bar in BarScope () {}

function allowed in FooScope () {
    foo();
    throw FooException();
    try {
        bar();
    } catch {
        barException -> {}
    }
}

function invalid in FooScope() {
    bar();
    throw BarException();
}
```
 
### inversion of control by scope
Issue:
 
Inversion of control and dependency injection are fundamental principles in software design.
Scopes can be used to declare variables for inversion of control.
 
 ```
scope ServiceScope
    uses
        consume: # -> ?

function karenIt in ServiceScope () {
    consume("Karen")
}

function main () {
    public log = # -> println ("Hello, " + #)
    karenIt() // Hello, Karen   
    public log = # -> println ("Good bye, " + #)
    karenIt() // Good bye, Karen
}
```
 
 
 
 


