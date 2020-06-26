# pebble-scala

A wrapper library for using [Pebble Templates](https://pebbletemplates.io/) in Scala.

Adds first class scala support to Pebble.

## Features

* Resolver for Scala values
* Support Scala Lists, Set, Maps, Options, ...
* Loops support scala collections
* Scala Support for Tests (empty, map, iterable)
* iterate over scala collections
* ...


## Version
Scala Version is 2.13.x / 2.12.x.

## SBT

```
resolvers ++= Seq(Resolver.jcenterRepo)
libraryDependencies += "com.sfxcode.templating" %% "pebble-scala" % "0.8.2"
```

## Travis

[![Build Status](https://travis-ci.org/sfxcode/pebble-scala.svg?branch=master)](https://travis-ci.org/sfxcode/pebble-scala)

## Download

[ ![Download](https://api.bintray.com/packages/sfxcode/maven/pebble-scala/images/download.svg) ](https://bintray.com/sfxcode/maven/pebble-scala/_latestVersion)


## Usage

1. Write Template

```html
<html>
<head>
    <title>{{ info.name }}</title>
</head>
<body>
<ul>
{% for value in list %}
    <li>{{ value }}</li>
{% endfor %}
</ul>
</body>
</html>
```

2. Init Engine

Scala map with global engine parameter is optional ...

```scala
 val Engine: ScalaPebbleEngine = ScalaPebbleEngine(globalContext = Map("header" -> "pebble-scala"))
```

3. Evaluate

```
 val context   = Map("list" -> List("Resolver for Scala values", "iterate over scala collections"))
 val evaluated = Engine.evaluateToString("templates/template.peb", context)
```

4. Result

```html
<html>
<head>
    <title>pebble-scala</title>
</head>
<body>
<ul>
    <li>Resolver for Scala values</li>
    <li>iterate over scala collections</li>
</ul>
</body>
</html>
```

## Supporters

JetBrains is supporting this open source project with:

[![Intellij IDEA](http://www.jetbrains.com/img/logos/logo_intellij_idea.png)](http://www.jetbrains.com/idea/)
